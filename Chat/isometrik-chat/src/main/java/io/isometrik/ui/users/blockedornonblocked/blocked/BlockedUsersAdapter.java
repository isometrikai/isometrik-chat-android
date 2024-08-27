package io.isometrik.ui.users.blockedornonblocked.blocked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBlockedNonBlockedUserItemBinding;
import io.isometrik.ui.users.blockedornonblocked.BlockedOrNonBlockedUsersModel;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;

import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the users blocked by logged in user.
 */
public class BlockedUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<BlockedOrNonBlockedUsersModel> blockedUsers;
  private final BlockedUsersFragment blockedUsersFragment;

  /**
   * Instantiates a new Blocked users adapter.
   *
   * @param mContext the m context
   * @param blockedUsers the blocked users
   * @param blockedUsersFragment the blocked users fragment
   */
  public BlockedUsersAdapter(Context mContext,
      ArrayList<BlockedOrNonBlockedUsersModel> blockedUsers,
      BlockedUsersFragment blockedUsersFragment) {
    this.mContext = mContext;
    this.blockedUsers = blockedUsers;
    this.blockedUsersFragment = blockedUsersFragment;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmBlockedNonBlockedUserItemBinding ismBlockedUserItemBinding =
        IsmBlockedNonBlockedUserItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new BlockedUsersViewHolder(ismBlockedUserItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    BlockedUsersViewHolder holder = (BlockedUsersViewHolder) viewHolder;

    try {
      BlockedOrNonBlockedUsersModel blockedUser = blockedUsers.get(position);

      holder.ismBlockedUserItemBinding.tvUserName.setText(blockedUser.getUserName());
      holder.ismBlockedUserItemBinding.tvUserIdentifier.setText(blockedUser.getUserIdentifier());
      holder.ismBlockedUserItemBinding.tvBlockOrUnblockUser.setText(
          mContext.getString(R.string.ism_unblock));
      if (PlaceholderUtils.isValidImageUrl(blockedUser.getUserProfileImageUrl())) {

        try {
          GlideApp.with(mContext)
              .load(blockedUser.getUserProfileImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismBlockedUserItemBinding.ivUserImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, blockedUser.getUserName(),
            holder.ismBlockedUserItemBinding.ivUserImage, position, 20);
      }
      //if (blockedUser.isOnline()) {
      //  holder.ismBlockedUserItemBinding.ivOnlineStatus.setImageDrawable(
      //      ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
      //} else {
      //  holder.ismBlockedUserItemBinding.ivOnlineStatus.setImageDrawable(
      //      ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
      //}
      holder.ismBlockedUserItemBinding.ivOnlineStatus.setVisibility(View.GONE);
      holder.ismBlockedUserItemBinding.rlBlockOrUnblockUser.setOnClickListener(
          v -> blockedUsersFragment.unblockUser(blockedUser.getUserId(), blockedUser.getUserName(),
              position));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return blockedUsers.size();
  }

  /**
   * The type Blocked users view holder.
   */
  static class BlockedUsersViewHolder extends RecyclerView.ViewHolder {

    private final IsmBlockedNonBlockedUserItemBinding ismBlockedUserItemBinding;

    /**
     * Instantiates a new Blocked users view holder.
     *
     * @param ismBlockedUserItemBinding the ism blocked user item binding
     */
    public BlockedUsersViewHolder(
        final IsmBlockedNonBlockedUserItemBinding ismBlockedUserItemBinding) {
      super(ismBlockedUserItemBinding.getRoot());
      this.ismBlockedUserItemBinding = ismBlockedUserItemBinding;
    }
  }
}