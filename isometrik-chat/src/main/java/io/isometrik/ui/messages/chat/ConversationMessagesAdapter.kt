package io.isometrik.ui.messages.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
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

    override fun getItemViewType(position: Int): Int {

        val message = messages[position]
        return (message as? MessagesModel)?.let { messagesModel ->
            return messagesModel.messageTypeUi.value
        } ?: 20 // conversation type

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        log("ChatSDK:", "onCreateViewHolder $viewType")
        val binder = MessageBinderRegistry.getBinder(MessageTypeUi.fromValue(viewType)) as? MessageItemBinder<T, VB>
            ?: itemBinders[MessageTypeUi.fromValue(viewType)] ?: defaultBinder
        val binding = binder.createBinding(parent, viewType)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        val viewType = getItemViewType(position)
        log("ChatSDK:", "onBindViewHolder $viewType")

        val binder =
            (MessageBinderRegistry.getBinder(MessageTypeUi.fromValue(viewType)) ?: itemBinders[MessageTypeUi.fromValue(viewType)] ?: defaultBinder)
                    as? MessageItemBinder<T, VB>
                ?: throw IllegalArgumentException("Invalid binder for viewType: $viewType")

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
        }catch (e : Exception){
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