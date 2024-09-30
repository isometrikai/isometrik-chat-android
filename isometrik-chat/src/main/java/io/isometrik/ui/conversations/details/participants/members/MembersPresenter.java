package io.isometrik.ui.conversations.details.participants.members;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.conversation.FetchConversationMembersQuery;
import io.isometrik.chat.builder.membershipcontrol.AddAdminQuery;
import io.isometrik.chat.builder.membershipcontrol.RemoveAdminQuery;
import io.isometrik.chat.builder.membershipcontrol.RemoveMembersQuery;
import io.isometrik.chat.response.conversation.utils.ConversationMember;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * The presenter to fetch list of members in a conversation with paging, search and pull to refresh
 * option and kickout member, grant/revoke admin privileges.
 */
public class MembersPresenter implements MembersContract.Presenter {

  private MembersContract.View membersView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

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
  public void attachView(MembersContract.View membersView) {
    this.membersView = membersView;
  }

  @Override
  public void detachView() {
    membersView = null;
  }

  @Override
  public void fetchMembers(int offset, boolean refreshRequest, boolean isSearchRequest, String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }

    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchConversationMembersQuery.Builder fetchConversationMembersQuery = new FetchConversationMembersQuery.Builder().setLimit(PAGE_SIZE)
        .setSkip(offset)
        .setConversationId(conversationId)
        .setUserToken(userToken);
    if (isSearchRequest && searchTag != null) {
      fetchConversationMembersQuery.setSearchTag(searchTag);
    }

    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchConversationMembers(fetchConversationMembersQuery.build(), (var1, var2) -> {
          isLoading = false;
          if (var1 != null) {

            ArrayList<ConversationMember> conversationMembers = var1.getConversationMembers();

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
            if (membersView != null) {
              membersView.onMembersFetchedSuccessfully(conversationMembersModels, refreshRequest);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No members found
                if (membersView != null) {

                  membersView.onMembersFetchedSuccessfully(new ArrayList<>(), true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (membersView != null) {
                membersView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void fetchMembersOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchMembers(offset * PAGE_SIZE, true, isSearchRequest, searchTag);
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
            if (membersView != null) membersView.onMemberRemovedSuccessfully(memberIds.get(0));
          } else {
            if (membersView != null) membersView.onError(var2.getErrorMessage());
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
            if (membersView != null) membersView.onMemberAdminPermissionsUpdated(memberId, true);
          } else {
            if (membersView != null) membersView.onError(var2.getErrorMessage());
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
            if (membersView != null) membersView.onMemberAdminPermissionsUpdated(memberId, false);
          } else {
            if (membersView != null) membersView.onError(var2.getErrorMessage());
          }
        });
  }
}
