package io.isometrik.chat.response.user.block;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.user.utils.NonBlockedUser;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch non blocked users request.
 */
public class FetchNonBlockedUsersResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("users")
  @Expose
  private ArrayList<NonBlockedUser> nonBlockedUsers;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets non blocked users.
   *
   * @return the non blocked users
   */
  public ArrayList<NonBlockedUser> getNonBlockedUsers() {
    return nonBlockedUsers;
  }
}
