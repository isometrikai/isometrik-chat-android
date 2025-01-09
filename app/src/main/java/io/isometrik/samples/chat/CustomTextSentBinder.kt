package io.isometrik.samples.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import io.isometrik.samples.chat.databinding.ItemCustomTextBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.messageBinders.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel

class CustomTextSentBinder : MessageItemBinder<MessagesModel, ItemCustomTextBinding> {
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemCustomTextBinding {
        return ItemCustomTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(
        context: Context,
        binding: ItemCustomTextBinding,
        data: MessagesModel,
        position: Int,
        multipleMessagesSelectModeOn: Boolean,
        isMessagingDisabled: Boolean,
        messageActionCallback: MessageActionCallback
    ) {

        binding.textTitle.text = data.textMessage

    }
}