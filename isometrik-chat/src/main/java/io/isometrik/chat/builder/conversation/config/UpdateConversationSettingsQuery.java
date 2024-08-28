package io.isometrik.chat.builder.conversation.config;

/**
 * The query builder for updating conversation settings request.
 */
public class UpdateConversationSettingsQuery {
  private final String conversationId;
  private final Boolean pushNotifications;
  private final Boolean readEvents;
  private final Boolean typingEvents;
  private final String userToken;

  private UpdateConversationSettingsQuery(UpdateConversationSettingsQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.pushNotifications = builder.pushNotifications;
    this.readEvents = builder.readEvents;
    this.typingEvents = builder.typingEvents;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building update conversation settings query.
   */
  public static class Builder {
    private String conversationId;
    private Boolean pushNotifications;
    private Boolean readEvents;
    private Boolean typingEvents;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public UpdateConversationSettingsQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets push notifications.
     *
     * @param pushNotifications the push notifications
     * @return the push notifications
     */
    public UpdateConversationSettingsQuery.Builder setPushNotifications(Boolean pushNotifications) {
      this.pushNotifications = pushNotifications;
      return this;
    }

    /**
     * Sets read events.
     *
     * @param readEvents the read events
     * @return the read events
     */
    public UpdateConversationSettingsQuery.Builder setReadEvents(Boolean readEvents) {
      this.readEvents = readEvents;
      return this;
    }

    /**
     * Sets typing events.
     *
     * @param typingEvents the typing events
     * @return the typing events
     */
    public UpdateConversationSettingsQuery.Builder setTypingEvents(Boolean typingEvents) {
      this.typingEvents = typingEvents;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public UpdateConversationSettingsQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build update conversation settings query.
     *
     * @return the update conversation settings query
     */
    public UpdateConversationSettingsQuery build() {
      return new UpdateConversationSettingsQuery(this);
    }
  }

  /**
   * Gets conversation id.
   *
   * @return the conversation id
   */
  public String getConversationId() {
    return conversationId;
  }

  /**
   * Gets push notifications.
   *
   * @return the push notifications
   */
  public Boolean getPushNotifications() {
    return pushNotifications;
  }

  /**
   * Gets read events.
   *
   * @return the read events
   */
  public Boolean getReadEvents() {
    return readEvents;
  }

  /**
   * Gets typing events.
   *
   * @return the typing events
   */
  public Boolean getTypingEvents() {
    return typingEvents;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }
}
