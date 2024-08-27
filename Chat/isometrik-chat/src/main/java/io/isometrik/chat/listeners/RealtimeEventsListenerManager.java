package io.isometrik.chat.listeners;

import io.isometrik.chat.Isometrik;

/**
 * Class for registering/unregistering listener for realtime events for connection, conversation,
 * membership control, message, reaction and user.
 */
public class RealtimeEventsListenerManager {

  private final ConnectionListenerManager connectionListenerManager;
  private final ConversationListenerManager conversationListenerManager;
  private final MembershipControlListenerManager membershipControlListenerManager;
  private final MessageListenerManager messageListenerManager;
  private final ReactionListenerManager reactionListenerManager;
  private final UserListenerManager userListenerManager;

  /**
   * Instantiates a new Realtime events listener manager.
   *
   * @param isometrik the isometrik
   */
  public RealtimeEventsListenerManager(Isometrik isometrik) {
    connectionListenerManager = new ConnectionListenerManager(isometrik);
    conversationListenerManager = new ConversationListenerManager(isometrik);
    membershipControlListenerManager = new MembershipControlListenerManager(isometrik);
    messageListenerManager = new MessageListenerManager(isometrik);
    reactionListenerManager = new ReactionListenerManager(isometrik);
    userListenerManager = new UserListenerManager(isometrik);
  }

  /**
   * Gets connection listener manager.
   *
   * @return the connection listener manager
   */
  public ConnectionListenerManager getConnectionListenerManager() {
    return connectionListenerManager;
  }

  /**
   * Gets conversation listener manager.
   *
   * @return the conversation listener manager
   */
  public ConversationListenerManager getConversationListenerManager() {
    return conversationListenerManager;
  }

  /**
   * Gets membership control listener manager.
   *
   * @return the membership control listener manager
   */
  public MembershipControlListenerManager getMembershipControlListenerManager() {
    return membershipControlListenerManager;
  }

  /**
   * Gets message listener manager.
   *
   * @return the message listener manager
   */
  public MessageListenerManager getMessageListenerManager() {
    return messageListenerManager;
  }

  /**
   * Gets reaction listener manager.
   *
   * @return the reaction listener manager
   */
  public ReactionListenerManager getReactionListenerManager() {
    return reactionListenerManager;
  }

  /**
   * Gets user listener manager.
   *
   * @return the user listener manager
   */
  public UserListenerManager getUserListenerManager() {
    return userListenerManager;
  }
}
