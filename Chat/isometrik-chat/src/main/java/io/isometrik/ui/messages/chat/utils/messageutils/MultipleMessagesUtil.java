package io.isometrik.ui.messages.chat.utils.messageutils;

import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.chat.utils.enums.MessageTypesForUI;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The helper class to select multiple messages and to check disability of actions based on messages
 * selected.
 */
public class MultipleMessagesUtil {

  private final HashMap<String, MessagesModel> selectedMessages = new HashMap<>();

  /**
   * Add message.
   *
   * @param messagesModel the messages model
   */
  public void addMessage(MessagesModel messagesModel) {

    selectedMessages.put(messagesModel.getMessageId(), messagesModel);
  }

  /**
   * Cleanup.
   */
  public void cleanup() {

    selectedMessages.clear();
  }

  /**
   * Remove message.
   *
   * @param messageId the message id
   */
  public void removeMessage(String messageId) {
    selectedMessages.remove(messageId);
  }

  /**
   * Remove messages.
   *
   * @param messageIds the message ids
   */
  public void removeMessages(ArrayList<String> messageIds) {

    for (String messageId : messageIds) {
      selectedMessages.remove(messageId);
    }
  }

  /**
   * Is copy enabled boolean.
   *
   * @return the boolean
   */
  public boolean isCopyEnabled() {
    if (selectedMessages.size() == 0) return false;
    for (MessagesModel messagesModel : selectedMessages.values()) {
      if (messagesModel.getCustomMessageType() != MessageTypesForUI.TextSent
          && messagesModel.getCustomMessageType() != MessageTypesForUI.TextReceived) {
        return false;
      }
    }
    return true;
  }

  /**
   * Is delete enabled boolean.
   *
   * @return the boolean
   */
  public boolean isDeleteEnabled() {
    if (selectedMessages.size() == 0) return false;
    for (MessagesModel messagesModel : selectedMessages.values()) {
      if (!messagesModel.isSentMessage()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets selected messages count.
   *
   * @return the selected messages count
   */
  public int getSelectedMessagesCount() {
    return selectedMessages.size();
  }

  /**
   * Gets selected message ids.
   *
   * @return the selected message ids
   */
  public ArrayList<String> getSelectedMessageIds() {
    return new ArrayList<>(selectedMessages.keySet());
  }

  /**
   * Copy text string.
   *
   * @return the string
   */
  public String copyText() {

    StringBuilder text = new StringBuilder();
    for (MessagesModel messagesModel : selectedMessages.values()) {
      if (messagesModel.getCustomMessageType() == MessageTypesForUI.TextSent
          || messagesModel.getCustomMessageType() == MessageTypesForUI.TextReceived) {
        text.append(" \n").append(messagesModel.getTextMessage());
      }
    }
    return text.toString().trim();
  }
}
