package io.isometrik.chat.builder.conversation;

/**
 * The query builder for fetching conversation presigned url request.
 */
public class FetchConversationPresignedUrlQuery {

  private final String conversationTitle;
  private final String userToken;
  private final String mediaExtension;
  private final String conversationId;
  private final Boolean newConversation;

  private FetchConversationPresignedUrlQuery(Builder builder) {
    this.conversationTitle = builder.conversationTitle;
    this.mediaExtension = builder.mediaExtension;
    this.userToken = builder.userToken;
    this.conversationId = builder.conversationId;
    this.newConversation = builder.newConversation;
  }

  /**
   * The builder class for building fetch conversation presigned url query.
   */
  public static class Builder {
    private String conversationTitle;
    private String userToken;
    private String mediaExtension;
    private String conversationId;
    private Boolean newConversation;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets new conversation.
     *
     * @param newConversation the new conversation
     * @return the new conversation
     */
    public Builder setNewConversation(Boolean newConversation) {
      this.newConversation = newConversation;
      return this;
    }

    /**
     * Sets conversation title.
     *
     * @param conversationTitle the conversation title
     * @return the conversation title
     */
    public Builder setConversationTitle(String conversationTitle) {
      this.conversationTitle = conversationTitle;
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
     * Sets media extension.
     *
     * @param mediaExtension the media extension
     * @return the media extension
     */
    public Builder setMediaExtension(String mediaExtension) {
      this.mediaExtension = mediaExtension;
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
     * Build fetch conversation presigned url query.
     *
     * @return the fetch conversation presigned url query
     */
    public FetchConversationPresignedUrlQuery build() {
      return new FetchConversationPresignedUrlQuery(this);
    }
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

  /**
   * Gets media extension.
   *
   * @return the media extension
   */
  public String getMediaExtension() {
    return mediaExtension;
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
   * Gets new conversation.
   *
   * @return the new conversation
   */
  public Boolean getNewConversation() {
    return newConversation;
  }
}
