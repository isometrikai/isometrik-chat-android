package io.isometrik.chat.response.conversation.cleanup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the clear conversation request.
 */
public class ClearConversationResult {
  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }
}
