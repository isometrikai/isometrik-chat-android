package io.isometrik.chat.response.upload;

/**
 * The helper class to parse the response of the upload user image request.
 */
public class UploadUserImageResult {
  private final String requestId;

  /**
   * Instantiates a new Upload user image result.
   *
   * @param requestId the request id
   */
  public UploadUserImageResult(String requestId) {
    this.requestId = requestId;
  }

  /**
   * Gets request id.
   *
   * @return the request id
   */
  public String getRequestId() {
    return requestId;
  }
}

