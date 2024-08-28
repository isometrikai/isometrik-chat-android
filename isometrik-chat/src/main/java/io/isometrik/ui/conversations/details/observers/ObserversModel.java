package io.isometrik.ui.conversations.details.observers;

import io.isometrik.chat.response.membershipcontrol.FetchObserversResult;

/**
 * The helper class for inflating items in the observers list, in an open conversation.
 */
public class ObserversModel {
  private final String observerId, observerName, observerProfileImageUrl;

  private final boolean isOnline;
  private final long lastSeen;

  /**
   * Instantiates a new Observers model.
   *
   * @param conversationObserver the conversation observer
   */
  public ObserversModel(FetchObserversResult.ConversationObserver conversationObserver) {
    observerName = conversationObserver.getUserName();
    observerId = conversationObserver.getUserId();
    observerProfileImageUrl = conversationObserver.getUserProfileImageUrl();
    isOnline = conversationObserver.isOnline();
    lastSeen = conversationObserver.getLastSeen();
  }

  /**
   * Gets observer name.
   *
   * @return the observer name
   */
  public String getObserverName() {
    return observerName;
  }

  /**
   * Gets observer id.
   *
   * @return the observer id
   */
  public String getObserverId() {
    return observerId;
  }

  /**
   * Gets observer profile image url.
   *
   * @return the observer profile image url
   */
  public String getObserverProfileImageUrl() {
    return observerProfileImageUrl;
  }

  /**
   * Is online boolean.
   *
   * @return the boolean
   */
  public boolean isOnline() {
    return isOnline;
  }

  /**
   * Gets last seen.
   *
   * @return the last seen
   */
  public long getLastSeen() {
    return lastSeen;
  }
}

