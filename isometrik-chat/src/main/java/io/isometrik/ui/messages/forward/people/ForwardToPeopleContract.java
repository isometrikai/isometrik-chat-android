package io.isometrik.ui.messages.forward.people;

import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface forward to people contract containing presenter and view interfaces implemented
 * by ForwardToPeoplePresenter and ForwardToPeopleFragment respectively.
 */
public interface ForwardToPeopleContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<ForwardToPeopleContract.View> {
    /**
     * Request users data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void fetchPeople(int offset, boolean refreshRequest, boolean isSearchRequest, String searchTag);

    /**
     * Request people on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchPeopleOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On people fetched successfully.
     *
     * @param peopleModels the people models
     * @param refreshRequest the refresh request
     * @param isSearchRequest the is search request
     */
    void onPeopleFetchedSuccessfully(ArrayList<PeopleModel> peopleModels, boolean refreshRequest, boolean isSearchRequest);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
