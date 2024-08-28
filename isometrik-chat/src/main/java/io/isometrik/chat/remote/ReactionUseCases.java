package io.isometrik.chat.remote;

import com.google.gson.Gson;
import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.builder.reaction.AddReactionQuery;
import io.isometrik.chat.builder.reaction.FetchReactionsQuery;
import io.isometrik.chat.builder.reaction.RemoveReactionQuery;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.models.reaction.AddReaction;
import io.isometrik.chat.models.reaction.FetchReactions;
import io.isometrik.chat.models.reaction.RemoveReaction;
import io.isometrik.chat.response.CompletionHandler;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.reaction.AddReactionResult;
import io.isometrik.chat.response.reaction.FetchReactionsResult;
import io.isometrik.chat.response.reaction.RemoveReactionResult;
import org.jetbrains.annotations.NotNull;

/**
 * The remote use case class containing methods for api calls for reaction operations-
 * AddReaction, FetchReactions and RemoveReaction.
 */
public class ReactionUseCases {

  private final AddReaction addReaction;
  private final FetchReactions fetchReactions;
  private final RemoveReaction removeReaction;

  private final IMConfiguration configuration;
  private final RetrofitManager retrofitManager;
  private final BaseResponse baseResponse;
  private final Gson gson;

  /**
   * Instantiates a new Reaction use cases.
   *
   * @param configuration the configuration
   * @param retrofitManager the retrofit manager
   * @param baseResponse the base response
   * @param gson the gson
   */
  public ReactionUseCases(IMConfiguration configuration, RetrofitManager retrofitManager,
      BaseResponse baseResponse, Gson gson) {
    this.configuration = configuration;
    this.retrofitManager = retrofitManager;
    this.baseResponse = baseResponse;
    this.gson = gson;

    addReaction = new AddReaction();
    fetchReactions = new FetchReactions();
    removeReaction = new RemoveReaction();
  }

  /**
   * Add reaction.
   *
   * @param addReactionQuery the add reaction query
   * @param completionHandler the completion handler
   */
  public void addReaction(@NotNull AddReactionQuery addReactionQuery,
      @NotNull CompletionHandler<AddReactionResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      addReaction.validateParams(addReactionQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch reactions.
   *
   * @param fetchReactionsQuery the fetch reactions query
   * @param completionHandler the completion handler
   */
  public void fetchReactions(@NotNull FetchReactionsQuery fetchReactionsQuery,
      @NotNull CompletionHandler<FetchReactionsResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      fetchReactions.validateParams(fetchReactionsQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove reaction.
   *
   * @param removeReactionQuery the remove reaction query
   * @param completionHandler the completion handler
   */
  public void removeReaction(@NotNull RemoveReactionQuery removeReactionQuery,
      @NotNull CompletionHandler<RemoveReactionResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateRemoteNetworkCallCommonParams(true);
    if (isometrikError == null) {
      removeReaction.validateParams(removeReactionQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }
}
