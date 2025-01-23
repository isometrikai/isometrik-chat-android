package io.isometrik.ui.messages.chat.utils.messageutils;

import io.isometrik.chat.enums.CustomMessageTypes;
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
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.chat.enums.MessageTypeUi;
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
        IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_cleared_conversation);

    messageModel =
        new MessagesModel(conversationActionMessage, null, clearConversationEvent.getSentAt(),
            false, MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_conversation_image,
            updateConversationImageEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, updateConversationImageEvent.getSentAt(),
            false, MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
      settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_settings_typing);
    }
    if (config.getReadEvents() != null) {
      settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_settings_read_delivery_events);
    }
    if (config.getPushNotifications() != null) {
      settingsUpdated = settingsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_settings_notifications);
    }

    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_settings, updateConversationSettingsEvent.getUserName(),
            settingsUpdated.substring(2));
    messageModel = new MessagesModel(conversationActionMessage, null,

        updateConversationSettingsEvent.getSentAt(), false,
        MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
      detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_custom_type);
    }
    if (details.getMetadata() != null && details.getMetadata().length() > 0) {
      detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_metadata);
    }
    if (details.getSearchableTags() != null) {
      detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_searchable_tags);
    }

    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_conversation_details,
            updateConversationDetailsEvent.getUserName(), detailsUpdated.substring(2));
    messageModel = new MessagesModel(conversationActionMessage, null,

        updateConversationDetailsEvent.getSentAt(), false,
        MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
      detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_custom_type);
    }
    if (details.getMetadata() != null && details.getMetadata().length() > 0) {
      detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_metadata);
    }
    if (details.getSearchableTags() != null) {
      detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_searchable_tags);
    }
    if (details.getBody() != null) {
      detailsUpdated = detailsUpdated + ", " + IsometrikChatSdk.getInstance()
          .getContext()
          .getString(R.string.ism_details_body);
    }

    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_message_details, updateMessageDetailsEvent.getUserName(),
            detailsUpdated.substring(2));
    messageModel = new MessagesModel(conversationActionMessage, null,

        updateMessageDetailsEvent.getSentAt(), false, MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_updated_conversation_title,
            updateConversationTitleEvent.getUserName(),
            updateConversationTitleEvent.getConversationTitle());
    messageModel =
        new MessagesModel(conversationActionMessage, null, updateConversationTitleEvent.getSentAt(),
            false, MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

    return messageModel;
  }

  /**
   * Parse add admin event messages model.
   *
   * @param addAdminEvent the add admin event
   * @return the messages model
   */
  public static MessagesModel parseAddAdminEvent(AddAdminEvent addAdminEvent) {
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_made_admin, addAdminEvent.getMemberName(),
            addAdminEvent.getInitiatorName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, addAdminEvent.getSentAt(), false,
            MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_members_added, addMembersEvent.getUserName(),
            membersAdded.substring(2));
    messageModel =
        new MessagesModel(conversationActionMessage, null, addMembersEvent.getSentAt(), false,
            MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_member_joined_public, joinConversationEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, joinConversationEvent.getSentAt(), false,
            MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_member_left, leaveConversationEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, leaveConversationEvent.getSentAt(),
            false, MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

    return messageModel;
  }

  /**
   * Parse remove admin event messages model.
   *
   * @param removeAdminEvent the remove admin event
   * @return the messages model
   */
  public static MessagesModel parseRemoveAdminEvent(RemoveAdminEvent removeAdminEvent) {
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_removed_admin, removeAdminEvent.getMemberName(),
            removeAdminEvent.getInitiatorName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, removeAdminEvent.getSentAt(), false,
            MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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

    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_members_removed, removeMembersEvent.getUserName(),
            membersRemoved.substring(2));
    messageModel =
        new MessagesModel(conversationActionMessage, null, removeMembersEvent.getSentAt(), false,
            MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_blocked_user, blockUserInConversationEvent.getOpponentName(),
            blockUserInConversationEvent.getInitiatorName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, blockUserInConversationEvent.getSentAt(),
            false, MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_unblocked_user, unblockUserInConversationEvent.getOpponentName(),
            unblockUserInConversationEvent.getInitiatorName());
    messageModel = new MessagesModel(conversationActionMessage, null,
        unblockUserInConversationEvent.getSentAt(), false,
        MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

    return messageModel;
  }

  /**
   * Parse join as observer event messages model.
   *
   * @param observerJoinEvent the observer join event
   * @return the messages model
   */
  public static MessagesModel parseJoinAsObserverEvent(ObserverJoinEvent observerJoinEvent) {
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_member_observer_joined, observerJoinEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, observerJoinEvent.getSentAt(), false,
            MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

    return messageModel;
  }

  /**
   * Parse leave as observer event messages model.
   *
   * @param observerLeaveEvent the observer leave event
   * @return the messages model
   */
  public static MessagesModel parseLeaveAsObserverEvent(ObserverLeaveEvent observerLeaveEvent) {
    conversationActionMessage = IsometrikChatSdk.getInstance()
        .getContext()
        .getString(R.string.ism_member_observer_left, observerLeaveEvent.getUserName());
    messageModel =
        new MessagesModel(conversationActionMessage, null, observerLeaveEvent.getSentAt(), false,
            MessageTypeUi.CONVERSATION_ACTION_MESSAGE);

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
  //      MessageTypeUi.CONVERSATION_ACTION_MESSAGE);
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
  //          false, MessageTypeUi.CONVERSATION_ACTION_MESSAGE);
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
          .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId());

      CustomMessageTypes customMessageType = CustomMessageTypes.Companion.fromValue(sendMessageEvent.getCustomType());
      switch (customMessageType) {

        case Text:

        case Replay: {
          boolean isQuoted = sendMessageEvent.getParentMessageId() != null;
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypeUi.TEXT_MESSAGE_SENT : MessageTypeUi.TEXT_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Payment: {
          boolean isQuoted = sendMessageEvent.getParentMessageId() != null;
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.PAYMENT_MESSAGE_SENT : MessageTypeUi.PAYMENT_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Post: {
          boolean isQuoted = sendMessageEvent.getParentMessageId() != null;
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.POST_MESSAGE_SENT : MessageTypeUi.POST_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Image: {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);

          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypeUi.PHOTO_MESSAGE_SENT : MessageTypeUi.PHOTO_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Video: {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);

          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypeUi.VIDEO_MESSAGE_SENT : MessageTypeUi.VIDEO_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Audio: {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypeUi.AUDIO_MESSAGE_SENT : MessageTypeUi.AUDIO_MESSAGE_RECEIVED,
                  customMessageType,
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
        case File: {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypeUi.FILE_MESSAGE_SENT : MessageTypeUi.FILE_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Location: {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);

          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypeUi.LOCATION_MESSAGE_SENT : MessageTypeUi.LOCATION_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Sticker: {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypeUi.STICKER_MESSAGE_SENT : MessageTypeUi.STICKER_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Gif: {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypeUi.GIF_MESSAGE_SENT : MessageTypeUi.GIF_MESSAGE_RECEIVED,
                  customMessageType, selfMessage,
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
        case Whiteboard: {
          Attachment attachment = sendMessageEvent.getAttachments().get(0);

          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
              selfMessage ? MessageTypeUi.WHITEBOARD_MESSAGE_SENT : MessageTypeUi.WHITEBOARD_MESSAGE_RECEIVED,
                  customMessageType,
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
        case Contact: {
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
              selfMessage ? MessageTypeUi.CONTACT_MESSAGE_SENT : MessageTypeUi.CONTACT_MESSAGE_RECEIVED,
                  customMessageType,
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
        case OfferSent: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.OFFER_SENT : MessageTypeUi.OFFER_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
        case CounterOffer: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.COUNTER_OFFER_SENT : MessageTypeUi.COUNTER_OFFER_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
        case EditOffer: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.EDIT_OFFER_SENT : MessageTypeUi.EDIT_OFFER_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
        case AcceptOffer: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.ACCEPT_OFFER_SENT : MessageTypeUi.ACCEPT_OFFER_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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

        case CancelDeal: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.CANCEL_DEAL_SENT : MessageTypeUi.CANCEL_DEAL_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
        case CancelOffer: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.CANCEL_OFFER_SENT : MessageTypeUi.CANCEL_OFFER_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
        case BuyDirect: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.BUY_DIRECT_SENT : MessageTypeUi.BUY_DIRECT_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
        case AcceptBuyDirect: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.ACCEPT_BUY_DIRECT_SENT : MessageTypeUi.ACCEPT_BUY_DIRECT_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
        case CancelBuyDirect: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.CANCEL_BUY_DIRECT_SENT : MessageTypeUi.CANCEL_BUY_DIRECT_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
        case PaymentEscrowed: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.PAYMENT_ESCROWED_SENT : MessageTypeUi.PAYMENT_ESCROWED_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
        case DealComplete: {
          messageModel = new MessagesModel(sendMessageEvent.getMessageId(),
                  selfMessage ? MessageTypeUi.DEAL_COMPLETE_SENT : MessageTypeUi.DEAL_COMPLETE_RECEIVED,
                  customMessageType,
                  selfMessage, sendMessageEvent.getSentAt(), false,
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
    switch (CustomMessageTypes.Companion.fromValue(sendMessageEvent.getCustomType())) {
      case Text: {

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
      case Image: {
        messagePlaceHolderImage = R.drawable.ism_ic_picture;
        messageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photo);
        break;
      }
      case Video: {
        messagePlaceHolderImage = R.drawable.ism_ic_video;
        messageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_video);
        break;
      }
      case Audio: {
        messagePlaceHolderImage = R.drawable.ism_ic_mic;
        messageText =
            IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_audio_recording);
        break;
      }
      case File: {
        messagePlaceHolderImage = R.drawable.ism_ic_file;
        messageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_file);
        break;
      }
      case Sticker: {
        messagePlaceHolderImage = R.drawable.ism_ic_sticker;
        messageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_sticker);
        break;
      }
      case Gif: {
        messagePlaceHolderImage = R.drawable.ism_ic_gif;
        messageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_gif);
        break;
      }
      case Whiteboard: {
        messagePlaceHolderImage = R.drawable.ism_ic_whiteboard;
        messageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_whiteboard);
        break;
      }
      case Location: {
        messagePlaceHolderImage = R.drawable.ism_ic_location;
        messageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_location);
        break;
      }
      case Contact: {
        messagePlaceHolderImage = R.drawable.ism_ic_contact;
        messageText = IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_contact);
        break;
      }
      case Replay:{
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
