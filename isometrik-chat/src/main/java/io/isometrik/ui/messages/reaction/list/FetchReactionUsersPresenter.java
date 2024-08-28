package io.isometrik.ui.messages.reaction.list;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.reaction.FetchReactionsQuery;
import io.isometrik.chat.builder.reaction.RemoveReactionQuery;
import io.isometrik.chat.response.reaction.FetchReactionsResult;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.ui.messages.reaction.add.ReactionModel;
import io.isometrik.ui.messages.reaction.util.ReactionUtil;
import io.isometrik.chat.utils.Constants;

import java.util.ArrayList;

/**
 * The presenter to fetch list of users who have added a particular reaction to a message to, with
 * paging and pull to refresh option.Api call to remove reaction added by logged in user.
 */
public class FetchReactionUsersPresenter implements FetchReactionUsersContract.Presenter {

  /**
   * Instantiates a new Fetch reaction users presenter.
   *
   * @param conversationId the conversation id
   * @param messageId the message id
   * @param reactionType the reaction type
   */
  FetchReactionUsersPresenter(String conversationId, String messageId, String reactionType) {
    this.conversationId = conversationId;
    this.messageId = messageId;
    this.reactionType = reactionType;
  }

  private FetchReactionUsersContract.View fetchReactionUsersView;
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();
  private final String conversationId, messageId, reactionType;

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;

  @Override
  public void attachView(FetchReactionUsersContract.View fetchReactionUsersView) {
    this.fetchReactionUsersView = fetchReactionUsersView;
  }

  @Override
  public void detachView() {
   fetchReactionUsersView = null;
  }

  @Override
  public void fetchReactionUsers(String conversationId, String messageId, String reactionType,
      int offset, int pageSize, boolean refreshRequest) {

    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }

    isometrik.getRemoteUseCases().getReactionUseCases().fetchReactions(new FetchReactionsQuery.Builder().setLimit(pageSize)
        .setSkip(offset)
        .setConversationId(conversationId)
        .setMessageId(messageId)
        .setReactionType(reactionType)
        .setUserToken(userToken)
        .build(), (var1, var2) -> {

      isLoading = false;

      if (var1 != null) {

        ArrayList<ReactionUsersModel> reactionUsersModels = new ArrayList<>();
        ArrayList<FetchReactionsResult.UserReaction> userReactions = var1.getUserReactions();
        int size = userReactions.size();
        String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();
        for (int i = 0; i < size; i++) {
          FetchReactionsResult.UserReaction userReaction = userReactions.get(i);
          reactionUsersModels.add(new ReactionUsersModel(userId.equals(userReaction.getUserId()),
              userReaction.isOnline(), userReaction.getUserName(),
              userReaction.getUserProfileImageUrl()));
        }
        if (size < pageSize) {

          isLastPage = true;
        }
        if (fetchReactionUsersView != null) {
          fetchReactionUsersView.onReactionsFetchedSuccessfully(reactionUsersModels,
              refreshRequest);
        }
      } else {
        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

          if (refreshRequest) {
            //No user reactions found
            if (fetchReactionUsersView != null) {
              fetchReactionUsersView.onReactionsFetchedSuccessfully(new ArrayList<>(), true);
            }
          } else {
            isLastPage = true;
          }
        } else {
          if (fetchReactionUsersView != null) {
            fetchReactionUsersView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  @Override
  public void removeReaction(String conversationId, String messageId, String reactionType,
      int reactionUserPosition) {

    isometrik.getRemoteUseCases().getReactionUseCases().removeReaction(new RemoveReactionQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .setMessageId(messageId)
        .setReactionType(reactionType)
        .build(), (var1, var2) -> {
      if (var1 != null) {
        if (fetchReactionUsersView != null) {
          ReactionModel reactionModel =
              ReactionUtil.parseAddOrRemoveReactionEvent(reactionType, var1.getReactionsCount());

          fetchReactionUsersView.onReactionRemovedSuccessfully(reactionUserPosition, messageId,
              reactionModel);
        }
      } else {
        if (fetchReactionUsersView != null) fetchReactionUsersView.onError(var2.getErrorMessage());
      }
    });
  }

  @Override
  public void requestReactionUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {

      int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchReactionUsers(conversationId, messageId, reactionType, offset * PAGE_SIZE, PAGE_SIZE,
            false);
      }
    }
  }

  @Override
  public int verifyUserPositionInList(int reactionUserPosition,
      ArrayList<ReactionUsersModel> reactionUsers) {
    int position = -1;

    for (int i = 0; i < reactionUsers.size(); i++) {
      if (reactionUsers.get(i).isReactionAddedBySelf()) {
        return i;
      }
    }
    return position;
  }
}