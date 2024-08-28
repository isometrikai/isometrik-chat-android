package io.isometrik.chat.response.message.utils.validations;

import io.isometrik.chat.builder.message.FetchAttachmentPresignedUrlsQuery;
import java.util.List;

/**
 * The helper class for presign url request validation.
 */
public class PresignUrlRequestValidation {

  /**
   * Validate presign url requests boolean.
   *
   * @param presignUrlRequests the presign url requests
   * @return the boolean
   */
  public boolean validatePresignUrlRequests(
      List<FetchAttachmentPresignedUrlsQuery.PresignUrlRequest> presignUrlRequests) {
    for (int i = 0; i < presignUrlRequests.size(); i++) {

      if (validatePresignUrlRequest(presignUrlRequests.get(i))) return true;
    }
    return false;
  }

  private boolean validatePresignUrlRequest(
      FetchAttachmentPresignedUrlsQuery.PresignUrlRequest presignUrlRequest) {

    return (presignUrlRequest.getMediaId() == null
        || presignUrlRequest.getNameWithExtension() == null
        || presignUrlRequest.getMediaType() < 0
        || presignUrlRequest.getMediaType() > 3);
  }
}
