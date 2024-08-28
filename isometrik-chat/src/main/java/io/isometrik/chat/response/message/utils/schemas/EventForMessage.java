package io.isometrik.chat.response.message.utils.schemas;

/**
 * The helper class for schema of the events for message.
 */
public class EventForMessage {

  private final Boolean sendPushNotification;
  private final Boolean updateUnreadCount;

  /**
   * Instantiates a new Event for message.
   *
   * @param sendPushNotification the send push notification
   * @param updateUnreadCount the update unread count
   */
  public EventForMessage(Boolean sendPushNotification, Boolean updateUnreadCount) {
    this.sendPushNotification = sendPushNotification;
    this.updateUnreadCount = updateUnreadCount;
  }

  /**
   * Gets send push notification.
   *
   * @return the send push notification
   */
  public Boolean getSendPushNotification() {
    return sendPushNotification;
  }

  /**
   * Gets update unread count.
   *
   * @return the update unread count
   */
  public Boolean getUpdateUnreadCount() {
    return updateUnreadCount;
  }
}