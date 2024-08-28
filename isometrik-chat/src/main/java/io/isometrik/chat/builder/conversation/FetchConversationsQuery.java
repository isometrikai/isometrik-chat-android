package io.isometrik.chat.builder.conversation;

import java.util.List;

/**
 * The query builder for fetching conversations request.
 */
public class FetchConversationsQuery {
  private final Integer sort;
  private final Integer limit;
  private final Integer skip;
  private final Boolean includeMembers;
  private final List<String> membersIncluded;
  private final List<String> membersExactly;
  private final List<String> conversationIds;
  private final String customType;
  private final Integer conversationType;
  private final Boolean isGroup;
  private final String userToken;
  private final Integer membersSkip;
  private final Integer membersLimit;
  private final String searchTag;

  private FetchConversationsQuery(FetchConversationsQuery.Builder builder) {
    this.sort = builder.sort;
    this.limit = builder.limit;
    this.skip = builder.skip;
    this.includeMembers = builder.includeMembers;
    this.membersIncluded = builder.membersIncluded;
    this.membersExactly = builder.membersExactly;
    this.conversationIds = builder.conversationIds;
    this.customType = builder.customType;
    this.conversationType = builder.conversationType;
    this.isGroup = builder.isGroup;
    this.userToken = builder.userToken;
    this.membersSkip = builder.membersSkip;
    this.membersLimit = builder.membersLimit;
    this.searchTag = builder.searchTag;
  }

  /**
   * The builder class for building fetch conversations query.
   */
  public static class Builder {
    private Integer sort;
    private Integer limit;
    private Integer skip;
    private Boolean includeMembers;
    private List<String> membersIncluded;
    private List<String> membersExactly;
    private List<String> conversationIds;
    private String customType;
    private Integer conversationType;
    private Boolean isGroup;
    private String userToken;
    private Integer membersSkip;
    private Integer membersLimit;
    private String searchTag;

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
    public FetchConversationsQuery.Builder setSort(Integer sort) {
      this.sort = sort;
      return this;
    }

    /**
     * Sets limit.
     *
     * @param limit the limit
     * @return the limit
     */
    public FetchConversationsQuery.Builder setLimit(Integer limit) {
      this.limit = limit;
      return this;
    }

    /**
     * Sets skip.
     *
     * @param skip the skip
     * @return the skip
     */
    public FetchConversationsQuery.Builder setSkip(Integer skip) {
      this.skip = skip;
      return this;
    }

    /**
     * Sets include members.
     *
     * @param includeMembers the include members
     * @return the include members
     */
    public FetchConversationsQuery.Builder setIncludeMembers(Boolean includeMembers) {
      this.includeMembers = includeMembers;
      return this;
    }

    /**
     * Sets members included.
     *
     * @param membersIncluded the members included
     * @return the members included
     */
    public FetchConversationsQuery.Builder setMembersIncluded(List<String> membersIncluded) {
      this.membersIncluded = membersIncluded;
      return this;
    }

    /**
     * Sets members exactly.
     *
     * @param membersExactly the members exactly
     * @return the members exactly
     */
    public FetchConversationsQuery.Builder setMembersExactly(List<String> membersExactly) {
      this.membersExactly = membersExactly;
      return this;
    }

    /**
     * Sets conversation ids.
     *
     * @param conversationIds the conversation ids
     * @return the conversation ids
     */
    public FetchConversationsQuery.Builder setConversationIds(List<String> conversationIds) {
      this.conversationIds = conversationIds;
      return this;
    }

    /**
     * Sets custom type.
     *
     * @param customType the custom type
     * @return the custom type
     */
    public FetchConversationsQuery.Builder setCustomType(String customType) {
      this.customType = customType;
      return this;
    }

    /**
     * Sets conversation type.
     *
     * @param conversationType the conversation type
     * @return the conversation type
     */
    public FetchConversationsQuery.Builder setConversationType(Integer conversationType) {
      this.conversationType = conversationType;
      return this;
    }

    /**
     * Sets group.
     *
     * @param group the group
     * @return the group
     */
    public FetchConversationsQuery.Builder setGroup(Boolean group) {
      isGroup = group;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchConversationsQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets members skip.
     *
     * @param membersSkip the members skip
     * @return the members skip
     */
    public FetchConversationsQuery.Builder setMembersSkip(Integer membersSkip) {
      this.membersSkip = membersSkip;
      return this;
    }

    /**
     * Sets members limit.
     *
     * @param membersLimit the members limit
     * @return the members limit
     */
    public FetchConversationsQuery.Builder setMembersLimit(Integer membersLimit) {
      this.membersLimit = membersLimit;
      return this;
    }

    /**
     * Sets search tag.
     *
     * @param searchTag the search tag
     * @return the search tag
     */
    public FetchConversationsQuery.Builder setSearchTag(String searchTag) {
      this.searchTag = searchTag;
      return this;
    }

    /**
     * Build fetch conversations query.
     *
     * @return the fetch conversations query
     */
    public FetchConversationsQuery build() {
      return new FetchConversationsQuery(this);
    }
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
   * Gets include members.
   *
   * @return the include members
   */
  public Boolean getIncludeMembers() {
    return includeMembers;
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

  /**
   * Gets search tag.
   *
   * @return the search tag
   */
  public String getSearchTag() {
    return searchTag;
  }
}
