package io.isometrik.chat.callbacks;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.events.conversation.CreateConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.ClearConversationEvent;
import io.isometrik.chat.events.conversation.cleanup.DeleteConversationLocallyEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationDetailsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationImageEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationSettingsEvent;
import io.isometrik.chat.events.conversation.config.UpdateConversationTitleEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class for the conversation event callback, with methods for conversation clear,
 * deleted locally, image/settings/title/detail update, conversation created events.
 */
public abstract class ConversationEventCallback {

  /**
   * Conversation cleared.
   *
   * @param isometrik the isometrik
   * @param clearConversationEvent the clear conversation event
   */
  public abstract void conversationCleared(@NotNull Isometrik isometrik,
      @NotNull ClearConversationEvent clearConversationEvent);

  /**
   * Conversation deleted locally.
   *
   * @param isometrik the isometrik
   * @param deleteConversationLocallyEvent the delete conversation locally event
   */
  public abstract void conversationDeletedLocally(@NotNull Isometrik isometrik,
      @NotNull DeleteConversationLocallyEvent deleteConversationLocallyEvent);

  /**
   * Conversation image updated.
   *
   * @param isometrik the isometrik
   * @param updateConversationImageEvent the update conversation image event
   */
  public abstract void conversationImageUpdated(@NotNull Isometrik isometrik,
      @NotNull UpdateConversationImageEvent updateConversationImageEvent);

  /**
   * Conversation settings updated.
   *
   * @param isometrik the isometrik
   * @param updateConversationSettingsEvent the update conversation settings event
   */
  public abstract void conversationSettingsUpdated(@NotNull Isometrik isometrik,
      @NotNull UpdateConversationSettingsEvent updateConversationSettingsEvent);

  /**
   * Conversation details updated.
   *
   * @param isometrik the isometrik
   * @param updateConversationDetailsEvent the update conversation details event
   */
  public abstract void conversationDetailsUpdated(@NotNull Isometrik isometrik,
      @NotNull UpdateConversationDetailsEvent updateConversationDetailsEvent);

  /**
   * Conversation title updated.
   *
   * @param isometrik the isometrik
   * @param updateConversationTitleEvent the update conversation title event
   */
  public abstract void conversationTitleUpdated(@NotNull Isometrik isometrik,
      @NotNull UpdateConversationTitleEvent updateConversationTitleEvent);

  /**
   * Conversation created.
   *
   * @param isometrik the isometrik
   * @param createConversationEvent the create conversation event
   */
  public abstract void conversationCreated(@NotNull Isometrik isometrik,
      @NotNull CreateConversationEvent createConversationEvent);
}
