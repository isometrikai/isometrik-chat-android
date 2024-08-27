package io.isometrik.chat.models.upload;

import android.webkit.MimeTypeMap;
import io.isometrik.chat.builder.upload.CancelMediaUploadQuery;
import io.isometrik.chat.builder.upload.UploadMediaQuery;
import io.isometrik.chat.managers.MediaTransferManager;
import io.isometrik.chat.managers.MediaUploadManager;
import io.isometrik.chat.models.upload.utils.UploadProgressInterceptor;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import io.isometrik.chat.response.upload.CancelMediaUploadResult;
import io.isometrik.chat.response.upload.UploadMediaResult;
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
 * The helper class to validate request params and make the upload media request and expose the parsed successful or error response.
 */
public class UploadMedia {

  private final Map<String, Object> uploadRequestsMap = new HashMap<>();

  /**
   * Upload media.
   *
   * @param uploadMediaQuery the upload media query
   * @param completionHandler the completion handler
   * @param mediaTransferManager the media transfer manager
   * @param baseResponse the base response
   */
  public void uploadMedia(@NotNull UploadMediaQuery uploadMediaQuery,
      @NotNull final CompletionHandler<UploadMediaResult> completionHandler,
      @NotNull MediaTransferManager mediaTransferManager,
      @NotNull final BaseResponse baseResponse) {

    String mediaPath = uploadMediaQuery.getMediaPath();
    String mediaId = uploadMediaQuery.getMediaId();
    String presignedUrl = uploadMediaQuery.getPresignedUrl();
    String localMessageId = uploadMediaQuery.getLocalMessageId();

    if (presignedUrl == null || presignedUrl.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PRESIGNED_URL_MISSING);
    } else if (mediaPath == null || mediaPath.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIA_PATH_MISSING);
    } else if (mediaId == null || mediaId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIAID_MISSING);
    } else if (localMessageId == null || localMessageId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_LOCAL_MESSAGE_ID_MISSING);
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

      //Log.d("log11", contentType);
      //Log.d("log12", extension);
      //Log.d("log13", mediaId);
      //Log.d("log14", mediaPath);
      //Log.d("log15", presignedUrl);
      RequestBody requestBody = RequestBody.create(file, MediaType.parse(contentType));
      //MultipartBody.Part mediaPart =
      //    MultipartBody.Part.createFormData("file", file.getName(), requestBody);

      Call<ResponseBody> call;
      MediaUploadManager mediaUploadManager = null;
      if (uploadMediaQuery.getUploadProgressListener() == null) {

        call = mediaTransferManager.getMediaUploadService()
            .uploadMedia(presignedUrl, contentType, requestBody);
      } else {
        mediaUploadManager = new MediaUploadManager(
            new UploadProgressInterceptor(uploadMediaQuery.getUploadProgressListener(), mediaId,
                localMessageId));
        call = mediaUploadManager.getMediaUploadService()
            .uploadMedia(presignedUrl, contentType, requestBody);
      }
      uploadRequestsMap.put(mediaId, call);

      MediaUploadManager finalMediaUploadManager = mediaUploadManager;
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(@NotNull Call<ResponseBody> call,
            @NotNull Response<ResponseBody> response) {

          if (response.isSuccessful()) {
            if (response.body() != null) {
              completionHandler.onComplete(new UploadMediaResult(mediaId, localMessageId), null);
              uploadRequestsMap.remove(mediaId);
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
          //uploadRequestsMap.remove(mediaId);
          //if (finalMediaUploadManager != null) {
          //  finalMediaUploadManager.destroy(true);
          //}
        }

        @Override
        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

          if (call.isCanceled()) {
            // Upload canceled error
            completionHandler.onComplete(null,
                IsometrikErrorBuilder.IMERROBJ_MEDIA_UPLOAD_CANCELED);
          } else {
            if (t instanceof IOException) {
              // Network failure
              completionHandler.onComplete(null, baseResponse.handleNetworkError(t));
            } else {

              // Parsing error
              completionHandler.onComplete(null, baseResponse.handleParsingError(t));
            }
          }
          uploadRequestsMap.remove(mediaId);
          if (finalMediaUploadManager != null) {
            finalMediaUploadManager.destroy(true);
          }
        }
      });
    }
    //}
  }

  /**
   * Cancel media upload.
   *
   * @param cancelMediaUploadQuery the cancel media upload query
   * @param completionHandler the completion handler
   */
  public void cancelMediaUpload(@NotNull CancelMediaUploadQuery cancelMediaUploadQuery,
      @NotNull final CompletionHandler<CancelMediaUploadResult> completionHandler) {

    String mediaId = cancelMediaUploadQuery.getMediaId();
    if (mediaId == null || mediaId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIAID_MISSING);
    } else {
      @SuppressWarnings("unchecked")
      Call<UploadMediaResult> call = (Call<UploadMediaResult>) uploadRequestsMap.get(mediaId);

      if (call != null) {
        call.cancel();
        uploadRequestsMap.remove(mediaId);
        completionHandler.onComplete(
            new CancelMediaUploadResult("Media upload canceled successfully"), null);
      } else {
        completionHandler.onComplete(null,
            IsometrikErrorBuilder.IMERROBJ_CANCEL_UPLOAD_REQUEST_NOT_FOUND);
      }
    }
  }
}
