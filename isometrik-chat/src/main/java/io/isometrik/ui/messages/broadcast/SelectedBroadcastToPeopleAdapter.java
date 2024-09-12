package io.isometrik.ui.messages.broadcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmSelectedConversationPeopleItemBinding;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of users that have been selected to broadcast a message
 * to.
 */
public class SelectedBroadcastToPeopleAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<BroadcastToPeopleModel> people;

  /**
   * Instantiates a new Selected broadcast to people adapter.
   *
   * @param mContext the m context
   * @param people the people
   */
  SelectedBroadcastToPeopleAdapter(Context mContext, ArrayList<BroadcastToPeopleModel> people) {
    this.mContext = mContext;
    this.people = people;
  }

  @Override
  public int getItemCount() {
    return people.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmSelectedConversationPeopleItemBinding ismSelectedConversationPeopleItemBinding =
        IsmSelectedConversationPeopleItemBinding.inflate(
            LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
    return new SelectedPeopleViewHolder(ismSelectedConversationPeopleItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    SelectedPeopleViewHolder holder = (SelectedPeopleViewHolder) viewHolder;

    try {
      BroadcastToPeopleModel peopleModel = people.get(position);
      if (peopleModel != null) {
        holder.ismSelectedConversationPeopleItemBinding.tvConversationPeopleName.setText(
            peopleModel.getUserName());

        holder.ismSelectedConversationPeopleItemBinding.ivRemoveConversationPeople.setOnClickListener(
            v -> ((BroadcastMessageActivity) mContext).removePeople(peopleModel.getUserId()));
        if (PlaceholderUtils.isValidImageUrl(peopleModel.getUserProfileImageUrl())) {

          try {
            Glide.with(mContext)
                .load(peopleModel.getUserProfileImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismSelectedConversationPeopleItemBinding.ivConversationPeopleImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, peopleModel.getUserName(),
              holder.ismSelectedConversationPeopleItemBinding.ivConversationPeopleImage, position,
              20);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Selected people view holder.
   */
  static class SelectedPeopleViewHolder extends RecyclerView.ViewHolder {

    private final IsmSelectedConversationPeopleItemBinding ismSelectedConversationPeopleItemBinding;

    /**
     * Instantiates a new Selected people view holder.
     *
     * @param ismSelectedConversationPeopleItemBinding the ism selected conversation people item
     * binding
     */
    public SelectedPeopleViewHolder(
        final IsmSelectedConversationPeopleItemBinding ismSelectedConversationPeopleItemBinding) {
      super(ismSelectedConversationPeopleItemBinding.getRoot());
      this.ismSelectedConversationPeopleItemBinding = ismSelectedConversationPeopleItemBinding;
    }
  }
}