package io.isometrik.chat.models.upload;

import android.webkit.MimeTypeMap;
import io.isometrik.chat.builder.upload.CancelUserImageUploadQuery;
import io.isometrik.chat.builder.upload.UploadUserImageQuery;
import io.isometrik.chat.managers.MediaTransferManager;
import io.isometrik.chat.managers.MediaUploadManager;
import io.isometrik.chat.models.upload.utils.UploadProgressInterceptor;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import io.isometrik.chat.response.upload.CancelUserImageUploadResult;
import io.isometrik.chat.response.upload.UploadUserImageResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the upload user image request and expose the parsed successful or error response.
 */
public class UploadUserImage {

  private final Map<String, Object> uploadRequestsMap = new HashMap<>();

  /**
   * Upload user image.
   *
   * @param uploadUserImageQuery the upload user image query
   * @param completionHandler the completion handler
   * @param mediaTransferManager the media transfer manager
   * @param baseResponse the base response
   */
  public void uploadUserImage(@NotNull UploadUserImageQuery uploadUserImageQuery,
      @NotNull final CompletionHandler<UploadUserImageResult> completionHandler,
      @NotNull MediaTransferManager mediaTransferManager, @NotNull final BaseResponse baseResponse) {

    String mediaPath = uploadUserImageQuery.getMediaPath();
    String requestId = uploadUserImageQuery.getRequestId();
    String presignedUrl = uploadUserImageQuery.getPresignedUrl();

    if (presignedUrl == null || presignedUrl.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PRESIGNED_URL_MISSING);
    } else if (mediaPath == null || mediaPath.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIA_PATH_MISSING);
    } else if (requestId == null || requestId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_REQUESTID_MISSING);
    } else {

      File file = new File(mediaPath);

      String contentType, extension;
      try {
        extension = FilenameUtils.getExtension(mediaPath);
      } catch (IllegalArgumentException ignore) {
        extension = "";
      }

      //if (extension.isEmpty()) {
      //  //Media type not supported as failed to get extension
      //  completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIA_NOT_SUPPORTED);
      //} else {
      contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
      if (contentType == null) {
        //try {
        //  contentType = new Tika().detect(file);
        //} catch (IOException ignore) {
        contentType = "application/octet-stream";
        //}
      }

      RequestBody requestBody = RequestBody.create(file, MediaType.parse(contentType));
      //MultipartBody.Part mediaPart =
      //    MultipartBody.Part.createFormData("file", file.getName(), requestBody);

      Call<ResponseBody> call;
      MediaUploadManager mediaUploadManager = null;
      if (uploadUserImageQuery.getUploadProgressListener() == null) {

        call = mediaTransferManager.getMediaUploadService()
            .uploadMedia(presignedUrl, contentType, requestBody);
      } else {
        mediaUploadManager = new MediaUploadManager(
            new UploadProgressInterceptor(uploadUserImageQuery.getUploadProgressListener(),
                requestId, requestId));
        call = mediaUploadManager.getMediaUploadService()
            .uploadMedia(presignedUrl, contentType, requestBody);
      }
      uploadRequestsMap.put(requestId, call);
      MediaUploadManager finalMediaUploadManager = mediaUploadManager;
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(@NotNull Call<ResponseBody> call,
            @NotNull Response<ResponseBody> response) {

          if (response.isSuccessful()) {
            if (response.body() != null) {
              completionHandler.onComplete(new UploadUserImageResult(requestId), null);
              uploadRequestsMap.remove(requestId);
              if (finalMediaUploadManager != null) {
                finalMediaUploadManager.destroy(true);
              }
            } else {
              //Upload media failed
              completionHandler.onComplete(null,
                  IsometrikErrorBuilder.IMERROBJ_UPLOAD_MEDIA_FAILED);
            }
          } else {
            //Upload media failed
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_UPLOAD_MEDIA_FAILED);
          }
          uploadRequestsMap.remove(requestId);
        }

        @Override
        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

          if (call.isCanceled()) {
            // Upload canceled error
            completionHandler.onComplete(null,
                IsometrikErrorBuilder.IMERROBJ_USER_IMAGE_UPLOAD_CANCELED);
          } else {
            if (t instanceof IOException) {
              // Network failure
              completionHandler.onComplete(null, baseResponse.handleNetworkError(t));
            } else {

              // Parsing error
              completionHandler.onComplete(null, baseResponse.handleParsingError(t));
            }
          }
          uploadRequestsMap.remove(requestId);
          if (finalMediaUploadManager != null) {
            finalMediaUploadManager.destroy(true);
          }
        }
      });
    }
    //}
  }

  /**
   * Cancel user image upload.
   *
   * @param cancelUserImageUploadQuery the cancel user image upload query
   * @param completionHandler the completion handler
   */
  public void cancelUserImageUpload(@NotNull CancelUserImageUploadQuery cancelUserImageUploadQuery,
      @NotNull final CompletionHandler<CancelUserImageUploadResult> completionHandler) {

    String requestId = cancelUserImageUploadQuery.getRequestId();
    if (requestId == null || requestId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_REQUESTID_MISSING);
    } else {
      @SuppressWarnings("unchecked")
      Call<UploadUserImageResult> call =
          (Call<UploadUserImageResult>) uploadRequestsMap.get(requestId);

      if (call != null) {
        call.cancel();
        uploadRequestsMap.remove(requestId);
        completionHandler.onComplete(
            new CancelUserImageUploadResult("User image upload canceled successfully"), null);
      } else {
        completionHandler.onComplete(null,
            IsometrikErrorBuilder.IMERROBJ_CANCEL_UPLOAD_REQUEST_NOT_FOUND);
      }
    }
  }
}
