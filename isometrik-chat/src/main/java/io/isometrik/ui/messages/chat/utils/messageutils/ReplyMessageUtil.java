package io.isometrik.ui.messages.chat.utils.messageutils;

import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.chat.enums.CustomMessageTypes;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to prepare parent message for a reply message based on medssage type of original
 * message.
 */
public class ReplyMessageUtil {

  /**
   * Prepare reply message metadata json object.
   *
   * @param messagesModel the messages model
   * @return the json object
   */
  public static JSONObject prepareReplyMessageMetadata(MessagesModel messagesModel) {
    JSONObject replyMessage = new JSONObject();
    JSONObject replyMessageDetails = new JSONObject();
    try {
      switch (messagesModel.getMessageTypeUi()) {
        case TEXT_MESSAGE_SENT:
        case TEXT_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Text.value);

          replyMessageDetails.put("parentMessageBody", messagesModel.getTextMessage());
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", null);
          break;
        }
        case PHOTO_MESSAGE_SENT:

        case PHOTO_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Image.value);

          replyMessageDetails.put("parentMessageBody",
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photo));
          replyMessageDetails.put("parentMessageAttachmentUrl", messagesModel.getPhotoThumbnailUrl());
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_picture);
          break;
        }
        case VIDEO_MESSAGE_SENT:

        case VIDEO_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Video.value);

          replyMessageDetails.put("parentMessageBody",
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_video));
          replyMessageDetails.put("parentMessageAttachmentUrl",messagesModel.getVideoThumbnailUrl());
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_video);
          break;
        }
        case AUDIO_MESSAGE_SENT:

        case AUDIO_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Audio.value);

          replyMessageDetails.put("parentMessageBody",
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_audio_recording));
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_mic);
          break;
        }
        case FILE_MESSAGE_SENT:

        case FILE_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.File.value);

          replyMessageDetails.put("parentMessageBody",
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_file));
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_file);
          break;
        }
        case STICKER_MESSAGE_SENT:

        case STICKER_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Sticker.value);

          replyMessageDetails.put("parentMessageBody",
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_sticker));
          replyMessageDetails.put("parentMessageAttachmentUrl", messagesModel.getStickerMainUrl());
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_sticker);
          break;
        }
        case GIF_MESSAGE_SENT:

        case GIF_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Gif.value);

          replyMessageDetails.put("parentMessageBody",
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_gif));
          replyMessageDetails.put("parentMessageAttachmentUrl", messagesModel.getGifMainUrl());
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_gif);
          break;
        }
        case WHITEBOARD_MESSAGE_SENT:

        case WHITEBOARD_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Whiteboard.value);

          replyMessageDetails.put("parentMessageBody",
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_whiteboard));
          replyMessageDetails.put("parentMessageAttachmentUrl", messagesModel.getWhiteboardThumbnailUrl());
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_whiteboard);
          break;
        }
        case LOCATION_MESSAGE_SENT:

        case LOCATION_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Location.value);

          replyMessageDetails.put("parentMessageBody",
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_location));
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_location);
          break;
        }
        case CONTACT_MESSAGE_SENT:

        case CONTACT_MESSAGE_RECEIVED: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Contact.value);

          replyMessageDetails.put("parentMessageBody",
              IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_contact));
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_contact);
          break;
        }
        default:
          replyMessage = null;
      }
      replyMessage.put("replyMessage",replyMessageDetails);
    } catch (JSONException ignore) {
      replyMessage = null;
    }

    return replyMessage;
  }
}
