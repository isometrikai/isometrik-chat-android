package io.isometrik.ui.messages.chat.common

import io.isometrik.chat.enums.CustomMessageTypes
import io.isometrik.chat.enums.MessageTypeUi
import io.isometrik.chat.utils.LogManger
import io.isometrik.chat.utils.LogManger.log
import io.isometrik.ui.messages.chat.messageBinders.MessageItemBinder

object MessageBinderRegistry {
    private val binders = mutableMapOf<MessageTypeUi, MessageItemBinder<*, *>>()

    /**
     * Register a binder for a message type.
     * @param messageType The message type
     * @param binder The binder to register
     */
    fun registerBinder(messageType: MessageTypeUi, binder: MessageItemBinder<*, *>) {
        binders[messageType] = binder
    }

    /**
     * Get the binder for a message type.
     * @param messageType The message type
     * @param customType The custom type value (if applicable)
     * @param isSent Whether the message is sent or received
     * @return The registered binder or null if not found
     */
    fun getBinder(messageType: MessageTypeUi, customType: String? = null, isSent: Boolean = true): MessageItemBinder<*, *>? {
        log("ChatSDK:", "getBinder() messageType: $messageType")
        if (messageType == MessageTypeUi.CUSTOM_MESSAGE_SENT || messageType == MessageTypeUi.CUSTOM_MESSAGE_RECEIVED) {
            customType?.let {
                 val binder = CustomMessageTypes.getCustomBinder(it, messageType == MessageTypeUi.CUSTOM_MESSAGE_SENT)
                log("ChatSDK:", "getBinder() customType: $customType")

                return binder
            }
        }
        // Fall back to regular binder
        return binders[messageType]
    }

    fun getBinderList() = binders
}
