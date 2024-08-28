package io.isometrik.chat.response.upload;

/**
 * The helper class to parse the response of the upload conversation image request.
 */
public class UploadConversationImageResult {
  private final String requestId;

  /**
   * Instantiates a new Upload conversation image result.
   *
   * @param requestId the request id
   */
  public UploadConversationImageResult(String requestId) {
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
