package io.isometrik.chat.builder.message;

import java.util.List;

/**
 * The query builder for fetching messages request.
 */
public class FetchMessagesQuery {
  private final Integer sort;
  private final Integer limit;
  private final Integer skip;
  private final String conversationId;
  private final String parentMessageId;
  private final Boolean conversationStatusMessage;
  private final Long lastMessageTimestamp;
  private final List<String> messageIds;
  private final List<Integer> messageTypes;
  private final List<Integer> attachmentTypes;
  private final List<String> customTypes;
  private final List<String> senderIds;
  private final Boolean showInConversation;
  private final String userToken;
  private final String searchTag;
  private final Boolean deliveredToMe;
  private final Boolean senderIdsExclusive;

  private FetchMessagesQuery(Builder builder) {
    this.sort = builder.sort;
    this.limit = builder.limit;
    this.skip = builder.skip;
    this.conversationId = builder.conversationId;
    this.parentMessageId = builder.parentMessageId;
    this.conversationStatusMessage = builder.conversationStatusMessage;
    this.lastMessageTimestamp = builder.lastMessageTimestamp;
    this.messageIds = builder.messageIds;
    this.messageTypes = builder.messageTypes;
    this.attachmentTypes = builder.attachmentTypes;
    this.customTypes = builder.customTypes;
    this.senderIds = builder.senderIds;
    this.showInConversation = builder.showInConversation;
    this.userToken = builder.userToken;
    this.searchTag = builder.searchTag;
    this.deliveredToMe = builder.deliveredToMe;
    this.senderIdsExclusive = builder.senderIdsExclusive;
  }

  /**
   * Gets delivered to me.
   *
   * @return the delivered to me
   */
  public Boolean getDeliveredToMe() {
    return deliveredToMe;
  }

  /**
   * Gets sort.
   *
   * @return the sort
   */
  public Integer getSort() {
    return sort;
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
   * Gets skip.
   *
   * @return the skip
   */
  public Integer getSkip() {
    return skip;
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
   * Gets parent message id.
   *
   * @return the parent message id
   */
  public String getParentMessageId() {
    return parentMessageId;
  }

  /**
   * Gets conversation status message.
   *
   * @return the conversation status message
   */
  public Boolean getConversationStatusMessage() {
    return conversationStatusMessage;
  }

  /**
   * Gets last message timestamp.
   *
   * @return the last message timestamp
   */
  public Long getLastMessageTimestamp() {
    return lastMessageTimestamp;
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
   * Gets message types.
   *
   * @return the message types
   */
  public List<Integer> getMessageTypes() {
    return messageTypes;
  }

  /**
   * Gets attachment types.
   *
   * @return the attachment types
   */
  public List<Integer> getAttachmentTypes() {
    return attachmentTypes;
  }

  /**
   * Gets custom types.
   *
   * @return the custom types
   */
  public List<String> getCustomTypes() {
    return customTypes;
  }

  /**
   * Gets sender ids.
   *
   * @return the sender ids
   */
  public List<String> getSenderIds() {
    return senderIds;
  }

  /**
   * Gets show in conversation.
   *
   * @return the show in conversation
   */
  public Boolean getShowInConversation() {
    return showInConversation;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  /**
   * Gets search tag.
   *
   * @return the search tag
   */
  public String getSearchTag() {
    return searchTag;
  }

  /**
   * Gets sender ids exclusive.
   *
   * @return the sender ids exclusive
   */
  public Boolean getSenderIdsExclusive() {
    return senderIdsExclusive;
  }

  /**
   * The builder class for building fetch messages query.
   */
  public static class Builder {
    private Integer sort;
    private Integer limit;
    private Integer skip;
    private String conversationId;
    private String parentMessageId;
    private Boolean conversationStatusMessage;
    private Long lastMessageTimestamp;
    private List<String> messageIds;
    private List<Integer> messageTypes;
    private List<Integer> attachmentTypes;
    private List<String> customTypes;
    private List<String> senderIds;
    private Boolean showInConversation;
    private String userToken;
    private String searchTag;
    private Boolean deliveredToMe;
    private Boolean senderIdsExclusive;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets sort.
     *
     * @param sort the sort
     * @return the sort
     */
    public Builder setSort(Integer sort) {
      this.sort = sort;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets parent message id.
     *
     * @param parentMessageId the parent message id
     * @return the parent message id
     */
    public Builder setParentMessageId(String parentMessageId) {
      this.parentMessageId = parentMessageId;
      return this;
    }

    /**
     * Sets conversation status message.
     *
     * @param conversationStatusMessage the conversation status message
     * @return the conversation status message
     */
    public Builder setConversationStatusMessage(Boolean conversationStatusMessage) {
      this.conversationStatusMessage = conversationStatusMessage;
      return this;
    }

    /**
     * Sets last message timestamp.
     *
     * @param lastMessageTimestamp the last message timestamp
     * @return the last message timestamp
     */
    public Builder setLastMessageTimestamp(Long lastMessageTimestamp) {
      this.lastMessageTimestamp = lastMessageTimestamp;
      return this;
    }

    /**
     * Sets message ids.
     *
     * @param messageIds the message ids
     * @return the message ids
     */
    public Builder setMessageIds(List<String> messageIds) {
      this.messageIds = messageIds;
      return this;
    }

    /**
     * Sets message types.
     *
     * @param messageTypes the message types
     * @return the message types
     */
    public Builder setMessageTypes(List<Integer> messageTypes) {
      this.messageTypes = messageTypes;
      return this;
    }

    /**
     * Sets attachment types.
     *
     * @param attachmentTypes the attachment types
     * @return the attachment types
     */
    public Builder setAttachmentTypes(List<Integer> attachmentTypes) {
      this.attachmentTypes = attachmentTypes;
      return this;
    }

    /**
     * Sets custom types.
     *
     * @param customTypes the custom types
     * @return the custom types
     */
    public Builder setCustomTypes(List<String> customTypes) {
      this.customTypes = customTypes;
      return this;
    }

    /**
     * Sets sender ids.
     *
     * @param senderIds the sender ids
     * @return the sender ids
     */
    public Builder setSenderIds(List<String> senderIds) {
      this.senderIds = senderIds;
      return this;
    }

    /**
     * Sets show in conversation.
     *
     * @param showInConversation the show in conversation
     * @return the show in conversation
     */
    public Builder setShowInConversation(Boolean showInConversation) {
      this.showInConversation = showInConversation;
      return this;
    }

    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets delivered to me.
     *
     * @param deliveredToMe the delivered to me
     * @return the delivered to me
     */
    public Builder setDeliveredToMe(Boolean deliveredToMe) {
      this.deliveredToMe = deliveredToMe;
      return this;
    }

    /**
     * Sets sender ids exclusive.
     *
     * @param senderIdsExclusive the sender ids exclusive
     * @return the sender ids exclusive
     */
    public Builder setSenderIdsExclusive(Boolean senderIdsExclusive) {
      this.senderIdsExclusive = senderIdsExclusive;
      return this;
    }

    /**
     * Build fetch messages query.
     *
     * @return the fetch messages query
     */
    public FetchMessagesQuery build() {
      return new FetchMessagesQuery(this);
    }
  }
}
