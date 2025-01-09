package io.isometrik.ui.messages.chat.common

import android.view.View
import android.view.ViewGroup
import io.isometrik.ui.messages.chat.MessagesModel

interface ChatTopViewHandler {
    /**
     * Called once to create the top view.
     */
    fun createTopView(parent: ViewGroup): View

    /**
     * Called to update the top view whenever the message changes or a new message arrives.
     */
    fun updateTopView(view: View, message: MessagesModel)
}
