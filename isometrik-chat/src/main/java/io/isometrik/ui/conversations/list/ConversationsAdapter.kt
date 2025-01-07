package io.isometrik.ui.conversations.list

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmConversationItemBinding
import io.isometrik.chat.utils.PlaceholderUtils

/**
 * The recycler view adapter for the list of the public/open and all conversations.
 */

/**
 * Instantiates a new Conversations adapter.
 *
 * @param mContext the m context
 * @param conversationsModels the conversations models
 * @param conversationsFragment the conversations fragment
 */

class ConversationsAdapter<T, VB : ViewBinding>(
    private val mContext: Context,
    private val dataList: List<T>,
    private val itemBinder: ChatListItemBinder<T, VB>,
    private val onJoinClick: (T) -> Unit
) : RecyclerView.Adapter<ConversationsAdapter<T, VB>.ConversationViewHolder>() {


    /**
     * The type Conversation view holder.
     */
    inner class ConversationViewHolder(val ismConversationItemBinding: VB) : RecyclerView.ViewHolder(ismConversationItemBinding.root)


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ConversationViewHolder {
         val binding = itemBinder.createBinding(viewGroup)
        return ConversationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val data = dataList[position]
        itemBinder.bindData(holder.itemView.context, holder.ismConversationItemBinding, data)
       if(holder.ismConversationItemBinding  is IsmConversationItemBinding){
           val binding = (holder.ismConversationItemBinding  as IsmConversationItemBinding)
           binding.tvJoinConversation.setOnClickListener {
               onJoinClick(data)
           }
       }
    }

    override fun getItemCount(): Int = dataList.size

}
