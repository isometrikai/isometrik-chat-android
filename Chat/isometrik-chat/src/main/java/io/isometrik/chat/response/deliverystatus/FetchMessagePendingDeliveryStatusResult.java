package io.isometrik.chat.response.deliverystatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.user.utils.PendingDeliveryToReadByUser;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch message pending delivery status request.
 */
public class FetchMessagePendingDeliveryStatusResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("users")
  @Expose
  private ArrayList<PendingDeliveryToReadByUser> deliveryPendingToUsers;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets delivery pending to users.
   *
   * @return the delivery pending to users
   */
  public ArrayList<PendingDeliveryToReadByUser> getDeliveryPendingToUsers() {
    return deliveryPendingToUsers;
  }
}
