package io.isometrik.ui.messages.search.utils;

import android.content.Context;

import org.json.JSONException;

import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.MessagesModel;

/**
 * The helper class for generating Notification Body for a message based on type.
 */
public class NotificationBodyUtils {

    /**
     * Generate Notification Body.
     *
     * @param messagesModel the messages model
     * @param mediaName     the media name
     * @return the Notification Body String
     */
    public static String getNotificationBody(MessagesModel messagesModel, String mediaName) {
        String notificationBody = "";
        Context context = IsometrikUiSdk.getInstance().getContext();
        switch (messagesModel.getCustomMessageType()) {
            case TextSent: {
                notificationBody = messagesModel.getTextMessage().toString();
                break;
            }
            case PhotoSent: {
                notificationBody = "📷 "+context.getString(R.string.ism_photo);
                break;
            }
            case VideoSent: {
                notificationBody = "📹 "+context.getString(R.string.ism_video);
                break;
            }
            case AudioSent: {
                notificationBody = "🎤 "+context.getString(R.string.ism_voice_message);
                break;
            }
            case FileSent: {
                notificationBody = "📄 " + messagesModel.getFileName();
                break;
            }
            case StickerSent: {
              notificationBody = context.getString(R.string.ism_sticker);
                break;
            }
            case GifSent: {
              notificationBody = context.getString(R.string.ism_gif);

              break;
            }
            case WhiteboardSent: {
                notificationBody = context.getString(R.string.ism_whiteboard);
                break;
            }
            case LocationSent: {
                notificationBody = "📍 " + messagesModel.getLocationDescription();
                break;
            }
            case ContactSent: {
                if (messagesModel.getContactList().length() > 0) {
                    if (messagesModel.getContactList().length() == 1) {
                        try {
                            String name = messagesModel.getContactList().getJSONObject(0).getString("contactName");
                            notificationBody = "👤 " + name;
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            String name = messagesModel.getContactList().getJSONObject(0).getString("contactName");
                            notificationBody = "👤 " + name +" "+(messagesModel.getContactList().length() - 1)+ context.getString(R.string.ism_other_contacts);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                break;
            }
            case ReplaySent: {
                notificationBody = messagesModel.getTextMessage().toString();
            }
        }
        return notificationBody;
    }

}
