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
import io.isometrik.chat.databinding.IsmReceivedMessageOfferBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class OfferReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageOfferBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageOfferBinding {
        return IsmReceivedMessageOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessageOfferBinding: IsmReceivedMessageOfferBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismReceivedMessageOfferBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessageOfferBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageOfferBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageOfferBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessageOfferBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageOfferBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessageOfferBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }

            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageOfferBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessageOfferBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessageOfferBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessageOfferBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessageOfferBinding.ivReaction.visibility =
                    View.VISIBLE
            }
            if (isMessagingDisabled) {
                ismReceivedMessageOfferBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageOfferBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageOfferBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismReceivedMessageOfferBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismReceivedMessageOfferBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessageOfferBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageOfferBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageOfferBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageOfferBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessageOfferBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageOfferBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessageOfferBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessageOfferBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageOfferBinding.rvMessageReactions.visibility =
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
                ismReceivedMessageOfferBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessageOfferBinding.tvSenderName.text =
                    message.senderName
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(
                            ismReceivedMessageOfferBinding.ivSenderImage
                        )
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    ismReceivedMessageOfferBinding.ivSenderImage,
                    position,
                    12
                )
            }
            ismReceivedMessageOfferBinding.tvMessageTime.text =
                message.messageTime

            ismReceivedMessageOfferBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/ false) { // not required as up now
                ismReceivedMessageOfferBinding.ivReaction.visibility =
                    View.GONE
            }
            ismReceivedMessageOfferBinding.rlContact.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    false
                )
            }
        } catch (ignore: Exception) {
        }
    }
}