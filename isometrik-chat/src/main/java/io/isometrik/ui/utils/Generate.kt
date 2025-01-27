package io.isometrik.ui.utils

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
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_photo)
        }

        Video -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_video)
        }

        Audio -> {
            lastMessageText =
                IsometrikChatSdk.getInstance().context.getString(R.string.ism_audio_recording)
        }

        File -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_file)
        }

        Sticker -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_sticker)
        }

        Gif -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_gif)
        }

        Whiteboard -> {
            lastMessageText =
                IsometrikChatSdk.getInstance().context.getString(R.string.ism_whiteboard)
        }

        Location -> {
            lastMessageText =
                IsometrikChatSdk.getInstance().context.getString(R.string.ism_location)
        }

        Contact -> {
            lastMessageText = IsometrikChatSdk.getInstance().context.getString(R.string.ism_contact)

        }

        Reply -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        Post -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        Payment -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        OfferSent -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        CounterOffer -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        EditOffer -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        AcceptOffer -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        CancelDeal -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        CancelOffer -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        BuyDirect -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        AcceptBuyDirect -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        CancelBuyDirect -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        RejectBuyDirect -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        PaymentEscrowed -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)

        DealComplete -> lastMessageText =
            IsometrikChatSdk.getInstance().context.getString(R.string.ism_offer)
    }

    return lastMessageText
}

