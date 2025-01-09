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
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessageContactBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class ContactSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageContactBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageContactBinding {
        return IsmSentMessageContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessageContactBinding: IsmSentMessageContactBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismSentMessageContactBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessageContactBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessageContactBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessageContactBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessageContactBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageContactBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessageContactBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageContactBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageContactBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessageContactBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageContactBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessageContactBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }

            if (message.isForwardedMessage) {
                ismSentMessageContactBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageContactBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismSentMessageContactBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessageContactBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageContactBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessageContactBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessageContactBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessageContactBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismSentMessageContactBinding.ivForward.visibility =
                    View.GONE
            } else {
                ismSentMessageContactBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
                    ismSentMessageContactBinding.ivForward.visibility =
                        View.VISIBLE
                    ismSentMessageContactBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessageContactBinding.ivForward.visibility =
                        View.GONE
                }
            }
            if (isMessagingDisabled) {
                ismSentMessageContactBinding.ivForward.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessageContactBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageContactBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessageContactBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessageContactBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessageContactBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageContactBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageContactBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessageContactBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessageContactBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageContactBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessageContactBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessageContactBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismSentMessageContactBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageContactBinding.rvMessageReactions.visibility =
                    View.GONE
            }
            if (PlaceholderUtils.isValidImageUrl(message.contactImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.contactImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismSentMessageContactBinding.ivContactImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext, message.contactName,
                    ismSentMessageContactBinding.ivContactImage, 12
                )
            }

            if (message.contactList.length() > 1) {
                if (message.contactList.length() == 2) {
                    ismSentMessageContactBinding.tvContactName.text =
                        message.contactName + mContext.getString(R.string.ism_and_1_other_contact)
                } else {
                    ismSentMessageContactBinding.tvContactName.text =
                        message.contactName + mContext.getString(R.string.ism_and) + (message.contactList.length() - 1) + mContext.getString(
                            R.string.ism_other_contacts
                        )
                }

                ismSentMessageContactBinding.tvContactIdentifier.visibility =
                    View.GONE
                ismSentMessageContactBinding.divider.visibility =
                    View.VISIBLE
                ismSentMessageContactBinding.textViewAll.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageContactBinding.tvContactName.text =
                    message.contactName
                ismSentMessageContactBinding.tvContactIdentifier.visibility =
                    View.VISIBLE
                ismSentMessageContactBinding.divider.visibility =
                    View.GONE
                ismSentMessageContactBinding.textViewAll.visibility =
                    View.GONE
            }


            ismSentMessageContactBinding.tvContactIdentifier.text =
                message.contactIdentifier
            ismSentMessageContactBinding.tvMessageTime.text =
                message.messageTime

            ismSentMessageContactBinding.rlContact.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    false
                )
            }
        } catch (ignore: Exception) {
        }
    }
}