package io.isometrik.ui.conversations.list;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchConversationsQuery;
import io.isometrik.chat.builder.conversation.FetchPublicOrOpenConversationsQuery;
import io.isometrik.chat.builder.membershipcontrol.JoinConversationQuery;
import io.isometrik.chat.builder.message.FetchUnreadMessagesCountQuery;
import io.isometrik.chat.callbacks.ConnectionEventCallback;
import io.isometrik.chat.callbacks.ConversationEventCallback;
import io.isometrik.chat.callbacks.MembershipControlEventCallback;
import io.isometrik.chat.callbacks.MessageEventCallback;
import io.isometrik.chat.callbacks.ReactionEventCallback;
import io.isometrik.chat.callbacks.UserEventCallback;
import io.isometrik.chat.enums.ConversationType;
import io.isometrik.chat.enums.CustomMessageTypes;
import io.isometrik.chat.events.connection.ConnectEvent;
import io.isometrik.chat.events.connection.ConnectionFailedEvent;
import io.isometrik.chat.events.connection.DisconnectEvent;
import io.isometrik.chat.events.conversation.CreateConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.ClearConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.DeleteConversationLocallyEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationDetailsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationImageEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationSettingsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationTitleEvent;
import io.isometrik.chat.events.membershipcontrol.AddAdminEvent;
import io.isometrik.chat.events.membershipcontrol.AddMembersEvent;
import io.isometrik.chat.events.membershipcontrol.JoinConversationEvent;
import io.isometrik.chat.events.membershipcontrol.LeaveConversationEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverJoinEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverLeaveEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveAdminEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveMembersEvent;
import io.isometrik.chat.events.message.SendMessageEvent;
import io.isometrik.chat.events.message.SendTypingMessageEvent;
import io.isometrik.chat.events.message.UpdateMessageDetailsEvent;
import io.isometrik.chat.events.message.cleanup.RemoveMessagesForEveryoneEvent;
import io.isometrik.chat.events.message.cleanup.RemoveMessagesForSelfEvent;
import io.isometrik.chat.events.message.delivery.MarkMessageAsDeliveredEvent;
import io.isometrik.chat.events.message.delivery.MarkMessageAsReadEvent;
import io.isometrik.chat.events.message.delivery.MarkMultipleMessagesAsReadEvent;
import io.isometrik.chat.events.message.delivery.UpdatedLastReadInConversationEvent;
import io.isometrik.chat.events.message.user.block.BlockUserInConversationEvent;
import io.isometrik.chat.events.message.user.block.UnblockUserInConversationEvent;
import io.isometrik.chat.events.reaction.AddReactionEvent;
import io.isometrik.chat.events.reaction.RemoveReactionEvent;
import io.isometrik.chat.events.user.DeleteUserEvent;
import io.isometrik.chat.events.user.UpdateUserEvent;
import io.isometrik.chat.events.user.block.BlockUserEvent;
import io.isometrik.chat.events.user.block.UnblockUserEvent;
import io.isometrik.chat.response.conversation.utils.Conversation;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.message.utils.fetchmessages.Config;
import io.isometrik.chat.response.message.utils.fetchmessages.Details;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.common.ChatConfig;
import io.isometrik.ui.messages.reaction.util.ReactionPlaceHolderIconHelper;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.TimeUtil;
import io.isometrik.chat.utils.UserSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * The presenter to fetch list of public/open and all conversations with paging, search and pull to
 * refresh
 * option, join public conversation and update ui on realtime message or action message in a
 * conversation.
 */
public class ConversationsListPresenter implements ConversationsListContract.Presenter {

  private ConversationsListContract.View conversationsListView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();
  private final String userId = IsometrikChatSdk.getInstance().getUserSession().getUserId();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private ConversationType conversationType;

  private final int PAGE_SIZE = Constants.CONVERSATIONS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  private final ConnectionEventCallback connectionEventCallback = new ConnectionEventCallback() {
    @Override
    public void disconnected(@NotNull Isometrik isometrik,
        @NotNull DisconnectEvent disconnectEvent) {
      conversationsRefreshRequired = true;
      if (conversationsListView != null) {
        conversationsListView.connectionStateChanged(false);
      }
    }

    @Override
    public void connected(@NotNull Isometrik isometrik, @NotNull ConnectEvent connectEvent) {
      if (conversationsRefreshRequired) {
        conversationsRefreshRequired = false;
        if (conversationsListView != null) {
          conversationsListView.fetchUnreadConversationsCount();
        }
        fetchConversations(0, false, conversationType, false, null);
      }
      if (conversationsListView != null) {
        conversationsListView.connectionStateChanged(true);
      }
    }

    @Override
    public void connectionFailed(@NotNull Isometrik isometrik,
        @NotNull IsometrikError isometrikError) {
      if (conversationsListView != null) {
        conversationsListView.connectionStateChanged(false);
      }
    }

    @Override
    public void failedToConnect(@NotNull Isometrik isometrik,
        @NotNull ConnectionFailedEvent connectionFailedEvent) {
      conversationsRefreshRequired = true;
      if (conversationsListView != null) {
        conversationsListView.connectionStateChanged(false);

        conversationsListView.failedToConnect(connectionFailedEvent.getReason());
      }
    }
  };
  private final ConversationEventCallback conversationEventCallback =
      new ConversationEventCallback() {
        @Override
        public void conversationCleared(@NotNull Isometrik isometrik,
            @NotNull ClearConversationEvent clearConversationEvent) {
          if (conversationsListView != null) {
            conversationsListView.onConversationCleared(clearConversationEvent.getConversationId(),
                IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_cleared_conversation),
                TimeUtil.formatTimestampToOnlyDate(clearConversationEvent.getSentAt()));
          }
        }

        @Override
        public void conversationDeletedLocally(@NotNull Isometrik isometrik,
            @NotNull DeleteConversationLocallyEvent deleteConversationLocallyEvent) {
          if (conversationsListView != null) {
            conversationsListView.removeConversation(
                deleteConversationLocallyEvent.getConversationId());
            conversationsListView.fetchUnreadConversationsCount();
          }
        }

        @Override
        public void conversationImageUpdated(@NotNull Isometrik isometrik,
            @NotNull UpdateConversationImageEvent updateConversationImageEvent) {
          if (conversationsListView != null) {
            conversationsListView.onConversationImageUpdated(
                updateConversationImageEvent.getConversationId(),
                updateConversationImageEvent.getConversationImageUrl());

            conversationsListView.updateLastMessageInConversation(
                updateConversationImageEvent.getConversationId(), IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_updated_conversation_image,
                        updateConversationImageEvent.getUserName()),
                updateConversationImageEvent.getUserProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(updateConversationImageEvent.getSentAt()), null,
                false, !updateConversationImageEvent.getUserId().equals(userId),
                updateConversationImageEvent.getUserName(), true);
          }
        }

        @Override
        public void conversationSettingsUpdated(@NotNull Isometrik isometrik,
            @NotNull UpdateConversationSettingsEvent updateConversationSettingsEvent) {
          if (conversationsListView != null) {
            Config config = updateConversationSettingsEvent.getConfig();

            String settingsUpdated = "";
            if (config.getTypingEvents() != null) {
              settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_settings_typing);
            }
            if (config.getReadEvents() != null) {
              settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_settings_read_delivery_events);
            }
            if (config.getPushNotifications() != null) {
              settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_settings_notifications);
            }

            if (config.getTypingEvents() != null || config.getReadEvents() != null) {
              conversationsListView.onConversationSettingsUpdated(
                  updateConversationSettingsEvent.getConversationId(), config.getTypingEvents(),
                  config.getReadEvents());
            }

            conversationsListView.updateLastMessageInConversation(
                updateConversationSettingsEvent.getConversationId(), IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_updated_settings,
                        updateConversationSettingsEvent.getUserName(),
                        settingsUpdated.substring(2)),
                updateConversationSettingsEvent.getUserProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(updateConversationSettingsEvent.getSentAt()),
                null, false, !updateConversationSettingsEvent.getUserId().equals(userId),
                updateConversationSettingsEvent.getUserName(), true);
          }
        }

        @Override
        public void conversationDetailsUpdated(@NotNull Isometrik isometrik,
            @NotNull UpdateConversationDetailsEvent updateConversationDetailsEvent) {
          if (conversationsListView != null) {
            Details details = updateConversationDetailsEvent.getDetails();

            String detailsUpdated = "";
            if (details.getCustomType() != null) {
              detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_details_custom_type);
            }
            if (details.getMetadata() != null && details.getMetadata().length() > 0) {
              detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_details_metadata);
            }
            if (details.getSearchableTags() != null) {
              detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_details_searchable_tags);
            }

            conversationsListView.updateLastMessageInConversation(
                updateConversationDetailsEvent.getConversationId(), IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_updated_conversation_details,
                        updateConversationDetailsEvent.getUserName(), detailsUpdated.substring(2)),
                updateConversationDetailsEvent.getUserProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(updateConversationDetailsEvent.getSentAt()),
                null, false, !updateConversationDetailsEvent.getUserId().equals(userId),
                updateConversationDetailsEvent.getUserName(), true);
          }
        }

        @Override
        public void conversationTitleUpdated(@NotNull Isometrik isometrik,
            @NotNull UpdateConversationTitleEvent updateConversationTitleEvent) {
          if (conversationsListView != null) {
            conversationsListView.onConversationTitleUpdated(
                updateConversationTitleEvent.getConversationId(),
                updateConversationTitleEvent.getConversationTitle());

            conversationsListView.updateLastMessageInConversation(
                updateConversationTitleEvent.getConversationId(), IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_updated_conversation_title,
                        updateConversationTitleEvent.getUserName(),
                        updateConversationTitleEvent.getConversationTitle()),
                updateConversationTitleEvent.getUserProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(updateConversationTitleEvent.getSentAt()), null,
                false, !updateConversationTitleEvent.getUserId().equals(userId),
                updateConversationTitleEvent.getUserName(), true);
          }
        }

        @Override
        public void conversationCreated(@NotNull Isometrik isometrik,
            @NotNull CreateConversationEvent createConversationEvent) {
          if (conversationsListView != null) {
            conversationsListView.onConversationCreated(
                new ConversationsModel(createConversationEvent),
                createConversationEvent.getUserId().equals(userId));
            conversationsListView.fetchUnreadConversationsCount();
          }
        }
      };

  @Override
  public void initialize(ConversationType conversationType) {
    this.conversationType = conversationType;
  }

  @Override
  public void attachView(ConversationsListContract.View conversationsListView) {
    this.conversationsListView = conversationsListView;
  }

  @Override
  public void detachView() {
    conversationsListView = null;
  }

  @Override
  public void fetchConversations(int skip, boolean onScroll, ConversationType conversationType,
      boolean isSearchRequest, String searchTag) {
    isLoading = true;

    if (skip == 0) {
      offset = 0;
      isLastPage = false;
    }

    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    if (conversationType == ConversationType.AllConversations) {

      FetchConversationsQuery.Builder fetchConversationsQuery =
          new FetchConversationsQuery.Builder().setUserToken(userToken)
              .setLimit(PAGE_SIZE)
              .setSkip(skip);
      if (isSearchRequest && searchTag != null) {
        fetchConversationsQuery.setSearchTag(searchTag);
      }

      isometrik.getRemoteUseCases()
          .getConversationUseCases()
          .fetchConversations(fetchConversationsQuery.build(), (var1, var2) -> {
            isLoading = false;
            if (var1 != null) {

              ArrayList<Conversation> conversations = var1.getConversations();

              if (conversations.size() > 0) {
                ArrayList<ConversationsModel> conversationsModels = new ArrayList<>();
                int size = conversations.size();

                for (int i = 0; i < size; i++) {
                  try {
                    if (ChatConfig.INSTANCE.getHideNotStartedConversationInChatList() && conversations.get(i) != null && conversations.get(i).getLastMessageDetails() != null && conversations.get(i).getLastMessageDetails().has("action") && conversations.get(i).getLastMessageDetails().getString("action").equals("conversationCreated")) {
                   // if conversation initiated but not started then don't show in UI
                    } else {
                      conversationsModels.add(new ConversationsModel(conversations.get(i), false, false));
                    }
                  } catch (Exception e) {
                    conversationsModels.add(new ConversationsModel(conversations.get(i), false, false));
                    e.printStackTrace();
                  }
                }
                if (size < PAGE_SIZE) {

                  isLastPage = true;
                }
                if (conversationsListView != null) {
                  conversationsListView.onConversationsFetchedSuccessfully(conversationsModels, onScroll);
                }
              } else {
                if (!onScroll) {
                  //No conversations found
                  if (conversationsListView != null) {
                    conversationsListView.onConversationsFetchedSuccessfully(new ArrayList<>(), false);
                  }
                } else {
                  isLastPage = true;
                }
              }
            } else {
              if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
                if (!onScroll) {
                  //No conversations found
                  if (conversationsListView != null) {
                    conversationsListView.onConversationsFetchedSuccessfully(new ArrayList<>(), false);
                  }
                }
              } else {
                if (conversationsListView != null) {
                  conversationsListView.onError(var2.getErrorMessage());
                }
              }
            }
          });
    } else {
      FetchPublicOrOpenConversationsQuery.Builder fetchPublicConversationsQuery =
          new FetchPublicOrOpenConversationsQuery.Builder().setUserToken(userToken)
              .setLimit(PAGE_SIZE)
              .setConversationType(conversationType.getValue())
              .setSkip(skip);
      if (isSearchRequest && searchTag != null) {
        fetchPublicConversationsQuery.setSearchTag(searchTag);
      }

      isometrik.getRemoteUseCases()
          .getConversationUseCases()
          .fetchPublicOrOpenConversations(fetchPublicConversationsQuery.build(), (var1, var2) -> {
            isLoading = false;
            if (var1 != null) {

              ArrayList<Conversation> conversations = var1.getConversations();

              if (conversations.size() > 0) {
                ArrayList<ConversationsModel> conversationsModels = new ArrayList<>();
                int size = conversations.size();

                for (int i = 0; i < size; i++) {
                  conversationsModels.add(new ConversationsModel(conversations.get(i),
                      conversationType == ConversationType.PublicConversation,
                      conversationType == ConversationType.OpenConversation));
                }
                if (size < PAGE_SIZE) {

                  isLastPage = true;
                }
                if (conversationsListView != null) {
                  conversationsListView.onConversationsFetchedSuccessfully(conversationsModels,
                      onScroll);
                }
              } else {
                if (!onScroll) {
                  //No conversations found
                  if (conversationsListView != null) {
                    conversationsListView.onConversationsFetchedSuccessfully(new ArrayList<>(),
                        false);
                  }
                } else {
                  isLastPage = true;
                }
              }
            } else {
              if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
                if (!onScroll) {
                  //No conversations found
                  if (conversationsListView != null) {
                    conversationsListView.onConversationsFetchedSuccessfully(new ArrayList<>(),
                        false);
                  }
                }
              } else {
                if (conversationsListView != null) {
                  conversationsListView.onError(var2.getErrorMessage());
                }
              }
            }
          });
    }
  }

  @Override
  public void fetchConversationsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount, ConversationType conversationType) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchConversations(offset * PAGE_SIZE, true, conversationType, isSearchRequest, searchTag);
      }
    }
  }

  private final UserEventCallback userEventCallback = new UserEventCallback() {
    @Override
    public void userBlocked(@NotNull Isometrik isometrik, @NotNull BlockUserEvent blockUserEvent) {
      //TODO Nothing
    }

    @Override
    public void userUnblocked(@NotNull Isometrik isometrik,
        @NotNull UnblockUserEvent unblockUserEvent) {
      //TODO Nothing
    }

    @Override
    public void userUpdated(@NotNull Isometrik isometrik,
        @NotNull UpdateUserEvent updateUserEvent) {
      if (updateUserEvent.getUserId()
          .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

        UserSession userSession = IsometrikChatSdk.getInstance().getUserSession();

        if (updateUserEvent.getUserName() != null) {
          userSession.setUserName(updateUserEvent.getUserName());
        }
        if (updateUserEvent.getUserIdentifier() != null) {
          userSession.setUserIdentifier(updateUserEvent.getUserIdentifier());
        }
        if (updateUserEvent.getMetaData() != null) {
          userSession.setUserMetadata(updateUserEvent.getMetaData());
        }
        if (updateUserEvent.getNotification() != null) {
          userSession.setUserNotification(updateUserEvent.getNotification());
        }

        if (updateUserEvent.getUserProfileImageUrl() != null) {

          if (conversationsListView != null) {

            conversationsListView.onUserProfileImageUpdated(
                updateUserEvent.getUserProfileImageUrl());
          }
          userSession.setUserProfilePic(updateUserEvent.getUserProfileImageUrl());
        }
      }
    }

    @Override
    public void userDeleted(@NotNull Isometrik isometrik,
        @NotNull DeleteUserEvent deleteUserEvent) {
      if (conversationsListView != null) {

        conversationsListView.onUserDeleted();
      }
    }
  };
  private boolean conversationsRefreshRequired;

  private final MembershipControlEventCallback membershipControlEventCallback =
      new MembershipControlEventCallback() {
        @Override
        public void observerJoined(@NotNull Isometrik isometrik,
            @NotNull ObserverJoinEvent observerJoinEvent) {
          if (conversationsListView != null) {
            conversationsListView.updateLastMessageInConversation(
                observerJoinEvent.getConversationId(), IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_member_observer_joined,
                        observerJoinEvent.getUserName()),
                observerJoinEvent.getUserProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(observerJoinEvent.getSentAt()), null, false,
                !observerJoinEvent.getUserId().equals(userId), observerJoinEvent.getUserName(),
                !observerJoinEvent.getUserId().equals(userId));
          }
        }

        @Override
        public void observerLeft(@NotNull Isometrik isometrik,
            @NotNull ObserverLeaveEvent observerLeaveEvent) {
          if (conversationsListView != null) {
            conversationsListView.updateLastMessageInConversation(
                observerLeaveEvent.getConversationId(), IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_member_observer_left, observerLeaveEvent.getUserName()),
                observerLeaveEvent.getUserProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(observerLeaveEvent.getSentAt()), null, false,
                !observerLeaveEvent.getUserId().equals(userId), observerLeaveEvent.getUserName(),
                !observerLeaveEvent.getUserId().equals(userId));
          }
        }

        @Override
        public void adminAdded(@NotNull Isometrik isometrik, @NotNull AddAdminEvent addAdminEvent) {
          if (conversationsListView != null) {
            conversationsListView.updateLastMessageInConversation(addAdminEvent.getConversationId(),
                IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_made_admin, addAdminEvent.getMemberName(),
                        addAdminEvent.getInitiatorName()),
                addAdminEvent.getInitiatorProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(addAdminEvent.getSentAt()), null, false,
                !addAdminEvent.getInitiatorId().equals(userId), addAdminEvent.getInitiatorName(),
                true);
          }
        }

        @Override
        public void membersAdded(@NotNull Isometrik isometrik,
            @NotNull AddMembersEvent addMembersEvent) {
          if (conversationsListView != null) {
            StringBuilder membersAdded = new StringBuilder();
            List<AddMembersEvent.ConversationMember> members = addMembersEvent.getMembers();
            int size = members.size();
            for (int i = 0; i < size; i++) {
              membersAdded.append(", ").append(members.get(i).getMemberName());
            }

            conversationsListView.updateLastMessageInConversation(
                addMembersEvent.getConversationId(), IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_members_added, addMembersEvent.getUserName(),
                        membersAdded.substring(2)), addMembersEvent.getUserProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(addMembersEvent.getSentAt()), null, false,
                !addMembersEvent.getUserId().equals(userId), addMembersEvent.getUserName(), true);

            conversationsListView.updateConversationMembersCount(
                addMembersEvent.getConversationId(), addMembersEvent.getMembersCount());
          }
        }

        @Override
        public void conversationJoined(@NotNull Isometrik isometrik,
            @NotNull JoinConversationEvent joinConversationEvent) {
          if (conversationsListView != null) {

            if (joinConversationEvent.getUserId().equals(userId)) {
              //Need to fetch conversation details

              fetchConversationDetailsById(joinConversationEvent.getConversationId());
            } else {
              conversationsListView.updateLastMessageInConversation(
                  joinConversationEvent.getConversationId(), IsometrikChatSdk.getInstance()
                      .getContext()
                      .getString(R.string.ism_member_joined_public,
                          joinConversationEvent.getUserName()),
                  joinConversationEvent.getUserProfileImageUrl(),
                  TimeUtil.formatTimestampToOnlyDate(joinConversationEvent.getSentAt()), null,
                  false, !joinConversationEvent.getUserId().equals(userId),
                  joinConversationEvent.getUserName(), true);

              conversationsListView.updateConversationMembersCount(
                  joinConversationEvent.getConversationId(),
                  joinConversationEvent.getMembersCount());
            }
          }
        }

        @Override
        public void conversationLeft(@NotNull Isometrik isometrik,
            @NotNull LeaveConversationEvent leaveConversationEvent) {
          if (conversationsListView != null && leaveConversationEvent.getConversationId() != null) {
            if (leaveConversationEvent.getUserId().equals(userId)) {
              conversationsListView.removeConversation(leaveConversationEvent.getConversationId());
              conversationsListView.fetchUnreadConversationsCount();
            } else {

              conversationsListView.updateLastMessageInConversation(
                  leaveConversationEvent.getConversationId(), IsometrikChatSdk.getInstance()
                      .getContext()
                      .getString(R.string.ism_member_left, leaveConversationEvent.getUserName()),
                  leaveConversationEvent.getUserProfileImageUrl(),
                  TimeUtil.formatTimestampToOnlyDate(leaveConversationEvent.getSentAt()), null,
                  false, !leaveConversationEvent.getUserId().equals(userId),
                  leaveConversationEvent.getUserName(),
                  !leaveConversationEvent.getUserId().equals(userId));

              conversationsListView.updateConversationMembersCount(
                  leaveConversationEvent.getConversationId(),
                  leaveConversationEvent.getMembersCount());
            }
          }
        }

        @Override
        public void adminRemoved(@NotNull Isometrik isometrik,
            @NotNull RemoveAdminEvent removeAdminEvent) {
          if (conversationsListView != null) {
            conversationsListView.updateLastMessageInConversation(
                removeAdminEvent.getConversationId(), IsometrikChatSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_removed_admin, removeAdminEvent.getMemberName(),
                        removeAdminEvent.getInitiatorName()),
                removeAdminEvent.getInitiatorProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(removeAdminEvent.getSentAt()), null, false,
                !removeAdminEvent.getInitiatorId().equals(userId),
                removeAdminEvent.getInitiatorName(), true);
          }
        }

        @Override
        public void membersRemoved(@NotNull Isometrik isometrik,
            @NotNull RemoveMembersEvent removeMembersEvent) {
          if (conversationsListView != null) {
            boolean loggedInUserRemoved = false;
            List<RemoveMembersEvent.ConversationMember> members = removeMembersEvent.getMembers();
            int size = members.size();
            StringBuilder membersRemoved = new StringBuilder();
            for (int i = 0; i < size; i++) {

              membersRemoved.append(", ").append(members.get(i).getMemberName());
              if (members.get(i).getMemberId().equals(userId)) {
                loggedInUserRemoved = true;
                break;
              }
            }

            if (loggedInUserRemoved) {
              conversationsListView.removeConversation(removeMembersEvent.getConversationId());
              conversationsListView.fetchUnreadConversationsCount();
            } else {

              conversationsListView.updateLastMessageInConversation(
                  removeMembersEvent.getConversationId(), IsometrikChatSdk.getInstance()
                      .getContext()
                      .getString(R.string.ism_members_removed, removeMembersEvent.getUserName(),
                          membersRemoved.substring(2)), removeMembersEvent.getUserProfileImageUrl(),
                  TimeUtil.formatTimestampToOnlyDate(removeMembersEvent.getSentAt()), null, false,
                  !removeMembersEvent.getUserId().equals(userId), removeMembersEvent.getUserName(),
                  true);

              conversationsListView.updateConversationMembersCount(
                  removeMembersEvent.getConversationId(), removeMembersEvent.getMembersCount());
            }
          }
        }
      };

  private final MessageEventCallback messageEventCallback = new MessageEventCallback() {

    @Override
    public void messageDetailsUpdated(@NotNull Isometrik isometrik,
        @NotNull UpdateMessageDetailsEvent updateMessageDetailsEvent) {
      Details details = updateMessageDetailsEvent.getDetails();

      String detailsUpdated = "";
      if (details.getCustomType() != null) {
        detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_details_custom_type);
      }
      if (details.getMetadata() != null && details.getMetadata().length() > 0) {
        detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_details_metadata);
      }
      if (details.getSearchableTags() != null) {
        detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_details_searchable_tags);
      }
      if (details.getBody() != null) {
        detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_details_body);
      }
      conversationsListView.updateLastMessageInConversation(
          updateMessageDetailsEvent.getConversationId(), IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_updated_message_details,
                  updateMessageDetailsEvent.getUserName(), detailsUpdated.substring(2)),
          updateMessageDetailsEvent.getUserProfileImageUrl(),
          TimeUtil.formatTimestampToOnlyDate(updateMessageDetailsEvent.getSentAt()), null, false,
          !updateMessageDetailsEvent.getUserId().equals(userId),
          updateMessageDetailsEvent.getUserName(), true);
    }

    @Override
    public void updatedLastReadInConversationEvent(@NotNull Isometrik isometrik,
        @NotNull UpdatedLastReadInConversationEvent updatedLastReadInConversationEvent) {

      if (updatedLastReadInConversationEvent.getUserId().equals(userId)) {
        if (conversationsListView != null) {
          conversationsListView.fetchUnreadConversationsCount();
        }
        fetchUnreadMessagesInConversationCount(
            updatedLastReadInConversationEvent.getConversationId());
      }
    }
    @Override
    public void messagesRemovedForEveryone(@NotNull Isometrik isometrik,
        @NotNull RemoveMessagesForEveryoneEvent removeMessagesForEveryoneEvent) {
      if (conversationsListView != null) {
        conversationsListView.updateLastMessageInConversation(
            removeMessagesForEveryoneEvent.getConversationId(), IsometrikChatSdk.getInstance()
                .getContext()
                .getString(R.string.ism_message_deleted_for_all,
                    removeMessagesForEveryoneEvent.getUserName()),
            removeMessagesForEveryoneEvent.getUserProfileImageUrl(),
            TimeUtil.formatTimestampToOnlyDate(removeMessagesForEveryoneEvent.getSentAt()), null,
            false, false, removeMessagesForEveryoneEvent.getUserName(), true);

        if (!removeMessagesForEveryoneEvent.getUserId()
            .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
          conversationsListView.fetchUnreadConversationsCount();
          fetchUnreadMessagesInConversationCount(
              removeMessagesForEveryoneEvent.getConversationId());
        }
      }
    }

    @Override
    public void messagesRemovedForSelf(@NotNull Isometrik isometrik,
        @NotNull RemoveMessagesForSelfEvent removeMessagesForSelfEvent) {
      if (conversationsListView != null) {
        conversationsListView.updateLastMessageInConversation(
            removeMessagesForSelfEvent.getConversationId(), IsometrikChatSdk.getInstance()
                .getContext()
                .getString(R.string.ism_message_deleted_locally,
                    removeMessagesForSelfEvent.getUserName()),
            removeMessagesForSelfEvent.getUserProfileImageUrl(),
            TimeUtil.formatTimestampToOnlyDate(removeMessagesForSelfEvent.getSentAt()), null, false,
            !removeMessagesForSelfEvent.getUserId().equals(userId),
            removeMessagesForSelfEvent.getUserName(), true);
      }
    }

    @Override
    public void messageMarkedAsDelivered(@NotNull Isometrik isometrik,
        @NotNull MarkMessageAsDeliveredEvent markMessageAsDeliveredEvent) {
      //TODO Nothing
    }

    @Override
    public void messageMarkedAsRead(@NotNull Isometrik isometrik,
        @NotNull MarkMessageAsReadEvent markMessageAsReadEvent) {
      if (markMessageAsReadEvent.getUserId().equals(userId)) {
        if (conversationsListView != null) {
          conversationsListView.fetchUnreadConversationsCount();
        }
        fetchUnreadMessagesInConversationCount(markMessageAsReadEvent.getConversationId());
      }
    }

    @Override
    public void multipleMessagesMarkedAsRead(@NotNull Isometrik isometrik,
        @NotNull MarkMultipleMessagesAsReadEvent markMultipleMessagesAsReadEvent) {
      if (markMultipleMessagesAsReadEvent.getUserId().equals(userId)) {
        if (conversationsListView != null) {
          conversationsListView.fetchUnreadConversationsCount();
        }
        fetchUnreadMessagesInConversationCount(markMultipleMessagesAsReadEvent.getConversationId());
      }
    }

    @Override
    public void messageSent(@NotNull Isometrik isometrik,
        @NotNull SendMessageEvent sendMessageEvent) {
      if (conversationsListView != null && sendMessageEvent.getCustomType() != null) {
        Integer lastMessagePlaceHolderImage = null;
        String lastMessageText = null;

        switch (CustomMessageTypes.Companion.fromValue(sendMessageEvent.getCustomType())) {
          case Text: {

            if (sendMessageEvent.getParentMessageId() == null) {

              if (sendMessageEvent.getAction() != null) {
                //action not received for normal messages

                if ("forward".equals(sendMessageEvent.getAction())) {
                  lastMessagePlaceHolderImage = R.drawable.ism_ic_forward;
                }
              }
            } else {

              lastMessagePlaceHolderImage = R.drawable.ism_ic_quote;
            }

            lastMessageText = sendMessageEvent.getBody();
            break;
          }
          case Image: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_picture;
            lastMessageText =
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photo);
            break;
          }
          case Video: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_video;
            lastMessageText =
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_video);
            break;
          }
          case Audio: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_mic;
            lastMessageText =
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_audio_recording);
            break;
          }
          case File: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_file;
            lastMessageText =
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_file);
            break;
          }
          case Sticker: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_sticker;
            lastMessageText =
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_sticker);
            break;
          }
          case Gif: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_gif;
            lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_gif);
            break;
          }
          case Whiteboard: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_whiteboard;
            lastMessageText =
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_whiteboard);
            break;
          }
          case Location: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_location;
            lastMessageText =
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_location);
            break;
          }
          case Contact: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_contact;
            lastMessageText =
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_contact);
            break;
          }
          case Reply: {
            lastMessagePlaceHolderImage = R.drawable.ism_ic_quote;
            lastMessageText = sendMessageEvent.getBody();
            break;

          }
          default:{
            lastMessageText  = "Data update";
          }
        }
        if (lastMessageText != null) {

          conversationsListView.updateLastMessageInConversation(
              sendMessageEvent.getConversationId(), lastMessageText,
              sendMessageEvent.getSenderProfileImageUrl(),
              TimeUtil.formatTimestampToOnlyDate(sendMessageEvent.getSentAt()),
              lastMessagePlaceHolderImage, false,
              sendMessageEvent.getEvents().getUpdateUnreadCount() && !sendMessageEvent.getSenderId()
                  .equals(userId), sendMessageEvent.getSenderName(), true, sendMessageEvent.getCustomType());
        }
      }
    }

    @Override
    public void typingMessageSent(@NotNull Isometrik isometrik,
        @NotNull SendTypingMessageEvent sendTypingMessageEvent) {
      if (conversationsListView != null) {
        if (!sendTypingMessageEvent.getUserId()
            .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
          conversationsListView.onRemoteUserTypingEvent(sendTypingMessageEvent.getConversationId(),
              IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_typing, sendTypingMessageEvent.getUserName()));
        }
      }
    }

    @Override
    public void blockedUserInConversation(@NotNull Isometrik isometrik,
        @NotNull BlockUserInConversationEvent blockUserInConversationEvent) {
      if (conversationsListView != null) {
        String lastMessageText;
        String initiatorName = blockUserInConversationEvent.getInitiatorName();
        String opponentName = blockUserInConversationEvent.getOpponentName();
        String currentUserName = IsometrikChatSdk.getInstance().getUserSession().getUserName();

        if (opponentName.equals(currentUserName)) {
          lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                  R.string.ism_unblocked_user_text, initiatorName,"You");
        } else if (initiatorName.equals(currentUserName)) {
          lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                  R.string.ism_unblocked_user_text, "You",opponentName);
        } else {
          lastMessageText = IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_unblocked_user,initiatorName, opponentName);
        }

        conversationsListView.updateLastMessageInConversation(
                blockUserInConversationEvent.getConversationId(), lastMessageText,
                blockUserInConversationEvent.getInitiatorProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(blockUserInConversationEvent.getSentAt()), null,
                false, !blockUserInConversationEvent.getInitiatorId().equals(userId),
                blockUserInConversationEvent.getInitiatorName(), true);
        conversationsListView.onMessagingStatusChanged(
                blockUserInConversationEvent.getConversationId(),
                blockUserInConversationEvent.isMessagingDisabled());
      }
    }

    @Override
    public void unblockedUserInConversation(@NotNull Isometrik isometrik,
        @NotNull UnblockUserInConversationEvent unblockUserInConversationEvent) {
      if (conversationsListView != null) {
        String lastMessageText;
        String initiatorName = unblockUserInConversationEvent.getInitiatorName();
        String opponentName = unblockUserInConversationEvent.getOpponentName();
        String currentUserName = IsometrikChatSdk.getInstance().getUserSession().getUserName();

        if (opponentName.equals(currentUserName)) {
          lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                  R.string.ism_unblocked_user_text, initiatorName,"You");
        } else if (initiatorName.equals(currentUserName)) {
          lastMessageText = IsometrikChatSdk.getInstance().getContext().getString(
                  R.string.ism_unblocked_user_text, "You",opponentName);
        } else {
          lastMessageText = IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_unblocked_user,initiatorName, opponentName);
        }
        conversationsListView.updateLastMessageInConversation(
                unblockUserInConversationEvent.getConversationId(), lastMessageText,
                unblockUserInConversationEvent.getInitiatorProfileImageUrl(),
                TimeUtil.formatTimestampToOnlyDate(unblockUserInConversationEvent.getSentAt()), null,
                false, !unblockUserInConversationEvent.getInitiatorId().equals(userId),
                unblockUserInConversationEvent.getInitiatorName(), true);
        conversationsListView.onMessagingStatusChanged(
                unblockUserInConversationEvent.getConversationId(),
                unblockUserInConversationEvent.isMessagingDisabled());
      }
    }
  };

  private final ReactionEventCallback reactionEventCallback = new ReactionEventCallback() {
    @Override
    public void reactionAdded(@NotNull Isometrik isometrik,
        @NotNull AddReactionEvent addReactionEvent) {
      if (conversationsListView != null) {
        conversationsListView.updateLastMessageInConversation(addReactionEvent.getConversationId(),
            IsometrikChatSdk.getInstance()
                .getContext()
                .getString(R.string.ism_reaction_added, addReactionEvent.getUserName()),
            addReactionEvent.getUserProfileImageUrl(),
            TimeUtil.formatTimestampToOnlyDate(addReactionEvent.getSentAt()),
            ReactionPlaceHolderIconHelper.fetchLastMessagePlaceHolderIcon(
                addReactionEvent.getReactionType()), true,
            !addReactionEvent.getUserId().equals(userId), addReactionEvent.getUserName(), true);
      }
    }

    @Override
    public void reactionRemoved(@NotNull Isometrik isometrik,
        @NotNull RemoveReactionEvent removeReactionEvent) {
      if (conversationsListView != null) {
        conversationsListView.updateLastMessageInConversation(
            removeReactionEvent.getConversationId(), IsometrikChatSdk.getInstance()
                .getContext()
                .getString(R.string.ism_reaction_removed, removeReactionEvent.getUserName()),
            removeReactionEvent.getUserProfileImageUrl(),
            TimeUtil.formatTimestampToOnlyDate(removeReactionEvent.getSentAt()),
            ReactionPlaceHolderIconHelper.fetchLastMessagePlaceHolderIcon(
                removeReactionEvent.getReactionType()), true,
            !removeReactionEvent.getUserId().equals(userId), removeReactionEvent.getUserName(),
            true);
      }
    }
  };

  @Override
  public void registerConversationEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getConversationListenerManager()
        .addListener(conversationEventCallback);
  }

  @Override
  public void unregisterConversationEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getConversationListenerManager()
        .removeListener(conversationEventCallback);
  }

  public void registerConnectionEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getConnectionListenerManager()
        .addListener(connectionEventCallback);
  }

  @Override
  public void unregisterConnectionEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getConnectionListenerManager()
        .removeListener(connectionEventCallback);
  }

  @Override
  public void registerMembershipControlEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getMembershipControlListenerManager()
        .addListener(membershipControlEventCallback);
  }

  @Override
  public void unregisterMembershipControlEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getMembershipControlListenerManager()
        .removeListener(membershipControlEventCallback);
  }

  @Override
  public void registerMessageEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getMessageListenerManager()
        .addListener(messageEventCallback);
  }

  @Override
  public void unregisterMessageEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getMessageListenerManager()
        .removeListener(messageEventCallback);
  }

  @Override
  public void registerReactionEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getReactionListenerManager()
        .addListener(reactionEventCallback);
  }

  @Override
  public void unregisterReactionEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getReactionListenerManager()
        .removeListener(reactionEventCallback);
  }

  @Override
  public void registerUserEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getUserListenerManager()
        .addListener(userEventCallback);
  }

  @Override
  public void unregisterUserEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getUserListenerManager()
        .removeListener(userEventCallback);
  }

  @Override
  public int fetchConversationPositionInList(ArrayList<ConversationsModel> conversations,
      String conversationId, boolean fetchRemoteConversationIfNotFoundLocally) {
    int size = conversations.size();
    for (int i = 0; i < size; i++) {
      if (conversations.get(i).getConversationId().equals(conversationId)) {
        return i;
      }
    }
    if (fetchRemoteConversationIfNotFoundLocally) fetchConversationDetailsById(conversationId);
    return -1;
  }

  @Override
  public void joinConversation(ConversationsModel conversationsModel) {
    isometrik.getRemoteUseCases()
        .getMembershipControlUseCases()
        .joinConversation(new JoinConversationQuery.Builder().setConversationId(
            conversationsModel.getConversationId()).setUserToken(userToken).build(),
            (var1, var2) -> {
              if (var1 != null) {
                if (conversationsListView != null) {
                  conversationsListView.onConversationJoinedSuccessfully(conversationsModel);
                }
              } else {
                if (conversationsListView != null) {
                  conversationsListView.onError(var2.getErrorMessage());
                }
              }
            });
  }

  private void fetchConversationDetailsById(String conversationId) {
    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchConversations(new FetchConversationsQuery.Builder().setUserToken(userToken)
            .setConversationIds(Collections.singletonList(conversationId))
            .build(), (var1, var2) -> {
          isLoading = false;
          if (var1 != null) {

            ArrayList<Conversation> conversations = var1.getConversations();

            if (conversations.size() > 0) {
              if (conversationsListView != null) {

                conversationsListView.onConversationCreated(
                    new ConversationsModel(conversations.get(0),
                        conversationType == ConversationType.PublicConversation,
                        conversationType == ConversationType.OpenConversation),
                    conversations.get(0).getCreatedBy().equals(userId));
              }
            }
          }
        });
  }

  private void fetchUnreadMessagesInConversationCount(String conversationId) {
    isometrik.getRemoteUseCases()
        .getMessageUseCases()
        .fetchUnreadMessagesCount(
            new FetchUnreadMessagesCountQuery.Builder().setUserToken(userToken)
                .setConversationId(conversationId)
                .build(), (var1, var2) -> {

              if (var1 != null) {

                if (conversationsListView != null) {

                  conversationsListView.onUnreadMessagesCountInConversationsFetchedSuccessfully(
                      conversationId, var1.getMessagesCount());
                }
              } else {
                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

                  conversationsListView.onUnreadMessagesCountInConversationsFetchedSuccessfully(
                      conversationId, 0);
                }
              }
            });
  }
}
