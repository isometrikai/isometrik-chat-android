package io.isometrik.chat.listeners;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.callbacks.ReactionEventCallback;
import io.isometrik.chat.events.reaction.AddReactionEvent;
import io.isometrik.chat.events.reaction.RemoveReactionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The helper class to add/remove listeners which are announced on reaction realtime events and
 * to announce added listeners on reaction events for add reaction and remove reaction.
 */
public class ReactionListenerManager {
  private final List<ReactionEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Reaction listener manager.
   *
   * @param isometrikInstance the isometrik instance
   */
  public ReactionListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the listener
   */
  public void addListener(ReactionEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the listener
   */
  public void removeListener(ReactionEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  private List<ReactionEventCallback> getListeners() {
    List<ReactionEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * Announce add reaction event.
   *
   * @param addReactionEvent the add reaction event
   */
  public void announce(AddReactionEvent addReactionEvent) {
    for (ReactionEventCallback reactionEventCallback : getListeners()) {
      reactionEventCallback.reactionAdded(this.isometrik, addReactionEvent);
    }
  }

  /**
   * Announce remove reaction event.
   *
   * @param removeReactionEvent the remove reaction event
   */
  public void announce(RemoveReactionEvent removeReactionEvent) {
    for (ReactionEventCallback reactionEventCallback : getListeners()) {
      reactionEventCallback.reactionRemoved(this.isometrik, removeReactionEvent);
    }
  }
}
