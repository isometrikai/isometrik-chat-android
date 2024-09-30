package io.isometrik.ui.search.messages;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.message.FetchUserMessagesQuery;
import io.isometrik.chat.response.message.utils.fetchmessages.UserMessage;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.search.messages.utils.SearchAttachmentMessageUtil;
import io.isometrik.chat.utils.Constants;

import java.util.ArrayList;

/**
 * The presenter to search for messages across all conversations and fetch results with paging and
 * pull to refresh option.
 */
public class SearchMessagesPresenter implements SearchMessagesContract.Presenter {

  private SearchMessagesContract.View searchMessagesView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.SEARCH_MESSAGES_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void fetchSearchedMessages(int skip, boolean onScroll, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (skip == 0) {
      offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchUserMessagesQuery.Builder fetchMessagesQuery =
        new FetchUserMessagesQuery.Builder().setFetchConversationDetails(true)
            .setLimit(PAGE_SIZE)
            .setSkip(skip)
            .setConversationStatusMessage(false)
            .setUserToken(userToken);
    if (isSearchRequest && searchTag != null) {
      fetchMessagesQuery.setSearchTag(searchTag);
    }

    isometrik.getRemoteUseCases()
        .getMessageUseCases()
        .fetchUserMessages(fetchMessagesQuery.build(), (var1, var2) -> {
          isLoading = false;
          if (var1 != null) {

            ArrayList<UserMessage> messages = var1.getMessages();

            if (messages.size() > 0) {
              ArrayList<MessagesModel> messagesModels = new ArrayList<>();
              int size = messages.size();
              MessagesModel messageModel;
              for (int i = 0; i < size; i++) {
                messageModel =
                    SearchAttachmentMessageUtil.prepareMessageAttachmentModel(messages.get(i));
                if (messageModel != null) {
                  messagesModels.add(0, messageModel);
                }
              }
              if (size < PAGE_SIZE) {

                isLastPage = true;
              }
              if (searchMessagesView != null) {
                searchMessagesView.onSearchedMessagesFetchedSuccessfully(messagesModels, onScroll);
              }
            } else {
              if (!onScroll) {
                //No messages found
                if (searchMessagesView != null) {
                  searchMessagesView.onSearchedMessagesFetchedSuccessfully(new ArrayList<>(),
                      false);
                }
              } else {
                isLastPage = true;
              }
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
              if (!onScroll) {
                //No messages found
                if (searchMessagesView != null) {
                  searchMessagesView.onSearchedMessagesFetchedSuccessfully(new ArrayList<>(),
                      false);
                }
              }
            } else {
              if (searchMessagesView != null) {
                searchMessagesView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchSearchedMessagesOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchSearchedMessages(offset * PAGE_SIZE, true, isSearchRequest, searchTag);
      }
    }
  }

  @Override
  public void attachView(SearchMessagesContract.View searchMessagesView) {
    this.searchMessagesView = searchMessagesView;
  }

  @Override
  public void detachView() {
    searchMessagesView = null;
  }
}