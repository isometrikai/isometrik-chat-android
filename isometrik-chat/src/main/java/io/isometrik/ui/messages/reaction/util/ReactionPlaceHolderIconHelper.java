package io.isometrik.ui.messages.reaction.util;

import io.isometrik.ui.messages.reaction.enums.ReactionIcons;

/**
 * The helper class for place holder icons for reactions.
 */
public class ReactionPlaceHolderIconHelper {

  /**
   * Fetch last message place holder icon integer.
   *
   * @param reactionType the reaction type
   * @return the integer
   */
  public static Integer fetchLastMessagePlaceHolderIcon(String reactionType) {
    switch (reactionType) {

      case "yes":
        return ReactionIcons.Yes.getReactionIcon();
      case "surprised":
        return ReactionIcons.Surprised.getReactionIcon();

      case "crying_with_laughter":
        return ReactionIcons.CryingWithLaughter.getReactionIcon();

      case "crying":
        return ReactionIcons.Crying.getReactionIcon();

      case "heart":
        return ReactionIcons.Heart.getReactionIcon();

      case "sarcastic":
        return ReactionIcons.Sarcastic.getReactionIcon();

      case "rock":
        return ReactionIcons.Rock.getReactionIcon();

      case "facepalm":
        return ReactionIcons.FacePalm.getReactionIcon();

      case "star":
        return ReactionIcons.Star.getReactionIcon();

      case "no":
        return ReactionIcons.No.getReactionIcon();

      case "bowing":
        return ReactionIcons.Bowing.getReactionIcon();

      case "party":
        return ReactionIcons.Party.getReactionIcon();

      case "high_five":
        return ReactionIcons.HighFive.getReactionIcon();

      case "talking_too_much":
        return ReactionIcons.TalkingTooMuch.getReactionIcon();

      case "dancing":
        return ReactionIcons.Dancing.getReactionIcon();
      default:
        return null;
    }
  }
}
