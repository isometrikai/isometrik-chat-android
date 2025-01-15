package io.isometrik.ui.messages.preview;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import org.json.JSONArray;

import io.isometrik.chat.R;
import io.isometrik.ui.conversations.gallery.GalleryModel;
import io.isometrik.ui.messages.chat.contactList.ContactsListActivity;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.ui.messages.preview.audio.PreviewAudioActivity;
import io.isometrik.ui.messages.preview.image.PreviewImageActivity;
import io.isometrik.ui.messages.preview.image.PreviewImagePopup;
import io.isometrik.ui.messages.preview.video.PreviewVideoActivity;

import java.io.File;

/**
 * The helper class to preview message based on message type.
 */
public class PreviewMessageUtil {

    /**
     * Preview message.
     *
     * @param activity     the activity
     * @param galleryModel the gallery model
     */
    public static void previewMessage(Activity activity, GalleryModel galleryModel) {

        switch (galleryModel.getCustomType()) {
            case "AttachmentMessage:Image":
                //case "AttachmentMessage:Sticker":
                //case "AttachmentMessage:Gif":
            case "AttachmentMessage:Whiteboard": {

                handlePhotoClick(activity, galleryModel.getMediaUrl(), galleryModel.getMimeType(), false);
                break;
            }
            case "AttachmentMessage:Sticker":
            case "AttachmentMessage:Gif": {
                new PreviewImagePopup().show(activity, galleryModel.getMediaUrl());
                break;
            }
            case "AttachmentMessage:Video": {
                handleVideoClick(activity, galleryModel.getMediaThumbnailUrl(), galleryModel.getMediaUrl(),
                        galleryModel.getMimeType(), false);
                break;
            }
            case "AttachmentMessage:Audio": {
                handleAudioClick(activity, galleryModel.getMediaUrl(), galleryModel.getMediaDescription(),
                        galleryModel.getMimeType(), false);

                break;
            }
            case "AttachmentMessage:File": {
                handleFileClick(activity, galleryModel.getMediaUrl(), galleryModel.getMimeType(), false);

                break;
            }

            case "AttachmentMessage:Location": {

                handleLocationClick(activity, String.valueOf(galleryModel.getLatitude()),
                        String.valueOf(galleryModel.getLongitude()));

                break;
            }
            case "AttachmentMessage:Contact": {
                handleContactClick(activity, galleryModel.getContactName(),
                        galleryModel.getContactIdentifier(), galleryModel.getContactList());

                break;
            }
        }
    }

    /**
     * Preview message.
     *
     * @param activity      the activity
     * @param messagesModel the messages model
     * @param localMedia    the local media
     */
    public static void previewMessage(Activity activity, MessagesModel messagesModel,
                                      boolean localMedia) {
        switch (messagesModel.getCustomMessageType()) {
            case PHOTO_MESSAGE_SENT:
            case PHOTO_MESSAGE_RECEIVED: {
                handlePhotoClick(activity,
                        localMedia ? messagesModel.getDownloadedMediaPath() : messagesModel.getPhotoMainUrl(),
                        messagesModel.getMimeType(), localMedia);

                break;
            }
            case STICKER_MESSAGE_SENT:
            case STICKER_MESSAGE_RECEIVED: {
                //handlePhotoClick(activity, messagesModel.getStickerMainUrl(), messagesModel.getMimeType());
                new PreviewImagePopup().show(activity, messagesModel.getStickerMainUrl());
                break;
            }
            case GIF_MESSAGE_SENT:
            case GIF_MESSAGE_RECEIVED: {
                //handlePhotoClick(activity, messagesModel.getGifMainUrl(), messagesModel.getMimeType());
                new PreviewImagePopup().show(activity, messagesModel.getGifMainUrl());
                break;
            }
            case WHITEBOARD_MESSAGE_SENT:
            case WHITEBOARD_MESSAGE_RECEIVED: {
                handlePhotoClick(activity, localMedia ? messagesModel.getDownloadedMediaPath()
                        : messagesModel.getWhiteboardMainUrl(), messagesModel.getMimeType(), localMedia);

                break;
            }

            case VIDEO_MESSAGE_SENT:
            case VIDEO_MESSAGE_RECEIVED: {
                handleVideoClick(activity, messagesModel.getVideoThumbnailUrl(),
                        localMedia ? messagesModel.getDownloadedMediaPath() : messagesModel.getVideoMainUrl(),
                        messagesModel.getMimeType(), localMedia);
                break;
            }
            case AUDIO_MESSAGE_SENT:
            case AUDIO_MESSAGE_RECEIVED: {
                handleAudioClick(activity,
                        localMedia ? messagesModel.getDownloadedMediaPath() : messagesModel.getAudioUrl(),
                        messagesModel.getAudioName(), messagesModel.getMimeType(), localMedia);
                break;
            }

            case FILE_MESSAGE_SENT:
            case FILE_MESSAGE_RECEIVED: {
                handleFileClick(activity,
                        localMedia ? messagesModel.getDownloadedMediaPath() : messagesModel.getFileUrl(),
                        messagesModel.getMimeType(), localMedia);
                break;
            }

            case LOCATION_MESSAGE_SENT:
            case LOCATION_MESSAGE_RECEIVED: {

                handleLocationClick(activity, messagesModel.getLatitude(), messagesModel.getLongitude());
                break;
            }

            case CONTACT_MESSAGE_SENT:
            case CONTACT_MESSAGE_RECEIVED: {

                handleContactClick(activity, messagesModel.getContactName(),
                        messagesModel.getContactIdentifier(), messagesModel.getContactList());

                break;
            }
        }
    }

    private static void handleLocationClick(Activity activity, String latitude, String longitude) {
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=17");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(activity.getPackageManager()) == null) {
            Toast.makeText(activity, activity.getString(R.string.ism_no_apps_installed),
                    Toast.LENGTH_SHORT).show();
        } else {
            activity.startActivity(mapIntent);
        }
    }

    private static void handleContactClick(Activity activity, String contactName,
                                           String contactIdentifier, JSONArray contactList) {
        if (contactList.length() > 1) {
            Intent contactListIntent = new Intent(activity, ContactsListActivity.class);
            Bundle b = new Bundle();
            b.putString("contactList", contactList.toString());
            contactListIntent.putExtras(b);
            activity.startActivity(contactListIntent);
        } else {
            new AlertDialog.Builder(activity).setTitle(activity.getString(R.string.ism_contact))
                    .setMessage(activity.getString(R.string.ism_dial_save_contact_alert))
                    .setCancelable(true)
                    .setPositiveButton(activity.getString(R.string.ism_add_contact), (dialog, id) -> {

                        dialog.cancel();

                        Intent intent = new Intent(Intent.ACTION_INSERT);
                        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                        intent.putExtra(ContactsContract.Intents.Insert.NAME, contactName);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, contactIdentifier);
                        if (intent.resolveActivity(activity.getPackageManager()) == null) {
                            Toast.makeText(activity, activity.getString(R.string.ism_no_apps_installed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            activity.startActivity(intent);
                        }
                    }).setNegativeButton(activity.getString(R.string.ism_dial_contact), (dialog, id) -> {
                        dialog.cancel();

                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + contactIdentifier));
                        activity.startActivity(intent);
                    }).create().show();
        }
    }

    private static void handlePhotoClick(Activity activity, String mediaUrl, String mimeType,
                                         boolean localMedia) {

        new AlertDialog.Builder(activity).setTitle(
                        activity.getString(R.string.ism_open_media_alert_heading))
                .setMessage(activity.getString(R.string.ism_open_media_alert_message))
                .setCancelable(true)
                .setPositiveButton(activity.getString(R.string.ism_open_in_app), (dialog, id) -> {
                    dialog.cancel();
                    Intent intent = new Intent(activity, PreviewImageActivity.class);
                    intent.putExtra("imageUrl", mediaUrl);

                    activity.startActivity(intent);
                })
                .setNegativeButton(activity.getString(R.string.ism_open_in_other_apps), (dialog, id) -> {
                    dialog.cancel();
                    handleFileClick(activity, mediaUrl, mimeType, localMedia);
                })
                .create()
                .show();
    }

    private static void handleVideoClick(Activity activity, String videoThumbnailUrl,
                                         String videoMainUrl, String mimeType, boolean localMedia) {

        new AlertDialog.Builder(activity).setTitle(
                        activity.getString(R.string.ism_open_media_alert_heading))
                .setMessage(activity.getString(R.string.ism_open_media_alert_message))
                .setCancelable(true)
                .setPositiveButton(activity.getString(R.string.ism_open_in_app), (dialog, id) -> {

                    dialog.cancel();
                    Intent intent = new Intent(activity, PreviewVideoActivity.class);
                    intent.putExtra("videoMainUrl", videoMainUrl);
                    intent.putExtra("videoThumbnailUrl", videoThumbnailUrl);

                    activity.startActivity(intent);
                })
                .setNegativeButton(activity.getString(R.string.ism_open_in_other_apps), (dialog, id) -> {
                    dialog.cancel();
                    handleFileClick(activity, videoMainUrl, mimeType, localMedia);
                })
                .create()
                .show();
    }

    private static void handleAudioClick(Activity activity, String audioUrl, String audioName,
                                         String mimeType, boolean localMedia) {

        new AlertDialog.Builder(activity).setTitle(
                        activity.getString(R.string.ism_open_media_alert_heading))
                .setMessage(activity.getString(R.string.ism_open_media_alert_message))
                .setCancelable(true)
                .setPositiveButton(activity.getString(R.string.ism_open_in_app), (dialog, id) -> {

                    dialog.cancel();
                    Intent intent = new Intent(activity, PreviewAudioActivity.class);
                    intent.putExtra("audioUrl", audioUrl);
                    intent.putExtra("audioName", audioName);

                    activity.startActivity(intent);
                })
                .setNegativeButton(activity.getString(R.string.ism_open_in_other_apps), (dialog, id) -> {
                    dialog.cancel();
                    handleFileClick(activity, audioUrl, mimeType, localMedia);
                })
                .create()
                .show();
    }

    private static void handleFileClick(Activity activity, String fileUrl, String mimeType,
                                        boolean localFile) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (localFile) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider",
                        new File(fileUrl));
                browserIntent.setData(uri);
                browserIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.parse(fileUrl);
                browserIntent.setDataAndType(uri, mimeType);
            }
        } else {
            uri = Uri.parse(fileUrl);
            browserIntent.setDataAndType(uri, mimeType);
        }

        Intent chooser =
                Intent.createChooser(browserIntent, activity.getString(R.string.ism_open_with));
        //chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // optional
        if (activity.getPackageManager()
                .queryIntentActivities(browserIntent, PackageManager.MATCH_ALL)
                .size() > 0) {
            activity.startActivity(chooser);
        } else {
            Toast.makeText(activity, localFile ? activity.getString(R.string.ism_no_apps_installed)
                    : activity.getString(R.string.ism_download_media_required), Toast.LENGTH_SHORT).show();
        }
    }
}
