package io.isometrik.ui.messages.chat.utils.enums;

/**
 * The enum Remote message types.
 */
public enum RemoteMessageTypes {
  /**
   * Normal message remote message types.
   */
  NormalMessage(0),
  /**
   * Forwarded message remote message types.
   */
  ForwardedMessage(1),
  /**
   * Reply message remote message types.
   */
  ReplyMessage(2),
  /**
   * Admin message remote message types.
   */
  AdminMessage(3);

  private final int value;

  RemoteMessageTypes(int value) {
    this.value = value;
  }

  /**
   * Gets value.
   *
   * @return the value
   */
  public final int getValue() {
    return this.value;
  }
}
