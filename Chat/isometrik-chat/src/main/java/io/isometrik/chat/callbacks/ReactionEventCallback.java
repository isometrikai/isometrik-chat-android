package io.isometrik.chat.callbacks;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.events.reaction.AddReactionEvent;
import io.isometrik.chat.events.reaction.RemoveReactionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class for the reaction event callback, with methods for reaction add/remove events.
 */
public abstract class ReactionEventCallback {

  /**
   * Reaction added.
   *
   * @param isometrik the isometrik
   * @param addReactionEvent the add reaction event
   */
  public abstract void reactionAdded(@NotNull Isometrik isometrik,
      @NotNull AddReactionEvent addReactionEvent);

  /**
   * Reaction removed.
   *
   * @param isometrik the isometrik
   * @param removeReactionEvent the remove reaction event
   */
  public abstract void reactionRemoved(@NotNull Isometrik isometrik,
      @NotNull RemoveReactionEvent removeReactionEvent);
}
