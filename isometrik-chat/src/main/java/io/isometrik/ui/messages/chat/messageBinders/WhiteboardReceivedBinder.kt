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
import io.isometrik.chat.databinding.IsmReceivedMessageWhiteboardBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class WhiteboardReceivedBinder :
    MessageItemBinder<MessagesModel, IsmReceivedMessageWhiteboardBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageWhiteboardBinding {
        return IsmReceivedMessageWhiteboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessageWhiteboardBinding: IsmReceivedMessageWhiteboardBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismReceivedMessageWhiteboardBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessageWhiteboardBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageWhiteboardBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessageWhiteboardBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageWhiteboardBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessageWhiteboardBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessageWhiteboardBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessageWhiteboardBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessageWhiteboardBinding.ivReaction.visibility =
                    View.VISIBLE
            }
            if (isMessagingDisabled) {
                ismReceivedMessageWhiteboardBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageWhiteboardBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageWhiteboardBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismReceivedMessageWhiteboardBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismReceivedMessageWhiteboardBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessageWhiteboardBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageWhiteboardBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageWhiteboardBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageWhiteboardBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessageWhiteboardBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageWhiteboardBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessageWhiteboardBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessageWhiteboardBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageWhiteboardBinding.rvMessageReactions.visibility =
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
                ismReceivedMessageWhiteboardBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessageWhiteboardBinding.tvSenderName.text =
                    message.senderName
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(
                            ismReceivedMessageWhiteboardBinding.ivSenderImage
                        )
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext, message.senderName,
                    ismReceivedMessageWhiteboardBinding.ivSenderImage,
                    position, 12
                )
            }
            try {
                Glide.with(mContext)
                    .load(message.whiteboardMainUrl)
                    .thumbnail(Glide.with(mContext).load(message.whiteboardThumbnailUrl))
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(CenterCrop())
                    .into(
                        ismReceivedMessageWhiteboardBinding.ivWhiteboard
                    )
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }

            if (message.isDownloaded) {
                ismReceivedMessageWhiteboardBinding.tvWhiteboardStatus.text =
                    mContext.getString(R.string.ism_open)
                ismReceivedMessageWhiteboardBinding.pbDownload.visibility =
                    View.GONE
            } else {
                if (message.isDownloading) {
                    ismReceivedMessageWhiteboardBinding.tvWhiteboardStatus.text =
                        mContext.getString(R.string.ism_attachments_cancel)
                    ismReceivedMessageWhiteboardBinding.pbDownload.visibility =
                        View.VISIBLE
                } else {
                    ismReceivedMessageWhiteboardBinding.tvWhiteboardStatus.text =
                        mContext.getString(R.string.ism_download)
                    ismReceivedMessageWhiteboardBinding.pbDownload.visibility =
                        View.GONE
                }
            }

            ismReceivedMessageWhiteboardBinding.tvWhiteboardSize.text =
                message.mediaSizeInMB

            ismReceivedMessageWhiteboardBinding.tvMessageTime.text =
                message.messageTime

            ismReceivedMessageWhiteboardBinding.rlWhiteboard.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    message.isDownloaded
                )
            }

            ismReceivedMessageWhiteboardBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/false) { // not required
                ismReceivedMessageWhiteboardBinding.ivReaction.visibility =
                    View.GONE
            }
            ismReceivedMessageWhiteboardBinding.rlDownload.setOnClickListener { v: View? ->
                if (message.isDownloaded) {
                    messageActionCallback.handleClickOnMessageCell(message, true)
                } else {
                    if (message.isDownloading) {
                        messageActionCallback.cancelMediaDownload(message, position)
                    } else {
                        ismReceivedMessageWhiteboardBinding.pbDownload
                            .setProgressCompat(0, false)
                        messageActionCallback.downloadMedia(
                            message,
                            mContext.getString(R.string.ism_whiteboard), position
                        )
                    }
                }
            }
        } catch (ignore: Exception) {
        }
    }
}