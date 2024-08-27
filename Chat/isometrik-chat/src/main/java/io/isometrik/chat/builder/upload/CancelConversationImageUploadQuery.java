package io.isometrik.chat.builder.upload;

/**
 * The query builder for canceling conversation image upload request.
 */
public class CancelConversationImageUploadQuery {

  private final String requestId;

  private CancelConversationImageUploadQuery(Builder builder) {
    this.requestId = builder.requestId;
  }

  /**
   * The builder class for building cancel conversation image upload query.
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
     * Build cancel conversation image upload query.
     *
     * @return the cancel conversation image upload query
     */
    public CancelConversationImageUploadQuery build() {
      return new CancelConversationImageUploadQuery(this);
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
