package io.isometrik.chat.builder.message;

/**
 * The query builder for fetching unread messages request.
 */
public class FetchUnreadMessagesQuery {
  private final Integer sort;
  private final Integer limit;
  private final Integer skip;
  private final String conversationId;
  private final String userToken;

  private FetchUnreadMessagesQuery(FetchUnreadMessagesQuery.Builder builder) {
    this.sort = builder.sort;
    this.limit = builder.limit;
    this.skip = builder.skip;
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch unread messages query.
   */
  public static class Builder {
    private Integer sort;
    private Integer limit;
    private Integer skip;
    private String conversationId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets sort.
     *
     * @param sort the sort
     * @return the sort
     */
    public FetchUnreadMessagesQuery.Builder setSort(Integer sort) {
      this.sort = sort;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchUnreadMessagesQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchUnreadMessagesQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public FetchUnreadMessagesQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchUnreadMessagesQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch unread messages query.
     *
     * @return the fetch unread messages query
     */
    public FetchUnreadMessagesQuery build() {
      return new FetchUnreadMessagesQuery(this);
    }
  }

  /**
   * Gets sort.
   *
   * @return the sort
   */
  public Integer getSort() {
    return sort;
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
   * Gets skip.
   *
   * @return the skip
   */
  public Integer getSkip() {
    return skip;
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
