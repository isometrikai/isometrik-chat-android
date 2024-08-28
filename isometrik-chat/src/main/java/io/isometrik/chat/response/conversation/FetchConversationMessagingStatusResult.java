package io.isometrik.chat.response.conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the fetch conversation messaging status request.
 */
public class FetchConversationMessagingStatusResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("messagingEnabled")
  @Expose
  private boolean messagingEnabled;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Is messaging enabled boolean.
   *
   * @return the boolean
   */
  public boolean isMessagingEnabled() {
    return messagingEnabled;
  }
}
