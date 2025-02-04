package io.isometrik.samples.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import io.isometrik.chat.enums.MessageTypeUi
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil
import io.isometrik.samples.chat.databinding.ChatItemBinding
import io.isometrik.samples.chat.databinding.CustomTopViewBinding
import io.isometrik.ui.conversations.list.ChatListItemBinder
import io.isometrik.ui.conversations.list.ConversationsListFragment
import io.isometrik.ui.conversations.list.ConversationsModel
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.AttachmentsConfig
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
//            MessageTypeUi.PAYMENT_ESCROWED_SENT,
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

            override fun updateTopView(view: View, conversationDetailsUtil : ConversationDetailsUtil?, messages: List<MessagesModel>) {
                binding?.apply {
                    rootView.visibility = View.VISIBLE
                    tvTitle.text = conversationDetailsUtil?.conversationTitle
                    Log.e("updateTopView ${conversationDetailsUtil?.conversationTitle}","messages size: ${messages.size}")
                }
            }
        }


//        ChatConfig.topViewHandler = MyCustomTopViewHandler()

//        ChatConfig.baseColor = R.color.ism_test_base
//        ChatConfig.chatBackGroundColor = R.color.ism_theme_base
//        ChatConfig.noConversationsStringResId = R.string.your_text
//        ChatConfig.noConversationsImageResId = R.drawable.your_image
//        ChatConfig.noConversationsImageResId = R.drawable.your_image
        ChatConfig.hideCreateChatOption = false
        ChatConfig.hideAudioCallOption = false
        ChatConfig.hideVideoCallOption = false
        ChatConfig.hideCaptureCameraOption = false
        ChatConfig.hideRecordAudioOption = false
        ChatConfig.disableReScheduleMqtt = true
        ChatConfig.textSentBubbleResId = R.drawable.sent_message_bubble
        ChatConfig.DEFAULT_PLACEHOLDER_IMAGE_URL = "https://www.gravatar.com/avatar/?d=identicon"

        AttachmentsConfig.hideCameraOption = false
        AttachmentsConfig.hideRecordVideoOption = false
//        AttachmentsConfig.hidePhotosOption  = true
//        AttachmentsConfig.hideVideosOption = true
//        AttachmentsConfig.hideFilesOption = true
//        AttachmentsConfig.hideLocationOption = true
//        AttachmentsConfig.hideContactOption = true
//        AttachmentsConfig.hideStickerOption = true
//        AttachmentsConfig.hideGIFOption = true

        ChatConfig.dontShowToastList = arrayListOf("conversation not found")


    }

    private fun loadChatFragment() {
        val customBinder = object : ChatListItemBinder<ConversationsModel, ChatItemBinding> {
            override fun createBinding(parent: ViewGroup): ChatItemBinding {
                return ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }

            override fun bindData(context: Context, binding: ChatItemBinding, data: ConversationsModel) {
                binding.chatName.text = data.lastMessageText
                binding.chatLastMessage.text = data.lastMessageCustomType
                Log.e(data.lastMessageText,"Status delivered: "+data.isLastMessageDeliveredToAll +" Read:"+data.isLastMessageReadByAll)

            }
        }

        val chatFragment = ConversationsListFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, chatFragment)
            .commit()
    }

}