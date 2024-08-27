package io.isometrik.ui.messages.reaction.util;

import io.isometrik.ui.messages.reaction.add.ReactionModel;
import io.isometrik.ui.messages.reaction.enums.ReactionIcons;
import io.isometrik.ui.messages.reaction.enums.ReactionNames;
import io.isometrik.ui.messages.reaction.enums.ReactionTypes;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The helper class to parse list of reactions for a message.
 */
public class ReactionUtil {

  /**
   * Parse reaction messages array list.
   *
   * @param messageReactions the message reactions
   * @return the array list
   */
  public static ArrayList<ReactionModel> parseReactionMessages(JSONObject messageReactions) {

    ArrayList<ReactionModel> reactionModels = null;
    if (messageReactions != null) {
      reactionModels = new ArrayList<>();
      Iterator<String> iterator = messageReactions.keys();

      while (iterator.hasNext()) {

        String reactionType = iterator.next();
        try {
          switch (reactionType) {

            case "yes": {

              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Yes.getReactionName(),
                    ReactionIcons.Yes.getReactionIcon(), ReactionTypes.Yes.getReactionType(),
                    count));
              }

              break;
            }

            case "surprised": {

              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Surprised.getReactionName(),
                    ReactionIcons.Surprised.getReactionIcon(),
                    ReactionTypes.Surprised.getReactionType(), count));
              }
              break;
            }

            case "crying_with_laughter": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(
                    new ReactionModel(ReactionNames.CryingWithLaughter.getReactionName(),
                        ReactionIcons.CryingWithLaughter.getReactionIcon(),
                        ReactionTypes.CryingWithLaughter.getReactionType(), count));
              }
              break;
            }

            case "crying": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Crying.getReactionName(),
                    ReactionIcons.Crying.getReactionIcon(), ReactionTypes.Crying.getReactionType(),
                    count));
              }
              break;
            }

            case "heart": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Heart.getReactionName(),
                    ReactionIcons.Heart.getReactionIcon(), ReactionTypes.Heart.getReactionType(),
                    count));
              }
              break;
            }
            case "sarcastic": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Sarcastic.getReactionName(),
                    ReactionIcons.Sarcastic.getReactionIcon(),
                    ReactionTypes.Sarcastic.getReactionType(), count));
              }
              break;
            }
            case "rock": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Rock.getReactionName(),
                    ReactionIcons.Rock.getReactionIcon(), ReactionTypes.Rock.getReactionType(),
                    count));
              }
              break;
            }
            case "facepalm": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.FacePalm.getReactionName(),
                    ReactionIcons.FacePalm.getReactionIcon(),
                    ReactionTypes.FacePalm.getReactionType(), count));
              }
              break;
            }
            case "star": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Star.getReactionName(),
                    ReactionIcons.Star.getReactionIcon(), ReactionTypes.Star.getReactionType(),
                    count));
              }
              break;
            }
            case "no": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.No.getReactionName(),
                    ReactionIcons.No.getReactionIcon(), ReactionTypes.No.getReactionType(), count));
              }
              break;
            }
            case "bowing": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Bowing.getReactionName(),
                    ReactionIcons.Bowing.getReactionIcon(), ReactionTypes.Bowing.getReactionType(),
                    count));
              }
              break;
            }
            case "party": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Party.getReactionName(),
                    ReactionIcons.Party.getReactionIcon(), ReactionTypes.Party.getReactionType(),
                    count));
              }
              break;
            }
            case "high_five": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.HighFive.getReactionName(),
                    ReactionIcons.HighFive.getReactionIcon(),
                    ReactionTypes.HighFive.getReactionType(), count));
              }
              break;
            }
            case "talking_too_much": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.TalkingTooMuch.getReactionName(),
                    ReactionIcons.TalkingTooMuch.getReactionIcon(),
                    ReactionTypes.TalkingTooMuch.getReactionType(), count));
              }
              break;
            }
            case "dancing": {
              JSONArray userIds = messageReactions.getJSONArray(reactionType);

              int count = userIds.length();

              if (count > 0) {
                reactionModels.add(new ReactionModel(ReactionNames.Dancing.getReactionName(),
                    ReactionIcons.Dancing.getReactionIcon(),
                    ReactionTypes.Dancing.getReactionType(), count));
              }
              break;
            }
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }

    return reactionModels;
  }

  /**
   * Parse add or remove reaction event reaction model.
   *
   * @param reactionType the reaction type
   * @param reactionCount the reaction count
   * @return the reaction model
   */
  public static ReactionModel parseAddOrRemoveReactionEvent(String reactionType,
      int reactionCount) {
    ReactionModel reactionModel = null;

    switch (reactionType) {

      case "yes": {

        reactionModel = new ReactionModel(ReactionNames.Yes.getReactionName(),
            ReactionIcons.Yes.getReactionIcon(), ReactionTypes.Yes.getReactionType(),
            reactionCount);

        break;
      }

      case "surprised": {

        reactionModel = new ReactionModel(ReactionNames.Surprised.getReactionName(),
            ReactionIcons.Surprised.getReactionIcon(), ReactionTypes.Surprised.getReactionType(),
            reactionCount);

        break;
      }

      case "crying_with_laughter": {
        reactionModel = new ReactionModel(ReactionNames.CryingWithLaughter.getReactionName(),
            ReactionIcons.CryingWithLaughter.getReactionIcon(),
            ReactionTypes.CryingWithLaughter.getReactionType(), reactionCount);
        break;
      }

      case "crying": {
        reactionModel = new ReactionModel(ReactionNames.Crying.getReactionName(),
            ReactionIcons.Crying.getReactionIcon(), ReactionTypes.Crying.getReactionType(),
            reactionCount);
        break;
      }

      case "heart": {
        reactionModel = new ReactionModel(ReactionNames.Heart.getReactionName(),
            ReactionIcons.Heart.getReactionIcon(), ReactionTypes.Heart.getReactionType(),
            reactionCount);
        break;
      }
      case "sarcastic": {
        reactionModel = new ReactionModel(ReactionNames.Sarcastic.getReactionName(),
            ReactionIcons.Sarcastic.getReactionIcon(), ReactionTypes.Sarcastic.getReactionType(),
            reactionCount);
        break;
      }
      case "rock": {
        reactionModel = new ReactionModel(ReactionNames.Rock.getReactionName(),
            ReactionIcons.Rock.getReactionIcon(), ReactionTypes.Rock.getReactionType(),
            reactionCount);
        break;
      }
      case "facepalm": {
        reactionModel = new ReactionModel(ReactionNames.FacePalm.getReactionName(),
            ReactionIcons.FacePalm.getReactionIcon(), ReactionTypes.FacePalm.getReactionType(),
            reactionCount);
        break;
      }
      case "star": {
        reactionModel = new ReactionModel(ReactionNames.Star.getReactionName(),
            ReactionIcons.Star.getReactionIcon(), ReactionTypes.Star.getReactionType(),
            reactionCount);
        break;
      }
      case "no": {
        reactionModel = new ReactionModel(ReactionNames.No.getReactionName(),
            ReactionIcons.No.getReactionIcon(), ReactionTypes.No.getReactionType(), reactionCount);
        break;
      }
      case "bowing": {
        reactionModel = new ReactionModel(ReactionNames.Bowing.getReactionName(),
            ReactionIcons.Bowing.getReactionIcon(), ReactionTypes.Bowing.getReactionType(),
            reactionCount);
        break;
      }
      case "party": {
        reactionModel = new ReactionModel(ReactionNames.Party.getReactionName(),
            ReactionIcons.Party.getReactionIcon(), ReactionTypes.Party.getReactionType(),
            reactionCount);
        break;
      }
      case "high_five": {
        reactionModel = new ReactionModel(ReactionNames.HighFive.getReactionName(),
            ReactionIcons.HighFive.getReactionIcon(), ReactionTypes.HighFive.getReactionType(),
            reactionCount);
        break;
      }
      case "talking_too_much": {
        reactionModel = new ReactionModel(ReactionNames.TalkingTooMuch.getReactionName(),
            ReactionIcons.TalkingTooMuch.getReactionIcon(),
            ReactionTypes.TalkingTooMuch.getReactionType(), reactionCount);
        break;
      }
      case "dancing": {
        reactionModel = new ReactionModel(ReactionNames.Dancing.getReactionName(),
            ReactionIcons.Dancing.getReactionIcon(), ReactionTypes.Dancing.getReactionType(),
            reactionCount);
        break;
      }
    }
    return reactionModel;
  }
}


