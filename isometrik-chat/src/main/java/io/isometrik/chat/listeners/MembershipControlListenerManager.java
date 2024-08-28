package io.isometrik.chat.listeners;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.callbacks.MembershipControlEventCallback;
import io.isometrik.chat.events.membershipcontrol.AddAdminEvent;
import io.isometrik.chat.events.membershipcontrol.AddMembersEvent;
import io.isometrik.chat.events.membershipcontrol.JoinConversationEvent;
import io.isometrik.chat.events.membershipcontrol.LeaveConversationEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverJoinEvent;
import io.isometrik.chat.events.membershipcontrol.ObserverLeaveEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveAdminEvent;
import io.isometrik.chat.events.membershipcontrol.RemoveMembersEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The helper class to add/remove listeners which are announced on membership control realtime
 * events and
 * to announce added listeners on membership control events for add admin, add members, join
 * conversation, leave conversation, remove admin, remove members, observer join and observer
 * leave.
 */
public class MembershipControlListenerManager {
  private final List<MembershipControlEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Membership control listener manager.
   *
   * @param isometrikInstance the isometrik instance
   */
  public MembershipControlListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the listener
   */
  public void addListener(MembershipControlEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the listener
   */
  public void removeListener(MembershipControlEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  private List<MembershipControlEventCallback> getListeners() {
    List<MembershipControlEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * Announce add admin event.
   *
   * @param addAdminEvent the add admin event
   */
  public void announce(AddAdminEvent addAdminEvent) {
    for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
      membershipControlEventCallback.adminAdded(this.isometrik, addAdminEvent);
    }
  }

  /**
   * Announce add members event.
   *
   * @param addMembersEvent the add members event
   */
  public void announce(AddMembersEvent addMembersEvent) {
    for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
      membershipControlEventCallback.membersAdded(this.isometrik, addMembersEvent);
    }
  }

  /**
   * Announce join conversation event.
   *
   * @param joinConversationEvent the join conversation event
   */
  public void announce(JoinConversationEvent joinConversationEvent) {
    for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
      membershipControlEventCallback.conversationJoined(this.isometrik, joinConversationEvent);
    }
  }

  /**
   * Announce leave conversation event.
   *
   * @param leaveConversationEvent the leave conversation event
   */
  public void announce(LeaveConversationEvent leaveConversationEvent) {
    for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
      membershipControlEventCallback.conversationLeft(this.isometrik, leaveConversationEvent);
    }
  }

  /**
   * Announce remove admin event.
   *
   * @param removeAdminEvent the remove admin event
   */
  public void announce(RemoveAdminEvent removeAdminEvent) {
    for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
      membershipControlEventCallback.adminRemoved(this.isometrik, removeAdminEvent);
    }
  }

  /**
   * Announce remove members event.
   *
   * @param removeMembersEvent the remove members event
   */
  public void announce(RemoveMembersEvent removeMembersEvent) {
    for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
      membershipControlEventCallback.membersRemoved(this.isometrik, removeMembersEvent);
    }
  }

  /**
   * Announce observer join event.
   *
   * @param observerJoinEvent the observer join event
   */
  public void announce(ObserverJoinEvent observerJoinEvent) {
    for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
      membershipControlEventCallback.observerJoined(this.isometrik, observerJoinEvent);
    }
  }

  /**
   * Announce observer leave event.
   *
   * @param observerLeaveEvent the observer leave event
   */
  public void announce(ObserverLeaveEvent observerLeaveEvent) {
    for (MembershipControlEventCallback membershipControlEventCallback : getListeners()) {
      membershipControlEventCallback.observerLeft(this.isometrik, observerLeaveEvent);
    }
  }
}
