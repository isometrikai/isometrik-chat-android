package io.isometrik.ui.conversations.details.observers;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.membershipcontrol.FetchObserversQuery;
import io.isometrik.chat.response.membershipcontrol.FetchObserversResult;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;

/**
 * The presenter to fetch list of observers in an open conversation with paging, search and pull to refresh option.
 */
public class ObserversPresenter implements ObserversContract.Presenter {

  private final ObserversContract.View observersView;
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;
  private final String conversationId;

  /**
   * Instantiates a new Observers presenter.
   *
   * @param observersView the observers view
   * @param conversationId the conversation id
   */
  ObserversPresenter(ObserversContract.View observersView, String conversationId) {
    this.observersView = observersView;
    this.conversationId = conversationId;
  }

  @Override
  public void fetchObservers(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }

    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchObserversQuery.Builder fetchObserversQuery =
        new FetchObserversQuery.Builder().setLimit(PAGE_SIZE)
            .setSkip(offset)
            .setConversationId(conversationId)
            .setUserToken(userToken);
    if (isSearchRequest && searchTag != null) {
      fetchObserversQuery.setSearchTag(searchTag);
    }

    isometrik.getRemoteUseCases()
        .getMembershipControlUseCases()
        .fetchObservers(fetchObserversQuery.build(), (var1, var2) -> {
          isLoading = false;
          if (var1 != null) {

            ArrayList<FetchObserversResult.ConversationObserver> conversationObservers =
                var1.getConversationObservers();

            ArrayList<ObserversModel> observersModels = new ArrayList<>();
            int size = conversationObservers.size();

            for (int i = 0; i < size; i++) {
              observersModels.add(new ObserversModel(conversationObservers.get(i)));
            }
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            if (observersView != null) {
              observersView.onObserversFetchedSuccessfully(observersModels, refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No observers found
                if (observersView != null) {

                  observersView.onObserversFetchedSuccessfully(new ArrayList<>(), true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (observersView != null) {
                observersView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchObserversOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchObservers(offset * PAGE_SIZE, true, isSearchRequest, searchTag);
      }
    }
  }
}
