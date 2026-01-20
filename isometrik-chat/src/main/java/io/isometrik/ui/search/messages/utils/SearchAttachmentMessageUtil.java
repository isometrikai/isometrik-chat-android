package io.isometrik.ui.search.messages.utils;

import io.isometrik.chat.enums.CustomMessageTypes;
import io.isometrik.chat.response.message.utils.fetchmessages.UserMessage;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.enums.MessageTypeUi;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.chat.utils.messageutils.OriginalReplyMessageUtil;
import io.isometrik.ui.messages.reaction.util.ReactionUtil;
import io.isometrik.ui.messages.tag.util.ParseMentionedUsersFromFetchMessagesResponseUtil;
import io.isometrik.chat.utils.TagUserUtil;
import io.isometrik.chat.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to create search results model class for recyclerview items for attachment
 * messages.
 */
public class SearchAttachmentMessageUtil {

  /**
   * Prepare message attachment model messages model.
   *
   * @param message the message
   * @return the messages model
   */
  public static MessagesModel prepareMessageAttachmentModel(UserMessage message) {
    MessagesModel messagesModel = null;

    if (message.getCustomType() != null) {
      boolean selfMessage = false;
      if (message.getSenderInfo().getUserId() != null) {
        selfMessage = message.getSenderInfo()
            .getUserId()
            .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId());
      }
      CustomMessageTypes customMessageType = CustomMessageTypes.Companion.fromValue(message.getCustomType());
      switch (customMessageType) {

        case Text: {

          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypeUi.TEXT_MESSAGE_SENT : MessageTypeUi.TEXT_MESSAGE_RECEIVED,
                  customMessageType,
              selfMessage, message.getSentAt(), message.getParentMessageId() != null,
              TagUserUtil.parseMentionedUsers(message.getBody(),
                  ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                      message.getMentionedUsers()), null), message.getSenderInfo().getUserName(),
              message.getSenderInfo().getUserProfileImageUrl(),
              ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
              (message.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                      message.getMetaData())), message.getMessageType(), message.getMetaData(),
              message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
              message.getMessageUpdated() != null);

          break;
        }
        case Image: {
          Attachment attachment = message.getAttachments().get(0);

          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypeUi.PHOTO_MESSAGE_SENT : MessageTypeUi.PHOTO_MESSAGE_RECEIVED,
                  customMessageType,
              selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                  TagUserUtil.parseMentionedUsers(message.getBody(),
                          ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                  message.getMentionedUsers()), null),
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
        case Video: {
          Attachment attachment = message.getAttachments().get(0);

          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypeUi.VIDEO_MESSAGE_SENT : MessageTypeUi.VIDEO_MESSAGE_RECEIVED,
                  customMessageType,
              selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                  TagUserUtil.parseMentionedUsers(message.getBody(),
                          ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                  message.getMentionedUsers()), null),
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
        case Audio: {
          Attachment attachment = message.getAttachments().get(0);
          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypeUi.AUDIO_MESSAGE_SENT : MessageTypeUi.AUDIO_MESSAGE_RECEIVED,
                  customMessageType,
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
        case File: {
          Attachment attachment = message.getAttachments().get(0);
          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypeUi.FILE_MESSAGE_SENT : MessageTypeUi.FILE_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Location: {
          Attachment attachment = message.getAttachments().get(0);

          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypeUi.LOCATION_MESSAGE_SENT : MessageTypeUi.LOCATION_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Sticker: {
          Attachment attachment = message.getAttachments().get(0);
          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypeUi.STICKER_MESSAGE_SENT : MessageTypeUi.STICKER_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Gif: {
          Attachment attachment = message.getAttachments().get(0);
          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypeUi.GIF_MESSAGE_SENT : MessageTypeUi.GIF_MESSAGE_RECEIVED,
                  customMessageType, selfMessage,
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
        case Whiteboard: {
          Attachment attachment = message.getAttachments().get(0);

          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypeUi.WHITEBOARD_MESSAGE_SENT : MessageTypeUi.WHITEBOARD_MESSAGE_RECEIVED,
                  customMessageType,
              selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                  TagUserUtil.parseMentionedUsers(message.getBody(),
                          ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                  message.getMentionedUsers()), null),
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
        case Contact: {
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
                  customMessageType,
              selfMessage, message.getSentAt(), message.getParentMessageId() != null, contactName,
              contactIdentifier, contactImageUrl, message.getSenderInfo().getUserName(),
              message.getSenderInfo().getUserProfileImageUrl(),
              ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
              (message.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                      message.getMetaData())), message.getMessageType(), message.getMetaData(),
              false, false, message.getConversationId(), message.getMessageUpdated() != null);
          break;
        }

        case Reply: {

          messagesModel = new MessagesModel(message.getMessageId(),
                  selfMessage ? MessageTypeUi.TEXT_MESSAGE_SENT : MessageTypeUi.TEXT_MESSAGE_RECEIVED,
                  customMessageType,
                  selfMessage, message.getSentAt(), message.getParentMessageId() != null,
                  TagUserUtil.parseMentionedUsers(message.getBody(),
                          ParseMentionedUsersFromFetchMessagesResponseUtil.parseMentionedUsers(
                                  message.getMentionedUsers()), null), message.getSenderInfo().getUserName(),
                  message.getSenderInfo().getUserProfileImageUrl(),
                  ReactionUtil.parseReactionMessages(message.getReactions()), true, null,
                  (message.getParentMessageId() == null) ? null
                          : (new OriginalReplyMessageUtil(message.getParentMessageId(),
                          message.getMetaData())), message.getMessageType(), message.getMetaData(),
                  message.isDeliveredToAll(), message.isReadByAll(), message.getConversationId(),
                  message.getMessageUpdated() != null);

          break;
        }
      }
    }
    if (messagesModel != null) {
      if (message.getPrivateOneToOne()) {
        messagesModel.setConversationImageUrl(
            message.getOpponentDetails().getUserProfileImageUrl());

        messagesModel.setOnline(message.getOpponentDetails().isOnline());
        messagesModel.setPrivateOneToOne(true);
        String conversationTitle = message.getOpponentDetails().getUserName();

        if (conversationTitle == null || conversationTitle.isEmpty()) {
          conversationTitle =
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
          messagesModel.setMessagingDisabled(true);
        }
        messagesModel.setConversationTitle(conversationTitle);
      } else {
        messagesModel.setOnline(false);
        messagesModel.setPrivateOneToOne(false);
        messagesModel.setConversationTitle(message.getConversationTitle());
        messagesModel.setConversationImageUrl(message.getConversationImageUrl());
      }
    }
    return messagesModel;
  }
}