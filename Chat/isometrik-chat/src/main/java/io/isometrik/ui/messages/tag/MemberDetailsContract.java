package io.isometrik.ui.messages.tag;

import io.isometrik.chat.utils.BasePresenter;

/**
 * The interface member details contract containing presenter and view interfaces implemented
 * by MemberDetailsPresenter and MemberDetailsFragment respectively.
 */
public interface MemberDetailsContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<View> {

    /**
     * Fetch member details.
     *
     * @param conversationId the conversation id
     * @param memberId the member id
     */
    void fetchMemberDetails(String conversationId, String memberId);
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On member details fetched successfully.
     *
     * @param memberName the member name
     * @param memberIdentifier the member identifier
     * @param memberProfileImageUrl the member profile image url
     * @param isOnline the is online
     */
    void onMemberDetailsFetchedSuccessfully(String memberName, String memberIdentifier,
        String memberProfileImageUrl, boolean isOnline);

    /**
     * On not allowed to fetch member details.
     *
     * @param errorMessage the error message
     */
    void onNotAllowedToFetchMemberDetails(String errorMessage);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
