package io.isometrik.chat.listeners;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.callbacks.ConnectionEventCallback;
import io.isometrik.chat.events.connection.ConnectEvent;
import io.isometrik.chat.events.connection.ConnectionFailedEvent;
import io.isometrik.chat.events.connection.DisconnectEvent;
import io.isometrik.chat.response.error.IsometrikError;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Connection listener manager.
 */
public class ConnectionListenerManager {
  private final List<ConnectionEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Connection listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.chat.Isometrik
   */
  public ConnectionListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the ConnectionEventCallback{@link io.isometrik.chat.callbacks.ConnectionEventCallback}
   * listener to be added
   * @see io.isometrik.chat.callbacks.ConnectionEventCallback
   */
  public void addListener(ConnectionEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the ConnectionEventCallback{@link io.isometrik.chat.callbacks.ConnectionEventCallback}
   * listener to be added
   * @see io.isometrik.chat.callbacks.ConnectionEventCallback
   */
  public void removeListener(ConnectionEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of ConnectionEventCallback{@link io.isometrik.chat.callbacks.ConnectionEventCallback}
   * listeners currently registered
   * @see io.isometrik.chat.callbacks.ConnectionEventCallback
   */
  private List<ConnectionEventCallback> getListeners() {
    List<ConnectionEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a ConnectEvent to listeners.
   *
   * @param connectEvent ConnectEvent{@link io.isometrik.chat.events.connection.ConnectEvent} which
   * will be broadcast to listeners.
   * @see io.isometrik.chat.events.connection.ConnectEvent
   */
  public void announce(ConnectEvent connectEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.connected(this.isometrik, connectEvent);
    }
  }

  /**
   * announce a DisconnectEvent to listeners.
   *
   * @param disconnectEvent DisconnectEvent{@link io.isometrik.chat.events.connection.DisconnectEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.chat.events.connection.DisconnectEvent
   */
  public void announce(DisconnectEvent disconnectEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.disconnected(this.isometrik, disconnectEvent);
    }
  }

  /**
   * announce a ConnectionFailedEvent to listeners.
   *
   * @param connectionFailedEvent ConnectionFailedEvent{@link io.isometrik.chat.events.connection.ConnectionFailedEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.chat.events.connection.ConnectionFailedEvent
   */
  public void announce(ConnectionFailedEvent connectionFailedEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.failedToConnect(this.isometrik, connectionFailedEvent);
    }
  }

  /**
   * announce a IsometrikError to listeners.
   *
   * @param isometrikError IsometrikError{@link io.isometrik.chat.response.error.IsometrikError}
   * which will be broadcast to listeners.
   * @see io.isometrik.chat.response.error.IsometrikError
   */
  public void announce(IsometrikError isometrikError) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.connectionFailed(this.isometrik, isometrikError);
    }
  }
}
