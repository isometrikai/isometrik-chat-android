package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessageAudioCallBinding
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class AudioCallSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageAudioCallBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageAudioCallBinding {
        return IsmSentMessageAudioCallBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bindData(
        mContext: Context,
        binding: IsmSentMessageAudioCallBinding,
        message: MessagesModel,
        position: Int,
        multipleMessagesSelectModeOn: Boolean,
        isMessagingDisabled: Boolean,
        messageActionCallback: MessageActionCallback
    ) {
        try {
            binding.ivEdited.visibility = if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                binding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )
                binding.ivDeliveryReadStatus.visibility = View.VISIBLE
            } else if (message.isDeliveredToAll) {
                binding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_delivered_doubletick)
                )
                binding.ivDeliveryReadStatus.visibility = View.VISIBLE
            } else {
                binding.ivDeliveryReadStatus.visibility = View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                binding.tvSendingMessage.visibility = View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    binding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    binding.tvSendingMessage.text = mContext.getString(R.string.ism_failed)
                } else {
                    binding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    binding.tvSendingMessage.text = mContext.getString(R.string.ism_sending)
                }
                binding.tvSendingMessage.visibility = View.VISIBLE
            }

            if (message.isForwardedMessage) {
                binding.vForwardedMessage.root.visibility = View.VISIBLE
                if (message.forwardedMessageNotes == null) {
                    binding.vForwardedMessageNotes.root.visibility = View.GONE
                } else {
                    binding.vForwardedMessageNotes.tvMessage.text = message.forwardedMessageNotes
                    binding.vForwardedMessageNotes.root.visibility = View.VISIBLE
                }
            } else {
                binding.vForwardedMessage.root.visibility = View.GONE
                binding.vForwardedMessageNotes.root.visibility = View.GONE
            }

            if (multipleMessagesSelectModeOn) {
                binding.ivSelectedStatus.isSelected = message.isSelected
                binding.ivSelectedStatus.visibility = View.VISIBLE
            } else {
                binding.ivSelectedStatus.visibility = View.GONE
            }

            if (message.isQuotedMessage) {
                binding.vParentMessage.root.visibility = View.VISIBLE
                binding.vParentMessage.tvSenderName.text = message.originalMessageSenderName
                binding.vParentMessage.tvMessage.text = message.originalMessage
                binding.vParentMessage.tvMessageTime.text = message.originalMessageTime
                binding.vParentMessage.ivMessageImage.visibility = View.GONE
            } else {
                binding.vParentMessage.root.visibility = View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    binding.rvMessageReactions.visibility = View.GONE
                } else {
                    binding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    binding.rvMessageReactions.adapter = MessageReactionsAdapter(
                        mContext,
                        message.reactions,
                        message.messageId,
                        messageActionCallback::onMessageReactionClicked
                    )
                    binding.rvMessageReactions.visibility = View.VISIBLE
                }
            } else {
                binding.rvMessageReactions.visibility = View.GONE
            }

            // Set call icon and title based on call status
            val userId = IsometrikChatSdk.instance.userSession?.userId
            val isInitiator = message.initiatorId == userId
            val missedByMembers = message.missedByMembers
            val hasMissedMembers = missedByMembers != null && missedByMembers.length() > 0

            // Get call duration for current user
            var callDurationText = ""
            if (!hasMissedMembers) {
                val callDurations = message.callDurations
                if (callDurations != null && callDurations.length() > 0) {
                    for (i in 0 until callDurations.length()) {
                        try {
                            val durationObj = callDurations.getJSONObject(i)
                            val memberId = durationObj.optString("memberId", "")
                            val durationMs = durationObj.optLong("durationInMilliseconds", 0)
                            if (memberId == userId && durationMs > 0) {
                                callDurationText = formatDuration(durationMs)
                                break
                            }
                        } catch (e: Exception) {
                            // Ignore
                        }
                    }
                }
            }

            // Set title and duration based on iOS logic
            if (isInitiator) {
                // User is initiator
                if (hasMissedMembers) {
                    binding.tvCallTitle.text = "Voice Call"
                    binding.tvCallDuration.text = "No answer"
                } else {
                    binding.tvCallTitle.text = "Voice Call"
                    binding.tvCallDuration.text = callDurationText.ifEmpty { "" }
                }
            } else {
                // User is not initiator (shouldn't happen for sent, but handle it)
                if (hasMissedMembers) {
                    binding.tvCallTitle.text = "Missed voice call"
                    binding.tvCallDuration.text = "No answer"
                } else {
                    binding.tvCallTitle.text = "Voice Call"
                    binding.tvCallDuration.text = callDurationText.ifEmpty { "" }
                }
            }

            binding.tvMessageTime.text = message.messageTime

            // Handle call click
            binding.rlCall.setOnClickListener {
                // You can add callback here to handle call back action
                // messageActionCallback.onCallBackClicked(message)
            }
        } catch (ignore: Exception) {
        }
    }

    private fun formatDuration(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
