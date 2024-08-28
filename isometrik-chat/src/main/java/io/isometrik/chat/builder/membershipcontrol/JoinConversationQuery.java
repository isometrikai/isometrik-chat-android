package io.isometrik.chat.builder.membershipcontrol;

/**
 * The query builder for joining conversation request.
 */
public class JoinConversationQuery {
  private final String conversationId;
  private final String userToken;

  private JoinConversationQuery(JoinConversationQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building join conversation query.
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
    public JoinConversationQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public JoinConversationQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build join conversation query.
     *
     * @return the join conversation query
     */
    public JoinConversationQuery build() {
      return new JoinConversationQuery(this);
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
