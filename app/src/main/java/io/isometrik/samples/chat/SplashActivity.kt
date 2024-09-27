package io.isometrik.samples.chat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.isometrik.ui.IsometrikUiSdk
import io.isometrik.ui.conversations.list.ConversationsListActivity
import io.isometrik.ui.messages.chat.ChatActionsClickListener
import io.isometrik.ui.users.list.UsersActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = if (IsometrikUiSdk.getInstance().userSession.userToken == null) {
            Intent(this@SplashActivity, UsersActivity::class.java)
        } else {
            Intent(this@SplashActivity, ConversationsListActivity::class.java)
        }
        startActivity(intent)

        IsometrikUiSdk.getInstance().addClickListeners(object : ChatActionsClickListener{
            override fun onNewChatIconClicked() {
               val i = Intent(this@SplashActivity,NewChatActivity::class.java)
                startActivity(i)
            }
        })

        finish()
    }
}