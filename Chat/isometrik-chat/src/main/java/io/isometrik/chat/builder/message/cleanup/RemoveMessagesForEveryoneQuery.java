package io.isometrik.chat.builder.message.cleanup;

import java.util.List;

/**
 * The query builder for removing messages for everyone request.
 */
public class RemoveMessagesForEveryoneQuery {
  private final String conversationId;
  private final List<String> messageIds;
  private final String userToken;

  private RemoveMessagesForEveryoneQuery(RemoveMessagesForEveryoneQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.messageIds = builder.messageIds;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building remove messages for everyone query.
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
    public RemoveMessagesForEveryoneQuery.Builder setMessageIds(List<String> messageIds) {
      this.messageIds = messageIds;
      return this;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public RemoveMessagesForEveryoneQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public RemoveMessagesForEveryoneQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build remove messages for everyone query.
     *
     * @return the remove messages for everyone query
     */
    public RemoveMessagesForEveryoneQuery build() {
      return new RemoveMessagesForEveryoneQuery(this);
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
