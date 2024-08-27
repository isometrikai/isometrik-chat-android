package io.isometrik.chat.builder.conversation.cleanup;

/**
 * The query builder for clearing conversation request.
 */
public class ClearConversationQuery {

  private final String conversationId;
  private final String userToken;

  private ClearConversationQuery(ClearConversationQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building clear conversation query.
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
    public ClearConversationQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public ClearConversationQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build clear conversation query.
     *
     * @return the clear conversation query
     */
    public ClearConversationQuery build() {
      return new ClearConversationQuery(this);
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
