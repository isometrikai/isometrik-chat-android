package io.isometrik.ui.conversations.list

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmActivityConversationsBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.conversations.newconversation.type.SelectConversationTypeActivity
import io.isometrik.ui.messages.broadcast.BroadcastMessageActivity
import io.isometrik.ui.messages.mentioned.MentionedMessagesActivity
import io.isometrik.ui.search.SearchActivity
import io.isometrik.ui.users.blockedornonblocked.BlockedOrNonBlockedUsersActivity
import io.isometrik.ui.users.details.UserDetailsActivity
import io.isometrik.ui.users.list.UsersActivity

/**
 * The activity containing fragments for the list of public, open and all conversations of which
 * user is a member.Api calls to fetch unread conversations count, all undelivered messages and
 * latest user details.
 */
class ConversationsActivity : FragmentActivity(), ConversationsContract.View {
    private lateinit var conversationsPresenter: ConversationsContract.Presenter
    private var ismActivityConversationsBinding: IsmActivityConversationsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ismActivityConversationsBinding = IsmActivityConversationsBinding.inflate(
            layoutInflater
        )
        val view: View = ismActivityConversationsBinding!!.root
        setContentView(view)
        conversationsPresenter = ConversationsPresenter(this)
        val conversationsFragmentAdapter =
            ConversationsFragmentAdapter(this@ConversationsActivity)
        ismActivityConversationsBinding!!.vpConversations.adapter = conversationsFragmentAdapter
        ismActivityConversationsBinding!!.vpConversations.offscreenPageLimit = 2
        TabLayoutMediator(
            ismActivityConversationsBinding!!.tabLayout,
            ismActivityConversationsBinding!!.vpConversations
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.setText(getString(R.string.ism_all_conversations))
                        .setIcon(R.drawable.ism_ic_private_group)
                }

                1 -> {
                    tab.setText(getString(R.string.ism_public_conversations))
                        .setIcon(R.drawable.ism_ic_public_group)
                }

                2 -> {
                    tab.setText(getString(R.string.ism_open_conversations))
                        .setIcon(R.drawable.ism_ic_open_chat)
                }
            }
        }.attach()

        try {
            if (!IsometrikChatSdk.getInstance().isometrik.isConnected) {
                IsometrikChatSdk.getInstance()
                    .isometrik
                    .executor
                    .execute {
                        IsometrikChatSdk.getInstance()
                            .isometrik
                            .createConnection(
                                IsometrikChatSdk.getInstance().userSession.userId
                                        + IsometrikChatSdk.getInstance().userSession.deviceId,
                                IsometrikChatSdk.getInstance().userSession.userToken
                            )
                    }
            }
        } catch (ignore: Exception) {
        }
        loadUserImage(IsometrikChatSdk.getInstance().userSession.userProfilePic)

        ismActivityConversationsBinding!!.ivNext.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@ConversationsActivity,
                    SelectConversationTypeActivity::class.java
                )
            )
        }

        ismActivityConversationsBinding!!.ivUserImage.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@ConversationsActivity,
                    UserDetailsActivity::class.java
                )
            )
        }

        ismActivityConversationsBinding!!.ivMentionedMessages.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@ConversationsActivity,
                    MentionedMessagesActivity::class.java
                )
            )
        }

        ismActivityConversationsBinding!!.ivSearch.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@ConversationsActivity,
                    SearchActivity::class.java
                )
            )
        }

        ismActivityConversationsBinding!!.ivMore.setOnClickListener { v: View? ->
            val popup = PopupMenu(
                this,
                v!!
            )
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.ism_home_page_menu, popup.menu)
            popup.setOnMenuItemClickListener { item: MenuItem ->
                if (item.itemId == R.id.broadcast) {
                    startActivity(
                        Intent(
                            this@ConversationsActivity,
                            BroadcastMessageActivity::class.java
                        )
                    )
                    return@setOnMenuItemClickListener true
                } else if (item.itemId == R.id.blocked) {
                    startActivity(
                        Intent(
                            this@ConversationsActivity,
                            BlockedOrNonBlockedUsersActivity::class.java
                        )
                    )
                    return@setOnMenuItemClickListener true
                }
                false
            }
            popup.show()
        }

        IsometrikChatSdk.getInstance().isometrik.executor.execute {
            fetchUnreadConversationsCount()
            conversationsPresenter.fetchUserDetails()
        }

        askNotificationPermission()
        NotificationManagerCompat.from(this).cancelAll()
        fetchAllUndeliveredMessages()
    }

    override fun onUnreadConversationsCountFetchedSuccessfully(count: Int) {
        runOnUiThread {
            try {
                if (ismActivityConversationsBinding!!.tabLayout.getTabAt(0) != null) {
                    val badgeDrawable =
                        ismActivityConversationsBinding!!.tabLayout.getTabAt(0)!!.getOrCreateBadge()

                    if (count > 0) {
                        badgeDrawable.badgeTextColor = Color.WHITE
                        badgeDrawable.backgroundColor =
                            ContextCompat.getColor(this, R.color.ism_select_text_blue)
                        badgeDrawable.isVisible = true
                        badgeDrawable.number = count
                    } else {
                        badgeDrawable.isVisible = false
                    }
                }
            } catch (ignore: NullPointerException) {
            }
        }
    }

    override fun onError(errorMessage: String?) {
        runOnUiThread {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.ism_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Fetch unread conversations count.
     */
    fun fetchUnreadConversationsCount() {
        conversationsPresenter!!.fetchUnreadConversationsCount()
    }

    /**
     * Connection state changed.
     *
     * @param connected the connected
     */
    fun connectionStateChanged(connected: Boolean) {
        runOnUiThread {
            ismActivityConversationsBinding!!.incConnectionState.tvConnectionState.visibility =
                if (connected) View.GONE else View.VISIBLE
        }
        if (connected) {
            fetchAllUndeliveredMessages()
        }
    }

    /**
     * Load user image.
     *
     * @param userProfileImageUrl the user profile image url
     */
    fun loadUserImage(userProfileImageUrl: String?) {
        runOnUiThread {
            if (PlaceholderUtils.isValidImageUrl(userProfileImageUrl)) {
                try {
                    Glide.with(this@ConversationsActivity)
                        .load(userProfileImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismActivityConversationsBinding!!.ivUserImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    this@ConversationsActivity,
                    IsometrikChatSdk.getInstance().userSession.userName,
                    ismActivityConversationsBinding!!.ivUserImage, 13
                )
            }
        }
    }

    override fun onUserDeleted() {
        startActivity(
            Intent(this@ConversationsActivity, UsersActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
        finish()
    }

    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean? ->
        if (!isGranted!!) {
            Toast.makeText(
                this,
                getString(R.string.ism_notification_permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    AlertDialog.Builder(this).setTitle(getString(R.string.ism_forward_description))
                        .setMessage(
                            getString(R.string.ism_notification_permission_ask)
                        )
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ism_ok)) { dialog: DialogInterface, id: Int ->
                            dialog.cancel()
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        .setNegativeButton(
                            getString(R.string.ism_no_thanks)
                        ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
                        .create()
                        .show()
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    /**
     * Fetch all undelivered messages.
     */
    fun fetchAllUndeliveredMessages() {
        IsometrikChatSdk.getInstance()
            .isometrik
            .executor
            .execute { conversationsPresenter!!.fetchAllUndeliveredMessages(0) }
    }
}
