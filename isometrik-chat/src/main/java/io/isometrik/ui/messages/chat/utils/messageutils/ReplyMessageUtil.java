package io.isometrik.ui.messages.chat.utils.messageutils;

import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.chat.utils.enums.CustomMessageTypes;
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
//  public static JSONObject prepareReplyMessageMetadata(MessagesModel messagesModel) {
//    JSONObject replyMessageDetails = new JSONObject();
//    try {
//      switch (messagesModel.getCustomMessageType()) {
//        case TextSent:
//        case TextReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.Text.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage", messagesModel.getTextMessage());
//          replyMessageDetails.put("originalMessagePlaceHolderImage", null);
//          break;
//        }
//        case PhotoSent:
//
//        case PhotoReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.Image.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage",
//              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_photo));
//          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_picture);
//          break;
//        }
//        case VideoSent:
//
//        case VideoReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.Video.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage",
//              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_video));
//          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_video);
//          break;
//        }
//        case AudioSent:
//
//        case AudioReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.Audio.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage",
//              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_audio_recording));
//          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_mic);
//          break;
//        }
//        case FileSent:
//
//        case FileReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.File.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage",
//              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_file));
//          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_file);
//          break;
//        }
//        case StickerSent:
//
//        case StickerReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.Sticker.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage",
//              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_sticker));
//          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_sticker);
//          break;
//        }
//        case GifSent:
//
//        case GifReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.Gif.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage",
//              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_gif));
//          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_gif);
//          break;
//        }
//        case WhiteboardSent:
//
//        case WhiteboardReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.Whiteboard.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage",
//              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_whiteboard));
//          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_whiteboard);
//          break;
//        }
//        case LocationSent:
//
//        case LocationReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.Location.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage",
//              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_location));
//          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_location);
//          break;
//        }
//        case ContactSent:
//
//        case ContactReceived: {
//          replyMessageDetails.put("originalMessageSenderName", messagesModel.getSenderName());
//          replyMessageDetails.put("originalMessageSentAt", messagesModel.getSentAt());
//          replyMessageDetails.put("originalMessageType", CustomMessageTypes.Contact.getValue());
//          replyMessageDetails.put("originalMessageSenderImageUrl",
//              messagesModel.getSenderImageUrl());
//
//          replyMessageDetails.put("originalMessage",
//              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_contact));
//          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_contact);
//          break;
//        }
//        default:
//          replyMessageDetails = null;
//      }
//    } catch (JSONException ignore) {
//      replyMessageDetails = null;
//    }
//    return replyMessageDetails;
//  }
  public static JSONObject prepareReplyMessageMetadata(MessagesModel messagesModel) {
    JSONObject replyMessage = new JSONObject();
    JSONObject replyMessageDetails = new JSONObject();
    try {
      switch (messagesModel.getCustomMessageType()) {
        case TextSent:
        case TextReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Text.getValue());

          replyMessageDetails.put("parentMessageBody", messagesModel.getTextMessage());
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", null);
          break;
        }
        case PhotoSent:

        case PhotoReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Image.getValue());

          replyMessageDetails.put("parentMessageBody",
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_photo));
          replyMessageDetails.put("parentMessageAttachmentUrl", messagesModel.getPhotoThumbnailUrl());
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_picture);
          break;
        }
        case VideoSent:

        case VideoReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Video.getValue());

          replyMessageDetails.put("parentMessageBody",
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_video));
          replyMessageDetails.put("parentMessageAttachmentUrl",messagesModel.getVideoThumbnailUrl());
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_video);
          break;
        }
        case AudioSent:

        case AudioReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Audio.getValue());

          replyMessageDetails.put("parentMessageBody",
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_audio_recording));
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_mic);
          break;
        }
        case FileSent:

        case FileReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.File.getValue());

          replyMessageDetails.put("parentMessageBody",
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_file));
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_file);
          break;
        }
        case StickerSent:

        case StickerReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Sticker.getValue());

          replyMessageDetails.put("parentMessageBody",
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_sticker));
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_sticker);
          break;
        }
        case GifSent:

        case GifReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Gif.getValue());

          replyMessageDetails.put("parentMessageBody",
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_gif));
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_gif);
          break;
        }
        case WhiteboardSent:

        case WhiteboardReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Whiteboard.getValue());

          replyMessageDetails.put("parentMessageBody",
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_whiteboard));
          replyMessageDetails.put("parentMessageAttachmentUrl", messagesModel.getWhiteboardThumbnailUrl());
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_whiteboard);
          break;
        }
        case LocationSent:

        case LocationReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Location.getValue());

          replyMessageDetails.put("parentMessageBody",
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_location));
          replyMessageDetails.put("parentMessageAttachmentUrl", null);
          replyMessageDetails.put("parentMessageInitiator", messagesModel.isSentMessage());
          replyMessageDetails.put("parentMessageUserId", "");
          replyMessageDetails.put("originalMessagePlaceHolderImage", R.drawable.ism_ic_location);
          break;
        }
        case ContactSent:

        case ContactReceived: {
          replyMessageDetails.put("parentMessageUserName", messagesModel.getSenderName());
          replyMessageDetails.put("parentMessageSentAt", messagesModel.getSentAt());
          replyMessageDetails.put("parentMessageMessageType", CustomMessageTypes.Contact.getValue());

          replyMessageDetails.put("parentMessageBody",
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_contact));
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
