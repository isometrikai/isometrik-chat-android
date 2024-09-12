package io.isometrik.ui.messages.chat;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmConversationActionMessageBinding;
import io.isometrik.chat.databinding.IsmReceivedMessageAudioBinding;
import io.isometrik.chat.databinding.IsmReceivedMessageContactBinding;
import io.isometrik.chat.databinding.IsmReceivedMessageFileBinding;
import io.isometrik.chat.databinding.IsmReceivedMessageGifBinding;
import io.isometrik.chat.databinding.IsmReceivedMessageLocationBinding;
import io.isometrik.chat.databinding.IsmReceivedMessagePhotoBinding;
import io.isometrik.chat.databinding.IsmReceivedMessageStickerBinding;
import io.isometrik.chat.databinding.IsmReceivedMessageTextBinding;
import io.isometrik.chat.databinding.IsmReceivedMessageVideoBinding;
import io.isometrik.chat.databinding.IsmReceivedMessageWhiteboardBinding;
import io.isometrik.chat.databinding.IsmSentMessageAudioBinding;
import io.isometrik.chat.databinding.IsmSentMessageContactBinding;
import io.isometrik.chat.databinding.IsmSentMessageFileBinding;
import io.isometrik.chat.databinding.IsmSentMessageGifBinding;
import io.isometrik.chat.databinding.IsmSentMessageLocationBinding;
import io.isometrik.chat.databinding.IsmSentMessagePhotoBinding;
import io.isometrik.chat.databinding.IsmSentMessageStickerBinding;
import io.isometrik.chat.databinding.IsmSentMessageTextBinding;
import io.isometrik.chat.databinding.IsmSentMessageVideoBinding;
import io.isometrik.chat.databinding.IsmSentMessageWhiteboardBinding;
import io.isometrik.ui.messages.chat.viewholders.AudioMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.AudioMessageSentViewHolder;
import io.isometrik.ui.messages.chat.viewholders.ContactMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.ContactMessageSentViewHolder;
import io.isometrik.ui.messages.chat.viewholders.ConversationActionMessageViewHolder;
import io.isometrik.ui.messages.chat.viewholders.FileMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.FileMessageSentViewHolder;
import io.isometrik.ui.messages.chat.viewholders.GifMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.GifMessageSentViewHolder;
import io.isometrik.ui.messages.chat.viewholders.LocationMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.LocationMessageSentViewHolder;
import io.isometrik.ui.messages.chat.viewholders.PhotoMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.PhotoMessageSentViewHolder;
import io.isometrik.ui.messages.chat.viewholders.StickerMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.StickerMessageSentViewHolder;
import io.isometrik.ui.messages.chat.viewholders.TextMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.TextMessageSentViewHolder;
import io.isometrik.ui.messages.chat.viewholders.VideoMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.VideoMessageSentViewHolder;
import io.isometrik.ui.messages.chat.viewholders.WhiteboardMessageReceivedViewHolder;
import io.isometrik.ui.messages.chat.viewholders.WhiteboardMessageSentViewHolder;
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter;
import io.isometrik.ui.messages.reaction.util.ReactionClickListener;
import io.isometrik.chat.utils.Constants;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;

import java.util.ArrayList;

/**
 * The recycler view adapter for the list of messages in a conversation.Supported message types are-
 * image/video/file/contact/location/whiteboard/sticker/gif/audio/text.
 */
public class ConversationMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<MessagesModel> messages;

  private static final int TEXT_MESSAGE_SENT = 0;
  private static final int PHOTO_MESSAGE_SENT = 1;
  private static final int VIDEO_MESSAGE_SENT = 2;
  private static final int AUDIO_MESSAGE_SENT = 3;
  private static final int FILE_MESSAGE_SENT = 4;
  private static final int STICKER_MESSAGE_SENT = 5;
  private static final int GIF_MESSAGE_SENT = 6;
  private static final int WHITEBOARD_MESSAGE_SENT = 7;
  private static final int LOCATION_MESSAGE_SENT = 8;
  private static final int CONTACT_MESSAGE_SENT = 9;
  private static final int REPLAY_MESSAGE_SENT = 21;
  private static final int TEXT_MESSAGE_RECEIVED = 10;
  private static final int PHOTO_MESSAGE_RECEIVED = 11;
  private static final int VIDEO_MESSAGE_RECEIVED = 12;
  private static final int AUDIO_MESSAGE_RECEIVED = 13;
  private static final int FILE_MESSAGE_RECEIVED = 14;
  private static final int STICKER_MESSAGE_RECEIVED = 15;
  private static final int GIF_MESSAGE_RECEIVED = 16;
  private static final int WHITEBOARD_MESSAGE_RECEIVED = 17;
  private static final int LOCATION_MESSAGE_RECEIVED = 18;
  private static final int CONTACT_MESSAGE_RECEIVED = 19;
  private static final int CONVERSATION_ACTION_MESSAGE = 20;
  private static final int REPLAY_MESSAGE_RECEIVED = 22;

  private final ConversationMessagesActivity conversationMessagesActivity;
  private final ReactionClickListener reactionClickListener;
  private final int cornerRadius;
  //private final float thumbnailSizeMultiplier = Constants.THUMBNAIL_SIZE_MULTIPLIER;
  private boolean multipleMessagesSelectModeOn, messagingDisabled, joiningAsObserver;

  /**
   * Instantiates a new Conversation messages adapter.
   *
   * @param mContext the m context
   * @param messages the messages
   * @param reactionClickListener the reaction click listener
   * @param messagingDisabled the messaging disabled
   * @param joiningAsObserver the joining as observer
   */
  public ConversationMessagesAdapter(Context mContext, ArrayList<MessagesModel> messages,
      ReactionClickListener reactionClickListener, boolean messagingDisabled,
      boolean joiningAsObserver) {
    this.mContext = mContext;
    this.messages = messages;
    this.reactionClickListener = reactionClickListener;
    conversationMessagesActivity = (ConversationMessagesActivity) mContext;
    cornerRadius = (int) (13 * mContext.getResources().getDisplayMetrics().density);
    multipleMessagesSelectModeOn = false;
    this.messagingDisabled = messagingDisabled;
    this.joiningAsObserver = joiningAsObserver;
  }

  @Override
  public int getItemViewType(int position) {

    if (messages.get(position).isSentMessage()) {
      switch (messages.get(position).getCustomMessageType()) {
        case TextSent:
          return TEXT_MESSAGE_SENT;
        case PhotoSent:
          return PHOTO_MESSAGE_SENT;
        case VideoSent:
          return VIDEO_MESSAGE_SENT;
        case AudioSent:
          return AUDIO_MESSAGE_SENT;
        case FileSent:
          return FILE_MESSAGE_SENT;
        case StickerSent:
          return STICKER_MESSAGE_SENT;
        case GifSent:
          return GIF_MESSAGE_SENT;
        case WhiteboardSent:
          return WHITEBOARD_MESSAGE_SENT;
        case LocationSent:
          return LOCATION_MESSAGE_SENT;
        case ContactSent:
          return CONTACT_MESSAGE_SENT;
          case ReplaySent:
          return REPLAY_MESSAGE_SENT;
        default:
          return -1;
      }
    } else {
      switch (messages.get(position).getCustomMessageType()) {
        case TextReceived:
          return TEXT_MESSAGE_RECEIVED;
        case PhotoReceived:
          return PHOTO_MESSAGE_RECEIVED;
        case VideoReceived:
          return VIDEO_MESSAGE_RECEIVED;
        case AudioReceived:
          return AUDIO_MESSAGE_RECEIVED;
        case FileReceived:
          return FILE_MESSAGE_RECEIVED;
        case StickerReceived:
          return STICKER_MESSAGE_RECEIVED;
        case GifReceived:
          return GIF_MESSAGE_RECEIVED;
        case WhiteboardReceived:
          return WHITEBOARD_MESSAGE_RECEIVED;
        case LocationReceived:
          return LOCATION_MESSAGE_RECEIVED;
        case ContactReceived:
          return CONTACT_MESSAGE_RECEIVED;
        case ConversationActionMessage:
          return CONVERSATION_ACTION_MESSAGE;
          case ReplayReceived:
          return REPLAY_MESSAGE_RECEIVED;
        default:
          return -1;
      }
    }
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    switch (viewType) {

      case TEXT_MESSAGE_SENT:
        //Text message sent
        IsmSentMessageTextBinding ismSentMessageTextBinding =
            IsmSentMessageTextBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new TextMessageSentViewHolder(ismSentMessageTextBinding);
      case PHOTO_MESSAGE_SENT:
        //Image message sent
        IsmSentMessagePhotoBinding ismSentMessagePhotoBinding =
            IsmSentMessagePhotoBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new PhotoMessageSentViewHolder(ismSentMessagePhotoBinding);
      case VIDEO_MESSAGE_SENT:
        //Video message sent
        IsmSentMessageVideoBinding ismSentMessageVideoBinding =
            IsmSentMessageVideoBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new VideoMessageSentViewHolder(ismSentMessageVideoBinding);
      case AUDIO_MESSAGE_SENT:
        //Audio message sent
        IsmSentMessageAudioBinding ismSentMessageAudioBinding =
            IsmSentMessageAudioBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new AudioMessageSentViewHolder(ismSentMessageAudioBinding);
      case FILE_MESSAGE_SENT:
        //File message sent
        IsmSentMessageFileBinding ismSentMessageFileBinding =
            IsmSentMessageFileBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new FileMessageSentViewHolder(ismSentMessageFileBinding);
      case STICKER_MESSAGE_SENT:
        //Sticker message sent
        IsmSentMessageStickerBinding ismSentMessageStickerBinding =
            IsmSentMessageStickerBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new StickerMessageSentViewHolder(ismSentMessageStickerBinding);
      case GIF_MESSAGE_SENT:
        //Gif message sent
        IsmSentMessageGifBinding ismSentMessageGifBinding =
            IsmSentMessageGifBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
                false);

        return new GifMessageSentViewHolder(ismSentMessageGifBinding);
      case WHITEBOARD_MESSAGE_SENT:
        //Whiteboard message sent
        IsmSentMessageWhiteboardBinding ismSentMessageWhiteboardBinding =
            IsmSentMessageWhiteboardBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new WhiteboardMessageSentViewHolder(ismSentMessageWhiteboardBinding);
      case LOCATION_MESSAGE_SENT:
        //Location message sent
        IsmSentMessageLocationBinding ismSentMessageLocationBinding =
            IsmSentMessageLocationBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new LocationMessageSentViewHolder(ismSentMessageLocationBinding);
      case CONTACT_MESSAGE_SENT:
        //Contact message sent
        IsmSentMessageContactBinding ismSentMessageContactBinding =
            IsmSentMessageContactBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new ContactMessageSentViewHolder(ismSentMessageContactBinding);
      case REPLAY_MESSAGE_SENT:
        //Replay message sent
        IsmSentMessageTextBinding ismSentMessageReplayBinding =
                IsmSentMessageTextBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                        viewGroup, false);
        return new TextMessageSentViewHolder(ismSentMessageReplayBinding);
      case TEXT_MESSAGE_RECEIVED:
        //Text message received
        IsmReceivedMessageTextBinding ismReceivedMessageTextBinding =
            IsmReceivedMessageTextBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new TextMessageReceivedViewHolder(ismReceivedMessageTextBinding);
      case PHOTO_MESSAGE_RECEIVED:
        //Image message received
        IsmReceivedMessagePhotoBinding ismReceivedMessagePhotoBinding =
            IsmReceivedMessagePhotoBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new PhotoMessageReceivedViewHolder(ismReceivedMessagePhotoBinding);
      case VIDEO_MESSAGE_RECEIVED:
        //Video message received
        IsmReceivedMessageVideoBinding ismReceivedMessageVideoBinding =
            IsmReceivedMessageVideoBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new VideoMessageReceivedViewHolder(ismReceivedMessageVideoBinding);
      case AUDIO_MESSAGE_RECEIVED:
        //Audio message received
        IsmReceivedMessageAudioBinding ismReceivedMessageAudioBinding =
            IsmReceivedMessageAudioBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new AudioMessageReceivedViewHolder(ismReceivedMessageAudioBinding);
      case FILE_MESSAGE_RECEIVED:
        //File message received
        IsmReceivedMessageFileBinding ismReceivedMessageFileBinding =
            IsmReceivedMessageFileBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new FileMessageReceivedViewHolder(ismReceivedMessageFileBinding);
      case STICKER_MESSAGE_RECEIVED:
        //Sticker message received
        IsmReceivedMessageStickerBinding ismReceivedMessageStickerBinding =
            IsmReceivedMessageStickerBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new StickerMessageReceivedViewHolder(ismReceivedMessageStickerBinding);
      case GIF_MESSAGE_RECEIVED:
        //Gif message received
        IsmReceivedMessageGifBinding ismReceivedMessageGifBinding =
            IsmReceivedMessageGifBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new GifMessageReceivedViewHolder(ismReceivedMessageGifBinding);
      case WHITEBOARD_MESSAGE_RECEIVED:
        //Whiteboard message received
        IsmReceivedMessageWhiteboardBinding ismReceivedMessageWhiteboardBinding =
            IsmReceivedMessageWhiteboardBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new WhiteboardMessageReceivedViewHolder(ismReceivedMessageWhiteboardBinding);
      case LOCATION_MESSAGE_RECEIVED:
        //Location message received
        IsmReceivedMessageLocationBinding ismReceivedMessageLocationBinding =
            IsmReceivedMessageLocationBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new LocationMessageReceivedViewHolder(ismReceivedMessageLocationBinding);
      case CONTACT_MESSAGE_RECEIVED:
        //Contact message received
        IsmReceivedMessageContactBinding ismReceivedMessageContactBinding =
            IsmReceivedMessageContactBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new ContactMessageReceivedViewHolder(ismReceivedMessageContactBinding);

      case REPLAY_MESSAGE_RECEIVED:
        //Replay message received
        IsmReceivedMessageTextBinding ismReceivedMessageReplayBinding =
                IsmReceivedMessageTextBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                        viewGroup, false);
        return new TextMessageReceivedViewHolder(ismReceivedMessageReplayBinding);
      default:
        //Conversation action message
        IsmConversationActionMessageBinding ismConversationActionMessageBinding =
            IsmConversationActionMessageBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                viewGroup, false);

        return new ConversationActionMessageViewHolder(ismConversationActionMessageBinding);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    switch (viewHolder.getItemViewType()) {
      case TEXT_MESSAGE_SENT:
        //Text message sent
        configureTextMessageSentViewHolder((TextMessageSentViewHolder) viewHolder, position);
        break;
      case PHOTO_MESSAGE_SENT:
        //Image message sent
        configurePhotoMessageSentViewHolder((PhotoMessageSentViewHolder) viewHolder, position);
        break;
      case VIDEO_MESSAGE_SENT:
        //Video message sent
        configureVideoMessageSentViewHolder((VideoMessageSentViewHolder) viewHolder, position);
        break;
      case AUDIO_MESSAGE_SENT:
        //Audio message sent
        configureAudioMessageSentViewHolder((AudioMessageSentViewHolder) viewHolder, position);
        break;
      case FILE_MESSAGE_SENT:
        //File message sent
        configureFileMessageSentViewHolder((FileMessageSentViewHolder) viewHolder, position);
        break;
      case STICKER_MESSAGE_SENT:
        //Sticker message sent
        configureStickerMessageSentViewHolder((StickerMessageSentViewHolder) viewHolder, position);
        break;
      case GIF_MESSAGE_SENT:
        //Gif message sent
        configureGifMessageSentViewHolder((GifMessageSentViewHolder) viewHolder, position);
        break;
      case WHITEBOARD_MESSAGE_SENT:
        //Whiteboard message sent
        configureWhiteboardMessageSentViewHolder((WhiteboardMessageSentViewHolder) viewHolder,
            position);
        break;
      case LOCATION_MESSAGE_SENT:
        //Location message sent
        configureLocationMessageSentViewHolder((LocationMessageSentViewHolder) viewHolder,
            position);
        break;
      case CONTACT_MESSAGE_SENT:
        //Contact message sent
        configureContactMessageSentViewHolder((ContactMessageSentViewHolder) viewHolder, position);
        break;
      case REPLAY_MESSAGE_SENT:
        //Replay message sent
        configureTextMessageSentViewHolder((TextMessageSentViewHolder) viewHolder, position);
        break;
      case TEXT_MESSAGE_RECEIVED:
        //Text message received
        configureTextMessageReceivedViewHolder((TextMessageReceivedViewHolder) viewHolder,
            position);
        break;
      case PHOTO_MESSAGE_RECEIVED:
        //Image message received
        configurePhotoMessageReceivedViewHolder((PhotoMessageReceivedViewHolder) viewHolder,
            position);
        break;
      case VIDEO_MESSAGE_RECEIVED:
        //Video message received
        configureVideoMessageReceivedViewHolder((VideoMessageReceivedViewHolder) viewHolder,
            position);
        break;
      case AUDIO_MESSAGE_RECEIVED:
        //Audio message received
        configureAudioMessageReceivedViewHolder((AudioMessageReceivedViewHolder) viewHolder,
            position);
        break;
      case FILE_MESSAGE_RECEIVED:
        //File message received
        configureFileMessageReceivedViewHolder((FileMessageReceivedViewHolder) viewHolder,
            position);
        break;
      case STICKER_MESSAGE_RECEIVED:
        //Sticker message received
        configureStickerMessageReceivedViewHolder((StickerMessageReceivedViewHolder) viewHolder,
            position);
        break;
      case GIF_MESSAGE_RECEIVED:
        //Gif message received
        configureGifMessageReceivedViewHolder((GifMessageReceivedViewHolder) viewHolder, position);
        break;
      case WHITEBOARD_MESSAGE_RECEIVED:
        //Whiteboard message received
        configureWhiteboardMessageReceivedViewHolder(
            (WhiteboardMessageReceivedViewHolder) viewHolder, position);
        break;
      case LOCATION_MESSAGE_RECEIVED:
        //Location message received
        configureLocationMessageReceivedViewHolder((LocationMessageReceivedViewHolder) viewHolder,
            position);
        break;
      case CONTACT_MESSAGE_RECEIVED:
        //Contact message received
        configureContactMessageReceivedViewHolder((ContactMessageReceivedViewHolder) viewHolder,
            position);
        break;
      case REPLAY_MESSAGE_RECEIVED:
        //Replay message received
        configureTextMessageReceivedViewHolder((TextMessageReceivedViewHolder) viewHolder,
                position);
        break;
      default:
        //Conversation action message
        configureConversationActionMessageViewHolder(
            (ConversationActionMessageViewHolder) viewHolder, position);
    }
  }

  private void configureTextMessageSentViewHolder(
      TextMessageSentViewHolder textMessageSentViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {

        textMessageSentViewHolder.ismSentMessageTextBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        if (message.isMessageSentSuccessfully()) {
          textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setVisibility(
              View.GONE);
        } else {
          if (message.isSendingMessageFailed()) {
            textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }

        if (message.isForwardedMessage()) {
          textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);
          textMessageSentViewHolder.ismSentMessageTextBinding.dividerForward.setVisibility(
              View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
            textMessageSentViewHolder.ismSentMessageTextBinding.dividerForwardNotes.setVisibility(
                View.GONE);
          } else {
            textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
            textMessageSentViewHolder.ismSentMessageTextBinding.dividerForwardNotes.setVisibility(
                View.VISIBLE);
          }
        } else {
          textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          textMessageSentViewHolder.ismSentMessageTextBinding.dividerForward.setVisibility(
              View.GONE);
          textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);
          textMessageSentViewHolder.ismSentMessageTextBinding.dividerForwardNotes.setVisibility(
              View.GONE);

        }

        if (multipleMessagesSelectModeOn) {

          textMessageSentViewHolder.ismSentMessageTextBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          textMessageSentViewHolder.ismSentMessageTextBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setVisibility(View.GONE);
        } else {
          textMessageSentViewHolder.ismSentMessageTextBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          if (message.isMessageSentSuccessfully()) {
            textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setVisibility(
                View.VISIBLE);
            textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setVisibility(View.GONE);
          }
        }
        if (messagingDisabled) {
          textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setVisibility(View.GONE);
        }
        if (message.isQuotedMessage()) {
          textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          textMessageSentViewHolder.ismSentMessageTextBinding.dividerReply.setVisibility(
              View.VISIBLE);

          if (message.getOriginalMessagePlaceholderImage() == null) {
            textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.ivMessageImage.setVisibility(
                View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.ivMessageImage.setVisibility(
                View.VISIBLE);
          }
        } else {
          textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
          textMessageSentViewHolder.ismSentMessageTextBinding.dividerReply.setVisibility(View.GONE);

        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        textMessageSentViewHolder.ismSentMessageTextBinding.tvTextMessage.setText(
            message.getTextMessage());
        textMessageSentViewHolder.ismSentMessageTextBinding.tvTextMessage    .
        setMovementMethod(LinkMovementMethod.getInstance());
        textMessageSentViewHolder.ismSentMessageTextBinding.tvMessageTime.setText(
            message.getMessageTime());
      }
    } catch (Exception ignore) {
    }
  }

  private void configurePhotoMessageSentViewHolder(
      PhotoMessageSentViewHolder photoMessageSentViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        if (message.isMessageSentSuccessfully()) {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvSendingMessage.setVisibility(
              View.GONE);
        } else {
          if (message.isSendingMessageFailed()) {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }
        if (message.isForwardedMessage()) {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivForward.setVisibility(View.GONE);
        } else {

          if (message.isMessageSentSuccessfully()) {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivForward.setVisibility(
                View.VISIBLE);
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivForward.setVisibility(
                View.GONE);
          }
        }
        if (messagingDisabled) {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivForward.setVisibility(View.GONE);
        }
        if (message.isQuotedMessage()) {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.vParentMessage.ivMessageImage.setVisibility(
                View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      photoMessageSentViewHolder.ismSentMessagePhotoBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.vParentMessage.ivMessageImage.setVisibility(
                View.VISIBLE);
          }
        } else {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvMessageTime.setText(
            message.getMessageTime());

        if (message.isUploaded()) {

          photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlUpload.setVisibility(View.GONE);

          if (message.isDownloaded() || message.isDownloading()) {

            if (message.isDownloaded()) {
              photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvDownloadPhotoStatus.setText(
                  mContext.getString(R.string.ism_open));
              photoMessageSentViewHolder.ismSentMessagePhotoBinding.pbDownload.setVisibility(
                  View.GONE);
            } else {

              photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvDownloadPhotoStatus.setText(
                  mContext.getString(R.string.ism_attachments_cancel));
              photoMessageSentViewHolder.ismSentMessagePhotoBinding.pbDownload.setVisibility(
                  View.VISIBLE);
            }

            try {
              Glide.with(mContext)
                  .load(message.getPhotoMainUrl())
                  .thumbnail(Glide.with(mContext).load(message.getPhotoThumbnailUrl()))
                  .placeholder(R.drawable.ism_avatar_group_large)
                  .transform(new CenterCrop())
                  .into(photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivPhoto);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }

            photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlDownload.setOnClickListener(
                v -> {
                  if (message.isDownloading()) {
                    conversationMessagesActivity.cancelMediaDownload(message, position);
                  } else {
                    conversationMessagesActivity.handleClickOnMessageCell(message, true);
                  }
                });
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlDownload.setVisibility(
                View.VISIBLE);
          } else {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlDownload.setVisibility(
                View.GONE);
            try {
              if(message.getLocalMediaPath()==null) {
                Glide.with(mContext)
                    .load(message.getPhotoMainUrl())
                    .thumbnail(Glide.with(mContext).load(message.getPhotoThumbnailUrl()))
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(new CenterCrop(),
                        new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
                    .into(photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivPhoto);
              }else{
                Glide.with(mContext)
                    .load(message.getLocalMediaPath())
                    .thumbnail(Glide.with(mContext).load(message.getLocalMediaPath()))
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(new CenterCrop(),
                        new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
                    .into(photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivPhoto);

              } } catch (IllegalArgumentException | NullPointerException ignore) {
            }
          }
        } else {

          photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlDownload.setVisibility(View.GONE);
          try {
            Glide.with(mContext)
                .load(message.getPhotoMainUrl())
                .thumbnail(Glide.with(mContext).load(message.getPhotoThumbnailUrl()))
                .placeholder(R.drawable.ism_avatar_group_large)
                .transform(new CenterCrop())
                .into(photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivPhoto);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
          if (message.isUploading()) {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvPhotoStatus.setText(
                mContext.getString(R.string.ism_attachments_cancel));
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.pbUpload.setVisibility(
                View.VISIBLE);
          } else {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvPhotoStatus.setText(
                mContext.getString(R.string.ism_remove));
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.pbUpload.setVisibility(View.GONE);
          }
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlUpload.setOnClickListener(v -> {
            if (message.isUploading()) {
              conversationMessagesActivity.cancelMediaUpload(message, position);
            } else {
              conversationMessagesActivity.removeCanceledMessage(message.getLocalMessageId(),
                  position);
            }
          });
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlUpload.setVisibility(
              View.VISIBLE);
        }

        photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvPhotoSize.setText(
            message.getMediaSizeInMB());

        photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlPhoto.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureVideoMessageSentViewHolder(
      VideoMessageSentViewHolder videoMessageSentViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        videoMessageSentViewHolder.ismSentMessageVideoBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        if (message.isMessageSentSuccessfully()) {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.tvSendingMessage.setVisibility(
              View.GONE);
        } else {
          if (message.isSendingMessageFailed()) {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            videoMessageSentViewHolder.ismSentMessageVideoBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            videoMessageSentViewHolder.ismSentMessageVideoBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          videoMessageSentViewHolder.ismSentMessageVideoBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }
        if (message.isForwardedMessage()) {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivForward.setVisibility(View.GONE);
        } else {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          if (message.isMessageSentSuccessfully()) {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.ivForward.setVisibility(
                View.VISIBLE);
            videoMessageSentViewHolder.ismSentMessageVideoBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.ivForward.setVisibility(
                View.GONE);
          }
        }
        if (messagingDisabled) {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivForward.setVisibility(View.GONE);
        }
        if (message.isQuotedMessage()) {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.vParentMessage.ivMessageImage.setVisibility(
                View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      videoMessageSentViewHolder.ismSentMessageVideoBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            videoMessageSentViewHolder.ismSentMessageVideoBinding.vParentMessage.ivMessageImage.setVisibility(
                View.VISIBLE);
          }
        } else {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            videoMessageSentViewHolder.ismSentMessageVideoBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            videoMessageSentViewHolder.ismSentMessageVideoBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        videoMessageSentViewHolder.ismSentMessageVideoBinding.tvMessageTime.setText(
            message.getMessageTime());

        if (message.isUploaded()) {

          videoMessageSentViewHolder.ismSentMessageVideoBinding.rlUpload.setVisibility(View.GONE);

          if (message.isDownloaded() || message.isDownloading()) {

            if (message.isDownloaded()) {
              videoMessageSentViewHolder.ismSentMessageVideoBinding.tvDownloadVideoStatus.setText(
                  mContext.getString(R.string.ism_open));
              videoMessageSentViewHolder.ismSentMessageVideoBinding.pbDownload.setVisibility(
                  View.GONE);
            } else {

              videoMessageSentViewHolder.ismSentMessageVideoBinding.tvDownloadVideoStatus.setText(
                  mContext.getString(R.string.ism_attachments_cancel));
              videoMessageSentViewHolder.ismSentMessageVideoBinding.pbDownload.setVisibility(
                  View.VISIBLE);
            }

            try {
              Glide.with(mContext)
                  .load(message.getVideoMainUrl())
                  .thumbnail(Glide.with(mContext).load(message.getVideoThumbnailUrl()))
                  .placeholder(R.drawable.ism_avatar_group_large)
                  .transform(new CenterCrop())
                  .into(videoMessageSentViewHolder.ismSentMessageVideoBinding.ivVideoThumbnail);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }

            videoMessageSentViewHolder.ismSentMessageVideoBinding.rlDownload.setOnClickListener(
                v -> {
                  if (message.isDownloading()) {
                    conversationMessagesActivity.cancelMediaDownload(message, position);
                  } else {
                    conversationMessagesActivity.handleClickOnMessageCell(message, true);
                  }
                });
            videoMessageSentViewHolder.ismSentMessageVideoBinding.rlDownload.setVisibility(
                View.VISIBLE);
          } else {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.rlDownload.setVisibility(
                View.GONE);
            try {
              if(message.getLocalMediaPath()==null) {
                Glide.with(mContext)
                    .load(message.getVideoMainUrl())
                    .thumbnail(Glide.with(mContext).load(message.getVideoThumbnailUrl()))
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(new CenterCrop(),
                        new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
                    .into(videoMessageSentViewHolder.ismSentMessageVideoBinding.ivVideoThumbnail);
              }else{
                Glide.with(mContext)
                    .load(message.getLocalMediaPath())
                    .thumbnail(Glide.with(mContext).load(message.getLocalMediaPath()))
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(new CenterCrop(),
                        new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
                    .into(videoMessageSentViewHolder.ismSentMessageVideoBinding.ivVideoThumbnail);

              }

            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
          }
        } else {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.rlDownload.setVisibility(View.GONE);
          try {
            Glide.with(mContext)
                .load(message.getVideoMainUrl())
                .thumbnail(Glide.with(mContext).load(message.getVideoThumbnailUrl()))
                .placeholder(R.drawable.ism_avatar_group_large)
                .transform(new CenterCrop())
                .into(videoMessageSentViewHolder.ismSentMessageVideoBinding.ivVideoThumbnail);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
          if (message.isUploading()) {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.tvVideoStatus.setText(
                mContext.getString(R.string.ism_attachments_cancel));
            videoMessageSentViewHolder.ismSentMessageVideoBinding.pbUpload.setVisibility(
                View.VISIBLE);
          } else {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.tvVideoStatus.setText(
                mContext.getString(R.string.ism_remove));
            videoMessageSentViewHolder.ismSentMessageVideoBinding.pbUpload.setVisibility(View.GONE);
          }
          videoMessageSentViewHolder.ismSentMessageVideoBinding.rlUpload.setOnClickListener(v -> {
            if (message.isUploading()) {
              conversationMessagesActivity.cancelMediaUpload(message, position);
            } else {
              conversationMessagesActivity.removeCanceledMessage(message.getLocalMessageId(),
                  position);
            }
          });
          videoMessageSentViewHolder.ismSentMessageVideoBinding.rlUpload.setVisibility(
              View.VISIBLE);
        }

        videoMessageSentViewHolder.ismSentMessageVideoBinding.tvVideoSize.setText(
            message.getMediaSizeInMB());

        videoMessageSentViewHolder.ismSentMessageVideoBinding.rlVideo.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureAudioMessageSentViewHolder(
      AudioMessageSentViewHolder audioMessageSentViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        audioMessageSentViewHolder.ismSentMessageAudioBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        if (message.isMessageSentSuccessfully()) {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.tvSendingMessage.setVisibility(
              View.GONE);
        } else {
          if (message.isSendingMessageFailed()) {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            audioMessageSentViewHolder.ismSentMessageAudioBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            audioMessageSentViewHolder.ismSentMessageAudioBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          audioMessageSentViewHolder.ismSentMessageAudioBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }
        if (message.isForwardedMessage()) {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivForward.setVisibility(View.GONE);
        } else {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          if (message.isMessageSentSuccessfully()) {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.ivForward.setVisibility(
                View.VISIBLE);
            audioMessageSentViewHolder.ismSentMessageAudioBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.ivForward.setVisibility(
                View.GONE);
          }
        }
        if (messagingDisabled) {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivForward.setVisibility(View.GONE);
        }
        if (message.isQuotedMessage()) {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.vParentMessage.ivMessageImage.setVisibility(
                View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      audioMessageSentViewHolder.ismSentMessageAudioBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            audioMessageSentViewHolder.ismSentMessageAudioBinding.vParentMessage.ivMessageImage.setVisibility(
                View.VISIBLE);
          }
        } else {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            audioMessageSentViewHolder.ismSentMessageAudioBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            audioMessageSentViewHolder.ismSentMessageAudioBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        audioMessageSentViewHolder.ismSentMessageAudioBinding.tvMessageTime.setText(
            message.getMessageTime());

        if (message.isUploaded()) {

          audioMessageSentViewHolder.ismSentMessageAudioBinding.rlUpload.setVisibility(View.GONE);
          if (message.isDownloaded() || message.isDownloading()) {

            if (message.isDownloaded()) {
              audioMessageSentViewHolder.ismSentMessageAudioBinding.tvDownloadAudioStatus.setText(
                  mContext.getString(R.string.ism_open));
              audioMessageSentViewHolder.ismSentMessageAudioBinding.pbDownload.setVisibility(
                  View.GONE);
            } else {

              audioMessageSentViewHolder.ismSentMessageAudioBinding.tvDownloadAudioStatus.setText(
                  mContext.getString(R.string.ism_attachments_cancel));
              audioMessageSentViewHolder.ismSentMessageAudioBinding.pbDownload.setVisibility(
                  View.VISIBLE);
            }

            audioMessageSentViewHolder.ismSentMessageAudioBinding.rlDownload.setOnClickListener(
                v -> {
                  if (message.isDownloading()) {
                    conversationMessagesActivity.cancelMediaDownload(message, position);
                  } else {
                    conversationMessagesActivity.handleClickOnMessageCell(message, true);
                  }
                });
            audioMessageSentViewHolder.ismSentMessageAudioBinding.rlDownload.setVisibility(
                View.VISIBLE);
          } else {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.rlDownload.setVisibility(
                View.GONE);
          }
        } else {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.rlDownload.setVisibility(View.GONE);
          if (message.isUploading()) {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.tvAudioStatus.setText(
                mContext.getString(R.string.ism_attachments_cancel));
            audioMessageSentViewHolder.ismSentMessageAudioBinding.pbUpload.setVisibility(
                View.VISIBLE);
          } else {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.tvAudioStatus.setText(
                mContext.getString(R.string.ism_remove));
            audioMessageSentViewHolder.ismSentMessageAudioBinding.pbUpload.setVisibility(View.GONE);
          }
          audioMessageSentViewHolder.ismSentMessageAudioBinding.rlUpload.setOnClickListener(v -> {
            if (message.isUploading()) {
              conversationMessagesActivity.cancelMediaUpload(message, position);
            } else {
              conversationMessagesActivity.removeCanceledMessage(message.getLocalMessageId(),
                  position);
            }
          });
          audioMessageSentViewHolder.ismSentMessageAudioBinding.rlUpload.setVisibility(
              View.VISIBLE);
        }
        audioMessageSentViewHolder.ismSentMessageAudioBinding.tvAudioName.setText(
            message.getAudioName());
        audioMessageSentViewHolder.ismSentMessageAudioBinding.tvAudioSize.setText(
            message.getMediaSizeInMB());
        audioMessageSentViewHolder.ismSentMessageAudioBinding.rlAudio.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureFileMessageSentViewHolder(
      FileMessageSentViewHolder fileMessageSentViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        fileMessageSentViewHolder.ismSentMessageFileBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          fileMessageSentViewHolder.ismSentMessageFileBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          fileMessageSentViewHolder.ismSentMessageFileBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          fileMessageSentViewHolder.ismSentMessageFileBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          fileMessageSentViewHolder.ismSentMessageFileBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          fileMessageSentViewHolder.ismSentMessageFileBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        if (message.isMessageSentSuccessfully()) {
          fileMessageSentViewHolder.ismSentMessageFileBinding.tvSendingMessage.setVisibility(
              View.GONE);
        } else {
          if (message.isSendingMessageFailed()) {
            fileMessageSentViewHolder.ismSentMessageFileBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            fileMessageSentViewHolder.ismSentMessageFileBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            fileMessageSentViewHolder.ismSentMessageFileBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            fileMessageSentViewHolder.ismSentMessageFileBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          fileMessageSentViewHolder.ismSentMessageFileBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }

        if (message.isForwardedMessage()) {
          fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          fileMessageSentViewHolder.ismSentMessageFileBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          fileMessageSentViewHolder.ismSentMessageFileBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          fileMessageSentViewHolder.ismSentMessageFileBinding.ivForward.setVisibility(View.GONE);
        } else {
          fileMessageSentViewHolder.ismSentMessageFileBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          if (message.isMessageSentSuccessfully()) {
            fileMessageSentViewHolder.ismSentMessageFileBinding.ivForward.setVisibility(
                View.VISIBLE);
            fileMessageSentViewHolder.ismSentMessageFileBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            fileMessageSentViewHolder.ismSentMessageFileBinding.ivForward.setVisibility(View.GONE);
          }
        }
        if (messagingDisabled) {
          fileMessageSentViewHolder.ismSentMessageFileBinding.ivForward.setVisibility(View.GONE);
        }
        if (message.isQuotedMessage()) {
          fileMessageSentViewHolder.ismSentMessageFileBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          fileMessageSentViewHolder.ismSentMessageFileBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          fileMessageSentViewHolder.ismSentMessageFileBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          fileMessageSentViewHolder.ismSentMessageFileBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            fileMessageSentViewHolder.ismSentMessageFileBinding.vParentMessage.ivMessageImage.setVisibility(
                View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      fileMessageSentViewHolder.ismSentMessageFileBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            fileMessageSentViewHolder.ismSentMessageFileBinding.vParentMessage.ivMessageImage.setVisibility(
                View.VISIBLE);
          }
        } else {
          fileMessageSentViewHolder.ismSentMessageFileBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            fileMessageSentViewHolder.ismSentMessageFileBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            fileMessageSentViewHolder.ismSentMessageFileBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            fileMessageSentViewHolder.ismSentMessageFileBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            fileMessageSentViewHolder.ismSentMessageFileBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          fileMessageSentViewHolder.ismSentMessageFileBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        fileMessageSentViewHolder.ismSentMessageFileBinding.tvMessageTime.setText(
            message.getMessageTime());

        if (message.isUploaded()) {

          fileMessageSentViewHolder.ismSentMessageFileBinding.rlUpload.setVisibility(View.GONE);
          if (message.isDownloaded() || message.isDownloading()) {

            if (message.isDownloaded()) {
              fileMessageSentViewHolder.ismSentMessageFileBinding.tvDownloadFileStatus.setText(
                  mContext.getString(R.string.ism_open));
              fileMessageSentViewHolder.ismSentMessageFileBinding.pbDownload.setVisibility(
                  View.GONE);
            } else {

              fileMessageSentViewHolder.ismSentMessageFileBinding.tvDownloadFileStatus.setText(
                  mContext.getString(R.string.ism_attachments_cancel));
              fileMessageSentViewHolder.ismSentMessageFileBinding.pbDownload.setVisibility(
                  View.VISIBLE);
            }

            fileMessageSentViewHolder.ismSentMessageFileBinding.rlDownload.setOnClickListener(v -> {
              if (message.isDownloading()) {
                conversationMessagesActivity.cancelMediaDownload(message, position);
              } else {
                conversationMessagesActivity.handleClickOnMessageCell(message, true);
              }
            });
            fileMessageSentViewHolder.ismSentMessageFileBinding.rlDownload.setVisibility(
                View.VISIBLE);
          } else {
            fileMessageSentViewHolder.ismSentMessageFileBinding.rlDownload.setVisibility(View.GONE);
          }
        } else {
          fileMessageSentViewHolder.ismSentMessageFileBinding.rlDownload.setVisibility(View.GONE);

          if (message.isUploading()) {
            fileMessageSentViewHolder.ismSentMessageFileBinding.tvFileStatus.setText(
                mContext.getString(R.string.ism_attachments_cancel));
            fileMessageSentViewHolder.ismSentMessageFileBinding.pbUpload.setVisibility(
                View.VISIBLE);
          } else {
            fileMessageSentViewHolder.ismSentMessageFileBinding.tvFileStatus.setText(
                mContext.getString(R.string.ism_remove));
            fileMessageSentViewHolder.ismSentMessageFileBinding.pbUpload.setVisibility(View.GONE);
          }
          fileMessageSentViewHolder.ismSentMessageFileBinding.rlUpload.setOnClickListener(v -> {
            if (message.isUploading()) {
              conversationMessagesActivity.cancelMediaUpload(message, position);
            } else {
              conversationMessagesActivity.removeCanceledMessage(message.getLocalMessageId(),
                  position);
            }
          });
          fileMessageSentViewHolder.ismSentMessageFileBinding.rlUpload.setVisibility(View.VISIBLE);
        }
        fileMessageSentViewHolder.ismSentMessageFileBinding.tvFileName.setText(
            message.getFileName());
        fileMessageSentViewHolder.ismSentMessageFileBinding.tvFileSize.setText(
            message.getMediaSizeInMB());
        fileMessageSentViewHolder.ismSentMessageFileBinding.rlFile.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureStickerMessageSentViewHolder(
      StickerMessageSentViewHolder stickerMessageSentViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        if (message.isMessageSentSuccessfully()) {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvSendingMessage.setVisibility(
              View.GONE);
        } else {
          if (message.isSendingMessageFailed()) {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }
        if (message.isForwardedMessage()) {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            stickerMessageSentViewHolder.ismSentMessageStickerBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            stickerMessageSentViewHolder.ismSentMessageStickerBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivForward.setVisibility(
              View.GONE);
        } else {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          if (message.isMessageSentSuccessfully()) {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivForward.setVisibility(
                View.VISIBLE);
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivForward.setVisibility(
                View.GONE);
          }
        }
        if (messagingDisabled) {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivForward.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.vParentMessage.ivMessageImage.setVisibility(
                View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      stickerMessageSentViewHolder.ismSentMessageStickerBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.vParentMessage.ivMessageImage.setVisibility(
                View.VISIBLE);
          }
        } else {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            stickerMessageSentViewHolder.ismSentMessageStickerBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        try {
          Glide.with(mContext)
              .load(message.getStickerMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getStickerStillUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivStickerImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvMessageTime.setText(
            message.getMessageTime());

        stickerMessageSentViewHolder.ismSentMessageStickerBinding.rlSticker.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message, false));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureGifMessageSentViewHolder(GifMessageSentViewHolder gifMessageSentViewHolder,
      int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        gifMessageSentViewHolder.ismSentMessageGifBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          gifMessageSentViewHolder.ismSentMessageGifBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          gifMessageSentViewHolder.ismSentMessageGifBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          gifMessageSentViewHolder.ismSentMessageGifBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          gifMessageSentViewHolder.ismSentMessageGifBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          gifMessageSentViewHolder.ismSentMessageGifBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        if (message.isMessageSentSuccessfully()) {
          gifMessageSentViewHolder.ismSentMessageGifBinding.tvSendingMessage.setVisibility(
              View.GONE);
        } else {
          if (message.isSendingMessageFailed()) {
            gifMessageSentViewHolder.ismSentMessageGifBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            gifMessageSentViewHolder.ismSentMessageGifBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            gifMessageSentViewHolder.ismSentMessageGifBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            gifMessageSentViewHolder.ismSentMessageGifBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          gifMessageSentViewHolder.ismSentMessageGifBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }
        if (message.isForwardedMessage()) {
          gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          gifMessageSentViewHolder.ismSentMessageGifBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          gifMessageSentViewHolder.ismSentMessageGifBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          gifMessageSentViewHolder.ismSentMessageGifBinding.ivForward.setVisibility(View.GONE);
        } else {
          gifMessageSentViewHolder.ismSentMessageGifBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          if (message.isMessageSentSuccessfully()) {
            gifMessageSentViewHolder.ismSentMessageGifBinding.ivForward.setVisibility(View.VISIBLE);
            gifMessageSentViewHolder.ismSentMessageGifBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            gifMessageSentViewHolder.ismSentMessageGifBinding.ivForward.setVisibility(View.GONE);
          }
        }
        if (messagingDisabled) {
          gifMessageSentViewHolder.ismSentMessageGifBinding.ivForward.setVisibility(View.GONE);
        }
        if (message.isQuotedMessage()) {
          gifMessageSentViewHolder.ismSentMessageGifBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          gifMessageSentViewHolder.ismSentMessageGifBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          gifMessageSentViewHolder.ismSentMessageGifBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          gifMessageSentViewHolder.ismSentMessageGifBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            gifMessageSentViewHolder.ismSentMessageGifBinding.vParentMessage.ivMessageImage.setVisibility(
                View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      gifMessageSentViewHolder.ismSentMessageGifBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            gifMessageSentViewHolder.ismSentMessageGifBinding.vParentMessage.ivMessageImage.setVisibility(
                View.VISIBLE);
          }
        } else {
          gifMessageSentViewHolder.ismSentMessageGifBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            gifMessageSentViewHolder.ismSentMessageGifBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            gifMessageSentViewHolder.ismSentMessageGifBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            gifMessageSentViewHolder.ismSentMessageGifBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            gifMessageSentViewHolder.ismSentMessageGifBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          gifMessageSentViewHolder.ismSentMessageGifBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        try {
          Glide.with(mContext)
              .load(message.getGifMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getGifStillUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(gifMessageSentViewHolder.ismSentMessageGifBinding.ivGifImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        gifMessageSentViewHolder.ismSentMessageGifBinding.tvMessageTime.setText(
            message.getMessageTime());
        gifMessageSentViewHolder.ismSentMessageGifBinding.rlGif.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message, false));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureWhiteboardMessageSentViewHolder(
      WhiteboardMessageSentViewHolder whiteboardMessageSentViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        if (message.isMessageSentSuccessfully()) {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvSendingMessage.setVisibility(
              View.GONE);
        } else {
          if (message.isSendingMessageFailed()) {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }
        if (message.isForwardedMessage()) {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivForward.setVisibility(
              View.GONE);
        } else {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          if (message.isMessageSentSuccessfully()) {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivForward.setVisibility(
                View.VISIBLE);
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivForward.setVisibility(
                View.GONE);
          }
        }
        if (messagingDisabled) {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivForward.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vParentMessage.tvSenderName
              .setText(message.getOriginalMessageSenderName());
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vParentMessage.tvMessageTime
              .setText(message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        if (message.isUploaded()) {

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlUpload.setVisibility(
              View.GONE);
          if (message.isDownloaded() || message.isDownloading()) {

            if (message.isDownloaded()) {
              whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvDownloadWhiteboardStatus
                  .setText(mContext.getString(R.string.ism_open));
              whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.pbDownload.setVisibility(
                  View.GONE);
            } else {

              whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvDownloadWhiteboardStatus
                  .setText(mContext.getString(R.string.ism_attachments_cancel));
              whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.pbDownload.setVisibility(
                  View.VISIBLE);
            }

            try {
              Glide.with(mContext)
                  .load(message.getWhiteboardMainUrl())
                  .thumbnail(Glide.with(mContext).load(message.getWhiteboardThumbnailUrl()))
                  .placeholder(R.drawable.ism_avatar_group_large)
                  .transform(new CenterCrop())
                  .into(
                      whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivWhiteboard);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }

            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlDownload.setOnClickListener(
                v -> {
                  if (message.isDownloading()) {
                    conversationMessagesActivity.cancelMediaDownload(message, position);
                  } else {
                    conversationMessagesActivity.handleClickOnMessageCell(message, true);
                  }
                });
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlDownload.setVisibility(
                View.VISIBLE);
          } else {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlDownload.setVisibility(
                View.GONE);

            try {
              if(message.getLocalMediaPath()==null){
              Glide.with(mContext)
                  .load(message.getWhiteboardMainUrl())
                  .thumbnail(Glide.with(mContext).load(message.getWhiteboardThumbnailUrl()))
                  .placeholder(R.drawable.ism_avatar_group_large)
                  .transform(new CenterCrop(),
                      new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
                  .into(
                      whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivWhiteboard);
            }else{
                Glide.with(mContext)
                    .load(message.getLocalMediaPath())
                    .thumbnail(Glide.with(mContext).load(message.getLocalMediaPath()))
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(new CenterCrop(),
                        new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
                    .into(
                        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivWhiteboard);

              }

            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
          }
        } else {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlDownload.setVisibility(
              View.GONE);
          try {
            Glide.with(mContext)
                .load(message.getWhiteboardMainUrl())
                .thumbnail(Glide.with(mContext).load(message.getWhiteboardThumbnailUrl()))
                .placeholder(R.drawable.ism_avatar_group_large)
                .transform(new CenterCrop())
                .into(whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivWhiteboard);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
          if (message.isUploading()) {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvWhiteboardStatus.setText(
                mContext.getString(R.string.ism_attachments_cancel));
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.pbUpload.setVisibility(
                View.VISIBLE);
          } else {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvWhiteboardStatus.setText(
                mContext.getString(R.string.ism_remove));
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.pbUpload.setVisibility(
                View.GONE);
          }

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlUpload.setOnClickListener(
              v -> {
                if (message.isUploading()) {
                  conversationMessagesActivity.cancelMediaUpload(message, position);
                } else {
                  conversationMessagesActivity.removeCanceledMessage(message.getLocalMessageId(),
                      position);
                }
              });

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlUpload.setVisibility(
              View.VISIBLE);
        }

        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvWhiteboardSize.setText(
            message.getMediaSizeInMB());

        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvMessageTime.setText(
            message.getMessageTime());
        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlWhiteboard.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureLocationMessageSentViewHolder(
      LocationMessageSentViewHolder locationMessageSentViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        locationMessageSentViewHolder.ismSentMessageLocationBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        //locationMessageSentViewHolder.ismSentMessageLocationBinding.  map.onCreate(null);
        //locationMessageSentViewHolder.ismSentMessageLocationBinding.   map.onResume();  //Probably U r missing this
        try {
          Glide.with(mContext)
              .load(Constants.LOCATION_PLACEHOLDER_IMAGE_URL)
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(locationMessageSentViewHolder.ismSentMessageLocationBinding.ivLocationImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        if(message.isMessageSentSuccessfully()){
          locationMessageSentViewHolder.ismSentMessageLocationBinding.tvSendingMessage.setVisibility(View.GONE);
        }else{
          if (message.isSendingMessageFailed()) {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            locationMessageSentViewHolder.ismSentMessageLocationBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            locationMessageSentViewHolder.ismSentMessageLocationBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          locationMessageSentViewHolder.ismSentMessageLocationBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }
        if (message.isForwardedMessage()) {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            locationMessageSentViewHolder.ismSentMessageLocationBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            locationMessageSentViewHolder.ismSentMessageLocationBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          locationMessageSentViewHolder.ismSentMessageLocationBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivForward.setVisibility(
              View.GONE);
        } else {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          if (message.isMessageSentSuccessfully()) {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.ivForward.setVisibility(
                View.VISIBLE);
            locationMessageSentViewHolder.ismSentMessageLocationBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.ivForward.setVisibility(
                View.GONE);
          }
        }
        if (messagingDisabled) {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivForward.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          locationMessageSentViewHolder.ismSentMessageLocationBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          locationMessageSentViewHolder.ismSentMessageLocationBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          locationMessageSentViewHolder.ismSentMessageLocationBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      locationMessageSentViewHolder.ismSentMessageLocationBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            locationMessageSentViewHolder.ismSentMessageLocationBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            locationMessageSentViewHolder.ismSentMessageLocationBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            locationMessageSentViewHolder.ismSentMessageLocationBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        locationMessageSentViewHolder.ismSentMessageLocationBinding.tvLocationName.setText(
            message.getLocationName());
        locationMessageSentViewHolder.ismSentMessageLocationBinding.tvLocationDescription.setText(
            message.getLocationDescription());

        locationMessageSentViewHolder.ismSentMessageLocationBinding.tvMessageTime.setText(
            message.getMessageTime());

        locationMessageSentViewHolder.ismSentMessageLocationBinding.rlLocationLayout.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message, false));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureContactMessageSentViewHolder(
      ContactMessageSentViewHolder contactMessageSentViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        contactMessageSentViewHolder.ismSentMessageContactBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isReadByAll()) {
          contactMessageSentViewHolder.ismSentMessageContactBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

          contactMessageSentViewHolder.ismSentMessageContactBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else if (message.isDeliveredToAll()) {
          contactMessageSentViewHolder.ismSentMessageContactBinding.ivDeliveryReadStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

          contactMessageSentViewHolder.ismSentMessageContactBinding.ivDeliveryReadStatus.setVisibility(
              View.VISIBLE);
        } else {

          contactMessageSentViewHolder.ismSentMessageContactBinding.ivDeliveryReadStatus.setVisibility(
              View.GONE);
        }

        if (message.isMessageSentSuccessfully()) {
          contactMessageSentViewHolder.ismSentMessageContactBinding.tvSendingMessage.setVisibility(
              View.GONE);
        } else {
          if (message.isSendingMessageFailed()) {
            contactMessageSentViewHolder.ismSentMessageContactBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_leave_red));
            contactMessageSentViewHolder.ismSentMessageContactBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_failed));
          } else {
            contactMessageSentViewHolder.ismSentMessageContactBinding.tvSendingMessage.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
            contactMessageSentViewHolder.ismSentMessageContactBinding.tvSendingMessage.setText(
                mContext.getString(R.string.ism_sending));
          }
          contactMessageSentViewHolder.ismSentMessageContactBinding.tvSendingMessage.setVisibility(
              View.VISIBLE);
        }

        if (message.isForwardedMessage()) {
          contactMessageSentViewHolder.ismSentMessageContactBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            contactMessageSentViewHolder.ismSentMessageContactBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            contactMessageSentViewHolder.ismSentMessageContactBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            contactMessageSentViewHolder.ismSentMessageContactBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          contactMessageSentViewHolder.ismSentMessageContactBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          contactMessageSentViewHolder.ismSentMessageContactBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          contactMessageSentViewHolder.ismSentMessageContactBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          contactMessageSentViewHolder.ismSentMessageContactBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          contactMessageSentViewHolder.ismSentMessageContactBinding.ivForward.setVisibility(
              View.GONE);
        } else {
          contactMessageSentViewHolder.ismSentMessageContactBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          if (message.isMessageSentSuccessfully()) {
            contactMessageSentViewHolder.ismSentMessageContactBinding.ivForward.setVisibility(
                View.VISIBLE);
            contactMessageSentViewHolder.ismSentMessageContactBinding.ivForward.setOnClickListener(
                v -> conversationMessagesActivity.forwardMessageRequest(message));
          } else {
            contactMessageSentViewHolder.ismSentMessageContactBinding.ivForward.setVisibility(
                View.GONE);
          }
        }
        if (messagingDisabled) {
          contactMessageSentViewHolder.ismSentMessageContactBinding.ivForward.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          contactMessageSentViewHolder.ismSentMessageContactBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          contactMessageSentViewHolder.ismSentMessageContactBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          contactMessageSentViewHolder.ismSentMessageContactBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          contactMessageSentViewHolder.ismSentMessageContactBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            contactMessageSentViewHolder.ismSentMessageContactBinding.vParentMessage.ivMessageImage.setVisibility(
                View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      contactMessageSentViewHolder.ismSentMessageContactBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            contactMessageSentViewHolder.ismSentMessageContactBinding.vParentMessage.ivMessageImage.setVisibility(
                View.VISIBLE);
          }
        } else {
          contactMessageSentViewHolder.ismSentMessageContactBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            contactMessageSentViewHolder.ismSentMessageContactBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            contactMessageSentViewHolder.ismSentMessageContactBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            contactMessageSentViewHolder.ismSentMessageContactBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            contactMessageSentViewHolder.ismSentMessageContactBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          contactMessageSentViewHolder.ismSentMessageContactBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (PlaceholderUtils.isValidImageUrl(message.getContactImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getContactImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(contactMessageSentViewHolder.ismSentMessageContactBinding.ivContactImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getContactName(),
              contactMessageSentViewHolder.ismSentMessageContactBinding.ivContactImage, 12);
        }

        if(message.getContactList().length() > 1){
          if(message.getContactList().length() == 2){
            contactMessageSentViewHolder.ismSentMessageContactBinding.tvContactName.setText(
                    message.getContactName() + mContext.getString(R.string.ism_and_1_other_contact));
          }else{
            contactMessageSentViewHolder.ismSentMessageContactBinding.tvContactName.setText(
                    message.getContactName() + mContext.getString(R.string.ism_and)+(message.getContactList().length()-1)+mContext.getString(R.string.ism_other_contacts));
          }

          contactMessageSentViewHolder.ismSentMessageContactBinding.tvContactIdentifier.setVisibility(View.GONE);
          contactMessageSentViewHolder.ismSentMessageContactBinding.divider.setVisibility(View.VISIBLE);
          contactMessageSentViewHolder.ismSentMessageContactBinding.textViewAll.setVisibility(View.VISIBLE);
        }else{
          contactMessageSentViewHolder.ismSentMessageContactBinding.tvContactName.setText(
                  message.getContactName());
          contactMessageSentViewHolder.ismSentMessageContactBinding.tvContactIdentifier.setVisibility(View.VISIBLE);
          contactMessageSentViewHolder.ismSentMessageContactBinding.divider.setVisibility(View.GONE);
          contactMessageSentViewHolder.ismSentMessageContactBinding.textViewAll.setVisibility(View.GONE);
        }


        contactMessageSentViewHolder.ismSentMessageContactBinding.tvContactIdentifier.setText(
            message.getContactIdentifier());
        contactMessageSentViewHolder.ismSentMessageContactBinding.tvMessageTime.setText(
            message.getMessageTime());

        contactMessageSentViewHolder.ismSentMessageContactBinding.rlContact.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message, false));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureTextMessageReceivedViewHolder(
      TextMessageReceivedViewHolder textMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.dividerForward.setVisibility(
              View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.dividerForwardNotes.setVisibility(
                View.GONE);
          } else {
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.dividerForwardNotes.setVisibility(
                View.VISIBLE);
          }
        } else {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.dividerForward.setVisibility(
              View.GONE);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.dividerForwardNotes.setVisibility(
              View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }
        if (messagingDisabled) {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.dividerReply.setVisibility(
              View.VISIBLE);

          if (message.getOriginalMessagePlaceholderImage() == null) {
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.dividerReply.setVisibility(
              View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.tvSenderName.setText(
              spannableString);
        } else {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivSenderImage, position,
              12);
        }
        textMessageReceivedViewHolder.ismReceivedMessageTextBinding.tvMessageTime.setText(
            message.getMessageTime());
        textMessageReceivedViewHolder.ismReceivedMessageTextBinding.tvTextMessage.setText(
            message.getTextMessage());
        textMessageReceivedViewHolder.ismReceivedMessageTextBinding.tvTextMessage.setMovementMethod(
            LinkMovementMethod.getInstance());
        textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivReaction.setVisibility(
              View.GONE);
        }
      }
    } catch (Exception ignore) {
    }
  }

  private void configurePhotoMessageReceivedViewHolder(
      PhotoMessageReceivedViewHolder photoMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }
        if (messagingDisabled) {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vParentMessage.tvMessageTime
              .setText(message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvSenderName.setText(
              spannableString);
        } else {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivSenderImage, position,
              12);
        }

        try {
          Glide.with(mContext)
              .load(message.getPhotoMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getPhotoThumbnailUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop())
              .into(photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivPhoto);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        if (message.isDownloaded()) {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvPhotoStatus.setText(
              mContext.getString(R.string.ism_open));
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.pbDownload.setVisibility(
              View.GONE);
        } else {

          if (message.isDownloading()) {
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvPhotoStatus.setText(
                mContext.getString(R.string.ism_attachments_cancel));
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.pbDownload.setVisibility(
                View.VISIBLE);
          } else {
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvPhotoStatus.setText(
                mContext.getString(R.string.ism_download));
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.pbDownload.setVisibility(
                View.GONE);
          }
        }

        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvPhotoSize.setText(
            message.getMediaSizeInMB());

        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvMessageTime.setText(
            message.getMessageTime());

        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivReaction.setVisibility(
              View.GONE);
        }
        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivPhoto.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));

        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rlDownload.setOnClickListener(
            v -> {

              if (message.isDownloaded()) {
                conversationMessagesActivity.handleClickOnMessageCell(message, true);
              } else {
                if (message.isDownloading()) {
                  conversationMessagesActivity.cancelMediaDownload(message, position);
                } else {
                  photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.pbDownload.setProgressCompat(
                      0, false);
                  conversationMessagesActivity.downloadMedia(message,
                      mContext.getString(R.string.ism_photo), position);
                }
              }
            });
      }
    } catch (Exception ignore) {
    }
  }

  private void configureVideoMessageReceivedViewHolder(
      VideoMessageReceivedViewHolder videoMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }
        if (messagingDisabled) {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vParentMessage.tvMessageTime
              .setText(message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvSenderName.setText(
              spannableString);
        } else {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivSenderImage, position,
              12);
        }
        try {
          Glide.with(mContext)
              .load(message.getVideoMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getVideoThumbnailUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop())
              .into(videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivVideoThumbnail);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        if (message.isDownloaded()) {

          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvVideoStatus.setText(
              mContext.getString(R.string.ism_open));
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.pbDownload.setVisibility(
              View.GONE);
        } else {

          if (message.isDownloading()) {
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvVideoStatus.setText(
                mContext.getString(R.string.ism_attachments_cancel));
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.pbDownload.setVisibility(
                View.VISIBLE);
          } else {
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvVideoStatus.setText(
                mContext.getString(R.string.ism_download));
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.pbDownload.setVisibility(
                View.GONE);
          }
        }

        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvVideoSize.setText(
            message.getMediaSizeInMB());

        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvMessageTime.setText(
            message.getMessageTime());

        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivReaction.setVisibility(
              View.GONE);
        }
        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rlDownload.setOnClickListener(
            v -> {
              if (message.isDownloaded()) {
                conversationMessagesActivity.handleClickOnMessageCell(message, true);
              } else {
                if (message.isDownloading()) {
                  conversationMessagesActivity.cancelMediaDownload(message, position);
                } else {
                  videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.pbDownload.setProgressCompat(
                      0, false);
                  conversationMessagesActivity.downloadMedia(message,
                      mContext.getString(R.string.ism_video), position);
                }
              }
            });

        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rlVideo.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureAudioMessageReceivedViewHolder(
      AudioMessageReceivedViewHolder audioMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }
        if (messagingDisabled) {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vParentMessage.tvMessageTime
              .setText(message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvSenderName.setText(
              spannableString);
        } else {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivSenderImage, position,
              12);
        }
        if (message.isDownloaded()) {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvAudioStatus.setText(
              mContext.getString(R.string.ism_open));
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.pbDownload.setVisibility(
              View.GONE);
        } else {

          if (message.isDownloading()) {
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvAudioStatus.setText(
                mContext.getString(R.string.ism_attachments_cancel));
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.pbDownload.setVisibility(
                View.VISIBLE);
          } else {
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvAudioStatus.setText(
                mContext.getString(R.string.ism_download));
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.pbDownload.setVisibility(
                View.GONE);
          }
        }
        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvAudioName.setText(
            message.getAudioName());
        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvAudioSize.setText(
            message.getMediaSizeInMB());

        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvMessageTime.setText(
            message.getMessageTime());

        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivReaction.setVisibility(
              View.GONE);
        }
        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rlDownload.setOnClickListener(
            v -> {
              if (message.isDownloaded()) {
                conversationMessagesActivity.handleClickOnMessageCell(message, true);
              } else {
                if (message.isDownloading()) {
                  conversationMessagesActivity.cancelMediaDownload(message, position);
                } else {

                  audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.pbDownload.setProgressCompat(
                      0, false);
                  conversationMessagesActivity.downloadMedia(message,
                      mContext.getString(R.string.ism_audio_recording), position);
                }
              }
            });
        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rlAudio.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureFileMessageReceivedViewHolder(
      FileMessageReceivedViewHolder fileMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }

        if (messagingDisabled) {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvSenderName.setText(
              spannableString);
        } else {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivSenderImage, position,
              12);
        }
        if (message.isDownloaded()) {

          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvFileStatus.setText(
              mContext.getString(R.string.ism_open));
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.pbDownload.setVisibility(
              View.GONE);
        } else {

          if (message.isDownloading()) {
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvFileStatus.setText(
                mContext.getString(R.string.ism_attachments_cancel));
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.pbDownload.setVisibility(
                View.VISIBLE);
          } else {
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvFileStatus.setText(
                mContext.getString(R.string.ism_download));
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.pbDownload.setVisibility(
                View.GONE);
          }

        }
        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvFileName.setText(
            message.getFileName());
        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvFileSize.setText(
            message.getMediaSizeInMB());

        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvMessageTime.setText(
            message.getMessageTime());

        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivReaction.setVisibility(
              View.GONE);
        }
        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rlDownload.setOnClickListener(
            v -> {
              if (message.isDownloaded()) {
                conversationMessagesActivity.handleClickOnMessageCell(message, true);
              } else {
                if (message.isDownloading()) {
                  conversationMessagesActivity.cancelMediaDownload(message, position);
                } else {

                  fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.pbDownload.setProgressCompat(
                      0, false);
                  conversationMessagesActivity.downloadMedia(message,
                      mContext.getString(R.string.ism_file), position);
                }
              }
            });
        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rlFile.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureStickerMessageReceivedViewHolder(
      StickerMessageReceivedViewHolder stickerMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }
        if (messagingDisabled) {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vParentMessage.tvSenderName
              .setText(message.getOriginalMessageSenderName());
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vParentMessage.tvMessage
              .setText(message.getOriginalMessage());
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vParentMessage.tvMessageTime
              .setText(message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.tvSenderName.setText(
              spannableString);
        } else {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivSenderImage, position,
              12);
        }
        try {
          Glide.with(mContext)
              .load(message.getStickerMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getStickerStillUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(
                  stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivStickerImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.tvMessageTime.setText(
            message.getMessageTime());

        stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rlSticker.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message, false));

        stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivReaction.setVisibility(
              View.GONE);
        }
      }
    } catch (Exception ignore) {
    }
  }

  private void configureGifMessageReceivedViewHolder(
      GifMessageReceivedViewHolder gifMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }
        if (messagingDisabled) {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vParentMessage.tvSenderName.setText(
              message.getOriginalMessageSenderName());
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vParentMessage.tvMessage.setText(
              message.getOriginalMessage());
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vParentMessage.tvMessageTime.setText(
              message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vParentMessage.ivMessageImage.setVisibility(
                View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vParentMessage.ivMessageImage.setVisibility(
                View.VISIBLE);
          }
        } else {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.tvSenderName.setText(
              spannableString);
        } else {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivSenderImage, position,
              12);
        }
        try {
          Glide.with(mContext)
              .load(message.getGifMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getGifStillUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivGifImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.tvMessageTime.setText(
            message.getMessageTime());
        gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rlGif.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message, false));

        gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivReaction.setVisibility(
              View.GONE);
        }
      }
    } catch (Exception ignore) {
    }
  }

  private void configureWhiteboardMessageReceivedViewHolder(
      WhiteboardMessageReceivedViewHolder whiteboardMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }
        if (messagingDisabled) {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vParentMessage.tvSenderName
              .setText(message.getOriginalMessageSenderName());
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vParentMessage.tvMessage
              .setText(message.getOriginalMessage());
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vParentMessage.tvMessageTime
              .setText(message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rvMessageReactions
                .setVisibility(View.GONE);
          } else {
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rvMessageReactions
                .setLayoutManager(
                    new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rvMessageReactions
                .setAdapter(new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rvMessageReactions
                .setVisibility(View.VISIBLE);
          }
        } else {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rvMessageReactions
              .setVisibility(View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvSenderName.setText(
              spannableString);
        } else {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivSenderImage,
              position, 12);
        }
        try {
          Glide.with(mContext)
              .load(message.getWhiteboardMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getWhiteboardThumbnailUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop())
              .into(
                  whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivWhiteboard);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        if (message.isDownloaded()) {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvWhiteboardStatus
              .setText(mContext.getString(R.string.ism_open));
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.pbDownload.setVisibility(
              View.GONE);
        } else {
          if (message.isDownloading()) {
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvWhiteboardStatus
                .setText(mContext.getString(R.string.ism_attachments_cancel));
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.pbDownload.setVisibility(
                View.VISIBLE);
          } else {
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvWhiteboardStatus
                .setText(mContext.getString(R.string.ism_download));
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.pbDownload.setVisibility(
                View.GONE);
          }
        }

        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvWhiteboardSize.setText(
            message.getMediaSizeInMB());

        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvMessageTime.setText(
            message.getMessageTime());

        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rlWhiteboard.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message,
                message.isDownloaded()));

        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivReaction.setVisibility(
              View.GONE);
        }
        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rlDownload.setOnClickListener(
            v -> {
              if (message.isDownloaded()) {
                conversationMessagesActivity.handleClickOnMessageCell(message, true);
              } else {
                if (message.isDownloading()) {
                  conversationMessagesActivity.cancelMediaDownload(message, position);
                } else {

                  whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.pbDownload
                      .setProgressCompat(0, false);
                  conversationMessagesActivity.downloadMedia(message,
                      mContext.getString(R.string.ism_whiteboard), position);
                }
              }
            });
      }
    } catch (Exception ignore) {
    }
  }

  private void configureLocationMessageReceivedViewHolder(
      LocationMessageReceivedViewHolder locationMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }
        if (multipleMessagesSelectModeOn) {

          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }
        if (messagingDisabled) {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vParentMessage.tvSenderName
              .setText(message.getOriginalMessageSenderName());
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vParentMessage.tvMessage
              .setText(message.getOriginalMessage());
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vParentMessage.tvMessageTime
              .setText(message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }
        try {
          Glide.with(mContext)
              .load(Constants.LOCATION_PLACEHOLDER_IMAGE_URL)
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(
                  locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivLocationImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        if (message.hasReactions()) {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  reactionClickListener));

          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
        } else {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.tvSenderName.setText(
              spannableString);
        } else {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivSenderImage,
              position, 12);
        }
        locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.tvLocationName.setText(
            message.getLocationName());
        locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.tvLocationDescription.setText(
            message.getLocationDescription());
        locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.tvMessageTime.setText(
            message.getMessageTime());

        locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivReaction.setVisibility(
              View.GONE);
        }
        locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.rlLocationLayout.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message, false));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureContactMessageReceivedViewHolder(
      ContactMessageReceivedViewHolder contactMessageReceivedViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivEdited.setVisibility(
            message.isEditedMessage() ? View.VISIBLE : View.GONE);

        if (message.isForwardedMessage()) {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vForwardedMessageNotes.tvMessage
                .setText(message.getForwardedMessageNotes());

            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);

        }

        if (multipleMessagesSelectModeOn) {

          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivSelectedStatus.setSelected(
              message.isSelected());
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivSelectedStatus.setVisibility(
              View.VISIBLE);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivReaction.setVisibility(
              View.GONE);
        } else {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivSelectedStatus.setVisibility(
              View.GONE);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivReaction.setVisibility(
              View.VISIBLE);
        }
        if (messagingDisabled) {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivReaction.setVisibility(
              View.GONE);
        }
        if (message.isQuotedMessage()) {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vParentMessage.getRoot()
              .setVisibility(View.VISIBLE);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vParentMessage.tvSenderName
              .setText(message.getOriginalMessageSenderName());
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vParentMessage.tvMessage
              .setText(message.getOriginalMessage());
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vParentMessage.tvMessageTime
              .setText(message.getOriginalMessageTime());
          if (message.getOriginalMessagePlaceholderImage() == null) {
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vParentMessage.ivMessageImage
                .setVisibility(View.GONE);
          } else {
            try {
              Glide.with(mContext)
                  .load(message.getOriginalMessagePlaceholderImage())
                  .diskCacheStrategy(DiskCacheStrategy.NONE)
                  .into(
                      contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vParentMessage.ivMessageImage);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vParentMessage.ivMessageImage
                .setVisibility(View.VISIBLE);
          }
        } else {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vParentMessage.getRoot()
              .setVisibility(View.GONE);
        }

        if (message.hasReactions()) {
          if (multipleMessagesSelectModeOn) {
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rvMessageReactions.setVisibility(
                View.GONE);
          } else {
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rvMessageReactions.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rvMessageReactions.setAdapter(
                new MessageReactionsAdapter(mContext, message.getReactions(),
                    message.getMessageId(), reactionClickListener));

            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rvMessageReactions.setVisibility(
                View.VISIBLE);
          }
        } else {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        if (message.isSenderDeleted()) {
          SpannableString spannableString = new SpannableString(message.getSenderName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvSenderName.setText(
              spannableString);
        } else {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvSenderName.setText(
              message.getSenderName());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getSenderImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getSenderImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivSenderImage, position,
              12);
        }
        if (PlaceholderUtils.isValidImageUrl(message.getContactImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getContactImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivContactImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {

          PlaceholderUtils.setTextRoundDrawable(mContext, message.getContactName(),
              contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivContactImage, 12);
        }
        if(message.getContactList().length() > 1){
          if(message.getContactList().length() == 2){
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvContactName.setText(
                    message.getContactName() + mContext.getString(R.string.ism_and_1_other_contact));
          }else{
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvContactName.setText(
                    message.getContactName() + mContext.getString(R.string.ism_and)+(message.getContactList().length()-1)+mContext.getString(R.string.ism_other_contacts));
          }

          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvContactIdentifier.setVisibility(View.GONE);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.divider.setVisibility(View.VISIBLE);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.textViewAll.setVisibility(View.VISIBLE);
        }else{
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvContactName.setText(
                  message.getContactName());
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvContactIdentifier.setText(message.getContactIdentifier());
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvContactIdentifier.setVisibility(View.VISIBLE);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.divider.setVisibility(View.GONE);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.textViewAll.setVisibility(View.GONE);
        }



        contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvMessageTime.setText(
            message.getMessageTime());

        contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivReaction.setOnClickListener(
            v -> conversationMessagesActivity.addReactionForMessage(message.getMessageId()));
        if (joiningAsObserver) {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivReaction.setVisibility(
              View.GONE);
        }
        contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rlContact.setOnClickListener(
            v -> conversationMessagesActivity.handleClickOnMessageCell(message, false));
      }
    } catch (Exception ignore) {
    }
  }

  private void configureConversationActionMessageViewHolder(
      ConversationActionMessageViewHolder conversationActionMessageViewHolder, int position) {
    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        conversationActionMessageViewHolder.ismConversationActionMessageBinding.tvActionMessage.setText(
            message.getConversationActionMessage());
      }
    } catch (Exception ignore) {
    }
  }

    private void configureReplayMessageSentViewHolder(
            TextMessageSentViewHolder textMessageSentViewHolder, int position) {
        try {
            MessagesModel message = messages.get(position);
            if (message != null) {

                textMessageSentViewHolder.ismSentMessageTextBinding.ivEdited.setVisibility(
                        message.isEditedMessage() ? View.VISIBLE : View.GONE);

                if (message.isReadByAll()) {
                    textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setImageDrawable(
                            ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle));

                    textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setVisibility(
                            View.VISIBLE);
                } else if (message.isDeliveredToAll()) {
                    textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setImageDrawable(
                            ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle));

                    textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setVisibility(
                            View.VISIBLE);
                } else {

                    textMessageSentViewHolder.ismSentMessageTextBinding.ivDeliveryReadStatus.setVisibility(
                            View.GONE);
                }

                if (message.isMessageSentSuccessfully()) {
                    textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setVisibility(
                            View.GONE);
                } else {
                    if (message.isSendingMessageFailed()) {
                        textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setTextColor(
                                ContextCompat.getColor(mContext, R.color.ism_leave_red));
                        textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setText(
                                mContext.getString(R.string.ism_failed));
                    } else {
                        textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setTextColor(
                                ContextCompat.getColor(mContext, R.color.ism_message_time_grey));
                        textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setText(
                                mContext.getString(R.string.ism_sending));
                    }
                    textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setVisibility(
                            View.VISIBLE);
                }

                if (message.isForwardedMessage()) {
                    textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessage.getRoot()
                            .setVisibility(View.VISIBLE);
                    textMessageSentViewHolder.ismSentMessageTextBinding.dividerForward.setVisibility(
                            View.VISIBLE);

                    if (message.getForwardedMessageNotes() == null) {

                        textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessageNotes.getRoot()
                                .setVisibility(View.GONE);
                        textMessageSentViewHolder.ismSentMessageTextBinding.dividerForwardNotes.setVisibility(
                                View.GONE);
                    } else {
                        textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessageNotes.tvMessage
                                .setText(message.getForwardedMessageNotes());

                        textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessageNotes.getRoot()
                                .setVisibility(View.VISIBLE);
                        textMessageSentViewHolder.ismSentMessageTextBinding.dividerForwardNotes.setVisibility(
                                View.VISIBLE);
                    }
                } else {
                    textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessage.getRoot()
                            .setVisibility(View.GONE);
                    textMessageSentViewHolder.ismSentMessageTextBinding.dividerForward.setVisibility(
                            View.GONE);
                    textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessageNotes.getRoot()
                            .setVisibility(View.GONE);
                    textMessageSentViewHolder.ismSentMessageTextBinding.dividerForwardNotes.setVisibility(
                            View.GONE);

                }

                if (multipleMessagesSelectModeOn) {

                    textMessageSentViewHolder.ismSentMessageTextBinding.ivSelectedStatus.setSelected(
                            message.isSelected());
                    textMessageSentViewHolder.ismSentMessageTextBinding.ivSelectedStatus.setVisibility(
                            View.VISIBLE);
                    textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setVisibility(View.GONE);
                } else {
                    textMessageSentViewHolder.ismSentMessageTextBinding.ivSelectedStatus.setVisibility(
                            View.GONE);
                    if (message.isMessageSentSuccessfully()) {
                        textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setVisibility(
                                View.VISIBLE);
                        textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setOnClickListener(
                                v -> conversationMessagesActivity.forwardMessageRequest(message));
                    } else {
                        textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setVisibility(View.GONE);
                    }
                }
                if (messagingDisabled) {
                    textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setVisibility(View.GONE);
                }
                if (message.isQuotedMessage()) {
                    textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.getRoot()
                            .setVisibility(View.VISIBLE);
                    textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.tvSenderName.setText(
                            message.getOriginalMessageSenderName());
                    textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.tvMessage.setText(
                            message.getOriginalMessage());
                    textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.tvMessageTime.setText(
                            message.getOriginalMessageTime());
                    textMessageSentViewHolder.ismSentMessageTextBinding.dividerReply.setVisibility(
                            View.VISIBLE);

                    if (message.getOriginalMessagePlaceholderImage() == null) {
                        textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.ivMessageImage.setVisibility(
                                View.GONE);
                    } else {
                        try {
                            Glide.with(mContext)
                                    .load(message.getOriginalMessagePlaceholderImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(
                                            textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.ivMessageImage);
                        } catch (IllegalArgumentException | NullPointerException ignore) {
                        }
                        textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.ivMessageImage.setVisibility(
                                View.VISIBLE);
                    }
                } else {
                    textMessageSentViewHolder.ismSentMessageTextBinding.vParentMessage.getRoot()
                            .setVisibility(View.GONE);
                    textMessageSentViewHolder.ismSentMessageTextBinding.dividerReply.setVisibility(View.GONE);

                }

                if (message.hasReactions()) {
                    if (multipleMessagesSelectModeOn) {
                        textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setVisibility(
                                View.GONE);
                    } else {
                        textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setLayoutManager(
                                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                        textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setAdapter(
                                new MessageReactionsAdapter(mContext, message.getReactions(),
                                        message.getMessageId(), reactionClickListener));

                        textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setVisibility(
                                View.VISIBLE);
                    }
                } else {
                    textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setVisibility(
                            View.GONE);
                }

                textMessageSentViewHolder.ismSentMessageTextBinding.tvTextMessage.setText(
                        message.getTextMessage());
                textMessageSentViewHolder.ismSentMessageTextBinding.tvTextMessage    .
                        setMovementMethod(LinkMovementMethod.getInstance());
                textMessageSentViewHolder.ismSentMessageTextBinding.tvMessageTime.setText(
                        message.getMessageTime());
            }
        } catch (Exception ignore) {
        }
    }

  /**
   * Update message status on media download finished.
   *
   * @param position the position
   * @param successfullyCompleted the successfully completed
   * @param downloadedMediaPath the downloaded media path
   */
  public void updateMessageStatusOnMediaDownloadFinished(int position,
      boolean successfullyCompleted, String downloadedMediaPath) {
    MessagesModel messagesModel = messages.get(position);
    messagesModel.setDownloaded(successfullyCompleted);
    messagesModel.setDownloading(false);
    if (successfullyCompleted) {
      messagesModel.setDownloadedMediaPath(downloadedMediaPath);
    }
    messages.set(position, messagesModel);

    notifyItemChanged(position);
  }

  /**
   * Update message status on downloading state changed.
   *
   * @param position the position
   * @param downloadingStarted the downloading started
   */
  public void updateMessageStatusOnDownloadingStateChanged(int position,
      boolean downloadingStarted) {

    MessagesModel messagesModel = messages.get(position);
    messagesModel.setDownloading(downloadingStarted);
    messages.set(position, messagesModel);
    notifyItemChanged(position);
  }

  /**
   * Update progress status of message.
   *
   * @param download the download
   * @param position the position
   * @param rvMessages the rv messages
   * @param progress the progress
   */
  @SuppressWarnings("ConstantConditions")
  public void updateProgressStatusOfMessage(boolean download, int position, RecyclerView rvMessages,
      int progress) {
    try {
      switch (messages.get(position).getCustomMessageType()) {

        case PhotoSent: {
          if (download) {
            ((PhotoMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessagePhotoBinding.pbDownload.setProgressCompat(progress, true);

            break;
          } else {
            ((PhotoMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessagePhotoBinding.pbUpload.setProgressCompat(progress, true);

            break;
          }
        }
        case PhotoReceived: {
          ((PhotoMessageReceivedViewHolder) rvMessages.findViewHolderForAdapterPosition(
              position)).ismReceivedMessagePhotoBinding.pbDownload.setProgressCompat(progress,
              true);

          break;
        }
        case VideoSent: {
          if (download) {
            ((VideoMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessageVideoBinding.pbDownload.setProgressCompat(progress, true);

            break;
          } else {
            ((VideoMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessageVideoBinding.pbUpload.setProgressCompat(progress, true);

            break;
          }
        }
        case VideoReceived: {
          ((VideoMessageReceivedViewHolder) rvMessages.findViewHolderForAdapterPosition(
              position)).ismReceivedMessageVideoBinding.pbDownload.setProgressCompat(progress,
              true);

          break;
        }
        case AudioSent: {
          if (download) {
            ((AudioMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessageAudioBinding.pbDownload.setProgressCompat(progress, true);

            break;
          } else {
            ((AudioMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessageAudioBinding.pbUpload.setProgressCompat(progress, true);

            break;
          }
        }
        case AudioReceived: {
          ((AudioMessageReceivedViewHolder) rvMessages.findViewHolderForAdapterPosition(
              position)).ismReceivedMessageAudioBinding.pbDownload.setProgressCompat(progress,
              true);

          break;
        }
        case FileSent: {
          if (download) {
            ((FileMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessageFileBinding.pbDownload.setProgressCompat(progress, true);

            break;
          } else {
            ((FileMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessageFileBinding.pbUpload.setProgressCompat(progress, true);

            break;
          }
        }
        case FileReceived: {
          ((FileMessageReceivedViewHolder) rvMessages.findViewHolderForAdapterPosition(
              position)).ismReceivedMessageFileBinding.pbDownload.setProgressCompat(progress, true);

          break;
        }
        case WhiteboardSent: {
          if (download) {
            ((WhiteboardMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessageWhiteboardBinding.pbDownload.setProgressCompat(progress,
                true);

            break;
          } else {
            ((WhiteboardMessageSentViewHolder) rvMessages.findViewHolderForAdapterPosition(
                position)).ismSentMessageWhiteboardBinding.pbUpload.setProgressCompat(progress,
                true);

            break;
          }
        }
        case WhiteboardReceived: {
          ((WhiteboardMessageReceivedViewHolder) rvMessages.findViewHolderForAdapterPosition(
              position)).ismReceivedMessageWhiteboardBinding.pbDownload.setProgressCompat(progress,
              true);

          break;
        }
      }
    } catch (Exception ignore) {
    }
  }

  /**
   * Sets multiple messages select mode on.
   *
   * @param multipleMessagesSelectModeOn the multiple messages select mode on
   */
  public void setMultipleMessagesSelectModeOn(boolean multipleMessagesSelectModeOn) {
    this.multipleMessagesSelectModeOn = multipleMessagesSelectModeOn;
  }
  //
  //public boolean isMultipleMessagesSelectModeOn() {
  //  return multipleMessagesSelectModeOn;
  //}

  /**
   * Is messaging disabled boolean.
   *
   * @return the boolean
   */
  public boolean isMessagingDisabled() {
    return messagingDisabled;
  }

  /**
   * Sets messaging disabled.
   *
   * @param messagingDisabled the messaging disabled
   */
  public void setMessagingDisabled(boolean messagingDisabled) {
    this.messagingDisabled = messagingDisabled;
  }
}
