package io.isometrik.chat.enums;

/**
 * The enum for the conversation types.
 */
public enum ConversationType {

  /**
   * Private conversation conversation type.
   */
  PrivateConversation(0),

  /**
   * Public conversation conversation type.
   */
  PublicConversation(1),

  /**
   * Open conversation conversation type.
   */
  OpenConversation(2),
  /**
   * All conversations conversation type.
   */
  AllConversations(3);
  private final int value;

  ConversationType(int value) {
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
