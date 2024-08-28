package io.isometrik.chat.callbacks;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.events.membershipcontrol.AddAdminEvent;
import io.isometrik.chat.events.membershipcontrol.AddMembersEvent;
import io.isometrik.chat.events.membershipcontrol.JoinConversationEvent;
import io.isometrik.chat.events.membershipcontrol.LeaveConversationEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverJoinEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverLeaveEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveAdminEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveMembersEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The abstract class for the membership control event callback, with methods for admin add/remove,
 * observer join/leave, user join/leave member add/remove events.
 */
public abstract class MembershipControlEventCallback {

  /**
   * Admin added.
   *
   * @param isometrik the isometrik
   * @param addAdminEvent the add admin event
   */
  public abstract void adminAdded(@NotNull Isometrik isometrik,
      @NotNull AddAdminEvent addAdminEvent);

  /**
   * Members added.
   *
   * @param isometrik the isometrik
   * @param addMembersEvent the add members event
   */
  public abstract void membersAdded(@NotNull Isometrik isometrik,
      @NotNull AddMembersEvent addMembersEvent);

  /**
   * Conversation joined.
   *
   * @param isometrik the isometrik
   * @param joinConversationEvent the join conversation event
   */
  public abstract void conversationJoined(@NotNull Isometrik isometrik,
      @NotNull JoinConversationEvent joinConversationEvent);

  /**
   * Conversation left.
   *
   * @param isometrik the isometrik
   * @param leaveConversationEvent the leave conversation event
   */
  public abstract void conversationLeft(@NotNull Isometrik isometrik,
      @NotNull LeaveConversationEvent leaveConversationEvent);

  /**
   * Admin removed.
   *
   * @param isometrik the isometrik
   * @param removeAdminEvent the remove admin event
   */
  public abstract void adminRemoved(@NotNull Isometrik isometrik,
      @NotNull RemoveAdminEvent removeAdminEvent);

  /**
   * Members removed.
   *
   * @param isometrik the isometrik
   * @param removeMembersEvent the remove members event
   */
  public abstract void membersRemoved(@NotNull Isometrik isometrik,
      @NotNull RemoveMembersEvent removeMembersEvent);

  /**
   * Observer joined.
   *
   * @param isometrik the isometrik
   * @param observerJoinEvent the observer join event
   */
  public abstract void observerJoined(@NotNull Isometrik isometrik,
      @NotNull ObserverJoinEvent observerJoinEvent);

  /**
   * Observer left.
   *
   * @param isometrik the isometrik
   * @param observerLeaveEvent the observer leave event
   */
  public abstract void observerLeft(@NotNull Isometrik isometrik,
      @NotNull ObserverLeaveEvent observerLeaveEvent);
}
