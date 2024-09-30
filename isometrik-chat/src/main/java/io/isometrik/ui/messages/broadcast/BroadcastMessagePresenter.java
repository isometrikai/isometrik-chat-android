package io.isometrik.ui.messages.broadcast;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.message.broadcastforward.BroadcastMessageQuery;
import io.isometrik.chat.builder.user.block.FetchNonBlockedUsersQuery;
import io.isometrik.chat.response.message.utils.schemas.EventForMessage;
import io.isometrik.chat.response.user.utils.NonBlockedUser;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.utils.enums.RemoteMessageTypes;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.enums.CustomMessageTypes;
import java.util.ArrayList;
import java.util.List;

/**
 * The presenter to fetch list of users to broadcast a message to, with paging, search and pull to
 * refresh option.Api call to broadcast a message to selected users.
 */
public class BroadcastMessagePresenter implements BroadcastMessageContract.Presenter {

  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private final BroadcastMessageContract.View broadcastMessageView;

  private boolean isSearchRequest;
  private String searchTag;

  /**
   * Instantiates a new Broadcast message presenter.
   *
   * @param broadcastMessageView the broadcast message view
   */
  BroadcastMessagePresenter(BroadcastMessageContract.View broadcastMessageView) {
    this.broadcastMessageView = broadcastMessageView;
  }

  @Override
  public void broadcastMessage(String message, ArrayList<String> userIds) {

    List<String> searchableTags = new ArrayList<>();
    searchableTags.add(
        IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_search_tag_text));
    searchableTags.add(message);

    BroadcastMessageQuery.Builder broadcastMessageQuery =
        new BroadcastMessageQuery.Builder().setUserToken(userToken)
            .setEncrypted(false)
            .setBody(message)
            .setMessageType(RemoteMessageTypes.NormalMessage.getValue())
            .setShowInConversation(true)
            .setEventForMessage(new EventForMessage(true, true))
            .setUserIds(userIds)
            .setSearchableTags(searchableTags)
            .setCustomType(CustomMessageTypes.Text.getValue())
            .setDeviceId(IsometrikChatSdk.getInstance().getUserSession().getDeviceId());

    isometrik.getRemoteUseCases()
        .getMessageUseCases()
        .broadcastMessage(broadcastMessageQuery.build(), (var1, var2) -> {
          if (var1 != null) {
            broadcastMessageView.onMessageBroadcastedSuccessfully();
          } else {
            broadcastMessageView.onError(var2.getErrorMessage());
          }
        });
  }

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

            ArrayList<BroadcastToPeopleModel> peopleModels = new ArrayList<>();
            ArrayList<NonBlockedUser> users = var1.getNonBlockedUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {

              peopleModels.add(new BroadcastToPeopleModel(users.get(i)));
            }
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            broadcastMessageView.onPeopleFetchedSuccessfully(peopleModels, refreshRequest,
                isSearchRequest);
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

              if (refreshRequest) {
                //No users found

                broadcastMessageView.onPeopleFetchedSuccessfully(new ArrayList<>(), true,
                    isSearchRequest);
              } else {
                isLastPage = true;
              }
            } else {

              broadcastMessageView.onError(var2.getErrorMessage());
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
}
