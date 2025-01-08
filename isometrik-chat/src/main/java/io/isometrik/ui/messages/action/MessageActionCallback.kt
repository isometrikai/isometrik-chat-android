package io.isometrik.ui.messages.action;

import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.reaction.add.ReactionModel;
import org.json.JSONObject;

/**
 * The interface for message action callbacks to messages screen for- reply/edit/delete for
 * self/everyone, fetch delivery info, add reaction, download media and forward message.
 */
public interface MessageActionCallback {

  /**
   * Send reply message.
   *
   * @param messageId the message id
   * @param replyMessage the reply message
   * @param replyMessageDetails the reply message details
   */
  void sendReplyMessage(String messageId, String replyMessage, JSONObject replyMessageDetails);

  /**
   * Reply message requested.
   *
   * @param messagesModel the messages model
   */
  void replyMessageRequested(MessagesModel messagesModel);

  /**
   * Delete message for self.
   *
   * @param messageId the message id
   * @param multipleMessagesSelected the multiple messages selected
   */
  void deleteMessageForSelf(String messageId, boolean multipleMessagesSelected);

  /**
   * Delete message for everyone.
   *
   * @param messageId the message id
   * @param multipleMessagesSelected the multiple messages selected
   */
  void deleteMessageForEveryone(String messageId, boolean multipleMessagesSelected);

  /**
   * Select multiple messages requested.
   */
  void selectMultipleMessagesRequested();

  /**
   * Add reaction for message.
   *
   * @param messageId the message id
   */
  void addReactionForMessage(String messageId);

  /**
   * Fetch messages info request.
   *
   * @param messagesModel the messages model
   */
  void fetchMessagesInfoRequest(MessagesModel messagesModel);

  /**
   * Update message reaction.
   *
   * @param messageId the message id
   * @param reactionModel the reaction model
   * @param reactionAdded the reaction added
   */
  void updateMessageReaction(String messageId, ReactionModel reactionModel, boolean reactionAdded);

  /**
   * Forward message request.
   *
   * @param messagesModel the messages model
   */
  void forwardMessageRequest(MessagesModel messagesModel);

  /**
   * Download media.
   *
   * @param messagesModel the messages model
   * @param mediaType the media type
   * @param messagePosition the message position
   */
  void downloadMedia(MessagesModel messagesModel, String mediaType, int messagePosition);

  /**
   * Edit message requested.
   *
   * @param messagesModel the messages model
   */
  void editMessageRequested(MessagesModel messagesModel);

  /**
   * Update message.
   *
   * @param messageId the message id
   * @param updatedMessage the updated message
   * @param originalMessage the original message
   */
  void updateMessage(String messageId, String updatedMessage, String originalMessage);
}
