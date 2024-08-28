package io.isometrik.chat.response.message.utils.fetchmessages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the details of the read by or delivered to user.
 */
public class ReadByDeliveredToUser {
  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  public long getTimestamp() {
    return timestamp;
  }
}
