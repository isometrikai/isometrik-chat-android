package io.isometrik.ui.messages.media.gifs;

import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface gifs contract containing presenter and view interfaces implemented
 * by GifsPresenter and GifsFragment respectively.
 */
public interface GifsContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<GifsContract.View> {

    /**
     * Fetch gifs categories.
     */
    void fetchGifsCategories();

    /**
     * Fetch gifs in a category.
     *
     * @param categoryId the category id
     * @param limit the limit
     * @param offset the offset
     */
    void fetchGifsInACategory(String categoryId, int limit, int offset);

    /**
     * Search gifs in a category.
     *
     * @param categoryId the category id
     * @param searchText the search text
     * @param limit the limit
     * @param offset the offset
     */
    void searchGifsInACategory(String categoryId, String searchText, int limit, int offset);

    /**
     * Fetch gifs on scroll.
     *
     * @param categoryId the category id
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchGifsOnScroll(String categoryId, int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On gifs categories fetched successfully.
     *
     * @param gifsCategoryModels the gifs category models
     */
    void onGifsCategoriesFetchedSuccessfully(ArrayList<GifsCategoryModel> gifsCategoryModels);

    /**
     * On gifs fetched in category.
     *
     * @param categoryId the category id
     * @param gifsModels the gifs models
     * @param notOnScroll the not on scroll
     */
    void onGifsFetchedInCategory(String categoryId, ArrayList<GifsModel> gifsModels,
        boolean notOnScroll);

    /**
     * On gifs search results fetched in category.
     *
     * @param categoryId the category id
     * @param gifsModels the gifs models
     * @param notOnScroll the not on scroll
     */
    void onGifsSearchResultsFetchedInCategory(String categoryId, ArrayList<GifsModel> gifsModels,
        boolean notOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message
     */
    void onError(String errorMessage);
  }
}
