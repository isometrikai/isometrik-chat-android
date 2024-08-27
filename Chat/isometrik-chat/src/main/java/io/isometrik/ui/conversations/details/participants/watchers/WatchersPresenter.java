package io.isometrik.ui.conversations.details.participants.watchers;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchConversationWatchersQuery;
import io.isometrik.chat.builder.membershipcontrol.AddAdminQuery;
import io.isometrik.chat.builder.membershipcontrol.RemoveAdminQuery;
import io.isometrik.chat.builder.membershipcontrol.RemoveMembersQuery;
import io.isometrik.chat.response.conversation.utils.ConversationMember;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * The presenter to fetch list of watchers in a conversation with paging, search and pull to refresh
 * option and kickout watcher, grant/revoke admin privileges.
 */
public class WatchersPresenter implements WatchersContract.Presenter {

  private WatchersContract.View watchersView;
  private final Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikUiSdk.getInstance().getUserSession().getUserToken();

  private String conversationId, userId;
  private boolean isUserAnAdmin;
  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void initialize(String conversationId, boolean isUserAnAdmin, String userId) {
    this.conversationId = conversationId;
    this.isUserAnAdmin = isUserAnAdmin;
    this.userId = userId;
  }

  @Override
  public void attachView(WatchersContract.View watchersView) {
    this.watchersView = watchersView;
  }

  @Override
  public void detachView() {
    watchersView = null;
  }

  @Override
  public void fetchWatchers(int offset, boolean refreshRequest, boolean isSearchRequest, String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }

    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchConversationWatchersQuery.Builder fetchConversationWatchersQuery = new FetchConversationWatchersQuery.Builder().setLimit(PAGE_SIZE)
        .setSkip(offset)
        .setConversationId(conversationId)
        .setUserToken(userToken);
    if (isSearchRequest && searchTag != null) {
      fetchConversationWatchersQuery.setSearchTag(searchTag);
    }

    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchConversationWatchers(fetchConversationWatchersQuery.build(), (var1, var2) -> {
          isLoading = false;
          if (var1 != null) {

            ArrayList<ConversationMember> conversationMembers = var1.getConversationWatchers();

            ArrayList<MembersWatchersModel> conversationMembersModels = new ArrayList<>();
            int size = conversationMembers.size();

            for (int i = 0; i < size; i++) {
              ConversationMember conversationMember = conversationMembers.get(i);
              conversationMembersModels.add(
                  new MembersWatchersModel(conversationMember, isUserAnAdmin, userId.equals(conversationMember.getUserId())));
            }
            if (size < PAGE_SIZE) {

              isLastPage = true;
            }
            if (watchersView != null) {
              watchersView.onWatchersFetchedSuccessfully(conversationMembersModels, refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No watchers found
                if (watchersView != null) {

                  watchersView.onWatchersFetchedSuccessfully(new ArrayList<>(), true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (watchersView != null) {
                watchersView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchWatchersOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchWatchers(offset * PAGE_SIZE, true, isSearchRequest, searchTag);
      }
    }
  }

  @Override
  public void kickOutMembers(String conversationId, List<String> memberIds) {
    isometrik.getRemoteUseCases()
        .getMembershipControlUseCases()
        .removeMembers(new RemoveMembersQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setMembers(memberIds)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            if (watchersView != null) watchersView.onMemberRemovedSuccessfully(memberIds.get(0));
          } else {
            if (watchersView != null) watchersView.onError(var2.getErrorMessage());
          }
        });
  }

  @Override
  public void makeAdmin(String conversationId, String memberId) {
    isometrik.getRemoteUseCases()
        .getMembershipControlUseCases()
        .addAdmin(new AddAdminQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setMemberId(memberId)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            if (watchersView != null) watchersView.onMemberAdminPermissionsUpdated(memberId, true);
          } else {
            if (watchersView != null) watchersView.onError(var2.getErrorMessage());
          }
        });
  }

  @Override
  public void revokeAdminPermissions(String conversationId, String memberId) {
    isometrik.getRemoteUseCases()
        .getMembershipControlUseCases()
        .removeAdmin(new RemoveAdminQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setMemberId(memberId)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            if (watchersView != null) watchersView.onMemberAdminPermissionsUpdated(memberId, false);
          } else {
            if (watchersView != null) watchersView.onError(var2.getErrorMessage());
          }
        });
  }
}
