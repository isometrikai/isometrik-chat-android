package io.isometrik.ui.conversations.list

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmActivityConversationsListBinding
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.IsometrikUiSdk
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
class ConversationsListActivity : FragmentActivity(), ConversationsContract.View {
    private var conversationsPresenter: ConversationsContract.Presenter? = null
    private var ismActivityConversationsListBinding: IsmActivityConversationsListBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ismActivityConversationsListBinding = IsmActivityConversationsListBinding.inflate(
            layoutInflater
        )
        val view: View = ismActivityConversationsListBinding!!.root
        setContentView(view)
        conversationsPresenter = ConversationsPresenter(this)

        val fragment = ConversationsListFragment()
//        val args = Bundle()
//        args.putInt("conversationType", ConversationType.AllConversations.value)
//        fragment.arguments = args

        loadFragment(fragment)


        try {
            if (!IsometrikUiSdk.getInstance().isometrik.isConnected) {
                IsometrikUiSdk.getInstance()
                    .isometrik
                    .executor
                    .execute {
                        IsometrikUiSdk.getInstance()
                            .isometrik
                            .createConnection(
                                IsometrikUiSdk.getInstance().userSession.userId
                                        + IsometrikUiSdk.getInstance().userSession.deviceId,
                                IsometrikUiSdk.getInstance().userSession.userToken
                            )
                    }
            }
        } catch (ignore: Exception) {
        }
        loadUserImage(IsometrikUiSdk.getInstance().userSession.userProfilePic)

        ismActivityConversationsListBinding!!.ivNext.setOnClickListener { v: View? ->
            startActivity(
                Intent(this@ConversationsListActivity, SelectConversationTypeActivity::class.java)
            )
        }

        ismActivityConversationsListBinding!!.ivUserImage.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@ConversationsListActivity, UserDetailsActivity::class.java
                )
            )
        }

        ismActivityConversationsListBinding!!.ivMentionedMessages.setOnClickListener { v: View? ->
            startActivity(
                Intent(this@ConversationsListActivity, MentionedMessagesActivity::class.java)
            )
        }

        ismActivityConversationsListBinding!!.ivSearch.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@ConversationsListActivity, SearchActivity::class.java
                )
            )
        }

        ismActivityConversationsListBinding!!.ivMore.setOnClickListener { v: View? ->
            val popup = PopupMenu(this, v!!)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.ism_home_page_menu, popup.menu)
            popup.setOnMenuItemClickListener { item: MenuItem ->
                if (item.itemId == R.id.broadcast) {
                    startActivity(
                        Intent(
                            this@ConversationsListActivity,
                            BroadcastMessageActivity::class.java
                        )
                    )
                    return@setOnMenuItemClickListener true
                } else if (item.itemId == R.id.blocked) {
                    startActivity(
                        Intent(
                            this@ConversationsListActivity,
                            BlockedOrNonBlockedUsersActivity::class.java
                        )
                    )
                    return@setOnMenuItemClickListener true
                }
                false
            }
            popup.show()
        }

        // As up now we are not showing unread count at bottom tab bar

//        IsometrikUiSdk.getInstance().isometrik.executor.execute {
//            fetchUnreadConversationsCount()
//            conversationsPresenter?.fetchUserDetails()
//        }
        askNotificationPermission()
        NotificationManagerCompat.from(this).cancelAll()
        fetchAllUndeliveredMessages()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onUnreadConversationsCountFetchedSuccessfully(count: Int) {
    }

    override fun onError(errorMessage: String) {
        runOnUiThread {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show()
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
            ismActivityConversationsListBinding!!.incConnectionState.tvConnectionState.visibility =
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
                    Glide.with(this@ConversationsListActivity)
                        .load(userProfileImageUrl)
                        .placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop())
                        .into(ismActivityConversationsListBinding!!.ivUserImage)
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    this@ConversationsListActivity,
                    IsometrikUiSdk.getInstance().userSession.userName,
                    ismActivityConversationsListBinding!!.ivUserImage, 13
                )
            }
        }
    }

    override fun onUserDeleted() {
        startActivity(
            Intent(this@ConversationsListActivity, UsersActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
        finish()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean? ->
            if (!isGranted!!) {
                Toast.makeText(
                    this, getString(R.string.ism_notification_permission_denied),
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
                        .setNegativeButton(getString(R.string.ism_no_thanks)) { dialog: DialogInterface, id: Int -> dialog.cancel() }
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
        IsometrikUiSdk.getInstance()
            .isometrik
            .executor
            .execute { conversationsPresenter!!.fetchAllUndeliveredMessages(0) }
    }
}
