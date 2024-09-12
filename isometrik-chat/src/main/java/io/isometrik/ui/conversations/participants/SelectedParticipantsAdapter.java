package io.isometrik.ui.conversations.participants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmSelectedMemberItemBinding;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of users that have been selected to be added as
 * participants to a conversation.
 */
public class SelectedParticipantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ParticipantsModel> participantsModels;

  /**
   * Instantiates a new Selected participants adapter.
   *
   * @param mContext the m context
   * @param participantsModels the participants models
   */
  SelectedParticipantsAdapter(Context mContext, ArrayList<ParticipantsModel> participantsModels) {
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

    IsmSelectedMemberItemBinding ismSelectedMemberItemBinding =
        IsmSelectedMemberItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new ParticipantsViewHolder(ismSelectedMemberItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ParticipantsViewHolder holder = (ParticipantsViewHolder) viewHolder;

    try {
      ParticipantsModel participantsModel = participantsModels.get(position);
      if (participantsModel != null) {
        holder.ismSelectedMemberItemBinding.tvUserName.setText(participantsModel.getUserName());

        holder.ismSelectedMemberItemBinding.ivRemoveUser.setOnClickListener(
            v -> ((AddParticipantsActivity) mContext).removeParticipant(participantsModel.getUserId()));
        if (PlaceholderUtils.isValidImageUrl(participantsModel.getUserProfilePic())) {

          try {
            Glide.with(mContext)
                .load(participantsModel.getUserProfilePic())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismSelectedMemberItemBinding.ivUserImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, participantsModel.getUserName(),
              holder.ismSelectedMemberItemBinding.ivUserImage, position, 20);
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

    private final IsmSelectedMemberItemBinding ismSelectedMemberItemBinding;

    /**
     * Instantiates a new Participants view holder.
     *
     * @param ismSelectedMemberItemBinding the ism selected member item binding
     */
    public ParticipantsViewHolder(final IsmSelectedMemberItemBinding ismSelectedMemberItemBinding) {
      super(ismSelectedMemberItemBinding.getRoot());
      this.ismSelectedMemberItemBinding = ismSelectedMemberItemBinding;
    }
  }
}