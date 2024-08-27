package io.isometrik.chat.builder.conversation.config;

import java.util.List;
import org.json.JSONObject;

/**
 * The query builder for updating conversation details request.
 */
public class UpdateConversationDetailsQuery {
  private final String conversationId;
  private final String userToken;
  private final List<String> searchableTags;
  private final JSONObject metaData;
  private final String customType;

  private UpdateConversationDetailsQuery(UpdateConversationDetailsQuery.Builder builder) {
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
    this.searchableTags = builder.searchableTags;
    this.metaData = builder.metaData;
    this.customType = builder.customType;
  }

  /**
   * The builder class for building update conversation details query.
   */
  public static class Builder {
    private String conversationId;
    private String userToken;
    private List<String> searchableTags;
    private JSONObject metaData;
    private String customType;

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
    public UpdateConversationDetailsQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets searchable tags.
     *
     * @param searchableTags the searchable tags
     * @return the searchable tags
     */
    public UpdateConversationDetailsQuery.Builder setSearchableTags(List<String> searchableTags) {
      this.searchableTags = searchableTags;
      return this;
    }

    /**
     * Sets meta data.
     *
     * @param metaData the meta data
     * @return the meta data
     */
    public UpdateConversationDetailsQuery.Builder setMetaData(JSONObject metaData) {
      this.metaData = metaData;
      return this;
    }

    /**
     * Sets custom type.
     *
     * @param customType the custom type
     * @return the custom type
     */
    public UpdateConversationDetailsQuery.Builder setCustomType(String customType) {
      this.customType = customType;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public UpdateConversationDetailsQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build update conversation details query.
     *
     * @return the update conversation details query
     */
    public UpdateConversationDetailsQuery build() {
      return new UpdateConversationDetailsQuery(this);
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

  /**
   * Gets searchable tags.
   *
   * @return the searchable tags
   */
  public List<String> getSearchableTags() {
    return searchableTags;
  }

  /**
   * Gets meta data.
   *
   * @return the meta data
   */
  public JSONObject getMetaData() {
    return metaData;
  }

  /**
   * Gets custom type.
   *
   * @return the custom type
   */
  public String getCustomType() {
    return customType;
  }
}
