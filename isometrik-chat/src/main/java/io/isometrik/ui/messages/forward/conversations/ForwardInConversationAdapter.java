package io.isometrik.ui.messages.forward.conversations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBroadcastForwardConversationUserItemBinding;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of conversations in which a message can be forwarded.
 */
public class ForwardInConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ForwardInConversationModel> conversationsModels;

  /**
   * Instantiates a new Forward in conversation adapter.
   *
   * @param mContext the m context
   * @param conversationsModels the conversations models
   */
  public ForwardInConversationAdapter(Context mContext,
      ArrayList<ForwardInConversationModel> conversationsModels) {
    this.mContext = mContext;
    this.conversationsModels = conversationsModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmBroadcastForwardConversationUserItemBinding ismForwardInConversationsItemBinding =
        IsmBroadcastForwardConversationUserItemBinding.inflate(
            LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
    return new ConversationViewHolder(ismForwardInConversationsItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    ConversationViewHolder holder = (ConversationViewHolder) viewHolder;

    try {
      ForwardInConversationModel conversationsModel = conversationsModels.get(position);

      holder.ismForwardInConversationsItemBinding.tvConversationOrUserName.setText(
          conversationsModel.getConversationTitle());
      holder.ismForwardInConversationsItemBinding.tvSelect.setText(
          conversationsModel.isSelected() ? mContext.getString(R.string.ism_remove)
              : mContext.getString(R.string.ism_add));
      if (PlaceholderUtils.isValidImageUrl(conversationsModel.getConversationImageUrl())) {

        try {
          Glide.with(mContext)
              .load(conversationsModel.getConversationImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismForwardInConversationsItemBinding.ivConversationOrUserImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, conversationsModel.getConversationTitle(),
            holder.ismForwardInConversationsItemBinding.ivConversationOrUserImage, position, 10);
      }
      if (conversationsModel.isPrivateOneToOneConversation()) {
        if (conversationsModel.isMessagingDisabled()) {
          holder.ismForwardInConversationsItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_ic_messaging_disabled));
        } else {
          if (conversationsModel.isOnline()) {
            holder.ismForwardInConversationsItemBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
          } else {
            holder.ismForwardInConversationsItemBinding.ivOnlineStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
          }
        }
        holder.ismForwardInConversationsItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
      } else {

        holder.ismForwardInConversationsItemBinding.ivOnlineStatus.setVisibility(View.GONE);
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

    private final IsmBroadcastForwardConversationUserItemBinding
        ismForwardInConversationsItemBinding;

    /**
     * Instantiates a new Conversation view holder.
     *
     * @param ismForwardInConversationsItemBinding the ism forward in conversations item binding
     */
    public ConversationViewHolder(
        final IsmBroadcastForwardConversationUserItemBinding ismForwardInConversationsItemBinding) {
      super(ismForwardInConversationsItemBinding.getRoot());
      this.ismForwardInConversationsItemBinding = ismForwardInConversationsItemBinding;
    }
  }
}
