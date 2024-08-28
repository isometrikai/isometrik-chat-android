package io.isometrik.chat.builder.conversation;

/**
 * The query builder for fetching conversation members request.
 */
public class FetchConversationMembersQuery {
  private final String conversationId;
  private final Integer skip;
  private final Integer limit;
  private final String userToken;
  private final String searchTag;

  private FetchConversationMembersQuery(FetchConversationMembersQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
    this.searchTag = builder.searchTag;
  }

  /**
   * The builder class for building fetch conversation members query.
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
    public FetchConversationMembersQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchConversationMembersQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchConversationMembersQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchConversationMembersQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public FetchConversationMembersQuery.Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch conversation members query.
     *
     * @return the fetch conversation members query
     */
    public FetchConversationMembersQuery build() {
      return new FetchConversationMembersQuery(this);
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
