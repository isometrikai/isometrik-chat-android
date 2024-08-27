package io.isometrik.chat.models.events;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.events.message.SendMessageEvent;
import io.isometrik.chat.events.message.UpdateMessageDetailsEvent;
import io.isometrik.chat.events.message.user.block.BlockUserInConversationEvent;
import io.isometrik.chat.events.message.user.block.UnblockUserInConversationEvent;
import io.isometrik.chat.models.message.delivery.operations.MarkMessagesAsDeliveredUtil;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to parse and announce message events on respective registered realtime event
 * listeners for-
 * UpdateMessageDetailsEvent, BlockUserInConversationEvent, UnblockUserInConversationEvent and
 * SendMessageEvent events.
 */
public class MessageEvents {

  /**
   * Handle message event.
   *
   * @param jsonObject the json object
   * @param isometrikInstance the isometrik instance
   * @throws JSONException the json exception
   */
  public void handleMessageEvent(JSONObject jsonObject, @NotNull Isometrik isometrikInstance)
      throws JSONException {

    String action = "";
    if (jsonObject.has("action")) action = jsonObject.getString("action");

    switch (action) {
      case "messageDetailsUpdated":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), UpdateMessageDetailsEvent.class));
        break;

      case "userBlockConversation":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), BlockUserInConversationEvent.class));
        break;

      case "userUnblockConversation":

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), UnblockUserInConversationEvent.class));
        break;

      case "broadcast":

      case "forward":

      default:
        SendMessageEvent sendMessageEvent =
            isometrikInstance.getGson().fromJson(jsonObject.toString(), SendMessageEvent.class);

        isometrikInstance.getRealtimeEventsListenerManager()
            .getMessageListenerManager()
            .announce(sendMessageEvent);
        try {
          MarkMessagesAsDeliveredUtil.markMessageAsDelivered(sendMessageEvent,
              isometrikInstance.getConfiguration().getUserToken(),
              isometrikInstance.getConfiguration().getClientId().substring(0, 24),
              isometrikInstance);
        } catch (NullPointerException ignore) {
        }
    }
  }
}
