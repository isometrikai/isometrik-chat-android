package io.isometrik.chat.builder.message.delivery;

/**
 * The query builder for marking message as delivered request.
 */
public class MarkMessageAsDeliveredQuery {
  private final String conversationId;
  private final String messageId;
  private final String userToken;

  private MarkMessageAsDeliveredQuery(MarkMessageAsDeliveredQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.messageId = builder.messageId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building mark message as delivered query.
   */
  public static class Builder {
    private String conversationId;
    private String messageId;
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
    public MarkMessageAsDeliveredQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     * @return the message id
     */
    public MarkMessageAsDeliveredQuery.Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public MarkMessageAsDeliveredQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build mark message as delivered query.
     *
     * @return the mark message as delivered query
     */
    public MarkMessageAsDeliveredQuery build() {
      return new MarkMessageAsDeliveredQuery(this);
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
