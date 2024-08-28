package io.isometrik.chat.services;

import io.isometrik.chat.response.conversation.FetchConversationMemberDetailsResult;
import io.isometrik.chat.response.membershipcontrol.AddAdminResult;
import io.isometrik.chat.response.membershipcontrol.AddMembersResult;
import io.isometrik.chat.response.membershipcontrol.FetchObserversResult;
import io.isometrik.chat.response.membershipcontrol.JoinAsObserverResult;
import io.isometrik.chat.response.membershipcontrol.JoinConversationResult;
import io.isometrik.chat.response.membershipcontrol.LeaveAsObserverResult;
import io.isometrik.chat.response.membershipcontrol.LeaveConversationResult;
import io.isometrik.chat.response.membershipcontrol.RemoveAdminResult;
import io.isometrik.chat.response.membershipcontrol.RemoveMembersResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

/**
 * The interface membership control service containing methods with api calls to add/remove admin,
 * add/remove members, join/leave conversations, join/leave as observer, fetch member details, fetch
 * members in conversation, fetch observers in conversation.
 */
public interface MembershipControlService {

  /**
   * Add admin call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PUT("/chat/conversation/admin")
  Call<AddAdminResult> addAdmin(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Add members call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PUT("/chat/conversation/members")
  Call<AddMembersResult> addMembers(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Join conversation call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PUT("/chat/conversation/join")
  Call<JoinConversationResult> joinConversation(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove members call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/conversation/members")
  Call<RemoveMembersResult> removeMembers(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Remove admin call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/conversation/admin")
  Call<RemoveAdminResult> removeAdmin(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Leave conversation call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/conversation/leave")
  Call<LeaveConversationResult> leaveConversation(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch conversation member details call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversation/member/details")
  Call<FetchConversationMemberDetailsResult> fetchConversationMemberDetails(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Join as observer call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PUT("/chat/conversation/observer/join")
  Call<JoinAsObserverResult> joinAsObserver(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Leave as observer call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/conversation/observer/leave")
  Call<LeaveAsObserverResult> leaveAsObserver(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch observers call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/conversation/observers")
  Call<FetchObserversResult> fetchObservers(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);
}
