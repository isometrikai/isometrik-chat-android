package io.isometrik.chat.builder.upload;

/**
 * The query builder for canceling media upload request.
 */
public class CancelMediaUploadQuery {

  private final String mediaId;

  private CancelMediaUploadQuery(Builder builder) {
    this.mediaId = builder.mediaId;
  }

  /**
   * The builder class for building cancel media upload query.
   */
  public static class Builder {
    private String mediaId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
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
     * Build cancel media upload query.
     *
     * @return the cancel media upload query
     */
    public CancelMediaUploadQuery build() {
      return new CancelMediaUploadQuery(this);
    }
  }

  /**
   * Gets media id.
   *
   * @return the media id
   */
  public String getMediaId() {
    return mediaId;
  }
}
