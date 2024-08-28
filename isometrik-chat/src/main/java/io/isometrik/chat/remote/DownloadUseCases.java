package io.isometrik.chat.remote;

import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.download.CancelMediaDownloadQuery;
import io.isometrik.chat.builder.download.DownloadMediaQuery;
import io.isometrik.chat.managers.MediaTransferManager;
import io.isometrik.chat.models.download.DownloadMedia;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.download.CancelMediaDownloadResult;
import io.isometrik.chat.response.download.DownloadMediaResult;
import io.isometrik.chat.response.error.BaseResponse;
import org.jetbrains.annotations.NotNull;

/**
 * The remote use case class containing methods for api calls for download operations for DownloadMedia.
 */
public class DownloadUseCases {

  private final DownloadMedia downloadMedia;

  private final IMConfiguration configuration;
  private final MediaTransferManager mediaTransferManager;
  private final BaseResponse baseResponse;

  /**
   * Instantiates a new Download use cases.
   *
   * @param configuration the configuration
   * @param mediaTransferManager the media transfer manager
   * @param baseResponse the base response
   */
  public DownloadUseCases(IMConfiguration configuration, MediaTransferManager mediaTransferManager,
      BaseResponse baseResponse) {
    this.configuration = configuration;
    this.mediaTransferManager = mediaTransferManager;
    this.baseResponse = baseResponse;

    downloadMedia = new DownloadMedia();
  }

  /**
   * Download media.
   *
   * @param downloadMediaQuery the download media query
   * @param completionHandler the completion handler
   */
  public void downloadMedia(@NotNull DownloadMediaQuery downloadMediaQuery,
      @NotNull CompletionHandler<DownloadMediaResult> completionHandler) {

    downloadMedia.downloadMedia(downloadMediaQuery, completionHandler, mediaTransferManager,
        baseResponse,configuration);
  }

  /**
   * Cancel media download.
   *
   * @param cancelMediaDownloadQuery the cancel media download query
   * @param completionHandler the completion handler
   */
  public void cancelMediaDownload(@NotNull CancelMediaDownloadQuery cancelMediaDownloadQuery,
      @NotNull CompletionHandler<CancelMediaDownloadResult> completionHandler) {

    downloadMedia.cancelMediaDownload(cancelMediaDownloadQuery, completionHandler);
  }
}
