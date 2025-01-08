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
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmReceivedMessageLocationBinding
import io.isometrik.chat.utils.Constants
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessageItemBinder
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class LocationReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageLocationBinding> {

    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): IsmReceivedMessageLocationBinding {
        return IsmReceivedMessageLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bindData(
        mContext: Context, ismReceivedMessageLocationBinding: IsmReceivedMessageLocationBinding,
        message: MessagesModel, position: Int, multipleMessagesSelectModeOn: Boolean,
        isMessagingDisabled: Boolean, messageActionCallback: MessageActionCallback
    ) {
        try {
            val cornerRadius = (13 * mContext.resources.displayMetrics.density).toInt()

            ismReceivedMessageLocationBinding.ivEdited.setVisibility(
                if (message.isEditedMessage) View.VISIBLE else View.GONE
            )

            if (message.isForwardedMessage) {
                ismReceivedMessageLocationBinding.vForwardedMessage.getRoot()
                    .setVisibility(View.VISIBLE)

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageLocationBinding.vForwardedMessageNotes.getRoot()
                        .setVisibility(View.GONE)
                } else {
                    ismReceivedMessageLocationBinding.vForwardedMessageNotes.tvMessage
                        .setText(message.forwardedMessageNotes)

                    ismReceivedMessageLocationBinding.vForwardedMessageNotes.getRoot()
                        .setVisibility(View.VISIBLE)
                }
            } else {
                ismReceivedMessageLocationBinding.vForwardedMessage.getRoot()
                    .setVisibility(View.GONE)
                ismReceivedMessageLocationBinding.vForwardedMessageNotes.getRoot()
                    .setVisibility(View.GONE)
            }
            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageLocationBinding.ivSelectedStatus.setSelected(
                    message.isSelected
                )
                ismReceivedMessageLocationBinding.ivSelectedStatus.setVisibility(
                    View.VISIBLE
                )
                ismReceivedMessageLocationBinding.ivReaction.setVisibility(
                    View.GONE
                )
            } else {
                ismReceivedMessageLocationBinding.ivSelectedStatus.setVisibility(
                    View.GONE
                )
                ismReceivedMessageLocationBinding.ivReaction.setVisibility(
                    View.VISIBLE
                )
            }
            if (/*messagingDisabled*/false) { // not required
                ismReceivedMessageLocationBinding.ivReaction.setVisibility(
                    View.GONE
                )
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageLocationBinding.vParentMessage.getRoot()
                    .setVisibility(View.VISIBLE)
                ismReceivedMessageLocationBinding.vParentMessage.tvSenderName
                    .setText(message.originalMessageSenderName)
                ismReceivedMessageLocationBinding.vParentMessage.tvMessage
                    .setText(message.originalMessage)
                ismReceivedMessageLocationBinding.vParentMessage.tvMessageTime
                    .setText(message.originalMessageTime)
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessageLocationBinding.vParentMessage.ivMessageImage
                        .setVisibility(View.GONE)
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageLocationBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageLocationBinding.vParentMessage.ivMessageImage
                        .setVisibility(View.VISIBLE)
                }
            } else {
                ismReceivedMessageLocationBinding.vParentMessage.getRoot()
                    .setVisibility(View.GONE)
            }
            try {
                Glide.with(mContext)
                    .load(Constants.LOCATION_PLACEHOLDER_IMAGE_URL)
                    .placeholder(R.drawable.ism_avatar_group_large)
                    .transform(
                        CenterCrop(),
                        GranularRoundedCorners(
                            0f,
                            0f,
                            cornerRadius.toFloat(),
                            cornerRadius.toFloat()
                        )
                    )
                    .into(
                        ismReceivedMessageLocationBinding.ivLocationImage
                    )
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }
            if (message.hasReactions()) {
                ismReceivedMessageLocationBinding.rvMessageReactions.setLayoutManager(
                    LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                )
                ismReceivedMessageLocationBinding.rvMessageReactions.setAdapter(
                    MessageReactionsAdapter(
                        mContext, message.reactions, message.messageId,
                        messageActionCallback::onMessageReactionClicked
                    )
                )

                ismReceivedMessageLocationBinding.rvMessageReactions.setVisibility(
                    View.VISIBLE
                )
            } else {
                ismReceivedMessageLocationBinding.rvMessageReactions.setVisibility(
                    View.GONE
                )
            }
            if (message.isSenderDeleted) {
                val spannableString = SpannableString(message.senderName)
                spannableString.setSpan(
                    StyleSpan(Typeface.ITALIC),
                    0,
                    spannableString.length,
                    0
                )
                ismReceivedMessageLocationBinding.tvSenderName.setText(
                    spannableString
                )
            } else {
                ismReceivedMessageLocationBinding.tvSenderName.setText(
                    message.senderName
                )
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(
                            ismReceivedMessageLocationBinding.ivSenderImage
                        )
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext, message.senderName,
                    ismReceivedMessageLocationBinding.ivSenderImage,
                    position, 12
                )
            }
            ismReceivedMessageLocationBinding.tvLocationName.setText(
                message.locationName
            )
            ismReceivedMessageLocationBinding.tvLocationDescription.setText(
                message.locationDescription
            )
            ismReceivedMessageLocationBinding.tvMessageTime.setText(
                message.messageTime
            )

            ismReceivedMessageLocationBinding.ivReaction.setOnClickListener { v ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/ false) { // not required
                ismReceivedMessageLocationBinding.ivReaction.setVisibility(
                    View.GONE
                )
            }
            ismReceivedMessageLocationBinding.rlLocationLayout.setOnClickListener { v ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    false
                )
            }
        } catch (ignore: Exception) {
        }
    }
}