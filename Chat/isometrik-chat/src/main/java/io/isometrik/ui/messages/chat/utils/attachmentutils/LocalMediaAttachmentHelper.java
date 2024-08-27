package io.isometrik.ui.messages.chat.utils.attachmentutils;

import io.isometrik.chat.utils.AttachmentMetadata;

/**
 * The helper class to get various attachment details from a local media path.
 */
public class LocalMediaAttachmentHelper {

  private final String mediaUrl, thumbnailUrl, mediaName, mimeType, mediaExtension;
  private final String sizeInMb;
  private final long fileSize;

  /**
   * Instantiates a new Local media attachment helper.
   *
   * @param localMediaPath the local media path
   */
  public LocalMediaAttachmentHelper(String localMediaPath) {
    mediaUrl = localMediaPath;
    thumbnailUrl = localMediaPath;

    AttachmentMetadata attachmentMetadata = new AttachmentMetadata(localMediaPath);
    sizeInMb = attachmentMetadata.getSizeInMb();
    mediaName = attachmentMetadata.getFileName();
    mimeType = attachmentMetadata.getMimeType();
    mediaExtension = attachmentMetadata.getExtension();
    fileSize=attachmentMetadata.getSize();
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
   * Gets size in mb.
   *
   * @return the size in mb
   */
  public String getSizeInMb() {
    return sizeInMb;
  }

  /**
   * Gets media name.
   *
   * @return the media name
   */
  public String getMediaName() {
    return mediaName;
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
   * Gets media extension.
   *
   * @return the media extension
   */
  public String getMediaExtension() {
    return mediaExtension;
  }

  /**
   * Gets file size.
   *
   * @return the file size
   */
  public long getFileSize() {
    return fileSize;
  }
}
