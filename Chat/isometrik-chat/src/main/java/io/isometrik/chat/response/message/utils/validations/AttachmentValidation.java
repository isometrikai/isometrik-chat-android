package io.isometrik.chat.response.message.utils.validations;

import io.isometrik.chat.response.message.utils.schemas.Attachment;
import java.util.List;

/**
 * The helper class for attachment validation.
 */
public class AttachmentValidation {
  private boolean isInvalidAttachment(Attachment attachment) {

    switch (attachment.getAttachmentSchemaType()) {

      case Media: {

        return (validateString(attachment.getMediaId())
            || validateString(attachment.getMediaUrl())
            || validateString(attachment.getName())
            || validateString(attachment.getMimeType())
            || validateString(attachment.getExtension())
            || validateString(attachment.getThumbnailUrl())
            || attachment.getSize() == null);
      }

      case Location: {

        return (attachment.getLatitude() == null
            || attachment.getLongitude() == null
            || validateString(attachment.getTitle())
            || validateString(attachment.getAddress()));
      }
      case GifSticker: {
        return (validateString(attachment.getThumbnailUrl()) || validateString(
            attachment.getMediaUrl()) || validateString(attachment.getStillUrl()));
      }

      default:
        return true;
    }
  }

  /**
   * Validate attachments boolean.
   *
   * @param attachments the attachments
   * @return the boolean
   */
  public boolean validateAttachments(List<Attachment> attachments) {
    for (int i = 0; i < attachments.size(); i++) {

      if (isInvalidAttachment(attachments.get(i))) return true;
    }
    return false;
  }

  private static boolean validateString(String data) {
    return data == null || data.isEmpty();
  }
}
