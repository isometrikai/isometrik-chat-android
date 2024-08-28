package io.isometrik.ui.messages.reaction.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmReactionUserItemBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of users who have added a particular reaction on a
 * message.
 */
public class FetchReactionUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ReactionUsersModel> reactionUsersModels;
  private final FetchReactionUsersFragment fetchReactionUsersFragment;
  private final boolean messagingDisabled;

  /**
   * Instantiates a new Fetch reaction users adapter.
   *
   * @param mContext the m context
   * @param reactionUsersModels the reaction users models
   * @param fetchReactionUsersFragment the fetch reaction users fragment
   * @param messagingDisabled the messaging disabled
   */
  public FetchReactionUsersAdapter(Context mContext,
      ArrayList<ReactionUsersModel> reactionUsersModels,
      FetchReactionUsersFragment fetchReactionUsersFragment, boolean messagingDisabled) {
    this.mContext = mContext;
    this.reactionUsersModels = reactionUsersModels;
    this.fetchReactionUsersFragment = fetchReactionUsersFragment;
    this.messagingDisabled = messagingDisabled;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    IsmReactionUserItemBinding ismReactionUserItemBinding =
        IsmReactionUserItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new ReactionUserViewHolder(ismReactionUserItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    ReactionUserViewHolder holder = (ReactionUserViewHolder) viewHolder;

    try {
      ReactionUsersModel reactionUsersModel = reactionUsersModels.get(position);
      if (reactionUsersModel != null) {
        holder.ismReactionUserItemBinding.tvUserName.setText(reactionUsersModel.getUserName());

        if (reactionUsersModel.isOnline()) {
          holder.ismReactionUserItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
        } else {
          holder.ismReactionUserItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
        }
        if (reactionUsersModel.isReactionAddedBySelf() && !messagingDisabled) {
          holder.ismReactionUserItemBinding.ivRemoveReaction.setVisibility(View.VISIBLE);
          holder.ismReactionUserItemBinding.ivRemoveReaction.setOnClickListener(
              v -> fetchReactionUsersFragment.removeReaction(position));
        } else {
          holder.ismReactionUserItemBinding.ivRemoveReaction.setVisibility(View.GONE);
        }
        if (PlaceholderUtils.isValidImageUrl(reactionUsersModel.getUserProfileImageUrl())) {

          try {
            GlideApp.with(mContext)
                .load(reactionUsersModel.getUserProfileImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismReactionUserItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, reactionUsersModel.getUserName(),
              holder.ismReactionUserItemBinding.ivProfilePic, position, 10);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return reactionUsersModels.size();
  }

  /**
   * The type Reaction user view holder.
   */
  static class ReactionUserViewHolder extends RecyclerView.ViewHolder {

    private final IsmReactionUserItemBinding ismReactionUserItemBinding;

    /**
     * Instantiates a new Reaction user view holder.
     *
     * @param ismReactionUserItemBinding the ism reaction user item binding
     */
    public ReactionUserViewHolder(final IsmReactionUserItemBinding ismReactionUserItemBinding) {
      super(ismReactionUserItemBinding.getRoot());
      this.ismReactionUserItemBinding = ismReactionUserItemBinding;
    }
  }

  /**
   * Update reaction users list on reaction removed.
   *
   * @param position the position
   */
  public void updateReactionUsersListOnReactionRemoved(int position) {
    reactionUsersModels.remove(position);
    notifyItemRemoved(position);
  }
}