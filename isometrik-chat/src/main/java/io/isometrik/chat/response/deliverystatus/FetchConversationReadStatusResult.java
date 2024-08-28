package io.isometrik.chat.response.deliverystatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.user.utils.UserReadStatusInConversation;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch conversation read status request.
 */
public class FetchConversationReadStatusResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("users")
  @Expose
  private ArrayList<UserReadStatusInConversation> usersReadStatusInConversation;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets users read status in conversation.
   *
   * @return the users read status in conversation
   */
  public ArrayList<UserReadStatusInConversation> getUsersReadStatusInConversation() {
    return usersReadStatusInConversation;
  }
}
