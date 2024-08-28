package io.isometrik.ui.users.details;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.user.DeleteUserQuery;
import io.isometrik.chat.builder.user.FetchUserDetailsQuery;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.utils.UserSession;

/**
 * The user details presenter to fetch the user details and to delete a user.
 * It implements UserDetailsContract.Presenter{@link UserDetailsContract.Presenter}
 *
 * @see UserDetailsContract.Presenter
 */
public class UserDetailsPresenter implements UserDetailsContract.Presenter {

  /**
   * Instantiates a new User details presenter.
   *
   * @param userDetailsView the user details view
   */
  UserDetailsPresenter(UserDetailsContract.View userDetailsView) {
    this.userDetailsView = userDetailsView;
  }

  private final UserDetailsContract.View userDetailsView;
  private boolean deletingUser;
  private boolean fetchingUserDetails;

  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  /**
   * {@link UserDetailsContract.Presenter#requestUserDetails(String)}
   */
  @Override
  public void requestUserDetails(String userToken) {
    if (!fetchingUserDetails) {
      fetchingUserDetails = true;
      isometrik.getRemoteUseCases()
          .getUserUseCases()
          .fetchUserDetails(new FetchUserDetailsQuery.Builder().setUserToken(userToken).build(),
              (var1, var2) -> {
                fetchingUserDetails = false;
                if (var1 != null) {

                  try {

                    UserSession userSession = IsometrikUiSdk.getInstance().getUserSession();

                    boolean notificationToBeUpdated , nameToBeUpdated = true,
                        identifierToBeUpdated = true, profilePicToBeUpdated = true,
                        metadataToBeUpdated = true;

                    if (userSession.getUserName() != null) {

                      nameToBeUpdated = !userSession.getUserName().equals(var1.getUserName());
                    }

                    if (userSession.getUserIdentifier() != null) {

                      identifierToBeUpdated =
                          !userSession.getUserIdentifier().equals(var1.getUserIdentifier());
                    }
                    if (userSession.getUserProfilePic() != null) {

                      profilePicToBeUpdated =
                          !userSession.getUserProfilePic().equals(var1.getUserProfileImageUrl());
                    }

                    notificationToBeUpdated =
                        userSession.getUserNotification() != var1.isNotification();

                    if (userSession.getUserMetadata() != null) {

                      metadataToBeUpdated =
                          !userSession.getUserMetadata().equals(var1.getMetaData());
                    }
                    userDetailsView.onUserDetailsReceived(var1.getUserName(),
                        var1.getUserIdentifier(), var1.getUserProfileImageUrl(), var1.getMetaData(),
                        var1.isNotification(), nameToBeUpdated, identifierToBeUpdated,
                        profilePicToBeUpdated, metadataToBeUpdated, notificationToBeUpdated);
                  } catch (Exception ignore) {
                    //For the exception,in case profile has been switched before response for user details api came
                  }
            } else {

              userDetailsView.onError(var2.getErrorMessage());
            }
          });
    }
  }

  /**
   * {@link UserDetailsContract.Presenter#clearUserSession()}
   */
  @Override
  public void clearUserSession() {
    IsometrikUiSdk.getInstance().getUserSession().clear();
  }

  /**
   * {@link UserDetailsContract.Presenter#requestUserDelete(String)}
   */
  @Override
  public void requestUserDelete(String userToken) {
    if (!deletingUser) {
      deletingUser = true;
      isometrik.getRemoteUseCases()
          .getUserUseCases()
          .deleteUser(new DeleteUserQuery.Builder().setUserToken(userToken).build(),
              (var1, var2) -> {
                deletingUser = false;
                if (var1 != null) {
                  clearUserSession();
                  userDetailsView.onUserDeleted();
                } else {

                  userDetailsView.onError(var2.getErrorMessage());
                }
              });
    }
  }
}
