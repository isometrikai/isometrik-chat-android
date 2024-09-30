package io.isometrik.ui.conversations.details.groupconversation;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchConversationDetailsQuery;
import io.isometrik.chat.builder.conversation.FetchConversationMembersQuery;
import io.isometrik.chat.builder.conversation.FetchConversationPresignedUrlQuery;
import io.isometrik.chat.builder.conversation.cleanup.ClearConversationQuery;
import io.isometrik.chat.builder.conversation.cleanup.DeleteConversationLocallyQuery;
import io.isometrik.chat.builder.conversation.config.UpdateConversationDetailsQuery;
import io.isometrik.chat.builder.conversation.config.UpdateConversationImageQuery;
import io.isometrik.chat.builder.conversation.config.UpdateConversationSettingsQuery;
import io.isometrik.chat.builder.conversation.config.UpdateConversationTitleQuery;
import io.isometrik.chat.builder.membershipcontrol.AddAdminQuery;
import io.isometrik.chat.builder.membershipcontrol.LeaveConversationQuery;
import io.isometrik.chat.builder.membershipcontrol.RemoveAdminQuery;
import io.isometrik.chat.builder.membershipcontrol.RemoveMembersQuery;
import io.isometrik.chat.builder.message.FetchMessagesQuery;
import io.isometrik.chat.builder.upload.CancelConversationImageUploadQuery;
import io.isometrik.chat.builder.upload.UploadConversationImageQuery;
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil;
import io.isometrik.chat.response.conversation.utils.ConversationMember;
import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.ui.conversations.gallery.GalleryMediaItemsSettingsUtil;
import io.isometrik.ui.conversations.gallery.GalleryModel;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.TimeUtil;
import io.isometrik.chat.utils.AttachmentMetadata;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The presenter to fetch details of a group conversation with option to add/remove members,
 * grant/revoke admin privileges, update title/image, fetch media items in gallery, change
 * typing/read-delivery/notifications settings, clear messages and leave conversation.
 */
public class ConversationDetailsPresenter implements ConversationDetailsContract.Presenter {

  /**
   * Instantiates a new Conversation details presenter.
   *
   * @param conversationDetailsView the conversation details view
   */
  ConversationDetailsPresenter(ConversationDetailsContract.View conversationDetailsView) {
    this.conversationDetailsView = conversationDetailsView;
    galleryMediaItemsSettingsUtil =
        new GalleryMediaItemsSettingsUtil(true, true, true, true, true, true, true, true, true);
  }

  private final ConversationDetailsContract.View conversationDetailsView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();
  private final GalleryMediaItemsSettingsUtil galleryMediaItemsSettingsUtil;

  private boolean isUserAnAdmin;
  private String userId;

  private String uploadRequestId;

  @Override
  public void deleteConversation(String conversationId) {

    isometrik.getRemoteUseCases().getConversationUseCases().deleteConversationLocally(new DeleteConversationLocallyQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .build(), (var1, var2) -> {
      if (var1 != null) {
        conversationDetailsView.onConversationDeletedSuccessfully();
      } else {
        conversationDetailsView.onError(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void clearConversation(String conversationId) {
    isometrik.getRemoteUseCases().getConversationUseCases().clearConversation(new ClearConversationQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .build(), (var1, var2) -> {
      if (var1 != null) {
        conversationDetailsView.onConversationClearedSuccessfully();
      } else {
        conversationDetailsView.onError(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void leaveConversation(String conversationId) {
    isometrik.getRemoteUseCases().getMembershipControlUseCases().leaveConversation(new LeaveConversationQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .build(), (var1, var2) -> {
      if (var1 != null) {
        conversationDetailsView.onConversationLeftSuccessfully();
      } else {
        conversationDetailsView.onError(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void requestConversationDetails(String conversationId, boolean includeMembers) {
    isometrik.getRemoteUseCases().getConversationUseCases().fetchConversationDetails(new FetchConversationDetailsQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .setIncludeMembers(includeMembers)
        .setMembersLimit(Constants.CONVERSATION_DETAILS_MEMBERS_DEFAULT_PAGE_SIZE)
        .build(), (var1, var2) -> {
      if (var1 != null) {
        ConversationDetailsUtil conversationDetailsUtil = var1.getConversationDetails();

        boolean isUserAnAdmin = conversationDetailsUtil.getUsersOwnDetails().isAdmin();
        String userId = conversationDetailsUtil.getUsersOwnDetails().getMemberId();

        this.isUserAnAdmin = isUserAnAdmin;
        this.userId = userId;

        ArrayList<MembersWatchersModel> conversationMembersModels = new ArrayList<>();
        ArrayList<ConversationMember> conversationMembers = conversationDetailsUtil.getConversationMembers();
        int size = conversationMembers.size();

        for (int i = 0; i < size; i++) {
          ConversationMember conversationMember = conversationMembers.get(i);
          conversationMembersModels.add(
              new MembersWatchersModel(conversationMember, isUserAnAdmin, userId.equals(conversationMember.getUserId())));
        }
        conversationDetailsView.onConversationDetailsFetchedSuccessfully(conversationDetailsUtil, conversationDetailsUtil.getCreatedByUserName()
                + ", "
                + TimeUtil.formatTimestampToBothDateAndTime(conversationDetailsUtil.getCreatedAt()), isUserAnAdmin,
            conversationMembersModels);
      } else {
        conversationDetailsView.onFailedToFetchConversationDetails(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void kickOutMembers(String conversationId, List<String> memberIds) {
    isometrik.getRemoteUseCases().getMembershipControlUseCases().removeMembers(new RemoveMembersQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .setMembers(memberIds)
        .build(), (var1, var2) -> {
      if (var1 != null) {
        conversationDetailsView.onMemberRemovedSuccessfully(memberIds.get(0));
      } else {
        conversationDetailsView.onError(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void makeAdmin(String conversationId, String memberId) {
    isometrik.getRemoteUseCases().getMembershipControlUseCases().addAdmin(new AddAdminQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .setMemberId(memberId)
        .build(), (var1, var2) -> {
      if (var1 != null) {
        conversationDetailsView.onMemberAdminPermissionsUpdated(memberId, true);
      } else {
        conversationDetailsView.onError(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void revokeAdminPermissions(String conversationId, String memberId) {
    isometrik.getRemoteUseCases().getMembershipControlUseCases().removeAdmin(new RemoveAdminQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .setMemberId(memberId)
        .build(), (var1, var2) -> {
      if (var1 != null) {
        conversationDetailsView.onMemberAdminPermissionsUpdated(memberId, false);
      } else {
        conversationDetailsView.onError(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void updateConversationTitle(String conversationId, String conversationTitle, String oldConversationTitle) {
    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .updateConversationTitle(new UpdateConversationTitleQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setConversationTitle(conversationTitle)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            if (!oldConversationTitle.equals(conversationTitle)) {

              IsometrikChatSdk.getInstance().getIsometrik()
                  .getExecutor()
                  .execute(() -> updateConversationSearchableTags(conversationId, conversationTitle,
                      oldConversationTitle));
            }
            conversationDetailsView.onConversationTitleUpdated(conversationTitle);
          } else {
            conversationDetailsView.onConversationTitleUpdateFailed(var2.getErrorMessage());
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
    isometrik.getRemoteUseCases().getConversationUseCases().updateConversationSettings(query.build(), (var1, var2) -> {
      if (var1 != null) {
        conversationDetailsView.onConversationSettingUpdatedSuccessfully(enableTypingMessage,
            enableDeliveryReadEvents, enableNotifications);
      } else {
        conversationDetailsView.onError(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void fetchGalleryItems(String conversationId, List<String> galleryItemsEnabled) {

    isometrik.getRemoteUseCases().getMessageUseCases().fetchMessages(new FetchMessagesQuery.Builder().setConversationId(conversationId)
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

          conversationDetailsView.onGalleryItemsFetchedSuccessfully(galleryModels, size == Constants.GALLERY_CONVERSATION_DETAILS_MEDIA_PAGE_SIZE);
        }
      } else {
        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
          conversationDetailsView.onGalleryItemsFetchedSuccessfully(new ArrayList<>(), false);
        } else {
          conversationDetailsView.onFailedToFetchGalleryItems(var2.getErrorMessage());
        }
      }
    });
  }

  @Override
  public GalleryMediaItemsSettingsUtil getGalleryMediaItemsSettingsUtil() {
    return galleryMediaItemsSettingsUtil;
  }

  @Override
  public void fetchConversationMembers(String conversationId) {
    isometrik.getRemoteUseCases().getConversationUseCases().fetchConversationMembers(new FetchConversationMembersQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .setLimit(Constants.CONVERSATION_DETAILS_MEMBERS_DEFAULT_PAGE_SIZE)
        .build(), (var1, var2) -> {
      if (var1 != null) {

        ArrayList<MembersWatchersModel> conversationMembersModels = new ArrayList<>();
        ArrayList<ConversationMember> conversationMembers = var1.getConversationMembers();
        int size = conversationMembers.size();

        for (int i = 0; i < size; i++) {
          ConversationMember conversationMember = conversationMembers.get(i);
          conversationMembersModels.add(
              new MembersWatchersModel(conversationMember, isUserAnAdmin, userId.equals(conversationMember.getUserId())));
        }
        conversationDetailsView.onConversationMembersFetched(conversationMembersModels, var1.getMembersCount(), isUserAnAdmin, userId);
      } else {

        conversationDetailsView.onError(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void requestImageUpload(String requestId, String conversationId, String mediaPath) {
    uploadRequestId = requestId;
    FetchConversationPresignedUrlQuery.Builder builder = new FetchConversationPresignedUrlQuery.Builder().setUserToken(
            IsometrikChatSdk.getInstance().getUserSession().getUserToken())
        .setConversationId(conversationId)
        .setNewConversation(false)
        .setMediaExtension(new AttachmentMetadata(mediaPath).getExtension());
    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchConversationPresignedUrl(builder.build(), (var1, var2) -> {

          if (var1 != null) {
            UploadConversationImageQuery uploadConversationImageQuery = new UploadConversationImageQuery.Builder().setPresignedUrl(var1.getPresignedUrl())
                .setMediaPath(mediaPath)
                .setRequestId(requestId)
                .setUploadProgressListener((requestId1, requestGroupId, bytesWritten, contentLength) -> {
                  conversationDetailsView.onUploadProgressUpdate((int) ((bytesWritten * 100) / contentLength));
                })
                .build();

            isometrik.getRemoteUseCases()
                .getUploadUseCases()
                .uploadConversationImage(uploadConversationImageQuery, (var11, var22) -> {
                  if (var11 != null) {
                    conversationDetailsView.onImageUploadResult(var1.getMediaUrl());
                  } else {
                    conversationDetailsView.onImageUploadError(var22.getErrorMessage());
                  }
                });
          } else {

            conversationDetailsView.onImageUploadError(var2.getErrorMessage());
          }
        });
  }

  @Override
  public void deleteImage(File imageFile) {
    if (imageFile != null && imageFile.exists()) {

      //noinspection ResultOfMethodCallIgnored
      imageFile.delete();
    }
  }

  @Override
  public void updateConversationImage(String conversationId, String conversationImageUrl) {
    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .updateConversationImage(new UpdateConversationImageQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setConversationImageUrl(conversationImageUrl)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            conversationDetailsView.onConversationImageUpdated();
          } else {
            conversationDetailsView.onConversationImageUpdateFailed(var2.getErrorMessage());
          }
        });
  }

  private void updateConversationSearchableTags(String conversationId, String conversationTitle,
      String oldConversationTitle) {

    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchConversationDetails(new FetchConversationDetailsQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setIncludeMembers(false)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            List<String> searchableTags = var1.getConversationDetails().getSearchableTags();

            searchableTags.remove(oldConversationTitle);
            searchableTags.add(conversationTitle);

            isometrik.getRemoteUseCases()
                .getConversationUseCases()
                .updateConversationDetails(
                    new UpdateConversationDetailsQuery.Builder().setConversationId(conversationId)
                        .setUserToken(userToken)
                        .setSearchableTags(searchableTags)
                        .build(), (var11, var22) -> {

                    });
          }
        });
  }

  @Override
  public void cancelConversationImageUpload() {
    isometrik.getRemoteUseCases()
        .getUploadUseCases()
        .cancelConversationImageUpload(
            new CancelConversationImageUploadQuery.Builder().setRequestId(uploadRequestId).build(),
            (var1, var2) -> {
            });
  }
}
