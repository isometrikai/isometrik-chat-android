package io.isometrik.ui.messages.deliverystatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmMessageDeliveryReadStatusItemBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of users to whom delivery/read is complete or
 * pending.
 */
public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<UsersModel> users;
  private final boolean pendingDelivery;

  /**
   * Instantiates a new Users adapter.
   *
   * @param mContext the m context
   * @param users the users
   * @param pendingDelivery the pending delivery
   */
  public UsersAdapter(Context mContext, ArrayList<UsersModel> users, boolean pendingDelivery) {
    this.mContext = mContext;
    this.users = users;
    this.pendingDelivery = pendingDelivery;
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmMessageDeliveryReadStatusItemBinding ismMessageDeliveryReadStatusItemBinding =
        IsmMessageDeliveryReadStatusItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new UsersViewHolder(ismMessageDeliveryReadStatusItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    UsersViewHolder holder = (UsersViewHolder) viewHolder;

    try {
      UsersModel user = users.get(position);
      if (user != null) {
        holder.ismMessageDeliveryReadStatusItemBinding.tvUserName.setText(user.getUserName());

        if (user.isOnline()) {
          holder.ismMessageDeliveryReadStatusItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
        } else {
          holder.ismMessageDeliveryReadStatusItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
        }
        if (pendingDelivery) {
          holder.ismMessageDeliveryReadStatusItemBinding.tvDeliveryReadTime.setVisibility(
              View.GONE);
        } else {
          holder.ismMessageDeliveryReadStatusItemBinding.tvDeliveryReadTime.setVisibility(
              View.VISIBLE);
          holder.ismMessageDeliveryReadStatusItemBinding.tvDeliveryReadTime.setText(
              user.getDeliveredOrReadAt());
        }
        if (PlaceholderUtils.isValidImageUrl(user.getUserProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(user.getUserProfilePic())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismMessageDeliveryReadStatusItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {

          PlaceholderUtils.setTextRoundDrawable(mContext, user.getUserName(),
              holder.ismMessageDeliveryReadStatusItemBinding.ivProfilePic, position, 10);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Users view holder.
   */
  static class UsersViewHolder extends RecyclerView.ViewHolder {

    private final IsmMessageDeliveryReadStatusItemBinding ismMessageDeliveryReadStatusItemBinding;

    /**
     * Instantiates a new Users view holder.
     *
     * @param ismMessageDeliveryReadStatusItemBinding the ism message delivery read status item
     * binding
     */
    public UsersViewHolder(
        final IsmMessageDeliveryReadStatusItemBinding ismMessageDeliveryReadStatusItemBinding) {
      super(ismMessageDeliveryReadStatusItemBinding.getRoot());
      this.ismMessageDeliveryReadStatusItemBinding = ismMessageDeliveryReadStatusItemBinding;
    }
  }
}