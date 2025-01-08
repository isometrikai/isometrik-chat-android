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
import io.isometrik.chat.databinding.IsmReceivedMessageFileBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class FileReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageFileBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageFileBinding {
        return IsmReceivedMessageFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessageFileBinding: IsmReceivedMessageFileBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {

            ismReceivedMessageFileBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessageFileBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageFileBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageFileBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessageFileBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageFileBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessageFileBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageFileBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessageFileBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessageFileBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessageFileBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessageFileBinding.ivReaction.visibility =
                    View.VISIBLE
            }

            if (isMessagingDisabled) {
                ismReceivedMessageFileBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageFileBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageFileBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismReceivedMessageFileBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismReceivedMessageFileBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessageFileBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageFileBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageFileBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageFileBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessageFileBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageFileBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessageFileBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessageFileBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageFileBinding.rvMessageReactions.visibility =
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
                ismReceivedMessageFileBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessageFileBinding.tvSenderName.text =
                    message.senderName
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismReceivedMessageFileBinding.ivSenderImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    ismReceivedMessageFileBinding.ivSenderImage,
                    position,
                    12
                )
            }
            if (message.isDownloaded) {
                ismReceivedMessageFileBinding.tvFileStatus.text =
                    mContext.getString(R.string.ism_open)
                ismReceivedMessageFileBinding.pbDownload.visibility =
                    View.GONE
            } else {
                if (message.isDownloading) {
                    ismReceivedMessageFileBinding.tvFileStatus.text =
                        mContext.getString(R.string.ism_attachments_cancel)
                    ismReceivedMessageFileBinding.pbDownload.visibility =
                        View.VISIBLE
                } else {
                    ismReceivedMessageFileBinding.tvFileStatus.text =
                        mContext.getString(R.string.ism_download)
                    ismReceivedMessageFileBinding.pbDownload.visibility =
                        View.GONE
                }
            }
            ismReceivedMessageFileBinding.tvFileName.text =
                message.fileName
            ismReceivedMessageFileBinding.tvFileSize.text =
                message.mediaSizeInMB

            ismReceivedMessageFileBinding.tvMessageTime.text =
                message.messageTime

            ismReceivedMessageFileBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/false) { // as up now
                ismReceivedMessageFileBinding.ivReaction.visibility =
                    View.GONE
            }
            ismReceivedMessageFileBinding.rlDownload.setOnClickListener { v: View? ->
                if (message.isDownloaded) {
                    messageActionCallback.handleClickOnMessageCell(message, true)
                } else {
                    if (message.isDownloading) {
                        messageActionCallback.cancelMediaDownload(message, position)
                    } else {
                        ismReceivedMessageFileBinding.pbDownload.setProgressCompat(
                            0, false
                        )
                        messageActionCallback.downloadMedia(
                            message,
                            mContext.getString(R.string.ism_file), position
                        )
                    }
                }
            }
            ismReceivedMessageFileBinding.rlFile.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    message.isDownloaded
                )
            }
        } catch (ignore: Exception) {
        }
    }
}