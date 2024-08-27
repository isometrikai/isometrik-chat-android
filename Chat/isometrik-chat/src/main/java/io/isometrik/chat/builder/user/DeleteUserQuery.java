package io.isometrik.chat.builder.user;

/**
 * The query builder for deleting user request.
 */
public class DeleteUserQuery {
  private final String userToken;

  private DeleteUserQuery(DeleteUserQuery.Builder builder) {
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building delete user query.
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
    public DeleteUserQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build delete user query.
     *
     * @return the delete user query
     */
    public DeleteUserQuery build() {
      return new DeleteUserQuery(this);
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
