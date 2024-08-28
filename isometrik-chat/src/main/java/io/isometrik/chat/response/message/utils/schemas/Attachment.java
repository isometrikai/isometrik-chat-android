package io.isometrik.chat.response.message.utils.schemas;

import com.google.gson.Gson;
import io.isometrik.chat.enums.AttachmentMessageType;
import io.isometrik.chat.enums.AttachmentSchemaType;
import org.json.JSONObject;

/**
 * The helper class for schemas of the message attachments.
 */
public class Attachment {

  private AttachmentSchemaType attachmentSchemaType;
  private AttachmentMessageType attachmentMessageType;
  private Integer attachmentType;
  /**
   * For Image/video/File attachment
   */

  private String mediaId;
  private String mediaUrl;
  private String name;
  private String mimeType;
  private String extension;
  private String thumbnailUrl;
  private Long size;

  /**
   * Instantiates a new Attachment.
   *
   * @param mediaId the media id
   * @param mediaUrl the media url
   * @param name the name
   * @param mimeType the mime type
   * @param extension the extension
   * @param thumbnailUrl the thumbnail url
   * @param size the size
   * @param attachmentMessageType the attachment message type
   */
  public Attachment(String mediaId, String mediaUrl, String name, String mimeType, String extension,
      String thumbnailUrl, Long size,  AttachmentMessageType attachmentMessageType) {
    this.mediaId = mediaId;
    this.mediaUrl = mediaUrl;
    this.name = name;
    this.mimeType = mimeType;
    this.extension = extension;
    this.thumbnailUrl = thumbnailUrl;
    this.size = size;
    this.attachmentMessageType = attachmentMessageType;
    attachmentType = attachmentMessageType.getValue();
    attachmentSchemaType = AttachmentSchemaType.Media;
  }

  /**
   * Gets media id.
   *
   * @return the media id
   */
  public String getMediaId() {
    return mediaId;
  }

  /**
   * Gets mime type.
   *
   * @return the mime type
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Gets extension.
   *
   * @return the extension
   */
  public String getExtension() {
    return extension;
  }

  /**
   * Gets attachment schema type.
   *
   * @return the attachment schema type
   */
  public AttachmentSchemaType getAttachmentSchemaType() {
    return attachmentSchemaType;
  }

  /**
   * Gets size.
   *
   * @return the size
   */
  public Long getSize() {
    return size;
  }

  /**
   * For location attachment
   */
  private double latitude;
  private String title;
  private double longitude;
  private String address;

  /**
   * Instantiates a new Attachment.
   *
   * @param title the title
   * @param address the address
   * @param latitude the latitude
   * @param longitude the longitude
   * @param attachmentMessageType the attachment message type
   */
  public Attachment(String title, String address, double latitude, double longitude,  AttachmentMessageType attachmentMessageType) {

    this.title = title;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
    this.attachmentMessageType = attachmentMessageType;
    attachmentType = attachmentMessageType.getValue();
    attachmentSchemaType = AttachmentSchemaType.Location;
  }

  /**
   * Gets title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Gets address.
   *
   * @return the address
   */
  public String getAddress() {
    return address;
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
   * Gets latitude.
   *
   * @return the latitude
   */
  public Double getLatitude() {
    return latitude;
  }

  /**
   * For gif/sticker attachment
   */
  private String stillUrl;

  /**
   * Instantiates a new Attachment.
   *
   * @param mediaName the media name
   * @param mediaUrl the media url
   * @param thumbnailUrl the thumbnail url
   * @param stillUrl the still url
   * @param attachmentMessageType the attachment message type
   */
  public Attachment(String mediaName, String mediaUrl, String thumbnailUrl, String stillUrl,  AttachmentMessageType attachmentMessageType) {
    this.name = mediaName;
    this.mediaUrl = mediaUrl;
    this.thumbnailUrl = thumbnailUrl;
    this.stillUrl = stillUrl;
    this.attachmentMessageType = attachmentMessageType;
    attachmentType = attachmentMessageType.getValue();
    attachmentSchemaType = AttachmentSchemaType.GifSticker;
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
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
   * Gets thumbnail url.
   *
   * @return the thumbnail url
   */
  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  /**
   * Gets still url.
   *
   * @return the still url
   */
  public String getStillUrl() {
    return stillUrl;
  }

  /**
   * For admin messages
   */
  private String initiatorId;
  private Object metaData;

  /**
   * Instantiates a new Attachment.
   *
   * @param initiatorId the initiator id
   * @param metaData the meta data
   */
  public Attachment(String initiatorId, Object metaData) {
    this.initiatorId = initiatorId;
    this.metaData = metaData;
  }

  /**
   * Gets initiator id.
   *
   * @return the initiator id
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets meta data.
   *
   * @return the meta data
   */
  public JSONObject getMetaData() {

    try {
      return new JSONObject(new Gson().toJson(metaData));
    } catch (Exception ignore) {
      return new JSONObject();
    }
  }

  /**
   * Gets attachment type.
   *
   * @return the attachment type
   */
  public AttachmentMessageType getAttachmentType() {
    return attachmentMessageType;
  }
}