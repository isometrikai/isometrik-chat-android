package io.isometrik.ui.search.messages.utils;

import io.isometrik.chat.response.message.utils.fetchmessages.UserMessage;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.chat.utils.enums.MessageTypesForUI;
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
      switch (message.getCustomType()) {

        case "AttachmentMessage:Text": {

          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypesForUI.TextSent : MessageTypesForUI.TextReceived,
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
        case "AttachmentMessage:Image": {
          Attachment attachment = message.getAttachments().get(0);

          messagesModel = new MessagesModel(message.getMessageId(),
              selfMessage ? MessageTypesForUI.PhotoSent : MessageTypesForUI.PhotoReceived,
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
              selfMessage ? MessageTypesForUI.VideoSent : MessageTypesForUI.VideoReceived,
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
              selfMessage ? MessageTypesForUI.AudioSent : MessageTypesForUI.AudioReceived,
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
              selfMessage ? MessageTypesForUI.FileSent : MessageTypesForUI.FileReceived,
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
              selfMessage ? MessageTypesForUI.LocationSent : MessageTypesForUI.LocationReceived,
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
              selfMessage ? MessageTypesForUI.StickerSent : MessageTypesForUI.StickerReceived,
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
              selfMessage ? MessageTypesForUI.GifSent : MessageTypesForUI.GifReceived, selfMessage,
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
              selfMessage ? MessageTypesForUI.WhiteboardSent : MessageTypesForUI.WhiteboardReceived,
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
              selfMessage ? MessageTypesForUI.ContactSent : MessageTypesForUI.ContactReceived,
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

        case "AttachmentMessage:Reply": {

          messagesModel = new MessagesModel(message.getMessageId(),
                  selfMessage ? MessageTypesForUI.TextSent : MessageTypesForUI.TextReceived,
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