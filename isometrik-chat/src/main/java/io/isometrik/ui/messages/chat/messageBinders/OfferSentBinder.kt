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
import io.isometrik.chat.databinding.IsmSentMessageOfferBinding
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class OfferSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageOfferBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageOfferBinding {
        return IsmSentMessageOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessageOfferBinding: IsmSentMessageOfferBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismSentMessageOfferBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessageOfferBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessageOfferBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessageOfferBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessageOfferBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageOfferBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessageOfferBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageOfferBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageOfferBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessageOfferBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageOfferBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessageOfferBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }

            if (message.isForwardedMessage) {
                ismSentMessageOfferBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageOfferBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismSentMessageOfferBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessageOfferBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageOfferBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessageOfferBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessageOfferBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessageOfferBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismSentMessageOfferBinding.ivForward.visibility =
                    View.GONE
            } else {
                ismSentMessageOfferBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
//                    ismSentMessageOfferBinding.ivForward.visibility =
//                        View.VISIBLE
                    ismSentMessageOfferBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessageOfferBinding.ivForward.visibility =
                        View.GONE
                }
            }
            if (isMessagingDisabled) {
                ismSentMessageOfferBinding.ivForward.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessageOfferBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageOfferBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessageOfferBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessageOfferBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessageOfferBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageOfferBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageOfferBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessageOfferBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessageOfferBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageOfferBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessageOfferBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessageOfferBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismSentMessageOfferBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageOfferBinding.rvMessageReactions.visibility =
                    View.GONE
            }


            ismSentMessageOfferBinding.tvContactIdentifier.text =
                message.contactIdentifier
            ismSentMessageOfferBinding.tvMessageTime.text =
                message.messageTime

        } catch (ignore: Exception) {
        }
    }
}