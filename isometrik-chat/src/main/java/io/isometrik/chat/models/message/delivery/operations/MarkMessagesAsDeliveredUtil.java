package io.isometrik.chat.models.message.delivery.operations;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.message.delivery.MarkMessageAsDeliveredQuery;
import io.isometrik.chat.events.message.SendMessageEvent;
import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import io.isometrik.chat.response.message.utils.fetchmessages.ReadByDeliveredToUser;
import io.isometrik.chat.response.message.utils.fetchmessages.UserMessage;
import java.util.ArrayList;

/**
 * The helper class to mark messages as delivered.
 */
public class MarkMessagesAsDeliveredUtil {

  /**
   * Mark user messages as delivered.
   *
   * @param messages the messages
   * @param userToken the user token
   * @param userId the user id
   * @param isometrik the isometrik
   */
  public static void markUserMessagesAsDelivered(ArrayList<UserMessage> messages, String userToken,
      String userId, Isometrik isometrik) {

    if (messages != null) {
      isometrik.getExecutor().execute(() -> {
        if (userId != null && !userId.isEmpty() && userToken != null && !userToken.isEmpty()) {
          int size = messages.size();
          boolean alreadyMarkedAsDelivered;
          UserMessage message;
          ArrayList<ReadByDeliveredToUser> deliveredToUsers;
          int sizeInner;
          for (int i = 0; i < size; i++) {
            try {
              if (messages.get(i).getDeliveryReadEventsEnabled() != null && messages.get(i)
                  .getDeliveryReadEventsEnabled()) {

                message = messages.get(i);

                if (message.getConversationStatusMessage() == null) {
                  if (!message.isDeliveredToAll()
                      && message.getSenderInfo() != null
                      && message.getSenderInfo().getUserId() != null
                      && !message.getSenderInfo().getUserId().equals(userId)) {
                    deliveredToUsers = message.getDeliveredToUsers();
                    if (deliveredToUsers != null) {
                      alreadyMarkedAsDelivered = false;
                      sizeInner = deliveredToUsers.size();
                      for (int j = 0; j < sizeInner; j++) {
                        if (deliveredToUsers.get(j).getUserId().equals(userId)) {
                          alreadyMarkedAsDelivered = true;
                          break;
                        }
                      }
                      if (!alreadyMarkedAsDelivered) {
                        UserMessage finalMessage = message;
                        isometrik.getRemoteUseCases()
                            .getMessageUseCases()
                            .markMessageAsDelivered(
                                new MarkMessageAsDeliveredQuery.Builder().setConversationId(
                                        message.getConversationId())
                                    .setUserToken(userToken)
                                    .setMessageId(message.getMessageId())
                                    .build(),
                                (var1, var2) -> isometrik.setDeliveryStatusUpdatedUpto(
                                    finalMessage.getSentAt()));
                      }
                    }
                  }
                }
              } else {
                isometrik.setDeliveryStatusUpdatedUpto(messages.get(i).getSentAt());
              }
            } catch (Exception ignore) {
            }
          }
        }
      });
    }
  }

  /**
   * Mark messages as delivered.
   *
   * @param messages the messages
   * @param userToken the user token
   * @param userId the user id
   * @param isometrik the isometrik
   */
  public static void markMessagesAsDelivered(ArrayList<Message> messages, String userToken,
      String userId, Isometrik isometrik) {
    if (messages != null) {
      isometrik.getExecutor().execute(() -> {
        if (userId != null && !userId.isEmpty() && userToken != null && !userToken.isEmpty()) {
          int size = messages.size();
          boolean alreadyMarkedAsDelivered;
          Message message;
          ArrayList<ReadByDeliveredToUser> deliveredToUsers;
          int sizeInner;
          for (int i = 0; i < size; i++) {
            try {
              if (messages.get(i).getDeliveryReadEventsEnabled() == null || !messages.get(i)
                  .getDeliveryReadEventsEnabled()) {
                break;
              } else {
                message = messages.get(i);

                if (message.getConversationStatusMessage() == null) {
                  if (!message.isDeliveredToAll()
                      && message.getSenderInfo() != null
                      && message.getSenderInfo().getUserId() != null
                      && !message.getSenderInfo().getUserId().equals(userId)) {
                    deliveredToUsers = message.getDeliveredToUsers();
                    if (deliveredToUsers != null) {
                      alreadyMarkedAsDelivered = false;
                      sizeInner = deliveredToUsers.size();
                      for (int j = 0; j < sizeInner; j++) {
                        if (deliveredToUsers.get(j).getUserId().equals(userId)) {
                          alreadyMarkedAsDelivered = true;
                          break;
                        }
                      }
                      if (!alreadyMarkedAsDelivered) {
                        Message finalMessage = message;
                        isometrik.getRemoteUseCases()
                            .getMessageUseCases()
                            .markMessageAsDelivered(
                                new MarkMessageAsDeliveredQuery.Builder().setConversationId(
                                        message.getConversationId())
                                    .setUserToken(userToken)
                                    .setMessageId(message.getMessageId())
                                    .build(),
                                (var1, var2) -> isometrik.setDeliveryStatusUpdatedUpto(
                                    finalMessage.getSentAt()));
                      }
                    }
                  }
                }
              }
            } catch (Exception ignore) {
            }
          }
        }
      });
    }
  }

  /**
   * Mark message as delivered.
   *
   * @param sendMessageEvent the send message event
   * @param userToken the user token
   * @param userId the user id
   * @param isometrik the isometrik
   */
  public static void markMessageAsDelivered(SendMessageEvent sendMessageEvent, String userToken,
      String userId, Isometrik isometrik) {
    if (sendMessageEvent.getDeliveryReadEventsEnabled()) {

      isometrik.getExecutor().execute(() -> {
        if (userId != null && !userId.isEmpty() && userToken != null && !userToken.isEmpty()) {

          if (!sendMessageEvent.getSenderId().equals(userId)) {

            isometrik.getRemoteUseCases()
                .getMessageUseCases()
                .markMessageAsDelivered(new MarkMessageAsDeliveredQuery.Builder().setConversationId(
                        sendMessageEvent.getConversationId())
                    .setUserToken(userToken)
                    .setMessageId(sendMessageEvent.getMessageId())
                    .build(), (var1, var2) -> isometrik.setDeliveryStatusUpdatedUpto(
                    sendMessageEvent.getSentAt()));
          }
        }
      });
    }
  }
}
