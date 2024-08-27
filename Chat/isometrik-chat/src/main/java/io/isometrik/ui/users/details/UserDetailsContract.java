package io.isometrik.ui.users.details;

import org.json.JSONObject;

/**
 * The interface user details contract containing interfaces Presenter and View to be implemented
 * by the
 * UserDetailsPresenter{@link UserDetailsPresenter} and
 * UserDetailsActivity{@link UserDetailsActivity} respectively.
 *
 * @see UserDetailsPresenter
 * @see UserDetailsActivity
 */
public interface UserDetailsContract {

  /**
   * The interface UserDetailsContract.Presenter to be implemented by UserDetailsPresenter{@link
   * UserDetailsPresenter}*
   *
   * @see UserDetailsPresenter
   */
  interface Presenter {

    /**
     * Request user details.
     *
     * @param userToken the token of the user whose details are to be fetched
     */
    void requestUserDetails(String userToken);

    /**
     * Request user delete.
     *
     * @param userToken the token of the user to be deleted
     */
    void requestUserDelete(String userToken);

    /**
     * Clear user session.
     */
    void clearUserSession();
  }

  /**
   * The interface UserDetailsContract.View to be implemented by UserDetailsActivity{@link
   * UserDetailsActivity}*
   *
   * @see UserDetailsActivity
   */
  interface View {

    /**
     * On user details received.
     *
     * @param userName the user name received
     * @param userIdentifier the user identifier received
     * @param userProfilePicUrl the user profile pic url received
     * @param userMetadata the user metadata received
     * @param userNotification the user notification setting
     * @param updateUserName whether to update the username in the UI
     * @param updateUserIdentifier whether to update the user identifier in the UI
     * @param updateUserProfilePic whether to update the user profile pic in the UI
     * @param updateUserMetadata whether to update the user metadata in the UI
     * @param updateUserNotification whether update the user notification setting in the UI
     */
    void onUserDetailsReceived(String userName, String userIdentifier, String userProfilePicUrl,
        JSONObject userMetadata, boolean userNotification, boolean updateUserName,
        boolean updateUserIdentifier, boolean updateUserProfilePic, boolean updateUserMetadata,
        boolean updateUserNotification);

    /**
     * On user deleted successfully.
     */
    void onUserDeleted();

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
