package io.isometrik.chat.response.conversation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the create conversation request.
 */
public class CreateConversationResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("conversationId")
  @Expose
  private String conversationId;

  @SerializedName("newConversation")
  @Expose
  private boolean newConversation;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets conversation id.
   *
   * @return the conversation id
   */
  public String getConversationId() {
    return conversationId;
  }

  /**
   * Is new conversation boolean.
   *
   * @return the boolean
   */
  public boolean isNewConversation() {
    return newConversation;
  }
}
