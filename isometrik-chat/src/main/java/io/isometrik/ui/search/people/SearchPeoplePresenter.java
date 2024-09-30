package io.isometrik.ui.search.people;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.user.block.FetchNonBlockedUsersQuery;
import io.isometrik.chat.response.user.utils.NonBlockedUser;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;

/**
 * The presenter to search for non-blocked users and fetch results with paging and pull to refresh option.
 */
public class SearchPeoplePresenter implements SearchPeopleContract.Presenter {

  private SearchPeopleContract.View searchPeoplePeopleView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();
  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void fetchSearchedPeople(int offset, boolean refreshRequest, boolean isSearchRequest,
      String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchNonBlockedUsersQuery.Builder fetchNonBlockedUsersQuery =
        new FetchNonBlockedUsersQuery.Builder().setLimit(PAGE_SIZE)
            .setSkip(offset)
            .setSort(Constants.SORT_ORDER_ASC)
            .setUserToken(userToken);
    if (isSearchRequest && searchTag != null) {
      fetchNonBlockedUsersQuery.setSearchTag(searchTag);
    }
    isometrik.getRemoteUseCases()
        .getUserUseCases()
        .fetchNonBlockedUsers(fetchNonBlockedUsersQuery.build(), (var1, var2) -> {

          isLoading = false;

          if (var1 != null) {

            ArrayList<SearchPeopleModel> peopleModels = new ArrayList<>();
            ArrayList<NonBlockedUser> users = var1.getNonBlockedUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {

              peopleModels.add(new SearchPeopleModel(users.get(i)));
            }
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            if (searchPeoplePeopleView != null) {
              searchPeoplePeopleView.onSearchedPeopleFetchedSuccessfully(peopleModels,
                  refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

              if (refreshRequest) {
                //No users found
                if (searchPeoplePeopleView != null) {

                  searchPeoplePeopleView.onSearchedPeopleFetchedSuccessfully(new ArrayList<>(),
                      true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (searchPeoplePeopleView != null) {

                searchPeoplePeopleView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchSearchedPeopleOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchSearchedPeople(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }

  @Override
  public void attachView(SearchPeopleContract.View searchPeopleView) {
    this.searchPeoplePeopleView = searchPeopleView;
  }

  @Override
  public void detachView() {
    searchPeoplePeopleView = null;
  }
}