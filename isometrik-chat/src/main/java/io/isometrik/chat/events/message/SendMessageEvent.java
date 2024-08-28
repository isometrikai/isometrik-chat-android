package io.isometrik.chat.events.message;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.response.message.utils.schemas.EventForMessage;
import io.isometrik.chat.response.message.utils.schemas.MentionedUser;
import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 * The type Send message event.
 */
public class SendMessageEvent implements Serializable {

  //@SerializedName("deletedFor")
  //@Expose
  //private ArrayList<String> deletedFor;
  //@SerializedName("readBy")
  //@Expose
  //private ArrayList<String> readBy;
  //@SerializedName("deliveredTo")
  //@Expose
  //private ArrayList<String> deliveredTo;
  @SerializedName("conversationImageUrl")
  @Expose
  private String conversationImageUrl;
  @SerializedName("conversationTitle")
  @Expose
  private String conversationTitle;
  @SerializedName("privateOneToOne")
  @Expose
  private boolean privateOneToOne;
  @SerializedName("deviceId")
  @Expose
  private String deviceId;
  @SerializedName("senderProfileImageUrl")
  @Expose
  private String senderProfileImageUrl;
  @SerializedName("senderName")
  @Expose
  private String senderName;
  @SerializedName("senderIdentifier")
  @Expose
  private String senderIdentifier;

  @SerializedName("sentAt")
  @Expose
  private long sentAt;
  @SerializedName("conversationId")
  @Expose
  private String conversationId;
  @SerializedName("senderId")
  @Expose
  private String senderId;
  @SerializedName("action")
  @Expose
  private String action;
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
  @SerializedName("mentionedUsers")
  @Expose
  private ArrayList<MentionedUser> mentionedUsers;
  @SerializedName("deliveryReadEventsEnabled")
  @Expose
  private boolean deliveryReadEventsEnabled;

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
   * Gets sender id.
   *
   * @return the sender id
   */
  public String getSenderId() {
    return senderId;
  }

  /**
   * Gets sender profile image url.
   *
   * @return the sender profile image url
   */
  public String getSenderProfileImageUrl() {
    return senderProfileImageUrl;
  }

  /**
   * Gets sender name.
   *
   * @return the sender name
   */
  public String getSenderName() {
    return senderName;
  }

  /**
   * Gets sender identifier.
   *
   * @return the sender identifier
   */
  public String getSenderIdentifier() {
    return senderIdentifier;
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
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
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
   * Gets mentioned users.
   *
   * @return the mentioned users
   */
  public ArrayList<MentionedUser> getMentionedUsers() {
    return mentionedUsers;
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
   * Gets delivery read events enabled.
   *
   * @return the delivery read events enabled
   */
  public boolean getDeliveryReadEventsEnabled() {
    return deliveryReadEventsEnabled;
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
   * Gets conversation title.
   *
   * @return the conversation title
   */
  public String getConversationTitle() {
    return conversationTitle;
  }

  /**
   * Is private one to one boolean.
   *
   * @return the boolean
   */
  public boolean isPrivateOneToOne() {
    return privateOneToOne;
  }
}
