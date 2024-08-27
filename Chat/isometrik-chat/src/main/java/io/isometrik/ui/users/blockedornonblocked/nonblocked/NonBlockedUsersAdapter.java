package io.isometrik.ui.users.blockedornonblocked.nonblocked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBlockedNonBlockedUserItemBinding;
import io.isometrik.ui.users.blockedornonblocked.BlockedOrNonBlockedUsersModel;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;

import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the users neither blocked by logged in user nor logged
 * in user blocked by them.
 */
public class NonBlockedUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<BlockedOrNonBlockedUsersModel> nonBlockedUsers;
  private final NonBlockedUsersFragment nonBlockedUsersFragment;

  /**
   * Instantiates a new Non blocked users adapter.
   *
   * @param mContext the m context
   * @param nonBlockedUsers the non blocked users
   * @param nonBlockedUsersFragment the non blocked users fragment
   */
  public NonBlockedUsersAdapter(Context mContext,
      ArrayList<BlockedOrNonBlockedUsersModel> nonBlockedUsers,
      NonBlockedUsersFragment nonBlockedUsersFragment) {
    this.mContext = mContext;
    this.nonBlockedUsers = nonBlockedUsers;
    this.nonBlockedUsersFragment = nonBlockedUsersFragment;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmBlockedNonBlockedUserItemBinding ismNonBlockedUserItemBinding =
        IsmBlockedNonBlockedUserItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new NonBlockedUsersViewHolder(ismNonBlockedUserItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    NonBlockedUsersViewHolder holder = (NonBlockedUsersViewHolder) viewHolder;

    try {
      BlockedOrNonBlockedUsersModel nonBlockedUser = nonBlockedUsers.get(position);

      holder.ismNonBlockedUserItemBinding.tvUserName.setText(nonBlockedUser.getUserName());
      holder.ismNonBlockedUserItemBinding.tvUserIdentifier.setText(
          nonBlockedUser.getUserIdentifier());

      if (PlaceholderUtils.isValidImageUrl(nonBlockedUser.getUserProfileImageUrl())) {

        try {
          GlideApp.with(mContext)
              .load(nonBlockedUser.getUserProfileImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismNonBlockedUserItemBinding.ivUserImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, nonBlockedUser.getUserName(),
            holder.ismNonBlockedUserItemBinding.ivUserImage, position, 20);
      }
      if (nonBlockedUser.isOnline()) {
        holder.ismNonBlockedUserItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
      } else {
        holder.ismNonBlockedUserItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
      }

      holder.ismNonBlockedUserItemBinding.rlBlockOrUnblockUser.setOnClickListener(
          v -> nonBlockedUsersFragment.blockUser(nonBlockedUser.getUserId(),
              nonBlockedUser.getUserName(), position));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return nonBlockedUsers.size();
  }

  /**
   * The type Non blocked users view holder.
   */
  static class NonBlockedUsersViewHolder extends RecyclerView.ViewHolder {

    private final IsmBlockedNonBlockedUserItemBinding ismNonBlockedUserItemBinding;

    /**
     * Instantiates a new Non blocked users view holder.
     *
     * @param ismNonBlockedUserItemBinding the ism non blocked user item binding
     */
    public NonBlockedUsersViewHolder(
        final IsmBlockedNonBlockedUserItemBinding ismNonBlockedUserItemBinding) {
      super(ismNonBlockedUserItemBinding.getRoot());
      this.ismNonBlockedUserItemBinding = ismNonBlockedUserItemBinding;
    }
  }
}