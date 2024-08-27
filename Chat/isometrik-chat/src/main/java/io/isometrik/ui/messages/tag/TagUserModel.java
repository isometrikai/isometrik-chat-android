package io.isometrik.ui.messages.tag;

import io.isometrik.chat.response.conversation.utils.ConversationMember;

/**
 * The helper class for inflating items in the list of members in conversation that can be tagged in
 * a message.
 */
public class TagUserModel {

  private final String memberName, memberId, memberProfileImageUrl;

  private final boolean isOnline;
  private boolean isAdmin;

  /**
   * Instantiates a new Tag user model.
   *
   * @param conversationMember the conversation member
   */
  public TagUserModel(ConversationMember conversationMember) {
    memberName = conversationMember.getUserName();
    memberId = conversationMember.getUserId();
    memberProfileImageUrl = conversationMember.getUserProfileImageUrl();
    isAdmin = conversationMember.isAdmin();
    isOnline = conversationMember.isOnline();
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
   * Gets member id.
   *
   * @return the member id
   */
  public String getMemberId() {
    return memberId;
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
   * Sets admin.
   *
   * @param admin the admin
   */
  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }

  /**
   * Is admin boolean.
   *
   * @return the boolean
   */
  public boolean isAdmin() {
    return isAdmin;
  }

  /**
   * Is online boolean.
   *
   * @return the boolean
   */
  public boolean isOnline() {
    return isOnline;
  }
}
