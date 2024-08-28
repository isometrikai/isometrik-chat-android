package io.isometrik.chat.services;

import io.isometrik.chat.response.conversation.CreateConversationResult;
import io.isometrik.chat.response.conversation.FetchConversationDetailsResult;
import io.isometrik.chat.response.conversation.FetchConversationMembersResult;
import io.isometrik.chat.response.conversation.FetchConversationMessagingStatusResult;
import io.isometrik.chat.response.conversation.FetchConversationPresignedUrlResult;
import io.isometrik.chat.response.conversation.FetchConversationWatchersResult;
import io.isometrik.chat.response.conversation.FetchConversationsCountResult;
import io.isometrik.chat.response.conversation.FetchConversationsResult;
import io.isometrik.chat.response.conversation.FetchPublicOrOpenConversationsResult;
import io.isometrik.chat.response.conversation.FetchUnreadConversationsCountResult;
import io.isometrik.chat.response.conversation.cleanup.ClearConversationResult;
import io.isometrik.chat.response.conversation.cleanup.DeleteConversationLocallyResult;
import io.isometrik.chat.response.conversation.config.UpdateConversationDetailsResult;
import io.isometrik.chat.response.conversation.config.UpdateConversationImageResult;
import io.isometrik.chat.response.conversation.config.UpdateConversationSettingsResult;
import io.isometrik.chat.response.conversation.config.UpdateConversationTitleResult;
import io.isometrik.chat.response.membershipcontrol.FetchMembersToAddToConversationResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * The interface conversation service containing methods with api calls to update conversation
 * title/image/settings/details.
 * Api calls to create conversation, fetch conversations/conversations
 * count/members/watcher/details.
 * Api calls to clear conversation, fetch members to add to conversation, fetch public/open
 * conversations.
 * Api calls to check messaging disabled status, fetch unread conversations, presigned url for
 * upload image of update/create conversation.
 */
public interface ConversationService {

  /**
   * Update conversation title call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PATCH("/chat/conversation/title")
  Call<UpdateConversationTitleResult> updateConversationTitle(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Update conversation settings call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PATCH("/chat/conversation/settings")
  Call<UpdateConversationSettingsResult> updateConversationSettings(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Update conversation image call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PATCH("/chat/conversation/image")
  Call<UpdateConversationImageResult> updateConversationImage(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Update conversation details call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PATCH("/chat/conversation/details")
  Call<UpdateConversationDetailsResult> updateConversationDetails(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Create conversation call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/conversation")
  Call<CreateConversationResult> createConversation(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Fetch conversations call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversations")
  Call<FetchConversationsResult> fetchConversations(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch conversations count call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversations/count")
  Call<FetchConversationsCountResult> fetchConversationsCount(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch conversation watchers call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversation/watchers")
  Call<FetchConversationWatchersResult> fetchConversationWatchers(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch conversation details call.
   *
   * @param headers the headers
   * @param conversationId the conversation id
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversation/details/{conversationId}")
  Call<FetchConversationDetailsResult> fetchConversationDetails(
      @HeaderMap Map<String, String> headers, @Path("conversationId") String conversationId,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch conversation members call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversation/members")
  Call<FetchConversationMembersResult> fetchConversationMembers(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Clear conversation call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/conversation/clear")
  Call<ClearConversationResult> clearConversation(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Delete conversation locally call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/conversation/local")
  Call<DeleteConversationLocallyResult> deleteConversationLocally(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch unread conversations count call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversations/unread/count")
  Call<FetchUnreadConversationsCountResult> fetchUnreadConversationsCount(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch members to add to conversation call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversation/eligible/members")
  Call<FetchMembersToAddToConversationResult> fetchMembersToAddToConversation(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch conversation presigned url call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversation/presignedurl")
  Call<FetchConversationPresignedUrlResult> fetchConversationPresignedUrl(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch public or open conversations call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversations/publicoropen")
  Call<FetchPublicOrOpenConversationsResult> fetchPublicOrOpenConversations(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch conversation messaging status call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversation/messaging/status")
  Call<FetchConversationMessagingStatusResult> fetchConversationMessagingStatus(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);
}
