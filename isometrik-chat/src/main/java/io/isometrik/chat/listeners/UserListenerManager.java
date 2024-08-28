package io.isometrik.chat.listeners;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.callbacks.UserEventCallback;
import io.isometrik.chat.events.user.DeleteUserEvent;
import io.isometrik.chat.events.user.UpdateUserEvent;
import io.isometrik.chat.events.user.block.BlockUserEvent;
import io.isometrik.chat.events.user.block.UnblockUserEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The helper class to add/remove listeners which are announced on user realtime events and
 * to announce added listeners on user events for block, unblock, update and delete user.
 */
public class UserListenerManager {
  private final List<UserEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new User listener manager.
   *
   * @param isometrikInstance the isometrik instance
   */
  public UserListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the listener
   */
  public void addListener(UserEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the listener
   */
  public void removeListener(UserEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  private List<UserEventCallback> getListeners() {
    List<UserEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * Announce block user event.
   *
   * @param blockUserEvent the block user event
   */
  public void announce(BlockUserEvent blockUserEvent) {
    for (UserEventCallback userEventCallback : getListeners()) {
      userEventCallback.userBlocked(this.isometrik, blockUserEvent);
    }
  }

  /**
   * Announce unblock user event.
   *
   * @param unblockUserEvent the unblock user event
   */
  public void announce(UnblockUserEvent unblockUserEvent) {
    for (UserEventCallback userEventCallback : getListeners()) {
      userEventCallback.userUnblocked(this.isometrik, unblockUserEvent);
    }
  }

  /**
   * Announce update user event.
   *
   * @param updateUserEvent the update user event
   */
  public void announce(UpdateUserEvent updateUserEvent) {
    for (UserEventCallback userEventCallback : getListeners()) {
      userEventCallback.userUpdated(this.isometrik, updateUserEvent);
    }
  }

  /**
   * Announce delete user event.
   *
   * @param deleteUserEvent the delete user event
   */
  public void announce(DeleteUserEvent deleteUserEvent) {
    for (UserEventCallback userEventCallback : getListeners()) {
      userEventCallback.userDeleted(this.isometrik, deleteUserEvent);
    }
  }
}
