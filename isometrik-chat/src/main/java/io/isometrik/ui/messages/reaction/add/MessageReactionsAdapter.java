package io.isometrik.ui.messages.reaction.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import io.isometrik.chat.databinding.IsmMessageReactionItemBinding;
import io.isometrik.ui.messages.reaction.util.ReactionClickListener;
import io.isometrik.ui.utils.GlideApp;

import java.util.ArrayList;

/**
 * The recycler view adapter for the list of reactions added to a message.
 */
public class MessageReactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ReactionModel> reactionsModels;
  private final String messageId;
  private final ReactionClickListener reactionClickListener;

  /**
   * Instantiates a new Message reactions adapter.
   *
   * @param mContext the m context
   * @param reactionsModels the reactions models
   * @param messageId the message id
   * @param reactionClickListener the reaction click listener
   */
  public MessageReactionsAdapter(Context mContext, ArrayList<ReactionModel> reactionsModels,
      String messageId, ReactionClickListener reactionClickListener) {
    this.mContext = mContext;
    this.reactionsModels = reactionsModels;
    this.messageId = messageId;
    this.reactionClickListener = reactionClickListener;
  }

  @Override
  public int getItemCount() {
    return reactionsModels.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmMessageReactionItemBinding ismMessageReactionItemBinding =
        IsmMessageReactionItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new MessageReactionsViewHolder(ismMessageReactionItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    MessageReactionsViewHolder holder = (MessageReactionsViewHolder) viewHolder;

    try {
      ReactionModel reactionModel = reactionsModels.get(position);
      if (reactionModel != null) {
        holder.ismMessageReactionItemBinding.tvReactionCount.setText(
            String.valueOf(reactionModel.getReactionCount()));
        try {
          GlideApp.with(mContext)
              .load(reactionModel.getReactionIcon())
              .diskCacheStrategy(DiskCacheStrategy.NONE)
              .into(holder.ismMessageReactionItemBinding.ivReaction);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      }
      holder.ismMessageReactionItemBinding.rlReaction.setOnClickListener(v -> {
        if (reactionClickListener != null) {
          reactionClickListener.onMessageReactionClicked(messageId, reactionModel);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Message reactions view holder.
   */
  static class MessageReactionsViewHolder extends RecyclerView.ViewHolder {

    private final IsmMessageReactionItemBinding ismMessageReactionItemBinding;

    /**
     * Instantiates a new Message reactions view holder.
     *
     * @param ismMessageReactionItemBinding the ism message reaction item binding
     */
    public MessageReactionsViewHolder(
        final IsmMessageReactionItemBinding ismMessageReactionItemBinding) {
      super(ismMessageReactionItemBinding.getRoot());
      this.ismMessageReactionItemBinding = ismMessageReactionItemBinding;
    }
  }
}
