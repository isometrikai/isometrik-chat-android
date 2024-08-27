package io.isometrik.ui.messages.reaction.list;

/**
 * The helper class for inflating items in the list of users who have added a particular reaction on
 * a message.
 */
public class ReactionUsersModel {

  private final boolean reactionAddedBySelf, online;
  private final String userName, userProfileImageUrl;

  /**
   * Instantiates a new Reaction users model.
   *
   * @param reactionAddedBySelf the reaction added by self
   * @param online the online
   * @param userName the user name
   * @param userProfileImageUrl the user profile image url
   */
  public ReactionUsersModel(boolean reactionAddedBySelf, boolean online, String userName,
      String userProfileImageUrl) {
    this.reactionAddedBySelf = reactionAddedBySelf;
    this.online = online;
    this.userName = userName;
    this.userProfileImageUrl = userProfileImageUrl;
  }

  /**
   * Is reaction added by self boolean.
   *
   * @return the boolean
   */
  public boolean isReactionAddedBySelf() {
    return reactionAddedBySelf;
  }

  /**
   * Is online boolean.
   *
   * @return the boolean
   */
  public boolean isOnline() {
    return online;
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Gets user profile image url.
   *
   * @return the user profile image url
   */
  public String getUserProfileImageUrl() {
    return userProfileImageUrl;
  }
}
