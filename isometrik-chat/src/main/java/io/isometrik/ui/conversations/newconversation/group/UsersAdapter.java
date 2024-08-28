package io.isometrik.ui.conversations.newconversation.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmUnselectedMemberItemBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of users to select members to create a group
 * conversation with.
 */
public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<UsersModel> users;

  /**
   * Instantiates a new Users adapter.
   *
   * @param mContext the m context
   * @param users the users
   */
  UsersAdapter(Context mContext, ArrayList<UsersModel> users) {
    this.mContext = mContext;
    this.users = users;
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmUnselectedMemberItemBinding ismUnselectedMemberItemBinding =
        IsmUnselectedMemberItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new UsersViewHolder(ismUnselectedMemberItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    UsersViewHolder holder = (UsersViewHolder) viewHolder;

    try {
      UsersModel user = users.get(position);
      if (user != null) {
        holder.ismUnselectedMemberItemBinding.tvUserName.setText(user.getUserName());

        if (user.isOnline()) {
          holder.ismUnselectedMemberItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
        } else {
          holder.ismUnselectedMemberItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
        }
        if (user.isSelected()) {
          holder.ismUnselectedMemberItemBinding.tvAddRemove.setText(
              mContext.getString(R.string.ism_remove));
        } else {
          holder.ismUnselectedMemberItemBinding.tvAddRemove.setText(
              mContext.getString(R.string.ism_add));
        }
        if (PlaceholderUtils.isValidImageUrl(user.getUserProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(user.getUserProfilePic())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismUnselectedMemberItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, user.getUserName(),
              holder.ismUnselectedMemberItemBinding.ivProfilePic, position, 10);
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

    private final IsmUnselectedMemberItemBinding ismUnselectedMemberItemBinding;

    /**
     * Instantiates a new Users view holder.
     *
     * @param ismUnselectedMemberItemBinding the ism unselected member item binding
     */
    public UsersViewHolder(final IsmUnselectedMemberItemBinding ismUnselectedMemberItemBinding) {
      super(ismUnselectedMemberItemBinding.getRoot());
      this.ismUnselectedMemberItemBinding = ismUnselectedMemberItemBinding;
    }
  }
}
