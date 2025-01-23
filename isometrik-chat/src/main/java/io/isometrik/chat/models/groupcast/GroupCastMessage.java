package io.isometrik.chat.models.groupcast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.groupcast.GroupCastCreateQuery;
import io.isometrik.chat.builder.groupcast.GroupCastMessageQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.ErrorResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;
import io.isometrik.chat.response.groupcast.GroupCastCreateResult;
import io.isometrik.chat.response.groupcast.GroupCastMessageResult;
import io.isometrik.chat.response.message.utils.fetchmessages.MentionedUser;
import io.isometrik.chat.response.message.utils.schemas.Attachment;
import io.isometrik.chat.response.message.utils.schemas.EventForMessage;
import io.isometrik.chat.response.message.utils.validations.AttachmentValidation;
import io.isometrik.chat.response.message.utils.validations.EventForMessageValidation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The helper class to validate request params and make the broadcast message request and expose the parsed successful or error response.
 */
public class GroupCastMessage {
  /**
   * Validate params.
   *
   * @param groupCastMessageQuery the broadcast message query
   * @param completionHandler the completion handler
   * @param retrofitManager the retrofit manager
   * @param imConfiguration the im configuration
   * @param baseResponse the base response
   * @param gson the gson
   */
  public void validateParams(@NotNull GroupCastMessageQuery groupCastMessageQuery,
                             @NotNull final CompletionHandler<GroupCastMessageResult> completionHandler,
                             @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
                             @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    Integer messageType = groupCastMessageQuery.getMessageType();
    Integer deliveryCount = groupCastMessageQuery.getDeliveryCount();
    Boolean showInConversation = groupCastMessageQuery.getShowInConversation();
    Boolean sendPushForNewConversationCreated = groupCastMessageQuery.getSendPushForNewConversationCreated();
    Boolean notifyOnCompletion = groupCastMessageQuery.getNotifyOnCompletion();
    Boolean hideNewConversationsForSender = groupCastMessageQuery.getHideNewConversationsForSender();
    Integer readCount = groupCastMessageQuery.getReadCount();
    String body = groupCastMessageQuery.getBody();
    String notificationTitle = groupCastMessageQuery.getNotificationTitle();
    String notificationBody = groupCastMessageQuery.getNotificationBody();
    Boolean encrypted = groupCastMessageQuery.getEncrypted();
    List<String> userIds = groupCastMessageQuery.getUserIds();
    List<MentionedUser> mentionedUsers = groupCastMessageQuery.getMentionedUsers();
    String deviceId = groupCastMessageQuery.getDeviceId();
    String groupcastId = groupCastMessageQuery.getGroupcastId();
    EventForMessage events = groupCastMessageQuery.getEvents();

    EventForMessage eventForMessage = groupCastMessageQuery.getEventForMessage();
    List<Attachment> attachments = groupCastMessageQuery.getAttachments();

    String customType = groupCastMessageQuery.getCustomType();
    JSONObject metaData = groupCastMessageQuery.getMetaData();
    String userToken = groupCastMessageQuery.getUserToken();
    List<String> searchableTags = groupCastMessageQuery.getSearchableTags();

    if (userToken == null || userToken.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_TOKEN_MISSING);
    } else if (deviceId == null || deviceId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_DEVICEID_MISSING);
    } else if (messageType == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGE_TYPE_MISSING);
    } else if (showInConversation == null) {
      completionHandler.onComplete(null,
              IsometrikErrorBuilder.IMERROBJ_SHOW_IN_CONVERSATION_MISSING);
    } else if (body == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGE_BODY_MISSING);
    } else if (encrypted == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGE_ENCRYPTED_MISSING);
    } else if (userIds == null || userIds.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USER_IDS_MISSING);
    } else if (eventForMessage != null && new EventForMessageValidation().validateEventForMessage(
            eventForMessage)) {
      completionHandler.onComplete(null,
              IsometrikErrorBuilder.IMERROBJ_EVENT_FOR_MESSAGE_INVALID_VALUE);
    } else if (attachments != null && new AttachmentValidation().validateAttachments(attachments)) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_ATTACHMENTS_INVALID_VALUE);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());
      headers.put("appSecret", imConfiguration.getAppSecret());
      headers.put("userToken", userToken);

      Map<String, Object> bodyParams = new HashMap<>();
      bodyParams.put("showInConversation", showInConversation);
      bodyParams.put("sendPushForNewConversationCreated", sendPushForNewConversationCreated);
      bodyParams.put("readCount", readCount);

      bodyParams.put("messageType", messageType);
      bodyParams.put("body", body);
      bodyParams.put("encrypted", encrypted);
      bodyParams.put("userIds", userIds);
      bodyParams.put("deviceId", deviceId);

      if (notifyOnCompletion != null) bodyParams.put("notifyOnCompletion", notifyOnCompletion);
      if (notificationTitle != null) bodyParams.put("notificationTitle", notificationTitle);
      if (notificationBody != null) bodyParams.put("notificationBody", notificationBody);
      if (mentionedUsers != null) bodyParams.put("mentionedUsers", mentionedUsers);
      if (hideNewConversationsForSender != null) bodyParams.put("hideNewConversationsForSender", hideNewConversationsForSender);
      if (groupcastId != null) bodyParams.put("groupcastId", groupcastId);
      if (events != null) bodyParams.put("groupcastId", events);
      if (deliveryCount != null) bodyParams.put("deliveryCount", events);


      if (customType != null) bodyParams.put("customType", customType);
      if (metaData != null) bodyParams.put("metaData", JsonParser.parseString(metaData.toString()));

      if (eventForMessage != null) bodyParams.put("eventForMessage", eventForMessage);
      if (attachments != null) bodyParams.put("attachments", attachments);
      if (searchableTags != null) bodyParams.put("searchableTags", searchableTags);

      Call<GroupCastMessageResult> call =
              retrofitManager.getMessageService().groupCastMessage(headers, bodyParams);

      call.enqueue(new Callback<GroupCastMessageResult>() {
        @Override
        public void onResponse(@NotNull Call<GroupCastMessageResult> call,
                               @NotNull Response<GroupCastMessageResult> response) {

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
        public void onFailure(@NotNull Call<GroupCastMessageResult> call, @NotNull Throwable t) {
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
