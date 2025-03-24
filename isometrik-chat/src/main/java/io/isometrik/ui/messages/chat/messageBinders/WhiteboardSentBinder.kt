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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessageWhiteboardBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class WhiteboardSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageWhiteboardBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageWhiteboardBinding {
        return IsmSentMessageWhiteboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessageWhiteboardBinding: IsmSentMessageWhiteboardBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            val cornerRadius = (13 * mContext.resources.displayMetrics.density).toInt()

            ismSentMessageWhiteboardBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageWhiteboardBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessageWhiteboardBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageWhiteboardBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageWhiteboardBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessageWhiteboardBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageWhiteboardBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessageWhiteboardBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }
            if (message.isForwardedMessage) {
                ismSentMessageWhiteboardBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageWhiteboardBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismSentMessageWhiteboardBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessageWhiteboardBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageWhiteboardBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessageWhiteboardBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessageWhiteboardBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessageWhiteboardBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismSentMessageWhiteboardBinding.ivForward.visibility =
                    View.GONE
            } else {
                ismSentMessageWhiteboardBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
//                    ismSentMessageWhiteboardBinding.ivForward.visibility =
//                        View.VISIBLE
                    ismSentMessageWhiteboardBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessageWhiteboardBinding.ivForward.visibility =
                        View.GONE
                }
            }
            if (isMessagingDisabled) {
                ismSentMessageWhiteboardBinding.ivForward.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessageWhiteboardBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageWhiteboardBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessageWhiteboardBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessageWhiteboardBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessageWhiteboardBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageWhiteboardBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageWhiteboardBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageWhiteboardBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageWhiteboardBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessageWhiteboardBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessageWhiteboardBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismSentMessageWhiteboardBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageWhiteboardBinding.rvMessageReactions.visibility =
                    View.GONE
            }

            if (message.isUploaded) {
                ismSentMessageWhiteboardBinding.rlUpload.visibility =
                    View.GONE
                if (message.isDownloaded || message.isDownloading) {
                    if (message.isDownloaded) {
                        ismSentMessageWhiteboardBinding.tvDownloadWhiteboardStatus.text =
                            mContext.getString(R.string.ism_open)
                        ismSentMessageWhiteboardBinding.pbDownload.visibility =
                            View.GONE
                    } else {
                        ismSentMessageWhiteboardBinding.tvDownloadWhiteboardStatus.text =
                            mContext.getString(R.string.ism_attachments_cancel)
                        ismSentMessageWhiteboardBinding.pbDownload.visibility =
                            View.VISIBLE
                    }

                    try {
                        Glide.with(mContext)
                            .load(message.whiteboardMainUrl)
                            .thumbnail(
                                Glide.with(mContext).load(message.whiteboardThumbnailUrl)
                            )
                            .placeholder(R.drawable.ism_avatar_group_large)
                            .transform(CenterCrop())
                            .into(
                                ismSentMessageWhiteboardBinding.ivWhiteboard
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }

                    ismSentMessageWhiteboardBinding.rlDownload.setOnClickListener { v: View? ->
                        if (message.isDownloading) {
                            messageActionCallback.cancelMediaDownload(message, position)
                        } else {
                            messageActionCallback.handleClickOnMessageCell(message, true)
                        }
                    }
                    ismSentMessageWhiteboardBinding.rlDownload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessageWhiteboardBinding.rlDownload.visibility =
                        View.GONE

                    try {
                        if (message.localMediaPath == null) {
                            Glide.with(mContext)
                                .load(message.whiteboardMainUrl)
                                .thumbnail(
                                    Glide.with(mContext).load(message.whiteboardThumbnailUrl)
                                )
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
                                .into(
                                    ismSentMessageWhiteboardBinding.ivWhiteboard
                                )
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
                                .into(
                                    ismSentMessageWhiteboardBinding.ivWhiteboard
                                )
                        }
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                }
            } else {
                ismSentMessageWhiteboardBinding.rlDownload.visibility =
                    View.GONE
                try {
                    Glide.with(mContext)
                        .load(message.whiteboardMainUrl)
                        .thumbnail(Glide.with(mContext).load(message.whiteboardThumbnailUrl))
                        .placeholder(R.drawable.ism_avatar_group_large)
                        .transform(CenterCrop())
                        .into(ismSentMessageWhiteboardBinding.ivWhiteboard)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
                if (message.isUploading) {
                    ismSentMessageWhiteboardBinding.tvWhiteboardStatus.text =
                        mContext.getString(R.string.ism_attachments_cancel)
                    ismSentMessageWhiteboardBinding.pbUpload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessageWhiteboardBinding.tvWhiteboardStatus.text =
                        mContext.getString(R.string.ism_remove)
                    ismSentMessageWhiteboardBinding.pbUpload.visibility =
                        View.GONE
                }

                ismSentMessageWhiteboardBinding.rlUpload.setOnClickListener { v: View? ->
                    if (message.isUploading) {
                        messageActionCallback.cancelMediaUpload(message, position)
                    } else {
                        messageActionCallback.removeCanceledMessage(
                            message.localMessageId,
                            position
                        )
                    }
                }

                ismSentMessageWhiteboardBinding.rlUpload.visibility =
                    View.VISIBLE
            }

            ismSentMessageWhiteboardBinding.tvWhiteboardSize.text =
                message.mediaSizeInMB

            ismSentMessageWhiteboardBinding.tvMessageTime.text =
                message.messageTime
            ismSentMessageWhiteboardBinding.rlWhiteboard.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    message.isDownloaded
                )
            }
        } catch (ignore: Exception) {
        }
    }


}