package io.isometrik.ui.search.conversations;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.ui.conversations.list.ConversationsModel;
import io.isometrik.chat.databinding.IsmSearchConversationsItemBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the search conversations results.
 */
public class SearchConversationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ConversationsModel> conversationsModels;

  /**
   * Instantiates a new Search conversations adapter.
   *
   * @param mContext the m context
   * @param conversationsModels the conversations models
   */
  public SearchConversationsAdapter(Context mContext,
      ArrayList<ConversationsModel> conversationsModels) {
    this.mContext = mContext;
    this.conversationsModels = conversationsModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmSearchConversationsItemBinding ismSearchConversationsItemBinding =
        IsmSearchConversationsItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new ConversationViewHolder(ismSearchConversationsItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    ConversationViewHolder holder = (ConversationViewHolder) viewHolder;

    try {
      ConversationsModel conversationsModel = conversationsModels.get(position);
      if (conversationsModel.isMessagingDisabled()) {
        SpannableString spannableString =
            new SpannableString(conversationsModel.getConversationTitle());
        spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
        holder.ismSearchConversationsItemBinding.tvConversationTitle.setText(spannableString);
      } else {
        holder.ismSearchConversationsItemBinding.tvConversationTitle.setText(
            conversationsModel.getConversationTitle());
      }
      holder.ismSearchConversationsItemBinding.tvLastMessage.setText(
          conversationsModel.getLastMessageText());

      holder.ismSearchConversationsItemBinding.tvConversationType.setText(
          conversationsModel.getConversationTypeText());

      holder.ismSearchConversationsItemBinding.tvLastMessageTime.setText(
          conversationsModel.getLastMessageTime());
      holder.ismSearchConversationsItemBinding.tvMembersCount.setText(
          conversationsModel.getMembersCountText());
      if (conversationsModel.getUnreadMessagesCount() > 0) {

        holder.ismSearchConversationsItemBinding.tvUnreadMessagesCount.setText(
            conversationsModel.getUnreadMessagesCount() > 99 ? mContext.getString(
                R.string.ism_hundred_unread_count)
                : String.valueOf(conversationsModel.getUnreadMessagesCount()));
        holder.ismSearchConversationsItemBinding.tvUnreadMessagesCount.setVisibility(View.VISIBLE);
      } else {
        holder.ismSearchConversationsItemBinding.tvUnreadMessagesCount.setVisibility(View.GONE);
      }
      if (PlaceholderUtils.isValidImageUrl(conversationsModel.getConversationImageUrl())) {

        try {
          GlideApp.with(mContext)
              .load(conversationsModel.getConversationImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismSearchConversationsItemBinding.ivConversationImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, conversationsModel.getConversationTitle(),
            holder.ismSearchConversationsItemBinding.ivConversationImage, position, 21);
      }
      if (conversationsModel.isPrivateOneToOneConversation()) {
        if (conversationsModel.isMessagingDisabled()) {
          holder.ismSearchConversationsItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_ic_messaging_disabled));
        } else {
          if (conversationsModel.isOnline()) {
            holder.ismSearchConversationsItemBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            holder.ismSearchConversationsItemBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
        }
        holder.ismSearchConversationsItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
      } else {

        if (conversationsModel.getLastMessageSendersProfileImageUrl() != null) {
          if (PlaceholderUtils.isValidImageUrl(
              conversationsModel.getLastMessageSendersProfileImageUrl())) {

            try {
              GlideApp.with(mContext)
                  .load(conversationsModel.getLastMessageSendersProfileImageUrl())
                  .placeholder(R.drawable.ism_ic_profile)
                  .transform(new CircleCrop())
                  .into(holder.ismSearchConversationsItemBinding.ivOnlineStatus);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
          } else {
            PlaceholderUtils.setTextRoundDrawable(mContext,
                conversationsModel.getLastMessageSenderName(),
                holder.ismSearchConversationsItemBinding.ivOnlineStatus, position+1, 5);
          }
          holder.ismSearchConversationsItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
        } else {
          holder.ismSearchConversationsItemBinding.ivOnlineStatus.setVisibility(View.GONE);
        }
      }

      if (conversationsModel.getLastMessagePlaceHolderImage() != null) {
        try {
          GlideApp.with(mContext)
              .load(conversationsModel.getLastMessagePlaceHolderImage())
              .diskCacheStrategy(DiskCacheStrategy.NONE)
              .into(holder.ismSearchConversationsItemBinding.ivLastMessageType);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        if (conversationsModel.isLastMessageWasReactionMessage()) {
          holder.ismSearchConversationsItemBinding.ivLastMessageType.clearColorFilter();
        } else {

          holder.ismSearchConversationsItemBinding.ivLastMessageType.setColorFilter(
              ContextCompat.getColor(mContext, R.color.ism_last_message_grey),
              PorterDuff.Mode.SRC_IN);
        }

        holder.ismSearchConversationsItemBinding.ivLastMessageType.setVisibility(View.VISIBLE);
      } else {
        holder.ismSearchConversationsItemBinding.ivLastMessageType.setVisibility(View.GONE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return conversationsModels.size();
  }

  /**
   * The type Conversation view holder.
   */
  static class ConversationViewHolder extends RecyclerView.ViewHolder {

    private final IsmSearchConversationsItemBinding ismSearchConversationsItemBinding;

    /**
     * Instantiates a new Conversation view holder.
     *
     * @param ismSearchConversationsItemBinding the ism search conversations item binding
     */
    public ConversationViewHolder(
        final IsmSearchConversationsItemBinding ismSearchConversationsItemBinding) {
      super(ismSearchConversationsItemBinding.getRoot());
      this.ismSearchConversationsItemBinding = ismSearchConversationsItemBinding;
    }
  }
}