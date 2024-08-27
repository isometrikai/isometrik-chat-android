package io.isometrik.chat.builder.user.block;

/**
 * The query builder for unblocking user request.
 */
public class UnblockUserQuery {
  private final String opponentId;
  private final String userToken;

  private UnblockUserQuery(UnblockUserQuery.Builder builder) {
    this.opponentId = builder.opponentId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building unblock user query.
   */
  public static class Builder {
    private String opponentId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets opponent id.
     *
     * @param opponentId the opponent id
     * @return the opponent id
     */
    public UnblockUserQuery.Builder setOpponentId(String opponentId) {
      this.opponentId = opponentId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public UnblockUserQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build unblock user query.
     *
     * @return the unblock user query
     */
    public UnblockUserQuery build() {
      return new UnblockUserQuery(this);
    }
  }

  /**
   * Gets opponent id.
   *
   * @return the opponent id
   */
  public String getOpponentId() {
    return opponentId;
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
