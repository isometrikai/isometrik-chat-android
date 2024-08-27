package io.isometrik.chat.builder.user;

/**
 * The query builder for fetching create user presigned url request.
 */
public class FetchCreateUserPresignedUrlQuery {

  private final String userIdentifier;
  private final String mediaExtension;

  private FetchCreateUserPresignedUrlQuery(Builder builder) {
    this.userIdentifier = builder.userIdentifier;
    this.mediaExtension = builder.mediaExtension;
  }

  /**
   * The builder class for building fetch create user presigned url query.
   */
  public static class Builder {
    private String userIdentifier;
    private String mediaExtension;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user identifier.
     *
     * @param userIdentifier the user identifier
     * @return the user identifier
     */
    public Builder setUserIdentifier(String userIdentifier) {
      this.userIdentifier = userIdentifier;
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
     * Build fetch create user presigned url query.
     *
     * @return the fetch create user presigned url query
     */
    public FetchCreateUserPresignedUrlQuery build() {
      return new FetchCreateUserPresignedUrlQuery(this);
    }
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets media extension.
   *
   * @return the media extension
   */
  public String getMediaExtension() {
    return mediaExtension;
  }
}
