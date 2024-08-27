package io.isometrik.chat.response.membershipcontrol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the join as observer request.
 */
public class JoinAsObserverResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets members count.
   *
   * @return the members count
   */
  public int getMembersCount() {
    return membersCount;
  }
}
