package io.isometrik.ui.messages.media.gifstickerutils;

import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.ui.messages.media.gifs.GifsModel;
import io.isometrik.ui.messages.media.stickers.StickersModel;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to fetch trending or search for stickers and gifs from Giphy.
 */
public class GiphyUtil {
  private final OkHttpClient client;
  private final HttpUrl.Builder searchGifsRequestBuilder;
  private final HttpUrl.Builder trendingGifsRequestBuilder;
  private final HttpUrl.Builder searchStickersRequestBuilder;
  private final HttpUrl.Builder trendingStickersRequestBuilder;

  /**
   * Instantiates a new Giphy util.
   */
  public GiphyUtil() {
    client = new OkHttpClient();

    searchGifsRequestBuilder = HttpUrl.parse(Constants.GIPHY_SEARCH_GIFS_ENDPOINT).newBuilder();
    searchGifsRequestBuilder.addQueryParameter("api_key", Constants.GIPHY_API_KEY);

    trendingGifsRequestBuilder = HttpUrl.parse(Constants.GIPHY_TRENDING_GIFS_ENDPOINT).newBuilder();
    trendingGifsRequestBuilder.addQueryParameter("api_key", Constants.GIPHY_API_KEY);

    searchStickersRequestBuilder =
        HttpUrl.parse(Constants.GIPHY_SEARCH_STICKERS_ENDPOINT).newBuilder();
    searchStickersRequestBuilder.addQueryParameter("api_key", Constants.GIPHY_API_KEY);

    trendingStickersRequestBuilder =
        HttpUrl.parse(Constants.GIPHY_TRENDING_STICKERS_ENDPOINT).newBuilder();
    trendingStickersRequestBuilder.addQueryParameter("api_key", Constants.GIPHY_API_KEY);
  }

  /**
   * Search gifs.
   *
   * @param searchText the search text
   * @param limit the limit
   * @param offset the offset
   * @param completionHandler the completion handler
   */
  public void searchGifs(String searchText, int limit, int offset,
      CompletionHandler<ArrayList<GifsModel>> completionHandler) {

    searchGifsRequestBuilder.removeAllQueryParameters("limit")
        .removeAllQueryParameters("offset")
        .removeAllQueryParameters("q");

    searchGifsRequestBuilder.addQueryParameter("limit", String.valueOf(limit))
        .addQueryParameter("offset", String.valueOf(offset))
        .addQueryParameter("q", searchText);

    parseResponse(new Request.Builder().url(searchGifsRequestBuilder.build().toString()).build(),
        completionHandler, null);
  }

  /**
   * Search stickers.
   *
   * @param searchText the search text
   * @param limit the limit
   * @param offset the offset
   * @param completionHandler the completion handler
   */
  public void searchStickers(String searchText, int limit, int offset,
      CompletionHandler<ArrayList<StickersModel>> completionHandler) {
    searchStickersRequestBuilder.removeAllQueryParameters("limit")
        .removeAllQueryParameters("offset")
        .removeAllQueryParameters("q");

    searchStickersRequestBuilder.addQueryParameter("limit", String.valueOf(limit))
        .addQueryParameter("offset", String.valueOf(offset))
        .addQueryParameter("q", searchText);

    parseResponse(
        new Request.Builder().url(searchStickersRequestBuilder.build().toString()).build(), null,
        completionHandler);
  }

  /**
   * Fetch trending gifs.
   *
   * @param limit the limit
   * @param offset the offset
   * @param completionHandler the completion handler
   */
  public void fetchTrendingGifs(int limit, int offset,
      CompletionHandler<ArrayList<GifsModel>> completionHandler) {

    trendingGifsRequestBuilder.removeAllQueryParameters("limit")
        .removeAllQueryParameters("offset");

    trendingGifsRequestBuilder.addQueryParameter("limit", String.valueOf(limit))
        .addQueryParameter("offset", String.valueOf(offset));


    parseResponse(new Request.Builder().url(trendingGifsRequestBuilder.build().toString()).build(),
        completionHandler, null);
  }

  /**
   * Fetch trending stickers.
   *
   * @param limit the limit
   * @param offset the offset
   * @param completionHandler the completion handler
   */
  public void fetchTrendingStickers(int limit, int offset,
      CompletionHandler<ArrayList<StickersModel>> completionHandler) {

    trendingStickersRequestBuilder.removeAllQueryParameters("limit")
        .removeAllQueryParameters("offset");

    trendingStickersRequestBuilder.addQueryParameter("limit", String.valueOf(limit))
        .addQueryParameter("offset", String.valueOf(offset));

    parseResponse(
        new Request.Builder().url(trendingStickersRequestBuilder.build().toString()).build(), null,
        completionHandler);
  }

  private void parseResponse(Request request,
      CompletionHandler<ArrayList<GifsModel>> gifsCompletionHandler,
      CompletionHandler<ArrayList<StickersModel>> stickersCompletionHandler) {
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        if (gifsCompletionHandler != null) {
          gifsCompletionHandler.onComplete(null,
              new IsometrikError.Builder().setIsometrikErrorCode(141)
                  .setErrorMessage(e.getMessage())
                  .setRemoteError(true)
                  .build());
        } else {
          stickersCompletionHandler.onComplete(null,
              new IsometrikError.Builder().setIsometrikErrorCode(141)
                  .setErrorMessage(e.getMessage())
                  .setRemoteError(true)
                  .build());
        }
      }

      @Override
      public void onResponse(@NotNull Call call, final @NotNull Response response) {
        if (!response.isSuccessful()) {
          if (gifsCompletionHandler != null) {
            gifsCompletionHandler.onComplete(null,
                new IsometrikError.Builder().setHttpResponseCode(response.code())
                    .setErrorMessage(response.message())
                    .setRemoteError(true)
                    .build());
          } else {
            stickersCompletionHandler.onComplete(null,
                new IsometrikError.Builder().setHttpResponseCode(response.code())
                    .setErrorMessage(response.message())
                    .setRemoteError(true)
                    .build());
          }
        } else {

          if (gifsCompletionHandler != null) {

            ArrayList<GifsModel> gifsModels = new ArrayList<>();
            try {

              JSONObject gifsResponse = new JSONObject(response.body().string());
              JSONArray gifsArray = gifsResponse.getJSONArray("data");

              for (int i = 0; i < gifsArray.length(); i++) {
                JSONObject data = gifsArray.getJSONObject(i);
                JSONObject images = data.getJSONObject("images");

                gifsModels.add(new GifsModel(images.getJSONObject("fixed_width").getString("url"),
                    data.getString("title"),
                    images.getJSONObject("fixed_width_still").getString("url")));
              }
            } catch (JSONException | IOException | NullPointerException ignore) {

            }
            gifsCompletionHandler.onComplete(gifsModels, null);
          } else {
            ArrayList<StickersModel> stickersModels = new ArrayList<>();
            try {
              JSONObject stickersResponse = new JSONObject(response.body().string());
              JSONArray stickersArray = stickersResponse.getJSONArray("data");

              for (int i = 0; i < stickersArray.length(); i++) {
                JSONObject data = stickersArray.getJSONObject(i);
                JSONObject images = data.getJSONObject("images");

                stickersModels.add(
                    new StickersModel(images.getJSONObject("fixed_width_still").getString("url"),
                        data.getString("title")));
              }
            } catch (JSONException | IOException | NullPointerException ignore) {

            }
            stickersCompletionHandler.onComplete(stickersModels, null);
          }
        }
      }
    });
  }
}
