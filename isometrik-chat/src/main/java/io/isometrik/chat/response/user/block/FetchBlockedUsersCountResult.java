package io.isometrik.chat.response.user.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the fetch blocked users count request.
 */
public class FetchBlockedUsersCountResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("blockedUsersCount")
  @Expose
  private int blockedUsersCount;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets blocked users count.
   *
   * @return the blocked users count
   */
  public int getBlockedUsersCount() {
    return blockedUsersCount;
  }
}
