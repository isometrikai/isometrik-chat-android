package io.isometrik.chat.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the delete user request.
 */
public class DeleteUserResult {
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
