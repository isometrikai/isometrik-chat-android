package io.isometrik.ui.conversations.participants;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.membershipcontrol.AddMembersQuery;
import io.isometrik.chat.builder.membershipcontrol.FetchMembersToAddToConversationQuery;
import io.isometrik.chat.response.membershipcontrol.FetchMembersToAddToConversationResult;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;

/**
 * The presenter to fetch list of users that can be added as participants to a conversation with
 * paging, search and pull to refresh option.Api call to add participant to a conversation.
 */
public class AddParticipantsPresenter implements AddParticipantsContract.Presenter {

  /**
   * Instantiates a new Add participants presenter.
   *
   * @param addParticipantsView the add participants view
   * @param conversationId the conversation id
   */
  AddParticipantsPresenter(AddParticipantsContract.View addParticipantsView,
      String conversationId) {
    this.addParticipantsView = addParticipantsView;
    this.conversationId = conversationId;
  }

  private final AddParticipantsContract.View addParticipantsView;
  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String conversationId;
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  private int offset;
  private boolean isLastPage;
  private boolean isLoading;
  private final int PAGE_SIZE = Constants.USERS_PAGE_SIZE;

  private boolean isSearchRequest;
  private String searchTag;

  @Override
  public void addMembers(ArrayList<ParticipantsModel> participantsModels) {
    ArrayList<String> memberIds = new ArrayList<>();
    int size = participantsModels.size();
    for (int i = 0; i < size; i++) {

      memberIds.add(participantsModels.get(i).getUserId());
    }

    isometrik.getRemoteUseCases()
        .getMembershipControlUseCases()
        .addMembers(new AddMembersQuery.Builder().setConversationId(conversationId)
            .setUserToken(userToken)
            .setMembers(memberIds)
            .build(), (var1, var2) -> {
          if (var1 != null) {
            addParticipantsView.onMembersAddedSuccessfully();
          } else {
            addParticipantsView.onError(var2.getErrorMessage());
          }
        });
  }

  @Override
  public void fetchMembersToAddToConversation(int offset, boolean refreshRequest,
      boolean isSearchRequest, String searchTag) {
    isLoading = true;

    if (refreshRequest) {
      this.offset = 0;
      isLastPage = false;
    }
    this.isSearchRequest = isSearchRequest;
    this.searchTag = isSearchRequest ? searchTag : null;

    FetchMembersToAddToConversationQuery.Builder fetchMembersToAddToConversationQuery =
        new FetchMembersToAddToConversationQuery.Builder().setLimit(PAGE_SIZE)
            .setSkip(offset)
            .setSort(Constants.SORT_ORDER_ASC)
            .setConversationId(conversationId)
            .setUserToken(userToken);
    if (isSearchRequest && searchTag != null) {
      fetchMembersToAddToConversationQuery.setSearchTag(searchTag);
    }

    isometrik.getRemoteUseCases()
        .getConversationUseCases()
        .fetchMembersToAddToConversation(fetchMembersToAddToConversationQuery.build(),
            (var1, var2) -> {

              isLoading = false;

              if (var1 != null) {

                ArrayList<ParticipantsModel> participantsModels = new ArrayList<>();
                ArrayList<FetchMembersToAddToConversationResult.MemberToAddToConversation> users =
                    var1.getMembersToAddToConversation();
                int size = users.size();

                for (int i = 0; i < size; i++) {

                  participantsModels.add(new ParticipantsModel(users.get(i)));
                }
                if (size < PAGE_SIZE) {

                  isLastPage = true;
                }
                addParticipantsView.onMembersToAddToConversationFetched(participantsModels,
                    refreshRequest, isSearchRequest);
              } else {
                if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

                  if (refreshRequest) {
                    //No participants to add found
                    addParticipantsView.onMembersToAddToConversationFetched(new ArrayList<>(), true,
                        isSearchRequest);
                  } else {
                    isLastPage = true;
                  }
                } else {
              addParticipantsView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  @Override
  public void requestMembersToAddToConversationOnScroll(int firstVisibleItemPosition,
      int visibleItemCount, int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount) >= PAGE_SIZE) {

        offset++;
        fetchMembersToAddToConversation(offset * PAGE_SIZE, false, isSearchRequest, searchTag);
      }
    }
  }
}
