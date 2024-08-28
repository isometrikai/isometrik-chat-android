package io.isometrik.ui.messages.deliverystatus.complete;

import io.isometrik.ui.messages.deliverystatus.UsersModel;
import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface delivery complete contract containing presenter and view interfaces implemented
 * by DeliveryCompletePresenter and DeliveryCompleteFragment respectively.
 */
public interface DeliveryCompleteContract {
  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<DeliveryCompleteContract.View> {

    /**
     * Initialize.
     *
     * @param conversationId the conversation id
     * @param messageId the message id
     * @param sentAt the sent at
     */
    void initialize(String conversationId, String messageId, long sentAt);

    /**
     * Fetch message delivery status.
     *
     * @param skip the skip
     * @param limit the limit
     * @param onScroll the on scroll
     */
    void fetchMessageDeliveryStatus(int skip, int limit, boolean onScroll);

    /**
     * Fetch message read status.
     *
     * @param skip the skip
     * @param limit the limit
     * @param onScroll the on scroll
     */
    void fetchMessageReadStatus(int skip, int limit, boolean onScroll);

    /**
     * Request message delivery status on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchMessageDeliveryStatusOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Request message read status on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchMessageReadStatusOnScroll(int firstVisibleItemPosition, int visibleItemCount,
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
     * On message delivery status fetched successfully.
     *
     * @param usersModels the users models
     * @param resultsOnScroll the results on scroll
     */
    void onMessageDeliveryStatusFetchedSuccessfully(ArrayList<UsersModel> usersModels,
        boolean resultsOnScroll);

    /**
     * On message read status fetched successfully.
     *
     * @param usersModels the users models
     * @param resultsOnScroll the results on scroll
     */
    void onMessageReadStatusFetchedSuccessfully(ArrayList<UsersModel> usersModels,
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
     * @param usersModel the users model
     */
    void onMessageDeliveredEvent(UsersModel usersModel);

    /**
     * On message read event.
     *
     * @param usersModel the users model
     */
    void onMessageReadEvent(UsersModel usersModel);

    /**
     * On message delivery read events disabled.
     */
    void onMessageDeliveryReadEventsDisabled();
  }
}
