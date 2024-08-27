package io.isometrik.ui.messages.broadcast;

import java.util.ArrayList;

/**
 * The interface broadcast message contract containing presenter and view interfaces implemented
 * by BroadcastMessagePresenter and BroadcastMessageActivity respectively.
 */
public interface BroadcastMessageContract {

  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Request users data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void fetchPeople(int offset, boolean refreshRequest, boolean isSearchRequest, String searchTag);

    /**
     * Request people on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void fetchPeopleOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Broadcast message.
     *
     * @param message the message
     * @param userIds the user ids
     */
    void broadcastMessage(String message, ArrayList<String> userIds);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On users data received.
     *
     * @param peopleModels the users
     * @param refreshRequest the latest users
     * @param isSearchRequest whether fetch users request is from the search or not
     */
    void onPeopleFetchedSuccessfully(ArrayList<BroadcastToPeopleModel> peopleModels,
        boolean refreshRequest, boolean isSearchRequest);

    /**
     * On message broadcasted successfully.
     */
    void onMessageBroadcastedSuccessfully();

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}