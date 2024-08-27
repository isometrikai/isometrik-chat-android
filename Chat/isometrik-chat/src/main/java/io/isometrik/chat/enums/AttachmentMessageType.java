package io.isometrik.chat.enums;

/**
 * The enum for the attachment message types.
 */
public enum AttachmentMessageType {

  /**
   * Image attachment message type.
   */
  //Audio/video/photo/file
  Image(0),
  /**
   * Video attachment message type.
   */
  Video(1),
  /**
   * Audio attachment message type.
   */
  Audio(2),
  /**
   * File attachment message type.
   */
  File(3),
  /**
   * Location attachment message type.
   */
  Location(4),
  /**
   * Sticker attachment message type.
   */
  Sticker(5),
  /**
   * Gif attachment message type.
   */
  Gif(6);

  private final int value;

  AttachmentMessageType(int value) {
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
