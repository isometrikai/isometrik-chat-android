package io.isometrik.chat.builder.message;

/**
 * The query builder for fetching unread messages count request.
 */
public class FetchUnreadMessagesCountQuery {
  private final String conversationId;
  private final String userToken;

  private FetchUnreadMessagesCountQuery(FetchUnreadMessagesCountQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch unread messages count query.
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
    public FetchUnreadMessagesCountQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchUnreadMessagesCountQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch unread messages count query.
     *
     * @return the fetch unread messages count query
     */
    public FetchUnreadMessagesCountQuery build() {
      return new FetchUnreadMessagesCountQuery(this);
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
