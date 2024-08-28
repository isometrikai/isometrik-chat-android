package io.isometrik.chat.builder.user;

import org.json.JSONObject;

/**
 * The query builder for updating user request.
 */
public class UpdateUserQuery {

  private final String userIdentifier;
  private final String userProfileImageUrl;
  private final String userName;
  private final JSONObject metadata;
  private final String userToken;
  private final Boolean notification;

  private UpdateUserQuery(UpdateUserQuery.Builder builder) {
    this.userIdentifier = builder.userIdentifier;
    this.userProfileImageUrl = builder.userProfileImageUrl;
    this.userName = builder.userName;
    this.metadata = builder.metadata;
    this.userToken = builder.userToken;
    this.notification = builder.notification;
  }

  /**
   * The builder class for building update user query.
   */
  public static class Builder {
    private String userIdentifier;
    private String userProfileImageUrl;
    private String userName;
    private JSONObject metadata;
    private String userToken;
    private Boolean notification;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user identifier.
     *
     * @param userIdentifier the user identifier
     * @return the user identifier
     */
    public UpdateUserQuery.Builder setUserIdentifier(String userIdentifier) {
      this.userIdentifier = userIdentifier;
      return this;
    }

    /**
     * Sets user profile image url.
     *
     * @param userProfileImageUrl the user profile image url
     * @return the user profile image url
     */
    public UpdateUserQuery.Builder setUserProfileImageUrl(String userProfileImageUrl) {
      this.userProfileImageUrl = userProfileImageUrl;
      return this;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     * @return the user name
     */
    public UpdateUserQuery.Builder setUserName(String userName) {
      this.userName = userName;
      return this;
    }

    /**
     * Sets metadata.
     *
     * @param metadata the metadata
     * @return the metadata
     */
    public UpdateUserQuery.Builder setMetadata(JSONObject metadata) {
      this.metadata = metadata;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public UpdateUserQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets notification.
     *
     * @param notification the notification
     * @return the notification
     */
    public UpdateUserQuery.Builder setNotification(Boolean notification) {
      this.notification = notification;
      return this;
    }

    /**
     * Build update user query.
     *
     * @return the update user query
     */
    public UpdateUserQuery build() {
      return new UpdateUserQuery(this);
    }
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
   * Gets metadata.
   *
   * @return the metadata
   */
  public JSONObject getMetadata() {
    return metadata;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  /**
   * Gets notification.
   *
   * @return the notification
   */
  public Boolean getNotification() {
    return notification;
  }
}
