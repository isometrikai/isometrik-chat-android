package io.isometrik.chat.builder.user.block;

/**
 * The query builder for blocking user request.
 */
public class BlockUserQuery {

  private final String opponentId;
  private final String userToken;

  private BlockUserQuery(BlockUserQuery.Builder builder) {
    this.opponentId = builder.opponentId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building block user query.
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
    public BlockUserQuery.Builder setOpponentId(String opponentId) {
      this.opponentId = opponentId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public BlockUserQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build block user query.
     *
     * @return the block user query
     */
    public BlockUserQuery build() {
      return new BlockUserQuery(this);
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
