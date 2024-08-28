package io.isometrik.ui.conversations.newconversation.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmSelectedMemberItemBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of users that have been selected to create a new group
 * conversation.
 */
public class SelectedUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<UsersModel> users;

  /**
   * Instantiates a new Selected users adapter.
   *
   * @param mContext the m context
   * @param users the users
   */
  SelectedUsersAdapter(Context mContext, ArrayList<UsersModel> users) {
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

    IsmSelectedMemberItemBinding ismSelectedMemberItemBinding =
        IsmSelectedMemberItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new UsersViewHolder(ismSelectedMemberItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    UsersViewHolder holder = (UsersViewHolder) viewHolder;

    try {
      UsersModel user = users.get(position);
      if (user != null) {
        holder.ismSelectedMemberItemBinding.tvUserName.setText(user.getUserName());

        holder.ismSelectedMemberItemBinding.ivRemoveUser.setOnClickListener(v -> {
          if (mContext instanceof NewGroupConversationActivity) {
            ((NewGroupConversationActivity) mContext).removeUser(user.getUserId());
          }
          //else if (mContext instanceof SearchParticipantsActivity) {
          //  ((SearchParticipantsActivity) mContext).removeUser(user.getUserId());
          //}
        });
        if (PlaceholderUtils.isValidImageUrl(user.getUserProfilePic())) {

          try {
            GlideApp.with(mContext)
                .load(user.getUserProfilePic())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismSelectedMemberItemBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, user.getUserName(),
              holder.ismSelectedMemberItemBinding.ivUserImage, position, 20);
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

    private final IsmSelectedMemberItemBinding ismSelectedMemberItemBinding;

    /**
     * Instantiates a new Users view holder.
     *
     * @param ismSelectedMemberItemBinding the ism selected member item binding
     */
    public UsersViewHolder(final IsmSelectedMemberItemBinding ismSelectedMemberItemBinding) {
      super(ismSelectedMemberItemBinding.getRoot());
      this.ismSelectedMemberItemBinding = ismSelectedMemberItemBinding;
    }
  }
}