package io.isometrik.chat.services;

import io.isometrik.chat.response.user.AuthenticateUserResult;
import io.isometrik.chat.response.user.CreateUserResult;
import io.isometrik.chat.response.user.DeleteUserResult;
import io.isometrik.chat.response.user.FetchCreateUserPresignedUrlResult;
import io.isometrik.chat.response.user.FetchUpdateUserPresignedUrlResult;
import io.isometrik.chat.response.user.FetchUserDetailsResult;
import io.isometrik.chat.response.user.FetchUsersResult;
import io.isometrik.chat.response.user.UpdateUserResult;
import io.isometrik.chat.response.user.block.BlockUserResult;
import io.isometrik.chat.response.user.block.FetchBlockedUsersCountResult;
import io.isometrik.chat.response.user.block.FetchBlockedUsersResult;
import io.isometrik.chat.response.user.block.FetchNonBlockedUsersResult;
import io.isometrik.chat.response.user.block.UnblockUserResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * The interface user service containing methods with api calls to authenticate,create/update/delete
 * a user.
 * Api calls to block/unblock user, fetch blocked/unblocked users.
 * Api calls to fetch blocked users count, fetch all users, fetch presigned url for image upload for
 * user create/update actions.
 */
public interface UserService {

  /**
   * Create user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/user")
  Call<CreateUserResult> createUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Authenticate user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/user/authenticate")
  Call<AuthenticateUserResult> authenticateUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Block user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/user/block")
  Call<BlockUserResult> blockUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Unblock user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/user/unblock")
  Call<UnblockUserResult> unblockUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Update user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @PATCH("/chat/user")
  Call<UpdateUserResult> updateUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Delete user call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/user")
  Call<DeleteUserResult> deleteUser(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch users call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/users")
  Call<FetchUsersResult> fetchUsers(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch user details call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/user/details")
  Call<FetchUserDetailsResult> fetchUserDetails(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch blocked users call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/user/block")
  Call<FetchBlockedUsersResult> fetchBlockedUsers(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch non blocked users call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/user/nonblock")
  Call<FetchNonBlockedUsersResult> fetchNonBlockedUsers(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch blocked users count call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/user/block/count")
  Call<FetchBlockedUsersCountResult> fetchBlockedUsersCount(@HeaderMap Map<String, String> headers,
      @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch create user presigned url call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/user/presignedurl/create")
  Call<FetchCreateUserPresignedUrlResult> fetchCreateUserPresignedUrl(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch update user presigned url call.
   *
   * @param headers the headers
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/user/presignedurl/update")
  Call<FetchUpdateUserPresignedUrlResult> fetchUpdateUserPresignedUrl(
      @HeaderMap Map<String, String> headers, @QueryMap Map<String, Object> queryParams);
}
