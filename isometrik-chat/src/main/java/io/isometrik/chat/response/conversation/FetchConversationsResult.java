package io.isometrik.chat.response.conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.conversation.utils.Conversation;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch conversations request.
 */
public class FetchConversationsResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("conversations")
  @Expose
  private ArrayList<Conversation> conversations;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets conversations.
   *
   * @return the conversations
   */
  public ArrayList<Conversation> getConversations() {
    return conversations;
  }
}
