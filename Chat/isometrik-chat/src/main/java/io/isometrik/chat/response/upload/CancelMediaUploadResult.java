package io.isometrik.chat.response.upload;

/**
 * The helper class to parse the response of the cancel media upload request.
 */
public class CancelMediaUploadResult {
  private final String message;

  /**
   * Instantiates a new Cancel media upload result.
   *
   * @param message the message
   */
  public CancelMediaUploadResult(String message) {
    this.message = message;
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }
}
