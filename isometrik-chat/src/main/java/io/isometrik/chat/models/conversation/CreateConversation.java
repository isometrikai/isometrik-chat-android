package io.isometrik.chat.models.conversation;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.conversation.CreateConversationQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.conversation.CreateConversationResult;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.ErrorResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the create conversation request and expose the parsed successful or error response.
 */
public class CreateConversation {
  /**
   * Validate params.
   *
   * @param createConversationQuery the create conversation query
   * @param completionHandler the completion handler
   * @param retrofitManager the retrofit manager
   * @param imConfiguration the im configuration
   * @param baseResponse the base response
   * @param gson the gson
   */
  public void validateParams(@NotNull CreateConversationQuery createConversationQuery,
      @NotNull final CompletionHandler<CreateConversationResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String conversationTitle = createConversationQuery.getConversationTitle();
    String conversationImageUrl = createConversationQuery.getConversationImageUrl();
    String customType = createConversationQuery.getCustomType();
    Boolean isGroup = createConversationQuery.getGroup();
    List<String> members = createConversationQuery.getMembers();
    Integer conversationType = createConversationQuery.getConversationType();
    JSONObject metaData = createConversationQuery.getMetaData();
    String userToken = createConversationQuery.getUserToken();
    Boolean readEvents = createConversationQuery.getReadEvents();
    Boolean typingEvents = createConversationQuery.getTypingEvents();
    Boolean pushNotifications = createConversationQuery.getPushNotifications();
    List<String> searchableTags = createConversationQuery.getSearchableTags();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (conversationType == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_CONVERSATION_TYPE_MISSING);
    } else if (isGroup == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_IS_GROUP_MISSING);
    } else if (readEvents == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_READ_EVENTS_MISSING);
    } else if (typingEvents == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_TYPING_EVENTS_MISSING);
    } else if (pushNotifications == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_PUSH_NOTIFICATIONS_MISSING);
    } else if (members == null || members.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MEMBERS_MISSING);
    } else if ((conversationType != 0 || isGroup) && (conversationTitle == null || conversationTitle
        .isEmpty())) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_CONVERSATION_TITLE_MISSING);
    } else if ((conversationType != 0 || isGroup) && (conversationImageUrl == null
        || conversationImageUrl.isEmpty())) {
      completionHandler.onComplete(null,
          IsometrikErrorBuilder.IMERROBJ_CONVERSATION_IMAGE_URL_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> bodyParams = new HashMap<>();

      if (metaData != null) bodyParams.put("metaData", JsonParser.parseString(metaData.toString()));
      if (customType != null) bodyParams.put("customType", customType);
      if (searchableTags != null) bodyParams.put("searchableTags", searchableTags);

      bodyParams.put("members", members);
      bodyParams.put("conversationType", conversationType);
      bodyParams.put("isGroup", isGroup);

      bodyParams.put("readEvents", readEvents);
      bodyParams.put("typingEvents", typingEvents);
      bodyParams.put("pushNotifications", pushNotifications);

      if (conversationType != 0 || isGroup) {
        bodyParams.put("conversationTitle", conversationTitle);
        bodyParams.put("conversationImageUrl", conversationImageUrl);
      }

      Call<CreateConversationResult> call =
          retrofitManager.getConversationService().createConversation(headers, bodyParams);

      call.enqueue(new Callback<CreateConversationResult>() {
        @Override
        public void onResponse(@NotNull Call<CreateConversationResult> call, @NotNull Response<CreateConversationResult> response) {

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
                new IsometrikError.Builder().setHttpResponseCode(response.code()).setRemoteError(true);

            switch (response.code()) {

              case 403:

                isometrikErrorBuilder = baseResponse.handle403responseCode(isometrikErrorBuilder, errorResponse);
                break;

              case 404:

                isometrikErrorBuilder = baseResponse.handle404responseCode(isometrikErrorBuilder, errorResponse);
                break;

              case 409:

                isometrikErrorBuilder = baseResponse.handle409responseCode(isometrikErrorBuilder, errorResponse, false);
                break;

              case 422:

                isometrikErrorBuilder = baseResponse.handle422responseCode(isometrikErrorBuilder, errorResponse);
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
        public void onFailure(@NotNull Call<CreateConversationResult> call, @NotNull Throwable t) {
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
