package io.isometrik.ui.users.blockedornonblocked;

import io.isometrik.chat.response.user.utils.BlockedUser;
import io.isometrik.chat.response.user.utils.NonBlockedUser;

/**
 * The helper class for inflating items in the blocked or non blocked users list, for the logged in
 * user.
 */
public class BlockedOrNonBlockedUsersModel {

  private final String userName, userProfileImageUrl, userId, userIdentifier;
  private boolean isOnline;
  private long lastSeenAt;

  /**
   * Instantiates a new Blocked or non blocked users model.
   *
   * @param user the user
   */
  public BlockedOrNonBlockedUsersModel(NonBlockedUser user) {

    userId = user.getUserId();
    userName = user.getUserName();
    isOnline = user.isOnline();
    userProfileImageUrl = user.getUserProfileImageUrl();
    userIdentifier = user.getUserIdentifier();
    lastSeenAt = user.getLastSeen();
  }

  /**
   * Instantiates a new Blocked or non blocked users model.
   *
   * @param user the user
   */
  public BlockedOrNonBlockedUsersModel(BlockedUser user) {

    userId = user.getUserId();
    userName = user.getUserName();
    userProfileImageUrl = user.getUserProfileImageUrl();
    userIdentifier = user.getUserIdentifier();
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
   * Gets last seen at.
   *
   * @return the last seen at
   */
  public long getLastSeenAt() {
    return lastSeenAt;
  }
}
