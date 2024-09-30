package io.isometrik.ui.messages.media.gifstickerutils;

import io.isometrik.ui.IsometrikChatSdk;

/**
 * The helper class containing endpoints for sticker and gif apis of Giphy.
 */
public class Constants {

  /**
   * The constant GIPHY_TRENDING_GIFS_ENDPOINT.
   */
  public static final String GIPHY_TRENDING_GIFS_ENDPOINT =
      "https://api.giphy.com/v1/gifs/trending";
  /**
   * The constant GIPHY_TRENDING_STICKERS_ENDPOINT.
   */
  public static final String GIPHY_TRENDING_STICKERS_ENDPOINT =
      "https://api.giphy.com/v1/stickers/trending";
  /**
   * The constant GIPHY_SEARCH_GIFS_ENDPOINT.
   */
  public static final String GIPHY_SEARCH_GIFS_ENDPOINT = "https://api.giphy.com/v1/gifs/search";
  /**
   * The constant GIPHY_SEARCH_STICKERS_ENDPOINT.
   */
  public static final String GIPHY_SEARCH_STICKERS_ENDPOINT =
      "https://api.giphy.com/v1/stickers/search";

  /**
   * The constant GIPHY_API_KEY.
   */
  public static final String GIPHY_API_KEY =
      IsometrikChatSdk.getInstance().getIsometrik().getConfiguration().getGiphyApiKey();

  /**
   * The constant GIFS_STICKERS_PAGE_SIZE.
   */
  public static final int GIFS_STICKERS_PAGE_SIZE = 20;
}
