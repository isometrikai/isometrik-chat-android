package io.isometrik.ui.users.edit;

import java.io.File;

/**
 * The interface edit user contract containing interfaces Presenter and View to be implemented
 * by the
 * EditUserPresenter{@link EditUserPresenter} and
 * EditUserActivity{@link EditUserActivity} respectively.
 *
 * @see EditUserPresenter
 * @see EditUserActivity
 */
public interface EditUserContract {

  /**
   * The interface EditUserContract.Presenter to be implemented by EditUserPresenter{@link
   * EditUserPresenter}*
   *
   * @see EditUserPresenter
   */
  interface Presenter {

    /**
     * Request user details update.
     *
     * @param userId the user id of the user whose details are to be updated
     * @param userName the user name of the user to be updated
     * @param userIdentifier the user identifier of the user to be updated
     * @param userProfilePicUrl the user profile pic url of the user to be updated
     * @param userMetadata the user metadata of the user to be updated
     * @param userNameUpdated whether user name has been updated or not
     * @param userIdentifierUpdated whether user identifier has been updated or not
     * @param userMetadataUpdated whether user metadata has been updated or not
     * @param userNotificationUpdated whether user notification setting has been updated or not
     * @param userNotification whether user enabled or disabled notification
     */
    void requestUserDetailsUpdate(String userId, String userName, String userIdentifier,
        String userProfilePicUrl, String userMetadata, boolean userNameUpdated,
        boolean userIdentifierUpdated, boolean userMetadataUpdated, boolean userNotificationUpdated,
        boolean userNotification);

    ///**
    // * Request image upload.
    // *
    // * @param imagePath the local path of the image to be uploaded
    // */
    //void requestImageUpload(String imagePath);

    /**
     * Validate user details.
     *
     * @param userName the user name of the user whose details are to be updated
     * @param userIdentifier the user identifier of the user whose details are to be updated
     * @param file the file containing image of the user who details are to be updated
     * @param profilePicUpdated whether to update the profile pic or not
     */
    void validateUserDetails(String userName, String userIdentifier, File file,
        boolean profilePicUpdated);

    /**
     * Delete image.
     *
     * @param file the file containing image to be deleted from local storage
     */
    void deleteImage(File file);

    /**
     * Request image upload.
     *
     * @param requestId the request id
     * @param mediaPath the media path
     */
    void requestImageUpload(String requestId, String mediaPath);

    /**
     * Cancel user image upload.
     */
    void cancelUserImageUpload();
  }

  /**
   * The interface EditUserContract.View to be implemented by EditUserActivity{@link
   * EditUserActivity}*
   *
   * @see EditUserActivity
   */
  interface View {

    /**
     * On user details updated.
     *
     * @param userProfilePicUrl the url of the user profile pic
     */
    void onUserDetailsUpdated(String userProfilePicUrl);

    /**
     * On user details validation result.
     *
     * @param errorMessage the error message containing details of the error in the validation of
     * the user details
     */
    void onUserDetailsValidationResult(String errorMessage);

    /**
     * On image upload result.
     *
     * @param url the url of the image uploaded
     */
    void onImageUploadResult(String url);

    /**
     * On image upload error.
     *
     * @param errorMessage the error message containing details of the error encountered while
     * trying to upload image
     */
    void onImageUploadError(String errorMessage);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On upload progress update.
     *
     * @param progress the progress
     */
    void onUploadProgressUpdate(int progress);

  }
}
