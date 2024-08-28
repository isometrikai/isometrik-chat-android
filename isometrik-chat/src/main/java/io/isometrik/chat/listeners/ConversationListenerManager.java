package io.isometrik.chat.listeners;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.callbacks.ConversationEventCallback;
import io.isometrik.chat.events.conversation.CreateConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.ClearConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.DeleteConversationLocallyEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationDetailsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationImageEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationSettingsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationTitleEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The helper class to add/remove listeners which are announced on conversation realtime events and
 * to announce added listeners on conversation events for clear conversation, delete conversation
 * locally, update conversation image, update conversation settings, update conversation details,
 * update conversation title and create conversation.
 */
public class ConversationListenerManager {

  private final List<ConversationEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Conversation listener manager.
   *
   * @param isometrikInstance the isometrik instance
   */
  public ConversationListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the listener
   */
  public void addListener(ConversationEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the listener
   */
  public void removeListener(ConversationEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  private List<ConversationEventCallback> getListeners() {
    List<ConversationEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * Announce clear conversation event.
   *
   * @param clearConversationEvent the clear conversation event
   */
  public void announce(ClearConversationEvent clearConversationEvent) {
    for (ConversationEventCallback conversationEventCallback : getListeners()) {
      conversationEventCallback.conversationCleared(this.isometrik, clearConversationEvent);
    }
  }

  /**
   * Announce delete conversation locally event.
   *
   * @param deleteConversationLocallyEvent the delete conversation locally event
   */
  public void announce(DeleteConversationLocallyEvent deleteConversationLocallyEvent) {
    for (ConversationEventCallback conversationEventCallback : getListeners()) {
      conversationEventCallback.conversationDeletedLocally(this.isometrik,
          deleteConversationLocallyEvent);
    }
  }

  /**
   * Announce update conversation image event.
   *
   * @param updateConversationImageEvent the update conversation image event
   */
  public void announce(UpdateConversationImageEvent updateConversationImageEvent) {
    for (ConversationEventCallback conversationEventCallback : getListeners()) {
      conversationEventCallback.conversationImageUpdated(this.isometrik,
          updateConversationImageEvent);
    }
  }

  /**
   * Announce update conversation settings event.
   *
   * @param updateConversationSettingsEvent the update conversation settings event
   */
  public void announce(UpdateConversationSettingsEvent updateConversationSettingsEvent) {
    for (ConversationEventCallback conversationEventCallback : getListeners()) {
      conversationEventCallback.conversationSettingsUpdated(this.isometrik,
          updateConversationSettingsEvent);
    }
  }

  /**
   * Announce update conversation details event.
   *
   * @param updateConversationDetailsEvent the update conversation details event
   */
  public void announce(UpdateConversationDetailsEvent updateConversationDetailsEvent) {
    for (ConversationEventCallback conversationEventCallback : getListeners()) {
      conversationEventCallback.conversationDetailsUpdated(this.isometrik,
          updateConversationDetailsEvent);
    }
  }

  /**
   * Announce update conversation title event.
   *
   * @param updateConversationTitleEvent the update conversation title event
   */
  public void announce(UpdateConversationTitleEvent updateConversationTitleEvent) {
    for (ConversationEventCallback conversationEventCallback : getListeners()) {
      conversationEventCallback.conversationTitleUpdated(this.isometrik,
          updateConversationTitleEvent);
    }
  }

  /**
   * Announce create conversation event.
   *
   * @param createConversationEvent the create conversation event
   */
  public void announce(CreateConversationEvent createConversationEvent) {
    for (ConversationEventCallback conversationEventCallback : getListeners()) {
      conversationEventCallback.conversationCreated(this.isometrik, createConversationEvent);
    }
  }
}
