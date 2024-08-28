package io.isometrik.ui.messages.forward.people;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.user.block.FetchNonBlockedUsersQuery;
import io.isometrik.chat.response.user.utils.NonBlockedUser;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;

/**
 * The presenter to fetch list of users to forward a message to, with paging, search and pull to
 * refresh option.
 */
public class ForwardToPeoplePresenter implements ForwardToPeopleContract.Presenter {

  private ForwardToPeopleContract.View forwardToPeopleView;
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();
  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void fetchPeople(int offset, boolean refreshRequest, boolean isSearchRequest,
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

            ArrayList<PeopleModel> peopleModels = new ArrayList<>();
            ArrayList<NonBlockedUser> users = var1.getNonBlockedUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {

              peopleModels.add(new PeopleModel(users.get(i)));
            }
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            if (forwardToPeopleView != null) {
              forwardToPeopleView.onPeopleFetchedSuccessfully(peopleModels, refreshRequest,
                  isSearchRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

              if (refreshRequest) {
                //No users found
                if (forwardToPeopleView != null) {

                  forwardToPeopleView.onPeopleFetchedSuccessfully(new ArrayList<>(), true,
                      isSearchRequest);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (forwardToPeopleView != null) {

                forwardToPeopleView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchPeopleOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchPeople(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }

  @Override
  public void attachView(ForwardToPeopleContract.View forwardToPeopleView) {
    this.forwardToPeopleView = forwardToPeopleView;
  }

  @Override
  public void detachView() {
    forwardToPeopleView = null;
  }
}
