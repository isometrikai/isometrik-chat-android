package io.isometrik.chat.services;

import io.isometrik.chat.response.deliverystatus.FetchConversationReadStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessageDeliveryStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessagePendingDeliveryStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessagePendingReadStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessageReadStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessagesDeliveryReadStatusResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

/**
 * The interface delivery status service containing methods with api calls to fetch complete/complete status of
 * delivery/read for a message, code to fetch delivered/read by to/all status for multiples
 * messages, fetch last read status for a conversation.
 */
public interface DeliveryStatusService {

  /**
   * Fetch message delivery status call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/message/status/delivery")
  Call<FetchMessageDeliveryStatusResult> fetchMessageDeliveryStatus(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch message read status call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/message/status/read")
  Call<FetchMessageReadStatusResult> fetchMessageReadStatus(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch message pending delivery status call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/message/status/delivery/pending")
  Call<FetchMessagePendingDeliveryStatusResult> fetchMessagePendingDeliveryStatus(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch message pending read status call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/message/status/read/pending")
  Call<FetchMessagePendingReadStatusResult> fetchMessagePendingReadStatus(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch conversation read status call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversation/status/read")
  Call<FetchConversationReadStatusResult> fetchConversationReadStatus(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch messages delivery read status call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/messages/status")
  Call<FetchMessagesDeliveryReadStatusResult> fetchMessagesDeliveryReadStatus(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);
}
