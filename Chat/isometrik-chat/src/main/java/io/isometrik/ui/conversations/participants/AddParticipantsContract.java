package io.isometrik.ui.conversations.participants;

import java.util.ArrayList;

/**
 * The interface add participants contract containing presenter and view interfaces implemented
 * by AddParticipantsPresenter and AddParticipantsActivity respectively.
 */
public interface AddParticipantsContract {
  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Add members.
     *
     * @param participantsModels the participants models
     */
    void addMembers(ArrayList<ParticipantsModel> participantsModels);

    /**
     * Request members to add to conversation data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void fetchMembersToAddToConversation(int offset, boolean refreshRequest,
        boolean isSearchRequest, String searchTag);

    /**
     * Request members to add to conversation data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestMembersToAddToConversationOnScroll(int firstVisibleItemPosition,
        int visibleItemCount, int totalItemCount);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On members added successfully.
     */
    void onMembersAddedSuccessfully();

    /**
     * On members to add to conversation fetched.
     *
     * @param participantsModels the participants models
     * @param latestUsers the latest users
     * @param isSearchRequest the is search request
     */
    void onMembersToAddToConversationFetched(ArrayList<ParticipantsModel> participantsModels,
        boolean latestUsers, boolean isSearchRequest);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
