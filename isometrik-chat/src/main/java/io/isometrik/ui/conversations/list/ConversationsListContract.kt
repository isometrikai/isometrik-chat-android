package io.isometrik.ui.conversations.list

import io.isometrik.chat.enums.ConversationType
import io.isometrik.chat.utils.BasePresenter

/**
 * The interface conversations list contract containing presenter and view interfaces implemented
 * by ConversationsListPresenter and ConversationsListFragment respectively.
 */
interface ConversationsListContract {
    /**
     * The interface Presenter.
     */
    interface Presenter : BasePresenter<View?> {
        /**
         * Initialize.
         *
         * @param conversationType the conversation type
         */
        fun initialize(conversationType: ConversationType?)

        /**
         * Fetch conversations.
         *
         * @param skip the skip
         * @param onScroll the on scroll
         * @param conversationType the conversation type
         * @param isSearchRequest the is search request
         * @param searchTag the search tag
         */
        fun fetchConversations(
            skip: Int,
            onScroll: Boolean,
            conversationType: ConversationType?,
            isSearchRequest: Boolean,
            searchTag: String?
        )

        /**
         * Request conversations on scroll.
         *
         * @param firstVisibleItemPosition the first visible item position
         * @param visibleItemCount the visible item count
         * @param totalItemCount the total item count
         * @param conversationType whether to fetch only public/open conversations or all
         */
        fun fetchConversationsOnScroll(
            firstVisibleItemPosition: Int, visibleItemCount: Int,
            totalItemCount: Int, conversationType: ConversationType?
        )

        /**
         * Register connection event listener.
         */
        fun registerConnectionEventListener()

        /**
         * Unregister connection event listener.
         */
        fun unregisterConnectionEventListener()

        /**
         * Register conversation event listener.
         */
        fun registerConversationEventListener()

        /**
         * Unregister conversation event listener.
         */
        fun unregisterConversationEventListener()

        /**
         * Register membership control event listener.
         */
        fun registerMembershipControlEventListener()

        /**
         * Unregister membership control event listener.
         */
        fun unregisterMembershipControlEventListener()

        /**
         * Register message event listener.
         */
        fun registerMessageEventListener()

        /**
         * Unregister message event listener.
         */
        fun unregisterMessageEventListener()

        /**
         * Register reaction event listener.
         */
        fun registerReactionEventListener()

        /**
         * Unregister reaction event listener.
         */
        fun unregisterReactionEventListener()

        /**
         * Register user event listener.
         */
        fun registerUserEventListener()

        /**
         * Unregister user event listener.
         */
        fun unregisterUserEventListener()

        /**
         * Fetch conversation position in list int.
         *
         * @param conversations the conversations
         * @param conversationId the conversation id
         * @param fetchRemoteConversationIfNotFoundLocally the fetch remote conversation if not found
         * locally
         * @return the int
         */
        fun fetchConversationPositionInList(
            conversations: ArrayList<ConversationsModel>,
            conversationId: String?, fetchRemoteConversationIfNotFoundLocally: Boolean
        ): Int

        /**
         * Join conversation.
         *
         * @param conversationsModel the conversations model
         */
        fun joinConversation(conversationsModel: ConversationsModel?)
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
        fun onConversationsFetchedSuccessfully(
            conversationsModels: ArrayList<ConversationsModel>,
            resultsOnScroll: Boolean
        )

        /**
         * On error.
         *
         * @param errorMessage the error message to be shown in the toast for details of the failed
         * operation
         */
        fun onError(errorMessage: String?)

        /**
         * On conversation created.
         *
         * @param conversationsModel the conversations model
         * @param publicConversationsOnly the public conversations only
         */
        fun onConversationCreated(
            conversationsModel: ConversationsModel,
            publicConversationsOnly: Boolean
        )

        /**
         * Remove conversation.
         *
         * @param conversationId the conversation id
         */
        fun removeConversation(conversationId: String)

        /**
         * On conversation title updated.
         *
         * @param conversationId the conversation id
         * @param newTitle the new title
         */
        fun onConversationTitleUpdated(conversationId: String, newTitle: String)

        /**
         * On conversation image updated.
         *
         * @param conversationId the conversation id
         * @param conversationImageUrl the conversation image url
         */
        fun onConversationImageUpdated(conversationId: String, conversationImageUrl: String)

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
        fun updateLastMessageInConversation(
            conversationId: String,
            lastMessageText: String,
            lastMessageSendersProfileImageUrl: String,
            lastMessageTime: String,
            lastMessagePlaceHolderImage: Int?,
            lastMessageWasReactionMessage: Boolean,
            updateUnreadMessagesCount: Boolean,
            lastMessageSendersName: String,
            fetchRemoteConversationIfNotFoundLocally: Boolean
        )

        fun updateLastMessageInConversation(
            conversationId: String,
            lastMessageText: String,
            lastMessageSendersProfileImageUrl: String,
            lastMessageTime: String,
            lastMessagePlaceHolderImage: Int?,
            lastMessageWasReactionMessage: Boolean,
            updateUnreadMessagesCount: Boolean,
            lastMessageSendersName: String,
            fetchRemoteConversationIfNotFoundLocally: Boolean,
            customType : String
        )

        /**
         * On conversation settings updated.
         *
         * @param conversationId the conversation id
         * @param typingEvents the typing events
         * @param readEvents the read events
         */
        fun onConversationSettingsUpdated(
            conversationId: String, typingEvents: Boolean,
            readEvents: Boolean?
        )

        /**
         * On conversation joined successfully.
         *
         * @param conversationsModel the conversations model
         */
        fun onConversationJoinedSuccessfully(conversationsModel: ConversationsModel)

        /**
         * Update conversation members count.
         *
         * @param conversationId the conversation id
         * @param membersCount the members count
         */
        fun updateConversationMembersCount(conversationId: String, membersCount: Int)

        /**
         * Fetch unread conversations count.
         */
        fun fetchUnreadConversationsCount()

        /**
         * On unread messages count in conversations fetched successfully.
         *
         * @param conversationId the conversation id
         * @param count the count
         */
        fun onUnreadMessagesCountInConversationsFetchedSuccessfully(
            conversationId: String,
            count: Int
        )

        /**
         * On user profile image updated.
         *
         * @param userProfileImageUrl the user profile image url
         */
        fun onUserProfileImageUpdated(userProfileImageUrl: String)

        /**
         * On user deleted.
         */
        fun onUserDeleted()

        /**
         * Connection state changed.
         *
         * @param connected whether connection to receive realtime events has been made or broken
         */
        fun connectionStateChanged(connected: Boolean)

        /**
         * Failed to connect.
         *
         * @param errorMessage the error message
         */
        fun failedToConnect(errorMessage: String?)

        /**
         * On remote user typing event.
         *
         * @param conversationId the conversation id
         * @param message the message
         */
        fun onRemoteUserTypingEvent(conversationId: String, message: String)

        /**
         * On messaging status changed.
         *
         * @param conversationId the conversation id
         * @param disabled the disabled
         */
        fun onMessagingStatusChanged(conversationId: String, disabled: Boolean)

        /**
         * On conversation cleared.
         *
         * @param conversationId the conversation id
         * @param lastMessageText the last message text
         * @param lastMessageTime the last message time
         */
        fun onConversationCleared(
            conversationId: String,
            lastMessageText: String,
            lastMessageTime: String
        )
    }
}

