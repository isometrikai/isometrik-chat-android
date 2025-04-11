package io.isometrik.ui.messages.chat

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.provider.Settings
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmActivityMessagesBinding
import io.isometrik.chat.enums.AttachmentMessageType
import io.isometrik.chat.enums.CustomMessageTypes
import io.isometrik.chat.enums.MessageTypeUi
import io.isometrik.chat.enums.PresignedUrlMediaTypes
import io.isometrik.chat.response.conversation.utils.ConversationDetailsUtil
import io.isometrik.chat.utils.AlertProgress
import io.isometrik.chat.utils.Constants
import io.isometrik.chat.utils.FileUriUtils.getRealPath
import io.isometrik.chat.utils.GalleryIntentsUtil
import io.isometrik.chat.utils.KeyboardUtil
import io.isometrik.chat.utils.LogManger.log
import io.isometrik.chat.utils.MentionedUserSpan
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.chat.utils.RecyclerItemClickListener
import io.isometrik.chat.utils.TagUserUtil
import io.isometrik.chat.utils.TimeUtil
import io.isometrik.chat.utils.Utilities
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.camera.CameraActivity
import io.isometrik.ui.camera.VideoRecordingActivity
import io.isometrik.ui.conversations.details.observers.ObserversActivity
import io.isometrik.ui.libwave.WaveformSeekBar
import io.isometrik.ui.messages.action.MessageActionCallback
import io.isometrik.ui.messages.action.MessageActionFragment
import io.isometrik.ui.messages.action.edit.EditMessageFragment
import io.isometrik.ui.messages.action.replies.SendMessageReplyFragment
import io.isometrik.ui.messages.chat.ConversationMessagesAdapter.OnScrollToMessageListener
import io.isometrik.ui.messages.chat.common.ChatConfig
import io.isometrik.ui.messages.chat.common.ChatTopViewHandler
import io.isometrik.ui.messages.chat.messageBinders.AudioReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.AudioSentBinder
import io.isometrik.ui.messages.chat.messageBinders.ContactReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.ContactSentBinder
import io.isometrik.ui.messages.chat.messageBinders.ConversationActionBinder
import io.isometrik.ui.messages.chat.messageBinders.FileReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.FileSentBinder
import io.isometrik.ui.messages.chat.messageBinders.GifReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.GifSentBinder
import io.isometrik.ui.messages.chat.messageBinders.LocationReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.LocationSentBinder
import io.isometrik.ui.messages.chat.messageBinders.OfferReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.OfferSentBinder
import io.isometrik.ui.messages.chat.messageBinders.PhotoReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.PhotoSentBinder
import io.isometrik.ui.messages.chat.messageBinders.PostReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.PostSentBinder
import io.isometrik.ui.messages.chat.messageBinders.StickerReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.StickerSentBinder
import io.isometrik.ui.messages.chat.messageBinders.TextReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.TextSentBinder
import io.isometrik.ui.messages.chat.messageBinders.VideoReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.VideoSentBinder
import io.isometrik.ui.messages.chat.messageBinders.WhiteboardReceivedBinder
import io.isometrik.ui.messages.chat.messageBinders.WhiteboardSentBinder
import io.isometrik.ui.messages.chat.utils.attachmentutils.PrepareAttachmentHelper
import io.isometrik.ui.messages.chat.utils.enums.RemoteMessageTypes
import io.isometrik.ui.messages.chat.utils.messageutils.ContactUtil
import io.isometrik.ui.messages.chat.utils.messageutils.MultipleMessagesUtil
import io.isometrik.ui.messages.chat.utils.messageutils.OriginalReplyMessageUtil
import io.isometrik.ui.messages.deliverystatus.MessageDeliveryStatusActivity
import io.isometrik.ui.messages.forward.ForwardMessageActivity
import io.isometrik.ui.messages.media.MediaSelectedToBeShared
import io.isometrik.ui.messages.media.MediaTypeToBeSharedCallback
import io.isometrik.ui.messages.media.ShareMediaFragment
import io.isometrik.ui.messages.media.audio.record.listeners.OnRecordListener
import io.isometrik.ui.messages.media.audio.util.AudioFileUtil
import io.isometrik.ui.messages.media.gifs.GifsFragment
import io.isometrik.ui.messages.media.location.LocationUtils
import io.isometrik.ui.messages.media.location.ShareLocationActivity
import io.isometrik.ui.messages.media.stickers.StickersFragment
import io.isometrik.ui.messages.media.visual.VisualMediaFragment
import io.isometrik.ui.messages.media.whiteboard.WhiteboardFragment
import io.isometrik.ui.messages.preview.PreviewMessageUtil
import io.isometrik.ui.messages.reaction.FullScreenReactionDialog
import io.isometrik.ui.messages.reaction.add.AddReactionFragment
import io.isometrik.ui.messages.reaction.add.ReactionModel
import io.isometrik.ui.messages.reaction.list.FetchReactionUsersFragment
import io.isometrik.ui.messages.reaction.util.ReactionClickListener
import io.isometrik.ui.messages.tag.MemberDetailsFragment
import io.isometrik.ui.messages.tag.TagUserAdapter
import io.isometrik.ui.messages.tag.TagUserModel
import io.isometrik.ui.messages.tag.TaggedUserCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Arrays
import java.util.concurrent.CopyOnWriteArrayList

/**
 * The activity to send/receive messages in realtime of type- image/video/file/contact/location/whiteboard/sticker/gif/audio/text,
 * forward or add reaction to a message, search for message, capture image from camera/record
 * audio, tag a member in a text message, fetch messages with paging and pull to refresh, take
 * various action like message
 * delete/edit on
 * long press of a message cell, multiselect messages, join/leave as observer and fetch observers
 * list for open conversation,
 * fetch opponent's online or last seen status for 1-1 conversation, update UI on receiving
 * realtime
 * message or various actions in conversation, initiate/cancel of upload/download media message,
 * copy text messages, send typing message.
 */
class ConversationMessagesActivity : AppCompatActivity(), ConversationMessagesContract.View,
    ReactionClickListener, MediaTypeToBeSharedCallback, MediaSelectedToBeShared,
    MessageActionCallback, TaggedUserCallback, OnScrollToMessageListener {
    private lateinit var conversationMessagesPresenter: ConversationMessagesContract.Presenter
    lateinit var ismActivityMessagesBinding: IsmActivityMessagesBinding
    private val messages = ArrayList<MessagesModel>()
    private lateinit var conversationMessagesAdapter: ConversationMessagesAdapter<MessagesModel, ViewBinding>
    private val tagUserModels = ArrayList<TagUserModel>()
    private var tagUserAdapter: TagUserAdapter? = null

    private var addReactionFragment: AddReactionFragment? = null
    private var  reactionDialog : FullScreenReactionDialog? = null
    private var fetchReactionUsersFragment: FetchReactionUsersFragment? = null
    private var gifsFragment: GifsFragment? = null
    private var stickersFragment: StickersFragment? = null
    private var visualMediaFragment: VisualMediaFragment? = null
    private var shareMediaFragment: ShareMediaFragment? = null
    private var whiteboardFragment: WhiteboardFragment? = null
    private var sendMessageReplyFragment: SendMessageReplyFragment? = null
    private var messageActionFragment: MessageActionFragment? = null
    private var memberDetailsFragment: MemberDetailsFragment? = null
    private var editMessageFragment: EditMessageFragment? = null

    private var unregisteredListeners = false
    private var scrollToMessageNeeded = false
    private var isometrikUserId: String? = null
    private var conversationUserImageUrl: String? = null
    private var userPersonalUserId: String? = null
    private var conversationUserFullName: String? = null
    private var conversationImageUrl: String? = null
    private var conversationTitle: String? = null
    private var lastMessageText: String? = null

    private var conversationDetailsActivityLauncher: ActivityResultLauncher<Intent>? = null
    private var userDetailsActivityLauncher: ActivityResultLauncher<Intent>? = null
    private var cameraActivityLauncher: ActivityResultLauncher<Intent>? = null
    private var multiplePhotosPicker: ActivityResultLauncher<Intent>? = null
    private var multipleVideosPicker: ActivityResultLauncher<Intent>? = null
    private var multipleFilesPicker: ActivityResultLauncher<Intent>? = null
    private var shareLocationActivityLauncher: ActivityResultLauncher<Intent>? = null
    private var contactPicker: ActivityResultLauncher<Intent>? = null

    private var messagesLayoutManager: LinearLayoutManager? = null
    private val handler = Handler()
    private val spansToRemove: MutableList<MentionedUserSpan> = CopyOnWriteArrayList()

    private var messagingDisabled = false
    private var joiningAsObserver = false
    private var opponentUserBlocked = false
    private var firstResume = true

    private var alertDialog: AlertDialog? = null
    private var alertProgress: AlertProgress? = null

    private var topView: View? = null
    private var topViewHandler: ChatTopViewHandler? = null
    private var conversationDetailsUtil: ConversationDetailsUtil? = null

    private var mediaPlayer: MediaPlayer? = null
    private var currentWaveSeekBar: WaveformSeekBar? = null
    private var currentIvPlayAudio: AppCompatImageView? = null
    private var isAudioPlaying = false
    private var currentAudioPosition = -1
    private var updateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ismActivityMessagesBinding = IsmActivityMessagesBinding.inflate(
            layoutInflater
        )
        val view: View = ismActivityMessagesBinding!!.root
        setContentView(view)
        updateShimmerVisibility(true)
        alertProgress = AlertProgress()
        conversationMessagesPresenter = ConversationMessagesPresenter(this, this)

        messagingDisabled = intent.extras!!.containsKey("messagingDisabled")
        if (messagingDisabled) {
            lastMessageText = intent.extras!!.getString("lastMessageText")
        }
        joiningAsObserver = intent.extras!!.getBoolean("joinAsObserver", false)


        topViewHandler = ChatConfig.topViewHandler

        topViewHandler?.let { handler ->
            // Create the top view
            topView = handler.createTopView(ismActivityMessagesBinding.topViewContainer)
            ismActivityMessagesBinding.topViewContainer.addView(topView)
        }

        ChatConfig.typingUiState.observe(this) { visiable ->
            CoroutineScope(Dispatchers.Main).launch {
                ismActivityMessagesBinding.rlBottomLayout.visibility =
                    if (visiable) View.VISIBLE else View.GONE

                ismActivityMessagesBinding.rlRecordAudio.visibility =
                if (!ChatConfig.hideRecordAudioOption && visiable) View.VISIBLE else View.GONE
            }
        }

        ismActivityMessagesBinding.ivAudioCall.visibility =
            if (ChatConfig.hideAudioCallOption) View.GONE else View.VISIBLE
        ismActivityMessagesBinding.ivVideoCall.visibility =
            if (ChatConfig.hideVideoCallOption) View.GONE else View.VISIBLE
        ismActivityMessagesBinding.ivCaptureImage.visibility =
            if (ChatConfig.hideCaptureCameraOption) View.GONE else View.VISIBLE
        ismActivityMessagesBinding.btRecord.visibility =
            if (ChatConfig.hideRecordAudioOption) View.GONE else View.VISIBLE

        ismActivityMessagesBinding.btRecord.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, ChatConfig.baseColor))
        ismActivityMessagesBinding.ivSendMessage.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, ChatConfig.baseColor))
        ismActivityMessagesBinding.ivAddAttachment.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, ChatConfig.baseColor))

        ismActivityMessagesBinding.relRoot.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, ChatConfig.chatBackGroundColor))
        ismActivityMessagesBinding.relTopbar.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, ChatConfig.chatBackGroundColor))


        val itemBinders = mapOf(
            MessageTypeUi.TEXT_MESSAGE_SENT to TextSentBinder(),
            MessageTypeUi.REPLAY_MESSAGE_SENT to TextSentBinder(),
            MessageTypeUi.PHOTO_MESSAGE_SENT to PhotoSentBinder(),
            MessageTypeUi.VIDEO_MESSAGE_SENT to VideoSentBinder(),
            MessageTypeUi.AUDIO_MESSAGE_SENT to AudioSentBinder(),
            MessageTypeUi.FILE_MESSAGE_SENT to FileSentBinder(),
            MessageTypeUi.STICKER_MESSAGE_SENT to StickerSentBinder(),
            MessageTypeUi.GIF_MESSAGE_SENT to GifSentBinder(),
            MessageTypeUi.WHITEBOARD_MESSAGE_SENT to WhiteboardSentBinder(),
            MessageTypeUi.LOCATION_MESSAGE_SENT to LocationSentBinder(),
            MessageTypeUi.CONTACT_MESSAGE_SENT to ContactSentBinder(),
            MessageTypeUi.OFFER_SENT to OfferSentBinder(),
            MessageTypeUi.COUNTER_OFFER_SENT to OfferSentBinder(),
            MessageTypeUi.EDIT_OFFER_SENT to OfferSentBinder(),
            MessageTypeUi.ACCEPT_OFFER_SENT to OfferSentBinder(),
            MessageTypeUi.CANCEL_DEAL_SENT to OfferSentBinder(),
            MessageTypeUi.CANCEL_OFFER_SENT to OfferSentBinder(),
            MessageTypeUi.BUY_DIRECT_SENT to OfferSentBinder(),
            MessageTypeUi.ACCEPT_BUY_DIRECT_SENT to OfferSentBinder(),
            MessageTypeUi.CANCEL_BUY_DIRECT_SENT to OfferSentBinder(),
            MessageTypeUi.PAYMENT_ESCROWED_SENT to OfferSentBinder(),
            MessageTypeUi.DEAL_COMPLETE_SENT to OfferSentBinder(),

            MessageTypeUi.TEXT_MESSAGE_RECEIVED to TextReceivedBinder(),
            MessageTypeUi.REPLAY_MESSAGE_RECEIVED to TextReceivedBinder(),
            MessageTypeUi.PHOTO_MESSAGE_RECEIVED to PhotoReceivedBinder(),
            MessageTypeUi.VIDEO_MESSAGE_RECEIVED to VideoReceivedBinder(),
            MessageTypeUi.AUDIO_MESSAGE_RECEIVED to AudioReceivedBinder(),
            MessageTypeUi.FILE_MESSAGE_RECEIVED to FileReceivedBinder(),
            MessageTypeUi.STICKER_MESSAGE_RECEIVED to StickerReceivedBinder(),
            MessageTypeUi.GIF_MESSAGE_RECEIVED to GifReceivedBinder(),
            MessageTypeUi.WHITEBOARD_MESSAGE_RECEIVED to WhiteboardReceivedBinder(),
            MessageTypeUi.LOCATION_MESSAGE_RECEIVED to LocationReceivedBinder(),
            MessageTypeUi.CONTACT_MESSAGE_RECEIVED to ContactReceivedBinder(),
            MessageTypeUi.CONVERSATION_ACTION_MESSAGE to ConversationActionBinder(),
            MessageTypeUi.POST_MESSAGE_SENT to PostSentBinder(),
            MessageTypeUi.POST_MESSAGE_RECEIVED to PostReceivedBinder(),
            MessageTypeUi.OFFER_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.COUNTER_OFFER_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.EDIT_OFFER_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.ACCEPT_OFFER_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.CANCEL_DEAL_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.CANCEL_OFFER_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.BUY_DIRECT_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.ACCEPT_BUY_DIRECT_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.CANCEL_BUY_DIRECT_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.PAYMENT_ESCROWED_RECEIVED to OfferReceivedBinder(),
            MessageTypeUi.DEAL_COMPLETE_RECEIVED to OfferReceivedBinder()
        )

        messagesLayoutManager = LinearLayoutManager(this)
        ismActivityMessagesBinding!!.rvMessages.layoutManager = messagesLayoutManager
        conversationMessagesAdapter = ConversationMessagesAdapter(
            messages,
            itemBinders,
            ConversationActionBinder(),
            messagingDisabled,
            joiningAsObserver,
            this
        )
        ismActivityMessagesBinding!!.rvMessages.adapter = conversationMessagesAdapter
        ismActivityMessagesBinding!!.rvMessages.addOnScrollListener(
            messagesRecyclerViewOnScrollListener
        )

        ismActivityMessagesBinding!!.vTagUsers.rvUsers.layoutManager = LinearLayoutManager(
            this
        )
        tagUserAdapter = TagUserAdapter(this, tagUserModels)
        ismActivityMessagesBinding!!.vTagUsers.rvUsers.adapter = tagUserAdapter

        conversationId = intent.extras!!.getString("conversationId")
        conversationUserImageUrl = intent.extras!!.getString("userImageUrl")
        conversationImageUrl = intent.extras!!.getString("conversationImageUrl")
        conversationTitle = intent.extras!!.getString("conversationTitle")
        isometrikUserId = intent.extras!!.getString("userId") // isometrikUserId
        userPersonalUserId = intent.extras!!.getString("identifier") //personalUserId

        val isPrivateOneToOne = intent.extras!!.getBoolean("isPrivateOneToOne")

        conversationMessagesPresenter.initializeConversation(
            conversationId,
            intent.extras!!
                .getBoolean("messageDeliveryReadEventsEnabled"),
            intent.extras!!.getBoolean("typingEventsEnabled"),
            isPrivateOneToOne,
            intent.extras,
            joiningAsObserver
        )

        if (messagingDisabled) {
            onMessagingStatusChanged(true)
            opponentUserBlocked = !lastMessageText!!.startsWith("You")
        }
        updateConversationDetailsInHeader(true, isPrivateOneToOne, null, false, 0, null, 0,conversationUserImageUrl.orEmpty())

        scrollToMessageNeeded = intent.getBooleanExtra("scrollToMessageNeeded", false)
        if (scrollToMessageNeeded) {
//            ismActivityMessagesBinding.ivSearch.setVisibility(View.GONE);
            ismActivityMessagesBinding!!.vLoadingOverlay.root.visibility = View.VISIBLE
        }

        if (joiningAsObserver) {
//            ismActivityMessagesBinding.ivSearch.setVisibility(View.GONE);
            val params =
                ismActivityMessagesBinding!!.ivObservers.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.ALIGN_PARENT_END)

            ismActivityMessagesBinding!!.ivObservers.layoutParams = params
            conversationMessagesPresenter.joinAsObserver()
        } else {
            fetchMessages(false, null, false)
        }
        conversationMessagesPresenter.registerConnectionEventListener()
        conversationMessagesPresenter.registerConversationEventListener()
        conversationMessagesPresenter.registerMessageEventListener()
        conversationMessagesPresenter.registerMembershipControlEventListener()
        conversationMessagesPresenter.registerReactionEventListener()
        conversationMessagesPresenter.registerUserEventListener()

        if (!joiningAsObserver && !intent.extras!!.getBoolean("newConversation", false)) {
            conversationMessagesPresenter.markMessagesAsRead()
        }
        ismActivityMessagesBinding!!.refresh.setOnRefreshListener {
            if (joiningAsObserver) {
                ismActivityMessagesBinding!!.refresh.isRefreshing = false
            } else {
                fetchMessages(false, null, true)
            }
        }
        addReactionFragment = AddReactionFragment()
        fetchReactionUsersFragment = FetchReactionUsersFragment()
        gifsFragment = GifsFragment()
        stickersFragment = StickersFragment()
        visualMediaFragment = VisualMediaFragment()
        shareMediaFragment = ShareMediaFragment()
        whiteboardFragment = WhiteboardFragment()
        sendMessageReplyFragment = SendMessageReplyFragment()
        messageActionFragment = MessageActionFragment()
        memberDetailsFragment = MemberDetailsFragment()
        editMessageFragment = EditMessageFragment()

        ismActivityMessagesBinding!!.ibBack.setOnClickListener { v: View? -> onBackPressed() }

        ismActivityMessagesBinding!!.ivAddAttachment2.setOnClickListener { v: View? ->
            if (!isFinishing && !shareMediaFragment!!.isAdded) {
                dismissAllDialogs()
                shareMediaFragment!!.updateParameters(this)
                shareMediaFragment!!.show(supportFragmentManager, ShareMediaFragment.TAG)
            }
        }

        conversationDetailsActivityLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    if (result.data!!.getBooleanExtra("conversationLeftOrDeleted", false)) {
                        onBackPressed()
                    } else if (result.data!!.getBooleanExtra("searchMessageRequested", false)) {
                        if (ismActivityMessagesBinding!!.rlSearch.visibility == View.GONE) {
                            ismActivityMessagesBinding!!.rlSearch.visibility =
                                View.VISIBLE
                        }
                        KeyboardUtil.showSoftKeyboard(
                            this,
                            ismActivityMessagesBinding!!.etSearch
                        )
                    }
                }
            }
        }

        userDetailsActivityLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    if (result.data!!.getBooleanExtra("conversationLeftOrDeleted", false)) {
                        onBackPressed()
                    } else if (result.data!!.getBooleanExtra("messagingBlocked", false)) {
                        onMessagingStatusChanged(true)
                    } else if (result.data!!.getBooleanExtra("searchMessageRequested", false)) {
                        if (ismActivityMessagesBinding!!.rlSearch.visibility == View.GONE) {
                            ismActivityMessagesBinding!!.rlSearch.visibility =
                                View.VISIBLE
                        }
                        KeyboardUtil.showSoftKeyboard(
                            this,
                            ismActivityMessagesBinding!!.etSearch
                        )
                    }
                }
            }
        }

        cameraActivityLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    conversationMessagesPresenter.shareMessage(
                        RemoteMessageTypes.NormalMessage,
                        null,
                        null,
                        CustomMessageTypes.Image,
                        CustomMessageTypes.Image.value,
                        false,
                        true,
                        true,
                        true,
                        null,
                        null,
                        null,
                        MessageTypeUi.PHOTO_MESSAGE_SENT,
                        ArrayList(
                            listOf(
                                result.data!!.getStringExtra("capturedImagePath")
                            )
                        ),
                        true,
                        PresignedUrlMediaTypes.Photo,
                        AttachmentMessageType.Image
                    )
                }
            } else {
                Toast.makeText(
                    this,
                    R.string.ism_image_capture_canceled,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        multiplePhotosPicker = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val photoPaths = GalleryIntentsUtil.getFilePaths(
                        result.data,
                        this
                    )

                    if (photoPaths == null) {
                        Toast.makeText(
                            this,
                            getString(
                                R.string.ism_photos_selection_limit_exceeded,
                                Constants.MAXIMUM_MEDIA_SELECTION_COUNT_AT_ONCE
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (photoPaths.isEmpty()) {
                            Toast.makeText(
                                this,
                                R.string.ism_photos_selected_deleted,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            conversationMessagesPresenter.shareMessage(
                                RemoteMessageTypes.NormalMessage,
                                null,
                                null,
                                CustomMessageTypes.Image,
                                CustomMessageTypes.Image.value,
                                false,
                                true,
                                true,
                                true,
                                null,
                                null,
                                null,
                                MessageTypeUi.PHOTO_MESSAGE_SENT,
                                photoPaths,
                                true,
                                PresignedUrlMediaTypes.Photo,
                                AttachmentMessageType.Image
                            )
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        R.string.ism_photos_selection_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    R.string.ism_photos_selection_canceled,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        multipleVideosPicker = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val videoPaths = GalleryIntentsUtil.getFilePaths(
                        result.data,
                        this
                    )
                    if (videoPaths == null) {
                        Toast.makeText(
                            this,
                            getString(
                                R.string.ism_videos_selection_limit_exceeded,
                                Constants.MAXIMUM_MEDIA_SELECTION_COUNT_AT_ONCE
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (videoPaths.isEmpty()) {
                            Toast.makeText(
                                this,
                                R.string.ism_videos_selected_deleted,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            conversationMessagesPresenter.shareMessage(
                                RemoteMessageTypes.NormalMessage,
                                null,
                                null,
                                CustomMessageTypes.Video,
                                CustomMessageTypes.Video.value,
                                false,
                                true,
                                true,
                                true,
                                null,
                                null,
                                null,
                                MessageTypeUi.VIDEO_MESSAGE_SENT,
                                videoPaths,
                                true,
                                PresignedUrlMediaTypes.Video,
                                AttachmentMessageType.Video
                            )
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        R.string.ism_videos_selection_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    R.string.ism_videos_selection_canceled,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        multipleFilesPicker = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val path = getRealPath(
                        this, result.data!!
                            .data!!
                    )
                    //                    ArrayList<String> filePaths = GalleryIntentsUtil.getFilePaths(result.getData(), this);
                    val filePaths =
                        ArrayList<String?>()
                    filePaths.add(path)
                    if (filePaths == null) {
                        Toast.makeText(
                            this,
                            getString(
                                R.string.ism_files_selection_limit_exceeded,
                                Constants.MAXIMUM_MEDIA_SELECTION_COUNT_AT_ONCE
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (filePaths.isEmpty()) {
                            Toast.makeText(
                                this,
                                R.string.ism_files_selected_deleted,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            conversationMessagesPresenter.shareMessage(
                                RemoteMessageTypes.NormalMessage,
                                null,
                                null,
                                CustomMessageTypes.File,
                                CustomMessageTypes.File.value,
                                false,
                                true,
                                true,
                                true,
                                null,
                                null,
                                null,
                                MessageTypeUi.FILE_MESSAGE_SENT,
                                filePaths,
                                true,
                                PresignedUrlMediaTypes.File,
                                AttachmentMessageType.File
                            )
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        R.string.ism_files_selection_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    R.string.ism_files_selection_canceled,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        shareLocationActivityLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data

                //Location data received
                val locationName = data!!.getStringExtra("locationName")
                val locationAddress = data.getStringExtra("locationAddress")
                val latitude = data.getStringExtra("latitude")!!.toDouble()
                val longitude = data.getStringExtra("longitude")!!.toDouble()

                //Log.d("log1", locationName + "*" + locationAddress + "*" + latitude + "*" + longitude);
                val locationAttachment = PrepareAttachmentHelper.prepareLocationAttachment(
                    locationName,
                    locationAddress,
                    latitude,
                    longitude
                )

                if (locationAttachment == null) {
                    Toast.makeText(
                        this,
                        R.string.ism_location_share_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    conversationMessagesPresenter.shareMessage(
                        RemoteMessageTypes.NormalMessage,
                        null,
                        null,
                        CustomMessageTypes.Location,
                        CustomMessageTypes.Location.value,
                        false,
                        true,
                        true,
                        true,
                        ArrayList(
                            listOf(locationAttachment)
                        ),
                        null,
                        null,
                        MessageTypeUi.LOCATION_MESSAGE_SENT,
                        null,
                        false,
                        null,
                        null
                    )
                }
            } else {
                Toast.makeText(
                    this,
                    R.string.ism_location_selection_canceled,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        contactPicker = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    val messageMetadata = ContactUtil.parseContactsData(
                        result.data,
                        contentResolver
                    )

                    if (messageMetadata == null) {
                        Toast.makeText(
                            this,
                            R.string.ism_contact_selection_failed,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        conversationMessagesPresenter.shareMessage(
                            RemoteMessageTypes.NormalMessage,
                            null,
                            null,
                            CustomMessageTypes.Contact,
                            CustomMessageTypes.Contact.value,
                            false,
                            true,
                            true,
                            true,
                            null,
                            messageMetadata,
                            null,
                            MessageTypeUi.CONTACT_MESSAGE_SENT,
                            null,
                            false,
                            null,
                            null
                        )
                    }
                } else {
                    Toast.makeText(
                        this,
                        R.string.ism_contact_selection_failed,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    R.string.ism_contact_selection_canceled,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        ismActivityMessagesBinding!!.ivCaptureImage.setOnClickListener { v: View? ->
            checkImageCapturePermissions(
                true
            )
        }

        if(!ChatConfig.disableTopHeaderClickAction){
            ismActivityMessagesBinding!!.rlConversationDetails.setOnClickListener { v: View? ->
                if (ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.root.visibility == View.GONE && clickActionsNotBlocked()) {
                    KeyboardUtil.hideKeyboard(this)

                    val intent =
                        conversationMessagesPresenter.getConversationDetailsIntent(
                            this,
                            isPrivateOneToOne
                        )
                    if (isPrivateOneToOne) {
                        if (!messagingDisabled) userDetailsActivityLauncher!!.launch(intent)
                    } else {
                        if (!joiningAsObserver) conversationDetailsActivityLauncher!!.launch(intent)
                    }
                }
            }
        }

        ismActivityMessagesBinding!!.etSendMessage.addTextChangedListener(sendMessageTextWatcher)

        ismActivityMessagesBinding!!.vTagUsers.rvUsers.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                ismActivityMessagesBinding!!.vTagUsers.rvUsers,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (position >= 0) {
                            if (ismActivityMessagesBinding!!.etSendMessage.text != null) {
                                ismActivityMessagesBinding!!.etSendMessage.removeTextChangedListener(
                                    sendMessageTextWatcher
                                )

                                ismActivityMessagesBinding!!.etSendMessage.setText(
                                    SpannableString(
                                        TagUserUtil.addTaggedUsername(
                                            ismActivityMessagesBinding!!.etSendMessage.text as SpannableStringBuilder?,
                                            ismActivityMessagesBinding!!.etSendMessage.selectionStart,
                                            tagUserModels[position],
                                            this@ConversationMessagesActivity
                                        )
                                    )
                                )

                                ismActivityMessagesBinding!!.vTagUsers.root.visibility = View.GONE
                                ismActivityMessagesBinding!!.etSendMessage.addTextChangedListener(
                                    sendMessageTextWatcher
                                )
                                ismActivityMessagesBinding!!.etSendMessage.setSelection(
                                    ismActivityMessagesBinding!!.etSendMessage.text?.length ?: 0
                                )
                            }
                        }
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                    }
                })
        )

        ismActivityMessagesBinding.ivSendMessage.setOnClickListener { v: View? ->
            if (ismActivityMessagesBinding!!.etSendMessage.text != null && ismActivityMessagesBinding!!.etSendMessage.text!!.length > 0) {
                conversationMessagesPresenter.shareMessage(
                    RemoteMessageTypes.NormalMessage,
                    null,
                    null,
                    CustomMessageTypes.Text,
                    ismActivityMessagesBinding!!.etSendMessage.text.toString(),
                    false,
                    true,
                    true,
                    true,
                    null,
                    null,
                    TagUserUtil.prepareMentionedUsers(
                        ismActivityMessagesBinding!!.etSendMessage.editableText
                    ),
                    MessageTypeUi.TEXT_MESSAGE_SENT,
                    null,
                    false,
                    null,
                    null
                )
                ismActivityMessagesBinding!!.etSendMessage.setText(null)
            } else {
                Toast.makeText(
                    this@ConversationMessagesActivity,
                    getString(R.string.ism_type_something),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ismActivityMessagesBinding.rvMessages.addOnItemTouchListener(
            RecyclerItemClickListener(
                this,
                ismActivityMessagesBinding.rvMessages,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (position >= 0) {
                            if (ismActivityMessagesBinding.vSelectMultipleMessagesHeader.root.visibility == View.VISIBLE) {
                                if (messages[position].messageTypeUi != MessageTypeUi.CONVERSATION_ACTION_MESSAGE) {
                                    val messagesModel = messages[position]
                                    val selected = !messagesModel.isSelected
                                    messagesModel.isSelected = selected
                                    conversationMessagesPresenter.updateMessageSelectionStatus(
                                        messagesModel,
                                        selected
                                    )
                                    messages[position] = messagesModel
                                    conversationMessagesAdapter.notifyItemChanged(position)
                                }
                            }
                        }
                    }

                    override fun onItemLongClick(view: View, position: Int) {
                        if (position >= 0) {
                            if (!messagingDisabled) {
                                if (messages[position].messageTypeUi != MessageTypeUi.CONVERSATION_ACTION_MESSAGE) {
                                    if (ismActivityMessagesBinding.vSelectMultipleMessagesHeader.root.visibility == View.VISIBLE) {
                                        val messagesModel = messages[position]
                                        val selected = !messagesModel.isSelected
                                        messagesModel.isSelected = selected
                                        conversationMessagesPresenter.updateMessageSelectionStatus(
                                            messagesModel,
                                            selected
                                        )
                                        messages[position] = messagesModel
                                        conversationMessagesAdapter.notifyItemChanged(position)
                                    } else {
                                        val messagesModel = messages[position]
                                        if (!messagesModel.isSentMessage || messagesModel.isMessageSentSuccessfully) {
                                            if (!joiningAsObserver && clickActionsNotBlocked()) {
//                                                dismissAllDialogs()
//                                                openReactionDialog(view)
                                                if (!isFinishing && !messageActionFragment!!.isAdded) {
                                                    dismissAllDialogs()
                                                    messageActionFragment!!.updateParameters(
                                                        messages[position],
                                                        this@ConversationMessagesActivity, position
                                                    )
                                                    messageActionFragment!!.show(
                                                        supportFragmentManager,
                                                        MessageActionFragment.TAG
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
        )

        ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.ibClose.setOnClickListener { v: View? ->
            ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.root.visibility =
                View.GONE
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.root.visibility =
                View.GONE

            val size = messages.size
            var messagesModel: MessagesModel
            conversationMessagesPresenter.cleanupSelectedMessages()
            conversationMessagesAdapter!!.setMultipleMessagesSelectModeOn(false)
            for (i in 0 until size) {
                if (messages[i].isSelected) {
                    messagesModel = messages[i]
                    messagesModel.isSelected = false
                    messages[i] = messagesModel
                }
            }
            conversationMessagesAdapter!!.notifyDataSetChanged()
        }

        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.rlDeleteForMe.setOnClickListener { v: View? ->
            if (ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivDeleteForMe.isSelected) {
                deleteMessageForSelf(null, true)
            }
        }

        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.rlDeleteForAll.setOnClickListener { v: View? ->
            if (ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivDeleteForAll.isSelected) {
                deleteMessageForEveryone(null, true)
            }
        }

        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.rlCopy.setOnClickListener { v: View? ->
            if (ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivCopy.isSelected) {
                conversationMessagesPresenter.copyText()
            }
        }

        ismActivityMessagesBinding!!.btRecord.setRecordView(ismActivityMessagesBinding!!.vRecordView)
        ismActivityMessagesBinding!!.vRecordView.setLockEnabled(true)
        ismActivityMessagesBinding!!.vRecordView.setRecordLockImageView(ismActivityMessagesBinding!!.vRecordLock)
        ismActivityMessagesBinding!!.vRecordView.timeLimit =
            Constants.MAXIMUM_AUDIO_RECORDING_DURATION_MILLISECONDS
        ismActivityMessagesBinding!!.vRecordView.setActivityContext(this)

        ismActivityMessagesBinding!!.vRecordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart(hasRecordingPermission: Boolean) {
                //KeyboardUtil.hideKeyboard(ConversationMessagesActivity.this);
                if (hasRecordingPermission) {
                    ismActivityMessagesBinding!!.rlBottomLayout.visibility = View.INVISIBLE
                    conversationMessagesPresenter.startAudioRecording(this@ConversationMessagesActivity)
                } else {
                    if (ActivityCompat.checkSelfPermission(
                            this@ConversationMessagesActivity,
                            Manifest.permission.RECORD_AUDIO
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this@ConversationMessagesActivity,
                                Manifest.permission.RECORD_AUDIO
                            )
                        ) {
                            val snackbar = Snackbar.make(
                                ismActivityMessagesBinding!!.root,
                                getString(R.string.ism_request_record_audio_permission),
                                Snackbar.LENGTH_INDEFINITE
                            ).setAction(
                                getString(R.string.ism_ok)
                            ) { view1: View? ->
                                ActivityCompat.requestPermissions(
                                    this@ConversationMessagesActivity,
                                    arrayOf(Manifest.permission.RECORD_AUDIO),
                                    RECORD_AUDIO_PERMISSIONS_REQUEST_CODE
                                )
                            }

                            snackbar.show()
                        } else {
                            ActivityCompat.requestPermissions(
                                this@ConversationMessagesActivity, arrayOf(
                                    Manifest.permission.RECORD_AUDIO
                                ), RECORD_AUDIO_PERMISSIONS_REQUEST_CODE
                            )
                        }
                    }
                }
            }

            override fun onCancel() {
                //On Swipe To Cancel
                conversationMessagesPresenter.stopAudioRecording(true)
            }

            override fun onFinish(recordTime: Long, limitReached: Boolean) {
                //Stop Recording..
                //limitReached to determine if the Record was finished when time limit reached.
                ismActivityMessagesBinding!!.rlBottomLayout.visibility = View.VISIBLE
                ismActivityMessagesBinding!!.btRecord.visibility = View.VISIBLE
                conversationMessagesPresenter.stopAudioRecording(false)
            }

            override fun onLessThanSecond() {
                //When the record time is less than One Second
                ismActivityMessagesBinding!!.rlBottomLayout.visibility = View.VISIBLE
                ismActivityMessagesBinding!!.btRecord.visibility = View.VISIBLE
                conversationMessagesPresenter.stopAudioRecording(true)
            }

            override fun switchedToLockedMode() {
                ismActivityMessagesBinding!!.btRecord.visibility = View.GONE
            }
        })

        ismActivityMessagesBinding!!.vRecordView.setOnBasketAnimationEndListener {
            ismActivityMessagesBinding!!.rlBottomLayout.visibility = View.VISIBLE
            ismActivityMessagesBinding!!.btRecord.visibility = View.VISIBLE
        }

        ismActivityMessagesBinding!!.ivSearch.setOnClickListener { v: View? ->
            if (ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.root.visibility == View.GONE && clickActionsNotBlocked()) {
                if (ismActivityMessagesBinding!!.rlSearch.visibility == View.VISIBLE) {
                    KeyboardUtil.hideKeyboard(this)
                    ismActivityMessagesBinding!!.rlSearch.visibility = View.GONE
                } else {
                    ismActivityMessagesBinding!!.rlSearch.visibility = View.VISIBLE
                    KeyboardUtil.showSoftKeyboard(
                        this,
                        ismActivityMessagesBinding!!.etSearch
                    )
                }
            }
        }

        ismActivityMessagesBinding!!.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!joiningAsObserver) {
                    if (s.length > 0) {
                        fetchMessages(true, s.toString(), false)
                    } else {
                        fetchMessages(false, null, false)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        ismActivityMessagesBinding!!.rlDeleteConversation.setOnClickListener { v: View? ->
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.ism_delete_conversation))
                .setMessage(getString(R.string.ism_delete_conversation_alert)).setCancelable(true)
                .setPositiveButton(
                    getString(R.string.ism_continue)
                ) { dialog: DialogInterface, id: Int ->
                    dialog.cancel()
                    conversationMessagesPresenter.deleteConversation()
                }.setNegativeButton(
                    getString(R.string.ism_cancel)
                ) { dialog: DialogInterface, id: Int -> dialog.cancel() }.create().show()
        }

        if (intent.getBooleanExtra("fromNotification", false)) {
            try {
                IsometrikChatSdk.instance.isometrik.executor.execute {
                    IsometrikChatSdk.instance.isometrik.createConnection(
                        IsometrikChatSdk.instance.userSession.userId + IsometrikChatSdk.instance.userSession.deviceId,
                        IsometrikChatSdk.instance.userSession.userToken
                    )
                }
            } catch (ignore: Exception) {
            }
        }

        ismActivityMessagesBinding!!.ivRefreshOnlineStatus.setOnClickListener { v: View? ->
            if (ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.root.visibility == View.GONE && clickActionsNotBlocked()) {
                conversationMessagesPresenter.requestConversationDetails()
            }
        }

        ismActivityMessagesBinding!!.ivObservers.setOnClickListener { v: View? ->
            val intent = Intent(
                this@ConversationMessagesActivity,
                ObserversActivity::class.java
            )
            intent.putExtra("conversationId", conversationId)
            startActivity(intent)
        }

        ismActivityMessagesBinding!!.ivMore.setOnClickListener { v: View? ->
            if (opponentUserBlocked) { // condition used for opponentUser Blocked
                Toast.makeText(
                    this,
                    R.string.ism_blocked_action,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if ((ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.root.visibility
                            == View.GONE) && clickActionsNotBlocked()
                ) {
                    val popup = PopupMenu(this, v)
                    val inflater = popup.menuInflater
                    if (messagingDisabled) {
                        inflater.inflate(R.menu.ism_unblock_menu, popup.menu)
                    } else {
                        inflater.inflate(R.menu.ism_block_menu, popup.menu)
                    }
                    popup.setOnMenuItemClickListener { item: MenuItem ->
                        if (item.itemId == R.id.blockOrUnBlockUser) {
                            if (messagingDisabled) {
                                showProgressDialog(
                                    getString(
                                        R.string.ism_unblocking_user,
                                        conversationUserFullName
                                    )
                                )
                                conversationMessagesPresenter.unBlockUser(
                                    isometrikUserId,
                                    false,
                                    userPersonalUserId
                                )
                            } else {
                                showProgressDialog(
                                    getString(
                                        R.string.ism_blocking_user,
                                        conversationUserFullName
                                    )
                                )
                                conversationMessagesPresenter.blockUser(
                                    isometrikUserId,
                                    true,
                                    userPersonalUserId
                                )
                            }
                            return@setOnMenuItemClickListener true
                        }
                        if (item.itemId == R.id.clearChat) {
                            // implement clear chat feature
                            AlertDialog.Builder(this)
                                .setTitle(getString(R.string.ism_clear_conversation))
                                .setMessage(getString(R.string.ism_clear_conversation_alert))
                                .setCancelable(true)
                                .setPositiveButton(getString(R.string.ism_continue)) { dialog: DialogInterface, id: Int ->
                                    dialog.cancel()
                                    showProgressDialog(getString(R.string.ism_clearing_conversation))
                                    conversationMessagesPresenter.clearConversation(
                                        conversationId
                                    )
                                }
                                .setNegativeButton(
                                    getString(R.string.ism_cancel)
                                ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
                                .create()
                                .show()
                        }
                        false
                    }
                    popup.show()
                }
            }
        }

        ismActivityMessagesBinding!!.ivAudioCall.setOnClickListener { v: View? ->
            IsometrikChatSdk.instance.chatActionsClickListener?.onCallClicked(
                true,
                isometrikUserId!!,
                conversationUserFullName + IsometrikChatSdk.instance.userSession.userName,
                conversationUserFullName!!,
                conversationUserImageUrl!!
            )
        }

        ismActivityMessagesBinding!!.ivVideoCall.setOnClickListener { v: View? ->
            IsometrikChatSdk.instance.chatActionsClickListener?.onCallClicked(
                false,
                isometrikUserId!!,
                conversationUserFullName + IsometrikChatSdk.instance.userSession.userName,
                conversationUserFullName!!,
                conversationUserImageUrl!!
            )
        }

        startDetailsApiPolling()
    }

    override fun blockedStatus(blockByOpponentUser: Boolean) {
        opponentUserBlocked = blockByOpponentUser
    }

    private fun showProgressDialog(message: String) {
        alertDialog = alertProgress!!.getProgressDialog(this, message)
        if (!isFinishing) alertDialog?.show()
    }


    private val sendMessageTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            if (count > 0) {
                val end = start + count
                val message = ismActivityMessagesBinding!!.etSendMessage.editableText
                val list = message.getSpans(
                    start, end,
                    MentionedUserSpan::class.java
                )

                for (span in list) {
                    val spanStart = message.getSpanStart(span)
                    val spanEnd = message.getSpanEnd(span)
                    if ((spanStart < end) && (spanEnd > start)) {
                        spansToRemove.add(span)
                    }
                }
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.length > 0) {
                ismActivityMessagesBinding!!.rlRecordAudio.visibility = View.GONE
                ismActivityMessagesBinding!!.ivCaptureImage.visibility = View.GONE
//                ismActivityMessagesBinding.ivAddAttachment2.visibility = View.GONE
            } else {
                ismActivityMessagesBinding.ivCaptureImage.visibility =
                    if (ChatConfig.hideCaptureCameraOption) View.GONE else View.VISIBLE
                ismActivityMessagesBinding!!.rlRecordAudio.visibility
                if (ChatConfig.hideRecordAudioOption) View.GONE else View.VISIBLE
//                ismActivityMessagesBinding.ivAddAttachment2.visibility = View.VISIBLE

            }
            if (!joiningAsObserver) conversationMessagesPresenter!!.sendTypingMessage()
        }

        override fun afterTextChanged(s: Editable) {
            if (!joiningAsObserver) {
                if (s.length > 0) {
                    val searchTag = TagUserUtil.getTaggedUsernameToSearch(
                        s.toString(),
                        ismActivityMessagesBinding!!.etSendMessage.selectionStart
                    )

                    if (searchTag != null) {
                        conversationMessagesPresenter!!.searchUserToTag(searchTag)
                    } else {
                        ismActivityMessagesBinding!!.vTagUsers.root.visibility =
                            View.GONE
                    }
                } else {
                    ismActivityMessagesBinding!!.vTagUsers.root.visibility = View.GONE
                }

                val message = ismActivityMessagesBinding!!.etSendMessage.editableText

                for (span in spansToRemove) {
                    val start = message.getSpanStart(span)
                    val end = message.getSpanEnd(span)

                    message.removeSpan(span)
                    if (start != end) {
                        message.delete(start, end)
                    }
                }
                spansToRemove.clear()
            }
        }
    }

    override fun onMediaDownloadedComplete(
        successfullyCompleted: Boolean,
        messageId: String,
        mediaTypeDownloadedMessage: String,
        messagePosition: Int,
        downloadedMediaPath: String?
    ) {
        runOnUiThread {
            if (!successfullyCompleted) {
                val snackbar = Snackbar.make(
                    ismActivityMessagesBinding!!.root,
                    mediaTypeDownloadedMessage,
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
            }
            val position = conversationMessagesPresenter!!.verifyMessagePositionInList(
                messageId,
                messagePosition,
                messages
            )
            if (position != -1) {
                conversationMessagesAdapter!!.updateMessageStatusOnMediaDownloadFinished(
                    position,
                    successfullyCompleted,
                    downloadedMediaPath
                )
            }
        }
    }

    override fun onMediaDownloadCanceled(
        successfullyCanceled: Boolean,
        messageId: String,
        mediaTypeDownloadCanceledMessage: String,
        messagePosition: Int
    ) {
        runOnUiThread {
            if (!successfullyCanceled) {
                val snackbar = Snackbar.make(
                    ismActivityMessagesBinding!!.root,
                    mediaTypeDownloadCanceledMessage,
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
            }
            val position = conversationMessagesPresenter!!.verifyMessagePositionInList(
                messageId,
                messagePosition,
                messages
            )
            if (position != -1) {
                conversationMessagesAdapter!!.updateMessageStatusOnDownloadingStateChanged(
                    position,
                    false
                )
            }
        }
    }

    override fun onMediaUploadCanceled(
        successfullyCanceled: Boolean,
        localMessageId: String,
        mediaTypeUploadCanceledMessage: String,
        messagePosition: Int
    ) {
        runOnUiThread {
            if (!successfullyCanceled) {
                val snackbar = Snackbar.make(
                    ismActivityMessagesBinding!!.root,
                    mediaTypeUploadCanceledMessage,
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
            }
            val position = conversationMessagesPresenter!!.verifyUnsentMessagePositionInList(
                localMessageId,
                messagePosition,
                messages
            )
            if (position != -1) {
                val messagesModel = messages[position]
                messagesModel.isUploading = false
                messages[position] = messagesModel
                conversationMessagesAdapter!!.notifyItemChanged(position)
            }
        }
    }

    override fun onDownloadProgressUpdate(messageId: String, messagePosition: Int, progress: Int) {
        runOnUiThread {
            val position = conversationMessagesPresenter!!.verifyMessagePositionInList(
                messageId,
                messagePosition,
                messages
            )
            if (position != -1) {
                conversationMessagesAdapter!!.updateProgressStatusOfMessage(
                    true,
                    position,
                    ismActivityMessagesBinding!!.rvMessages,
                    progress
                )
            }
        }
    }

    override fun onUploadProgressUpdate(
        localMessageId: String,
        mediaId: String,
        messagePosition: Int,
        progress: Int
    ) {
        runOnUiThread {
            val position = conversationMessagesPresenter!!.verifyUnsentMessagePositionInList(
                localMessageId,
                messagePosition,
                messages
            )
            if (position != -1) {
                conversationMessagesAdapter!!.updateProgressStatusOfMessage(
                    false,
                    position,
                    ismActivityMessagesBinding!!.rvMessages,
                    progress
                )
            }
        }
    }

    override fun onFailedToSendMessage(localMessageId: String, error: String?) {
        runOnUiThread {
            val position = conversationMessagesPresenter!!.verifyUnsentMessagePositionInList(
                localMessageId,
                messages.size - 1,
                messages
            )
            if (position != -1) {
                val messagesModel = messages[position]
                messagesModel.isSendingMessageFailed = true
                messages[position] = messagesModel
                conversationMessagesAdapter!!.notifyItemChanged(position)
            }
        }
    }

    override fun onMessageReactionClicked(messageId: String, reactionModel: ReactionModel) {
        if (!isFinishing && !fetchReactionUsersFragment!!.isAdded) {
            dismissAllDialogs()
            fetchReactionUsersFragment!!.updateParameters(
                conversationId, messageId, reactionModel,
                this, messagingDisabled
            )
            fetchReactionUsersFragment!!.show(
                supportFragmentManager,
                FetchReactionUsersFragment.TAG
            )
        }
    }

    override fun handleAudioMessagePlay(
        message: MessagesModel,
        downloaded: Boolean,
        position: Int,
        waveSeekBar: WaveformSeekBar,
        ivPlayAudio: AppCompatImageView
    ) {

        // Stop and release the current media player
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        // Reset the current WaveSeekBar progress
        currentWaveSeekBar?.progress = 0F
        currentWaveSeekBar = null
        currentIvPlayAudio?.setImageResource(R.drawable.ism_ic_play_audio)
        if (currentAudioPosition == position && isAudioPlaying) {
            isAudioPlaying = false
            return
        }
        currentAudioPosition = position
        ivPlayAudio.setImageResource(R.drawable.ism_ic_audio_buffer)
        // Cancel the existing coroutine job if any
        updateJob?.cancel()

        if (position != -1) {

            mediaPlayer = MediaPlayer().apply {
                setDataSource(message.audioUrl)
                prepare()
                start()

                setOnCompletionListener {
                    waveSeekBar.progress = 0F
                    isAudioPlaying = false
                    with(Dispatchers.Main) {
                        currentIvPlayAudio?.setImageResource(R.drawable.ism_ic_play_audio)
                    }
                    updateJob?.cancel()
                    Log.e("startProgressUpdate", "==> Completed")
                }
            }
            currentWaveSeekBar = waveSeekBar
            isAudioPlaying = true
            currentIvPlayAudio = ivPlayAudio
            currentIvPlayAudio?.setImageResource(R.drawable.ism_ic_pause_audio)

            // Start updating WaveSeekBar progress with coroutine
            startProgressUpdate(waveSeekBar)
        } else {
            isAudioPlaying = false
        }
    }

    private fun startProgressUpdate(waveSeekBar: WaveformSeekBar) {
        updateJob = lifecycleScope.launch {
            while (isAudioPlaying && mediaPlayer != null && currentWaveSeekBar == waveSeekBar) {
                val progress =
                    (mediaPlayer!!.currentPosition.toFloat() / mediaPlayer!!.duration) * 100
                waveSeekBar.progress = progress
                Log.e("startProgressUpdate", "==> ${progress}")
                delay(100L) // Update every 100ms
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAPTURE_IMAGE_PERMISSIONS_REQUEST_CODE -> {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestImageCapture()
                } else {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_permission_image_capture_denied,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }
            }

            DOWNLOAD_MEDIA_PERMISSIONS_REQUEST_CODE -> {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_storage_permission_to_download_granted,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                } else {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_storage_permission_to_download_denied,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }
            }

            SHARE_LOCATION_PERMISSIONS_REQUEST_CODE -> {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestLocationShare()
                } else {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_location_permission_denied,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }
            }

            RECORD_AUDIO_PERMISSIONS_REQUEST_CODE -> {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_press_hold_to_record_audio,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                } else {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_record_audio_denied,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }
            }

            SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE -> {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestMediaShareFromStorage(SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE)
                } else {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_permission_photos_share_denied,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }
            }

            SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE -> {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestMediaShareFromStorage(SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE)
                } else {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_permission_videos_share_denied,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }
            }

            SHARE_FILES_PERMISSIONS_REQUEST_CODE -> {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestMediaShareFromStorage(SHARE_FILES_PERMISSIONS_REQUEST_CODE)
                } else {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_permission_files_share_denied,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }
            }

            SHARE_CONTACT_PERMISSIONS_REQUEST_CODE -> {
                if (Utilities.isAllPermissionGranted(grantResults)) {
                    requestContactShare()
                } else {
                    val snackbar = Snackbar.make(
                        ismActivityMessagesBinding!!.root,
                        R.string.ism_permission_contact_share_denied,
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.show()
                }
            }
        }
    }

    override fun onMediaTypeToBeSharedSelected(mediaTypeSelected: MessageTypeUi) {
        when (mediaTypeSelected) {
            MessageTypeUi.CAMERA_PHOTO_SENT -> {
                //Capture Image
                checkImageCapturePermissions(true)
            }

            MessageTypeUi.RECORD_VIDEO_SENT -> {
                //RecordVideo
                onRecordVideoRequested()
            }

            MessageTypeUi.PHOTO_MESSAGE_SENT -> {
                //Photos
                checkAccessStoragePermissions(
                    SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE,
                    getString(R.string.ism_permission_photos_share)
                )
            }

            MessageTypeUi.VIDEO_MESSAGE_SENT -> {
                //Videos
                checkAccessStoragePermissions(
                    SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE,
                    getString(R.string.ism_permission_videos_share)
                )
            }

            MessageTypeUi.FILE_MESSAGE_SENT -> {
                //Files
                checkAccessStoragePermissions(
                    SHARE_FILES_PERMISSIONS_REQUEST_CODE,
                    getString(R.string.ism_permission_files_share)
                )
            }

            MessageTypeUi.GIF_MESSAGE_SENT,
            MessageTypeUi.STICKER_MESSAGE_SENT -> {
                //Sticker
                if (!isFinishing && !visualMediaFragment!!.isAdded) {
                    dismissAllDialogs()
                    visualMediaFragment!!.updateParameters(this)
                    visualMediaFragment!!.show(supportFragmentManager, VisualMediaFragment.TAG)
                }

//                if (!isFinishing && !stickersFragment!!.isAdded) {
//                    dismissAllDialogs()
//                    stickersFragment!!.updateParameters(this)
//                    stickersFragment!!.show(supportFragmentManager, StickersFragment.TAG)
//                }
            }

//            MessageTypeUi.GIF_MESSAGE_SENT -> {
//                //Gif
//                if (!isFinishing && !gifsFragment!!.isAdded) {
//                    dismissAllDialogs()
//                    gifsFragment!!.updateParameters(this)
//                    gifsFragment!!.show(supportFragmentManager, GifsFragment.TAG)
//                }
//            }

            MessageTypeUi.WHITEBOARD_MESSAGE_SENT -> {
                //Whiteboard
                if (!isFinishing && !whiteboardFragment!!.isAdded) {
                    dismissAllDialogs()
                    whiteboardFragment!!.updateParameters(this)
                    whiteboardFragment!!.show(supportFragmentManager, WhiteboardFragment.TAG)
                }
            }

            MessageTypeUi.LOCATION_MESSAGE_SENT -> {
                //Location
                if (ActivityCompat.checkSelfPermission(
                        this@ConversationMessagesActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    requestLocationShare()
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@ConversationMessagesActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        val snackbar = Snackbar.make(
                            ismActivityMessagesBinding!!.root,
                            getString(R.string.ism_request_location_permission),
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction(
                            getString(R.string.ism_ok)
                        ) { view: View? ->
                            ActivityCompat.requestPermissions(
                                this@ConversationMessagesActivity,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                SHARE_LOCATION_PERMISSIONS_REQUEST_CODE
                            )
                        }

                        snackbar.show()
                    } else {
                        ActivityCompat.requestPermissions(
                            this@ConversationMessagesActivity, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ), SHARE_LOCATION_PERMISSIONS_REQUEST_CODE
                        )
                    }
                }
            }

            MessageTypeUi.CONTACT_MESSAGE_SENT -> {
                //Contact
                if (ActivityCompat.checkSelfPermission(
                        this@ConversationMessagesActivity,
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    requestContactShare()
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@ConversationMessagesActivity,
                            Manifest.permission.READ_CONTACTS
                        )
                    ) {
                        val snackbar = Snackbar.make(
                            ismActivityMessagesBinding!!.root,
                            getString(R.string.ism_permission_contact_share),
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction(
                            getString(R.string.ism_ok)
                        ) { view: View? ->
                            ActivityCompat.requestPermissions(
                                this@ConversationMessagesActivity,
                                arrayOf(Manifest.permission.READ_CONTACTS),
                                SHARE_CONTACT_PERMISSIONS_REQUEST_CODE
                            )
                        }

                        snackbar.show()
                    } else {
                        ActivityCompat.requestPermissions(
                            this@ConversationMessagesActivity, arrayOf(
                                Manifest.permission.READ_CONTACTS
                            ), SHARE_CONTACT_PERMISSIONS_REQUEST_CODE
                        )
                    }
                }
            }

            else -> {

            }
        }
    }

    override fun gifShareRequested(gifName: String, gifImageUrl: String, gifStillUrl: String) {
        val gifAttachment = PrepareAttachmentHelper.prepareGifAttachment(
            gifName,
            gifImageUrl,
            gifStillUrl,
            gifStillUrl
        )
        dismissAllDialogs()
        if (gifAttachment == null) {
            Toast.makeText(this, R.string.ism_gif_share_failed, Toast.LENGTH_SHORT).show()
        } else {
            conversationMessagesPresenter!!.shareMessage(
                RemoteMessageTypes.NormalMessage,
                null,
                null,
                CustomMessageTypes.Gif,
                CustomMessageTypes.Gif.value,
                false,
                true,
                true,
                true,
                ArrayList(
                    listOf(gifAttachment)
                ),
                null,
                null,
                MessageTypeUi.GIF_MESSAGE_SENT,
                null,
                false,
                null,
                null
            )
        }
    }

    override fun stickerShareRequested(stickerName: String, stickerImageUrl: String) {
        val stickerAttachment = PrepareAttachmentHelper.prepareStickerAttachment(
            stickerName,
            stickerImageUrl,
            stickerImageUrl,
            stickerImageUrl
        )

        dismissAllDialogs()
        if (stickerAttachment == null) {
            Toast.makeText(this, R.string.ism_sticker_share_failed, Toast.LENGTH_SHORT).show()
        } else {
            conversationMessagesPresenter!!.shareMessage(
                RemoteMessageTypes.NormalMessage,
                null,
                null,
                CustomMessageTypes.Sticker,
                CustomMessageTypes.Sticker.value,
                false,
                true,
                true,
                true,
                ArrayList(
                    listOf(stickerAttachment)
                ),
                null,
                null,
                MessageTypeUi.STICKER_MESSAGE_SENT,
                null,
                false,
                null,
                null
            )
        }
    }

    override fun whiteboardShareRequested(whiteboardImageUrl: String) {
        conversationMessagesPresenter!!.shareMessage(
            RemoteMessageTypes.NormalMessage,
            null,
            null,
            CustomMessageTypes.Whiteboard,
            CustomMessageTypes.Whiteboard.value,
            false,
            true,
            true,
            true,
            null,
            null,
            null,
            MessageTypeUi.WHITEBOARD_MESSAGE_SENT,
            ArrayList(
                listOf(whiteboardImageUrl)
            ),
            true,
            PresignedUrlMediaTypes.Photo,
            AttachmentMessageType.Image
        )
    }

    override fun downloadMedia(
        messagesModel: MessagesModel,
        mediaType: String,
        messagePosition: Int
    ) {
        if (Utilities.checkSelfExternalStoragePermissionIsGranted(
                this@ConversationMessagesActivity,
                false
            )
        ) {
            val position = conversationMessagesPresenter!!.verifyMessagePositionInList(
                messagesModel.messageId,
                messagePosition,
                messages
            )
            if (position != -1) {
                conversationMessagesAdapter!!.updateMessageStatusOnDownloadingStateChanged(
                    position,
                    true
                )
            }
            conversationMessagesPresenter!!.downloadMedia(messagesModel, messagePosition)
        } else {
            if (Utilities.shouldShowExternalPermissionStorageRational(
                    this@ConversationMessagesActivity,
                    false
                )
            ) {
                val snackbar = Snackbar.make(
                    ismActivityMessagesBinding!!.root,
                    getString(R.string.ism_request_storage_permission_to_download, mediaType),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    getString(R.string.ism_ok)
                ) { view: View? ->
                    Utilities.requestExternalStoragePermission(
                        this@ConversationMessagesActivity,
                        DOWNLOAD_MEDIA_PERMISSIONS_REQUEST_CODE,
                        false
                    )
                }

                snackbar.show()
            } else {
                Utilities.requestExternalStoragePermission(
                    this@ConversationMessagesActivity,
                    DOWNLOAD_MEDIA_PERMISSIONS_REQUEST_CODE,
                    false
                )
            }
        }
    }

    /**
     * Cancel media download.
     *
     * @param messagesModel   the messages model
     * @param messagePosition the message position
     */
    override fun cancelMediaDownload(messagesModel: MessagesModel, messagePosition: Int) {
        conversationMessagesPresenter!!.cancelMediaDownload(messagesModel, messagePosition)
    }

    /**
     * Cancel media upload.
     *
     * @param messagesModel   the messages model
     * @param messagePosition the message position
     */
    override fun cancelMediaUpload(messagesModel: MessagesModel, messagePosition: Int) {
        conversationMessagesPresenter!!.cancelMediaUpload(messagesModel, messagePosition)
    }

    /**
     * Remove canceled message.
     *
     * @param localMessageId  the local message id
     * @param messagePosition the message position
     */
    override fun removeCanceledMessage(localMessageId: String, messagePosition: Int) {
        val position = conversationMessagesPresenter!!.verifyUnsentMessagePositionInList(
            localMessageId,
            messagePosition,
            messages
        )
        if (position != -1) {
            messages.removeAt(position)
            conversationMessagesAdapter!!.notifyItemRemoved(position)
            if (messages.size == 0) {
                if (ismActivityMessagesBinding!!.tvNoMessages.visibility == View.GONE) {
                    ismActivityMessagesBinding!!.tvNoMessages.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun addReactionForMessage(messageId: String) {
        if (clickActionsNotBlocked()) {
            if (!isFinishing && !addReactionFragment!!.isAdded) {
                dismissAllDialogs()
                addReactionFragment!!.updateParameters(
                    conversationId, messageId,
                    this
                )
                addReactionFragment!!.show(supportFragmentManager, AddReactionFragment.TAG)
            }
        }
    }

    override fun editMessageRequested(messagesModel: MessagesModel) {
        if (!isFinishing && !editMessageFragment!!.isAdded) {
            dismissAllDialogs()
            editMessageFragment!!.updateParameters(messagesModel, this)
            editMessageFragment!!.show(supportFragmentManager, EditMessageFragment.TAG)
        }
    }

    private fun requestLocationShare() {
        if (GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
        ) {
            if (LocationUtils.isLocationEnabled(this)) {
                shareLocationActivityLauncher!!.launch(
                    Intent(
                        this@ConversationMessagesActivity,
                        ShareLocationActivity::class.java
                    )
                )
            } else {
                val dialog = android.app.AlertDialog.Builder(this)
                dialog.setMessage(getString(R.string.ism_location_not_enabled))
                dialog.setPositiveButton(getString(R.string.ism_location_enable)) { paramDialogInterface: DialogInterface?, paramInt: Int ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                dialog.show()
            }
        } else {
            Toast.makeText(this, getString(R.string.ism_location_not_supported), Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Checking permissions for accessing media files
     */
    private fun checkImageCapturePermissions(requestPermissions: Boolean) {
        val permissionRequired: MutableList<String> = ArrayList()
        permissionRequired.add(Manifest.permission.RECORD_AUDIO)
        permissionRequired.add(Manifest.permission.CAMERA)

        // Add permissions for media access based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            permissionRequired.add(Manifest.permission.READ_MEDIA_IMAGES)
            permissionRequired.add(Manifest.permission.READ_MEDIA_VIDEO)
            permissionRequired.add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            permissionRequired.addAll(Arrays.asList(*Utilities.getStoragePermissions()))
        }

        // List to hold permissions that are not yet granted
        val permissionsNotGranted: MutableList<String> = ArrayList()

        // Check each permission and add to list if not granted
        for (permission in permissionRequired) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNotGranted.add(permission)
            }
        }

        // If there are any permissions not granted, request them
        if (!permissionsNotGranted.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNotGranted.toTypedArray<String>(),
                PERMISSION_REQUEST_CODE // Use the new request code
            )
        } else {
            // All permissions are granted, proceed with the action
            requestImageCapture()
        }
    }

    private fun requestCameraPermissions() {
        val permissionsRequired = ArrayList<String>()

        if (ActivityCompat.checkSelfPermission(
                this@ConversationMessagesActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsRequired.add(Manifest.permission.CAMERA)
        }
        if (ActivityCompat.checkSelfPermission(
                this@ConversationMessagesActivity,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsRequired.add(Manifest.permission.RECORD_AUDIO)
        }

        if (!Utilities.checkSelfExternalStoragePermissionIsGranted(
                this@ConversationMessagesActivity,
                true
            )
        ) {
            permissionsRequired.addAll(Utilities.getPermissionsListForExternalStorage(true))
        }

        ActivityCompat.requestPermissions(
            this@ConversationMessagesActivity,
            permissionsRequired.toTypedArray<String>(),
            CAPTURE_IMAGE_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun checkAccessStoragePermissions(
        permissionsRequestCode: Int,
        permissionRationale: String
    ) {
        if (Utilities.checkSelfExternalStoragePermissionIsGranted(this, true)) {
            // Permission already granted
            requestMediaShareFromStorage(permissionsRequestCode)
        } else {
            //not granted
            if (Utilities.shouldShowExternalPermissionStorageRational(this, true)) {
                val snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    permissionRationale,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    getString(R.string.ism_ok)
                ) { view: View? ->
                    Utilities.requestExternalStoragePermission(
                        this,
                        permissionsRequestCode,
                        true
                    )
                }
                snackbar.show()
                (snackbar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView).gravity =
                    Gravity.CENTER_HORIZONTAL
            } else {
                Utilities.requestExternalStoragePermission(this, permissionsRequestCode, true)
            }
        }
    }

    private fun requestImageCapture() {
        KeyboardUtil.hideKeyboard(this)
        cameraActivityLauncher!!.launch(Intent(this, CameraActivity::class.java))
    }

    private fun requestContactShare() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        contactPicker!!.launch(intent)
    }

    private fun requestMediaShareFromStorage(permissionsRequestCode: Int) {
        when (permissionsRequestCode) {
            SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE -> {
                multiplePhotosPicker!!.launch(GalleryIntentsUtil.getPhotosIntent(true))
            }

            SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE -> {
                multipleVideosPicker!!.launch(GalleryIntentsUtil.getVideosIntent(true))
            }

            SHARE_FILES_PERMISSIONS_REQUEST_CODE -> {
                multipleFilesPicker!!.launch(GalleryIntentsUtil.getFilesIntent(true))
            }
        }
    }

    override fun onMessageSentSuccessfully(
        localMessageId: String,
        messageId: String,
        mediaUrl: String?,
        thumbnailUrl: String?
    ) {
        runOnUiThread {
            val position = conversationMessagesPresenter!!.verifyUnsentMessagePositionInList(
                localMessageId,
                messages.size - 1,
                messages
            )
            if (position != -1) {
                val messagesModel = messages[position]
                messagesModel.isMessageSentSuccessfully = true
                messagesModel.messageId = messageId
                messagesModel.isUploaded = true
                messagesModel.isUploading = false
                if (mediaUrl != null) {
                    when (messagesModel.messageTypeUi) {
                        MessageTypeUi.PHOTO_MESSAGE_SENT -> {
                            messagesModel.photoMainUrl = mediaUrl
                            messagesModel.photoThumbnailUrl = thumbnailUrl
                        }

                        MessageTypeUi.VIDEO_MESSAGE_SENT -> {
                            messagesModel.videoMainUrl = mediaUrl
                            messagesModel.videoThumbnailUrl = thumbnailUrl
                        }

                        MessageTypeUi.WHITEBOARD_MESSAGE_SENT -> {
                            messagesModel.whiteboardMainUrl = mediaUrl
                            messagesModel.whiteboardThumbnailUrl = thumbnailUrl
                        }

                        MessageTypeUi.FILE_MESSAGE_SENT -> {
                            messagesModel.fileUrl = mediaUrl
                        }

                        MessageTypeUi.AUDIO_MESSAGE_SENT -> {
                            messagesModel.audioUrl = mediaUrl
                        }

                        else -> {}
                    }
                }

                messages[position] = messagesModel
                conversationMessagesAdapter!!.notifyItemChanged(position)
            }
        }
    }

    override fun onError(errorMessage: String?) {
        if (ismActivityMessagesBinding!!.refresh.isRefreshing) {
            ismActivityMessagesBinding!!.refresh.isRefreshing = false
        }
        updateShimmerVisibility(false)
        runOnUiThread {
            if (errorMessage != null) {
                if (Utilities.showToast(errorMessage)) {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.ism_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun addSentMessageInUILocally(messagesModel: MessagesModel, uploadRequired: Boolean) {
        runOnUiThread {
            if (ismActivityMessagesBinding!!.tvNoMessages.visibility == View.VISIBLE) {
                ismActivityMessagesBinding!!.tvNoMessages.visibility = View.GONE
            }
            messages.add(messagesModel)
            conversationMessagesAdapter!!.notifyItemInserted(messages.size - 1)
            scrollToLastMessage()
            if (uploadRequired) {
                conversationMessagesPresenter!!.saveUploadingMessagePosition(
                    messagesModel.localMessageId,
                    messages.size - 1
                )
            }
            onMessageUpdated(conversationDetailsUtil, messages)
        }
    }

    override fun addMessageInUI(messagesModel: MessagesModel) {
        log("real:addMessageInUI", "Added in UI messagesModel")
        runOnUiThread {
            if (ismActivityMessagesBinding!!.tvNoMessages.visibility == View.VISIBLE) {
                ismActivityMessagesBinding!!.tvNoMessages.visibility = View.GONE
            }
            messages.add(messagesModel)
            conversationMessagesAdapter!!.notifyItemInserted(messages.size - 1)
            scrollToLastMessage()
            onMessageUpdated(conversationDetailsUtil, messages)
        }
    }

    override fun addMessagesInUI(
        messagesModel: ArrayList<MessagesModel>,
        refreshRequest: Boolean,
        hideSearchingMessageOverlay: Boolean,
        messageFound: Boolean,
        onReconnect: Boolean
    ) {
        runOnUiThread {
            if (onReconnect) {
                try {
                    if (messagesModel[messagesModel.size - 1].messageId == messages[messages.size - 1].messageId) {
                        return@runOnUiThread
                    }
                } catch (ignore: Exception) {
                }
            }
            if (refreshRequest) {
                messages.clear()
                messages.addAll(0, messagesModel)
                conversationMessagesAdapter!!.notifyDataSetChanged()
                ismActivityMessagesBinding!!.refresh.isRefreshing = false

                if (!scrollToMessageNeeded) {
                    scrollToLastMessage()
                }
            } else {
                messages.addAll(0, messagesModel)
                conversationMessagesAdapter!!.notifyItemRangeInserted(0, messagesModel.size)
            }

            if (scrollToMessageNeeded) {
                if (hideSearchingMessageOverlay) {
                    scrollToMessageNeeded = false

                    if (messageFound) {
                        val messageIdToScrollTo =
                            intent.extras!!.getString("messageId")
                        for (i in messages.indices) {
                            if (messages[i].messageId != null && messages[i].messageId == messageIdToScrollTo) {
                                try {
                                    val finalI = i
                                    handler.postDelayed({
                                        messagesLayoutManager!!.scrollToPositionWithOffset(
                                            finalI,
                                            0
                                        )
                                        //                                        ismActivityMessagesBinding.ivSearch.setVisibility(View.VISIBLE);
                                        ismActivityMessagesBinding!!.vLoadingOverlay.root.visibility =
                                            View.GONE
                                        updateShimmerVisibility(false)
                                    }, 100)
                                } catch (ignore: IndexOutOfBoundsException) {
//                                    ismActivityMessagesBinding.ivSearch.setVisibility(View.VISIBLE);
                                    ismActivityMessagesBinding!!.vLoadingOverlay.root.visibility =
                                        View.GONE
                                    updateShimmerVisibility(false)
                                } catch (ignore: NullPointerException) {
                                    ismActivityMessagesBinding!!.vLoadingOverlay.root.visibility =
                                        View.GONE
                                    updateShimmerVisibility(false)
                                }
                                break
                            }
                        }
                    } else {
//                        ismActivityMessagesBinding.ivSearch.setVisibility(View.VISIBLE);
                        ismActivityMessagesBinding!!.vLoadingOverlay.root.visibility =
                            View.GONE
                        scrollToLastMessage()
                        Toast.makeText(
                            this,
                            getString(R.string.ism_message_not_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            if (messages.size == 0) {
                if (ismActivityMessagesBinding!!.tvNoMessages.visibility == View.GONE) {
                    ismActivityMessagesBinding!!.tvNoMessages.visibility = View.VISIBLE
                }
            } else {
                if (ismActivityMessagesBinding!!.tvNoMessages.visibility == View.VISIBLE) {
                    ismActivityMessagesBinding!!.tvNoMessages.visibility = View.GONE
                }
            }
            onMessageUpdated(conversationDetailsUtil, messages)
        }
    }

    /**
     * Scrolls message list to last message on receipt of new message
     */
    private fun scrollToLastMessage() {
        runOnUiThread {
            try {
                handler.postDelayed({
                    messagesLayoutManager!!.scrollToPositionWithOffset(
                        conversationMessagesAdapter!!.itemCount - 1,
                        0
                    )
                    updateShimmerVisibility(false)
                }, 100)
            } catch (ignore: IndexOutOfBoundsException) {
                updateShimmerVisibility(false)
            } catch (ignore: NullPointerException) {
                updateShimmerVisibility(false)
            }
        }
    }

    private val messagesRecyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!joiningAsObserver) {
                    if (dy != 0 && messagesLayoutManager!!.findFirstVisibleItemPosition() == 0) {
                        conversationMessagesPresenter!!.fetchMessagesOnScroll()
                    }
                }
            }
        }

    private fun fetchMessages(
        isSearchRequest: Boolean,
        searchTag: String?,
        showProgressDialog: Boolean
    ) {
        //Have to disable refresh if downloading/uploading media

        if (ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.root.visibility == View.VISIBLE) {
            ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.ibClose.performClick()
        }
        //if(showProgressDialog) {
        //showProgressDialog(getString(R.string.ism_fetching_conversations));
        //}
        conversationMessagesPresenter!!.fetchMessages(0, true, isSearchRequest, searchTag, false)
    }

    private fun dismissAllDialogs() {
        if (!isFinishing) {
            try {
                if (addReactionFragment!!.dialog != null && addReactionFragment!!.dialog!!
                        .isShowing && !addReactionFragment!!.isRemoving
                ) {
                    addReactionFragment!!.dismiss()
                } else if (fetchReactionUsersFragment!!.dialog != null && fetchReactionUsersFragment!!.dialog!!
                        .isShowing && !fetchReactionUsersFragment!!.isRemoving
                ) {
                    fetchReactionUsersFragment!!.dismiss()
                } /*else if (gifsFragment!!.dialog != null && gifsFragment!!.dialog!!.isShowing && !gifsFragment!!.isRemoving) {
                    gifsFragment!!.dismiss()
                } else if (stickersFragment!!.dialog != null && stickersFragment!!.dialog!!
                        .isShowing && !stickersFragment!!.isRemoving
                ) {
                    stickersFragment!!.dismiss()
                }*/ else if (visualMediaFragment!!.dialog != null && visualMediaFragment!!.dialog!!
                        .isShowing && !visualMediaFragment!!.isRemoving
                ) {
                    visualMediaFragment!!.dismiss()
                } else if (shareMediaFragment!!.dialog != null && shareMediaFragment!!.dialog!!
                        .isShowing && !shareMediaFragment!!.isRemoving
                ) {
                    shareMediaFragment!!.dismiss()
                } else if (whiteboardFragment!!.dialog != null && whiteboardFragment!!.dialog!!
                        .isShowing && !whiteboardFragment!!.isRemoving
                ) {
                    whiteboardFragment!!.dismiss()
                } else if (sendMessageReplyFragment!!.dialog != null && sendMessageReplyFragment!!.dialog!!
                        .isShowing && !sendMessageReplyFragment!!.isRemoving
                ) {
                    sendMessageReplyFragment!!.dismiss()
                } else if (messageActionFragment!!.dialog != null && messageActionFragment!!.dialog!!
                        .isShowing && !messageActionFragment!!.isRemoving
                ) {
                    messageActionFragment!!.dismiss()
                } else if (memberDetailsFragment!!.dialog != null && memberDetailsFragment!!.dialog!!
                        .isShowing && !memberDetailsFragment!!.isRemoving
                ) {
                    memberDetailsFragment!!.dismiss()
                } else if (editMessageFragment!!.dialog != null && editMessageFragment!!.dialog!!
                        .isShowing && !editMessageFragment!!.isRemoving
                ) {
                    editMessageFragment!!.dismiss()
                }
                KeyboardUtil.hideKeyboard(this)
            } catch (ignore: Exception) {
            }
        }
    }

    override fun replyMessageRequested(messagesModel: MessagesModel) {
        if (!isFinishing && !sendMessageReplyFragment!!.isAdded) {
            dismissAllDialogs()
            sendMessageReplyFragment!!.updateParameters(messagesModel, this)
            sendMessageReplyFragment!!.show(supportFragmentManager, SendMessageReplyFragment.TAG)
        }
    }

    override fun deleteMessageForSelf(messageId: String?, multipleMessagesSelected: Boolean) {
        AlertDialog.Builder(this).setTitle(getString(R.string.ism_delete_for_me_heading))
            .setMessage(getString(R.string.ism_delete_for_me_alert_message)).setCancelable(true)
            .setPositiveButton(
                getString(R.string.ism_continue)
            ) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                if (multipleMessagesSelected) {
                    conversationMessagesPresenter!!.deleteMessageForSelf(null, true)
                } else {
                    messageId?.let {
                        conversationMessagesPresenter!!.deleteMessageForSelf(
                            ArrayList(
                                listOf(it)
                            ), false
                        )
                    }
                }
            }.setNegativeButton(
                getString(R.string.ism_cancel)
            ) { dialog: DialogInterface, id: Int -> dialog.cancel() }.create().show()
    }

    override fun deleteMessageForEveryone(messageId: String?, multipleMessagesSelected: Boolean) {
        AlertDialog.Builder(this).setTitle(getString(R.string.ism_delete_for_all_heading))
            .setMessage(getString(R.string.ism_delete_for_all_alert_message)).setCancelable(true)
            .setPositiveButton(
                getString(R.string.ism_continue)
            ) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                if (multipleMessagesSelected) {
                    conversationMessagesPresenter!!.deleteMessageForEveryone(null, true)
                } else {
                    messageId?.let {
                        conversationMessagesPresenter!!.deleteMessageForEveryone(
                            ArrayList(
                                listOf(it)
                            ), false
                        )
                    }
                }
            }.setNegativeButton(
                getString(R.string.ism_cancel)
            ) { dialog: DialogInterface, id: Int -> dialog.cancel() }.create().show()
    }

    override fun selectMultipleMessagesRequested() {
        ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.root.visibility =
            View.VISIBLE
        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.root.visibility =
            View.VISIBLE

        conversationMessagesAdapter!!.setMultipleMessagesSelectModeOn(true)
        ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.tvSelectedMessagesCount.text =
            getString(R.string.ism_number_of_messages_selected, 0)
        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivCopy.isSelected = false
        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.tvCopy.isSelected = false
        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivDeleteForMe.isSelected = false
        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivDeleteForAll.isSelected = false
        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.tvDeleteForMe.isSelected = false
        ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.tvDeleteForAll.isSelected = false

        conversationMessagesAdapter!!.notifyDataSetChanged()
    }

    override fun fetchMessagesInfoRequest(messagesModel: MessagesModel) {
        if (messagesModel.messageTypeUi == MessageTypeUi.TEXT_MESSAGE_SENT || messagesModel.messageTypeUi == MessageTypeUi.TEXT_MESSAGE_RECEIVED) {
            //To handle  java.lang.IllegalArgumentException: class android.widget.ListView declares multiple JSON fields named mPendingCheckForTap for tagged users
            messagesModel.textMessage = SpannableString(messagesModel.textMessage.toString())
        }
        val intent = Intent(this, MessageDeliveryStatusActivity::class.java)
        intent.putExtra(
            "message",
            IsometrikChatSdk.instance.isometrik.gson.toJson(messagesModel)
        )
        intent.putExtra("conversationId", conversationId)
        intent.putExtra("messageId", messagesModel.messageId)
        intent.putExtra("sentAt", messagesModel.sentAt)
        startActivity(intent)
    }

    override fun forwardMessageRequest(messagesModel: MessagesModel) {
        if (clickActionsNotBlocked()) {
            if (messagesModel.messageTypeUi == MessageTypeUi.TEXT_MESSAGE_SENT || messagesModel.messageTypeUi == MessageTypeUi.TEXT_MESSAGE_RECEIVED) {
                //To handle  java.lang.IllegalArgumentException: class android.widget.ListView declares multiple JSON fields named mPendingCheckForTap for tagged users
                messagesModel.textMessage = SpannableString(messagesModel.textMessage.toString())
            }
            KeyboardUtil.hideKeyboard(this)

            val intent = Intent(this, ForwardMessageActivity::class.java)
            intent.putExtra(
                "message",
                IsometrikChatSdk.instance.isometrik.gson.toJson(messagesModel)
            )
            startActivity(intent)
        }
    }

    override fun onMessageSelectionStatus(multipleMessagesUtil: MultipleMessagesUtil) {
        ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.tvSelectedMessagesCount.text =
            getString(
                R.string.ism_number_of_messages_selected,
                multipleMessagesUtil.selectedMessagesCount
            )

        if (multipleMessagesUtil.isCopyEnabled) {
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivCopy.isSelected = true
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.tvCopy.isSelected = true
        } else {
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivCopy.isSelected = false
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.tvCopy.isSelected = false
        }

        if (multipleMessagesUtil.isDeleteEnabled) {
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivDeleteForMe.isSelected =
                true
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivDeleteForAll.isSelected =
                true
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.tvDeleteForMe.isSelected =
                true
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.tvDeleteForAll.isSelected =
                true
        } else {
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivDeleteForMe.isSelected =
                false
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.ivDeleteForAll.isSelected =
                false
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.tvDeleteForMe.isSelected =
                false
            ismActivityMessagesBinding!!.vSelectMultipleMessagesFooter.tvDeleteForAll.isSelected =
                false
        }
    }

    override fun sendReplyMessage(
        messageId: String,
        replyMessage: String,
        replyMessageDetails: JSONObject
    ) {
        conversationMessagesPresenter!!.shareMessage(
            RemoteMessageTypes.ReplyMessage,
            messageId,
            OriginalReplyMessageUtil(messageId, replyMessageDetails),
            CustomMessageTypes.Reply,
            replyMessage,
            false,
            true,
            true,
            true,
            null,
            replyMessageDetails,
            null,
            MessageTypeUi.REPLAY_MESSAGE_SENT,
            null,
            false,
            null,
            null
        )
    }

    override fun onTextCopyRequest(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, getString(R.string.ism_text_copied), Toast.LENGTH_SHORT).show()
    }

    override fun onMessageDeletedSuccessfully(messageIds: List<String>) {
        val size = messageIds.size
        runOnUiThread {
            var messageId: String
            for (j in 0 until size) {
                messageId = messageIds[j]
                for (i in messages.indices.reversed()) {
                    if (messages[i].messageId != null && messages[i].messageId == messageId) {
                        messages.removeAt(i)
                        conversationMessagesAdapter!!.notifyItemRemoved(i)
                        if (messages.size == 0) {
                            if (ismActivityMessagesBinding!!.tvNoMessages.visibility == View.GONE) {
                                ismActivityMessagesBinding!!.tvNoMessages.visibility =
                                    View.VISIBLE
                            }
                        }
                        break
                    }
                }
            }
        }
    }

    override fun onConversationCleared() {
        runOnUiThread {
            messages.clear()
            conversationMessagesAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onConversationTitleUpdated(newTitle: String) {
        runOnUiThread {
            conversationUserFullName = newTitle
            ismActivityMessagesBinding!!.tvConversationOrUserName.text = newTitle
            ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.tvConversationTitle.text =
                newTitle
        }
    }

    override fun onRemoteUserTypingEvent(message: String) {
        runOnUiThread {
            ismActivityMessagesBinding!!.tvTyping.text = message
            ismActivityMessagesBinding!!.tvTyping.visibility = View.VISIBLE
            try {
                handler.postDelayed(
                    typingMessageRunnable,
                    Constants.TYPING_MESSAGE_VISIBILITY_DURATION_IN_MS.toLong()
                )
            } catch (ignore: Exception) {
            }
        }
    }

    private val typingMessageRunnable = Runnable {
        try {
            ismActivityMessagesBinding!!.tvTyping.visibility = View.GONE
        } catch (ignore: Exception) {
        }
    }

    override fun markMessageAsDeliveredToAll(messageId: String) {
        runOnUiThread {
            for (i in messages.indices.reversed()) {
                if (messages[i].messageId != null && messages[i].messageId == messageId) {
                    val messagesModel = messages[i]
                    messagesModel.isDeliveredToAll = true
                    messages[i] = messagesModel
                    conversationMessagesAdapter!!.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun markMessageAsReadByAll(messageId: String) {
        runOnUiThread {
            for (i in messages.indices.reversed()) {
                if (messages[i].messageId != null && messages[i].messageId == messageId) {
                    val messagesModel = messages[i]
                    messagesModel.isDeliveredToAll = true
                    messagesModel.isReadByAll = true

                    messages[i] = messagesModel
                    conversationMessagesAdapter!!.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun onMultipleMessagesMarkedAsReadEvent() {
        conversationMessagesPresenter!!.fetchMessageDeliveryReadStatusOnMultipleMessagesMarkedAsReadEvent(
            messages
        )
    }

    override fun updateMessageReaction(
        messageId: String,
        reactionModel: ReactionModel,
        reactionAdded: Boolean
    ) {
        runOnUiThread {
            for (i in messages.indices.reversed()) {
                if (messages[i].messageId != null && messages[i].messageId == messageId) {
                    val messagesModel = messages[i]

                    if (messagesModel.hasReactions()) {
                        val reactions = messagesModel.reactions

                        var reactionAlreadyExists = false
                        for (j in reactions.indices.reversed()) {
                            if (reactions[j].reactionType == reactionModel.reactionType) {
                                if (reactionAdded) {
                                    reactions[j] = reactionModel
                                } else {
                                    if (reactionModel.reactionCount == 0) {
                                        reactions.removeAt(j)
                                    } else {
                                        reactions[j] = reactionModel
                                    }
                                }
                                reactionAlreadyExists = true
                                break
                            }
                        }
                        if (!reactionAlreadyExists && (reactionAdded || reactionModel.reactionCount > 0)) {
                            reactions.add(0, reactionModel)
                        }
                        messagesModel.reactions = reactions
                    } else {
                        if (reactionAdded || reactionModel.reactionCount > 0) {
                            messagesModel.reactions =
                                ArrayList(
                                    listOf(
                                        reactionModel
                                    )
                                )
                        }
                    }

                    messages[i] = messagesModel
                    conversationMessagesAdapter!!.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun closeConversation() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (clickActionsNotBlocked()) {
            unregisterListeners()
            KeyboardUtil.hideKeyboard(this)
            try {
                super.onBackPressed()
            } catch (ignore: Exception) {
            }
        }
    }

    override fun onDestroy() {
        conversationId = null
        unregisterListeners()
        mediaPlayer?.release()
        mediaPlayer = null
        updateJob?.cancel()
        super.onDestroy()
    }

    /**
     * Cleanup all realtime isometrik event listeners that were added at time of exit
     */
    private fun unregisterListeners() {
        if (!unregisteredListeners) {
            if (joiningAsObserver) {
                conversationMessagesPresenter!!.leaveAsObserver()
            }
            handler.removeCallbacksAndMessages(null)
            dismissAllDialogs()
            unregisteredListeners = true
            conversationMessagesPresenter!!.unregisterConnectionEventListener()
            conversationMessagesPresenter!!.unregisterConversationEventListener()
            conversationMessagesPresenter!!.unregisterMessageEventListener()
            conversationMessagesPresenter!!.unregisterMembershipControlEventListener()
            conversationMessagesPresenter!!.unregisterReactionEventListener()
            conversationMessagesPresenter!!.unregisterUserEventListener()
        }
    }

    override fun onAudioRecordedSuccessfully(audioFilePath: String) {
        AlertDialog.Builder(this).setTitle(getString(R.string.ism_audio_recordings_alert_heading))
            .setMessage(getString(R.string.ism_audio_recordings_alert_message)).setCancelable(true)
            .setPositiveButton(
                getString(R.string.ism_send)
            ) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                conversationMessagesPresenter!!.shareMessage(
                    RemoteMessageTypes.NormalMessage,
                    null,
                    null,
                    CustomMessageTypes.Audio,
                    CustomMessageTypes.Audio.value,
                    false,
                    true,
                    true,
                    true,
                    null,
                    null,
                    null,
                    MessageTypeUi.AUDIO_MESSAGE_SENT,
                    ArrayList(
                        listOf(audioFilePath)
                    ),
                    true,
                    PresignedUrlMediaTypes.Audio,
                    AttachmentMessageType.Audio
                )
            }
            .setNegativeButton(getString(R.string.ism_discard)) { dialog: DialogInterface, id: Int ->
                dialog.cancel()
                AudioFileUtil.deleteRecordingFile(audioFilePath)
            }.setCancelable(false).create().show()
    }

    override fun onMessageUpdatedSuccessfully(messageId: String, updatedMessage: String) {
        runOnUiThread {
            for (i in messages.indices.reversed()) {
                if (messages[i].messageId != null && messages[i].messageId == messageId) {
                    val messagesModel = messages[i]
                    messagesModel.isEditedMessage = true
                    messagesModel.textMessage = SpannableString(updatedMessage)
                    messages[i] = messagesModel
                    conversationMessagesAdapter!!.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun updateMessage(messageId: String, updatedMessage: String, originalMessage: String) {
        conversationMessagesPresenter!!.updateMessage(messageId, updatedMessage, originalMessage)
    }

    override fun updateParticipantsCount(participantsCount: Int) {
        runOnUiThread {
            ismActivityMessagesBinding!!.tvParticipantsCountOrOnlineStatus.text =
                getString(R.string.ism_participants_count, participantsCount)
        }
    }

    override fun onSearchedUsersFetched(usersModels: ArrayList<TagUserModel>) {
        if (usersModels.size > 0) {
            tagUserModels.clear()
            tagUserModels.addAll(usersModels)
            tagUserAdapter!!.notifyDataSetChanged()

            ismActivityMessagesBinding!!.vTagUsers.root.visibility = View.VISIBLE
        } else {
            ismActivityMessagesBinding!!.vTagUsers.root.visibility = View.GONE
        }
    }

    override fun onTaggedUserClicked(memberId: String) {
        if (clickActionsNotBlocked() && !messagingDisabled) {
            if (!isFinishing && !memberDetailsFragment!!.isAdded) {
                dismissAllDialogs()
                memberDetailsFragment!!.updateParameters(conversationId, memberId)
                memberDetailsFragment!!.show(supportFragmentManager, MemberDetailsFragment.TAG)
            }
        }
    }

    override fun onConversationDeletedSuccessfully() {
        onBackPressed()
    }

    private fun clickActionsNotBlocked(): Boolean {
        return !conversationMessagesPresenter!!.isRecordingAudio
    }

    /**
     * Handle click on message cell.
     *
     * @param messagesModel the messages model
     * @param localMedia    the local media
     */
    override fun handleClickOnMessageCell(messagesModel: MessagesModel, localMedia: Boolean) {
        if (ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.root.visibility == View.GONE && clickActionsNotBlocked()) {
            if (!messagesModel.isUploading && !messagesModel.isDownloading) {
                PreviewMessageUtil.previewMessage(
                    this@ConversationMessagesActivity,
                    messagesModel,
                    localMedia
                )
            }
        }
    }

    override fun updateConversationDetailsInHeader(
        local: Boolean,
        isPrivateOneToOne: Boolean,
        userName: String?,
        isOnline: Boolean,
        lastSeenAt: Long,
        conversationTitle: String?,
        participantsCount: Int,
        conversationImage : String
    ) {
        var userName: String? = userName
        var isOnline = isOnline
        var lastSeenAt = lastSeenAt
        var conversationTitle: String? = conversationTitle
        var participantsCount = participantsCount
        var conversationImage = conversationImage
        if (isPrivateOneToOne) {
            if (local) {
                userName = if (intent.extras!!.containsKey("userName")) {
                    intent.extras!!.getString("userName")
                } else {
                    ""
                }
            }
            conversationUserFullName = userName
            ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.tvConversationTitle.text =
                userName
            ismActivityMessagesBinding!!.ivOnlineStatus.visibility = View.VISIBLE

            //            ismActivityMessagesBinding.ivRefreshOnlineStatus.setVisibility(messagingDisabled ? View.GONE : View.VISIBLE);
            if (local) {
                isOnline = if (intent.extras!!.containsKey("isOnline")) {
                    intent.extras!!.getBoolean("isOnline")
                } else {
                    false
                }
                if (!isOnline) {
                    lastSeenAt = if (intent.extras!!.containsKey("lastSeenAt")) {
                        intent.extras!!.getLong("lastSeenAt")
                    } else {
                        0
                    }
                }
            }
            if (messagingDisabled) {
                ismActivityMessagesBinding!!.ivOnlineStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, R.drawable.ism_ic_blocked
                    )
                )
                ismActivityMessagesBinding!!.tvParticipantsCountOrOnlineStatus.text =
                    getString(R.string.ism_unavailable)
                val spannableString = SpannableString(userName)
                spannableString.setSpan(StyleSpan(Typeface.ITALIC), 0, spannableString.length, 0)
                ismActivityMessagesBinding!!.tvConversationOrUserName.text = spannableString
            } else {
                ismActivityMessagesBinding!!.tvConversationOrUserName.text = userName

                if (isOnline) {
                    ismActivityMessagesBinding!!.ivOnlineStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            this, R.drawable.ism_user_online_status_circle
                        )
                    )
                    ismActivityMessagesBinding!!.tvParticipantsCountOrOnlineStatus.text =
                        getString(R.string.ism_online)
                } else {
                    ismActivityMessagesBinding!!.ivOnlineStatus.setImageDrawable(
                        ContextCompat.getDrawable(
                            this, R.drawable.ism_user_offline_status_circle
                        )
                    )

                    if (lastSeenAt == 0L) {
                        ismActivityMessagesBinding!!.tvParticipantsCountOrOnlineStatus.text =
                            getString(R.string.ism_offline)
                    } else {
                        ismActivityMessagesBinding!!.tvParticipantsCountOrOnlineStatus.text =
                            getString(
                                R.string.ism_last_seen_at,
                                TimeUtil.formatTimestampToBothDateAndTime(
                                    lastSeenAt
                                )
                            )
                    }
                }
            }
            loadHeaderPic(conversationImage,conversationUserFullName.orEmpty())

        } else {
            if (local) {
                conversationTitle = intent.extras!!.getString("conversationTitle")
            }

            ismActivityMessagesBinding!!.ivOnlineStatus.visibility = View.GONE

            if(ChatConfig.hideSubHeader){
                ismActivityMessagesBinding!!.tvParticipantsCountOrOnlineStatus.visibility =  View.GONE
            }

            //            ismActivityMessagesBinding.ivRefreshOnlineStatus.setVisibility(View.GONE);
            if (local) {
                participantsCount = if (intent.extras!!.containsKey("participantsCount")) {
                    intent.extras!!.getInt("participantsCount")
                } else {
                    1
                }
            }
            if(participantsCount <= 2){
                ismActivityMessagesBinding!!.tvConversationOrUserName.text = userName.orEmpty()
                ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.tvConversationTitle.text = userName.orEmpty()
                ismActivityMessagesBinding!!.tvParticipantsCountOrOnlineStatus.text = conversationTitle
            }else{
                ismActivityMessagesBinding!!.tvConversationOrUserName.text = conversationTitle
                ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.tvConversationTitle.text = conversationTitle
                ismActivityMessagesBinding!!.tvParticipantsCountOrOnlineStatus.text =
                    getString(R.string.ism_participants_count, participantsCount)
            }


            loadHeaderPic(conversationImage,conversationTitle.orEmpty())
        }
    }

    private fun loadHeaderPic(picUrl : String, userName: String){
        if (PlaceholderUtils.isValidImageUrl(picUrl)) {
            try {
                Glide.with(this)
                    .load(picUrl)
                    .placeholder(R.drawable.ism_ic_profile)
                    .transform(CircleCrop())
                    .into(ismActivityMessagesBinding.ivConversationImage)
            } catch (ignore: IllegalArgumentException) {
            } catch (ignore: NullPointerException) {
            }
        } else {
            PlaceholderUtils.setTextRoundDrawable(
                this,
                userName,
                ismActivityMessagesBinding.ivConversationImage,
                0,
                12
            )
        }
    }

    override fun fetchedConversationDetails(conversationDetailsUtil: ConversationDetailsUtil) {
        this.conversationDetailsUtil = conversationDetailsUtil
        onMessageUpdated(conversationDetailsUtil, messages)
    }

    override fun messageToScrollToNotFound() {
        runOnUiThread {
            scrollToMessageNeeded = false
            //            ismActivityMessagesBinding.ivSearch.setVisibility(View.VISIBLE);
            ismActivityMessagesBinding!!.vLoadingOverlay.root.visibility = View.GONE
            scrollToLastMessage()
            Toast.makeText(
                this,
                getString(R.string.ism_message_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onMessagingStatusChanged(disabled: Boolean) {
        runOnUiThread {
            messagingDisabled = disabled
            ismActivityMessagesBinding!!.rlBottomLayout.visibility =
                if (disabled) View.INVISIBLE else View.VISIBLE
            ismActivityMessagesBinding!!.rlRecordAudio.visibility =
                if (disabled) View.GONE else View.VISIBLE
            ismActivityMessagesBinding!!.rlDeleteConversation.visibility =
                if (disabled) View.VISIBLE else View.GONE
            if (disabled) {
                if (!conversationMessagesAdapter!!.isMessagingDisabled) {
                    conversationMessagesAdapter!!.isMessagingDisabled = true
                    conversationMessagesAdapter!!.notifyDataSetChanged()
                }
                ismActivityMessagesBinding!!.ivOnlineStatus.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ism_ic_blocked
                    )
                )
                ismActivityMessagesBinding!!.tvParticipantsCountOrOnlineStatus.text =
                    getString(R.string.ism_unavailable)
                //                ismActivityMessagesBinding.ivRefreshOnlineStatus.setVisibility(View.GONE);
            } else {
                if (conversationMessagesAdapter!!.isMessagingDisabled) {
                    conversationMessagesAdapter!!.isMessagingDisabled = false
                    conversationMessagesAdapter!!.notifyDataSetChanged()
                }
                if (conversationMessagesPresenter!!.isPrivateOneToOne) {
//                    ismActivityMessagesBinding.ivRefreshOnlineStatus.setVisibility(View.VISIBLE);
                    conversationMessagesPresenter!!.requestConversationDetails()
                }
            }
        }
    }

    override fun connectionStateChanged(connected: Boolean) {
        if(!ChatConfig.disableOfflineLabel){
            runOnUiThread {
                ismActivityMessagesBinding!!.incConnectionState.tvConnectionState.visibility =
                    if (connected) View.GONE else View.VISIBLE
            }
        }

    }

    private fun updateShimmerVisibility(visible: Boolean) {
        if (visible) {
            ismActivityMessagesBinding!!.incShimmer.rlConversationDetailsOne.root.visibility =
                View.GONE
            ismActivityMessagesBinding!!.incShimmer.rlConversationDetailsTwo.root.visibility =
                View.GONE
            ismActivityMessagesBinding!!.incShimmer.rlConversationDetailsThree.root.visibility =
                View.GONE
            ismActivityMessagesBinding!!.incShimmer.rlConversationDetailsFour.root.visibility =
                View.GONE
            ismActivityMessagesBinding!!.incShimmer.rlConversationDetailsFive.root.visibility =
                View.GONE

            ismActivityMessagesBinding!!.shimmerFrameLayout.startShimmer()
            ismActivityMessagesBinding!!.rlShimmer.visibility = View.VISIBLE
        } else {
            if (ismActivityMessagesBinding!!.rlShimmer.visibility == View.VISIBLE) {
                ismActivityMessagesBinding!!.rlShimmer.visibility = View.GONE
                ismActivityMessagesBinding!!.shimmerFrameLayout.stopShimmer()
            }
        }
    }

    override fun showMessageNotification(
        conversationId: String,
        conversationTitle: String,
        message: String,
        privateOneToOne: Boolean,
        messagePlaceHolderImage: Int?,
        isReactionMessage: Boolean,
        conversationImageUrl: String?,
        senderImageUrl: String?,
        senderName: String
    ) {
        runOnUiThread {
            ismActivityMessagesBinding!!.incMessageNotification.tvConversationTitle.text =
                conversationTitle
            ismActivityMessagesBinding!!.incMessageNotification.tvConversationType.text =
                if (privateOneToOne) getString(R.string.ism_1_1) else getString(R.string.ism_group)
            ismActivityMessagesBinding!!.incMessageNotification.tvMessage.text = message
            if (PlaceholderUtils.isValidImageUrl(conversationImageUrl)) {
                try {
                    Glide.with(this@ConversationMessagesActivity)
                        .load(conversationImageUrl).placeholder(R.drawable.ism_ic_profile)
                        .transform(CircleCrop()).into(
                            ismActivityMessagesBinding!!.incMessageNotification.ivConversationImage
                        )
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }
            } else {
                PlaceholderUtils.setTextRoundDrawable(
                    this@ConversationMessagesActivity,
                    conversationTitle,
                    ismActivityMessagesBinding!!.incMessageNotification.ivConversationImage,
                    16
                )
            }

            if (senderImageUrl != null) {
                if (PlaceholderUtils.isValidImageUrl(senderImageUrl)) {
                    try {
                        Glide.with(this@ConversationMessagesActivity)
                            .load(senderImageUrl).placeholder(R.drawable.ism_ic_profile)
                            .transform(CircleCrop()).into(
                                ismActivityMessagesBinding!!.incMessageNotification.ivSenderImage
                            )
                    } catch (ignore: IllegalArgumentException) {
                    } catch (ignore: NullPointerException) {
                    }
                } else {
                    PlaceholderUtils.setTextRoundDrawable(
                        this@ConversationMessagesActivity,
                        senderName,
                        ismActivityMessagesBinding!!.incMessageNotification.ivSenderImage,
                        5
                    )
                }
                ismActivityMessagesBinding!!.incMessageNotification.ivSenderImage.visibility =
                    View.VISIBLE
            } else {
                ismActivityMessagesBinding!!.incMessageNotification.ivSenderImage.visibility =
                    View.GONE
            }

            if (messagePlaceHolderImage != null) {
                try {
                    Glide.with(this@ConversationMessagesActivity)
                        .load(messagePlaceHolderImage).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(
                            ismActivityMessagesBinding!!.incMessageNotification.ivMessageType
                        )
                } catch (ignore: IllegalArgumentException) {
                } catch (ignore: NullPointerException) {
                }

                if (isReactionMessage) {
                    ismActivityMessagesBinding!!.incMessageNotification.ivMessageType.clearColorFilter()
                } else {
                    ismActivityMessagesBinding!!.incMessageNotification.ivMessageType.setColorFilter(
                        ContextCompat.getColor(
                            this@ConversationMessagesActivity,
                            R.color.ism_last_message_grey
                        ),
                        PorterDuff.Mode.SRC_IN
                    )
                }

                ismActivityMessagesBinding!!.incMessageNotification.ivMessageType.visibility =
                    View.VISIBLE
            } else {
                ismActivityMessagesBinding!!.incMessageNotification.ivMessageType.visibility =
                    View.GONE
            }

            ismActivityMessagesBinding!!.incMessageNotification.root.setOnClickListener { v: View? ->
                val intent = Intent(
                    this@ConversationMessagesActivity,
                    ConversationMessagesActivity::class.java
                )
                intent.putExtra("conversationId", conversationId)
                intent.putExtra("privateOneToOne", privateOneToOne)
                startActivity(intent)
            }
            ismActivityMessagesBinding!!.incMessageNotification.root.visibility =
                View.VISIBLE
            try {
                handler.postDelayed(
                    messageNotificationRunnable,
                    Constants.MESSAGE_NOTIFICATION_VISIBILITY_DURATION_IN_MS.toLong()
                )
            } catch (ignore: Exception) {
            }
        }
    }

    private val messageNotificationRunnable = Runnable {
        try {
            ismActivityMessagesBinding!!.incMessageNotification.root.visibility =
                View.GONE
        } catch (ignore: Exception) {
        }
    }

    override fun updateVisibilityOfObserversIcon() {
//        runOnUiThread {
//            ismActivityMessagesBinding!!.ivObservers.visibility =
//                View.VISIBLE
//        }
    }

    override fun onUserBlocked() {
        IsometrikChatSdk.instance.chatActionsClickListener?.onBlockStatusUpdate(
            true,
            userPersonalUserId!!
        )
        hideProgressDialog()
        onMessagingStatusChanged(true)
    }

    override fun onUserUnBlocked() {
        IsometrikChatSdk.instance.chatActionsClickListener?.onBlockStatusUpdate(
            false,
            userPersonalUserId!!
        )
        hideProgressDialog()
        onMessagingStatusChanged(false)
    }

    private fun hideProgressDialog() {
        if (alertDialog != null && alertDialog!!.isShowing) alertDialog!!.dismiss()
    }

    override fun onConversationClearedSuccessfully() {
        onConversationCleared()
        hideProgressDialog()
    }

    // Check the necessary permissions for recording video
    private fun checkRecordVideoPermissions(): Boolean {
        val requiredPermissions: MutableList<String> = ArrayList()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requiredPermissions.add(Manifest.permission.CAMERA)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requiredPermissions.add(Manifest.permission.RECORD_AUDIO)
        }

        if (!requiredPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                requiredPermissions.toTypedArray<String>(), VIDEO_RECORD_PERMISSIONS_REQUEST_CODE
            )
            return false
        }
        return true
    }

    private fun startRecording() {
        KeyboardUtil.hideKeyboard(this)
        ismActivityMessagesBinding!!.rlBottomLayout.visibility = View.INVISIBLE
        conversationMessagesPresenter!!.startAudioRecording(this)
    }

    fun onRecordVideoRequested() {
        if (checkRecordVideoPermissions()) {
            // When you want to start VideoRecordingActivity
            val videoRecordingIntent = Intent(this, VideoRecordingActivity::class.java)
            startActivityForResult(videoRecordingIntent, VIDEO_PREVIEW_REQUEST_CODE)
        }
    }


    override fun onJoinedAsObserverSuccessfully() {
        runOnUiThread {
//            ismActivityMessagesBinding!!.ivObservers.visibility = View.VISIBLE
            updateShimmerVisibility(false)
        }
    }

    override fun onFailedToJoinAsObserverOrFetchMessagesOrConversationDetails(errorMessage: String) {
        onError(errorMessage)
        onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        if (firstResume) {
            firstResume = false
        } else {
            conversationMessagesPresenter!!.setActiveInConversation(true)
            if (!joiningAsObserver) {
                conversationMessagesPresenter!!.markMessagesAsRead()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        conversationMessagesPresenter!!.setActiveInConversation(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VIDEO_PREVIEW_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val videoUriString = data.getStringExtra("videoUri")
            if (videoUriString != null) {
                handleRecordedVideo(Uri.parse(videoUriString))
            }
        }
    }

    private fun handleRecordedVideo(videoUri: Uri) {
        val videoPaths = ArrayList(listOf(videoUri.path))

        conversationMessagesPresenter!!.shareMessage(
            RemoteMessageTypes.NormalMessage,
            null,
            null,
            CustomMessageTypes.Video,
            CustomMessageTypes.Video.value,
            false,
            true,
            true,
            true,
            null,
            null,
            null,
            MessageTypeUi.VIDEO_MESSAGE_SENT,
            videoPaths,
            true,
            PresignedUrlMediaTypes.Video,
            AttachmentMessageType.Video
        )
    }

    override fun onScrollToParentMessage(messageId: String?) {
        messageId?.let {
            val position = getPositionById(messageId)
            if (position != -1) {
                ismActivityMessagesBinding!!.rvMessages.smoothScrollToPosition(position)
            }
        }
    }

    private fun getPositionById(messageId: String): Int {
        for (i in messages.indices) {
            if (messages[i].messageId == messageId) {
                return i
            }
        }
        return -1
    }

    /**
     * Update the top view when a message changes or a new message arrives.
     */
    fun onMessageUpdated(
        conversationDetailsUtil: ConversationDetailsUtil?,
        messages: List<MessagesModel>
    ) {
        try {
            for ( message in messages){
                log("ChatSDK:", "onMessageUpdated  customMessageType: ${message?.customMessageType?.value}  dynamicCustomType: ${message?.dynamicCustomType}")
            }
        }catch (e : Exception){

        }


        topView?.let { view ->
            topViewHandler?.updateTopView(view, conversationDetailsUtil, messages)
        }
    }

    fun openReactionDialog(messageView: View){
        reactionDialog = FullScreenReactionDialog(messageView)
        reactionDialog?.show(supportFragmentManager, FullScreenReactionDialog.TAG)
    }

    private fun startDetailsApiPolling() {
        /**
         * used for getting user current status(online-offline)
        * */
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.RESUMED) {
                while (true) {
                    try {
                        if (ismActivityMessagesBinding!!.vSelectMultipleMessagesHeader.root.visibility == View.GONE && clickActionsNotBlocked()) {
                            conversationMessagesPresenter.requestConversationDetails()
                        }
                    } catch (e: Exception) {
                    }
                    delay(15_000) // Wait for 15 seconds before the next call
                }
            }
        }
    }

    companion object {
        var conversationId: String? = null
        private const val DOWNLOAD_MEDIA_PERMISSIONS_REQUEST_CODE = 0
        private const val SHARE_LOCATION_PERMISSIONS_REQUEST_CODE = 1
        private const val RECORD_AUDIO_PERMISSIONS_REQUEST_CODE = 2
        private const val CAPTURE_IMAGE_PERMISSIONS_REQUEST_CODE = 3
        private const val SHARE_PHOTOS_PERMISSIONS_REQUEST_CODE = 4
        private const val SHARE_VIDEOS_PERMISSIONS_REQUEST_CODE = 5
        private const val SHARE_FILES_PERMISSIONS_REQUEST_CODE = 6
        private const val SHARE_CONTACT_PERMISSIONS_REQUEST_CODE = 7

        // Request code for permissions
        private const val PERMISSION_REQUEST_CODE = 123
        private const val VIDEO_RECORD_PERMISSIONS_REQUEST_CODE = 9
        private const val VIDEO_PREVIEW_REQUEST_CODE = 8 // New request code for video preview


        fun openBrowser(context: Context, url: String) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(browserIntent)
        }
    }
}
