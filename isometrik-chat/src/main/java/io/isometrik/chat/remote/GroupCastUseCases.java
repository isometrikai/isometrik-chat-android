package io.isometrik.chat.remote;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.deliverystatus.FetchConversationReadStatusQuery;
import io.isometrik.chat.builder.deliverystatus.FetchMessageDeliveryStatusQuery;
import io.isometrik.chat.builder.deliverystatus.FetchMessagePendingDeliveryStatusQuery;
import io.isometrik.chat.builder.deliverystatus.FetchMessagePendingReadStatusQuery;
import io.isometrik.chat.builder.deliverystatus.FetchMessageReadStatusQuery;
import io.isometrik.chat.builder.deliverystatus.FetchMessagesDeliveryReadStatusQuery;
import io.isometrik.chat.builder.groupcast.GroupCastCreateQuery;
import io.isometrik.chat.builder.groupcast.GroupCastMessageQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.models.deliverystatus.FetchConversationReadStatus;
import io.isometrik.chat.models.deliverystatus.FetchMessageDeliveryStatus;
import io.isometrik.chat.models.deliverystatus.FetchMessagePendingDeliveryStatus;
import io.isometrik.chat.models.deliverystatus.FetchMessagePendingReadStatus;
import io.isometrik.chat.models.deliverystatus.FetchMessageReadStatus;
import io.isometrik.chat.models.deliverystatus.FetchMessagesDeliveryReadStatus;
import io.isometrik.chat.models.groupcast.GroupCastCreate;
import io.isometrik.chat.models.groupcast.GroupCastMessage;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.deliverystatus.FetchConversationReadStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessageDeliveryStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessagePendingDeliveryStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessagePendingReadStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessageReadStatusResult;
import io.isometrik.chat.response.deliverystatus.FetchMessagesDeliveryReadStatusResult;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.groupcast.GroupCastCreateResult;
import io.isometrik.chat.response.groupcast.GroupCastMessageResult;

/**
 * The remote use case class containing methods for api calls for delivery status actions-
 * FetchConversationReadStatus, FetchMessageDeliveryStatus, FetchMessagePendingDeliveryStatus,
 * FetchMessagePendingReadStatus, FetchMessageReadStatus, FetchMessagesDeliveryReadStatus.
 */
public class GroupCastUseCases {

  private final GroupCastCreate groupCastCreate;
  private final GroupCastMessage groupCastMessage;
  private final FetchMessagePendingDeliveryStatus fetchMessagePendingDeliveryStatus;
  private final FetchMessagePendingReadStatus fetchMessagePendingReadStatus;
  private final FetchMessageReadStatus fetchMessageReadStatus;
  private final FetchMessagesDeliveryReadStatus fetchMessagesDeliveryReadStatus;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  /**
   * Instantiates a new Delivery status use cases.
   *
   * @param configuration the configuration
   * @param retrofitManager the retrofit manager
   * @param baseResponse the base response
   * @param gson the gson
   */
  public GroupCastUseCases(IMConfiguration configuration, RetrofitManager retrofitManager,
                           BaseResponse baseResponse, Gson gson) {
    this.configuration = configuration;
    this.retrofitManager = retrofitManager;
    this.baseResponse = baseResponse;
    this.gson = gson;

    groupCastCreate = new GroupCastCreate();
    groupCastMessage = new GroupCastMessage();
    fetchMessagePendingDeliveryStatus = new FetchMessagePendingDeliveryStatus();
    fetchMessagePendingReadStatus = new FetchMessagePendingReadStatus();
    fetchMessageReadStatus = new FetchMessageReadStatus();
    fetchMessagesDeliveryReadStatus = new FetchMessagesDeliveryReadStatus();
  }

  /**
   * group Cast Create.
   *
   * @param groupCastCreateQuery the fetch conversation read status query
   * @param completionHandler the completion handler
   */
  public void groupCastCreate(
      @NotNull GroupCastCreateQuery groupCastCreateQuery,
      @NotNull CompletionHandler<GroupCastCreateResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      groupCastCreate.validateParams(groupCastCreateQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch message delivery status.
   *
   * @param groupCastMessageQuery the fetch message delivery status query
   * @param completionHandler the completion handler
   */
  public void groupCastMessage(
      @NotNull GroupCastMessageQuery groupCastMessageQuery,
      @NotNull CompletionHandler<GroupCastMessageResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      groupCastMessage.validateParams(groupCastMessageQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch message pending delivery status.
   *
   * @param fetchMessagePendingDeliveryStatusQuery the fetch message pending delivery status query
   * @param completionHandler the completion handler
   */
  public void fetchMessagePendingDeliveryStatus(
      @NotNull FetchMessagePendingDeliveryStatusQuery fetchMessagePendingDeliveryStatusQuery,
      @NotNull CompletionHandler<FetchMessagePendingDeliveryStatusResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMessagePendingDeliveryStatus.validateParams(fetchMessagePendingDeliveryStatusQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch message pending read status.
   *
   * @param fetchMessagePendingReadStatusQuery the fetch message pending read status query
   * @param completionHandler the completion handler
   */
  public void fetchMessagePendingReadStatus(
      @NotNull FetchMessagePendingReadStatusQuery fetchMessagePendingReadStatusQuery,
      @NotNull CompletionHandler<FetchMessagePendingReadStatusResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMessagePendingReadStatus.validateParams(fetchMessagePendingReadStatusQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch message read status.
   *
   * @param fetchMessageReadStatusQuery the fetch message read status query
   * @param completionHandler the completion handler
   */
  public void fetchMessageReadStatus(
      @NotNull FetchMessageReadStatusQuery fetchMessageReadStatusQuery,
      @NotNull CompletionHandler<FetchMessageReadStatusResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMessageReadStatus.validateParams(fetchMessageReadStatusQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch messages delivery read status.
   *
   * @param fetchMessagesDeliveryReadStatusQuery the fetch messages delivery read status query
   * @param completionHandler the completion handler
   */
  public void fetchMessagesDeliveryReadStatus(
      @NotNull FetchMessagesDeliveryReadStatusQuery fetchMessagesDeliveryReadStatusQuery,
      @NotNull CompletionHandler<FetchMessagesDeliveryReadStatusResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMessagesDeliveryReadStatus.validateParams(fetchMessagesDeliveryReadStatusQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
