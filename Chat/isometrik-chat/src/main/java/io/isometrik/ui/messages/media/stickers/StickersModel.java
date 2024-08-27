package io.isometrik.ui.messages.media.stickers;

/**
 * The helper class for inflating items in the list of stickers.
 */
public class StickersModel {

  private final String stickerImageUrl;

  private final String stickerName;

  /**
   * Instantiates a new Stickers model.
   *
   * @param stickerImageUrl the sticker image url
   * @param stickerName the sticker name
   */
  public StickersModel(String stickerImageUrl, String stickerName) {
    this.stickerImageUrl = stickerImageUrl;
    this.stickerName = stickerName;
  }

  /**
   * Gets sticker image url.
   *
   * @return the sticker image url
   */
  public String getStickerImageUrl() {
    return stickerImageUrl;
  }

  /**
   * Gets sticker name.
   *
   * @return the sticker name
   */
  public String getStickerName() {
    return stickerName;
  }
}
