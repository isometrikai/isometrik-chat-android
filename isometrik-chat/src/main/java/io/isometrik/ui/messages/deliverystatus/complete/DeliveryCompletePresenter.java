package io.isometrik.ui.messages.deliverystatus.complete;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.deliverystatus.FetchMessageDeliveryStatusQuery;
import io.isometrik.chat.builder.deliverystatus.FetchMessageReadStatusQuery;
import io.isometrik.chat.callbacks.MessageEventCallback;
import io.isometrik.chat.events.message.SendMessageEvent;
import io.isometrik.chat.events.message.SendTypingMessageEvent;
import io.isometrik.chat.events.message.UpdateMessageDetailsEvent;
import io.isometrik.chat.events.message.cleanup.RemoveMessagesForEveryoneEvent;
import io.isometrik.chat.events.message.cleanup.RemoveMessagesForSelfEvent;
import io.isometrik.chat.events.message.delivery.MarkMessageAsDeliveredEvent;
import io.isometrik.chat.events.message.delivery.MarkMessageAsReadEvent;
import io.isometrik.chat.events.message.delivery.MarkMultipleMessagesAsReadEvent;
import io.isometrik.chat.events.message.delivery.UpdatedLastReadInConversationEvent;
import io.isometrik.chat.events.message.user.block.BlockUserInConversationEvent;
import io.isometrik.chat.events.message.user.block.UnblockUserInConversationEvent;
import io.isometrik.chat.response.user.utils.DeliveredToOrReadByUser;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.messages.deliverystatus.UsersModel;
import io.isometrik.chat.utils.Constants;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The presenter to fetch list of users for whom message delivery and read is complete
 * for a message, with paging.
 */
public class DeliveryCompletePresenter implements DeliveryCompleteContract.Presenter {

  private DeliveryCompleteContract.View deliveryCompleteView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  private String conversationId, messageId;

  private int offsetDelivery, deliveryCount;
  private boolean isLastPageDelivery, isLoadingDelivery;

  private int offsetRead, readCount;
  private boolean isLastPageRead, isLoadingRead;

  private long sentAt;

  @Override
  public void initialize(String conversationId, String messageId, long sentAt) {
    this.conversationId = conversationId;
    this.messageId = messageId;
    this.sentAt = sentAt;
  }

  @Override
  public void attachView(DeliveryCompleteContract.View deliveryCompleteView) {
    this.deliveryCompleteView = deliveryCompleteView;
  }

  @Override
  public void detachView() {
    deliveryCompleteView = null;
  }

  @Override
  public void fetchMessageDeliveryStatus(int skip, int limit, boolean onScroll) {

    isLoadingDelivery = true;
    if (skip == 0) {
      isLastPageDelivery = false;
      deliveryCount = 0;
      offsetDelivery=0;
    }
    isometrik.getRemoteUseCases().getDeliveryStatusUseCases().fetchMessageDeliveryStatus(
        new FetchMessageDeliveryStatusQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setMessageId(messageId)
            .setLimit(limit)
            .setSkip(skip)
            .build(), (var1, var2) -> {
          isLoadingDelivery = false;
          if (var1 != null) {

            ArrayList<UsersModel> usersModels = new ArrayList<>();
            ArrayList<DeliveredToOrReadByUser> users = var1.getDeliveredToUsers();
            int size = users.size();
            String userId = IsometrikChatSdk.getInstance().getUserSession().getUserId();
            for (int i = 0; i < size; i++) {
              if (users.get(i).getUserId().equals(userId)) {
                deliveryCount++;
              } else {
                usersModels.add(new UsersModel(users.get(i)));
              }
            }
            if (size < limit) {

              isLastPageDelivery = true;
            }
            if (deliveryCompleteView != null) {
              deliveryCompleteView.onMessageDeliveryStatusFetchedSuccessfully(usersModels,
                  onScroll);
            }
          } else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

            if (!onScroll) {
              //No users found
              if (deliveryCompleteView != null) {
                deliveryCompleteView.onMessageDeliveryStatusFetchedSuccessfully(new ArrayList<>(),
                    false);
              }
            } else {
              isLastPageDelivery = true;
            }
          } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
            if (deliveryCompleteView != null) {
              deliveryCompleteView.onMessageDeliveryReadEventsDisabled();
            }
          } else {
            if (deliveryCompleteView != null) {
              deliveryCompleteView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void fetchMessageReadStatus(int skip, int limit, boolean onScroll) {
    isLoadingRead = true;
    if (skip == 0) {
      isLastPageRead = false;
      readCount = 0;
      offsetRead=0;
    }
    isometrik.getRemoteUseCases().getDeliveryStatusUseCases().fetchMessageReadStatus(
        new FetchMessageReadStatusQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setMessageId(messageId)
            .setLimit(limit)
            .setSkip(skip)
            .build(), (var1, var2) -> {
          isLoadingRead = false;
          if (var1 != null) {

            ArrayList<UsersModel> usersModels = new ArrayList<>();
            ArrayList<DeliveredToOrReadByUser> users = var1.getReadByUsers();
            int size = users.size();
            String userId = IsometrikChatSdk.getInstance().getUserSession().getUserId();
            for (int i = 0; i < size; i++) {
              if (users.get(i).getUserId().equals(userId)) {
                readCount++;
              } else {
                usersModels.add(new UsersModel(users.get(i)));
              }
            }
            if (size < limit) {

              isLastPageRead = true;
            }
            if (deliveryCompleteView != null) {
              deliveryCompleteView.onMessageReadStatusFetchedSuccessfully(usersModels, onScroll);
            }
          } else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

            if (!onScroll) {
              //No users found
              if (deliveryCompleteView != null) {
                deliveryCompleteView.onMessageReadStatusFetchedSuccessfully(new ArrayList<>(),
                    false);
              }
            } else {
              isLastPageRead = true;
            }
          } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
            if (deliveryCompleteView != null) {
              deliveryCompleteView.onMessageDeliveryReadEventsDisabled();
            }
          } else {
            if (deliveryCompleteView != null) {
              deliveryCompleteView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void fetchMessageDeliveryStatusOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoadingDelivery && !isLastPageDelivery) {

      int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount + deliveryCount) >= PAGE_SIZE) {

        offsetDelivery++;
        fetchMessageDeliveryStatus(offsetDelivery * PAGE_SIZE, PAGE_SIZE, true);
      }
    }
  }

  @Override
  public void fetchMessageReadStatusOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoadingRead && !isLastPageRead) {

      int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount + readCount) >= PAGE_SIZE) {

        offsetRead++;
        fetchMessageReadStatus(offsetRead * PAGE_SIZE, PAGE_SIZE, true);
      }
    }
  }

  private final MessageEventCallback messageEventCallback = new MessageEventCallback() {
    @Override
    public void messageDetailsUpdated(@NotNull Isometrik isometrik,
        @NotNull UpdateMessageDetailsEvent updateMessageDetailsEvent) {
      //TODO Nothing
    }

    @Override
    public void updatedLastReadInConversationEvent(@NotNull Isometrik isometrik,
        @NotNull UpdatedLastReadInConversationEvent updatedLastReadInConversationEvent) {
      //TODO Nothing
    }

    @Override
    public void messagesRemovedForEveryone(@NotNull Isometrik isometrik,
        @NotNull RemoveMessagesForEveryoneEvent removeMessagesForEveryoneEvent) {
      //TODO Nothing
    }

    @Override
    public void messagesRemovedForSelf(@NotNull Isometrik isometrik,
        @NotNull RemoveMessagesForSelfEvent removeMessagesForSelfEvent) {
      //TODO Nothing
    }

    @Override
    public void messageMarkedAsDelivered(@NotNull Isometrik isometrik,
        @NotNull MarkMessageAsDeliveredEvent markMessageAsDeliveredEvent) {
      if (markMessageAsDeliveredEvent.getConversationId().equals(conversationId)) {
        if (!markMessageAsDeliveredEvent.getUserId()
            .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

          if (markMessageAsDeliveredEvent.getMessageId().equals(messageId)) {
            if (deliveryCompleteView != null) {
              deliveryCompleteView.onMessageDeliveredEvent(
                  new UsersModel(markMessageAsDeliveredEvent));
            }
          }
        }
      }
    }

    @Override
    public void messageMarkedAsRead(@NotNull Isometrik isometrik,
        @NotNull MarkMessageAsReadEvent markMessageAsReadEvent) {
      if (markMessageAsReadEvent.getConversationId().equals(conversationId)) {
        if (!markMessageAsReadEvent.getUserId()
            .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

          if (markMessageAsReadEvent.getMessageId().equals(messageId)) {
            if (deliveryCompleteView != null) {
              deliveryCompleteView.onMessageDeliveredEvent(new UsersModel(markMessageAsReadEvent));
              deliveryCompleteView.onMessageReadEvent(new UsersModel(markMessageAsReadEvent));
            }
          }
        }
      }
    }

    @Override
    public void multipleMessagesMarkedAsRead(@NotNull Isometrik isometrik,
        @NotNull MarkMultipleMessagesAsReadEvent markMultipleMessagesAsReadEvent) {

      if (markMultipleMessagesAsReadEvent.getConversationId().equals(conversationId)) {

        if (!markMultipleMessagesAsReadEvent.getUserId()
            .equals(IsometrikChatSdk.getInstance().getUserSession().getUserId())) {

          if (markMultipleMessagesAsReadEvent.getLastReadAt() > sentAt) {

            if (deliveryCompleteView != null) {
              deliveryCompleteView.onMessageDeliveredEvent(
                  new UsersModel(markMultipleMessagesAsReadEvent));

              deliveryCompleteView.onMessageReadEvent(
                  new UsersModel(markMultipleMessagesAsReadEvent));
            }
          }
        }
      }
    }

    @Override
    public void messageSent(@NotNull Isometrik isometrik,
        @NotNull SendMessageEvent sendMessageEvent) {
      //TODO Nothing
    }

    @Override
    public void typingMessageSent(@NotNull Isometrik isometrik,
        @NotNull SendTypingMessageEvent sendTypingMessageEvent) {
      //TODO Nothing
    }
    @Override
    public void blockedUserInConversation(@NotNull Isometrik isometrik,
        @NotNull BlockUserInConversationEvent blockUserInConversationEvent) {
      //TODO Nothing
    }

    @Override
    public void unblockedUserInConversation(@NotNull Isometrik isometrik,
        @NotNull UnblockUserInConversationEvent unblockUserInConversationEvent) {
      //TODO Nothing
    }
  };

  @Override
  public void registerMessageEventListener() {
    isometrik.getRealtimeEventsListenerManager().getMessageListenerManager().addListener(messageEventCallback);
  }

  @Override
  public void unregisterMessageEventListener() {
    isometrik.getRealtimeEventsListenerManager().getMessageListenerManager().removeListener(messageEventCallback);
  }
}