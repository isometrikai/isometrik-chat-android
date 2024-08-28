package io.isometrik.chat.builder.message;

import java.util.List;
import org.json.JSONObject;

/**
 * The query builder for updating message details request.
 */
public class UpdateMessageDetailsQuery {
  private final String conversationId;
  private final String userToken;
  private final List<String> searchableTags;
  private final JSONObject metaData;
  private final String customType;
  private final String body;
  private final String messageId;

  private UpdateMessageDetailsQuery(Builder builder) {
    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
    this.searchableTags = builder.searchableTags;
    this.metaData = builder.metaData;
    this.customType = builder.customType;
    this.body = builder.body;
    this.messageId = builder.messageId;
  }

  /**
   * The builder class for building update message details query.
   */
  public static class Builder {
    private String conversationId;
    private String userToken;
    private List<String> searchableTags;
    private JSONObject metaData;
    private String customType;
    private String body;
    private String messageId;

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
    public Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets searchable tags.
     *
     * @param searchableTags the searchable tags
     * @return the searchable tags
     */
    public Builder setSearchableTags(List<String> searchableTags) {
      this.searchableTags = searchableTags;
      return this;
    }

    /**
     * Sets meta data.
     *
     * @param metaData the meta data
     * @return the meta data
     */
    public Builder setMetaData(JSONObject metaData) {
      this.metaData = metaData;
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
     * Sets body.
     *
     * @param body the body
     * @return the body
     */
    public Builder setBody(String body) {
      this.body = body;
      return this;
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     * @return the message id
     */
    public Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Build update message details query.
     *
     * @return the update message details query
     */
    public UpdateMessageDetailsQuery build() {
      return new UpdateMessageDetailsQuery(this);
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

  /**
   * Gets body.
   *
   * @return the body
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }
}
