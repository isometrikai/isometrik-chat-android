package io.isometrik.ui.messages.tag;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchConversationMemberDetailsQuery;
import io.isometrik.ui.IsometrikChatSdk;

/**
 * The presenter to fetch details of a tagged user on click.
 */
public class MemberDetailsPresenter implements MemberDetailsContract.Presenter {

  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  private MemberDetailsContract.View memberDetailsView;

  @Override
  public void attachView(MemberDetailsContract.View memberDetailsView) {
    this.memberDetailsView = memberDetailsView;
  }

  @Override
  public void detachView() {
    this.memberDetailsView = null;
  }

  @Override
  public void fetchMemberDetails(String conversationId, String memberId) {

    isometrik.getRemoteUseCases()
        .getMembershipControlUseCases()
        .fetchConversationMemberDetails(
            new FetchConversationMemberDetailsQuery.Builder().setConversationId(conversationId)
                .setUserToken(userToken)
                .setMemberId(memberId)
                .build(), (var1, var2) -> {
              if (var1 != null) {
                if (memberDetailsView != null) {

                  memberDetailsView.onMemberDetailsFetchedSuccessfully(var1.getUserName(),
                      var1.getUserIdentifier(), var1.getUserProfileImageUrl(), var1.isOnline());
                }
              } else {
                if (var2.getHttpResponseCode() == 400) {
                  if (memberDetailsView != null) {
                    memberDetailsView.onNotAllowedToFetchMemberDetails(var2.getErrorMessage());
                  }
                } else {

                  if (memberDetailsView != null) memberDetailsView.onError(var2.getErrorMessage());
                }
              }
            });
  }
}
