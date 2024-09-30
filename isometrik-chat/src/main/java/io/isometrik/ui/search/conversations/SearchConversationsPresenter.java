package io.isometrik.ui.search.conversations;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchConversationsQuery;
import io.isometrik.chat.response.conversation.utils.Conversation;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.conversations.list.ConversationsModel;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;

/**
 * The presenter to search for conversations and fetch results with paging and pull to refresh
 * option.
 */
public class SearchConversationsPresenter implements SearchConversationsContract.Presenter {

  private SearchConversationsContract.View searchConversationsView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.CONVERSATIONS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void fetchSearchedConversations(int skip, boolean onScroll, boolean isSearchRequest,
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
              ArrayList<ConversationsModel> conversationsModels = new ArrayList<>();
              int size = conversations.size();

              for (int i = 0; i < size; i++) {

                conversationsModels.add(new ConversationsModel(conversations.get(i)));
              }
              if (size < PAGE_SIZE) {

                isLastPage = true;
              }
              if (searchConversationsView != null) {
                searchConversationsView.onSearchedConversationsFetchedSuccessfully(
                    conversationsModels, onScroll);
              }
            } else {
              if (!onScroll) {
                //No conversations found
                if (searchConversationsView != null) {
                  searchConversationsView.onSearchedConversationsFetchedSuccessfully(
                      new ArrayList<>(), false);
                }
              } else {
                isLastPage = true;
              }
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
              if (!onScroll) {
                //No conversations found
                if (searchConversationsView != null) {
                  searchConversationsView.onSearchedConversationsFetchedSuccessfully(
                      new ArrayList<>(), false);
                }
              }
            } else {
              if (searchConversationsView != null) {
                searchConversationsView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchSearchedConversationsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchSearchedConversations(offset * PAGE_SIZE, true, isSearchRequest, searchTag);
      }
    }
  }

  @Override
  public void attachView(SearchConversationsContract.View searchConversationsView) {
    this.searchConversationsView = searchConversationsView;
  }

  @Override
  public void detachView() {
    searchConversationsView = null;
  }
}