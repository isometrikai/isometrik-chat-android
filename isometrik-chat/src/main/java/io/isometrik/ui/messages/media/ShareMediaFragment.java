package io.isometrik.ui.messages.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.chat.databinding.IsmBottomsheetAttachmentsBinding;
import io.isometrik.chat.utils.enums.MessageTypesForUI;

/**
 * The fragment with option to share various kind of media as message in a conversation.Supported
 * message types are image/video/file/contact/location/whiteboard/sticker/gif
 */
public class ShareMediaFragment extends BottomSheetDialogFragment {
  /**
   * The constant TAG.
   */
  public static final String TAG = "ShareMediaFragment";

  /**
   * Instantiates a new share media fragment.
   */
  public ShareMediaFragment() {
    // Required empty public constructor
  }

  private IsmBottomsheetAttachmentsBinding ismBottomsheetAttachmentsBinding;
  private MediaTypeToBeSharedCallback mediaTypeToBeSharedCallback;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    ismBottomsheetAttachmentsBinding =
            IsmBottomsheetAttachmentsBinding.inflate(inflater, container, false);

    ismBottomsheetAttachmentsBinding.rlCamera.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.CameraPhoto);
    });
    ismBottomsheetAttachmentsBinding.rlVideoRecord.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.RecordVideo);
    });
    ismBottomsheetAttachmentsBinding.rlPhotos.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.PhotoSent);
    });
    ismBottomsheetAttachmentsBinding.rlVideos.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.VideoSent);
    });
    ismBottomsheetAttachmentsBinding.rlFiles.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.FileSent);
    });
    ismBottomsheetAttachmentsBinding.rlSticker.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.StickerSent);
    });
    ismBottomsheetAttachmentsBinding.rlGifs.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.GifSent);
    });
    ismBottomsheetAttachmentsBinding.rlWhiteboard.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.WhiteboardSent);
    });
    ismBottomsheetAttachmentsBinding.rlLocation.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.LocationSent);
    });
    ismBottomsheetAttachmentsBinding.rlContact.setOnClickListener(v -> {
      dismissDialog();
      mediaTypeToBeSharedCallback.onMediaTypeToBeSharedSelected(MessageTypesForUI.ContactSent);
    });

    ismBottomsheetAttachmentsBinding.ivClose.setOnClickListener(v -> dismissDialog());

    return ismBottomsheetAttachmentsBinding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ismBottomsheetAttachmentsBinding = null;
  }

  /**
   * Update parameters.
   *
   * @param mediaTypeToBeSharedCallback the media type to be shared callback
   */
  public void updateParameters(MediaTypeToBeSharedCallback mediaTypeToBeSharedCallback) {

    this.mediaTypeToBeSharedCallback = mediaTypeToBeSharedCallback;
  }

  private void dismissDialog() {
    try {
      dismiss();
    } catch (Exception ignore) {
    }
  }
}
