package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmReceivedMessageVideoCallBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.ConversationMessagesActivity.Companion.conversationId
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class VideoCallReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageVideoCallBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageVideoCallBinding {
        return IsmReceivedMessageVideoCallBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bindData(
        mContext: Context,
        binding: IsmReceivedMessageVideoCallBinding,
        message: MessagesModel,
        position: Int,
        multipleMessagesSelectModeOn: Boolean,
        isMessagingDisabled: Boolean,
        messageActionCallback: MessageActionCallback
    ) {
        try {
            binding.ivEdited.visibility = if (message.isEditedMessage) View.VISIBLE else View.GONE

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
                binding.ivReaction.visibility = View.GONE
            } else {
                binding.ivSelectedStatus.visibility = View.GONE
                binding.ivReaction.visibility = View.VISIBLE
            }

            if (isMessagingDisabled) {
                binding.ivReaction.visibility = View.GONE
            }

            if (message.isQuotedMessage) {
                binding.vParentMessage.root.visibility = View.VISIBLE
                binding.vParentMessage.tvSenderName.text = message.originalMessageSenderName
                binding.vParentMessage.tvMessage.text = message.originalMessage
                binding.vParentMessage.tvMessageTime.text = message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    binding.vParentMessage.ivMessageImage.visibility = View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(binding.vParentMessage.ivMessageImage)
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    binding.vParentMessage.ivMessageImage.visibility = View.VISIBLE
                }
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

            if (message.isSenderDeleted) {
                val spannableString = SpannableString(message.senderName)
                spannableString.setSpan(StyleSpan(Typeface.ITALIC), 0, spannableString.length, 0)
                binding.tvSenderName.text = spannableString
            } else {
                binding.tvSenderName.text = message.senderName
            }

            if (ChatConfig.hideSenderNameInMessageCell) {
                binding.tvSenderName.visibility = View.GONE
                binding.tvComma.visibility = View.GONE
            }

            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(binding.ivSenderImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    binding.ivSenderImage,
                    position,
                    12
                )
            }

            // Set call icon and title based on call status
            val userId = IsometrikChatSdk.instance.userSession?.userId ?: ""
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
                    binding.tvCallTitle.text = "Video Call"
                    binding.tvCallDuration.text = "No answer"
                } else {
                    binding.tvCallTitle.text = "Video Call"
                    binding.tvCallDuration.text = callDurationText.ifEmpty { "" }
                }
            } else {
                // User is not initiator
                if (hasMissedMembers) {
                    binding.tvCallTitle.text = "Missed video call"
                    binding.tvCallDuration.text = "Tap to call back"
                } else {
                    if (callDurationText.isNotEmpty()) {
                        binding.tvCallTitle.text = "Video Call"
                        binding.tvCallDuration.text = callDurationText
                    } else {
                        binding.tvCallTitle.text = "Missed video call"
                        binding.tvCallDuration.text = "Tap to call back"
                    }
                }
            }

            binding.tvMessageTime.text = message.messageTime
            Log.e("VideoCallReceivedBinder", "bindData: "+message.initiatorId)

            binding.ivReaction.setOnClickListener {
                messageActionCallback.addReactionForMessage(message.messageId)
            }

            // Handle call click - could trigger callback to initiate call
            binding.rlCall.setOnClickListener {
                IsometrikChatSdk.instance.chatActionsClickListener?.onCallClicked(
                    mContext,
                    false,
                    message.initiatorId,
                    message.initiatorName!!,
                    message.initiatorName!!,
                    message.initiatorImageUrl!!,
                    conversationId!!
                )
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
