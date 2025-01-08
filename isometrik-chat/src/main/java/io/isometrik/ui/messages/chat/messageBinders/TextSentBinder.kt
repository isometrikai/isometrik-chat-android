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
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessageTextBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class TextSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageTextBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageTextBinding {
        return IsmSentMessageTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessageTextBinding: IsmSentMessageTextBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismSentMessageTextBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessageTextBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessageTextBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessageTextBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessageTextBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageTextBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessageTextBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageTextBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageTextBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessageTextBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageTextBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessageTextBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }

            if (message.isForwardedMessage) {
                ismSentMessageTextBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageTextBinding.dividerForward.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageTextBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                    ismSentMessageTextBinding.dividerForwardNotes.visibility =
                        View.GONE
                } else {
                    ismSentMessageTextBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessageTextBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                    ismSentMessageTextBinding.dividerForwardNotes.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageTextBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessageTextBinding.dividerForward.visibility =
                    View.GONE
                ismSentMessageTextBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
                ismSentMessageTextBinding.dividerForwardNotes.visibility =
                    View.GONE
            }

            if (multipleMessagesSelectModeOn) {
                ismSentMessageTextBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessageTextBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismSentMessageTextBinding.ivForward.visibility =
                    View.GONE
            } else {
                ismSentMessageTextBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
                    ismSentMessageTextBinding.ivForward.visibility =
                        View.VISIBLE
                    ismSentMessageTextBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessageTextBinding.ivForward.visibility =
                        View.GONE
                }
            }
            if (isMessagingDisabled) {
                ismSentMessageTextBinding.ivForward.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessageTextBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageTextBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessageTextBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessageTextBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                ismSentMessageTextBinding.dividerReply.visibility =
                    View.VISIBLE

                if (message.originalMessageAttachmentUrl == null) {
                    ismSentMessageTextBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                    ismSentMessageTextBinding.vParentMessage.tvMessage.visibility =
                        View.VISIBLE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessageAttachmentUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageTextBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageTextBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessageTextBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessageTextBinding.vParentMessage.root.visibility =
                    View.GONE
                ismSentMessageTextBinding.dividerReply.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageTextBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessageTextBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessageTextBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismSentMessageTextBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageTextBinding.rvMessageReactions.visibility =
                    View.GONE
            }

            ismSentMessageTextBinding.tvTextMessage.text =
                message.textMessage
            ismSentMessageTextBinding.tvTextMessage.setMovementMethod(
                LinkMovementMethod.getInstance()
            )
            ismSentMessageTextBinding.tvMessageTime.text =
                message.messageTime
        } catch (ignore: Exception) {
        }
    }
}