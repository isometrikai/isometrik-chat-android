package io.isometrik.chat.builder.conversation;

/**
 * The query builder for fetching unread conversations count request.
 */
public class FetchUnreadConversationsCountQuery {
  private final String userToken;

  private FetchUnreadConversationsCountQuery(FetchUnreadConversationsCountQuery.Builder builder) {
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch unread conversations count query.
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
    public FetchUnreadConversationsCountQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch unread conversations count query.
     *
     * @return the fetch unread conversations count query
     */
    public FetchUnreadConversationsCountQuery build() {
      return new FetchUnreadConversationsCountQuery(this);
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
