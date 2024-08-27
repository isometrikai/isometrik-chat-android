package io.isometrik.chat.models.download;

import android.media.MediaScannerConnection;
import android.os.Build;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.download.CancelMediaDownloadQuery;
import io.isometrik.chat.builder.download.DownloadMediaQuery;
import io.isometrik.chat.enums.DownloadMediaType;
import io.isometrik.chat.managers.MediaDownloadManager;
import io.isometrik.chat.managers.MediaTransferManager;
import io.isometrik.chat.models.download.mediautils.AndroidQMediaUtil;
import io.isometrik.chat.models.download.mediautils.DownloadFolderPathUtility;
import io.isometrik.chat.models.download.mediautils.DownloadProgressInterceptor;
import io.isometrik.chat.models.download.mediautils.SaveDownloadedMediaUtil;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.download.CancelMediaDownloadResult;
import io.isometrik.chat.response.download.DownloadMediaResult;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the download media request and expose the parsed successful or error response.
 */
public class DownloadMedia {

  private final Map<String, Object> downloadRequestsMap = new HashMap<>();

  /**
   * Download media.
   *
   * @param downloadMediaQuery the download media query
   * @param completionHandler the completion handler
   * @param mediaTransferManager the media transfer manager
   * @param baseResponse the base response
   * @param configuration the configuration
   */
  public void downloadMedia(@NotNull DownloadMediaQuery downloadMediaQuery,
      @NotNull final CompletionHandler<DownloadMediaResult> completionHandler,
      @NotNull MediaTransferManager mediaTransferManager, @NotNull final BaseResponse baseResponse,
      IMConfiguration configuration) {

    String mediaUrl = downloadMediaQuery.getMediaUrl();
    String messageId = downloadMediaQuery.getMessageId();
    String mediaExtension = downloadMediaQuery.getMediaExtension();
    String mimeType = downloadMediaQuery.getMimeType();
    String applicationName = downloadMediaQuery.getApplicationName();
    DownloadMediaType downloadMediaType = downloadMediaQuery.getDownloadMediaType();

    if (messageId == null || messageId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGEID_MISSING);
    } else if (mediaUrl == null || mediaUrl.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIA_URL_MISSING);
    } else if (mediaExtension == null || mediaExtension.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIA_EXTENSION_MISSING);
    } else if (mimeType == null || mimeType.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIA_EXTENSION_MISSING);
    } else if (applicationName == null || applicationName.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_APPLICATION_NAME_MISSING);
    } else if (downloadMediaType == null) {
      completionHandler.onComplete(null,
          IsometrikErrorBuilder.IMERROBJ_DOWNLOAD_MEDIA_TYPE_MISSING);
    } else {

      Call<ResponseBody> call;
      MediaDownloadManager mediaDownloadManager = null;
      if (downloadMediaQuery.getDownloadProgressListener() == null) {

        call = mediaTransferManager.getMediaDownloadService().downloadMediaAsync(mediaUrl);
      } else {
        mediaDownloadManager = new MediaDownloadManager(
            new DownloadProgressInterceptor(downloadMediaQuery.getDownloadProgressListener(),
                messageId));
        call = mediaDownloadManager.getMediaDownloadService().downloadMediaAsync(mediaUrl);
      }
      downloadRequestsMap.put(messageId, call);

      MediaDownloadManager finalMediaDownloadManager = mediaDownloadManager;
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(@NotNull Call<ResponseBody> call,
            @NotNull Response<ResponseBody> response) {

          if (response.isSuccessful()) {

            if (response.body() != null) {

              ExecutorService executor = Executors.newSingleThreadExecutor();

              executor.execute(() -> {

                String downloadedMediaPath =
                    DownloadFolderPathUtility.getDownloadFolderPath(configuration.getContext(),
                        applicationName, downloadMediaType)
                        + "/"
                        + messageId
                        + "."
                        + mediaExtension;

                boolean writtenToDisk = SaveDownloadedMediaUtil.
                    writeDownloadedResponseBodyToDisk(response.body(), downloadedMediaPath,
                        messageId, downloadMediaQuery.getDownloadProgressListener());

                if (writtenToDisk) {
                  //try {
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    downloadedMediaPath = AndroidQMediaUtil.addMediaToGallery(
                        messageId + "_" + System.currentTimeMillis() + "." + mediaExtension,
                        configuration.getContext(), new File(downloadedMediaPath), applicationName,
                        mimeType);
                  } else {
                    MediaScannerConnection.scanFile(configuration.getContext(),
                        new String[] { downloadedMediaPath }, null, null);
                  }
                  downloadRequestsMap.remove(messageId);
                  if (finalMediaDownloadManager != null) {
                    finalMediaDownloadManager.destroy(true);
                  }
                  completionHandler.onComplete(new DownloadMediaResult(response.body().byteStream(),
                      response.body().contentLength(), downloadedMediaPath), null);
                  //} catch (Exception ignore) {
                  //
                  //  //Handle case when getting null uri exception due to duplicate file name
                  //
                  //  completionHandler.onComplete(null,
                  //      IsometrikErrorBuilder.IMERROBJ_DOWNLOAD_MEDIA_FAILED);
                  //}
                } else {
                  //Download media failed due to network issue or explicit cancel
                  downloadRequestsMap.remove(messageId);
                  if (finalMediaDownloadManager != null) {
                    finalMediaDownloadManager.destroy(true);
                  }
                  completionHandler.onComplete(null,
                      IsometrikErrorBuilder.IMERROBJ_MEDIA_DOWNLOAD_CANCELED_OR_NETWORK_ERROR);
                }
              });
            } else {

              //Download media failed
              downloadRequestsMap.remove(messageId);
              if (finalMediaDownloadManager != null) {
                finalMediaDownloadManager.destroy(true);
              }
              completionHandler.onComplete(null,
                  IsometrikErrorBuilder.IMERROBJ_DOWNLOAD_MEDIA_FAILED);
            }
          } else {

            //Media not present at the url
            downloadRequestsMap.remove(messageId);
            if (finalMediaDownloadManager != null) {
              finalMediaDownloadManager.destroy(true);
            }
            completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIA_NOT_FOUND);
          }


        }

        @Override
        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {


          if (call.isCanceled()) {

            // Download canceled error
            completionHandler.onComplete(null,
                IsometrikErrorBuilder.IMERROBJ_MEDIA_DOWNLOAD_CANCELED);
          } else {

            if (t instanceof IOException) {
              // Network failure
              completionHandler.onComplete(null, baseResponse.handleNetworkError(t));
            } else {
              // Parsing error
              completionHandler.onComplete(null, baseResponse.handleParsingError(t));
            }
          }
          downloadRequestsMap.remove(messageId);

          if (finalMediaDownloadManager != null) {
            finalMediaDownloadManager.destroy(true);
          }
        }
      });
    }
  }

  /**
   * Cancel media download.
   *
   * @param cancelMediaDownloadQuery the cancel media download query
   * @param completionHandler the completion handler
   */
  public void cancelMediaDownload(@NotNull CancelMediaDownloadQuery cancelMediaDownloadQuery,
      @NotNull final CompletionHandler<CancelMediaDownloadResult> completionHandler) {

    String messageId = cancelMediaDownloadQuery.getMessageId();
    if (messageId == null || messageId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGEID_MISSING);
    } else {
      @SuppressWarnings("unchecked")
      Call<DownloadMediaResult> call =
          (Call<DownloadMediaResult>) downloadRequestsMap.get(messageId);

      if (call != null) {
        call.cancel();
        downloadRequestsMap.remove(messageId);
        completionHandler.onComplete(
            new CancelMediaDownloadResult("Media download canceled successfully"), null);
      } else {
        completionHandler.onComplete(null,
            IsometrikErrorBuilder.IMERROBJ_CANCEL_DOWNLOAD_REQUEST_NOT_FOUND);
      }
    }
  }
}
