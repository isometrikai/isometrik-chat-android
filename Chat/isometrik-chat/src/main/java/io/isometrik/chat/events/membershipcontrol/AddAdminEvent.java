package io.isometrik.chat.events.membershipcontrol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The type Add admin event.
 */
/**
 * The class containing  event details.
 */
public class AddAdminEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;
  @SerializedName("conversationImageUrl")
  @Expose
  private String conversationImageUrl;
  @SerializedName("conversationTitle")
  @Expose
  private String conversationTitle;
  @SerializedName("privateOneToOne")
  @Expose
  private boolean privateOneToOne;
  @SerializedName("conversationId")
  @Expose
  private String conversationId;

  @SerializedName("memberId")
  @Expose
  private String memberId;

  @SerializedName("memberName")
  @Expose
  private String memberName;

  @SerializedName("memberIdentifier")
  @Expose
  private String memberIdentifier;

  @SerializedName("memberProfileImageUrl")
  @Expose
  private String memberProfileImageUrl;

  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;

  @SerializedName("initiatorName")
  @Expose
  private String initiatorName;

  @SerializedName("initiatorIdentifier")
  @Expose
  private String initiatorIdentifier;

  @SerializedName("initiatorProfileImageUrl")
  @Expose
  private String initiatorProfileImageUrl;

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
   * Gets member id.
   *
   * @return the member id
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets member name.
   *
   * @return the member name
   */
  public String getMemberName() {
    return memberName;
  }

  /**
   * Gets member identifier.
   *
   * @return the member identifier
   */
  public String getMemberIdentifier() {
    return memberIdentifier;
  }

  /**
   * Gets member profile image url.
   *
   * @return the member profile image url
   */
  public String getMemberProfileImageUrl() {
    return memberProfileImageUrl;
  }

  /**
   * Gets initiator id.
   *
   * @return the initiator id
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets initiator name.
   *
   * @return the initiator name
   */
  public String getInitiatorName() {
    return initiatorName;
  }

  /**
   * Gets initiator identifier.
   *
   * @return the initiator identifier
   */
  public String getInitiatorIdentifier() {
    return initiatorIdentifier;
  }

  /**
   * Gets initiator profile image url.
   *
   * @return the initiator profile image url
   */
  public String getInitiatorProfileImageUrl() {
    return initiatorProfileImageUrl;
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
   * Gets conversation title.
   *
   * @return the conversation title
   */
  public String getConversationTitle() {
    return conversationTitle;
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
