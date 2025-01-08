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
import io.isometrik.chat.databinding.IsmSentMessageLocationBinding
import io.isometrik.chat.utils.Constants
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class LocationSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageLocationBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageLocationBinding {
        return IsmSentMessageLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessageLocationBinding: IsmSentMessageLocationBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            val cornerRadius = (13 * mContext.resources.displayMetrics.density).toInt()

            ismSentMessageLocationBinding.ivEdited.setVisibility(
                if (message.isEditedMessage) View.VISIBLE else View.GONE
            )

            if (message.isReadByAll) {
                ismSentMessageLocationBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_circle)
                )

                ismSentMessageLocationBinding.ivDeliveryReadStatus.setVisibility(
                    View.VISIBLE
                )
            } else if (message.isDeliveredToAll) {
                ismSentMessageLocationBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_circle)
                )

                ismSentMessageLocationBinding.ivDeliveryReadStatus.setVisibility(
                    View.VISIBLE
                )
            } else {
                ismSentMessageLocationBinding.ivDeliveryReadStatus.setVisibility(
                    View.GONE
                )
            }

            try {
                Glide.with(mContext)
                    .load(Constants.LOCATION_PLACEHOLDER_IMAGE_URL)
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
                    .into(ismSentMessageLocationBinding.ivLocationImage)
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }
            if (message.isMessageSentSuccessfully) {
                ismSentMessageLocationBinding.tvSendingMessage.setVisibility(
                    View.GONE
                )
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageLocationBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageLocationBinding.tvSendingMessage.setText(
                        mContext.getString(R.string.ism_failed)
                    )
                } else {
                    ismSentMessageLocationBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageLocationBinding.tvSendingMessage.setText(
                        mContext.getString(R.string.ism_sending)
                    )
                }
                ismSentMessageLocationBinding.tvSendingMessage.setVisibility(
                    View.VISIBLE
                )
            }
            if (message.isForwardedMessage) {
                ismSentMessageLocationBinding.vForwardedMessage.getRoot()
                    .setVisibility(View.VISIBLE)

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageLocationBinding.vForwardedMessageNotes.getRoot()
                        .setVisibility(View.GONE)
                } else {
                    ismSentMessageLocationBinding.vForwardedMessageNotes.tvMessage
                        .setText(message.forwardedMessageNotes)

                    ismSentMessageLocationBinding.vForwardedMessageNotes.getRoot()
                        .setVisibility(View.VISIBLE)
                }
            } else {
                ismSentMessageLocationBinding.vForwardedMessage.getRoot()
                    .setVisibility(View.GONE)
                ismSentMessageLocationBinding.vForwardedMessageNotes.getRoot()
                    .setVisibility(View.GONE)
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessageLocationBinding.ivSelectedStatus.setSelected(
                    message.isSelected
                )
                ismSentMessageLocationBinding.ivSelectedStatus.setVisibility(
                    View.VISIBLE
                )
                ismSentMessageLocationBinding.ivForward.setVisibility(
                    View.GONE
                )
            } else {
                ismSentMessageLocationBinding.ivSelectedStatus.setVisibility(
                    View.GONE
                )
                if (message.isMessageSentSuccessfully) {
                    ismSentMessageLocationBinding.ivForward.setVisibility(
                        View.VISIBLE
                    )
                    ismSentMessageLocationBinding.ivForward.setOnClickListener { v ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessageLocationBinding.ivForward.setVisibility(
                        View.GONE
                    )
                }
            }
            if (/*messagingDisabled*/false) { // not required
                ismSentMessageLocationBinding.ivForward.setVisibility(
                    View.GONE
                )
            }
            if (message.isQuotedMessage) {
                ismSentMessageLocationBinding.vParentMessage.getRoot()
                    .setVisibility(View.VISIBLE)
                ismSentMessageLocationBinding.vParentMessage.tvSenderName.setText(
                    message.originalMessageSenderName
                )
                ismSentMessageLocationBinding.vParentMessage.tvMessage.setText(
                    message.originalMessage
                )
                ismSentMessageLocationBinding.vParentMessage.tvMessageTime.setText(
                    message.originalMessageTime
                )
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessageLocationBinding.vParentMessage.ivMessageImage
                        .setVisibility(View.GONE)
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageLocationBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageLocationBinding.vParentMessage.ivMessageImage
                        .setVisibility(View.VISIBLE)
                }
            } else {
                ismSentMessageLocationBinding.vParentMessage.getRoot()
                    .setVisibility(View.GONE)
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageLocationBinding.rvMessageReactions.setVisibility(
                        View.GONE
                    )
                } else {
                    ismSentMessageLocationBinding.rvMessageReactions.setLayoutManager(
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    )
                    ismSentMessageLocationBinding.rvMessageReactions.setAdapter(
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )
                    )

                    ismSentMessageLocationBinding.rvMessageReactions.setVisibility(
                        View.VISIBLE
                    )
                }
            } else {
                ismSentMessageLocationBinding.rvMessageReactions.setVisibility(
                    View.GONE
                )
            }

            ismSentMessageLocationBinding.tvLocationName.setText(
                message.locationName
            )
            ismSentMessageLocationBinding.tvLocationDescription.setText(
                message.locationDescription
            )

            ismSentMessageLocationBinding.tvMessageTime.setText(
                message.messageTime
            )

            ismSentMessageLocationBinding.rlLocationLayout.setOnClickListener { v ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    false
                )
            }
        } catch (ignore: Exception) {
        }
    }


}