package io.isometrik.chat.response.message.utils.fetchmessages;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.conversation.utils.OpponentDetails;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.response.message.utils.schemas.EventForMessage;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * The helper class to parse the details of the mentioned message.
 */
public class MentionedMessage {

  @SerializedName("messageUpdated")
  @Expose
  private Boolean messageUpdated;
  @SerializedName("conversationId")
  @Expose
  private String conversationId;
  @SerializedName("conversationTitle")
  @Expose
  private String conversationTitle;
  @SerializedName("conversationImageUrl")
  @Expose
  private String conversationImageUrl;
  @SerializedName("privateOneToOne")
  @Expose
  private Boolean privateOneToOne;
  @SerializedName("opponentDetails")
  @Expose
  private OpponentDetails opponentDetails;

  @SerializedName("sentAt")
  @Expose
  private long sentAt;

  @SerializedName("messageId")
  @Expose
  private String messageId;

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
  @SerializedName("searchableTags")
  @Expose
  private ArrayList<String> searchableTags;
  @SerializedName("deliveredToAll")
  @Expose
  private boolean deliveredToAll;
  @SerializedName("readByAll")
  @Expose
  private boolean readByAll;

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
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
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
   * Is private one to one boolean.
   *
   * @return the boolean
   */
  public Boolean isPrivateOneToOne() {
    return privateOneToOne;
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
   * Gets conversation image url.
   *
   * @return the conversation image url
   */
  public String getConversationImageUrl() {
    return conversationImageUrl;
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
   * Gets message updated.
   *
   * @return the message updated
   */
  public Boolean getMessageUpdated() {
    return messageUpdated;
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
}
