package io.isometrik.ui.users.blockedornonblocked.blocked;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.user.block.FetchBlockedUsersQuery;
import io.isometrik.chat.builder.user.block.UnblockUserQuery;
import io.isometrik.chat.response.user.utils.BlockedUser;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.users.blockedornonblocked.BlockedOrNonBlockedUsersModel;
import io.isometrik.chat.utils.Constants;

import java.util.ArrayList;

/**
 * The presenter to fetch list of users blocked by logged in user with paging, search and pull to
 * refresh option.Api call to unblock user.
 */
public class BlockedUsersPresenter implements BlockedUsersContract.Presenter {

  private BlockedUsersContract.View blockedUsersView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();
  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void fetchBlockedUsers(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchBlockedUsersQuery.Builder fetchBlockedUsersQuery =
        new FetchBlockedUsersQuery.Builder().setLimit(PAGE_SIZE).setSkip(offset)
            //.setSort(Constants.SORT_ORDER_ASC)
            .setUserToken(userToken);
    if (isSearchRequest && searchTag != null) {
      fetchBlockedUsersQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .fetchBlockedUsers(fetchBlockedUsersQuery.build(), (var1, var2) -> {

          isLoading = false;

          if (var1 != null) {

            ArrayList<BlockedOrNonBlockedUsersModel> blockedUsersModels = new ArrayList<>();
            ArrayList<BlockedUser> users = var1.getBlockedUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {
              blockedUsersModels.add(new BlockedOrNonBlockedUsersModel(users.get(i)));
            }
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            if (blockedUsersView != null) {
              blockedUsersView.onBlockedUsersFetchedSuccessfully(blockedUsersModels,
                  refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

              if (refreshRequest) {
                //No users found
                if (blockedUsersView != null) {

                  blockedUsersView.onBlockedUsersFetchedSuccessfully(new ArrayList<>(), true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (blockedUsersView != null) {

                blockedUsersView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchBlockedUsersOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchBlockedUsers(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }

  @Override
  public void attachView(BlockedUsersContract.View blockedUsersView) {
    this.blockedUsersView = blockedUsersView;
  }

  @Override
  public void detachView() {
    blockedUsersView = null;
  }

  @Override
  public void unblockUser(String opponentId, int position) {

    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .unblockUser(new UnblockUserQuery.Builder().setUserToken(userToken)
            .setOpponentId(opponentId)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            blockedUsersView.onUserUnblockedSuccessfully(opponentId, position);
          } else {
            blockedUsersView.onError(var2.getErrorMessage());
          }
        });
  }
}
