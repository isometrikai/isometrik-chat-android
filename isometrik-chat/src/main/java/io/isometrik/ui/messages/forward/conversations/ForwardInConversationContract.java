package io.isometrik.ui.messages.forward.conversations;

import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface forward in conversation contract containing presenter and view interfaces
 * implemented by ForwardInConversationPresenter and ForwardInConversationFragment respectively.
 */
public interface ForwardInConversationContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<ForwardInConversationContract.View> {

    /**
     * Fetch conversations.
     *
     * @param skip the skip
     * @param onScroll the on scroll
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    void fetchConversations(int skip, boolean onScroll, boolean isSearchRequest, String searchTag);

    /**
     * Request conversations on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchConversationsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On conversations fetched successfully.
     *
     * @param conversationsModels the conversations models
     * @param resultsOnScroll the results on scroll
     * @param isSearchRequest the is search request
     */
    void onConversationsFetchedSuccessfully(
        ArrayList<ForwardInConversationModel> conversationsModels, boolean resultsOnScroll,
        boolean isSearchRequest);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
