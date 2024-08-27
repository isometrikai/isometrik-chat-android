package io.isometrik.ui.messages.chat.utils.attachmentutils;

import io.isometrik.chat.enums.AttachmentMessageType;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.utils.AttachmentMetadata;

/**
 * The helper class to prepare and Attachment object for remote api param based on media type of
 * message to be sent.
 */
public class PrepareAttachmentHelper {

  /**
   * Prepare location attachment attachment.
   *
   * @param locationTitle the location title
   * @param locationAddress the location address
   * @param latitude the latitude
   * @param longitude the longitude
   * @return the attachment
   */
  public static Attachment prepareLocationAttachment(String locationTitle, String locationAddress,
      double latitude, double longitude) {

    return (validateString(locationTitle) || validateString(locationAddress)) ? null
        : new Attachment(locationTitle, locationAddress, latitude, longitude,
            AttachmentMessageType.Location);
  }

  /**
   * Prepare sticker attachment attachment.
   *
   * @param mediaName the media name
   * @param mediaUrl the media url
   * @param thumbnailUrl the thumbnail url
   * @param stillUrl the still url
   * @return the attachment
   */
  public static Attachment prepareStickerAttachment(String mediaName, String mediaUrl,
      String thumbnailUrl, String stillUrl) {
    return (validateString(mediaName)
        || validateString(mediaUrl)
        || validateString(thumbnailUrl)
        || validateString(stillUrl)) ? null
        : new Attachment(mediaName, mediaUrl, thumbnailUrl, stillUrl,
            AttachmentMessageType.Sticker);
  }

  /**
   * Prepare gif attachment attachment.
   *
   * @param mediaName the media name
   * @param mediaUrl the media url
   * @param thumbnailUrl the thumbnail url
   * @param stillUrl the still url
   * @return the attachment
   */
  public static Attachment prepareGifAttachment(String mediaName, String mediaUrl,
      String thumbnailUrl, String stillUrl) {
    return (validateString(mediaName)
        || validateString(mediaUrl)
        || validateString(thumbnailUrl)
        || validateString(stillUrl)) ? null
        : new Attachment(mediaName, mediaUrl, thumbnailUrl, stillUrl, AttachmentMessageType.Gif);
  }

  /**
   * Prepare media attachment attachment.
   *
   * @param mediaId the media id
   * @param mediaUrl the media url
   * @param thumbnailUrl the thumbnail url
   * @param mediaPath the media path
   * @param attachmentMessageType the attachment message type
   * @return the attachment
   */
  public static Attachment prepareMediaAttachment(String mediaId, String mediaUrl,
      String thumbnailUrl, String mediaPath, AttachmentMessageType attachmentMessageType) {

    AttachmentMetadata attachmentMetadata = new AttachmentMetadata(mediaPath);

    return new Attachment(mediaId, mediaUrl, attachmentMetadata.getFileName(),
        attachmentMetadata.getMimeType(), attachmentMetadata.getExtension(), thumbnailUrl,
        attachmentMetadata.getSize(), attachmentMessageType);
  }

  /**
   * Prepare media attachment attachment.
   *
   * @param mediaId the media id
   * @param mediaUrl the media url
   * @param thumbnailUrl the thumbnail url
   * @param attachmentMessageType the attachment message type
   * @param fileName the file name
   * @param mimeType the mime type
   * @param extension the extension
   * @param size the size
   * @return the attachment
   */
  public static Attachment prepareMediaAttachment(String mediaId, String mediaUrl,
      String thumbnailUrl, AttachmentMessageType attachmentMessageType, String fileName,
      String mimeType, String extension, long size) {

    return new Attachment(mediaId, mediaUrl, fileName, mimeType, extension, thumbnailUrl, size,
        attachmentMessageType);
  }

  private static boolean validateString(String data) {
    return data == null || data.isEmpty();
  }
}
