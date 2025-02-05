package io.isometrik.chat.response.conversation.utils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * The helper class to parse and expose details of the conversation.
 */
public class ConversationDetailsUtil {

  @SerializedName("messagingDisabled")
  @Expose
  private Boolean messagingDisabled;

  @SerializedName("opponentDetails")
  @Expose
  private OpponentDetails opponentDetails;

  @SerializedName("updatedAt")
  @Expose
  private long updatedAt;

  @SerializedName("privateOneToOne")
  @Expose
  private boolean privateOneToOne;

  @SerializedName("metaData")
  @Expose
  private Object metaData;

  @SerializedName("lastReadAt")
  @Expose
  private Object lastReadAt;

  @SerializedName("isGroup")
  @Expose
  private boolean isGroup;

  @SerializedName("customType")
  @Expose
  private String customType;

  @SerializedName("createdBy")
  @Expose
  private String createdBy;

  @SerializedName("createdByUserName")
  @Expose
  private String createdByUserName;

  @SerializedName("createdByUserImageUrl")
  @Expose
  private String createdByUserImageUrl;

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

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

  @SerializedName("members")
  @Expose
  private ArrayList<ConversationMember> conversationMembers;

  @SerializedName("config")
  @Expose
  private Config config;

  @SerializedName("usersOwnDetails")
  @Expose
  private UsersOwnDetails usersOwnDetails;

  @SerializedName("searchableTags")
  @Expose
  private ArrayList<String> searchableTags;

  /**
   * The type Users own details.
   */
  public static class UsersOwnDetails {

    @SerializedName("memberId")
    @Expose
    private String memberId;
    @SerializedName("isAdmin")
    @Expose
    private boolean isAdmin;

    /**
     * Gets member id.
     *
     * @return the member id
     */
    public String getMemberId() {
      return memberId;
    }

    /**
     * Is admin boolean.
     *
     * @return the boolean
     */
    public boolean isAdmin() {
      return isAdmin;
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
   * Gets members count.
   *
   * @return the members count
   */
  public int getMembersCount() {
    return membersCount;
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

  /**
   * Gets created by user name.
   *
   * @return the created by user name
   */
  public String getCreatedByUserName() {
    return createdByUserName;
  }

  /**
   * Gets created by user image url.
   *
   * @return the created by user image url
   */
  public String getCreatedByUserImageUrl() {
    return createdByUserImageUrl;
  }

  /**
   * Gets users own details.
   *
   * @return the users own details
   */
  public UsersOwnDetails getUsersOwnDetails() {
    return usersOwnDetails;
  }

  /**
   * Gets opponent details.
   *
   * @return the opponent details
   */
  public OpponentDetails getOpponentDetails() {
    return opponentDetails;
  }

  /**
   * Gets searchable tags.
   *
   * @return the searchable tags
   */
  public ArrayList<String> getSearchableTags() {
    return searchableTags;
  }

  /**
   * Gets messaging disabled.
   *
   * @return the messaging disabled
   */
  public Boolean getMessagingDisabled() {
    return messagingDisabled;
  }

  public void setConversationId(String conversationId) {
    this.conversationId = conversationId;
  }
}
