package io.isometrik.ui.messages.action

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import io.isometrik.chat.R
import io.isometrik.chat.databinding.IsmDialogMessageActionOverlayBinding
import io.isometrik.chat.enums.MessageTypeUi
import io.isometrik.chat.utils.MentionedUserSpan
import io.isometrik.chat.utils.RecyclerItemClickListener
import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.reaction.add.AddReactionAdapter
import io.isometrik.ui.messages.reaction.add.AddReactionContract
import io.isometrik.ui.messages.reaction.add.AddReactionPresenter
import io.isometrik.ui.messages.reaction.add.ReactionModel
import io.isometrik.ui.messages.reaction.util.ReactionRepository

/**
 * iOS-style long-press overlay: reactions + compact actions. Callbacks stay on MessageActionCallback.
 */
class MessageActionOverlayFragment : DialogFragment(), AddReactionContract.View {

  companion object {
    const val TAG = "MessageActionOverlayFragment"
  }

  private var binding: IsmDialogMessageActionOverlayBinding? = null
  private var messagesModel: MessagesModel? = null
  private var messageActionCallback: MessageActionCallback? = null
  private var conversationId: String? = null
  private var position: Int = 0
  private var anchorX: Int = 0
  private var anchorY: Int = 0
  private var anchorWidth: Int = 0
  private var anchorHeight: Int = 0
  private var previewBitmap: Bitmap? = null
  private val addReactionPresenter = AddReactionPresenter()

  fun updateParameters(
    messagesModel: MessagesModel,
    messageActionCallback: MessageActionCallback,
    conversationId: String?,
    position: Int,
    anchorX: Int,
    anchorY: Int,
    anchorWidth: Int,
    anchorHeight: Int,
    previewBitmap: Bitmap?
  ) {
    this.messagesModel = messagesModel
    this.messageActionCallback = messageActionCallback
    this.conversationId = conversationId
    this.position = position
    this.anchorX = anchorX
    this.anchorY = anchorY
    this.anchorWidth = anchorWidth
    this.anchorHeight = anchorHeight
    this.previewBitmap?.recycle()
    this.previewBitmap = previewBitmap
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
    dialog.setCanceledOnTouchOutside(true)
    return dialog
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = IsmDialogMessageActionOverlayBinding.inflate(inflater, container, false)
    return binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val model = messagesModel ?: return
    val callback = messageActionCallback ?: return
    val overlayBinding = binding ?: return

    overlayBinding.flOverlayRoot.setOnClickListener { dismissAllowingStateLoss() }
    overlayBinding.llOverlayContent.setOnClickListener { }

    setupReactions(overlayBinding, model, callback)
    setupPreview(overlayBinding)
    setupActions(overlayBinding, model, callback)
    overlayBinding.llOverlayContent.post { positionContent(overlayBinding) }
  }

  override fun onStart() {
    super.onStart()
    dialog?.window?.apply {
      setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
      setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      setDimAmount(0f)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addReactionPresenter.attachView(this)
  }

  override fun onDestroy() {
    addReactionPresenter.detachView()
    super.onDestroy()
  }

  override fun onDestroyView() {
    previewBitmap?.recycle()
    previewBitmap = null
    binding = null
    super.onDestroyView()
  }

  private fun setupReactions(
    overlayBinding: IsmDialogMessageActionOverlayBinding,
    model: MessagesModel,
    callback: MessageActionCallback
  ) {
    val reactions = ArrayList(ReactionRepository.getReactions())
    overlayBinding.rvOverlayReactions.layoutManager = GridLayoutManager(requireContext(), 5)
    overlayBinding.rvOverlayReactions.adapter = AddReactionAdapter(requireContext(), reactions)
    overlayBinding.rvOverlayReactions.addOnItemTouchListener(
      RecyclerItemClickListener(
        requireContext(),
        overlayBinding.rvOverlayReactions,
        object : RecyclerItemClickListener.OnItemClickListener {
          override fun onItemClick(view: View, index: Int) {
            if (index < 0 || index >= reactions.size) return
            val conversation = conversationId
            val messageId = model.messageId
            if (conversation.isNullOrEmpty() || messageId.isNullOrEmpty()) return
            addReactionPresenter.addReaction(
              conversation,
              messageId,
              reactions[index].reactionType
            )
          }

          override fun onItemLongClick(view: View, position: Int) {}
        })
    )
  }

  private fun setupPreview(overlayBinding: IsmDialogMessageActionOverlayBinding) {
    val bitmap = previewBitmap
    if (bitmap != null && !bitmap.isRecycled) {
      overlayBinding.ivMessagePreview.setImageBitmap(bitmap)
      overlayBinding.ivMessagePreview.visibility = View.VISIBLE
    } else {
      overlayBinding.ivMessagePreview.visibility = View.GONE
    }
  }

  private fun setupActions(
    overlayBinding: IsmDialogMessageActionOverlayBinding,
    model: MessagesModel,
    callback: MessageActionCallback
  ) {
    val isText = model.messageTypeUi == MessageTypeUi.TEXT_MESSAGE_SENT
        || model.messageTypeUi == MessageTypeUi.TEXT_MESSAGE_RECEIVED

    bindRow(
      overlayBinding.rowReply.root,
      getString(R.string.ism_reply),
      R.drawable.ism_ic_forward,
      false
    )
    overlayBinding.rowReply.root.findViewById<ImageView>(R.id.ivAction).rotationY = 180f
    overlayBinding.rowReply.root.setOnClickListener {
      callback.replyMessageRequested(model)
      dismissAllowingStateLoss()
    }

    bindRow(
      overlayBinding.rowForward.root,
      getString(R.string.ism_forward_heading),
      R.drawable.ism_ic_forward,
      false
    )
    overlayBinding.rowForward.root.setOnClickListener {
      callback.forwardMessageRequest(model)
      dismissAllowingStateLoss()
    }

    overlayBinding.rowCopy.root.visibility = if (isText) View.VISIBLE else View.GONE
    bindRow(
      overlayBinding.rowCopy.root,
      getString(R.string.ism_copy_heading),
      R.drawable.ism_ic_copy,
      false
    )
    overlayBinding.rowCopy.root.setOnClickListener {
      val clipboard =
        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      clipboard.setPrimaryClip(ClipData.newPlainText("label", model.textMessage))
      Toast.makeText(requireContext(), getString(R.string.ism_text_copied), Toast.LENGTH_SHORT)
        .show()
      dismissAllowingStateLoss()
    }

    var canEdit = false
    if (isText && model.isSentMessage) {
      val spannable: SpannableString? = model.textMessage
      canEdit = try {
        spannable == null || spannable.getSpans(0, spannable.length, MentionedUserSpan::class.java)
          .isEmpty()
      } catch (_: Exception) {
        true
      }
    }
    overlayBinding.rowEdit.root.visibility = if (canEdit) View.VISIBLE else View.GONE
    bindRow(
      overlayBinding.rowEdit.root,
      getString(R.string.ism_edit_heading),
      R.drawable.ism_ic_edit_title,
      false
    )
    overlayBinding.rowEdit.root.setOnClickListener {
      callback.editMessageRequested(model)
      dismissAllowingStateLoss()
    }

    val showInfo = model.isSentMessage && model.isMessageSentSuccessfully
    overlayBinding.rowInfo.root.visibility = if (showInfo) View.VISIBLE else View.GONE
    bindRow(
      overlayBinding.rowInfo.root,
      getString(R.string.ism_info_heading),
      R.drawable.ism_ic_message_info,
      false
    )
    overlayBinding.rowInfo.root.setOnClickListener {
      callback.fetchMessagesInfoRequest(model)
      dismissAllowingStateLoss()
    }

    val downloadType = downloadTypeFor(model)
    val canDownload = downloadType != null && !model.isDownloaded && !model.isDownloading
    overlayBinding.rowDownload.root.visibility = if (canDownload) View.VISIBLE else View.GONE
    bindRow(
      overlayBinding.rowDownload.root,
      getString(R.string.ism_download_heading),
      R.drawable.ism_ic_download,
      false
    )
    overlayBinding.rowDownload.root.setOnClickListener {
      if (downloadType != null) {
        callback.downloadMedia(model, downloadType, position)
      }
      dismissAllowingStateLoss()
    }

    bindRow(
      overlayBinding.rowSelect.root,
      getString(R.string.ism_select_messages_heading),
      R.drawable.ism_ic_select_multiple_messages,
      false
    )
    overlayBinding.rowSelect.root.setOnClickListener {
      callback.selectMultipleMessagesRequested()
      dismissAllowingStateLoss()
    }

    bindRow(
      overlayBinding.rowDelete.root,
      getString(R.string.ism_delete),
      R.drawable.ism_ic_trash,
      true
    )
    overlayBinding.rowDelete.root.setOnClickListener {
      val messageId = model.messageId ?: return@setOnClickListener
      if (model.isSentMessage) {
        callback.confirmDeleteSentMessage(messageId)
      } else {
        callback.deleteMessageForSelf(messageId, false)
      }
      dismissAllowingStateLoss()
    }

    hideLastVisibleDivider(overlayBinding)
  }

  private fun bindRow(row: View, title: String, icon: Int, destructive: Boolean) {
    val tv = row.findViewById<TextView>(R.id.tvAction)
    val iv = row.findViewById<ImageView>(R.id.ivAction)
    tv.text = title
    iv.setImageResource(icon)
    val color = ContextCompat.getColor(
      requireContext(),
      if (destructive) R.color.ism_leave_red else R.color.ism_text_black
    )
    tv.setTextColor(color)
    ImageViewCompat.setImageTintList(iv, ColorStateList.valueOf(color))
  }

  private fun hideLastVisibleDivider(overlayBinding: IsmDialogMessageActionOverlayBinding) {
    val rows = listOf(
      overlayBinding.rowReply.root,
      overlayBinding.rowForward.root,
      overlayBinding.rowCopy.root,
      overlayBinding.rowEdit.root,
      overlayBinding.rowInfo.root,
      overlayBinding.rowDownload.root,
      overlayBinding.rowSelect.root,
      overlayBinding.rowDelete.root
    )
    val visible = rows.filter { it.visibility == View.VISIBLE }
    visible.forEach { it.findViewById<View>(R.id.vDivider).visibility = View.VISIBLE }
    visible.lastOrNull()?.findViewById<View>(R.id.vDivider)?.visibility = View.GONE
  }

  private fun downloadTypeFor(model: MessagesModel): String? {
    return when (model.messageTypeUi) {
      MessageTypeUi.PHOTO_MESSAGE_SENT, MessageTypeUi.PHOTO_MESSAGE_RECEIVED ->
        getString(R.string.ism_photo)
      MessageTypeUi.WHITEBOARD_MESSAGE_SENT, MessageTypeUi.WHITEBOARD_MESSAGE_RECEIVED ->
        getString(R.string.ism_whiteboard)
      MessageTypeUi.VIDEO_MESSAGE_SENT, MessageTypeUi.VIDEO_MESSAGE_RECEIVED ->
        getString(R.string.ism_video)
      MessageTypeUi.AUDIO_MESSAGE_SENT, MessageTypeUi.AUDIO_MESSAGE_RECEIVED ->
        getString(R.string.ism_audio_recording)
      MessageTypeUi.FILE_MESSAGE_SENT, MessageTypeUi.FILE_MESSAGE_RECEIVED ->
        getString(R.string.ism_file)
      else -> null
    }
  }

  private fun positionContent(overlayBinding: IsmDialogMessageActionOverlayBinding) {
    val content = overlayBinding.llOverlayContent
    val pad = resources.getDimensionPixelSize(R.dimen.ism_dp_16)
    val screenW = resources.displayMetrics.widthPixels
    val screenH = resources.displayMetrics.heightPixels
    val contentW = content.width
    val contentH = content.height
    val sent = messagesModel?.isSentMessage == true

    var x = if (sent) {
      anchorX + anchorWidth - contentW
    } else {
      anchorX
    }
    x = x.coerceIn(pad, (screenW - contentW - pad).coerceAtLeast(pad))

    val previewVisible = overlayBinding.ivMessagePreview.visibility == View.VISIBLE
    val reactionsH = overlayBinding.rvOverlayReactions.height
    val gap = resources.getDimensionPixelSize(R.dimen.ism_dp_8)
    var y = if (previewVisible) {
      anchorY - reactionsH - gap
    } else {
      anchorY + anchorHeight + gap
    }
    if (y < pad) y = pad
    if (y + contentH > screenH - pad) {
      y = (screenH - contentH - pad).coerceAtLeast(pad)
    }

    val rootLoc = IntArray(2)
    overlayBinding.flOverlayRoot.getLocationOnScreen(rootLoc)
    content.x = (x - rootLoc[0]).toFloat()
    content.y = (y - rootLoc[1]).toFloat()
  }

  override fun onReactionAddedSuccessfully(messageId: String, reactionModel: ReactionModel) {
    messageActionCallback?.updateMessageReaction(messageId, reactionModel, true)
    dismissAllowingStateLoss()
  }

  override fun onReactionRemovedSuccessfully(messageId: String, reactionModel: ReactionModel) {
    messageActionCallback?.updateMessageReaction(messageId, reactionModel, false)
    dismissAllowingStateLoss()
  }

  override fun onError(errorMessage: String?) {
    val ctx = context ?: return
    Toast.makeText(
      ctx,
      errorMessage ?: getString(R.string.ism_error),
      Toast.LENGTH_SHORT
    ).show()
  }
}
