package io.isometrik.ui.messages.groupcast

import android.util.Log
import io.isometrik.chat.builder.groupcast.GroupCastCreateQuery
import io.isometrik.chat.builder.groupcast.GroupCastMessageQuery
import io.isometrik.chat.builder.message.broadcastforward.BroadcastMessageQuery
import io.isometrik.chat.enums.CustomMessageTypes
import io.isometrik.chat.response.CompletionHandler
import io.isometrik.chat.response.error.IsometrikError
import io.isometrik.chat.response.groupcast.GroupCastCreateResult
import io.isometrik.chat.response.groupcast.GroupCastMessageResult
import io.isometrik.chat.response.message.broadcastforward.GroupCastMessageResult
import io.isometrik.chat.response.message.utils.fetchmessages.MentionedUser
import io.isometrik.chat.response.message.utils.schemas.EventForMessage
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.messages.chat.utils.enums.RemoteMessageTypes
import org.json.JSONObject

class GroupCastPresenter {


    fun groupCastCreate(
        groupcastTitle: String,
        groupcastImageUrl: String,
        members: List<Map<String, Any>>,
        metaData: JSONObject
    ) {
        val userToken = IsometrikChatSdk.getInstance().userSession.userToken


        val groupCastCreateQuery =
            GroupCastCreateQuery.Builder(userToken, groupcastTitle, groupcastImageUrl)
                .setMemberData(members)
                .setCustomType(CustomMessageTypes.Text.value)
                .setMetaData(metaData)

        IsometrikChatSdk.getInstance().getIsometrik().remoteUseCases
            .groupCastUseCases
            .groupCastCreate(groupCastCreateQuery.build(),
                CompletionHandler { var1: GroupCastCreateResult?, var2: IsometrikError ->
                    if (var1 != null) {
                        Log.e("GroupCastId: ", "==> " + var1.groupcastId)
                    } else {

                    }
                })
    }


    fun groupCastMessage(
        message: String,
    ) {
        val userToken = IsometrikChatSdk.getInstance().userSession.userToken

        

        val groupCastMessageQuery =
            GroupCastMessageQuery.Builder().setUserToken(userToken)
                .setEncrypted(false)
                .setBody(message)
                .setMessageType(RemoteMessageTypes.NormalMessage.value)
                .setShowInConversation(true)
                .setEventForMessage(EventForMessage(true, true))
                .setCustomType(CustomMessageTypes.Text.value)
                .setDeviceId(IsometrikChatSdk.getInstance().userSession.deviceId)

        groupCastMessageQuery.showInConversation = true
        groupCastMessageQuery.sendPushForNewConversationCreated = true
        groupCastMessageQuery.notificationTitle = "Notification"
        groupCastMessageQuery.mentionedUsers = arrayListOf(MentionedUser())
        groupCastMessageQuery.events = EventForMessage(true,true)
        // so on..


        IsometrikChatSdk.getInstance().getIsometrik().remoteUseCases
            .groupCastUseCases
            .groupCastMessage(groupCastMessageQuery.build()
            ) { var1: GroupCastMessageResult?, var2: IsometrikError ->
                if (var1 != null) {
                    Log.e("MessageId: ", "==> " + var1.messageId)

                } else {

                }
            }
    }

}