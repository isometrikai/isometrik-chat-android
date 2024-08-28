package io.isometrik.chat.builder.membershipcontrol;

import java.util.List;

/**
 * The query builder for adding members request.
 */
public class AddMembersQuery {
  private final String conversationId;
  private final List<String> members;
  private final String userToken;

  private AddMembersQuery(AddMembersQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.members = builder.members;
    this.userToken = builder.userToken;
  }

  /**
   * The builder class for building add members query.
   */
  public static class Builder {
    private String conversationId;
    private List<String> members;
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
    public AddMembersQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets members.
     *
     * @param members the members
     * @return the members
     */
    public AddMembersQuery.Builder setMembers(List<String> members) {
      this.members = members;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public AddMembersQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build add members query.
     *
     * @return the add members query
     */
    public AddMembersQuery build() {
      return new AddMembersQuery(this);
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
   * Gets members.
   *
   * @return the members
   */
  public List<String> getMembers() {
    return members;
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
