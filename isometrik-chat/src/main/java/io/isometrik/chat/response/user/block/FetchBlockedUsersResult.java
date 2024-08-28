package io.isometrik.chat.response.user.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.user.utils.BlockedUser;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch blocked users request.
 */
public class FetchBlockedUsersResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("users")
  @Expose
  private ArrayList<BlockedUser> blockedUsers;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets blocked users.
   *
   * @return the blocked users
   */
  public ArrayList<BlockedUser> getBlockedUsers() {
    return blockedUsers;
  }
}
