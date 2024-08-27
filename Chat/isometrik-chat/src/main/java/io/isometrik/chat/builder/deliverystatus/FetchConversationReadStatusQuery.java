package io.isometrik.chat.builder.deliverystatus;

/**
 * The query builder for fetching conversation read status request.
 */
public class FetchConversationReadStatusQuery {
  private final String conversationId;
  private final Integer skip;
  private final Integer limit;
  private final String userToken;

  private FetchConversationReadStatusQuery(FetchConversationReadStatusQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch conversation read status query.
   */
  public static class Builder {
    private String conversationId;
    private Integer skip;
    private Integer limit;
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
    public FetchConversationReadStatusQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchConversationReadStatusQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchConversationReadStatusQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchConversationReadStatusQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch conversation read status query.
     *
     * @return the fetch conversation read status query
     */
    public FetchConversationReadStatusQuery build() {
      return new FetchConversationReadStatusQuery(this);
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
