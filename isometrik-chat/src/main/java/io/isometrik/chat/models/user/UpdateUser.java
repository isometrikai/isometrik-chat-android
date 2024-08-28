package io.isometrik.chat.models.user;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.user.UpdateUserQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.ErrorResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import io.isometrik.chat.response.user.UpdateUserResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the update user request and expose the parsed successful or error response.
 */
public class UpdateUser {
  /**
   * Validate params.
   *
   * @param updateUserQuery the update user query
   * @param completionHandler the completion handler
   * @param retrofitManager the retrofit manager
   * @param imConfiguration the im configuration
   * @param baseResponse the base response
   * @param gson the gson
   */
  public void validateParams(@NotNull UpdateUserQuery updateUserQuery,
      @NotNull final CompletionHandler<UpdateUserResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {
    String userIdentifier = updateUserQuery.getUserIdentifier();
    String userProfileImageUrl = updateUserQuery.getUserProfileImageUrl();
    String userName = updateUserQuery.getUserName();
    JSONObject metadata = updateUserQuery.getMetadata();
    String userToken = updateUserQuery.getUserToken();
    Boolean notification = updateUserQuery.getNotification();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (userIdentifier == null
        && userProfileImageUrl == null
        && userName == null
        && metadata == null&& notification==null) {
      completionHandler.onComplete(null,
          IsometrikErrorBuilder.IMERROBJ_UPDATE_USER_DETAILS_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> bodyParams = new HashMap<>();
      if (userIdentifier != null) bodyParams.put("userIdentifier", userIdentifier);
      if (metadata != null) {
        bodyParams.put("metaData", JsonParser.parseString(metadata.toString()));
      }
      if (userProfileImageUrl != null) bodyParams.put("userProfileImageUrl", userProfileImageUrl);
      if (userName != null) bodyParams.put("userName", userName);
      if (notification != null) bodyParams.put("notification", notification);

      Call<UpdateUserResult> call =
          retrofitManager.getUserService().updateUser(headers, bodyParams);

      call.enqueue(new Callback<UpdateUserResult>() {
        @Override
        public void onResponse(@NotNull Call<UpdateUserResult> call,
            @NotNull Response<UpdateUserResult> response) {

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

              case 409:

                isometrikErrorBuilder =
                    baseResponse.handle409responseCode(isometrikErrorBuilder, errorResponse, false);
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
        public void onFailure(@NotNull Call<UpdateUserResult> call, @NotNull Throwable t) {
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
