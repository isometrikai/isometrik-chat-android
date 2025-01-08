package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.method.LinkMovementMethod
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
import io.isometrik.chat.databinding.IsmReceivedMessageVideoBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter
import io.isometrik.ui.utils.TimeUtil

class VideoReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageVideoBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageVideoBinding {
        return IsmReceivedMessageVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessageVideoBinding: IsmReceivedMessageVideoBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismReceivedMessageVideoBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessageVideoBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageVideoBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageVideoBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessageVideoBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageVideoBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessageVideoBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageVideoBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessageVideoBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessageVideoBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessageVideoBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessageVideoBinding.ivReaction.visibility =
                    View.VISIBLE
            }
            if (isMessagingDisabled) {
                ismReceivedMessageVideoBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageVideoBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageVideoBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismReceivedMessageVideoBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismReceivedMessageVideoBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessageVideoBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageVideoBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageVideoBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageVideoBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessageVideoBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageVideoBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessageVideoBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessageVideoBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageVideoBinding.rvMessageReactions.visibility =
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
                ismReceivedMessageVideoBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessageVideoBinding.tvSenderName.text =
                    message.senderName
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismReceivedMessageVideoBinding.ivSenderImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    ismReceivedMessageVideoBinding.ivSenderImage,
                    position,
                    12
                )
            }
            try {
                Glide.with(mContext)
                    .load(message.videoMainUrl)
                    .thumbnail(Glide.with(mContext).load(message.videoThumbnailUrl))
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(CenterCrop())
                    .into(ismReceivedMessageVideoBinding.ivVideoThumbnail)
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }
            if (message.isDownloaded) {
                ismReceivedMessageVideoBinding.tvVideoStatus.text =
                    mContext.getString(R.string.ism_open)
                ismReceivedMessageVideoBinding.pbDownload.visibility =
                    View.GONE
            } else {
                if (message.isDownloading) {
                    ismReceivedMessageVideoBinding.tvVideoStatus.text =
                        mContext.getString(R.string.ism_attachments_cancel)
                    ismReceivedMessageVideoBinding.pbDownload.visibility =
                        View.VISIBLE
                } else {
                    ismReceivedMessageVideoBinding.tvVideoStatus.text =
                        mContext.getString(R.string.ism_download)
                    ismReceivedMessageVideoBinding.pbDownload.visibility =
                        View.GONE
                }
            }

            ismReceivedMessageVideoBinding.tvVideoSize.text =
                message.mediaSizeInMB

            ismReceivedMessageVideoBinding.tvMessageTime.text =
                message.messageTime

            ismReceivedMessageVideoBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/false) { // not required
                ismReceivedMessageVideoBinding.ivReaction.visibility =
                    View.GONE
            }
            ismReceivedMessageVideoBinding.rlDownload.setOnClickListener { v: View? ->
                if (message.isDownloaded) {
                    messageActionCallback.handleClickOnMessageCell(message, true)
                } else {
                    if (message.isDownloading) {
                        messageActionCallback.cancelMediaDownload(message, position)
                    } else {
                        ismReceivedMessageVideoBinding.pbDownload.setProgressCompat(
                            0, false
                        )
                        messageActionCallback.downloadMedia(
                            message,
                            mContext.getString(R.string.ism_video), position
                        )
                    }
                }
            }

            ismReceivedMessageVideoBinding.rlVideo.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    message.isDownloaded
                )
            }
        } catch (ignore: Exception) {
        }
    }
}