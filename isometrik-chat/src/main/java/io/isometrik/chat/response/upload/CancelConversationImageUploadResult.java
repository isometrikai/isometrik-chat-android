package io.isometrik.chat.response.upload;

/**
 * The helper class to parse the response of the cancel conversation image upload request.
 */
public class CancelConversationImageUploadResult {
  private final String message;

  /**
   * Instantiates a new Cancel conversation image upload result.
   *
   * @param message the message
   */
  public CancelConversationImageUploadResult(String message) {
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
