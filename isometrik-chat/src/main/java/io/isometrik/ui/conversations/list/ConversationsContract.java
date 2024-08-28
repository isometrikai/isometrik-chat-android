package io.isometrik.ui.conversations.list;

/**
 * The interface conversations contract containing presenter and view interfaces implemented
 * by ConversationsPresenter and ConversationsActivity respectively.
 */
public interface ConversationsContract {

  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Fetch unread conversations count.
     */
    void fetchUnreadConversationsCount();

    /**
     * Fetch user details.
     */
    void fetchUserDetails();

    /**
     * Fetch all undelivered messages.
     *
     * @param offset the offset
     */
    void fetchAllUndeliveredMessages(int offset);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On unread conversations count fetched successfully.
     *
     * @param count the count
     */
    void onUnreadConversationsCountFetchedSuccessfully(int count);

    /**
     * On user deleted.
     */
    void onUserDeleted();
  }
}
