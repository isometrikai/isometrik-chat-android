package io.isometrik.chat.enums;

/**
 * The enum for the presigned url media types.
 */
public enum PresignedUrlMediaTypes {
  /**
   * Photo presigned url media types.
   */
  Photo(0),
  /**
   * Video presigned url media types.
   */
  Video(1),
  /**
   * Audio presigned url media types.
   */
  Audio(2),
  /**
   * File presigned url media types.
   */
  File(3);
  private final int value;

  PresignedUrlMediaTypes(int value) {
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
