package io.isometrik.ui.messages.deliverystatus.pending;

import io.isometrik.ui.messages.deliverystatus.UsersModel;
import io.isometrik.chat.utils.BasePresenter;

import java.util.ArrayList;

/**
 * The interface delivery pending contract containing presenter and view interfaces implemented
 * by DeliveryPendingPresenter and DeliveryPendingFragment respectively.
 */
public interface DeliveryPendingContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<DeliveryPendingContract.View> {

    /**
     * Initialize.
     *
     * @param conversationId the conversation id
     * @param messageId the message id
     * @param sentAt the sent at
     */
    void initialize(String conversationId, String messageId, long sentAt);

    /**
     * Fetch message pending delivery status.
     *
     * @param skip the skip
     * @param limit the limit
     * @param onScroll the on scroll
     */
    void fetchMessagePendingDeliveryStatus(int skip, int limit, boolean onScroll);

    /**
     * Fetch message pending read status.
     *
     * @param skip the skip
     * @param limit the limit
     * @param onScroll the on scroll
     */
    void fetchMessagePendingReadStatus(int skip, int limit, boolean onScroll);

    /**
     * Request message pending delivery status on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchMessagePendingDeliveryStatusOnScroll(int firstVisibleItemPosition,
        int visibleItemCount, int totalItemCount);

    /**
     * Request message pending read status on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchMessagePendingReadStatusOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Register message event listener.
     */
    void registerMessageEventListener();

    /**
     * Unregister message event listener.
     */
    void unregisterMessageEventListener();
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On message pending delivery status fetched successfully.
     *
     * @param usersModels the users models
     * @param resultsOnScroll the results on scroll
     */
    void onMessagePendingDeliveryStatusFetchedSuccessfully(ArrayList<UsersModel> usersModels,
        boolean resultsOnScroll);

    /**
     * On message pending read status fetched successfully.
     *
     * @param usersModels the users models
     * @param resultsOnScroll the results on scroll
     */
    void onMessagePendingReadStatusFetchedSuccessfully(ArrayList<UsersModel> usersModels,
        boolean resultsOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On message delivered event.
     *
     * @param userId the user id
     */
    void onMessageDeliveredEvent(String userId);

    /**
     * On message read event.
     *
     * @param userId the user id
     */
    void onMessageReadEvent(String userId);

    /**
     * On message delivery read events disabled.
     */
    void onMessageDeliveryReadEventsDisabled();
  }
}
