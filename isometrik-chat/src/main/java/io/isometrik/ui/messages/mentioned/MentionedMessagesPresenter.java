package io.isometrik.ui.messages.mentioned;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.message.FetchMentionedMessagesQuery;
import io.isometrik.chat.response.message.utils.fetchmessages.MentionedMessage;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;

/**
 * The presenter to fetch list of messages in which logged in user has been mentioned, with paging,
 * search and pull to refresh option.
 */
public class MentionedMessagesPresenter implements MentionedMessagesContract.Presenter {

  /**
   * Instantiates a new Mentioned messages presenter.
   *
   * @param mentionedMessagesView the mentioned messages view
   */
  MentionedMessagesPresenter(MentionedMessagesContract.View mentionedMessagesView) {
    this.mentionedMessagesView = mentionedMessagesView;
  }

  private final MentionedMessagesContract.View mentionedMessagesView;
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();
  private final int MENTIONED_MESSAGES_PAGE_SIZE = Constants.MENTIONED_MESSAGES_PAGE_SIZE;

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;

  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void fetchMentionedMessages(int skip, boolean onScroll, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (skip == 0) {
      isLastPage = false;
      offset = 0;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchMentionedMessagesQuery.Builder fetchMentionedMessagesQuery =
        new FetchMentionedMessagesQuery.Builder().setUserToken(userToken)
            .setLimit(MENTIONED_MESSAGES_PAGE_SIZE)
            .setSkip(skip)
            .setSort(Constants.SORT_ORDER_DSC);
    if (isSearchRequest && searchTag != null) {
      fetchMentionedMessagesQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getMessageUseCases()
        .fetchMentionedMessages(fetchMentionedMessagesQuery.build(), (var1, var2) -> {
          isLoading = false;
          if (var1 != null) {

            ArrayList<MentionedMessage> messages = var1.getMessages();

            ArrayList<MentionedMessagesModel> galleryModels = new ArrayList<>();
            int size = messages.size();

            for (int i = 0; i < size; i++) {

              galleryModels.add(new MentionedMessagesModel(messages.get(i)));
            }
            if (size < MENTIONED_MESSAGES_PAGE_SIZE) {

              isLastPage = true;
            }

            mentionedMessagesView.onMentionedMessagesFetchedSuccessfully(galleryModels, onScroll);
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {
              if (!onScroll) {
                //No mentioned messages found
                mentionedMessagesView.onMentionedMessagesFetchedSuccessfully(new ArrayList<>(),
                    false);
              } else {
                isLastPage = true;
              }
            } else {
              mentionedMessagesView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void fetchMentionedMessagesOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= MENTIONED_MESSAGES_PAGE_SIZE) {

        offset++;
        fetchMentionedMessages(offset * MENTIONED_MESSAGES_PAGE_SIZE, true, isSearchRequest,
            searchTag);
      }
    }
  }
}
