package io.isometrik.chat.events.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The type Delete user event.
 */
public class DeleteUserEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("sentAt")
  @Expose
  private long sentAt;

  /**
   * Gets action.
   *
   * @return the action
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets sent at.
   *
   * @return the sent at
   */
  public long getSentAt() {
    return sentAt;
  }
}
