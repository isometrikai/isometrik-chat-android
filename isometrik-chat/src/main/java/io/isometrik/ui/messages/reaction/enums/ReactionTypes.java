package io.isometrik.ui.messages.reaction.enums;

/**
 * The enum Reaction types.
 */
public enum ReactionTypes {

  /**
   * Yes reaction types.
   */
  Yes("yes"),
  /**
   * Surprised reaction types.
   */
  Surprised("surprised"),
  /**
   * Crying with laughter reaction types.
   */
  CryingWithLaughter("crying_with_laughter"),
  /**
   * Crying reaction types.
   */
  Crying("crying"),
  /**
   * Heart reaction types.
   */
  Heart("heart"),
  /**
   * Sarcastic reaction types.
   */
  Sarcastic("sarcastic"),
  /**
   * Rock reaction types.
   */
  Rock("rock"),
  /**
   * Face palm reaction types.
   */
  FacePalm("facepalm"),
  /**
   * Star reaction types.
   */
  Star("star"),
  /**
   * No reaction types.
   */
  No("no"),
  /**
   * Bowing reaction types.
   */
  Bowing("bowing"),
  /**
   * Party reaction types.
   */
  Party("party"),
  /**
   * High five reaction types.
   */
  HighFive("high_five"),
  /**
   * Talking too much reaction types.
   */
  TalkingTooMuch("talking_too_much"),
  /**
   * Dancing reaction types.
   */
  Dancing("dancing");

  private final String reactionType;

  ReactionTypes(String reactionType) {
    this.reactionType = reactionType;
  }

  /**
   * Gets reaction type.
   *
   * @return the reaction type
   */
  public final String getReactionType() {
    return this.reactionType;
  }
}
