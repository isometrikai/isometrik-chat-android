package io.isometrik.ui.messages.reaction.add;

import io.isometrik.chat.utils.BasePresenter;

/**
 * The interface add reaction contract containing presenter and view interfaces implemented
 * by AddReactionPresenter and AddReactionFragment respectively.
 */
public interface AddReactionContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<View> {

    /**
     * Add reaction.
     *
     * @param conversationId the conversation id
     * @param messageId the message id
     * @param reactionType the reaction type
     */
    void addReaction(String conversationId, String messageId, String reactionType);

  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On reaction added successfully.
     *
     * @param messageId the message id
     * @param reactionModel the reaction model
     */
    void onReactionAddedSuccessfully(String messageId, ReactionModel reactionModel);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
