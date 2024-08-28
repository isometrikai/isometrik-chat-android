package io.isometrik.chat.builder.user.block;

/**
 * The query builder for fetching blocked users request.
 */
public class FetchBlockedUsersQuery {
  private final Integer skip;
  private final Integer limit;
  private final String userToken;
    private final String searchTag;
  private FetchBlockedUsersQuery(FetchBlockedUsersQuery.Builder builder) {
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
    this.searchTag=builder.searchTag;
  }

  /**
   * The builder class for building fetch blocked users query.
   */
  public static class Builder {
    private Integer skip;
    private Integer limit;
    private String userToken;
    private  String searchTag;

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
    public FetchBlockedUsersQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchBlockedUsersQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchBlockedUsersQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public FetchBlockedUsersQuery.Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch blocked users query.
     *
     * @return the fetch blocked users query
     */
    public FetchBlockedUsersQuery build() {
      return new FetchBlockedUsersQuery(this);
    }
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
