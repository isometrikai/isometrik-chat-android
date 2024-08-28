package io.isometrik.chat.events.membershipcontrol;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * The type Remove members event.
 */
public class RemoveMembersEvent implements Serializable {

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

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

  @SerializedName("members")
  @Expose
  private List<ConversationMember> members;

  @SerializedName("sentAt")
  @Expose
  private long sentAt;

  /**
   * The type Conversation member.
   */
  public static class ConversationMember {

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
  }

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
   * Gets members.
   *
   * @return the members
   */
  public List<ConversationMember> getMembers() {
    return members;
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
   * Gets members count.
   *
   * @return the members count
   */
  public int getMembersCount() {
    return membersCount;
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
