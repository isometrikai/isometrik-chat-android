package io.isometrik.ui.messages.chat.utils.messageutils;

import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.utils.enums.MessageTypeUi;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.reaction.util.ReactionUtil;
import io.isometrik.ui.messages.tag.TaggedUserCallback;
import io.isometrik.ui.messages.tag.util.ParseMentionedUsersFromFetchMessagesResponseUtil;
import io.isometrik.chat.utils.TagUserUtil;
import io.isometrik.chat.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to generate a MessageModel for attachment messages.
 */
public class ConversationAttachmentMessageUtil {

    /**
     * Prepare message attachment model messages model.
     *
     * @param message            the message
     * @param taggedUserCallback the tagged user callback
     * @return the messages model
     */
    public static MessagesModel prepareMessageAttachmentModel(Message message,
                                                              TaggedUserCallback taggedUserCallback) {
        MessagesModel messagesModel = null;

        if (message.getCustomType() != null) {
            boolean selfMessage = IsometrikChatSdk.getInstance()
                    .getUserSession()
                    .getUserId()
                    .equals(message.getSenderInfo().getUserId());

            //selfMessage = false;
            switch (message.getCustomType()) {

                case "AttachmentMessage:Text":

                case "AttachmentMessage:Reply": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.TEXT_MESSAGE_SENT : MessageTypeUi.TEXT_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                    break;
                }

                case "AttachmentMessage:Payment Request": {
                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.PAYMENT_MESSAGE_SENT : MessageTypeUi.PAYMENT_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                    break;
                }

                case "AttachmentMessage:Post": {
                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.POST_MESSAGE_SENT : MessageTypeUi.POST_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);
                    break;
                }

                case "AttachmentMessage:Image": {
                    Attachment attachment = message.getAttachments().get(0);

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.PHOTO_MESSAGE_SENT : MessageTypeUi.PHOTO_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
                            attachment.getThumbnailUrl(), attachment.getMediaUrl(), attachment.getMimeType(),
                            attachment.getExtension(), message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), attachment.getSize(), attachment.getName(),
                            attachment.getMediaId(), message.getMessageType(), message.getMetaData(), null,
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                    break;
                }
                case "AttachmentMessage:Video": {
                    Attachment attachment = message.getAttachments().get(0);

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.VIDEO_MESSAGE_SENT : MessageTypeUi.VIDEO_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
                            attachment.getThumbnailUrl(), attachment.getMediaUrl(), attachment.getMimeType(),
                            attachment.getExtension(), message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), attachment.getSize(), attachment.getName(),
                            attachment.getMediaId(), message.getMessageType(), message.getMetaData(), null,
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                    break;
                }
                case "AttachmentMessage:Audio": {
                    Attachment attachment = message.getAttachments().get(0);
                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.AUDIO_MESSAGE_SENT : MessageTypeUi.AUDIO_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
                            attachment.getMediaUrl(), attachment.getName(), attachment.getMimeType(),
                            attachment.getExtension(), message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), attachment.getSize(), attachment.getMediaId(),
                            message.getMessageType(), message.getMetaData(), message.isDeliveredToAll(),
                            message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                    break;
                }
                case "AttachmentMessage:File": {
                    Attachment attachment = message.getAttachments().get(0);
                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.FILE_MESSAGE_SENT : MessageTypeUi.FILE_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
                            attachment.getMediaUrl(), attachment.getName(), attachment.getMimeType(),
                            attachment.getExtension(), message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), false, true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), attachment.getSize(), attachment.getMediaId(),
                            message.getMessageType(), message.getMetaData(), message.isDeliveredToAll(),
                            message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);
                    break;
                }
                case "AttachmentMessage:Location": {
                    Attachment attachment = message.getAttachments().get(0);

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.LOCATION_MESSAGE_SENT : MessageTypeUi.LOCATION_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            String.valueOf(attachment.getLatitude()), attachment.getTitle(),
                            String.valueOf(attachment.getLongitude()), attachment.getAddress(),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);
                    break;
                }
                case "AttachmentMessage:Sticker": {
                    Attachment attachment = message.getAttachments().get(0);
                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.STICKER_MESSAGE_SENT : MessageTypeUi.STICKER_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            attachment.getStillUrl(), attachment.getMediaUrl(),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), attachment.getName(), attachment.getMediaId(),
                            message.getMessageType(), message.getMetaData(), message.isDeliveredToAll(),
                            message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                    break;
                }
                case "AttachmentMessage:Gif": {
                    Attachment attachment = message.getAttachments().get(0);
                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.GIF_MESSAGE_SENT : MessageTypeUi.GIF_MESSAGE_RECEIVED, selfMessage,
                            message.getSentAt(), message.getParentMessageId() != null, attachment.getStillUrl(),
                            attachment.getMediaUrl(), message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), false, true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), attachment.getName(), attachment.getMediaId(),
                            message.getMessageType(), message.getMetaData(), message.isDeliveredToAll(),
                            message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                    break;
                }
                case "AttachmentMessage:Whiteboard": {
                    Attachment attachment = message.getAttachments().get(0);

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.WHITEBOARD_MESSAGE_SENT : MessageTypeUi.WHITEBOARD_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
                            attachment.getThumbnailUrl(), attachment.getMediaUrl(), attachment.getMimeType(),
                            attachment.getExtension(), message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), attachment.getSize(), attachment.getName(),
                            attachment.getMediaId(), message.getMessageType(), message.getMetaData(), null,
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                    break;
                }
                case "AttachmentMessage:Contact": {
                    JSONObject messageMetadata = message.getMetaData();

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
                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.CONTACT_MESSAGE_SENT : MessageTypeUi.CONTACT_MESSAGE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null, contactName,
                            contactIdentifier, contactImageUrl, message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);
                    break;
                }
                case "AttachmentMessage:OfferSent": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.OFFER_SENT : MessageTypeUi.OFFER_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }
                case "AttachmentMessage:CounterOffer": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.COUNTER_OFFER_SENT : MessageTypeUi.COUNTER_OFFER_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }
                case "AttachmentMessage:EditOffer": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.EDIT_OFFER_SENT : MessageTypeUi.EDIT_OFFER_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }

                case "AttachmentMessage:AcceptOffer": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.ACCEPT_OFFER_SENT : MessageTypeUi.ACCEPT_OFFER_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }
                case "AttachmentMessage:CancelDeal": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.CANCEL_DEAL_SENT : MessageTypeUi.CANCEL_DEAL_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }
                case "AttachmentMessage:CancelOffer": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.CANCEL_OFFER_SENT : MessageTypeUi.CANCEL_OFFER_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }
                case "AttachmentMessage:BuyDirect": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.BUY_DIRECT_SENT : MessageTypeUi.BUY_DIRECT_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }
                case "AttachmentMessage:AcceptBuyDirect": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.ACCEPT_BUY_DIRECT_SENT : MessageTypeUi.ACCEPT_BUY_DIRECT_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }
                case "AttachmentMessage:CancelBuyDirect": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.CANCEL_BUY_DIRECT_SENT : MessageTypeUi.CANCEL_BUY_DIRECT_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }

                case "AttachmentMessage:PaymentEscrowed": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.PAYMENT_ESCROWED_SENT : MessageTypeUi.PAYMENT_ESCROWED_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }
                case "AttachmentMessage:DealComplete": {

                    messagesModel = new MessagesModel(message.getMessageId(),
                            selfMessage ? MessageTypeUi.DEAL_COMPLETE_SENT : MessageTypeUi.DEAL_COMPLETE_RECEIVED,
                            selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                            TagUserUtil.parseMentionedUsers(message.getBody(),
                                    ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                            message.getMentionedUsers()), taggedUserCallback),
                            message.getSenderInfo().getUserName(),
                            message.getSenderInfo().getUserProfileImageUrl(),
                            ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                            (message.getParentMessageId() == null) ? null
                                    : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                                    message.getMetaData())), message.getMessageType(), message.getMetaData(),
                            message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                            message.getMessageUpdated() != null);

                }

            }
        }
        return messagesModel;
    }
}
