package io.isometrik.chat.builder.upload;

/**
 * The query builder for canceling user image upload request.
 */
public class CancelUserImageUploadQuery {

  private final String requestId;

  private CancelUserImageUploadQuery(Builder builder) {
    this.requestId = builder.requestId;
  }

  /**
   * The builder class for building cancel user image upload query.
   */
  public static class Builder {
    private String requestId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
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
     * Build cancel user image upload query.
     *
     * @return the cancel user image upload query
     */
    public CancelUserImageUploadQuery build() {
      return new CancelUserImageUploadQuery(this);
    }
  }

  /**
   * Gets request id.
   *
   * @return the request id
   */
  public String getRequestId() {
    return requestId;
  }
}
