package io.isometrik.chat.builder.conversation;

import java.util.List;

/**
 * The query builder for fetching conversations count request.
 */
public class FetchConversationsCountQuery {

  private final List<String> membersIncluded;
  private final List<String> membersExactly;
  private final List<String> conversationIds;
  private final String customType;
  private final Integer conversationType;
  private final Boolean isGroup;
  private final String userToken;

  private FetchConversationsCountQuery(FetchConversationsCountQuery.Builder builder) {

    this.membersIncluded = builder.membersIncluded;
    this.membersExactly = builder.membersExactly;
    this.conversationIds = builder.conversationIds;
    this.customType = builder.customType;
    this.conversationType = builder.conversationType;
    this.isGroup = builder.isGroup;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building fetch conversations count query.
   */
  public static class Builder {

    private List<String> membersIncluded;
    private List<String> membersExactly;
    private List<String> conversationIds;
    private String customType;
    private Integer conversationType;
    private Boolean isGroup;
    private String userToken;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets members included.
     *
     * @param membersIncluded the members included
     * @return the members included
     */
    public FetchConversationsCountQuery.Builder setMembersIncluded(List<String> membersIncluded) {
      this.membersIncluded = membersIncluded;
      return this;
    }

    /**
     * Sets members exactly.
     *
     * @param membersExactly the members exactly
     * @return the members exactly
     */
    public FetchConversationsCountQuery.Builder setMembersExactly(List<String> membersExactly) {
      this.membersExactly = membersExactly;
      return this;
    }

    /**
     * Sets conversation ids.
     *
     * @param conversationIds the conversation ids
     * @return the conversation ids
     */
    public FetchConversationsCountQuery.Builder setConversationIds(List<String> conversationIds) {
      this.conversationIds = conversationIds;
      return this;
    }

    /**
     * Sets custom type.
     *
     * @param customType the custom type
     * @return the custom type
     */
    public FetchConversationsCountQuery.Builder setCustomType(String customType) {
      this.customType = customType;
      return this;
    }

    /**
     * Sets conversation type.
     *
     * @param conversationType the conversation type
     * @return the conversation type
     */
    public FetchConversationsCountQuery.Builder setConversationType(Integer conversationType) {
      this.conversationType = conversationType;
      return this;
    }

    /**
     * Sets group.
     *
     * @param group the group
     * @return the group
     */
    public FetchConversationsCountQuery.Builder setGroup(Boolean group) {
      isGroup = group;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchConversationsCountQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch conversations count query.
     *
     * @return the fetch conversations count query
     */
    public FetchConversationsCountQuery build() {
      return new FetchConversationsCountQuery(this);
    }
  }

  /**
   * Gets members included.
   *
   * @return the members included
   */
  public List<String> getMembersIncluded() {
    return membersIncluded;
  }

  /**
   * Gets members exactly.
   *
   * @return the members exactly
   */
  public List<String> getMembersExactly() {
    return membersExactly;
  }

  /**
   * Gets conversation ids.
   *
   * @return the conversation ids
   */
  public List<String> getConversationIds() {
    return conversationIds;
  }

  /**
   * Gets custom type.
   *
   * @return the custom type
   */
  public String getCustomType() {
    return customType;
  }

  /**
   * Gets conversation type.
   *
   * @return the conversation type
   */
  public Integer getConversationType() {
    return conversationType;
  }

  /**
   * Gets group.
   *
   * @return the group
   */
  public Boolean getGroup() {
    return isGroup;
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
