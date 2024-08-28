package io.isometrik.ui.messages.action.edit;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBottomsheetEditBinding;
import io.isometrik.ui.messages.action.MessageActionCallback;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.chat.utils.enums.MessageTypesForUI;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.TimeUtil;

/**
 * The fragment to allow editing a text message.
 */
public class EditMessageFragment extends BottomSheetDialogFragment {

  /**
   * The constant TAG.
   */
  public static final String TAG = "EditMessageFragment";
  private String originalMessage, originalMessageSenderName, messageId;
  private long originalMessageSentAt;
  private Integer originalMessagePlaceHolderIcon;
  private Activity activity;
  private MessageActionCallback editMessageCallback;
  private IsmBottomsheetEditBinding ismBottomsheetEditBinding;

  /**
   * Instantiates a new edit message fragment.
   */
  public EditMessageFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    ismBottomsheetEditBinding = IsmBottomsheetEditBinding.inflate(inflater, container, false);

    ismBottomsheetEditBinding.tvMessage.setText(originalMessage);
    ismBottomsheetEditBinding.tvSenderName.setText(originalMessageSenderName);
    ismBottomsheetEditBinding.tvMessageTime.setText(
        TimeUtil.formatTimestampToBothDateAndTime(originalMessageSentAt));

    if (originalMessagePlaceHolderIcon != null) {

      try {
        GlideApp.with(activity)
            .load(originalMessagePlaceHolderIcon)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ismBottomsheetEditBinding.ivMessageImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
      ismBottomsheetEditBinding.ivMessageImage.setVisibility(View.VISIBLE);
    } else {
      ismBottomsheetEditBinding.ivMessageImage.setVisibility(View.GONE);
    }

    ismBottomsheetEditBinding.ivSendMessage.setOnClickListener(v -> {
      if (ismBottomsheetEditBinding.etEditMessage.getText() != null
          && ismBottomsheetEditBinding.etEditMessage.getText().length() > 0) {
        editMessageCallback.updateMessage(messageId,
            ismBottomsheetEditBinding.etEditMessage.getText().toString(), originalMessage);
        close();
      } else {
        Toast.makeText(activity, getString(R.string.ism_type_edit_message), Toast.LENGTH_SHORT)
            .show();
      }
    });

    return ismBottomsheetEditBinding.getRoot();
  }

  @Override
  public void onResume() {
    super.onResume();
    try {
      //https://stackoverflow.com/questions/13303469/edittext-settext-not-working-with-fragment
      ismBottomsheetEditBinding.etEditMessage.setText(originalMessage);
    } catch (Exception ignore) {
    }
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
   * @param editMessageCallback the edit message callback
   */
  public void updateParameters(MessagesModel messagesModel,
      MessageActionCallback editMessageCallback) {

    if (messagesModel.getCustomMessageType() != MessageTypesForUI.TextSent) {
      close();
    } else {
      this.editMessageCallback = editMessageCallback;
      messageId = messagesModel.getMessageId();

      originalMessage = messagesModel.getTextMessage().toString();
      originalMessageSenderName = messagesModel.getSenderName();
      originalMessageSentAt = messagesModel.getSentAt();
      originalMessagePlaceHolderIcon = R.drawable.ism_ic_text;
    }
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
