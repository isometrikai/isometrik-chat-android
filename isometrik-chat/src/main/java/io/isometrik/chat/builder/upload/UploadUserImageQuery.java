package io.isometrik.chat.builder.upload;

import io.isometrik.chat.models.upload.utils.UploadProgressListener;

/**
 * The query builder for uploading user image request.
 */
public class UploadUserImageQuery {

  private final String mediaPath;
  private final String requestId;
  private final String presignedUrl;
  private final UploadProgressListener uploadProgressListener;

  private UploadUserImageQuery(Builder builder) {

    this.mediaPath = builder.mediaPath;
    this.requestId = builder.requestId;
    this.presignedUrl = builder.presignedUrl;
    this.uploadProgressListener = builder.uploadProgressListener;
  }

  /**
   * The builder class for building upload user image query.
   */
  public static class Builder {

    private String mediaPath;
    private String requestId;
    private String presignedUrl;
    private UploadProgressListener uploadProgressListener;

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
     * Sets request id.
     *
     * @param requestId the request id
     * @return the request id
     */
    public Builder setRequestId(String requestId) {
      this.requestId = requestId;
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
     * Build upload user image query.
     *
     * @return the upload user image query
     */
    public UploadUserImageQuery build() {
      return new UploadUserImageQuery(this);
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
   * Gets request id.
   *
   * @return the request id
   */
  public String getRequestId() {
    return requestId;
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
}
