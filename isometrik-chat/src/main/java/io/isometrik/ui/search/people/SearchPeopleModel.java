package io.isometrik.ui.search.people;

import io.isometrik.chat.response.user.utils.NonBlockedUser;

/**
 * The helper class for inflating items in the search people results.
 */
public class SearchPeopleModel {

  private final String userName, userProfileImageUrl, userId, userIdentifier;
  private final boolean isOnline;
  private final long lastSeenAt;

  /**
   * Instantiates a new Search people model.
   *
   * @param user the user
   */
  SearchPeopleModel(NonBlockedUser user) {

    userId = user.getUserId();
    userName = user.getUserName();
    isOnline = user.isOnline();
    userProfileImageUrl = user.getUserProfileImageUrl();
    userIdentifier = user.getUserIdentifier();
    lastSeenAt=user.getLastSeen();
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
