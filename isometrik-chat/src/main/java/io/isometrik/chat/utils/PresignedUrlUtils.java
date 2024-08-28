package io.isometrik.chat.utils;

import org.apache.commons.io.FilenameUtils;

/**
 * The helper class to return mediaId based on media type for presigned urls.
 */
public class PresignedUrlUtils {

  /**
   * Gets media id.
   *
   * @param mediaType the media type
   * @return the media id
   */
  public static String getMediaId(int mediaType) {

    String mediaNamePrefix;
    switch (mediaType) {
      case 0: {
        mediaNamePrefix = "Image-";
        break;
      }

      case 1: {
        mediaNamePrefix = "Video-";
        break;
      }

      case 2: {
        mediaNamePrefix = "Audio-";
        break;
      }

      case 3: {
        mediaNamePrefix = "File-";
        break;
      }

      default:
        mediaNamePrefix = "";
    }
    return mediaNamePrefix + System.currentTimeMillis();
  }

  /**
   * Gets media name.
   *
   * @param mediaPath the media path
   * @return the media name
   */
  public static String getMediaName(String mediaPath) {
    return FilenameUtils.getName(mediaPath);
  }
}
