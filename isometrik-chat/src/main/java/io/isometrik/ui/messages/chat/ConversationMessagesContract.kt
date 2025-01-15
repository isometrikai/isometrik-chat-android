package io.isometrik.ui.messages.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.isometrik.chat.enums.AttachmentMessageType
import io.isometrik.chat.enums.PresignedUrlMediaTypes
import io.isometrik.chat.response.message.utils.schemas.Attachment
import io.isometrik.chat.response.message.utils.schemas.MentionedUser
import io.isometrik.chat.utils.enums.MessageTypeUi
import io.isometrik.ui.messages.chat.utils.enums.RemoteMessageTypes
import io.isometrik.ui.messages.chat.utils.messageutils.MultipleMessagesUtil
import io.isometrik.ui.messages.chat.utils.messageutils.OriginalReplyMessageUtil
import io.isometrik.ui.messages.reaction.add.ReactionModel
import io.isometrik.ui.messages.tag.TagUserModel
import org.json.JSONObject

/**
 * The interface conversation messages contract containing presenter and view interfaces implemented
 * by ConversationMessagesPresenter and ConversationMessagesActivity respectively.
 */
interface ConversationMessagesContract {
    /**
     * The interface Presenter.
     */
    interface Presenter {
        /**
         * Initialize conversation.
         *
         * @param conversationId the conversation id
         * @param messageDeliveryReadEventsEnabled the message delivery read events enabled
         * @param typingEventsEnabled the typing events enabled
         * @param isPrivateOneToOne the is private one to one
         * @param extras the extras
         * @param joiningAsObserver the joining as observer
         */
        fun initializeConversation(
            conversationId: String?, messageDeliveryReadEventsEnabled: Boolean,
            typingEventsEnabled: Boolean, isPrivateOneToOne: Boolean, extras: Bundle?,
            joiningAsObserver: Boolean
        )

        /**
         * Download media.
         *
         * @param messagesModel the messages model
         * @param messagePosition the message position
         */
        fun downloadMedia(messagesModel: MessagesModel?, messagePosition: Int)

        /**
         * Cancel media download.
         *
         * @param messagesModel the messages model
         * @param messagePosition the message position
         */
        fun cancelMediaDownload(messagesModel: MessagesModel?, messagePosition: Int)

        /**
         * Verify message position in list int.
         *
         * @param messageId the message id
         * @param position the position
         * @param messages the messages
         * @return the int
         */
        fun verifyMessagePositionInList(
            messageId: String?, position: Int,
            messages: ArrayList<MessagesModel>
        ): Int

        /**
         * Verify unsent message position in list int.
         *
         * @param messageId the message id
         * @param position the position
         * @param messages the messages
         * @return the int
         */
        fun verifyUnsentMessagePositionInList(
            messageId: String, position: Int,
            messages: ArrayList<MessagesModel>
        ): Int

        /**
         * Share message.
         *
         * @param messageType the message type
         * @param parentMessageId the parent message id
         * @param originalReplyMessageUtil the original reply message util
         * @param customType the custom type
         * @param messageBody the message body
         * @param encrypted the encrypted
         * @param showInConversation the show in conversation
         * @param sendPushNotification the send push notification
         * @param updateUnreadCount the update unread count
         * @param attachments the attachments
         * @param messageMetadata the message metadata
         * @param mentionedUsers the mentioned users
         * @param messageTypesForUI the message types for ui
         * @param mediaPaths the media paths
         * @param uploadMediaRequired the upload media required
         * @param presignedUrlMediaTypes the presigned url media types
         * @param attachmentMessageType the attachment message type
         */
        fun shareMessage(
            messageType: RemoteMessageTypes?,
            parentMessageId: String?,
            originalReplyMessageUtil: OriginalReplyMessageUtil?,
            customType: String?,
            messageBody: String?,
            encrypted: Boolean,
            showInConversation: Boolean,
            sendPushNotification: Boolean?,
            updateUnreadCount: Boolean?,
            attachments: ArrayList<Attachment?>?,
            messageMetadata: JSONObject?,
            mentionedUsers: ArrayList<MentionedUser?>?,
            messageTypeUi: MessageTypeUi?,
            mediaPaths: ArrayList<String?>?,
            uploadMediaRequired: Boolean,
            presignedUrlMediaTypes: PresignedUrlMediaTypes?,
            attachmentMessageType: AttachmentMessageType?
        )

        /**
         * Fetch messages.
         *
         * @param skip the skip
         * @param refreshRequest the refresh request
         * @param isSearchRequest the is search request
         * @param searchTag the search tag
         * @param onReconnect the on reconnect
         */
        fun fetchMessages(
            skip: Int, refreshRequest: Boolean, isSearchRequest: Boolean, searchTag: String?,
            onReconnect: Boolean
        )

        /**
         * Fetch messages on scroll.
         */
        fun fetchMessagesOnScroll()

        /**
         * Register connection event listener.
         */
        fun registerConnectionEventListener()

        /**
         * Unregister connection event listener.
         */
        fun unregisterConnectionEventListener()

        /**
         * Register conversation event listener.
         */
        fun registerConversationEventListener()

        /**
         * Unregister conversation event listener.
         */
        fun unregisterConversationEventListener()

        /**
         * Register membership control event listener.
         */
        fun registerMembershipControlEventListener()

        /**
         * Unregister membership control event listener.
         */
        fun unregisterMembershipControlEventListener()

        /**
         * Register message event listener.
         */
        fun registerMessageEventListener()

        /**
         * Unregister message event listener.
         */
        fun unregisterMessageEventListener()

        /**
         * Register reaction event listener.
         */
        fun registerReactionEventListener()

        /**
         * Unregister reaction event listener.
         */
        fun unregisterReactionEventListener()

        /**
         * Register user event listener.
         */
        fun registerUserEventListener()

        /**
         * Unregister user event listener.
         */
        fun unregisterUserEventListener()

        /**
         * Delete message for self.
         *
         * @param messageIds the message ids
         * @param multipleMessagesSelected the multiple messages selected
         */
        fun deleteMessageForSelf(messageIds: ArrayList<String?>?, multipleMessagesSelected: Boolean)

        /**
         * Delete message for everyone.
         *
         * @param messageIds the message ids
         * @param multipleMessagesSelected the multiple messages selected
         */
        fun deleteMessageForEveryone(
            messageIds: ArrayList<String?>?,
            multipleMessagesSelected: Boolean
        )

        /**
         * Update message selection status.
         *
         * @param messagesModel the messages model
         * @param selected the selected
         */
        fun updateMessageSelectionStatus(messagesModel: MessagesModel?, selected: Boolean)

        /**
         * Cleanup selected messages.
         */
        fun cleanupSelectedMessages()

        /**
         * Copy text.
         */
        fun copyText()

        /**
         * Mark messages as read.
         */
        fun markMessagesAsRead()

        /**
         * Send typing message.
         */
        fun sendTypingMessage()

        /**
         * Save uploading message position.
         *
         * @param localMessageId the local message id
         * @param position the position
         */
        fun saveUploadingMessagePosition(localMessageId: String?, position: Int)

        /**
         * Cancel media upload.
         *
         * @param messagesModel the messages model
         * @param messagePosition the message position
         */
        fun cancelMediaUpload(messagesModel: MessagesModel?, messagePosition: Int)

        /**
         * Start audio recording.
         *
         * @param context the context
         */
        fun startAudioRecording(context: Context?)

        /**
         * Stop audio recording.
         *
         * @param canceled the canceled
         */
        fun stopAudioRecording(canceled: Boolean)

        /**
         * Is recording audio boolean.
         *
         * @return the boolean
         */
        val isRecordingAudio: Boolean

        /**
         * Search user to tag.
         *
         * @param searchTag the search tag
         */
        fun searchUserToTag(searchTag: String?)

        /**
         * Fetch message delivery read status on multiple messages marked as read event.
         *
         * @param messagesModels the messages models
         */
        fun fetchMessageDeliveryReadStatusOnMultipleMessagesMarkedAsReadEvent(
            messagesModels: ArrayList<MessagesModel>
        )

        /**
         * Gets conversation details intent.
         *
         * @param activity the activity
         * @param isPrivateOneToOne the is private one to one
         * @return the conversation details intent
         */
        fun getConversationDetailsIntent(activity: Activity?, isPrivateOneToOne: Boolean): Intent?

        /**
         * Delete conversation.
         */
        fun deleteConversation()

        /**
         * Request conversation details.
         */
        fun requestConversationDetails()

        /**
         * Is private one to one boolean.
         *
         * @return the boolean
         */
        val isPrivateOneToOne: Boolean

        /**
         * Sets active in conversation.
         *
         * @param active the active
         */
        fun setActiveInConversation(active: Boolean)

        /**
         * Update message.
         *
         * @param messageId the message id
         * @param body the body
         * @param oldMessage the old message
         */
        fun updateMessage(messageId: String?, body: String?, oldMessage: String?)

        /**
         * Join as observer.
         */
        fun joinAsObserver()

        /**
         * Leave as observer.
         */
        fun leaveAsObserver()

        /**
         * Block user.
         *
         * @param userId the user id
         */
        fun blockUser(userId: String?, isBlocked: Boolean, personalUserId: String?)

        /**
         * UnBlock user.
         *
         * @param userId the user id
         */
        fun unBlockUser(userId: String?, isBlocked: Boolean, personalUserId: String?)

        /**
         * Clear conversation.
         *
         * @param conversationId the conversation id
         */
        fun clearConversation(conversationId: String?)
    }

    /**
     * The interface View.
     */
    interface View {
        /**
         * On media downloaded complete.
         *
         * @param successfullyCompleted the successfully completed
         * @param messageId the message id
         * @param mediaTypeDownloadedMessage the media type downloaded message
         * @param messagePosition the message position
         * @param downloadedMediaPath the downloaded media path
         */
        fun onMediaDownloadedComplete(
            successfullyCompleted: Boolean, messageId: String,
            mediaTypeDownloadedMessage: String, messagePosition: Int, downloadedMediaPath: String?
        )

        /**
         * On media download canceled.
         *
         * @param successfullyCanceled the successfully canceled
         * @param messageId the message id
         * @param mediaTypeDownloadCanceledMessage the media type download canceled message
         * @param messagePosition the message position
         */
        fun onMediaDownloadCanceled(
            successfullyCanceled: Boolean, messageId: String,
            mediaTypeDownloadCanceledMessage: String, messagePosition: Int
        )

        /**
         * On message sent successfully.
         *
         * @param localMessageId the local message id
         * @param messageId the message id
         * @param mediaUrl the media url
         * @param thumbnailUrl the thumbnail url
         */
        fun onMessageSentSuccessfully(
            localMessageId: String,
            messageId: String,
            mediaUrl: String?,
            thumbnailUrl: String?
        )

        /**
         * On failed to send message.
         *
         * @param localMessageId the local message id
         * @param error the error
         */
        fun onFailedToSendMessage(localMessageId: String, error: String?)

        /**
         * On error.
         *
         * @param errorMessage the error message
         */
        fun onError(errorMessage: String?)

        /**
         * Add sent message in ui locally.
         *
         * @param messagesModel the messages model
         * @param uploadRequired the upload required
         */
        fun addSentMessageInUILocally(messagesModel: MessagesModel, uploadRequired: Boolean)

        /**
         * Add message in ui.
         *
         * @param messagesModel the messages model
         */
        fun addMessageInUI(messagesModel: MessagesModel)

        /**
         * Add messages in ui.
         *
         * @param messagesModel the messages model
         * @param refreshRequest the refresh request
         * @param hideSearchingMessageOverlay the hide searching message overlay
         * @param messageFound the message found
         * @param onReconnect the on reconnect
         */
        fun addMessagesInUI(
            messagesModel: ArrayList<MessagesModel>, refreshRequest: Boolean,
            hideSearchingMessageOverlay: Boolean, messageFound: Boolean, onReconnect: Boolean
        )

        /**
         * On message deleted successfully.
         *
         * @param messageIds the message ids
         */
        fun onMessageDeletedSuccessfully(messageIds: List<String>)

        /**
         * On message selection status.
         *
         * @param multipleMessagesUtil the multiple messages util
         */
        fun onMessageSelectionStatus(multipleMessagesUtil: MultipleMessagesUtil)

        /**
         * On text copy request.
         *
         * @param text the text
         */
        fun onTextCopyRequest(text: String)

        /**
         * On conversation cleared.
         */
        fun onConversationCleared()

        /**
         * On conversation title updated.
         *
         * @param newTitle the new title
         */
        fun onConversationTitleUpdated(newTitle: String)

        /**
         * On remote user typing event.
         *
         * @param message the message
         */
        fun onRemoteUserTypingEvent(message: String)

        /**
         * Update message reaction.
         *
         * @param messageId the message id
         * @param reactionModel the reaction model
         * @param reactionAdded the reaction added
         */
        fun updateMessageReaction(
            messageId: String, reactionModel: ReactionModel,
            reactionAdded: Boolean
        )

        /**
         * Close conversation.
         */
        fun closeConversation()

        /**
         * On download progress update.
         *
         * @param messageId the message id
         * @param messagePosition the message position
         * @param progress the progress
         */
        fun onDownloadProgressUpdate(messageId: String, messagePosition: Int, progress: Int)

        /**
         * On upload progress update.
         *
         * @param localMessageId the local message id
         * @param mediaId the media id
         * @param messagePosition the message position
         * @param progress the progress
         */
        fun onUploadProgressUpdate(
            localMessageId: String, mediaId: String, messagePosition: Int,
            progress: Int
        )

        /**
         * On media upload canceled.
         *
         * @param successfullyCanceled the successfully canceled
         * @param localMessageId the local message id
         * @param mediaTypeUploadCanceledMessage the media type upload canceled message
         * @param messagePosition the message position
         */
        fun onMediaUploadCanceled(
            successfullyCanceled: Boolean, localMessageId: String,
            mediaTypeUploadCanceledMessage: String, messagePosition: Int
        )

        /**
         * On audio recorded successfully.
         *
         * @param audioFilePath the audio file path
         */
        fun onAudioRecordedSuccessfully(audioFilePath: String)

        /**
         * Update participants count.
         *
         * @param participantsCount the participants count
         */
        fun updateParticipantsCount(participantsCount: Int)

        /**
         * On searched users fetched.
         *
         * @param usersModels the users models
         */
        fun onSearchedUsersFetched(usersModels: ArrayList<TagUserModel>)

        /**
         * Mark message as delivered to all.
         *
         * @param messageId the message id
         */
        fun markMessageAsDeliveredToAll(messageId: String)

        /**
         * Mark message as read by all.
         *
         * @param messageId the message id
         */
        fun markMessageAsReadByAll(messageId: String)

        /**
         * On multiple messages marked as read event.
         */
        fun onMultipleMessagesMarkedAsReadEvent()

        /**
         * Update conversation details in header.
         *
         * @param local the local
         * @param isPrivateOneToOne the is private one to one
         * @param userName the user name
         * @param isOnline the is online
         * @param lastSeenAt the last seen at
         * @param conversationTitle the conversation title
         * @param membersCount the members count
         */
        fun updateConversationDetailsInHeader(
            local: Boolean, isPrivateOneToOne: Boolean,
            userName: String?, isOnline: Boolean, lastSeenAt: Long, conversationTitle: String?,
            membersCount: Int
        )

        /**
         * Message to scroll to not found.
         */
        fun messageToScrollToNotFound()

        /**
         * On messaging status changed.
         *
         * @param disabled the disabled
         */
        fun onMessagingStatusChanged(disabled: Boolean)

        /**
         * On conversation deleted successfully.
         */
        fun onConversationDeletedSuccessfully()

        /**
         * Connection state changed.
         *
         * @param connected whether connection to receive realtime events has been made or broken
         */
        fun connectionStateChanged(connected: Boolean)

        /**
         * Show message notification.
         *
         * @param conversationId the conversation id
         * @param conversationTitle the conversation title
         * @param message the message
         * @param privateOneToOne the private one to one
         * @param messagePlaceHolderImage the message place holder image
         * @param isReactionMessage the is reaction message
         * @param conversationImageUrl the conversation image url
         * @param senderImageUrl the sender image url
         * @param senderName the sender name
         */
        fun showMessageNotification(
            conversationId: String, conversationTitle: String, message: String,
            privateOneToOne: Boolean, messagePlaceHolderImage: Int?, isReactionMessage: Boolean,
            conversationImageUrl: String?, senderImageUrl: String?, senderName: String
        )

        /**
         * On message updated successfully.
         *
         * @param messageId the message id
         * @param updatedMessage the updated message
         */
        fun onMessageUpdatedSuccessfully(messageId: String, updatedMessage: String)

        /**
         * On joined as observer successfully.
         */
        fun onJoinedAsObserverSuccessfully()

        /**
         * On failed to join as observer or fetch messages or conversation details.
         *
         * @param errorMessage the error message
         */
        fun onFailedToJoinAsObserverOrFetchMessagesOrConversationDetails(errorMessage: String)

        /**
         * Update visibility of observers icon.
         */
        fun updateVisibilityOfObserversIcon()

        /**
         * On user blocked.
         */
        fun onUserBlocked()

        /**
         * On user unBlocked.
         */
        fun onUserUnBlocked()

        /**
         * On conversation cleared successfully.
         */
        fun onConversationClearedSuccessfully()
    }
}