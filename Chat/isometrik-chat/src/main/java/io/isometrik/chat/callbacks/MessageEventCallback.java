package io.isometrik.chat.callbacks;

import io.isometrik.chat.Isometrik;
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
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class for the message event callback, with methods for new message received,
 * message
 * marked as read/delivered, multiple messages read, message deleted for self/everyone, message
 * updated, user blocked/unblocked in 1-1 conversation, user typing message, last read by a user in
 * conversation updated events.
 */
public abstract class MessageEventCallback {

  /**
   * Messages removed for everyone.
   *
   * @param isometrik the isometrik
   * @param removeMessagesForEveryoneEvent the remove messages for everyone event
   */
  public abstract void messagesRemovedForEveryone(@NotNull Isometrik isometrik,
      @NotNull RemoveMessagesForEveryoneEvent removeMessagesForEveryoneEvent);

  /**
   * Messages removed for self.
   *
   * @param isometrik the isometrik
   * @param removeMessagesForSelfEvent the remove messages for self event
   */
  public abstract void messagesRemovedForSelf(@NotNull Isometrik isometrik,
      @NotNull RemoveMessagesForSelfEvent removeMessagesForSelfEvent);

  /**
   * Message marked as delivered.
   *
   * @param isometrik the isometrik
   * @param markMessageAsDeliveredEvent the mark message as delivered event
   */
  public abstract void messageMarkedAsDelivered(@NotNull Isometrik isometrik,
      @NotNull MarkMessageAsDeliveredEvent markMessageAsDeliveredEvent);

  /**
   * Message marked as read.
   *
   * @param isometrik the isometrik
   * @param markMessageAsReadEvent the mark message as read event
   */
  public abstract void messageMarkedAsRead(@NotNull Isometrik isometrik,
      @NotNull MarkMessageAsReadEvent markMessageAsReadEvent);

  /**
   * Multiple messages marked as read.
   *
   * @param isometrik the isometrik
   * @param markMultipleMessagesAsReadEvent the mark multiple messages as read event
   */
  public abstract void multipleMessagesMarkedAsRead(@NotNull Isometrik isometrik,
      @NotNull MarkMultipleMessagesAsReadEvent markMultipleMessagesAsReadEvent);

  /**
   * Updated last read in conversation event.
   *
   * @param isometrik the isometrik
   * @param updatedLastReadInConversationEvent the updated last read in conversation event
   */
  public abstract void updatedLastReadInConversationEvent(@NotNull Isometrik isometrik,
      @NotNull UpdatedLastReadInConversationEvent updatedLastReadInConversationEvent);

  /**
   * Message sent.
   *
   * @param isometrik the isometrik
   * @param sendMessageEvent the send message event
   */
  public abstract void messageSent(@NotNull Isometrik isometrik,
      @NotNull SendMessageEvent sendMessageEvent);

  /**
   * Typing message sent.
   *
   * @param isometrik the isometrik
   * @param sendTypingMessageEvent the send typing message event
   */
  public abstract void typingMessageSent(@NotNull Isometrik isometrik,
      @NotNull SendTypingMessageEvent sendTypingMessageEvent);

  /**
   * Blocked user in conversation.
   *
   * @param isometrik the isometrik
   * @param blockUserInConversationEvent the block user in conversation event
   */
  public abstract void blockedUserInConversation(@NotNull Isometrik isometrik,
      @NotNull BlockUserInConversationEvent blockUserInConversationEvent);

  /**
   * Unblocked user in conversation.
   *
   * @param isometrik the isometrik
   * @param unblockUserInConversationEvent the unblock user in conversation event
   */
  public abstract void unblockedUserInConversation(@NotNull Isometrik isometrik,
      @NotNull UnblockUserInConversationEvent unblockUserInConversationEvent);

  /**
   * Message details updated.
   *
   * @param isometrik the isometrik
   * @param updateMessageDetailsEvent the update message details event
   */
  public abstract void messageDetailsUpdated(@NotNull Isometrik isometrik,
      @NotNull UpdateMessageDetailsEvent updateMessageDetailsEvent);
}
