package io.isometrik.chat.response.user;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * The helper class to parse the response of the fetch users request.
 */
public class FetchUsersResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("pageToken")
  @Expose
  private String pageToken;

  @SerializedName("users")
  @Expose
  private ArrayList<User> users;

  /**
   * The type User.
   */
  public static class User {
    @SerializedName("userProfileImageUrl")
    @Expose
    private String userProfileImageUrl;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userIdentifier")
    @Expose
    private String userIdentifier;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("online")
    @Expose
    private boolean online;
    @SerializedName("notification")
    @Expose
    private boolean notification;
    @SerializedName("visibility")
    @Expose
    private boolean visibility;
    @SerializedName("lastSeen")
    @Expose
    private long lastSeen;
    @SerializedName("metaData")
    @Expose
    private Object metaData;
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

      try {
        return new JSONObject(new Gson().toJson(metaData));
      } catch (Exception ignore) {
        return new JSONObject();
      }
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

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets users.
   *
   * @return the users
   */
  public ArrayList<User> getUsers() {
    return users;
  }

  /**
   * Gets page token.
   *
   * @return the page token
   */
  public String getPageToken() {
    return pageToken;
  }
}
