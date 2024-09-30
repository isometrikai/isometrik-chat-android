package io.isometrik.ui.users.edit;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.upload.CancelUserImageUploadQuery;
import io.isometrik.chat.builder.upload.UploadUserImageQuery;
import io.isometrik.chat.builder.user.FetchUpdateUserPresignedUrlQuery;
import io.isometrik.chat.builder.user.UpdateUserQuery;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.chat.utils.UserSession;
import io.isometrik.chat.utils.AttachmentMetadata;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The edit user presenter to update the user details.Contains helper methods to locally validate
 * the user details, upload user profile pic image and to delete the image from local storage after
 * image upload.
 * It implements EditUserContract.Presenter{@link EditUserContract.Presenter}
 *
 * @see EditUserContract.Presenter
 */
public class EditUserPresenter implements EditUserContract.Presenter {

  /**
   * Instantiates a new edit user presenter.
   *
   * @param editUserView the edit user view
   */
  EditUserPresenter(EditUserContract.View editUserView) {
    this.editUserView = editUserView;
  }

  private final EditUserContract.View editUserView;
  private boolean updatingUser;
  private String uploadRequestId;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();

  /**
   * {@link EditUserContract.Presenter#requestUserDetailsUpdate(String, String, String,
   * String, String, boolean, boolean, boolean, boolean, boolean)}
   */
  @Override
  public void requestUserDetailsUpdate(String userToken, String userName, String userIdentifier,
      String userProfilePicUrl, String userMetadata, boolean userNameUpdated,
      boolean userIdentifierUpdated, boolean userMetadataUpdated, boolean userNotificationUpdated,
      boolean userNotification) {
    if (!updatingUser) {
      updatingUser = true;

      UpdateUserQuery.Builder builder = new UpdateUserQuery.Builder().setUserToken(userToken);
      if (userIdentifierUpdated) {
        builder.setUserIdentifier(userIdentifier);
      }
      if (userNameUpdated) {
        builder.setUserName(userName);
      }
      if (userNotificationUpdated) {
        builder.setNotification(userNotification);
      }
      if (userProfilePicUrl != null) {
        builder.setUserProfileImageUrl(userProfilePicUrl);
      }
      JSONObject metadata = new JSONObject();

      if (userMetadataUpdated) {
        try {
          metadata = metadata.put("metadata", userMetadata);
          builder.setMetadata(metadata);
        } catch (JSONException ignore) {
        }
      }

      JSONObject finalMetadata = metadata;

      isometrik.getRemoteUseCases().getUserUseCases().updateUser(builder.build(), (var1, var2) -> {
        updatingUser = false;

        UserSession userSession = IsometrikChatSdk.getInstance().getUserSession();

        if (var1 != null) {
          if (userNameUpdated) {
            userSession.setUserName(userName);
          }
          if (userIdentifierUpdated) {
            userSession.setUserIdentifier(userIdentifier);
          }
          if (userProfilePicUrl != null) {
            userSession.setUserProfilePic(userProfilePicUrl);
          }
          if (userMetadataUpdated) {
            userSession.setUserMetadata(finalMetadata);
          }
          editUserView.onUserDetailsUpdated(userProfilePicUrl);
        } else {

          editUserView.onError(var2.getErrorMessage());
        }
      });
    }
  }

  ///**
  // * {@link EditUserContract.Presenter#requestImageUpload(String)}
  // */
  //@Override
  //public void requestImageUpload(String imagePath) {
  //  UploadImageResult uploadImageResult = new UploadImageResult() {
  //    @Override
  //    public void uploadSuccess(String requestId, @SuppressWarnings("rawtypes") Map resultData) {
  //      editUserView.onImageUploadResult((String) resultData.get("url"));
  //    }
  //
  //    //@Override
  //    //public void uploadError(String requestId, ErrorInfo error) {
  //    //
  //    //  editUserView.onImageUploadError(error.getDescription());
  //    //}
  //  };
  //
  //  ImageUtil.requestUploadImage(imagePath, false, null, uploadImageResult);
  //}

  /**
   * {@link EditUserContract.Presenter#validateUserDetails(String, String, File, boolean)}
   */
  @Override
  public void validateUserDetails(String userName, String userIdentifier, File file,
      boolean profilePicUpdated) {
    String errorMessage = null;

    if (userName.isEmpty()) {
      errorMessage =
          IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_invalid_username);
    } else if (userIdentifier.isEmpty()) {
      errorMessage =
          IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_invalid_user_identifier);
    } else {
      if (profilePicUpdated) {
        if (file == null || !file.exists()) {
          errorMessage =
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_invalid_user_image);
        }
      }
    }
    editUserView.onUserDetailsValidationResult(errorMessage);
  }

  /**
   * {@link EditUserContract.Presenter#deleteImage(File)}
   */
  @Override
  public void deleteImage(File imageFile) {
    if (imageFile != null && imageFile.exists()) {

      //noinspection ResultOfMethodCallIgnored
      imageFile.delete();
    }
  }

  @Override
  public void requestImageUpload(String requestId, String mediaPath) {
    uploadRequestId = requestId;
    FetchUpdateUserPresignedUrlQuery.Builder builder =
        new FetchUpdateUserPresignedUrlQuery.Builder().setUserToken(
            IsometrikChatSdk.getInstance().getUserSession().getUserToken())
            .setMediaExtension(new AttachmentMetadata(mediaPath).getExtension());
    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .fetchUpdateUserPresignedUrl(builder.build(), (var1, var2) -> {

          if (var1 != null) {
            UploadUserImageQuery uploadUserImageQuery =
                new UploadUserImageQuery.Builder().setPresignedUrl(var1.getPresignedUrl())
                    .setMediaPath(mediaPath)
                    .setRequestId(requestId)
                    .setUploadProgressListener(
                        (requestId1, requestGroupId, bytesWritten, contentLength) -> {
                          editUserView.onUploadProgressUpdate(
                              (int) ((bytesWritten * 100) / contentLength));
                        })
                    .build();

            isometrik.getRemoteUseCases()
                .getUploadUseCases()
                .uploadUserImage(uploadUserImageQuery, (var11, var22) -> {
                  if (var11 != null) {
                    editUserView.onImageUploadResult(var1.getMediaUrl());
                  } else {
                    editUserView.onImageUploadError(var22.getErrorMessage());
                  }
                });
          } else {

            editUserView.onImageUploadError(var2.getErrorMessage());
          }
        });
  }

  @Override
  public void cancelUserImageUpload() {
    isometrik.getRemoteUseCases()
        .getUploadUseCases()
        .cancelUserImageUpload(
            new CancelUserImageUploadQuery.Builder().setRequestId(uploadRequestId).build(),
            (var1, var2) -> {
            });
  }
}
