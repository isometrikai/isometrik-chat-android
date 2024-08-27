package io.isometrik.chat.builder.conversation.cleanup;

/**
 * The query builder for deleting conversation locally request.
 */
public class DeleteConversationLocallyQuery {
  private final String conversationId;
  private final String userToken;

  private DeleteConversationLocallyQuery(DeleteConversationLocallyQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building delete conversation locally query.
   */
  public static class Builder {
    private String conversationId;
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
    public DeleteConversationLocallyQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public DeleteConversationLocallyQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build delete conversation locally query.
     *
     * @return the delete conversation locally query
     */
    public DeleteConversationLocallyQuery build() {
      return new DeleteConversationLocallyQuery(this);
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
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }
}
