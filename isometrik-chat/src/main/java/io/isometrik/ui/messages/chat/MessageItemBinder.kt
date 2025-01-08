package io.isometrik.ui.messages.chat

import android.content.Context
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import io.isometrik.ui.messages.action.MessageActionCallback

interface MessageItemBinder<T, VB : ViewBinding> {
    fun createBinding(parent: ViewGroup, viewType: Int): VB
    fun bindData(
        context: Context,
        binding: VB,
        data: T,
        position : Int,
        multipleMessagesSelectModeOn: Boolean,
        isMessagingDisabled: Boolean,
        messageActionCallback: MessageActionCallback
    )
}
