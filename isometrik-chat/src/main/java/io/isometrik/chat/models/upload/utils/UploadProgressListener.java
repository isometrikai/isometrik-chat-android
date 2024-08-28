package io.isometrik.chat.models.upload.utils;

/**
 * The interface upload progress listener.
 */
public interface UploadProgressListener {

  /**
   * On upload progress.
   *
   * @param requestId the request id
   * @param requestGroupId the request group id
   * @param bytesWritten the bytes written
   * @param contentLength the content length
   */
  void onUploadProgress(String requestId,String requestGroupId, long bytesWritten, long contentLength);
}
