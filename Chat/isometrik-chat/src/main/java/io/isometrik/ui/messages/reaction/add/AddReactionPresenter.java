package io.isometrik.ui.messages.reaction.add;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.reaction.AddReactionQuery;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.ui.messages.reaction.util.ReactionUtil;

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
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();

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
    isometrik.getRemoteUseCases().getReactionUseCases().addReaction(new AddReactionQuery.Builder().setConversationId(conversationId)
        .setUserToken(userToken)
        .setMessageId(messageId)
        .setReactionType(reactionType)
        .build(), (var1, var2) -> {
      if (var1 != null) {
        if (addReactionView != null) {
          ReactionModel reactionModel =
              ReactionUtil.parseAddOrRemoveReactionEvent(reactionType, var1.getReactionsCount());

          addReactionView.onReactionAddedSuccessfully(messageId, reactionModel);
        }
      } else {
        if (addReactionView != null) addReactionView.onError(var2.getErrorMessage());
      }
    });
  }
}
