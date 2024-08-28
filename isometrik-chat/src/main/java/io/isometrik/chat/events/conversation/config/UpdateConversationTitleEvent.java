package io.isometrik.chat.events.conversation.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The type Update conversation title event.
 */
/**
 * The class containing  event details.
 */
public class UpdateConversationTitleEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("conversationId")
  @Expose
  private String conversationId;
  @SerializedName("conversationImageUrl")
  @Expose
  private String conversationImageUrl;
  @SerializedName("conversationTitle")
  @Expose
  private String conversationTitle;
  @SerializedName("privateOneToOne")
  @Expose
  private boolean privateOneToOne;
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
   * Gets conversation title.
   *
   * @return the conversation title
   */
  public String getConversationTitle() {
    return conversationTitle;
  }

  /**
   * Gets sent at.
   *
   * @return the sent at
   */
  public long getSentAt() {
    return sentAt;
  }

  /**
   * Gets conversation image url.
   *
   * @return the conversation image url
   */
  public String getConversationImageUrl() {
    return conversationImageUrl;
  }

  /**
   * Is private one to one boolean.
   *
   * @return the boolean
   */
  public boolean isPrivateOneToOne() {
    return privateOneToOne;
  }
}
