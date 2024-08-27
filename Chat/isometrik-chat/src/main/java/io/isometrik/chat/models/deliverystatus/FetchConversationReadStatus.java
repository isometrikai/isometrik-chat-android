package io.isometrik.chat.models.deliverystatus;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.deliverystatus.FetchConversationReadStatusQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.deliverystatus.FetchConversationReadStatusResult;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.ErrorResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the fetch conversation read status request and expose the parsed successful or error response.
 */
public class FetchConversationReadStatus {
  /**
   * Validate params.
   *
   * @param fetchConversationReadStatusQuery the fetch conversation read status query
   * @param completionHandler the completion handler
   * @param retrofitManager the retrofit manager
   * @param imConfiguration the im configuration
   * @param baseResponse the base response
   * @param gson the gson
   */
  public void validateParams(
      @NotNull FetchConversationReadStatusQuery fetchConversationReadStatusQuery,
      @NotNull final CompletionHandler<FetchConversationReadStatusResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String conversationId = fetchConversationReadStatusQuery.getConversationId();
    Integer limit = fetchConversationReadStatusQuery.getLimit();
    Integer skip = fetchConversationReadStatusQuery.getSkip();
    String userToken = fetchConversationReadStatusQuery.getUserToken();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (conversationId == null || conversationId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_CONVERSATIONID_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> queryParams = new HashMap<>();
      queryParams.put("conversationId", conversationId);
      if (skip != null) queryParams.put("skip", skip);
      if (limit != null) queryParams.put("limit", limit);

      Call<FetchConversationReadStatusResult> call = retrofitManager.getDeliveryStatusService()
          .fetchConversationReadStatus(headers, queryParams);

      call.enqueue(new Callback<FetchConversationReadStatusResult>() {
        @Override
        public void onResponse(@NotNull Call<FetchConversationReadStatusResult> call,
            @NotNull Response<FetchConversationReadStatusResult> response) {

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

              case 400:

                isometrikErrorBuilder =
                    baseResponse.handle400responseCode(isometrikErrorBuilder, errorResponse, false);
                break;
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
        public void onFailure(@NotNull Call<FetchConversationReadStatusResult> call,
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
