package io.isometrik.chat.builder.message;

import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.response.message.utils.schemas.EventForMessage;
import io.isometrik.chat.response.message.utils.schemas.MentionedUser;
import java.util.List;
import org.json.JSONObject;

/**
 * The query builder for sending message request.
 */
public class SendMessageQuery {

  private final Integer messageType;
  private final Boolean showInConversation;
  private final String body;
  private final String conversationId;
  private final Boolean encrypted;
  private final String userToken;
  private final String deviceId;
  private final String notificationTitle;
  private final String notificationBody;

  private final EventForMessage eventForMessage;
  private final String parentMessageId;
  private final String customType;
  private final List<MentionedUser> mentionedUsers;
  private final JSONObject metaData;
  private final List<Attachment> attachments;
  private final List<String> searchableTags;

  private SendMessageQuery(SendMessageQuery.Builder builder) {
    this.messageType = builder.messageType;
    this.showInConversation = builder.showInConversation;
    this.body = builder.body;
    this.conversationId = builder.conversationId;
    this.encrypted = builder.encrypted;
    this.userToken = builder.userToken;
    this.deviceId = builder.deviceId;
    this.notificationTitle = builder.notificationTitle;
    this.notificationBody = builder.notificationBody;

    this.eventForMessage = builder.eventForMessage;
    this.parentMessageId = builder.parentMessageId;
    this.customType = builder.customType;
    this.mentionedUsers = builder.mentionedUsers;
    this.metaData = builder.metaData;
    this.attachments = builder.attachments;
    this.searchableTags = builder.searchableTags;
  }

  /**
   * The builder class for building send message query.
   */
  public static class Builder {
    private Integer messageType;
    private Boolean showInConversation;
    private String body;
    private String conversationId;
    private Boolean encrypted;
    private String userToken;
    private String deviceId;
    private String notificationTitle;
    private String notificationBody;

    private EventForMessage eventForMessage;
    private String parentMessageId;
    private String customType;
    private List<MentionedUser> mentionedUsers;
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
    public SendMessageQuery.Builder setMessageType(Integer messageType) {
      this.messageType = messageType;
      return this;
    }

    /**
     * Sets show in conversation.
     *
     * @param showInConversation the show in conversation
     * @return the show in conversation
     */
    public SendMessageQuery.Builder setShowInConversation(Boolean showInConversation) {
      this.showInConversation = showInConversation;
      return this;
    }

    /**
     * Sets body.
     *
     * @param body the body
     * @return the body
     */
    public SendMessageQuery.Builder setBody(String body) {
      this.body = body;
      return this;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public SendMessageQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets encrypted.
     *
     * @param encrypted the encrypted
     * @return the encrypted
     */
    public SendMessageQuery.Builder setEncrypted(Boolean encrypted) {
      this.encrypted = encrypted;
      return this;
    }

    /**
     * Sets event for message.
     *
     * @param eventForMessage the event for message
     * @return the event for message
     */
    public SendMessageQuery.Builder setEventForMessage(EventForMessage eventForMessage) {
      this.eventForMessage = eventForMessage;
      return this;
    }

    /**
     * Sets parent message id.
     *
     * @param parentMessageId the parent message id
     * @return the parent message id
     */
    public SendMessageQuery.Builder setParentMessageId(String parentMessageId) {
      this.parentMessageId = parentMessageId;
      return this;
    }

    /**
     * Sets custom type.
     *
     * @param customType the custom type
     * @return the custom type
     */
    public SendMessageQuery.Builder setCustomType(String customType) {
      this.customType = customType;
      return this;
    }

    /**
     * Sets mentioned users.
     *
     * @param mentionedUsers the mentioned users
     * @return the mentioned users
     */
    public SendMessageQuery.Builder setMentionedUsers(List<MentionedUser> mentionedUsers) {
      this.mentionedUsers = mentionedUsers;
      return this;
    }

    /**
     * Sets meta data.
     *
     * @param metaData the meta data
     * @return the meta data
     */
    public SendMessageQuery.Builder setMetaData(JSONObject metaData) {
      this.metaData = metaData;
      return this;
    }

    /**
     * Sets attachments.
     *
     * @param attachments the attachments
     * @return the attachments
     */
    public SendMessageQuery.Builder setAttachments(List<Attachment> attachments) {
      this.attachments = attachments;
      return this;
    }

    /**
     * Sets device id.
     *
     * @param deviceId the device id
     * @return the device id
     */
    public SendMessageQuery.Builder setDeviceId(String deviceId) {
      this.deviceId = deviceId;
      return this;
    }

    /**
     * Sets searchable tags.
     *
     * @param searchableTags the searchable tags
     * @return the searchable tags
     */
    public SendMessageQuery.Builder setSearchableTags(List<String> searchableTags) {
      this.searchableTags = searchableTags;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public SendMessageQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets notification Title.
     *
     * @param notificationTitle
     * @return notificationTitle
     */
    public SendMessageQuery.Builder setNotificationTitle(String notificationTitle) {
      this.notificationTitle = notificationTitle;
      return this;
    }

    /**
     * Sets notification Title.
     *
     * @param notificationBody
     * @return notificationBody
     */
    public SendMessageQuery.Builder setNotificationBody(String notificationBody) {
      this.notificationBody = notificationBody;
      return this;
    }



    /**
     * Build send message query.
     *
     * @return the send message query
     */
    public SendMessageQuery build() {
      return new SendMessageQuery(this);
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
   * Gets conversation id.
   *
   * @return the conversation id
   */
  public String getConversationId() {
    return conversationId;
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
   * Gets parent message id.
   *
   * @return the parent message id
   */
  public String getParentMessageId() {
    return parentMessageId;
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
   * Gets mentioned users.
   *
   * @return the mentioned users
   */
  public List<MentionedUser> getMentionedUsers() {
    return mentionedUsers;
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

    /**
     * Gets notificationTitle.
     *
     * @return the notificationTitle
     */

    public String getNotificationTitle() {
        return notificationTitle;
    }

    /**
     * Gets notificationBody.
     *
     * @return the notificationBody
     */
    public String getNotificationBody() {
        return notificationBody;
    }
}
