package io.isometrik.ui.notifications;

import com.google.firebase.messaging.FirebaseMessaging;
import io.isometrik.chat.utils.Constants;

/**
 * The helper class to subscribe/unsubscribe to/from topics for receiving firebase notifications.
 */
public class FirebaseUtils {

  /**
   * Subscribe to topic.
   *
   * @param userId the user id
   */
  public static void subscribeToTopic(String userId) {
    try {
      FirebaseMessaging.getInstance()
          .subscribeToTopic(Constants.NOTIFICATION_TOPIC_PREFIX + userId);
      //.addOnCompleteListener(task -> {
      //
      //});
    } catch (Exception ignore) {
    }
  }

  /**
   * Unsubscribe from topic.
   *
   * @param userId the user id
   */
  public static void unsubscribeFromTopic(String userId) {
    try {
      FirebaseMessaging.getInstance()
          .unsubscribeFromTopic(Constants.NOTIFICATION_TOPIC_PREFIX + userId);
      //.addOnCompleteListener(task -> {
      //});
    } catch (Exception ignore) {
    }
  }
}
