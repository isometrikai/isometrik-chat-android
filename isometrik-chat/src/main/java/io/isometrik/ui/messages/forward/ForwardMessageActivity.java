package io.isometrik.ui.messages.forward;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityForwardMessageBinding;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.forward.conversations.ForwardInConversationFragment;
import io.isometrik.ui.messages.forward.people.ForwardToPeopleFragment;
import io.isometrik.ui.messages.preview.PreviewMessageUtil;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * The activity containing fragments for forward message to people and forward in conversations.Code
 * to forward a message to/in users/conversations respectively.
 */
public class ForwardMessageActivity extends FragmentActivity
    implements ForwardMessageContract.View {

  private ForwardMessageContract.Presenter forwardMessagePresenter;
  private MessagesModel messagesModel;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private int position;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IsmActivityForwardMessageBinding ismActivityForwardMessageBinding =
        IsmActivityForwardMessageBinding.inflate(getLayoutInflater());
    View view = ismActivityForwardMessageBinding.getRoot();
    setContentView(view);
    alertProgress = new AlertProgress();
    forwardMessagePresenter = new ForwardMessagePresenter(this);

    messagesModel = IsometrikChatSdk.getInstance()
        .getIsometrik()
        .getGson()
        .fromJson(getIntent().getStringExtra("message"), MessagesModel.class);

    int cornerRadius = (int) (13 * getResources().getDisplayMetrics().density);

    Integer attachmentPlaceholderIcon = null;
    switch (messagesModel.getMessageTypeUi()) {

      case TEXT_MESSAGE_RECEIVED:
      case TEXT_MESSAGE_SENT: {
        //TextMessage
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvMediaDescription.setText(messagesModel.getTextMessage());
        attachmentPlaceholderIcon = R.drawable.ism_ic_text;
        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.GONE);
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_text));
        break;
      }

      case PHOTO_MESSAGE_RECEIVED:
      case PHOTO_MESSAGE_SENT: {
        //Image
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.GONE);
        attachmentPlaceholderIcon = R.drawable.ism_ic_picture;
        try {
          Glide.with(this)
              .load(messagesModel.getPhotoMainUrl())
              .thumbnail(Glide.with(this).load(messagesModel.getPhotoThumbnailUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(cornerRadius, cornerRadius, cornerRadius,
                      cornerRadius))
              .into(ismActivityForwardMessageBinding.ivAttachment);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvAttachmentSize.setText(messagesModel.getMediaSizeInMB());
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_photo));
        break;
      }
      case VIDEO_MESSAGE_RECEIVED:
      case VIDEO_MESSAGE_SENT: {
        //Video
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.GONE);
        attachmentPlaceholderIcon = R.drawable.ism_ic_video;
        try {
          Glide.with(this)
              .load(messagesModel.getVideoMainUrl())
              .thumbnail(Glide.with(this).load(messagesModel.getVideoThumbnailUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(cornerRadius, cornerRadius, cornerRadius,
                      cornerRadius))
              .into(ismActivityForwardMessageBinding.ivAttachment);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvAttachmentSize.setText(messagesModel.getMediaSizeInMB());
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_video));
        ismActivityForwardMessageBinding.ivPlayVideo.setVisibility(View.VISIBLE);
        break;
      }
      case AUDIO_MESSAGE_RECEIVED:
      case AUDIO_MESSAGE_SENT: {
        //Audio
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvMediaDescription.setText(messagesModel.getAudioName());
        attachmentPlaceholderIcon = R.drawable.ism_ic_mic;
        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvAttachmentSize.setText(messagesModel.getMediaSizeInMB());
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_audio_recording));

        break;
      }
      case FILE_MESSAGE_RECEIVED:
      case FILE_MESSAGE_SENT: {
        //File
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvMediaDescription.setText(messagesModel.getFileName());
        attachmentPlaceholderIcon = R.drawable.ism_ic_file;
        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvAttachmentSize.setText(messagesModel.getMediaSizeInMB());
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_file));

        break;
      }
      case LOCATION_MESSAGE_RECEIVED:
      case LOCATION_MESSAGE_SENT: {
        //Location
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvMediaDescription.setText(
            String.format("%s\n%s", messagesModel.getLocationName(), messagesModel.getLocationDescription()));
        attachmentPlaceholderIcon = R.drawable.ism_ic_location;
        try {
          Glide.with(this)
              .load(Constants.LOCATION_PLACEHOLDER_IMAGE_URL)
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(cornerRadius, cornerRadius, cornerRadius,
                      cornerRadius))
              .into(ismActivityForwardMessageBinding.ivAttachment);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.GONE);
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_location));

        break;
      }
      case CONTACT_MESSAGE_RECEIVED:
      case CONTACT_MESSAGE_SENT: {
        //Contact
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvMediaDescription.setText(
            String.format("%s\n%s", messagesModel.getContactName(), messagesModel.getContactIdentifier()));
        attachmentPlaceholderIcon = R.drawable.ism_ic_contact;
        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.GONE);
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_contact));

        break;
      }
      case WHITEBOARD_MESSAGE_RECEIVED:
      case WHITEBOARD_MESSAGE_SENT: {
        //Whiteboard
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.GONE);
        attachmentPlaceholderIcon = R.drawable.ism_ic_whiteboard;
        try {
          Glide.with(this)
              .load(messagesModel.getWhiteboardMainUrl())
              .thumbnail(Glide.with(this).load(messagesModel.getWhiteboardThumbnailUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(cornerRadius, cornerRadius, cornerRadius,
                      cornerRadius))
              .into(ismActivityForwardMessageBinding.ivAttachment);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.VISIBLE);
        ismActivityForwardMessageBinding.tvAttachmentSize.setText(messagesModel.getMediaSizeInMB());
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_whiteboard));

        break;
      }
      case GIF_MESSAGE_RECEIVED:
      case GIF_MESSAGE_SENT: {
        //Gif
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.GONE);
        attachmentPlaceholderIcon = R.drawable.ism_ic_gif;
        try {
          Glide.with(this)
              .load(messagesModel.getGifMainUrl())
              .thumbnail(Glide.with(this).load(messagesModel.getGifStillUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(cornerRadius, cornerRadius, cornerRadius,
                      cornerRadius))
              .into(ismActivityForwardMessageBinding.ivAttachment);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.GONE);
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_gif));

        break;
      }
      case STICKER_MESSAGE_RECEIVED:
      case STICKER_MESSAGE_SENT: {
        //Sticker
        ismActivityForwardMessageBinding.tvMediaDescription.setVisibility(View.GONE);
        attachmentPlaceholderIcon = R.drawable.ism_ic_sticker;
        try {
          Glide.with(this)
              .load(messagesModel.getStickerMainUrl())
              .thumbnail(Glide.with(this).load(messagesModel.getStickerStillUrl()))
              .placeholder(R.drawable.ism_avatar_group_large)
              .transform(new CenterCrop(),
                  new GranularRoundedCorners(cornerRadius, cornerRadius, cornerRadius,
                      cornerRadius))
              .into(ismActivityForwardMessageBinding.ivAttachment);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }

        ismActivityForwardMessageBinding.tvAttachmentSize.setVisibility(View.GONE);
        ismActivityForwardMessageBinding.tvAttachmentType.setText(getString(R.string.ism_sticker));

        break;
      }
    }
    if (attachmentPlaceholderIcon != null) {
      try {
        Glide.with(this)
            .load(attachmentPlaceholderIcon)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ismActivityForwardMessageBinding.ivAttachmentPh);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
    }
    final ForwardMessageFragmentAdapter forwardMessageFragmentAdapter = new ForwardMessageFragmentAdapter(ForwardMessageActivity.this);
    ismActivityForwardMessageBinding.vpConversationOrPeople.setAdapter(forwardMessageFragmentAdapter);
    ismActivityForwardMessageBinding.vpConversationOrPeople.setOffscreenPageLimit(1);
    new TabLayoutMediator(ismActivityForwardMessageBinding.tabLayout,
        ismActivityForwardMessageBinding.vpConversationOrPeople, (tab, position) -> {
      if (position == 0) {
        tab.setText(getString(R.string.ism_conversations)).setIcon(R.drawable.ism_ic_forward_conversation);
      } else {
        tab.setText(getString(R.string.ism_people)).setIcon(R.drawable.ism_ic_forward_user);
      }
    }).attach();
    ismActivityForwardMessageBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        position=tab.getPosition();
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
        position=tab.getPosition();
      }
    });
    ismActivityForwardMessageBinding.btSend.setOnClickListener(v -> {

      if (position == 0) {

        ForwardInConversationFragment forwardInConversationFragment =
            (ForwardInConversationFragment) getSupportFragmentManager().findFragmentByTag(
                "f" + ismActivityForwardMessageBinding.vpConversationOrPeople.getCurrentItem());
        if (forwardInConversationFragment == null) {
          Toast.makeText(this, R.string.ism_forwarding_message_in_conversations_failed,
              Toast.LENGTH_SHORT).show();
        } else {
          ArrayList<String> conversationsIds =
              forwardInConversationFragment.fetchSelectedConversations();

          if (conversationsIds.isEmpty()) {
            Toast.makeText(this, R.string.ism_minimum_one_conversation_forward, Toast.LENGTH_SHORT)
                .show();
          } else {
            new AlertDialog.Builder(this).setTitle(getString(R.string.ism_forward_description)).setMessage(
                getString(R.string.ism_forward_message_in_conversations_confirmation,
                    conversationsIds.size()))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                  dialog.cancel();
                  showProgressDialog(getString(R.string.ism_forwarding_message_in_conversations));
                  forwardMessagePresenter.forwardMessage(messagesModel, null, conversationsIds,
                      (ismActivityForwardMessageBinding.etOptionalMessage.getText() == null) ? ""
                          : ismActivityForwardMessageBinding.etOptionalMessage.getText()
                              .toString());
                })
                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                .create()
                .show();
          }
        }
      } else {
        ForwardToPeopleFragment forwardToPeopleFragment =
            (ForwardToPeopleFragment) getSupportFragmentManager().findFragmentByTag(
                "f" + ismActivityForwardMessageBinding.vpConversationOrPeople.getCurrentItem());
        if (forwardToPeopleFragment == null) {
          Toast.makeText(this, R.string.ism_forwarding_message_to_people_failed, Toast.LENGTH_SHORT)
              .show();
        } else {
          ArrayList<String> userIds = forwardToPeopleFragment.fetchSelectedPeople();

          if (userIds.isEmpty()) {
            Toast.makeText(this, R.string.ism_minimum_one_person_forward, Toast.LENGTH_SHORT)
                .show();
          } else {
            new AlertDialog.Builder(this).setTitle(getString(R.string.ism_forward_description)).setMessage(
                getString(R.string.ism_forward_message_to_people_confirmation, userIds.size()))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                  dialog.cancel();
                  showProgressDialog(getString(R.string.ism_forwarding_message_to_people));

                  forwardMessagePresenter.forwardMessage(messagesModel, userIds, null,
                      (ismActivityForwardMessageBinding.etOptionalMessage.getText() == null) ? ""
                          : ismActivityForwardMessageBinding.etOptionalMessage.getText()
                              .toString());
                })
                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                .create()
                .show();
          }
        }
      }
    });

    ismActivityForwardMessageBinding.rlAttachment.setOnClickListener(
        v -> PreviewMessageUtil.previewMessage(ForwardMessageActivity.this, messagesModel, messagesModel.isDownloaded()));

    ismActivityForwardMessageBinding.ibBack.setOnClickListener(v -> onBackPressed());
  }

  @Override
  public void onMessageForwardedSuccessfully() {
    hideProgressDialog();
    onBackPressed();
  }

  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(ForwardMessageActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(ForwardMessageActivity.this, getString(R.string.ism_error),
            Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }
}