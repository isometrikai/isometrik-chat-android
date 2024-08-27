package io.isometrik.chat.models.conversation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.conversation.FetchPublicOrOpenConversationsQuery;
import io.isometrik.chat.enums.ConversationType;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.conversation.FetchPublicOrOpenConversationsResult;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.ErrorResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import io.isometrik.chat.utils.MapUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the fetch public or open conversations request and expose the parsed successful or error response.
 */
public class FetchPublicOrOpenConversations {
  /**
   * Validate params.
   *
   * @param fetchPublicOrOpenConversationsQuery the fetch public or open conversations query
   * @param completionHandler the completion handler
   * @param retrofitManager the retrofit manager
   * @param imConfiguration the im configuration
   * @param baseResponse the base response
   * @param gson the gson
   */
  public void validateParams(
      @NotNull FetchPublicOrOpenConversationsQuery fetchPublicOrOpenConversationsQuery,
      @NotNull final CompletionHandler<FetchPublicOrOpenConversationsResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {
    String userToken = fetchPublicOrOpenConversationsQuery.getUserToken();
    Integer membersSkip = fetchPublicOrOpenConversationsQuery.getMembersSkip();
    Integer membersLimit = fetchPublicOrOpenConversationsQuery.getMembersLimit();
    Integer conversationType = fetchPublicOrOpenConversationsQuery.getConversationType();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (conversationType == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_CONVERSATION_TYPE_MISSING);
    } else if (conversationType != ConversationType.PublicConversation.getValue()
        && conversationType != ConversationType.OpenConversation.getValue()) {
      completionHandler.onComplete(null,
          IsometrikErrorBuilder.IMERROBJ_CONVERSATION_TYPE_INVALID_VALUE);
    } else {
      Integer sort = fetchPublicOrOpenConversationsQuery.getSort();
      Integer limit = fetchPublicOrOpenConversationsQuery.getLimit();
      Integer skip = fetchPublicOrOpenConversationsQuery.getSkip();
      Boolean includeMembers = fetchPublicOrOpenConversationsQuery.getIncludeMembers();
      List<String> membersIncluded = fetchPublicOrOpenConversationsQuery.getMembersIncluded();
      List<String> membersExactly = fetchPublicOrOpenConversationsQuery.getMembersExactly();
      List<String> conversationIds = fetchPublicOrOpenConversationsQuery.getConversationIds();
      String customType = fetchPublicOrOpenConversationsQuery.getCustomType();
      String searchTag = fetchPublicOrOpenConversationsQuery.getSearchTag();

      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> queryParams = new HashMap<>();
      queryParams.put("conversationType",conversationType);
      if (membersIncluded != null) {
        queryParams.put("membersIncluded", MapUtils.stringListToCsv(membersIncluded));
      }
      if (membersExactly != null) {
        queryParams.put("membersExactly", MapUtils.stringListToCsv(membersExactly));
      }
      if (conversationIds != null) {
        queryParams.put("ids", MapUtils.stringListToCsv(conversationIds));
      }
      if (customType != null) queryParams.put("customType", customType);
      if (searchTag != null) queryParams.put("searchTag", searchTag);

      if (includeMembers != null) {
        queryParams.put("includeMembers", includeMembers);
        if (includeMembers) {
          if (membersSkip != null) queryParams.put("skip", membersSkip);
          if (membersLimit != null) queryParams.put("limit", membersLimit);
        }
      }

      if (skip != null) queryParams.put("skip", skip);
      if (limit != null) queryParams.put("limit", limit);
      if (sort != null) queryParams.put("sort", sort);

      Call<FetchPublicOrOpenConversationsResult> call =
          retrofitManager.getConversationService().fetchPublicOrOpenConversations(headers, queryParams);

      call.enqueue(new Callback<FetchPublicOrOpenConversationsResult>() {
        @Override
        public void onResponse(@NotNull Call<FetchPublicOrOpenConversationsResult> call,
            @NotNull Response<FetchPublicOrOpenConversationsResult> response) {

          if (response.isSuccessful()) {

            if (response.code() == 200) {
              completionHandler.onComplete(response.body(), null);
            }
          } else {

            ErrorResponse errorResponse;
            IsometrikError.Builder isometrikErrorBuilder;
            try {

              if (response.errorBody() != null) {
                errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
              } else {
                errorResponse = new ErrorResponse();
              }
            } catch (IOException | IllegalStateException | JsonSyntaxException e) {
              // handle failure to read error
              errorResponse = new ErrorResponse();
            }

            isometrikErrorBuilder =
                new IsometrikError.Builder().setHttpResponseCode(response.code())
                    .setRemoteError(true);

            switch (response.code()) {
              case 403:

                isometrikErrorBuilder =
                    baseResponse.handle403responseCode(isometrikErrorBuilder, errorResponse);
                break;
              case 404:

                isometrikErrorBuilder =
                    baseResponse.handle404responseCode(isometrikErrorBuilder, errorResponse);
                break;

              case 422:

                isometrikErrorBuilder =
                    baseResponse.handle422responseCode(isometrikErrorBuilder, errorResponse);
                break;

              case 502:

                isometrikErrorBuilder = baseResponse.handle502responseCode(isometrikErrorBuilder);
                break;

              case 503:
                isometrikErrorBuilder =
                    baseResponse.handle503responseCode(isometrikErrorBuilder, errorResponse);
                break;

              default:
                //500 response code
                isometrikErrorBuilder = baseResponse.handle500responseCode(isometrikErrorBuilder);
            }

            completionHandler.onComplete(null, isometrikErrorBuilder.build());
          }
        }

        @Override
        public void onFailure(@NotNull Call<FetchPublicOrOpenConversationsResult> call,
            @NotNull Throwable t) {
          if (t instanceof IOException) {
            // Network failure
            completionHandler.onComplete(null, baseResponse.handleNetworkError(t));
          } else {
            // Parsing error
            completionHandler.onComplete(null, baseResponse.handleParsingError(t));
          }
        }
      });
    }
  }
}
