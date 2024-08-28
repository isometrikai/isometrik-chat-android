package io.isometrik.chat.response.message.utils.validations;

import io.isometrik.chat.response.message.utils.schemas.EventForMessage;

/**
 * The helper class for event for message validation.
 */
public class EventForMessageValidation {

  /**
   * Validate event for message boolean.
   *
   * @param eventForMessage the event for message
   * @return the boolean
   */
  public boolean validateEventForMessage(EventForMessage eventForMessage) {

    return eventForMessage.getSendPushNotification() == null
        || eventForMessage.getUpdateUnreadCount() == null;
  }
}
