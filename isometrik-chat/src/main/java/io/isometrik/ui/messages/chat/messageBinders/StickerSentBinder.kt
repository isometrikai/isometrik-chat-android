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
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessageStickerBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class StickerSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageStickerBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageStickerBinding {
        return IsmSentMessageStickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessageStickerBinding: IsmSentMessageStickerBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            val cornerRadius = (13 * mContext.resources.displayMetrics.density).toInt()

            ismSentMessageStickerBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessageStickerBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessageStickerBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessageStickerBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessageStickerBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageStickerBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessageStickerBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageStickerBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageStickerBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessageStickerBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageStickerBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessageStickerBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }
            if (message.isForwardedMessage) {
                ismSentMessageStickerBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageStickerBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismSentMessageStickerBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessageStickerBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageStickerBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessageStickerBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessageStickerBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessageStickerBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismSentMessageStickerBinding.ivForward.visibility =
                    View.GONE
            } else {
                ismSentMessageStickerBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
//                    ismSentMessageStickerBinding.ivForward.visibility =
//                        View.VISIBLE
                    ismSentMessageStickerBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessageStickerBinding.ivForward.visibility =
                        View.GONE
                }
            }
            if (isMessagingDisabled) {
                ismSentMessageStickerBinding.ivForward.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessageStickerBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageStickerBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessageStickerBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessageStickerBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessageStickerBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageStickerBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageStickerBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessageStickerBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessageStickerBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageStickerBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessageStickerBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessageStickerBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismSentMessageStickerBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageStickerBinding.rvMessageReactions.visibility =
                    View.GONE
            }
            try {
                Glide.with(mContext)
                    .load(message.stickerMainUrl)
                    .thumbnail(Glide.with(mContext).load(message.stickerStillUrl))
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(
                        GranularRoundedCorners(
                            0f,
                            0f,
                            cornerRadius.toFloat(),
                            cornerRadius.toFloat()
                        )
                    )
                    .into(ismSentMessageStickerBinding.ivStickerImage)
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }
            ismSentMessageStickerBinding.tvMessageTime.text =
                message.messageTime

            ismSentMessageStickerBinding.rlSticker.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    false
                )
            }
        } catch (ignore: Exception) {
        }
    }
}