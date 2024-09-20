package io.isometrik.chat.events.connection;

/**
 * The type Connection failed event.
 */
/**
 * The class containing  event details.
 */
public class ConnectionFailedEvent {

  private final String reason;
  private final int errorCode;
  private final Throwable throwable;

  /**
   * Instantiates a new Disconnect event.
   *
   * @param mqttException the cause of the connection drop
   */
  public ConnectionFailedEvent(Exception mqttException) {
    this.errorCode = 0;
    this.reason = mqttException.getMessage();
    this.throwable = mqttException.getCause();
  }

  /**
   * Gets reason.
   *
   * @return the reason
   */
  public String getReason() {
    return reason;
  }

  /**
   * Gets error code.
   *
   * @return the error code
   */
  public int getErrorCode() {
    return errorCode;
  }

  /**
   * Gets throwable.
   *
   * @return the throwable
   */
  public Throwable getThrowable() {
    return throwable;
  }
}
