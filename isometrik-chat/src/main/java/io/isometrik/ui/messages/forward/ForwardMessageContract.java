package io.isometrik.ui.messages.forward;

import io.isometrik.ui.messages.chat.MessagesModel;
import java.util.ArrayList;

/**
 * The interface forward message contract containing presenter and view interfaces implemented
 * by ForwardMessagePresenter and ForwardMessageActivity respectively.
 */
public interface ForwardMessageContract {

  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Forward message.
     *
     * @param messagesModel the messages model
     * @param userIds the user ids
     * @param conversationIds the conversation ids
     * @param forwardMessageNotes the forward message notes
     */
    void forwardMessage(MessagesModel messagesModel, ArrayList<String> userIds,
        ArrayList<String> conversationIds, String forwardMessageNotes);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On message forwarded successfully.
     */
    void onMessageForwardedSuccessfully();

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
