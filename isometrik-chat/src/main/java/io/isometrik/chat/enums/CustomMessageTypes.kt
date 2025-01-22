package io.isometrik.chat.enums

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
    DealComplete("DEAL_COMPLETE");

    companion object {
        /**
         * Get the enum type from its string value.
         *
         * @param value the string value
         * @return the matching enum type or null if no match is found
         */
        fun fromValue(value: String): CustomMessageTypes {
            return values().find { it.value == value }?:Text
        }
    }

}
