package io.isometrik.chat.builder.user;

/**
 * The query builder for fetching update user presigned url request.
 */
public class FetchUpdateUserPresignedUrlQuery {

  private final String userToken;
  private final String mediaExtension;

  private FetchUpdateUserPresignedUrlQuery(Builder builder) {
    this.userToken = builder.userToken;
    this.mediaExtension = builder.mediaExtension;
  }

  /**
   * The builder class for building fetch update user presigned url query.
   */
  public static class Builder {
    private String userToken;
    private String mediaExtension;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public Builder setUserToken(String userToken) {
      this.userToken = userToken;
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
     * Build fetch update user presigned url query.
     *
     * @return the fetch update user presigned url query
     */
    public FetchUpdateUserPresignedUrlQuery build() {
      return new FetchUpdateUserPresignedUrlQuery(this);
    }
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
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
