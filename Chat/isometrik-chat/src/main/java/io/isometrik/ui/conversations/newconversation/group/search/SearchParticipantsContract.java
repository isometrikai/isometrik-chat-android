//package io.isometrik.chat.ui.conversations.newconversation.group.search;
//
//import io.isometrik.chat.conversations.newconversation.group.UsersModel;
//import java.util.ArrayList;
//
//public class SearchParticipantsContract {
//
//  interface Presenter {
//
//    /**
//     * Request users data.
//     *
//     * @param offset the offset
//     * @param refreshRequest the refresh request
//     * @param isSearchRequest whether fetch users request is from the search or not
//     * @param searchTag the user's name to be searched
//     */
//    void fetchNonBlockedUsers(int offset, boolean refreshRequest, boolean isSearchRequest,
//        String searchTag);
//
//    /**
//     * Request users data on scroll.
//     *
//     * @param firstVisibleItemPosition the first visible item position
//     * @param visibleItemCount the visible item count
//     * @param totalItemCount the total item count
//     */
//    void requestNonBlockedUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
//        int totalItemCount);
//  }
//
//  interface View {
//
//    /**
//     * On users data received.
//     *
//     * @param users the users
//     * @param latestUsers the latest users
//     * @param isSearchRequest whether fetch users request is from the search or not
//     */
//    void onNonBlockedUsersFetched(ArrayList<UsersModel> users, boolean latestUsers,
//        boolean isSearchRequest);
//
//    /**
//     * On error.
//     *
//     * @param errorMessage the error message to be shown in the toast for details of the failed
//     * operation
//     */
//    void onError(String errorMessage);
//  }
//}
