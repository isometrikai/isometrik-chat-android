package io.isometrik.ui.search.messages;

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
import io.isometrik.ui.messages.chat.MessagesModel;
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
import io.isometrik.chat.utils.Constants;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the search messages results.
 */
public class SearchMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

  private final int cornerRadius;
  //private final float thumbnailSizeMultiplier = Constants.THUMBNAIL_SIZE_MULTIPLIER;

  /**
   * Instantiates a new Search messages adapter.
   *
   * @param mContext the m context
   * @param messages the messages
   */
  public SearchMessagesAdapter(Context mContext, ArrayList<MessagesModel> messages) {
    this.mContext = mContext;
    this.messages = messages;
    cornerRadius = (int) (13 * mContext.getResources().getDisplayMetrics().density);
  }

  @Override
  public int getItemViewType(int position) {

    if (messages.get(position).isSentMessage()) {
      switch (messages.get(position).getCustomMessageType()) {
        case TEXT_MESSAGE_SENT:
          return TEXT_MESSAGE_SENT;
        case PHOTO_MESSAGE_SENT:
          return PHOTO_MESSAGE_SENT;
        case VIDEO_MESSAGE_SENT:
          return VIDEO_MESSAGE_SENT;
        case AUDIO_MESSAGE_SENT:
          return AUDIO_MESSAGE_SENT;
        case FILE_MESSAGE_SENT:
          return FILE_MESSAGE_SENT;
        case STICKER_MESSAGE_SENT:
          return STICKER_MESSAGE_SENT;
        case GIF_MESSAGE_SENT:
          return GIF_MESSAGE_SENT;
        case WHITEBOARD_MESSAGE_SENT:
          return WHITEBOARD_MESSAGE_SENT;
        case LOCATION_MESSAGE_SENT:
          return LOCATION_MESSAGE_SENT;
        case CONTACT_MESSAGE_SENT:
          return CONTACT_MESSAGE_SENT;
        default:
          return -1;
      }
    } else {
      switch (messages.get(position).getCustomMessageType()) {
        case TEXT_MESSAGE_RECEIVED:
          return TEXT_MESSAGE_RECEIVED;
        case PHOTO_MESSAGE_RECEIVED:
          return PHOTO_MESSAGE_RECEIVED;
        case VIDEO_MESSAGE_RECEIVED:
          return VIDEO_MESSAGE_RECEIVED;
        case AUDIO_MESSAGE_RECEIVED:
          return AUDIO_MESSAGE_RECEIVED;
        case FILE_MESSAGE_RECEIVED:
          return FILE_MESSAGE_RECEIVED;
        case STICKER_MESSAGE_RECEIVED:
          return STICKER_MESSAGE_RECEIVED;
        case GIF_MESSAGE_RECEIVED:
          return GIF_MESSAGE_RECEIVED;
        case WHITEBOARD_MESSAGE_RECEIVED:
          return WHITEBOARD_MESSAGE_RECEIVED;
        case LOCATION_MESSAGE_RECEIVED:
          return LOCATION_MESSAGE_RECEIVED;
        case CONTACT_MESSAGE_RECEIVED:
          return CONTACT_MESSAGE_RECEIVED;
        case CONVERSATION_ACTION_MESSAGE:
          return CONVERSATION_ACTION_MESSAGE;
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
        textMessageSentViewHolder.ismSentMessageTextBinding.tvSendingMessage.setVisibility(
            View.GONE);

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
            textMessageSentViewHolder.ismSentMessageTextBinding.vForwardedMessageNotes.tvMessage.setText(
                message.getForwardedMessageNotes());

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
        textMessageSentViewHolder.ismSentMessageTextBinding.ivForward.setVisibility(View.GONE);

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

          textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
        } else {
          textMessageSentViewHolder.ismSentMessageTextBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        textMessageSentViewHolder.ismSentMessageTextBinding.tvTextMessage.setText(
            message.getTextMessage());
        textMessageSentViewHolder.ismSentMessageTextBinding.tvTextMessage.
            setMovementMethod(LinkMovementMethod.getInstance());
        textMessageSentViewHolder.ismSentMessageTextBinding.tvMessageTime.setText(
            message.getMessageTime());

        textMessageSentViewHolder.ismSentMessageTextBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          textMessageSentViewHolder.ismSentMessageTextBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          textMessageSentViewHolder.ismSentMessageTextBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(textMessageSentViewHolder.ismSentMessageTextBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              textMessageSentViewHolder.ismSentMessageTextBinding.ivConversationImage, position,
              10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            textMessageSentViewHolder.ismSentMessageTextBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            textMessageSentViewHolder.ismSentMessageTextBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          textMessageSentViewHolder.ismSentMessageTextBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          textMessageSentViewHolder.ismSentMessageTextBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvSendingMessage.setVisibility(
            View.GONE);

        if (message.isForwardedMessage()) {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessageNotes.tvMessage.setText(
                message.getForwardedMessageNotes());

            photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);
        }

        photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivForward.setVisibility(View.GONE);

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

          photoMessageSentViewHolder.ismSentMessagePhotoBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
        } else {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvMessageTime.setText(
            message.getMessageTime());
        photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlUpload.setVisibility(View.GONE);
        photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlDownload.setVisibility(View.GONE);
        try {
          Glide.with(mContext)
              .load(message.getPhotoMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getPhotoThumbnailUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivPhoto);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvPhotoSize.setText(
            message.getMediaSizeInMB());

        photoMessageSentViewHolder.ismSentMessagePhotoBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivConversationImage, position,
              10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          photoMessageSentViewHolder.ismSentMessagePhotoBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        videoMessageSentViewHolder.ismSentMessageVideoBinding.tvSendingMessage.setVisibility(
            View.GONE);

        if (message.isForwardedMessage()) {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessageNotes.tvMessage.setText(
                message.getForwardedMessageNotes());

            videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          videoMessageSentViewHolder.ismSentMessageVideoBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);
        }
        videoMessageSentViewHolder.ismSentMessageVideoBinding.ivForward.setVisibility(View.GONE);

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
          videoMessageSentViewHolder.ismSentMessageVideoBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          videoMessageSentViewHolder.ismSentMessageVideoBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          videoMessageSentViewHolder.ismSentMessageVideoBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
        } else {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        videoMessageSentViewHolder.ismSentMessageVideoBinding.tvMessageTime.setText(
            message.getMessageTime());
        videoMessageSentViewHolder.ismSentMessageVideoBinding.rlUpload.setVisibility(View.GONE);
        videoMessageSentViewHolder.ismSentMessageVideoBinding.rlDownload.setVisibility(View.GONE);
        try {
          Glide.with(mContext)
              .load(message.getVideoMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getVideoThumbnailUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(videoMessageSentViewHolder.ismSentMessageVideoBinding.ivVideoThumbnail);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        videoMessageSentViewHolder.ismSentMessageVideoBinding.tvVideoSize.setText(
            message.getMediaSizeInMB());

        videoMessageSentViewHolder.ismSentMessageVideoBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          videoMessageSentViewHolder.ismSentMessageVideoBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          videoMessageSentViewHolder.ismSentMessageVideoBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(videoMessageSentViewHolder.ismSentMessageVideoBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              videoMessageSentViewHolder.ismSentMessageVideoBinding.ivConversationImage, position,
              10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            videoMessageSentViewHolder.ismSentMessageVideoBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          videoMessageSentViewHolder.ismSentMessageVideoBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        audioMessageSentViewHolder.ismSentMessageAudioBinding.tvSendingMessage.setVisibility(
            View.GONE);

        if (message.isForwardedMessage()) {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessageNotes.tvMessage.setText(
                message.getForwardedMessageNotes());

            audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          audioMessageSentViewHolder.ismSentMessageAudioBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);
        }
        audioMessageSentViewHolder.ismSentMessageAudioBinding.ivForward.setVisibility(View.GONE);

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
          audioMessageSentViewHolder.ismSentMessageAudioBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          audioMessageSentViewHolder.ismSentMessageAudioBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          audioMessageSentViewHolder.ismSentMessageAudioBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
        } else {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        audioMessageSentViewHolder.ismSentMessageAudioBinding.tvMessageTime.setText(
            message.getMessageTime());
        audioMessageSentViewHolder.ismSentMessageAudioBinding.rlUpload.setVisibility(View.GONE);
        audioMessageSentViewHolder.ismSentMessageAudioBinding.rlDownload.setVisibility(View.GONE);

        audioMessageSentViewHolder.ismSentMessageAudioBinding.tvAudioName.setText(
            message.getAudioName());
        audioMessageSentViewHolder.ismSentMessageAudioBinding.tvAudioSize.setText(
            message.getMediaSizeInMB());

        audioMessageSentViewHolder.ismSentMessageAudioBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          audioMessageSentViewHolder.ismSentMessageAudioBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          audioMessageSentViewHolder.ismSentMessageAudioBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(audioMessageSentViewHolder.ismSentMessageAudioBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              audioMessageSentViewHolder.ismSentMessageAudioBinding.ivConversationImage, position,
              10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            audioMessageSentViewHolder.ismSentMessageAudioBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          audioMessageSentViewHolder.ismSentMessageAudioBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        fileMessageSentViewHolder.ismSentMessageFileBinding.tvSendingMessage.setVisibility(
            View.GONE);

        if (message.isForwardedMessage()) {
          fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessageNotes.tvMessage.setText(
                message.getForwardedMessageNotes());

            fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          fileMessageSentViewHolder.ismSentMessageFileBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);
        }
        fileMessageSentViewHolder.ismSentMessageFileBinding.ivForward.setVisibility(View.GONE);

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
          fileMessageSentViewHolder.ismSentMessageFileBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          fileMessageSentViewHolder.ismSentMessageFileBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          fileMessageSentViewHolder.ismSentMessageFileBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
        } else {
          fileMessageSentViewHolder.ismSentMessageFileBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }

        fileMessageSentViewHolder.ismSentMessageFileBinding.tvMessageTime.setText(
            message.getMessageTime());
        fileMessageSentViewHolder.ismSentMessageFileBinding.rlUpload.setVisibility(View.GONE);
        fileMessageSentViewHolder.ismSentMessageFileBinding.rlDownload.setVisibility(View.GONE);

        fileMessageSentViewHolder.ismSentMessageFileBinding.tvFileName.setText(
            message.getFileName());
        fileMessageSentViewHolder.ismSentMessageFileBinding.tvFileSize.setText(
            message.getMediaSizeInMB());

        fileMessageSentViewHolder.ismSentMessageFileBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          fileMessageSentViewHolder.ismSentMessageFileBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          fileMessageSentViewHolder.ismSentMessageFileBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(fileMessageSentViewHolder.ismSentMessageFileBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              fileMessageSentViewHolder.ismSentMessageFileBinding.ivConversationImage, position,
              10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            fileMessageSentViewHolder.ismSentMessageFileBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            fileMessageSentViewHolder.ismSentMessageFileBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          fileMessageSentViewHolder.ismSentMessageFileBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          fileMessageSentViewHolder.ismSentMessageFileBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvSendingMessage.setVisibility(
            View.GONE);

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
        stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivForward.setVisibility(
            View.GONE);
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

          stickerMessageSentViewHolder.ismSentMessageStickerBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          stickerMessageSentViewHolder.ismSentMessageStickerBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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

        stickerMessageSentViewHolder.ismSentMessageStickerBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          stickerMessageSentViewHolder.ismSentMessageStickerBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        gifMessageSentViewHolder.ismSentMessageGifBinding.tvSendingMessage.setVisibility(View.GONE);

        if (message.isForwardedMessage()) {
          gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessage.getRoot()
              .setVisibility(View.VISIBLE);

          if (message.getForwardedMessageNotes() == null) {

            gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.GONE);
          } else {
            gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessageNotes.tvMessage.setText(
                message.getForwardedMessageNotes());

            gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessageNotes.getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          gifMessageSentViewHolder.ismSentMessageGifBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);
        }
        gifMessageSentViewHolder.ismSentMessageGifBinding.ivForward.setVisibility(View.GONE);

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
          gifMessageSentViewHolder.ismSentMessageGifBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          gifMessageSentViewHolder.ismSentMessageGifBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          gifMessageSentViewHolder.ismSentMessageGifBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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

        gifMessageSentViewHolder.ismSentMessageGifBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          gifMessageSentViewHolder.ismSentMessageGifBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          gifMessageSentViewHolder.ismSentMessageGifBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(gifMessageSentViewHolder.ismSentMessageGifBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              gifMessageSentViewHolder.ismSentMessageGifBinding.ivConversationImage, position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            gifMessageSentViewHolder.ismSentMessageGifBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            gifMessageSentViewHolder.ismSentMessageGifBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          gifMessageSentViewHolder.ismSentMessageGifBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          gifMessageSentViewHolder.ismSentMessageGifBinding.ivOnlineStatus.setVisibility(View.GONE);
        }
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
        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvSendingMessage.setVisibility(
            View.GONE);

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
        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivForward.setVisibility(
            View.GONE);

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

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
        } else {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rvMessageReactions.setVisibility(
              View.GONE);
        }
        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlUpload.setVisibility(
            View.GONE);
        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlDownload.setVisibility(
            View.GONE);
        try {
          Glide.with(mContext)
              .load(message.getWhiteboardMainUrl())
              .thumbnail(Glide.with(mContext).load(message.getWhiteboardThumbnailUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivWhiteboard);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvWhiteboardSize.setText(
            message.getMediaSizeInMB());

        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvMessageTime.setText(
            message.getMessageTime());

        whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          whiteboardMessageSentViewHolder.ismSentMessageWhiteboardBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        locationMessageSentViewHolder.ismSentMessageLocationBinding.tvSendingMessage.setVisibility(
            View.GONE);

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
        locationMessageSentViewHolder.ismSentMessageLocationBinding.ivForward.setVisibility(
            View.GONE);
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

          locationMessageSentViewHolder.ismSentMessageLocationBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          locationMessageSentViewHolder.ismSentMessageLocationBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          locationMessageSentViewHolder.ismSentMessageLocationBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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

        locationMessageSentViewHolder.ismSentMessageLocationBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          locationMessageSentViewHolder.ismSentMessageLocationBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          locationMessageSentViewHolder.ismSentMessageLocationBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    locationMessageSentViewHolder.ismSentMessageLocationBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              locationMessageSentViewHolder.ismSentMessageLocationBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            locationMessageSentViewHolder.ismSentMessageLocationBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          locationMessageSentViewHolder.ismSentMessageLocationBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        contactMessageSentViewHolder.ismSentMessageContactBinding.tvSendingMessage.setVisibility(
            View.GONE);

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
        contactMessageSentViewHolder.ismSentMessageContactBinding.ivForward.setVisibility(
            View.GONE);

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

          contactMessageSentViewHolder.ismSentMessageContactBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          contactMessageSentViewHolder.ismSentMessageContactBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          contactMessageSentViewHolder.ismSentMessageContactBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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
        contactMessageSentViewHolder.ismSentMessageContactBinding.tvContactName.setText(
            message.getContactName());
        contactMessageSentViewHolder.ismSentMessageContactBinding.tvContactIdentifier.setText(
            message.getContactIdentifier());
        contactMessageSentViewHolder.ismSentMessageContactBinding.tvMessageTime.setText(
            message.getMessageTime());

        contactMessageSentViewHolder.ismSentMessageContactBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          contactMessageSentViewHolder.ismSentMessageContactBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          contactMessageSentViewHolder.ismSentMessageContactBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    contactMessageSentViewHolder.ismSentMessageContactBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              contactMessageSentViewHolder.ismSentMessageContactBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            contactMessageSentViewHolder.ismSentMessageContactBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            contactMessageSentViewHolder.ismSentMessageContactBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          contactMessageSentViewHolder.ismSentMessageContactBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          contactMessageSentViewHolder.ismSentMessageContactBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivReaction.setVisibility(
            View.GONE);

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

          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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
        textMessageReceivedViewHolder.ismReceivedMessageTextBinding.tvTextMessage.
            setMovementMethod(LinkMovementMethod.getInstance());

        textMessageReceivedViewHolder.ismReceivedMessageTextBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          textMessageReceivedViewHolder.ismReceivedMessageTextBinding.ivOnlineStatus.setVisibility(
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
        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivReaction.setVisibility(
            View.GONE);
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

          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivPhoto);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rlDownload.setVisibility(
            View.GONE);

        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvPhotoSize.setText(
            message.getMediaSizeInMB());

        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvMessageTime.setText(
            message.getMessageTime());

        photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          photoMessageReceivedViewHolder.ismReceivedMessagePhotoBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivReaction.setVisibility(
            View.GONE);

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

          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivVideoThumbnail);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rlDownload.setVisibility(
            View.GONE);

        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvVideoSize.setText(
            message.getMediaSizeInMB());

        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvMessageTime.setText(
            message.getMessageTime());

        videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          videoMessageReceivedViewHolder.ismReceivedMessageVideoBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivReaction.setVisibility(
            View.GONE);

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

          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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
                .transform(new CenterCrop(),
                    new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
                .into(audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivSenderImage, position,
              12);
        }
        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rlDownload.setVisibility(
            View.GONE);

        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvAudioName.setText(
            message.getAudioName());
        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvAudioSize.setText(
            message.getMediaSizeInMB());

        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvMessageTime.setText(
            message.getMessageTime());

        audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          audioMessageReceivedViewHolder.ismReceivedMessageAudioBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivReaction.setVisibility(
            View.GONE);

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

          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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
                .transform(new CenterCrop(),
                    new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
                .into(fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivSenderImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getSenderName(),
              fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivSenderImage, position,
              12);
        }
        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rlDownload.setVisibility(
            View.GONE);

        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvFileName.setText(
            message.getFileName());
        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvFileSize.setText(
            message.getMediaSizeInMB());

        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvMessageTime.setText(
            message.getMessageTime());

        fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          fileMessageReceivedViewHolder.ismReceivedMessageFileBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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

            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vForwardedMessageNotes
                .getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);
        }
        stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivReaction.setVisibility(
            View.GONE);

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

          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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
              stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivSenderImage,
              position, 12);
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

        stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          stickerMessageReceivedViewHolder.ismReceivedMessageStickerBinding.ivOnlineStatus.setVisibility(
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
        gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivReaction.setVisibility(
            View.GONE);

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

          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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

        gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          gifMessageReceivedViewHolder.ismReceivedMessageGifBinding.ivOnlineStatus.setVisibility(
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

            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes
                .getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes
              .getRoot()
              .setVisibility(View.GONE);
        }
        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivReaction.setVisibility(
            View.GONE);
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

          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rvMessageReactions
              .setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rvMessageReactions
              .setAdapter(new MessageReactionsAdapter(mContext, message.getReactions(),
                  message.getMessageId(), null));

          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rvMessageReactions
              .setVisibility(View.VISIBLE);
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
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
              .into(
                  whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivWhiteboard);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rlDownload.setVisibility(
            View.GONE);

        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvWhiteboardSize.setText(
            message.getMediaSizeInMB());

        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvMessageTime.setText(
            message.getMessageTime());

        whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.rlConversationDetails
            .setVisibility(View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvConversationTitle
              .setText(spannableString);
        } else {
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.tvConversationTitle
              .setText(message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          whiteboardMessageReceivedViewHolder.ismReceivedMessageWhiteboardBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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

            locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vForwardedMessageNotes
                .getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.vForwardedMessageNotes
              .getRoot()
              .setVisibility(View.GONE);
        }
        locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivReaction.setVisibility(
            View.GONE);

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
                  null));

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

        locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          locationMessageReceivedViewHolder.ismReceivedMessageLocationBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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

            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vForwardedMessageNotes
                .getRoot()
                .setVisibility(View.VISIBLE);
          }
        } else {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vForwardedMessage.getRoot()
              .setVisibility(View.GONE);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.vForwardedMessageNotes.getRoot()
              .setVisibility(View.GONE);
        }
        contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivReaction.setVisibility(
            View.GONE);

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

          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rvMessageReactions.setLayoutManager(
              new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rvMessageReactions.setAdapter(
              new MessageReactionsAdapter(mContext, message.getReactions(), message.getMessageId(),
                  null));

          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rvMessageReactions.setVisibility(
              View.VISIBLE);
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
              contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivSenderImage,
              position, 12);
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
        contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvContactName.setText(
            message.getContactName());
        contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvContactIdentifier.setText(
            message.getContactIdentifier());

        contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvMessageTime.setText(
            message.getMessageTime());

        contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.rlConversationDetails.setVisibility(
            View.VISIBLE);
        if (message.isMessagingDisabled()) {
          SpannableString spannableString = new SpannableString(message.getConversationTitle());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvConversationTitle.setText(
              spannableString);
        } else {
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.tvConversationTitle.setText(
              message.getConversationTitle());
        }
        if (PlaceholderUtils.isValidImageUrl(message.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(message.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(
                    contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivConversationImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, message.getConversationTitle(),
              contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivConversationImage,
              position, 10);
        }
        if (message.isPrivateOneToOne()) {
          if (message.isOnline()) {
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivOnlineStatus.setVisibility(
              View.VISIBLE);
        } else {

          contactMessageReceivedViewHolder.ismReceivedMessageContactBinding.ivOnlineStatus.setVisibility(
              View.GONE);
        }
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
}