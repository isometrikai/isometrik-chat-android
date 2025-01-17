package io.isometrik.samples.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.isometrik.chat.enums.ConversationType
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.conversations.list.ConversationsListActivity
import io.isometrik.ui.conversations.newconversation.group.NewGroupConversationActivity
import io.isometrik.ui.messages.chat.ChatActionsClickListener
import io.isometrik.ui.users.list.UsersActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = if (IsometrikChatSdk.getInstance().userSession.userToken == null) {
            Intent(this@SplashActivity, UsersActivity::class.java)
        } else {
            Intent(this@SplashActivity, ChatListActivity::class.java)
        }
        startActivity(intent)

        IsometrikChatSdk.getInstance().addClickListeners(object : ChatActionsClickListener {

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
                        ConversationsListActivity::class.java
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

        finish()
    }
}