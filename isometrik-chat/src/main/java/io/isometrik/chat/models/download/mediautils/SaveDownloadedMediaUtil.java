package io.isometrik.chat.models.download.mediautils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.ResponseBody;

/**
 * The helper class to save downloaded media to disk.
 */
public class SaveDownloadedMediaUtil {
  /**
   * Write downloaded response body to disk boolean.
   *
   * @param body the body
   * @param mediaPath the media path
   * @param messageId the message id
   * @param downloadProgressListener the download progress listener
   * @return the boolean
   */
  public static boolean writeDownloadedResponseBodyToDisk(ResponseBody body, String mediaPath,
      String messageId, DownloadProgressListener downloadProgressListener) {
    try {
      File mediaFile = new File(mediaPath);

      InputStream inputStream = null;
      OutputStream outputStream = null;

      try {
        byte[] fileReader = new byte[4096];

        long fileSize = body.contentLength();
        long fileSizeDownloaded = 0;

        inputStream = body.byteStream();
        outputStream = new FileOutputStream(mediaFile);

        while (true) {
          int read = inputStream.read(fileReader);

          if (read == -1) {
            break;
          }

          outputStream.write(fileReader, 0, read);

          fileSizeDownloaded += read;
          if (downloadProgressListener != null) {
            downloadProgressListener.onFileSaveProgress(messageId, fileSizeDownloaded, fileSize);
          }
          //Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
        }

        outputStream.flush();

        return true;
      } catch (IOException e) {
        return false;
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }

        if (outputStream != null) {
          outputStream.close();
        }
      }
    } catch (IOException e) {
      return false;
    }
  }
}
