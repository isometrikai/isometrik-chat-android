package io.isometrik.ui.messages.reaction.add

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.isometrik.chat.databinding.IsmMessageReactionItemBinding
import io.isometrik.ui.messages.reaction.util.ReactionClickListener

/**
 * The recycler view adapter for the list of reactions added to a message.
 */

/**
 * Instantiates a new Message reactions adapter.
 *
 * @param mContext the m context
 * @param reactionsModels the reactions models
 * @param messageId the message id
 * @param reactionClickListener the reaction click listener
 */
class MessageReactionsAdapter
    (
    private val mContext: Context,
    private val reactionsModels: ArrayList<ReactionModel>,
    private val messageId: String,
    private val onMessageReactionClicked: (String, ReactionModel) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return reactionsModels.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val ismMessageReactionItemBinding =
            IsmMessageReactionItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup, false
            )
        return MessageReactionsViewHolder(ismMessageReactionItemBinding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as MessageReactionsViewHolder

        try {
            val reactionModel = reactionsModels[position]
            if (reactionModel != null) {
                holder.ismMessageReactionItemBinding.tvReactionCount.text =
                    reactionModel.reactionCount.toString()
                try {
                    Glide.with(mContext)
                        .load(reactionModel.reactionIcon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.ismMessageReactionItemBinding.ivReaction)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            }
            holder.ismMessageReactionItemBinding.rlReaction.setOnClickListener { v: View? ->
                onMessageReactionClicked(messageId, reactionModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * The type Message reactions view holder.
     */
    internal class MessageReactionsViewHolder
    /**
     * Instantiates a new Message reactions view holder.
     *
     * @param ismMessageReactionItemBinding the ism message reaction item binding
     */(val ismMessageReactionItemBinding: IsmMessageReactionItemBinding) :
        RecyclerView.ViewHolder(ismMessageReactionItemBinding.root)
}
