package io.isometrik.ui.conversations.details.userconversation;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchConversationDetailsQuery;
import io.isometrik.chat.builder.conversation.cleanup.ClearConversationQuery;
import io.isometrik.chat.builder.conversation.cleanup.DeleteConversationLocallyQuery;
import io.isometrik.chat.builder.conversation.config.UpdateConversationSettingsQuery;
import io.isometrik.chat.builder.message.FetchMessagesQuery;
import io.isometrik.chat.builder.user.block.BlockUserQuery;
import io.isometrik.chat.callbacks.UserEventCallback;
import io.isometrik.chat.events.user.DeleteUserEvent;
import io.isometrik.chat.events.user.UpdateUserEvent;
import io.isometrik.chat.events.user.block.BlockUserEvent;
import io.isometrik.chat.events.user.block.UnblockUserEvent;
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil;
import io.isometrik.chat.response.conversation.utils.ConversationMember;
import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.ui.conversations.gallery.GalleryMediaItemsSettingsUtil;
import io.isometrik.ui.conversations.gallery.GalleryModel;
import io.isometrik.chat.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * The presenter to fetch details of a 1-1 conversation with option to block user, create new group
 * conversation with opponent, fetch media items in gallery, change
 * typing/read-delivery/notifications settings and clear messages.
 */
public class UserConversationDetailsPresenter implements UserConversationDetailsContract.Presenter {

  /**
   * Instantiates a new User conversation details presenter.
   *
   * @param userConversationDetailsView the user conversation details view
   * @param opponentId the opponent id
   */
  UserConversationDetailsPresenter(UserConversationDetailsContract.View userConversationDetailsView,
      String opponentId) {
    this.userConversationDetailsView = userConversationDetailsView;
    galleryMediaItemsSettingsUtil =
        new GalleryMediaItemsSettingsUtil(true, true, true, true, true, true, true, true, true);
    this.opponentId = opponentId;
  }

  private final UserConversationDetailsContract.View userConversationDetailsView;
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();
  private final GalleryMediaItemsSettingsUtil galleryMediaItemsSettingsUtil;
  private String opponentId;

  @Override
  public void deleteConversation(String conversationId) {

    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .deleteConversationLocally(new DeleteConversationLocallyQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .build(), (var1, var2) -> {

          if (var1 != null) {
            userConversationDetailsView.onConversationDeletedSuccessfully();
          } else {
            userConversationDetailsView.onError(var2.getErrorMessage());
          }
        });
  }

  @Override
  public void clearConversation(String conversationId) {
    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .clearConversation(new ClearConversationQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            userConversationDetailsView.onConversationClearedSuccessfully();
          } else {
            userConversationDetailsView.onError(var2.getErrorMessage());
          }
        });
  }

  @Override
  public void requestConversationDetails(String conversationId, boolean includeMembers) {
    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchConversationDetails(new FetchConversationDetailsQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setIncludeMembers(includeMembers)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            ConversationDetailsUtil conversationDetailsUtil = var1.getConversationDetails();

            String userId = conversationDetailsUtil.getUsersOwnDetails().getMemberId();

            ArrayList<ConversationMember> conversationMembers = conversationDetailsUtil.getConversationMembers();
            int size = conversationMembers.size();

            MembersWatchersModel userDetails = null;

            for (int i = 0; i < size; i++) {
              ConversationMember conversationMember = conversationMembers.get(i);

              if (!userId.equals(conversationMember.getUserId())) {
                userDetails = new MembersWatchersModel(conversationMember,
                    conversationDetailsUtil.getUsersOwnDetails().isAdmin(), false);
                opponentId = userDetails.getMemberId();
                break;
              }
            }
            userConversationDetailsView.onConversationDetailsFetchedSuccessfully(
                conversationDetailsUtil, conversationDetailsUtil.getUsersOwnDetails().isAdmin(), userDetails);
          } else {
            userConversationDetailsView.onFailedToFetchConversationDetails(var2.getErrorMessage());
          }
        });
  }

  @Override
  public void fetchGalleryItems(String conversationId, List<String> galleryItemsEnabled) {

    isometrik.getRemoteUseCases()
        .getMessageUseCases()
        .fetchMessages(new FetchMessagesQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setLimit(Constants.GALLERY_CONVERSATION_DETAILS_MEDIA_PAGE_SIZE)
            .setCustomTypes(galleryItemsEnabled)
            .setSort(Constants.SORT_ORDER_DSC)
            .build(), (var1, var2) -> {
          if (var1 != null) {

            ArrayList<Message> messages = var1.getMessages();

            if (messages.size() > 0) {
              ArrayList<GalleryModel> galleryModels = new ArrayList<>();
              int size = messages.size();

              for (int i = 0; i < size; i++) {
                galleryModels.add(new GalleryModel(messages.get(i)));
              }

              userConversationDetailsView.onGalleryItemsFetchedSuccessfully(galleryModels, size == Constants.GALLERY_CONVERSATION_DETAILS_MEDIA_PAGE_SIZE);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
              userConversationDetailsView.onGalleryItemsFetchedSuccessfully(new ArrayList<>(), false);
            } else {
              userConversationDetailsView.onFailedToFetchGalleryItems(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void updateConversationSettings(String conversationId, Boolean enableTypingMessage, Boolean enableDeliveryReadEvents, Boolean enableNotifications) {

    UpdateConversationSettingsQuery.Builder query =
        new UpdateConversationSettingsQuery.Builder().setConversationId(conversationId).setUserToken(userToken);

    if (enableTypingMessage != null) {
      query.setTypingEvents(enableTypingMessage);
    }
    if (enableDeliveryReadEvents != null) {
      query.setReadEvents(enableDeliveryReadEvents);
    }
    if (enableNotifications != null) {
      query.setPushNotifications(enableNotifications);
    }
    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .updateConversationSettings(query.build(), (var1, var2) -> {
          if (var1 != null) {
            userConversationDetailsView.onConversationSettingUpdatedSuccessfully(enableTypingMessage, enableDeliveryReadEvents, enableNotifications);
          } else {
            userConversationDetailsView.onError(var2.getErrorMessage());
          }
        });
  }

  @Override
  public GalleryMediaItemsSettingsUtil getGalleryMediaItemsSettingsUtil() {
    return galleryMediaItemsSettingsUtil;
  }

  @Override
  public void blockUser(String userId) {

    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .blockUser(
            new BlockUserQuery.Builder().setUserToken(userToken).setOpponentId(userId).build(), (var1, var2) -> {
              if (var1 != null) {
                userConversationDetailsView.onUserBlocked();
              } else {
                userConversationDetailsView.onError(var2.getErrorMessage());
              }
            });
  }

  @Override
  public void registerUserEventListener() {
    isometrik.getRealtimeEventsListenerManager().getUserListenerManager().addListener(userEventCallback);
  }

  @Override
  public void unregisterUserEventListener() {
    isometrik.getRealtimeEventsListenerManager().getUserListenerManager().removeListener(userEventCallback);
  }

  private final UserEventCallback userEventCallback = new UserEventCallback() {
    @Override
    public void userBlocked(@NotNull Isometrik isometrik, @NotNull BlockUserEvent blockUserEvent) {

      if (blockUserEvent.getInitiatorId().equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        if (blockUserEvent.getOpponentId().equals(opponentId)) {
          userConversationDetailsView.onUserBlocked();
        }
      } else if (blockUserEvent.getInitiatorId().equals(opponentId)) {
        userConversationDetailsView.onUserBlocked();
      }
    }

    @Override
    public void userUnblocked(@NotNull Isometrik isometrik, @NotNull UnblockUserEvent unblockUserEvent) {
      //TODO Nothing
    }

    @Override
    public void userUpdated(@NotNull Isometrik isometrik, @NotNull UpdateUserEvent updateUserEvent) {
      //TODO Nothing
    }

    @Override
    public void userDeleted(@NotNull Isometrik isometrik, @NotNull DeleteUserEvent deleteUserEvent) {
      //TODO Nothing
    }
  };
}
