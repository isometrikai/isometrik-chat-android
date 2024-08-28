package io.isometrik.chat.remote;

import com.google.gson.Gson;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.Isometrik;
import io.isometrik.chat.builder.message.FetchAttachmentPresignedUrlsQuery;
import io.isometrik.chat.builder.message.FetchMentionedMessagesQuery;
import io.isometrik.chat.builder.message.FetchMessagesCountQuery;
import io.isometrik.chat.builder.message.FetchMessagesQuery;
import io.isometrik.chat.builder.message.FetchUnreadMessagesCountQuery;
import io.isometrik.chat.builder.message.FetchUnreadMessagesQuery;
import io.isometrik.chat.builder.message.FetchUserMessagesQuery;
import io.isometrik.chat.builder.message.SendMessageQuery;
import io.isometrik.chat.builder.message.SendTypingMessageQuery;
import io.isometrik.chat.builder.message.UpdateMessageDetailsQuery;
import io.isometrik.chat.builder.message.broadcastforward.BroadcastMessageQuery;
import io.isometrik.chat.builder.message.broadcastforward.ForwardMessageQuery;
import io.isometrik.chat.builder.message.cleanup.RemoveMessagesForEveryoneQuery;
import io.isometrik.chat.builder.message.cleanup.RemoveMessagesForSelfQuery;
import io.isometrik.chat.builder.message.delivery.MarkMessageAsDeliveredQuery;
import io.isometrik.chat.builder.message.delivery.MarkMessageAsReadQuery;
import io.isometrik.chat.builder.message.delivery.MarkMultipleMessagesAsReadQuery;
import io.isometrik.chat.builder.message.delivery.UpdateLastReadInConversationQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.models.message.FetchAttachmentPresignedUrls;
import io.isometrik.chat.models.message.FetchMentionedMessages;
import io.isometrik.chat.models.message.FetchMessages;
import io.isometrik.chat.models.message.FetchMessagesCount;
import io.isometrik.chat.models.message.FetchUnreadMessages;
import io.isometrik.chat.models.message.FetchUnreadMessagesCount;
import io.isometrik.chat.models.message.FetchUserMessages;
import io.isometrik.chat.models.message.SendMessage;
import io.isometrik.chat.models.message.SendTypingMessage;
import io.isometrik.chat.models.message.UpdateMessageDetails;
import io.isometrik.chat.models.message.broadcastforward.BroadcastMessage;
import io.isometrik.chat.models.message.broadcastforward.ForwardMessage;
import io.isometrik.chat.models.message.cleanup.RemoveMessagesForEveryone;
import io.isometrik.chat.models.message.cleanup.RemoveMessagesForSelf;
import io.isometrik.chat.models.message.delivery.MarkMessageAsDelivered;
import io.isometrik.chat.models.message.delivery.MarkMessageAsRead;
import io.isometrik.chat.models.message.delivery.MarkMultipleMessagesAsRead;
import io.isometrik.chat.models.message.delivery.UpdateLastReadInConversation;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.message.FetchAttachmentPresignedUrlsResult;
import io.isometrik.chat.response.message.FetchMentionedMessagesResult;
import io.isometrik.chat.response.message.FetchMessagesCountResult;
import io.isometrik.chat.response.message.FetchMessagesResult;
import io.isometrik.chat.response.message.FetchUnreadMessagesCountResult;
import io.isometrik.chat.response.message.FetchUnreadMessagesResult;
import io.isometrik.chat.response.message.FetchUserMessagesResult;
import io.isometrik.chat.response.message.SendMessageResult;
import io.isometrik.chat.response.message.SendTypingMessageResult;
import io.isometrik.chat.response.message.UpdateMessageDetailsResult;
import io.isometrik.chat.response.message.broadcastforward.BroadcastMessageResult;
import io.isometrik.chat.response.message.broadcastforward.ForwardMessageResult;
import io.isometrik.chat.response.message.cleanup.RemoveMessagesForEveryoneResult;
import io.isometrik.chat.response.message.cleanup.RemoveMessagesForSelfResult;
import io.isometrik.chat.response.message.delivery.MarkMessageAsDeliveredResult;
import io.isometrik.chat.response.message.delivery.MarkMessageAsReadResult;
import io.isometrik.chat.response.message.delivery.MarkMultipleMessagesAsReadResult;
import io.isometrik.chat.response.message.delivery.UpdateLastReadInConversationResult;
import org.jetbrains.annotations.NotNull;

/**
 * The remote use case class containing methods for api calls for message operations-
 * BroadcastMessage, ForwardMessage, RemoveMessagesForEveryone, RemoveMessagesForSelf,
 * MarkMessageAsDelivered, MarkMessageAsRead, MarkMultipleMessagesAsRead,
 * UpdateLastReadInConversation,
 * FetchMessages, FetchMessagesCount, FetchUnreadMessages, FetchUnreadMessagesCount,
 * FetchUserMessages, FetchMentionedMessages, SendMessage, SendTypingMessage,
 * FetchAttachmentPresignedUrls and UpdateMessageDetails.
 */
public class MessageUseCases {

  private final BroadcastMessage broadcastMessage;
  private final ForwardMessage forwardMessage;
  private final RemoveMessagesForEveryone removeMessagesForEveryone;
  private final RemoveMessagesForSelf removeMessagesForSelf;
  private final MarkMessageAsDelivered markMessageAsDelivered;
  private final MarkMessageAsRead markMessageAsRead;
  private final MarkMultipleMessagesAsRead markMultipleMessagesAsRead;
  private final UpdateLastReadInConversation updateLastReadInConversation;
  private final FetchMessages fetchMessages;
  private final FetchMessagesCount fetchMessagesCount;
  private final FetchUnreadMessages fetchUnreadMessages;
  private final FetchUnreadMessagesCount fetchUnreadMessagesCount;
  private final FetchUserMessages fetchUserMessages;
  private final FetchMentionedMessages fetchMentionedMessages;
  private final SendMessage sendMessage;
  private final SendTypingMessage sendTypingMessage;
  private final FetchAttachmentPresignedUrls fetchAttachmentPresignedUrls;
  private final UpdateMessageDetails updateMessageDetails;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Message use cases.
   *
   * @param configuration the configuration
   * @param retrofitManager the retrofit manager
   * @param baseResponse the base response
   * @param gson the gson
   * @param isometrik the isometrik
   */
  public MessageUseCases(IMConfiguration configuration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson, Isometrik isometrik) {
    this.configuration = configuration;
    this.retrofitManager = retrofitManager;
    this.baseResponse = baseResponse;
    this.gson = gson;
    this.isometrik = isometrik;

    broadcastMessage = new BroadcastMessage();
    forwardMessage = new ForwardMessage();
    removeMessagesForEveryone = new RemoveMessagesForEveryone();
    removeMessagesForSelf = new RemoveMessagesForSelf();
    markMessageAsDelivered = new MarkMessageAsDelivered();
    markMessageAsRead = new MarkMessageAsRead();
    markMultipleMessagesAsRead = new MarkMultipleMessagesAsRead();
    updateLastReadInConversation = new UpdateLastReadInConversation();
    fetchMessages = new FetchMessages();
    fetchMessagesCount = new FetchMessagesCount();
    fetchUnreadMessages = new FetchUnreadMessages();
    fetchUnreadMessagesCount = new FetchUnreadMessagesCount();
    fetchUserMessages = new FetchUserMessages();
    fetchMentionedMessages = new FetchMentionedMessages();
    sendMessage = new SendMessage();
    sendTypingMessage = new SendTypingMessage();
    fetchAttachmentPresignedUrls = new FetchAttachmentPresignedUrls();
    updateMessageDetails = new UpdateMessageDetails();
  }

  /**
   * Broadcast message.
   *
   * @param broadcastMessageQuery the broadcast message query
   * @param completionHandler the completion handler
   */
  public void broadcastMessage(@NotNull BroadcastMessageQuery broadcastMessageQuery,
      @NotNull CompletionHandler<BroadcastMessageResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      broadcastMessage.validateParams(broadcastMessageQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Forward message.
   *
   * @param forwardMessageQuery the forward message query
   * @param completionHandler the completion handler
   */
  public void forwardMessage(@NotNull ForwardMessageQuery forwardMessageQuery,
      @NotNull CompletionHandler<ForwardMessageResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      forwardMessage.validateParams(forwardMessageQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove messages for everyone.
   *
   * @param removeMessagesForEveryoneQuery the remove messages for everyone query
   * @param completionHandler the completion handler
   */
  public void removeMessagesForEveryone(
      @NotNull RemoveMessagesForEveryoneQuery removeMessagesForEveryoneQuery,
      @NotNull CompletionHandler<RemoveMessagesForEveryoneResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeMessagesForEveryone.validateParams(removeMessagesForEveryoneQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove messages for self.
   *
   * @param removeMessagesForSelfQuery the remove messages for self query
   * @param completionHandler the completion handler
   */
  public void removeMessagesForSelf(@NotNull RemoveMessagesForSelfQuery removeMessagesForSelfQuery,
      @NotNull CompletionHandler<RemoveMessagesForSelfResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeMessagesForSelf.validateParams(removeMessagesForSelfQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Mark message as delivered.
   *
   * @param markMessageAsDeliveredQuery the mark message as delivered query
   * @param completionHandler the completion handler
   */
  public void markMessageAsDelivered(
      @NotNull MarkMessageAsDeliveredQuery markMessageAsDeliveredQuery,
      @NotNull CompletionHandler<MarkMessageAsDeliveredResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      markMessageAsDelivered.validateParams(markMessageAsDeliveredQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Mark message as read.
   *
   * @param markMessageAsReadQuery the mark message as read query
   * @param completionHandler the completion handler
   */
  public void markMessageAsRead(@NotNull MarkMessageAsReadQuery markMessageAsReadQuery,
      @NotNull CompletionHandler<MarkMessageAsReadResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      markMessageAsRead.validateParams(markMessageAsReadQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Mark multiple messages as read.
   *
   * @param markMultipleMessagesAsReadQuery the mark multiple messages as read query
   * @param completionHandler the completion handler
   */
  public void markMultipleMessagesAsRead(
      @NotNull MarkMultipleMessagesAsReadQuery markMultipleMessagesAsReadQuery,
      @NotNull CompletionHandler<MarkMultipleMessagesAsReadResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      markMultipleMessagesAsRead.validateParams(markMultipleMessagesAsReadQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update last read in conversation.
   *
   * @param updateLastReadInConversationQuery the update last read in conversation query
   * @param completionHandler the completion handler
   */
  public void updateLastReadInConversation(
      @NotNull UpdateLastReadInConversationQuery updateLastReadInConversationQuery,
      @NotNull CompletionHandler<UpdateLastReadInConversationResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateLastReadInConversation.validateParams(updateLastReadInConversationQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch messages.
   *
   * @param fetchMessagesQuery the fetch messages query
   * @param completionHandler the completion handler
   */
  public void fetchMessages(@NotNull FetchMessagesQuery fetchMessagesQuery,
      @NotNull CompletionHandler<FetchMessagesResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMessages.validateParams(fetchMessagesQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson, isometrik);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch messages count.
   *
   * @param fetchMessagesCountQuery the fetch messages count query
   * @param completionHandler the completion handler
   */
  public void fetchMessagesCount(@NotNull FetchMessagesCountQuery fetchMessagesCountQuery,
      @NotNull CompletionHandler<FetchMessagesCountResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMessagesCount.validateParams(fetchMessagesCountQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch unread messages.
   *
   * @param fetchUnreadMessagesQuery the fetch unread messages query
   * @param completionHandler the completion handler
   */
  public void fetchUnreadMessages(@NotNull FetchUnreadMessagesQuery fetchUnreadMessagesQuery,
      @NotNull CompletionHandler<FetchUnreadMessagesResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchUnreadMessages.validateParams(fetchUnreadMessagesQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson, isometrik);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch unread messages count.
   *
   * @param fetchUnreadMessagesCountQuery the fetch unread messages count query
   * @param completionHandler the completion handler
   */
  public void fetchUnreadMessagesCount(
      @NotNull FetchUnreadMessagesCountQuery fetchUnreadMessagesCountQuery,
      @NotNull CompletionHandler<FetchUnreadMessagesCountResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchUnreadMessagesCount.validateParams(fetchUnreadMessagesCountQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch user messages.
   *
   * @param fetchUserMessagesQuery the fetch user messages query
   * @param completionHandler the completion handler
   */
  public void fetchUserMessages(@NotNull FetchUserMessagesQuery fetchUserMessagesQuery,
      @NotNull CompletionHandler<FetchUserMessagesResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchUserMessages.validateParams(fetchUserMessagesQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson, isometrik);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch mentioned messages.
   *
   * @param fetchMentionedMessagesQuery the fetch mentioned messages query
   * @param completionHandler the completion handler
   */
  public void fetchMentionedMessages(
      @NotNull FetchMentionedMessagesQuery fetchMentionedMessagesQuery,
      @NotNull CompletionHandler<FetchMentionedMessagesResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMentionedMessages.validateParams(fetchMentionedMessagesQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Send message.
   *
   * @param sendMessageQuery the send message query
   * @param completionHandler the completion handler
   */
  public void sendMessage(@NotNull SendMessageQuery sendMessageQuery,
      @NotNull CompletionHandler<SendMessageResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      sendMessage.validateParams(sendMessageQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Send typing message.
   *
   * @param sendTypingMessageQuery the send typing message query
   * @param completionHandler the completion handler
   */
  public void sendTypingMessage(@NotNull SendTypingMessageQuery sendTypingMessageQuery,
      @NotNull CompletionHandler<SendTypingMessageResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      sendTypingMessage.validateParams(sendTypingMessageQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch attachment presigned urls.
   *
   * @param getPresignedUrlsQuery the get presigned urls query
   * @param completionHandler the completion handler
   */
  public void fetchAttachmentPresignedUrls(
      @NotNull FetchAttachmentPresignedUrlsQuery getPresignedUrlsQuery,
      @NotNull CompletionHandler<FetchAttachmentPresignedUrlsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchAttachmentPresignedUrls.validateParams(getPresignedUrlsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update message details.
   *
   * @param updateMessageDetailsQuery the update message details query
   * @param completionHandler the completion handler
   */
  public void updateMessageDetails(@NotNull UpdateMessageDetailsQuery updateMessageDetailsQuery,
      @NotNull CompletionHandler<UpdateMessageDetailsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateMessageDetails.validateParams(updateMessageDetailsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
