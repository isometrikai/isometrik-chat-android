package io.isometrik.chat.builder.message.delivery;

/**
 * The query builder for marking multiple messages as read request.
 */
public class MarkMultipleMessagesAsReadQuery {
  private final String conversationId;
  private final Long timestamp;
  private final String userToken;

  private MarkMultipleMessagesAsReadQuery(MarkMultipleMessagesAsReadQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.timestamp = builder.timestamp;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building mark multiple messages as read query.
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
    public MarkMultipleMessagesAsReadQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     * @return the timestamp
     */
    public MarkMultipleMessagesAsReadQuery.Builder setTimestamp(Long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public MarkMultipleMessagesAsReadQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build mark multiple messages as read query.
     *
     * @return the mark multiple messages as read query
     */
    public MarkMultipleMessagesAsReadQuery build() {
      return new MarkMultipleMessagesAsReadQuery(this);
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
