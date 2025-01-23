package io.isometrik.ui.messages.chat.common

import androidx.viewbinding.ViewBinding
import io.isometrik.chat.enums.MessageTypeUi
import io.isometrik.ui.messages.chat.messageBinders.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel

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

    fun getBinderList() = customBinders
}
