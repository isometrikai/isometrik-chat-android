package io.isometrik.chat.response.deliverystatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.user.utils.DeliveredToOrReadByUser;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch message delivery status request.
 */
public class FetchMessageDeliveryStatusResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("users")
  @Expose
  private ArrayList<DeliveredToOrReadByUser> deliveredToUsers;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets delivered to users.
   *
   * @return the delivered to users
   */
  public ArrayList<DeliveredToOrReadByUser> getDeliveredToUsers() {
    return deliveredToUsers;
  }
}
