package io.isometrik.ui.conversations.details.participants;

import io.isometrik.chat.response.conversation.utils.ConversationMember;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;

/**
 * The helper class for inflating items in the members/watchers list, in a conversation.
 */
public class MembersWatchersModel {
  private final String memberId, memberProfileImageUrl;

  private final boolean isUserAnAdmin, isOnline, isSelf, isDeletedUser;
  private boolean isAdmin;
  private final long lastSeen;
  private String memberName;

  /**
   * Instantiates a new Members watchers model.
   *
   * @param conversationMember the conversation member
   * @param isUserAnAdmin the is user an admin
   * @param isSelf the is self
   */
  public MembersWatchersModel(ConversationMember conversationMember, boolean isUserAnAdmin,
      boolean isSelf) {
    memberName = conversationMember.getUserName();
    memberId = conversationMember.getUserId();
    memberProfileImageUrl = conversationMember.getUserProfileImageUrl();
    isAdmin = conversationMember.isAdmin();
    isOnline = conversationMember.isOnline();
    this.isUserAnAdmin = isUserAnAdmin;
    this.isSelf = isSelf;
    lastSeen = conversationMember.getLastSeen();

    if (memberId == null) {
      isDeletedUser = true;
      memberName = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
    } else {
      isDeletedUser = false;
    }
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
   * Is user an admin boolean.
   *
   * @return the boolean
   */
  public boolean isUserAnAdmin() {
    return isUserAnAdmin;
  }

  /**
   * Is online boolean.
   *
   * @return the boolean
   */
  public boolean isOnline() {
    return isOnline;
  }

  /**
   * Is self boolean.
   *
   * @return the boolean
   */
  public boolean isSelf() {
    return isSelf;
  }

  /**
   * Gets last seen.
   *
   * @return the last seen
   */
  public long getLastSeen() {
    return lastSeen;
  }

  /**
   * Is deleted user boolean.
   *
   * @return the boolean
   */
  public boolean isDeletedUser() {
    return isDeletedUser;
  }
}
