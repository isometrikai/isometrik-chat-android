package io.isometrik.ui.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.FutureTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.isometrik.chat.builder.message.delivery.MarkMessageAsDeliveredQuery;
import io.isometrik.ui.IsometrikChatSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.chat.ConversationMessagesActivity;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.NotificationUtil;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The service to receive the firebase message, parse the content and generate
 * notification for realtime message and various conversation action messages.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);

    Map<String, String> data = remoteMessage.getData();
    try {

      if (IsometrikChatSdk.getInstance().getUserSession().getUserId() != null) {
        IsometrikChatSdk.getInstance()
            .getIsometrik()
            .getConfiguration()
            .setClientId(IsometrikChatSdk.getInstance().getUserSession().getUserId());
        IsometrikChatSdk.getInstance()
            .getIsometrik()
            .getConfiguration()
            .setUserToken(IsometrikChatSdk.getInstance().getUserSession().getUserToken());

        String senderName = null, senderProfileImageUrl = null, senderId = null;
        String message = null;
        long sentAt = Long.parseLong(data.get("sentAt"));
        JSONObject conversationDetails = null;
        if (data.containsKey("conversationStatusMessage")) {
          switch (data.get("action")) {

            case "observerJoin": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              message = IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_member_observer_joined, senderName);
              break;
            }

            case "observerLeave": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              message = IsometrikChatSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_member_observer_left, senderName);
              break;
            }

            case "userBlockConversation": {
              senderId = data.get("initiatorId");
              senderName = data.get("initiatorName");
              senderProfileImageUrl = data.get("initiatorProfileImageUrl");

              if (data.get("opponentName").equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) {
                message = IsometrikChatSdk.getInstance().getContext().getString(
                        R.string.ism_blocked_user_text, data.get("initiatorName"),"You");
              } else if (data.get("initiatorName").equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) {
                message = IsometrikChatSdk.getInstance().getContext().getString(
                        R.string.ism_blocked_user_text, "You",data.get("opponentName"));
              } else {
                message = IsometrikChatSdk.getInstance().getContext().getString(
                        R.string.ism_blocked_user, data.get("initiatorName"), data.get("opponentName"));
              }
              break;
            }

            case "userUnblockConversation": {
              senderId = data.get("initiatorId");
              senderName = data.get("initiatorName");
              senderProfileImageUrl = data.get("initiatorProfileImageUrl");

              if (data.get("opponentName").equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) {
                message = IsometrikChatSdk.getInstance().getContext().getString(
                        R.string.ism_unblocked_user_text, data.get("initiatorName"),"You");
              } else if (data.get("initiatorName").equals(IsometrikChatSdk.getInstance().getUserSession().getUserName())) {
                message = IsometrikChatSdk.getInstance().getContext().getString(
                        R.string.ism_unblocked_user_text, "You",data.get("opponentName"));
              } else {
                message = IsometrikChatSdk.getInstance().getContext().getString(
                        R.string.ism_unblocked_user, data.get("initiatorName"), data.get("opponentName"));
              }
              break;
            }
            case "conversationCreated": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              message = getString(R.string.ism_created_conversation, senderName);
              conversationDetails = new JSONObject(data.get("conversationDetails"));
              break;
            }

            case "addAdmin": {
              senderId = data.get("initiatorId");
              senderName = data.get("initiatorName");
              senderProfileImageUrl = data.get("initiatorProfileImageUrl");
              message = getString(R.string.ism_made_admin, data.get("memberName"), senderName);
              break;
            }

            case "removeAdmin": {
              senderId = data.get("initiatorId");
              senderName = data.get("initiatorName");
              senderProfileImageUrl = data.get("initiatorProfileImageUrl");
              message = getString(R.string.ism_removed_admin, data.get("memberName"), senderName);
              break;
            }

            case "memberJoin": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              message = getString(R.string.ism_member_joined_public, senderName);
              break;
            }

            case "memberLeave": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              message = getString(R.string.ism_member_left, senderName);
              break;
            }

            case "membersAdd": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              StringBuilder membersAdded = new StringBuilder();
              try {
                JSONArray members = new JSONArray(data.get("members"));
                int size = members.length();
                for (int i = 0; i < size; i++) {
                  membersAdded.append(", ")
                      .append(members.getJSONObject(i).getString("memberName"));
                }
                message =
                    getString(R.string.ism_members_added, senderName, membersAdded.substring(2));
              } catch (JSONException ignore) {
              }
              break;
            }
            case "membersRemove": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              StringBuilder membersRemoved = new StringBuilder();
              try {
                JSONArray members = new JSONArray(data.get("members"));
                int size = members.length();
                for (int i = 0; i < size; i++) {
                  membersRemoved.append(", ")
                      .append(members.getJSONObject(i).getString("memberName"));
                }

                message = getString(R.string.ism_members_removed, senderName,
                    membersRemoved.substring(2));
              } catch (JSONException ignore) {
              }
              break;
            }

            case "conversationImageUpdated": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              message = getString(R.string.ism_updated_conversation_image, senderName);
              break;
            }

            case "conversationTitleUpdated": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              message = getString(R.string.ism_updated_conversation_title, senderName,
                  data.get("conversationTitle"));
              break;
            }

            case "reactionAdd": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");

              message = getString(R.string.ism_reaction_added_notification, senderName,
                  data.get("reactionType"));
              break;
            }

            case "reactionRemove": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");

              message = getString(R.string.ism_reaction_removed_notification, senderName,
                  data.get("reactionType"));
              break;
            }
            case "messagesDeleteLocal": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              message = getString(R.string.ism_message_deleted_locally, senderName);
              break;
            }

            case "messagesDeleteForAll": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              message = getString(R.string.ism_message_deleted_for_all, senderName);
              break;
            }

            case "conversationSettingsUpdated": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              try {
                JSONObject config = new JSONObject(data.get("config"));

                String settingsUpdated = "";
                if (config.has("config.typingEvents")) {
                  settingsUpdated =
                      settingsUpdated + ", " + getString(R.string.ism_settings_typing);
                }
                if (config.has("config.readEvents")) {
                  settingsUpdated = settingsUpdated + ", " + getString(
                      R.string.ism_settings_read_delivery_events);
                }
                if (config.has("config.pushNotifications")) {
                  settingsUpdated =
                      settingsUpdated + ", " + getString(R.string.ism_settings_notifications);
                }

                message = getString(R.string.ism_updated_settings, senderName,
                    settingsUpdated.substring(2));
              } catch (JSONException ignore) {
              }
              break;
            }

            case "conversationDetailsUpdated": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              try {
                JSONObject details = new JSONObject(data.get("details"));

                String detailsUpdated = "";
                if (details.has("customType")) {
                  detailsUpdated =
                      detailsUpdated + ", " + getString(R.string.ism_details_custom_type);
                }
                if (details.has("metadata")) {
                  detailsUpdated = detailsUpdated + ", " + getString(R.string.ism_details_metadata);
                }
                if (details.has("searchableTags")) {
                  detailsUpdated =
                      detailsUpdated + ", " + getString(R.string.ism_details_searchable_tags);
                }

                message = getString(R.string.ism_updated_conversation_details, senderName,
                    detailsUpdated.substring(2));
              } catch (JSONException ignore) {
              }
              break;
            }
            case "messageDetailsUpdated": {
              senderId = data.get("userId");
              senderName = data.get("userName");
              senderProfileImageUrl = data.get("userProfileImageUrl");
              try {
                JSONObject details = new JSONObject(data.get("details"));

                String detailsUpdated = "";
                if (details.has("customType")) {
                  detailsUpdated =
                      detailsUpdated + ", " + getString(R.string.ism_details_custom_type);
                }
                if (details.has("metadata")) {
                  detailsUpdated = detailsUpdated + ", " + getString(R.string.ism_details_metadata);
                }
                if (details.has("searchableTags")) {
                  detailsUpdated =
                      detailsUpdated + ", " + getString(R.string.ism_details_searchable_tags);
                }
                if (details.has("body")) {
                  detailsUpdated = detailsUpdated + ", " + getString(R.string.ism_details_body);
                }

                message = getString(R.string.ism_updated_message_details, senderName,
                    detailsUpdated.substring(2));
              } catch (JSONException ignore) {
              }
              break;
            }
          }
        } else {
          senderId = data.get("senderId");
          senderName = data.get("senderName");
          senderProfileImageUrl = data.get("senderProfileImageUrl");

          switch (data.get("customType")) {
            case "AttachmentMessage:Text": {
              String prefix = null;
              if (data.get("parentMessageId") == null) {

                if (data.get("action") != null) {
                  //action not received for normal messages

                  if ("forward".equals(data.get("action"))) {
                    prefix = getString(R.string.ism_forward_heading);
                  }
                }
              } else {
                prefix = getString(R.string.ism_quote_heading);
              }

              message = data.get("body");
              if (prefix != null) {
                message = prefix + ": " + message;
              }
              break;
            }
            case "AttachmentMessage:Image": {
              message = getString(R.string.ism_attachment_photo)+" " + getString(R.string.ism_photo);
              break;
            }
            case "AttachmentMessage:Video": {
              message = getString(R.string.ism_attachment_video)+" " + getString(R.string.ism_video);
              break;
            }
            case "AttachmentMessage:Audio": {
              message = getString(R.string.ism_attachment_audio)+" " + getString(
                  R.string.ism_audio_recording);
              break;
            }
            case "AttachmentMessage:File": {
              message = getString(R.string.ism_attachment_file)+" " + getString(R.string.ism_file);
              break;
            }
            case "AttachmentMessage:Sticker": {
              message = getString(R.string.ism_attachment_prefix) + getString(R.string.ism_sticker);
              break;
            }
            case "AttachmentMessage:Gif": {
              message = getString(R.string.ism_attachment_prefix) + getString(R.string.ism_gif);
              break;
            }
            case "AttachmentMessage:Whiteboard": {
              message =
                  getString(R.string.ism_attachment_prefix) + getString(R.string.ism_whiteboard);
              break;
            }
            case "AttachmentMessage:Location": {
              message =
                  getString(R.string.ism_attachment_location)+" " + getString(R.string.ism_location);
              break;
            }
            case "AttachmentMessage:Contact": {
              message = getString(R.string.ism_attachment_prefix) + getString(R.string.ism_contact);
              break;
            }
            case "AttachmentMessage:Reply":{
              String prefix = null;
              prefix = getString(R.string.ism_quote_heading);
              message = data.get("body");
              message = prefix + ": " + message;
              break;
            }
          }
          try {
            if (IsometrikChatSdk.getInstance().getUserSession().getUserToken() != null) {
              if (senderId != null && !senderId.equals(
                  IsometrikChatSdk.getInstance().getUserSession().getUserId())) {
                if (data.get("deliveryReadEventsEnabled") == null || (Boolean.parseBoolean(
                    data.get("deliveryReadEventsEnabled")))) {

                  IsometrikChatSdk.getInstance()
                      .getIsometrik()
                      .getExecutor()
                      .execute(() -> IsometrikChatSdk.getInstance()
                          .getIsometrik()
                          .getRemoteUseCases()
                          .getMessageUseCases()
                          .markMessageAsDelivered(
                              new MarkMessageAsDeliveredQuery.Builder().setConversationId(
                                      data.get("conversationId"))
                                  .setUserToken(
                                      IsometrikChatSdk.getInstance().getUserSession().getUserToken())
                                  .setMessageId(data.get("messageId"))
                                  .build(), (var1, var2) -> {
                                if (var1 != null) {
                                  //Commented out intentionally to handle case when user receives few messages within 30 sec of killing app and hence for them push was not received and if lastupdated time updated for newer message from push, previous messages were not shown as delivered

                                  //IsometrikChatSdk.getInstance()
                                  //    .getUserSession()
                                  //    .setDeliveryStatusUpdatedUpto(sentAt);
                                }
                              }));
                }
              }
            }
          } catch (Exception ignore) {
          }
        }

        if (senderId != null) {
          if (!IsometrikChatSdk.getInstance().getUserSession().getUserId().equals(senderId)) {
            message = getString(R.string.ism_bullet) + message;

            Bitmap bitmap = null;
            if (PlaceholderUtils.isValidImageUrl(senderProfileImageUrl)) {

              int density = (int) getResources().getDisplayMetrics().density;

              FutureTarget<Bitmap> futureTarget = Glide.with(this)
                  .asBitmap()
                  .load(senderProfileImageUrl)
                  .transform(new CircleCrop())
                  .submit(density * PlaceholderUtils.BITMAP_WIDTH,
                      density * PlaceholderUtils.BITMAP_HEIGHT);
              try {
                bitmap = futureTarget.get();
              } catch (Exception ignore) {

              }
            }

            Person person;
            if (bitmap == null) {
              person = new Person.Builder().setKey(senderId).setName(senderName).build();
            } else {
              person = new Person.Builder().setKey(senderId)
                  .setName(senderName)
                  .setIcon(IconCompat.createWithBitmap(bitmap))
                  .build();
            }

            String conversationId = data.get("conversationId");
            String conversationTitle;
            if (data.get("conversationTitle") != null) {
              conversationTitle = data.get("conversationTitle");
            } else {
              if (conversationDetails != null) {

                conversationTitle = conversationDetails.getString("conversationTitle");
              } else {
                if (data.get("privateOneToOne") != null && (Boolean.parseBoolean(
                    data.get("privateOneToOne")))) {
                  conversationTitle = senderName;
                } else {
                  conversationTitle = conversationId;
                }
              }
            }
            Notification existingNotification;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
              existingNotification = findActiveNotification(conversationId);
            } else {
              existingNotification = null;
            }
            if (existingNotification != null) {
              NotificationCompat.MessagingStyle extractedMessagingStyleFromNotification =
                  NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(
                      existingNotification);

              // The recoveredBuilder is Notification.Builder whereas the activeStyle is NotificationCompat.MessagingStyle.
              // It means you need to recreate the style as Notification.MessagingStyle to make it compatible with the builder.

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Notification.Builder recoveredBuilder =
                    Notification.Builder.recoverBuilder(this, existingNotification);

                android.app.Person senderPerson;
                if (person.getIcon() != null) {
                  senderPerson = new android.app.Person.Builder().setKey(person.getKey())
                      .setName(person.getName())
                      .setIcon(person.getIcon().toIcon(this))
                      .build();
                } else {
                  senderPerson = new android.app.Person.Builder().setKey(person.getKey())
                      .setName(person.getName())
                      .build();
                }
                Notification.MessagingStyle messagingStyle =
                    new Notification.MessagingStyle(senderPerson).setConversationTitle(
                        conversationTitle).setGroupConversation(true);

                List<NotificationCompat.MessagingStyle.Message> messages =
                    extractedMessagingStyleFromNotification.getMessages();

                for (int i = 0; i < messages.size(); i++) {
                  NotificationCompat.MessagingStyle.Message previousMessage = messages.get(i);
                  android.app.Person previousMessagePerson;
                  if (previousMessage.getPerson().getIcon() != null) {
                    previousMessagePerson = new android.app.Person.Builder().setKey(
                            previousMessage.getPerson().getKey())
                        .setName(previousMessage.getPerson().getName())
                        .setIcon(previousMessage.getPerson().getIcon().toIcon(this))
                        .build();
                  } else {
                    previousMessagePerson = new android.app.Person.Builder().setKey(
                            previousMessage.getPerson().getKey())
                        .setName(previousMessage.getPerson().getName())
                        .build();
                  }
                  messagingStyle.addMessage(
                      new Notification.MessagingStyle.Message(previousMessage.getText(),
                          previousMessage.getTimestamp(), previousMessagePerson));
                }

                messagingStyle.addMessage(
                    new Notification.MessagingStyle.Message(message, sentAt, senderPerson));
                recoveredBuilder.setStyle(messagingStyle);
                NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
                notificationManager.notify(conversationId,
                    NotificationUtil.getNotificationId(conversationId), recoveredBuilder.build());
              }
            } else {

              String channelId = getString(R.string.ism_channel_id);
              NotificationCompat.MessagingStyle.Message notificationMessage =
                  new NotificationCompat.MessagingStyle.Message(message, sentAt, person);

              NotificationCompat.MessagingStyle messagingStyle =
                  new NotificationCompat.MessagingStyle(person).setConversationTitle(
                      conversationTitle).setGroupConversation(true).addMessage(notificationMessage);

              Intent resultIntent = new Intent(this, ConversationMessagesActivity.class);
              resultIntent.putExtra("conversationId", conversationId);
              resultIntent.putExtra("fromNotification", true);
              resultIntent.putExtra("conversationTitle", conversationTitle);
              resultIntent.setAction(conversationId);

              if (data.get("privateOneToOne") != null) {
                resultIntent.putExtra("isPrivateOneToOne",
                    Boolean.parseBoolean(data.get("privateOneToOne")));
              }
              PendingIntent resultPendingIntent;

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                resultPendingIntent =
                    PendingIntent.getActivity(this, (int) System.currentTimeMillis(), resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
              } else {
                resultPendingIntent =
                    PendingIntent.getActivity(this, (int) System.currentTimeMillis(), resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
              }

              Notification notification =
                  new NotificationCompat.Builder(this, channelId).setSmallIcon(
                          R.drawable.ism_notification_small_icon)
                      .setColor(ContextCompat.getColor(this, R.color.ism_notification_icon))
                      .setLargeIcon(
                          BitmapFactory.decodeResource(getResources(), R.mipmap.ism_ic_launcher))
                      .setStyle(messagingStyle)
                      .setPriority(NotificationCompat.PRIORITY_HIGH)
                      .setDefaults(NotificationCompat.DEFAULT_ALL)
                      .setGroup(getString(R.string.ism_group_name))
                      .setAutoCancel(true)
                      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                      .setContentIntent(resultPendingIntent)
                      .build();

              NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

              // Since android Oreo notification channel is needed.
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel =
                    new NotificationChannel(channelId, getString(R.string.ism_channel_name),
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(channel);
              }

              notificationManager.notify(conversationId,
                  NotificationUtil.getNotificationId(conversationId), notification);

              Notification summaryNotification =
                  new NotificationCompat.Builder(this, channelId).setContentTitle(
                          getString(R.string.ism_summary_title))
                      //set content text to support devices running API level < 24
                      .setContentText(getString(R.string.ism_summary_text))
                      .setSmallIcon(R.drawable.ism_notification_small_icon)
                      .setColor(ContextCompat.getColor(this, R.color.ism_notification_icon))
                      .setGroup(getString(R.string.ism_group_name))
                      .setGroupSummary(true)
                      //.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                      .build();

              notificationManager.notify(getString(R.string.ism_summary), 1, summaryNotification);
            }
          }
        }
      }
    } catch (Exception ignore) {
    }
  }

  /**
   * Find active notification notification.
   *
   * @param conversationId the conversation id
   * @return the notification
   */
  public Notification findActiveNotification(String conversationId) {

    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    if (notificationManager != null) {

      StatusBarNotification[] notifications = notificationManager.getActiveNotifications();

      for (StatusBarNotification notification : notifications) {

        if (notification.getPackageName().equals(IsometrikChatSdk.getInstance().getApplicationId())
            && notification.getTag() != null
            && notification.getTag().equals(conversationId)) {

          return notification.getNotification();
        }
      }
    }
    return null;
  }

  @Override
  public void onNewToken(@NonNull @NotNull String token) {
    super.onNewToken(token);
    //TODO Nothing
  }
}