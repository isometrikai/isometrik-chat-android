package io.isometrik.chat.utils;

import android.webkit.MimeTypeMap;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * The helper class to fetch details of an attachment from a file path.
 */
public class AttachmentMetadata {

  private String mimeType, extension;
  private final long size;
  private final String fileName, sizeInMb;

  /**
   * Instantiates a new Attachment metadata.
   *
   * @param mediaPath the media path
   */
  public AttachmentMetadata(String mediaPath) {

    size = new File(mediaPath).length();
    sizeInMb = FileUtils.byteCountToDisplaySize(size);

    try {
      extension = FilenameUtils.getExtension(mediaPath);
    } catch (IllegalArgumentException ignore) {
      extension = "";
    }

    //if (extension.isEmpty()) {
    //  //Media type not supported as failed to get extension
    //  completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIA_NOT_SUPPORTED);
    //} else {
    mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
    if (mimeType == null) {
      //try {
      //  contentType = new Tika().detect(file);
      //} catch (IOException ignore) {
      mimeType = "application/octet-stream";
      //}
    }

    fileName = FilenameUtils.getName(mediaPath);
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
   * Gets file name.
   *
   * @return the file name
   */
  public String getFileName() {
    return fileName;
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
   * Gets size.
   *
   * @return the size
   */
  public long getSize() {
    return size;
  }
}