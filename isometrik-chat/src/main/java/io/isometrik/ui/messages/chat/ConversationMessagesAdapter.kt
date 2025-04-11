package io.isometrik.ui.messages.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import io.isometrik.chat.enums.CustomMessageTypes
import io.isometrik.chat.enums.MessageTypeUi
import io.isometrik.chat.utils.LogManger.log
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.common.MessageBinderRegistry
import io.isometrik.ui.messages.chat.messageBinders.MessageItemBinder
import io.isometrik.ui.messages.chat.viewholders.AudioMessageReceivedViewHolder
import io.isometrik.ui.messages.chat.viewholders.AudioMessageSentViewHolder
import io.isometrik.ui.messages.chat.viewholders.FileMessageReceivedViewHolder
import io.isometrik.ui.messages.chat.viewholders.FileMessageSentViewHolder
import io.isometrik.ui.messages.chat.viewholders.PhotoMessageReceivedViewHolder
import io.isometrik.ui.messages.chat.viewholders.PhotoMessageSentViewHolder
import io.isometrik.ui.messages.chat.viewholders.VideoMessageReceivedViewHolder
import io.isometrik.ui.messages.chat.viewholders.VideoMessageSentViewHolder
import io.isometrik.ui.messages.chat.viewholders.WhiteboardMessageReceivedViewHolder
import io.isometrik.ui.messages.chat.viewholders.WhiteboardMessageSentViewHolder

/**
 * The recycler view adapter for the list of messages in a conversation.Supported message types are-
 * image/video/file/contact/location/whiteboard/sticker/gif/audio/text.
 */
class ConversationMessagesAdapter<T, VB : ViewBinding>(
    private val messages: ArrayList<T>,
    private val itemBinders: Map<MessageTypeUi, MessageItemBinder<T, out VB>>,
    private val defaultBinder: MessageItemBinder<T, out VB>,
    public var isMessagingDisabled: Boolean,
    private val joiningAsObserver: Boolean,
    private val messageActionCallback: MessageActionCallback
) : RecyclerView.Adapter<ConversationMessagesAdapter<T, VB>.MessageViewHolder>() {

    private val customMessageTypeMap = mutableMapOf<String, Int>()
    private var nextCustomTypeId = MessageTypeUi.values().size + 10
    inner class MessageViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    //private final float thumbnailSizeMultiplier = Constants.THUMBNAIL_SIZE_MULTIPLIER;
    private var multipleMessagesSelectModeOn = false

    /**
     * Is messaging disabled boolean.
     *
     * @return the boolean
     */

    interface OnScrollToMessageListener {
        fun onScrollToParentMessage(messageId: String?)
    }

    /**
     * Get the appropriate binder for a message type.
     * @param messageType The message type
     * @param customType The custom type value (if applicable)
     * @return The registered binder or default binder if not found
     */
    @Suppress("UNCHECKED_CAST")
    private fun getBinder(messageType: MessageTypeUi, customType: String? = null): MessageItemBinder<T, VB> {
        log("ChatSDK:", "getBinder() messageType: $messageType, customType: $customType")
        
        // First check for custom message type binders
        if (messageType == MessageTypeUi.CUSTOM_MESSAGE_SENT || messageType == MessageTypeUi.CUSTOM_MESSAGE_RECEIVED) {
            customType?.let {
                val customBinder = CustomMessageTypes.getCustomBinder(it, messageType == MessageTypeUi.CUSTOM_MESSAGE_SENT)
                if (customBinder != null) {
                    return customBinder as MessageItemBinder<T, VB>
                }
            }
        }
        
        // Then check for registered binders
        val registeredBinder = MessageBinderRegistry.getBinder(messageType, customType)
        if (registeredBinder != null) {
            return registeredBinder as MessageItemBinder<T, VB>
        }
        
        // Finally check itemBinders map
        return (itemBinders[messageType] ?: defaultBinder) as MessageItemBinder<T, VB>
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return (message as? MessagesModel)?.let { messagesModel ->
            log("ChatSDK:", "getItemViewType() messagesModel.messageTypeUi: ${messagesModel.messageTypeUi}")

            when (messagesModel.messageTypeUi) {
                MessageTypeUi.CUSTOM_MESSAGE_SENT, MessageTypeUi.CUSTOM_MESSAGE_RECEIVED -> {
                    log("ChatSDK:", "getItemViewType() messagesModel.dynamicCustomType: ${messagesModel.dynamicCustomType}")

                    messagesModel.dynamicCustomType?.let { customType ->
                        // Get or create a unique ID for this custom type
                        customMessageTypeMap.getOrPut(customType) { nextCustomTypeId++ }
                    } ?: messagesModel.messageTypeUi.value
                }
                else -> messagesModel.messageTypeUi.value
            }
        } ?: 20 // conversation type
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        log("ChatSDK:", "onCreateViewHolder $viewType  customMessageTypeMap.size: ${customMessageTypeMap.size}")

        val (messageType, customType) = if (viewType > MessageTypeUi.values().size) {
            // This is a custom message type
            val customType = customMessageTypeMap.entries.find { it.value == viewType }?.key
            val baseType = if (customType?.contains("sent") == true) {
                MessageTypeUi.CUSTOM_MESSAGE_SENT
            } else {
                MessageTypeUi.CUSTOM_MESSAGE_RECEIVED
            }
            Pair(baseType, customType)
        } else {
            Pair(MessageTypeUi.fromValue(viewType), null)
        }

        val binder = getBinder(messageType, customType)
        val binding = binder.createBinding(parent, viewType)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        val viewType = getItemViewType(position)
        log("ChatSDK:", "onBindViewHolder $viewType")
        log("ChatSDK:", "onBindViewHolder MessageTypeUi.values().size ${MessageTypeUi.values().size}")

        val (messageType, customType) = if (viewType > MessageTypeUi.values().size) {
            // This is a custom message type
            val customType = customMessageTypeMap.entries.find { it.value == viewType }?.key
            val baseType = if (customType?.contains("sent") == true) {
                MessageTypeUi.CUSTOM_MESSAGE_SENT
            } else {
                MessageTypeUi.CUSTOM_MESSAGE_RECEIVED
            }
            Pair(baseType, customType)
        } else {
            Pair(MessageTypeUi.fromValue(viewType), null)
        }
        val binder = getBinder(messageType, customType)

        log("ChatSDK:", "onBindViewHolder found $binder")

        try {
            binder.bindData(
                holder.itemView.context,
                holder.binding,
                message,
                position,
                multipleMessagesSelectModeOn,
                isMessagingDisabled,
                messageActionCallback
            )
        } catch (e: Exception) {
            log("ChatSDK:", "onBindViewHolder Exception: $e")
        }
        log("ChatSDK:", "onBindViewHolder finish ")
    }


    /**
     * Update message status on media download finished.
     *
     * @param position the position
     * @param successfullyCompleted the successfully completed
     * @param downloadedMediaPath the downloaded media path
     */
    fun updateMessageStatusOnMediaDownloadFinished(
        position: Int,
        successfullyCompleted: Boolean, downloadedMediaPath: String?
    ) {
        val item = messages[position]
        if (item is MessagesModel) {
            item.isDownloaded = successfullyCompleted
            item.isDownloading = false
            if (successfullyCompleted) {
                item.downloadedMediaPath = downloadedMediaPath
            }
            notifyItemChanged(position)
        }
    }

    /**
     * Update message status on downloading state changed.
     *
     * @param position the position
     * @param downloadingStarted the downloading started
     */
    fun updateMessageStatusOnDownloadingStateChanged(
        position: Int,
        downloadingStarted: Boolean
    ) {
        val item = messages[position]
        if (item is MessagesModel) {
            item.isDownloading = downloadingStarted
            notifyItemChanged(position)
        }
    }

    /**
     * Update progress status of message.
     *
     * @param download the download
     * @param position the position
     * @param rvMessages the rv messages
     * @param progress the progress
     */
    fun updateProgressStatusOfMessage(
        download: Boolean, position: Int, rvMessages: RecyclerView,
        progress: Int
    ) {
        try {
            val item = messages[position]
            if (item is MessagesModel) {
                when (item.messageTypeUi) {
                    MessageTypeUi.PHOTO_MESSAGE_SENT -> {
                        if (download) {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as PhotoMessageSentViewHolder).ismSentMessagePhotoBinding.pbDownload.setProgressCompat(
                                progress,
                                true
                            )


                        } else {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as PhotoMessageSentViewHolder).ismSentMessagePhotoBinding.pbUpload.setProgressCompat(
                                progress,
                                true
                            )


                        }
                    }

                    MessageTypeUi.PHOTO_MESSAGE_RECEIVED -> {
                        (rvMessages.findViewHolderForAdapterPosition(
                            position
                        ) as PhotoMessageReceivedViewHolder).ismReceivedMessagePhotoBinding.pbDownload.setProgressCompat(
                            progress,
                            true
                        )
                    }

                    MessageTypeUi.VIDEO_MESSAGE_SENT -> {
                        if (download) {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as VideoMessageSentViewHolder).ismSentMessageVideoBinding.pbDownload.setProgressCompat(
                                progress,
                                true
                            )

                        } else {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as VideoMessageSentViewHolder).ismSentMessageVideoBinding.pbUpload.setProgressCompat(
                                progress,
                                true
                            )

                        }
                    }

                    MessageTypeUi.VIDEO_MESSAGE_RECEIVED -> {
                        (rvMessages.findViewHolderForAdapterPosition(
                            position
                        ) as VideoMessageReceivedViewHolder).ismReceivedMessageVideoBinding.pbDownload.setProgressCompat(
                            progress,
                            true
                        )
                    }

                    MessageTypeUi.AUDIO_MESSAGE_SENT -> {
                        if (download) {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as AudioMessageSentViewHolder).ismSentMessageAudioBinding.pbDownload.setProgressCompat(
                                progress,
                                true
                            )

                        } else {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as AudioMessageSentViewHolder).ismSentMessageAudioBinding.pbUpload.setProgressCompat(
                                progress,
                                true
                            )

                        }
                    }

                    MessageTypeUi.AUDIO_MESSAGE_RECEIVED -> {
                        (rvMessages.findViewHolderForAdapterPosition(
                            position
                        ) as AudioMessageReceivedViewHolder).ismReceivedMessageAudioBinding.pbDownload.setProgressCompat(
                            progress,
                            true
                        )
                    }

                    MessageTypeUi.FILE_MESSAGE_SENT -> {
                        if (download) {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as FileMessageSentViewHolder).ismSentMessageFileBinding.pbDownload.setProgressCompat(
                                progress,
                                true
                            )

                        } else {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as FileMessageSentViewHolder).ismSentMessageFileBinding.pbUpload.setProgressCompat(
                                progress,
                                true
                            )

                        }
                    }

                    MessageTypeUi.FILE_MESSAGE_RECEIVED -> {
                        (rvMessages.findViewHolderForAdapterPosition(
                            position
                        ) as FileMessageReceivedViewHolder).ismReceivedMessageFileBinding.pbDownload.setProgressCompat(
                            progress,
                            true
                        )
                    }

                    MessageTypeUi.WHITEBOARD_MESSAGE_SENT -> {
                        if (download) {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as WhiteboardMessageSentViewHolder).ismSentMessageWhiteboardBinding.pbDownload.setProgressCompat(
                                progress,
                                true
                            )

                        } else {
                            (rvMessages.findViewHolderForAdapterPosition(
                                position
                            ) as WhiteboardMessageSentViewHolder).ismSentMessageWhiteboardBinding.pbUpload.setProgressCompat(
                                progress,
                                true
                            )

                        }
                    }

                    MessageTypeUi.WHITEBOARD_MESSAGE_RECEIVED -> {
                        (rvMessages.findViewHolderForAdapterPosition(
                            position
                        ) as WhiteboardMessageReceivedViewHolder).ismReceivedMessageWhiteboardBinding.pbDownload.setProgressCompat(
                            progress,
                            true
                        )
                    }

                    else -> {}
                }
            }
        } catch (ignore: Exception) {
        }
    }

    /**
     * Sets multiple messages select mode on.
     *
     * @param multipleMessagesSelectModeOn the multiple messages select mode on
     */
    fun setMultipleMessagesSelectModeOn(multipleMessagesSelectModeOn: Boolean) {
        this.multipleMessagesSelectModeOn = multipleMessagesSelectModeOn
    }

}