package io.isometrik.chat.services;

import io.isometrik.chat.response.message.FetchAttachmentPresignedUrlsResult;
import io.isometrik.chat.response.message.FetchMentionedMessagesResult;
import io.isometrik.chat.response.message.FetchMessagesCountResult;
import io.isometrik.chat.response.message.FetchMessagesResult;
import io.isometrik.chat.response.message.FetchUnreadMessagesCountResult;
import io.isometrik.chat.response.message.FetchUnreadMessagesResult;
import io.isometrik.chat.response.message.FetchUserMessagesResult;
import io.isometrik.chat.response.message.SendMessageResult;
import io.isometrik.chat.response.message.SendTypingMessageResult;
import io.isometrik.chat.response.message.UpdateMessageDetailsResult;
import io.isometrik.chat.response.message.broadcastforward.BroadcastMessageResult;
import io.isometrik.chat.response.message.broadcastforward.ForwardMessageResult;
import io.isometrik.chat.response.message.cleanup.RemoveMessagesForEveryoneResult;
import io.isometrik.chat.response.message.cleanup.RemoveMessagesForSelfResult;
import io.isometrik.chat.response.message.delivery.MarkMessageAsDeliveredResult;
import io.isometrik.chat.response.message.delivery.MarkMessageAsReadResult;
import io.isometrik.chat.response.message.delivery.MarkMultipleMessagesAsReadResult;
import io.isometrik.chat.response.message.delivery.UpdateLastReadInConversationResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

/**
 * The interface message service containing methods with api calls to send a message, fetch
 * messages, forward/broadcast message, delete a message for self/everyone, send typing message,
 * mark message as delivered/read, fetch message in a conversation, fetch unread messages in
 * conversation, fetch all messages which were sent to me, mark multiple messages as read, fetch
 * presigned url for media upload in conversation, update last read timestamp in conversation,
 * update details of a message in conversation.
 */
public interface MessageService {

  /**
   * Broadcast message call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/message/broadcast")
  Call<BroadcastMessageResult> broadcastMessage(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Forward message call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/message/forward")
  Call<ForwardMessageResult> forwardMessage(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove messages for self call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/messages/self")
  Call<RemoveMessagesForSelfResult> removeMessagesForSelf(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Remove messages for everyone call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/messages/everyone")
  Call<RemoveMessagesForEveryoneResult> removeMessagesForEveryone(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Mark multiple messages as read call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PUT("/chat/messages/read")
  Call<MarkMultipleMessagesAsReadResult> markMultipleMessagesAsRead(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Mark message as read call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PUT("/chat/indicator/read")
  Call<MarkMessageAsReadResult> markMessageAsRead(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Mark message as delivered call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PUT("/chat/indicator/delivered")
  Call<MarkMessageAsDeliveredResult> markMessageAsDelivered(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Send typing message call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/indicator/typing")
  Call<SendTypingMessageResult> sendTypingMessage(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Fetch messages call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/messages")
  Call<FetchMessagesResult> fetchMessages(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch user messages call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/messages/user")
  Call<FetchUserMessagesResult> fetchUserMessages(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch messages count call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/messages/count")
  Call<FetchMessagesCountResult> fetchMessagesCount(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Send message call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/message")
  Call<SendMessageResult> sendMessage(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Fetch unread messages count call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/messages/unread/count")
  Call<FetchUnreadMessagesCountResult> fetchUnreadMessagesCount(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch unread messages call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/messages/unread")
  Call<FetchUnreadMessagesResult> fetchUnreadMessages(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch attachment presigned urls call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/messages/presignedurls")
  Call<FetchAttachmentPresignedUrlsResult> fetchAttachmentPresignedUrls(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Fetch mentioned messages call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/messages/mentioned")
  Call<FetchMentionedMessagesResult> fetchMentionedMessages(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Update last read in conversation call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PUT("/chat/conversation/lastread")
  Call<UpdateLastReadInConversationResult> updateLastReadInConversation(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Update message details call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PATCH("/chat/message")
  Call<UpdateMessageDetailsResult> updateMessageDetails(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);
}
