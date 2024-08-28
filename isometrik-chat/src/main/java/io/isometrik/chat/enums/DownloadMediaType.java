package io.isometrik.chat.enums;

/**
 * The enum for the download media types.
 */
public enum DownloadMediaType {

  /**
   * Image download media type.
   */
  //Audio/video/photo/file
  Image(0),
  /**
   * Video download media type.
   */
  Video(1),
  /**
   * Audio download media type.
   */
  Audio(2),
  /**
   * File download media type.
   */
  File(3),
  /**
   * Sticker download media type.
   */
  Sticker(4),
  /**
   * Gif download media type.
   */
  Gif(5),
  /**
   * Whiteboard download media type.
   */
  Whiteboard(6);

  private final int value;

  DownloadMediaType(int value) {
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
