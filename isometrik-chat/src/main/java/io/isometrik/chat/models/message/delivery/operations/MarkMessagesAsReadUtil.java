package io.isometrik.chat.models.message.delivery.operations;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.message.delivery.MarkMessageAsReadQuery;
import io.isometrik.chat.events.message.SendMessageEvent;
import io.isometrik.chat.response.message.utils.fetchmessages.Message;
import io.isometrik.chat.response.message.utils.fetchmessages.ReadByDeliveredToUser;
import java.util.ArrayList;

/**
 * The helper class to mark messages as read.
 */
public class MarkMessagesAsReadUtil {

  /**
   * Mark messages as read.
   *
   * @param messages the messages
   * @param userToken the user token
   * @param userId the user id
   * @param isometrik the isometrik
   */
  public static void markMessagesAsRead(ArrayList<Message> messages, String userToken,
      String userId, Isometrik isometrik) {
    if (messages != null) {
isometrik.getExecutor().execute(() -> {
  if (userId != null && !userId.isEmpty() && userToken != null && !userToken.isEmpty()) {
    int size = messages.size();
    boolean alreadyMarkedAsRead;
    Message message;
    ArrayList<ReadByDeliveredToUser> readByUsers;
    int sizeInner;
    for (int i = 0; i < size; i++) {
      try {
        if (messages.get(i).getDeliveryReadEventsEnabled() == null || !messages.get(i)
            .getDeliveryReadEventsEnabled()) {
          break;
        } else {
          message = messages.get(i);

          if (message.getConversationStatusMessage() == null) {
            if (!message.isReadByAll()
                && message.getSenderInfo() != null
                && message.getSenderInfo().getUserId() != null
                && !message.getSenderInfo().getUserId().equals(userId)) {

              readByUsers = message.getReadByUsers();

              if (readByUsers != null) {

                alreadyMarkedAsRead = false;
                sizeInner = readByUsers.size();
                for (int j = 0; j < sizeInner; j++) {
                  if (readByUsers.get(j).getUserId().equals(userId)) {
                    alreadyMarkedAsRead = true;
                    break;
                  }
                }
                if (!alreadyMarkedAsRead) {

                  isometrik.getRemoteUseCases()
                      .getMessageUseCases()
                      .markMessageAsRead(new MarkMessageAsReadQuery.Builder().setConversationId(
                              message.getConversationId())
                          .setUserToken(userToken)
                          .setMessageId(message.getMessageId())
                          .build(), (var1, var2) -> {

                      });
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
   * Fetch messages to be marked as read count int.
   *
   * @param messages the messages
   * @param userToken the user token
   * @param userId the user id
   * @return the int
   */
  public static int fetchMessagesToBeMarkedAsReadCount(ArrayList<Message> messages,
      String userToken, String userId) {
    int count = 0;
    if (messages != null) {
      if (userId != null && !userId.isEmpty() && userToken != null && !userToken.isEmpty()) {
        int size = messages.size();
        boolean alreadyMarkedAsRead;
        Message message;
        ArrayList<ReadByDeliveredToUser> readByUsers;
        int sizeInner;
        for (int i = 0; i < size; i++) {
          try {
            if (messages.get(i).getDeliveryReadEventsEnabled() == null || !messages.get(i)
                .getDeliveryReadEventsEnabled()) {
              break;
            } else {
              message = messages.get(i);

              if (message.getConversationStatusMessage() == null) {
                if (!message.isReadByAll()
                    && message.getSenderInfo() != null
                    && message.getSenderInfo().getUserId() != null
                    && !message.getSenderInfo().getUserId().equals(userId)) {

                  readByUsers = message.getReadByUsers();

                  if (readByUsers != null) {

                    alreadyMarkedAsRead = false;
                    sizeInner = readByUsers.size();
                    for (int j = 0; j < sizeInner; j++) {
                      if (readByUsers.get(j).getUserId().equals(userId)) {
                        alreadyMarkedAsRead = true;
                        break;
                      }
                    }
                    if (!alreadyMarkedAsRead) {

                      count++;
                    }
                  }
                }
              }
            }
          } catch (Exception ignore) {
          }
        }
      }
    }
    return count;
  }

  /**
   * Mark message as read.
   *
   * @param sendMessageEvent the send message event
   * @param userToken the user token
   * @param userId the user id
   * @param isometrik the isometrik
   */
  public static void markMessageAsRead(SendMessageEvent sendMessageEvent, String userToken,
      String userId, Isometrik isometrik) {
    if (sendMessageEvent.getDeliveryReadEventsEnabled()) {
      isometrik.getExecutor().execute(() -> {

        if (userId != null && !userId.isEmpty() && userToken != null && !userToken.isEmpty()) {

          if (!sendMessageEvent.getSenderId().equals(userId)) {

            isometrik.getRemoteUseCases()
                .getMessageUseCases()
                .markMessageAsRead(new MarkMessageAsReadQuery.Builder().setConversationId(
                        sendMessageEvent.getConversationId())
                    .setUserToken(userToken)
                    .setMessageId(sendMessageEvent.getMessageId())
                    .build(), (var1, var2) -> {

                });
          }
        }
      });
    }
  }
}
