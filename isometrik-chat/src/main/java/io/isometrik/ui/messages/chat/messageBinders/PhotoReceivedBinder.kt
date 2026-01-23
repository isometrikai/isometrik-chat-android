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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmReceivedMessagePhotoBinding
import io.isometrik.chat.utils.LinkPreviewUtil
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class PhotoReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessagePhotoBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessagePhotoBinding {
        return IsmReceivedMessagePhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
    }

    override fun bindData(
            mContext: Context,
            ismReceivedMessagePhotoBinding: IsmReceivedMessagePhotoBinding,
            message: MessagesModel,
            position: Int,
            multipleMessagesSelectModeOn: Boolean,
            isMessagingDisabled: Boolean,
            messageActionCallback: MessageActionCallback
    ) {
        try {

            ismReceivedMessagePhotoBinding.ivEdited.visibility =
                    if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessagePhotoBinding.vForwardedMessage.root.visibility = View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessagePhotoBinding.vForwardedMessageNotes.root.visibility =
                            View.GONE
                } else {
                    ismReceivedMessagePhotoBinding.vForwardedMessageNotes.tvMessage.text =
                            message.forwardedMessageNotes

                    ismReceivedMessagePhotoBinding.vForwardedMessageNotes.root.visibility =
                            View.VISIBLE
                }
            } else {
                ismReceivedMessagePhotoBinding.vForwardedMessage.root.visibility = View.GONE
                ismReceivedMessagePhotoBinding.vForwardedMessageNotes.root.visibility = View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessagePhotoBinding.ivSelectedStatus.isSelected = message.isSelected
                ismReceivedMessagePhotoBinding.ivSelectedStatus.visibility = View.VISIBLE
                ismReceivedMessagePhotoBinding.ivReaction.visibility = View.GONE
            } else {
                ismReceivedMessagePhotoBinding.ivSelectedStatus.visibility = View.GONE
                ismReceivedMessagePhotoBinding.ivReaction.visibility = View.VISIBLE
            }
            if (isMessagingDisabled) {
                ismReceivedMessagePhotoBinding.ivReaction.visibility = View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessagePhotoBinding.vParentMessage.root.visibility = View.VISIBLE
                ismReceivedMessagePhotoBinding.vParentMessage.tvSenderName.text =
                        message.originalMessageSenderName
                ismReceivedMessagePhotoBinding.vParentMessage.tvMessage.text =
                        message.originalMessage
                ismReceivedMessagePhotoBinding.vParentMessage.tvMessageTime.text =
                        message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessagePhotoBinding.vParentMessage.ivMessageImage.visibility =
                            View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                                .load(message.originalMessagePlaceholderImage)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(ismReceivedMessagePhotoBinding.vParentMessage.ivMessageImage)
                    } catch (ignore: IllegalArgumentException) {} catch (
                            ignore: NullPointerException) {}
                    ismReceivedMessagePhotoBinding.vParentMessage.ivMessageImage.visibility =
                            View.VISIBLE
                }
            } else {
                ismReceivedMessagePhotoBinding.vParentMessage.root.visibility = View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessagePhotoBinding.rvMessageReactions.visibility = View.GONE
                } else {
                    ismReceivedMessagePhotoBinding.rvMessageReactions.layoutManager =
                            LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessagePhotoBinding.rvMessageReactions.adapter =
                            MessageReactionsAdapter(
                                    mContext,
                                    message.reactions,
                                    message.messageId,
                                    messageActionCallback::onMessageReactionClicked
                            )

                    ismReceivedMessagePhotoBinding.rvMessageReactions.visibility = View.VISIBLE
                }
            } else {
                ismReceivedMessagePhotoBinding.rvMessageReactions.visibility = View.GONE
            }
            if (message.isSenderDeleted) {
                val spannableString = SpannableString(message.senderName)
                spannableString.setSpan(StyleSpan(Typeface.ITALIC), 0, spannableString.length, 0)
                ismReceivedMessagePhotoBinding.tvSenderName.text = spannableString
            } else {
                ismReceivedMessagePhotoBinding.tvSenderName.text = message.senderName
            }

            if (ChatConfig.hideSenderNameInMessageCell) {
                ismReceivedMessagePhotoBinding.tvSenderName.visibility = View.GONE
                ismReceivedMessagePhotoBinding.tvComma.visibility = View.GONE
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                            .load(message.senderImageUrl)
                            .placeholder(R.drawable.ism_ic_profile)
                            .transform(CircleCrop())
                            .into(ismReceivedMessagePhotoBinding.ivSenderImage)
                } catch (ignore: IllegalArgumentException) {} catch (
                        ignore: NullPointerException) {}
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                        mContext,
                        message.senderName,
                        ismReceivedMessagePhotoBinding.ivSenderImage,
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
                        .into(ismReceivedMessagePhotoBinding.ivPhoto)
            } catch (ignore: IllegalArgumentException) {} catch (ignore: NullPointerException) {}
            if (message.isDownloaded) {
                ismReceivedMessagePhotoBinding.tvPhotoStatus.text =
                        mContext.getString(R.string.ism_open)
                ismReceivedMessagePhotoBinding.pbDownload.visibility = View.GONE
            } else {
                if (message.isDownloading) {
                    ismReceivedMessagePhotoBinding.tvPhotoStatus.text =
                            mContext.getString(R.string.ism_attachments_cancel)
                    ismReceivedMessagePhotoBinding.pbDownload.visibility = View.VISIBLE
                } else {
                    ismReceivedMessagePhotoBinding.tvPhotoStatus.text =
                            mContext.getString(R.string.ism_download)
                    ismReceivedMessagePhotoBinding.pbDownload.visibility = View.GONE
                }
            }

            ismReceivedMessagePhotoBinding.tvPhotoSize.text = message.mediaSizeInMB

            // Display caption if available
            if (message.textMessage != null && message.textMessage.toString().isNotBlank()) {
                val captionText = message.textMessage.toString()
                val spannableText = SpannableString(captionText)
                LinkPreviewUtil.makeLinksClickable(spannableText)
                ismReceivedMessagePhotoBinding.tvCaption.text = spannableText
                ismReceivedMessagePhotoBinding.tvCaption.movementMethod =
                        LinkMovementMethod.getInstance()
                ismReceivedMessagePhotoBinding.tvCaption.visibility = View.VISIBLE
            } else {
                ismReceivedMessagePhotoBinding.tvCaption.visibility = View.GONE
            }

            ismReceivedMessagePhotoBinding.tvMessageTime.text = message.messageTime

            ismReceivedMessagePhotoBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(message.messageId)
            }
            if (/*joiningAsObserver*/ false) { // not required
                ismReceivedMessagePhotoBinding.ivReaction.visibility = View.GONE
            }
            ismReceivedMessagePhotoBinding.ivPhoto.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(message, message.isDownloaded)
            }

            ismReceivedMessagePhotoBinding.rlDownload.setOnClickListener { v: View? ->
                if (message.isDownloaded) {
                    messageActionCallback.handleClickOnMessageCell(message, true)
                } else {
                    if (message.isDownloading) {
                        messageActionCallback.cancelMediaDownload(message, position)
                    } else {
                        ismReceivedMessagePhotoBinding.pbDownload.setProgressCompat(0, false)
                        messageActionCallback.downloadMedia(
                                message,
                                mContext.getString(R.string.ism_photo),
                                position
                        )
                    }
                }
            }
        } catch (ignore: Exception) {}
    }
}
