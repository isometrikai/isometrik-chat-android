package io.isometrik.ui.messages.chat.utils.attachmentutils;


import io.isometrik.chat.builder.download.DownloadMediaQuery;
import io.isometrik.chat.enums.DownloadMediaType;
import io.isometrik.chat.models.download.mediautils.DownloadProgressListener;
import io.isometrik.chat.utils.enums.MessageTypeUi;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.MessagesModel;

/**
 * The helper class to prepare success and error message for media download or upload completeion.
 */
public class MediaDownloadOrUploadHelper {

  /**
   * Prepare download media query download media query parse response.
   *
   * @param messagesModel the messages model
   * @param downloadProgressListener the download progress listener
   * @return the download media query parse response
   */
  public static DownloadMediaQueryParseResponse prepareDownloadMediaQuery(
          MessagesModel messagesModel, DownloadProgressListener downloadProgressListener) {

    String downloadSuccessMessage;
    String downloadFailureMessage;

    DownloadMediaQuery.Builder downloadMediaQuery =
        new DownloadMediaQuery.Builder().setMessageId(messagesModel.getMessageId())
            .setApplicationName(IsometrikChatSdk.getInstance().getApplicationName())
            .setMimeType(messagesModel.getMimeType())
            .setMediaExtension(messagesModel.getMediaExtension())
            .setDownloadProgressListener(downloadProgressListener);

    switch (messagesModel.getCustomMessageType()) {

      case PHOTO_MESSAGE_SENT:

      case PHOTO_MESSAGE_RECEIVED: {
        downloadMediaQuery.setDownloadMediaType(DownloadMediaType.Image)
            .setMediaUrl(messagesModel.getPhotoMainUrl());
        downloadSuccessMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_downloaded_successfully,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photo));

        downloadFailureMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photo));
        break;
      }

      case VIDEO_MESSAGE_SENT:

      case VIDEO_MESSAGE_RECEIVED: {
        downloadMediaQuery.setDownloadMediaType(DownloadMediaType.Video)
            .setMediaUrl(messagesModel.getVideoMainUrl());
        downloadSuccessMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_downloaded_successfully,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_video));

        downloadFailureMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_video));

        break;
      }

      case AUDIO_MESSAGE_SENT:

      case AUDIO_MESSAGE_RECEIVED: {
        downloadMediaQuery.setDownloadMediaType(DownloadMediaType.Audio)
            .setMediaUrl(messagesModel.getAudioUrl());

        downloadSuccessMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_downloaded_successfully,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_audio_recording));

        downloadFailureMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_audio_recording));

        break;
      }

      case FILE_MESSAGE_SENT:

      case FILE_MESSAGE_RECEIVED: {
        downloadMediaQuery.setDownloadMediaType(DownloadMediaType.File)
            .setMediaUrl(messagesModel.getFileUrl());

        downloadSuccessMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_downloaded_successfully,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_file));

        downloadFailureMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_file));

        break;
      }

      case WHITEBOARD_MESSAGE_SENT:

      case WHITEBOARD_MESSAGE_RECEIVED: {
        downloadMediaQuery.setDownloadMediaType(DownloadMediaType.Whiteboard)
            .setMediaUrl(messagesModel.getWhiteboardMainUrl());
        downloadSuccessMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_downloaded_successfully,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_whiteboard));
        downloadFailureMessage = IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_whiteboard));

        break;
      }

      default:
        return null;
    }
    return new DownloadMediaQueryParseResponse(downloadSuccessMessage, downloadFailureMessage,
        downloadMediaQuery);
  }

  /**
   * The type Download media query parse response.
   */
  public static class DownloadMediaQueryParseResponse {
    private final String downloadSuccessMessage;
    private final String downloadFailureMessage;
    private final DownloadMediaQuery.Builder downloadMediaQuery;

    /**
     * Instantiates a new Download media query parse response.
     *
     * @param downloadSuccessMessage the download success message
     * @param downloadFailureMessage the download failure message
     * @param downloadMediaQuery the download media query
     */
    public DownloadMediaQueryParseResponse(String downloadSuccessMessage,
        String downloadFailureMessage, DownloadMediaQuery.Builder downloadMediaQuery) {
      this.downloadSuccessMessage = downloadSuccessMessage;
      this.downloadFailureMessage = downloadFailureMessage;
      this.downloadMediaQuery = downloadMediaQuery;
    }

    /**
     * Gets download success message.
     *
     * @return the download success message
     */
    public String getDownloadSuccessMessage() {
      return downloadSuccessMessage;
    }

    /**
     * Gets download failure message.
     *
     * @return the download failure message
     */
    public String getDownloadFailureMessage() {
      return downloadFailureMessage;
    }

    /**
     * Gets download media query.
     *
     * @return the download media query
     */
    public DownloadMediaQuery.Builder getDownloadMediaQuery() {
      return downloadMediaQuery;
    }
  }

  /**
   * Parse download media failed message string.
   *
   * @param messageTypeUi the message types for ui
   * @param errorMessage the error message
   * @return the string
   */
  public static String parseDownloadMediaFailedMessage(MessageTypeUi messageTypeUi,
      String errorMessage) {

    switch (messageTypeUi) {

      case PHOTO_MESSAGE_SENT:

      case PHOTO_MESSAGE_RECEIVED: {

        return IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed_due_to,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photo),
                errorMessage);
      }

      case VIDEO_MESSAGE_SENT:

      case VIDEO_MESSAGE_RECEIVED: {
        return IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed_due_to,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_video),
                errorMessage);
      }

      case AUDIO_MESSAGE_SENT:

      case AUDIO_MESSAGE_RECEIVED: {
        return IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed_due_to,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_audio_recording),
                errorMessage);
      }

      case FILE_MESSAGE_SENT:

      case FILE_MESSAGE_RECEIVED: {
        return IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed_due_to,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_file),
                errorMessage);
      }

      case WHITEBOARD_MESSAGE_SENT:

      case WHITEBOARD_MESSAGE_RECEIVED: {
        return IsometrikChatSdk.getInstance()
            .getContext()
            .getString(R.string.ism_download_failed_due_to,
                IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_whiteboard),
                errorMessage);
      }

      default:
        return null;
    }
  }

  /**
   * Parse media download or upload canceled message string.
   *
   * @param messageTypeUi the message types for ui
   * @param success the success
   * @param download the download
   * @return the string
   */
  public static String parseMediaDownloadOrUploadCanceledMessage(MessageTypeUi messageTypeUi,
      boolean success, boolean download) {

    String actionType=download? IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_download): IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_upload);
    
    
    switch (messageTypeUi) {

      case PHOTO_MESSAGE_SENT:

      case PHOTO_MESSAGE_RECEIVED: {

        if (success) {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_download_upload_canceled,
                  IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photo),actionType);
        } else {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_cancel_download_upload_failed,actionType,
                  IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_photo));
        }
      }

      case VIDEO_MESSAGE_SENT:

      case VIDEO_MESSAGE_RECEIVED: {
        if (success) {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_download_upload_canceled,
                  IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_video),actionType);
        } else {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_cancel_download_upload_failed,actionType,
                  IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_video));
        }
      }

      case AUDIO_MESSAGE_SENT:

      case AUDIO_MESSAGE_RECEIVED: {
        if (success) {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_download_upload_canceled, IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_audio_recording),actionType);
        } else {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_cancel_download_upload_failed,actionType, IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_audio_recording));
        }
      }

      case FILE_MESSAGE_SENT:

      case FILE_MESSAGE_RECEIVED: {
        if (success) {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_download_upload_canceled,
                  IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_file),actionType);
        } else {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_cancel_download_upload_failed,actionType,
                  IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_file));
        }
      }

      case WHITEBOARD_MESSAGE_SENT:

      case WHITEBOARD_MESSAGE_RECEIVED: {
        if (success) {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_download_upload_canceled,
                  IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_whiteboard),actionType);
        } else {
          return IsometrikChatSdk.getInstance()
              .getContext()
              .getString(R.string.ism_cancel_download_upload_failed,actionType,
                  IsometrikChatSdk.getInstance().getContext().getString(R.string.ism_whiteboard));
        }
      }

      default:
        return null;
    }
  }

}
