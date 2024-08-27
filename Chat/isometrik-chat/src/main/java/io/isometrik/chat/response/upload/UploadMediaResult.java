package io.isometrik.chat.response.upload;

/**
 * The helper class to parse the response of the upload media request.
 */
public class UploadMediaResult {
  private final String mediaId;
  private final String localMessageId;

  /**
   * Instantiates a new Upload media result.
   *
   * @param mediaId the media id
   * @param localMessageId the local message id
   */
  public UploadMediaResult(String mediaId,String localMessageId) {
    this.mediaId = mediaId;
    this.localMessageId
        =localMessageId;
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
   * Gets local message id.
   *
   * @return the local message id
   */
  public String getLocalMessageId() {
    return localMessageId;
  }
}
