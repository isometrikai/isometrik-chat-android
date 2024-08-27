package io.isometrik.chat.response.message.delivery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the mark message as delivered request.
 */
public class MarkMessageAsDeliveredResult {
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
