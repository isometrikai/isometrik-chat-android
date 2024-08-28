package io.isometrik.chat.response.message.utils.fetchmessages;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * The helper class to parse the details of the conversation.
 */
public class ConversationDetails {

  @SerializedName("updatedAt")
  @Expose
  private long updatedAt;

  @SerializedName("unreadMessagesCount")
  @Expose
  private int unreadMessagesCount;

  @SerializedName("privateOneToOne")
  @Expose
  private boolean privateOneToOne;

  @SerializedName("metaData")
  @Expose
  private Object metaData;

  @SerializedName("lastReadAt")
  @Expose
  private Object lastReadAt;

  @SerializedName("lastMessageDetails")
  @Expose
  private Object lastMessageDetails;

  @SerializedName("isGroup")
  @Expose
  private boolean isGroup;

  @SerializedName("customType")
  @Expose
  private String customType;

  @SerializedName("createdBy")
  @Expose
  private String createdBy;

  @SerializedName("createdAt")
  @Expose
  private long createdAt;

  @SerializedName("conversationType")
  @Expose
  private int conversationType;

  @SerializedName("conversationTitle")
  @Expose
  private String conversationTitle;
  @SerializedName("conversationImageUrl")
  @Expose
  private String conversationImageUrl;
  @SerializedName("conversationId")
  @Expose
  private String conversationId;

  @SerializedName("adminCount")
  @Expose
  private int adminCount;

  @SerializedName("members")
  @Expose
  private ArrayList<ConversationMember> conversationMembers;

  /**
   * The type Conversation member.
   */
  public static class ConversationMember {

    @SerializedName("userProfileImageUrl")
    @Expose
    private String userProfileImageUrl;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userIdentifier")
    @Expose
    private String userIdentifier;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("online")
    @Expose
    private boolean online;
    @SerializedName("isAdmin")
    @Expose
    private boolean isAdmin;
    @SerializedName("lastSeen")
    @Expose
    private long lastSeen;
    @SerializedName("metaData")
    @Expose
    private Object metaData;

    /**
     * Gets user profile image url.
     *
     * @return the user profile image url
     */
    public String getUserProfileImageUrl() {
      return userProfileImageUrl;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
      return userName;
    }

    /**
     * Gets user identifier.
     *
     * @return the user identifier
     */
    public String getUserIdentifier() {
      return userIdentifier;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
      return userId;
    }

    /**
     * Is online boolean.
     *
     * @return the boolean
     */
    public boolean isOnline() {
      return online;
    }

    /**
     * Is admin boolean.
     *
     * @return the boolean
     */
    public boolean isAdmin() {
      return isAdmin;
    }

    /**
     * Gets last seen.
     *
     * @return the last seen
     */
    public long getLastSeen() {
      return lastSeen;
    }

    /**
     * Gets meta data.
     *
     * @return the meta data
     */
    public JSONObject getMetaData() {
      try {
        return new JSONObject(new Gson().toJson(metaData));
      } catch (Exception ignore) {
        return new JSONObject();
      }
    }
  }

  @SerializedName("config")
  @Expose
  private Config config;

  /**
   * The type Config.
   */
  public static class Config {
    @SerializedName("typingEvents")
    @Expose
    private boolean typingEvents;
    @SerializedName("readEvents")
    @Expose
    private boolean readEvents;
    @SerializedName("pushNotifications")
    @Expose
    private boolean pushNotifications;

    /**
     * Is typing events boolean.
     *
     * @return the boolean
     */
    public boolean isTypingEvents() {
      return typingEvents;
    }

    /**
     * Is read events boolean.
     *
     * @return the boolean
     */
    public boolean isReadEvents() {
      return readEvents;
    }

    /**
     * Is push notifications boolean.
     *
     * @return the boolean
     */
    public boolean isPushNotifications() {
      return pushNotifications;
    }
  }

  /**
   * Gets updated at.
   *
   * @return the updated at
   */
  public long getUpdatedAt() {
    return updatedAt;
  }

  /**
   * Gets unread messages count.
   *
   * @return the unread messages count
   */
  public int getUnreadMessagesCount() {
    return unreadMessagesCount;
  }

  /**
   * Is private one to one boolean.
   *
   * @return the boolean
   */
  public boolean isPrivateOneToOne() {
    return privateOneToOne;
  }

  /**
   * Gets meta data.
   *
   * @return the meta data
   */
  public JSONObject getMetaData() {

    try {
      return new JSONObject(new Gson().toJson(metaData));
    } catch (Exception ignore) {
      return new JSONObject();
    }
  }

  /**
   * Gets last read at.
   *
   * @return the last read at
   */
  public JSONObject getLastReadAt() {

    try {
      return new JSONObject(new Gson().toJson(lastReadAt));
    } catch (Exception ignore) {
      return new JSONObject();
    }
  }

  /**
   * Gets last message details.
   *
   * @return the last message details
   */
  public JSONObject getLastMessageDetails() {

    try {
      return new JSONObject(new Gson().toJson(lastMessageDetails));
    } catch (Exception ignore) {
      return new JSONObject();
    }
  }

  /**
   * Is group boolean.
   *
   * @return the boolean
   */
  public boolean isGroup() {
    return isGroup;
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
   * Gets created by.
   *
   * @return the created by
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Gets created at.
   *
   * @return the created at
   */
  public long getCreatedAt() {
    return createdAt;
  }

  /**
   * Gets conversation type.
   *
   * @return the conversation type
   */
  public int getConversationType() {
    return conversationType;
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
   * Gets conversation id.
   *
   * @return the conversation id
   */
  public String getConversationId() {
    return conversationId;
  }

  /**
   * Gets admin count.
   *
   * @return the admin count
   */
  public int getAdminCount() {
    return adminCount;
  }

  /**
   * Gets conversation members.
   *
   * @return the conversation members
   */
  public ArrayList<ConversationMember> getConversationMembers() {
    return conversationMembers;
  }

  /**
   * Gets config.
   *
   * @return the config
   */
  public Config getConfig() {
    return config;
  }
}