package io.isometrik.ui.conversations.newconversation.group;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.CreateConversationQuery;
import io.isometrik.chat.builder.conversation.FetchConversationPresignedUrlQuery;
import io.isometrik.chat.builder.upload.CancelConversationImageUploadQuery;
import io.isometrik.chat.builder.upload.UploadConversationImageQuery;
import io.isometrik.chat.builder.user.block.FetchNonBlockedUsersQuery;
import io.isometrik.chat.response.user.utils.NonBlockedUser;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.AttachmentMetadata;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The presenter to fetch list of users to select members to create new group conversation,
 * with paging, search and pull to refresh option.Api call to create a new group conversation.
 */
public class NewGroupConversationPresenter implements NewGroupConversationContract.Presenter {

  /**
   * Instantiates a new New group conversation presenter.
   *
   * @param newGroupConversationView the new group conversation view
   */
  NewGroupConversationPresenter(NewGroupConversationContract.View newGroupConversationView) {
    this.newGroupConversationView = newGroupConversationView;
  }

  private final NewGroupConversationContract.View newGroupConversationView;
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  private String uploadRequestId;

  @Override
  public void validateConversationDetails(String conversationTitle, File imageFile,
      ArrayList<UsersModel> selectedUsers) {

    String errorMessage = null;

    if (conversationTitle.isEmpty()) {
      errorMessage = IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_conversation_title_missing);
    }
    //else if (imageFile == null || !imageFile.exists()) {
    //  errorMessage = IsometrikUiSdk.getInstance()
    //      .getContext()
    //      .getString(R.string.ism_invalid_conversation_image);
    //}
    else if (selectedUsers.size() < 1) {
      errorMessage =
          IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_no_participant_added);
    }
    newGroupConversationView.conversationDetailsValidationResult(errorMessage);
  }

  @Override
  public void createNewConversation(String conversationTitle, String conversationImageUrl,
      int conversationType, boolean enablePushNotifications, boolean enableMessageTypingEvents,
      boolean enableMessageDeliveryReadEvents, ArrayList<UsersModel> users) {

    ArrayList<String> memberIds = new ArrayList<>();
    int size = users.size();
    for (int i = 0; i < size; i++) {

      memberIds.add(users.get(i).getUserId());
    }
    isometrik.getRemoteUseCases().getConversationUseCases().createConversation(
        new CreateConversationQuery.Builder().setConversationImageUrl(conversationImageUrl)
            .setUserToken(userToken)
            .setConversationTitle(conversationTitle)
            .setGroup(true)
            .setConversationType(conversationType)
            .setMembers(memberIds)
            .setReadEvents(enableMessageDeliveryReadEvents)
            .setTypingEvents(enableMessageTypingEvents)
            .setPushNotifications(enablePushNotifications)
            .setSearchableTags(Collections.singletonList(conversationTitle))
            .build(), (var1, var2) -> {
          if (var1 != null) {
            newGroupConversationView.onConversationCreatedSuccessfully(var1.getConversationId(),
                conversationTitle, conversationImageUrl, size + 1);
          }else{
            newGroupConversationView.onError(var2.getErrorMessage());
          }
        });
  }

  @Override
  public void fetchNonBlockedUsers(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }

    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchNonBlockedUsersQuery.Builder fetchNonBlockedUsersQuery =
        new FetchNonBlockedUsersQuery.Builder().setLimit(PAGE_SIZE)
            .setSkip(offset)
            .setSort(Constants.SORT_ORDER_ASC)
            .setUserToken(userToken);
    if (isSearchRequest && searchTag != null) {
      fetchNonBlockedUsersQuery.setSearchTag(searchTag);
    }

    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .fetchNonBlockedUsers(fetchNonBlockedUsersQuery.build(), (var1, var2) -> {

          isLoading = false;

          if (var1 != null) {

            ArrayList<UsersModel> usersModels = new ArrayList<>();
            ArrayList<NonBlockedUser> users = var1.getNonBlockedUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {

              usersModels.add(new UsersModel(users.get(i)));
            }
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            newGroupConversationView.onNonBlockedUsersFetched(usersModels, refreshRequest,
                isSearchRequest);
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

              if (refreshRequest) {
                //No users found
                newGroupConversationView.onNonBlockedUsersFetched(new ArrayList<>(), true,
                    isSearchRequest);
              } else {
                isLastPage = true;
              }
            } else {
              newGroupConversationView.onError(var2.getErrorMessage());
            }
      }
    });
  }

  @Override
  public void requestNonBlockedUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchNonBlockedUsers(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }
  //
  //@Override
  //public void requestImageUpload(String path) {
  //
  //  UploadImageResult uploadImageResult = new UploadImageResult() {
  //    @Override
  //    public void uploadSuccess(String requestId, @SuppressWarnings("rawtypes") Map resultData) {
  //      newGroupConversationView.onImageUploadResult((String) resultData.get("url"));
  //    }
  //
  //    //@Override
  //    //public void uploadError(String requestId, ErrorInfo error) {
  //    //
  //    //  newGroupConversationView.onImageUploadError(error.getDescription());
  //    //}
  //  };
  //
  //  ImageUtil.requestUploadImage(path, false, null, uploadImageResult);
  //}

  @Override
  public void deleteImage(File imageFile) {
    if (imageFile != null && imageFile.exists()) {

      //noinspection ResultOfMethodCallIgnored
      imageFile.delete();
    }
  }

  @Override
  public void requestImageUpload(String requestId, String conversationTitle, String mediaPath) {
    uploadRequestId = requestId;
    FetchConversationPresignedUrlQuery.Builder builder =
        new FetchConversationPresignedUrlQuery.Builder().setUserToken(
            IsometrikUiSdk.getInstance().getUserSession().getUserToken())
            .setConversationTitle(conversationTitle)
            .setNewConversation(true)
            .setMediaExtension(new AttachmentMetadata(mediaPath).getExtension());
    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchConversationPresignedUrl(builder.build(), (var1, var2) -> {

          if (var1 != null) {
            UploadConversationImageQuery uploadConversationImageQuery =
                new UploadConversationImageQuery.Builder().setPresignedUrl(var1.getPresignedUrl())
                    .setMediaPath(mediaPath)
                    .setRequestId(requestId)
                    .setUploadProgressListener(
                        (requestId1, requestGroupId, bytesWritten, contentLength) -> {
                          newGroupConversationView.onUploadProgressUpdate(
                              (int) ((bytesWritten * 100) / contentLength));
                        })
                    .build();

            isometrik.getRemoteUseCases()
                .getUploadUseCases()
                .uploadConversationImage(uploadConversationImageQuery, (var11, var22) -> {
                  if (var11 != null) {
                    newGroupConversationView.onImageUploadResult(var1.getMediaUrl());
                  } else {
                    newGroupConversationView.onImageUploadError(var22.getErrorMessage());
                  }
                });
          } else {

            newGroupConversationView.onImageUploadError(var2.getErrorMessage());
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
