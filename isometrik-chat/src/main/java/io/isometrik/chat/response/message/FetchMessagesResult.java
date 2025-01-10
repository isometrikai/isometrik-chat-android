package io.isometrik.chat.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import java.util.ArrayList;

/**
 * The type Fetch messages result.
 */
/**
 * The helper class to parse the response of the fetch messages request.
 */
public class FetchMessagesResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("messages")
  @Expose
  private ArrayList<Message> messages;

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
  public ArrayList<Message> getMessages() {
    return messages;
  }
}
