package io.isometrik.chat.response.conversation.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the details of the message config.
 */
public class Config {

  @SerializedName("typingEvents")
  @Expose
  private boolean typingEvents;
  @SerializedName("readEvents")
  @Expose
  private boolean readEvents;
  @SerializedName("pushNotifications")
  @Expose
  private boolean pushNotifications;

  /**
   * Is typing events boolean.
   *
   * @return the boolean
   */
  public boolean isTypingEvents() {
    return typingEvents;
  }

  /**
   * Is read events boolean.
   *
   * @return the boolean
   */
  public boolean isReadEvents() {
    return readEvents;
  }

  /**
   * Is push notifications boolean.
   *
   * @return the boolean
   */
  public boolean isPushNotifications() {
    return pushNotifications;
  }
}
