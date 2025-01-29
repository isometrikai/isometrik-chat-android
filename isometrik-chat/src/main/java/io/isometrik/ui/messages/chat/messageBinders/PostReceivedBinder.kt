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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmReceivedMessagePostBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter
import io.isometrik.ui.utils.TimeUtil

class PostReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessagePostBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessagePostBinding {
        return IsmReceivedMessagePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessagePostBinding: IsmReceivedMessagePostBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {

            ismReceivedMessagePostBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessagePostBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessagePostBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismReceivedMessagePostBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessagePostBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessagePostBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessagePostBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessagePostBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessagePostBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessagePostBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessagePostBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessagePostBinding.ivReaction.visibility =
                    View.GONE
            }
            if (isMessagingDisabled) {
                ismReceivedMessagePostBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessagePostBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessagePostBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismReceivedMessagePostBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismReceivedMessagePostBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessagePostBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessagePostBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessagePostBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessagePostBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessagePostBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessagePostBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessagePostBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessagePostBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessagePostBinding.rvMessageReactions.visibility =
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
                ismReceivedMessagePostBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessagePostBinding.tvSenderName.text =
                    message.senderName
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismReceivedMessagePostBinding.ivSenderImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    ismReceivedMessagePostBinding.ivSenderImage,
                    position,
                    12
                )
            }

            try {
                Glide.with(mContext)
                    .load(message.photoMainUrl)
                    .thumbnail(Glide.with(mContext).load(message.photoThumbnailUrl))
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(CenterCrop())
                    .into(ismReceivedMessagePostBinding.ivPhoto)
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }

            ismReceivedMessagePostBinding.tvMessageTime.text =
                TimeUtil.convert24to12hourformat(message.messageTime)

            ismReceivedMessagePostBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/ false) { // not required
                ismReceivedMessagePostBinding.ivReaction.visibility =
                    View.GONE
            }
            ismReceivedMessagePostBinding.rlPhoto.setOnClickListener { view: View? ->
                IsometrikChatSdk.instance.chatActionsClickListener?.onSharedPostClick(
                    message.messageId
                )
            }
        } catch (ignore: Exception) {
        }
    }
}