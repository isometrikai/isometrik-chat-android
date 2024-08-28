package io.isometrik.chat.builder.message.cleanup;

import java.util.List;

/**
 * The query builder for removing messages for self request.
 */
public class RemoveMessagesForSelfQuery {
  private final String conversationId;
  private final List<String> messageIds;
  private final String userToken;

  private RemoveMessagesForSelfQuery(RemoveMessagesForSelfQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.messageIds = builder.messageIds;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building remove messages for self query.
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
    public RemoveMessagesForSelfQuery.Builder setMessageIds(List<String> messageIds) {
      this.messageIds = messageIds;
      return this;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public RemoveMessagesForSelfQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public RemoveMessagesForSelfQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build remove messages for self query.
     *
     * @return the remove messages for self query
     */
    public RemoveMessagesForSelfQuery build() {
      return new RemoveMessagesForSelfQuery(this);
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
