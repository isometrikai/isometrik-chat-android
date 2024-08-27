package io.isometrik.ui.conversations.newconversation.onetoone;

import java.util.ArrayList;

/**
 * The interface new one to one conversation contract containing presenter and view interfaces
 * implemented
 * by NewOneToOneConversationPresenter and NewOneToOneConversationActivity respectively.
 */
public interface NewOneToOneConversationContract {
  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Create new conversation.
     *
     * @param enablePushNotifications the enable push notifications
     * @param enableMessageTypingEvents the enable message typing events
     * @param enableMessageDeliveryReadEvents the enable message delivery read events
     * @param selectedUserId the selected user id
     * @param selectedUsername the selected username
     */
    void createNewConversation(boolean enablePushNotifications, boolean enableMessageTypingEvents,
        boolean enableMessageDeliveryReadEvents, String selectedUserId,String selectedUsername);

    /**
     * Request users data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void fetchNonBlockedUsers(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request users data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestNonBlockedUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On conversation created successfully.
     *
     * @param conversationId the conversation id
     */
    void onConversationCreatedSuccessfully(String conversationId);

    /**
     * On users data received.
     *
     * @param users the users
     * @param latestUsers the latest users
     */
    void onNonBlockedUsersFetched(ArrayList<UsersModel> users, boolean latestUsers);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}

