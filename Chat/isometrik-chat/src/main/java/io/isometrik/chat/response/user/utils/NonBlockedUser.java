package io.isometrik.chat.response.user.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.json.JSONObject;

/**
 * The helper class to parse the details of the non blocked user.
 */
public class NonBlockedUser {
  @SerializedName("userProfileImageUrl")
  @Expose
  private String userProfileImageUrl;
  @SerializedName("userName")
  @Expose
  private String userName;
  @SerializedName("userIdentifier")
  @Expose
  private String userIdentifier;
  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("online")
  @Expose
  private boolean online;
  @SerializedName("lastSeen")
  @Expose
  private long lastSeen;
  @SerializedName("metaData")
  @Expose
  private Object metaData;
  @SerializedName("language")
  @Expose
  private String language;
  @SerializedName("notification")
  @Expose
  private boolean notification;
  @SerializedName("visibility")
  @Expose
  private boolean visibility;
  @SerializedName("updatedAt")
  @Expose
  private long updatedAt;
  @SerializedName("createdAt")
  @Expose
  private long createdAt;

  /**
   * Gets user profile image url.
   *
   * @return the user profile image url
   */
  public String getUserProfileImageUrl() {
    return userProfileImageUrl;
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
    return online;
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
   * Gets language.
   *
   * @return the language
   */
  public String getLanguage() {
    return language;
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

  /**
   * Gets meta data.
   *
   * @return the meta data
   */
  public JSONObject getMetaData() {

    try {
      return new JSONObject(new Gson().toJson(metaData));
    } catch (Exception ignore) {
      return new JSONObject();
    }
  }
}