package io.isometrik.ui.conversations.details.groupconversation;

import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil;
import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.ui.conversations.gallery.GalleryMediaItemsSettingsUtil;
import io.isometrik.ui.conversations.gallery.GalleryModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The interface conversation details contract containing presenter and view interfaces implemented
 * by ConversationDetailsPresenter and ConversationDetailsActivity respectively.
 */
public interface ConversationDetailsContract {
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
     * Leave conversation.
     *
     * @param conversationId the conversation id
     */
    void leaveConversation(String conversationId);

    /**
     * Request conversation details.
     *
     * @param conversationId the conversation id
     * @param includeMembers the include members
     */
    void requestConversationDetails(String conversationId, boolean includeMembers);

    /**
     * Kick out members.
     *
     * @param conversationId the conversation id
     * @param memberIds the member ids
     */
    void kickOutMembers(String conversationId, List<String> memberIds);

    /**
     * Make admin.
     *
     * @param conversationId the conversation id
     * @param memberId the member id
     */
    void makeAdmin(String conversationId, String memberId);

    /**
     * Revoke admin permissions.
     *
     * @param conversationId the conversation id
     * @param memberId the member id
     */
    void revokeAdminPermissions(String conversationId, String memberId);

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
     * Update conversation title.
     *
     * @param conversationId the conversation id
     * @param conversationTitle the conversation title
     * @param oldConversationTitle the old conversation title
     */
    void updateConversationTitle(String conversationId, String conversationTitle,
        String oldConversationTitle);

    /**
     * Fetch gallery items.
     *
     * @param conversationId the conversation id
     * @param galleryItemsEnabled the gallery items enabled
     */
    void fetchGalleryItems(String conversationId, List<String> galleryItemsEnabled);

    /**
     * Fetch conversation members.
     *
     * @param conversationId the conversation id
     */
    void fetchConversationMembers(String conversationId);

    /**
     * Gets gallery media items settings util.
     *
     * @return the gallery media items settings util
     */
    GalleryMediaItemsSettingsUtil getGalleryMediaItemsSettingsUtil();

    /**
     * Request image upload.
     *
     * @param requestId the request id
     * @param conversationId the conversation id
     * @param mediaPath the media path
     */
    void requestImageUpload(String requestId, String conversationId, String mediaPath);

    /**
     * Delete image.
     *
     * @param file the file to be deleted
     */
    void deleteImage(File file);

    /**
     * Update conversation image.
     *
     * @param conversationId the conversation id
     * @param conversationImageUrl the conversation image url
     */
    void updateConversationImage(String conversationId, String conversationImageUrl);

    /**
     * Cancel conversation image upload.
     */
    void cancelConversationImageUpload();
  }

  /**
   * The interface View.
   */
  interface View {

    /**
     * On conversation deleted successfully.
     */
    void onConversationDeletedSuccessfully();

    /**
     * On conversation cleared successfully.
     */
    void onConversationClearedSuccessfully();

    /**
     * On failed to fetch conversation details.
     *
     * @param error the error
     */
    void onFailedToFetchConversationDetails(String error);

    /**
     * On conversation left successfully.
     */
    void onConversationLeftSuccessfully();

    /**
     * On conversation title updated.
     *
     * @param conversationTitle the conversation title
     */
    void onConversationTitleUpdated(String conversationTitle);

    /**
     * On conversation title update failed.
     *
     * @param error the error
     */
    void onConversationTitleUpdateFailed(String error);

    /**
     * On conversation details fetched successfully.
     *
     * @param conversationDetails the conversation details
     * @param conversationCreationDetails the conversation creation details
     * @param isAdmin the is admin
     * @param conversationMembers the conversation members
     */
    void onConversationDetailsFetchedSuccessfully(ConversationDetailsUtil conversationDetails,
        String conversationCreationDetails, boolean isAdmin,
        ArrayList<MembersWatchersModel> conversationMembers);

    /**
     * On gallery items fetched successfully.
     *
     * @param galleryItems the gallery items
     * @param hasMoreItems the has more items
     */
    void onGalleryItemsFetchedSuccessfully(ArrayList<GalleryModel> galleryItems,
        boolean hasMoreItems);

    /**
     * On member removed successfully.
     *
     * @param memberId the member id
     */
    void onMemberRemovedSuccessfully(String memberId);

    /**
     * On member admin permissions updated.
     *
     * @param memberId the member id
     * @param admin the admin
     */
    void onMemberAdminPermissionsUpdated(String memberId, boolean admin);

    /**
     * On error.
     *
     * @param errorMessage the error message
     */
    void onError(String errorMessage);

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
     * On conversation members fetched.
     *
     * @param conversationMembers the conversation members
     * @param membersCount the members count
     * @param isUserAnAdmin the is user an admin
     * @param userId the user id
     */
    void onConversationMembersFetched(ArrayList<MembersWatchersModel> conversationMembers,
        int membersCount, boolean isUserAnAdmin, String userId);

    /**
     * On image upload result.
     *
     * @param url the url of the image uploaded
     */
    void onImageUploadResult(String url);

    /**
     * On conversation image updated.
     */
    void onConversationImageUpdated();

    /**
     * On conversation image update failed.
     *
     * @param error the error
     */
    void onConversationImageUpdateFailed(String error);

    /**
     * On failed to fetch gallery items.
     *
     * @param error the error
     */
    void onFailedToFetchGalleryItems(String error);

    /**
     * On upload progress update.
     *
     * @param progress the progress
     */
    void onUploadProgressUpdate(int progress);

    /**
     * On image upload error.
     *
     * @param errorMessage the error message containing details of the error encountered while
     * trying to upload image
     */
    void onImageUploadError(String errorMessage);
  }
}

