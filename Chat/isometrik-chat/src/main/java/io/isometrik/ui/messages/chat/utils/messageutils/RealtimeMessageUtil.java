package io.isometrik.ui.messages.chat.utils.messageutils;

import io.isometrik.chat.events.conversation.cleanup.ClearConversationEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationDetailsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationImageEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationSettingsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationTitleEvent;
import io.isometrik.chat.events.membershipcontrol.AddAdminEvent;
import io.isometrik.chat.events.membershipcontrol.AddMembersEvent;
import io.isometrik.chat.events.membershipcontrol.JoinConversationEvent;
import io.isometrik.chat.events.membershipcontrol.LeaveConversationEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverJoinEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverLeaveEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveAdminEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveMembersEvent;
import io.isometrik.chat.events.message.SendMessageEvent;
import io.isometrik.chat.events.message.UpdateMessageDetailsEvent;
import io.isometrik.chat.events.message.user.block.BlockUserInConversationEvent;
import io.isometrik.chat.events.message.user.block.UnblockUserInConversationEvent;
import io.isometrik.chat.response.message.utils.fetchmessages.Config;
import io.isometrik.chat.response.message.utils.fetchmessages.Details;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.chat.utils.enums.MessageTypesForUI;
import io.isometrik.ui.messages.tag.TaggedUserCallback;
import io.isometrik.chat.utils.TagUserUtil;
import io.isometrik.chat.utils.FileUtils;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to parse the realtime message or conversation action messages.
 */
public class RealtimeMessageUtil {
  private static String conversationActionMessage;
  private static MessagesModel messageModel;

  /**
   * Parse clear conversation event messages model.
   *
   * @param clearConversationEvent the clear conversation event
   * @return the messages model
   */
  public static MessagesModel parseClearConversationEvent(
      ClearConversationEvent clearConversationEvent) {
    conversationActionMessage =
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_cleared_conversation);

    messageModel =
        new MessagesModel(conversationActionMessage, null, clearConversationEvent.getSentAt(),
            false, MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse update conversation image event messages model.
   *
   * @param updateConversationImageEvent the update conversation image event
   * @return the messages model
   */
  public static MessagesModel parseUpdateConversationImageEvent(
      UpdateConversationImageEvent updateConversationImageEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_conversation_image,
            updateConversationImageEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, updateConversationImageEvent.getSentAt(),
            false, MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse update conversation settings event messages model.
   *
   * @param updateConversationSettingsEvent the update conversation settings event
   * @return the messages model
   */
  public static MessagesModel parseUpdateConversationSettingsEvent(
      UpdateConversationSettingsEvent updateConversationSettingsEvent) {

    Config config = updateConversationSettingsEvent.getConfig();

    String settingsUpdated = "";
    if (config.getTypingEvents() != null) {
      settingsUpdated = settingsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_settings_typing);
    }
    if (config.getReadEvents() != null) {
      settingsUpdated = settingsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_settings_read_delivery_events);
    }
    if (config.getPushNotifications() != null) {
      settingsUpdated = settingsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_settings_notifications);
    }

    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_settings, updateConversationSettingsEvent.getUserName(),
            settingsUpdated.substring(2));
    messageModel = new MessagesModel(conversationActionMessage, null,

        updateConversationSettingsEvent.getSentAt(), false,
        MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse update conversation details event messages model.
   *
   * @param updateConversationDetailsEvent the update conversation details event
   * @return the messages model
   */
  public static MessagesModel parseUpdateConversationDetailsEvent(
      UpdateConversationDetailsEvent updateConversationDetailsEvent) {

    Details details = updateConversationDetailsEvent.getDetails();

    String detailsUpdated = "";
    if (details.getCustomType() != null) {
      detailsUpdated = detailsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_custom_type);
    }
    if (details.getMetadata() != null && details.getMetadata().length() > 0) {
      detailsUpdated = detailsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_metadata);
    }
    if (details.getSearchableTags() != null) {
      detailsUpdated = detailsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_searchable_tags);
    }

    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_conversation_details,
            updateConversationDetailsEvent.getUserName(), detailsUpdated.substring(2));
    messageModel = new MessagesModel(conversationActionMessage, null,

        updateConversationDetailsEvent.getSentAt(), false,
        MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse update message details event messages model.
   *
   * @param updateMessageDetailsEvent the update message details event
   * @return the messages model
   */
  public static MessagesModel parseUpdateMessageDetailsEvent(
      UpdateMessageDetailsEvent updateMessageDetailsEvent) {

    Details details = updateMessageDetailsEvent.getDetails();

    String detailsUpdated = "";
    if (details.getCustomType() != null) {
      detailsUpdated = detailsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_custom_type);
    }
    if (details.getMetadata() != null && details.getMetadata().length() > 0) {
      detailsUpdated = detailsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_metadata);
    }
    if (details.getSearchableTags() != null) {
      detailsUpdated = detailsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_searchable_tags);
    }
    if (details.getBody() != null) {
      detailsUpdated = detailsUpdated + ", " + IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_body);
    }

    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_message_details, updateMessageDetailsEvent.getUserName(),
            detailsUpdated.substring(2));
    messageModel = new MessagesModel(conversationActionMessage, null,

        updateMessageDetailsEvent.getSentAt(), false, MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse update conversation title event messages model.
   *
   * @param updateConversationTitleEvent the update conversation title event
   * @return the messages model
   */
  public static MessagesModel parseUpdateConversationTitleEvent(
      UpdateConversationTitleEvent updateConversationTitleEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_conversation_title,
            updateConversationTitleEvent.getUserName(),
            updateConversationTitleEvent.getConversationTitle());
    messageModel =
        new MessagesModel(conversationActionMessage, null, updateConversationTitleEvent.getSentAt(),
            false, MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse add admin event messages model.
   *
   * @param addAdminEvent the add admin event
   * @return the messages model
   */
  public static MessagesModel parseAddAdminEvent(AddAdminEvent addAdminEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_made_admin, addAdminEvent.getMemberName(),
            addAdminEvent.getInitiatorName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, addAdminEvent.getSentAt(), false,
            MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse add members event messages model.
   *
   * @param addMembersEvent the add members event
   * @return the messages model
   */
  public static MessagesModel parseAddMembersEvent(AddMembersEvent addMembersEvent) {
    StringBuilder membersAdded = new StringBuilder();
    List<AddMembersEvent.ConversationMember> members = addMembersEvent.getMembers();
int size = members.size();
    for (int j = 0; j < size; j++) {
      membersAdded.append(", ").append(members.get(j).getMemberName());
    }
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_members_added, addMembersEvent.getUserName(),
            membersAdded.substring(2));
    messageModel =
        new MessagesModel(conversationActionMessage, null, addMembersEvent.getSentAt(), false,
            MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse join conversation event messages model.
   *
   * @param joinConversationEvent the join conversation event
   * @return the messages model
   */
  public static MessagesModel parseJoinConversationEvent(
      JoinConversationEvent joinConversationEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_member_joined_public, joinConversationEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, joinConversationEvent.getSentAt(), false,
            MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse leave conversation event messages model.
   *
   * @param leaveConversationEvent the leave conversation event
   * @return the messages model
   */
  public static MessagesModel parseLeaveConversationEvent(
      LeaveConversationEvent leaveConversationEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_member_left, leaveConversationEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, leaveConversationEvent.getSentAt(),
            false, MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse remove admin event messages model.
   *
   * @param removeAdminEvent the remove admin event
   * @return the messages model
   */
  public static MessagesModel parseRemoveAdminEvent(RemoveAdminEvent removeAdminEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_removed_admin, removeAdminEvent.getMemberName(),
            removeAdminEvent.getInitiatorName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, removeAdminEvent.getSentAt(), false,
            MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse remove members event messages model.
   *
   * @param removeMembersEvent the remove members event
   * @return the messages model
   */
  public static MessagesModel parseRemoveMembersEvent(RemoveMembersEvent removeMembersEvent) {
    StringBuilder membersRemoved = new StringBuilder();
    List<RemoveMembersEvent.ConversationMember> members = removeMembersEvent.getMembers();
int size= members.size();
    for (int j = 0; j < size; j++) {
      membersRemoved.append(", ").append(members.get(j).getMemberName());
    }

    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_members_removed, removeMembersEvent.getUserName(),
            membersRemoved.substring(2));
    messageModel =
        new MessagesModel(conversationActionMessage, null, removeMembersEvent.getSentAt(), false,
            MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse user block event messages model.
   *
   * @param blockUserInConversationEvent the block user in conversation event
   * @return the messages model
   */
  public static MessagesModel parseUserBlockEvent(
      BlockUserInConversationEvent blockUserInConversationEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_blocked_user, blockUserInConversationEvent.getOpponentName(),
            blockUserInConversationEvent.getInitiatorName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, blockUserInConversationEvent.getSentAt(),
            false, MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse user unblock event messages model.
   *
   * @param unblockUserInConversationEvent the unblock user in conversation event
   * @return the messages model
   */
  public static MessagesModel parseUserUnblockEvent(
      UnblockUserInConversationEvent unblockUserInConversationEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_unblocked_user, unblockUserInConversationEvent.getOpponentName(),
            unblockUserInConversationEvent.getInitiatorName());
    messageModel = new MessagesModel(conversationActionMessage, null,
        unblockUserInConversationEvent.getSentAt(), false,
        MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse join as observer event messages model.
   *
   * @param observerJoinEvent the observer join event
   * @return the messages model
   */
  public static MessagesModel parseJoinAsObserverEvent(ObserverJoinEvent observerJoinEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_member_observer_joined, observerJoinEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, observerJoinEvent.getSentAt(), false,
            MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }

  /**
   * Parse leave as observer event messages model.
   *
   * @param observerLeaveEvent the observer leave event
   * @return the messages model
   */
  public static MessagesModel parseLeaveAsObserverEvent(ObserverLeaveEvent observerLeaveEvent) {
    conversationActionMessage = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_member_observer_left, observerLeaveEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, observerLeaveEvent.getSentAt(), false,
            MessageTypesForUI.ConversationActionMessage);

    return messageModel;
  }
  //public static MessagesModel parseRemoveMessagesForEveryoneEvent(
  //    RemoveMessagesForEveryoneEvent removeMessagesForEveryoneEvent) {
  //  conversationActionMessage = IsometrikUiSdk.getInstance()
  //      .getContext()
  //      .getString(R.string.ism_message_deleted_for_all,
  //          removeMessagesForEveryoneEvent.getUserName());
  //  messageModel = new MessagesModel(conversationActionMessage, null,
  //      removeMessagesForEveryoneEvent.getSentAt(), false,
  //      MessageTypesForUI.ConversationActionMessage);
  //
  //  return messageModel;
  //}
  //
  //public static MessagesModel parseRemoveMessagesForSelfEvent(
  //    RemoveMessagesForSelfEvent removeMessagesForSelfEvent) {
  //  conversationActionMessage = IsometrikUiSdk.getInstance()
  //      .getContext()
  //      .getString(R.string.ism_message_deleted_locally, removeMessagesForSelfEvent.getUserName());
  //  messageModel =
  //      new MessagesModel(conversationActionMessage, null, removeMessagesForSelfEvent.getSentAt(),
  //          false, MessageTypesForUI.ConversationActionMessage);
  //
  //  return messageModel;
  //}

  /**
   * Parse send message event messages model.
   *
   * @param sendMessageEvent the send message event
   * @param taggedUserCallback the tagged user callback
   * @return the messages model
   */
  public static MessagesModel parseSendMessageEvent(SendMessageEvent sendMessageEvent,
      TaggedUserCallback taggedUserCallback) {
    messageModel = null;
    if (sendMessageEvent.getCustomType() != null) {
      boolean selfMessage = sendMessageEvent.getSenderId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId());

      switch (sendMessageEvent.getCustomType()) {

        case "AttachmentMessage:Text":

        case "AttachmentMessage:Reply": {
          boolean isQuoted = sendMessageEvent.getParentMessageId() != null;
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.TextSent : MessageTypesForUI.TextReceived,
              selfMessage, sendMessageEvent.getSentAt(), isQuoted,
              TagUserUtil.parseMentionedUsers(sendMessageEvent.getBody(),
                  sendMessageEvent.getMentionedUsers(), taggedUserCallback),
              sendMessageEvent.getSenderName(), sendMessageEvent.getSenderProfileImageUrl(), null,
              true, null, (sendMessageEvent.getParentMessageId() == null) ? null
              : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                  sendMessageEvent.getMetaData())), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), false, false, sendMessageEvent.getConversationId(),
              false);

          break;
        }
        case "AttachmentMessage:Image": {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);

          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.PhotoSent : MessageTypesForUI.PhotoReceived,
              selfMessage, sendMessageEvent.getSentAt(),
              sendMessageEvent.getParentMessageId() != null,
              FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
              attachment.getThumbnailUrl(), attachment.getMediaUrl(), attachment.getMimeType(),
              attachment.getExtension(), sendMessageEvent.getSenderName(),
              sendMessageEvent.getSenderProfileImageUrl(), null, true, null,
              (sendMessageEvent.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                      sendMessageEvent.getMetaData())), attachment.getSize(), attachment.getName(),
              attachment.getMediaId(), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), null, false, false,
              sendMessageEvent.getConversationId(), false);

          break;
        }
        case "AttachmentMessage:Video": {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);

          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.VideoSent : MessageTypesForUI.VideoReceived,
              selfMessage, sendMessageEvent.getSentAt(),
              sendMessageEvent.getParentMessageId() != null,
              FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
              attachment.getThumbnailUrl(), attachment.getMediaUrl(), attachment.getMimeType(),
              attachment.getExtension(), sendMessageEvent.getSenderName(),
              sendMessageEvent.getSenderProfileImageUrl(), null, true, null,
              (sendMessageEvent.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                      sendMessageEvent.getMetaData())), attachment.getSize(), attachment.getName(),
              attachment.getMediaId(), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), null, false, false,
              sendMessageEvent.getConversationId(), false);

          break;
        }
        case "AttachmentMessage:Audio": {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.AudioSent : MessageTypesForUI.AudioReceived,
              selfMessage, sendMessageEvent.getSentAt(),
              sendMessageEvent.getParentMessageId() != null,
              FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
              attachment.getMediaUrl(), attachment.getName(), attachment.getMimeType(),
              attachment.getExtension(), sendMessageEvent.getSenderName(),
              sendMessageEvent.getSenderProfileImageUrl(), null, true, true, null,
              (sendMessageEvent.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                      sendMessageEvent.getMetaData())), attachment.getSize(),
              attachment.getMediaId(), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), false, false, sendMessageEvent.getConversationId(),
              false);

          break;
        }
        case "AttachmentMessage:File": {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.FileSent : MessageTypesForUI.FileReceived,
              selfMessage, sendMessageEvent.getSentAt(),
              sendMessageEvent.getParentMessageId() != null,
              FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
              attachment.getMediaUrl(), attachment.getName(), attachment.getMimeType(),
              attachment.getExtension(), sendMessageEvent.getSenderName(),
              sendMessageEvent.getSenderProfileImageUrl(), null, false, true, null,
              (sendMessageEvent.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                      sendMessageEvent.getMetaData())), attachment.getSize(),
              attachment.getMediaId(), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), false, false, sendMessageEvent.getConversationId(),
              false);
          break;
        }
        case "AttachmentMessage:Location": {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);

          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.LocationSent : MessageTypesForUI.LocationReceived,
              selfMessage, sendMessageEvent.getSentAt(),
              sendMessageEvent.getParentMessageId() != null,
              String.valueOf(attachment.getLatitude()), attachment.getTitle(),
              String.valueOf(attachment.getLongitude()), attachment.getAddress(),
              sendMessageEvent.getSenderName(), sendMessageEvent.getSenderProfileImageUrl(), null,
              true, null, (sendMessageEvent.getParentMessageId() == null) ? null
              : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                  sendMessageEvent.getMetaData())), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), false, false, sendMessageEvent.getConversationId(),
              false);
          break;
        }
        case "AttachmentMessage:Sticker": {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.StickerSent : MessageTypesForUI.StickerReceived,
              selfMessage, sendMessageEvent.getSentAt(),
              sendMessageEvent.getParentMessageId() != null, attachment.getStillUrl(),
              attachment.getMediaUrl(), sendMessageEvent.getSenderName(),
              sendMessageEvent.getSenderProfileImageUrl(), null, true, true, null,
              (sendMessageEvent.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                      sendMessageEvent.getMetaData())), attachment.getName(),
              attachment.getMediaId(), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), false, false, sendMessageEvent.getConversationId(),
              false);

          break;
        }
        case "AttachmentMessage:Gif": {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.GifSent : MessageTypesForUI.GifReceived, selfMessage,
              sendMessageEvent.getSentAt(), sendMessageEvent.getParentMessageId() != null,
              attachment.getStillUrl(), attachment.getMediaUrl(), sendMessageEvent.getSenderName(),
              sendMessageEvent.getSenderProfileImageUrl(), null, false, true, null,
              (sendMessageEvent.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                      sendMessageEvent.getMetaData())), attachment.getName(),
              attachment.getMediaId(), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), false, false, sendMessageEvent.getConversationId(),
              false);

          break;
        }
        case "AttachmentMessage:Whiteboard": {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);

          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.WhiteboardSent : MessageTypesForUI.WhiteboardReceived,
              selfMessage, sendMessageEvent.getSentAt(),
              sendMessageEvent.getParentMessageId() != null,
              FileUtils.getSizeOfFile(attachment.getSize()), false, false, true, false,
              attachment.getThumbnailUrl(), attachment.getMediaUrl(), attachment.getMimeType(),
              attachment.getExtension(), sendMessageEvent.getSenderName(),
              sendMessageEvent.getSenderProfileImageUrl(), null, true, null,
              (sendMessageEvent.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                      sendMessageEvent.getMetaData())), attachment.getSize(), attachment.getName(),
              attachment.getMediaId(), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), null, false, false,
              sendMessageEvent.getConversationId(), false);

          break;
        }
        case "AttachmentMessage:Contact": {
          JSONObject messageMetadata = sendMessageEvent.getMetaData();
          String contactName = "", contactIdentifier = "", contactImageUrl = "";
          try {
            contactName = messageMetadata.getJSONArray("contacts").getJSONObject(0).getString("contactName");
            contactIdentifier = messageMetadata.getJSONArray("contacts").getJSONObject(0).getString("contactIdentifier");
            contactImageUrl = messageMetadata.getJSONArray("contacts").getJSONObject(0).getString("contactImageUrl");
          } catch (JSONException ignore) {
            try {
              contactName = messageMetadata.getString("contactName");
              contactIdentifier = messageMetadata.getString("contactIdentifier");
              contactImageUrl = messageMetadata.getString("contactImageUrl");
            } catch (JSONException e) {
              throw new RuntimeException(e);
            }

          }
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypesForUI.ContactSent : MessageTypesForUI.ContactReceived,
              selfMessage, sendMessageEvent.getSentAt(),
              sendMessageEvent.getParentMessageId() != null, contactName, contactIdentifier,
              contactImageUrl, sendMessageEvent.getSenderName(),
              sendMessageEvent.getSenderProfileImageUrl(), null, true, null,
              (sendMessageEvent.getParentMessageId() == null) ? null
                  : (new OriginalReplyMessageUtil(sendMessageEvent.getParentMessageId(),
                      sendMessageEvent.getMetaData())), sendMessageEvent.getMessageType(),
              sendMessageEvent.getMetaData(), false, false, sendMessageEvent.getConversationId(),
              false);
          break;
        }
      }
    }
    return messageModel;
  }

  /**
   * Parse message event for notification local message notification util.
   *
   * @param sendMessageEvent the send message event
   * @return the local message notification util
   */
  public static LocalMessageNotificationUtil parseMessageEventForNotification(
      SendMessageEvent sendMessageEvent) {
    String messageText = null;
    String conversationTitle, conversationImageUrl;
    Integer messagePlaceHolderImage = null;
    if (sendMessageEvent.isPrivateOneToOne()) {

      conversationTitle = sendMessageEvent.getSenderName();
      conversationImageUrl = sendMessageEvent.getSenderProfileImageUrl();
    } else {
      conversationTitle = sendMessageEvent.getConversationTitle();
      conversationImageUrl = sendMessageEvent.getConversationImageUrl();
    }
    switch (sendMessageEvent.getCustomType()) {
      case "AttachmentMessage:Text": {

        if (sendMessageEvent.getParentMessageId() == null) {

          if (sendMessageEvent.getAction() != null) {
            //action not received for normal messages

            if ("forward".equals(sendMessageEvent.getAction())) {
              messagePlaceHolderImage = R.drawable.ism_ic_forward;
            }
          }
        } else {
          messagePlaceHolderImage = R.drawable.ism_ic_quote;
        }

        messageText = sendMessageEvent.getBody();
        break;
      }
      case "AttachmentMessage:Image": {
        messagePlaceHolderImage = R.drawable.ism_ic_picture;
        messageText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_photo);
        break;
      }
      case "AttachmentMessage:Video": {
        messagePlaceHolderImage = R.drawable.ism_ic_video;
        messageText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_video);
        break;
      }
      case "AttachmentMessage:Audio": {
        messagePlaceHolderImage = R.drawable.ism_ic_mic;
        messageText =
            IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_audio_recording);
        break;
      }
      case "AttachmentMessage:File": {
        messagePlaceHolderImage = R.drawable.ism_ic_file;
        messageText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_file);
        break;
      }
      case "AttachmentMessage:Sticker": {
        messagePlaceHolderImage = R.drawable.ism_ic_sticker;
        messageText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_sticker);
        break;
      }
      case "AttachmentMessage:Gif": {
        messagePlaceHolderImage = R.drawable.ism_ic_gif;
        messageText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_gif);
        break;
      }
      case "AttachmentMessage:Whiteboard": {
        messagePlaceHolderImage = R.drawable.ism_ic_whiteboard;
        messageText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_whiteboard);
        break;
      }
      case "AttachmentMessage:Location": {
        messagePlaceHolderImage = R.drawable.ism_ic_location;
        messageText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_location);
        break;
      }
      case "AttachmentMessage:Contact": {
        messagePlaceHolderImage = R.drawable.ism_ic_contact;
        messageText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_contact);
        break;
      }
      case "AttachmentMessage:Reply":{
        messagePlaceHolderImage = R.drawable.ism_ic_quote;
        messageText = sendMessageEvent.getBody();
      }
    }
    if (messageText == null) {
      return null;
    } else {
      return new LocalMessageNotificationUtil(sendMessageEvent.getSenderName() + ": " + messageText,
          conversationTitle, conversationImageUrl, messagePlaceHolderImage);
    }
  }
}
