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
import io.isometrik.chat.databinding.IsmReceivedMessageAudioBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class AudioReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageAudioBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageAudioBinding {
        return IsmReceivedMessageAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessageAudioBinding: IsmReceivedMessageAudioBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismReceivedMessageAudioBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessageAudioBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageAudioBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageAudioBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessageAudioBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageAudioBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessageAudioBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageAudioBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessageAudioBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessageAudioBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessageAudioBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessageAudioBinding.ivReaction.visibility =
                    View.VISIBLE
            }
            if (isMessagingDisabled) {
                ismReceivedMessageAudioBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageAudioBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageAudioBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismReceivedMessageAudioBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismReceivedMessageAudioBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessageAudioBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageAudioBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageAudioBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageAudioBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessageAudioBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageAudioBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessageAudioBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessageAudioBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageAudioBinding.rvMessageReactions.visibility =
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
                ismReceivedMessageAudioBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessageAudioBinding.tvSenderName.text =
                    message.senderName
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismReceivedMessageAudioBinding.ivSenderImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    ismReceivedMessageAudioBinding.ivSenderImage,
                    position,
                    12
                )
            }
            if (message.isDownloaded) {
                ismReceivedMessageAudioBinding.tvAudioStatus.text =
                    mContext.getString(R.string.ism_open)
                ismReceivedMessageAudioBinding.pbDownload.visibility =
                    View.GONE
            } else {
                if (message.isDownloading) {
                    ismReceivedMessageAudioBinding.tvAudioStatus.text =
                        mContext.getString(R.string.ism_attachments_cancel)
                    ismReceivedMessageAudioBinding.pbDownload.visibility =
                        View.VISIBLE
                } else {
                    ismReceivedMessageAudioBinding.tvAudioStatus.text =
                        mContext.getString(R.string.ism_download)
                    ismReceivedMessageAudioBinding.pbDownload.visibility =
                        View.GONE
                }
            }
            ismReceivedMessageAudioBinding.tvAudioName.text =
                message.audioName
            ismReceivedMessageAudioBinding.tvAudioSize.text =
                message.mediaSizeInMB

            ismReceivedMessageAudioBinding.tvMessageTime.text =
                message.messageTime

            ismReceivedMessageAudioBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/ false) {  // not require as up now
                ismReceivedMessageAudioBinding.ivReaction.visibility =
                    View.GONE
            }
            ismReceivedMessageAudioBinding.rlDownload.setOnClickListener { v: View? ->
                if (message.isDownloaded) {
                    messageActionCallback.handleClickOnMessageCell(message, true)
                } else {
                    if (message.isDownloading) {
                        messageActionCallback.cancelMediaDownload(message, position)
                    } else {
                        ismReceivedMessageAudioBinding.pbDownload.setProgressCompat(
                            0, false
                        )
                        messageActionCallback.downloadMedia(
                            message,
                            mContext.getString(R.string.ism_audio_recording), position
                        )
                    }
                }
            }
            ismReceivedMessageAudioBinding.rlAudio.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    message.isDownloaded
                )
            }
        } catch (ignore: Exception) {
        }
    }
}