package io.isometrik.ui.messages.search.utils;

import android.content.Context;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.MessagesModel;
import java.util.ArrayList;
import java.util.List;

/**
 * The helper class for generating searchable tags for a message based on type.
 */
public class SearchTagUtils {

  /**
   * Generate search tags list.
   *
   * @param messagesModel the messages model
   * @param mediaName the media name
   * @return the list
   */
  public static List<String> generateSearchTags(MessagesModel messagesModel, String mediaName) {
    List<String> searchableTags = new ArrayList<>();
    Context context = IsometrikChatSdk.getInstance().getContext();
    switch (messagesModel.getCustomMessageType()) {
      case TEXT_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_text));
        searchableTags.add(messagesModel.getTextMessage().toString());
        break;
      }
      case PHOTO_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_photo));
        if (mediaName != null) {
          searchableTags.add(mediaName);
        }
        break;
      }
      case VIDEO_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_video));
        if (mediaName != null) {
          searchableTags.add(mediaName);
        }
        break;
      }
      case AUDIO_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_audio));
        searchableTags.add(messagesModel.getAudioName());
        break;
      }
      case FILE_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_file));
        searchableTags.add(messagesModel.getFileName());
        break;
      }
      case STICKER_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_sticker));
        if (mediaName != null) {
          searchableTags.add(mediaName);
        }
        break;
      }
      case GIF_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_gif));
        if (mediaName != null) {
          searchableTags.add(mediaName);
        }
        break;
      }
      case WHITEBOARD_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_whiteboard));
        break;
      }
      case LOCATION_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_location));
        searchableTags.add(messagesModel.getLocationName());
        searchableTags.add(messagesModel.getLocationDescription());
        break;
      }
      case CONTACT_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_contact));
        searchableTags.add(messagesModel.getContactName());
        searchableTags.add(messagesModel.getContactIdentifier());
        break;
      }
      case REPLAY_MESSAGE_SENT: {
        searchableTags.add(context.getString(R.string.ism_search_tag_text));
        searchableTags.add(messagesModel.getTextMessage().toString());
      }
    }
    return searchableTags;
  }

  /**
   * Generate search tags list.
   *
   * @param attachment the attachment
   * @param isWhiteboard the is whiteboard
   * @return the list
   */
  public static List<String> generateSearchTags(Attachment attachment, boolean isWhiteboard) {
    List<String> searchableTags = new ArrayList<>();

    Context context = IsometrikChatSdk.getInstance().getContext();
    switch (attachment.getAttachmentType()) {

      case Image: {
        if (isWhiteboard) {
          searchableTags.add(context.getString(R.string.ism_search_tag_whiteboard));
        } else {
          searchableTags.add(context.getString(R.string.ism_search_tag_photo));
          searchableTags.add(attachment.getName());
        }
        break;
      }
      case Video: {
        searchableTags.add(context.getString(R.string.ism_search_tag_video));
        searchableTags.add(attachment.getName());
        break;
      }
      case Audio: {
        searchableTags.add(context.getString(R.string.ism_search_tag_audio));
        searchableTags.add(attachment.getName());
        break;
      }
      case File: {
        searchableTags.add(context.getString(R.string.ism_search_tag_file));
        searchableTags.add(attachment.getName());
        break;
      }
    }
    return searchableTags;
  }
}
