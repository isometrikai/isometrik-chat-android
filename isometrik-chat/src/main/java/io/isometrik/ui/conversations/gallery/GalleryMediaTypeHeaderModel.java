package io.isometrik.ui.conversations.gallery;

import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;

/**
 * The helper class for inflating items in the list of media items types on gallery page opened on
 * click of view more from conversation details page.
 */
public class GalleryMediaTypeHeaderModel {

  private String mediaTypeText;
  private Integer mediaTypeIcon;
  private final String customMediaMessageType;

  /**
   * Instantiates a new Gallery media type header model.
   *
   * @param customType the custom type
   */
  public GalleryMediaTypeHeaderModel(String customType) {
    this.customMediaMessageType = customType;
    switch (customType) {

      case "AttachmentMessage:Image":
        mediaTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photos);
        mediaTypeIcon = R.drawable.ism_ic_picture;
        break;
      case "AttachmentMessage:Video":
        mediaTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_videos);
        mediaTypeIcon = R.drawable.ism_ic_video;
        break;
      case "AttachmentMessage:Audio":
        mediaTypeText =
            IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_audio_recordings);
        mediaTypeIcon = R.drawable.ism_ic_mic;
        break;

      case "AttachmentMessage:File":
        mediaTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_files);
        mediaTypeIcon = R.drawable.ism_ic_files;
        break;

      case "AttachmentMessage:Sticker":
        mediaTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_stickers);
        mediaTypeIcon = R.drawable.ism_ic_sticker;
        break;

      case "AttachmentMessage:Gif":
        mediaTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_gifs);
        mediaTypeIcon = R.drawable.ism_ic_gif;
        break;

      case "AttachmentMessage:Whiteboard":
        mediaTypeText =
            IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_whiteboards);
        mediaTypeIcon = R.drawable.ism_ic_whiteboard;
        break;

      case "AttachmentMessage:Location":
        mediaTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_locations);
        mediaTypeIcon = R.drawable.ism_ic_location;
        break;

      case "AttachmentMessage:Contact":
        mediaTypeText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_contacts);
        mediaTypeIcon = R.drawable.ism_ic_contact;
        break;
    }
  }

  /**
   * Gets media type text.
   *
   * @return the media type text
   */
  public String getMediaTypeText() {
    return mediaTypeText;
  }

  /**
   * Gets media type icon.
   *
   * @return the media type icon
   */
  public Integer getMediaTypeIcon() {
    return mediaTypeIcon;
  }

  /**
   * Gets custom media message type.
   *
   * @return the custom media message type
   */
  public String getCustomMediaMessageType() {
    return customMediaMessageType;
  }
}
