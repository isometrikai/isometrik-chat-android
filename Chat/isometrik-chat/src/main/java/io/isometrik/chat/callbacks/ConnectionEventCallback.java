package io.isometrik.chat.callbacks;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.events.connection.ConnectEvent;
import io.isometrik.chat.events.connection.ConnectionFailedEvent;
import io.isometrik.chat.events.connection.DisconnectEvent;
import io.isometrik.chat.response.error.IsometrikError;
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class for the connection event callback, with methods for connected made, connection
 * fail and connection drop events.
 */
public abstract class ConnectionEventCallback {

  /**
   * Disconnected.
   *
   * @param isometrik the isometrik instance
   * @param disconnectEvent the disconnect event
   * @see io.isometrik.chat.Isometrik
   * @see io.isometrik.chat.events.connection.DisconnectEvent
   */
  public abstract void disconnected(@NotNull Isometrik isometrik,
      @NotNull DisconnectEvent disconnectEvent);

  /**
   * Connected.
   *
   * @param isometrik the isometrik instance
   * @param connectEvent the connect event
   * @see io.isometrik.chat.Isometrik
   * @see io.isometrik.chat.events.connection.ConnectEvent
   */
  public abstract void connected(@NotNull Isometrik isometrik, @NotNull ConnectEvent connectEvent);

  /**
   * Connection failed.
   *
   * @param isometrik the isometrik instance
   * @param isometrikError the isometrik error instance
   * @see io.isometrik.chat.Isometrik
   * @see io.isometrik.chat.response.error.IsometrikError
   */
  public abstract void connectionFailed(@NotNull Isometrik isometrik,
      @NotNull IsometrikError isometrikError);

  /**
   * Connected.
   *
   * @param isometrik the isometrik instance
   * @param connectionFailedEvent the connection failed event
   * @see io.isometrik.chat.Isometrik
   * @see io.isometrik.chat.events.connection.ConnectionFailedEvent
   */
  public abstract void failedToConnect(@NotNull Isometrik isometrik,
      @NotNull ConnectionFailedEvent connectionFailedEvent);
}
