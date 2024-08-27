package io.isometrik.ui.messages.chat.utils.messageutils;

/**
 * The helper class to generate a local message notification if message in a non-active
 * conversation received.
 */
public class LocalMessageNotificationUtil {
  private final String messageText, conversationTitle, conversationImageUrl;
  private final Integer messagePlaceHolderImage;

  /**
   * Instantiates a new Local message notification util.
   *
   * @param messageText the message text
   * @param conversationTitle the conversation title
   * @param conversationImageUrl the conversation image url
   * @param messagePlaceHolderImage the message place holder image
   */
  public LocalMessageNotificationUtil(String messageText, String conversationTitle,
      String conversationImageUrl, Integer messagePlaceHolderImage) {
    this.messageText = messageText;
    this.conversationTitle = conversationTitle;
    this.conversationImageUrl = conversationImageUrl;
    this.messagePlaceHolderImage = messagePlaceHolderImage;
  }

  /**
   * Gets message text.
   *
   * @return the message text
   */
  public String getMessageText() {
    return messageText;
  }

  /**
   * Gets conversation title.
   *
   * @return the conversation title
   */
  public String getConversationTitle() {
    return conversationTitle;
  }

  /**
   * Gets conversation image url.
   *
   * @return the conversation image url
   */
  public String getConversationImageUrl() {
    return conversationImageUrl;
  }

  /**
   * Gets message place holder image.
   *
   * @return the message place holder image
   */
  public Integer getMessagePlaceHolderImage() {
    return messagePlaceHolderImage;
  }
}
