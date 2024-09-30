package io.isometrik.ui.messages.deliverystatus.pending;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.deliverystatus.FetchMessagePendingDeliveryStatusQuery;
import io.isometrik.chat.builder.deliverystatus.FetchMessagePendingReadStatusQuery;
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
import io.isometrik.chat.response.user.utils.PendingDeliveryToReadByUser;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.messages.deliverystatus.UsersModel;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The presenter to fetch list of users for whom message delivery and read is pending for a message,
 * with paging.
 */
public class DeliveryPendingPresenter implements DeliveryPendingContract.Presenter {

  private DeliveryPendingContract.View deliveryPendingView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  private String conversationId, messageId;

  private int offsetPendingDelivery, deliveryCount;
  private boolean isLastPagePendingDelivery, isLoadingPendingDelivery;

  private int offsetPendingRead, readCount;
  private boolean isLastPagePendingRead, isLoadingPendingRead;

  private long sentAt;

  @Override
  public void initialize(String conversationId, String messageId, long sentAt) {
    this.conversationId = conversationId;
    this.messageId = messageId;
    this.sentAt = sentAt;
  }

  @Override
  public void attachView(DeliveryPendingContract.View deliveryPendingView) {
    this.deliveryPendingView = deliveryPendingView;
  }

  @Override
  public void detachView() {
    deliveryPendingView = null;
  }

  @Override
  public void fetchMessagePendingDeliveryStatus(int skip, int limit, boolean onScroll) {
    isLoadingPendingDelivery = true;

    if (skip == 0) {
      isLastPagePendingDelivery = false;
      deliveryCount = 0;
      offsetPendingDelivery=0;
    }
    isometrik.getRemoteUseCases()
        .getDeliveryStatusUseCases()
        .fetchMessagePendingDeliveryStatus(
            new FetchMessagePendingDeliveryStatusQuery.Builder().setConversationId(conversationId)
                .setUserToken(userToken)
                .setMessageId(messageId)
                .setLimit(limit)
                .setSkip(skip)
                .build(), (var1, var2) -> {
              isLoadingPendingDelivery = false;
              if (var1 != null) {

                ArrayList<UsersModel> usersModels = new ArrayList<>();
        ArrayList<PendingDeliveryToReadByUser> users = var1.getDeliveryPendingToUsers();
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

                  isLastPagePendingDelivery = true;
                }
                if (deliveryPendingView != null) {
                  deliveryPendingView.onMessagePendingDeliveryStatusFetchedSuccessfully(usersModels,
                      onScroll);
                }
              } else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

                if (!onScroll) {
                  //No users found
                  if (deliveryPendingView != null) {
                    deliveryPendingView.onMessagePendingDeliveryStatusFetchedSuccessfully(
                        new ArrayList<>(), false);
                  }
                } else {
                  isLastPagePendingDelivery = true;
                }
              } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
                if (deliveryPendingView != null) {
                  deliveryPendingView.onMessageDeliveryReadEventsDisabled();
                }
              } else {
                if (deliveryPendingView != null) {
                  deliveryPendingView.onError(var2.getErrorMessage());
                }
              }
            });
  }

  @Override
  public void fetchMessagePendingReadStatus(int skip, int limit, boolean onScroll) {
    isLoadingPendingRead = true;
    if (skip == 0) {
      isLastPagePendingRead = false;
      readCount = 0;
      offsetPendingRead=0;
    }

    isometrik.getRemoteUseCases()
        .getDeliveryStatusUseCases()
        .fetchMessagePendingReadStatus(
            new FetchMessagePendingReadStatusQuery.Builder().setConversationId(conversationId)
                .setUserToken(userToken)
                .setMessageId(messageId)
                .setLimit(limit)
                .setSkip(skip)
                .build(), (var1, var2) -> {
              isLoadingPendingRead = false;
              if (var1 != null) {

                ArrayList<UsersModel> usersModels = new ArrayList<>();
            ArrayList<PendingDeliveryToReadByUser> users = var1.getReadPendingByUsers();
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

                  isLastPagePendingRead = true;
                }
                if (deliveryPendingView != null) {
                  deliveryPendingView.onMessagePendingReadStatusFetchedSuccessfully(usersModels,
                      onScroll);
                }
              } else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

                if (!onScroll) {
                  //No users found
                  if (deliveryPendingView != null) {
                    deliveryPendingView.onMessagePendingReadStatusFetchedSuccessfully(
                        new ArrayList<>(), false);
                  }
                } else {
                  isLastPagePendingRead = true;
                }
              } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
                if (deliveryPendingView != null) {
                  deliveryPendingView.onMessageDeliveryReadEventsDisabled();
                }
              } else {
                if (deliveryPendingView != null) {
                  deliveryPendingView.onError(var2.getErrorMessage());
                }
              }
            });
  }

  @Override
  public void fetchMessagePendingDeliveryStatusOnScroll(int firstVisibleItemPosition,
      int visibleItemCount, int totalItemCount) {
    if (!isLoadingPendingDelivery && !isLastPagePendingDelivery) {

      int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount + deliveryCount) >= PAGE_SIZE) {

        offsetPendingDelivery++;
        fetchMessagePendingDeliveryStatus(offsetPendingDelivery * PAGE_SIZE, PAGE_SIZE, true);
      }
    }
  }

  @Override
  public void fetchMessagePendingReadStatusOnScroll(int firstVisibleItemPosition,
      int visibleItemCount, int totalItemCount) {
    if (!isLoadingPendingRead && !isLastPagePendingRead) {

      int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount + readCount) >= PAGE_SIZE) {

        offsetPendingRead++;
        fetchMessagePendingReadStatus(offsetPendingRead * PAGE_SIZE, PAGE_SIZE, true);
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
            if (deliveryPendingView != null) {
              deliveryPendingView.onMessageDeliveredEvent(markMessageAsDeliveredEvent.getUserId());
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

            if (deliveryPendingView != null) {
              deliveryPendingView.onMessageDeliveredEvent(markMessageAsReadEvent.getUserId());

              deliveryPendingView.onMessageReadEvent(markMessageAsReadEvent.getUserId());
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
            if (deliveryPendingView != null) {
              deliveryPendingView.onMessageDeliveredEvent(markMultipleMessagesAsReadEvent.getUserId());

              deliveryPendingView.onMessageReadEvent(markMultipleMessagesAsReadEvent.getUserId());
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
    isometrik.getRealtimeEventsListenerManager()
        .getMessageListenerManager()
        .addListener(messageEventCallback);
  }

  @Override
  public void unregisterMessageEventListener() {
    isometrik.getRealtimeEventsListenerManager()
        .getMessageListenerManager()
        .removeListener(messageEventCallback);
  }
}