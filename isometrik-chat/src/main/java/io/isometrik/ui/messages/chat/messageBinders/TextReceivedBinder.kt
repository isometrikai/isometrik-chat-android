package io.isometrik.ui.messages.chat.messageBinders

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
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
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmReceivedMessageTextBinding
import io.isometrik.chat.utils.LinkPreviewUtil
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TextReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageTextBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageTextBinding {
        return IsmReceivedMessageTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessageTextBinding: IsmReceivedMessageTextBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismReceivedMessageTextBinding.rlText.setBackgroundResource(ChatConfig.textReceivedBubbleResId)
            ismReceivedMessageTextBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessageTextBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageTextBinding.dividerForward.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageTextBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                    ismReceivedMessageTextBinding.dividerForwardNotes.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageTextBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessageTextBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                    ismReceivedMessageTextBinding.dividerForwardNotes.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageTextBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.dividerForward.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.dividerForwardNotes.visibility =
                    View.GONE
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageTextBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessageTextBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessageTextBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessageTextBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.ivReaction.visibility =
                    View.VISIBLE
            }
            if (isMessagingDisabled) {
                ismReceivedMessageTextBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageTextBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageTextBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                if (message.originalMessage == "AttachmentMessage:Gif") {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessage.text =
                        "GIF"
                } else {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessage.text =
                        message.originalMessage
                }
                ismReceivedMessageTextBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                ismReceivedMessageTextBinding.dividerReply.visibility =
                    View.VISIBLE

                if (message.originalMessageTime == null || message.originalMessageTime.isEmpty()) {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessageTime.visibility =
                        View.GONE
                    ismReceivedMessageTextBinding.vParentMessage.tvComma.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessageTime.visibility =
                        View.VISIBLE
                }
                ismReceivedMessageTextBinding.vParentMessage.ivMessageImage.visibility =
                    View.GONE

                if (message.originalMessageAttachmentUrl != null && !message.originalMessageAttachmentUrl.isEmpty()) {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessageAttachmentUrl)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageTextBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageTextBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                    ismReceivedMessageTextBinding.vParentMessage.tvMessage.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageTextBinding.vParentMessage.tvMessage.visibility =
                        View.VISIBLE
                }
                ismReceivedMessageTextBinding.vParentMessage.root
                    .setOnClickListener { view: View? ->
                        messageActionCallback.onScrollToParentMessage(
                            message.parentMessageId
                        )
                    }
            } else {
                ismReceivedMessageTextBinding.vParentMessage.root.visibility =
                    View.GONE
                ismReceivedMessageTextBinding.dividerReply.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessageTextBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageTextBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessageTextBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessageTextBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageTextBinding.rvMessageReactions.visibility =
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
                ismReceivedMessageTextBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessageTextBinding.tvSenderName.text =
                    message.senderName
            }
            if(ChatConfig.hideSenderNameInMessageCell){
                ismReceivedMessageTextBinding.tvSenderName.visibility = View.GONE
                ismReceivedMessageTextBinding.tvComma.visibility = View.GONE
            }

            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismReceivedMessageTextBinding.ivSenderImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    ismReceivedMessageTextBinding.ivSenderImage,
                    position,
                    12
                )
            }
            ismReceivedMessageTextBinding.tvMessageTime.text =
                message.messageTime
            
            // Set text message with clickable links
            val textMessage = message.textMessage?.toString() ?: ""
            val spannableText = SpannableString(textMessage)
            LinkPreviewUtil.makeLinksClickable(spannableText)
            ismReceivedMessageTextBinding.tvTextMessage.text = spannableText
            ismReceivedMessageTextBinding.tvTextMessage.movementMethod =
                LinkMovementMethod.getInstance()
            
            // Handle link preview
            handleLinkPreview(mContext, textMessage, ismReceivedMessageTextBinding, multipleMessagesSelectModeOn)
            
            ismReceivedMessageTextBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/false) { // not required
                ismReceivedMessageTextBinding.ivReaction.visibility =
                    View.GONE
            }
        } catch (ignore: Exception) {
        }
    }

    private fun handleLinkPreview(
        context: Context,
        textMessage: String,
        binding: IsmReceivedMessageTextBinding,
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