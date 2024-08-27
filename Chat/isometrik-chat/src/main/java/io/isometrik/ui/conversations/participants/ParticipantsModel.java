package io.isometrik.ui.conversations.participants;

import io.isometrik.chat.response.membershipcontrol.FetchMembersToAddToConversationResult;

/**
 * The helper class for inflating items in the list of users that can be added as participants to a
 * conversation.
 */
public class ParticipantsModel {

  private final String userId;
  private final String userName;
  private final boolean isOnline;
  private final String userProfilePic;

  private boolean selected;

  /**
   * Instantiates a new Participants model.
   *
   * @param user the user
   */
  ParticipantsModel(FetchMembersToAddToConversationResult.MemberToAddToConversation user) {

    userId = user.getUserId();
    userName = user.getUserName();
    isOnline = user.isOnline();
    userProfilePic = user.getUserProfileImageUrl();
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
   * Is online boolean.
   *
   * @return the boolean
   */
  public boolean isOnline() {
    return isOnline;
  }

  /**
   * Gets user profile pic.
   *
   * @return the user profile pic
   */
  public String getUserProfilePic() {
    return userProfilePic;
  }

  /**
   * Is selected boolean.
   *
   * @return the boolean
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Sets selected.
   *
   * @param selected the selected
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}
