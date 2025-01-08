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
import io.isometrik.chat.databinding.IsmReceivedMessageGifBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class GifReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageGifBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageGifBinding {
        return IsmReceivedMessageGifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessageGifBinding: IsmReceivedMessageGifBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            val cornerRadius = (13 * mContext.resources.displayMetrics.density).toInt()

            ismReceivedMessageGifBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessageGifBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageGifBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageGifBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessageGifBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageGifBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessageGifBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageGifBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessageGifBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessageGifBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessageGifBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessageGifBinding.ivReaction.visibility =
                    View.VISIBLE
            }
            if (isMessagingDisabled) {
                ismReceivedMessageGifBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageGifBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageGifBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismReceivedMessageGifBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismReceivedMessageGifBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessageGifBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageGifBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageGifBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismReceivedMessageGifBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismReceivedMessageGifBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessageGifBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageGifBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessageGifBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessageGifBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageGifBinding.rvMessageReactions.visibility =
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
                ismReceivedMessageGifBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessageGifBinding.tvSenderName.text =
                    message.senderName
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismReceivedMessageGifBinding.ivSenderImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    ismReceivedMessageGifBinding.ivSenderImage,
                    position,
                    12
                )
            }
            try {
                Glide.with(mContext)
                    .load(message.gifMainUrl)
                    .thumbnail(Glide.with(mContext).load(message.gifStillUrl))
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
                    .into(ismReceivedMessageGifBinding.ivGifImage)
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }
            ismReceivedMessageGifBinding.tvMessageTime.text =
                message.messageTime
            ismReceivedMessageGifBinding.rlGif.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    false
                )
            }

            ismReceivedMessageGifBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/ false) { // not require as up now
                ismReceivedMessageGifBinding.ivReaction.visibility =
                    View.GONE
            }
        } catch (ignore: Exception) {
        }
    }
}