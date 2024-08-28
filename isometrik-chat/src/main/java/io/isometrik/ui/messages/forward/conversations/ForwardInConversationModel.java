package io.isometrik.ui.messages.forward.conversations;

import io.isometrik.chat.response.conversation.utils.Conversation;

/**
 * The helper class for inflating items in the list of conversations in which a message can be forwarded.
 */
public class ForwardInConversationModel {

  private final String conversationImageUrl, conversationId;
  private final boolean isOnline, isPrivateOneToOneConversation, messagingDisabled;
  private boolean selected;
  private final String conversationTitle;

  /**
   * Instantiates a new Forward in conversation model.
   *
   * @param conversation the conversation
   */
  public ForwardInConversationModel(Conversation conversation) {
    messagingDisabled = conversation.isMessagingDisabled();
    isPrivateOneToOneConversation = conversation.isPrivateOneToOne();
    if (isPrivateOneToOneConversation) {
      conversationImageUrl = conversation.getOpponentDetails().getUserProfileImageUrl();
      conversationTitle = conversation.getOpponentDetails().getUserName();
      isOnline = conversation.getOpponentDetails().isOnline();
      //if (conversationTitle == null || conversationTitle.isEmpty()) {
      //  conversationTitle =
      //      IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
      //}
    } else {

      conversationImageUrl = conversation.getConversationImageUrl();
      conversationTitle = conversation.getConversationTitle();
      isOnline = false;
    }
    conversationId = conversation.getConversationId();
  }

  /**
   * Is selected boolean.
   *
   * @return the boolean
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Sets selected.
   *
   * @param selected the selected
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
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
   * Is online boolean.
   *
   * @return the boolean
   */
  public boolean isOnline() {
    return isOnline;
  }

  /**
   * Is private one to one conversation boolean.
   *
   * @return the boolean
   */
  public boolean isPrivateOneToOneConversation() {
    return isPrivateOneToOneConversation;
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
   * Is messaging disabled boolean.
   *
   * @return the boolean
   */
  public boolean isMessagingDisabled() {
    return messagingDisabled;
  }
}
