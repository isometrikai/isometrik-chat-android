package io.isometrik.chat.response.deliverystatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.user.utils.PendingDeliveryToReadByUser;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch message pending read status request.
 */
public class FetchMessagePendingReadStatusResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("users")
  @Expose
  private ArrayList<PendingDeliveryToReadByUser> readPendingByUsers;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets read pending by users.
   *
   * @return the read pending by users
   */
  public ArrayList<PendingDeliveryToReadByUser> getReadPendingByUsers() {
    return readPendingByUsers;
  }
}
