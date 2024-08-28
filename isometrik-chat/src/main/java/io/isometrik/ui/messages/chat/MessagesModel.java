package io.isometrik.ui.messages.chat;

import android.text.SpannableString;

import io.isometrik.chat.R;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.ui.messages.chat.utils.enums.MessageTypesForUI;
import io.isometrik.ui.messages.chat.utils.enums.RemoteMessageTypes;
import io.isometrik.ui.messages.chat.utils.messageutils.OriginalReplyMessageUtil;
import io.isometrik.chat.utils.TimeUtil;
import io.isometrik.ui.messages.reaction.add.ReactionModel;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class for inflating items in the list of messages in a conversation.Supported message
 * types are-
 * image/video/file/contact/location/whiteboard/sticker/gif/audio/text.
 */
public class MessagesModel implements Serializable {
    //For place holder of edited message
    private boolean editedMessage;

    /**
     * Is edited message boolean.
     *
     * @return the boolean
     */
    public boolean isEditedMessage() {
        return editedMessage;
    }

    /**
     * Sets edited message.
     *
     * @param editedMessage the edited message
     */
    public void setEditedMessage(boolean editedMessage) {
        this.editedMessage = editedMessage;
    }

    //For italic span for deleted user name
    private boolean senderDeleted;

    /**
     * Is sender deleted boolean.
     *
     * @return the boolean
     */
    public boolean isSenderDeleted() {
        return senderDeleted;
    }

    //For messages search
    private boolean online, privateOneToOne, messagingDisabled;
    private String conversationTitle, conversationImageUrl, conversationId;

    /**
     * Gets conversation id.
     *
     * @return the conversation id
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * Is private one to one boolean.
     *
     * @return the boolean
     */
    public boolean isPrivateOneToOne() {
        return privateOneToOne;
    }

    /**
     * Sets private one to one.
     *
     * @param privateOneToOne the private one to one
     */
    public void setPrivateOneToOne(boolean privateOneToOne) {
        this.privateOneToOne = privateOneToOne;
    }

    /**
     * Is messaging disabled boolean.
     *
     * @return the boolean
     */
    public boolean isMessagingDisabled() {
        return messagingDisabled;
    }

    /**
     * Sets messaging disabled.
     *
     * @param messagingDisabled the messaging disabled
     */
    public void setMessagingDisabled(boolean messagingDisabled) {
        this.messagingDisabled = messagingDisabled;
    }

    /**
     * Is online boolean.
     *
     * @return the boolean
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Sets online.
     *
     * @param online the online
     */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
     * Gets conversation image url.
     *
     * @return the conversation image url
     */
    public String getConversationImageUrl() {
        return conversationImageUrl;
    }

    /**
     * Sets conversation image url.
     *
     * @param conversationImageUrl the conversation image url
     */
    public void setConversationImageUrl(String conversationImageUrl) {
        this.conversationImageUrl = conversationImageUrl;
    }

    /**
     * Gets conversation title.
     *
     * @return the conversation title
     */
    public String getConversationTitle() {
        return conversationTitle;
    }

    /**
     * Sets conversation title.
     *
     * @param conversationTitle the conversation title
     */
    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    private boolean readByAll, deliveredToAll;

    /**
     * Is read by all boolean.
     *
     * @return the boolean
     */
    public boolean isReadByAll() {
        return readByAll;
    }

    /**
     * Is delivered to all boolean.
     *
     * @return the boolean
     */
    public boolean isDeliveredToAll() {
        return deliveredToAll;
    }

    /**
     * Sets read by all.
     *
     * @param readByAll the read by all
     */
    public void setReadByAll(boolean readByAll) {
        this.readByAll = readByAll;
    }

    /**
     * Sets delivered to all.
     *
     * @param deliveredToAll the delivered to all
     */
    public void setDeliveredToAll(boolean deliveredToAll) {
        this.deliveredToAll = deliveredToAll;
    }

    //To load media sent in same session from localPAth to avoid flicker on media upload complete
    private String localMediaPath;

    /**
     * Gets local media path.
     *
     * @return the local media path
     */
    public String getLocalMediaPath() {
        return localMediaPath;
    }

    //For storing local media-path after download
    private String downloadedMediaPath;

    /**
     * Gets downloaded media path.
     *
     * @return the downloaded media path
     */
    public String getDownloadedMediaPath() {
        return downloadedMediaPath;
    }

    /**
     * Sets downloaded media path.
     *
     * @param downloadedMediaPath the downloaded media path
     */
    public void setDownloadedMediaPath(String downloadedMediaPath) {
        this.downloadedMediaPath = downloadedMediaPath;
    }

    /**
     * Sets audio url.
     *
     * @param audioUrl the audio url
     */
    //For updating urls after upload
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    /**
     * Sets video thumbnail url.
     *
     * @param videoThumbnailUrl the video thumbnail url
     */
    public void setVideoThumbnailUrl(String videoThumbnailUrl) {
        this.videoThumbnailUrl = videoThumbnailUrl;
    }

    /**
     * Sets video main url.
     *
     * @param videoMainUrl the video main url
     */
    public void setVideoMainUrl(String videoMainUrl) {
        this.videoMainUrl = videoMainUrl;
    }

    /**
     * Sets whiteboard thumbnail url.
     *
     * @param whiteboardThumbnailUrl the whiteboard thumbnail url
     */
    public void setWhiteboardThumbnailUrl(String whiteboardThumbnailUrl) {
        this.whiteboardThumbnailUrl = whiteboardThumbnailUrl;
    }

    /**
     * Sets whiteboard main url.
     *
     * @param whiteboardMainUrl the whiteboard main url
     */
    public void setWhiteboardMainUrl(String whiteboardMainUrl) {
        this.whiteboardMainUrl = whiteboardMainUrl;
    }

    /**
     * Sets photo thumbnail url.
     *
     * @param photoThumbnailUrl the photo thumbnail url
     */
    public void setPhotoThumbnailUrl(String photoThumbnailUrl) {
        this.photoThumbnailUrl = photoThumbnailUrl;
    }

    /**
     * Sets photo main url.
     *
     * @param photoMainUrl the photo main url
     */
    public void setPhotoMainUrl(String photoMainUrl) {
        this.photoMainUrl = photoMainUrl;
    }

    /**
     * Sets file url.
     *
     * @param fileUrl the file url
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    //For message forward
    private long attachmentSize;
    private String attachmentName;
    private boolean forwardedMessage;
    private String forwardedMessageNotes;

    /**
     * Is forwarded message boolean.
     *
     * @return the boolean
     */
    public boolean isForwardedMessage() {
        return forwardedMessage;
    }

    /**
     * Gets forwarded message notes.
     *
     * @return the forwarded message notes
     */
    public String getForwardedMessageNotes() {
        return forwardedMessageNotes;
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param messageId                the message id
     * @param customMessageType        the custom message type
     * @param isSentMessage            the is sent message
     * @param messageTime              the message time
     * @param isQuotedMessage          the is quoted message
     * @param gifOrStickerStillUrl     the gif or sticker still url
     * @param gifOrStickerMainUrl      the gif or sticker main url
     * @param senderName               the sender name
     * @param senderImageUrl           the sender image url
     * @param reactions                the reactions
     * @param sticker                  the sticker
     * @param messageSentSuccessfully  the message sent successfully
     * @param localMessageId           the local message id
     * @param originalReplyMessageUtil the original reply message util
     * @param mediaName                the media name
     * @param mediaId                  the media id
     * @param remoteMessageTypes       the remote message types
     * @param messageMetadata          the message metadata
     * @param deliveredToAll           the delivered to all
     * @param readByAll                the read by all
     * @param conversationId           the conversation id
     * @param editedMessage            the edited message
     */
    //Gif/Sticker message
    public MessagesModel(String messageId, MessageTypesForUI customMessageType, boolean isSentMessage,
                         long messageTime, boolean isQuotedMessage, String gifOrStickerStillUrl,
                         String gifOrStickerMainUrl, String senderName, String senderImageUrl,
                         ArrayList<ReactionModel> reactions, boolean sticker, boolean messageSentSuccessfully,
                         String localMessageId, OriginalReplyMessageUtil originalReplyMessageUtil, String mediaName,
                         String mediaId, Integer remoteMessageTypes, JSONObject messageMetadata,
                         boolean deliveredToAll, boolean readByAll, String conversationId, boolean editedMessage) {
        this.messageId = messageId;
        this.customMessageType = customMessageType;
        this.isSentMessage = isSentMessage;
        this.sentAt = messageTime;
        this.messageTime = TimeUtil.formatTimestampToBothDateAndTime(messageTime);
        this.isQuotedMessage = isQuotedMessage;

        if (isQuotedMessage) {
            originalMessageSenderName = originalReplyMessageUtil.getOriginalMessageSenderName();
            originalMessage = originalReplyMessageUtil.getOriginalMessage();
            originalMessageTime = originalReplyMessageUtil.getGetOriginalMessageTime();
            originalMessagePlaceholderImage =
                    originalReplyMessageUtil.getOriginalMessagePlaceholderImage();
        }

        if (sticker) {
            this.stickerStillUrl = gifOrStickerStillUrl;
            this.stickerMainUrl = gifOrStickerMainUrl;
        } else {
            this.gifStillUrl = gifOrStickerStillUrl;
            this.gifMainUrl = gifOrStickerMainUrl;
        }
        if (senderName == null || senderName.isEmpty()) {
            senderName = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
            senderDeleted = true;
        }
        this.senderName = senderName;
        this.senderImageUrl = senderImageUrl;
        this.reactions = reactions;
        this.messageSentSuccessfully = messageSentSuccessfully;
        this.localMessageId = localMessageId;
        this.attachmentName = mediaName;
        this.mediaId = mediaId;
        forwardedMessage = remoteMessageTypes == RemoteMessageTypes.ForwardedMessage.getValue();
        if (forwardedMessage) {

            if (messageMetadata == null) {
                forwardedMessageNotes = null;
            } else {
                if (messageMetadata.isNull("forwardMessageNotes")) {
                    forwardedMessageNotes = null;
                } else {
                    try {
                        forwardedMessageNotes = messageMetadata.getString("forwardMessageNotes");
                    } catch (JSONException ignore) {
                        forwardedMessageNotes = null;
                    }
                }
            }
        }
        this.deliveredToAll = deliveredToAll;
        this.readByAll = readByAll;
        this.conversationId = conversationId;
        this.editedMessage = editedMessage;
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param messageId                the message id
     * @param customMessageType        the custom message type
     * @param isSentMessage            the is sent message
     * @param messageTime              the message time
     * @param isQuotedMessage          the is quoted message
     * @param mediaSizeInMB            the media size in mb
     * @param isDownloaded             the is downloaded
     * @param isDownloading            the is downloading
     * @param isUploaded               the is uploaded
     * @param isUploading              the is uploading
     * @param audioOrFileUrl           the audio or file url
     * @param audioOrFileName          the audio or file name
     * @param mimeType                 the mime type
     * @param mediaExtension           the media extension
     * @param senderName               the sender name
     * @param senderImageUrl           the sender image url
     * @param reactions                the reactions
     * @param audio                    the audio
     * @param messageSentSuccessfully  the message sent successfully
     * @param localMessageId           the local message id
     * @param originalReplyMessageUtil the original reply message util
     * @param mediaSize                the media size
     * @param mediaId                  the media id
     * @param remoteMessageTypes       the remote message types
     * @param messageMetadata          the message metadata
     * @param deliveredToAll           the delivered to all
     * @param readByAll                the read by all
     * @param conversationId           the conversation id
     * @param editedMessage            the edited message
     */
    //Audio/file message
    public MessagesModel(String messageId, MessageTypesForUI customMessageType, boolean isSentMessage,
                         long messageTime, boolean isQuotedMessage, String mediaSizeInMB, boolean isDownloaded,
                         boolean isDownloading, boolean isUploaded, boolean isUploading, String audioOrFileUrl,
                         String audioOrFileName, String mimeType, String mediaExtension, String senderName,
                         String senderImageUrl, ArrayList<ReactionModel> reactions, boolean audio,
                         boolean messageSentSuccessfully, String localMessageId,
                         OriginalReplyMessageUtil originalReplyMessageUtil, long mediaSize, String mediaId,
                         Integer remoteMessageTypes, JSONObject messageMetadata, boolean deliveredToAll,
                         boolean readByAll, String conversationId, boolean editedMessage) {
        this.messageId = messageId;
        this.customMessageType = customMessageType;
        this.isSentMessage = isSentMessage;
        this.sentAt = messageTime;
        this.messageTime = TimeUtil.formatTimestampToBothDateAndTime(messageTime);
        this.isQuotedMessage = isQuotedMessage;

        if (isQuotedMessage) {
            originalMessageSenderName = originalReplyMessageUtil.getOriginalMessageSenderName();
            originalMessage = originalReplyMessageUtil.getOriginalMessage();
            originalMessageTime = originalReplyMessageUtil.getGetOriginalMessageTime();
            originalMessagePlaceholderImage =
                    originalReplyMessageUtil.getOriginalMessagePlaceholderImage();
        }
        this.mediaSizeInMB = mediaSizeInMB;
        this.isDownloaded = isDownloaded;
        this.isDownloading = isDownloading;
        this.isUploaded = isUploaded;
        this.isUploading = isUploading;
        if (audio) {
            this.audioUrl = audioOrFileUrl;
            this.audioName = audioOrFileName;
        } else {
            this.fileUrl = audioOrFileUrl;
            this.fileName = audioOrFileName;
        }
        if (senderName == null || senderName.isEmpty()) {
            senderName = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
            senderDeleted = true;
        }
        this.senderName = senderName;
        this.senderImageUrl = senderImageUrl;
        this.reactions = reactions;
        this.messageSentSuccessfully = messageSentSuccessfully;
        this.localMessageId = localMessageId;
        this.mimeType = mimeType;
        this.mediaExtension = mediaExtension;
        this.attachmentSize = mediaSize;
        this.attachmentName = audioOrFileName;
        this.mediaId = mediaId;
        forwardedMessage = remoteMessageTypes == RemoteMessageTypes.ForwardedMessage.getValue();
        if (forwardedMessage) {

            if (messageMetadata == null) {
                forwardedMessageNotes = null;
            } else {
                if (messageMetadata.isNull("forwardMessageNotes")) {
                    forwardedMessageNotes = null;
                } else {
                    try {
                        forwardedMessageNotes = messageMetadata.getString("forwardMessageNotes");
                    } catch (JSONException ignore) {
                        forwardedMessageNotes = null;
                    }
                }
            }
        }
        this.deliveredToAll = deliveredToAll;
        this.readByAll = readByAll;
        this.conversationId = conversationId;
        this.editedMessage = editedMessage;
    }

    //For cancel of media upload
    private String mediaId;

    /**
     * Gets media id.
     *
     * @return the media id
     */
    public String getMediaId() {
        return mediaId;
    }

    /**
     * Sets media id.
     *
     * @param mediaId the media id
     */
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    //For media download progress bar
    private String mimeType;
    private String mediaExtension;

    /**
     * Gets mime type.
     *
     * @return the mime type
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Gets media extension.
     *
     * @return the media extension
     */
    public String getMediaExtension() {
        return mediaExtension;
    }

    //For conversation action messages
    private String conversationActionMessage;

    /**
     * Gets conversation action message.
     *
     * @return the conversation action message
     */
    public String getConversationActionMessage() {
        return conversationActionMessage;
    }

    //To show sending progress bar for messages not sent yet
    private boolean messageSentSuccessfully;

    /**
     * Is message sent successfully boolean.
     *
     * @return the boolean
     */
    public boolean isMessageSentSuccessfully() {
        return messageSentSuccessfully;
    }

    /**
     * Sets message sent successfully.
     *
     * @param messageSentSuccessfully the message sent successfully
     */
    public void setMessageSentSuccessfully(boolean messageSentSuccessfully) {
        this.messageSentSuccessfully = messageSentSuccessfully;
    }

    //To shoe messages which failed to send
    private boolean sendingMessageFailed;

    /**
     * Is sending message failed boolean.
     *
     * @return the boolean
     */
    public boolean isSendingMessageFailed() {
        return sendingMessageFailed;
    }

    /**
     * Sets sending message failed.
     *
     * @param sendingMessageFailed the sending message failed
     */
    public void setSendingMessageFailed(boolean sendingMessageFailed) {
        this.sendingMessageFailed = sendingMessageFailed;
    }

    //Unique identifier for message not sent yet
    private String localMessageId;

    /**
     * Gets local message id.
     *
     * @return the local message id
     */
    public String getLocalMessageId() {
        return localMessageId;
    }

    //Unique message identifier
    private String messageId;

    /**
     * Gets message id.
     *
     * @return the message id
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    //For identifying the custom message type
    private final MessageTypesForUI customMessageType;

    /**
     * Gets custom message type.
     *
     * @return the custom message type
     */
    public MessageTypesForUI getCustomMessageType() {
        return customMessageType;
    }

    //For checking if given message is sent or received
    private final boolean isSentMessage;

    /**
     * Is sent message boolean.
     *
     * @return the boolean
     */
    public boolean isSentMessage() {
        return isSentMessage;
    }

    //For the messageTime

    private final String messageTime;

    /**
     * Gets message time.
     *
     * @return the message time
     */
    public String getMessageTime() {
        return messageTime;
    }

    private final long sentAt;

    /**
     * Gets sent at.
     *
     * @return the sent at
     */
    public long getSentAt() {
        return sentAt;
    }

    //For the quoted message
    private boolean isQuotedMessage;
    private String originalMessageSenderName;
    private String originalMessage;
    private String originalMessageTime;
    private Integer originalMessagePlaceholderImage;

    /**
     * Is quoted message boolean.
     *
     * @return the boolean
     */
    public boolean isQuotedMessage() {
        return isQuotedMessage;
    }

    /**
     * Gets original message sender name.
     *
     * @return the original message sender name
     */
    public String getOriginalMessageSenderName() {
        return originalMessageSenderName;
    }

    /**
     * Gets original message.
     *
     * @return the original message
     */
    public String getOriginalMessage() {
        return originalMessage;
    }

    /**
     * Gets original message time.
     *
     * @return the original message time
     */
    public String getOriginalMessageTime() {
        return originalMessageTime;
    }

    /**
     * Gets original message placeholder image.
     *
     * @return the original message placeholder image
     */
    public Integer getOriginalMessagePlaceholderImage() {
        return originalMessagePlaceholderImage;
    }

    ////Attachment details
    //private Attachment attachment;
    //
    //public Attachment getAttachment() {
    //  return attachment;
    //}
    //

    //For media size in MB
    private String mediaSizeInMB;

    /**
     * Gets media size in mb.
     *
     * @return the media size in mb
     */
    public String getMediaSizeInMB() {
        return mediaSizeInMB;
    }

    //Download status of message,if applicable
    private boolean isDownloaded;

    /**
     * Is downloaded boolean.
     *
     * @return the boolean
     */
    public boolean isDownloaded() {
        return isDownloaded;
    }

    /**
     * Sets downloaded.
     *
     * @param downloaded the downloaded
     */
    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    //Whether media is currently downloading
    private boolean isDownloading;

    /**
     * Is downloading boolean.
     *
     * @return the boolean
     */
    public boolean isDownloading() {
        return isDownloading;
    }

    /**
     * Sets downloading.
     *
     * @param downloading the downloading
     */
    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    //Upload status of message,if applicable
    private boolean isUploaded;

    /**
     * Is uploaded boolean.
     *
     * @return the boolean
     */
    public boolean isUploaded() {
        return isUploaded;
    }

    /**
     * Sets uploaded.
     *
     * @param uploaded the uploaded
     */
    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    //Whether media is currently uploading
    private boolean isUploading;

    /**
     * Is uploading boolean.
     *
     * @return the boolean
     */
    public boolean isUploading() {
        return isUploading;
    }

    /**
     * Sets uploading.
     *
     * @param uploading the uploading
     */
    public void setUploading(boolean uploading) {
        isUploading = uploading;
    }

    //For text message
    private SpannableString textMessage;

    //For video message
    private String videoThumbnailUrl, videoMainUrl;

    //For whiteboard message
    private String whiteboardThumbnailUrl, whiteboardMainUrl;

    //For image message
    private String photoThumbnailUrl, photoMainUrl;

    //For audio message
    private String audioUrl;
    private String audioName;

    //For sticker/gif url
    private String stickerStillUrl, stickerMainUrl;
    private String gifStillUrl, gifMainUrl;

    //For file message
    private String fileUrl;
    private String fileName;

    //For the contact message;
    private String contactName;
    private String contactIdentifier;
    private String contactImageUrl;
    private JSONArray contactList = new JSONArray();

    //For the location message
    private String latitude;
    private String locationName;
    private String longitude;
    private String locationDescription;

    /**
     * Gets video thumbnail url.
     *
     * @return the video thumbnail url
     */
    public String getVideoThumbnailUrl() {
        return videoThumbnailUrl;
    }

    /**
     * Gets video main url.
     *
     * @return the video main url
     */
    public String getVideoMainUrl() {
        return videoMainUrl;
    }

    /**
     * Gets whiteboard thumbnail url.
     *
     * @return the whiteboard thumbnail url
     */
    public String getWhiteboardThumbnailUrl() {
        return whiteboardThumbnailUrl;
    }

    /**
     * Gets whiteboard main url.
     *
     * @return the whiteboard main url
     */
    public String getWhiteboardMainUrl() {
        return whiteboardMainUrl;
    }

    /**
     * Gets photo thumbnail url.
     *
     * @return the photo thumbnail url
     */
    public String getPhotoThumbnailUrl() {
        return photoThumbnailUrl;
    }

    /**
     * Gets photo main url.
     *
     * @return the photo main url
     */
    public String getPhotoMainUrl() {
        return photoMainUrl;
    }

    /**
     * Gets audio url.
     *
     * @return the audio url
     */
    public String getAudioUrl() {
        return audioUrl;
    }

    /**
     * Gets audio name.
     *
     * @return the audio name
     */
    public String getAudioName() {
        return audioName;
    }

    /**
     * Gets sticker still url.
     *
     * @return the sticker still url
     */
    public String getStickerStillUrl() {
        return stickerStillUrl;
    }

    /**
     * Gets sticker main url.
     *
     * @return the sticker main url
     */
    public String getStickerMainUrl() {
        return stickerMainUrl;
    }

    /**
     * Gets gif still url.
     *
     * @return the gif still url
     */
    public String getGifStillUrl() {
        return gifStillUrl;
    }

    /**
     * Gets gif main url.
     *
     * @return the gif main url
     */
    public String getGifMainUrl() {
        return gifMainUrl;
    }

    /**
     * Gets file url.
     *
     * @return the file url
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * Gets file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Gets location name.
     *
     * @return the location name
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Gets location description.
     *
     * @return the location description
     */
    public String getLocationDescription() {
        return locationDescription;
    }

    /**
     * Gets contact name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Gets contact identifier.
     *
     * @return the contact identifier
     */
    public String getContactIdentifier() {
        return contactIdentifier;
    }

    /**
     * Gets contact image url.
     *
     * @return the contact image url
     */
    public String getContactImageUrl() {
        return contactImageUrl;
    }

    /**
     * Gets text message.
     *
     * @return the text message
     */
    public SpannableString getTextMessage() {
        return textMessage;
    }
    //For the senderInfo

    private String senderName;
    private String senderImageUrl;

    /**
     * Gets sender name.
     *
     * @return the sender name
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Gets sender image url.
     *
     * @return the sender image url
     */
    public String getSenderImageUrl() {
        return senderImageUrl;
    }

    //For the message reactions
    private ArrayList<ReactionModel> reactions;

    /**
     * Gets reactions.
     *
     * @return the reactions
     */
    public ArrayList<ReactionModel> getReactions() {
        return reactions;
    }

    /**
     * Sets reactions.
     *
     * @param reactions the reactions
     */
    public void setReactions(ArrayList<ReactionModel> reactions) {
        this.reactions = reactions;
    }

    /**
     * Has reactions boolean.
     *
     * @return the boolean
     */
    public boolean hasReactions() {
        return (reactions != null && (!reactions.isEmpty()));
    }

    public JSONArray getContactList() {
        return contactList;
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param messageId                the message id
     * @param customMessageType        the custom message type
     * @param isSentMessage            the is sent message
     * @param messageTime              the message time
     * @param isQuotedMessage          the is quoted message
     * @param latitude                 the latitude
     * @param locationName             the location name
     * @param longitude                the longitude
     * @param locationDescription      the location description
     * @param senderName               the sender name
     * @param senderImageUrl           the sender image url
     * @param reactions                the reactions
     * @param messageSentSuccessfully  the message sent successfully
     * @param localMessageId           the local message id
     * @param originalReplyMessageUtil the original reply message util
     * @param remoteMessageTypes       the remote message types
     * @param messageMetadata          the message metadata
     * @param deliveredToAll           the delivered to all
     * @param readByAll                the read by all
     * @param conversationId           the conversation id
     * @param editedMessage            the edited message
     */
    //Location message constructor
    public MessagesModel(String messageId, MessageTypesForUI customMessageType, boolean isSentMessage,
                         long messageTime, boolean isQuotedMessage, String latitude, String locationName,
                         String longitude, String locationDescription, String senderName, String senderImageUrl,
                         ArrayList<ReactionModel> reactions, boolean messageSentSuccessfully, String localMessageId,
                         OriginalReplyMessageUtil originalReplyMessageUtil, Integer remoteMessageTypes,
                         JSONObject messageMetadata, boolean deliveredToAll, boolean readByAll, String conversationId,
                         boolean editedMessage) {
        this.messageId = messageId;
        this.customMessageType = customMessageType;
        this.isSentMessage = isSentMessage;
        this.sentAt = messageTime;
        this.messageTime = TimeUtil.formatTimestampToBothDateAndTime(messageTime);
        this.isQuotedMessage = isQuotedMessage;
        if (isQuotedMessage) {
            originalMessageSenderName = originalReplyMessageUtil.getOriginalMessageSenderName();
            originalMessage = originalReplyMessageUtil.getOriginalMessage();
            originalMessageTime = originalReplyMessageUtil.getGetOriginalMessageTime();
            originalMessagePlaceholderImage =
                    originalReplyMessageUtil.getOriginalMessagePlaceholderImage();
        }

        this.latitude = latitude;
        this.locationName = locationName;
        this.longitude = longitude;
        this.locationDescription = locationDescription;
        if (senderName == null || senderName.isEmpty()) {
            senderName = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
            senderDeleted = true;
        }
        this.senderName = senderName;
        this.senderImageUrl = senderImageUrl;
        this.reactions = reactions;
        this.messageSentSuccessfully = messageSentSuccessfully;
        this.localMessageId = localMessageId;
        forwardedMessage = remoteMessageTypes == RemoteMessageTypes.ForwardedMessage.getValue();
        if (forwardedMessage) {

            if (messageMetadata == null) {
                forwardedMessageNotes = null;
            } else {
                if (messageMetadata.isNull("forwardMessageNotes")) {
                    forwardedMessageNotes = null;
                } else {
                    try {
                        forwardedMessageNotes = messageMetadata.getString("forwardMessageNotes");
                    } catch (JSONException ignore) {
                        forwardedMessageNotes = null;
                    }
                }
            }
        }
        this.deliveredToAll = deliveredToAll;
        this.readByAll = readByAll;
        this.conversationId = conversationId;
        this.editedMessage = editedMessage;
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param messageId                the message id
     * @param customMessageType        the custom message type
     * @param isSentMessage            the is sent message
     * @param messageTime              the message time
     * @param isQuotedMessage          the is quoted message
     * @param contactName              the contact name
     * @param contactIdentifier        the contact identifier
     * @param contactImageUrl          the contact image url
     * @param senderName               the sender name
     * @param senderImageUrl           the sender image url
     * @param reactions                the reactions
     * @param messageSentSuccessfully  the message sent successfully
     * @param localMessageId           the local message id
     * @param originalReplyMessageUtil the original reply message util
     * @param remoteMessageTypes       the remote message types
     * @param messageMetadata          the message metadata
     * @param deliveredToAll           the delivered to all
     * @param readByAll                the read by all
     * @param conversationId           the conversation id
     * @param editedMessage            the edited message
     */
    //Contact message
    public MessagesModel(String messageId, MessageTypesForUI customMessageType, boolean isSentMessage,
                         long messageTime, boolean isQuotedMessage, String contactName, String contactIdentifier,
                         String contactImageUrl, String senderName, String senderImageUrl,
                         ArrayList<ReactionModel> reactions, boolean messageSentSuccessfully, String localMessageId,
                         OriginalReplyMessageUtil originalReplyMessageUtil, Integer remoteMessageTypes,
                         JSONObject messageMetadata, boolean deliveredToAll, boolean readByAll, String conversationId,
                         boolean editedMessage) {
        this.messageId = messageId;
        this.customMessageType = customMessageType;
        this.isSentMessage = isSentMessage;
        this.sentAt = messageTime;
        this.messageTime = TimeUtil.formatTimestampToBothDateAndTime(messageTime);
        this.isQuotedMessage = isQuotedMessage;

        if (isQuotedMessage) {
            originalMessageSenderName = originalReplyMessageUtil.getOriginalMessageSenderName();
            originalMessage = originalReplyMessageUtil.getOriginalMessage();
            originalMessageTime = originalReplyMessageUtil.getGetOriginalMessageTime();
            originalMessagePlaceholderImage =
                    originalReplyMessageUtil.getOriginalMessagePlaceholderImage();
        }
        this.contactName = contactName;
        this.contactIdentifier = contactIdentifier;
        this.contactImageUrl = contactImageUrl;
        if (senderName == null || senderName.isEmpty()) {
            senderName = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
            senderDeleted = true;
        }
        this.senderName = senderName;
        this.senderImageUrl = senderImageUrl;
        this.reactions = reactions;
        this.messageSentSuccessfully = messageSentSuccessfully;
        this.localMessageId = localMessageId;
        forwardedMessage = remoteMessageTypes == RemoteMessageTypes.ForwardedMessage.getValue();
        if (forwardedMessage) {

            if (messageMetadata == null) {
                forwardedMessageNotes = null;
            } else {
                if (messageMetadata.isNull("forwardMessageNotes")) {
                    forwardedMessageNotes = null;
                } else {
                    try {
                        forwardedMessageNotes = messageMetadata.getString("forwardMessageNotes");
                    } catch (JSONException ignore) {
                        forwardedMessageNotes = null;
                    }
                }
            }
        }
        this.deliveredToAll = deliveredToAll;
        this.readByAll = readByAll;
        this.conversationId = conversationId;
        this.editedMessage = editedMessage;
        try {
            this.contactList = messageMetadata.getJSONArray("contacts");
        } catch (JSONException e) {
            this.contactList = new JSONArray();
        }
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param messageId                the message id
     * @param customMessageType        the custom message type
     * @param isSentMessage            the is sent message
     * @param messageTime              the message time
     * @param isQuotedMessage          the is quoted message
     * @param mediaSizeInMB            the media size in mb
     * @param isDownloaded             the is downloaded
     * @param isDownloading            the is downloading
     * @param isUploaded               the is uploaded
     * @param isUploading              the is uploading
     * @param thumbnailUrl             the thumbnail url
     * @param mainUrl                  the main url
     * @param mimeType                 the mime type
     * @param mediaExtension           the media extension
     * @param senderName               the sender name
     * @param senderImageUrl           the sender image url
     * @param reactions                the reactions
     * @param messageSentSuccessfully  the message sent successfully
     * @param localMessageId           the local message id
     * @param originalReplyMessageUtil the original reply message util
     * @param mediaSize                the media size
     * @param mediaName                the media name
     * @param mediaId                  the media id
     * @param remoteMessageTypes       the remote message types
     * @param messageMetadata          the message metadata
     * @param localMediaPath           the local media path
     * @param deliveredToAll           the delivered to all
     * @param readByAll                the read by all
     * @param conversationId           the conversation id
     * @param editedMessage            the edited message
     */
    //Video/photo/whiteboard message
    public MessagesModel(String messageId, MessageTypesForUI customMessageType, boolean isSentMessage,
                         long messageTime, boolean isQuotedMessage, String mediaSizeInMB, boolean isDownloaded,
                         boolean isDownloading, boolean isUploaded, boolean isUploading, String thumbnailUrl,
                         String mainUrl, String mimeType, String mediaExtension, String senderName,
                         String senderImageUrl, ArrayList<ReactionModel> reactions, boolean messageSentSuccessfully,
                         String localMessageId, OriginalReplyMessageUtil originalReplyMessageUtil, long mediaSize,
                         String mediaName, String mediaId, Integer remoteMessageTypes, JSONObject messageMetadata,
                         String localMediaPath, boolean deliveredToAll, boolean readByAll, String conversationId,
                         boolean editedMessage) {
        this.messageId = messageId;
        this.customMessageType = customMessageType;
        this.isSentMessage = isSentMessage;
        this.sentAt = messageTime;
        this.messageTime = TimeUtil.formatTimestampToBothDateAndTime(messageTime);
        this.isQuotedMessage = isQuotedMessage;

        if (isQuotedMessage) {
            originalMessageSenderName = originalReplyMessageUtil.getOriginalMessageSenderName();
            originalMessage = originalReplyMessageUtil.getOriginalMessage();
            originalMessageTime = originalReplyMessageUtil.getGetOriginalMessageTime();
            originalMessagePlaceholderImage =
                    originalReplyMessageUtil.getOriginalMessagePlaceholderImage();
        }
        this.mediaSizeInMB = mediaSizeInMB;
        this.isDownloaded = isDownloaded;
        this.isDownloading = isDownloading;
        this.isUploaded = isUploaded;
        this.isUploading = isUploading;
        switch (customMessageType) {
            case PhotoReceived:
            case PhotoSent: {
                this.photoThumbnailUrl = thumbnailUrl;
                this.photoMainUrl = mainUrl;
                break;
            }
            case VideoReceived:
            case VideoSent: {
                this.videoThumbnailUrl = thumbnailUrl;
                this.videoMainUrl = mainUrl;
                break;
            }
            case WhiteboardReceived:
            case WhiteboardSent: {
                this.whiteboardThumbnailUrl = thumbnailUrl;
                this.whiteboardMainUrl = mainUrl;
                break;
            }
        }
        if (senderName == null || senderName.isEmpty()) {
            senderName = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
            senderDeleted = true;
        }
        this.senderName = senderName;
        this.senderImageUrl = senderImageUrl;
        this.reactions = reactions;
        this.messageSentSuccessfully = messageSentSuccessfully;
        this.localMessageId = localMessageId;
        this.mimeType = mimeType;
        this.mediaExtension = mediaExtension;
        this.attachmentSize = mediaSize;
        this.attachmentName = mediaName;
        this.mediaId = mediaId;
        forwardedMessage = remoteMessageTypes == RemoteMessageTypes.ForwardedMessage.getValue();
        if (forwardedMessage) {

            if (messageMetadata == null) {
                forwardedMessageNotes = null;
            } else {
                if (messageMetadata.isNull("forwardMessageNotes")) {
                    forwardedMessageNotes = null;
                } else {
                    try {
                        forwardedMessageNotes = messageMetadata.getString("forwardMessageNotes");
                    } catch (JSONException ignore) {
                        forwardedMessageNotes = null;
                    }
                }
            }
        }
        this.localMediaPath = localMediaPath;
        this.deliveredToAll = deliveredToAll;
        this.readByAll = readByAll;
        this.conversationId = conversationId;
        this.editedMessage = editedMessage;
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param messageId                the message id
     * @param customMessageType        the custom message type
     * @param isSentMessage            the is sent message
     * @param messageTime              the message time
     * @param isQuotedMessage          the is quoted message
     * @param textMessage              the text message
     * @param senderName               the sender name
     * @param senderImageUrl           the sender image url
     * @param reactions                the reactions
     * @param messageSentSuccessfully  the message sent successfully
     * @param localMessageId           the local message id
     * @param originalReplyMessageUtil the original reply message util
     * @param remoteMessageTypes       the remote message types
     * @param messageMetadata          the message metadata
     * @param deliveredToAll           the delivered to all
     * @param readByAll                the read by all
     * @param conversationId           the conversation id
     * @param editedMessage            the edited message
     */
    //Text message
    public MessagesModel(String messageId, MessageTypesForUI customMessageType, boolean isSentMessage,
                         long messageTime, boolean isQuotedMessage, SpannableString textMessage, String senderName,
                         String senderImageUrl, ArrayList<ReactionModel> reactions, boolean messageSentSuccessfully,
                         String localMessageId, OriginalReplyMessageUtil originalReplyMessageUtil,
                         Integer remoteMessageTypes, JSONObject messageMetadata, boolean deliveredToAll,
                         boolean readByAll, String conversationId, boolean editedMessage) {
        this.messageId = messageId;
        this.customMessageType = customMessageType;
        this.isSentMessage = isSentMessage;
        this.sentAt = messageTime;
        this.messageTime = TimeUtil.formatTimestampToBothDateAndTime(messageTime);
        this.isQuotedMessage = isQuotedMessage;

        if (isQuotedMessage) {
            originalMessageSenderName = originalReplyMessageUtil.getOriginalMessageSenderName();
            originalMessage = originalReplyMessageUtil.getOriginalMessage();
            originalMessageTime = originalReplyMessageUtil.getGetOriginalMessageTime();
            originalMessagePlaceholderImage =
                    originalReplyMessageUtil.getOriginalMessagePlaceholderImage();
        }
        this.textMessage = textMessage;
        if (senderName == null || senderName.isEmpty()) {
            senderName = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
            senderDeleted = true;
        }
        this.senderName = senderName;
        this.senderImageUrl = senderImageUrl;
        this.reactions = reactions;
        this.messageSentSuccessfully = messageSentSuccessfully;
        this.localMessageId = localMessageId;
        forwardedMessage = remoteMessageTypes == RemoteMessageTypes.ForwardedMessage.getValue();
        if (forwardedMessage) {

            if (messageMetadata == null) {
                forwardedMessageNotes = null;
            } else {
                if (messageMetadata.isNull("forwardMessageNotes")) {
                    forwardedMessageNotes = null;
                } else {
                    try {
                        forwardedMessageNotes = messageMetadata.getString("forwardMessageNotes");
                    } catch (JSONException ignore) {
                        forwardedMessageNotes = null;
                    }
                }
            }
        }
        this.deliveredToAll = deliveredToAll;
        this.readByAll = readByAll;
        this.conversationId = conversationId;
        this.editedMessage = editedMessage;
    }

    /**
     * Gets attachment size.
     *
     * @return the attachment size
     */
    public long getAttachmentSize() {
        return attachmentSize;
    }

    /**
     * Gets attachment name.
     *
     * @return the attachment name
     */
    public String getAttachmentName() {
        return attachmentName;
    }

    /**
     * Instantiates a new Messages model.
     *
     * @param conversationActionMessage the conversation action message
     * @param messageId                 the message id
     * @param messageTime               the message time
     * @param isSentMessage             the is sent message
     * @param customMessageType         the custom message type
     */
    public MessagesModel(String conversationActionMessage, String messageId, long messageTime,
                         boolean isSentMessage, MessageTypesForUI customMessageType) {
        this.conversationActionMessage = conversationActionMessage;
        this.messageId = messageId;
        this.customMessageType = customMessageType;
        this.sentAt = messageTime;
        this.messageTime = TimeUtil.formatTimestampToBothDateAndTime(messageTime);
        this.isSentMessage = isSentMessage;
    }

    private boolean selected;

    /**
     * Is selected boolean.
     *
     * @return the boolean
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets selected.
     *
     * @param selected the selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Sets text message.
     *
     * @param textMessage the text message
     */
    public void setTextMessage(SpannableString textMessage) {
        this.textMessage = textMessage;
    }
}
