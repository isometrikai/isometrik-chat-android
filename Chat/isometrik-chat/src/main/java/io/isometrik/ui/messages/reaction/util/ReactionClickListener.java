package io.isometrik.ui.messages.reaction.util;

import io.isometrik.ui.messages.reaction.add.ReactionModel;

/**
 * The interface Reaction click listener for callback on click of a reaction.
 */
public interface ReactionClickListener {

  /**
   * On message reaction clicked.
   *
   * @param messageId the message id
   * @param reactionModel the reaction model
   */
  void onMessageReactionClicked(String messageId, ReactionModel reactionModel);
}
