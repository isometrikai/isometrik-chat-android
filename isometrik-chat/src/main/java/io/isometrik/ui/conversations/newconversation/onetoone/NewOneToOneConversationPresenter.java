package io.isometrik.ui.conversations.newconversation.onetoone;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.CreateConversationQuery;
import io.isometrik.chat.builder.user.block.FetchNonBlockedUsersQuery;
import io.isometrik.chat.response.user.utils.NonBlockedUser;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The presenter to fetch list of users to select opponent to create new one to one conversation,
 * with paging, search and pull to refresh option.Api call to create a new one to one conversation.
 */
public class NewOneToOneConversationPresenter implements NewOneToOneConversationContract.Presenter {

  /**
   * Instantiates a new New one to one conversation presenter.
   *
   * @param newOneToOneConversationView the new one to one conversation view
   */
  NewOneToOneConversationPresenter(
      NewOneToOneConversationContract.View newOneToOneConversationView) {
    this.newOneToOneConversationView = newOneToOneConversationView;
  }

  private final NewOneToOneConversationContract.View newOneToOneConversationView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;

  private boolean isSearchRequest;
  private String searchTag;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  @Override
  public void createNewConversation(boolean enablePushNotifications,
      boolean enableMessageTypingEvents, boolean enableMessageDeliveryReadEvents,
      String selectedUserId, String selectedUsername) {

    ArrayList<String> searchableTags = new ArrayList<>();
    searchableTags.add(selectedUsername);
    searchableTags.add(IsometrikChatSdk.getInstance().getUserSession().getUserName());

    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .createConversation(new CreateConversationQuery.Builder().setUserToken(userToken)
            .setGroup(false)
            .setConversationType(0)
            .setMembers(Collections.singletonList(selectedUserId))
            .setReadEvents(enableMessageDeliveryReadEvents)
            .setTypingEvents(enableMessageTypingEvents)
            .setPushNotifications(enablePushNotifications)
            .setSearchableTags(searchableTags)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            newOneToOneConversationView.onConversationCreatedSuccessfully(var1.getConversationId());
          } else {
            newOneToOneConversationView.onError(var2.getErrorMessage());
          }
        });
  }

  public void fetchNonBlockedUsers(int offset, boolean refreshRequest, boolean isSearchRequest,
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

            ArrayList<UsersModel> usersModels = new ArrayList<>();
            ArrayList<NonBlockedUser> users = var1.getNonBlockedUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {

              usersModels.add(new UsersModel(users.get(i)));
            }
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            newOneToOneConversationView.onNonBlockedUsersFetched(usersModels, refreshRequest);
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

              if (refreshRequest) {
                //No users found
                newOneToOneConversationView.onNonBlockedUsersFetched(new ArrayList<>(), true);
              } else {
                isLastPage = true;
              }
            } else {
              newOneToOneConversationView.onError(var2.getErrorMessage());
        }
      }
    });
  }

  @Override
  public void requestNonBlockedUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {


      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchNonBlockedUsers(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }
}
