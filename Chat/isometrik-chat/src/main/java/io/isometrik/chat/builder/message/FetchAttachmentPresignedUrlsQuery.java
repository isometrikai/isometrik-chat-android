package io.isometrik.chat.builder.message;

import java.util.List;

/**
 * The query builder for fetching attachment presigned urls request.
 */
public class FetchAttachmentPresignedUrlsQuery {

  private final String conversationId;
  private final String userToken;
  private final List<PresignUrlRequest> presignUrlRequests;

  /**
   * The type Presign url request.
   */
  public static class PresignUrlRequest {
    private final String mediaId;
    private final String nameWithExtension;
    private final int mediaType;

    /**
     * Instantiates a new Presign url request.
     *
     * @param mediaId the media id
     * @param nameWithExtension the name with extension
     * @param mediaType the media type
     */
    public PresignUrlRequest(String mediaId, String nameWithExtension, int mediaType) {
      this.mediaId = mediaId;
      this.nameWithExtension = nameWithExtension;
      this.mediaType = mediaType;
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
     * Gets name with extension.
     *
     * @return the name with extension
     */
    public String getNameWithExtension() {
      return nameWithExtension;
    }

    /**
     * Gets media type.
     *
     * @return the media type
     */
    public int getMediaType() {
      return mediaType;
    }
  }

  private FetchAttachmentPresignedUrlsQuery(FetchAttachmentPresignedUrlsQuery.Builder builder) {

    this.conversationId = builder.conversationId;
    this.userToken = builder.userToken;
    this.presignUrlRequests = builder.presignUrlRequests;
  }

  /**
   * The builder class for building fetch attachment presigned urls query.
   */
  public static class Builder {

    private String conversationId;
    private String userToken;
    private List<PresignUrlRequest> presignUrlRequests;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets presign url requests.
     *
     * @param presignUrlRequests the presign url requests
     * @return the presign url requests
     */
    public FetchAttachmentPresignedUrlsQuery.Builder setPresignUrlRequests(
        List<PresignUrlRequest> presignUrlRequests) {
      this.presignUrlRequests = presignUrlRequests;
      return this;
    }

    /**
     * Sets conversation id.
     *
     * @param conversationId the conversation id
     * @return the conversation id
     */
    public FetchAttachmentPresignedUrlsQuery.Builder setConversationId(String conversationId) {
      this.conversationId = conversationId;
      return this;
    }

    /**
     * Sets user token.
     *
     * @param userToken the user token
     * @return the user token
     */
    public FetchAttachmentPresignedUrlsQuery.Builder setUserToken(String userToken) {
      this.userToken = userToken;
      return this;
    }

    /**
     * Build fetch attachment presigned urls query.
     *
     * @return the fetch attachment presigned urls query
     */
    public FetchAttachmentPresignedUrlsQuery build() {
      return new FetchAttachmentPresignedUrlsQuery(this);
    }
  }

  /**
   * Gets conversation id.
   *
   * @return the conversation id
   */
  public String getConversationId() {
    return conversationId;
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
   * Gets presign url requests.
   *
   * @return the presign url requests
   */
  public List<PresignUrlRequest> getPresignUrlRequests() {
    return presignUrlRequests;
  }
}
