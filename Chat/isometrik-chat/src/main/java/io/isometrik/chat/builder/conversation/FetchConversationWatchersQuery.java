package io.isometrik.chat.builder.conversation;

/**
 * The query builder for fetching conversation watchers request.
 */
public class FetchConversationWatchersQuery {
  private final String conversationId;
  private final Integer skip;
  private final Integer limit;
  private final String userToken;
  private final String searchTag;

  private FetchConversationWatchersQuery(FetchConversationWatchersQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
    this.searchTag = builder.searchTag;
  }

  /**
   * The builder class for building fetch conversation watchers query.
   */
  public static class Builder {
    private String conversationId;
    private Integer skip;
    private Integer limit;
    private String userToken;
    private String searchTag;

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
    public FetchConversationWatchersQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchConversationWatchersQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchConversationWatchersQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchConversationWatchersQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public FetchConversationWatchersQuery.Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch conversation watchers query.
     *
     * @return the fetch conversation watchers query
     */
    public FetchConversationWatchersQuery build() {
      return new FetchConversationWatchersQuery(this);
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

  /**
   * Gets search tag.
   *
   * @return the search tag
   */
  public String getSearchTag() {
    return searchTag;
  }
}
