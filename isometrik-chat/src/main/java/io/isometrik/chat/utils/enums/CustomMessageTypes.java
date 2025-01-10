package io.isometrik.chat.utils.enums;

/**
 * The enum Custom message types for various media types.
 */
public enum CustomMessageTypes {

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

    Post("AttachmentMessage:Post"),

    OfferSent("AttachmentMessage:OfferSent"),
    CounterOffer("AttachmentMessage:CounterOffer"),
    EditOffer("AttachmentMessage:EditOffer"),
    AcceptOffer("AttachmentMessage:AcceptOffer"),
    CancelDeal("AttachmentMessage:CancelDeal"),
    CancelOffer("AttachmentMessage:CancelOffer"),
    BuyDirect("AttachmentMessage:BuyDirect"),
    AcceptBuyDirect("AttachmentMessage:AcceptBuyDirect"),
    CancelBuyDirect("AttachmentMessage:CancelBuyDirect"),
    PaymentEscrowed("AttachmentMessage:PaymentEscrowed"),
    DealComplete("AttachmentMessage:DealComplete");

    private final String value;

    CustomMessageTypes(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public final String getValue() {
        return this.value;
    }
}
