package io.isometrik.ui.utils

import io.isometrik.chat.R
import io.isometrik.chat.events.message.SendMessageEvent
import io.isometrik.ui.IsometrikChatSdk

fun lastMessage(sendMessageEvent: SendMessageEvent): String? {
    var lastMessageText: String? = null

    when (sendMessageEvent.getCustomType()) {
        "AttachmentMessage:Text" -> {

            lastMessageText = sendMessageEvent.body
        }

        "AttachmentMessage:Image" -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_photo)
        }

        "AttachmentMessage:Video" -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_video)
        }

        "AttachmentMessage:Audio" -> {
            lastMessageText =
                IsometrikChatSdk.getInstance().context.getString(R.string.ism_audio_recording)
        }

        "AttachmentMessage:File" -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_file)
        }

        "AttachmentMessage:Sticker" -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_sticker)
        }

        "AttachmentMessage:Gif" -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_gif)
        }

        "AttachmentMessage:Whiteboard" -> {
            lastMessageText =
                IsometrikChatSdk.getInstance().context.getString(R.string.ism_whiteboard)
        }

        "AttachmentMessage:Location" -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_location)
        }

        "AttachmentMessage:Contact" -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_contact)
        }
    }

    return lastMessageText
}

