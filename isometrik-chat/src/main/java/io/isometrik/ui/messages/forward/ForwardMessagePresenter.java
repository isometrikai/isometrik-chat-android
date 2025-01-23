package io.isometrik.ui.messages.forward;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.message.broadcastforward.ForwardMessageQuery;
import io.isometrik.chat.enums.AttachmentMessageType;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.response.message.utils.schemas.EventForMessage;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.chat.utils.attachmentutils.PrepareAttachmentHelper;
import io.isometrik.ui.messages.chat.utils.enums.RemoteMessageTypes;
import io.isometrik.ui.messages.chat.utils.messageutils.ContactUtil;
import io.isometrik.ui.messages.search.utils.SearchTagUtils;
import io.isometrik.chat.enums.CustomMessageTypes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The presenter for forwarding message to people and forward in conversations with api
 * call to forward a message to/in users/conversations respectively.
 */
public class ForwardMessagePresenter implements ForwardMessageContract.Presenter {

  private final Isometrik isometrik = IsometrikChatSdk.getInstance().getIsometrik();
  private final String userToken = IsometrikChatSdk.getInstance().getUserSession().getUserToken();

  private final ForwardMessageContract.View forwardMessageView;

  /**
   * Instantiates a new Forward message presenter.
   *
   * @param forwardMessageView the forward message view
   */
  ForwardMessagePresenter(ForwardMessageContract.View forwardMessageView) {
    this.forwardMessageView = forwardMessageView;
  }

  @Override
  public void forwardMessage(MessagesModel messagesModel, ArrayList<String> userIds,
      ArrayList<String> conversationIds, String forwardMessageNotes) {

    String messageBody = null, customType = null;
    Attachment mediaAttachment = null;
    JSONObject messageMetadata = null;

    switch (messagesModel.getMessageTypeUi()) {

      case TEXT_MESSAGE_SENT:
      case TEXT_MESSAGE_RECEIVED: {
        messageBody = messagesModel.getTextMessage().toString();
        customType = CustomMessageTypes.Text.value;
        break;
      }
      case PHOTO_MESSAGE_SENT:
      case PHOTO_MESSAGE_RECEIVED: {
        messageBody = CustomMessageTypes.Image.value;
        customType = CustomMessageTypes.Image.value;

        mediaAttachment = PrepareAttachmentHelper.prepareMediaAttachment(messagesModel.getMediaId(),
            messagesModel.getPhotoMainUrl(), messagesModel.getPhotoThumbnailUrl(),
            AttachmentMessageType.Image, messagesModel.getAttachmentName(),
            messagesModel.getMimeType(), messagesModel.getMediaExtension(),
            messagesModel.getAttachmentSize());

        break;
      }
      case VIDEO_MESSAGE_SENT:
      case VIDEO_MESSAGE_RECEIVED: {
        messageBody = CustomMessageTypes.Video.value;
        customType = CustomMessageTypes.Video.value;

        mediaAttachment = PrepareAttachmentHelper.prepareMediaAttachment(messagesModel.getMediaId(),
            messagesModel.getVideoMainUrl(), messagesModel.getVideoThumbnailUrl(),
            AttachmentMessageType.Video, messagesModel.getAttachmentName(),
            messagesModel.getMimeType(), messagesModel.getMediaExtension(),
            messagesModel.getAttachmentSize());

        break;
      }
      case AUDIO_MESSAGE_SENT:
      case AUDIO_MESSAGE_RECEIVED: {
        messageBody = CustomMessageTypes.Audio.value;
        customType = CustomMessageTypes.Audio.value;

        mediaAttachment = PrepareAttachmentHelper.prepareMediaAttachment(messagesModel.getMediaId(),
            messagesModel.getAudioUrl(), messagesModel.getAudioUrl(), AttachmentMessageType.Audio,
            messagesModel.getAttachmentName(), messagesModel.getMimeType(),
            messagesModel.getMediaExtension(), messagesModel.getAttachmentSize());

        break;
      }
      case FILE_MESSAGE_SENT:
      case FILE_MESSAGE_RECEIVED: {
        messageBody = CustomMessageTypes.File.value;
        customType = CustomMessageTypes.File.value;

        mediaAttachment = PrepareAttachmentHelper.prepareMediaAttachment(messagesModel.getMediaId(),
            messagesModel.getFileUrl(), messagesModel.getFileUrl(), AttachmentMessageType.File,
            messagesModel.getAttachmentName(), messagesModel.getMimeType(),
            messagesModel.getMediaExtension(), messagesModel.getAttachmentSize());

        break;
      }
      case WHITEBOARD_MESSAGE_SENT:
      case WHITEBOARD_MESSAGE_RECEIVED: {
        messageBody = CustomMessageTypes.Whiteboard.value;
        customType = CustomMessageTypes.Whiteboard.value;

        mediaAttachment = PrepareAttachmentHelper.prepareMediaAttachment(messagesModel.getMediaId(),
            messagesModel.getWhiteboardMainUrl(), messagesModel.getWhiteboardThumbnailUrl(),
            AttachmentMessageType.Image, messagesModel.getAttachmentName(),
            messagesModel.getMimeType(), messagesModel.getMediaExtension(),
            messagesModel.getAttachmentSize());

        break;
      }
      case STICKER_MESSAGE_SENT:
      case STICKER_MESSAGE_RECEIVED: {
        messageBody = CustomMessageTypes.Sticker.value;
        customType = CustomMessageTypes.Sticker.value;

        mediaAttachment =
            PrepareAttachmentHelper.prepareStickerAttachment(messagesModel.getAttachmentName(),
                messagesModel.getStickerMainUrl(), messagesModel.getStickerStillUrl(),
                messagesModel.getStickerStillUrl());
        break;
      }
      case GIF_MESSAGE_SENT:
      case GIF_MESSAGE_RECEIVED: {
        messageBody = CustomMessageTypes.Gif.value;
        customType = CustomMessageTypes.Gif.value;

        mediaAttachment =
            PrepareAttachmentHelper.prepareGifAttachment(messagesModel.getAttachmentName(),
                messagesModel.getGifMainUrl(), messagesModel.getGifStillUrl(),
                messagesModel.getGifStillUrl());

        break;
      }
      case LOCATION_MESSAGE_SENT:
      case LOCATION_MESSAGE_RECEIVED: {
        messageBody = CustomMessageTypes.Location.value;
        customType = CustomMessageTypes.Location.value;

        mediaAttachment =
            PrepareAttachmentHelper.prepareLocationAttachment(messagesModel.getLocationName(),
                messagesModel.getLocationDescription(),
                Double.parseDouble(messagesModel.getLatitude()),
                Double.parseDouble(messagesModel.getLongitude()));
        break;
      }
      case CONTACT_MESSAGE_SENT:
      case CONTACT_MESSAGE_RECEIVED: {
        messageBody = CustomMessageTypes.Contact.value;
        customType = CustomMessageTypes.Contact.value;

        messageMetadata = ContactUtil.parseContactsData(messagesModel.getContactName(),
            messagesModel.getContactIdentifier(), messagesModel.getContactImageUrl());

        break;
      }
    }

    if (!forwardMessageNotes.isEmpty()) {

      if (messageMetadata == null) {
        messageMetadata = new JSONObject();
      }
      try {
        messageMetadata.put("forwardMessageNotes", forwardMessageNotes);
      } catch (JSONException ignore) {
      }
    }

    ForwardMessageQuery.Builder forwardMessageQuery =
        new ForwardMessageQuery.Builder().setUserToken(userToken)
            .setEncrypted(false)
            .setBody(messageBody)
            .setMessageType(RemoteMessageTypes.ForwardedMessage.getValue())
            .setShowInConversation(true)
            .setEventForMessage(new EventForMessage(true, true))
            .setUserIds(userIds)
            .setConversationIds(conversationIds)
            .setDeviceId(IsometrikChatSdk.getInstance().getUserSession().getDeviceId());

    String mediaName = null;
    if (mediaAttachment != null) {
      forwardMessageQuery.setAttachments(Collections.singletonList(mediaAttachment));
      mediaName = mediaAttachment.getName();
    }

    List<String> searchableTags = SearchTagUtils.generateSearchTags(messagesModel, mediaName);

    if (!searchableTags.isEmpty() || !forwardMessageNotes.isEmpty()) {
      if (!forwardMessageNotes.isEmpty()) {
        searchableTags.add(forwardMessageNotes);
      }
      forwardMessageQuery.setSearchableTags(searchableTags);
    }

    if (customType != null) {
      forwardMessageQuery.setCustomType(customType);
    }
    //if (mentionedUsers != null) {
    //  sendMessageQuery.setMentionedUsers(mentionedUsers);
    //}
    if (messageMetadata != null) {
      forwardMessageQuery.setMetaData(messageMetadata);
    }

    isometrik.getRemoteUseCases()
        .getMessageUseCases()
        .forwardMessage(forwardMessageQuery.build(), (var1, var2) -> {
          if (var1 != null) {
            forwardMessageView.onMessageForwardedSuccessfully();
          } else {
            forwardMessageView.onError(var2.getErrorMessage());
          }
        });
  }
}
