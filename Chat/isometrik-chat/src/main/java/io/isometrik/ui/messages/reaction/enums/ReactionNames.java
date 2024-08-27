package io.isometrik.ui.messages.reaction.enums;

/**
 * The enum Reaction names.
 */
public enum ReactionNames {

  /**
   * Yes reaction names.
   */
  Yes("Yes"),
  /**
   * Surprised reaction names.
   */
  Surprised("Surprised"),
  /**
   * Crying with laughter reaction names.
   */
  CryingWithLaughter("CryingWithLaughter"),
  /**
   * Crying reaction names.
   */
  Crying("Crying"),
  /**
   * Heart reaction names.
   */
  Heart("Heart"),
  /**
   * Sarcastic reaction names.
   */
  Sarcastic("Sarcastic"),
  /**
   * Rock reaction names.
   */
  Rock("Rock"),
  /**
   * Face palm reaction names.
   */
  FacePalm("FacePalm"),
  /**
   * Star reaction names.
   */
  Star("Star"),
  /**
   * No reaction names.
   */
  No("No"),
  /**
   * Bowing reaction names.
   */
  Bowing("Bowing"),
  /**
   * Party reaction names.
   */
  Party("Party"),
  /**
   * High five reaction names.
   */
  HighFive("HighFive"),
  /**
   * Talking too much reaction names.
   */
  TalkingTooMuch("TalkingTooMuch"),
  /**
   * Dancing reaction names.
   */
  Dancing("Dancing");

  private final String reactionName;

  ReactionNames(String reactionName) {
    this.reactionName = reactionName;
  }

  /**
   * Gets reaction name.
   *
   * @return the reaction name
   */
  public final String getReactionName() {
    return this.reactionName;
  }
}
