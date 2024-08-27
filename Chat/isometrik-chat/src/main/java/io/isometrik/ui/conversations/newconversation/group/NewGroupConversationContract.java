package io.isometrik.ui.conversations.newconversation.group;

import java.io.File;
import java.util.ArrayList;

/**
 * The interface new group conversation contract containing presenter and view interfaces
 * implemented
 * by NewGroupConversationPresenter and NewGroupConversationActivity respectively.
 */
public interface NewGroupConversationContract {
  /**
   * The interface Presenter.
   */
  interface Presenter {

    /**
     * Validate conversation details.
     *
     * @param conversationTitle the conversation title
     * @param file the file
     * @param selectedUsers the selected users
     */
    void validateConversationDetails(String conversationTitle, File file,
        ArrayList<UsersModel> selectedUsers);

    ///**
    // * Request image upload.
    // *
    // * @param imagePath the local path of the image to be uploaded
    // */
    //void requestImageUpload(String imagePath);

    /**
     * Delete image.
     *
     * @param file the file to be deleted
     */
    void deleteImage(File file);

    /**
     * Create new conversation.
     *
     * @param conversationTitle the conversation title
     * @param conversationImageUrl the conversation image url
     * @param conversationType the conversation type
     * @param enablePushNotifications the enable push notifications
     * @param enableMessageTypingEvents the enable message typing events
     * @param enableMessageDeliveryReadEvents the enable message delivery read events
     * @param selectedUsers the selected users
     */
    void createNewConversation(String conversationTitle, String conversationImageUrl,
        int conversationType, boolean enablePushNotifications, boolean enableMessageTypingEvents,
        boolean enableMessageDeliveryReadEvents, ArrayList<UsersModel> selectedUsers);

    /**
     * Request users data.
     *
     * @param offset the offset
     * @param refreshRequest the refresh request
     * @param isSearchRequest whether fetch users request is from the search or not
     * @param searchTag the user's name to be searched
     */
    void fetchNonBlockedUsers(int offset, boolean refreshRequest, boolean isSearchRequest,
        String searchTag);

    /**
     * Request users data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestNonBlockedUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Request image upload.
     *
     * @param requestId the request id
     * @param conversationTitle the conversation title
     * @param mediaPath the media path
     */
    void requestImageUpload(String requestId, String conversationTitle, String mediaPath);

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
     * Conversation details validation result.
     *
     * @param errorMessage the error message
     */
    void conversationDetailsValidationResult(String errorMessage);

    /**
     * On conversation created successfully.
     *
     * @param conversationId the conversation id
     * @param conversationTitle the conversation title
     * @param conversationImageUrl the conversation image url
     * @param participantsCount the participants count
     */
    void onConversationCreatedSuccessfully(String conversationId, String conversationTitle,
        String conversationImageUrl, int participantsCount);

    /**
     * On users data received.
     *
     * @param users the users
     * @param latestUsers the latest users
     * @param isSearchRequest whether fetch users request is from the search or not
     */
    void onNonBlockedUsersFetched(ArrayList<UsersModel> users, boolean latestUsers,
        boolean isSearchRequest);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On image upload result.
     *
     * @param url the url of the image uploaded
     */
    void onImageUploadResult(String url);

    /**
     * On image upload error.
     *
     * @param errorMessage the error message containing details of the error encountered while
     * trying to upload image
     */
    void onImageUploadError(String errorMessage);

    /**
     * On upload progress update.
     *
     * @param progress the progress
     */
    void onUploadProgressUpdate(int progress);
  }
}

