package io.isometrik.chat.response.download;

import java.io.InputStream;

/**
 * The helper class to parse the response of the download media request.
 */
public class DownloadMediaResult {
  private final InputStream stream;
  private final long contentLength;
  private final String downloadedMediaPath;

  /**
   * Instantiates a new Download media result.
   *
   * @param stream the stream
   * @param contentLength the content length
   * @param downloadedMediaPath the downloaded media path
   */
  public DownloadMediaResult(InputStream stream, long contentLength,String downloadedMediaPath) {
    this.stream = stream;
    this.contentLength = contentLength;
    this.downloadedMediaPath=downloadedMediaPath;
  }

  /**
   * Gets stream.
   *
   * @return the stream
   */
  public InputStream getStream() {
    return stream;
  }

  /**
   * Gets content length.
   *
   * @return the content length
   */
  public long getContentLength() {
    return contentLength;
  }

  /**
   * Gets downloaded media path.
   *
   * @return the downloaded media path
   */
  public String getDownloadedMediaPath() {
    return downloadedMediaPath;
  }
}
