package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmConversationActionMessageBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class ConversationActionBinder : MessageItemBinder<MessagesModel, IsmConversationActionMessageBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmConversationActionMessageBinding {
        return IsmConversationActionMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismConversationActionMessageBinding: IsmConversationActionMessageBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismConversationActionMessageBinding.tvActionMessage.text =
                message.conversationActionMessage
        } catch (ignore: Exception) {
        }
    }
}