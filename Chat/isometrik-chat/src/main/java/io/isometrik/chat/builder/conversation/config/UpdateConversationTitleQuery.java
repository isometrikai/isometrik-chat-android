package io.isometrik.chat.builder.conversation.config;

/**
 * The query builder for updating conversation title request.
 */
public class UpdateConversationTitleQuery {
  private final String conversationId;
  private final String conversationTitle;
  private final String userToken;

  private UpdateConversationTitleQuery(UpdateConversationTitleQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.conversationTitle = builder.conversationTitle;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building update conversation title query.
   */
  public static class Builder {
    private String conversationId;
    private String conversationTitle;
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
    public UpdateConversationTitleQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets conversation title.
     *
     * @param conversationTitle the conversation title
     * @return the conversation title
     */
    public UpdateConversationTitleQuery.Builder setConversationTitle(String conversationTitle) {
      this.conversationTitle = conversationTitle;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public UpdateConversationTitleQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build update conversation title query.
     *
     * @return the update conversation title query
     */
    public UpdateConversationTitleQuery build() {
      return new UpdateConversationTitleQuery(this);
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
   * Gets conversation title.
   *
   * @return the conversation title
   */
  public String getConversationTitle() {
    return conversationTitle;
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
