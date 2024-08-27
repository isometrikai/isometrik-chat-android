package io.isometrik.ui.messages.tag.util;

import io.isometrik.chat.response.message.utils.schemas.MentionedUser;
import java.util.ArrayList;

/**
 * The helper class to parse mentioned users from fetch messages response.
 */
public class ParseMentionedUsersFromFetchMessagesResponseUtil {

  /**
   * Parse mentioned users array list.
   *
   * @param mentionedUsers the mentioned users
   * @return the array list
   */
  public static ArrayList<MentionedUser> parseMentionedUsers(
      ArrayList<io.isometrik.chat.response.message.utils.fetchmessages.MentionedUser> mentionedUsers) {

    if (mentionedUsers == null) {
      return null;
    } else {
      ArrayList<MentionedUser> mentionedUserArrayList = new ArrayList<>();

      for (int i = 0; i < mentionedUsers.size(); i++) {
        mentionedUserArrayList.add(new MentionedUser(mentionedUsers.get(i).getWordCount(),
            mentionedUsers.get(i).getOrder(), mentionedUsers.get(i).getUserId()));
      }

      return mentionedUserArrayList;
    }
  }
}
