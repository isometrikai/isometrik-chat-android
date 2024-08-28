package io.isometrik.ui.messages.deliverystatus;

import io.isometrik.chat.events.message.delivery.MarkMessageAsDeliveredEvent;
import io.isometrik.chat.events.message.delivery.MarkMessageAsReadEvent;
import io.isometrik.chat.events.message.delivery.MarkMultipleMessagesAsReadEvent;
import io.isometrik.chat.response.user.utils.DeliveredToOrReadByUser;
import io.isometrik.chat.response.user.utils.PendingDeliveryToReadByUser;
import io.isometrik.chat.utils.TimeUtil;

/**
 * The helper class for inflating items in the list of users to whom delivery/read is complete or
 * pending.
 */
public class UsersModel {

  private final String userId;
  private final String userName;
  private final boolean isOnline;
  private final String userProfilePic;
  private String deliveredOrReadAt;

  /**
   * Instantiates a new Users model.
   *
   * @param user the user
   */
  public UsersModel(PendingDeliveryToReadByUser user) {

    userId = user.getUserId();
    userName = user.getUserName();
    isOnline = user.isOnline();
    userProfilePic = user.getUserProfileImageUrl();
  }

  /**
   * Instantiates a new Users model.
   *
   * @param user the user
   */
  public UsersModel(DeliveredToOrReadByUser user) {

    userId = user.getUserId();
    userName = user.getUserName();
    isOnline = user.isOnline();
    userProfilePic = user.getUserProfileImageUrl();
    deliveredOrReadAt = TimeUtil.formatTimestampToOnlyDate(user.getTimestamp());
  }

  /**
   * Instantiates a new Users model.
   *
   * @param markMessageAsDeliveredEvent the mark message as delivered event
   */
  public UsersModel(MarkMessageAsDeliveredEvent markMessageAsDeliveredEvent) {

    userId = markMessageAsDeliveredEvent.getUserId();
    userName = markMessageAsDeliveredEvent.getUserName();
    isOnline = true;
    userProfilePic = markMessageAsDeliveredEvent.getUserProfileImageUrl();
    deliveredOrReadAt = TimeUtil.formatTimestampToOnlyDate(
        markMessageAsDeliveredEvent.getSentAt());
  }

  /**
   * Instantiates a new Users model.
   *
   * @param markMessageAsReadEvent the mark message as read event
   */
  public UsersModel(MarkMessageAsReadEvent markMessageAsReadEvent) {

    userId = markMessageAsReadEvent.getUserId();
    userName = markMessageAsReadEvent.getUserName();
    isOnline = true;
    userProfilePic = markMessageAsReadEvent.getUserProfileImageUrl();
    deliveredOrReadAt =
        TimeUtil.formatTimestampToOnlyDate(markMessageAsReadEvent.getSentAt());
  }

  /**
   * Instantiates a new Users model.
   *
   * @param markMultipleMessagesAsReadEvent the mark multiple messages as read event
   */
  public UsersModel(MarkMultipleMessagesAsReadEvent markMultipleMessagesAsReadEvent) {

    userId = markMultipleMessagesAsReadEvent.getUserId();
    userName = markMultipleMessagesAsReadEvent.getUserName();
    isOnline = true;
    userProfilePic = markMultipleMessagesAsReadEvent.getUserProfileImageUrl();
    deliveredOrReadAt = TimeUtil.formatTimestampToOnlyDate(
        markMultipleMessagesAsReadEvent.getSentAt());
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Is online boolean.
   *
   * @return the boolean
   */
  public boolean isOnline() {
    return isOnline;
  }

  /**
   * Gets user profile pic.
   *
   * @return the user profile pic
   */
  public String getUserProfilePic() {
    return userProfilePic;
  }

  /**
   * Gets delivered or read at.
   *
   * @return the delivered or read at
   */
  public String getDeliveredOrReadAt() {
    return deliveredOrReadAt;
  }
}
