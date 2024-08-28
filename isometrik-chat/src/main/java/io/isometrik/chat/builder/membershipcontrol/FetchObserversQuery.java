package io.isometrik.chat.builder.membershipcontrol;

/**
 * The query builder for fetching observers request.
 */
public class FetchObserversQuery {
  private final String conversationId;
  private final Integer skip;
  private final Integer limit;
  private final String userToken;
  private final String searchTag;

  private FetchObserversQuery(FetchObserversQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
    this.searchTag = builder.searchTag;
  }

  /**
   * The builder class for building fetch observers query.
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
    public FetchObserversQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchObserversQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchObserversQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchObserversQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public FetchObserversQuery.Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch observers query.
     *
     * @return the fetch observers query
     */
    public FetchObserversQuery build() {
      return new FetchObserversQuery(this);
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
