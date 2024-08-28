package io.isometrik.chat.response.conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the fetch unread conversations count request.
 */
public class FetchUnreadConversationsCountResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("count")
  @Expose
  private int conversationsCount;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets conversations count.
   *
   * @return the conversations count
   */
  public int getConversationsCount() {
    return conversationsCount;
  }
}
