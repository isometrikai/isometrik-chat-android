package io.isometrik.chat.remote;

import io.isometrik.chat.builder.upload.CancelConversationImageUploadQuery;
import io.isometrik.chat.builder.upload.CancelMediaUploadQuery;
import io.isometrik.chat.builder.upload.CancelUserImageUploadQuery;
import io.isometrik.chat.builder.upload.UploadConversationImageQuery;
import io.isometrik.chat.builder.upload.UploadMediaQuery;
import io.isometrik.chat.builder.upload.UploadUserImageQuery;
import io.isometrik.chat.managers.MediaTransferManager;
import io.isometrik.chat.models.upload.UploadConversationImage;
import io.isometrik.chat.models.upload.UploadMedia;
import io.isometrik.chat.models.upload.UploadUserImage;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.upload.CancelConversationImageUploadResult;
import io.isometrik.chat.response.upload.CancelMediaUploadResult;
import io.isometrik.chat.response.upload.CancelUserImageUploadResult;
import io.isometrik.chat.response.upload.UploadConversationImageResult;
import io.isometrik.chat.response.upload.UploadMediaResult;
import io.isometrik.chat.response.upload.UploadUserImageResult;
import org.jetbrains.annotations.NotNull;

/**
 * The remote use case class containing methods for api calls for upload operations-
 * UploadMedia, UploadConversationImage and UploadUserImage.
 */
public class UploadUseCases {

  private final UploadMedia uploadMedia;
  private final UploadConversationImage uploadConversationImage;
  private final UploadUserImage uploadUserImage;

  private final MediaTransferManager mediaTransferManager;
  private final BaseResponse baseResponse;

  /**
   * Instantiates a new Upload use cases.
   *
   * @param mediaTransferManager the media transfer manager
   * @param baseResponse the base response
   */
  public UploadUseCases(MediaTransferManager mediaTransferManager, BaseResponse baseResponse) {

    this.mediaTransferManager = mediaTransferManager;
    this.baseResponse = baseResponse;

    uploadMedia = new UploadMedia();
    uploadConversationImage = new UploadConversationImage();
    uploadUserImage = new UploadUserImage();
  }

  /**
   * Upload media.
   *
   * @param uploadMediaQuery the upload media query
   * @param completionHandler the completion handler
   */
  public void uploadMedia(@NotNull UploadMediaQuery uploadMediaQuery,
      @NotNull CompletionHandler<UploadMediaResult> completionHandler) {

    uploadMedia.uploadMedia(uploadMediaQuery, completionHandler, mediaTransferManager,
        baseResponse);
  }

  /**
   * Cancel media upload.
   *
   * @param cancelMediaUploadQuery the cancel media upload query
   * @param completionHandler the completion handler
   */
  public void cancelMediaUpload(@NotNull CancelMediaUploadQuery cancelMediaUploadQuery,
      @NotNull CompletionHandler<CancelMediaUploadResult> completionHandler) {

    uploadMedia.cancelMediaUpload(cancelMediaUploadQuery, completionHandler);
  }

  /**
   * Upload conversation image.
   *
   * @param uploadConversationImageQuery the upload conversation image query
   * @param completionHandler the completion handler
   */
  public void uploadConversationImage(
      @NotNull UploadConversationImageQuery uploadConversationImageQuery,
      @NotNull CompletionHandler<UploadConversationImageResult> completionHandler) {

    uploadConversationImage.uploadConversationImage(uploadConversationImageQuery, completionHandler,
        mediaTransferManager, baseResponse);
  }

  /**
   * Cancel conversation image upload.
   *
   * @param cancelConversationImageUploadQuery the cancel conversation image upload query
   * @param completionHandler the completion handler
   */
  public void cancelConversationImageUpload(
      @NotNull CancelConversationImageUploadQuery cancelConversationImageUploadQuery,
      @NotNull CompletionHandler<CancelConversationImageUploadResult> completionHandler) {

    uploadConversationImage.cancelConversationImageUpload(cancelConversationImageUploadQuery,
        completionHandler);
  }

  /**
   * Upload user image.
   *
   * @param uploadUserImageQuery the upload user image query
   * @param completionHandler the completion handler
   */
  public void uploadUserImage(@NotNull UploadUserImageQuery uploadUserImageQuery,
      @NotNull CompletionHandler<UploadUserImageResult> completionHandler) {

    uploadUserImage.uploadUserImage(uploadUserImageQuery, completionHandler, mediaTransferManager,
        baseResponse);
  }

  /**
   * Cancel user image upload.
   *
   * @param cancelUserImageUploadQuery the cancel user image upload query
   * @param completionHandler the completion handler
   */
  public void cancelUserImageUpload(@NotNull CancelUserImageUploadQuery cancelUserImageUploadQuery,
      @NotNull CompletionHandler<CancelUserImageUploadResult> completionHandler) {

    uploadUserImage.cancelUserImageUpload(cancelUserImageUploadQuery, completionHandler);
  }
}
