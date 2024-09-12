package io.isometrik.ui.messages.deliverystatus;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.google.android.material.tabs.TabLayoutMediator;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityMessageDeliveryStatusBinding;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.chat.utils.Constants;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;

/**
 * The activity containing fragments for message delivery or read complete and pending status of a message.
 */
public class MessageDeliveryStatusActivity extends FragmentActivity
 {
private   IsmActivityMessageDeliveryStatusBinding ismActivityMessageDeliveryStatusBinding;
   @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
      ismActivityMessageDeliveryStatusBinding =
         IsmActivityMessageDeliveryStatusBinding.inflate(getLayoutInflater());
     View view = ismActivityMessageDeliveryStatusBinding.getRoot();
     setContentView(view);

     MessagesModel messagesModel = IsometrikUiSdk.getInstance()
         .getIsometrik()
         .getGson()
         .fromJson(getIntent().getStringExtra("message"), MessagesModel.class);

     boolean isQuotedMessage = messagesModel.isQuotedMessage();
     int cornerRadius = (int) (13 * getResources().getDisplayMetrics().density);

     String originalMessageSenderName = null, originalMessage = null, originalMessageTime = null;

     Integer originalMessagePlaceholderImage = null;
     if (isQuotedMessage) {
       originalMessageSenderName = messagesModel.getOriginalMessageSenderName();
       originalMessage = messagesModel.getOriginalMessage();
       originalMessageTime = messagesModel.getOriginalMessageTime();
       originalMessagePlaceholderImage = messagesModel.getOriginalMessagePlaceholderImage();
     }

     switch (messagesModel.getCustomMessageType()) {

       case ReplaySent:
       case TextSent: {
         //TextMessage
         ismActivityMessageDeliveryStatusBinding.rlTextMessage.setVisibility(View.VISIBLE);

         ismActivityMessageDeliveryStatusBinding.tvTextMessage.setText(
             messagesModel.getTextMessage());

         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessageText.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessageText.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessageText.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessageText.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(ismActivityMessageDeliveryStatusBinding.vParentMessageText.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessageText.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
           ismActivityMessageDeliveryStatusBinding.dividerText.setVisibility(View.VISIBLE);
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessageText.getRoot()
               .setVisibility(View.GONE);
           ismActivityMessageDeliveryStatusBinding.dividerText.setVisibility(View.GONE);
         }

         break;
       }
       case PhotoSent: {
         //Image
         ismActivityMessageDeliveryStatusBinding.rlPhoto.setVisibility(View.VISIBLE);
         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessagePhoto.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessagePhoto.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessagePhoto.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessagePhoto.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(
                       ismActivityMessageDeliveryStatusBinding.vParentMessagePhoto.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessagePhoto.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessagePhoto.getRoot()
               .setVisibility(View.GONE);
         }
         try {
           Glide.with(this)
               .load(messagesModel.getPhotoMainUrl())
               .thumbnail(Glide.with(this).load(messagesModel.getPhotoThumbnailUrl()))
               .placeholder(R.drawable.ism_avatar_group_large)
               .transform(new CenterCrop(),
                   new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
               .into(ismActivityMessageDeliveryStatusBinding.ivPhoto);
         } catch (IllegalArgumentException | NullPointerException ignore) {
         }
         ismActivityMessageDeliveryStatusBinding.tvPhotoSize.setText(
             messagesModel.getMediaSizeInMB());

         break;
       }
       case VideoSent: {
         //Video
         ismActivityMessageDeliveryStatusBinding.rlVideo.setVisibility(View.VISIBLE);
         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessageVideo.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessageVideo.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessageVideo.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessageVideo.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(
                       ismActivityMessageDeliveryStatusBinding.vParentMessageVideo.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessageVideo.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessageVideo.getRoot()
               .setVisibility(View.GONE);
         }
         try {
           Glide.with(this)
               .load(messagesModel.getVideoMainUrl())
               .thumbnail(Glide.with(this).load(messagesModel.getVideoThumbnailUrl()))
               .placeholder(R.drawable.ism_avatar_group_large)
               .transform(new CenterCrop(),
                   new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
               .into(ismActivityMessageDeliveryStatusBinding.ivVideoThumbnail);
         } catch (IllegalArgumentException | NullPointerException ignore) {
         }
         ismActivityMessageDeliveryStatusBinding.tvVideoSize.setText(
             messagesModel.getMediaSizeInMB());

         break;
       }
       case AudioSent: {
         //Audio
         ismActivityMessageDeliveryStatusBinding.rlAudioMessage.setVisibility(View.VISIBLE);
         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessageAudio.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessageAudio.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessageAudio.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessageAudio.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(
                       ismActivityMessageDeliveryStatusBinding.vParentMessageAudio.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessageAudio.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessageAudio.getRoot()
               .setVisibility(View.GONE);
         }

         ismActivityMessageDeliveryStatusBinding.tvAudioName.setText(messagesModel.getAudioName());
         ismActivityMessageDeliveryStatusBinding.tvAudioSize.setText(
             messagesModel.getMediaSizeInMB());

         break;
       }
       case FileSent: {
         //File
         ismActivityMessageDeliveryStatusBinding.rlFile.setVisibility(View.VISIBLE);
         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessageFile.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessageFile.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessageFile.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessageFile.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(ismActivityMessageDeliveryStatusBinding.vParentMessageFile.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessageFile.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessageFile.getRoot()
               .setVisibility(View.GONE);
         }

         ismActivityMessageDeliveryStatusBinding.tvFileName.setText(messagesModel.getFileName());
         ismActivityMessageDeliveryStatusBinding.tvFileSize.setText(
             messagesModel.getMediaSizeInMB());
         break;
       }

       case LocationSent: {
         //Location
         ismActivityMessageDeliveryStatusBinding.rlLocation.setVisibility(View.VISIBLE);
         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessageLocation.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessageLocation.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessageLocation.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessageLocation.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(
                       ismActivityMessageDeliveryStatusBinding.vParentMessageLocation.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessageLocation.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessageLocation.getRoot()
               .setVisibility(View.GONE);
         }
         try {
           Glide.with(this)
               .load(Constants.LOCATION_PLACEHOLDER_IMAGE_URL)
               .placeholder(R.drawable.ism_avatar_group_large)
               .transform(new CenterCrop(),
                   new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
               .into(ismActivityMessageDeliveryStatusBinding.ivLocationImage);
         } catch (IllegalArgumentException | NullPointerException ignore) {
         }
         ismActivityMessageDeliveryStatusBinding.tvLocationName.setText(
             messagesModel.getLocationName());
         ismActivityMessageDeliveryStatusBinding.tvLocationDescription.setText(
             messagesModel.getLocationDescription());
         break;
       }
       case ContactSent: {
         //Contact
         ismActivityMessageDeliveryStatusBinding.rlContact.setVisibility(View.VISIBLE);
         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessageContact.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessageContact.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessageContact.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessageContact.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(
                       ismActivityMessageDeliveryStatusBinding.vParentMessageContact.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessageContact.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessageContact.getRoot()
               .setVisibility(View.GONE);
         }

         if (PlaceholderUtils.isValidImageUrl(messagesModel.getContactImageUrl())) {

           try {
             Glide.with(this)
                 .load(messagesModel.getContactImageUrl())
                 .placeholder(R.drawable.ism_ic_profile)
                 .transform(new CircleCrop())
                 .into(ismActivityMessageDeliveryStatusBinding.ivContactImage);
           } catch (IllegalArgumentException | NullPointerException ignore) {
           }
         } else {
           PlaceholderUtils.setTextRoundDrawable(this, messagesModel.getContactName(),
               ismActivityMessageDeliveryStatusBinding.ivContactImage, 12);
         }
         ismActivityMessageDeliveryStatusBinding.tvContactName.setText(
             messagesModel.getContactName());
         ismActivityMessageDeliveryStatusBinding.tvContactIdentifier.setText(
             messagesModel.getContactIdentifier());
         break;
       }

       case WhiteboardSent: {
         //Whiteboard
         ismActivityMessageDeliveryStatusBinding.rlWhiteboard.setVisibility(View.VISIBLE);
         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessageWhiteboard.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessageWhiteboard.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessageWhiteboard.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessageWhiteboard.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(
                       ismActivityMessageDeliveryStatusBinding.vParentMessageWhiteboard.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessageWhiteboard.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessageWhiteboard.getRoot()
               .setVisibility(View.GONE);
         }
         try {
           Glide.with(this)
               .load(messagesModel.getWhiteboardMainUrl())
               .thumbnail(Glide.with(this).load(messagesModel.getWhiteboardThumbnailUrl()))
               .placeholder(R.drawable.ism_avatar_group_large)
               .transform(new CenterCrop(),
                   new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
               .into(ismActivityMessageDeliveryStatusBinding.ivWhiteboard);
         } catch (IllegalArgumentException | NullPointerException ignore) {
         }
         ismActivityMessageDeliveryStatusBinding.tvWhiteboardSize.setText(
             messagesModel.getMediaSizeInMB());

         break;
       }

       case GifSent: {
         //Gif
         ismActivityMessageDeliveryStatusBinding.rlGif.setVisibility(View.VISIBLE);
         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessageGif.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessageGif.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessageGif.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessageGif.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(ismActivityMessageDeliveryStatusBinding.vParentMessageGif.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessageGif.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessageGif.getRoot()
               .setVisibility(View.GONE);
         }
         try {
           Glide.with(this)
               .load(messagesModel.getGifMainUrl())
               .thumbnail(Glide.with(this).load(messagesModel.getGifStillUrl()))
               .placeholder(R.drawable.ism_avatar_group_large)
               .transform(new CenterCrop(),
                   new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
               .into(ismActivityMessageDeliveryStatusBinding.ivGifImage);
         } catch (IllegalArgumentException | NullPointerException ignore) {
         }
         break;
       }
       case StickerSent: {
         //Sticker
         ismActivityMessageDeliveryStatusBinding.rlSticker.setVisibility(View.VISIBLE);
         if (isQuotedMessage) {
           ismActivityMessageDeliveryStatusBinding.vParentMessageSticker.tvSenderName.setText(
               originalMessageSenderName);
           ismActivityMessageDeliveryStatusBinding.vParentMessageSticker.tvMessage.setText(
               originalMessage);
           ismActivityMessageDeliveryStatusBinding.vParentMessageSticker.tvMessageTime.setText(
               originalMessageTime);

           if (originalMessagePlaceholderImage == null) {
             ismActivityMessageDeliveryStatusBinding.vParentMessageSticker.ivMessageImage.setVisibility(
                 View.GONE);
           } else {
             try {
               Glide.with(this)
                   .load(originalMessagePlaceholderImage)
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .into(
                       ismActivityMessageDeliveryStatusBinding.vParentMessageSticker.ivMessageImage);
             } catch (IllegalArgumentException | NullPointerException ignore) {
             }
             ismActivityMessageDeliveryStatusBinding.vParentMessageSticker.ivMessageImage.setVisibility(
                 View.VISIBLE);
           }
         } else {
           ismActivityMessageDeliveryStatusBinding.vParentMessageSticker.getRoot()
               .setVisibility(View.GONE);
         }
         try {
           Glide.with(this)
               .load(messagesModel.getStickerMainUrl())
               .thumbnail(Glide.with(this).load(messagesModel.getStickerStillUrl()))
               .placeholder(R.drawable.ism_avatar_group_large)
               .transform(new GranularRoundedCorners(0, 0, cornerRadius, cornerRadius))
               .into(ismActivityMessageDeliveryStatusBinding.ivStickerImage);
         } catch (IllegalArgumentException | NullPointerException ignore) {
         }
         break;
       }
     }

     final MessageDeliveryStatusFragmentAdapter messageDeliveryStatusFragmentAdapter =
         new MessageDeliveryStatusFragmentAdapter(MessageDeliveryStatusActivity.this);
     ismActivityMessageDeliveryStatusBinding.vpDeliveryStatus.setAdapter(
         messageDeliveryStatusFragmentAdapter);
     ismActivityMessageDeliveryStatusBinding.vpDeliveryStatus.setOffscreenPageLimit(1);
     new TabLayoutMediator(ismActivityMessageDeliveryStatusBinding.tabLayout,
         ismActivityMessageDeliveryStatusBinding.vpDeliveryStatus, (tab, position) -> {
       if (position == 0) {
         tab.setText(getString(R.string.ism_complete)).setIcon(R.drawable.ism_ic_delivery_complete);
       } else {
         tab.setText(getString(R.string.ism_pending)).setIcon(R.drawable.ism_ic_delivery_pending);
       }
     }).attach();

     ismActivityMessageDeliveryStatusBinding.ibBack.setOnClickListener(v -> onBackPressed());
   }

   /**
    * On message delivery read events disabled for conversation.
    */
   public void onMessageDeliveryReadEventsDisabledForConversation(){
     runOnUiThread(
         () -> ismActivityMessageDeliveryStatusBinding.rlReadEventsDisabled.setVisibility(View.VISIBLE));
     }
 }
