package io.isometrik.ui.messages.action.replies;

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
import io.isometrik.chat.databinding.IsmBottomsheetReplyBinding;
import io.isometrik.ui.messages.action.MessageActionCallback;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.chat.utils.messageutils.ReplyMessageUtil;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.TimeUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The fragment to allow replying to a text message.
 */
public class SendMessageReplyFragment extends BottomSheetDialogFragment {

  /**
   * The constant TAG.
   */
  public static final String TAG = "SendMessageReplyFragment";
  private String parentMessage, parentMessageSenderName, messageId;
  private long parentMessageSentAt;
  private Integer parentMessagePlaceHolderIcon;
  private Activity activity;
  private JSONObject replyMessage;
  private MessageActionCallback replyMessageCallback;
  private IsmBottomsheetReplyBinding ismBottomsheetReplyBinding;

  /**
   * Instantiates a new send message reply fragment.
   */
  public SendMessageReplyFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    ismBottomsheetReplyBinding = IsmBottomsheetReplyBinding.inflate(inflater, container, false);

    ismBottomsheetReplyBinding.tvMessage.setText(parentMessage);
    ismBottomsheetReplyBinding.tvSenderName.setText(parentMessageSenderName);
    ismBottomsheetReplyBinding.tvMessageTime.setText(
        TimeUtil.formatTimestampToBothDateAndTime(parentMessageSentAt));

    if (parentMessagePlaceHolderIcon != null) {

      try {
        Glide.with(activity)
            .load(parentMessagePlaceHolderIcon)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ismBottomsheetReplyBinding.ivMessageImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
      ismBottomsheetReplyBinding.ivMessageImage.setVisibility(View.VISIBLE);
    } else {
      ismBottomsheetReplyBinding.ivMessageImage.setVisibility(View.GONE);
    }

    ismBottomsheetReplyBinding.ivSendMessage.setOnClickListener(v -> {
      if (ismBottomsheetReplyBinding.etSendReply.getText() != null
          && ismBottomsheetReplyBinding.etSendReply.getText().length() > 0) {
        replyMessageCallback.sendReplyMessage(messageId,
            ismBottomsheetReplyBinding.etSendReply.getText().toString(), replyMessage);
        close();
      } else {
        Toast.makeText(activity, getString(R.string.ism_type_reply), Toast.LENGTH_SHORT).show();
      }
    });

    return ismBottomsheetReplyBinding.getRoot();
  }

  @Override
  public void onResume() {
    super.onResume();
    try {
      //https://stackoverflow.com/questions/13303469/edittext-settext-not-working-with-fragment
      ismBottomsheetReplyBinding.etSendReply.setText(null);
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
   * @param replyMessageCallback the reply message callback
   */
  public void updateParameters(MessagesModel messagesModel,
      MessageActionCallback replyMessageCallback) {

    replyMessage = ReplyMessageUtil.prepareReplyMessageMetadata(messagesModel);

    if (replyMessage == null) {
      close();
    } else {
      this.replyMessageCallback = replyMessageCallback;
      messageId = messagesModel.getMessageId();
      try {

        JSONObject replyMessageDetails = replyMessage.getJSONObject("replyMessage");
        parentMessage = replyMessageDetails.getString("parentMessageBody");
        parentMessageSenderName = replyMessageDetails.getString("parentMessageUserName");
        parentMessageSentAt = replyMessageDetails.getLong("parentMessageSentAt");
        parentMessagePlaceHolderIcon =
            replyMessageDetails.getInt("originalMessagePlaceHolderImage");
      } catch (JSONException ignore) {

      }
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
