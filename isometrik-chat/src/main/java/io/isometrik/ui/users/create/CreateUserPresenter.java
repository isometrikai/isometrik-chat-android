package io.isometrik.ui.users.create;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.upload.CancelUserImageUploadQuery;
import io.isometrik.chat.builder.upload.UploadUserImageQuery;
import io.isometrik.chat.builder.user.CreateUserQuery;
import io.isometrik.chat.builder.user.FetchCreateUserPresignedUrlQuery;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.chat.utils.AttachmentMetadata;
import io.isometrik.chat.utils.PasswordRegex;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The create user presenter to create a new user.Contains helper methods to locally validate the
 * user details, upload user profile pic image and to delete the image from local storage after
 * image upload.
 * It implements CreateUserContract.Presenter{@link CreateUserContract.Presenter}
 *
 * @see CreateUserContract.Presenter
 */
public class CreateUserPresenter implements CreateUserContract.Presenter {

  /**
   * Instantiates a new create user presenter.
   *
   * @param createUserView the create user view
   */
  CreateUserPresenter(CreateUserContract.View createUserView) {
    this.createUserView = createUserView;
  }

  private final CreateUserContract.View createUserView;
  private boolean creatingUser;
  private String uploadRequestId;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();

  /**
   * {@link CreateUserContract.Presenter#requestCreateUser(String, String, String, String, String)}
   */
  @Override
  public void requestCreateUser(String userName, String userIdentifier, String userProfilePicUrl,
      String userPassword, String userMetadata) {
    if (!creatingUser) {
      creatingUser = true;
      JSONObject metadata = new JSONObject();
      CreateUserQuery.Builder builder =
          new CreateUserQuery.Builder().setUserIdentifier(userIdentifier)
              .setUserName(userName)
              .setUserProfileImageUrl(userProfilePicUrl)
              .setPassword(userPassword);
      if (userMetadata != null && !userMetadata.isEmpty()) {
        try {
          metadata = metadata.put("metadata", userMetadata);
          builder.setMetadata(metadata);
        } catch (JSONException ignore) {
        }
      }
      JSONObject finalMetadata = metadata;
      isometrik.getRemoteUseCases().getUserUseCases().createUser(builder.build(), (var1, var2) -> {
        creatingUser = false;
        if (var1 != null) {

          IsometrikChatSdk.getInstance()
              .getUserSession()
              .switchUser(var1.getUserId(), var1.getUserToken(), userName, userIdentifier,
                  userProfilePicUrl, false, finalMetadata, true,0);

          createUserView.onUserCreated();
        } else {

          createUserView.onError(var2.getErrorMessage());
        }
      });
    }
  }

  /**
   * {@link CreateUserContract.Presenter#validateUserDetails(String, String, String, File)}
   */
  @Override
  public void validateUserDetails(String userName, String userIdentifier, String userPassword,
      File file) {

    String errorMessage = null;

    if (userName.isEmpty()) {
      errorMessage =
          IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_invalid_username);
    } else if (userIdentifier.isEmpty()) {
      errorMessage =
          IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_invalid_user_identifier);
    } else if (userPassword.isEmpty()) {
      errorMessage =
          IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_missing_user_password);
    } else if (!PasswordRegex.isValidPassword(userPassword)) {
      errorMessage =
          IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_invalid_user_password);
    }
    //else if (file == null || !file.exists()) {
    //  errorMessage =
    //      IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_invalid_user_image);
    //}
    createUserView.onUserDetailsValidationResult(errorMessage);
  }

  ///**
  // * {@link CreateUserContract.Presenter#requestImageUpload(String)}
  // */
  //@Override
  //public void requestImageUpload(String path) {
  //
  //  UploadImageResult uploadImageResult = new UploadImageResult() {
  //    @Override
  //    public void uploadSuccess(String requestId, @SuppressWarnings("rawtypes") Map resultData) {
  //      createUserView.onImageUploadResult((String) resultData.get("url"));
  //    }
  //    //
  //    //@Override
  //    //public void uploadError(String requestId, ErrorInfo error) {
  //    //
  //    //  createUserView.onImageUploadError(error.getDescription());
  //    //}
  //  };
  //
  //  ImageUtil.requestUploadImage(path, false, null, uploadImageResult);
  //}

  /**
   * {@link CreateUserContract.Presenter#deleteImage(File)}
   */
  @Override
  public void deleteImage(File imageFile) {
    if (imageFile != null && imageFile.exists()) {

      //noinspection ResultOfMethodCallIgnored
      imageFile.delete();
    }
  }

  @Override
  public void requestImageUpload(String requestId, String userIdentifier,
      String mediaPath) {
    uploadRequestId = requestId;
    FetchCreateUserPresignedUrlQuery.Builder builder =
        new FetchCreateUserPresignedUrlQuery.Builder().setUserIdentifier(userIdentifier)
            .setMediaExtension(new AttachmentMetadata(mediaPath).getExtension());
    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .fetchCreateUserPresignedUrl(builder.build(), (var1, var2) -> {

          if (var1 != null) {
            UploadUserImageQuery uploadUserImageQuery =
                new UploadUserImageQuery.Builder().setPresignedUrl(var1.getPresignedUrl())
                    .setMediaPath(mediaPath)
                    .setRequestId(requestId)
                    .setUploadProgressListener(
                        (requestId1, requestGroupId, bytesWritten, contentLength) -> {
                          createUserView.onUploadProgressUpdate(
                              (int) ((bytesWritten * 100) / contentLength));
                        })
                    .build();

            isometrik.getRemoteUseCases()
                .getUploadUseCases()
                .uploadUserImage(uploadUserImageQuery, (var11, var22) -> {
                  if (var11 != null) {
                    createUserView.onImageUploadResult(var1.getMediaUrl());
                  } else {
                    createUserView.onImageUploadError(var22.getErrorMessage());
                  }
                });
          } else {

            createUserView.onImageUploadError(var2.getErrorMessage());
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
