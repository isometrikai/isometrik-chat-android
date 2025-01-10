package io.isometrik.samples.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.isometrik.chat.utils.enums.MessageTypeUi
import io.isometrik.samples.chat.databinding.ChatItemBinding
import io.isometrik.samples.chat.databinding.CustomTopViewBinding
import io.isometrik.ui.conversations.list.ChatListItemBinder
import io.isometrik.ui.conversations.list.ConversationsListFragment
import io.isometrik.ui.conversations.list.ConversationsModel
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.chat.common.ChatTopViewHandler
import io.isometrik.ui.messages.chat.common.MessageBinderRegistry

class ChatListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat_list)

        if (savedInstanceState == null) {
            loadChatFragment()
        }

//        MessageBinderRegistry.registerBinder(
//            MessageTypeUi.TEXT_MESSAGE_SENT,
//            CustomTextSentBinder()
//        )

//        MessageBinderRegistry.registerBinder(
//            MessageTypeUi.TEXT_MESSAGE_RECEIVED,
//            CustomTextSentBinder()
//        )

        class MyCustomTopViewHandler : ChatTopViewHandler {

            private var binding: CustomTopViewBinding? = null

            override fun createTopView(parent: ViewGroup): View {
                val inflater = LayoutInflater.from(parent.context)
                binding = CustomTopViewBinding.inflate(inflater, parent, false)
                return binding!!.root
            }

            override fun updateTopView(view: View, message: MessagesModel) {
                binding?.apply {
                    rootView.visibility = View.VISIBLE
                    tvTitle.text = message.textMessage
                }
            }
        }


        ChatConfig.topViewHandler = MyCustomTopViewHandler()

        ChatConfig.baseColor = R.color.ism_theme_base
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

        val chatFragment = ConversationsListFragment.newInstance(customBinder)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, chatFragment)
            .commit()
    }

}