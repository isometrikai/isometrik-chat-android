package io.isometrik.chat.builder.user.block;

/**
 * The query builder for fetching non blocked users request.
 */
public class FetchNonBlockedUsersQuery {
  private final Integer sort;
  private final Integer limit;
  private final Integer skip;
  private final String userToken;
  private final String searchTag;
  private FetchNonBlockedUsersQuery(FetchNonBlockedUsersQuery.Builder builder) {
    this.sort = builder.sort;
    this.limit = builder.limit;
    this.skip = builder.skip;
    this.userToken = builder.userToken;
    this.searchTag = builder.searchTag;
  }

  /**
   * The builder class for building fetch non blocked users query.
   */
  public static class Builder {
    private Integer sort;
    private Integer limit;
    private Integer skip;
    private String userToken;
    private String searchTag;

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
    public FetchNonBlockedUsersQuery.Builder setSort(Integer sort) {
      this.sort = sort;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchNonBlockedUsersQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchNonBlockedUsersQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchNonBlockedUsersQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public FetchNonBlockedUsersQuery.Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch non blocked users query.
     *
     * @return the fetch non blocked users query
     */
    public FetchNonBlockedUsersQuery build() {
      return new FetchNonBlockedUsersQuery(this);
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
