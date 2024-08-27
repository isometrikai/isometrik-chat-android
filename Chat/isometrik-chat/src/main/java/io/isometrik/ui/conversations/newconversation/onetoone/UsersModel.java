package io.isometrik.ui.conversations.newconversation.onetoone;

import io.isometrik.chat.response.user.utils.NonBlockedUser;

/**
 * The helper class for inflating items in the users list to select opponent to create a one to one
 * conversation with.
 */
public class UsersModel {

  private final String userId;
  private final String userName;
  private final boolean isOnline;
  private final String userProfilePic;
  private final long lastSeenAt;
  private boolean selected;

  /**
   * Instantiates a new Users model.
   *
   * @param user the user
   */
  UsersModel(NonBlockedUser user) {
    lastSeenAt = user.getLastSeen();
    userId = user.getUserId();
    userName = user.getUserName();
    isOnline = user.isOnline();
    userProfilePic = user.getUserProfileImageUrl();
  }

  /**
   * Instantiates a new Users model.
   *
   * @param userId the user id
   * @param userName the user name
   * @param isOnline the is online
   * @param userProfilePic the user profile pic
   * @param lastSeenAt the last seen at
   */
  public UsersModel(String userId, String userName, boolean isOnline, String userProfilePic,
      long lastSeenAt) {
    this.userId = userId;
    this.userName = userName;
    this.isOnline = isOnline;
    this.userProfilePic = userProfilePic;
    this.lastSeenAt = lastSeenAt;
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

  /**
   * Gets last seen at.
   *
   * @return the last seen at
   */
  public long getLastSeenAt() {
    return lastSeenAt;
  }
}
