package io.isometrik.ui.conversations.details.userconversation;

import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil;
import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.ui.conversations.gallery.GalleryMediaItemsSettingsUtil;
import io.isometrik.ui.conversations.gallery.GalleryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface user conversation details contract containing presenter and view interfaces
 * implemented
 * by UserConversationDetailsPresenter and UserConversationDetailsActivity respectively.
 */
public interface UserConversationDetailsContract {
  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Delete conversation.
     *
     * @param conversationId the conversation id
     */
    void deleteConversation(String conversationId);

    /**
     * Clear conversation.
     *
     * @param conversationId the conversation id
     */
    void clearConversation(String conversationId);

    /**
     * Request conversation details.
     *
     * @param conversationId the conversation id
     * @param includeMembers the include members
     */
    void requestConversationDetails(String conversationId, boolean includeMembers);

    /**
     * Update conversation settings.
     *
     * @param conversationId the conversation id
     * @param enableTypingMessage the enable typing message
     * @param enableDeliveryReadEvents the enable delivery read events
     * @param enableNotifications the enable notifications
     */
    void updateConversationSettings(String conversationId, Boolean enableTypingMessage,
        Boolean enableDeliveryReadEvents, Boolean enableNotifications);

    /**
     * Fetch gallery items.
     *
     * @param conversationId the conversation id
     * @param galleryItemsEnabled the gallery items enabled
     */
    void fetchGalleryItems(String conversationId, List<String> galleryItemsEnabled);

    /**
     * Gets gallery media items settings util.
     *
     * @return the gallery media items settings util
     */
    GalleryMediaItemsSettingsUtil getGalleryMediaItemsSettingsUtil();

    /**
     * Block user.
     *
     * @param userId the user id
     */
    void blockUser(String userId);

    /**
     * Register user event listener.
     */
    void registerUserEventListener();

    /**
     * Unregister user event listener.
     */
    void unregisterUserEventListener();
  }

  /**
   * The interface View.
   */
  interface View {
    /**
     * On failed to fetch conversation details.
     *
     * @param error the error
     */
    void onFailedToFetchConversationDetails(String error);

    /**
     * On conversation deleted successfully.
     */
    void onConversationDeletedSuccessfully();

    /**
     * On conversation cleared successfully.
     */
    void onConversationClearedSuccessfully();

    /**
     * On error.
     *
     * @param errorMessage the error message
     */
    void onError(String errorMessage);

    /**
     * On conversation details fetched successfully.
     *
     * @param conversationDetails the conversation details
     * @param isAdmin the is admin
     * @param userDetails the user details
     */
    void onConversationDetailsFetchedSuccessfully(ConversationDetailsUtil conversationDetails,
        boolean isAdmin, MembersWatchersModel userDetails);

    /**
     * On gallery items fetched successfully.
     *
     * @param galleryItems the gallery items
     * @param hasMoreItems the has more items
     */
    void onGalleryItemsFetchedSuccessfully(ArrayList<GalleryModel> galleryItems,
        boolean hasMoreItems);

    /**
     * On conversation setting updated successfully.
     *
     * @param enableTypingMessage the enable typing message
     * @param enableDeliveryReadEvents the enable delivery read events
     * @param enableNotifications the enable notifications
     */
    void onConversationSettingUpdatedSuccessfully(Boolean enableTypingMessage,
        Boolean enableDeliveryReadEvents, Boolean enableNotifications);

    /**
     * On failed to fetch gallery items.
     *
     * @param error the error
     */
    void onFailedToFetchGalleryItems(String error);

    /**
     * On user blocked.
     */
    void onUserBlocked();
  }
}

