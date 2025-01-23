package io.isometrik.samples.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import io.isometrik.samples.chat.databinding.ChatItemBinding
import io.isometrik.samples.chat.databinding.CustomTopViewBinding
import io.isometrik.ui.conversations.list.ChatListItemBinder
import io.isometrik.ui.conversations.list.ConversationsListFragment
import io.isometrik.ui.conversations.list.ConversationsModel
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.AttachmentsConfig
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.chat.common.ChatTopViewHandler

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


//        ChatConfig.topViewHandler = MyCustomTopViewHandler()

//        ChatConfig.baseColor = R.color.ism_test_base
//        ChatConfig.chatBackGroundColor = R.color.ism_theme_base
//        ChatConfig.noConversationsStringResId = R.string.your_text
//        ChatConfig.noConversationsImageResId = R.drawable.your_image
//        ChatConfig.noConversationsImageResId = R.drawable.your_image
        ChatConfig.hideCreateChatOption = false
//        ChatConfig.hideAudioCallOption = true
//        ChatConfig.hideVideoCallOption = true
//        ChatConfig.hideCaptureCameraOption = true
        ChatConfig.hideRecordAudioOption = false

        AttachmentsConfig.hideCameraOption = true
        AttachmentsConfig.hideRecordVideoOption = true
//        AttachmentsConfig.hidePhotosOption  = true
//        AttachmentsConfig.hideVideosOption = true
//        AttachmentsConfig.hideFilesOption = true
//        AttachmentsConfig.hideLocationOption = true
//        AttachmentsConfig.hideContactOption = true
//        AttachmentsConfig.hideStickerOption = true
//        AttachmentsConfig.hideGIFOption = true


    }

    private fun loadChatFragment() {
        val customBinder = object : ChatListItemBinder<ConversationsModel, ChatItemBinding> {
            override fun createBinding(parent: ViewGroup): ChatItemBinding {
                return ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }

            override fun bindData(context: Context, binding: ChatItemBinding, data: ConversationsModel) {
                binding.chatName.text = data.lastMessageText
                binding.chatLastMessage.text = data.customType
                Log.e(data.lastMessageText,"Status delivered: "+data.isDeliveredToAll+" Type:"+data.customType)

            }
        }

        val chatFragment = ConversationsListFragment.newInstance(customBinder)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, chatFragment)
            .commit()
    }

}