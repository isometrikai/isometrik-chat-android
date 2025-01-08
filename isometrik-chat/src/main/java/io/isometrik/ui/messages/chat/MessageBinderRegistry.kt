package io.isometrik.ui.messages.chat

import androidx.viewbinding.ViewBinding
import io.isometrik.chat.utils.enums.MessageTypeUi

object MessageBinderRegistry {
    private val customBinders = mutableMapOf<MessageTypeUi, MessageItemBinder<out MessagesModel, *>>()

    fun <VB : ViewBinding> registerBinder(
        viewType: MessageTypeUi,
        binder: MessageItemBinder<out MessagesModel, VB>
    ) {
        customBinders[viewType] = binder
    }

    fun getBinder(viewType: MessageTypeUi): MessageItemBinder<out MessagesModel, *>? {
        return customBinders[viewType]
    }
}
