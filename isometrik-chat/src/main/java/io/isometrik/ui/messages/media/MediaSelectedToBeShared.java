package io.isometrik.ui.messages.media;

/**
 * The interface media selected to be shared for callback to messages screen of
 * sticker/gif/whiteboard share request.
 */
public interface MediaSelectedToBeShared {

  /**
   * Gif share requested.
   *
   * @param gifName the gif name
   * @param gifImageUrl the gif image url
   * @param gifStillUrl the gif still url
   */
  void gifShareRequested(String gifName, String gifImageUrl, String gifStillUrl);

  /**
   * Sticker share requested.
   *
   * @param stickerName the sticker name
   * @param stickerImageUrl the sticker image url
   */
  void stickerShareRequested(String stickerName, String stickerImageUrl);

  /**
   * Whiteboard share requested.
   *
   * @param whiteboardImageUrl the whiteboard image url
   */
  void whiteboardShareRequested(String whiteboardImageUrl);
}
