package io.isometrik.chat.response.message.utils.validations;

import io.isometrik.chat.response.message.utils.schemas.MentionedUser;
import java.util.List;

/**
 * The helper class for mentioned user validation.
 */
public class MentionedUserValidation {
  private boolean validateMentionedUser(MentionedUser mentionedUser) {

    return mentionedUser.getOrder() == null
        || mentionedUser.getWordCount() == null
        || mentionedUser.getUserId() == null;
  }

  /**
   * Validate mentioned users boolean.
   *
   * @param mentionedUsers the mentioned users
   * @return the boolean
   */
  public boolean validateMentionedUsers(List<MentionedUser> mentionedUsers) {
    for (int i = 0; i < mentionedUsers.size(); i++) {

      if (validateMentionedUser(mentionedUsers.get(i))) return true;
    }
    return false;
  }
}
