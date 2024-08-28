package io.isometrik.ui.conversations.list;

import io.isometrik.chat.enums.ConversationType;
import io.isometrik.chat.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface conversations list contract containing presenter and view interfaces implemented
 * by ConversationsListPresenter and ConversationsListFragment respectively.
 */
public interface ConversationsListContract {

  /**
   * The interface Presenter.
   */
  interface Presenter extends BasePresenter<ConversationsListContract.View> {

    /**
     * Initialize.
     *
     * @param conversationType the conversation type
     */
    void initialize(ConversationType conversationType);

    /**
     * Fetch conversations.
     *
     * @param skip the skip
     * @param onScroll the on scroll
     * @param conversationType the conversation type
     * @param isSearchRequest the is search request
     * @param searchTag the search tag
     */
    void fetchConversations(int skip, boolean onScroll, ConversationType conversationType, boolean isSearchRequest,
        String searchTag);

    /**
     * Request conversations on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     * @param conversationType whether to fetch only public/open conversations or all
     */
    void fetchConversationsOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount,ConversationType conversationType);

    /**
     * Register connection event listener.
     */
    void registerConnectionEventListener();

    /**
     * Unregister connection event listener.
     */
    void unregisterConnectionEventListener();

    /**
     * Register conversation event listener.
     */
    void registerConversationEventListener();

    /**
     * Unregister conversation event listener.
     */
    void unregisterConversationEventListener();

    /**
     * Register membership control event listener.
     */
    void registerMembershipControlEventListener();

    /**
     * Unregister membership control event listener.
     */
    void unregisterMembershipControlEventListener();

    /**
     * Register message event listener.
     */
    void registerMessageEventListener();

    /**
     * Unregister message event listener.
     */
    void unregisterMessageEventListener();

    /**
     * Register reaction event listener.
     */
    void registerReactionEventListener();

    /**
     * Unregister reaction event listener.
     */
    void unregisterReactionEventListener();

    /**
     * Register user event listener.
     */
    void registerUserEventListener();

    /**
     * Unregister user event listener.
     */
    void unregisterUserEventListener();

    /**
     * Fetch conversation position in list int.
     *
     * @param conversations the conversations
     * @param conversationId the conversation id
     * @param fetchRemoteConversationIfNotFoundLocally the fetch remote conversation if not found
     * locally
     * @return the int
     */
    int fetchConversationPositionInList(ArrayList<ConversationsModel> conversations,
        String conversationId, boolean fetchRemoteConversationIfNotFoundLocally);

    /**
     * Join conversation.
     *
     * @param conversationsModel the conversations model
     */
    void joinConversation(ConversationsModel conversationsModel);
  }

  /**
   * The interface View.
   */
  interface View {
    /**
     * On conversations fetched successfully.
     *
     * @param conversationsModels the conversations models
     * @param resultsOnScroll the results on scroll
     */
    void onConversationsFetchedSuccessfully(ArrayList<ConversationsModel> conversationsModels,
        boolean resultsOnScroll);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On conversation created.
     *
     * @param conversationsModel the conversations model
     * @param publicConversationsOnly the public conversations only
     */
    void onConversationCreated(ConversationsModel conversationsModel,
        boolean publicConversationsOnly);

    /**
     * Remove conversation.
     *
     * @param conversationId the conversation id
     */
    void removeConversation(String conversationId);

    /**
     * On conversation title updated.
     *
     * @param conversationId the conversation id
     * @param newTitle the new title
     */
    void onConversationTitleUpdated(String conversationId, String newTitle);

    /**
     * On conversation image updated.
     *
     * @param conversationId the conversation id
     * @param conversationImageUrl the conversation image url
     */
    void onConversationImageUpdated(String conversationId, String conversationImageUrl);

    /**
     * Update last message in conversation.
     *
     * @param conversationId the conversation id
     * @param lastMessageText the last message text
     * @param lastMessageSendersProfileImageUrl the last message senders profile image url
     * @param lastMessageTime the last message time
     * @param lastMessagePlaceHolderImage the last message place holder image
     * @param lastMessageWasReactionMessage the last message was reaction message
     * @param updateUnreadMessagesCount the update unread messages count
     * @param lastMessageSendersName the last message senders name
     * @param fetchRemoteConversationIfNotFoundLocally the fetch remote conversation if not found
     * locally
     */
    void updateLastMessageInConversation(String conversationId, String lastMessageText,
        String lastMessageSendersProfileImageUrl, String lastMessageTime,
        Integer lastMessagePlaceHolderImage, boolean lastMessageWasReactionMessage,
        boolean updateUnreadMessagesCount, String lastMessageSendersName, boolean fetchRemoteConversationIfNotFoundLocally);

    /**
     * On conversation settings updated.
     *
     * @param conversationId the conversation id
     * @param typingEvents the typing events
     * @param readEvents the read events
     */
    void onConversationSettingsUpdated(String conversationId, Boolean typingEvents,
        Boolean readEvents);

    /**
     * On conversation joined successfully.
     *
     * @param conversationsModel the conversations model
     */
    void onConversationJoinedSuccessfully(ConversationsModel conversationsModel);

    /**
     * Update conversation members count.
     *
     * @param conversationId the conversation id
     * @param membersCount the members count
     */
    void updateConversationMembersCount(String conversationId, int membersCount);

    /**
     * Fetch unread conversations count.
     */
    void fetchUnreadConversationsCount();

    /**
     * On unread messages count in conversations fetched successfully.
     *
     * @param conversationId the conversation id
     * @param count the count
     */
    void onUnreadMessagesCountInConversationsFetchedSuccessfully(String conversationId, int count);

    /**
     * On user profile image updated.
     *
     * @param userProfileImageUrl the user profile image url
     */
    void onUserProfileImageUpdated(String userProfileImageUrl);

    /**
     * On user deleted.
     */
    void onUserDeleted();

    /**
     * Connection state changed.
     *
     * @param connected whether connection to receive realtime events has been made or broken
     */
    void connectionStateChanged(boolean connected);

    /**
     * Failed to connect.
     *
     * @param errorMessage the error message
     */
    void failedToConnect(String errorMessage);

    /**
     * On remote user typing event.
     *
     * @param conversationId the conversation id
     * @param message the message
     */
    void onRemoteUserTypingEvent(String conversationId, String message);

    /**
     * On messaging status changed.
     *
     * @param conversationId the conversation id
     * @param disabled the disabled
     */
    void onMessagingStatusChanged(String conversationId, boolean disabled);

    /**
     * On conversation cleared.
     *
     * @param conversationId the conversation id
     * @param lastMessageText the last message text
     * @param lastMessageTime the last message time
     */
    void onConversationCleared(String conversationId, String lastMessageText,String lastMessageTime);
  }
}

