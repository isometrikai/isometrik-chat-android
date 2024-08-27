package io.isometrik.chat.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the fetch messages count request.
 */
public class FetchMessagesCountResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("messagesCount")
  @Expose
  private int messagesCount;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets messages count.
   *
   * @return the messages count
   */
  public int getMessagesCount() {
    return messagesCount;
  }
}
