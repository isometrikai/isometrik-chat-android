package io.isometrik.ui.messages.mentioned;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmMentionedMessageItemBinding;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of messages in which logged in user has been mentioned.
 */
public class MentionedMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<MentionedMessagesModel> mentionedMessagesModels;

  /**
   * Instantiates a new Mentioned messages adapter.
   *
   * @param mContext the m context
   * @param mentionedMessagesModels the mentioned messages models
   */
  public MentionedMessagesAdapter(Context mContext,
      ArrayList<MentionedMessagesModel> mentionedMessagesModels) {
    this.mContext = mContext;
    this.mentionedMessagesModels = mentionedMessagesModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmMentionedMessageItemBinding ismMentionedMessageItemBinding =
        IsmMentionedMessageItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new MentionedMessagesViewHolder(ismMentionedMessageItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    MentionedMessagesViewHolder holder = (MentionedMessagesViewHolder) viewHolder;

    try {
      MentionedMessagesModel mentionedMessagesModel = mentionedMessagesModels.get(position);

      holder.ismMentionedMessageItemBinding.tvMentionedBy.setText(
          mentionedMessagesModel.getMentionedBy());
      holder.ismMentionedMessageItemBinding.tvMentionedMessage.setText(
          mentionedMessagesModel.getMentionedText());
      holder.ismMentionedMessageItemBinding.tvConversationTitle.setText(
          mentionedMessagesModel.getConversationTitle());

      holder.ismMentionedMessageItemBinding.tvMentionedMessageTime.setText(
          mentionedMessagesModel.getMentionedMessageTime());
      if (PlaceholderUtils.isValidImageUrl(mentionedMessagesModel.getConversationImageUrl())) {

        try {
          Glide.with(mContext)
              .load(mentionedMessagesModel.getConversationImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismMentionedMessageItemBinding.ivConversationImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext,
            mentionedMessagesModel.getConversationTitle(),
            holder.ismMentionedMessageItemBinding.ivConversationImage, position, 21);
      }

      if (mentionedMessagesModel.isPrivateOneToOneConversation()) {
        if (mentionedMessagesModel.isOnline()) {
          holder.ismMentionedMessageItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
        } else {
          holder.ismMentionedMessageItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
        }
        holder.ismMentionedMessageItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
      } else {

        if (mentionedMessagesModel.getMentionedMessageSendersProfileImageUrl() != null) {
          if (PlaceholderUtils.isValidImageUrl(
              mentionedMessagesModel.getMentionedMessageSendersProfileImageUrl())) {

            try {
              Glide.with(mContext)
                  .load(mentionedMessagesModel.getMentionedMessageSendersProfileImageUrl())
                  .placeholder(R.drawable.ism_ic_profile)
                  .transform(new CircleCrop())
                  .into(holder.ismMentionedMessageItemBinding.ivOnlineStatus);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
          } else {
            PlaceholderUtils.setTextRoundDrawable(mContext,
                mentionedMessagesModel.getMentionedMessageSenderName(),
                holder.ismMentionedMessageItemBinding.ivOnlineStatus, position + 1, 5);
          }
          holder.ismMentionedMessageItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
        } else {
          holder.ismMentionedMessageItemBinding.ivOnlineStatus.setVisibility(View.GONE);
        }
      }

      if (mentionedMessagesModel.getLastMessagePlaceHolderImage() != null) {
        try {
          Glide.with(mContext)
              .load(mentionedMessagesModel.getLastMessagePlaceHolderImage())
              .diskCacheStrategy(DiskCacheStrategy.NONE)
              .into(holder.ismMentionedMessageItemBinding.ivMentionedMessageType);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        holder.ismMentionedMessageItemBinding.ivMentionedMessageType.setVisibility(View.VISIBLE);
      } else {
        holder.ismMentionedMessageItemBinding.ivMentionedMessageType.setVisibility(View.GONE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return mentionedMessagesModels.size();
  }

  /**
   * The type Mentioned messages view holder.
   */
  static class MentionedMessagesViewHolder extends RecyclerView.ViewHolder {

    private final IsmMentionedMessageItemBinding ismMentionedMessageItemBinding;

    /**
     * Instantiates a new Mentioned messages view holder.
     *
     * @param ismMentionedMessageItemBinding the ism mentioned message item binding
     */
    public MentionedMessagesViewHolder(
        final IsmMentionedMessageItemBinding ismMentionedMessageItemBinding) {
      super(ismMentionedMessageItemBinding.getRoot());
      this.ismMentionedMessageItemBinding = ismMentionedMessageItemBinding;
    }
  }
}