package io.isometrik.ui.messages.media.stickers;

import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface stickers contract containing presenter and view interfaces implemented
 * by StickersPresenter and StickersFragment respectively.
 */
public interface StickersContract {
  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<StickersContract.View> {

    /**
     * Fetch stickers categories.
     */
    void fetchStickersCategories();

    /**
     * Fetch stickers in a category.
     *
     * @param categoryId the category id
     * @param limit the limit
     * @param offset the offset
     */
    void fetchStickersInACategory(String categoryId, int limit, int offset);

    /**
     * Search stickers in a category.
     *
     * @param categoryId the category id
     * @param searchText the search text
     * @param limit the limit
     * @param offset the offset
     */
    void searchStickersInACategory(String categoryId, String searchText, int limit, int offset);

    /**
     * Fetch stickers on scroll.
     *
     * @param categoryId the category id
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchStickersOnScroll(String categoryId, int firstVisibleItemPosition,
        int visibleItemCount, int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On stickers categories fetched successfully.
     *
     * @param stickersCategoryModels the stickers category models
     */
    void onStickersCategoriesFetchedSuccessfully(
        ArrayList<StickersCategoryModel> stickersCategoryModels);

    /**
     * On stickers fetched in category.
     *
     * @param categoryId the category id
     * @param stickersModels the stickers models
     * @param notOnScroll the not on scroll
     */
    void onStickersFetchedInCategory(String categoryId, ArrayList<StickersModel> stickersModels,
        boolean notOnScroll);

    /**
     * On stickers search results fetched in category.
     *
     * @param categoryId the category id
     * @param stickersModels the stickers models
     * @param notOnScroll the not on scroll
     */
    void onStickersSearchResultsFetchedInCategory(String categoryId,
        ArrayList<StickersModel> stickersModels, boolean notOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message
     */
    void onError(String errorMessage);
  }
}
