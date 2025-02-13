package io.isometrik.samples.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import io.isometrik.chat.enums.ConversationType
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil
import io.isometrik.samples.chat.databinding.CustomTopViewBinding
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.conversations.list.ConversationsListActivity
import io.isometrik.ui.conversations.newconversation.group.NewGroupConversationActivity
import io.isometrik.ui.conversations.newconversation.onetoone.NewOneToOneConversationActivity
import io.isometrik.ui.messages.chat.ChatActionsClickListener
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.chat.common.ChatTopViewHandler
import io.isometrik.ui.users.list.UsersActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = if (IsometrikChatSdk.instance.userSession.userToken == null) {
            Intent(this@SplashActivity, UsersActivity::class.java)
        } else {
            Intent(this@SplashActivity, ChatListActivity::class.java)
        }
        startActivity(intent)

        IsometrikChatSdk.instance.addClickListeners(object : ChatActionsClickListener {

            override fun onCreateChatIconClicked(isGroup: Boolean) {
                if (isGroup) {
                      val i = Intent(
                        this@SplashActivity,
                        NewGroupConversationActivity::class.java
                    )
                    i.putExtra("conversationType", ConversationType.PrivateConversation.value)
                    startActivity(i)
                } else {
                    val i = Intent(
                        this@SplashActivity,
                        NewOneToOneConversationActivity::class.java
                    )
                    startActivity(i)
                }


            }

            override fun onBlockStatusUpdate(isBlocked: Boolean, userId: String) {
            }

            override fun onCallClicked(
                isAudio: Boolean,
                userId: String,
                meetingDescription: String,
                opponentName: String,
                opponentImageUrl: String
            ) {

            }

            override fun onSharedPostClick(postId: String) {

            }

            override fun onViewSocialProfileClick(userId: String) {

            }
        })

//        class MyCustomTopViewHandler : ChatTopViewHandler {
//
//            private var binding: CustomTopViewBinding? = null
//
//            override fun createTopView(parent: ViewGroup): View {
//                val inflater = LayoutInflater.from(parent.context)
//                binding = CustomTopViewBinding.inflate(inflater, parent, false)
//                return binding!!.root
//            }
//
//            override fun updateTopView(view: View, conversationDetailsUtil : ConversationDetailsUtil?, messages: List<MessagesModel>) {
//                binding?.apply {
//                    rootView.visibility = View.VISIBLE
//                    tvTitle.text = conversationDetailsUtil?.conversationTitle
//                }
//            }
//        }
//
//
//        ChatConfig.topViewHandler = MyCustomTopViewHandler()

        finish()
    }
}