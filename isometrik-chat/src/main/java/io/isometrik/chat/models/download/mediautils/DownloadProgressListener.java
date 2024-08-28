package io.isometrik.chat.models.download.mediautils;

/**
 * The interface download progress listener.
 */
public interface DownloadProgressListener {

  /**
   * On file save progress.
   *
   * @param messageId the message id
   * @param fileSizeDownloaded the file size downloaded
   * @param fileSize the file size
   */
  void onFileSaveProgress(String messageId, long fileSizeDownloaded, long fileSize);

  /**
   * On download progress.
   *
   * @param messageId the message id
   * @param bytesRead the bytes read
   * @param contentLength the content length
   * @param done the done
   */
  void onDownloadProgress(String messageId, long bytesRead, long contentLength, boolean done);
}
