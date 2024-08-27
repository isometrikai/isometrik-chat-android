package io.isometrik.ui.search.messages;

import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface search messages contract containing presenter and view interfaces implemented
 * by SearchMessagesPresenter and SearchMessagesFragment respectively.
 */
public interface SearchMessagesContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<SearchMessagesContract.View> {

    /**
     * Fetch searched messages.
     *
     * @param skip the skip
     * @param onScroll the on scroll
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    void fetchSearchedMessages(int skip, boolean onScroll, boolean isSearchRequest,
        String searchTag);

    /**
     * Request searched messages on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchSearchedMessagesOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On searched messages fetched successfully.
     *
     * @param searchedMessages the searched messages
     * @param resultsOnScroll the results on scroll
     */
    void onSearchedMessagesFetchedSuccessfully(ArrayList<MessagesModel> searchedMessages,
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
