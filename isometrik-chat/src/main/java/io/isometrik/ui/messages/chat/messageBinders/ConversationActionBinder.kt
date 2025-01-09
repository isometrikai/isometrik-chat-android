package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import io.isometrik.chat.databinding.IsmConversationActionMessageBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel

class ConversationActionBinder :
    MessageItemBinder<MessagesModel, IsmConversationActionMessageBinding> {

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