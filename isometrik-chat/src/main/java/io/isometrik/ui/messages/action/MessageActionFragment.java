package io.isometrik.ui.messages.action;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBottomsheetMessageActionsBinding;
import io.isometrik.chat.utils.MentionedUserSpan;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.chat.utils.enums.MessageTypesForUI;

import static io.isometrik.ui.messages.chat.utils.enums.MessageTypesForUI.TextReceived;
import static io.isometrik.ui.messages.chat.utils.enums.MessageTypesForUI.TextSent;

/**
 * The fragment for showing actionable items on click of a message for- reply/edit/delete for
 * self/everyone, fetch delivery info, add reaction, download media and forward message.
 */
public class MessageActionFragment extends BottomSheetDialogFragment {

  /**
   * The constant TAG.
   */
  public static final String TAG = "MessageActionFragment";
  private MessagesModel messagesModel;
  private MessageActionCallback messageActionCallback;
  private Activity activity;
  private int position;

  /**
   * Instantiates a new message action fragment.
   */
  public MessageActionFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    IsmBottomsheetMessageActionsBinding ismBottomsheetMessageActionsBinding =
        IsmBottomsheetMessageActionsBinding.inflate(inflater, container, false);

    if (messagesModel.getCustomMessageType().equals(MessageTypesForUI.TextSent)
        || messagesModel.getCustomMessageType().equals(MessageTypesForUI.TextReceived)) {
      ismBottomsheetMessageActionsBinding.rlCopy.setVisibility(View.VISIBLE);
      if (messagesModel.isSentMessage()) {

        SpannableString spannableString = messagesModel.getTextMessage();

        boolean hasMentionedUsers;
        try {
          hasMentionedUsers = spannableString.getSpans(0, spannableString.length() - 1,
              MentionedUserSpan.class).length > 0;
        } catch (Exception ignore) {
          hasMentionedUsers = false;
        }
        ismBottomsheetMessageActionsBinding.rlEdit.setVisibility(
            hasMentionedUsers ? View.GONE : View.VISIBLE);
      } else {
        ismBottomsheetMessageActionsBinding.rlEdit.setVisibility(View.GONE);
      }
    } else {
      ismBottomsheetMessageActionsBinding.rlCopy.setVisibility(View.GONE);
      ismBottomsheetMessageActionsBinding.rlEdit.setVisibility(View.GONE);
    }

    if (messagesModel.isSentMessage()) {
      if (messagesModel.isMessageSentSuccessfully()) {
        ismBottomsheetMessageActionsBinding.rlInfo.setVisibility(View.VISIBLE);
      } else {
        ismBottomsheetMessageActionsBinding.rlInfo.setVisibility(View.GONE);
      }

      ismBottomsheetMessageActionsBinding.rlDeleteForAll.setVisibility(View.VISIBLE);
      ismBottomsheetMessageActionsBinding.rlDeleteForMe.setVisibility(View.VISIBLE);
    } else {
      ismBottomsheetMessageActionsBinding.rlInfo.setVisibility(View.GONE);
      ismBottomsheetMessageActionsBinding.rlDeleteForAll.setVisibility(View.GONE);
      ismBottomsheetMessageActionsBinding.rlDeleteForMe.setVisibility(View.GONE);
    }

    ismBottomsheetMessageActionsBinding.rlReaction.setOnClickListener(v -> {

      messageActionCallback.addReactionForMessage(messagesModel.getMessageId());
      close();
    });

    ismBottomsheetMessageActionsBinding.rlQuote.setOnClickListener(v -> {

      messageActionCallback.replyMessageRequested(messagesModel);
      close();
    });

    ismBottomsheetMessageActionsBinding.rlDeleteForMe.setOnClickListener(v -> {

      messageActionCallback.deleteMessageForSelf(messagesModel.getMessageId(), false);
      close();
    });

    ismBottomsheetMessageActionsBinding.rlDeleteForAll.setOnClickListener(v -> {

      messageActionCallback.deleteMessageForEveryone(messagesModel.getMessageId(), false);
      close();
    });

    ismBottomsheetMessageActionsBinding.rlInfo.setOnClickListener(v -> {

      messageActionCallback.fetchMessagesInfoRequest(messagesModel);
      close();
    });

    ismBottomsheetMessageActionsBinding.rlForward.setOnClickListener(v -> {

      messageActionCallback.forwardMessageRequest(messagesModel);
      close();
    });

    ismBottomsheetMessageActionsBinding.rlSelectMultipleMessages.setOnClickListener(v -> {

      messageActionCallback.selectMultipleMessagesRequested();
      close();
    });
    ismBottomsheetMessageActionsBinding.rlEdit.setOnClickListener(v -> {
      messageActionCallback.editMessageRequested(messagesModel);
      close();
    });
    ismBottomsheetMessageActionsBinding.rlCopy.setOnClickListener(v -> {
      ClipboardManager clipboard =
          (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
      ClipData clip = ClipData.newPlainText("label", messagesModel.getTextMessage());
      clipboard.setPrimaryClip(clip);
      Toast.makeText(activity, getString(R.string.ism_text_copied), Toast.LENGTH_SHORT).show();
      close();
    });

    boolean canBeDownloaded = false;
    String downloadMediaType = null;
    switch (messagesModel.getCustomMessageType()) {

      case PhotoSent:
      case PhotoReceived: {
        canBeDownloaded = true;
        downloadMediaType = getString(R.string.ism_photo);
        break;
      }

      case WhiteboardSent:
      case WhiteboardReceived: {
        canBeDownloaded = true;
        downloadMediaType = getString(R.string.ism_whiteboard);
        break;
      }

      case VideoSent:
      case VideoReceived: {
        canBeDownloaded = true;
        downloadMediaType = getString(R.string.ism_video);
        break;
      }
      case AudioSent:
      case AudioReceived: {
        canBeDownloaded = true;
        downloadMediaType = getString(R.string.ism_audio_recording);
        break;
      }

      case FileSent:
      case FileReceived: {
        canBeDownloaded = true;
        downloadMediaType = getString(R.string.ism_file);
        break;
      }
    }

    if (canBeDownloaded) {
      if (messagesModel.isDownloaded() || messagesModel.isDownloading()) {
        ismBottomsheetMessageActionsBinding.rlDownload.setVisibility(View.GONE);
      } else {
        ismBottomsheetMessageActionsBinding.rlDownload.setVisibility(View.VISIBLE);
        String finalDownloadMediaType = downloadMediaType;
        ismBottomsheetMessageActionsBinding.rlDownload.setOnClickListener(v -> {
          messageActionCallback.downloadMedia(messagesModel, finalDownloadMediaType, position);
          close();
        });
      }
    } else {
      ismBottomsheetMessageActionsBinding.rlDownload.setVisibility(View.GONE);
    }

    return ismBottomsheetMessageActionsBinding.getRoot();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    activity = null;
  }

  /**
   * Update parameters.
   *
   * @param messagesModel the messages model
   * @param messageActionCallback the message action callback
   * @param position the position
   */
  public void updateParameters(MessagesModel messagesModel,
      MessageActionCallback messageActionCallback, int position) {
    this.messagesModel = messagesModel;
    this.messageActionCallback = messageActionCallback;
    this.position = position;
  }

  /**
   * Close.
   */
  public void close() {
    try {
      dismiss();
    } catch (Exception ignore) {
    }
  }
}
