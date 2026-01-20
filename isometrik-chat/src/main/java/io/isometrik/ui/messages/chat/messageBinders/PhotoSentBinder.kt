package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.text.SpannableString
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
import io.isometrik.chat.databinding.IsmSentMessagePhotoBinding
import io.isometrik.chat.utils.LinkPreviewUtil
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class PhotoSentBinder : MessageItemBinder<MessagesModel, IsmSentMessagePhotoBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessagePhotoBinding {
        return IsmSentMessagePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessagePhotoBinding: IsmSentMessagePhotoBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            val cornerRadius = (13 * mContext.resources.displayMetrics.density).toInt()

            ismSentMessagePhotoBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessagePhotoBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessagePhotoBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessagePhotoBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessagePhotoBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessagePhotoBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessagePhotoBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessagePhotoBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessagePhotoBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessagePhotoBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessagePhotoBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessagePhotoBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }
            if (message.isForwardedMessage) {
                ismSentMessagePhotoBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessagePhotoBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismSentMessagePhotoBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessagePhotoBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessagePhotoBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessagePhotoBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessagePhotoBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessagePhotoBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismSentMessagePhotoBinding.ivForward.visibility =
                    View.GONE
            } else {
                ismSentMessagePhotoBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
//                    ismSentMessagePhotoBinding.ivForward.visibility =
//                        View.VISIBLE
                    ismSentMessagePhotoBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessagePhotoBinding.ivForward.visibility =
                        View.GONE
                }
            }
            if (isMessagingDisabled) {
                ismSentMessagePhotoBinding.ivForward.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessagePhotoBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessagePhotoBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessagePhotoBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessagePhotoBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessagePhotoBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessagePhotoBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessagePhotoBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessagePhotoBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessagePhotoBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessagePhotoBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessagePhotoBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessagePhotoBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )
                    ismSentMessagePhotoBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessagePhotoBinding.rvMessageReactions.visibility =
                    View.GONE
            }

            ismSentMessagePhotoBinding.tvMessageTime.text =
                message.messageTime

            if (message.isUploaded) {
                ismSentMessagePhotoBinding.rlUpload.visibility =
                    View.GONE

                if (message.isDownloaded || message.isDownloading) {
                    if (message.isDownloaded) {
                        ismSentMessagePhotoBinding.tvDownloadPhotoStatus.text =
                            mContext.getString(R.string.ism_open)
                        ismSentMessagePhotoBinding.pbDownload.visibility =
                            View.GONE
                    } else {
                        ismSentMessagePhotoBinding.tvDownloadPhotoStatus.text =
                            mContext.getString(R.string.ism_attachments_cancel)
                        ismSentMessagePhotoBinding.pbDownload.visibility =
                            View.VISIBLE
                    }

                    try {
                        Glide.with(mContext)
                            .load(message.photoMainUrl)
                            .thumbnail(Glide.with(mContext).load(message.photoThumbnailUrl))
                            .placeholder(R.drawable.ism_avatar_group_large)
                            .transform(CenterCrop())
                            .into(ismSentMessagePhotoBinding.ivPhoto)
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }

                    ismSentMessagePhotoBinding.rlDownload.setOnClickListener { v: View? ->
                        if (message.isDownloading) {
                            messageActionCallback.cancelMediaDownload(message, position)
                        } else {
                            messageActionCallback.handleClickOnMessageCell(message, true)
                        }
                    }
                    ismSentMessagePhotoBinding.rlDownload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessagePhotoBinding.rlDownload.visibility =
                        View.GONE
                    try {
                        if (message.localMediaPath == null) {
                            Glide.with(mContext)
                                .load(message.photoMainUrl)
                                .thumbnail(Glide.with(mContext).load(message.photoThumbnailUrl))
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
                                .into(ismSentMessagePhotoBinding.ivPhoto)
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
                                .into(ismSentMessagePhotoBinding.ivPhoto)
                        }
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                }
            } else {
                ismSentMessagePhotoBinding.rlDownload.visibility =
                    View.GONE
                try {
                    Glide.with(mContext)
                        .load(message.photoMainUrl)
                        .thumbnail(Glide.with(mContext).load(message.photoThumbnailUrl))
                        .placeholder(R.drawable.ism_avatar_group_large)
                        .transform(CenterCrop())
                        .into(ismSentMessagePhotoBinding.ivPhoto)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
                if (message.isUploading) {
                    ismSentMessagePhotoBinding.tvPhotoStatus.text =
                        mContext.getString(R.string.ism_attachments_cancel)
                    ismSentMessagePhotoBinding.pbUpload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessagePhotoBinding.tvPhotoStatus.text =
                        mContext.getString(R.string.ism_remove)
                    ismSentMessagePhotoBinding.pbUpload.visibility =
                        View.GONE
                }
                ismSentMessagePhotoBinding.rlUpload.setOnClickListener { v: View? ->
                    if (message.isUploading) {
                        messageActionCallback.cancelMediaUpload(message, position)
                    } else {
                        messageActionCallback.removeCanceledMessage(
                            message.localMessageId,
                            position
                        )
                    }
                }
                ismSentMessagePhotoBinding.rlUpload.visibility =
                    View.VISIBLE
            }

            ismSentMessagePhotoBinding.tvPhotoSize.text =
                message.mediaSizeInMB

            // Display caption if available
            if (message.textMessage != null && message.textMessage.toString().isNotBlank()) {
                val captionText = message.textMessage.toString()
                val spannableText = SpannableString(captionText)
                LinkPreviewUtil.makeLinksClickable(spannableText)
                ismSentMessagePhotoBinding.tvCaption.text = spannableText
                ismSentMessagePhotoBinding.tvCaption.setMovementMethod(
                    LinkMovementMethod.getInstance()
                )
                ismSentMessagePhotoBinding.tvCaption.visibility = View.VISIBLE
            } else {
                ismSentMessagePhotoBinding.tvCaption.visibility = View.GONE
            }

            ismSentMessagePhotoBinding.rlPhoto.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    message.isDownloaded
                )
            }
        } catch (ignore: Exception) {
        }
    }
}