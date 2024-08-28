package io.isometrik.ui.messages.search.utils;

import android.content.Context;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.ui.IsometrikUiSdk;
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
    Context context = IsometrikUiSdk.getInstance().getContext();
    switch (messagesModel.getCustomMessageType()) {
      case TextSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_text));
        searchableTags.add(messagesModel.getTextMessage().toString());
        break;
      }
      case PhotoSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_photo));
        if (mediaName != null) {
          searchableTags.add(mediaName);
        }
        break;
      }
      case VideoSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_video));
        if (mediaName != null) {
          searchableTags.add(mediaName);
        }
        break;
      }
      case AudioSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_audio));
        searchableTags.add(messagesModel.getAudioName());
        break;
      }
      case FileSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_file));
        searchableTags.add(messagesModel.getFileName());
        break;
      }
      case StickerSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_sticker));
        if (mediaName != null) {
          searchableTags.add(mediaName);
        }
        break;
      }
      case GifSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_gif));
        if (mediaName != null) {
          searchableTags.add(mediaName);
        }
        break;
      }
      case WhiteboardSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_whiteboard));
        break;
      }
      case LocationSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_location));
        searchableTags.add(messagesModel.getLocationName());
        searchableTags.add(messagesModel.getLocationDescription());
        break;
      }
      case ContactSent: {
        searchableTags.add(context.getString(R.string.ism_search_tag_contact));
        searchableTags.add(messagesModel.getContactName());
        searchableTags.add(messagesModel.getContactIdentifier());
        break;
      }
      case ReplaySent: {
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

    Context context = IsometrikUiSdk.getInstance().getContext();
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
