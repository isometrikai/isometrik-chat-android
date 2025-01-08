package io.isometrik.chat.utils.enums;

/**
 * The enum Message types for ui.
 */
public enum MessageTypesForUI {

  /**
   * Text sent message types for ui.
   */
  TextSent(0),

  /**
   * Photo sent message types for ui.
   */
  PhotoSent(1),

  /**
   * Video sent message types for ui.
   */
  VideoSent(2),

  /**
   * Audio sent message types for ui.
   */
  AudioSent(3),

  /**
   * File sent message types for ui.
   */
  FileSent(4),

  /**
   * Sticker sent message types for ui.
   */
  StickerSent(5),

  /**
   * Gif sent message types for ui.
   */
  GifSent(6),

  /**
   * Whiteboard sent message types for ui.
   */
  WhiteboardSent(7),

  /**
   * Location sent message types for ui.
   */
  LocationSent(8),

  /**
   * Contact sent message types for ui.
   */
  ContactSent(9),

  /**
   * Text received message types for ui.
   */
  TextReceived(10),

  /**
   * Photo received message types for ui.
   */
  PhotoReceived(11),

  /**
   * Video received message types for ui.
   */
  VideoReceived(12),

  /**
   * Audio received message types for ui.
   */
  AudioReceived(13),

  /**
   * File received message types for ui.
   */
  FileReceived(14),

  /**
   * Sticker received message types for ui.
   */
  StickerReceived(15),

  /**
   * Gif received message types for ui.
   */
  GifReceived(16),

  /**
   * Whiteboard received message types for ui.
   */
  WhiteboardReceived(17),

  /**
   * Location received message types for ui.
   */
  LocationReceived(18),

  /**
   * Contact received message types for ui.
   */
  ContactReceived(19),

  /**
   * Conversation action message message types for ui.
   */
  ConversationActionMessage(20),

  /**
   * Replay sent message types for ui.
   */
  ReplaySent(21),
  /**
   * Replay received message types for ui.
   */
  ReplayReceived(22),
  /**
   * post sent message types for ui.
   */
  PostSent(23),
  /**
   * post received message types for ui.
   */
  PostReceived(24),
  /**
   * post sent record types for ui.
   */
  PostRecord(25),
  /**
   * Camera photo sent message types for ui.
   */
  CameraPhoto(26),
  /**
   * Record Video sent message types for ui.
   */
  RecordVideo(27);

  private final int value;

  MessageTypesForUI(int value) {
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
