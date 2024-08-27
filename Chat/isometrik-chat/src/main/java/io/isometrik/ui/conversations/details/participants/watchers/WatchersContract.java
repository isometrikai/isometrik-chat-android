package io.isometrik.ui.conversations.details.participants.watchers;

import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * The interface watchers contract containing presenter and view interfaces implemented
 * by WatchersPresenter and WatchersFragment respectively.
 */
public interface WatchersContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<WatchersContract.View> {

    /**
     * Initialize.
     *
     * @param conversationId the conversation id
     * @param isUserAnAdmin the is user an admin
     * @param userId the user id
     */
    void initialize(String conversationId, boolean isUserAnAdmin, String userId);

    /**
     * Fetch watchers.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    /*
     * Request watchers data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void fetchWatchers(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request watchers on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchWatchersOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Kick out members.
     *
     * @param conversationId the conversation id
     * @param memberIds the member ids
     */
    void kickOutMembers(String conversationId, List<String> memberIds);

    /**
     * Make admin.
     *
     * @param conversationId the conversation id
     * @param memberId the member id
     */
    void makeAdmin(String conversationId, String memberId);

    /**
     * Revoke admin permissions.
     *
     * @param conversationId the conversation id
     * @param memberId the member id
     */
    void revokeAdminPermissions(String conversationId, String memberId);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On watchers fetched successfully.
     *
     * @param watchersModels the watchers models
     * @param refreshRequest the refresh request
     */
    void onWatchersFetchedSuccessfully(ArrayList<MembersWatchersModel> watchersModels,
        boolean refreshRequest);

    /**
     * On member removed successfully.
     *
     * @param memberId the member id
     */
    void onMemberRemovedSuccessfully(String memberId);

    /**
     * On member admin permissions updated.
     *
     * @param memberId the member id
     * @param admin the admin
     */
    void onMemberAdminPermissionsUpdated(String memberId, boolean admin);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
