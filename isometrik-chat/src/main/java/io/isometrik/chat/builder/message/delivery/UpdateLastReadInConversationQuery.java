package io.isometrik.chat.builder.message.delivery;

/**
 * The query builder for updating last read in conversation request.
 */
public class UpdateLastReadInConversationQuery {
  private final String conversationId;
  private final Long timestamp;
  private final String userToken;

  private UpdateLastReadInConversationQuery(Builder builder) {
    this.conversationId = builder.conversationId;
    this.timestamp = builder.timestamp;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building update last read in conversation query.
   */
  public static class Builder {
    private String conversationId;
    private Long timestamp;
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
    public Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     * @return the timestamp
     */
    public Builder setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build update last read in conversation query.
     *
     * @return the update last read in conversation query
     */
    public UpdateLastReadInConversationQuery build() {
      return new UpdateLastReadInConversationQuery(this);
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
   * Gets timestamp.
   *
   * @return the timestamp
   */
  public Long getTimestamp() {
    return timestamp;
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
