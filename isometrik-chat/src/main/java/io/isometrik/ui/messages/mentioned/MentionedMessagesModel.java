package io.isometrik.ui.messages.mentioned;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import io.isometrik.chat.response.message.utils.fetchmessages.MentionedMessage;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.chat.utils.TimeUtil;

/**
 * The helper class for inflating items in the list of messages in which logged in user has been
 * mentioned.
 */
public class MentionedMessagesModel {

  private final boolean isOnline, privateOneToOneConversation;
  private final String conversationId, conversationTitle, conversationImageUrl, messageId,
      mentionedMessageTime, mentionedMessageSenderName, mentionedMessageSendersProfileImageUrl;
  private String mentionedText;
  private Integer lastMessagePlaceHolderImage;
  private boolean messagingDisabled;
  private final SpannableString mentionedBy;

  /**
   * Instantiates a new Mentioned messages model.
   *
   * @param mentionedMessage the mentioned message
   */
  public MentionedMessagesModel(MentionedMessage mentionedMessage) {
    conversationId = mentionedMessage.getConversationId();
    privateOneToOneConversation = mentionedMessage.isPrivateOneToOne();
    if (privateOneToOneConversation) {
      conversationImageUrl = mentionedMessage.getOpponentDetails().getUserProfileImageUrl();
      isOnline = mentionedMessage.getOpponentDetails().isOnline();
      if (mentionedMessage.getOpponentDetails().getUserId() == null) {
        conversationTitle =
            IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
        mentionedMessageSenderName =
            IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user);
        messagingDisabled = true;
      } else {
        conversationTitle = mentionedMessage.getOpponentDetails().getUserName();
        mentionedMessageSenderName = mentionedMessage.getSenderInfo().getUserName();
      }
    } else {
      isOnline = false;
      conversationImageUrl = mentionedMessage.getConversationImageUrl();
      conversationTitle = mentionedMessage.getConversationTitle();
      mentionedMessageSenderName = mentionedMessage.getSenderInfo().getUserName();
    }

    if (messagingDisabled) {
      mentionedBy = new SpannableString(IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_mentioned_you, mentionedMessageSenderName));
      mentionedBy.setSpan(new StyleSpan(Typeface.ITALIC), 0, mentionedMessageSenderName.length(),
          0);
    } else {
      mentionedBy = new SpannableString(IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_mentioned_you, mentionedMessageSenderName));
    }

    messageId = mentionedMessage.getMessageId();
    mentionedMessageTime = TimeUtil.formatTimestampToOnlyDate(mentionedMessage.getSentAt());
    mentionedMessageSendersProfileImageUrl =
        mentionedMessage.getSenderInfo().getUserProfileImageUrl();
    switch (mentionedMessage.getCustomType()) {
      case "AttachmentMessage:Text": {
        lastMessagePlaceHolderImage = null;
        if (mentionedMessage.getParentMessageId() != null) {

          lastMessagePlaceHolderImage = R.drawable.ism_ic_quote;
        }

        mentionedText = mentionedMessage.getBody();
        break;
      }
      case "AttachmentMessage:Image": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_picture;
        mentionedText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_photo);
        break;
      }
      case "AttachmentMessage:Video": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_video;
        mentionedText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_video);
        break;
      }
      case "AttachmentMessage:Audio": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_mic;
        mentionedText =
            IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_audio_recording);
        break;
      }
      case "AttachmentMessage:File": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_file;
        mentionedText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_file);
        break;
      }
      case "AttachmentMessage:Sticker": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_sticker;
        mentionedText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_sticker);
        break;
      }
      case "AttachmentMessage:Gif": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_gif;
        mentionedText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_gif);
        break;
      }
      case "AttachmentMessage:Whiteboard": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_whiteboard;
        mentionedText =
            IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_whiteboard);
        break;
      }
      case "AttachmentMessage:Location": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_location;
        mentionedText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_location);
        break;
      }
      case "AttachmentMessage:Contact": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_contact;
        mentionedText = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_contact);
        break;
      }
      case "AttachmentMessage:Reply": {
        lastMessagePlaceHolderImage = R.drawable.ism_ic_quote;
        mentionedText = mentionedMessage.getBody();
        break;
      }
    }
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
    return privateOneToOneConversation;
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
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets mentioned message time.
   *
   * @return the mentioned message time
   */
  public String getMentionedMessageTime() {
    return mentionedMessageTime;
  }

  /**
   * Gets mentioned message senders profile image url.
   *
   * @return the mentioned message senders profile image url
   */
  public String getMentionedMessageSendersProfileImageUrl() {
    return mentionedMessageSendersProfileImageUrl;
  }

  /**
   * Gets mentioned message sender name.
   *
   * @return the mentioned message sender name
   */
  public String getMentionedMessageSenderName() {
    return mentionedMessageSenderName;
  }

  /**
   * Gets mentioned text.
   *
   * @return the mentioned text
   */
  public String getMentionedText() {
    return mentionedText;
  }

  /**
   * Gets last message place holder image.
   *
   * @return the last message place holder image
   */
  public Integer getLastMessagePlaceHolderImage() {
    return lastMessagePlaceHolderImage;
  }

  /**
   * Gets mentioned by.
   *
   * @return the mentioned by
   */
  public SpannableString getMentionedBy() {
    return mentionedBy;
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
