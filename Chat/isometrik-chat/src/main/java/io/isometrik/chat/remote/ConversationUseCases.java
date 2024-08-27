package io.isometrik.chat.remote;

import com.google.gson.Gson;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.conversation.CreateConversationQuery;
import io.isometrik.chat.builder.conversation.FetchConversationDetailsQuery;
import io.isometrik.chat.builder.conversation.FetchConversationMembersQuery;
import io.isometrik.chat.builder.conversation.FetchConversationMessagingStatusQuery;
import io.isometrik.chat.builder.conversation.FetchConversationPresignedUrlQuery;
import io.isometrik.chat.builder.conversation.FetchConversationWatchersQuery;
import io.isometrik.chat.builder.conversation.FetchConversationsCountQuery;
import io.isometrik.chat.builder.conversation.FetchConversationsQuery;
import io.isometrik.chat.builder.conversation.FetchPublicOrOpenConversationsQuery;
import io.isometrik.chat.builder.conversation.FetchUnreadConversationsCountQuery;
import io.isometrik.chat.builder.conversation.cleanup.ClearConversationQuery;
import io.isometrik.chat.builder.conversation.cleanup.DeleteConversationLocallyQuery;
import io.isometrik.chat.builder.conversation.config.UpdateConversationDetailsQuery;
import io.isometrik.chat.builder.conversation.config.UpdateConversationImageQuery;
import io.isometrik.chat.builder.conversation.config.UpdateConversationSettingsQuery;
import io.isometrik.chat.builder.conversation.config.UpdateConversationTitleQuery;
import io.isometrik.chat.builder.membershipcontrol.FetchMembersToAddToConversationQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.models.conversation.CreateConversation;
import io.isometrik.chat.models.conversation.FetchConversationDetails;
import io.isometrik.chat.models.conversation.FetchConversationMembers;
import io.isometrik.chat.models.conversation.FetchConversationMessagingStatus;
import io.isometrik.chat.models.conversation.FetchConversationPresignedUrl;
import io.isometrik.chat.models.conversation.FetchConversationWatchers;
import io.isometrik.chat.models.conversation.FetchConversations;
import io.isometrik.chat.models.conversation.FetchConversationsCount;
import io.isometrik.chat.models.conversation.FetchMembersToAddToConversation;
import io.isometrik.chat.models.conversation.FetchPublicOrOpenConversations;
import io.isometrik.chat.models.conversation.FetchUnreadConversationsCount;
import io.isometrik.chat.models.conversation.cleanup.ClearConversation;
import io.isometrik.chat.models.conversation.cleanup.DeleteConversationLocally;
import io.isometrik.chat.models.conversation.config.UpdateConversationDetails;
import io.isometrik.chat.models.conversation.config.UpdateConversationImage;
import io.isometrik.chat.models.conversation.config.UpdateConversationSettings;
import io.isometrik.chat.models.conversation.config.UpdateConversationTitle;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.conversation.CreateConversationResult;
import io.isometrik.chat.response.conversation.FetchConversationDetailsResult;
import io.isometrik.chat.response.conversation.FetchConversationMembersResult;
import io.isometrik.chat.response.conversation.FetchConversationMessagingStatusResult;
import io.isometrik.chat.response.conversation.FetchConversationPresignedUrlResult;
import io.isometrik.chat.response.conversation.FetchConversationWatchersResult;
import io.isometrik.chat.response.conversation.FetchConversationsCountResult;
import io.isometrik.chat.response.conversation.FetchConversationsResult;
import io.isometrik.chat.response.conversation.FetchPublicOrOpenConversationsResult;
import io.isometrik.chat.response.conversation.FetchUnreadConversationsCountResult;
import io.isometrik.chat.response.conversation.cleanup.ClearConversationResult;
import io.isometrik.chat.response.conversation.cleanup.DeleteConversationLocallyResult;
import io.isometrik.chat.response.conversation.config.UpdateConversationDetailsResult;
import io.isometrik.chat.response.conversation.config.UpdateConversationImageResult;
import io.isometrik.chat.response.conversation.config.UpdateConversationSettingsResult;
import io.isometrik.chat.response.conversation.config.UpdateConversationTitleResult;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.membershipcontrol.FetchMembersToAddToConversationResult;
import org.jetbrains.annotations.NotNull;

/**
 * The remote use case class containing methods for api calls for conversation operations-
 * ClearConversation, DeleteConversationLocally,
 * UpdateConversationImage, UpdateConversationSettings, UpdateConversationTitle,
 * UpdateConversationDetails, CreateConversation, FetchConversationDetails,
 * FetchConversationMembers, FetchMembersToAddToConversation, FetchConversations,
 * FetchConversationsCount, FetchConversationWatchers, FetchUnreadConversationsCount,
 * FetchConversationPresignedUrl, FetchPublicOrOpenConversations and
 * FetchConversationMessagingStatus.
 */
public class ConversationUseCases {
  private final ClearConversation clearConversation;
  private final DeleteConversationLocally deleteConversationLocally;
  private final UpdateConversationImage updateConversationImage;
  private final UpdateConversationSettings updateConversationSettings;
  private final UpdateConversationTitle updateConversationTitle;
  private final UpdateConversationDetails updateConversationDetails;
  private final CreateConversation createConversation;
  private final FetchConversationDetails fetchConversationDetails;
  private final FetchConversationMembers fetchConversationMembers;
  private final FetchMembersToAddToConversation fetchMembersToAddToConversation;
  private final FetchConversations fetchConversations;
  private final FetchConversationsCount fetchConversationsCount;
  private final FetchConversationWatchers fetchConversationWatchers;
  private final FetchUnreadConversationsCount fetchUnreadConversationsCount;
  private final FetchConversationPresignedUrl fetchConversationPresignedUrl;
  private final FetchPublicOrOpenConversations fetchPublicConversations;
  private final FetchConversationMessagingStatus fetchConversationMessagingStatus;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  /**
   * Instantiates a new Conversation use cases.
   *
   * @param configuration the configuration
   * @param retrofitManager the retrofit manager
   * @param baseResponse the base response
   * @param gson the gson
   */
  public ConversationUseCases(IMConfiguration configuration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    this.configuration = configuration;
    this.retrofitManager = retrofitManager;
    this.baseResponse = baseResponse;
    this.gson = gson;

    clearConversation = new ClearConversation();
    deleteConversationLocally = new DeleteConversationLocally();
    updateConversationImage = new UpdateConversationImage();
    updateConversationSettings = new UpdateConversationSettings();
    updateConversationTitle = new UpdateConversationTitle();
    updateConversationDetails = new UpdateConversationDetails();
    createConversation = new CreateConversation();
    fetchConversationDetails = new FetchConversationDetails();
    fetchConversationMembers = new FetchConversationMembers();
    fetchMembersToAddToConversation = new FetchMembersToAddToConversation();
    fetchConversations = new FetchConversations();
    fetchConversationsCount = new FetchConversationsCount();
    fetchConversationWatchers = new FetchConversationWatchers();
    fetchUnreadConversationsCount = new FetchUnreadConversationsCount();
    fetchConversationPresignedUrl = new FetchConversationPresignedUrl();
    fetchPublicConversations = new FetchPublicOrOpenConversations();
    fetchConversationMessagingStatus = new FetchConversationMessagingStatus();
  }

  /**
   * Clear conversation.
   *
   * @param clearConversationQuery the clear conversation query
   * @param completionHandler the completion handler
   */
  public void clearConversation(@NotNull ClearConversationQuery clearConversationQuery,
      @NotNull CompletionHandler<ClearConversationResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      clearConversation.validateParams(clearConversationQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Delete conversation locally.
   *
   * @param deleteConversationLocallyQuery the delete conversation locally query
   * @param completionHandler the completion handler
   */
  public void deleteConversationLocally(
      @NotNull DeleteConversationLocallyQuery deleteConversationLocallyQuery,
      @NotNull CompletionHandler<DeleteConversationLocallyResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      deleteConversationLocally.validateParams(deleteConversationLocallyQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update conversation image.
   *
   * @param updateConversationImageQuery the update conversation image query
   * @param completionHandler the completion handler
   */
  public void updateConversationImage(
      @NotNull UpdateConversationImageQuery updateConversationImageQuery,
      @NotNull CompletionHandler<UpdateConversationImageResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateConversationImage.validateParams(updateConversationImageQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update conversation settings.
   *
   * @param updateConversationSettingsQuery the update conversation settings query
   * @param completionHandler the completion handler
   */
  public void updateConversationSettings(
      @NotNull UpdateConversationSettingsQuery updateConversationSettingsQuery,
      @NotNull CompletionHandler<UpdateConversationSettingsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateConversationSettings.validateParams(updateConversationSettingsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update conversation title.
   *
   * @param updateConversationTitleQuery the update conversation title query
   * @param completionHandler the completion handler
   */
  public void updateConversationTitle(
      @NotNull UpdateConversationTitleQuery updateConversationTitleQuery,
      @NotNull CompletionHandler<UpdateConversationTitleResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateConversationTitle.validateParams(updateConversationTitleQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update conversation details.
   *
   * @param updateConversationDetailsQuery the update conversation details query
   * @param completionHandler the completion handler
   */
  public void updateConversationDetails(
      @NotNull UpdateConversationDetailsQuery updateConversationDetailsQuery,
      @NotNull CompletionHandler<UpdateConversationDetailsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      updateConversationDetails.validateParams(updateConversationDetailsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Create conversation.
   *
   * @param createConversationQuery the create conversation query
   * @param completionHandler the completion handler
   */
  public void createConversation(@NotNull CreateConversationQuery createConversationQuery,
      @NotNull CompletionHandler<CreateConversationResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      createConversation.validateParams(createConversationQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch conversation details.
   *
   * @param fetchConversationDetailsQuery the fetch conversation details query
   * @param completionHandler the completion handler
   */
  public void fetchConversationDetails(
      @NotNull FetchConversationDetailsQuery fetchConversationDetailsQuery,
      @NotNull CompletionHandler<FetchConversationDetailsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchConversationDetails.validateParams(fetchConversationDetailsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch conversation members.
   *
   * @param fetchConversationMembersQuery the fetch conversation members query
   * @param completionHandler the completion handler
   */
  public void fetchConversationMembers(
      @NotNull FetchConversationMembersQuery fetchConversationMembersQuery,
      @NotNull CompletionHandler<FetchConversationMembersResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchConversationMembers.validateParams(fetchConversationMembersQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch members to add to conversation.
   *
   * @param fetchMembersToAddToConversationQuery the fetch members to add to conversation query
   * @param completionHandler the completion handler
   */
  public void fetchMembersToAddToConversation(
      @NotNull FetchMembersToAddToConversationQuery fetchMembersToAddToConversationQuery,
      @NotNull CompletionHandler<FetchMembersToAddToConversationResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchMembersToAddToConversation.validateParams(fetchMembersToAddToConversationQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch conversations.
   *
   * @param fetchConversationsQuery the fetch conversations query
   * @param completionHandler the completion handler
   */
  public void fetchConversations(@NotNull FetchConversationsQuery fetchConversationsQuery,
      @NotNull CompletionHandler<FetchConversationsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchConversations.validateParams(fetchConversationsQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch conversations count.
   *
   * @param fetchConversationsCountQuery the fetch conversations count query
   * @param completionHandler the completion handler
   */
  public void fetchConversationsCount(
      @NotNull FetchConversationsCountQuery fetchConversationsCountQuery,
      @NotNull CompletionHandler<FetchConversationsCountResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchConversationsCount.validateParams(fetchConversationsCountQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch conversation watchers.
   *
   * @param fetchConversationWatchersQuery the fetch conversation watchers query
   * @param completionHandler the completion handler
   */
  public void fetchConversationWatchers(
      @NotNull FetchConversationWatchersQuery fetchConversationWatchersQuery,
      @NotNull CompletionHandler<FetchConversationWatchersResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchConversationWatchers.validateParams(fetchConversationWatchersQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch unread conversations count.
   *
   * @param fetchUnreadConversationsCountQuery the fetch unread conversations count query
   * @param completionHandler the completion handler
   */
  public void fetchUnreadConversationsCount(
      @NotNull FetchUnreadConversationsCountQuery fetchUnreadConversationsCountQuery,
      @NotNull CompletionHandler<FetchUnreadConversationsCountResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchUnreadConversationsCount.validateParams(fetchUnreadConversationsCountQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch conversation presigned url.
   *
   * @param fetchConversationPresignedUrlQuery the fetch conversation presigned url query
   * @param completionHandler the completion handler
   */
  public void fetchConversationPresignedUrl(
      @NotNull FetchConversationPresignedUrlQuery fetchConversationPresignedUrlQuery,
      @NotNull CompletionHandler<FetchConversationPresignedUrlResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchConversationPresignedUrl.validateParams(fetchConversationPresignedUrlQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch public or open conversations.
   *
   * @param fetchPublicOrOpenConversationsQuery the fetch public or open conversations query
   * @param completionHandler the completion handler
   */
  public void fetchPublicOrOpenConversations(
      @NotNull FetchPublicOrOpenConversationsQuery fetchPublicOrOpenConversationsQuery,
      @NotNull CompletionHandler<FetchPublicOrOpenConversationsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchPublicConversations.validateParams(fetchPublicOrOpenConversationsQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch conversation messaging status.
   *
   * @param fetchConversationMessagingStatusQuery the fetch conversation messaging status query
   * @param completionHandler the completion handler
   */
  public void fetchConversationMessagingStatus(
      @NotNull FetchConversationMessagingStatusQuery fetchConversationMessagingStatusQuery,
      @NotNull CompletionHandler<FetchConversationMessagingStatusResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchConversationMessagingStatus.validateParams(fetchConversationMessagingStatusQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
