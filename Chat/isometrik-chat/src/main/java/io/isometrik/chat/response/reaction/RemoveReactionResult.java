package io.isometrik.chat.response.reaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the remove reaction request.
 */
public class RemoveReactionResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("reactionsCount")
  @Expose
  private int reactionsCount;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets reactions count.
   *
   * @return the reactions count
   */
  public int getReactionsCount() {
    return reactionsCount;
  }
}
