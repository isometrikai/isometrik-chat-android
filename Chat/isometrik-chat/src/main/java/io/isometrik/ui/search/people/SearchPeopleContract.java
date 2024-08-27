package io.isometrik.ui.search.people;

import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface search people contract containing presenter and view interfaces implemented
 * by SearchPeoplePresenter and SearchPeopleFragment respectively.
 */
public interface SearchPeopleContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<SearchPeopleContract.View> {

    /**
     * Fetch searched people.
     *
     * @param skip the skip
     * @param refreshRequest the refresh request
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    void fetchSearchedPeople(int skip, boolean refreshRequest, boolean isSearchRequest, String searchTag);

    /**
     * Request searched people on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchSearchedPeopleOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On searched people fetched successfully.
     *
     * @param people the people
     * @param resultsOnScroll the results on scroll
     */
    void onSearchedPeopleFetchedSuccessfully(ArrayList<SearchPeopleModel> people,
        boolean resultsOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
