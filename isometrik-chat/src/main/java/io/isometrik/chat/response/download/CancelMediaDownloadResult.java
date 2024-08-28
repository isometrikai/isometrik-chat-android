package io.isometrik.chat.response.download;

/**
 * The helper class to parse the response of the cancel media download request.
 */
public class CancelMediaDownloadResult {
  private final String message;

  /**
   * Instantiates a new Cancel media download result.
   *
   * @param message the message
   */
  public CancelMediaDownloadResult(String message) {
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
