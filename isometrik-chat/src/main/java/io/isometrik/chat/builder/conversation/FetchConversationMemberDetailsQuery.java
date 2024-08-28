package io.isometrik.chat.builder.conversation;

/**
 * The query builder for fetching conversation member details request.
 */
public class FetchConversationMemberDetailsQuery {
  private final String conversationId;
  private final String userToken;
  private final String memberId;

  private FetchConversationMemberDetailsQuery(Builder builder) {
    this.conversationId = builder.conversationId;

    this.userToken = builder.userToken;
    this.memberId = builder.memberId;
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
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  /**
   * Gets member id.
   *
   * @return the member id
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * The builder class for building fetch conversation member details query.
   */
  public static class Builder {
    private String conversationId;
    private String userToken;
    private String memberId;

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
    public Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets member id.
     *
     * @param memberId the member id
     * @return the member id
     */
    public Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    /**
     * Build fetch conversation member details query.
     *
     * @return the fetch conversation member details query
     */
    public FetchConversationMemberDetailsQuery build() {
      return new FetchConversationMemberDetailsQuery(this);
    }
  }
}
