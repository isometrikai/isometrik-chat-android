package io.isometrik.chat.response.error;

/**
 * The base class for the Isometrik error containing details of the error- error message, whether
 * its local or remote message, local/remote error code and http response code.
 */
public class IsometrikError {

  private final String errorMessage;
  private final int isometrikErrorCode;
  private final boolean isRemoteError;
  private final int httpResponseCode;
  private final int remoteErrorCode;

  private IsometrikError(Builder builder) {
    this.errorMessage = builder.errorMessage;
    this.isometrikErrorCode = builder.isometrikErrorCode;
    this.isRemoteError = builder.isRemoteError;
    this.httpResponseCode = builder.httpResponseCode;
    this.remoteErrorCode = builder.remoteErrorCode;
  }

  /**
   * The builder class to build an instance of the IsometrikError.
   */
  public static class Builder {
    private String errorMessage;
    private int isometrikErrorCode;
    private boolean isRemoteError;
    private int httpResponseCode;
    private int remoteErrorCode;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets error message.
     *
     * @param errorMessage the error message
     * @return the error message
     */
    public Builder setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
      return this;
    }

    /**
     * Sets isometrik error code.
     *
     * @param isometrikErrorCode the isometrik error code
     * @return the isometrik error code
     */
    public Builder setIsometrikErrorCode(int isometrikErrorCode) {
      this.isometrikErrorCode = isometrikErrorCode;
      return this;
    }

    /**
     * Sets remote error.
     *
     * @param remoteError the remote error
     * @return the remote error
     */
    public Builder setRemoteError(boolean remoteError) {
      isRemoteError = remoteError;
      return this;
    }

    /**
     * Sets http response code.
     *
     * @param httpResponseCode the http response code
     * @return the http response code
     */
    public Builder setHttpResponseCode(int httpResponseCode) {
      this.httpResponseCode = httpResponseCode;
      return this;
    }

    /**
     * Sets remote error code.
     *
     * @param remoteErrorCode the remote error code
     * @return the remote error code
     */
    public Builder setRemoteErrorCode(int remoteErrorCode) {
      this.remoteErrorCode = remoteErrorCode;
      return this;
    }

    /**
     * Build isometrik error.
     *
     * @return the isometrik error
     */
    public io.isometrik.chat.response.error.IsometrikError build() {
      return new io.isometrik.chat.response.error.IsometrikError(this);
    }
  }

  /**
   * Gets error message.
   *
   * @return the error message
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Gets isometrik error code.
   *
   * @return the isometrik error code
   */
  public int getIsometrikErrorCode() {
    return isometrikErrorCode;
  }

  /**
   * Is remote error boolean.
   *
   * @return the boolean
   */
  public boolean isRemoteError() {
    return isRemoteError;
  }

  /**
   * Gets http response code.
   *
   * @return the http response code
   */
  public int getHttpResponseCode() {
    return httpResponseCode;
  }

  /**
   * Gets remote error code.
   *
   * @return the remote error code
   */
  public int getRemoteErrorCode() {
    return remoteErrorCode;
  }
}
