package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessageFileBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class FileSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageFileBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageFileBinding {
        return IsmSentMessageFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessageFileBinding: IsmSentMessageFileBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {

            ismSentMessageFileBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessageFileBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessageFileBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessageFileBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessageFileBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageFileBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessageFileBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageFileBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageFileBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessageFileBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageFileBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessageFileBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }

            if (message.isForwardedMessage) {
                ismSentMessageFileBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageFileBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismSentMessageFileBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessageFileBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageFileBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessageFileBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessageFileBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessageFileBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
//                ismSentMessageFileBinding.ivForward.visibility =
//                    View.GONE
            } else {
                ismSentMessageFileBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
//                    ismSentMessageFileBinding.ivForward.visibility =
//                        View.VISIBLE
                    ismSentMessageFileBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
//                    ismSentMessageFileBinding.ivForward.visibility =
//                        View.GONE
                }
            }
            if (isMessagingDisabled) {
//                ismSentMessageFileBinding.ivForward.visibility =
//                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessageFileBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageFileBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessageFileBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessageFileBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessageFileBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageFileBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageFileBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessageFileBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessageFileBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageFileBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessageFileBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessageFileBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismSentMessageFileBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageFileBinding.rvMessageReactions.visibility =
                    View.GONE
            }

            ismSentMessageFileBinding.tvMessageTime.text =
                message.messageTime

            if (message.isUploaded) {
                ismSentMessageFileBinding.rlUpload.visibility =
                    View.GONE
                if (message.isDownloaded || message.isDownloading) {
                    if (message.isDownloaded) {
                        ismSentMessageFileBinding.tvDownloadFileStatus.text =
                            mContext.getString(R.string.ism_open)
                        ismSentMessageFileBinding.pbDownload.visibility =
                            View.GONE
                    } else {
                        ismSentMessageFileBinding.tvDownloadFileStatus.text =
                            mContext.getString(R.string.ism_attachments_cancel)
                        ismSentMessageFileBinding.pbDownload.visibility =
                            View.VISIBLE
                    }

                    ismSentMessageFileBinding.rlDownload.setOnClickListener { v: View? ->
                        if (message.isDownloading) {
                            messageActionCallback.cancelMediaDownload(message, position)
                        } else {
                            messageActionCallback.handleClickOnMessageCell(message, true)
                        }
                    }
                    ismSentMessageFileBinding.rlDownload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessageFileBinding.rlDownload.visibility =
                        View.GONE
                }
            } else {
                ismSentMessageFileBinding.rlDownload.visibility =
                    View.GONE

                if (message.isUploading) {
                    ismSentMessageFileBinding.tvFileStatus.text =
                        mContext.getString(R.string.ism_attachments_cancel)
                    ismSentMessageFileBinding.pbUpload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessageFileBinding.tvFileStatus.text =
                        mContext.getString(R.string.ism_remove)
                    ismSentMessageFileBinding.pbUpload.visibility =
                        View.GONE
                }
                ismSentMessageFileBinding.rlUpload.setOnClickListener { v: View? ->
                    if (message.isUploading) {
                        messageActionCallback.cancelMediaUpload(message, position)
                    } else {
                        messageActionCallback.removeCanceledMessage(
                            message.localMessageId,
                            position
                        )
                    }
                }
                ismSentMessageFileBinding.rlUpload.visibility =
                    View.VISIBLE
            }
            ismSentMessageFileBinding.tvFileName.text =
                message.fileName
            ismSentMessageFileBinding.tvFileSize.text =
                message.mediaSizeInMB
            ismSentMessageFileBinding.rlFile.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    message.isDownloaded
                )
            }
        } catch (ignore: Exception) {
        }
    }
}