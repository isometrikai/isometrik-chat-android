package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmSentMessageTextBinding
import io.isometrik.chat.utils.LinkPreviewUtil
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TextSentBinder : MessageItemBinder<MessagesModel, IsmSentMessageTextBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmSentMessageTextBinding {
        return IsmSentMessageTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismSentMessageTextBinding: IsmSentMessageTextBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismSentMessageTextBinding.rlText.setBackgroundResource(ChatConfig.textSentBubbleResId)
            ismSentMessageTextBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isReadByAll) {
                ismSentMessageTextBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(mContext, R.drawable.ism_message_read_double_tick)
                )

                ismSentMessageTextBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else if (message.isDeliveredToAll) {
                ismSentMessageTextBinding.ivDeliveryReadStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        mContext,
                        R.drawable.ism_message_delivered_doubletick
                    )
                )

                ismSentMessageTextBinding.ivDeliveryReadStatus.visibility =
                    View.VISIBLE
            } else {
                ismSentMessageTextBinding.ivDeliveryReadStatus.visibility =
                    View.GONE
            }

            if (message.isMessageSentSuccessfully) {
                ismSentMessageTextBinding.tvSendingMessage.visibility =
                    View.GONE
            } else {
                if (message.isSendingMessageFailed) {
                    ismSentMessageTextBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_leave_red)
                    )
                    ismSentMessageTextBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_failed)
                } else {
                    ismSentMessageTextBinding.tvSendingMessage.setTextColor(
                        ContextCompat.getColor(mContext, R.color.ism_message_time_grey)
                    )
                    ismSentMessageTextBinding.tvSendingMessage.text =
                        mContext.getString(R.string.ism_sending)
                }
                ismSentMessageTextBinding.tvSendingMessage.visibility =
                    View.VISIBLE
            }

            if (message.isForwardedMessage) {
                ismSentMessageTextBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageTextBinding.dividerForward.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismSentMessageTextBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                    ismSentMessageTextBinding.dividerForwardNotes.visibility =
                        View.GONE
                } else {
                    ismSentMessageTextBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismSentMessageTextBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                    ismSentMessageTextBinding.dividerForwardNotes.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageTextBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismSentMessageTextBinding.dividerForward.visibility =
                    View.GONE
                ismSentMessageTextBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
                ismSentMessageTextBinding.dividerForwardNotes.visibility =
                    View.GONE
            }

            if (multipleMessagesSelectModeOn) {
                ismSentMessageTextBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismSentMessageTextBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
//                ismSentMessageTextBinding.ivForward.visibility =
//                    View.GONE
            } else {
                ismSentMessageTextBinding.ivSelectedStatus.visibility =
                    View.GONE
                if (message.isMessageSentSuccessfully) {
//                    ismSentMessageTextBinding.ivForward.visibility =
//                        View.VISIBLE
                    ismSentMessageTextBinding.ivForward.setOnClickListener { v: View? ->
                        messageActionCallback.forwardMessageRequest(
                            message
                        )
                    }
                } else {
//                    ismSentMessageTextBinding.ivForward.visibility =
//                        View.GONE
                }
            }
            if (isMessagingDisabled) {
//                ismSentMessageTextBinding.ivForward.visibility =
//                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismSentMessageTextBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismSentMessageTextBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismSentMessageTextBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismSentMessageTextBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                ismSentMessageTextBinding.dividerReply.visibility =
                    View.VISIBLE

                if (message.originalMessageAttachmentUrl == null) {
                    ismSentMessageTextBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                    ismSentMessageTextBinding.vParentMessage.tvMessage.visibility =
                        View.VISIBLE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessageAttachmentUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismSentMessageTextBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismSentMessageTextBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
                ismSentMessageTextBinding.vParentMessage.root.setOnClickListener {
                    messageActionCallback.onScrollToParentMessage(
                        message.parentMessageId
                    )
                }
            } else {
                ismSentMessageTextBinding.vParentMessage.root.visibility =
                    View.GONE
                ismSentMessageTextBinding.dividerReply.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismSentMessageTextBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismSentMessageTextBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismSentMessageTextBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismSentMessageTextBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismSentMessageTextBinding.rvMessageReactions.visibility =
                    View.GONE
            }

            // Set text message with clickable links
            val textMessage = message.textMessage?.toString() ?: ""
            val spannableText = SpannableString(textMessage)
            LinkPreviewUtil.makeLinksClickable(spannableText)
            ismSentMessageTextBinding.tvTextMessage.text = spannableText
            ismSentMessageTextBinding.tvTextMessage.setMovementMethod(
                LinkMovementMethod.getInstance()
            )
            ismSentMessageTextBinding.tvMessageTime.text =
                message.messageTime

            // Handle link preview
            handleLinkPreview(mContext, textMessage, ismSentMessageTextBinding, multipleMessagesSelectModeOn)
        } catch (ignore: Exception) {
        }
    }

    private fun handleLinkPreview(
        context: Context,
        textMessage: String,
        binding: IsmSentMessageTextBinding,
        multipleMessagesSelectModeOn: Boolean
    ) {
        if (multipleMessagesSelectModeOn) {
            binding.vLinkPreview.root.visibility = View.GONE
            return
        }

        val url = LinkPreviewUtil.extractFirstUrl(textMessage)
        if (url == null) {
            binding.vLinkPreview.root.visibility = View.GONE
            return
        }

        // Show loading state
        binding.vLinkPreview.root.visibility = View.VISIBLE
        binding.vLinkPreview.ivPreviewImage.visibility = View.GONE
        binding.vLinkPreview.tvPreviewTitle.visibility = View.GONE
        binding.vLinkPreview.tvPreviewDescription.visibility = View.GONE
        binding.vLinkPreview.tvPreviewUrl.text = url

        // Fetch link preview in background
        CoroutineScope(Dispatchers.Main).launch {
            val preview = withContext(Dispatchers.IO) {
                LinkPreviewUtil.fetchLinkPreview(url)
            }

            if (preview != null) {
                // Show title
                if (!preview.title.isNullOrEmpty()) {
                    binding.vLinkPreview.tvPreviewTitle.text = preview.title
                    binding.vLinkPreview.tvPreviewTitle.visibility = View.VISIBLE
                }

                // Show description
                if (!preview.description.isNullOrEmpty()) {
                    binding.vLinkPreview.tvPreviewDescription.text = preview.description
                    binding.vLinkPreview.tvPreviewDescription.visibility = View.VISIBLE
                }

                // Show image
                if (!preview.imageUrl.isNullOrEmpty()) {
                    try {
                        Glide.with(context)
                            .load(preview.imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.ism_ic_profile)
                            .into(binding.vLinkPreview.ivPreviewImage)
                        binding.vLinkPreview.ivPreviewImage.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        binding.vLinkPreview.ivPreviewImage.visibility = View.GONE
                    }
                }

                // Set click listener to open URL
                binding.vLinkPreview.root.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(preview.url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                // If preview fetch failed, still show URL and make it clickable
                binding.vLinkPreview.root.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}