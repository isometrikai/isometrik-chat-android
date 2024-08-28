package io.isometrik.chat.builder.conversation;

/**
 * The query builder for fetching conversation messaging status request.
 */
public class FetchConversationMessagingStatusQuery {
  private final String conversationId;
  private final String userToken;

  private FetchConversationMessagingStatusQuery(FetchConversationMessagingStatusQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch conversation messaging status query.
   */
  public static class Builder {
    private String conversationId;
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
    public FetchConversationMessagingStatusQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchConversationMessagingStatusQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch conversation messaging status query.
     *
     * @return the fetch conversation messaging status query
     */
    public FetchConversationMessagingStatusQuery build() {
      return new FetchConversationMessagingStatusQuery(this);
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
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }
}

