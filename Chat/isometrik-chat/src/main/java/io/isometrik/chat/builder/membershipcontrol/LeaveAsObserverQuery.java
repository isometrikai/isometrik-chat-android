package io.isometrik.chat.builder.membershipcontrol;

/**
 * The query builder for leaving as observer request.
 */
public class LeaveAsObserverQuery {
  private final String conversationId;
  private final String userToken;

  private LeaveAsObserverQuery(LeaveAsObserverQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building leave as observer query.
   */
  public static class Builder {
    private String conversationId;
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
    public LeaveAsObserverQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public LeaveAsObserverQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build leave as observer query.
     *
     * @return the leave as observer query
     */
    public LeaveAsObserverQuery build() {
      return new LeaveAsObserverQuery(this);
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
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }
}
