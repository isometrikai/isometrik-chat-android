package io.isometrik.ui.messages.media.stickers;

import io.isometrik.ui.messages.media.gifstickerutils.Constants;
import io.isometrik.ui.messages.media.gifstickerutils.GiphyUtil;

import java.util.ArrayList;

/**
 * The presenter to fetch list of stickers in a category, with paging and
 * search.
 */
public class StickersPresenter implements StickersContract.Presenter {

  /**
   * Instantiates a new Stickers presenter.
   */
  StickersPresenter() {
    giphyUtil = new GiphyUtil();
  }

  private StickersContract.View stickersView;
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
  public void attachView(StickersContract.View stickersView) {
    this.stickersView = stickersView;
  }

  @Override
  public void detachView() {
    stickersView = null;
  }

  @Override
  public void fetchStickersCategories() {
    if (stickersView != null) {
      stickersView.onStickersCategoriesFetchedSuccessfully(
          StickerCategoriesRepository.getStickerCategories());
    }
  }

  @Override
  public void fetchStickersInACategory(String categoryId, int limit, int offset) {

    if (categoryId.equals(StickerCategoriesRepository.StickerCategoryNameEnum.Featured.getValue())) {
      isLoading = true;
      if (offset == 0) {
        isLastPage = false;
        searchText = "";
      }
      giphyUtil.fetchTrendingStickers(limit, offset, (var1, var2) -> {
        isLoading = false;
        if (stickersView != null) {
          if (var1 != null) {
            int size = var1.size();
            if (size > 0) {
              if (size < limit) {

                isLastPage = true;
              }
              stickersView.onStickersFetchedInCategory(categoryId, var1, offset == 0);
            } else {
              if (offset == 0) {
                stickersView.onStickersFetchedInCategory(categoryId, new ArrayList<>(), true);
              } else {
                isLastPage = true;
              }
            }
          } else {

            stickersView.onError(var2.getErrorMessage());
          }
        }
      });
    }
  }

  @Override
  public void searchStickersInACategory(String categoryId, String searchText, int limit,
      int offset) {
    if (categoryId.equals(StickerCategoriesRepository.StickerCategoryNameEnum.Featured.getValue())) {
      isLoading = true;
      if (offset == 0) {
        isLastPage = false;
        this.offset = 0;
        this.searchText = searchText;
      }
      giphyUtil.searchStickers(searchText, limit, offset, (var1, var2) -> {
        isLoading = false;
        if (stickersView != null) {
          if (var1 != null) {
            int size = var1.size();
            if (size > 0) {
              if (size < limit) {

                isLastPage = true;
              }
              stickersView.onStickersSearchResultsFetchedInCategory(categoryId, var1, offset == 0);
            } else {
              if (offset == 0) {
                stickersView.onStickersSearchResultsFetchedInCategory(categoryId, new ArrayList<>(),
                    true);
              } else {
                isLastPage = true;
              }
            }
          } else {

            stickersView.onError(var2.getErrorMessage());
          }
        }
      });
    }
  }

  @Override
  public void fetchStickersOnScroll(String categoryId, int firstVisibleItemPosition,
      int visibleItemCount, int totalItemCount) {
    if (categoryId.equals(StickerCategoriesRepository.StickerCategoryNameEnum.Featured.getValue())) {
      if (!isLoading && !isLastPage) {
        if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
            && firstVisibleItemPosition >= 0
            && (totalItemCount) >= PAGE_SIZE) {

          offset++;
          if (searchText.isEmpty()) {
            fetchStickersInACategory(categoryId, PAGE_SIZE, offset * PAGE_SIZE);
          } else {
            searchStickersInACategory(categoryId, searchText, PAGE_SIZE, offset * PAGE_SIZE);
          }
        }
      }
    }
  }
}
