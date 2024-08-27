package io.isometrik.ui.messages.reaction.add;

/**
 * The helper class for inflating items in the list of reactions added to a message.
 */
public class ReactionModel {
  private final String reactionName, reactionType;
  private final int reactionIcon;
  private int reactionCount;

  /**
   * Instantiates a new Reaction model.
   *
   * @param reactionName the reaction name
   * @param reactionIcon the reaction icon
   * @param reactionType the reaction type
   * @param reactionCount the reaction count
   */
  public ReactionModel(String reactionName, int reactionIcon, String reactionType, int reactionCount) {
    this.reactionName = reactionName;
    this.reactionIcon = reactionIcon;
    this.reactionType = reactionType;
    this.reactionCount=reactionCount;
  }

  /**
   * Gets reaction name.
   *
   * @return the reaction name
   */
  public String getReactionName() {
    return reactionName;
  }

  /**
   * Gets reaction icon.
   *
   * @return the reaction icon
   */
  public int getReactionIcon() {
    return reactionIcon;
  }

  /**
   * Gets reaction type.
   *
   * @return the reaction type
   */
  public String getReactionType() {
    return reactionType;
  }

  /**
   * Gets reaction count.
   *
   * @return the reaction count
   */
  public int getReactionCount() {
    return reactionCount;
  }

  /**
   * Sets reaction count.
   *
   * @param reactionCount the reaction count
   */
  public void setReactionCount(int reactionCount) {
    this.reactionCount = reactionCount;
  }
}
