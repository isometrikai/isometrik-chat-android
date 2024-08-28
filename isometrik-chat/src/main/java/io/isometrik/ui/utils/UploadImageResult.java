package io.isometrik.ui.utils;

import java.util.Map;

/**
 * The interface containing methods for upload image on cloudinary results.
 */
public abstract class UploadImageResult {

  /**
   * Upload success.
   *
   * @param requestId the request id
   * @param resultData the result data
   */
  public abstract void uploadSuccess(String requestId,@SuppressWarnings("rawtypes") Map resultData);

  /*
   * Upload error.
   *
   * @param requestId the request id
   * @param error the error
   */
  //public abstract void uploadError(String requestId, ErrorInfo error);
}
