package io.isometrik.chat.enums;

/**
 * The enum for the attachment schema types.
 */
public enum AttachmentSchemaType {

  /**
   * Media attachment schema type.
   */
  //Audio/video/photo/file
  Media(0),

  /**
   * Location attachment schema type.
   */
  Location(1),

  /**
   * Gif sticker attachment schema type.
   */
  GifSticker(2);

  private final int value;

  AttachmentSchemaType(int value) {
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
