package io.isometrik.ui.messages.reaction.add;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.reaction.AddReactionQuery;
import io.isometrik.chat.builder.reaction.FetchReactionsQuery;
import io.isometrik.chat.builder.reaction.RemoveReactionQuery;
import io.isometrik.chat.response.reaction.FetchReactionsResult;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.messages.reaction.util.ReactionUtil;
import io.isometrik.chat.utils.Constants;

/**
 * The presenter to add a reaction of particular type on a message by logged in user.
 */
public class AddReactionPresenter implements AddReactionContract.Presenter {

  /**
   * Instantiates a new Add reaction presenter.
   */
  AddReactionPresenter() {
  }

  private AddReactionContract.View addReactionView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  @Override
  public void attachView(AddReactionContract.View reactionView) {
    this.addReactionView = reactionView;
  }

  @Override
  public void detachView() {
    addReactionView = null;
  }

  @Override
  public void addReaction(String conversationId, String messageId, String reactionType) {
    isometrik.getRemoteUseCases().getReactionUseCases().fetchReactions(
        new FetchReactionsQuery.Builder().setLimit(Constants.USERS_PAGE_SIZE)
            .setSkip(0)
            .setConversationId(conversationId)
            .setMessageId(messageId)
            .setReactionType(reactionType)
            .setUserToken(userToken)
            .build(), (fetchResult, fetchError) -> {
          if (fetchResult != null) {
            if (hasUserAlreadyReacted(fetchResult)) {
              removeReaction(conversationId, messageId, reactionType);
            } else {
              requestAddReaction(conversationId, messageId, reactionType);
            }
          } else if (fetchError != null && fetchError.getHttpResponseCode() == 404
              && fetchError.getRemoteErrorCode() == 2) {
            requestAddReaction(conversationId, messageId, reactionType);
          } else if (addReactionView != null) {
            addReactionView.onError(fetchError != null ? fetchError.getErrorMessage() : null);
          }
        });
  }

  private boolean hasUserAlreadyReacted(FetchReactionsResult fetchResult) {
    if (fetchResult.getUserReactions() == null || fetchResult.getUserReactions().isEmpty()) {
      return false;
    }
    String userId = IsometrikChatSdk.getInstance().getUserSession().getUserId();
    for (FetchReactionsResult.UserReaction userReaction : fetchResult.getUserReactions()) {
      if (userId.equals(userReaction.getUserId())) {
        return true;
      }
    }
    return false;
  }

  private void requestAddReaction(String conversationId, String messageId, String reactionType) {
    isometrik.getRemoteUseCases().getReactionUseCases().addReaction(
        new AddReactionQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setMessageId(messageId)
            .setReactionType(reactionType)
            .build(), (addResult, addError) -> {
          if (addResult != null) {
            if (addReactionView != null) {
              ReactionModel reactionModel =
                  ReactionUtil.parseAddOrRemoveReactionEvent(reactionType,
                      addResult.getReactionsCount());
              addReactionView.onReactionAddedSuccessfully(messageId, reactionModel);
            }
          } else if (addError != null && isDuplicateReactionError(addError)) {
            removeReaction(conversationId, messageId, reactionType);
          } else if (addReactionView != null) {
            addReactionView.onError(addError != null ? addError.getErrorMessage() : null);
          }
        });
  }

  private void removeReaction(String conversationId, String messageId, String reactionType) {
    isometrik.getRemoteUseCases().getReactionUseCases().removeReaction(
        new RemoveReactionQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setMessageId(messageId)
            .setReactionType(reactionType)
            .build(), (removeResult, removeError) -> {
          if (removeResult != null) {
            if (addReactionView != null) {
              ReactionModel reactionModel =
                  ReactionUtil.parseAddOrRemoveReactionEvent(reactionType,
                      removeResult.getReactionsCount());
              addReactionView.onReactionRemovedSuccessfully(messageId, reactionModel);
            }
          } else if (addReactionView != null) {
            addReactionView.onError(removeError != null ? removeError.getErrorMessage() : null);
          }
        });
  }

  private boolean isDuplicateReactionError(io.isometrik.chat.response.error.IsometrikError error) {
    int httpCode = error.getHttpResponseCode();
    if (httpCode == 400 || httpCode == 409) {
      return true;
    }
    String errorMessage = error.getErrorMessage();
    return errorMessage != null && errorMessage.toLowerCase().contains("already");
  }
}
