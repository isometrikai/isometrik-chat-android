package io.isometrik.chat.events.conversation.cleanup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The type Clear conversation event.
 */
/**
 * The class containing  event details.
 */
public class ClearConversationEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("conversationId")
  @Expose
  private String conversationId;

  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("sentAt")
  @Expose
  private long sentAt;

  /**
   * Gets action.
   *
   * @return the action
   */
  public String getAction() {
    return action;
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
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets sent at.
   *
   * @return the sent at
   */
  public long getSentAt() {
    return sentAt;
  }
}
