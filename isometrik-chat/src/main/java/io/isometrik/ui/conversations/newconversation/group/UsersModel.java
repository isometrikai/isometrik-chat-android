package io.isometrik.ui.conversations.newconversation.group;

import io.isometrik.chat.response.user.utils.NonBlockedUser;
import java.io.Serializable;

/**
 * The helper class for inflating items in the users list to select members to create a group
 * conversation with.
 */
public class UsersModel implements Serializable {

  private final String userId;
  private final String userName;
  private final boolean isOnline;
  private final String userProfilePic;

  private boolean selected;

  /**
   * Instantiates a new Users model.
   *
   * @param userId the user id
   * @param userName the user name
   * @param userProfilePic the user profile pic
   * @param isOnline the is online
   */
  public UsersModel(String userId, String userName, String userProfilePic, boolean isOnline) {
    this.userId = userId;
    this.userName = userName;
    this.isOnline = isOnline;
    this.userProfilePic = userProfilePic;
  }

  /**
   * Instantiates a new Users model.
   *
   * @param user the user
   */
  UsersModel(NonBlockedUser user) {

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
