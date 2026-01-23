package io.isometrik.ui.conversations.list

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmConversationItemBinding
import io.isometrik.chat.utils.PlaceholderUtils

class DefaultChatListItemBinder :
        ChatListItemBinder<ConversationsModel, IsmConversationItemBinding> {
    val hideMessagekeywords = listOf("blocked you", "unblocked you")

    override fun createBinding(parent: ViewGroup): IsmConversationItemBinding {
        val inflater = LayoutInflater.from(parent.context)
        return IsmConversationItemBinding.inflate(inflater, parent, false)
    }

    override fun bindData(
            mContext: Context,
            ismConversationItemBinding: IsmConversationItemBinding,
            conversationsModel: ConversationsModel
    ) {

        try {
            if (conversationsModel.isMessagingDisabled) {
                val spannableString = SpannableString(conversationsModel.conversationTitle)
                spannableString.setSpan(StyleSpan(Typeface.ITALIC), 0, spannableString.length, 0)
                ismConversationItemBinding.tvConversationTitle.text = spannableString
            } else {
                ismConversationItemBinding.tvConversationTitle.text =
                        conversationsModel.conversationTitle
            }
            val lastMessageText = conversationsModel.lastMessageText
            if (lastMessageText != null && !containsKeyword(lastMessageText, hideMessagekeywords)) {
                ismConversationItemBinding.tvLastMessage.text = lastMessageText
            } else if (lastMessageText == null) {
                ismConversationItemBinding.tvLastMessage.text = ""
            }
            if (conversationsModel.isCanJoin) {
                ismConversationItemBinding.tvJoinConversation.visibility = View.VISIBLE
            } else {
                ismConversationItemBinding.tvJoinConversation.visibility = View.GONE
            }

            ismConversationItemBinding.tvLastMessageTime.text = conversationsModel.lastMessageTime

            if (conversationsModel.unreadMessagesCount > 0) {
                ismConversationItemBinding.tvUnreadMessagesCount.text =
                        if (conversationsModel.unreadMessagesCount > 99)
                                mContext.getString(R.string.ism_hundred_unread_count)
                        else conversationsModel.unreadMessagesCount.toString()
                ismConversationItemBinding.tvUnreadMessagesCount.visibility = View.VISIBLE
            } else {
                ismConversationItemBinding.tvUnreadMessagesCount.visibility = View.GONE
            }
            if (PlaceholderUtils.isValidImageUrl(conversationsModel.conversationImageUrl)) {
                try {
                    Glide.with(mContext)
                            .load(conversationsModel.conversationImageUrl)
                            .placeholder(R.drawable.ism_ic_profile)
                            .transform(CircleCrop())
                            .into(ismConversationItemBinding.ivConversationImage)
                } catch (ignore: IllegalArgumentException) {} catch (
                        ignore: NullPointerException) {}
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                        mContext,
                        conversationsModel.conversationTitle,
                        ismConversationItemBinding.ivConversationImage, /*position*/
                        -1,
                        16
                )
            }
            if (conversationsModel.isPrivateOneToOneConversation) {
                if (conversationsModel.isMessagingDisabled) {
                    ismConversationItemBinding.ivOnlineStatus.setImageDrawable(
                            ContextCompat.getDrawable(
                                    mContext,
                                    R.drawable.ism_ic_messaging_disabled
                            )
                    )
                } else {
                    if (conversationsModel.isOnline) {
                        ismConversationItemBinding.ivOnlineStatus.setImageDrawable(
                                ContextCompat.getDrawable(
                                        mContext,
                                        R.drawable.ism_user_online_status_circle
                                )
                        )
                    } else {
                        ismConversationItemBinding.ivOnlineStatus.setImageDrawable(
                                ContextCompat.getDrawable(
                                        mContext,
                                        R.drawable.ism_user_offline_status_circle
                                )
                        )
                    }
                }
                ismConversationItemBinding.ivOnlineStatus.visibility = View.VISIBLE
            } else {
                if (conversationsModel.lastMessageSendersProfileImageUrl != null) {
                    if (PlaceholderUtils.isValidImageUrl(
                                    conversationsModel.lastMessageSendersProfileImageUrl
                            )
                    ) {
                        try {
                            Glide.with(mContext)
                                    .load(conversationsModel.lastMessageSendersProfileImageUrl)
                                    .placeholder(R.drawable.ism_ic_profile)
                                    .transform(CircleCrop())
                                    .into(ismConversationItemBinding.ivOnlineStatus)
                        } catch (ignore: IllegalArgumentException) {} catch (
                                ignore: NullPointerException) {}
                    } else {
                        PlaceholderUtils.setTextRoundDrawable(
                                mContext,
                                conversationsModel.lastMessageSenderName,
                                ismConversationItemBinding.ivOnlineStatus, /*position + 1*/
                                -1,
                                5
                        )
                    }
                    ismConversationItemBinding.ivOnlineStatus.visibility = View.VISIBLE
                } else {
                    ismConversationItemBinding.ivOnlineStatus.visibility = View.GONE
                }
            }

            if (conversationsModel.lastMessagePlaceHolderImage != null) {
                try {
                    Glide.with(mContext)
                            .load(conversationsModel.lastMessagePlaceHolderImage)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(ismConversationItemBinding.ivLastMessageType)
                } catch (ignore: IllegalArgumentException) {} catch (
                        ignore: NullPointerException) {}

                if (conversationsModel.isLastMessageWasReactionMessage) {
                    ismConversationItemBinding.ivLastMessageType.clearColorFilter()
                } else {
                    ismConversationItemBinding.ivLastMessageType.setColorFilter(
                            ContextCompat.getColor(mContext, R.color.ism_last_message_grey),
                            PorterDuff.Mode.SRC_IN
                    )
                }

                ismConversationItemBinding.ivLastMessageType.visibility = View.VISIBLE
            } else {
                ismConversationItemBinding.ivLastMessageType.visibility = View.GONE
            }

            if (conversationsModel.isRemoteUserTyping) {
                ismConversationItemBinding.tvLastMessage.visibility = View.GONE
                ismConversationItemBinding.ivLastMessageType.visibility = View.GONE
                ismConversationItemBinding.tvTypingMessage.text =
                        conversationsModel.remoteUserTypingMessage
                ismConversationItemBinding.tvTypingMessage.visibility = View.VISIBLE
            } else {
                ismConversationItemBinding.tvTypingMessage.visibility = View.GONE
                ismConversationItemBinding.tvLastMessage.visibility = View.VISIBLE
                if (conversationsModel.lastMessagePlaceHolderImage != null) {
                    ismConversationItemBinding.ivLastMessageType.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun containsKeyword(sentence: String, keywords: List<String>): Boolean {
        return keywords.any { keyword -> sentence.contains(keyword, ignoreCase = true) }
    }
}
