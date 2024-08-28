package io.isometrik.chat.events.message.delivery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The type Updated last read in conversation event.
 */
public class UpdatedLastReadInConversationEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("conversationId")
  @Expose
  private String conversationId;

  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("userName")
  @Expose
  private String userName;

  @SerializedName("userIdentifier")
  @Expose
  private String userIdentifier;

  @SerializedName("userProfileImageUrl")
  @Expose
  private String userProfileImageUrl;

  @SerializedName("lastReadAt")
  @Expose
  private long lastReadAt;

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
   * Gets user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets user profile image url.
   *
   * @return the user profile image url
   */
  public String getUserProfileImageUrl() {
    return userProfileImageUrl;
  }

  /**
   * Gets last read at.
   *
   * @return the last read at
   */
  public long getLastReadAt() {
    return lastReadAt;
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
