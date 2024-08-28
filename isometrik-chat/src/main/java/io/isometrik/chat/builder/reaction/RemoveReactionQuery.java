package io.isometrik.chat.builder.reaction;

/**
 * The query builder for removing reaction request.
 */
public class RemoveReactionQuery {
  private final String reactionType;
  private final String conversationId;
  private final String messageId;
  private final String userToken;

  private RemoveReactionQuery(RemoveReactionQuery.Builder builder) {
    this.reactionType = builder.reactionType;
    this.conversationId = builder.conversationId;
    this.messageId = builder.messageId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building remove reaction query.
   */
  public static class Builder {
    private String reactionType;
    private String conversationId;
    private String messageId;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets reaction type.
     *
     * @param reactionType the reaction type
     * @return the reaction type
     */
    public RemoveReactionQuery.Builder setReactionType(String reactionType) {
      this.reactionType = reactionType;
      return this;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public RemoveReactionQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     * @return the message id
     */
    public RemoveReactionQuery.Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public RemoveReactionQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build remove reaction query.
     *
     * @return the remove reaction query
     */
    public RemoveReactionQuery build() {
      return new RemoveReactionQuery(this);
    }
  }

  /**
   * Gets reaction type.
   *
   * @return the reaction type
   */
  public String getReactionType() {
    return reactionType;
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
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
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
