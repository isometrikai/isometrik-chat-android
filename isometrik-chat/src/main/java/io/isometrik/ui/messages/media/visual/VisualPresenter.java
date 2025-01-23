package io.isometrik.ui.messages.media.visual;

import java.util.ArrayList;

import io.isometrik.ui.messages.media.gifs.GifCategoriesRepository;
import io.isometrik.ui.messages.media.gifstickerutils.Constants;
import io.isometrik.ui.messages.media.gifstickerutils.GiphyUtil;
import io.isometrik.ui.messages.media.stickers.StickerCategoriesRepository;

/**
 * The presenter to fetch list of stickers in a category, with paging and
 * search.
 */
public class VisualPresenter implements VisualContract.Presenter {

    /**
     * Instantiates a new Stickers presenter.
     */
    VisualPresenter() {
        giphyUtil = new GiphyUtil();
    }

    private VisualContract.View visualView;
    private final GiphyUtil giphyUtil;
    private int offset;
    private boolean isLastPage;
    private boolean isLoadingStickers;
    private boolean isLoadingGif;
    private String searchText = "";
    /**
     * The Page size.
     */
    int PAGE_SIZE = Constants.GIFS_STICKERS_PAGE_SIZE;

    @Override
    public void attachView(VisualContract.View visualView) {
        this.visualView = visualView;
    }

    @Override
    public void detachView() {
        visualView = null;
    }

    @Override
    public void fetchStickersCategories() {
        if (visualView != null) {
            visualView.onStickersCategoriesFetchedSuccessfully(
                    StickerCategoriesRepository.getStickerCategories());
        }
    }

    @Override
    public void fetchStickersInACategory(String categoryId, int limit, int offset) {
        if (isLoadingStickers) {
            return;
        }
        if (categoryId.equals(StickerCategoriesRepository.StickerCategoryNameEnum.Featured.getValue())) {
            isLoadingStickers = true;
            if (offset == 0) {
                isLastPage = false;
                searchText = "";
            }
            giphyUtil.fetchTrendingStickers(limit, offset, (var1, var2) -> {
                isLoadingStickers = false;
                if (visualView != null) {
                    if (var1 != null) {
                        int size = var1.size();
                        if (size > 0) {
                            if (size < limit) {

                                isLastPage = true;
                            }
                            visualView.onStickersFetchedInCategory(categoryId, var1, offset == 0);
                        } else {
                            if (offset == 0) {
                                visualView.onStickersFetchedInCategory(categoryId, new ArrayList<>(), true);
                            } else {
                                isLastPage = true;
                            }
                        }
                    } else {

                        visualView.onError(var2.getErrorMessage());
                    }
                }
            });
        }
    }

    @Override
    public void searchStickersInACategory(String categoryId, String searchText, int limit,
                                          int offset) {
        if (isLoadingStickers) {
            return;
        }
        if (categoryId.equals(StickerCategoriesRepository.StickerCategoryNameEnum.Featured.getValue())) {
            isLoadingStickers = true;
            if (offset == 0) {
                isLastPage = false;
                this.offset = 0;
                this.searchText = searchText;
            }
            giphyUtil.searchStickers(searchText, limit, offset, (var1, var2) -> {
                isLoadingStickers = false;
                if (visualView != null) {
                    if (var1 != null) {
                        int size = var1.size();
                        if (size > 0) {
                            if (size < limit) {

                                isLastPage = true;
                            }
                            visualView.onStickersSearchResultsFetchedInCategory(categoryId, var1, offset == 0);
                        } else {
                            if (offset == 0) {
                                visualView.onStickersSearchResultsFetchedInCategory(categoryId, new ArrayList<>(),
                                        true);
                            } else {
                                isLastPage = true;
                            }
                        }
                    } else {

                        visualView.onError(var2.getErrorMessage());
                    }
                }
            });
        }
    }

    @Override
    public void fetchStickersOnScroll(String categoryId, int firstVisibleItemPosition,
                                      int visibleItemCount, int totalItemCount) {
        if (categoryId.equals(StickerCategoriesRepository.StickerCategoryNameEnum.Featured.getValue())) {
            if (!isLoadingStickers && !isLastPage) {
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


    @Override
    public void fetchGifsCategories() {
        if (visualView != null) {
            visualView.onGifsCategoriesFetchedSuccessfully(GifCategoriesRepository.getGifCategories());
        }
    }

    @Override
    public void fetchGifsInACategory(String categoryId, int limit, int offset) {
        if (isLoadingGif) {
            return;
        }
        if (categoryId.equals(GifCategoriesRepository.GifCategoryNameEnum.Featured.getValue())) {
            isLoadingGif = true;
            if (offset == 0) {
                isLastPage = false;
                searchText = "";
            }
            giphyUtil.fetchTrendingGifs(limit, offset, (var1, var2) -> {
                isLoadingGif = false;
                if (visualView != null) {
                    if (var1 != null) {

                        int size = var1.size();
                        if (size > 0) {
                            if (size < limit) {

                                isLastPage = true;
                            }
                            visualView.onGifsFetchedInCategory(categoryId, var1, offset == 0);
                        } else {
                            if (offset == 0) {
                                visualView.onGifsFetchedInCategory(categoryId, new ArrayList<>(), true);
                            } else {
                                isLastPage = true;
                            }
                        }
                    } else {

                        visualView.onError(var2.getErrorMessage());
                    }
                }
            });
        }
    }

    @Override
    public void searchGifsInACategory(String categoryId, String searchText, int limit, int offset) {
        if (isLoadingGif) {
            return;
        }
        if (categoryId.equals(GifCategoriesRepository.GifCategoryNameEnum.Featured.getValue())) {
            isLoadingGif = true;
            if (offset == 0) {
                isLastPage = false;
                this.offset = 0;
                this.searchText = searchText;
            }
            giphyUtil.searchGifs(searchText, limit, offset, (var1, var2) -> {
                isLoadingGif = false;
                if (visualView != null) {
                    if (var1 != null) {
                        int size = var1.size();
                        if (size > 0) {
                            if (size < limit) {

                                isLastPage = true;
                            }
                            visualView.onGifsSearchResultsFetchedInCategory(categoryId, var1, offset == 0);
                        } else {
                            if (offset == 0) {
                                visualView.onGifsSearchResultsFetchedInCategory(categoryId, new ArrayList<>(), true);
                            } else {
                                isLastPage = true;
                            }
                        }
                    } else {

                        visualView.onError(var2.getErrorMessage());
                    }
                }
            });
        }
    }

    @Override
    public void fetchGifsOnScroll(String categoryId, int firstVisibleItemPosition,
                                  int visibleItemCount, int totalItemCount) {
        if (categoryId.equals(GifCategoriesRepository.GifCategoryNameEnum.Featured.getValue())) {
            if (!isLoadingGif && !isLastPage) {


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
