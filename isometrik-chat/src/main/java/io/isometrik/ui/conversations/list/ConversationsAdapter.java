package io.isometrik.ui.conversations.list;

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
import io.isometrik.chat.databinding.IsmConversationItemBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the public/open and all conversations.
 */
public class ConversationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ConversationsModel> conversationsModels;
  private final ConversationsListFragment conversationsFragment;

  /**
   * Instantiates a new Conversations adapter.
   *
   * @param mContext the m context
   * @param conversationsModels the conversations models
   * @param conversationsFragment the conversations fragment
   */
  public ConversationsAdapter(Context mContext, ArrayList<ConversationsModel> conversationsModels,
      ConversationsListFragment conversationsFragment) {
    this.mContext = mContext;
    this.conversationsModels = conversationsModels;
    this.conversationsFragment = conversationsFragment;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmConversationItemBinding ismConversationItemBinding =
        IsmConversationItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new ConversationViewHolder(ismConversationItemBinding);
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
        holder.ismConversationItemBinding.tvConversationTitle.setText(spannableString);
      } else {

        holder.ismConversationItemBinding.tvConversationTitle.setText(
            conversationsModel.getConversationTitle());
      }
      
      holder.ismConversationItemBinding.tvLastMessage.setText(
          conversationsModel.getLastMessageText());
      if (conversationsModel.isCanJoin()) {
        holder.ismConversationItemBinding.tvJoinConversation.setVisibility(View.VISIBLE);
        holder.ismConversationItemBinding.tvConversationType.setVisibility(View.GONE);
        holder.ismConversationItemBinding.tvJoinConversation.setOnClickListener(
            v -> conversationsFragment.joinConversation(conversationsModel));
      } else {

        holder.ismConversationItemBinding.tvJoinConversation.setVisibility(View.GONE);
        holder.ismConversationItemBinding.tvConversationType.setVisibility(View.VISIBLE);
        holder.ismConversationItemBinding.tvConversationType.setText(
            conversationsModel.getConversationTypeText());

      }

      holder.ismConversationItemBinding.tvLastMessageTime.setText(
          conversationsModel.getLastMessageTime());

      if (conversationsModel.getUnreadMessagesCount() > 0) {


        holder.ismConversationItemBinding.tvUnreadMessagesCount.setText(
            conversationsModel.getUnreadMessagesCount()>99? mContext.getString(R.string.ism_hundred_unread_count) :  String.valueOf(conversationsModel.getUnreadMessagesCount()));
        holder.ismConversationItemBinding.tvUnreadMessagesCount.setVisibility(View.VISIBLE);
      } else {
        holder.ismConversationItemBinding.tvUnreadMessagesCount.setVisibility(View.GONE);
      }
      if (PlaceholderUtils.isValidImageUrl(conversationsModel.getConversationImageUrl())) {

        try {
          GlideApp.with(mContext)
              .load(conversationsModel.getConversationImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismConversationItemBinding.ivConversationImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, conversationsModel.getConversationTitle(),
            holder.ismConversationItemBinding.ivConversationImage, position, 16);
      }
      if (conversationsModel.isPrivateOneToOneConversation()) {

        if (conversationsModel.isMessagingDisabled()) {
          holder.ismConversationItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_ic_messaging_disabled));
        } else {
          if (conversationsModel.isOnline()) {
            holder.ismConversationItemBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            holder.ismConversationItemBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
        }
        holder.ismConversationItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
      } else {

        if (conversationsModel.getLastMessageSendersProfileImageUrl() != null) {
          if (PlaceholderUtils.isValidImageUrl(
              conversationsModel.getLastMessageSendersProfileImageUrl())) {

            try {
              GlideApp.with(mContext)
                  .load(conversationsModel.getLastMessageSendersProfileImageUrl())
                  .placeholder(R.drawable.ism_ic_profile)
                  .transform(new CircleCrop())
                  .into(holder.ismConversationItemBinding.ivOnlineStatus);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
          } else {

            PlaceholderUtils.setTextRoundDrawable(mContext,
                conversationsModel.getLastMessageSenderName(),
                holder.ismConversationItemBinding.ivOnlineStatus, position + 1, 5);
          }
          holder.ismConversationItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
        } else {
          holder.ismConversationItemBinding.ivOnlineStatus.setVisibility(View.GONE);
        }
      }

      if (conversationsModel.getLastMessagePlaceHolderImage() != null) {
        try {
          GlideApp.with(mContext)
              .load(conversationsModel.getLastMessagePlaceHolderImage())
              .diskCacheStrategy(DiskCacheStrategy.NONE)
              .into(holder.ismConversationItemBinding.ivLastMessageType);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        if (conversationsModel.isLastMessageWasReactionMessage()) {
          holder.ismConversationItemBinding.ivLastMessageType.clearColorFilter();
        } else {

          holder.ismConversationItemBinding.ivLastMessageType.setColorFilter(
              ContextCompat.getColor(mContext, R.color.ism_last_message_grey),
              PorterDuff.Mode.SRC_IN);
        }

        holder.ismConversationItemBinding.ivLastMessageType.setVisibility(View.VISIBLE);
      } else {
        holder.ismConversationItemBinding.ivLastMessageType.setVisibility(View.GONE);
      }

      if (conversationsModel.isRemoteUserTyping()) {
        holder.ismConversationItemBinding.tvLastMessage.setVisibility(View.GONE);
        holder.ismConversationItemBinding.ivLastMessageType.setVisibility(View.GONE);
        holder.ismConversationItemBinding.tvTypingMessage.setText(
            conversationsModel.getRemoteUserTypingMessage());
        holder.ismConversationItemBinding.tvTypingMessage.setVisibility(View.VISIBLE);
      } else {
        holder.ismConversationItemBinding.tvTypingMessage.setVisibility(View.GONE);
        holder.ismConversationItemBinding.tvLastMessage.setVisibility(View.VISIBLE);
        if (conversationsModel.getLastMessagePlaceHolderImage() != null) {
          holder.ismConversationItemBinding.ivLastMessageType.setVisibility(View.VISIBLE);
        }
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

    private final IsmConversationItemBinding ismConversationItemBinding;

    /**
     * Instantiates a new Conversation view holder.
     *
     * @param ismConversationItemBinding the ism conversation item binding
     */
    public ConversationViewHolder(final IsmConversationItemBinding ismConversationItemBinding) {
      super(ismConversationItemBinding.getRoot());
      this.ismConversationItemBinding = ismConversationItemBinding;
    }
  }
}
