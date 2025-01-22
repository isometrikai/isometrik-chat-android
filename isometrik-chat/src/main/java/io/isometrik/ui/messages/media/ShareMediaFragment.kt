package io.isometrik.ui.messages.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.isometrik.chat.databinding.IsmBottomsheetAttachmentsBinding
import io.isometrik.chat.enums.MessageTypeUi
import io.isometrik.ui.messages.chat.common.AttachmentsConfig

/**
 * The fragment with option to share various kind of media as message in a conversation.Supported
 * message types are image/video/file/contact/location/whiteboard/sticker/gif
 */
class ShareMediaFragment
/**
 * Instantiates a new share media fragment.
 */
    : BottomSheetDialogFragment() {
    private var ismBottomsheetAttachmentsBinding: IsmBottomsheetAttachmentsBinding? = null
    private var mediaTypeToBeSharedCallback: MediaTypeToBeSharedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ismBottomsheetAttachmentsBinding =
            IsmBottomsheetAttachmentsBinding.inflate(inflater, container, false)

        ismBottomsheetAttachmentsBinding!!.rlCamera.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.CAMERA_PHOTO_SENT)
        }
        ismBottomsheetAttachmentsBinding!!.rlVideoRecord.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.RECORD_VIDEO_SENT)
        }
        ismBottomsheetAttachmentsBinding!!.rlPhotos.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.PHOTO_MESSAGE_SENT)
        }
        ismBottomsheetAttachmentsBinding!!.rlVideos.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.VIDEO_MESSAGE_SENT)
        }
        ismBottomsheetAttachmentsBinding!!.rlFiles.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.FILE_MESSAGE_SENT)
        }
        ismBottomsheetAttachmentsBinding!!.rlSticker.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.STICKER_MESSAGE_SENT)
        }
        ismBottomsheetAttachmentsBinding!!.rlGifs.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.GIF_MESSAGE_SENT)
        }
        ismBottomsheetAttachmentsBinding!!.rlWhiteboard.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.WHITEBOARD_MESSAGE_SENT)
        }
        ismBottomsheetAttachmentsBinding!!.rlLocation.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.LOCATION_MESSAGE_SENT)
        }
        ismBottomsheetAttachmentsBinding!!.rlContact.setOnClickListener { v: View? ->
            dismissDialog()
            mediaTypeToBeSharedCallback!!.onMediaTypeToBeSharedSelected(MessageTypeUi.CONTACT_MESSAGE_SENT)
        }

        if (AttachmentsConfig.hideCameraOption) {
            ismBottomsheetAttachmentsBinding!!.rlCamera.visibility = View.GONE
        }
        if (AttachmentsConfig.hideRecordVideoOption) {
            ismBottomsheetAttachmentsBinding!!.rlVideoRecord.visibility = View.GONE
        }
        if (AttachmentsConfig.hidePhotosOption) {
            ismBottomsheetAttachmentsBinding!!.rlPhotos.visibility = View.GONE
        }
        if (AttachmentsConfig.hideVideosOption) {
            ismBottomsheetAttachmentsBinding!!.rlVideos.visibility = View.GONE
        }
        if (AttachmentsConfig.hideFilesOption) {
            ismBottomsheetAttachmentsBinding!!.rlFiles.visibility = View.GONE
        }
        if (AttachmentsConfig.hideLocationOption) {
            ismBottomsheetAttachmentsBinding!!.rlLocation.visibility = View.GONE
        }
        if (AttachmentsConfig.hideContactOption) {
            ismBottomsheetAttachmentsBinding!!.rlContact.visibility = View.GONE
        }
        if (AttachmentsConfig.hideStickerOption) {
            ismBottomsheetAttachmentsBinding!!.rlSticker.visibility = View.GONE
        }
        if (AttachmentsConfig.hideGIFOption) {
            ismBottomsheetAttachmentsBinding!!.rlGifs.visibility = View.GONE
        }

        ismBottomsheetAttachmentsBinding!!.ivClose.setOnClickListener { v: View? -> dismissDialog() }

        return ismBottomsheetAttachmentsBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ismBottomsheetAttachmentsBinding = null
    }

    /**
     * Update parameters.
     *
     * @param mediaTypeToBeSharedCallback the media type to be shared callback
     */
    fun updateParameters(mediaTypeToBeSharedCallback: MediaTypeToBeSharedCallback?) {
        this.mediaTypeToBeSharedCallback = mediaTypeToBeSharedCallback
    }

    private fun dismissDialog() {
        try {
            dismiss()
        } catch (ignore: Exception) {
        }
    }

    companion object {
        /**
         * The constant TAG.
         */
        const val TAG: String = "ShareMediaFragment"
    }
}
