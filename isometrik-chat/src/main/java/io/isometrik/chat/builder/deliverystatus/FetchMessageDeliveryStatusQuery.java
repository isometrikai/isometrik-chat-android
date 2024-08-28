package io.isometrik.chat.builder.deliverystatus;

/**
 * The query builder for fetching message delivery status request.
 */
public class FetchMessageDeliveryStatusQuery {
  private final String conversationId;
  private final String messageId;
  private final Integer skip;
  private final Integer limit;
  private final String userToken;

  private FetchMessageDeliveryStatusQuery(FetchMessageDeliveryStatusQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.messageId = builder.messageId;
    this.skip = builder.skip;
    this.limit = builder.limit;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch message delivery status query.
   */
  public static class Builder {
    private String conversationId;
    private String messageId;
    private Integer skip;
    private Integer limit;
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
    public FetchMessageDeliveryStatusQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     * @return the message id
     */
    public FetchMessageDeliveryStatusQuery.Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchMessageDeliveryStatusQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchMessageDeliveryStatusQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchMessageDeliveryStatusQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch message delivery status query.
     *
     * @return the fetch message delivery status query
     */
    public FetchMessageDeliveryStatusQuery build() {
      return new FetchMessageDeliveryStatusQuery(this);
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
   * Gets skip.
   *
   * @return the skip
   */
  public Integer getSkip() {
    return skip;
  }

  /**
   * Gets limit.
   *
   * @return the limit
   */
  public Integer getLimit() {
    return limit;
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
