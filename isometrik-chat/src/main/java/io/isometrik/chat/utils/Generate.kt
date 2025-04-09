package io.isometrik.chat.utils

import io.isometrik.chat.R
import io.isometrik.chat.enums.CustomMessageTypes
import io.isometrik.chat.enums.CustomMessageTypes.*
import io.isometrik.chat.events.message.SendMessageEvent
import io.isometrik.ui.IsometrikChatSdk

fun lastMessage(sendMessageEvent: SendMessageEvent): String? {
    var lastMessageText: String? = null

    when (CustomMessageTypes.fromValue(sendMessageEvent.customType)) {
        Text -> {
            lastMessageText = sendMessageEvent.body
        }

        Image -> {
            lastMessageText = IsometrikChatSdk.instance.context.getString(R.string.ism_photo)
        }

        Video -> {
            lastMessageText = IsometrikChatSdk.instance.context.getString(R.string.ism_video)
        }

        Audio -> {
            lastMessageText =
                IsometrikChatSdk.instance.context.getString(R.string.ism_audio_recording)
        }

        File -> {
            lastMessageText = IsometrikChatSdk.instance.context.getString(R.string.ism_file)
        }

        Sticker -> {
            lastMessageText = IsometrikChatSdk.instance.context.getString(R.string.ism_sticker)
        }

        Gif -> {
            lastMessageText = IsometrikChatSdk.instance.context.getString(R.string.ism_gif)
        }

        Whiteboard -> {
            lastMessageText =
                IsometrikChatSdk.instance.context.getString(R.string.ism_whiteboard)
        }

        Location -> {
            lastMessageText =
                IsometrikChatSdk.instance.context.getString(R.string.ism_location)
        }

        Contact -> {
            lastMessageText = IsometrikChatSdk.instance.context.getString(R.string.ism_contact)
        }

        Reply -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        Post -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        Payment -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        OfferSent -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        CounterOffer -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        EditOffer -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        AcceptOffer -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        CancelDeal -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        CancelOffer -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        BuyDirect -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        AcceptBuyDirect -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        CancelBuyDirect -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        RejectBuyDirect -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        PaymentEscrowed -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        DealComplete -> lastMessageText =
            IsometrikChatSdk.instance.context.getString(R.string.ism_offer)

        Custom -> {
            // Handle custom message types
            val customTypeInfo = CustomMessageTypes.getCustomTypeInfo(sendMessageEvent.customType)
            lastMessageText = // Use display name if available, otherwise use type name
                customTypeInfo?.typeName
                    ?: (// Fallback to message body or default string
                            sendMessageEvent.body ?: IsometrikChatSdk.instance.context.getString(R.string.ism_custom_message))
        }
    }

    return lastMessageText
}

