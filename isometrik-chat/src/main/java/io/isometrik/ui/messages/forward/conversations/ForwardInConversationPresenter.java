package io.isometrik.ui.messages.forward.conversations;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchConversationsQuery;
import io.isometrik.chat.response.conversation.utils.Conversation;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;

/**
 * The presenter to fetch list of conversations to forward a message to, with paging, search and pull
 * to refresh option.
 */
public class ForwardInConversationPresenter implements ForwardInConversationContract.Presenter {

  private ForwardInConversationContract.View forwardInConversationView;
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.CONVERSATIONS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void fetchConversations(int skip, boolean onScroll, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (skip == 0) {
      offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchConversationsQuery.Builder fetchConversationsQuery =
        new FetchConversationsQuery.Builder().setLimit(PAGE_SIZE)
            .setSkip(skip)
            .setUserToken(userToken);
    if (isSearchRequest && searchTag != null) {
      fetchConversationsQuery.setSearchTag(searchTag);
    }

    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchConversations(fetchConversationsQuery.build(), (var1, var2) -> {
          isLoading = false;
          if (var1 != null) {

            ArrayList<Conversation> conversations = var1.getConversations();

            if (conversations.size() > 0) {
              ArrayList<ForwardInConversationModel> conversationsModels = new ArrayList<>();
              int size = conversations.size();

              for (int i = 0; i < size; i++) {

                if (!conversations.get(i).isMessagingDisabled()) {
                  if (!conversations.get(i).isPrivateOneToOne() || (conversations.get(i)
                      .getOpponentDetails()
                      .getUserId() != null)) {
                    conversationsModels.add(new ForwardInConversationModel(conversations.get(i)));
                  }
                }
              }
              if (size < PAGE_SIZE) {

                isLastPage = true;
              }
              if (forwardInConversationView != null) {
                forwardInConversationView.onConversationsFetchedSuccessfully(conversationsModels,
                    onScroll, isSearchRequest);
              }
            } else {
              if (!onScroll) {
                //No conversations found
                if (forwardInConversationView != null) {
                  forwardInConversationView.onConversationsFetchedSuccessfully(new ArrayList<>(),
                      false, isSearchRequest);
                }
              } else {
                isLastPage = true;
              }
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
              if (!onScroll) {
                //No conversations found
                if (forwardInConversationView != null) {
                  forwardInConversationView.onConversationsFetchedSuccessfully(new ArrayList<>(),
                      false, isSearchRequest);
                }
              }
            } else {
              if (forwardInConversationView != null) {
                forwardInConversationView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchConversationsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchConversations(offset * PAGE_SIZE, true, isSearchRequest, searchTag);
      }
    }
  }

  @Override
  public void attachView(ForwardInConversationContract.View forwardInConversationView) {
    this.forwardInConversationView = forwardInConversationView;
  }

  @Override
  public void detachView() {
    forwardInConversationView = null;
  }
}
