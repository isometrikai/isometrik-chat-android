package io.isometrik.ui.messages.broadcast;

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
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of users to which a message can be broadcasted.
 */
public class BroadcastToPeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<BroadcastToPeopleModel> peopleModels;

  /**
   * Instantiates a new Broadcast to people adapter.
   *
   * @param mContext the m context
   * @param peopleModels the people models
   */
  public BroadcastToPeopleAdapter(Context mContext,
      ArrayList<BroadcastToPeopleModel> peopleModels) {
    this.mContext = mContext;
    this.peopleModels = peopleModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmBroadcastForwardConversationUserItemBinding ismBroadcastToUsersItemBinding =
        IsmBroadcastForwardConversationUserItemBinding.inflate(
            LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
    return new BroadcastToPeopleViewHolder(ismBroadcastToUsersItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    BroadcastToPeopleViewHolder holder = (BroadcastToPeopleViewHolder) viewHolder;

    try {
      BroadcastToPeopleModel broadcastToPeopleModel = peopleModels.get(position);

      holder.ismBroadcastToUsersItemBinding.tvConversationOrUserName.setText(
          broadcastToPeopleModel.getUserName());

      holder.ismBroadcastToUsersItemBinding.tvSelect.setText(
          broadcastToPeopleModel.isSelected() ? mContext.getString(R.string.ism_remove)
              : mContext.getString(R.string.ism_add));
      if (PlaceholderUtils.isValidImageUrl(broadcastToPeopleModel.getUserProfileImageUrl())) {
        try {
          GlideApp.with(mContext)
              .load(broadcastToPeopleModel.getUserProfileImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismBroadcastToUsersItemBinding.ivConversationOrUserImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, broadcastToPeopleModel.getUserName(),
            holder.ismBroadcastToUsersItemBinding.ivConversationOrUserImage, position, 10);
      }
      if (broadcastToPeopleModel.isOnline()) {
        holder.ismBroadcastToUsersItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
      } else {
        holder.ismBroadcastToUsersItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
      }
      holder.ismBroadcastToUsersItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return peopleModels.size();
  }

  /**
   * The type Broadcast to people view holder.
   */
  static class BroadcastToPeopleViewHolder extends RecyclerView.ViewHolder {

    private final IsmBroadcastForwardConversationUserItemBinding ismBroadcastToUsersItemBinding;

    /**
     * Instantiates a new Broadcast to people view holder.
     *
     * @param ismBroadcastToUsersItemBinding the ism broadcast to users item binding
     */
    public BroadcastToPeopleViewHolder(
        final IsmBroadcastForwardConversationUserItemBinding ismBroadcastToUsersItemBinding) {
      super(ismBroadcastToUsersItemBinding.getRoot());
      this.ismBroadcastToUsersItemBinding = ismBroadcastToUsersItemBinding;
    }
  }
}
