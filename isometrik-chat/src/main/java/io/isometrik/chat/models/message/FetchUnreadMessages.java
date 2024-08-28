package io.isometrik.chat.models.message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.message.FetchUnreadMessagesQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.models.message.delivery.operations.MarkMessagesAsDeliveredUtil;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.ErrorResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import io.isometrik.chat.response.message.FetchUnreadMessagesResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the fetch unread messages request and expose the parsed successful or error response.
 */
public class FetchUnreadMessages {
  /**
   * Validate params.
   *
   * @param fetchUnreadMessagesQuery the fetch unread messages query
   * @param completionHandler the completion handler
   * @param retrofitManager the retrofit manager
   * @param imConfiguration the im configuration
   * @param baseResponse the base response
   * @param gson the gson
   * @param isometrik the isometrik
   */
  public void validateParams(@NotNull FetchUnreadMessagesQuery fetchUnreadMessagesQuery,
      @NotNull final CompletionHandler<FetchUnreadMessagesResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson,
      final @NotNull Isometrik isometrik) {
    Integer sort = fetchUnreadMessagesQuery.getSort();
    Integer limit = fetchUnreadMessagesQuery.getLimit();
    Integer skip = fetchUnreadMessagesQuery.getSkip();
    String conversationId = fetchUnreadMessagesQuery.getConversationId();
    String userToken = fetchUnreadMessagesQuery.getUserToken();

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
      if (sort != null) queryParams.put("sort", sort);

      Call<FetchUnreadMessagesResult> call =
          retrofitManager.getMessageService().fetchUnreadMessages(headers, queryParams);

      call.enqueue(new Callback<FetchUnreadMessagesResult>() {
        @Override
        public void onResponse(@NotNull Call<FetchUnreadMessagesResult> call,
            @NotNull Response<FetchUnreadMessagesResult> response) {

          if (response.isSuccessful()) {

            if (response.code() == 200) {
              completionHandler.onComplete(response.body(), null);
              if (response.body() != null) {
                try{
                MarkMessagesAsDeliveredUtil.markMessagesAsDelivered(
                    response.body().getMessages(), userToken, imConfiguration.getClientId().substring(0, 24),
                    isometrik);
                }catch(NullPointerException ignore){}
              }
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
        public void onFailure(@NotNull Call<FetchUnreadMessagesResult> call, @NotNull Throwable t) {
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
