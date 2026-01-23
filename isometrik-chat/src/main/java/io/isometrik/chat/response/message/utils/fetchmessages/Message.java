package io.isometrik.chat.response.message.utils.fetchmessages;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.response.message.utils.schemas.EventForMessage;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * The helper class to parse the details of the message.
 */
public class Message {
  @SerializedName("messageUpdated")
  @Expose
  private Boolean messageUpdated;
  @SerializedName("conversationDetails")
  @Expose
  private ConversationDetails conversationDetails;
  @SerializedName("sentAt")
  @Expose
  private long sentAt;
  @SerializedName("conversationId")
  @Expose
  private String conversationId;
  @SerializedName("userId")
  @Expose
  private String userId;
  @SerializedName("action")
  @Expose
  private String action;
  @SerializedName("userName")
  @Expose
  private String userName;
  @SerializedName("userIdentifier")
  @Expose
  private String userIdentifier;
  @SerializedName("userProfileImageUrl")
  @Expose
  private String userProfileImageUrl;
  @SerializedName("conversationStatusMessage")
  @Expose
  private Boolean conversationStatusMessage;
  @SerializedName("messageId")
  @Expose
  private String messageId;
  @SerializedName("reactionType")
  @Expose
  private String reactionType;
  @SerializedName("numberOfMessages")
  @Expose
  private Integer numberOfMessages;
  @SerializedName("lastReadAt")
  @Expose
  private Long lastReadAt;
  @SerializedName("messageIds")
  @Expose
  private ArrayList<String> messageIds;
  @SerializedName("conversationTitle")
  @Expose
  private String conversationTitle;
  @SerializedName("conversationImageUrl")
  @Expose
  private String conversationImageUrl;
  @SerializedName("memberId")
  @Expose
  private String memberId;
  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;
  @SerializedName("memberName")
  @Expose
  private String memberName;
  @SerializedName("memberIdentifier")
  @Expose
  private String memberIdentifier;
  @SerializedName("memberProfileImageUrl")
  @Expose
  private String memberProfileImageUrl;
  @SerializedName("initiatorName")
  @Expose
  private String initiatorName;
  @SerializedName("initiatorIdentifier")
  @Expose
  private String initiatorIdentifier;
  @SerializedName("initiatorProfileImageUrl")
  @Expose
  private String initiatorProfileImageUrl;
  @SerializedName("config")
  @Expose
  private Config config;
  @SerializedName("members")
  @Expose
  private ArrayList<AddRemoveMember> addRemoveMembers;
  @SerializedName("updatedAt")
  @Expose
  private Long updatedAt;
  @SerializedName("showInConversation")
  @Expose
  private Boolean showInConversation;
  @SerializedName("reactions")
  @Expose
  private Object reactions;
  @SerializedName("metaData")
  @Expose
  private Object metaData;
  @SerializedName("parentMessageId")
  @Expose
  private String parentMessageId;
  @SerializedName("messageType")
  @Expose
  private Integer messageType;
  @SerializedName("encrypted")
  @Expose
  private Boolean encrypted;
  @SerializedName("customType")
  @Expose
  private String customType;
  @SerializedName("body")
  @Expose
  private String body;
  @SerializedName("attachments")
  @Expose
  private ArrayList<Attachment> attachments;
  @SerializedName("events")
  @Expose
  private EventForMessage events;
  @SerializedName("readBy")
  @Expose
  private ArrayList<ReadByDeliveredToUser> readByUsers;
  @SerializedName("deliveredTo")
  @Expose
  private ArrayList<ReadByDeliveredToUser> deliveredToUsers;
  @SerializedName("senderInfo")
  @Expose
  private SenderInfo senderInfo;
  @SerializedName("mentionedUsers")
  @Expose
  private ArrayList<MentionedUser> mentionedUsers;
  @SerializedName("deliveryReadEventsEnabled")
  @Expose
  private Boolean deliveryReadEventsEnabled;
  @SerializedName("details")
  @Expose
  private Details details;
  @SerializedName("searchableTags")
  @Expose
  private ArrayList<String> searchableTags;
  @SerializedName("deliveredToAll")
  @Expose
  private boolean deliveredToAll;
  @SerializedName("readByAll")
  @Expose
  private boolean readByAll;
  @SerializedName("notification")
  @Expose
  private Boolean notification;
  @SerializedName("opponentName")
  @Expose
  private String opponentName;
  @SerializedName("opponentIdentifier")
  @Expose
  private String opponentIdentifier;
  @SerializedName("opponentProfileImageUrl")
  @Expose
  private String opponentProfileImageUrl;

  /**
   * Gets notification.
   *
   * @return the notification
   */
  public Boolean getNotification() {
    return notification;
  }

  /**
   * Gets sent at.
   *
   * @return the sent at
   */
  public long getSentAt() {
    return sentAt;
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
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets action.
   *
   * @return the action
   */
  public String getAction() {
    return action;
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
   * Gets user profile image url.
   *
   * @return the user profile image url
   */
  public String getUserProfileImageUrl() {
    return userProfileImageUrl;
  }

  /**
   * Gets conversation status message.
   *
   * @return the conversation status message
   */
  public Boolean getConversationStatusMessage() {
    return conversationStatusMessage;
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets reaction type.
   *
   * @return the reaction type
   */
  public String getReactionType() {
    return reactionType;
  }

  /**
   * Gets number of messages.
   *
   * @return the number of messages
   */
  public Integer getNumberOfMessages() {
    return numberOfMessages;
  }

  /**
   * Gets last read at.
   *
   * @return the last read at
   */
  public Long getLastReadAt() {
    return lastReadAt;
  }

  /**
   * Gets message ids.
   *
   * @return the message ids
   */
  public ArrayList<String> getMessageIds() {
    return messageIds;
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
   * Gets member id.
   *
   * @return the member id
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets initiator id.
   *
   * @return the initiator id
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets member name.
   *
   * @return the member name
   */
  public String getMemberName() {
    return memberName;
  }

  /**
   * Gets member identifier.
   *
   * @return the member identifier
   */
  public String getMemberIdentifier() {
    return memberIdentifier;
  }

  /**
   * Gets member profile image url.
   *
   * @return the member profile image url
   */
  public String getMemberProfileImageUrl() {
    return memberProfileImageUrl;
  }

  /**
   * Gets initiator name.
   *
   * @return the initiator name
   */
  public String getInitiatorName() {
    return initiatorName;
  }

  /**
   * Gets initiator identifier.
   *
   * @return the initiator identifier
   */
  public String getInitiatorIdentifier() {
    return initiatorIdentifier;
  }

  /**
   * Gets initiator profile image url.
   *
   * @return the initiator profile image url
   */
  public String getInitiatorProfileImageUrl() {
    return initiatorProfileImageUrl;
  }

  /**
   * Gets add remove members.
   *
   * @return the add remove members
   */
  public ArrayList<AddRemoveMember> getAddRemoveMembers() {
    return addRemoveMembers;
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
   * Gets updated at.
   *
   * @return the updated at
   */
  public Long getUpdatedAt() {
    return updatedAt;
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
   * Gets reactions.
   *
   * @return the reactions
   */
  public JSONObject getReactions() {

    //return reactions;
    try {
      return new JSONObject(new Gson().toJson(reactions));
    } catch (Exception ignore) {
      return new JSONObject();
    }
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
   * Gets parent message id.
   *
   * @return the parent message id
   */
  public String getParentMessageId() {
    return parentMessageId;
  }

  /**
   * Gets message type.
   *
   * @return the message type
   */
  public Integer getMessageType() {
    if(messageType == null){
      return 0;
    }
    return messageType;
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
   * Gets attachments.
   *
   * @return the attachments
   */
  public ArrayList<Attachment> getAttachments() {
    return attachments;
  }

  /**
   * Gets events.
   *
   * @return the events
   */
  public EventForMessage getEvents() {
    return events;
  }

  /**
   * Gets read by users.
   *
   * @return the read by users
   */
  public ArrayList<ReadByDeliveredToUser> getReadByUsers() {
    return readByUsers;
  }

  /**
   * Gets delivered to users.
   *
   * @return the delivered to users
   */
  public ArrayList<ReadByDeliveredToUser> getDeliveredToUsers() {
    return deliveredToUsers;
  }

  /**
   * Gets sender info.
   *
   * @return the sender info
   */
  public SenderInfo getSenderInfo() {
    return senderInfo;
  }

  /**
   * Gets mentioned users.
   *
   * @return the mentioned users
   */
  public ArrayList<MentionedUser> getMentionedUsers() {
    return mentionedUsers;
  }

  /**
   * Gets conversation details.
   *
   * @return the conversation details
   */
  public ConversationDetails getConversationDetails() {
    return conversationDetails;
  }

  /**
   * Gets delivery read events enabled.
   *
   * @return the delivery read events enabled
   */
  public Boolean getDeliveryReadEventsEnabled() {
    return deliveryReadEventsEnabled;
  }

  /**
   * Gets details.
   *
   * @return the details
   */
  public Details getDetails() {
    return details;
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
   * Is delivered to all boolean.
   *
   * @return the boolean
   */
  public boolean isDeliveredToAll() {
    return deliveredToAll;
  }

  /**
   * Is read by all boolean.
   *
   * @return the boolean
   */
  public boolean isReadByAll() {
    return readByAll;
  }

  /**
   * Gets opponent name.
   *
   * @return the opponent name
   */
  public String getOpponentName() {
    return opponentName;
  }

  /**
   * Gets opponent identifier.
   *
   * @return the opponent identifier
   */
  public String getOpponentIdentifier() {
    return opponentIdentifier;
  }

  /**
   * Gets opponent profile image url.
   *
   * @return the opponent profile image url
   */
  public String getOpponentProfileImageUrl() {
    return opponentProfileImageUrl;
  }

  /**
   * Gets message updated.
   *
   * @return the message updated
   */
  public Boolean getMessageUpdated() {
    return messageUpdated;
  }
}