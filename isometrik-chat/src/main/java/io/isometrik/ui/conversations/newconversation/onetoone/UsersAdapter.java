package io.isometrik.ui.conversations.newconversation.onetoone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmUnselectedMemberItemBinding;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of users to select opponent to create a one to one
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
              mContext.getString(R.string.ism_unselect));
        } else {
          holder.ismUnselectedMemberItemBinding.tvAddRemove.setText(
              mContext.getString(R.string.ism_select));
        }
        if (PlaceholderUtils.isValidImageUrl(user.getUserProfilePic())) {

          try {
            Glide.with(mContext)
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
