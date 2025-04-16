package io.isometrik.chat.enums

import io.isometrik.ui.messages.chat.MessagesModel
import io.isometrik.ui.messages.chat.messageBinders.MessageItemBinder

/**
 * Data class to store information about custom message types.
 */
data class CustomTypeInfo(
    val typeName: String,
    val value: String
)

/**
 * The enum Custom message types for various media types.
 */
enum class CustomMessageTypes(
    /**
     * Gets value.
     *
     * @return the value
     */
    @JvmField val value: String
) {
    /**
     * Text custom message types.
     */
    Text("AttachmentMessage:Text"),

    /**
     * Image custom message types.
     */
    Image("AttachmentMessage:Image"),

    /**
     * Video custom message types.
     */
    Video("AttachmentMessage:Video"),

    /**
     * Audio custom message types.
     */
    Audio("AttachmentMessage:Audio"),

    /**
     * File custom message types.
     */
    File("AttachmentMessage:File"),

    /**
     * Location custom message types.
     */
    Location("AttachmentMessage:Location"),

    /**
     * Sticker custom message types.
     */
    Sticker("AttachmentMessage:Sticker"),

    /**
     * Gif custom message types.
     */
    Gif("AttachmentMessage:Gif"),

    /**
     * Whiteboard custom message types.
     */
    Whiteboard("AttachmentMessage:Whiteboard"),

    /**
     * Contact custom message types.
     */
    Contact("AttachmentMessage:Contact"),

    /**
     * Reply custom message types.
     */
    Reply("AttachmentMessage:Reply"),

    /**
     * Post custom message types.
     */
    Post("AttachmentMessage:Post"),

    /**
     * Payment custom message types.
     */
    Payment("AttachmentMessage:Payment Request"),

    /**
     * OfferSent custom message types.
     */
    OfferSent("OFFER_SENT"),

    /**
     * CounterOffer custom message types.
     */
    CounterOffer("COUNTER_OFFER"),

    /**
     * EditOffer custom message types.
     */
    EditOffer("EDIT_OFFER"),

    /**
     * AcceptOffer custom message types.
     */
    AcceptOffer("ACCEPT_OFFER"),

    /**
     * CancelDeal custom message types.
     */
    CancelDeal("CANCEL_DEAL"),

    /**
     * CancelOffer custom message types.
     */
    CancelOffer("CANCEL_OFFER"),

    /**
     * BuyDirect custom message types.
     */
    BuyDirect("BUYDIRECT_REQUEST"),

    /**
     * AcceptBuyDirect custom message types.
     */
    AcceptBuyDirect("ACCEPT_BUYDIRECT_REQUEST"),

    /**
     * CancelBuyDirect custom message types.
     */
    CancelBuyDirect("CANCEL_BUYDIRECT_REQUEST"),

    /**
     * RejectBuyDirect custom message types.
     */
    RejectBuyDirect("REJECT_BUYDIRECT_REQUEST"),

    /**
     * PaymentEscrowed custom message types.
     */
    PaymentEscrowed("PAYMENT_ESCROWED"),

    /**
     * DealComplete custom message types.
     */
    DealComplete("DEAL_COMPLETE"),

    /**
     * Custom message type for dynamic types.
     */
    Custom("CUSTOM_MESSAGE_TYPE");

    companion object {
        private val customTypes = mutableMapOf<String, CustomTypeInfo>()
        private val customTypeBinders = mutableMapOf<String, MessageItemBinder<out MessagesModel, *>>()

        /**
         * Register a custom message type.
         * @param typeName The name of the custom message type
         * @param value The value string for the custom message type
         * @return The registered CustomMessageTypes instance
         */
        @JvmStatic
        fun registerCustomType(typeName: String, value: String): CustomMessageTypes {
            customTypes[value] = CustomTypeInfo(typeName, value)
            return CustomMessageTypes.Custom
        }

        /**
         * Register a custom binder for a custom message type.
         * @param value The value of the custom type
         * @param sentBinder The binder for sent messages
         * @param receivedBinder The binder for received messages
         */
        @JvmStatic
        fun registerCustomBinder(value: String, sentBinder: MessageItemBinder<out MessagesModel, *>, receivedBinder: MessageItemBinder<out MessagesModel, *>) {
            customTypeBinders[value] = sentBinder
            customTypeBinders["${value}_received"] = receivedBinder
        }

        /**
         * Get the binder for a custom message type.
         * @param value The value of the custom type
         * @param isSent Whether the message is sent or received
         * @return The registered binder or null if not found
         */
        @JvmStatic
        fun getCustomBinder(value: String, isSent: Boolean): MessageItemBinder<*, *>? {
            val key = if (isSent) value else "${value}_received"
            return customTypeBinders[key]
        }

        /**
         * Get the enum type from its string value.
         *
         * @param value the string value
         * @return the matching enum type or Text if no match is found
         */
        @JvmStatic
        fun fromValue(value: String): CustomMessageTypes {
            return values().find { it.value == value } 
                ?: if (customTypes.containsKey(value)) Custom else Text
        }

        /**
         * Check if a message type is a custom type.
         * @param value The value to check
         * @return true if it's a custom type, false otherwise
         */
        @JvmStatic
        fun isCustomType(value: String): Boolean {
            return customTypes.containsKey(value)
        }

        /**
         * Get custom type information.
         * @param value The value of the custom type
         * @return CustomTypeInfo if found, null otherwise
         */
        @JvmStatic
        fun getCustomTypeInfo(value: String): CustomTypeInfo? {
            return customTypes[value]
        }

        /**
         * Get all registered custom types.
         * @return Map of custom type values to their CustomTypeInfo
         */
        @JvmStatic
        fun getCustomTypes(): Map<String, CustomTypeInfo> {
            return customTypes.toMap()
        }
    }
}
