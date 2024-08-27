package io.isometrik.chat.builder.download;

import io.isometrik.chat.enums.DownloadMediaType;
import io.isometrik.chat.models.download.mediautils.DownloadProgressListener;

/**
 * The query builder for downloading media request.
 */
public class DownloadMediaQuery {

  private final String messageId;
  private final String mediaUrl;
  private final String mediaExtension;
  private final String mimeType;
  private final DownloadMediaType downloadMediaType;
  private final String applicationName;
  private final DownloadProgressListener downloadProgressListener;

  private DownloadMediaQuery(Builder builder) {
    this.messageId = builder.messageId;
    this.mediaUrl = builder.mediaUrl;
    this.downloadMediaType = builder.downloadMediaType;
    this.mediaExtension = builder.mediaExtension;
    this.mimeType = builder.mimeType;
    this.applicationName = builder.applicationName;
    this.downloadProgressListener = builder.downloadProgressListener;
  }

  /**
   * The builder class for building download media query.
   */
  public static class Builder {
    private String messageId;
    private String mediaUrl;
    private String mediaExtension;
    private String mimeType;
    private DownloadMediaType downloadMediaType;
    private String applicationName;
    private DownloadProgressListener downloadProgressListener;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     * @return the message id
     */
    public Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Sets media url.
     *
     * @param mediaUrl the media url
     * @return the media url
     */
    public Builder setMediaUrl(String mediaUrl) {
      this.mediaUrl = mediaUrl;
      return this;
    }

    /**
     * Sets download media type.
     *
     * @param downloadMediaType the download media type
     * @return the download media type
     */
    public Builder setDownloadMediaType(DownloadMediaType downloadMediaType) {
      this.downloadMediaType = downloadMediaType;
      return this;
    }

    /**
     * Sets media extension.
     *
     * @param mediaExtension the media extension
     * @return the media extension
     */
    public Builder setMediaExtension(String mediaExtension) {
      this.mediaExtension = mediaExtension;
      return this;
    }

    /**
     * Sets mime type.
     *
     * @param mimeType the mime type
     * @return the mime type
     */
    public Builder setMimeType(String mimeType) {
      this.mimeType = mimeType;
      return this;
    }

    /**
     * Sets application name.
     *
     * @param applicationName the application name
     * @return the application name
     */
    public Builder setApplicationName(String applicationName) {
      this.applicationName = applicationName;
      return this;
    }

    /**
     * Sets download progress listener.
     *
     * @param downloadProgressListener the download progress listener
     * @return the download progress listener
     */
    public Builder setDownloadProgressListener(DownloadProgressListener downloadProgressListener) {
      this.downloadProgressListener = downloadProgressListener;
      return this;
    }

    /**
     * Build download media query.
     *
     * @return the download media query
     */
    public DownloadMediaQuery build() {
      return new DownloadMediaQuery(this);
    }
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets media url.
   *
   * @return the media url
   */
  public String getMediaUrl() {
    return mediaUrl;
  }

  /**
   * Gets download media type.
   *
   * @return the download media type
   */
  public DownloadMediaType getDownloadMediaType() {
    return downloadMediaType;
  }

  /**
   * Gets media extension.
   *
   * @return the media extension
   */
  public String getMediaExtension() {
    return mediaExtension;
  }

  /**
   * Gets mime type.
   *
   * @return the mime type
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Gets application name.
   *
   * @return the application name
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * Gets download progress listener.
   *
   * @return the download progress listener
   */
  public DownloadProgressListener getDownloadProgressListener() {
    return downloadProgressListener;
  }
}
