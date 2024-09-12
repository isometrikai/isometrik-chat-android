package io.isometrik.ui.conversations.participants;

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
 * The recycler view adapter for the list of users that can be added as participants to a
 * conversation.
 */
public class ParticipantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ParticipantsModel> participantsModels;

  /**
   * Instantiates a new Participants adapter.
   *
   * @param mContext the m context
   * @param participantsModels the participants models
   */
  ParticipantsAdapter(Context mContext, ArrayList<ParticipantsModel> participantsModels) {
    this.mContext = mContext;
    this.participantsModels = participantsModels;
  }

  @Override
  public int getItemCount() {
    return participantsModels.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmUnselectedMemberItemBinding ismUnselectedMemberItemBinding =
        IsmUnselectedMemberItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new ParticipantsViewHolder(ismUnselectedMemberItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ParticipantsViewHolder holder = (ParticipantsViewHolder) viewHolder;

    try {
      ParticipantsModel participantsModel = participantsModels.get(position);
      if (participantsModel != null) {
        holder.ismUnselectedMemberItemBinding.tvUserName.setText(participantsModel.getUserName());

        if (participantsModel.isOnline()) {
          holder.ismUnselectedMemberItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
        } else {
          holder.ismUnselectedMemberItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
        }
        if (participantsModel.isSelected()) {
          holder.ismUnselectedMemberItemBinding.tvAddRemove.setText(
              mContext.getString(R.string.ism_remove));
        } else {
          holder.ismUnselectedMemberItemBinding.tvAddRemove.setText(
              mContext.getString(R.string.ism_add));
        }
        if (PlaceholderUtils.isValidImageUrl(participantsModel.getUserProfilePic())) {

          try {
            Glide.with(mContext)
                .load(participantsModel.getUserProfilePic())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismUnselectedMemberItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, participantsModel.getUserName(),
              holder.ismUnselectedMemberItemBinding.ivProfilePic, position, 10);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Participants view holder.
   */
  static class ParticipantsViewHolder extends RecyclerView.ViewHolder {

    private final IsmUnselectedMemberItemBinding ismUnselectedMemberItemBinding;

    /**
     * Instantiates a new Participants view holder.
     *
     * @param ismUnselectedMemberItemBinding the ism unselected member item binding
     */
    public ParticipantsViewHolder(
        final IsmUnselectedMemberItemBinding ismUnselectedMemberItemBinding) {
      super(ismUnselectedMemberItemBinding.getRoot());
      this.ismUnselectedMemberItemBinding = ismUnselectedMemberItemBinding;
    }
  }
}
