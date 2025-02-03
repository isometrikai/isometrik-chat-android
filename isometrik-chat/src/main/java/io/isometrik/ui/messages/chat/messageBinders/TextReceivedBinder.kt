package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.method.LinkMovementMethod
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
import io.isometrik.chat.databinding.IsmReceivedMessageTextBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class TextReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageTextBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageTextBinding {
        return IsmReceivedMessageTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessageTextBinding: IsmReceivedMessageTextBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismReceivedMessageTextBinding.rlText.setBackgroundResource(ChatConfig.textReceivedBubbleResId)
            ismReceivedMessageTextBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessageTextBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageTextBinding.dividerForward.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageTextBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                    ismReceivedMessageTextBinding.dividerForwardNotes.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageTextBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessageTextBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                    ismReceivedMessageTextBinding.dividerForwardNotes.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageTextBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.dividerForward.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.dividerForwardNotes.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageTextBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessageTextBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessageTextBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessageTextBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.ivReaction.visibility =
                    View.VISIBLE
            }
            if (isMessagingDisabled) {
                ismReceivedMessageTextBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageTextBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageTextBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                if (message.originalMessage == "AttachmentMessage:Gif") {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessage.text =
                        "GIF"
                } else {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessage.text =
                        message.originalMessage
                }
                ismReceivedMessageTextBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                ismReceivedMessageTextBinding.dividerReply.visibility =
                    View.VISIBLE

                if (message.originalMessageTime == null || message.originalMessageTime.isEmpty()) {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessageTime.visibility =
                        View.GONE
                    ismReceivedMessageTextBinding.vParentMessage.tvComma.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessageTime.visibility =
                        View.VISIBLE
                }
                ismReceivedMessageTextBinding.vParentMessage.ivMessageImage.visibility =
                    View.GONE

                if (message.originalMessageAttachmentUrl != null && !message.originalMessageAttachmentUrl.isEmpty()) {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessageAttachmentUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageTextBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageTextBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                    ismReceivedMessageTextBinding.vParentMessage.tvMessage.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessage.visibility =
                        View.VISIBLE
                }
                ismReceivedMessageTextBinding.vParentMessage.root
                    .setOnClickListener { view: View? ->
                        messageActionCallback.onScrollToParentMessage(
                            message.parentMessageId
                        )
                    }
            } else {
                ismReceivedMessageTextBinding.vParentMessage.root.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.dividerReply.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessageTextBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageTextBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessageTextBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessageTextBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageTextBinding.rvMessageReactions.visibility =
                    View.GONE
            }
            if (message.isSenderDeleted) {
                val spannableString = SpannableString(message.senderName)
                spannableString.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    0,
                    spannableString.length,
                    0
                )
                ismReceivedMessageTextBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessageTextBinding.tvSenderName.text =
                    message.senderName
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismReceivedMessageTextBinding.ivSenderImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    ismReceivedMessageTextBinding.ivSenderImage,
                    position,
                    12
                )
            }
            ismReceivedMessageTextBinding.tvMessageTime.text =
                message.messageTime
            ismReceivedMessageTextBinding.tvTextMessage.text =
                message.textMessage
            ismReceivedMessageTextBinding.tvTextMessage.movementMethod =
                LinkMovementMethod.getInstance()
            ismReceivedMessageTextBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/false) { // not required
                ismReceivedMessageTextBinding.ivReaction.visibility =
                    View.GONE
            }
        } catch (ignore: Exception) {
        }
    }
}