package io.isometrik.ui.users.blockedornonblocked.nonblocked;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.user.block.BlockUserQuery;
import io.isometrik.chat.builder.user.block.FetchNonBlockedUsersQuery;
import io.isometrik.chat.response.user.utils.NonBlockedUser;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.ui.users.blockedornonblocked.BlockedOrNonBlockedUsersModel;
import io.isometrik.chat.utils.Constants;

import java.util.ArrayList;

/**
 * The presenter to fetch list of users neither blocked by logged in user nor logged
 * in user blocked by them with paging, search and pull to refresh
 * option.Api call to block user.
 */
public class NonBlockedUsersPresenter implements NonBlockedUsersContract.Presenter {

  private NonBlockedUsersContract.View nonBlockedUsersView;
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();
  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

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

            ArrayList<BlockedOrNonBlockedUsersModel> nonBlockedUsersModels = new ArrayList<>();
            ArrayList<NonBlockedUser> users = var1.getNonBlockedUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {

              nonBlockedUsersModels.add(new BlockedOrNonBlockedUsersModel(users.get(i)));
            }
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            if (nonBlockedUsersView != null) {
              nonBlockedUsersView.onNonBlockedUsersFetchedSuccessfully(nonBlockedUsersModels,
                  refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

              if (refreshRequest) {
                //No users found
                if (nonBlockedUsersView != null) {

                  nonBlockedUsersView.onNonBlockedUsersFetchedSuccessfully(new ArrayList<>(), true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (nonBlockedUsersView != null) {

                nonBlockedUsersView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchNonBlockedUsersOnScroll(int firstVisibleItemPosition, int visibleItemCount,
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

  @Override
  public void attachView(NonBlockedUsersContract.View nonBlockedUsersView) {
    this.nonBlockedUsersView = nonBlockedUsersView;
  }

  @Override
  public void detachView() {
    nonBlockedUsersView = null;
  }

  @Override
  public void blockUser(String opponentId, int position) {

    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .blockUser(
            new BlockUserQuery.Builder().setUserToken(userToken).setOpponentId(opponentId).build(),
            (var1, var2) -> {
              if (var1 != null) {
                nonBlockedUsersView.onUserBlockedSuccessfully(opponentId, position);
              } else {
                nonBlockedUsersView.onError(var2.getErrorMessage());
              }
            });
  }
}