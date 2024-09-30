package io.isometrik.ui.messages.media.gifs;

import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import java.util.ArrayList;

/**
 * The local repository to mimick fetching of gif categories from a remote source.Code in this
 * utility class can eventually be replaced with an api call to fetch list of gif categories.
 */
public class GifCategoriesRepository {

  private static ArrayList<GifsModel> getClassicGifs() {
    ArrayList<GifsModel> gifsModels = new ArrayList<>();
    int i = 0;
    gifsModels.add(new GifsModel(ClassicGifsEnum.GIFS_URL_GIF1.getValue(),
        IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++),
        ClassicGifStillsEnum.GIF_STILL_URL_GIF1.getValue()));
    gifsModels.add(new GifsModel(ClassicGifsEnum.GIFS_URL_GIF2.getValue(),
        IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++),
        ClassicGifStillsEnum.GIF_STILL_URL_GIF2.getValue()));
    gifsModels.add(new GifsModel(ClassicGifsEnum.GIFS_URL_GIF3.getValue(),
        IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++),
        ClassicGifStillsEnum.GIF_STILL_URL_GIF3.getValue()));
    gifsModels.add(new GifsModel(ClassicGifsEnum.GIFS_URL_GIF4.getValue(),
        IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++),
        ClassicGifStillsEnum.GIF_STILL_URL_GIF4.getValue()));
    gifsModels.add(new GifsModel(ClassicGifsEnum.GIFS_URL_GIF5.getValue(),
        IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i),
        ClassicGifStillsEnum.GIF_STILL_URL_GIF5.getValue()));

    return gifsModels;
  }

  /**
   * Gets gif categories.
   *
   * @return the gif categories
   */
  public static ArrayList<GifsCategoryModel> getGifCategories() {

    ArrayList<GifsCategoryModel> gifsCategoryModels = new ArrayList<>();
    gifsCategoryModels.add(new GifsCategoryModel(GifCategoryNameEnum.Classic.getValue(),
        GifCategoryAssetsEnum.Classic.getValue(), true, getClassicGifs()));
    gifsCategoryModels.add(new GifsCategoryModel(GifCategoryNameEnum.Featured.getValue(),
        GifCategoryAssetsEnum.Featured.getValue(), false, null));
    return gifsCategoryModels;
  }

  private enum ClassicGifsEnum {

    /**
     * Gifs url gif 1 classic gifs enum.
     */
    GIFS_URL_GIF1(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448241/gifts/gifs/urcu7q.gif"),

    /**
     * Gifs url gif 2 classic gifs enum.
     */
    GIFS_URL_GIF2(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448217/gifts/gifs/vmovga.gif"),

    /**
     * Gifs url gif 3 classic gifs enum.
     */
    GIFS_URL_GIF3(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448159/gifts/gifs/b1goou.gif"),

    /**
     * Gifs url gif 4 classic gifs enum.
     */
    GIFS_URL_GIF4(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448130/gifts/gifs/j2v6uu.gif"),

    /**
     * Gifs url gif 5 classic gifs enum.
     */
    GIFS_URL_GIF5(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448105/gifts/gifs/pd6dqi.gif");

    private final String value;

    ClassicGifsEnum(String value) {
      this.value = value;
    }

    private String getValue() {
      return this.value;
    }
  }

  private enum ClassicGifStillsEnum {

    /**
     * Gif still url gif 1 classic gif stills enum.
     */
    GIF_STILL_URL_GIF1(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448241/gifts/gifs/urcu7q.jpg"),

    /**
     * Gif still url gif 2 classic gif stills enum.
     */
    GIF_STILL_URL_GIF2(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448217/gifts/gifs/vmovga.jpg"),

    /**
     * Gif still url gif 3 classic gif stills enum.
     */
    GIF_STILL_URL_GIF3(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448159/gifts/gifs/b1goou.jpg"),

    /**
     * Gif still url gif 4 classic gif stills enum.
     */
    GIF_STILL_URL_GIF4(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448130/gifts/gifs/j2v6uu.jpg"),

    /**
     * Gif still url gif 5 classic gif stills enum.
     */
    GIF_STILL_URL_GIF5(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448105/gifts/gifs/pd6dqi.jpg");

    private final String value;

    ClassicGifStillsEnum(String value) {
      this.value = value;
    }

    private String getValue() {
      return this.value;
    }
  }

  private enum GifCategoryAssetsEnum {

    /**
     * Classic gif category assets enum.
     */
    Classic(R.drawable.ism_ic_gif_sticker_category_classic),
    /**
     * Featured gif category assets enum.
     */
    Featured(R.drawable.ism_ic_gif_sticker_category_featured);

    private final int value;

    GifCategoryAssetsEnum(int value) {
      this.value = value;
    }

    private int getValue() {
      return this.value;
    }
  }

  /**
   * The enum Gif category name enum.
   */
  enum GifCategoryNameEnum {

    /**
     * Classic gif category name enum.
     */
    Classic("Classic"),
    /**
     * Featured gif category name enum.
     */
    Featured("Featured");

    private final String value;

    GifCategoryNameEnum(String value) {
      this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    String getValue() {
      return this.value;
    }
  }
}
