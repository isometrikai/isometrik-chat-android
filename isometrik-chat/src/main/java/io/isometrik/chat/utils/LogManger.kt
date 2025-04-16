package io.isometrik.chat.utils

import android.util.Log
import io.isometrik.chat.enums.IMRealtimeEventsVerbosity
import io.isometrik.chat.network.IsometrikConnection.Companion.ISM_TAG
import io.isometrik.ui.IsometrikChatSdk.Companion.instance
import io.isometrik.ui.messages.chat.common.ChatConfig

object LogManger {
    val isEnable = instance.isometrik.configuration.realtimeEventsVerbosity == IMRealtimeEventsVerbosity.FULL

    fun log(log : String){
        if(isEnable){
            Log.d(ISM_TAG, log)
        }
    }
    fun log(tag : String,log : String){
        if(isEnable){
            Log.d(tag, log)
        }
    }
}