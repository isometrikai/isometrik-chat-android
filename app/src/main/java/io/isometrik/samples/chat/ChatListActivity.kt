package io.isometrik.samples.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import io.isometrik.chat.enums.CustomMessageTypes
import io.isometrik.chat.enums.MessageTypeUi
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil
import io.isometrik.samples.chat.databinding.ChatItemBinding
import io.isometrik.samples.chat.databinding.CustomTopViewBinding
import io.isometrik.samples.chat.upload.TusPreferencesURLStore
import io.isometrik.ui.IsometrikChatSdk.Companion.instance
import io.isometrik.ui.conversations.list.ChatListItemBinder
import io.isometrik.ui.conversations.list.ConversationsListFragment
import io.isometrik.ui.conversations.list.ConversationsModel
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.AttachmentsConfig
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.chat.common.ChatTopViewHandler
import io.isometrik.ui.messages.chat.common.MessageBinderRegistry
import java.net.URL


class ChatListActivity : AppCompatActivity() {
//    private lateinit var client: TusClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat_list)

        if (savedInstanceState == null) {
            loadChatFragment()
        }

//        try {
//            val pref = getSharedPreferences("tus", 0)
//            client = TusClient()
//            client.setUploadCreationURL(URL("url"))
//            // Set custom headers directly on the TUS client
//            val headers: MutableMap<String, String> = HashMap()
//            headers["authorization"] = MyApplication.getInstance().getApiToken()
//            headers["lang"] = "en"
//            // Set the headers on the TUS client
//            client.setHeaders(headers)
//            client.enableResuming(TusPreferencesURLStore(pref))
//        } catch (e: Exception) {
//        }


        CustomMessageTypes.registerCustomType(
            typeName = "SimpleText",
            value = "AttachmentMessage:Text"
        )

      // Register custom binders
        CustomMessageTypes.registerCustomBinder(
            value = "AttachmentMessage:Text",
            sentBinder = CustomTextSentBinder(),
            receivedBinder = CustomTextSentBinder()
        )


//        MessageBinderRegistry.registerBinder(
//            MessageTypeUi.PAYMENT_ESCROWED_SENT,
//            CustomTextSentBinder()
//        )
//
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
                    Log.e("updateTopView ${conversationDetailsUtil?.conversationTitle}","conversationId: ${conversationDetailsUtil?.conversationId}")
                }
            }
        }


        ChatConfig.topViewHandler = MyCustomTopViewHandler()

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
        ChatConfig.hideSubHeader = true
        ChatConfig.textSentBubbleResId = R.drawable.sent_message_bubble
//        ChatConfig.DEFAULT_PLACEHOLDER_IMAGE_URL = "https://www.gravatar.com/avatar/?d=identicon"

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

//        CustomUploadHandler.registerUploadHandler { mediaId: String, mediaPath: String, callback: Consumer<UploadedMediaResponse?> ->
//            CoroutineScope(Dispatchers.IO).launch {
//                val uploadManager = UploadManager(client)
//              val result = startUploadingVideoAndThumbnail(
//                    mediaPath,
//                    "video",
//                    uploadManager,
//                    true
//                )
//
//
//                  System.out.println("Uploaded video URL: " + result.videoUrl)
//                  System.out.println("Uploaded thumbnail URL: " + result.thumbnailUrl)
//
//                  val response = UploadedMediaResponse(mediaId, result.videoUrl, result.thumbnailUrl)
//                  callback.accept(response)
//
//            }
//
////            try {
////                Thread.sleep(2000) // Simulating network delay
////
////                // Replace this with actual API call for media upload
////                val mediaUrl = "https://picsum.photos/300/200"
////                val thumbnailUrl = "https://picsum.photos/200/100"
////
////                val response = UploadedMediaResponse(mediaId, mediaUrl, thumbnailUrl)
////                callback.accept(response)
////            } catch (e: InterruptedException) {
////                e.printStackTrace()
////                callback.accept(null)
////            }
//
//        }


//        Handler(Looper.getMainLooper()).postDelayed({
//            ConversationMessagesActivity.openBrowser(this,"https://assets.platform.kwibal.com/invoice/asset/buyerOD1738672668020062C.pdf")
//        },15000)


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

    override fun onDestroy() {
        super.onDestroy()
        instance.onTerminate()
    }

}