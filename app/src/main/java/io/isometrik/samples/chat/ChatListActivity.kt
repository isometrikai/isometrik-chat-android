package io.isometrik.samples.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import io.isometrik.chat.utils.enums.MessageTypeUi
import io.isometrik.samples.chat.databinding.ChatItemBinding
import io.isometrik.ui.conversations.list.ChatListItemBinder
import io.isometrik.ui.conversations.list.ConversationsListFragment
import io.isometrik.ui.conversations.list.ConversationsModel
import io.isometrik.ui.messages.chat.ConversationMessagesAdapter
import io.isometrik.ui.messages.chat.MessageBinderRegistry

class ChatListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat_list)

        if (savedInstanceState == null) {
            loadChatFragment()
        }

        MessageBinderRegistry.registerBinder(
            MessageTypeUi.TEXT_MESSAGE_SENT,
            CustomTextSentBinder()
        )

        MessageBinderRegistry.registerBinder(
            MessageTypeUi.TEXT_MESSAGE_RECEIVED,
            CustomTextSentBinder()
        )
    }

    private fun loadChatFragment() {
        val customBinder = object : ChatListItemBinder<ConversationsModel, ChatItemBinding> {
            override fun createBinding(parent: ViewGroup): ChatItemBinding {
                return ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }

            override fun bindData(context: Context, binding: ChatItemBinding, data: ConversationsModel) {
                binding.chatName.text = data.conversationTitle
                binding.chatLastMessage.text = data.lastMessageSenderName

            }
        }

        val chatFragment = ConversationsListFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, chatFragment)
            .commit()
    }

}