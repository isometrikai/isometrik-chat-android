package io.isometrik.ui.conversations.gallery;

import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.TimeUtil;
import io.isometrik.chat.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * The helper class for inflating items in the gallery on gallery page opened on click of
 * view more from conversation details page.
 */
public class GalleryModel {

  private String mediaUrl, mediaThumbnailUrl, mimeType, mediaTypeText, mediaDescription, mediaSize;
  private Integer mediaTypeIcon;
  private final String senderProfileImageUrl, senderName;
  private final String sentTime, customType;

  private Double latitude, longitude;
  private String contactName, contactIdentifier;
  private boolean isVideo = false;
  private JSONArray contactList = new JSONArray();

  /**
   * Instantiates a new Gallery model.
   *
   * @param message the message
   */
  public GalleryModel(Message message) {

    senderName = message.getSenderInfo().getUserName();

    senderProfileImageUrl = message.getSenderInfo().getUserProfileImageUrl();
    sentTime = TimeUtil.formatTimestampToOnlyDate(message.getSentAt());
    customType = message.getCustomType();

    switch (customType) {

      case "AttachmentMessage:Image": {
        mediaTypeText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_photo);
        mediaTypeIcon = R.drawable.ism_ic_picture;

        mediaUrl = message.getAttachments().get(0).getMediaUrl();
        mediaThumbnailUrl = message.getAttachments().get(0).getThumbnailUrl();
        mimeType = message.getAttachments().get(0).getMimeType();

        mediaSize = FileUtils.getSizeOfFile(message.getAttachments().get(0).getSize());
        break;
      }
      case "AttachmentMessage:Video": {
        mediaTypeText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_video);
        mediaTypeIcon = R.drawable.ism_ic_video;

        mediaUrl = message.getAttachments().get(0).getMediaUrl();
        mediaThumbnailUrl = message.getAttachments().get(0).getThumbnailUrl();
        mimeType = message.getAttachments().get(0).getMimeType();

        mediaSize = FileUtils.getSizeOfFile(message.getAttachments().get(0).getSize());
        isVideo = true;
        break;
      }
      case "AttachmentMessage:Audio": {
        mediaTypeText =
            IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_audio_recording);
        mediaTypeIcon = R.drawable.ism_ic_mic;
        mediaDescription = message.getAttachments().get(0).getName();
        mediaSize = FileUtils.getSizeOfFile(message.getAttachments().get(0).getSize());

        mediaUrl = message.getAttachments().get(0).getMediaUrl();
        mediaThumbnailUrl = message.getAttachments().get(0).getThumbnailUrl();
        mimeType = message.getAttachments().get(0).getMimeType();

        break;
      }
      case "AttachmentMessage:File": {
        mediaTypeText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_file);
        mediaTypeIcon = R.drawable.ism_ic_file;
        mediaDescription = message.getAttachments().get(0).getName();
        mediaSize = FileUtils.getSizeOfFile(message.getAttachments().get(0).getSize());

        mediaUrl = message.getAttachments().get(0).getMediaUrl();
        mediaThumbnailUrl = message.getAttachments().get(0).getThumbnailUrl();
        mimeType = message.getAttachments().get(0).getMimeType();

        break;
      }
      case "AttachmentMessage:Sticker": {
        mediaTypeText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_sticker);
        mediaTypeIcon = R.drawable.ism_ic_sticker;

        mediaUrl = message.getAttachments().get(0).getMediaUrl();
        mediaThumbnailUrl = message.getAttachments().get(0).getThumbnailUrl();
        mimeType = message.getAttachments().get(0).getMimeType();

        break;
      }
      case "AttachmentMessage:Gif": {
        mediaTypeText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_gif);
        mediaTypeIcon = R.drawable.ism_ic_gif;

        mediaUrl = message.getAttachments().get(0).getMediaUrl();
        mediaThumbnailUrl = message.getAttachments().get(0).getThumbnailUrl();
        mimeType = message.getAttachments().get(0).getMimeType();

        break;
      }
      case "AttachmentMessage:Whiteboard": {
        mediaTypeText =
            IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_whiteboard);
        mediaTypeIcon = R.drawable.ism_ic_whiteboard;

        mediaUrl = message.getAttachments().get(0).getMediaUrl();
        mediaThumbnailUrl = message.getAttachments().get(0).getThumbnailUrl();
        mimeType = message.getAttachments().get(0).getMimeType();

        mediaSize = FileUtils.getSizeOfFile(message.getAttachments().get(0).getSize());
        break;
      }
      case "AttachmentMessage:Location": {
        mediaTypeText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_location);
        mediaTypeIcon = R.drawable.ism_ic_location;
        mediaDescription = message.getAttachments().get(0).getTitle()
            + System.getProperty("line.separator")
            + message.getAttachments().get(0).getAddress();
        latitude = message.getAttachments().get(0).getLatitude();
        longitude = message.getAttachments().get(0).getLongitude();
        mediaUrl = Constants.LOCATION_PLACEHOLDER_IMAGE_URL;
        break;
      }
      case "AttachmentMessage:Contact": {
        mediaTypeText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_contact);
        mediaTypeIcon = R.drawable.ism_ic_contact;
        try {
          contactList =  message.getMetaData().getJSONArray("contacts");
          if(contactList.length() > 0){
            contactName =contactList.getJSONObject(0).getString("contactName");
            contactIdentifier = contactList.getJSONObject(0).getString("contactIdentifier");
            mediaDescription = contactName + System.getProperty("line.separator") + contactIdentifier;
          }

        } catch (JSONException e) {
          mediaDescription =
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_contact);
          contactName = "";
          contactIdentifier = "";
        } break;
      }
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
   * Gets sender profile image url.
   *
   * @return the sender profile image url
   */
  public String getSenderProfileImageUrl() {
    return senderProfileImageUrl;
  }

  /**
   * Gets sent time.
   *
   * @return the sent time
   */
  public String getSentTime() {
    return sentTime;
  }

  /**
   * Gets media url.
   *
   * @return the media url
   */
  public String getMediaUrl() {
    return mediaUrl;
  }

  /**
   * Gets media thumbnail url.
   *
   * @return the media thumbnail url
   */
  public String getMediaThumbnailUrl() {
    return mediaThumbnailUrl;
  }

  /**
   * Gets sender name.
   *
   * @return the sender name
   */
  public String getSenderName() {
    return senderName;
  }

  /**
   * Gets custom type.
   *
   * @return the custom type
   */
  public String getCustomType() {
    return customType;
  }

  /**
   * Gets media description.
   *
   * @return the media description
   */
  public String getMediaDescription() {
    return mediaDescription;
  }

  /**
   * Gets media size.
   *
   * @return the media size
   */
  public String getMediaSize() {
    return mediaSize;
  }

  /**
   * Gets latitude.
   *
   * @return the latitude
   */
  public Double getLatitude() {
    return latitude;
  }

  /**
   * Gets longitude.
   *
   * @return the longitude
   */
  public Double getLongitude() {
    return longitude;
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
   * Is video boolean.
   *
   * @return the boolean
   */
  public boolean isVideo() {
    return isVideo;
  }

  /**
   * Gets mime type.
   *
   * @return the mime type
   */
  public String getMimeType() {
    return mimeType;
  }

    public JSONArray getContactList() {
        return contactList;
    }
}
