package io.isometrik.ui.conversations.list;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchUnreadConversationsCountQuery;
import io.isometrik.chat.builder.message.FetchUserMessagesQuery;
import io.isometrik.chat.builder.user.FetchUserDetailsQuery;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.utils.Constants;
import java.util.Collections;

/**
 * The presenter to fetch unread conversations count, all undelivered messages and
 * latest user details.
 */
public class ConversationsPresenter implements ConversationsContract.Presenter {

  private final ConversationsContract.View conversationsView;

  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  /**
   * Instantiates a new Conversations presenter.
   *
   * @param conversationsView the conversations view
   */
  ConversationsPresenter(ConversationsContract.View conversationsView) {
    this.conversationsView = conversationsView;
  }

  @Override
  public void fetchUnreadConversationsCount() {
    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchUnreadConversationsCount(
            new FetchUnreadConversationsCountQuery.Builder().setUserToken(userToken).build(),
            (var1, var2) -> {
              if (var1 != null) {

                conversationsView.onUnreadConversationsCountFetchedSuccessfully(
                    var1.getConversationsCount());
              } else {
                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
                  conversationsView.onUnreadConversationsCountFetchedSuccessfully(0);
                } else {
                  conversationsView.onError(var2.getErrorMessage());
                }
              }
            });
  }

  @Override
  public void fetchUserDetails() {
    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .fetchUserDetails(new FetchUserDetailsQuery.Builder().setUserToken(userToken).build(),
            (var1, var2) -> {
              if (var1 != null) {
                IsometrikChatSdk.getInstance()
                    .getUserSession()
                    .switchUser(IsometrikChatSdk.getInstance().getUserSession().getUserId(),
                        userToken, var1.getUserName(), var1.getUserIdentifier(),
                        var1.getUserProfileImageUrl(),
                        IsometrikChatSdk.getInstance().getUserSession().getUserSelected(),
                        var1.getMetaData(), var1.isNotification(), -1);
              } else {
                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
                  IsometrikChatSdk.getInstance().getUserSession().clear();
                  conversationsView.onUserDeleted();
                }
              }
            });
  }

  @Override
  public void fetchAllUndeliveredMessages(int offset) {
    try {
      isometrik.getRemoteUseCases()
          .getMessageUseCases()
          .fetchUserMessages(
              new FetchUserMessagesQuery.Builder().setLimit(Constants.USER_MESSAGES_PAGE_SIZE)
                  .setSkip(offset)
                  .setFetchConversationDetails(true)
                  .setDeliveredToMe(false)
                  .setSenderIdsExclusive(true)
                  .setSenderIds(Collections.singletonList(
                      IsometrikChatSdk.getInstance().getUserSession().getUserId()))
                  .setConversationStatusMessage(false)
                  .setUserToken(userToken)
                  .setMarkMessageAsDelivered(true)
                  .setLastMessageTimestamp(
                      IsometrikChatSdk.getInstance().getUserSession().getDeliveryStatusUpdatedUpto())
                  .build(), (var1, var2) -> {
                if (var1 != null
                    && var1.getMessages().size() == Constants.USER_MESSAGES_PAGE_SIZE) {
                  fetchAllUndeliveredMessages((offset + 1) * Constants.USER_MESSAGES_PAGE_SIZE);
                }
              });
    } catch (Exception ignore) {
    }
  }
}
