package io.isometrik.ui.messages.media.stickers;

import java.util.ArrayList;

/**
 * The helper class for inflating items in the list of sticker categories.
 */
public class StickersCategoryModel {

  private final String stickerCategoryName;
  private final int stickerCategoryImage;
  private boolean selected;
  private ArrayList<StickersModel> stickers;

  /**
   * Instantiates a new Stickers category model.
   *
   * @param stickerCategoryName the sticker category name
   * @param stickerCategoryImage the sticker category image
   * @param selected the selected
   * @param stickers the stickers
   */
  StickersCategoryModel(String stickerCategoryName, int stickerCategoryImage, boolean selected,
      ArrayList<StickersModel> stickers) {
    this.stickerCategoryName = stickerCategoryName;
    this.stickerCategoryImage = stickerCategoryImage;
    if (stickers != null) this.stickers = stickers;
    this.selected = selected;
  }

  /**
   * Sets stickers.
   *
   * @param stickers the stickers
   */
  public void setStickers(ArrayList<StickersModel> stickers) {
    this.stickers = stickers;
  }

  /**
   * Sets selected.
   *
   * @param selected the selected
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * Gets sticker category name.
   *
   * @return the sticker category name
   */
  public String getStickerCategoryName() {
    return stickerCategoryName;
  }

  /**
   * Gets sticker category image.
   *
   * @return the sticker category image
   */
  public int getStickerCategoryImage() {
    return stickerCategoryImage;
  }

  /**
   * Is selected boolean.
   *
   * @return the boolean
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Gets stickers.
   *
   * @return the stickers
   */
  public ArrayList<StickersModel> getStickers() {
    return stickers;
  }
}


