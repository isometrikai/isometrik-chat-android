package io.isometrik.ui.messages.mentioned;

import java.util.ArrayList;

/**
 * The interface mentioned messages contract containing presenter and view interfaces implemented
 * by MentionedMessagesPresenter and MentionedMessagesActivity respectively.
 */
public interface MentionedMessagesContract {

  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Fetch mentioned messages.
     *
     * @param skip the skip
     * @param onScroll the on scroll
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    void fetchMentionedMessages(int skip, boolean onScroll, boolean isSearchRequest,
        String searchTag);

    /**
     * Request mentioned messages on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchMentionedMessagesOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On mentioned messages fetched successfully.
     *
     * @param mentionedMessages the mentioned messages
     * @param resultsOnScroll the results on scroll
     */
    void onMentionedMessagesFetchedSuccessfully(ArrayList<MentionedMessagesModel> mentionedMessages,
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
