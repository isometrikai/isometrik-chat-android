package io.isometrik.ui.messages.media.stickers;

import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import java.util.ArrayList;

/**
 * The local repository to mimick fetching of sticker categories from a remote source.Code in this
 * utility class can eventually be replaced with an api call to fetch list of sticker categories.
 */
public class StickerCategoriesRepository {

  /**
   * The enum Classic stickers enum.
   */
  public enum ClassicStickersEnum {

    /**
     * Sticker url image 1 classic stickers enum.
     */
    STICKER_URL_IMAGE1(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263305/gifts/classic/2bd5d14d1b5416f175954cecc3a8292d175422_3x_oas4vp.png"),

    /**
     * Sticker url image 2 classic stickers enum.
     */
    STICKER_URL_IMAGE2(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263289/gifts/classic/9e348e184ec5eb9945d6db77aebc5555150138_3x_fzhxki.png"),

    /**
     * Sticker url image 3 classic stickers enum.
     */
    STICKER_URL_IMAGE3(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263270/gifts/classic/53f6757a8d320f344140915d0bc22557200319_3x_ycqulc.png"),

    /**
     * Sticker url image 4 classic stickers enum.
     */
    STICKER_URL_IMAGE4(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263246/gifts/classic/16bd9cc19976ff2736ea7bad9687ba0d180436_3x_iodz9v.png"),

    /**
     * Sticker url image 5 classic stickers enum.
     */
    STICKER_URL_IMAGE5(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263224/gifts/classic/ic_gift_cruise_3x_pwxjnm.png"),

    /**
     * Sticker url image 6 classic stickers enum.
     */
    STICKER_URL_IMAGE6(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263224/gifts/classic/ic_gift_pearl_3x_ynp83y.png"),

    /**
     * Sticker url image 7 classic stickers enum.
     */
    STICKER_URL_IMAGE7(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263223/gifts/classic/b9dd261987c4416443614ad7393c950d192133_3x_koiaja.png"),

    /**
     * Sticker url image 8 classic stickers enum.
     */
    STICKER_URL_IMAGE8(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263223/gifts/classic/686853fd18e63bececceb5de997f54c3200358_3x_yy6xqn.png"),

    /**
     * Sticker url image 9 classic stickers enum.
     */
    STICKER_URL_IMAGE9(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263220/gifts/classic/f26b02a5063572d39c6c0699c9f5728b151930_3x_xrc1w8.png"),

    /**
     * Sticker url image 10 classic stickers enum.
     */
    STICKER_URL_IMAGE10(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263215/gifts/classic/bf4ca53bdba84d365e44cc395d46b574151931_3x_ap08p3.png"),

    /**
     * Sticker url image 11 classic stickers enum.
     */
    STICKER_URL_IMAGE11(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263215/gifts/classic/d3aec3407458b0e356065da0469e5590165307_3x_lsjbi3.png"),

    /**
     * Sticker url image 12 classic stickers enum.
     */
    STICKER_URL_IMAGE12(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263214/gifts/classic/26f60d715eb72e67f2f23e58f48f2206170618_3x_lmqoln.png"),

    /**
     * Sticker url image 13 classic stickers enum.
     */
    STICKER_URL_IMAGE13(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263213/gifts/classic/61bbd740b6c81bb485dfe3ea2e78d5f3171326_3x_qgew6k.png"),

    /**
     * Sticker url image 14 classic stickers enum.
     */
    STICKER_URL_IMAGE14(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263213/gifts/classic/43e9269adaa1db036103d54a6b8c674f191832_3x_ccsvgd.png"),

    /**
     * Sticker url image 15 classic stickers enum.
     */
    STICKER_URL_IMAGE15(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263213/gifts/classic/afb35ae8ab955002b915a1908f309ad4164719_3x_xqwnpj.png"),

    /**
     * Sticker url image 16 classic stickers enum.
     */
    STICKER_URL_IMAGE16(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1600263213/gifts/classic/2e1bab78b4ed86271abc5d2d6b8a338b192025_3x_q8nkcl.png"),

    /**
     * Sticker url image 17 classic stickers enum.
     */
    STICKER_URL_IMAGE17(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449538/gifts/images/wkyytl.png"),

    /**
     * Sticker url image 18 classic stickers enum.
     */
    STICKER_URL_IMAGE18(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449350/gifts/images/mgjyj3.png"),

    /**
     * Sticker url image 19 classic stickers enum.
     */
    STICKER_URL_IMAGE19(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449318/gifts/images/lsnq6u.png"),

    /**
     * Sticker url image 20 classic stickers enum.
     */
    STICKER_URL_IMAGE20(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449252/gifts/images/qlftf5.png"),

    /**
     * Sticker url image 21 classic stickers enum.
     */
    STICKER_URL_IMAGE21(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449207/gifts/images/sjzrzo.png"),

    /**
     * Sticker url image 22 classic stickers enum.
     */
    STICKER_URL_IMAGE22(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449167/gifts/images/pnrki9.png"),

    /**
     * Sticker url image 23 classic stickers enum.
     */
    STICKER_URL_IMAGE23(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449120/gifts/images/moytp4.png"),

    /**
     * Sticker url image 24 classic stickers enum.
     */
    STICKER_URL_IMAGE24(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449102/gifts/images/pnl0im.png"),

    /**
     * Sticker url image 25 classic stickers enum.
     */
    STICKER_URL_IMAGE25(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449045/gifts/images/ahvenx.png"),

    /**
     * Sticker url image 26 classic stickers enum.
     */
    STICKER_URL_IMAGE26(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449031/gifts/images/cqwimq.png"),

    /**
     * Sticker url image 27 classic stickers enum.
     */
    STICKER_URL_IMAGE27(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449019/gifts/images/o5obw0.png"),

    /**
     * Sticker url image 28 classic stickers enum.
     */
    STICKER_URL_IMAGE28(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589449007/gifts/images/cmaatk.png"),

    /**
     * Sticker url image 29 classic stickers enum.
     */
    STICKER_URL_IMAGE29(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448977/gifts/images/x5b0nx.png"),

    /**
     * Sticker url image 30 classic stickers enum.
     */
    STICKER_URL_IMAGE30(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448963/gifts/images/h9hu5k.png"),

    /**
     * Sticker url image 31 classic stickers enum.
     */
    STICKER_URL_IMAGE31(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448953/gifts/images/rcbpuz.png"),

    /**
     * Sticker url image 32 classic stickers enum.
     */
    STICKER_URL_IMAGE32(
        "https://res.cloudinary.com/dedibgpdw/image/upload/v1589448938/gifts/images/kzou81.png");

    private final String value;

    ClassicStickersEnum(String value) {
      this.value = value;
    }

    private String getValue() {
      return this.value;
    }
  }

  private enum StickerCategoryAssetsEnum {

    /**
     * Classic sticker category assets enum.
     */
    Classic(R.drawable.ism_ic_gif_sticker_category_classic),
    /**
     * Featured sticker category assets enum.
     */
    Featured(R.drawable.ism_ic_gif_sticker_category_featured);

    private final int value;

    StickerCategoryAssetsEnum(int value) {
      this.value = value;
    }

    private int getValue() {
      return this.value;
    }
  }

  /**
   * The enum Sticker category name enum.
   */
  enum StickerCategoryNameEnum {

    /**
     * Classic sticker category name enum.
     */
    Classic("Classic"),
    /**
     * Featured sticker category name enum.
     */
    Featured("Featured");

    private final String value;

    StickerCategoryNameEnum(String value) {
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

  private static ArrayList<StickersModel> getClassicStickers() {
    ArrayList<StickersModel> stickersModels = new ArrayList<>();
    int i = 0;
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE1.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE2.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE3.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE4.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE5.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE6.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE7.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE8.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE9.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE10.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE11.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE12.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE13.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE14.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE15.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE16.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE17.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE18.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE19.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE20.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE21.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE22.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE23.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE24.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE25.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE26.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE27.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE28.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE29.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE30.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE31.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i++)));
    stickersModels.add(new StickersModel(ClassicStickersEnum.STICKER_URL_IMAGE32.getValue(),
        IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_classic_gif_name, i)));

    return stickersModels;
  }

  /**
   * Gets sticker categories.
   *
   * @return the sticker categories
   */
  public static ArrayList<StickersCategoryModel> getStickerCategories() {

    ArrayList<StickersCategoryModel> stickersCategoryModels = new ArrayList<>();
    stickersCategoryModels.add(new StickersCategoryModel(StickerCategoryNameEnum.Classic.getValue(),
        StickerCategoryAssetsEnum.Classic.getValue(), true, getClassicStickers()));
    stickersCategoryModels.add(
        new StickersCategoryModel(StickerCategoryNameEnum.Featured.getValue(),
            StickerCategoryAssetsEnum.Featured.getValue(), false, null));
    return stickersCategoryModels;
  }
}
