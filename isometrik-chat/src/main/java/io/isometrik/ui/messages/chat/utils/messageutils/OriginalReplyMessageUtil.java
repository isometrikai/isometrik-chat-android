package io.isometrik.ui.messages.chat.utils.messageutils;

import io.isometrik.chat.utils.TimeUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to parse details of parent message of a reply message.
 */
public class OriginalReplyMessageUtil {

  private final String parentMessageId;

  private String originalMessageSenderName;
  private String originalMessage;
  private String getOriginalMessageTime;
  private Integer originalMessagePlaceholderImage;

  /**
   * Instantiates a new Original reply message util.
   *
   * @param parentMessageId the parent message id
   * @param replyMessage the reply message details
   */
  public OriginalReplyMessageUtil(String parentMessageId, JSONObject replyMessage) {
    this.parentMessageId = parentMessageId;

    if (parentMessageId != null && replyMessage!=null) {
      try {
        JSONObject replyMessageDetails = replyMessage.getJSONObject("replyMessage");
        originalMessageSenderName = replyMessageDetails.getString("parentMessageUserName");
        originalMessage = replyMessageDetails.getString("parentMessageBody");
        getOriginalMessageTime = TimeUtil.formatTimestampToBothDateAndTime(
            replyMessageDetails.getLong("parentMessageSentAt"));
        originalMessagePlaceholderImage =
            replyMessageDetails.getInt("originalMessagePlaceHolderImage");
      } catch (JSONException ignore) {
      }
    }
  }

  /**
   * Gets parent message id.
   *
   * @return the parent message id
   */
  public String getParentMessageId() {
    return parentMessageId;
  }

  /**
   * Gets original message sender name.
   *
   * @return the original message sender name
   */
  public String getOriginalMessageSenderName() {
    return originalMessageSenderName;
  }

  /**
   * Gets original message.
   *
   * @return the original message
   */
  public String getOriginalMessage() {
    return originalMessage;
  }

  /**
   * Gets get original message time.
   *
   * @return the get original message time
   */
  public String getGetOriginalMessageTime() {
    return getOriginalMessageTime;
  }

  /**
   * Gets original message placeholder image.
   *
   * @return the original message placeholder image
   */
  public Integer getOriginalMessagePlaceholderImage() {
    return originalMessagePlaceholderImage;
  }
}
