package io.isometrik.chat.response.user.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the details of the blocked user.
 */
public class BlockedUser {
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
  @SerializedName("metaData")
  @Expose
  private Object metaData;

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
   * Gets meta data.
   *
   * @return the meta data
   */
  public Object getMetaData() {
    return metaData;
  }
}
