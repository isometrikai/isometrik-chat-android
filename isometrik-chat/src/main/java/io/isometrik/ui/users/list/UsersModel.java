package io.isometrik.ui.users.list;

import io.isometrik.chat.response.user.FetchUsersResult;
import org.json.JSONObject;

/**
 * The helper class for inflating items in the users list.
 */
public class UsersModel {

  private final String userName;
  private final String userIdentifier;
  private final String userProfileImageUrl;
  private final String language;
  private final boolean online;
  private final boolean notification;
  private final boolean visibility;
  private final long lastSeen;
  private final JSONObject metaData;
  private final long updatedAt;
  private final long createdAt;

  /**
   * Instantiates a new Users model.
   *
   * @param user the user
   */
  UsersModel(FetchUsersResult.User user) {

    userName = user.getUserName();
    userIdentifier = user.getUserIdentifier();
    userProfileImageUrl = user.getUserProfileImageUrl();
    language = user.getLanguage();
    online = user.isOnline();
    notification = user.isNotification();
    visibility = user.isVisibility();
    lastSeen = user.getLastSeen();
    metaData = user.getMetaData();
    updatedAt = user.getUpdatedAt();
    createdAt = user.getCreatedAt();
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  String getUserName() {
    return userName;
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets user profile image url.
   *
   * @return the user profile image url
   */
  String getUserProfileImageUrl() {
    return userProfileImageUrl;
  }

  /**
   * Gets language.
   *
   * @return the language
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Is online boolean.
   *
   * @return the boolean
   */
  public boolean isOnline() {
    return online;
  }

  /**
   * Is notification boolean.
   *
   * @return the boolean
   */
  public boolean isNotification() {
    return notification;
  }

  /**
   * Is visibility boolean.
   *
   * @return the boolean
   */
  public boolean isVisibility() {
    return visibility;
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
   * Gets meta data.
   *
   * @return the meta data
   */
  public JSONObject getMetaData() {
    return metaData;
  }

  /**
   * Gets updated at.
   *
   * @return the updated at
   */
  public long getUpdatedAt() {
    return updatedAt;
  }

  /**
   * Gets created at.
   *
   * @return the created at
   */
  public long getCreatedAt() {
    return createdAt;
  }
}
