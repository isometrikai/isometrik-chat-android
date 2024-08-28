package io.isometrik.chat.builder.conversation;

import java.util.List;
import org.json.JSONObject;

/**
 * The query builder for creating conversation request.
 */
public class CreateConversationQuery {

  private final String conversationTitle;
  private final String conversationImageUrl;
  private final String customType;
  private final Boolean isGroup;
  private final List<String> members;
  private final Integer conversationType;
  private final JSONObject metaData;
  private final String userToken;
  private final Boolean readEvents;
  private final Boolean typingEvents;
  private final Boolean pushNotifications;
  private final List<String> searchableTags;

  private CreateConversationQuery(CreateConversationQuery.Builder builder) {
    this.conversationTitle = builder.conversationTitle;
    this.conversationImageUrl = builder.conversationImageUrl;
    this.customType = builder.customType;
    this.isGroup = builder.isGroup;
    this.members = builder.members;
    this.conversationType = builder.conversationType;
    this.metaData = builder.metaData;
    this.userToken = builder.userToken;
    this.readEvents = builder.readEvents;
    this.typingEvents = builder.typingEvents;
    this.pushNotifications = builder.pushNotifications;
    this.searchableTags = builder.searchableTags;
  }

  /**
   * The builder class for building create conversation query.
   */
  public static class Builder {
    private String conversationTitle;
    private String conversationImageUrl;
    private String customType;
    private Boolean isGroup;
    private List<String> members;
    private Integer conversationType;
    private JSONObject metaData;
    private String userToken;
    private Boolean readEvents;
    private Boolean typingEvents;
    private Boolean pushNotifications;
    private List<String> searchableTags;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets conversation title.
     *
     * @param conversationTitle the conversation title
     * @return the conversation title
     */
    public CreateConversationQuery.Builder setConversationTitle(String conversationTitle) {
      this.conversationTitle = conversationTitle;
      return this;
    }

    /**
     * Sets conversation image url.
     *
     * @param conversationImageUrl the conversation image url
     * @return the conversation image url
     */
    public CreateConversationQuery.Builder setConversationImageUrl(String conversationImageUrl) {
      this.conversationImageUrl = conversationImageUrl;
      return this;
    }

    /**
     * Sets custom type.
     *
     * @param customType the custom type
     * @return the custom type
     */
    public CreateConversationQuery.Builder setCustomType(String customType) {
      this.customType = customType;
      return this;
    }

    /**
     * Sets group.
     *
     * @param group the group
     * @return the group
     */
    public CreateConversationQuery.Builder setGroup(Boolean group) {
      isGroup = group;
      return this;
    }

    /**
     * Sets members.
     *
     * @param members the members
     * @return the members
     */
    public CreateConversationQuery.Builder setMembers(List<String> members) {
      this.members = members;
      return this;
    }

    /**
     * Sets conversation type.
     *
     * @param conversationType the conversation type
     * @return the conversation type
     */
    public CreateConversationQuery.Builder setConversationType(Integer conversationType) {
      this.conversationType = conversationType;
      return this;
    }

    /**
     * Sets meta data.
     *
     * @param metaData the meta data
     * @return the meta data
     */
    public CreateConversationQuery.Builder setMetaData(JSONObject metaData) {
      this.metaData = metaData;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public CreateConversationQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Sets read events.
     *
     * @param readEvents the read events
     * @return the read events
     */
    public CreateConversationQuery.Builder setReadEvents(Boolean readEvents) {
      this.readEvents = readEvents;
      return this;
    }

    /**
     * Sets typing events.
     *
     * @param typingEvents the typing events
     * @return the typing events
     */
    public CreateConversationQuery.Builder setTypingEvents(Boolean typingEvents) {
      this.typingEvents = typingEvents;
      return this;
    }

    /**
     * Sets push notifications.
     *
     * @param pushNotifications the push notifications
     * @return the push notifications
     */
    public CreateConversationQuery.Builder setPushNotifications(Boolean pushNotifications) {
      this.pushNotifications = pushNotifications;
      return this;
    }

    /**
     * Sets searchable tags.
     *
     * @param searchableTags the searchable tags
     * @return the searchable tags
     */
    public CreateConversationQuery.Builder setSearchableTags(List<String> searchableTags) {
      this.searchableTags = searchableTags;
      return this;
    }

    /**
     * Build create conversation query.
     *
     * @return the create conversation query
     */
    public CreateConversationQuery build() {
      return new CreateConversationQuery(this);
    }
  }

  /**
   * Gets conversation title.
   *
   * @return the conversation title
   */
  public String getConversationTitle() {
    return conversationTitle;
  }

  /**
   * Gets conversation image url.
   *
   * @return the conversation image url
   */
  public String getConversationImageUrl() {
    return conversationImageUrl;
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
   * Gets group.
   *
   * @return the group
   */
  public Boolean getGroup() {
    return isGroup;
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
   * Gets conversation type.
   *
   * @return the conversation type
   */
  public Integer getConversationType() {
    return conversationType;
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
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  /**
   * Gets read events.
   *
   * @return the read events
   */
  public Boolean getReadEvents() {
    return readEvents;
  }

  /**
   * Gets typing events.
   *
   * @return the typing events
   */
  public Boolean getTypingEvents() {
    return typingEvents;
  }

  /**
   * Gets push notifications.
   *
   * @return the push notifications
   */
  public Boolean getPushNotifications() {
    return pushNotifications;
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
