package io.isometrik.chat.response.upload;

/**
 * The helper class to parse the response of the cancel user image upload request.
 */
public class CancelUserImageUploadResult {
  private final String message;

  /**
   * Instantiates a new Cancel user image upload result.
   *
   * @param message the message
   */
  public CancelUserImageUploadResult(String message) {
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
