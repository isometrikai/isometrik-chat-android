package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessagePostBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter
import io.isometrik.ui.utils.TimeUtil

class PostSentBinder : MessageItemBinder<MessagesModel, IsmSentMessagePostBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessagePostBinding {
        return IsmSentMessagePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessagePostBinding: IsmSentMessagePostBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            val cornerRadius = (13 * mContext.resources.displayMetrics.density).toInt()

            ismSentMessagePostBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessagePostBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessagePostBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessagePostBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessagePostBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessagePostBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessagePostBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessagePostBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessagePostBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessagePostBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessagePostBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessagePostBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }
            if (message.isForwardedMessage) {
                ismSentMessagePostBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessagePostBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismSentMessagePostBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessagePostBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessagePostBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessagePostBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessagePostBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessagePostBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismSentMessagePostBinding.ivForward.visibility =
                    View.GONE
            } else {
                if (message.isMessageSentSuccessfully) {
                    ismSentMessagePostBinding.ivForward.visibility =
                        View.GONE
                    ismSentMessagePostBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessagePostBinding.ivForward.visibility =
                        View.GONE
                }
            }
            if (isMessagingDisabled) {
                ismSentMessagePostBinding.ivForward.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessagePostBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessagePostBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessagePostBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessagePostBinding.vParentMessage.tvMessageTime.text =
                    TimeUtil.convert24to12hourformat(message.originalMessageTime)
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessagePostBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessagePostBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessagePostBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessagePostBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessagePostBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessagePostBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessagePostBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessagePostBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )
                    ismSentMessagePostBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessagePostBinding.rvMessageReactions.visibility =
                    View.GONE
            }

            ismSentMessagePostBinding.tvMessageTime.text =
                TimeUtil.convert24to12hourformat(message.messageTime)

            if (message.isUploaded) {
                ismSentMessagePostBinding.rlUpload.visibility =
                    View.GONE

                if (message.isDownloaded || message.isDownloading) {
                    try {
                        Glide.with(mContext)
                            .load(message.photoMainUrl)
                            .thumbnail(Glide.with(mContext).load(message.photoThumbnailUrl))
                            .placeholder(R.drawable.ism_avatar_group_large)
                            .transform(CenterCrop())
                            .into(ismSentMessagePostBinding.ivPhoto)
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                } else {
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
                                .into(ismSentMessagePostBinding.ivPhoto)
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
                                .into(ismSentMessagePostBinding.ivPhoto)
                        }
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                }
            } else {
                try {
                    Glide.with(mContext)
                        .load(message.photoMainUrl)
                        .thumbnail(Glide.with(mContext).load(message.photoThumbnailUrl))
                        .placeholder(R.drawable.ism_avatar_group_large)
                        .transform(CenterCrop())
                        .into(ismSentMessagePostBinding.ivPhoto)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
                ismSentMessagePostBinding.rlUpload.setOnClickListener { v: View? ->
                    if (message.isUploading) {
                        messageActionCallback.cancelMediaUpload(message, position)
                    } else {
                        messageActionCallback.removeCanceledMessage(
                            message.localMessageId,
                            position
                        )
                    }
                }
                ismSentMessagePostBinding.rlUpload.visibility =
                    View.VISIBLE
            }

            ismSentMessagePostBinding.rlPhoto.setOnClickListener { view: View? ->
                IsometrikChatSdk.getInstance().chatActionsClickListener.onSharedPostClick(
                    message.messageId
                )
            }
        } catch (ignore: Exception) {
        }
    }
}