package io.isometrik.chat.models.events;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.events.conversation.CreateConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.ClearConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.DeleteConversationLocallyEvent;
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
import io.isometrik.chat.events.message.SendTypingMessageEvent;
import io.isometrik.chat.events.message.cleanup.RemoveMessagesForEveryoneEvent;
import io.isometrik.chat.events.message.cleanup.RemoveMessagesForSelfEvent;
import io.isometrik.chat.events.message.delivery.MarkMessageAsDeliveredEvent;
import io.isometrik.chat.events.message.delivery.MarkMessageAsReadEvent;
import io.isometrik.chat.events.message.delivery.MarkMultipleMessagesAsReadEvent;
import io.isometrik.chat.events.message.delivery.UpdatedLastReadInConversationEvent;
import io.isometrik.chat.events.reaction.AddReactionEvent;
import io.isometrik.chat.events.reaction.RemoveReactionEvent;
import io.isometrik.chat.events.user.DeleteUserEvent;
import io.isometrik.chat.events.user.UpdateUserEvent;
import io.isometrik.chat.events.user.block.BlockUserEvent;
import io.isometrik.chat.events.user.block.UnblockUserEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to parse and announce presence events on respective registered realtime event
 * listeners for- ClearConversationEvent, DeleteConversationLocallyEvent,
 * UpdateConversationImageEvent,  UpdateConversationSettingsEvent, UpdateConversationDetailsEvent,
 * UpdateConversationTitleEvent, CreateConversationEvent, AddAdminEvent, AddMembersEvent,
 * JoinConversationEvent, LeaveConversationEvent, ObserverJoinEvent, ObserverLeaveEvent,
 * RemoveAdminEvent, RemoveMembersEvent, RemoveMessagesForEveryoneEvent,
 * RemoveMessagesForSelfEvent,
 * MarkMessageAsDeliveredEvent, MarkMessageAsReadEvent,   MarkMultipleMessagesAsReadEvent,
 * UpdatedLastReadInConversationEvent, SendTypingMessageEvent, AddReactionEvent,
 * RemoveReactionEvent, BlockUserEvent, UnblockUserEvent, UpdateUserEvent, DeleteUserEvent
 * events.
 */
public class PresenceEvents {
  /**
   * Handle presence event.
   *
   * @param jsonObject the json object
   * @param isometrikInstance the isometrik instance
   * @throws JSONException the json exception
   */
  public void handlePresenceEvent(JSONObject jsonObject, @NotNull Isometrik isometrikInstance)
      throws JSONException {

    String action = jsonObject.getString("action");

    switch (action) {

      case "clearConversation":

        isometrikInstance.getRealtimeEventsListenerManager().getConversationListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), ClearConversationEvent.class));
        break;

      case "deleteConversationLocally":

        isometrikInstance.getRealtimeEventsListenerManager().getConversationListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), DeleteConversationLocallyEvent.class));
        break;

      case "conversationImageUpdated":

        isometrikInstance.getRealtimeEventsListenerManager().getConversationListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), UpdateConversationImageEvent.class));
        break;

      case "conversationSettingsUpdated":

        isometrikInstance.getRealtimeEventsListenerManager().getConversationListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), UpdateConversationSettingsEvent.class));
        break;

      case "conversationDetailsUpdated":

        isometrikInstance.getRealtimeEventsListenerManager().getConversationListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), UpdateConversationDetailsEvent.class));
        break;

      case "conversationTitleUpdated":

        isometrikInstance.getRealtimeEventsListenerManager().getConversationListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), UpdateConversationTitleEvent.class));
        break;

      case "conversationCreated":

        isometrikInstance.getRealtimeEventsListenerManager().getConversationListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), CreateConversationEvent.class));
        break;

      case "addAdmin":

        isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), AddAdminEvent.class));
        break;

      case "membersAdd":

        isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), AddMembersEvent.class));
        break;

      case "memberJoin":

        isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), JoinConversationEvent.class));
        break;

      case "memberLeave":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMembershipControlListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), LeaveConversationEvent.class));
        break;

      case "observerJoin":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMembershipControlListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), ObserverJoinEvent.class));
        break;

      case "observerLeave":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMembershipControlListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), ObserverLeaveEvent.class));
        break;

      case "removeAdmin":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMembershipControlListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), RemoveAdminEvent.class));
        break;

      case "membersRemove":

        isometrikInstance.getRealtimeEventsListenerManager().getMembershipControlListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), RemoveMembersEvent.class));
        break;

      case "messagesDeleteForAll":

        isometrikInstance.getRealtimeEventsListenerManager().getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), RemoveMessagesForEveryoneEvent.class));
        break;

      case "messagesDeleteLocal":

        isometrikInstance.getRealtimeEventsListenerManager().getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), RemoveMessagesForSelfEvent.class));
        break;

      case "messageDelivered":

        isometrikInstance.getRealtimeEventsListenerManager().getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), MarkMessageAsDeliveredEvent.class));
        break;

      case "messageRead":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), MarkMessageAsReadEvent.class));
        break;

      case "multipleMessagesRead":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), MarkMultipleMessagesAsReadEvent.class));
        break;

      case "lastReadUpdated":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), UpdatedLastReadInConversationEvent.class));
        break;

      case "typingEvent":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), SendTypingMessageEvent.class));
        break;

      case "reactionAdd":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getReactionListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), AddReactionEvent.class));
        break;

      case "reactionRemove":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getReactionListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), RemoveReactionEvent.class));
        break;

      case "userBlock":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getUserListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), BlockUserEvent.class));
        break;

      case "userUnblock":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getUserListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), UnblockUserEvent.class));
        break;

      case "userUpdate":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getUserListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), UpdateUserEvent.class));
        break;

      case "userDelete":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getUserListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), DeleteUserEvent.class));
        break;
    }
  }
}
