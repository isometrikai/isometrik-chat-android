package io.isometrik.chat.utils.enums

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
    Replay("AttachmentMessage:Reply"),

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
    OfferSent("AttachmentMessage:OfferSent"),

    /**
     * CounterOffer custom message types.
     */
    CounterOffer("AttachmentMessage:CounterOffer"),

    /**
     * EditOffer custom message types.
     */
    EditOffer("AttachmentMessage:EditOffer"),

    /**
     * AcceptOffer custom message types.
     */
    AcceptOffer("AttachmentMessage:AcceptOffer"),

    /**
     * CancelDeal custom message types.
     */
    CancelDeal("AttachmentMessage:CancelDeal"),

    /**
     * CancelOffer custom message types.
     */
    CancelOffer("AttachmentMessage:CancelOffer"),

    /**
     * BuyDirect custom message types.
     */
    BuyDirect("AttachmentMessage:BuyDirect"),

    /**
     * AcceptBuyDirect custom message types.
     */
    AcceptBuyDirect("AttachmentMessage:AcceptBuyDirect"),

    /**
     * CancelBuyDirect custom message types.
     */
    CancelBuyDirect("AttachmentMessage:CancelBuyDirect"),

    /**
     * PaymentEscrowed custom message types.
     */
    PaymentEscrowed("AttachmentMessage:PaymentEscrowed"),

    /**
     * DealComplete custom message types.
     */
    DealComplete("AttachmentMessage:DealComplete");

    companion object {
        /**
         * Get the enum type from its string value.
         *
         * @param value the string value
         * @return the matching enum type or null if no match is found
         */
        fun fromValue(value: String): CustomMessageTypes? {
            return values().find { it.value == value }
        }
    }

}
