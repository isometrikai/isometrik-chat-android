package io.isometrik.chat.builder.membershipcontrol;

/**
 * The query builder for removing admin request.
 */
public class RemoveAdminQuery {
  private final String conversationId;
  private final String memberId;
  private final String userToken;

  private RemoveAdminQuery(RemoveAdminQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.memberId = builder.memberId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building remove admin query.
   */
  public static class Builder {
    private String conversationId;
    private String memberId;
    private String userToken;

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
    public RemoveAdminQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets member id.
     *
     * @param memberId the member id
     * @return the member id
     */
    public RemoveAdminQuery.Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public RemoveAdminQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build remove admin query.
     *
     * @return the remove admin query
     */
    public RemoveAdminQuery build() {
      return new RemoveAdminQuery(this);
    }
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
   * Gets member id.
   *
   * @return the member id
   */
  public String getMemberId() {
    return memberId;
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
