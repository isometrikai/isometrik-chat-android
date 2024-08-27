package io.isometrik.chat.builder.membershipcontrol;

/**
 * The query builder for fetching members to add to conversation request.
 */
public class FetchMembersToAddToConversationQuery {
  private final String conversationId;
  private final String userToken;
  private final Integer sort;
  private final Integer limit;
  private final Integer skip;
  private final String searchTag;

  private FetchMembersToAddToConversationQuery(
      FetchMembersToAddToConversationQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
    this.sort = builder.sort;
    this.limit = builder.limit;
    this.skip = builder.skip;
    this.searchTag = builder.searchTag;
  }

  /**
   * The builder class for building fetch members to add to conversation query.
   */
  public static class Builder {
    private String conversationId;
    private String userToken;
    private Integer sort;
    private Integer limit;
    private Integer skip;
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
    public FetchMembersToAddToConversationQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchMembersToAddToConversationQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets sort.
     *
     * @param sort the sort
     * @return the sort
     */
    public FetchMembersToAddToConversationQuery.Builder setSort(Integer sort) {
      this.sort = sort;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchMembersToAddToConversationQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchMembersToAddToConversationQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public FetchMembersToAddToConversationQuery.Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch members to add to conversation query.
     *
     * @return the fetch members to add to conversation query
     */
    public FetchMembersToAddToConversationQuery build() {
      return new FetchMembersToAddToConversationQuery(this);
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
   * Gets search tag.
   *
   * @return the search tag
   */
  public String getSearchTag() {
    return searchTag;
  }
}
