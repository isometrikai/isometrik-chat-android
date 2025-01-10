package io.isometrik.ui.conversations.list

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmFragmentConversationsBinding
import io.isometrik.chat.enums.ConversationType
import io.isometrik.chat.utils.AlertProgress
import io.isometrik.chat.utils.RecyclerItemClickListener
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.conversations.newconversation.type.SelectConversationTypeBottomSheet
import io.isometrik.ui.messages.chat.ConversationMessagesActivity
import io.isometrik.ui.messages.chat.common.ChatConfig

/**
 * The fragment to fetch list of public/open and all conversations with paging, search and pull to
 * refresh
 * option, join public conversation and update ui on realtime message or action message in a conversation.
 */
class ConversationsListFragment : Fragment(), ConversationsListContract.View {
    private var itemBinder: ChatListItemBinder<ConversationsModel, out ViewBinding>? = null
    private var conversationsLayoutManager: LinearLayoutManager? = null

    private lateinit var conversationsListPresenter: ConversationsListContract.Presenter
    private val conversations = ArrayList<ConversationsModel>()
    private var unregisteredListeners = false

    private lateinit var conversationsAdapter: ConversationsAdapter<ConversationsModel, out ViewBinding>
    private var alertProgress: AlertProgress? = null
    private var alertDialog: AlertDialog? = null

    private var ismFragmentConversationsBinding: IsmFragmentConversationsBinding? = null
    private var conversationType = ConversationType.AllConversations

    private var handler: Handler? = null

    companion object {
        fun newInstance(customBinder: ChatListItemBinder<ConversationsModel, out ViewBinding>? = null): ConversationsListFragment {
            return ConversationsListFragment().apply {
                this.itemBinder = customBinder
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        conversationsListPresenter = ConversationsListPresenter()
        conversationsListPresenter.attachView(this)

        val args = arguments
        if (args != null) {
            when (args.getInt("conversationType")) {
                3 -> {
                    conversationType = ConversationType.AllConversations
                }

                1 -> {
                    conversationType = ConversationType.PublicConversation
                }

                2 -> {
                    conversationType = ConversationType.OpenConversation
                }
            }
        }
        conversationsListPresenter.initialize(conversationType)
        if (conversationType == ConversationType.AllConversations) {
            handler = Handler()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ismFragmentConversationsBinding =
            IsmFragmentConversationsBinding.inflate(inflater, container, false)
        alertProgress = AlertProgress()

        updateShimmerVisibility(true)
        conversationsLayoutManager = LinearLayoutManager(activity)

        ismFragmentConversationsBinding!!.rvConversations.layoutManager = conversationsLayoutManager

        val binder = itemBinder ?: DefaultChatListItemBinder()
        conversationsAdapter =
            ConversationsAdapter(requireActivity(), conversations, binder) { data ->
                joinConversation(data)
            }

        ismFragmentConversationsBinding!!.rvConversations.addOnScrollListener(
            conversationsOnScrollListener
        )

        ismFragmentConversationsBinding!!.rvConversations.adapter = conversationsAdapter

        fetchConversations(false, null, false, false)
        if (conversationType == ConversationType.AllConversations) {
            conversationsListPresenter!!.registerConnectionEventListener()
            conversationsListPresenter!!.registerConversationEventListener()
            conversationsListPresenter!!.registerMessageEventListener()
            conversationsListPresenter!!.registerMembershipControlEventListener()
            conversationsListPresenter!!.registerReactionEventListener()
            conversationsListPresenter!!.registerUserEventListener()
        } else if (conversationType == ConversationType.PublicConversation) {
            ismFragmentConversationsBinding!!.etSearch.hint =
                getString(R.string.ism_search_public_conversations)
        } else if (conversationType == ConversationType.OpenConversation) {
            ismFragmentConversationsBinding!!.etSearch.hint =
                getString(R.string.ism_search_open_conversations)
        }
        ismFragmentConversationsBinding!!.rvConversations.addOnItemTouchListener(
            RecyclerItemClickListener(
                activity,
                ismFragmentConversationsBinding!!.rvConversations,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (conversationType != ConversationType.PublicConversation) {
                            if (position >= 0) {
                                openMessagesScreen(conversations[position])
                            }
                        }
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                    }
                })
        )

        ismFragmentConversationsBinding!!.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0) {
                    fetchConversations(true, s.toString(), false, false)
                } else {
                    fetchConversations(false, null, false, false)
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        ismFragmentConversationsBinding!!.refresh.setOnRefreshListener {
            fetchConversations(
                false,
                null,
                true,
                true
            )
        }
        ismFragmentConversationsBinding!!.ivAdd.setOnClickListener { v: View? ->
            val bottomSheet = SelectConversationTypeBottomSheet()
            bottomSheet.show(
                requireActivity().supportFragmentManager,
                "SelectConversationTypeBottomSheet"
            )
        }

        ismFragmentConversationsBinding!!.ivNoConversations.setImageResource(ChatConfig.noConversationsImageResId)
        ismFragmentConversationsBinding!!.ivAdd.visibility =
            if (ChatConfig.hideCreateChatOption) View.GONE else View.VISIBLE

        return ismFragmentConversationsBinding!!.root
    }

    override fun onDestroy() {
        unregisterListeners()
        super.onDestroy()
        conversationsListPresenter!!.detachView()
    }

    override fun onDestroyView() {
        unregisterListeners()
        super.onDestroyView()
        ismFragmentConversationsBinding = null
    }

    private val conversationsOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                conversationsListPresenter!!.fetchConversationsOnScroll(
                    conversationsLayoutManager!!.findFirstVisibleItemPosition(),
                    conversationsLayoutManager!!.childCount, conversationsLayoutManager!!.itemCount,
                    conversationType
                )
            }
        }

    override fun onError(errorMessage: String?) {
        if (ismFragmentConversationsBinding!!.refresh.isRefreshing) {
            ismFragmentConversationsBinding!!.refresh.isRefreshing = false
        }

        hideProgressDialog()

        updateShimmerVisibility(false)
        if (activity != null) {
            requireActivity().runOnUiThread {
                if (errorMessage != null) {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onConversationsFetchedSuccessfully(
        conversationsModels: ArrayList<ConversationsModel>,
        resultsOnScroll: Boolean
    ) {
        if (!resultsOnScroll) {
            conversations.clear()
        }
        conversations.addAll(conversationsModels)
        if (activity != null) {
            requireActivity().runOnUiThread {
                if (!resultsOnScroll) {
                    if (conversations.size > 0) {
                        ismFragmentConversationsBinding!!.lnrNoConversations.visibility =
                            View.GONE
                        ismFragmentConversationsBinding!!.rvConversations.visibility =
                            View.VISIBLE
                    } else {
                        ismFragmentConversationsBinding!!.lnrNoConversations.visibility =
                            View.VISIBLE

                        if (conversationType == ConversationType.AllConversations) {
                            ismFragmentConversationsBinding!!.tvNoConversations.text =
                                getString(ChatConfig.noConversationsStringResId)
                        } else if (conversationType == ConversationType.PublicConversation) {
                            ismFragmentConversationsBinding!!.tvNoConversations.text =
                                getString(R.string.ism_no_public_conversations)
                        } else if (conversationType == ConversationType.OpenConversation) {
                            ismFragmentConversationsBinding!!.tvNoConversations.text =
                                getString(R.string.ism_no_open_conversations)
                        }

                        ismFragmentConversationsBinding!!.rvConversations.visibility =
                            View.GONE
                    }
                }
                conversationsAdapter!!.notifyDataSetChanged()

                hideProgressDialog()
                if (ismFragmentConversationsBinding!!.refresh.isRefreshing) {
                    ismFragmentConversationsBinding!!.refresh.isRefreshing = false
                }
                updateShimmerVisibility(false)
            }
        }
    }

    private fun fetchConversations(
        isSearchRequest: Boolean, searchTag: String?,
        showProgressDialog: Boolean, isPullToRefresh: Boolean
    ) {
        if (showProgressDialog) {
            if (conversationType == ConversationType.AllConversations) {
                showProgressDialog(getString(R.string.ism_fetching_conversations))
            } else if (conversationType == ConversationType.PublicConversation) {
                showProgressDialog(getString(R.string.ism_fetching_public_conversations))
            } else if (conversationType == ConversationType.OpenConversation) {
                showProgressDialog(getString(R.string.ism_fetching_open_conversations))
            }
        }

        try {
            conversationsListPresenter!!.fetchConversations(
                0, false, conversationType, isSearchRequest,
                searchTag
            )

            if (conversationType == ConversationType.AllConversations && isPullToRefresh) {
                fetchUnreadConversationsCount()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onConversationCreated(
        conversationsModel: ConversationsModel,
        createdByLoggedInUser: Boolean
    ) {
        if (conversationType == ConversationType.AllConversations || !createdByLoggedInUser) {
            if (activity != null) {
                requireActivity().runOnUiThread {
                    if (conversations.size == 0) {
                        ismFragmentConversationsBinding!!.lnrNoConversations.visibility =
                            View.GONE
                        ismFragmentConversationsBinding!!.rvConversations.visibility =
                            View.VISIBLE
                    }
                    conversations.add(0, conversationsModel)
                    conversationsAdapter!!.notifyItemInserted(0)
                }
            }
        }
    }

    override fun removeConversation(conversationId: String) {
        if (activity != null) {
            requireActivity().runOnUiThread {
                val position = conversationsListPresenter!!.fetchConversationPositionInList(
                    conversations,
                    conversationId, true
                )
                if (position != -1) {
                    conversations.removeAt(position)
                    conversationsAdapter!!.notifyItemRemoved(position)
                    if (conversations.size == 0) {
                        ismFragmentConversationsBinding!!.lnrNoConversations.visibility =
                            View.VISIBLE
                        if (conversationType == ConversationType.AllConversations) {
                            ismFragmentConversationsBinding!!.tvNoConversations.text =
                                getString(ChatConfig.noConversationsStringResId)
                        } else if (conversationType == ConversationType.PublicConversation) {
                            ismFragmentConversationsBinding!!.tvNoConversations.text =
                                getString(R.string.ism_no_public_conversations)
                        } else if (conversationType == ConversationType.OpenConversation) {
                            ismFragmentConversationsBinding!!.tvNoConversations.text =
                                getString(R.string.ism_no_open_conversations)
                        }
                        ismFragmentConversationsBinding!!.rvConversations.visibility =
                            View.GONE
                    }
                }
            }
        }
    }

    override fun onConversationTitleUpdated(conversationId: String, newTitle: String) {
        if (activity != null) {
            requireActivity().runOnUiThread {
                val position = conversationsListPresenter!!.fetchConversationPositionInList(
                    conversations,
                    conversationId, true
                )
                if (position != -1) {
                    val conversationsModel = conversations[position]
                    conversationsModel.conversationTitle = newTitle

                    conversations.removeAt(position)
                    conversationsAdapter!!.notifyItemRemoved(position)
                    conversations.add(0, conversationsModel)
                    conversationsAdapter!!.notifyItemInserted(0)
                }
            }
        }
    }

    override fun onConversationImageUpdated(conversationId: String, conversationImageUrl: String) {
        if (activity != null) {
            requireActivity().runOnUiThread {
                val position = conversationsListPresenter!!.fetchConversationPositionInList(
                    conversations,
                    conversationId, true
                )
                if (position != -1) {
                    val conversationsModel = conversations[position]
                    conversationsModel.conversationImageUrl = conversationImageUrl

                    conversations.removeAt(position)
                    conversationsAdapter!!.notifyItemRemoved(position)
                    conversations.add(0, conversationsModel)
                    conversationsAdapter!!.notifyItemInserted(0)
                }
            }
        }
    }

    override fun onMessagingStatusChanged(conversationId: String, disabled: Boolean) {
        if (activity != null) {
            requireActivity().runOnUiThread {
                val position = conversationsListPresenter!!.fetchConversationPositionInList(
                    conversations,
                    conversationId, false
                )
                if (position != -1) {
                    val conversationsModel = conversations[position]
                    conversationsModel.isMessagingDisabled = disabled
                    conversations[position] = conversationsModel
                    conversationsAdapter!!.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onConversationCleared(
        conversationId: String, lastMessageText: String,
        lastMessageTime: String
    ) {
        if (activity != null) {
            requireActivity().runOnUiThread {
                val position = conversationsListPresenter!!.fetchConversationPositionInList(
                    conversations,
                    conversationId, false
                )
                if (position != -1) {
                    val conversationsModel = conversations[position]
                    conversationsModel.lastMessageText = lastMessageText
                    conversationsModel.lastMessageSendersProfileImageUrl = null
                    conversationsModel.lastMessageSenderName = null
                    conversationsModel.lastMessageTime = lastMessageTime
                    conversationsModel.lastMessagePlaceHolderImage = null
                    conversationsModel.isLastMessageWasReactionMessage = false
                    conversationsModel.unreadMessagesCount = 0

                    conversations[position] = conversationsModel
                    conversationsAdapter!!.notifyItemChanged(position)
                }
            }
        }
    }

    override fun updateLastMessageInConversation(
        conversationId: String, lastMessageText: String,
        lastMessageSendersProfileImageUrl: String, lastMessageTime: String,
        lastMessagePlaceHolderImage: Int?, lastMessageWasReactionMessage: Boolean,
        updateUnreadMessagesCount: Boolean, lastMessageSendersName: String,
        fetchRemoteConversationIfNotFoundLocally: Boolean
    ) {
        if (activity != null) {
            requireActivity().runOnUiThread {
                val position = conversationsListPresenter!!.fetchConversationPositionInList(
                    conversations,
                    conversationId, fetchRemoteConversationIfNotFoundLocally
                )
                if (position != -1) {
                    val conversationsModel = conversations[position]
                    conversationsModel.lastMessageText = lastMessageText
                    conversationsModel.lastMessageSendersProfileImageUrl =
                        lastMessageSendersProfileImageUrl
                    conversationsModel.lastMessageSenderName = lastMessageSendersName
                    conversationsModel.lastMessageTime = lastMessageTime
                    conversationsModel.lastMessagePlaceHolderImage = lastMessagePlaceHolderImage
                    conversationsModel.isLastMessageWasReactionMessage =
                        lastMessageWasReactionMessage
                    if (updateUnreadMessagesCount
                        && conversationsModel.isMessageDeliveryReadEventsEnabled
                    ) {
                        if (conversationsModel.unreadMessagesCount == 0) {
                            fetchUnreadConversationsCount()
                        }
                        conversationsModel.unreadMessagesCount =
                            conversationsModel.unreadMessagesCount + 1
                    }

                    conversations.removeAt(position)
                    conversationsAdapter!!.notifyItemRemoved(position)
                    conversations.add(0, conversationsModel)
                    conversationsAdapter!!.notifyItemInserted(0)
                }
            }
        }
    }

    override fun onConversationSettingsUpdated(
        conversationId: String, typingEvents: Boolean,
        readEvents: Boolean?
    ) {
        val position =
            conversationsListPresenter!!.fetchConversationPositionInList(
                conversations, conversationId,
                true
            )

        if (position != -1) {
            val conversationsModel = conversations[position]
            if (readEvents != null) conversationsModel.isMessageDeliveryReadEventsEnabled =
                readEvents
            if (typingEvents != null) conversationsModel.isTypingEventsEnabled = typingEvents
            conversations[position] = conversationsModel
        }
    }

    override fun updateConversationMembersCount(conversationId: String, membersCount: Int) {
        val position =
            conversationsListPresenter!!.fetchConversationPositionInList(
                conversations, conversationId,
                true
            )

        if (position != -1) {
            val conversationsModel = conversations[position]
            conversationsModel.conversationMembersCount = membersCount
            conversations[position] = conversationsModel
        }
    }

    override fun onConversationJoinedSuccessfully(conversationsModel: ConversationsModel) {
        if (conversationType == ConversationType.PublicConversation) {
            if (activity != null) {
                requireActivity().runOnUiThread {
                    val position =
                        conversationsListPresenter!!.fetchConversationPositionInList(
                            conversations,
                            conversationsModel.conversationId, false
                        )
                    if (position != -1) {
                        conversations.removeAt(position)
                        conversationsAdapter!!.notifyItemRemoved(position)
                        if (conversations.size == 0) {
                            ismFragmentConversationsBinding!!.lnrNoConversations.visibility =
                                View.VISIBLE
                            ismFragmentConversationsBinding!!.tvNoConversations.text =
                                getString(R.string.ism_no_public_conversations)
                            ismFragmentConversationsBinding!!.rvConversations.visibility =
                                View.GONE
                        }
                    }
                    hideProgressDialog()
                    openMessagesScreen(conversationsModel)
                }
            }
        }
    }

    private fun showProgressDialog(message: String) {
        if (activity != null) {
            alertDialog = alertProgress!!.getProgressDialog(activity, message)
            if (!requireActivity().isFinishing) alertDialog?.show()
        }
    }

    private fun hideProgressDialog() {
        if (alertDialog != null && alertDialog!!.isShowing) alertDialog!!.dismiss()
    }

    /**
     * Cleanup all realtime isometrik event listeners that were added at time of exit
     */
    private fun unregisterListeners() {
        if (!unregisteredListeners) {
            unregisteredListeners = true
            if (conversationType == ConversationType.AllConversations) {
                handler!!.removeCallbacksAndMessages(null)
                conversationsListPresenter!!.unregisterConnectionEventListener()
                conversationsListPresenter!!.unregisterConversationEventListener()
                conversationsListPresenter!!.unregisterMessageEventListener()
                conversationsListPresenter!!.unregisterMembershipControlEventListener()
                conversationsListPresenter!!.unregisterReactionEventListener()
                conversationsListPresenter!!.unregisterUserEventListener()
            }
        }
    }

    /**
     * Join conversation.
     *
     * @param conversationsModel the conversations model
     */
    fun joinConversation(conversationsModel: ConversationsModel?) {
        if (activity != null) {
            AlertDialog.Builder(requireActivity())
                .setTitle(getString(R.string.ism_join_conversation))
                .setMessage(getString(R.string.ism_join_conversation_alert_message))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ism_continue)) { dialog: DialogInterface, id: Int ->
                    dialog.cancel()
                    showProgressDialog(getString(R.string.ism_joining_conversation))
                    conversationsListPresenter!!.joinConversation(conversationsModel)
                }
                .setNegativeButton(
                    getString(R.string.ism_cancel)
                ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
                .create()
                .show()
        }
    }

    private fun openMessagesScreen(conversationsModel: ConversationsModel) {
        val intent = Intent(activity, ConversationMessagesActivity::class.java)
        intent.putExtra(
            "messageDeliveryReadEventsEnabled",
            conversationsModel.isMessageDeliveryReadEventsEnabled
        )
        intent.putExtra("typingEventsEnabled", conversationsModel.isTypingEventsEnabled)
        intent.putExtra("newConversation", false)
        intent.putExtra("conversationId", conversationsModel.conversationId)
        intent.putExtra("isPrivateOneToOne", conversationsModel.isPrivateOneToOneConversation)
        if (conversationsModel.isPrivateOneToOneConversation) {
            intent.putExtra("userName", conversationsModel.conversationTitle)
            intent.putExtra("userImageUrl", conversationsModel.conversationImageUrl)
            intent.putExtra("isOnline", conversationsModel.isOnline)
            if (!conversationsModel.isOnline) {
                intent.putExtra("lastSeenAt", conversationsModel.lastSeenAt)
            }
            intent.putExtra("userId", conversationsModel.opponentId)
            intent.putExtra("identifier", conversationsModel.opponentIdentifier)

            if (conversationsModel.isMessagingDisabled) {
                intent.putExtra("messagingDisabled", true)
            }
        } else {
            intent.putExtra("conversationTitle", conversationsModel.conversationTitle)
            intent.putExtra("participantsCount", conversationsModel.conversationMembersCount)
            intent.putExtra("conversationImageUrl", conversationsModel.conversationImageUrl)

            if (conversationType == ConversationType.OpenConversation) {
                intent.putExtra("joinAsObserver", true)
            }
        }

        startActivity(intent)
    }

    override fun fetchUnreadConversationsCount() {
        if (activity != null) {
            if (activity is ConversationsListActivity) {
                (activity as ConversationsListActivity).fetchUnreadConversationsCount()
            }
        }
    }

    override fun connectionStateChanged(connected: Boolean) {
        if (activity != null) {
            if (activity is ConversationsListActivity) {
                (activity as ConversationsListActivity).connectionStateChanged(connected)
            }
        }
    }

    override fun failedToConnect(errorMessage: String?) {
        if (activity != null) {
            requireActivity().runOnUiThread {
                if (errorMessage != null) {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onUnreadMessagesCountInConversationsFetchedSuccessfully(
        conversationId: String,
        count: Int
    ) {
        if (activity != null) {
            requireActivity().runOnUiThread {
                val position = conversationsListPresenter!!.fetchConversationPositionInList(
                    conversations,
                    conversationId, false
                )
                if (position != -1) {
                    val conversationsModel = conversations[position]
                    conversationsModel.unreadMessagesCount = count
                    conversations[position] = conversationsModel
                    conversationsAdapter!!.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onUserProfileImageUpdated(userProfileImageUrl: String) {
        if (activity != null) {
            if (activity is ConversationsListActivity) {
                (activity as ConversationsListActivity).loadUserImage(userProfileImageUrl)
            }
        }
    }

    override fun onUserDeleted() {
        if (activity != null) {
            if (activity is ConversationsListActivity) {
                (activity as ConversationsListActivity).onUserDeleted()
            }
        }
    }

    private fun updateShimmerVisibility(visible: Boolean) {
        if (visible) {
            ismFragmentConversationsBinding!!.shimmerFrameLayout.startShimmer()
            ismFragmentConversationsBinding!!.shimmerFrameLayout.visibility =
                View.VISIBLE
        } else {
            if (ismFragmentConversationsBinding!!.shimmerFrameLayout.visibility == View.VISIBLE) {
                ismFragmentConversationsBinding!!.shimmerFrameLayout.visibility =
                    View.GONE
                ismFragmentConversationsBinding!!.shimmerFrameLayout.stopShimmer()
            }
        }
    }

    override fun onRemoteUserTypingEvent(conversationId: String, message: String) {
        if (activity != null) {
            requireActivity().runOnUiThread {
                val size = conversations.size
                for (i in 0 until size) {
                    if (conversations[i].conversationId == conversationId) {
                        val conversationsModel = conversations[i]
                        conversationsModel.isRemoteUserTyping = true
                        conversationsModel.remoteUserTypingMessage = message
                        conversations[i] = conversationsModel
                        conversationsAdapter!!.notifyItemChanged(i)

                        try {
                            handler!!.postDelayed({
                                val size1 = conversations.size
                                for (i1 in 0 until size1) {
                                    if (conversations[i1].conversationId == conversationId) {
                                        val conversationsModel1 =
                                            conversations[i1]
                                        conversationsModel1.isRemoteUserTyping = false
                                        conversations[i1] =
                                            conversationsModel1
                                        conversationsAdapter!!.notifyItemChanged(i1)
                                        break
                                    }
                                }
                            }, 1000)
                        } catch (ignore: Exception) {
                        }
                        break
                    }
                }
            }
        }
    }
}
