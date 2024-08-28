package io.isometrik.chat.models.download.mediautils;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import org.jetbrains.annotations.NotNull;

/**
 * The custom response body for download progress response.
 */
public class DownloadProgressResponseBody extends ResponseBody {

  private final ResponseBody responseBody;
  private final DownloadProgressListener progressListener;
 private final String messageId;
  private BufferedSource bufferedSource;

  /**
   * Instantiates a new Download progress response body.
   *
   * @param responseBody the response body
   * @param progressListener the progress listener
   * @param messageId the message id
   */
  DownloadProgressResponseBody(ResponseBody responseBody, DownloadProgressListener progressListener,String messageId) {
    this.responseBody = responseBody;
    this.progressListener = progressListener;
    this.messageId=messageId;
  }

  @Override
  public MediaType contentType() {
    return responseBody.contentType();
  }

  @Override
  public long contentLength() {
    return responseBody.contentLength();
  }

  @Override
  public @NotNull BufferedSource source() {
    if (bufferedSource == null) {
      bufferedSource = Okio.buffer(source(responseBody.source()));
    }
    return bufferedSource;
  }

  private Source source(Source source) {
    return new ForwardingSource(source) {
      long totalBytesRead = 0L;

      @Override
      public long read(@NotNull Buffer sink, long byteCount) throws IOException {
        long bytesRead = super.read(sink, byteCount);
        // read() returns the number of bytes read, or -1 if this source is exhausted.
        totalBytesRead += bytesRead != -1 ? bytesRead : 0;
        progressListener.onDownloadProgress(messageId,totalBytesRead, responseBody.contentLength(),
            bytesRead == -1);
        return bytesRead;
      }
    };
  }
}
