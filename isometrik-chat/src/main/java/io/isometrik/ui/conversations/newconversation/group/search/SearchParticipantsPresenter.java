//package io.isometrik.chat.ui.conversations.newconversation.group.search;
//
//import io.isometrik.chat.Isometrik;
//import io.isometrik.chat.builder.user.block.FetchNonBlockedUsersQuery;
//import io.isometrik.chat.response.user.utils.BlockNonBlockUser;
//import io.isometrik.ui.IsometrikUiSdk;
//import io.isometrik.chat.conversations.newconversation.group.UsersModel;
//import io.isometrik.chat.utils.Constants;
//import java.util.ArrayList;
//
//public class SearchParticipantsPresenter implements SearchParticipantsContract.Presenter {
//
//  SearchParticipantsPresenter(SearchParticipantsContract.View searchParticipantsView) {
//    this.searchParticipantsView = searchParticipantsView;
//  }
//
//  private final SearchParticipantsContract.View searchParticipantsView;
//  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
//  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();
//
//  private int offset;
//  private boolean isLastPage;
//  private boolean isLoading;
//  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;
//
//  private boolean isSearchRequest;
//  private String searchTag;
//
//  @Override
//  public void fetchNonBlockedUsers(int offset, boolean refreshRequest, boolean isSearchRequest,
//      String searchTag) {
//    isLoading = true;
//
//    if (refreshRequest) {
//      this.offset = 0;
//      isLastPage = false;
//    }
//
//    this.isSearchRequest = isSearchRequest;
//    this.searchTag = isSearchRequest ? searchTag : null;
//
//    FetchNonBlockedUsersQuery.Builder fetchNonBlockedUsersQuery =
//        new FetchNonBlockedUsersQuery.Builder().setLimit(PAGE_SIZE)
//            .setSkip(offset)
//            .setSort(Constants.SORT_ORDER_ASC)
//            .setUserToken(userToken);
//    if (isSearchRequest && searchTag != null) {
//      fetchNonBlockedUsersQuery.setSearchTag(searchTag);
//    }
//
//    isometrik.getRemoteUseCases()
//        .getUserUseCases()
//        .fetchNonBlockedUsers(fetchNonBlockedUsersQuery.build(), (var1, var2) -> {
//
//          isLoading = false;
//
//          if (var1 != null) {
//
//            ArrayList<UsersModel> usersModels = new ArrayList<>();
//            ArrayList<BlockNonBlockUser> users = var1.getNonBlockedUsers();
//            int size = users.size();
//
//            for (int i = 0; i < size; i++) {
//
//              usersModels.add(new UsersModel(users.get(i)));
//            }
//            if (size < PAGE_SIZE) {
//
//              isLastPage = true;
//            }
//            searchParticipantsView.onNonBlockedUsersFetched(usersModels, refreshRequest,
//                isSearchRequest);
//          } else {
//            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {
//
//              if (refreshRequest) {
//                //No users found
//                searchParticipantsView.onNonBlockedUsersFetched(new ArrayList<>(), true,
//                    isSearchRequest);
//              } else {
//                isLastPage = true;
//              }
//            } else {
//              searchParticipantsView.onError(var2.getErrorMessage());
//            }
//          }
//        });
//  }
//
//  @Override
//  public void requestNonBlockedUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
//      int totalItemCount) {
//    if (!isLoading && !isLastPage) {
//
//      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
//          && firstVisibleItemPosition >= 0
//          && (totalItemCount) >= PAGE_SIZE) {
//
//        offset++;
//        fetchNonBlockedUsers(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
//      }
//    }
//  }
//}
