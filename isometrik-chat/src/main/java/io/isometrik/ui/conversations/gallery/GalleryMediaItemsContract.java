package io.isometrik.ui.conversations.gallery;

import java.util.ArrayList;

/**
 * The interface gallery media items contract containing presenter and view interfaces implemented
 * by GalleryMediaItemsPresenter and GalleryMediaItemsActivity respectively.
 */
public interface GalleryMediaItemsContract {

  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Initialize.
     *
     * @param conversationId the conversation id
     */
    void initialize(String conversationId);

    /**
     * Fetch gallery media items.
     *
     * @param customType the custom type
     * @param skip the skip
     * @param onScroll the on scroll
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    void fetchGalleryMediaItems(String customType, int skip, boolean onScroll, boolean isSearchRequest,
        String searchTag);

    /**
     * Request gallery media items on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     * @param customType the media type
     */
    void fetchGalleryMediaItemsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount, String customType);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On gallery media items fetched successfully.
     *
     * @param galleryItems the gallery items
     * @param resultsOnScroll the results on scroll
     */
    void onGalleryMediaItemsFetchedSuccessfully(ArrayList<GalleryModel> galleryItems,
        boolean resultsOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message
     */
    void onError(String errorMessage);
  }
}
