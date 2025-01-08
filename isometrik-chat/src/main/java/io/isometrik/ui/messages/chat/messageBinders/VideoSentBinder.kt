package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessageVideoBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class VideoSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageVideoBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageVideoBinding {
        return IsmSentMessageVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessageVideoBinding: IsmSentMessageVideoBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            val cornerRadius = (13 * mContext.resources.displayMetrics.density).toInt()

            ismSentMessageVideoBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessageVideoBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessageVideoBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessageVideoBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessageVideoBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageVideoBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessageVideoBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageVideoBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageVideoBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessageVideoBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageVideoBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessageVideoBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }
            if (message.isForwardedMessage) {
                ismSentMessageVideoBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageVideoBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismSentMessageVideoBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessageVideoBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageVideoBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessageVideoBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessageVideoBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessageVideoBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismSentMessageVideoBinding.ivForward.visibility =
                    View.GONE
            } else {
                ismSentMessageVideoBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
                    ismSentMessageVideoBinding.ivForward.visibility =
                        View.VISIBLE
                    ismSentMessageVideoBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessageVideoBinding.ivForward.visibility =
                        View.GONE
                }
            }
            if (isMessagingDisabled) {
                ismSentMessageVideoBinding.ivForward.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessageVideoBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageVideoBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessageVideoBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessageVideoBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessageVideoBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageVideoBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageVideoBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessageVideoBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessageVideoBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageVideoBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessageVideoBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessageVideoBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismSentMessageVideoBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageVideoBinding.rvMessageReactions.visibility =
                    View.GONE
            }

            ismSentMessageVideoBinding.tvMessageTime.text =
                message.messageTime

            if (message.isUploaded) {
                ismSentMessageVideoBinding.rlUpload.visibility =
                    View.GONE

                if (message.isDownloaded || message.isDownloading) {
                    if (message.isDownloaded) {
                        ismSentMessageVideoBinding.tvDownloadVideoStatus.text =
                            mContext.getString(R.string.ism_open)
                        ismSentMessageVideoBinding.pbDownload.visibility =
                            View.GONE
                    } else {
                        ismSentMessageVideoBinding.tvDownloadVideoStatus.text =
                            mContext.getString(R.string.ism_attachments_cancel)
                        ismSentMessageVideoBinding.pbDownload.visibility =
                            View.VISIBLE
                    }

                    try {
                        Glide.with(mContext)
                            .load(message.videoMainUrl)
                            .thumbnail(Glide.with(mContext).load(message.videoThumbnailUrl))
                            .placeholder(R.drawable.ism_avatar_group_large)
                            .transform(CenterCrop())
                            .into(ismSentMessageVideoBinding.ivVideoThumbnail)
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }

                    ismSentMessageVideoBinding.rlDownload.setOnClickListener { v: View? ->
                        if (message.isDownloading) {
                            messageActionCallback.cancelMediaDownload(message, position)
                        } else {
                            messageActionCallback.handleClickOnMessageCell(message, true)
                        }
                    }
                    ismSentMessageVideoBinding.rlDownload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessageVideoBinding.rlDownload.visibility =
                        View.GONE
                    try {
                        if (message.localMediaPath == null) {
                            Glide.with(mContext)
                                .load(message.videoMainUrl)
                                .thumbnail(Glide.with(mContext).load(message.videoThumbnailUrl))
                                .placeholder(R.drawable.ism_avatar_group_large)
                                .transform(
                                    CenterCrop(),
                                    GranularRoundedCorners(
                                        0f,
                                        0f,
                                        cornerRadius.toFloat(),
                                        cornerRadius.toFloat()
                                    )
                                )
                                .into(ismSentMessageVideoBinding.ivVideoThumbnail)
                        } else {
                            Glide.with(mContext)
                                .load(message.localMediaPath)
                                .thumbnail(Glide.with(mContext).load(message.localMediaPath))
                                .placeholder(R.drawable.ism_avatar_group_large)
                                .transform(
                                    CenterCrop(),
                                    GranularRoundedCorners(
                                        0f,
                                        0f,
                                        cornerRadius.toFloat(),
                                        cornerRadius.toFloat()
                                    )
                                )
                                .into(ismSentMessageVideoBinding.ivVideoThumbnail)
                        }
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                }
            } else {
                ismSentMessageVideoBinding.rlDownload.visibility =
                    View.GONE
                try {
                    Glide.with(mContext)
                        .load(message.videoMainUrl)
                        .thumbnail(Glide.with(mContext).load(message.videoThumbnailUrl))
                        .placeholder(R.drawable.ism_avatar_group_large)
                        .transform(CenterCrop())
                        .into(ismSentMessageVideoBinding.ivVideoThumbnail)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
                if (message.isUploading) {
                    ismSentMessageVideoBinding.tvVideoStatus.text =
                        mContext.getString(R.string.ism_attachments_cancel)
                    ismSentMessageVideoBinding.pbUpload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessageVideoBinding.tvVideoStatus.text =
                        mContext.getString(R.string.ism_remove)
                    ismSentMessageVideoBinding.pbUpload.visibility =
                        View.GONE
                }
                ismSentMessageVideoBinding.rlUpload.setOnClickListener { v: View? ->
                    if (message.isUploading) {
                        messageActionCallback.cancelMediaUpload(message, position)
                    } else {
                        messageActionCallback.removeCanceledMessage(
                            message.localMessageId,
                            position
                        )
                    }
                }
                ismSentMessageVideoBinding.rlUpload.visibility =
                    View.VISIBLE
            }

            ismSentMessageVideoBinding.tvVideoSize.text =
                message.mediaSizeInMB

            ismSentMessageVideoBinding.rlVideo.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    message.isDownloaded
                )
            }
        } catch (ignore: Exception) {
        }
    }
}