package io.isometrik.chat.models.download.mediautils;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

/**
 * The custom interceptor to track download progress.
 */
public class DownloadProgressInterceptor implements Interceptor {

  private final DownloadProgressListener downloadProgressListener;
  private final String messageId;

  /**
   * Instantiates a new Download progress interceptor.
   *
   * @param downloadProgressListener the download progress listener
   * @param messageId the message id
   */
  public DownloadProgressInterceptor(DownloadProgressListener downloadProgressListener, String messageId) {
    this.downloadProgressListener = downloadProgressListener;
    this.messageId = messageId;
  }

  @Override
  public @NotNull Response intercept(Chain chain) throws IOException {
    Response originalResponse = chain.proceed(chain.request());

    return originalResponse.newBuilder()
        .body(new DownloadProgressResponseBody(originalResponse.body(), downloadProgressListener, messageId))
        .build();
  }
}