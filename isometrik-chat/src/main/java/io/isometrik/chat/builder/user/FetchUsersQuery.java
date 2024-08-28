package io.isometrik.chat.builder.user;

/**
 * The query builder for fetching users request.
 */
public class FetchUsersQuery {
  private final String pageToken;
  private final Integer count;
  private final String searchTag;

  private FetchUsersQuery(FetchUsersQuery.Builder builder) {
    this.pageToken = builder.pageToken;
    this.count = builder.count;
    this.searchTag = builder.searchTag;
  }

  /**
   * The builder class for building fetch users query.
   */
  public static class Builder {
    private String pageToken;
    private Integer count;
    private String searchTag;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets page token.
     *
     * @param pageToken the page token
     * @return the page token
     */
    public FetchUsersQuery.Builder setPageToken(String pageToken) {
      this.pageToken = pageToken;
      return this;
    }

    /**
     * Sets count.
     *
     * @param count the count
     * @return the count
     */
    public FetchUsersQuery.Builder setCount(Integer count) {
      this.count = count;
      return this;
    }

    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public FetchUsersQuery.Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch users query.
     *
     * @return the fetch users query
     */
    public FetchUsersQuery build() {
      return new FetchUsersQuery(this);
    }
  }

  /**
   * Gets page token.
   *
   * @return the page token
   */
  public String getPageToken() {
    return pageToken;
  }

  /**
   * Gets count.
   *
   * @return the count
   */
  public Integer getCount() {
    return count;
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
