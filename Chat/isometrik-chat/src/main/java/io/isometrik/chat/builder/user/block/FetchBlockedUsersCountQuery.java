package io.isometrik.chat.builder.user.block;

/**
 * The query builder for fetching blocked users count request.
 */
public class FetchBlockedUsersCountQuery {
  private final String userToken;

  private FetchBlockedUsersCountQuery(FetchBlockedUsersCountQuery.Builder builder) {
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch blocked users count query.
   */
  public static class Builder {
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchBlockedUsersCountQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch blocked users count query.
     *
     * @return the fetch blocked users count query
     */
    public FetchBlockedUsersCountQuery build() {
      return new FetchBlockedUsersCountQuery(this);
    }
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
