package io.isometrik.ui.messages.media.gifs;

import io.isometrik.ui.messages.media.gifstickerutils.Constants;
import io.isometrik.ui.messages.media.gifstickerutils.GiphyUtil;
import java.util.ArrayList;

/**
 * The presenter to fetch list of gifs in a category, with paging and
 * search.
 */
public class GifsPresenter implements GifsContract.Presenter {

  /**
   * Instantiates a new Gifs presenter.
   */
  GifsPresenter() {
    giphyUtil = new GiphyUtil();
  }

  private GifsContract.View gifsView;
  private final GiphyUtil giphyUtil;
  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private String searchText = "";
  /**
   * The Page size.
   */
  int PAGE_SIZE = Constants.GIFS_STICKERS_PAGE_SIZE;
  @Override
  public void attachView(GifsContract.View gifsView) {
    this.gifsView = gifsView;
  }

  @Override
  public void detachView() {
   gifsView = null;
  }

  @Override
  public void fetchGifsCategories() {
    if (gifsView != null) {
      gifsView.onGifsCategoriesFetchedSuccessfully(GifCategoriesRepository.getGifCategories());
    }
  }

  @Override
  public void fetchGifsInACategory(String categoryId, int limit, int offset) {

    if (categoryId.equals(GifCategoriesRepository.GifCategoryNameEnum.Featured.getValue())) {
      isLoading = true;
      if (offset == 0) {
        isLastPage = false;
        searchText ="";
      }
      giphyUtil.fetchTrendingGifs(limit, offset, (var1, var2) -> {
        isLoading = false;
        if (gifsView != null) {
          if (var1 != null) {

            int size = var1.size();
            if (size > 0) {
              if (size < limit) {

                isLastPage = true;
              }
              gifsView.onGifsFetchedInCategory(categoryId, var1, offset == 0);
            } else {
              if (offset == 0) {
                gifsView.onGifsFetchedInCategory(categoryId, new ArrayList<>(), true);
              } else {
                isLastPage = true;
              }
            }
          } else {

            gifsView.onError(var2.getErrorMessage());
          }
        }
      });
    }
  }

  @Override
  public void searchGifsInACategory(String categoryId, String searchText, int limit, int offset) {
    if (categoryId.equals(GifCategoriesRepository.GifCategoryNameEnum.Featured.getValue())) {
      isLoading = true;
      if (offset == 0) {
        isLastPage = false;
        this.offset = 0;
        this.searchText = searchText;
      }
      giphyUtil.searchGifs(searchText, limit, offset, (var1, var2) -> {
        isLoading = false;
        if (gifsView != null) {
          if (var1 != null) {
            int size = var1.size();
            if (size > 0) {
              if (size < limit) {

                isLastPage = true;
              }
              gifsView.onGifsSearchResultsFetchedInCategory(categoryId, var1, offset == 0);
            } else {
              if (offset == 0) {
                gifsView.onGifsSearchResultsFetchedInCategory(categoryId, new ArrayList<>(), true);
              } else {
                isLastPage = true;
              }
            }
          } else {

            gifsView.onError(var2.getErrorMessage());
          }
        }
      });
    }
  }

  @Override
  public void fetchGifsOnScroll(String categoryId, int firstVisibleItemPosition,
      int visibleItemCount, int totalItemCount) {
    if (categoryId.equals(GifCategoriesRepository.GifCategoryNameEnum.Featured.getValue())) {
      if (!isLoading && !isLastPage) {


        if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
            && firstVisibleItemPosition >= 0
            && (totalItemCount) >= PAGE_SIZE) {

          offset++;
          if (searchText.isEmpty()) {
            fetchGifsInACategory(categoryId, PAGE_SIZE, offset * PAGE_SIZE);
          } else {
            searchGifsInACategory(categoryId, searchText, PAGE_SIZE, offset * PAGE_SIZE);
          }
        }
      }
    }
  }
}
