package io.isometrik.ui.notifications


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.FutureTarget
import com.google.firebase.messaging.RemoteMessage
import io.isometrik.chat.R
import io.isometrik.chat.builder.message.delivery.MarkMessageAsDeliveredQuery
import io.isometrik.chat.response.error.IsometrikError
import io.isometrik.chat.response.message.delivery.MarkMessageAsDeliveredResult
import io.isometrik.chat.utils.NotificationUtil
import io.isometrik.chat.utils.PlaceholderUtils
import io.isometrik.ui.IsometrikChatSdk
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Boolean
import kotlin.Exception
import kotlin.Int
import kotlin.String

class HandlePushRemote {
    fun handle(context: Context, remoteMessage : RemoteMessage, resultIntent : Intent){
        val data: Map<String, String?> = remoteMessage.getData()
        try {
            if (IsometrikChatSdk.getInstance().userSession.userId != null) {
                IsometrikChatSdk.getInstance()
                    .isometrik
                    .configuration.clientId = IsometrikChatSdk.getInstance().userSession.userId
                IsometrikChatSdk.getInstance()
                    .isometrik
                    .configuration.userToken = IsometrikChatSdk.getInstance().userSession.userToken
                var senderName: String? = null
                var senderProfileImageUrl: String? = null
                var senderId: String? = null
                var message: String? = null
                val sentAt = data["sentAt"]!!.toLong()
                var conversationDetails: JSONObject? = null
                if (data.containsKey("conversationStatusMessage")) {
                    when (data["action"]) {
                        "observerJoin" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(R.string.ism_member_observer_joined, senderName)
                        }

                        "observerLeave" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = IsometrikChatSdk.getInstance()
                                .context
                                .getString(R.string.ism_member_observer_left, senderName)
                        }

                        "userBlockConversation" -> {
                            senderId = data["initiatorId"]
                            senderName = data["initiatorName"]
                            senderProfileImageUrl = data["initiatorProfileImageUrl"]
                            message = IsometrikChatSdk.getInstance()
                                .context
                                .getString(
                                    R.string.ism_blocked_user,
                                    data["opponentName"],
                                    senderName
                                )
                        }

                        "userUnblockConversation" -> {
                            senderId = data["initiatorId"]
                            senderName = data["initiatorName"]
                            senderProfileImageUrl = data["initiatorProfileImageUrl"]
                            message = IsometrikChatSdk.getInstance()
                                .context
                                .getString(
                                    R.string.ism_unblocked_user,
                                    data["opponentName"],
                                    senderName
                                )
                        }

                        "conversationCreated" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(R.string.ism_created_conversation/*, senderName*/)
                            conversationDetails = JSONObject(data["conversationDetails"])
                        }

                        "addAdmin" -> {
                            senderId = data["initiatorId"]
                            senderName = data["initiatorName"]
                            senderProfileImageUrl = data["initiatorProfileImageUrl"]
                            message =
                                context.getString(R.string.ism_made_admin, data["memberName"], senderName)
                        }

                        "removeAdmin" -> {
                            senderId = data["initiatorId"]
                            senderName = data["initiatorName"]
                            senderProfileImageUrl = data["initiatorProfileImageUrl"]
                            message = context.getString(
                                R.string.ism_removed_admin,
                                data["memberName"],
                                senderName
                            )
                        }

                        "memberJoin" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(R.string.ism_member_joined_public, senderName)
                        }

                        "memberLeave" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(R.string.ism_member_left, senderName)
                        }

                        "membersAdd" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            val membersAdded = StringBuilder()
                            try {
                                val members = JSONArray(data["members"])
                                val size = members.length()
                                var i = 0
                                while (i < size) {
                                    membersAdded.append(", ")
                                        .append(members.getJSONObject(i).getString("memberName"))
                                    i++
                                }
                                message = context.getString(
                                    R.string.ism_members_added,
                                    senderName,
                                    membersAdded.substring(2)
                                )
                            } catch (ignore: JSONException) {
                            }
                        }

                        "membersRemove" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            val membersRemoved = StringBuilder()
                            try {
                                val members = JSONArray(data["members"])
                                val size = members.length()
                                var i = 0
                                while (i < size) {
                                    membersRemoved.append(", ")
                                        .append(members.getJSONObject(i).getString("memberName"))
                                    i++
                                }
                                message = context.getString(
                                    R.string.ism_members_removed, senderName,
                                    membersRemoved.substring(2)
                                )
                            } catch (ignore: JSONException) {
                            }
                        }

                        "conversationImageUpdated" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(R.string.ism_updated_conversation_image, senderName)
                        }

                        "conversationTitleUpdated" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(
                                R.string.ism_updated_conversation_title, senderName,
                                data["conversationTitle"]
                            )
                        }

                        "reactionAdd" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(
                                R.string.ism_reaction_added_notification, senderName,
                                data["reactionType"]
                            )
                        }

                        "reactionRemove" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(
                                R.string.ism_reaction_removed_notification, senderName,
                                data["reactionType"]
                            )
                        }

                        "messagesDeleteLocal" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(R.string.ism_message_deleted_locally, senderName)
                        }

                        "messagesDeleteForAll" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            message = context.getString(R.string.ism_message_deleted_for_all, senderName)
                        }

                        "conversationSettingsUpdated" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            try {
                                val config = JSONObject(data["config"])
                                var settingsUpdated = ""
                                if (config.has("config.typingEvents")) {
                                    settingsUpdated =
                                        settingsUpdated + ", " + context.getString(R.string.ism_settings_typing)
                                }
                                if (config.has("config.readEvents")) {
                                    settingsUpdated = "$settingsUpdated, " + context.getString(
                                        R.string.ism_settings_read_delivery_events
                                    )
                                }
                                if (config.has("config.pushNotifications")) {
                                    settingsUpdated =
                                        settingsUpdated + ", " + context.getString(R.string.ism_settings_notifications)
                                }
                                message = context.getString(
                                    R.string.ism_updated_settings, senderName,
                                    settingsUpdated.substring(2)
                                )
                            } catch (ignore: JSONException) {
                            }
                        }

                        "conversationDetailsUpdated" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            try {
                                val details = JSONObject(data["details"])
                                var detailsUpdated = ""
                                if (details.has("customType")) {
                                    detailsUpdated =
                                        detailsUpdated + ", " + context.getString(R.string.ism_details_custom_type)
                                }
                                if (details.has("metadata")) {
                                    detailsUpdated =
                                        detailsUpdated + ", " + context.getString(R.string.ism_details_metadata)
                                }
                                if (details.has("searchableTags")) {
                                    detailsUpdated =
                                        detailsUpdated + ", " + context.getString(R.string.ism_details_searchable_tags)
                                }
                                message = context.getString(
                                    R.string.ism_updated_conversation_details, senderName,
                                    detailsUpdated.substring(2)
                                )
                            } catch (ignore: JSONException) {
                            }
                        }

                        "messageDetailsUpdated" -> {
                            senderId = data["userId"]
                            senderName = data["userName"]
                            senderProfileImageUrl = data["userProfileImageUrl"]
                            try {
                                val details = JSONObject(data["details"])
                                var detailsUpdated = ""
                                if (details.has("customType")) {
                                    detailsUpdated =
                                        detailsUpdated + ", " + context.getString(R.string.ism_details_custom_type)
                                }
                                if (details.has("metadata")) {
                                    detailsUpdated =
                                        detailsUpdated + ", " + context.getString(R.string.ism_details_metadata)
                                }
                                if (details.has("searchableTags")) {
                                    detailsUpdated =
                                        detailsUpdated + ", " + context.getString(R.string.ism_details_searchable_tags)
                                }
                                if (details.has("body")) {
                                    detailsUpdated =
                                        detailsUpdated + ", " + context.getString(R.string.ism_details_body)
                                }
                                message = context.getString(
                                    R.string.ism_updated_message_details, senderName,
                                    detailsUpdated.substring(2)
                                )
                            } catch (ignore: JSONException) {
                            }
                        }
                    }
                } else {
                    senderId = data["senderId"]
                    senderName = data["senderName"]
                    senderProfileImageUrl = data["senderProfileImageUrl"]
                    when (data["customType"]) {
                        "AttachmentMessage:Text" -> {
                            var prefix: String? = null
                            if (data["parentMessageId"] == null) {
                                if (data["action"] != null) {
                                    //action not received for normal messages
                                    if ("forward" == data["action"]) {
                                        prefix = context.getString(R.string.ism_forward_heading)
                                    }
                                }
                            } else {
                                prefix = context.getString(R.string.ism_quote_heading)
                            }
                            message = data["body"]
                            if (prefix != null) {
                                message = "$prefix: $message"
                            }
                        }

                        "AttachmentMessage:Image" -> {
                            message =
                                context.getString(R.string.ism_attachment_prefix) + context.getString(R.string.ism_photo)
                        }

                        "AttachmentMessage:Post" -> {
                            message =
                                context.getString(R.string.ism_attachment_prefix) + context.getString(R.string.ism_post)
                        }

                        "AttachmentMessage:Video" -> {
                            message =
                                context.getString(R.string.ism_attachment_prefix) + context.getString(R.string.ism_video)
                        }

                        "AttachmentMessage:Audio" -> {
                            message = context.getString(R.string.ism_attachment_prefix) + context.getString(
                                R.string.ism_audio_recording
                            )
                        }

                        "AttachmentMessage:File" -> {
                            message =
                                context.getString(R.string.ism_attachment_prefix) + context.getString(R.string.ism_file)
                        }

                        "AttachmentMessage:Sticker" -> {
                            message =
                                context.getString(R.string.ism_attachment_prefix) + context.getString(R.string.ism_sticker)
                        }

                        "AttachmentMessage:Gif" -> {
                            message =
                                context.getString(R.string.ism_attachment_prefix) + context.getString(R.string.ism_gif)
                        }

                        "AttachmentMessage:Whiteboard" -> {
                            message =
                                context.getString(R.string.ism_attachment_prefix) + context.getString(R.string.ism_whiteboard)
                        }

                        "AttachmentMessage:Location" -> {
                            message =
                                context.getString(R.string.ism_attachment_prefix) + context.getString(R.string.ism_location)
                        }

                        "AttachmentMessage:Contact" -> {
                            message =
                                context.getString(R.string.ism_attachment_prefix) + context.getString(R.string.ism_contact)
                        }
                    }
                    try {
                        if (IsometrikChatSdk.getInstance().userSession.userToken != null) {
                            if (senderId != null && senderId != IsometrikChatSdk.getInstance().userSession.userId) {
                                if (data["deliveryReadEventsEnabled"] == null || Boolean.parseBoolean(
                                        data["deliveryReadEventsEnabled"]
                                    )
                                ) {
                                    IsometrikChatSdk.getInstance()
                                        .isometrik
                                        .executor
                                        .execute {
                                            IsometrikChatSdk.getInstance()
                                                .isometrik
                                                .remoteUseCases
                                                .messageUseCases
                                                .markMessageAsDelivered(
                                                    MarkMessageAsDeliveredQuery.Builder()
                                                        .setConversationId(
                                                            data["conversationId"]
                                                        )
                                                        .setUserToken(
                                                            IsometrikChatSdk.getInstance()
                                                                .userSession.userToken
                                                        )
                                                        .setMessageId(data["messageId"])
                                                        .build()
                                                ) { var1: MarkMessageAsDeliveredResult?, var2: IsometrikError? ->
                                                    if (var1 != null) {
                                                        //Commented out intentionally to handle case when user receives few messages within 30 sec of killing app and hence for them push was not received and if lastupdated time updated for newer message from push, previous messages were not shown as delivered

                                                        //IsometrikChatSdk.getInstance()
                                                        //    .getUserSession()
                                                        //    .setDeliveryStatusUpdatedUpto(sentAt);
                                                    }
                                                }
                                        }
                                }
                            }
                        }
                    } catch (ignore: Exception) {
                    }
                }
                if (senderId != null) {
                    if (IsometrikChatSdk.getInstance().userSession.userId != senderId) {
                        message = context.getString(R.string.ism_bullet) + message
                        var bitmap: Bitmap? = null
                        if (PlaceholderUtils.isValidImageUrl(senderProfileImageUrl)) {
                            val density: Int = context.getResources().getDisplayMetrics().density.toInt()
                            val futureTarget: FutureTarget<Bitmap> = Glide.with(context)
                                .asBitmap()
                                .load(senderProfileImageUrl)
                                .transform(CircleCrop())
                                .submit(
                                    density * PlaceholderUtils.BITMAP_WIDTH,
                                    density * PlaceholderUtils.BITMAP_HEIGHT
                                )
                            try {
                                bitmap = futureTarget.get()
                            } catch (ignore: Exception) {
                            }
                        }
                        val person: Person
                        person = if (bitmap == null) {
                            Person.Builder().setKey(senderId).setName(senderName).build()
                        } else {
                            Person.Builder().setKey(senderId)
                                .setName(senderName)
                                .setIcon(IconCompat.createWithBitmap(bitmap))
                                .build()
                        }
                        val conversationId = data["conversationId"]
                        val conversationTitle: String?
                        conversationTitle = if (data["conversationTitle"] != null) {
                            data["conversationTitle"]
                        } else {
                            if (conversationDetails != null) {
                                conversationDetails.getString("conversationTitle")
                            } else {
                                if (data["privateOneToOne"] != null && Boolean.parseBoolean(
                                        data["privateOneToOne"]
                                    )
                                ) {
                                    senderName
                                } else {
                                    conversationId
                                }
                            }
                        }
                        val existingNotification: Notification?
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            existingNotification = findActiveNotification(conversationId.orEmpty(),context)
                        } else {
                            existingNotification = null
                        }
                        if (existingNotification != null) {
                            val extractedMessagingStyleFromNotification =
                                NotificationCompat.MessagingStyle.extractMessagingStyleFromNotification(
                                    existingNotification
                                )

                            // The recoveredBuilder is Notification.Builder whereas the activeStyle is NotificationCompat.MessagingStyle.
                            // It means you need to recreate the style as Notification.MessagingStyle to make it compatible with the builder.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                val recoveredBuilder =
                                    Notification.Builder.recoverBuilder(context, existingNotification)
                                val senderPerson: android.app.Person
                                senderPerson = if (person.icon != null) {
                                    android.app.Person.Builder().setKey(person.key)
                                        .setName(person.name)
                                        .setIcon(person.icon!!.toIcon(context))
                                        .build()
                                } else {
                                    android.app.Person.Builder().setKey(person.key)
                                        .setName(person.name)
                                        .build()
                                }
                                val messagingStyle =
                                    Notification.MessagingStyle(senderPerson).setConversationTitle(
                                        conversationTitle
                                    ).setGroupConversation(true)
                                val messages = extractedMessagingStyleFromNotification!!.messages
                                for (i in messages.indices) {
                                    val previousMessage = messages[i]
                                    var previousMessagePerson: android.app.Person
                                    previousMessagePerson =
                                        if (previousMessage.person!!.icon != null) {
                                            android.app.Person.Builder().setKey(
                                                previousMessage.person!!.key
                                            )
                                                .setName(previousMessage.person!!.name)
                                                .setIcon(previousMessage.person!!.icon!!.toIcon(context))
                                                .build()
                                        } else {
                                            android.app.Person.Builder().setKey(
                                                previousMessage.person!!.key
                                            )
                                                .setName(previousMessage.person!!.name)
                                                .build()
                                        }
                                    messagingStyle.addMessage(
                                        Notification.MessagingStyle.Message(
                                            previousMessage.text!!,
                                            previousMessage.timestamp, previousMessagePerson
                                        )
                                    )
                                }
                                messagingStyle.addMessage(
                                    Notification.MessagingStyle.Message(
                                        message,
                                        sentAt,
                                        senderPerson
                                    )
                                )
                                recoveredBuilder.style = messagingStyle
                                val notificationManager = NotificationManagerCompat.from(context)
                                notificationManager.notify(
                                    conversationId,
                                    NotificationUtil.getNotificationId(conversationId),
                                    recoveredBuilder.build()
                                )
                            }
                        } else {
                            val channelId: String = context.getString(R.string.ism_channel_id)
                            val notificationMessage =
                                NotificationCompat.MessagingStyle.Message(message, sentAt, person)
                            val messagingStyle =
                                NotificationCompat.MessagingStyle(person).setConversationTitle(
                                    conversationTitle
                                ).setGroupConversation(true).addMessage(notificationMessage)
//                            val resultIntent =
//                                Intent(context, ConversationMessagesActivity::class.java)
                            resultIntent.putExtra("conversationId", conversationId)
                            resultIntent.putExtra("fromNotification", true)
                            resultIntent.putExtra("conversationTitle", conversationTitle)
                            resultIntent.action = conversationId
                            if (data["privateOneToOne"] != null) {
                                resultIntent.putExtra(
                                    "isPrivateOneToOne",
                                    Boolean.parseBoolean(data["privateOneToOne"])
                                )
                            }
                            val resultPendingIntent: PendingIntent
                            resultPendingIntent =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    PendingIntent.getActivity(
                                        context, System.currentTimeMillis().toInt(), resultIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                    )
                                } else {
                                    PendingIntent.getActivity(
                                        context, System.currentTimeMillis().toInt(), resultIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                    )
                                }
                            val notification: Notification =
                                NotificationCompat.Builder(context, channelId).setSmallIcon(
                                    R.drawable.ism_notification_small_icon
                                )
                                    .setColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.ism_notification_icon
                                        )
                                    )
                                    .setLargeIcon(
                                        BitmapFactory.decodeResource(
                                            context.getResources(),
                                            R.mipmap.ism_ic_launcher
                                        )
                                    )
                                    .setStyle(messagingStyle)
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setGroup(context.getString(R.string.ism_group_name))
                                    .setAutoCancel(true)
                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                    .setContentIntent(resultPendingIntent)
                                    .build()
                            val notificationManager = NotificationManagerCompat.from(context)

                            // Since android Oreo notification channel is needed.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val channel = NotificationChannel(
                                    channelId, context.getString(R.string.ism_channel_name),
                                    NotificationManager.IMPORTANCE_HIGH
                                )
                                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                                notificationManager.createNotificationChannel(channel)
                            }
                            notificationManager.notify(
                                conversationId,
                                NotificationUtil.getNotificationId(conversationId), notification
                            )
                            val summaryNotification: Notification =
                                NotificationCompat.Builder(context, channelId).setContentTitle(
                                    context.getString(R.string.ism_summary_title)
                                ) //set content text to support devices running API level < 24
                                    .setContentText(context.getString(R.string.ism_summary_text))
                                    .setSmallIcon(R.drawable.ism_notification_small_icon)
                                    .setColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.ism_notification_icon
                                        )
                                    )
                                    .setGroup(context.getString(R.string.ism_group_name))
                                    .setGroupSummary(true) //.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                                    .build()
                            notificationManager.notify(
                                context.getString(R.string.ism_summary),
                                1,
                                summaryNotification
                            )
                        }
                    }
                }
            }
        } catch (ignore: Exception) {
        }
    }
    fun findActiveNotification(conversationId: String, context: Context): Notification? {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        if (notificationManager != null) {
            val notifications = notificationManager.activeNotifications
            for (notification in notifications) {
                if (notification.packageName == IsometrikChatSdk.getInstance().applicationId && notification.tag != null && notification.tag == conversationId) {
                    return notification.notification
                }
            }
        }
        return null
    }
}