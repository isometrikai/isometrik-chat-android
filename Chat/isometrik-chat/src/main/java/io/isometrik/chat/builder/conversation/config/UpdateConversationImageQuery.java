package io.isometrik.chat.builder.conversation.config;

/**
 * The query builder for updating conversation image request.
 */
public class UpdateConversationImageQuery {
  private final String conversationId;
  private final String conversationImageUrl;
  private final String userToken;

  private UpdateConversationImageQuery(UpdateConversationImageQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.conversationImageUrl = builder.conversationImageUrl;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building update conversation image query.
   */
  public static class Builder {
    private String conversationId;
    private String conversationImageUrl;
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
    public UpdateConversationImageQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets conversation image url.
     *
     * @param conversationImageUrl the conversation image url
     * @return the conversation image url
     */
    public UpdateConversationImageQuery.Builder setConversationImageUrl(
        String conversationImageUrl) {
      this.conversationImageUrl = conversationImageUrl;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public UpdateConversationImageQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build update conversation image query.
     *
     * @return the update conversation image query
     */
    public UpdateConversationImageQuery build() {
      return new UpdateConversationImageQuery(this);
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
   * Gets conversation image url.
   *
   * @return the conversation image url
   */
  public String getConversationImageUrl() {
    return conversationImageUrl;
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
