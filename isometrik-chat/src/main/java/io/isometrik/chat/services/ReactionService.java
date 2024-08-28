package io.isometrik.chat.services;

import io.isometrik.chat.response.reaction.AddReactionResult;
import io.isometrik.chat.response.reaction.FetchReactionsResult;
import io.isometrik.chat.response.reaction.RemoveReactionResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * The interface reaction service containing methods with api calls.
 */
public interface ReactionService {

  /**
   * Add reaction call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call
   */
  @POST("/chat/reaction")
  Call<AddReactionResult> addReaction(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove reaction call.
   *
   * @param headers the headers
   * @param reactionType the reaction type
   * @param queryParams the query params
   * @return the call
   */
  @DELETE("/chat/reaction/{reactionType}")
  Call<RemoveReactionResult> removeReaction(@HeaderMap Map<String, String> headers,
      @Path("reactionType") String reactionType, @QueryMap Map<String, Object> queryParams);

  /**
   * Fetch reactions call.
   *
   * @param headers the headers
   * @param reactionType the reaction type
   * @param queryParams the query params
   * @return the call
   */
  @GET("/chat/reaction/{reactionType}")
  Call<FetchReactionsResult> fetchReactions(@HeaderMap Map<String, String> headers,
      @Path("reactionType") String reactionType, @QueryMap Map<String, Object> queryParams);
}
