package io.isometrik.chat.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the send message request.
 */
public class SendMessageResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("messageId")
  @Expose
  private String messageId;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }
}
