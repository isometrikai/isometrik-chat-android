package io.isometrik.chat.enums

/**
 * The enum MessageType for UI in conversation Screen.
 */
enum class MessageTypeUi(val value: Int) {
    TEXT_MESSAGE_SENT(0),
    PHOTO_MESSAGE_SENT(1),
    VIDEO_MESSAGE_SENT(2),
    AUDIO_MESSAGE_SENT(3),
    FILE_MESSAGE_SENT(4),
    STICKER_MESSAGE_SENT(5),
    GIF_MESSAGE_SENT(6),
    WHITEBOARD_MESSAGE_SENT(7),
    LOCATION_MESSAGE_SENT(8),
    CONTACT_MESSAGE_SENT(9),
    TEXT_MESSAGE_RECEIVED(10),
    PHOTO_MESSAGE_RECEIVED(11),
    VIDEO_MESSAGE_RECEIVED(12),
    AUDIO_MESSAGE_RECEIVED(13),
    FILE_MESSAGE_RECEIVED(14),
    STICKER_MESSAGE_RECEIVED(15),
    GIF_MESSAGE_RECEIVED(16),
    WHITEBOARD_MESSAGE_RECEIVED(17),
    LOCATION_MESSAGE_RECEIVED(18),
    CONTACT_MESSAGE_RECEIVED(19),
    CONVERSATION_ACTION_MESSAGE(20),
    REPLAY_MESSAGE_SENT(21),
    REPLAY_MESSAGE_RECEIVED(22),
    POST_MESSAGE_SENT(23),
    POST_MESSAGE_RECEIVED(24),
    OFFER_SENT(25),
    OFFER_RECEIVED(26),
    COUNTER_OFFER_SENT(27),
    COUNTER_OFFER_RECEIVED(28),
    EDIT_OFFER_SENT(29),
    EDIT_OFFER_RECEIVED(30),
    ACCEPT_OFFER_SENT(31),
    ACCEPT_OFFER_RECEIVED(32),
    CANCEL_DEAL_SENT(33),
    CANCEL_DEAL_RECEIVED(34),
    CANCEL_OFFER_SENT(35),
    CANCEL_OFFER_RECEIVED(36),
    BUY_DIRECT_SENT(37),
    BUY_DIRECT_RECEIVED(38),
    ACCEPT_BUY_DIRECT_SENT(39),
    ACCEPT_BUY_DIRECT_RECEIVED(40),
    CANCEL_BUY_DIRECT_SENT(41),
    CANCEL_BUY_DIRECT_RECEIVED(42),
    PAYMENT_ESCROWED_SENT(43),
    PAYMENT_ESCROWED_RECEIVED(44),
    DEAL_COMPLETE_SENT(45),
    DEAL_COMPLETE_RECEIVED(46),
    RECORD_VIDEO_SENT(47),
    CAMERA_PHOTO_SENT(48),
    PAYMENT_MESSAGE_SENT(49),
    PAYMENT_MESSAGE_RECEIVED(50),
    AUDIO_CALL_SENT(53),
    AUDIO_CALL_RECEIVED(54),
    VIDEO_CALL_SENT(55),
    VIDEO_CALL_RECEIVED(56),
    CUSTOM_MESSAGE_SENT(51),
    CUSTOM_MESSAGE_RECEIVED(52);

    companion object {
        private val customTypes = mutableMapOf<String, MessageTypeUi>()

        /**
         * Register a custom message type for UI.
         * @param typeName The name of the custom message type
         * @param isSent Whether the message is sent or received
         * @return The registered MessageTypeUi instance
         */
        @JvmStatic
        fun registerCustomType(typeName: String, isSent: Boolean): MessageTypeUi {
            val customType = if (isSent) CUSTOM_MESSAGE_SENT else CUSTOM_MESSAGE_RECEIVED
            customTypes[typeName] = customType
            return customType
        }

        /**
         * Get the enum type from its integer value.
         * @param value the integer value
         * @return the matching enum type or CONVERSATION_ACTION_MESSAGE if no match is found
         */
        @JvmStatic
        fun fromValue(value: Int): MessageTypeUi {
            return values().find { it.value == value } ?: CONVERSATION_ACTION_MESSAGE
        }

        /**
         * Get the UI message type for a custom message type.
         * @param customType The custom message type
         * @param isSent Whether the message is sent or received
         * @return The corresponding UI message type
         */
        @JvmStatic
        fun getUiTypeForCustomType(customType: String, isSent: Boolean): MessageTypeUi {
            return if (isSent) CUSTOM_MESSAGE_SENT else CUSTOM_MESSAGE_RECEIVED
        }

        /**
         * Check if a message type is a custom type.
         * @param type The message type to check
         * @return true if it's a custom type, false otherwise
         */
        @JvmStatic
        fun isCustomType(type: MessageTypeUi): Boolean {
            return type == CUSTOM_MESSAGE_SENT || type == CUSTOM_MESSAGE_RECEIVED
        }
    }
}
