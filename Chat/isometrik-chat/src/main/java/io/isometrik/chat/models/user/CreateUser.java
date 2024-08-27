package io.isometrik.chat.models.user;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.user.CreateUserQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.ErrorResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import io.isometrik.chat.response.user.CreateUserResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the create user request and expose the parsed successful or error response.
 */
public class CreateUser {
  /**
   * Validate params.
   *
   * @param createUserQuery the create user query
   * @param completionHandler the completion handler
   * @param retrofitManager the retrofit manager
   * @param imConfiguration the im configuration
   * @param baseResponse the base response
   * @param gson the gson
   */
  public void validateParams(@NotNull CreateUserQuery createUserQuery,
      @NotNull final CompletionHandler<CreateUserResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {
    String userIdentifier = createUserQuery.getUserIdentifier();
    String password = createUserQuery.getPassword();
    String userProfileImageUrl = createUserQuery.getUserProfileImageUrl();
    String userName = createUserQuery.getUserName();
    JSONObject metadata = createUserQuery.getMetadata();

    if (userIdentifier == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_IDENTIFIER_MISSING);
    } else if (password == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_PASSWORD_MISSING);
    } else if (userProfileImageUrl == null) {
      completionHandler.onComplete(null,
          IsometrikErrorBuilder.IMERROBJ_USER_PROFILE_IMAGEURL_MISSING);
    } else if (userName == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_NAME_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userSecret", imConfiguration.getUserSecret());

      Map<String, Object> bodyParams = new HashMap<>();
      bodyParams.put("userIdentifier", userIdentifier);
      bodyParams.put("password", password);
      bodyParams.put("userProfileImageUrl", userProfileImageUrl);
      bodyParams.put("userName", userName);

      if (metadata != null) {
        bodyParams.put("metaData", JsonParser.parseString(metadata.toString()));
      }
      Call<CreateUserResult> call =
          retrofitManager.getUserService().createUser(headers, bodyParams);

      call.enqueue(new Callback<CreateUserResult>() {
        @Override
        public void onResponse(@NotNull Call<CreateUserResult> call,
            @NotNull Response<CreateUserResult> response) {

          if (response.isSuccessful()) {

            if (response.code() == 201) {
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

              case 401:

                isometrikErrorBuilder =
                    baseResponse.handle401responseCode(isometrikErrorBuilder, errorResponse,false);
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
        public void onFailure(@NotNull Call<CreateUserResult> call, @NotNull Throwable t) {
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
