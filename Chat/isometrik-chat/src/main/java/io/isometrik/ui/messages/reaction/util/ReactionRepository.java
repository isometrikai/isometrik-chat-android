package io.isometrik.ui.messages.reaction.util;

import io.isometrik.ui.messages.reaction.add.ReactionModel;
import io.isometrik.ui.messages.reaction.enums.ReactionIcons;
import io.isometrik.ui.messages.reaction.enums.ReactionNames;
import io.isometrik.ui.messages.reaction.enums.ReactionTypes;
import java.util.ArrayList;

/**
 * The local repository to mimick fetching of supported reaction from a remote source.Code in this
 * repository can eventually be replaced with an api call to fetch list of supported reactions.
 */
public class ReactionRepository {

  /**
   * Gets reactions.
   *
   * @return the reactions
   */
  public static ArrayList<ReactionModel> getReactions() {

    ReactionModel reactionModel;
    ArrayList<ReactionModel> reactionModels = new ArrayList<>();
    for (int i = 0; i < 15; i++) {
      switch (i) {
        case 0:
          reactionModel = new ReactionModel(ReactionNames.Yes.getReactionName(),
              ReactionIcons.Yes.getReactionIcon(), ReactionTypes.Yes.getReactionType(),0);
          break;
        case 1:
          reactionModel = new ReactionModel(ReactionNames.Surprised.getReactionName(),
              ReactionIcons.Surprised.getReactionIcon(), ReactionTypes.Surprised.getReactionType(),0);
          break;
        case 2:
          reactionModel = new ReactionModel(ReactionNames.CryingWithLaughter.getReactionName(),
              ReactionIcons.CryingWithLaughter.getReactionIcon(),
              ReactionTypes.CryingWithLaughter.getReactionType(),0);
          break;
        case 3:
          reactionModel = new ReactionModel(ReactionNames.Crying.getReactionName(),
              ReactionIcons.Crying.getReactionIcon(), ReactionTypes.Crying.getReactionType(),0);
          break;
        case 4:
          reactionModel = new ReactionModel(ReactionNames.Heart.getReactionName(),
              ReactionIcons.Heart.getReactionIcon(), ReactionTypes.Heart.getReactionType(),0);
          break;
        case 5:
          reactionModel = new ReactionModel(ReactionNames.Sarcastic.getReactionName(),
              ReactionIcons.Sarcastic.getReactionIcon(), ReactionTypes.Sarcastic.getReactionType(),0);
          break;
        case 6:
          reactionModel = new ReactionModel(ReactionNames.Rock.getReactionName(),
              ReactionIcons.Rock.getReactionIcon(), ReactionTypes.Rock.getReactionType(),0);
          break;
        case 7:
          reactionModel = new ReactionModel(ReactionNames.FacePalm.getReactionName(),
              ReactionIcons.FacePalm.getReactionIcon(), ReactionTypes.FacePalm.getReactionType(),0);
          break;
        case 8:
          reactionModel = new ReactionModel(ReactionNames.Star.getReactionName(),
              ReactionIcons.Star.getReactionIcon(), ReactionTypes.Star.getReactionType(),0);
          break;
        case 9:
          reactionModel = new ReactionModel(ReactionNames.No.getReactionName(),
              ReactionIcons.No.getReactionIcon(), ReactionTypes.No.getReactionType(),0);
          break;
        case 10:
          reactionModel = new ReactionModel(ReactionNames.Bowing.getReactionName(),
              ReactionIcons.Bowing.getReactionIcon(), ReactionTypes.Bowing.getReactionType(),0);
          break;
        case 11:
          reactionModel = new ReactionModel(ReactionNames.Party.getReactionName(),
              ReactionIcons.Party.getReactionIcon(), ReactionTypes.Party.getReactionType(),0);
          break;
        case 12:
          reactionModel = new ReactionModel(ReactionNames.HighFive.getReactionName(),
              ReactionIcons.HighFive.getReactionIcon(), ReactionTypes.HighFive.getReactionType(),0);
          break;
        case 13:
          reactionModel = new ReactionModel(ReactionNames.TalkingTooMuch.getReactionName(),
              ReactionIcons.TalkingTooMuch.getReactionIcon(),
              ReactionTypes.TalkingTooMuch.getReactionType(),0);
          break;
        default:
          reactionModel = new ReactionModel(ReactionNames.Dancing.getReactionName(),
              ReactionIcons.Dancing.getReactionIcon(), ReactionTypes.Dancing.getReactionType(),0);
          break;
      }
      reactionModels.add(reactionModel);
    }

    return reactionModels;
  }
}
