package io.isometrik.ui.conversations.details.observers;

import java.util.ArrayList;

/**
 * The interface observers contract containing presenter and view interfaces implemented
 * by ObserversPresenter and ObserversActivity respectively.
 */
public interface ObserversContract {

  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Fetch observers.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    /*
     * Request observers data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void fetchObservers(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request observers on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchObserversOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On observers fetched successfully.
     *
     * @param observersModels the observers models
     * @param refreshRequest the refresh request
     */
    void onObserversFetchedSuccessfully(ArrayList<ObserversModel> observersModels,
        boolean refreshRequest);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
