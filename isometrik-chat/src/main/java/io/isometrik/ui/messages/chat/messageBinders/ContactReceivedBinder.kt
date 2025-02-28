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
import io.isometrik.chat.databinding.IsmReceivedMessageContactBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.reaction.add.MessageReactionsAdapter

class ContactReceivedBinder : MessageItemBinder<MessagesModel, IsmReceivedMessageContactBinding> {

    override fun createBinding(parent: ViewGroup, viewType: Int): IsmReceivedMessageContactBinding {
        return IsmReceivedMessageContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bindData(mContext: Context, ismReceivedMessageContactBinding: IsmReceivedMessageContactBinding,
                          message: MessagesModel, position : Int, multipleMessagesSelectModeOn : Boolean,
                          isMessagingDisabled : Boolean, messageActionCallback : MessageActionCallback
    ) {
        try {
            ismReceivedMessageContactBinding.ivEdited.visibility =
                if (message.isEditedMessage) View.VISIBLE else View.GONE

            if (message.isForwardedMessage) {
                ismReceivedMessageContactBinding.vForwardedMessage.root.visibility =
                    View.VISIBLE

                if (message.forwardedMessageNotes == null) {
                    ismReceivedMessageContactBinding.vForwardedMessageNotes.root.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageContactBinding.vForwardedMessageNotes.tvMessage.text =
                        message.forwardedMessageNotes

                    ismReceivedMessageContactBinding.vForwardedMessageNotes.root.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageContactBinding.vForwardedMessage.root.visibility =
                    View.GONE
                ismReceivedMessageContactBinding.vForwardedMessageNotes.root.visibility =
                    View.GONE
            }

            if (multipleMessagesSelectModeOn) {
                ismReceivedMessageContactBinding.ivSelectedStatus.isSelected =
                    message.isSelected
                ismReceivedMessageContactBinding.ivSelectedStatus.visibility =
                    View.VISIBLE
                ismReceivedMessageContactBinding.ivReaction.visibility =
                    View.GONE
            } else {
                ismReceivedMessageContactBinding.ivSelectedStatus.visibility =
                    View.GONE
                ismReceivedMessageContactBinding.ivReaction.visibility =
                    View.VISIBLE
            }
            if (isMessagingDisabled) {
                ismReceivedMessageContactBinding.ivReaction.visibility =
                    View.GONE
            }
            if (message.isQuotedMessage) {
                ismReceivedMessageContactBinding.vParentMessage.root.visibility =
                    View.VISIBLE
                ismReceivedMessageContactBinding.vParentMessage.tvSenderName.text =
                    message.originalMessageSenderName
                ismReceivedMessageContactBinding.vParentMessage.tvMessage.text =
                    message.originalMessage
                ismReceivedMessageContactBinding.vParentMessage.tvMessageTime.text =
                    message.originalMessageTime
                if (message.originalMessagePlaceholderImage == null) {
                    ismReceivedMessageContactBinding.vParentMessage.ivMessageImage.visibility =
                        View.GONE
                } else {
                    try {
                        Glide.with(mContext)
                            .load(message.originalMessagePlaceholderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(
                                ismReceivedMessageContactBinding.vParentMessage.ivMessageImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                    ismReceivedMessageContactBinding.vParentMessage.ivMessageImage.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageContactBinding.vParentMessage.root.visibility =
                    View.GONE
            }

            if (message.hasReactions()) {
                if (multipleMessagesSelectModeOn) {
                    ismReceivedMessageContactBinding.rvMessageReactions.visibility =
                        View.GONE
                } else {
                    ismReceivedMessageContactBinding.rvMessageReactions.layoutManager =
                        LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
                    ismReceivedMessageContactBinding.rvMessageReactions.adapter =
                        MessageReactionsAdapter(
                            mContext, message.reactions,
                            message.messageId, messageActionCallback::onMessageReactionClicked
                        )

                    ismReceivedMessageContactBinding.rvMessageReactions.visibility =
                        View.VISIBLE
                }
            } else {
                ismReceivedMessageContactBinding.rvMessageReactions.visibility =
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
                ismReceivedMessageContactBinding.tvSenderName.text =
                    spannableString
            } else {
                ismReceivedMessageContactBinding.tvSenderName.text =
                    message.senderName
            }
            if(ChatConfig.hideSenderNameInMessageCell){
                ismReceivedMessageContactBinding.tvSenderName.visibility = View.GONE
                ismReceivedMessageContactBinding.tvComma.visibility = View.GONE
            }
            if (PlaceholderUtils.isValidImageUrl(message.senderImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.senderImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(
                            ismReceivedMessageContactBinding.ivSenderImage
                        )
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.senderName,
                    ismReceivedMessageContactBinding.ivSenderImage,
                    position,
                    12
                )
            }
            if (PlaceholderUtils.isValidImageUrl(message.contactImageUrl)) {
                try {
                    Glide.with(mContext)
                        .load(message.contactImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(
                            ismReceivedMessageContactBinding.ivContactImage
                        )
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    mContext,
                    message.contactName,
                    ismReceivedMessageContactBinding.ivContactImage,
                    12
                )
            }
            if (message.contactList.length() > 1) {
                if (message.contactList.length() == 2) {
                    ismReceivedMessageContactBinding.tvContactName.text =
                        message.contactName + mContext.getString(R.string.ism_and_1_other_contact)
                } else {
                    ismReceivedMessageContactBinding.tvContactName.text =
                        message.contactName + mContext.getString(R.string.ism_and) + (message.contactList.length() - 1) + mContext.getString(
                            R.string.ism_other_contacts
                        )
                }

                ismReceivedMessageContactBinding.tvContactIdentifier.visibility =
                    View.GONE
                ismReceivedMessageContactBinding.divider.visibility =
                    View.VISIBLE
                ismReceivedMessageContactBinding.textViewAll.visibility =
                    View.VISIBLE
            } else {
                ismReceivedMessageContactBinding.tvContactName.text =
                    message.contactName
                ismReceivedMessageContactBinding.tvContactIdentifier.text =
                    message.contactIdentifier
                ismReceivedMessageContactBinding.tvContactIdentifier.visibility =
                    View.VISIBLE
                ismReceivedMessageContactBinding.divider.visibility =
                    View.GONE
                ismReceivedMessageContactBinding.textViewAll.visibility =
                    View.GONE
            }



            ismReceivedMessageContactBinding.tvMessageTime.text =
                message.messageTime

            ismReceivedMessageContactBinding.ivReaction.setOnClickListener { v: View? ->
                messageActionCallback.addReactionForMessage(
                    message.messageId
                )
            }
            if (/*joiningAsObserver*/ false) { // not required as up now
                ismReceivedMessageContactBinding.ivReaction.visibility =
                    View.GONE
            }
            ismReceivedMessageContactBinding.rlContact.setOnClickListener { v: View? ->
                messageActionCallback.handleClickOnMessageCell(
                    message,
                    false
                )
            }
        } catch (ignore: Exception) {
        }
    }
}