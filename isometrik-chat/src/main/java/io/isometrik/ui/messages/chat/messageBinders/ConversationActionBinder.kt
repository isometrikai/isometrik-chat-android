package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.isometrik.chat.databinding.IsmConversationActionMessageBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel

class ConversationActionBinder :
    MessageItemBinder<MessagesModel, IsmConversationActionMessageBinding> {

    val hideMessagekeywords = listOf("searchable tags", "blocked you", "unblocked you")

    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): IsmConversationActionMessageBinding {
        return IsmConversationActionMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bindData(
        mContext: Context, ismConversationActionMessageBinding: IsmConversationActionMessageBinding,
        message: MessagesModel, position: Int, multipleMessagesSelectModeOn: Boolean,
        isMessagingDisabled: Boolean, messageActionCallback: MessageActionCallback
    ) {
        try {
            if (!containsKeyword(message.conversationActionMessage, hideMessagekeywords)) {
                ismConversationActionMessageBinding.tvActionMessage.text =
                    message.conversationActionMessage
            } else {
                ismConversationActionMessageBinding.tvActionMessage.visibility = View.GONE
            }

        } catch (ignore: Exception) {
        }
    }

    fun containsKeyword(sentence: String, keywords: List<String>): Boolean {
        return keywords.any { keyword -> sentence.contains(keyword, ignoreCase = true) }
    }
}