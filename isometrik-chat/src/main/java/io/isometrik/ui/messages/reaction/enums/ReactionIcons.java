package io.isometrik.ui.messages.reaction.enums;

import io.isometrik.chat.R;

/**
 * The enum Reaction icons.
 */
public enum ReactionIcons {

  /**
   * Yes reaction icons.
   */
  Yes(R.drawable.ism_ic_reaction_yes),
  /**
   * Surprised reaction icons.
   */
  Surprised(R.drawable.ism_ic_reaction_surprised),
  /**
   * Crying with laughter reaction icons.
   */
  CryingWithLaughter(R.drawable.ism_ic_reaction_crying_laughing),
  /**
   * Crying reaction icons.
   */
  Crying(R.drawable.ism_ic_reaction_crying),
  /**
   * Heart reaction icons.
   */
  Heart(R.drawable.ism_ic_reaction_heart),
  /**
   * Sarcastic reaction icons.
   */
  Sarcastic(R.drawable.ism_ic_reaction_sarcasm),
  /**
   * Rock reaction icons.
   */
  Rock(R.drawable.ism_ic_reaction_rock),
  /**
   * Face palm reaction icons.
   */
  FacePalm(R.drawable.ism_ic_reaction_facepalm),
  /**
   * Star reaction icons.
   */
  Star(R.drawable.ism_ic_reaction_star),
  /**
   * No reaction icons.
   */
  No(R.drawable.ism_ic_reaction_no),
  /**
   * Bowing reaction icons.
   */
  Bowing(R.drawable.ism_ic_reaction_bowing),
  /**
   * Party reaction icons.
   */
  Party(R.drawable.ism_ic_reaction_party),
  /**
   * High five reaction icons.
   */
  HighFive(R.drawable.ism_ic_reaction_high_five),
  /**
   * Talking too much reaction icons.
   */
  TalkingTooMuch(R.drawable.ism_ic_reaction_talking_too_much),
  /**
   * Dancing reaction icons.
   */
  Dancing(R.drawable.ism_ic_reaction_dancing);

  private final int reactionIcon;

  ReactionIcons(int reactionIcon) {
    this.reactionIcon = reactionIcon;
  }

  /**
   * Gets reaction icon.
   *
   * @return the reaction icon
   */
  public final int getReactionIcon() {
    return this.reactionIcon;
  }
}
