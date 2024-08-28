package io.isometrik.chat.builder.upload;

import io.isometrik.chat.models.upload.utils.UploadProgressListener;

/**
 * The query builder for uploading media request.
 */
public class UploadMediaQuery {

  private final String mediaPath;
  private final String mediaId;
  private final String presignedUrl;
  private final UploadProgressListener uploadProgressListener;
  private final String localMessageId;

  private UploadMediaQuery(Builder builder) {

    this.mediaPath = builder.mediaPath;
    this.mediaId = builder.mediaId;
    this.presignedUrl = builder.presignedUrl;
    this.uploadProgressListener = builder.uploadProgressListener;
    this.localMessageId = builder.localMessageId;
  }

  /**
   * The builder class for building upload media query.
   */
  public static class Builder {

    private String mediaPath;
    private String mediaId;
    private String presignedUrl;
    private UploadProgressListener uploadProgressListener;
    private String localMessageId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets media path.
     *
     * @param mediaPath the media path
     * @return the media path
     */
    public Builder setMediaPath(String mediaPath) {
      this.mediaPath = mediaPath;
      return this;
    }

    /**
     * Sets media id.
     *
     * @param mediaId the media id
     * @return the media id
     */
    public Builder setMediaId(String mediaId) {
      this.mediaId = mediaId;
      return this;
    }

    /**
     * Sets presigned url.
     *
     * @param presignedUrl the presigned url
     * @return the presigned url
     */
    public Builder setPresignedUrl(String presignedUrl) {
      this.presignedUrl = presignedUrl;
      return this;
    }

    /**
     * Sets upload progress listener.
     *
     * @param uploadProgressListener the upload progress listener
     * @return the upload progress listener
     */
    public Builder setUploadProgressListener(UploadProgressListener uploadProgressListener) {
      this.uploadProgressListener = uploadProgressListener;
      return this;
    }

    /**
     * Sets local message id.
     *
     * @param localMessageId the local message id
     * @return the local message id
     */
    public Builder setLocalMessageId(String localMessageId) {
      this.localMessageId = localMessageId;
      return this;
    }

    /**
     * Build upload media query.
     *
     * @return the upload media query
     */
    public UploadMediaQuery build() {
      return new UploadMediaQuery(this);
    }
  }

  /**
   * Gets media path.
   *
   * @return the media path
   */
  public String getMediaPath() {
    return mediaPath;
  }

  /**
   * Gets media id.
   *
   * @return the media id
   */
  public String getMediaId() {
    return mediaId;
  }

  /**
   * Gets presigned url.
   *
   * @return the presigned url
   */
  public String getPresignedUrl() {
    return presignedUrl;
  }

  /**
   * Gets upload progress listener.
   *
   * @return the upload progress listener
   */
  public UploadProgressListener getUploadProgressListener() {
    return uploadProgressListener;
  }

  /**
   * Gets local message id.
   *
   * @return the local message id
   */
  public String getLocalMessageId() {
    return localMessageId;
  }
}
