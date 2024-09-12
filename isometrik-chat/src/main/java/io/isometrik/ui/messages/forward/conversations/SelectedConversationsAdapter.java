package io.isometrik.ui.messages.forward.conversations;

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
 * The recycler view adapter for the list of conversations that have been selected to forward a
 * message to.
 */
public class SelectedConversationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ForwardInConversationModel> conversationModels;
  private final ForwardInConversationFragment forwardInConversationFragment;

  /**
   * Instantiates a new Selected conversations adapter.
   *
   * @param mContext the m context
   * @param conversationModels the conversation models
   * @param forwardInConversationFragment the forward in conversation fragment
   */
  SelectedConversationsAdapter(Context mContext,
      ArrayList<ForwardInConversationModel> conversationModels,
      ForwardInConversationFragment forwardInConversationFragment) {
    this.mContext = mContext;
    this.conversationModels = conversationModels;
    this.forwardInConversationFragment = forwardInConversationFragment;
  }

  @Override
  public int getItemCount() {
    return conversationModels.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmSelectedConversationPeopleItemBinding ismSelectedConversationPeopleItemBinding =
        IsmSelectedConversationPeopleItemBinding.inflate(
            LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
    return new SelectedConversationsViewHolder(ismSelectedConversationPeopleItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    SelectedConversationsViewHolder holder = (SelectedConversationsViewHolder) viewHolder;

    try {
      ForwardInConversationModel conversationModel = conversationModels.get(position);
      if (conversationModel != null) {
        holder.ismSelectedConversationPeopleItemBinding.tvConversationPeopleName.setText(
            conversationModel.getConversationTitle());

        holder.ismSelectedConversationPeopleItemBinding.ivRemoveConversationPeople.setOnClickListener(
            v -> forwardInConversationFragment.removeConversation(
                conversationModel.getConversationId()));
        if (PlaceholderUtils.isValidImageUrl(conversationModel.getConversationImageUrl())) {

          try {
            Glide.with(mContext)
                .load(conversationModel.getConversationImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismSelectedConversationPeopleItemBinding.ivConversationPeopleImage);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, conversationModel.getConversationTitle(),
              holder.ismSelectedConversationPeopleItemBinding.ivConversationPeopleImage, position,
              20);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Selected conversations view holder.
   */
  static class SelectedConversationsViewHolder extends RecyclerView.ViewHolder {

    private final IsmSelectedConversationPeopleItemBinding ismSelectedConversationPeopleItemBinding;

    /**
     * Instantiates a new Selected conversations view holder.
     *
     * @param ismSelectedConversationPeopleItemBinding the ism selected conversation people item
     * binding
     */
    public SelectedConversationsViewHolder(
        final IsmSelectedConversationPeopleItemBinding ismSelectedConversationPeopleItemBinding) {
      super(ismSelectedConversationPeopleItemBinding.getRoot());
      this.ismSelectedConversationPeopleItemBinding = ismSelectedConversationPeopleItemBinding;
    }
  }
}