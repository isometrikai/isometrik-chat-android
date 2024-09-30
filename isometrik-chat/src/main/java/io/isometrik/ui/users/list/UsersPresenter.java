package io.isometrik.ui.users.list;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.user.AuthenticateUserQuery;
import io.isometrik.chat.builder.user.FetchUsersQuery;
import io.isometrik.chat.response.user.FetchUsersResult;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;

/**
 * The presenter to fetch list of users to login with paging, search and pull to refresh
 * option.Api call to authorize credentials of selected user.
 * It implements UsersContract.Presenter{@link UsersContract.Presenter}
 *
 * @see UsersContract.Presenter
 */
public class UsersPresenter implements UsersContract.Presenter {

  /**
   * Instantiates a new users presenter.
   *
   * @param usersView the users view
   */
  UsersPresenter(UsersContract.View usersView) {
    this.usersView = usersView;
  }

  private final UsersContract.View usersView;
  private String pageToken;
  private boolean isLastPage;
  private boolean isLoading, authenticatingUser;

  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();

  private boolean isSearchRequest;
  private String searchTag;

  private final int USERS_PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  @Override
  public void requestUsersData(boolean refreshRequest, boolean isSearchRequest, String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      pageToken = null;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchUsersQuery.Builder fetchUsersQuery =
        new FetchUsersQuery.Builder().setCount(USERS_PAGE_SIZE).setPageToken(pageToken);
    if (isSearchRequest && searchTag != null) {
      fetchUsersQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .fetchUsers(fetchUsersQuery.build(), (var1, var2) -> {

          isLoading = false;

          if (var1 != null) {

            ArrayList<UsersModel> usersModels = new ArrayList<>();

            pageToken = var1.getPageToken();
            if (pageToken == null) {

              isLastPage = true;
            }

            ArrayList<FetchUsersResult.User> users = var1.getUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {

              usersModels.add(new UsersModel(users.get(i)));
            }

            usersView.onUsersDataReceived(usersModels, refreshRequest);
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No users found
                usersView.onUsersDataReceived(new ArrayList<>(), true);
              } else {
                isLastPage = true;
              }
            } else {
              usersView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  /**
   * {@link UsersContract.Presenter#requestUsersDataOnScroll(int, int, int)}
   */
  @Override
  public void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= USERS_PAGE_SIZE) {
        requestUsersData(false, isSearchRequest, searchTag);
      }
    }
  }

  /**
   * {@link UsersContract.Presenter#authorizeUser(UsersModel, String)}
   */
  @Override
  public void authorizeUser(UsersModel userModel, String userPassword) {
    if (!authenticatingUser) {
      authenticatingUser = true;

      AuthenticateUserQuery.Builder builder =
          new AuthenticateUserQuery.Builder().setUserIdentifier(userModel.getUserIdentifier())
              .setPassword(userPassword);

      isometrik.getRemoteUseCases().getUserUseCases().authenticateUser(builder.build(), (var1, var2) -> {
        authenticatingUser = false;
        if (var1 != null) {
          IsometrikChatSdk.getInstance()
              .getUserSession()
              .switchUser(var1.getUserId(), var1.getUserToken(), userModel.getUserName(),
                  userModel.getUserIdentifier(), userModel.getUserProfileImageUrl(), true,
                  userModel.getMetaData(), userModel.isNotification(), 0);
          usersView.onUserAuthorized();
        } else {

          usersView.onUserAuthorizationError(var2.getErrorMessage());
        }
      });
    }
  }
}
