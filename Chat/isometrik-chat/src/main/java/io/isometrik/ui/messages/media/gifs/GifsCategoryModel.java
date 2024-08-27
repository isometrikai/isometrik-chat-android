package io.isometrik.ui.messages.media.gifs;

import java.util.ArrayList;

/**
 * The helper class for inflating items in the list of gif categories.
 */
public class GifsCategoryModel {

  private final String gifCategoryName;
  private final int gifCategoryImage;
  private boolean selected;
  private ArrayList<GifsModel> gifs;

  /**
   * Instantiates a new Gifs category model.
   *
   * @param gifCategoryName the gif category name
   * @param gifCategoryImage the gif category image
   * @param selected the selected
   * @param gifs the gifs
   */
  public GifsCategoryModel(String gifCategoryName, int gifCategoryImage, boolean selected,
      ArrayList<GifsModel> gifs) {
    this.gifCategoryName = gifCategoryName;
    this.gifCategoryImage = gifCategoryImage;
    this.selected = selected;
    if (gifs != null) this.gifs = gifs;
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
   * Sets gifs.
   *
   * @param gifs the gifs
   */
  public void setGifs(ArrayList<GifsModel> gifs) {
    this.gifs = gifs;
  }

  /**
   * Gets gif category name.
   *
   * @return the gif category name
   */
  public String getGifCategoryName() {
    return gifCategoryName;
  }

  /**
   * Gets gif category image.
   *
   * @return the gif category image
   */
  public int getGifCategoryImage() {
    return gifCategoryImage;
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
   * Gets gifs.
   *
   * @return the gifs
   */
  public ArrayList<GifsModel> getGifs() {
    return gifs;
  }
}


