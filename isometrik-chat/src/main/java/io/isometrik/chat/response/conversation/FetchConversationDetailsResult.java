package io.isometrik.chat.response.conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil;

/**
 * The helper class to parse the response of the fetch conversation details request.
 */
public class FetchConversationDetailsResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("conversationDetails")
  @Expose
  private ConversationDetailsUtil conversationDetails;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets conversation details.
   *
   * @return the conversation details
   */
  public ConversationDetailsUtil getConversationDetails() {
    return conversationDetails;
  }
}
