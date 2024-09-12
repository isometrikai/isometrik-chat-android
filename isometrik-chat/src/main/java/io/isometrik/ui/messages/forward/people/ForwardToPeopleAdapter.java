package io.isometrik.ui.messages.forward.people;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBroadcastForwardConversationUserItemBinding;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of users to which a message can be forwarded.
 */
public class ForwardToPeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<PeopleModel> peopleModels;

  /**
   * Instantiates a new Forward to people adapter.
   *
   * @param mContext the m context
   * @param peopleModels the people models
   */
  public ForwardToPeopleAdapter(Context mContext, ArrayList<PeopleModel> peopleModels) {
    this.mContext = mContext;
    this.peopleModels = peopleModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmBroadcastForwardConversationUserItemBinding ismForwardToUsersItemBinding =
        IsmBroadcastForwardConversationUserItemBinding.inflate(
            LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
    return new PeopleViewHolder(ismForwardToUsersItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    PeopleViewHolder holder = (PeopleViewHolder) viewHolder;

    try {
      PeopleModel peopleModel = peopleModels.get(position);

      holder.ismForwardToUsersItemBinding.tvConversationOrUserName.setText(
          peopleModel.getUserName());

      holder.ismForwardToUsersItemBinding.tvSelect.setText(
          peopleModel.isSelected() ? mContext.getString(R.string.ism_remove)
              : mContext.getString(R.string.ism_add));
      if (PlaceholderUtils.isValidImageUrl(peopleModel.getUserProfileImageUrl())) {

        try {
          Glide.with(mContext)
              .load(peopleModel.getUserProfileImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismForwardToUsersItemBinding.ivConversationOrUserImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, peopleModel.getUserName(),
            holder.ismForwardToUsersItemBinding.ivConversationOrUserImage, position, 10);
      }
      if (peopleModel.isOnline()) {
        holder.ismForwardToUsersItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
      } else {
        holder.ismForwardToUsersItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
      }
      holder.ismForwardToUsersItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return peopleModels.size();
  }

  /**
   * The type People view holder.
   */
  static class PeopleViewHolder extends RecyclerView.ViewHolder {

    private final IsmBroadcastForwardConversationUserItemBinding ismForwardToUsersItemBinding;

    /**
     * Instantiates a new People view holder.
     *
     * @param ismForwardToUsersItemBinding the ism forward to users item binding
     */
    public PeopleViewHolder(
        final IsmBroadcastForwardConversationUserItemBinding ismForwardToUsersItemBinding) {
      super(ismForwardToUsersItemBinding.getRoot());
      this.ismForwardToUsersItemBinding = ismForwardToUsersItemBinding;
    }
  }
}
