package io.isometrik.chat.builder.message.broadcastforward;

import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.response.message.utils.schemas.EventForMessage;
import java.util.List;
import org.json.JSONObject;

/**
 * The query builder for broadcasting message request.
 */
public class BroadcastMessageQuery {
  private final Integer messageType;
  private final Boolean showInConversation;
  private final String body;
  private final Boolean encrypted;
  private final String userToken;
  private final String deviceId;

  private final EventForMessage eventForMessage;
  private final String customType;
  private final List<String> userIds;
  private final JSONObject metaData;
  private final List<Attachment> attachments;
  private final List<String> searchableTags;

  private BroadcastMessageQuery(BroadcastMessageQuery.Builder builder) {
    this.messageType = builder.messageType;
    this.showInConversation = builder.showInConversation;
    this.body = builder.body;
    this.encrypted = builder.encrypted;
    this.userToken = builder.userToken;
    this.deviceId = builder.deviceId;

    this.eventForMessage = builder.eventForMessage;
    this.customType = builder.customType;
    this.userIds = builder.userIds;
    this.metaData = builder.metaData;
    this.attachments = builder.attachments;
    this.searchableTags = builder.searchableTags;
  }

  /**
   * The builder class for building broadcast message query.
   */
  public static class Builder {
    private Integer messageType;
    private Boolean showInConversation;
    private String body;
    private Boolean encrypted;
    private String userToken;
    private String deviceId;

    private EventForMessage eventForMessage;
    private String customType;
    private List<String> userIds;
    private JSONObject metaData;
    private List<Attachment> attachments;
    private List<String> searchableTags;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets message type.
     *
     * @param messageType the message type
     * @return the message type
     */
    public BroadcastMessageQuery.Builder setMessageType(Integer messageType) {
      this.messageType = messageType;
      return this;
    }

    /**
     * Sets show in conversation.
     *
     * @param showInConversation the show in conversation
     * @return the show in conversation
     */
    public BroadcastMessageQuery.Builder setShowInConversation(Boolean showInConversation) {
      this.showInConversation = showInConversation;
      return this;
    }

    /**
     * Sets body.
     *
     * @param body the body
     * @return the body
     */
    public BroadcastMessageQuery.Builder setBody(String body) {
      this.body = body;
      return this;
    }

    /**
     * Sets encrypted.
     *
     * @param encrypted the encrypted
     * @return the encrypted
     */
    public BroadcastMessageQuery.Builder setEncrypted(Boolean encrypted) {
      this.encrypted = encrypted;
      return this;
    }

    /**
     * Sets event for message.
     *
     * @param eventForMessage the event for message
     * @return the event for message
     */
    public BroadcastMessageQuery.Builder setEventForMessage(EventForMessage eventForMessage) {
      this.eventForMessage = eventForMessage;
      return this;
    }

    /**
     * Sets custom type.
     *
     * @param customType the custom type
     * @return the custom type
     */
    public BroadcastMessageQuery.Builder setCustomType(String customType) {
      this.customType = customType;
      return this;
    }

    /**
     * Sets user ids.
     *
     * @param userIds the user ids
     * @return the user ids
     */
    public BroadcastMessageQuery.Builder setUserIds(List<String> userIds) {
      this.userIds = userIds;
      return this;
    }

    /**
     * Sets meta data.
     *
     * @param metaData the meta data
     * @return the meta data
     */
    public BroadcastMessageQuery.Builder setMetaData(JSONObject metaData) {
      this.metaData = metaData;
      return this;
    }

    /**
     * Sets attachments.
     *
     * @param attachments the attachments
     * @return the attachments
     */
    public BroadcastMessageQuery.Builder setAttachments(List<Attachment> attachments) {
      this.attachments = attachments;
      return this;
    }

    /**
     * Sets device id.
     *
     * @param deviceId the device id
     * @return the device id
     */
    public BroadcastMessageQuery.Builder setDeviceId(String deviceId) {
      this.deviceId = deviceId;
      return this;
    }

    /**
     * Sets searchable tags.
     *
     * @param searchableTags the searchable tags
     * @return the searchable tags
     */
    public BroadcastMessageQuery.Builder setSearchableTags(List<String> searchableTags) {
      this.searchableTags = searchableTags;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public BroadcastMessageQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build broadcast message query.
     *
     * @return the broadcast message query
     */
    public BroadcastMessageQuery build() {
      return new BroadcastMessageQuery(this);
    }
  }

  /**
   * Gets message type.
   *
   * @return the message type
   */
  public Integer getMessageType() {
    return messageType;
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
   * Gets body.
   *
   * @return the body
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets encrypted.
   *
   * @return the encrypted
   */
  public Boolean getEncrypted() {
    return encrypted;
  }

  /**
   * Gets event for message.
   *
   * @return the event for message
   */
  public EventForMessage getEventForMessage() {
    return eventForMessage;
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
   * Gets user ids.
   *
   * @return the user ids
   */
  public List<String> getUserIds() {
    return userIds;
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
   * Gets attachments.
   *
   * @return the attachments
   */
  public List<Attachment> getAttachments() {
    return attachments;
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
   * Gets device id.
   *
   * @return the device id
   */
  public String getDeviceId() {
    return deviceId;
  }

  /**
   * Gets searchable tags.
   *
   * @return the searchable tags
   */
  public List<String> getSearchableTags() {
    return searchableTags;
  }
}
