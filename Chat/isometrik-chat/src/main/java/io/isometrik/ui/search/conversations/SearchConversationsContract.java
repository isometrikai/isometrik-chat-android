package io.isometrik.ui.search.conversations;

import io.isometrik.ui.conversations.list.ConversationsModel;
import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface search conversations contract containing presenter and view interfaces implemented
 * by SearchConversationsPresenter and SearchConversationsFragment respectively.
 */
public interface SearchConversationsContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<SearchConversationsContract.View> {

    /**
     * Fetch searched conversations.
     *
     * @param skip the skip
     * @param onScroll the on scroll
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    void fetchSearchedConversations(int skip, boolean onScroll, boolean isSearchRequest,
        String searchTag);

    /**
     * Request searched conversations on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchSearchedConversationsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On searched conversations fetched successfully.
     *
     * @param conversations the conversations
     * @param resultsOnScroll the results on scroll
     */
    void onSearchedConversationsFetchedSuccessfully(ArrayList<ConversationsModel> conversations,
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
