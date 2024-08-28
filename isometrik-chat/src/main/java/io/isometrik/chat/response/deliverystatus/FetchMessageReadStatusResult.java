package io.isometrik.chat.response.deliverystatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.user.utils.DeliveredToOrReadByUser;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch message read status request.
 */
public class FetchMessageReadStatusResult {

  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("users")
  @Expose
  private ArrayList<DeliveredToOrReadByUser> readByUsers;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets read by users.
   *
   * @return the read by users
   */
  public ArrayList<DeliveredToOrReadByUser> getReadByUsers() {
    return readByUsers;
  }
}
