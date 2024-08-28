package io.isometrik.ui.users.blockedornonblocked.nonblocked;

import io.isometrik.ui.users.blockedornonblocked.BlockedOrNonBlockedUsersModel;
import io.isometrik.chat.utils.BasePresenter;

import java.util.ArrayList;

/**
 * The interface non blocked users contract containing presenter and view interfaces implemented
 * by NonBlockedUsersPresenter and NonBlockedUsersFragment respectively.
 */
public interface NonBlockedUsersContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<NonBlockedUsersContract.View> {

    /**
     * Fetch non blocked users.
     *
     * @param skip the skip
     * @param refreshRequest the refresh request
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    void fetchNonBlockedUsers(int skip, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request non blocked users on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchNonBlockedUsersOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Block user.
     *
     * @param opponentId the opponent id
     * @param position the position
     */
    void blockUser(String opponentId, int position);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On non blocked users fetched successfully.
     *
     * @param nonBlockedUsers the non blocked users
     * @param resultsOnScroll the results on scroll
     */
    void onNonBlockedUsersFetchedSuccessfully(
            ArrayList<BlockedOrNonBlockedUsersModel> nonBlockedUsers, boolean resultsOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On user blocked successfully.
     *
     * @param opponentId the opponent id
     * @param position the position
     */
    void onUserBlockedSuccessfully(String opponentId, int position);
  }
}
