package io.isometrik.ui.messages.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import io.isometrik.chat.enums.AttachmentMessageType;
import io.isometrik.chat.enums.PresignedUrlMediaTypes;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.response.message.utils.schemas.MentionedUser;
import io.isometrik.ui.messages.chat.utils.enums.MessageTypesForUI;
import io.isometrik.ui.messages.chat.utils.enums.RemoteMessageTypes;
import io.isometrik.ui.messages.chat.utils.messageutils.MultipleMessagesUtil;
import io.isometrik.ui.messages.chat.utils.messageutils.OriginalReplyMessageUtil;
import io.isometrik.ui.messages.reaction.add.ReactionModel;
import io.isometrik.ui.messages.tag.TagUserModel;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * The interface conversation messages contract containing presenter and view interfaces implemented
 * by ConversationMessagesPresenter and ConversationMessagesActivity respectively.
 */
public interface ConversationMessagesContract {
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
    void initializeConversation(String conversationId, boolean messageDeliveryReadEventsEnabled,
        boolean typingEventsEnabled, boolean isPrivateOneToOne, Bundle extras,
        boolean joiningAsObserver);

    /**
     * Download media.
     *
     * @param messagesModel the messages model
     * @param messagePosition the message position
     */
    void downloadMedia(MessagesModel messagesModel, int messagePosition);

    /**
     * Cancel media download.
     *
     * @param messagesModel the messages model
     * @param messagePosition the message position
     */
    void cancelMediaDownload(MessagesModel messagesModel, int messagePosition);

    /**
     * Verify message position in list int.
     *
     * @param messageId the message id
     * @param position the position
     * @param messages the messages
     * @return the int
     */
    int verifyMessagePositionInList(String messageId, int position,
        ArrayList<MessagesModel> messages);

    /**
     * Verify unsent message position in list int.
     *
     * @param messageId the message id
     * @param position the position
     * @param messages the messages
     * @return the int
     */
    int verifyUnsentMessagePositionInList(String messageId, int position,
        ArrayList<MessagesModel> messages);

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
    void shareMessage(RemoteMessageTypes messageType, String parentMessageId,
                      OriginalReplyMessageUtil originalReplyMessageUtil, String customType, String messageBody,
                      boolean encrypted, boolean showInConversation, Boolean sendPushNotification,
                      Boolean updateUnreadCount, ArrayList<Attachment> attachments, JSONObject messageMetadata,
                      ArrayList<MentionedUser> mentionedUsers, MessageTypesForUI messageTypesForUI,
                      ArrayList<String> mediaPaths, boolean uploadMediaRequired,
                      PresignedUrlMediaTypes presignedUrlMediaTypes, AttachmentMessageType attachmentMessageType);

    /**
     * Fetch messages.
     *
     * @param skip the skip
     * @param refreshRequest the refresh request
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     * @param onReconnect the on reconnect
     */
    void fetchMessages(int skip, boolean refreshRequest, boolean isSearchRequest, String searchTag,
        boolean onReconnect);

    /**
     * Fetch messages on scroll.
     */
    void fetchMessagesOnScroll();

    /**
     * Register connection event listener.
     */
    void registerConnectionEventListener();

    /**
     * Unregister connection event listener.
     */
    void unregisterConnectionEventListener();

    /**
     * Register conversation event listener.
     */
    void registerConversationEventListener();

    /**
     * Unregister conversation event listener.
     */
    void unregisterConversationEventListener();

    /**
     * Register membership control event listener.
     */
    void registerMembershipControlEventListener();

    /**
     * Unregister membership control event listener.
     */
    void unregisterMembershipControlEventListener();

    /**
     * Register message event listener.
     */
    void registerMessageEventListener();

    /**
     * Unregister message event listener.
     */
    void unregisterMessageEventListener();

    /**
     * Register reaction event listener.
     */
    void registerReactionEventListener();

    /**
     * Unregister reaction event listener.
     */
    void unregisterReactionEventListener();

    /**
     * Register user event listener.
     */
    void registerUserEventListener();

    /**
     * Unregister user event listener.
     */
    void unregisterUserEventListener();

    /**
     * Delete message for self.
     *
     * @param messageIds the message ids
     * @param multipleMessagesSelected the multiple messages selected
     */
    void deleteMessageForSelf(ArrayList<String> messageIds, boolean multipleMessagesSelected);

    /**
     * Delete message for everyone.
     *
     * @param messageIds the message ids
     * @param multipleMessagesSelected the multiple messages selected
     */
    void deleteMessageForEveryone(ArrayList<String> messageIds, boolean multipleMessagesSelected);

    /**
     * Update message selection status.
     *
     * @param messagesModel the messages model
     * @param selected the selected
     */
    void updateMessageSelectionStatus(MessagesModel messagesModel, boolean selected);

    /**
     * Cleanup selected messages.
     */
    void cleanupSelectedMessages();

    /**
     * Copy text.
     */
    void copyText();

    /**
     * Mark messages as read.
     */
    void markMessagesAsRead();

    /**
     * Send typing message.
     */
    void sendTypingMessage();

    /**
     * Save uploading message position.
     *
     * @param localMessageId the local message id
     * @param position the position
     */
    void saveUploadingMessagePosition(String localMessageId, int position);

    /**
     * Cancel media upload.
     *
     * @param messagesModel the messages model
     * @param messagePosition the message position
     */
    void cancelMediaUpload(MessagesModel messagesModel, int messagePosition);

    /**
     * Start audio recording.
     *
     * @param context the context
     */
    void startAudioRecording(Context context);

    /**
     * Stop audio recording.
     *
     * @param canceled the canceled
     */
    void stopAudioRecording(boolean canceled);

    /**
     * Is recording audio boolean.
     *
     * @return the boolean
     */
    boolean isRecordingAudio();

    /**
     * Search user to tag.
     *
     * @param searchTag the search tag
     */
    void searchUserToTag(String searchTag);

    /**
     * Fetch message delivery read status on multiple messages marked as read event.
     *
     * @param messagesModels the messages models
     */
    void fetchMessageDeliveryReadStatusOnMultipleMessagesMarkedAsReadEvent(
        ArrayList<MessagesModel> messagesModels);

    /**
     * Gets conversation details intent.
     *
     * @param activity the activity
     * @param isPrivateOneToOne the is private one to one
     * @return the conversation details intent
     */
    Intent getConversationDetailsIntent(Activity activity, boolean isPrivateOneToOne);

    /**
     * Delete conversation.
     */
    void deleteConversation();

    /**
     * Request conversation details.
     */
    void requestConversationDetails();

    /**
     * Is private one to one boolean.
     *
     * @return the boolean
     */
    boolean isPrivateOneToOne();

    /**
     * Sets active in conversation.
     *
     * @param active the active
     */
    void setActiveInConversation(boolean active);

    /**
     * Update message.
     *
     * @param messageId the message id
     * @param body the body
     * @param oldMessage the old message
     */
    void updateMessage(String messageId, String body, String oldMessage);

    /**
     * Join as observer.
     */
    void joinAsObserver();

    /**
     * Leave as observer.
     */
    void leaveAsObserver();

    /**
     * Block user.
     *
     * @param userId the user id
     */
    void blockUser(String userId,boolean isBlocked,String personalUserId);

    /**
     * UnBlock user.
     *
     * @param userId the user id
     */
    void unBlockUser(String userId,boolean isBlocked,String personalUserId);
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
    void onMediaDownloadedComplete(boolean successfullyCompleted, String messageId,
        String mediaTypeDownloadedMessage, int messagePosition, String downloadedMediaPath);

    /**
     * On media download canceled.
     *
     * @param successfullyCanceled the successfully canceled
     * @param messageId the message id
     * @param mediaTypeDownloadCanceledMessage the media type download canceled message
     * @param messagePosition the message position
     */
    void onMediaDownloadCanceled(boolean successfullyCanceled, String messageId,
        String mediaTypeDownloadCanceledMessage, int messagePosition);

    /**
     * On message sent successfully.
     *
     * @param localMessageId the local message id
     * @param messageId the message id
     * @param mediaUrl the media url
     * @param thumbnailUrl the thumbnail url
     */
    void onMessageSentSuccessfully(String localMessageId, String messageId, String mediaUrl, String thumbnailUrl);

    /**
     * On failed to send message.
     *
     * @param localMessageId the local message id
     * @param error the error
     */
    void onFailedToSendMessage(String localMessageId, String error);

    /**
     * On error.
     *
     * @param errorMessage the error message
     */
    void onError(String errorMessage);

    /**
     * Add sent message in ui locally.
     *
     * @param messagesModel the messages model
     * @param uploadRequired the upload required
     */
    void addSentMessageInUILocally(MessagesModel messagesModel, boolean uploadRequired);

    /**
     * Add message in ui.
     *
     * @param messagesModel the messages model
     */
    void addMessageInUI(MessagesModel messagesModel);

    /**
     * Add messages in ui.
     *
     * @param messagesModel the messages model
     * @param refreshRequest the refresh request
     * @param hideSearchingMessageOverlay the hide searching message overlay
     * @param messageFound the message found
     * @param onReconnect the on reconnect
     */
    void addMessagesInUI(ArrayList<MessagesModel> messagesModel, boolean refreshRequest,
        boolean hideSearchingMessageOverlay, boolean messageFound, boolean onReconnect);

    /**
     * On message deleted successfully.
     *
     * @param messageIds the message ids
     */
    void onMessageDeletedSuccessfully(List<String> messageIds);

    /**
     * On message selection status.
     *
     * @param multipleMessagesUtil the multiple messages util
     */
    void onMessageSelectionStatus(MultipleMessagesUtil multipleMessagesUtil);

    /**
     * On text copy request.
     *
     * @param text the text
     */
    void onTextCopyRequest(String text);

    /**
     * On conversation cleared.
     */
    void onConversationCleared();

    /**
     * On conversation title updated.
     *
     * @param newTitle the new title
     */
    void onConversationTitleUpdated(String newTitle);

    /**
     * On remote user typing event.
     *
     * @param message the message
     */
    void onRemoteUserTypingEvent(String message);

    /**
     * Update message reaction.
     *
     * @param messageId the message id
     * @param reactionModel the reaction model
     * @param reactionAdded the reaction added
     */
    void updateMessageReaction(String messageId, ReactionModel reactionModel,
        boolean reactionAdded);

    /**
     * Close conversation.
     */
    void closeConversation();

    /**
     * On download progress update.
     *
     * @param messageId the message id
     * @param messagePosition the message position
     * @param progress the progress
     */
    void onDownloadProgressUpdate(String messageId, int messagePosition, int progress);

    /**
     * On upload progress update.
     *
     * @param localMessageId the local message id
     * @param mediaId the media id
     * @param messagePosition the message position
     * @param progress the progress
     */
    void onUploadProgressUpdate(String localMessageId, String mediaId, int messagePosition,
        int progress);

    /**
     * On media upload canceled.
     *
     * @param successfullyCanceled the successfully canceled
     * @param localMessageId the local message id
     * @param mediaTypeUploadCanceledMessage the media type upload canceled message
     * @param messagePosition the message position
     */
    void onMediaUploadCanceled(boolean successfullyCanceled, String localMessageId,
        String mediaTypeUploadCanceledMessage, int messagePosition);

    /**
     * On audio recorded successfully.
     *
     * @param audioFilePath the audio file path
     */
    void onAudioRecordedSuccessfully(String audioFilePath);

    /**
     * Update participants count.
     *
     * @param participantsCount the participants count
     */
    void updateParticipantsCount(int participantsCount);

    /**
     * On searched users fetched.
     *
     * @param usersModels the users models
     */
    void onSearchedUsersFetched(ArrayList<TagUserModel> usersModels);

    /**
     * Mark message as delivered to all.
     *
     * @param messageId the message id
     */
    void markMessageAsDeliveredToAll(String messageId);

    /**
     * Mark message as read by all.
     *
     * @param messageId the message id
     */
    void markMessageAsReadByAll(String messageId);

    /**
     * On multiple messages marked as read event.
     */
    void onMultipleMessagesMarkedAsReadEvent();

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
    void updateConversationDetailsInHeader(boolean local, boolean isPrivateOneToOne,
        String userName, boolean isOnline, long lastSeenAt, String conversationTitle,
        int membersCount);

    /**
     * Message to scroll to not found.
     */
    void messageToScrollToNotFound();

    /**
     * On messaging status changed.
     *
     * @param disabled the disabled
     */
    void onMessagingStatusChanged(boolean disabled);

    /**
     * On conversation deleted successfully.
     */
    void onConversationDeletedSuccessfully();

    /**
     * Connection state changed.
     *
     * @param connected whether connection to receive realtime events has been made or broken
     */
    void connectionStateChanged(boolean connected);

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
    void showMessageNotification(String conversationId, String conversationTitle, String message,
        boolean privateOneToOne, Integer messagePlaceHolderImage, boolean isReactionMessage,
        String conversationImageUrl, String senderImageUrl, String senderName);

    /**
     * On message updated successfully.
     *
     * @param messageId the message id
     * @param updatedMessage the updated message
     */
    void onMessageUpdatedSuccessfully(String messageId, String updatedMessage);

    /**
     * On joined as observer successfully.
     */
    void onJoinedAsObserverSuccessfully();

    /**
     * On failed to join as observer or fetch messages or conversation details.
     *
     * @param errorMessage the error message
     */
    void onFailedToJoinAsObserverOrFetchMessagesOrConversationDetails(String errorMessage);

    /**
     * Update visibility of observers icon.
     */
    void updateVisibilityOfObserversIcon();
  }
}