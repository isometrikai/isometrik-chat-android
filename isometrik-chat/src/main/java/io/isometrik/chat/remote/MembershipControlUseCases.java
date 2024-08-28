package io.isometrik.chat.remote;

import com.google.gson.Gson;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.conversation.FetchConversationMemberDetailsQuery;
import io.isometrik.chat.builder.membershipcontrol.AddAdminQuery;
import io.isometrik.chat.builder.membershipcontrol.AddMembersQuery;
import io.isometrik.chat.builder.membershipcontrol.FetchObserversQuery;
import io.isometrik.chat.builder.membershipcontrol.JoinAsObserverQuery;
import io.isometrik.chat.builder.membershipcontrol.JoinConversationQuery;
import io.isometrik.chat.builder.membershipcontrol.LeaveAsObserverQuery;
import io.isometrik.chat.builder.membershipcontrol.LeaveConversationQuery;
import io.isometrik.chat.builder.membershipcontrol.RemoveAdminQuery;
import io.isometrik.chat.builder.membershipcontrol.RemoveMembersQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.models.conversation.FetchConversationMemberDetails;
import io.isometrik.chat.models.membershipcontrol.AddAdmin;
import io.isometrik.chat.models.membershipcontrol.AddMembers;
import io.isometrik.chat.models.membershipcontrol.FetchObservers;
import io.isometrik.chat.models.membershipcontrol.JoinAsObserver;
import io.isometrik.chat.models.membershipcontrol.JoinConversation;
import io.isometrik.chat.models.membershipcontrol.LeaveAsObserver;
import io.isometrik.chat.models.membershipcontrol.LeaveConversation;
import io.isometrik.chat.models.membershipcontrol.RemoveAdmin;
import io.isometrik.chat.models.membershipcontrol.RemoveMembers;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.conversation.FetchConversationMemberDetailsResult;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.membershipcontrol.AddAdminResult;
import io.isometrik.chat.response.membershipcontrol.AddMembersResult;
import io.isometrik.chat.response.membershipcontrol.FetchObserversResult;
import io.isometrik.chat.response.membershipcontrol.JoinAsObserverResult;
import io.isometrik.chat.response.membershipcontrol.JoinConversationResult;
import io.isometrik.chat.response.membershipcontrol.LeaveAsObserverResult;
import io.isometrik.chat.response.membershipcontrol.LeaveConversationResult;
import io.isometrik.chat.response.membershipcontrol.RemoveAdminResult;
import io.isometrik.chat.response.membershipcontrol.RemoveMembersResult;
import org.jetbrains.annotations.NotNull;

/**
 * The remote use case class containing methods for api calls for membership control operations-
 * AddAdmin, AddMembers, JoinConversation, LeaveConversation, RemoveAdmin,RemoveMembers,
 * FetchConversationMemberDetails, JoinAsObserver, LeaveAsObserver and FetchObservers.
 */
public class MembershipControlUseCases {

  private final AddAdmin addAdmin;
  private final AddMembers addMembers;
  private final JoinConversation joinConversation;
  private final LeaveConversation leaveConversation;
  private final RemoveAdmin removeAdmin;
  private final RemoveMembers removeMembers;
  private final FetchConversationMemberDetails fetchConversationMemberDetails;
  private final JoinAsObserver joinAsObserver;
  private final LeaveAsObserver leaveAsObserver;
  private final FetchObservers fetchObservers;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  /**
   * Instantiates a new Membership control use cases.
   *
   * @param configuration the configuration
   * @param retrofitManager the retrofit manager
   * @param baseResponse the base response
   * @param gson the gson
   */
  public MembershipControlUseCases(IMConfiguration configuration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    this.configuration = configuration;
    this.retrofitManager = retrofitManager;
    this.baseResponse = baseResponse;
    this.gson = gson;

    addAdmin = new AddAdmin();
    addMembers = new AddMembers();
    joinConversation = new JoinConversation();
    leaveConversation = new LeaveConversation();
    removeAdmin = new RemoveAdmin();
    removeMembers = new RemoveMembers();
    fetchConversationMemberDetails = new FetchConversationMemberDetails();
    joinAsObserver = new JoinAsObserver();
    leaveAsObserver = new LeaveAsObserver();
    fetchObservers = new FetchObservers();
  }

  /**
   * Add admin.
   *
   * @param addAdminQuery the add admin query
   * @param completionHandler the completion handler
   */
  public void addAdmin(@NotNull AddAdminQuery addAdminQuery,
      @NotNull CompletionHandler<AddAdminResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addAdmin.validateParams(addAdminQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Add members.
   *
   * @param addMembersQuery the add members query
   * @param completionHandler the completion handler
   */
  public void addMembers(@NotNull AddMembersQuery addMembersQuery,
      @NotNull CompletionHandler<AddMembersResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addMembers.validateParams(addMembersQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Join conversation.
   *
   * @param joinConversationQuery the join conversation query
   * @param completionHandler the completion handler
   */
  public void joinConversation(@NotNull JoinConversationQuery joinConversationQuery,
      @NotNull CompletionHandler<JoinConversationResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      joinConversation.validateParams(joinConversationQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Leave conversation.
   *
   * @param leaveConversationQuery the leave conversation query
   * @param completionHandler the completion handler
   */
  public void leaveConversation(@NotNull LeaveConversationQuery leaveConversationQuery,
      @NotNull CompletionHandler<LeaveConversationResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      leaveConversation.validateParams(leaveConversationQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove admin.
   *
   * @param removeAdminQuery the remove admin query
   * @param completionHandler the completion handler
   */
  public void removeAdmin(@NotNull RemoveAdminQuery removeAdminQuery,
      @NotNull CompletionHandler<RemoveAdminResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeAdmin.validateParams(removeAdminQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove members.
   *
   * @param removeMembersQuery the remove members query
   * @param completionHandler the completion handler
   */
  public void removeMembers(@NotNull RemoveMembersQuery removeMembersQuery,
      @NotNull CompletionHandler<RemoveMembersResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeMembers.validateParams(removeMembersQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch conversation member details.
   *
   * @param fetchConversationMemberDetailsQuery the fetch conversation member details query
   * @param completionHandler the completion handler
   */
  public void fetchConversationMemberDetails(
      @NotNull FetchConversationMemberDetailsQuery fetchConversationMemberDetailsQuery,
      @NotNull CompletionHandler<FetchConversationMemberDetailsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchConversationMemberDetails.validateParams(fetchConversationMemberDetailsQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Join as observer.
   *
   * @param joinAsObserverQuery the join as observer query
   * @param completionHandler the completion handler
   */
  public void joinAsObserver(@NotNull JoinAsObserverQuery joinAsObserverQuery,
      @NotNull CompletionHandler<JoinAsObserverResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      joinAsObserver.validateParams(joinAsObserverQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Leave as observer.
   *
   * @param leaveAsObserverQuery the leave as observer query
   * @param completionHandler the completion handler
   */
  public void leaveAsObserver(@NotNull LeaveAsObserverQuery leaveAsObserverQuery,
      @NotNull CompletionHandler<LeaveAsObserverResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      leaveAsObserver.validateParams(leaveAsObserverQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch observers.
   *
   * @param fetchObserversQuery the fetch observers query
   * @param completionHandler the completion handler
   */
  public void fetchObservers(@NotNull FetchObserversQuery fetchObserversQuery,
      @NotNull CompletionHandler<FetchObserversResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchObservers.validateParams(fetchObserversQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
