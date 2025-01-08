package io.isometrik.ui.messages.action

import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.ReactionModel
import org.json.JSONObject

/**
 * The interface for message action callbacks to messages screen for- reply/edit/delete for
 * self/everyone, fetch delivery info, add reaction, download media and forward message.
 */
interface MessageActionCallback {
    /**
     * Send reply message.
     *
     * @param messageId the message id
     * @param replyMessage the reply message
     * @param replyMessageDetails the reply message details
     */
    fun sendReplyMessage(
        messageId: String,
        replyMessage: String,
        replyMessageDetails: JSONObject
    )

    /**
     * Reply message requested.
     *
     * @param messagesModel the messages model
     */
    fun replyMessageRequested(messagesModel: MessagesModel)

    /**
     * Delete message for self.
     *
     * @param messageId the message id
     * @param multipleMessagesSelected the multiple messages selected
     */
    fun deleteMessageForSelf(messageId: String?, multipleMessagesSelected: Boolean)

    /**
     * Delete message for everyone.
     *
     * @param messageId the message id
     * @param multipleMessagesSelected the multiple messages selected
     */
    fun deleteMessageForEveryone(messageId: String?, multipleMessagesSelected: Boolean)

    /**
     * Select multiple messages requested.
     */
    fun selectMultipleMessagesRequested()

    /**
     * Add reaction for message.
     *
     * @param messageId the message id
     */
    fun addReactionForMessage(messageId: String)

    /**
     * Fetch messages info request.
     *
     * @param messagesModel the messages model
     */
    fun fetchMessagesInfoRequest(messagesModel: MessagesModel)

    /**
     * Update message reaction.
     *
     * @param messageId the message id
     * @param reactionModel the reaction model
     * @param reactionAdded the reaction added
     */
    fun updateMessageReaction(
        messageId: String,
        reactionModel: ReactionModel,
        reactionAdded: Boolean
    )

    /**
     * Forward message request.
     *
     * @param messagesModel the messages model
     */
    fun forwardMessageRequest(messagesModel: MessagesModel)

    /**
     * Download media.
     *
     * @param messagesModel the messages model
     * @param mediaType the media type
     * @param messagePosition the message position
     */
    fun downloadMedia(messagesModel: MessagesModel, mediaType: String, messagePosition: Int)

    /**
     * Edit message requested.
     *
     * @param messagesModel the messages model
     */
    fun editMessageRequested(messagesModel: MessagesModel)

    /**
     * Update message.
     *
     * @param messageId the message id
     * @param updatedMessage the updated message
     * @param originalMessage the original message
     */
    fun updateMessage(messageId: String, updatedMessage: String, originalMessage: String)

    /**
     * use for scrolling to parent message
    * */
    fun onScrollToParentMessage(messageId: String?)

    /**
     * cancel Media download
     * */
    fun cancelMediaDownload(messagesModel: MessagesModel, messagePosition: Int)

    /**
     * click on message cell
     * */
    fun handleClickOnMessageCell(messagesModel: MessagesModel, localMedia: Boolean)

    /**
     * cancel media upload
     * */
    fun cancelMediaUpload(messagesModel: MessagesModel, messagePosition: Int)

    /**
     * remove canceled message
     * */
    fun removeCanceledMessage(localMessageId: String, messagePosition: Int)

    /**
     * message reaction clicked
     * */
    fun onMessageReactionClicked(messageId: String, reactionModel: ReactionModel)

}
