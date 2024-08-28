package io.isometrik.chat.builder.deliverystatus;

import java.util.List;

/**
 * The query builder for fetching messages delivery read status request.
 */
public class FetchMessagesDeliveryReadStatusQuery {

  private final String conversationId;
  private final List<String> messageIds;
  private final String userToken;

  private FetchMessagesDeliveryReadStatusQuery(
      FetchMessagesDeliveryReadStatusQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.messageIds = builder.messageIds;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch messages delivery read status query.
   */
  public static class Builder {
    private String conversationId;
    private List<String> messageIds;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets message ids.
     *
     * @param messageIds the message ids
     * @return the message ids
     */
    public FetchMessagesDeliveryReadStatusQuery.Builder setMessageIds(List<String> messageIds) {
      this.messageIds = messageIds;
      return this;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public FetchMessagesDeliveryReadStatusQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchMessagesDeliveryReadStatusQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch messages delivery read status query.
     *
     * @return the fetch messages delivery read status query
     */
    public FetchMessagesDeliveryReadStatusQuery build() {
      return new FetchMessagesDeliveryReadStatusQuery(this);
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
   * Gets message ids.
   *
   * @return the message ids
   */
  public List<String> getMessageIds() {
    return messageIds;
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
