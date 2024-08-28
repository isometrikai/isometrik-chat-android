package io.isometrik.ui.users.blockedornonblocked.blocked;

import io.isometrik.ui.users.blockedornonblocked.BlockedOrNonBlockedUsersModel;
import io.isometrik.chat.utils.BasePresenter;

import java.util.ArrayList;

/**
 * The interface blocked users contract containing presenter and view interfaces implemented
 * by BlockedUsersPresenter and BlockedUsersFragment respectively.
 */
public interface BlockedUsersContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<BlockedUsersContract.View> {

    /**
     * Fetch blocked users.
     *
     * @param skip the skip
     * @param refreshRequest the refresh request
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    void fetchBlockedUsers(int skip, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request blocked users on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchBlockedUsersOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Unblock user.
     *
     * @param opponentId the opponent id
     * @param position the position
     */
    void unblockUser(String opponentId, int position);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On blocked users fetched successfully.
     *
     * @param blockedUsers the blocked users
     * @param resultsOnScroll the results on scroll
     */
    void onBlockedUsersFetchedSuccessfully(ArrayList<BlockedOrNonBlockedUsersModel> blockedUsers,
        boolean resultsOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On user unblocked successfully.
     *
     * @param opponentId the opponent id
     * @param position the position
     */
    void onUserUnblockedSuccessfully(String opponentId, int position);
  }
}
