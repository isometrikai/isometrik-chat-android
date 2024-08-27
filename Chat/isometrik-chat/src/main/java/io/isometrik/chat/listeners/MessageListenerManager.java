package io.isometrik.chat.listeners;

import io.isometrik.chat.Isometrik;
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
import java.util.ArrayList;
import java.util.List;

/**
 * The helper class to add/remove listeners which are announced on message realtime events and
 * to announce added listeners on message events for remove messages for everyone, remove messages
 * for self, mark message as delivered,   mark message as read, mark multiple messages as read,
 * updated last read in conversation, send message, send typing message, block user in conversation,
 * unblock user in conversation and update message details.
 */
public class MessageListenerManager {
  private final List<MessageEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Message listener manager.
   *
   * @param isometrikInstance the isometrik instance
   */
  public MessageListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the listener
   */
  public void addListener(MessageEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the listener
   */
  public void removeListener(MessageEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  private List<MessageEventCallback> getListeners() {
    List<MessageEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * Announce remove messages for everyone event.
   *
   * @param removeMessagesForEveryoneEvent the remove messages for everyone event
   */
  public void announce(RemoveMessagesForEveryoneEvent removeMessagesForEveryoneEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messagesRemovedForEveryone(this.isometrik,
          removeMessagesForEveryoneEvent);
    }
  }

  /**
   * Announce remove messages for self event.
   *
   * @param removeMessagesForSelfEvent the remove messages for self event
   */
  public void announce(RemoveMessagesForSelfEvent removeMessagesForSelfEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messagesRemovedForSelf(this.isometrik, removeMessagesForSelfEvent);
    }
  }

  /**
   * Announce mark message as delivered event.
   *
   * @param markMessageAsDeliveredEvent the mark message as delivered event
   */
  public void announce(MarkMessageAsDeliveredEvent markMessageAsDeliveredEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageMarkedAsDelivered(this.isometrik, markMessageAsDeliveredEvent);
    }
  }

  /**
   * Announce mark message as read event.
   *
   * @param markMessageAsReadEvent the mark message as read event
   */
  public void announce(MarkMessageAsReadEvent markMessageAsReadEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageMarkedAsRead(this.isometrik, markMessageAsReadEvent);
    }
  }

  /**
   * Announce mark multiple messages as read event.
   *
   * @param markMultipleMessagesAsReadEvent the mark multiple messages as read event
   */
  public void announce(MarkMultipleMessagesAsReadEvent markMultipleMessagesAsReadEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.multipleMessagesMarkedAsRead(this.isometrik,
          markMultipleMessagesAsReadEvent);
    }
  }

  /**
   * Announce updated last read in conversation event.
   *
   * @param updatedLastReadInConversationEvent the updated last read in conversation event
   */
  public void announce(UpdatedLastReadInConversationEvent updatedLastReadInConversationEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.updatedLastReadInConversationEvent(this.isometrik,
          updatedLastReadInConversationEvent);
    }
  }

  /**
   * Announce send message event.
   *
   * @param sendMessageEvent the send message event
   */
  public void announce(SendMessageEvent sendMessageEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageSent(this.isometrik, sendMessageEvent);
    }
  }

  /**
   * Announce send typing message event.
   *
   * @param sendTypingMessageEvent the send typing message event
   */
  public void announce(SendTypingMessageEvent sendTypingMessageEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.typingMessageSent(this.isometrik, sendTypingMessageEvent);
    }
  }

  /**
   * Announce block user in conversation event.
   *
   * @param blockUserInConversationEvent the block user in conversation event
   */
  public void announce(BlockUserInConversationEvent blockUserInConversationEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.blockedUserInConversation(this.isometrik, blockUserInConversationEvent);
    }
  }

  /**
   * Announce unblock user in conversation event.
   *
   * @param unblockUserInConversationEvent the unblock user in conversation event
   */
  public void announce(UnblockUserInConversationEvent unblockUserInConversationEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.unblockedUserInConversation(this.isometrik,
          unblockUserInConversationEvent);
    }
  }

  /**
   * Announce update message details event.
   *
   * @param updateMessageDetailsEvent the update message details event
   */
  public void announce(UpdateMessageDetailsEvent updateMessageDetailsEvent) {
    for (MessageEventCallback messageEventCallback : getListeners()) {
      messageEventCallback.messageDetailsUpdated(this.isometrik, updateMessageDetailsEvent);
    }
  }
}
