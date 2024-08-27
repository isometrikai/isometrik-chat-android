package io.isometrik.ui.messages.media.gifs;

/**
 * The helper class for inflating items in the list of gifs.
 */
public class GifsModel {

  private final String gifImageUrl;
  private final String gifStillUrl;

  private final String gifName;

  /**
   * Instantiates a new Gifs model.
   *
   * @param gifImageUrl the gif image url
   * @param gifName the gif name
   * @param gifStillUrl the gif still url
   */
  public GifsModel(String gifImageUrl, String gifName, String gifStillUrl) {
    this.gifImageUrl = gifImageUrl;
    this.gifName = gifName;
    this.gifStillUrl = gifStillUrl;
  }

  /**
   * Gets gif image url.
   *
   * @return the gif image url
   */
  public String getGifImageUrl() {
    return gifImageUrl;
  }

  /**
   * Gets gif name.
   *
   * @return the gif name
   */
  public String getGifName() {
    return gifName;
  }

  /**
   * Gets gif still url.
   *
   * @return the gif still url
   */
  public String getGifStillUrl() {
    return gifStillUrl;
  }
}
