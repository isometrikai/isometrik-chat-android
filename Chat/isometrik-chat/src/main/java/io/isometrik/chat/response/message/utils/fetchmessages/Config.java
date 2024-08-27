package io.isometrik.chat.response.message.utils.fetchmessages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the details of the conversation configuration.
 */
public class Config {
  @SerializedName("config.pushNotifications")
  @Expose
  private Boolean pushNotifications;
  @SerializedName("config.readEvents")
  @Expose
  private Boolean readEvents;
  @SerializedName("config.typingEvents")
  @Expose
  private Boolean typingEvents;

  /**
   * Gets push notifications.
   *
   * @return the push notifications
   */
  public Boolean getPushNotifications() {
    return pushNotifications;
  }

  /**
   * Gets read events.
   *
   * @return the read events
   */
  public Boolean getReadEvents() {
    return readEvents;
  }

  /**
   * Gets typing events.
   *
   * @return the typing events
   */
  public Boolean getTypingEvents() {
    return typingEvents;
  }
}
