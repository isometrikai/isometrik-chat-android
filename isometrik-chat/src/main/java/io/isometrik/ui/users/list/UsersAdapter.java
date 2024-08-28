package io.isometrik.ui.users.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmUsersItemBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the users.
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
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    IsmUsersItemBinding ismUsersItemBinding =
        IsmUsersItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
    return new UsersViewHolder(ismUsersItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    UsersViewHolder holder = (UsersViewHolder) viewHolder;

    try {
      UsersModel user = users.get(position);
      if (user != null) {
        holder.ismUsersItemBinding.tvUserName.setText(user.getUserName());
        holder.ismUsersItemBinding.tvUserIdentifier.setText(user.getUserIdentifier());

        holder.ismUsersItemBinding.rlSelect.setOnClickListener(
            v -> ((UsersActivity) mContext).requestUserCredentials(user));
        if (PlaceholderUtils.isValidImageUrl(user.getUserProfileImageUrl())) {

          try {
            GlideApp.with(mContext)
                .load(user.getUserProfileImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismUsersItemBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, user.getUserName(),
              holder.ismUsersItemBinding.ivUserImage, position, 20);
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
    private final IsmUsersItemBinding ismUsersItemBinding;

    /**
     * Instantiates a new Users view holder.
     *
     * @param ismUsersItemBinding the ism users item binding
     */
    public UsersViewHolder(final IsmUsersItemBinding ismUsersItemBinding) {
      super(ismUsersItemBinding.getRoot());
      this.ismUsersItemBinding = ismUsersItemBinding;
    }
  }
}