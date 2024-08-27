package io.isometrik.ui.utils;

/**
 * The helper class to generate notification id for FCM notifications.
 */
public class NotificationUtil {

  /**
   * Gets notification id.
   *
   * @param conversationId the conversation id
   * @return the notification id
   */
  public static int getNotificationId(String conversationId) {
    return Math.abs(Integer.parseInt(conversationId.substring(0, 8), 16));
  }
}
