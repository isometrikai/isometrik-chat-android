package io.isometrik.chat.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.message.utils.fetchmessages.UserMessage;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch user messages request.
 */
public class FetchUserMessagesResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("messages")
  @Expose
  private ArrayList<UserMessage> messages;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets messages.
   *
   * @return the messages
   */
  public ArrayList<UserMessage> getMessages() {
    return messages;
  }
}
