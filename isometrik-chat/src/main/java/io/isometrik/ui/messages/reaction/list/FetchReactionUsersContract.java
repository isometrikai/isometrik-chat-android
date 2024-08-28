package io.isometrik.ui.messages.reaction.list;

import io.isometrik.ui.messages.reaction.add.ReactionModel;
import io.isometrik.chat.utils.BasePresenter;

import java.util.ArrayList;

/**
 * The interface fetch reaction users contract containing presenter and view interfaces implemented
 * by FetchReactionUsersPresenter and FetchReactionUsersFragment respectively.
 */
public interface FetchReactionUsersContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<FetchReactionUsersContract.View> {

    /**
     * Fetch reaction users.
     *
     * @param conversationId the conversation id
     * @param messageId the message id
     * @param reactionType the reaction type
     * @param offset the offset
     * @param pageSize the page size
     * @param refreshRequest the refresh request
     */
    void fetchReactionUsers(String conversationId, String messageId, String reactionType,
        int offset, int pageSize, boolean refreshRequest);

    /**
     * Remove reaction.
     *
     * @param conversationId the conversation id
     * @param messageId the message id
     * @param reactionType the reaction type
     * @param reactionUserPosition the reaction user position
     */
    void removeReaction(String conversationId, String messageId, String reactionType,
        int reactionUserPosition);

    /**
     * Request reaction users data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestReactionUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Verify user position in list int.
     *
     * @param reactionUserPosition the reaction user position
     * @param reactionUsers the reaction users
     * @return the int
     */
    int verifyUserPositionInList(int reactionUserPosition,
        ArrayList<ReactionUsersModel> reactionUsers);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On reactions fetched successfully.
     *
     * @param reactionUsers the reaction users
     * @param latestUsers the latest users
     */
    void onReactionsFetchedSuccessfully(ArrayList<ReactionUsersModel> reactionUsers,
        boolean latestUsers);

    /**
     * On reaction removed successfully.
     *
     * @param reactionUserPosition the reaction user position
     * @param messageId the message id
     * @param reactionModel the reaction model
     */
    void onReactionRemovedSuccessfully(int reactionUserPosition, String messageId,
        ReactionModel reactionModel);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
