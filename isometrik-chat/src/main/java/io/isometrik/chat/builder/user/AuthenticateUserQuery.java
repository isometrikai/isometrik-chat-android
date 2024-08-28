package io.isometrik.chat.builder.user;

/**
 * The query builder for authenticating user request.
 */
public class AuthenticateUserQuery {

  private final String userIdentifier;
  private final String password;

  private AuthenticateUserQuery(AuthenticateUserQuery.Builder builder) {
    this.userIdentifier = builder.userIdentifier;
    this.password = builder.password;
  }

  /**
   * The builder class for building authenticate user query.
   */
  public static class Builder {
    private String userIdentifier;
    private String password;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user identifier.
     *
     * @param userIdentifier the user identifier
     * @return the user identifier
     */
    public AuthenticateUserQuery.Builder setUserIdentifier(String userIdentifier) {
      this.userIdentifier = userIdentifier;
      return this;
    }

    /**
     * Sets password.
     *
     * @param password the password
     * @return the password
     */
    public AuthenticateUserQuery.Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    /**
     * Build authenticate user query.
     *
     * @return the authenticate user query
     */
    public AuthenticateUserQuery build() {
      return new AuthenticateUserQuery(this);
    }
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }
}
