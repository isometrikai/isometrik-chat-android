package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegSession
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessageAudioBinding
import io.isometrik.ui.libwave.SeekBarOnProgressChanged
import io.isometrik.ui.libwave.Utils
import io.isometrik.ui.libwave.WaveGravity
import io.isometrik.ui.libwave.WaveformSeekBar
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter
import io.isometrik.ui.utils.AudioFIleUtil
import io.isometrik.ui.utils.DummyDataUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.util.HashMap
import java.util.Random

class AudioSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageAudioBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageAudioBinding {
        return IsmSentMessageAudioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bindData(
        mContext: Context, ismSentMessageAudioBinding: IsmSentMessageAudioBinding,
        message: MessagesModel, position: Int, multipleMessagesSelectModeOn: Boolean,
        isMessagingDisabled: Boolean, messageActionCallback: MessageActionCallback
    ) {
        try {
            ismSentMessageAudioBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessageAudioBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessageAudioBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessageAudioBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessageAudioBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageAudioBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessageAudioBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageAudioBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageAudioBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessageAudioBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageAudioBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessageAudioBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }
            if (message.isForwardedMessage) {
                ismSentMessageAudioBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageAudioBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismSentMessageAudioBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessageAudioBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageAudioBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessageAudioBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismSentMessageAudioBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessageAudioBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismSentMessageAudioBinding.ivForward.visibility =
                    View.GONE
            } else {
                ismSentMessageAudioBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
                    ismSentMessageAudioBinding.ivForward.visibility =
                        View.VISIBLE
                    ismSentMessageAudioBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
                    ismSentMessageAudioBinding.ivForward.visibility =
                        View.GONE
                }
            }
            if (isMessagingDisabled) {
                ismSentMessageAudioBinding.ivForward.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessageAudioBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageAudioBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessageAudioBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessageAudioBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismSentMessageAudioBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageAudioBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageAudioBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessageAudioBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessageAudioBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageAudioBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessageAudioBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessageAudioBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismSentMessageAudioBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageAudioBinding.rvMessageReactions.visibility =
                    View.GONE
            }

            ismSentMessageAudioBinding.tvMessageTime.text =
                message.messageTime

            if (message.isUploaded) {
                ismSentMessageAudioBinding.rlUpload.visibility =
                    View.GONE
                if (message.isDownloaded || message.isDownloading) {
                    if (message.isDownloaded) {
                        ismSentMessageAudioBinding.tvDownloadAudioStatus.text =
                            mContext.getString(R.string.ism_open)
                        ismSentMessageAudioBinding.pbDownload.visibility =
                            View.GONE
                    } else {
                        ismSentMessageAudioBinding.tvDownloadAudioStatus.text =
                            mContext.getString(R.string.ism_attachments_cancel)
                        ismSentMessageAudioBinding.pbDownload.visibility =
                            View.VISIBLE
                    }

                    ismSentMessageAudioBinding.rlDownload.setOnClickListener { v: View? ->
                        if (message.isDownloading) {
                            messageActionCallback.cancelMediaDownload(message, position)
                        } else {
                            messageActionCallback.handleClickOnMessageCell(message, true)
                        }
                    }
                    ismSentMessageAudioBinding.rlDownload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessageAudioBinding.rlDownload.visibility =
                        View.GONE
                }
            } else {
                ismSentMessageAudioBinding.rlDownload.visibility =
                    View.GONE
                if (message.isUploading) {
                    ismSentMessageAudioBinding.tvAudioStatus.text =
                        mContext.getString(R.string.ism_attachments_cancel)
                    ismSentMessageAudioBinding.pbUpload.visibility =
                        View.VISIBLE
                } else {
                    ismSentMessageAudioBinding.tvAudioStatus.text =
                        mContext.getString(R.string.ism_remove)
                    ismSentMessageAudioBinding.pbUpload.visibility =
                        View.GONE
                }
                ismSentMessageAudioBinding.rlUpload.setOnClickListener { v: View? ->
                    if (message.isUploading) {
                        messageActionCallback.cancelMediaUpload(message, position)
                    } else {
                        messageActionCallback.removeCanceledMessage(
                            message.localMessageId,
                            position
                        )
                    }
                }
                ismSentMessageAudioBinding.rlUpload.visibility =
                    View.VISIBLE
            }
            ismSentMessageAudioBinding.tvAudioName.text =
                message.audioName
            ismSentMessageAudioBinding.tvAudioSize.text =
                message.mediaSizeInMB
            ismSentMessageAudioBinding.rlAudio.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    message.isDownloaded
                )
            }
            if (message.isUploaded) {

                CoroutineScope(Dispatchers.Main).launch {
                    val file = AudioFIleUtil.getOrDownloadAudioFile(mContext, message.audioUrl)
                    val amplitudes = AudioFIleUtil.extractAmplitudes(mContext, file)
                    ismSentMessageAudioBinding.waveSeekBar.apply {
                        sample = amplitudes.toIntArray()
                    }

                }
            } else {
                ismSentMessageAudioBinding.waveSeekBar.apply {
                    progress = 0F
                    waveWidth = Utils.dp(context, 2)
                    waveGap = Utils.dp(context, 1)
                    waveMinHeight = Utils.dp(context, 5)
                    waveCornerRadius = Utils.dp(context, 2)
                    waveGravity = WaveGravity.CENTER
                    waveBackgroundColor =
                        ContextCompat.getColor(context, R.color.ism_identifier_text_grey)
                    waveProgressColor =
                        ContextCompat.getColor(context, R.color.ism_blue)
                    sample = DummyDataUtil.getDummyWaveSample()
//                marker = getDummyMarkerSample(ismSentMessageAudioBinding)
                    onProgressChanged = object : SeekBarOnProgressChanged {
                        override fun onProgressChanged(
                            waveformSeekBar: WaveformSeekBar,
                            progress: Float,
                            fromUser: Boolean
                        ) {
                            if (!fromUser)
                                ismSentMessageAudioBinding.waveSeekBar.progress =
                                    progress
                        }
                    }
                }
            }

        } catch (ignore: Exception) {
        }
    }
}