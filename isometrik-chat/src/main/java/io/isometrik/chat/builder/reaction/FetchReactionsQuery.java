package io.isometrik.chat.builder.reaction;

/**
 * The query builder for fetching reactions request.
 */
public class FetchReactionsQuery {
  private final String reactionType;
  private final String conversationId;
  private final String messageId;
  private final Integer skip;
  private final Integer limit;
  private final String userToken;

  private FetchReactionsQuery(FetchReactionsQuery.Builder builder) {
    this.reactionType = builder.reactionType;
    this.conversationId = builder.conversationId;
    this.messageId = builder.messageId;
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch reactions query.
   */
  public static class Builder {
    private String reactionType;
    private String conversationId;
    private String messageId;
    private Integer skip;
    private Integer limit;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchReactionsQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchReactionsQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets reaction type.
     *
     * @param reactionType the reaction type
     * @return the reaction type
     */
    public FetchReactionsQuery.Builder setReactionType(String reactionType) {
      this.reactionType = reactionType;
      return this;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public FetchReactionsQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     * @return the message id
     */
    public FetchReactionsQuery.Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchReactionsQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch reactions query.
     *
     * @return the fetch reactions query
     */
    public FetchReactionsQuery build() {
      return new FetchReactionsQuery(this);
    }
  }

  /**
   * Gets reaction type.
   *
   * @return the reaction type
   */
  public String getReactionType() {
    return reactionType;
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
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets skip.
   *
   * @return the skip
   */
  public Integer getSkip() {
    return skip;
  }

  /**
   * Gets limit.
   *
   * @return the limit
   */
  public Integer getLimit() {
    return limit;
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
