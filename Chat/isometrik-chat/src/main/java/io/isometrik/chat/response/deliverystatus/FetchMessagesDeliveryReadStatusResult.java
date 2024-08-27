package io.isometrik.chat.response.deliverystatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch messages delivery read status request.
 */
public class FetchMessagesDeliveryReadStatusResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("messages")
  @Expose
  private ArrayList<MessageDeliveryReadStatus> messageDeliveryReadStatuses;

  /**
   * The type Message delivery read status.
   */
  public static class MessageDeliveryReadStatus {
    @SerializedName("deliveredToAll")
    @Expose
    private boolean deliveredToAll;
    @SerializedName("readByAll")
    @Expose
    private boolean readByAll;
    @SerializedName("messageId")
    @Expose
    private String messageId;

    /**
     * Is delivered to all boolean.
     *
     * @return the boolean
     */
    public boolean isDeliveredToAll() {
      return deliveredToAll;
    }

    /**
     * Is read by all boolean.
     *
     * @return the boolean
     */
    public boolean isReadByAll() {
      return readByAll;
    }

    /**
     * Gets message id.
     *
     * @return the message id
     */
    public String getMessageId() {
      return messageId;
    }
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets message delivery read statuses.
   *
   * @return the message delivery read statuses
   */
  public ArrayList<MessageDeliveryReadStatus> getMessageDeliveryReadStatuses() {
    return messageDeliveryReadStatuses;
  }
}
