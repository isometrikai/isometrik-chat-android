package io.isometrik.chat.builder.conversation;

/**
 * The query builder for fetching conversation details request.
 */
public class FetchConversationDetailsQuery {
  private final String conversationId;
  private final Boolean includeMembers;
  private final String userToken;
  private final Integer membersSkip;
  private final Integer membersLimit;

  private FetchConversationDetailsQuery(FetchConversationDetailsQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.includeMembers = builder.includeMembers;
    this.userToken = builder.userToken;
    this.membersSkip = builder.membersSkip;
    this.membersLimit = builder.membersLimit;
  }

  /**
   * The builder class for building fetch conversation details query.
   */
  public static class Builder {
    private String conversationId;
    private Boolean includeMembers;
    private String userToken;
    private Integer membersSkip;
    private Integer membersLimit;

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
    public FetchConversationDetailsQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets include members.
     *
     * @param includeMembers the include members
     * @return the include members
     */
    public FetchConversationDetailsQuery.Builder setIncludeMembers(Boolean includeMembers) {
      this.includeMembers = includeMembers;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchConversationDetailsQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets members skip.
     *
     * @param membersSkip the members skip
     * @return the members skip
     */
    public FetchConversationDetailsQuery.Builder setMembersSkip(Integer membersSkip) {
      this.membersSkip = membersSkip;
      return this;
    }

    /**
     * Sets members limit.
     *
     * @param membersLimit the members limit
     * @return the members limit
     */
    public FetchConversationDetailsQuery.Builder setMembersLimit(Integer membersLimit) {
      this.membersLimit = membersLimit;
      return this;
    }

    /**
     * Build fetch conversation details query.
     *
     * @return the fetch conversation details query
     */
    public FetchConversationDetailsQuery build() {
      return new FetchConversationDetailsQuery(this);
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
   * Gets include members.
   *
   * @return the include members
   */
  public Boolean getIncludeMembers() {
    return includeMembers;
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
   * Gets members skip.
   *
   * @return the members skip
   */
  public Integer getMembersSkip() {
    return membersSkip;
  }

  /**
   * Gets members limit.
   *
   * @return the members limit
   */
  public Integer getMembersLimit() {
    return membersLimit;
  }
}
