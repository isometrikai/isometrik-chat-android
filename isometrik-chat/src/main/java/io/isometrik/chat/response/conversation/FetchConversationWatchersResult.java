package io.isometrik.chat.response.conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.conversation.utils.ConversationMember;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch conversation watchers request.
 */
public class FetchConversationWatchersResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("conversationWatchers")
  @Expose
  private ArrayList<ConversationMember> conversationWatchers;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets conversation watchers.
   *
   * @return the conversation watchers
   */
  public ArrayList<ConversationMember> getConversationWatchers() {
    return conversationWatchers;
  }
}
