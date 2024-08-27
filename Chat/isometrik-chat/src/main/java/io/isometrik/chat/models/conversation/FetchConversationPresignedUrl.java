package io.isometrik.chat.models.conversation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.conversation.FetchConversationPresignedUrlQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.conversation.FetchConversationPresignedUrlResult;
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
 * The helper class to validate request params and make the fetch conversation presigned url request and expose the parsed successful or error response.
 */
public class FetchConversationPresignedUrl {

  /**
   * Validate params.
   *
   * @param fetchConversationPresignedUrlQuery the fetch conversation presigned url query
   * @param completionHandler the completion handler
   * @param retrofitManager the retrofit manager
   * @param imConfiguration the im configuration
   * @param baseResponse the base response
   * @param gson the gson
   */
  public void validateParams(
      @NotNull FetchConversationPresignedUrlQuery fetchConversationPresignedUrlQuery,
      @NotNull final CompletionHandler<FetchConversationPresignedUrlResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String conversationTitle = fetchConversationPresignedUrlQuery.getConversationTitle();
    String mediaExtension = fetchConversationPresignedUrlQuery.getMediaExtension();
    String userToken = fetchConversationPresignedUrlQuery.getUserToken();
    String conversationId = fetchConversationPresignedUrlQuery.getConversationId();
    Boolean newConversation = fetchConversationPresignedUrlQuery.getNewConversation();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (newConversation == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_NEW_CONVERSATION_MISSING);
    } else if (newConversation && (conversationTitle == null || conversationTitle.isEmpty())) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_CONVERSATION_TITLE_MISSING);
    } else if (!newConversation && (conversationId == null || conversationId.isEmpty())) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_CONVERSATIONID_MISSING);
    } else if (mediaExtension == null || mediaExtension.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEDIA_EXTENSION_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> queryParams = new HashMap<>();

      queryParams.put("mediaExtension", mediaExtension);
      queryParams.put("newConversation", newConversation);
      if (newConversation) {
        queryParams.put("conversationTitle", conversationTitle);
      } else {
        queryParams.put("conversationId", conversationId);
      }

      Call<FetchConversationPresignedUrlResult> call = retrofitManager.getConversationService()
          .fetchConversationPresignedUrl(headers, queryParams);

      call.enqueue(new Callback<FetchConversationPresignedUrlResult>() {
        @Override
        public void onResponse(@NotNull Call<FetchConversationPresignedUrlResult> call,
            @NotNull Response<FetchConversationPresignedUrlResult> response) {

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
                new IsometrikError.Builder().setHttpResponseCode(response.code()).setRemoteError(true);

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
                isometrikErrorBuilder = baseResponse.handle503responseCode(isometrikErrorBuilder, errorResponse);
                break;

              default:
                //500 response code
                isometrikErrorBuilder = baseResponse.handle500responseCode(isometrikErrorBuilder);
            }

            completionHandler.onComplete(null, isometrikErrorBuilder.build());
          }
        }

        @Override
        public void onFailure(@NotNull Call<FetchConversationPresignedUrlResult> call, @NotNull Throwable t) {
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
