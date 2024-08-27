package io.isometrik.chat.builder.conversation;

import java.util.List;

/**
 * The query builder for fetching public or open conversations request.
 */
public class FetchPublicOrOpenConversationsQuery {
  private final Integer sort;
  private final Integer limit;
  private final Integer skip;
  private final Boolean includeMembers;
  private final List<String> membersIncluded;
  private final List<String> membersExactly;
  private final List<String> conversationIds;
  private final String customType;

  private final String userToken;
  private final Integer membersSkip;
  private final Integer membersLimit;
  private final String searchTag;
  private final Integer conversationType;

  private FetchPublicOrOpenConversationsQuery(Builder builder) {
    this.sort = builder.sort;
    this.limit = builder.limit;
    this.skip = builder.skip;
    this.includeMembers = builder.includeMembers;
    this.membersIncluded = builder.membersIncluded;
    this.membersExactly = builder.membersExactly;
    this.conversationIds = builder.conversationIds;
    this.customType = builder.customType;
    this.userToken = builder.userToken;
    this.membersSkip = builder.membersSkip;
    this.membersLimit = builder.membersLimit;
    this.searchTag = builder.searchTag;
    this.conversationType = builder.conversationType;
  }

  /**
   * The builder class for building fetch public or open conversations query.
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
    private String userToken;
    private Integer membersSkip;
    private Integer membersLimit;
    private String searchTag;
    private Integer conversationType;

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
     * Sets include members.
     *
     * @param includeMembers the include members
     * @return the include members
     */
    public Builder setIncludeMembers(Boolean includeMembers) {
      this.includeMembers = includeMembers;
      return this;
    }

    /**
     * Sets members included.
     *
     * @param membersIncluded the members included
     * @return the members included
     */
    public Builder setMembersIncluded(List<String> membersIncluded) {
      this.membersIncluded = membersIncluded;
      return this;
    }

    /**
     * Sets members exactly.
     *
     * @param membersExactly the members exactly
     * @return the members exactly
     */
    public Builder setMembersExactly(List<String> membersExactly) {
      this.membersExactly = membersExactly;
      return this;
    }

    /**
     * Sets conversation ids.
     *
     * @param conversationIds the conversation ids
     * @return the conversation ids
     */
    public Builder setConversationIds(List<String> conversationIds) {
      this.conversationIds = conversationIds;
      return this;
    }

    /**
     * Sets custom type.
     *
     * @param customType the custom type
     * @return the custom type
     */
    public Builder setCustomType(String customType) {
      this.customType = customType;
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
     * Sets members skip.
     *
     * @param membersSkip the members skip
     * @return the members skip
     */
    public Builder setMembersSkip(Integer membersSkip) {
      this.membersSkip = membersSkip;
      return this;
    }

    /**
     * Sets members limit.
     *
     * @param membersLimit the members limit
     * @return the members limit
     */
    public Builder setMembersLimit(Integer membersLimit) {
      this.membersLimit = membersLimit;
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
     * Sets conversation type.
     *
     * @param conversationType the conversation type
     * @return the conversation type
     */
    public Builder setConversationType(Integer conversationType) {
      this.conversationType = conversationType;
      return this;
    }

    /**
     * Build fetch public or open conversations query.
     *
     * @return the fetch public or open conversations query
     */
    public FetchPublicOrOpenConversationsQuery build() {
      return new FetchPublicOrOpenConversationsQuery(this);
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

  /**
   * Gets conversation type.
   *
   * @return the conversation type
   */
  public Integer getConversationType() {
    return conversationType;
  }
}
