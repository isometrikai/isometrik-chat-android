package io.isometrik.ui.messages.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchConversationDetailsQuery;
import io.isometrik.chat.builder.conversation.FetchConversationMembersQuery;
import io.isometrik.chat.builder.conversation.FetchConversationMessagingStatusQuery;
import io.isometrik.chat.builder.conversation.cleanup.ClearConversationQuery;
import io.isometrik.chat.builder.conversation.cleanup.DeleteConversationLocallyQuery;
import io.isometrik.chat.builder.deliverystatus.FetchMessagesDeliveryReadStatusQuery;
import io.isometrik.chat.builder.download.CancelMediaDownloadQuery;
import io.isometrik.chat.builder.membershipcontrol.JoinAsObserverQuery;
import io.isometrik.chat.builder.membershipcontrol.LeaveAsObserverQuery;
import io.isometrik.chat.builder.message.FetchAttachmentPresignedUrlsQuery;
import io.isometrik.chat.builder.message.FetchMessagesQuery;
import io.isometrik.chat.builder.message.SendMessageQuery;
import io.isometrik.chat.builder.message.SendTypingMessageQuery;
import io.isometrik.chat.builder.message.UpdateMessageDetailsQuery;
import io.isometrik.chat.builder.message.cleanup.RemoveMessagesForEveryoneQuery;
import io.isometrik.chat.builder.message.cleanup.RemoveMessagesForSelfQuery;
import io.isometrik.chat.builder.message.delivery.MarkMessageAsReadQuery;
import io.isometrik.chat.builder.message.delivery.MarkMultipleMessagesAsReadQuery;
import io.isometrik.chat.builder.message.delivery.UpdateLastReadInConversationQuery;
import io.isometrik.chat.builder.upload.CancelMediaUploadQuery;
import io.isometrik.chat.builder.upload.UploadMediaQuery;
import io.isometrik.chat.builder.user.block.BlockUserQuery;
import io.isometrik.chat.builder.user.block.UnblockUserQuery;
import io.isometrik.chat.callbacks.ConnectionEventCallback;
import io.isometrik.chat.callbacks.ConversationEventCallback;
import io.isometrik.chat.callbacks.MembershipControlEventCallback;
import io.isometrik.chat.callbacks.MessageEventCallback;
import io.isometrik.chat.callbacks.ReactionEventCallback;
import io.isometrik.chat.callbacks.UserEventCallback;
import io.isometrik.chat.enums.AttachmentMessageType;
import io.isometrik.chat.enums.ConversationType;
import io.isometrik.chat.enums.PresignedUrlMediaTypes;
import io.isometrik.chat.events.connection.ConnectEvent;
import io.isometrik.chat.events.connection.ConnectionFailedEvent;
import io.isometrik.chat.events.connection.DisconnectEvent;
import io.isometrik.chat.events.conversation.CreateConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.ClearConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.DeleteConversationLocallyEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationDetailsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationImageEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationSettingsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationTitleEvent;
import io.isometrik.chat.events.membershipcontrol.AddAdminEvent;
import io.isometrik.chat.events.membershipcontrol.AddMembersEvent;
import io.isometrik.chat.events.membershipcontrol.JoinConversationEvent;
import io.isometrik.chat.events.membershipcontrol.LeaveConversationEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverJoinEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverLeaveEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveAdminEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveMembersEvent;
import io.isometrik.chat.events.message.SendMessageEvent;
import io.isometrik.chat.events.message.SendTypingMessageEvent;
import io.isometrik.chat.events.message.UpdateMessageDetailsEvent;
import io.isometrik.chat.events.message.cleanup.RemoveMessagesForEveryoneEvent;
import io.isometrik.chat.events.message.cleanup.RemoveMessagesForSelfEvent;
import io.isometrik.chat.events.message.delivery.MarkMessageAsDeliveredEvent;
import io.isometrik.chat.events.message.delivery.MarkMessageAsReadEvent;
import io.isometrik.chat.events.message.delivery.MarkMultipleMessagesAsReadEvent;
import io.isometrik.chat.events.message.delivery.UpdatedLastReadInConversationEvent;
import io.isometrik.chat.events.message.user.block.BlockUserInConversationEvent;
import io.isometrik.chat.events.message.user.block.UnblockUserInConversationEvent;
import io.isometrik.chat.events.reaction.AddReactionEvent;
import io.isometrik.chat.events.reaction.RemoveReactionEvent;
import io.isometrik.chat.events.user.DeleteUserEvent;
import io.isometrik.chat.events.user.UpdateUserEvent;
import io.isometrik.chat.events.user.block.BlockUserEvent;
import io.isometrik.chat.events.user.block.UnblockUserEvent;
import io.isometrik.chat.models.download.mediautils.DownloadProgressListener;
import io.isometrik.chat.models.message.delivery.operations.MarkMessagesAsReadUtil;
import io.isometrik.chat.models.upload.utils.UploadProgressListener;
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil;
import io.isometrik.chat.response.conversation.utils.ConversationMember;
import io.isometrik.chat.response.deliverystatus.FetchMessagesDeliveryReadStatusResult;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.message.FetchAttachmentPresignedUrlsResult;
import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.response.message.utils.schemas.EventForMessage;
import io.isometrik.chat.response.message.utils.schemas.MentionedUser;
import io.isometrik.chat.enums.MessageTypeUi;
import io.isometrik.chat.utils.LogManger;
import io.isometrik.chat.utils.upload.CustomUploadHandler;
import io.isometrik.chat.utils.upload.UploadedMediaResponse;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.conversations.details.groupconversation.ConversationDetailsActivity;
import io.isometrik.ui.conversations.details.userconversation.UserConversationDetailsActivity;
import io.isometrik.ui.messages.chat.utils.attachmentutils.LocalMediaAttachmentHelper;
import io.isometrik.ui.messages.chat.utils.attachmentutils.MediaDownloadOrUploadHelper;
import io.isometrik.ui.messages.chat.utils.attachmentutils.PrepareAttachmentHelper;
import io.isometrik.ui.messages.chat.utils.enums.RemoteMessageTypes;
import io.isometrik.ui.messages.chat.utils.messageutils.ConversationActionMessageUtil;
import io.isometrik.ui.messages.chat.utils.messageutils.ConversationAttachmentMessageUtil;
import io.isometrik.ui.messages.chat.utils.messageutils.LocalMessageNotificationUtil;
import io.isometrik.ui.messages.chat.utils.messageutils.MultipleMessagesUtil;
import io.isometrik.ui.messages.chat.utils.messageutils.OriginalReplyMessageUtil;
import io.isometrik.ui.messages.chat.utils.messageutils.RealtimeMessageUtil;
import io.isometrik.ui.messages.media.audio.util.RecordAudioUtil;
import io.isometrik.ui.messages.reaction.add.ReactionModel;
import io.isometrik.ui.messages.reaction.util.ReactionUtil;
import io.isometrik.ui.messages.search.utils.NotificationBodyUtils;
import io.isometrik.ui.messages.search.utils.SearchTagUtils;
import io.isometrik.ui.messages.tag.TagUserModel;
import io.isometrik.ui.messages.tag.TaggedUserCallback;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.TagUserUtil;
import io.isometrik.chat.enums.CustomMessageTypes;
import io.isometrik.chat.utils.PresignedUrlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The presenter to send/receive messages in realtime of type- image/video/file/contact/location/whiteboard/sticker/gif/audio/text,
 * forward or add reaction to a message, search for message, tag a member in a text message, fetch
 * messages with paging and pull to refresh, take
 * various action like message
 * delete/edit on, join/leave as observer and fetch observers
 * list for open conversation,
 * fetch opponent's online or last seen status for 1-1 conversation, update UI on receiving
 * realtime
 * message or various actions in conversation, initiate/cancel of upload/download media message,
 * copy text messages, send typing message.
 */
public class ConversationMessagesPresenter implements ConversationMessagesContract.Presenter {

    private final TaggedUserCallback taggedUserCallback;

    /**
     * Instantiates a new Conversation messages presenter.
     *
     * @param conversationMessagesView the conversation messages view
     * @param taggedUserCallback       the tagged user callback
     */
    ConversationMessagesPresenter(ConversationMessagesContract.View conversationMessagesView,
                                  TaggedUserCallback taggedUserCallback) {
        this.conversationMessagesView = conversationMessagesView;
        multipleMessagesUtil = new MultipleMessagesUtil();
        this.taggedUserCallback = taggedUserCallback;
    }

    private final ConversationMessagesContract.View conversationMessagesView;
    private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
    private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();
    /**
     * The constant CONVERSATION_MESSAGES_PAGE_SIZE.
     */
    public static final int CONVERSATION_MESSAGES_PAGE_SIZE = Constants.CONVERSATION_MESSAGES_PAGE_SIZE;
    private String conversationId, audioFilePath;
    private int offset;
    private boolean isLastPage;
    private boolean isLoading;
    private final MultipleMessagesUtil multipleMessagesUtil;
    private boolean messageDeliveryReadEventsEnabled, typingEventsEnabled;
    private final Map<String, Integer> downloadMessagePositions = new HashMap<>();
    private final Map<String, Integer> uploadMessagePositions = new HashMap<>();
    private RecordAudioUtil recordAudioUtil;
    private boolean isSearchRequest;
    private String searchTag;
    private boolean isPrivateOneToOne;
    private String userName, userId, userImageUrl;
    private long lastSeenAt;
    private boolean isOnline;
    private String conversationImageUrl, conversationTitle;
    private boolean scrollToMessageNeeded, activeInConversation, joiningAsObserver;
    private String messageIdToScrollTo;

    @Override
    public void initializeConversation(String conversationId,
                                       boolean messageDeliveryReadEventsEnabled, boolean typingEventsEnabled,
                                       boolean isPrivateOneToOne, Bundle extras, boolean joiningAsObserver) {
        this.conversationId = conversationId;
        this.messageDeliveryReadEventsEnabled = messageDeliveryReadEventsEnabled;
        this.typingEventsEnabled = typingEventsEnabled;
        this.isPrivateOneToOne = isPrivateOneToOne;
        activeInConversation = true;
        scrollToMessageNeeded = extras.containsKey("scrollToMessageNeeded");
        if (scrollToMessageNeeded) {
            messageIdToScrollTo = extras.getString("messageId");
        }
        if (isPrivateOneToOne) {
            userName = extras.getString("userName");
            userImageUrl = extras.getString("userImageUrl");
            isOnline = extras.getBoolean("isOnline", false);
            if (!isOnline) {
                lastSeenAt = extras.getLong("lastSeenAt", 0);
            }
            userId = extras.getString("userId");

            checkIfMessagingEnabled();
        } else {
            conversationTitle = extras.getString("conversationTitle");
            conversationImageUrl = extras.getString("conversationImageUrl");
        }

        recordAudioUtil = new RecordAudioUtil();
        this.joiningAsObserver = joiningAsObserver;
        if (!joiningAsObserver) requestConversationDetails();
    }

    @Override
    public void downloadMedia(MessagesModel messagesModel, int messagePosition) {

        MediaDownloadOrUploadHelper.DownloadMediaQueryParseResponse downloadMediaQueryParseResponse =
                MediaDownloadOrUploadHelper.prepareDownloadMediaQuery(messagesModel,
                        downloadProgressListener);

        if (downloadMediaQueryParseResponse != null) {
            downloadMessagePositions.put(messagesModel.getMessageId(), messagePosition);
            isometrik.getRemoteUseCases()
                    .getDownloadUseCases()
                    .downloadMedia(downloadMediaQueryParseResponse.getDownloadMediaQuery().build(), (var1, var2) -> {

                        downloadMessagePositions.remove(messagesModel.getMessageId());
                        if (var1 != null) {
                            conversationMessagesView.onMediaDownloadedComplete(true, messagesModel.getMessageId(),
                                    downloadMediaQueryParseResponse.getDownloadSuccessMessage(), messagePosition,
                                    var1.getDownloadedMediaPath());
                        } else {
                            if (var2.getErrorMessage() == null) {
                                conversationMessagesView.onMediaDownloadedComplete(false,
                                        messagesModel.getMessageId(),
                                        downloadMediaQueryParseResponse.getDownloadFailureMessage(), messagePosition,
                                        null);
                            } else {
                                conversationMessagesView.onMediaDownloadedComplete(false,
                                        messagesModel.getMessageId(),
                                        MediaDownloadOrUploadHelper.parseDownloadMediaFailedMessage(
                                                messagesModel.getMessageTypeUi(), var2.getErrorMessage()),
                                        messagePosition, null);
                            }
                        }
                    });
        }
    }

    @Override
    public void cancelMediaDownload(MessagesModel messagesModel, int messagePosition) {
        isometrik.getRemoteUseCases()
                .getDownloadUseCases()
                .cancelMediaDownload(
                        new CancelMediaDownloadQuery.Builder().setMessageId(messagesModel.getMessageId()).build(), (var1, var2) -> {
                            downloadMessagePositions.remove(messagesModel.getMessageId());

                            if (var1 != null) {
                                conversationMessagesView.onMediaDownloadCanceled(true, messagesModel.getMessageId(),
                                        MediaDownloadOrUploadHelper.parseMediaDownloadOrUploadCanceledMessage(
                                                messagesModel.getMessageTypeUi(), true, true), messagePosition);
                            } else {
                                conversationMessagesView.onMediaDownloadCanceled(false,
                                        messagesModel.getMessageId(),
                                        MediaDownloadOrUploadHelper.parseMediaDownloadOrUploadCanceledMessage(
                                                messagesModel.getMessageTypeUi(), false, true), messagePosition);
                            }
                        });
    }

    @Override
    public void shareMessage(RemoteMessageTypes messageType, String parentMessageId,
                             OriginalReplyMessageUtil originalReplyMessageUtil, CustomMessageTypes customMessageType, String messageBody,
                             boolean encrypted, boolean showInConversation, Boolean sendPushNotification, Boolean updateUnreadCount, ArrayList<Attachment> attachments, JSONObject messageMetadata,
                             ArrayList<MentionedUser> mentionedUsers, MessageTypeUi messageTypeUi, ArrayList<String> mediaPaths, boolean uploadMediaRequired,
                             PresignedUrlMediaTypes presignedUrlMediaTypes, AttachmentMessageType attachmentMessageType) {

        MessagesModel messageModel = null;


        if (uploadMediaRequired) {
            Map<String, Map<String, String>> mediaDetailsMap = new HashMap<>();
            for (int i = 0; i < mediaPaths.size(); i++) {
                String localMessageId = String.valueOf(System.currentTimeMillis());
                Map<String, String> media = new HashMap<>();
                media.put("mediaPath", mediaPaths.get(i));
                media.put("localMessageId", localMessageId);
                String mediaId = PresignedUrlUtils.getMediaId(presignedUrlMediaTypes.getValue());
                mediaDetailsMap.put(mediaId, media);

                switch (messageTypeUi) {
                    case PHOTO_MESSAGE_SENT:
                    case VIDEO_MESSAGE_SENT:
                    case WHITEBOARD_MESSAGE_SENT: {
                        LocalMediaAttachmentHelper localMediaAttachmentHelper =
                                new LocalMediaAttachmentHelper(mediaPaths.get(i));
                        messageModel =
                                new MessagesModel(null, messageTypeUi, customMessageType, true, System.currentTimeMillis(),
                                        parentMessageId != null, localMediaAttachmentHelper.getSizeInMb(), false, false,
                                        false, true, localMediaAttachmentHelper.getThumbnailUrl(),
                                        localMediaAttachmentHelper.getMediaUrl(),
                                        localMediaAttachmentHelper.getMimeType(),
                                        localMediaAttachmentHelper.getMediaExtension(),
                                        IsometrikChatSdk.getInstance().getUserSession().getUserName(),
                                        IsometrikChatSdk.getInstance().getUserSession().getUserProfilePic(),
                                        new ArrayList<>(), false, localMessageId, originalReplyMessageUtil,
                                        localMediaAttachmentHelper.getFileSize(),
                                        localMediaAttachmentHelper.getMediaName(), mediaId, messageType.getValue(),
                                        messageMetadata, mediaPaths.get(i), false, false, conversationId, false);

                        break;
                    }
                    case FILE_MESSAGE_SENT: {

                        LocalMediaAttachmentHelper localMediaAttachmentHelper = new LocalMediaAttachmentHelper(mediaPaths.get(i));
                        messageModel =
                                new MessagesModel(null, messageTypeUi, customMessageType, true, System.currentTimeMillis(),
                                        parentMessageId != null, localMediaAttachmentHelper.getSizeInMb(), false, false,
                                        false, true, localMediaAttachmentHelper.getMediaUrl(),
                                        localMediaAttachmentHelper.getMediaName(),
                                        localMediaAttachmentHelper.getMimeType(),
                                        localMediaAttachmentHelper.getMediaExtension(),
                                        IsometrikChatSdk.getInstance().getUserSession().getUserName(),
                                        IsometrikChatSdk.getInstance().getUserSession().getUserProfilePic(),
                                        new ArrayList<>(), false, false, localMessageId, originalReplyMessageUtil,
                                        localMediaAttachmentHelper.getFileSize(), mediaId, messageType.getValue(),
                                        messageMetadata, false, false, conversationId, false);

                        break;
                    }
                    case AUDIO_MESSAGE_SENT: {
                        LocalMediaAttachmentHelper localMediaAttachmentHelper = new LocalMediaAttachmentHelper(mediaPaths.get(i));
                        messageModel =
                                new MessagesModel(null, messageTypeUi, customMessageType, true, System.currentTimeMillis(),
                                        parentMessageId != null, localMediaAttachmentHelper.getSizeInMb(), false, false,
                                        false, true, localMediaAttachmentHelper.getMediaUrl(),
                                        localMediaAttachmentHelper.getMediaName(),
                                        localMediaAttachmentHelper.getMimeType(),
                                        localMediaAttachmentHelper.getMediaExtension(),
                                        IsometrikChatSdk.getInstance().getUserSession().getUserName(),
                                        IsometrikChatSdk.getInstance().getUserSession().getUserProfilePic(),
                                        new ArrayList<>(), true, false, localMessageId, originalReplyMessageUtil,
                                        localMediaAttachmentHelper.getFileSize(), mediaId, messageType.getValue(),
                                        messageMetadata, false, false, conversationId, false);

                        break;
                    }
                }

                if (messageModel != null) {
                    messageModel.setMediaId(mediaId);
                    conversationMessagesView.addSentMessageInUILocally(messageModel, true);
                }
            }
            requestPresignedUrls(conversationId, messageType, parentMessageId, customMessageType.value, messageBody,
                    encrypted, showInConversation, sendPushNotification, updateUnreadCount, messageMetadata,
                    mentionedUsers, presignedUrlMediaTypes, mediaDetailsMap, attachmentMessageType,
                    messageTypeUi == MessageTypeUi.WHITEBOARD_MESSAGE_SENT, messageModel);
        } else {
            String localMessageId = String.valueOf(System.currentTimeMillis());
            List<String> searchableTags = null;
            String notificationTitle = IsometrikChatSdk.getInstance().getUserSession().getUserName();
            String notificationBody = "";

            switch (messageTypeUi) {

                case TEXT_MESSAGE_SENT:

                case REPLAY_MESSAGE_SENT: {
                    messageModel =
                            new MessagesModel(null, messageTypeUi, customMessageType, true, System.currentTimeMillis(),
                                    parentMessageId != null,
                                    TagUserUtil.parseMentionedUsers(messageBody, mentionedUsers, taggedUserCallback),
                                    IsometrikChatSdk.getInstance().getUserSession().getUserName(),
                                    IsometrikChatSdk.getInstance().getUserSession().getUserProfilePic(),
                                    new ArrayList<>(), false, localMessageId, originalReplyMessageUtil,
                                    messageType.getValue(), messageMetadata, false, false, conversationId, false);
                    searchableTags = SearchTagUtils.generateSearchTags(messageModel, null);
                    notificationBody = NotificationBodyUtils.getNotificationBody(messageModel, null);
                    break;
                }

                case LOCATION_MESSAGE_SENT: {
                    Attachment attachment = attachments.get(0);
                    messageModel =
                            new MessagesModel(null, messageTypeUi, customMessageType, true, System.currentTimeMillis(),
                                    parentMessageId != null, String.valueOf(attachment.getLatitude()),
                                    attachment.getTitle(), String.valueOf(attachment.getLongitude()),
                                    attachment.getAddress(),
                                    IsometrikChatSdk.getInstance().getUserSession().getUserName(),
                                    IsometrikChatSdk.getInstance().getUserSession().getUserProfilePic(),
                                    new ArrayList<>(), false, localMessageId, originalReplyMessageUtil,
                                    messageType.getValue(), messageMetadata, false, false, conversationId, false);
                    searchableTags = SearchTagUtils.generateSearchTags(messageModel, null);
                    notificationBody = NotificationBodyUtils.getNotificationBody(messageModel, null);
                    break;
                }

                case STICKER_MESSAGE_SENT: {
                    Attachment attachment = attachments.get(0);
                    messageModel =
                            new MessagesModel(null, messageTypeUi, customMessageType, true, System.currentTimeMillis(),
                                    parentMessageId != null, attachment.getStillUrl(), attachment.getMediaUrl(),
                                    IsometrikChatSdk.getInstance().getUserSession().getUserName(),
                                    IsometrikChatSdk.getInstance().getUserSession().getUserProfilePic(),
                                    new ArrayList<>(), true, false, localMessageId, originalReplyMessageUtil,
                                    attachment.getName(), attachment.getMediaId(), messageType.getValue(),
                                    messageMetadata, false, false, conversationId, false);
                    searchableTags = SearchTagUtils.generateSearchTags(messageModel, attachment.getName());
                    notificationBody = NotificationBodyUtils.getNotificationBody(messageModel, attachment.getName());
                    break;
                }

                case GIF_MESSAGE_SENT: {
                    Attachment attachment = attachments.get(0);
                    messageModel =
                            new MessagesModel(null, messageTypeUi, customMessageType, true, System.currentTimeMillis(),
                                    parentMessageId != null, attachment.getStillUrl(), attachment.getMediaUrl(),
                                    IsometrikChatSdk.getInstance().getUserSession().getUserName(),
                                    IsometrikChatSdk.getInstance().getUserSession().getUserProfilePic(),
                                    new ArrayList<>(), false, false, localMessageId, originalReplyMessageUtil,
                                    attachment.getName(), attachment.getMediaId(), messageType.getValue(),
                                    messageMetadata, false, false, conversationId, false);
                    searchableTags = SearchTagUtils.generateSearchTags(messageModel, attachment.getName());
                    notificationBody = NotificationBodyUtils.getNotificationBody(messageModel, attachment.getName());
                    break;
                }

                case CONTACT_MESSAGE_SENT: {
                    String contactName = "", contactIdentifier = "", contactImageUrl = "";
                    try {
                        contactName = messageMetadata.getJSONArray("contacts").getJSONObject(0).getString("contactName");
                        contactIdentifier = messageMetadata.getJSONArray("contacts").getJSONObject(0).getString("contactIdentifier");
                        contactImageUrl = messageMetadata.getJSONArray("contacts").getJSONObject(0).getString("contactImageUrl");
                    } catch (JSONException ignore) {
                        try {
                            contactName = messageMetadata.getString("contactName");
                            contactIdentifier = messageMetadata.getString("contactIdentifier");
                            contactImageUrl = messageMetadata.getString("contactImageUrl");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    messageModel =
                            new MessagesModel(null, messageTypeUi, customMessageType, true, System.currentTimeMillis(),
                                    parentMessageId != null, contactName,
                                    contactIdentifier,
                                    contactImageUrl,
                                    IsometrikChatSdk.getInstance().getUserSession().getUserName(),
                                    IsometrikChatSdk.getInstance().getUserSession().getUserProfilePic(),
                                    new ArrayList<>(), false, localMessageId, originalReplyMessageUtil,
                                    messageType.getValue(), messageMetadata, false, false, conversationId, false);

                    searchableTags = SearchTagUtils.generateSearchTags(messageModel, null);
                    notificationBody = NotificationBodyUtils.getNotificationBody(messageModel, null);
                    break;
                }
            }
            if (messageModel != null) {

                conversationMessagesView.addSentMessageInUILocally(messageModel, false);
            }

            SendMessageQuery.Builder sendMessageQuery = new SendMessageQuery.Builder().setUserToken(userToken)
                    .setConversationId(conversationId)
                    .setEncrypted(encrypted)
                    .setBody(messageBody)
                    .setMessageType(messageType.getValue())
                    .setShowInConversation(showInConversation)
                    .setNotificationTitle(notificationTitle)
                    .setNotificationBody(notificationBody)
                    .setDeviceId(IsometrikChatSdk.getInstance().getUserSession().getDeviceId());

            if (attachments != null) {
                sendMessageQuery.setAttachments(attachments);
            }
            if (customMessageType != null) {
                sendMessageQuery.setCustomType(customMessageType.value);
            }
            if (mentionedUsers != null) {
                sendMessageQuery.setMentionedUsers(mentionedUsers);
            }
            if (messageMetadata != null) {
                sendMessageQuery.setMetaData(messageMetadata);
            }
            if (parentMessageId != null) {
                sendMessageQuery.setParentMessageId(parentMessageId);
            }
            if (sendPushNotification != null && updateUnreadCount != null) {
                sendMessageQuery.setEventForMessage(new EventForMessage(sendPushNotification, updateUnreadCount));
            }
            if (searchableTags != null && !searchableTags.isEmpty()) {
                sendMessageQuery.setSearchableTags(searchableTags);
            }
            isometrik.getRemoteUseCases()
                    .getMessageUseCases()
                    .sendMessage(sendMessageQuery.build(), (var1, var2) -> {
                        if (var1 != null) {

                            conversationMessagesView.onMessageSentSuccessfully(localMessageId, var1.getMessageId(), null, null);
                        } else {

                            conversationMessagesView.onFailedToSendMessage(localMessageId, var2.getErrorMessage());
                        }
                    });
        }
    }

    @Override
    public int verifyMessagePositionInList(String messageId, int position, ArrayList<MessagesModel> messages) {

        if (position < messages.size()) {

            if (messages.get(position).getMessageId().equals(messageId)) {
                return position;
            } else {
                return verifyMessagePosition(messageId, messages);
            }
        } else {
            return verifyMessagePosition(messageId, messages);
        }
    }

    @Override
    public int verifyUnsentMessagePositionInList(String localMessageId, int position, ArrayList<MessagesModel> messages) {

        if (position < messages.size()) {

            if (messages.get(position).isSentMessage() && !messages.get(position).isMessageSentSuccessfully() && messages.get(position)
                    .getLocalMessageId()
                    .equals(localMessageId)) {
                return position;
            } else {
                return verifyUnsentMessagePosition(localMessageId, messages);
            }
        } else {
            return verifyUnsentMessagePosition(localMessageId, messages);
        }
    }

    private void requestPresignedUrls(String conversationId, RemoteMessageTypes messageType, String parentMessageId,
                                      String customType, String messageBody, boolean encrypted, boolean showInConversation,
                                      Boolean sendPushNotification, Boolean updateUnreadCount, JSONObject messageMetadata,
                                      ArrayList<MentionedUser> mentionedUsers, PresignedUrlMediaTypes presignedUrlMediaTypes,
                                      Map<String, Map<String, String>> mediaDetailsMap, AttachmentMessageType attachmentMessageType,
                                      boolean isWhiteboard, MessagesModel messageModel) {

        List<FetchAttachmentPresignedUrlsQuery.PresignUrlRequest> presignedUrlRequests = new ArrayList<>();

        // Collect presigned URL requests
        for (Map.Entry<String, Map<String, String>> entry : mediaDetailsMap.entrySet()) {
            String mediaId = entry.getKey();
            String mediaPath = entry.getValue().get("mediaPath");

            FetchAttachmentPresignedUrlsQuery.PresignUrlRequest request = new FetchAttachmentPresignedUrlsQuery.PresignUrlRequest(
                    mediaId, PresignedUrlUtils.getMediaName(mediaPath), presignedUrlMediaTypes.getValue()
            );
            presignedUrlRequests.add(request);
        }

        FetchAttachmentPresignedUrlsQuery fetchQuery = new FetchAttachmentPresignedUrlsQuery.Builder()
                .setUserToken(userToken)
                .setConversationId(conversationId)
                .setPresignUrlRequests(presignedUrlRequests)
                .build();

        isometrik.getRemoteUseCases()
                .getMessageUseCases()
                .fetchAttachmentPresignedUrls(fetchQuery, (result, error) -> {
                    if (result != null) {
                        handleMediaUpload(result.getPresignedUrls(), conversationId, messageType, parentMessageId,
                                customType, messageBody, encrypted, showInConversation, sendPushNotification,
                                updateUnreadCount, messageMetadata, mentionedUsers, mediaDetailsMap,
                                attachmentMessageType, isWhiteboard, messageModel);
                    } else {
                        conversationMessagesView.onError(error.getErrorMessage());
                    }
                });
    }

    /**
     * Handles media upload, allowing custom upload logic if registered.
     */
    private void handleMediaUpload(List<FetchAttachmentPresignedUrlsResult.PresignedUrl> presignedUrls,
                                   String conversationId, RemoteMessageTypes messageType, String parentMessageId,
                                   String customType, String messageBody, boolean encrypted, boolean showInConversation,
                                   Boolean sendPushNotification, Boolean updateUnreadCount, JSONObject messageMetadata,
                                   ArrayList<MentionedUser> mentionedUsers, Map<String, Map<String, String>> mediaDetailsMap,
                                   AttachmentMessageType attachmentMessageType, boolean isWhiteboard, MessagesModel messageModel) {

        for (FetchAttachmentPresignedUrlsResult.PresignedUrl presignedUrl : presignedUrls) {
            Map<String, String> mediaDetailMap = mediaDetailsMap.get(presignedUrl.getMediaId());
            mediaDetailMap.put("mediaUrl", presignedUrl.getMediaUrl());
            mediaDetailMap.put("thumbnailUrl", presignedUrl.getThumbnailUrl());

            if (CustomUploadHandler.isRegistered()) {
                // Use custom upload flow from base module
                CustomUploadHandler.uploadMedia(presignedUrl.getMediaId(), mediaDetailMap.get("mediaPath"), uploadedMediaResponse -> {
                    if (uploadedMediaResponse != null) {
                        // Use the uploaded URL provided by the base module
                        mediaDetailMap.put("mediaUrl", uploadedMediaResponse.getMediaUrl());
                        mediaDetailMap.put("thumbnailUrl", uploadedMediaResponse.getThumbnailUrl());
                        mediaDetailsMap.put(presignedUrl.getMediaId(), mediaDetailMap);


                        // Continue with sending the message after upload
                        sendMessageAfterUpload(uploadedMediaResponse,conversationId, messageType, parentMessageId,
                                customType, messageBody, encrypted, showInConversation, sendPushNotification,
                                updateUnreadCount, messageMetadata, mentionedUsers,mediaDetailsMap,
                                attachmentMessageType, isWhiteboard, messageModel);
                    } else {
                        conversationMessagesView.onFailedToSendMessage(
                                mediaDetailMap.get("localMessageId"), "Custom Upload Failed");
                    }
                });

            } else {
                // Use default presigned URL upload
                UploadMediaQuery uploadMediaQuery = new UploadMediaQuery.Builder()
                        .setMediaId(presignedUrl.getMediaId())
                        .setMediaPath(mediaDetailMap.get("mediaPath"))
                        .setPresignedUrl(presignedUrl.getMediaPresignedUrl())
                        .setUploadProgressListener(uploadProgressListener)
                        .setLocalMessageId(mediaDetailMap.get("localMessageId"))
                        .build();

                isometrik.getRemoteUseCases()
                        .getUploadUseCases()
                        .uploadMedia(uploadMediaQuery, (uploadResult, uploadError) -> {
                            if (uploadResult != null) {
                                UploadedMediaResponse uploadedMediaResponse = new UploadedMediaResponse(
                                        uploadResult.getMediaId(), mediaDetailMap.get("mediaUrl"), mediaDetailMap.get("thumbnailUrl")
                                );
                                sendMessageAfterUpload(uploadedMediaResponse, conversationId, messageType, parentMessageId,
                                        customType, messageBody, encrypted, showInConversation, sendPushNotification,
                                        updateUnreadCount, messageMetadata, mentionedUsers, mediaDetailsMap,
                                        attachmentMessageType, isWhiteboard, messageModel);
                            } else {
                                conversationMessagesView.onFailedToSendMessage(mediaDetailMap.get("localMessageId"),
                                        uploadError.getErrorMessage());
                            }
                        });
            }

        }
    }

    /**
     * Sends the message after media upload is complete.
     */
    private void sendMessageAfterUpload(UploadedMediaResponse uploadedMediaResponse, String conversationId,
                                        RemoteMessageTypes messageType, String parentMessageId, String customType,
                                        String messageBody, boolean encrypted, boolean showInConversation,
                                        Boolean sendPushNotification, Boolean updateUnreadCount, JSONObject messageMetadata,
                                        ArrayList<MentionedUser> mentionedUsers, Map<String, Map<String, String>> mediaDetailsMap,
                                        AttachmentMessageType attachmentMessageType, boolean isWhiteboard, MessagesModel messageModel) {

        Map<String, String> mediaMap = mediaDetailsMap.get(uploadedMediaResponse.getMediaId());

        SendMessageQuery.Builder sendMessageQuery = new SendMessageQuery.Builder()
                .setUserToken(userToken)
                .setConversationId(conversationId)
                .setEncrypted(encrypted)
                .setBody(messageBody)
                .setMessageType(messageType.getValue())
                .setShowInConversation(showInConversation)
                .setNotificationTitle(IsometrikChatSdk.getInstance().getUserSession().getUserName())
                .setNotificationBody(NotificationBodyUtils.getNotificationBody(messageModel, null))
                .setDeviceId(IsometrikChatSdk.getInstance().getUserSession().getDeviceId());

        Attachment mediaAttachment = PrepareAttachmentHelper.prepareMediaAttachment(uploadedMediaResponse.getMediaId(),
                uploadedMediaResponse.getMediaUrl(), uploadedMediaResponse.getThumbnailUrl(), mediaMap.get("mediaPath"),
                attachmentMessageType);

        sendMessageQuery.setAttachments(Collections.singletonList(mediaAttachment));

        if (customType != null) {
            sendMessageQuery.setCustomType(customType);
        }
        if (mentionedUsers != null) {
            sendMessageQuery.setMentionedUsers(mentionedUsers);
        }
        if (messageMetadata != null) {
            sendMessageQuery.setMetaData(messageMetadata);
        }
        if (parentMessageId != null) {
            sendMessageQuery.setParentMessageId(parentMessageId);
        }
        if (sendPushNotification != null && updateUnreadCount != null) {
            sendMessageQuery.setEventForMessage(new EventForMessage(sendPushNotification, updateUnreadCount));
        }

        List<String> searchableTags = SearchTagUtils.generateSearchTags(mediaAttachment, isWhiteboard);

        if (!searchableTags.isEmpty()) {
            sendMessageQuery.setSearchableTags(searchableTags);
        }

        isometrik.getRemoteUseCases()
                .getMessageUseCases()
                .sendMessage(sendMessageQuery.build(), (response, error) -> {
                    if (response != null) {
                        uploadMessagePositions.remove(mediaMap.get("localMessageId"));
                        conversationMessagesView.onMessageSentSuccessfully(mediaMap.get("localMessageId"),
                                response.getMessageId(),
                                uploadedMediaResponse.getMediaUrl(),
                                uploadedMediaResponse.getThumbnailUrl());
                    } else {
                        conversationMessagesView.onFailedToSendMessage(mediaMap.get("localMessageId"), error.getErrorMessage());
                    }
                });
    }



    private boolean messagesRefreshRequired;
    private final ConnectionEventCallback connectionEventCallback = new ConnectionEventCallback() {
        @Override
        public void disconnected(@NotNull Isometrik isometrik,
                                 @NotNull DisconnectEvent disconnectEvent) {
            messagesRefreshRequired = true;
            conversationMessagesView.connectionStateChanged(false);
        }

        @Override
        public void connected(@NotNull Isometrik isometrik, @NotNull ConnectEvent connectEvent) {
            if (messagesRefreshRequired) {
                messagesRefreshRequired = false;

                fetchMessages(0, true, false, null, true);
            }
            conversationMessagesView.connectionStateChanged(true);
        }

        @Override
        public void connectionFailed(@NotNull Isometrik isometrik,
                                     @NotNull IsometrikError isometrikError) {
            conversationMessagesView.connectionStateChanged(false);
        }

        @Override
        public void failedToConnect(@NotNull Isometrik isometrik,
                                    @NotNull ConnectionFailedEvent connectionFailedEvent) {
            messagesRefreshRequired = true;
            conversationMessagesView.connectionStateChanged(false);
        }
    };

    @Override
    public void deleteMessageForSelf(ArrayList<String> messageIds, boolean multipleMessagesSelected) {


        if (multipleMessagesSelected) {
            messageIds = multipleMessagesUtil.getSelectedMessageIds();
        }
        RemoveMessagesForSelfQuery.Builder removeMessageForSelfQuery =
                new RemoveMessagesForSelfQuery.Builder().setUserToken(userToken)
                        .setConversationId(conversationId)
                        .setMessageIds(messageIds);

        ArrayList<String> finalMessageIds = messageIds;
        isometrik.getRemoteUseCases()
                .getMessageUseCases()
                .removeMessagesForSelf(removeMessageForSelfQuery.build(), (var1, var2) -> {

                    if (var1 != null) {

                        conversationMessagesView.onMessageDeletedSuccessfully(finalMessageIds);
                        if (multipleMessagesSelected) {
                            multipleMessagesUtil.removeMessages(finalMessageIds);
                            conversationMessagesView.onMessageSelectionStatus(multipleMessagesUtil);
                        }
                    } else {
                        conversationMessagesView.onError(var2.getErrorMessage());
                    }
                });
    }

    @Override
    public void deleteMessageForEveryone(ArrayList<String> messageIds,
                                         boolean multipleMessagesSelected) {
        if (multipleMessagesSelected) {
            messageIds = multipleMessagesUtil.getSelectedMessageIds();
        }
        RemoveMessagesForEveryoneQuery.Builder removeMessageForEveryoneQuery =
                new RemoveMessagesForEveryoneQuery.Builder().setUserToken(userToken)
                        .setConversationId(conversationId)
                        .setMessageIds(messageIds);

        ArrayList<String> finalMessageIds = messageIds;
        isometrik.getRemoteUseCases()
                .getMessageUseCases()
                .removeMessagesForEveryone(removeMessageForEveryoneQuery.build(), (var1, var2) -> {

                    if (var1 != null) {

                        conversationMessagesView.onMessageDeletedSuccessfully(finalMessageIds);
                        if (multipleMessagesSelected) {
                            multipleMessagesUtil.removeMessages(finalMessageIds);
                            conversationMessagesView.onMessageSelectionStatus(multipleMessagesUtil);
                        }
                    } else {
                        conversationMessagesView.onError(var2.getErrorMessage());
                    }
                });
    }

    @Override
    public void copyText() {
        conversationMessagesView.onTextCopyRequest(multipleMessagesUtil.copyText());
    }

    @Override
    public void updateMessageSelectionStatus(MessagesModel messagesModel, boolean selected) {
        if (selected) {
            multipleMessagesUtil.addMessage(messagesModel);
        } else {
            multipleMessagesUtil.removeMessage(messagesModel.getMessageId());
        }
        conversationMessagesView.onMessageSelectionStatus(multipleMessagesUtil);
    }

    @Override
    public void markMessagesAsRead() {
        if (messageDeliveryReadEventsEnabled && activeInConversation && !joiningAsObserver) {
            MarkMultipleMessagesAsReadQuery.Builder markMultipleMessagesAsReadQuery =
                    new MarkMultipleMessagesAsReadQuery.Builder().setUserToken(userToken)
                            .setConversationId(conversationId);
            isometrik.getRemoteUseCases()
                    .getMessageUseCases()
                    .markMultipleMessagesAsRead(markMultipleMessagesAsReadQuery.build(), (var1, var2) -> {

                    });
        }
    }

    @Override
    public void sendTypingMessage() {
        if (typingEventsEnabled) {
            SendTypingMessageQuery.Builder sendTypingMessageQuery =
                    new SendTypingMessageQuery.Builder().setUserToken(userToken)
                            .setConversationId(conversationId);

            isometrik.getRemoteUseCases()
                    .getMessageUseCases()
                    .sendTypingMessage(sendTypingMessageQuery.build(), (var1, var2) -> {

                    });
        }
    }

    @Override
    public void cleanupSelectedMessages() {
        multipleMessagesUtil.cleanup();
    }

    @Override
    public void fetchMessages(int skip, boolean refreshRequest, boolean isSearchRequest,
                              String searchTag, boolean onReconnect) {

        isLoading = true;

        if (skip == 0) {
            isLastPage = false;
            offset = 0;
        }

        this.isSearchRequest = isSearchRequest;
        this.searchTag = isSearchRequest ? searchTag : null;

        FetchMessagesQuery.Builder fetchMessagesQuery = new FetchMessagesQuery.Builder().setUserToken(userToken)
                .setConversationId(conversationId)
                .setLimit(CONVERSATION_MESSAGES_PAGE_SIZE)
                .setSkip(skip);

        if (isSearchRequest && searchTag != null) {
            fetchMessagesQuery.setSearchTag(searchTag);
        }
        isometrik.getRemoteUseCases()
                .getMessageUseCases()
                .fetchMessages(fetchMessagesQuery.build(), (var1, var2) -> {
                    isLoading = false;
                    if (var1 != null) {

                        ArrayList<MessagesModel> messageModels = new ArrayList<>();
                        MessagesModel messageModel;
                        ArrayList<Message> messages = var1.getMessages();
                        Message message;
                        boolean messageIdToScrollToExists = false;

                        for (int i = 0; i < messages.size(); i++) {
                            message = messages.get(i);

                            if (message.getConversationStatusMessage() != null) {
                                String conversationActionMessage =
                                        ConversationActionMessageUtil.parseConversationActionMessage(message);

                                if (conversationActionMessage != null) {

                                    JSONObject metadata = null;
                                    if (message.getConversationDetails() != null) {
                                        metadata = message.getConversationDetails().getMetaData();

                                    }
                                    messageModel =
                                            new MessagesModel(conversationActionMessage, message.getMessageId(), message.getSentAt(), false, MessageTypeUi.CONVERSATION_ACTION_MESSAGE, metadata);
                                    messageModels.add(0, messageModel);
                                }
                            } else {
                                messageModel =
                                        ConversationAttachmentMessageUtil.prepareMessageAttachmentModel(message,
                                                taggedUserCallback);
                                if (messageModel != null) {
                                    messageModels.add(0, messageModel);
                                }
                                if (scrollToMessageNeeded) {
                                    if (message.getMessageId().equals(messageIdToScrollTo)) {
                                        messageIdToScrollToExists = true;
                                    }
                                }
                            }
                        }
                        if (messages.size() < CONVERSATION_MESSAGES_PAGE_SIZE) {

                            isLastPage = true;
                        }

                        boolean hideSearchOverlay =
                                scrollToMessageNeeded && (isLastPage || messageIdToScrollToExists);
                        if (hideSearchOverlay) {
                            scrollToMessageNeeded = false;
                        }
                        conversationMessagesView.addMessagesInUI(messageModels, refreshRequest,
                                hideSearchOverlay, messageIdToScrollToExists, onReconnect);

                        if (scrollToMessageNeeded && !messageIdToScrollToExists) {
                            fetchMessagesOnScroll();
                        }
                        int count =
                                MarkMessagesAsReadUtil.fetchMessagesToBeMarkedAsReadCount(messages, userToken,
                                        IsometrikChatSdk.getInstance().getUserSession().getUserId());
                        if (count > 0) {
                            markMessagesAsRead();
                        }
                    } else {
                        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
                            if (refreshRequest) {
                                //No messages found
                                scrollToMessageNeeded = false;
                                conversationMessagesView.addMessagesInUI(new ArrayList<>(), true, true, false,
                                        onReconnect);
                            } else {
                                if (scrollToMessageNeeded) {
                                    scrollToMessageNeeded = false;
                                    conversationMessagesView.messageToScrollToNotFound();
                                }
                                isLastPage = true;
                            }
                        } else if (var2.getHttpResponseCode() == 400) {
                            conversationMessagesView.onFailedToJoinAsObserverOrFetchMessagesOrConversationDetails(
                                    var2.getErrorMessage());
                        } else {
                            conversationMessagesView.onError(var2.getErrorMessage());
                        }
                    }
                });
    }

    private final ConversationEventCallback conversationEventCallback =
            new ConversationEventCallback() {
                @Override
                public void conversationCleared(@NotNull Isometrik isometrik,
                                                @NotNull ClearConversationEvent clearConversationEvent) {

                    if (clearConversationEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.onConversationCleared();
                        conversationMessagesView.addMessageInUI(
                                RealtimeMessageUtil.parseClearConversationEvent(clearConversationEvent));
                    }
                }

                @Override
                public void conversationDeletedLocally(@NotNull Isometrik isometrik,
                                                       @NotNull DeleteConversationLocallyEvent deleteConversationLocallyEvent) {
                    if (deleteConversationLocallyEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.closeConversation();
                    }
                }

                @Override
                public void conversationImageUpdated(@NotNull Isometrik isometrik,
                                                     @NotNull UpdateConversationImageEvent updateConversationImageEvent) {
                    MessagesModel messagesModel =
                            RealtimeMessageUtil.parseUpdateConversationImageEvent(updateConversationImageEvent);
                    if (updateConversationImageEvent.getConversationId().equals(conversationId)) {

                        conversationImageUrl = updateConversationImageEvent.getConversationImageUrl();
                        conversationMessagesView.addMessageInUI(messagesModel);
                        if (!updateConversationImageEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {

                        if (!updateConversationImageEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(
                                    updateConversationImageEvent.getConversationId(),
                                    updateConversationImageEvent.getConversationTitle(),
                                    messagesModel.getConversationActionMessage(),
                                    updateConversationImageEvent.isPrivateOneToOne(), null, false,
                                    updateConversationImageEvent.getConversationImageUrl(),
                                    updateConversationImageEvent.getUserProfileImageUrl(),
                                    updateConversationImageEvent.getUserName());
                        }
                    }
                }

                @Override
                public void conversationSettingsUpdated(@NotNull Isometrik isometrik,
                                                        @NotNull UpdateConversationSettingsEvent updateConversationSettingsEvent) {

                    MessagesModel messagesModel = RealtimeMessageUtil.parseUpdateConversationSettingsEvent(
                            updateConversationSettingsEvent);
                    if (updateConversationSettingsEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.addMessageInUI(messagesModel);
                        if (updateConversationSettingsEvent.getConfig().getReadEvents() != null) {
                            messageDeliveryReadEventsEnabled =
                                    updateConversationSettingsEvent.getConfig().getReadEvents();
                        }
                        if (updateConversationSettingsEvent.getConfig().getTypingEvents() != null) {
                            typingEventsEnabled = updateConversationSettingsEvent.getConfig().getTypingEvents();
                        }
                        if (!updateConversationSettingsEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {
                        if (!updateConversationSettingsEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(
                                    updateConversationSettingsEvent.getConversationId(),
                                    updateConversationSettingsEvent.isPrivateOneToOne()
                                            ? updateConversationSettingsEvent.getUserName()
                                            : updateConversationSettingsEvent.getConversationTitle(),
                                    messagesModel.getConversationActionMessage(),
                                    updateConversationSettingsEvent.isPrivateOneToOne(), null, false,
                                    updateConversationSettingsEvent.isPrivateOneToOne()
                                            ? updateConversationSettingsEvent.getUserProfileImageUrl()
                                            : updateConversationSettingsEvent.getConversationImageUrl(),
                                    updateConversationSettingsEvent.getUserProfileImageUrl(),
                                    updateConversationSettingsEvent.getUserName());
                        }
                    }
                }

                @Override
                public void conversationDetailsUpdated(@NotNull Isometrik isometrik,
                                                       @NotNull UpdateConversationDetailsEvent updateConversationDetailsEvent) {

                    MessagesModel messagesModel = RealtimeMessageUtil.parseUpdateConversationDetailsEvent(
                            updateConversationDetailsEvent);
                    if (updateConversationDetailsEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.addMessageInUI(messagesModel);
                        if (!updateConversationDetailsEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {
                        if (!updateConversationDetailsEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(
                                    updateConversationDetailsEvent.getConversationId(),
                                    updateConversationDetailsEvent.isPrivateOneToOne()
                                            ? updateConversationDetailsEvent.getUserName()
                                            : updateConversationDetailsEvent.getConversationTitle(),
                                    messagesModel.getConversationActionMessage(),
                                    updateConversationDetailsEvent.isPrivateOneToOne(), null, false,
                                    updateConversationDetailsEvent.isPrivateOneToOne()
                                            ? updateConversationDetailsEvent.getUserProfileImageUrl()
                                            : updateConversationDetailsEvent.getConversationImageUrl(),
                                    updateConversationDetailsEvent.getUserProfileImageUrl(),
                                    updateConversationDetailsEvent.getUserName());
                        }
                    }
                }

                @Override
                public void conversationTitleUpdated(@NotNull Isometrik isometrik,
                                                     @NotNull UpdateConversationTitleEvent updateConversationTitleEvent) {
                    MessagesModel messagesModel =
                            RealtimeMessageUtil.parseUpdateConversationTitleEvent(updateConversationTitleEvent);
                    if (updateConversationTitleEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.onConversationTitleUpdated(
                                updateConversationTitleEvent.getConversationTitle());
                        conversationTitle = updateConversationTitleEvent.getConversationTitle();
                        conversationMessagesView.addMessageInUI(messagesModel);
                        if (!updateConversationTitleEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {
                        if (!updateConversationTitleEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(
                                    updateConversationTitleEvent.getConversationId(),
                                    updateConversationTitleEvent.getConversationTitle(),
                                    messagesModel.getConversationActionMessage(),
                                    updateConversationTitleEvent.isPrivateOneToOne(), null, false,
                                    updateConversationTitleEvent.getConversationImageUrl(),
                                    updateConversationTitleEvent.getUserProfileImageUrl(),
                                    updateConversationTitleEvent.getUserName());
                        }
                    }
                }

                @Override
                public void conversationCreated(@NotNull Isometrik isometrik,
                                                @NotNull CreateConversationEvent createConversationEvent) {
                    //Added to avoid race conditions although not needed
                    if (!createConversationEvent.getConversationId().equals(conversationId)) {

                        if (!createConversationEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            conversationMessagesView.showMessageNotification(
                                    createConversationEvent.getConversationId(),
                                    createConversationEvent.getConversationDetails().isPrivateOneToOne()
                                            ? createConversationEvent.getUserName()
                                            : createConversationEvent.getConversationDetails().getConversationTitle(),
                                    IsometrikChatSdk.getInstance()
                                            .getContext()
                                            .getString(R.string.ism_created_conversation,
                                                    createConversationEvent.getUserName()),
                                    createConversationEvent.getConversationDetails().isPrivateOneToOne(), null, false,
                                    createConversationEvent.getConversationDetails().isPrivateOneToOne()
                                            ? createConversationEvent.getUserProfileImageUrl()
                                            : createConversationEvent.getConversationDetails().getConversationImageUrl(),
                                    createConversationEvent.getUserProfileImageUrl(),
                                    createConversationEvent.getUserName());
                        }
                    }
                }
            };

    private final UserEventCallback userEventCallback = new UserEventCallback() {
        @Override
        public void userBlocked(@NotNull Isometrik isometrik, @NotNull BlockUserEvent blockUserEvent) {

            if (isPrivateOneToOne) {
                if (blockUserEvent.getInitiatorId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    if (blockUserEvent.getOpponentId().equals(userId)) {
                        conversationMessagesView.onMessagingStatusChanged(true);
                    }
                } else if (blockUserEvent.getInitiatorId().equals(userId)) {
                    conversationMessagesView.onMessagingStatusChanged(true);
                }
            }
        }

        @Override
        public void userUnblocked(@NotNull Isometrik isometrik,
                                  @NotNull UnblockUserEvent unblockUserEvent) {
            if (isPrivateOneToOne) {
                if (unblockUserEvent.getInitiatorId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    if (unblockUserEvent.getOpponentId().equals(userId)) {
                        checkIfMessagingEnabled();
                    }
                } else if (unblockUserEvent.getInitiatorId().equals(userId)) {
                    checkIfMessagingEnabled();
                }
            }
        }

        @Override
        public void userUpdated(@NotNull Isometrik isometrik,
                                @NotNull UpdateUserEvent updateUserEvent) {
            //TODO Nothing
        }

        @Override
        public void userDeleted(@NotNull Isometrik isometrik,
                                @NotNull DeleteUserEvent deleteUserEvent) {
            //TODO Nothing
        }
    };

    private final MembershipControlEventCallback membershipControlEventCallback =
            new MembershipControlEventCallback() {
                @Override
                public void observerJoined(@NotNull Isometrik isometrik,
                                           @NotNull ObserverJoinEvent observerJoinEvent) {
                    MessagesModel messagesModel =
                            RealtimeMessageUtil.parseJoinAsObserverEvent(observerJoinEvent);
                    if (observerJoinEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.addMessageInUI(messagesModel);
                        if (!observerJoinEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {
                        if (!observerJoinEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(
                                    observerJoinEvent.getConversationId(), observerJoinEvent.getConversationTitle(),
                                    messagesModel.getConversationActionMessage(),
                                    observerJoinEvent.isPrivateOneToOne(), null, false,
                                    observerJoinEvent.getConversationImageUrl(),
                                    observerJoinEvent.getUserProfileImageUrl(), observerJoinEvent.getUserName());
                        }
                    }
                }

                @Override
                public void observerLeft(@NotNull Isometrik isometrik,
                                         @NotNull ObserverLeaveEvent observerLeaveEvent) {
                    MessagesModel messagesModel =
                            RealtimeMessageUtil.parseLeaveAsObserverEvent(observerLeaveEvent);
                    if (observerLeaveEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.addMessageInUI(messagesModel);
                        if (!observerLeaveEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {
                        if (!observerLeaveEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(observerLeaveEvent.getConversationId(),
                                    observerLeaveEvent.getConversationTitle(),
                                    messagesModel.getConversationActionMessage(), observerLeaveEvent.isPrivateOneToOne(),
                                    null, false, observerLeaveEvent.getConversationImageUrl(),
                                    observerLeaveEvent.getUserProfileImageUrl(), observerLeaveEvent.getUserName());
                        }
                    }
                }

                @Override
                public void adminAdded(@NotNull Isometrik isometrik, @NotNull AddAdminEvent addAdminEvent) {
                    MessagesModel messagesModel = RealtimeMessageUtil.parseAddAdminEvent(addAdminEvent);
                    if (addAdminEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.addMessageInUI(messagesModel);
                        if (!addAdminEvent.getInitiatorId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {
                        if (!addAdminEvent.getInitiatorId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(addAdminEvent.getConversationId(),
                                    addAdminEvent.getConversationTitle(), messagesModel.getConversationActionMessage(),
                                    addAdminEvent.isPrivateOneToOne(), null, false,
                                    addAdminEvent.getConversationImageUrl(), addAdminEvent.getInitiatorProfileImageUrl(),
                                    addAdminEvent.getInitiatorName());
                        }
                    }
                }

                @Override
                public void membersAdded(@NotNull Isometrik isometrik, @NotNull AddMembersEvent addMembersEvent) {
                    MessagesModel messagesModel = RealtimeMessageUtil.parseAddMembersEvent(addMembersEvent);
                    if (addMembersEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.addMessageInUI(messagesModel);
                        conversationMessagesView.updateParticipantsCount(addMembersEvent.getMembersCount());
                        if (!addMembersEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {
                        if (!addMembersEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(addMembersEvent.getConversationId(),
                                    addMembersEvent.getConversationTitle(), messagesModel.getConversationActionMessage(),
                                    addMembersEvent.isPrivateOneToOne(), null, false,
                                    addMembersEvent.getConversationImageUrl(), addMembersEvent.getUserProfileImageUrl(),
                                    addMembersEvent.getUserName());
                        }
                    }
                }

                @Override
                public void conversationJoined(@NotNull Isometrik isometrik, @NotNull JoinConversationEvent joinConversationEvent) {
                    MessagesModel messagesModel =
                            RealtimeMessageUtil.parseJoinConversationEvent(joinConversationEvent);
                    if (joinConversationEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.addMessageInUI(messagesModel);
                        conversationMessagesView.updateParticipantsCount(joinConversationEvent.getMembersCount());
                        if (!joinConversationEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {
                        if (!joinConversationEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(
                                    joinConversationEvent.getConversationId(),
                                    joinConversationEvent.getConversationTitle(),
                                    messagesModel.getConversationActionMessage(),
                                    joinConversationEvent.isPrivateOneToOne(), null, false,
                                    joinConversationEvent.getConversationImageUrl(),
                                    joinConversationEvent.getUserProfileImageUrl(), joinConversationEvent.getUserName());
                        }
                    }
                }

                @Override
                public void conversationLeft(@NotNull Isometrik isometrik, @NotNull LeaveConversationEvent leaveConversationEvent) {
                    MessagesModel messagesModel =
                            RealtimeMessageUtil.parseLeaveConversationEvent(leaveConversationEvent);
                    if (leaveConversationEvent.getConversationId().equals(conversationId)) {
                        if (leaveConversationEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            conversationMessagesView.closeConversation();
                        } else {
                            conversationMessagesView.addMessageInUI(messagesModel);

                            conversationMessagesView.updateParticipantsCount(
                                    leaveConversationEvent.getMembersCount());

                            updateLastReadInConversation();
                        }
                    } else {
                        if (!leaveConversationEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(
                                    leaveConversationEvent.getConversationId(),
                                    leaveConversationEvent.getConversationTitle(),
                                    messagesModel.getConversationActionMessage(),
                                    leaveConversationEvent.isPrivateOneToOne(), null, false,
                                    leaveConversationEvent.getConversationImageUrl(),
                                    leaveConversationEvent.getUserProfileImageUrl(),
                                    leaveConversationEvent.getUserName());
                        }
                    }
                }

                @Override
                public void adminRemoved(@NotNull Isometrik isometrik, @NotNull RemoveAdminEvent removeAdminEvent) {
                    MessagesModel messagesModel = RealtimeMessageUtil.parseRemoveAdminEvent(removeAdminEvent);
                    if (removeAdminEvent.getConversationId().equals(conversationId)) {
                        conversationMessagesView.addMessageInUI(messagesModel);
                        if (!removeAdminEvent.getInitiatorId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                            updateLastReadInConversation();
                        }
                    } else {
                        if (!removeAdminEvent.getInitiatorId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(removeAdminEvent.getConversationId(),
                                    removeAdminEvent.getConversationTitle(), messagesModel.getConversationActionMessage(),
                                    removeAdminEvent.isPrivateOneToOne(), null, false,
                                    removeAdminEvent.getConversationImageUrl(),
                                    removeAdminEvent.getInitiatorProfileImageUrl(), removeAdminEvent.getInitiatorName());
                        }
                    }
                }

                @Override
                public void membersRemoved(@NotNull Isometrik isometrik, @NotNull RemoveMembersEvent removeMembersEvent) {
                    MessagesModel messagesModel = RealtimeMessageUtil.parseRemoveMembersEvent(removeMembersEvent);
                    if (removeMembersEvent.getConversationId().equals(conversationId)) {

                        String userId = IsometrikChatSdk.getInstance().getUserSession().getUserId();

                        boolean loggedInUserRemoved = false;
                        List<RemoveMembersEvent.ConversationMember> members = removeMembersEvent.getMembers();
                        int size = members.size();
                        for (int i = 0; i < size; i++) {
                            if (members.get(i).getMemberId().equals(userId)) {
                                loggedInUserRemoved = true;
                                break;
                            }
                        }

                        if (loggedInUserRemoved) {
                            if (!removeMembersEvent.getUserId()
                                    .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                                updateLastReadInConversation();
                            }
                            conversationMessagesView.closeConversation();
                        } else {
                            conversationMessagesView.addMessageInUI(messagesModel);
                            conversationMessagesView.updateParticipantsCount(removeMembersEvent.getMembersCount());
                            if (!removeMembersEvent.getUserId()
                                    .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                                updateLastReadInConversation();
                            }
                        }
                    } else {
                        if (!removeMembersEvent.getUserId()
                                .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                            conversationMessagesView.showMessageNotification(removeMembersEvent.getConversationId(),
                                    removeMembersEvent.getConversationTitle(),
                                    messagesModel.getConversationActionMessage(), removeMembersEvent.isPrivateOneToOne(),
                                    null, false, removeMembersEvent.getConversationImageUrl(),
                                    removeMembersEvent.getUserProfileImageUrl(), removeMembersEvent.getUserName());
                        }
                    }
                }
            };

    private final MessageEventCallback messageEventCallback = new MessageEventCallback() {
        @Override
        public void messageDetailsUpdated(@NotNull Isometrik isometrik,
                                          @NotNull UpdateMessageDetailsEvent updateMessageDetailsEvent) {
            MessagesModel messagesModel =
                    RealtimeMessageUtil.parseUpdateMessageDetailsEvent(updateMessageDetailsEvent);
            if (updateMessageDetailsEvent.getConversationId().equals(conversationId)) {
                conversationMessagesView.addMessageInUI(messagesModel);
                if (!updateMessageDetailsEvent.getUserId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                    updateLastReadInConversation();
                }
                if (updateMessageDetailsEvent.getDetails().getBody() != null) {
                    conversationMessagesView.onMessageUpdatedSuccessfully(
                            updateMessageDetailsEvent.getMessageId(),
                            updateMessageDetailsEvent.getDetails().getBody());
                }
            } else {
                if (!updateMessageDetailsEvent.getUserId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    conversationMessagesView.showMessageNotification(
                            updateMessageDetailsEvent.getConversationId(),
                            updateMessageDetailsEvent.isPrivateOneToOne()
                                    ? updateMessageDetailsEvent.getUserName()
                                    : updateMessageDetailsEvent.getConversationTitle(),
                            messagesModel.getConversationActionMessage(),
                            updateMessageDetailsEvent.isPrivateOneToOne(), null, false,
                            updateMessageDetailsEvent.isPrivateOneToOne()
                                    ? updateMessageDetailsEvent.getUserProfileImageUrl()
                                    : updateMessageDetailsEvent.getConversationImageUrl(),
                            updateMessageDetailsEvent.getUserProfileImageUrl(),
                            updateMessageDetailsEvent.getUserName());
                }
            }
        }

        @Override
        public void updatedLastReadInConversationEvent(@NotNull Isometrik isometrik,
                                                       @NotNull UpdatedLastReadInConversationEvent updatedLastReadInConversationEvent) {
            //TODO Nothing
        }

        @Override
        public void messagesRemovedForEveryone(@NotNull Isometrik isometrik,
                                               @NotNull RemoveMessagesForEveryoneEvent removeMessagesForEveryoneEvent) {

            if (removeMessagesForEveryoneEvent.getConversationId().equals(conversationId)) {
                //conversationMessagesView.addMessageInUI(
                //    RealtimeMessageUtil.parseRemoveMessagesForEveryoneEvent(
                //        removeMessagesForEveryoneEvent));

                conversationMessagesView.onMessageDeletedSuccessfully(
                        removeMessagesForEveryoneEvent.getMessageIds());
            } else {
                if (!removeMessagesForEveryoneEvent.getUserId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    conversationMessagesView.showMessageNotification(
                            removeMessagesForEveryoneEvent.getConversationId(),
                            removeMessagesForEveryoneEvent.isPrivateOneToOne()
                                    ? removeMessagesForEveryoneEvent.getUserName()
                                    : removeMessagesForEveryoneEvent.getConversationTitle(),
                            IsometrikChatSdk.getInstance()
                                    .getContext()
                                    .getString(R.string.ism_message_deleted_for_all,
                                            removeMessagesForEveryoneEvent.getUserName()),
                            removeMessagesForEveryoneEvent.isPrivateOneToOne(), null, false,
                            removeMessagesForEveryoneEvent.isPrivateOneToOne()
                                    ? removeMessagesForEveryoneEvent.getUserProfileImageUrl()
                                    : removeMessagesForEveryoneEvent.getConversationImageUrl(),
                            removeMessagesForEveryoneEvent.getUserProfileImageUrl(),
                            removeMessagesForEveryoneEvent.getUserName());
                }
            }
        }

        @Override
        public void messagesRemovedForSelf(@NotNull Isometrik isometrik, @NotNull RemoveMessagesForSelfEvent removeMessagesForSelfEvent) {

            if (removeMessagesForSelfEvent.getConversationId().equals(conversationId)) {
                //conversationMessagesView.addMessageInUI(
                //    RealtimeMessageUtil.parseRemoveMessagesForSelfEvent(removeMessagesForSelfEvent));
                conversationMessagesView.onMessageDeletedSuccessfully(
                        removeMessagesForSelfEvent.getMessageIds());
            }
        }

        @Override
        public void messageMarkedAsDelivered(@NotNull Isometrik isometrik, @NotNull MarkMessageAsDeliveredEvent markMessageAsDeliveredEvent) {

            if (markMessageAsDeliveredEvent.getConversationId().equals(conversationId)) {
                if (!markMessageAsDeliveredEvent.getUserId().equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    if (isPrivateOneToOne) {
                        conversationMessagesView.markMessageAsDeliveredToAll(markMessageAsDeliveredEvent.getMessageId());
                    } else {

                        fetchDeliveryReadStatusOfMessage(markMessageAsDeliveredEvent.getConversationId(),

                                Collections.singletonList(markMessageAsDeliveredEvent.getMessageId()));
                    }
                }
            }
        }

        @Override
        public void messageMarkedAsRead(@NotNull Isometrik isometrik, @NotNull MarkMessageAsReadEvent markMessageAsReadEvent) {
            if (markMessageAsReadEvent.getConversationId().equals(conversationId)) {
                if (!markMessageAsReadEvent.getUserId().equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    if (isPrivateOneToOne) {
                        conversationMessagesView.markMessageAsReadByAll(markMessageAsReadEvent.getMessageId());
                    } else {
                        fetchDeliveryReadStatusOfMessage(markMessageAsReadEvent.getConversationId(), Collections.singletonList(markMessageAsReadEvent.getMessageId()));
                    }
                }
            }
        }

        @Override
        public void multipleMessagesMarkedAsRead(@NotNull Isometrik isometrik, @NotNull MarkMultipleMessagesAsReadEvent markMultipleMessagesAsReadEvent) {
            if (markMultipleMessagesAsReadEvent.getConversationId().equals(conversationId)) {
                if (!markMultipleMessagesAsReadEvent.getUserId().equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                    conversationMessagesView.onMultipleMessagesMarkedAsReadEvent();
                }
            }
        }

        @Override
        public void messageSent(@NotNull Isometrik isometrik, @NotNull SendMessageEvent sendMessageEvent) {

            LogManger.INSTANCE.log("real:messageSent", "In");

            LogManger.INSTANCE.log("real:messageSent", "sendMessageEvent.getConversationId().equals(conversationId) 11 " + sendMessageEvent.getConversationId().equals(conversationId));
            LogManger.INSTANCE.log("real:messageSent", "sendMessageEvent.getConversationId().equals(conversationId) 22 " + (sendMessageEvent.getAction() != null || (!sendMessageEvent.getSenderId()
                    .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())
                    || !sendMessageEvent.getDeviceId()
                    .equals(IsometrikChatSdk.getInstance().getUserSession().getDeviceId()))));

            LogManger.INSTANCE.log("real:messageSent", "sendMessageEvent.getAction() " + sendMessageEvent.getAction());
            LogManger.INSTANCE.log("real:messageSent", "sendMessageEvent.getSenderId() " + sendMessageEvent.getSenderId());
            LogManger.INSTANCE.log("real:messageSent", "IsometrikChatSdk.getInstance().getUserSession().getUserId() " + IsometrikChatSdk.getInstance().getUserSession().getUserId());
            LogManger.INSTANCE.log("real:messageSent", "sendMessageEvent.getDeviceId() " + sendMessageEvent.getDeviceId());
            LogManger.INSTANCE.log("real:messageSent", "IsometrikChatSdk.getInstance().getUserSession().getDeviceId() " + IsometrikChatSdk.getInstance().getUserSession().getDeviceId());
            LogManger.INSTANCE.log("real:messageSent", "isSharedFromApp " + sendMessageEvent.getMetaData().has("isSharedFromApp"));



            /*
            in Fax getAction() getting null for other user message, so realtime message not getting
            * */
            if (sendMessageEvent.getConversationId().equals(conversationId)) {
                if (/*sendMessageEvent.getAction() != null ||*/ (!sendMessageEvent.getSenderId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())
                        || !sendMessageEvent.getDeviceId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getDeviceId()) || sendMessageEvent.getMetaData().has("isSharedFromApp"))) {
                    MessagesModel messageModel =
                            RealtimeMessageUtil.parseSendMessageEvent(sendMessageEvent, taggedUserCallback);

                    LogManger.INSTANCE.log("real:messageSent", " " + messageModel);

                    if (messageModel != null) {
                        conversationMessagesView.addMessageInUI(messageModel);
                    }
                }

                if (!sendMessageEvent.getSenderId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    if (sendMessageEvent.getDeliveryReadEventsEnabled()
                            && activeInConversation
                            && !joiningAsObserver) {

                        IsometrikChatSdk.getInstance()
                                .getIsometrik()
                                .getExecutor()
                                .execute(() -> isometrik.getRemoteUseCases()
                                        .getMessageUseCases()
                                        .markMessageAsRead(new MarkMessageAsReadQuery.Builder().setConversationId(
                                                        sendMessageEvent.getConversationId())
                                                .setUserToken(userToken)
                                                .setMessageId(sendMessageEvent.getMessageId())
                                                .build(), (var1, var2) -> {

                                        }));
                    }
                }
            } else {
                if (!sendMessageEvent.getSenderId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    LocalMessageNotificationUtil localMessageNotificationUtil =
                            RealtimeMessageUtil.parseMessageEventForNotification(sendMessageEvent);
                    if (localMessageNotificationUtil != null) {
                        conversationMessagesView.showMessageNotification(sendMessageEvent.getConversationId(),
                                localMessageNotificationUtil.getConversationTitle(),
                                localMessageNotificationUtil.getMessageText(), sendMessageEvent.isPrivateOneToOne(),
                                localMessageNotificationUtil.getMessagePlaceHolderImage(), false,
                                localMessageNotificationUtil.getConversationImageUrl(),
                                sendMessageEvent.getSenderProfileImageUrl(), sendMessageEvent.getSenderName());
                    }
                }
            }
        }

        @Override
        public void typingMessageSent(@NotNull Isometrik isometrik,
                                      @NotNull SendTypingMessageEvent sendTypingMessageEvent) {

            if (sendTypingMessageEvent.getConversationId().equals(conversationId)) {
                if (!sendTypingMessageEvent.getUserId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                    conversationMessagesView.onRemoteUserTypingEvent(IsometrikChatSdk.getInstance()
                            .getContext()
                            .getString(R.string.ism_typing, sendTypingMessageEvent.getUserName()));
                }
            }
        }

        @Override
        public void blockedUserInConversation(@NotNull Isometrik isometrik,
                                              @NotNull BlockUserInConversationEvent blockUserInConversationEvent) {
            MessagesModel messagesModel =
                    RealtimeMessageUtil.parseUserBlockEvent(blockUserInConversationEvent);
            String initiatorName = blockUserInConversationEvent.getInitiatorName();
            String opponentName = blockUserInConversationEvent.getOpponentName();

            if (opponentName.equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) { // opponentUser blocked
                conversationMessagesView.blockedStatus(true);
            } else if (initiatorName.equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) { // selfUser Blocked
                conversationMessagesView.blockedStatus(false);
            }
            if (blockUserInConversationEvent.getConversationId().equals(conversationId)) {
                conversationMessagesView.addMessageInUI(messagesModel);
                conversationMessagesView.onMessagingStatusChanged(
                        blockUserInConversationEvent.isMessagingDisabled());
                if (!blockUserInConversationEvent.getInitiatorId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                    updateLastReadInConversation();
                }
            } else {
                if (!blockUserInConversationEvent.getInitiatorId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    conversationMessagesView.showMessageNotification(
                            blockUserInConversationEvent.getConversationId(),
                            blockUserInConversationEvent.getInitiatorName(),
                            messagesModel.getConversationActionMessage(), true, null, false,
                            blockUserInConversationEvent.getInitiatorProfileImageUrl(),
                            blockUserInConversationEvent.getInitiatorProfileImageUrl(),
                            blockUserInConversationEvent.getInitiatorName());
                }
            }
        }

        @Override
        public void unblockedUserInConversation(@NotNull Isometrik isometrik,
                                                @NotNull UnblockUserInConversationEvent unblockUserInConversationEvent) {
            MessagesModel messagesModel =
                    RealtimeMessageUtil.parseUserUnblockEvent(unblockUserInConversationEvent);
            String initiatorName = unblockUserInConversationEvent.getInitiatorName();
            String opponentName = unblockUserInConversationEvent.getOpponentName();

            if (opponentName.equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) { // opponentUser unBlocked
                conversationMessagesView.blockedStatus(false);
            } else if (initiatorName.equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) { // selfUser unBlocked
                conversationMessagesView.blockedStatus(false);
            }

            if (unblockUserInConversationEvent.getConversationId().equals(conversationId)) {
                conversationMessagesView.addMessageInUI(messagesModel);
                conversationMessagesView.onMessagingStatusChanged(
                        unblockUserInConversationEvent.isMessagingDisabled());
                if (!unblockUserInConversationEvent.getInitiatorId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                    updateLastReadInConversation();
                }
            } else {
                if (!unblockUserInConversationEvent.getInitiatorId()
                        .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                    conversationMessagesView.showMessageNotification(
                            unblockUserInConversationEvent.getConversationId(),
                            unblockUserInConversationEvent.getInitiatorName(),
                            messagesModel.getConversationActionMessage(), true, null, false,
                            unblockUserInConversationEvent.getInitiatorProfileImageUrl(),
                            unblockUserInConversationEvent.getInitiatorProfileImageUrl(),
                            unblockUserInConversationEvent.getInitiatorName());
                }
            }
        }
    };

    private final ReactionEventCallback reactionEventCallback = new ReactionEventCallback() {
        @Override
        public void reactionAdded(@NotNull Isometrik isometrik, @NotNull AddReactionEvent addReactionEvent) {

            ReactionModel reactionModel =
                    ReactionUtil.parseAddOrRemoveReactionEvent(addReactionEvent.getReactionType(),
                            addReactionEvent.getReactionsCount());
            if (reactionModel != null) {
                if (addReactionEvent.getConversationId().equals(conversationId)) {

                    conversationMessagesView.updateMessageReaction(addReactionEvent.getMessageId(),
                            reactionModel, true);
                } else {
                    if (!addReactionEvent.getUserId()
                            .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                        conversationMessagesView.showMessageNotification(addReactionEvent.getConversationId(),
                                addReactionEvent.isPrivateOneToOne() ? addReactionEvent.getUserName()
                                        : addReactionEvent.getConversationTitle(), IsometrikChatSdk.getInstance()
                                        .getContext()
                                        .getString(R.string.ism_reaction_added, addReactionEvent.getUserName()),
                                addReactionEvent.isPrivateOneToOne(), reactionModel.getReactionIcon(), true,
                                addReactionEvent.isPrivateOneToOne() ? addReactionEvent.getUserProfileImageUrl()
                                        : addReactionEvent.getConversationImageUrl(),
                                addReactionEvent.getUserProfileImageUrl(), addReactionEvent.getUserName());
                    }
                }
            }
        }

        @Override
        public void reactionRemoved(@NotNull Isometrik isometrik, @NotNull RemoveReactionEvent removeReactionEvent) {
            ReactionModel reactionModel =
                    ReactionUtil.parseAddOrRemoveReactionEvent(removeReactionEvent.getReactionType(),
                            removeReactionEvent.getReactionsCount());
            if (reactionModel != null) {
                if (removeReactionEvent.getConversationId().equals(conversationId)) {

                    conversationMessagesView.updateMessageReaction(removeReactionEvent.getMessageId(),
                            reactionModel, false);
                } else {
                    if (!removeReactionEvent.getUserId()
                            .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

                        conversationMessagesView.showMessageNotification(
                                removeReactionEvent.getConversationId(),
                                removeReactionEvent.isPrivateOneToOne() ? removeReactionEvent.getUserName()
                                        : removeReactionEvent.getConversationTitle(), IsometrikChatSdk.getInstance()
                                        .getContext()
                                        .getString(R.string.ism_reaction_removed, removeReactionEvent.getUserName()),
                                removeReactionEvent.isPrivateOneToOne(), reactionModel.getReactionIcon(), true,
                                removeReactionEvent.isPrivateOneToOne()
                                        ? removeReactionEvent.getUserProfileImageUrl()
                                        : removeReactionEvent.getConversationImageUrl(),
                                removeReactionEvent.getUserProfileImageUrl(), removeReactionEvent.getUserName());
                    }
                }
            }
        }
    };

    @Override
    public void fetchMessagesOnScroll() {
        if (!isLoading && !isLastPage) {

            offset++;
            fetchMessages(offset * CONVERSATION_MESSAGES_PAGE_SIZE, false, isSearchRequest, searchTag,
                    false);
        }
    }

    public void registerConnectionEventListener() {
        isometrik.getRealtimeEventsListenerManager()
                .getConnectionListenerManager()
                .addListener(connectionEventCallback);
    }

    @Override
    public void unregisterConnectionEventListener() {
        isometrik.getRealtimeEventsListenerManager()
                .getConnectionListenerManager()
                .removeListener(connectionEventCallback);
    }

    @Override
    public void registerConversationEventListener() {
        isometrik.getRealtimeEventsListenerManager()
                .getConversationListenerManager()
                .addListener(conversationEventCallback);
    }

    @Override
    public void unregisterConversationEventListener() {
        isometrik.getRealtimeEventsListenerManager()
                .getConversationListenerManager()
                .removeListener(conversationEventCallback);
    }

    @Override
    public void registerMembershipControlEventListener() {
        isometrik.getRealtimeEventsListenerManager().getMembershipControlListenerManager().addListener(membershipControlEventCallback);
    }

    @Override
    public void unregisterMembershipControlEventListener() {
        isometrik.getRealtimeEventsListenerManager().getMembershipControlListenerManager().removeListener(membershipControlEventCallback);
    }

    @Override
    public void registerMessageEventListener() {
        isometrik.getRealtimeEventsListenerManager().getMessageListenerManager().addListener(messageEventCallback);
    }

    @Override
    public void unregisterMessageEventListener() {
        isometrik.getRealtimeEventsListenerManager().getMessageListenerManager().removeListener(messageEventCallback);
    }

    @Override
    public void registerReactionEventListener() {
        isometrik.getRealtimeEventsListenerManager().getReactionListenerManager().addListener(reactionEventCallback);
    }

    @Override
    public void unregisterReactionEventListener() {
        isometrik.getRealtimeEventsListenerManager().getReactionListenerManager().removeListener(reactionEventCallback);
    }

    @Override
    public void registerUserEventListener() {
        isometrik.getRealtimeEventsListenerManager()
                .getUserListenerManager().addListener(userEventCallback);
    }

    @Override
    public void unregisterUserEventListener() {
        isometrik.getRealtimeEventsListenerManager().getUserListenerManager().removeListener(userEventCallback);
    }

    @Override
    public void cancelMediaUpload(MessagesModel messagesModel, int messagePosition) {
        isometrik.getRemoteUseCases()
                .getUploadUseCases()
                .cancelMediaUpload(
                        new CancelMediaUploadQuery.Builder().setMediaId(messagesModel.getMediaId()).build(), (var1, var2) -> {
                            uploadMessagePositions.remove(messagesModel.getLocalMessageId());

                            if (var1 != null) {
                                conversationMessagesView.onMediaUploadCanceled(true, messagesModel.getLocalMessageId(),
                                        MediaDownloadOrUploadHelper.parseMediaDownloadOrUploadCanceledMessage(
                                                messagesModel.getMessageTypeUi(), true, false), messagePosition);
                            } else {
                                conversationMessagesView.onMediaUploadCanceled(false, messagesModel.getLocalMessageId(),
                                        MediaDownloadOrUploadHelper.parseMediaDownloadOrUploadCanceledMessage(
                                                messagesModel.getMessageTypeUi(), false, false), messagePosition);
                            }
                        });
    }

    @Override
    public void saveUploadingMessagePosition(String localMessageId, int position) {
        uploadMessagePositions.put(localMessageId, position);
    }

    private final DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
        @Override
        public void onFileSaveProgress(String messageId, long fileSizeDownloaded, long fileSize) {
        }

        @Override
        public void onDownloadProgress(String messageId, long bytesRead, long contentLength, boolean done) {
            int messagePosition;
            if (downloadMessagePositions.get(messageId) == null) {
                messagePosition = 0;
            } else {
                try {
                    //noinspection ConstantConditions
                    messagePosition = downloadMessagePositions.get(messageId);
                } catch (NullPointerException ignore) {
                    messagePosition = 0;
                }
            }

            conversationMessagesView.onDownloadProgressUpdate(messageId, messagePosition, (int) ((bytesRead * 100) / contentLength));
        }
    };

    private final UploadProgressListener uploadProgressListener = new UploadProgressListener() {
        @Override
        public void onUploadProgress(String requestId, String requestGroupId, long bytesWritten, long contentLength) {
            int messagePosition;
            if (uploadMessagePositions.get(requestGroupId) == null) {
                messagePosition = 0;
            } else {
                try {
                    //noinspection ConstantConditions
                    messagePosition = uploadMessagePositions.get(requestGroupId);
                } catch (NullPointerException ignore) {
                    messagePosition = 0;
                }
            }
            conversationMessagesView.onUploadProgressUpdate(requestGroupId, requestId, messagePosition, (int) ((bytesWritten * 100) / contentLength));
        }
    };

    private int verifyMessagePosition(String messageId, ArrayList<MessagesModel> messages) {
        int position = -1;
        int size = messages.size();
        for (int i = 0; i < size; i++) {
            if (messages.get(i).getMessageId().equals(messageId)) {
                return i;
            }
        }
        return position;
    }

    private int verifyUnsentMessagePosition(String localMessageId, ArrayList<MessagesModel> messages) {
        int position = -1;
        int size = messages.size();
        for (int i = 0; i < size; i++) {

            if (messages.get(i).isSentMessage() && !messages.get(i).isMessageSentSuccessfully() && messages.get(i).getLocalMessageId().equals(localMessageId)) {
                return i;
            }
        }
        return position;
    }

    @Override
    public void startAudioRecording(Context context) {

        if (!recordAudioUtil.isRecordingAudio()) {
            audioFilePath = recordAudioUtil.startAudioRecord(context);
        }
    }

    @Override
    public void stopAudioRecording(boolean canceled) {
        recordAudioUtil.stopAudioRecord(canceled, audioFilePath);
        if (canceled) {
            audioFilePath = null;
        } else {
            conversationMessagesView.onAudioRecordedSuccessfully(audioFilePath);
        }
    }

    @Override
    public boolean isRecordingAudio() {
        return recordAudioUtil.isRecordingAudio();
    }

    @Override
    public void searchUserToTag(String searchTag) {

        if (searchTag.isEmpty()) {
            searchTag = null;
        }
        isometrik.getRemoteUseCases()
                .getConversationUseCases()
                .fetchConversationMembers(
                        new FetchConversationMembersQuery.Builder().setLimit(Constants.USERS_PAGE_SIZE)
                                .setSkip(0)
                                .setConversationId(conversationId)
                                .setUserToken(userToken)
                                .setSearchTag(searchTag)
                                .build(), (var1, var2) -> {

                            if (var1 != null) {

                                ArrayList<ConversationMember> conversationMembers = var1.getConversationMembers();

                                ArrayList<TagUserModel> tagUsersModels = new ArrayList<>();
                                int size = conversationMembers.size();

                                for (int i = 0; i < size; i++) {
                                    ConversationMember conversationMember = conversationMembers.get(i);
                                    if (!conversationMember.getUserId().equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                                        tagUsersModels.add(new TagUserModel(conversationMember));
                                    }
                                }

                                conversationMessagesView.onSearchedUsersFetched(tagUsersModels);
                            } else {
                                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

                                    conversationMessagesView.onSearchedUsersFetched(new ArrayList<>());
                                } else {
                                    conversationMessagesView.onError(var2.getErrorMessage());
                                }
                            }
                        });
    }

    private void fetchDeliveryReadStatusOfMessage(String conversationId, List<String> messageIds) {

        isometrik.getRemoteUseCases()
                .getDeliveryStatusUseCases()
                .fetchMessagesDeliveryReadStatus(new FetchMessagesDeliveryReadStatusQuery.Builder().setConversationId(conversationId)
                        .setMessageIds(messageIds)
                        .setUserToken(IsometrikChatSdk.getInstance().getUserSession().getUserToken())
                        .build(), (var1, var2) -> {
                    if (var1 != null) {
                        ArrayList<FetchMessagesDeliveryReadStatusResult.MessageDeliveryReadStatus>
                                deliveryReadStatus = var1.getMessageDeliveryReadStatuses();
                        for (int i = 0; i < deliveryReadStatus.size(); i++) {
                            if (deliveryReadStatus.get(i).isReadByAll()) {
                                conversationMessagesView.markMessageAsReadByAll(
                                        deliveryReadStatus.get(i).getMessageId());
                            } else if (deliveryReadStatus.get(i).isDeliveredToAll()) {
                                conversationMessagesView.markMessageAsDeliveredToAll(
                                        deliveryReadStatus.get(i).getMessageId());
                            }
                        }
                    }
                });
    }

    @Override
    public void fetchMessageDeliveryReadStatusOnMultipleMessagesMarkedAsReadEvent(ArrayList<MessagesModel> messagesModels) {
        ArrayList<String> messageIds = new ArrayList<>();

        for (int i = messagesModels.size() - 1; i >= 0; i--) {
            if (messagesModels.get(i).isSentMessage()
                    && messagesModels.get(i).isMessageSentSuccessfully()
                    && !messagesModels.get(i).isReadByAll()) {
                messageIds.add(messagesModels.get(i).getMessageId());
            }
        }
        int size = messageIds.size();
        if (size > 0) {

            int maximumMessageIdsInSingleRequest = Constants.CONVERSATION_MESSAGES_PAGE_SIZE;

            if (size > maximumMessageIdsInSingleRequest) {

                for (int i = 0; i < size; i += maximumMessageIdsInSingleRequest) {
                    fetchDeliveryReadStatusOfMessage(conversationId,
                            messageIds.subList(i, (Math.min(i + maximumMessageIdsInSingleRequest, size))));
                }
            } else {
                fetchDeliveryReadStatusOfMessage(conversationId, messageIds);
            }
        }
    }

    public void requestConversationDetails() {
        isometrik.getRemoteUseCases()
                .getConversationUseCases()
                .fetchConversationDetails(
                        new FetchConversationDetailsQuery.Builder().setConversationId(conversationId)
                                .setUserToken(userToken)
                                .setIncludeMembers(true)
                                .build(), (var1, var2) -> {
                            if (var1 != null) {
                                ConversationDetailsUtil conversationDetailsUtil = var1.getConversationDetails();
                                conversationDetailsUtil.setConversationId(conversationId);
                                conversationMessagesView.fetchedConversationDetails(conversationDetailsUtil);
                                messageDeliveryReadEventsEnabled =
                                        conversationDetailsUtil.getConfig().isReadEvents();
                                typingEventsEnabled = conversationDetailsUtil.getConfig().isTypingEvents();
                                isPrivateOneToOne = conversationDetailsUtil.isPrivateOneToOne();

                                if (isPrivateOneToOne) {
                                    userId = conversationDetailsUtil.getOpponentDetails().getUserId();
                                    userName = conversationDetailsUtil.getOpponentDetails().getUserName();
                                    userImageUrl =
                                            conversationDetailsUtil.getOpponentDetails().getUserProfileImageUrl();
                                    isOnline = conversationDetailsUtil.getOpponentDetails().isOnline();
                                    if (!isOnline) {
                                        lastSeenAt = conversationDetailsUtil.getOpponentDetails().getLastSeen();
                                    }

                                    if (userId == null) {
                                        userName = IsometrikChatSdk.getInstance()
                                                .getContext()
                                                .getString(R.string.ism_deleted_user);
                                        conversationMessagesView.onMessagingStatusChanged(true);
                                    }
                                    conversationMessagesView.updateConversationDetailsInHeader(false, true, userName,
                                            isOnline, lastSeenAt, null, 2,userImageUrl);
                                } else {
                                    conversationImageUrl = conversationDetailsUtil.getConversationImageUrl();
                                    conversationTitle = conversationDetailsUtil.getConversationTitle();
                                    String userName = null;
                                    if (conversationDetailsUtil.getConversationMembers() != null && !conversationDetailsUtil.getConversationMembers().isEmpty() && conversationDetailsUtil.getMembersCount() <= 2) {
                                        for (ConversationMember member : conversationDetailsUtil.getConversationMembers()) {
                                            if(!member.getUserId().equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())){
                                                userName = member.getUserName();
                                                conversationImageUrl = member.getUserProfileImageUrl();
                                            }
                                        }
                                    }
                                    conversationMessagesView.updateConversationDetailsInHeader(false, false, userName,
                                            false, 0, conversationTitle, conversationDetailsUtil.getMembersCount(),conversationImageUrl);
                                    if (conversationDetailsUtil.getConversationType()
                                            == ConversationType.OpenConversation.getValue()) {
                                        conversationMessagesView.updateVisibilityOfObserversIcon();
                                    }
                                }
                                //if (var1.getConversationDetails().getMessagingDisabled()) {
                                //  conversationMessagesView.onMessagingStatusChanged(
                                //      var1.getConversationDetails().getMessagingDisabled());
                                //}
                            } else {
                                conversationMessagesView.onFailedToJoinAsObserverOrFetchMessagesOrConversationDetails(
                                        var2.getErrorMessage());
                            }
                        });
    }

    @Override
    public Intent getConversationDetailsIntent(Activity activity, boolean isPrivateOneToOne) {
        Intent intent;
        if (isPrivateOneToOne) {

            intent = new Intent(activity, UserConversationDetailsActivity.class);
            intent.putExtra("conversationId", conversationId);
            intent.putExtra("userName", userName);
            intent.putExtra("userImageUrl", userImageUrl);
            intent.putExtra("isOnline", isOnline);
            if (!isOnline) {
                intent.putExtra("lastSeenAt", lastSeenAt);
            }
            intent.putExtra("userId", userId);
        } else {
            intent = new Intent(activity, ConversationDetailsActivity.class);

            intent.putExtra("conversationId", conversationId);
            intent.putExtra("conversationTitle", conversationTitle);
            intent.putExtra("conversationImageUrl", conversationImageUrl);
        }
        return intent;
    }

    @Override
    public void deleteConversation() {

        isometrik.getRemoteUseCases()
                .getConversationUseCases()
                .deleteConversationLocally(
                        new DeleteConversationLocallyQuery.Builder().setConversationId(conversationId)
                                .setUserToken(userToken)
                                .build(), (var1, var2) -> {

                            if (var1 != null) {
                                conversationMessagesView.onConversationDeletedSuccessfully();
                            } else {
                                conversationMessagesView.onError(var2.getErrorMessage());
                            }
                        });
    }

    private void checkIfMessagingEnabled() {
        isometrik.getRemoteUseCases()
                .getConversationUseCases()
                .fetchConversationMessagingStatus(
                        new FetchConversationMessagingStatusQuery.Builder().setConversationId(conversationId)
                                .setUserToken(userToken)
                                .build(), (var1, var2) -> {
                            if (var1 != null) {

                                if (!var1.isMessagingEnabled()) {
                                    conversationMessagesView.onMessagingStatusChanged(true);
                                }
                            } else {
                                conversationMessagesView.onError(var2.getErrorMessage());
                            }
                        });
    }

    @Override
    public boolean isPrivateOneToOne() {
        return isPrivateOneToOne;
    }

    private void updateLastReadInConversation() {
        if (messageDeliveryReadEventsEnabled && activeInConversation && !joiningAsObserver) {
            isometrik.getRemoteUseCases()
                    .getMessageUseCases()
                    .updateLastReadInConversation(
                            new UpdateLastReadInConversationQuery.Builder().setConversationId(conversationId)
                                    .setUserToken(userToken)
                                    .build(), (var1, var2) -> {
                            });
        }
    }

    @Override
    public void setActiveInConversation(boolean active) {
        this.activeInConversation = active;
    }

    @Override
    public void updateMessage(String messageId, String body, String oldMessage) {

        isometrik.getRemoteUseCases()
                .getMessageUseCases()
                .fetchMessages(new FetchMessagesQuery.Builder().setConversationId(conversationId)
                        .setUserToken(userToken)
                        .setMessageIds(new ArrayList<>(Collections.singletonList(messageId)))
                        .build(), (var1, var2) -> {
                    if (var1 != null) {
                        try {
                            List<String> searchableTags = var1.getMessages().get(0).getSearchableTags();

                            searchableTags.remove(oldMessage);
                            searchableTags.add(body);
                            updateMessageWithSearchableTags(messageId, body, searchableTags);
                        } catch (Exception ignore) {
                            updateMessageWithSearchableTags(messageId, body, null);
                        }
                    } else {
                        updateMessageWithSearchableTags(messageId, body, null);
                    }
                });
    }

    private void updateMessageWithSearchableTags(String messageId, String body,
                                                 List<String> searchableTags) {
        isometrik.getRemoteUseCases()
                .getMessageUseCases()
                .updateMessageDetails(
                        new UpdateMessageDetailsQuery.Builder().setConversationId(conversationId)
                                .setUserToken(userToken)
                                .setMessageId(messageId)
                                .setBody(body)
                                .setSearchableTags(searchableTags)
                                .build(), (var11, var22) -> {
                            if (var11 != null) {

                                conversationMessagesView.onMessageUpdatedSuccessfully(messageId, body);
                            } else {
                                conversationMessagesView.onError(var22.getErrorMessage());
                            }
                        });
    }

//    private void updateMessageWithMetadata(ArrayList<String> messageIds) {
//        for (String messageId : messageIds ) {
//            isometrik.getRemoteUseCases()
//                    .getMessageUseCases()
//                    .updateMessageDetails(
//                            new UpdateMessageDetailsQuery.Builder().setConversationId(conversationId)
//                                    .setUserToken(userToken)
//                                    .setMessageId(messageId)
//                                    .setBody(body)
//                                    .setSearchableTags(new ArrayList<>())
//                                    .build(), (var11, var22) -> {
//                                if (var11 != null) {
//
//                                    conversationMessagesView.onMessageUpdatedSuccessfully(messageId, body);
//                                } else {
//                                    conversationMessagesView.onError(var22.getErrorMessage());
//                                }
//                            });
//        }
//    }

    @Override
    public void joinAsObserver() {
        isometrik.getRemoteUseCases()
                .getMembershipControlUseCases()
                .joinAsObserver(new JoinAsObserverQuery.Builder().setConversationId(conversationId)
                        .setUserToken(userToken)
                        .build(), (var1, var2) -> {
                    if (var1 != null) {
                        conversationMessagesView.onJoinedAsObserverSuccessfully();
                    } else {
                        conversationMessagesView.onFailedToJoinAsObserverOrFetchMessagesOrConversationDetails(
                                var2.getErrorMessage());
                    }
                });
    }

    @Override
    public void leaveAsObserver() {

        isometrik.getRemoteUseCases()
                .getMembershipControlUseCases()
                .leaveAsObserver(new LeaveAsObserverQuery.Builder().setConversationId(conversationId)
                        .setUserToken(userToken)
                        .build(), (var1, var2) -> {

                });
    }

    @Override
    public void blockUser(String userId, boolean isBlocked, String personalUserId) {
        isometrik.getRemoteUseCases()
                .getUserUseCases()
                .blockUser(
                        new BlockUserQuery.Builder().setUserToken(userToken).setOpponentId(userId).build(), (var1, var2) -> {
                            if (var1 != null) {
                                conversationMessagesView.onUserBlocked();
                            } else {
                                conversationMessagesView.onError(var2.getErrorMessage());
                            }
                        });
    }

    @Override
    public void unBlockUser(String userId, boolean isBlocked, String personalUserId) {
        isometrik.getRemoteUseCases()
                .getUserUseCases()
                .unblockUser(new UnblockUserQuery.Builder().setUserToken(userToken)
                        .setOpponentId(userId)
                        .build(), (var1, var2) -> {
                    if (var1 != null) {
                        conversationMessagesView.onUserUnBlocked();
                    } else {
                        conversationMessagesView.onError(var2.getErrorMessage());
                    }
                });
    }

    @Override
    public void clearConversation(String conversationId) {
        isometrik.getRemoteUseCases().getConversationUseCases().clearConversation(new ClearConversationQuery.Builder().setConversationId(conversationId)
                .setUserToken(userToken)
                .build(), (var1, var2) -> {
            if (var1 != null) {
                conversationMessagesView.onConversationClearedSuccessfully();
            } else {
                conversationMessagesView.onError(var2.getErrorMessage());
            }
        });
    }
}

