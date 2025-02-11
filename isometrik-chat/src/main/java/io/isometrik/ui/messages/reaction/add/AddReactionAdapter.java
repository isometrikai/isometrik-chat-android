package io.isometrik.ui.messages.reaction.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import io.isometrik.chat.databinding.IsmReactionItemBinding;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of reactions that can be added to a message.
 */
public class AddReactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ReactionModel> reactionsModels;

  /**
   * Instantiates a new Add reaction adapter.
   *
   * @param mContext the m context
   * @param reactionsModels the reactions models
   */
  public AddReactionAdapter(Context mContext, ArrayList<ReactionModel> reactionsModels) {
    this.mContext = mContext;
    this.reactionsModels = reactionsModels;
  }

  @Override
  public int getItemCount() {
    return reactionsModels.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmReactionItemBinding ismReactionItemBinding =
        IsmReactionItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new ReactionsViewHolder(ismReactionItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ReactionsViewHolder holder = (ReactionsViewHolder) viewHolder;

    try {
      ReactionModel reactionModel = reactionsModels.get(position);
      if (reactionModel != null) {

        try {
          Glide.with(mContext)
              .load(reactionModel.getReactionIcon())
              .diskCacheStrategy(DiskCacheStrategy.NONE)
              .into(holder.ismReactionItemBinding.ivReaction);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Reactions view holder.
   */
  static class ReactionsViewHolder extends RecyclerView.ViewHolder {

    private final IsmReactionItemBinding ismReactionItemBinding;

    /**
     * Instantiates a new Reactions view holder.
     *
     * @param ismReactionItemBinding the ism reaction item binding
     */
    public ReactionsViewHolder(final IsmReactionItemBinding ismReactionItemBinding) {
      super(ismReactionItemBinding.getRoot());
      this.ismReactionItemBinding = ismReactionItemBinding;
    }
  }
}
