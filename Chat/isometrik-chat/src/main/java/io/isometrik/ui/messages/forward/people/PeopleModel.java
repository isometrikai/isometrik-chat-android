package io.isometrik.ui.messages.forward.people;

import io.isometrik.chat.response.user.utils.NonBlockedUser;

/**
 * The helper class for inflating items in the list of users to which a message can be forwarded.
 */
public class PeopleModel {

  private final String userName, userProfileImageUrl, userId;
  private final boolean isOnline;
  private boolean selected;

  /**
   * Instantiates a new People model.
   *
   * @param user the user
   */
  PeopleModel(NonBlockedUser user) {

    userId = user.getUserId();
    userName = user.getUserName();
    isOnline = user.isOnline();
    userProfileImageUrl = user.getUserProfileImageUrl();
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
   * Gets user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return userName;
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
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
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
   * Is selected boolean.
   *
   * @return the boolean
   */
  public boolean isSelected() {
    return selected;
  }
}
