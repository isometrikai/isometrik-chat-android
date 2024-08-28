package io.isometrik.ui.utils;

import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import io.isometrik.chat.response.message.utils.schemas.MentionedUser;
import io.isometrik.ui.messages.tag.TagUserModel;
import io.isometrik.ui.messages.tag.TaggedUserCallback;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The helper class for tagging user in a text message functionality.
 */
public class TagUserUtil {
  private static final Pattern pattern = Pattern.compile("[@][a-zA-Z0-9-.]*");

  /**
   * Gets tagged username to search.
   *
   * @param text the text
   * @param cursorPosition the cursor position
   * @return the tagged username to search
   */
  public static String getTaggedUsernameToSearch(String text, int cursorPosition) {

    Matcher matcher = pattern.matcher(text);
    while (matcher.find()) {
      if (cursorPosition >= matcher.start() && cursorPosition <= matcher.end()) {
        // add 1 to ommit the "@" tag

        return text.substring(matcher.start() + 1, matcher.end());
      }
    }
    return null;
  }

  /**
   * Add tagged username char sequence.
   *
   * @param originalTextStringBuilder the original text string builder
   * @param cursorPosition the cursor position
   * @param tagUserModel the tag user model
   * @param taggedUserCallback the tagged user callback
   * @return the char sequence
   */
  public static CharSequence addTaggedUsername(SpannableStringBuilder originalTextStringBuilder,
      int cursorPosition, TagUserModel tagUserModel, TaggedUserCallback taggedUserCallback) {

    Matcher matcher = pattern.matcher(originalTextStringBuilder);
    while (matcher.find()) {
      if (cursorPosition >= matcher.start() && cursorPosition <= matcher.end()) {

        SpannableString memberNameSpannable =
            new SpannableString("@" + tagUserModel.getMemberName());

        MentionedUserSpan mentionedUserSpan = new MentionedUserSpan(tagUserModel.getMemberId(),
            getNumberOfWords(tagUserModel.getMemberName()), taggedUserCallback);

        memberNameSpannable.setSpan(mentionedUserSpan, 0, memberNameSpannable.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        CharSequence postfix;
        if (originalTextStringBuilder.length() > matcher.end()) {
          postfix = originalTextStringBuilder.subSequence(matcher.end(),
              originalTextStringBuilder.length() - 1);
        } else {
          postfix = " ";
        }

        return TextUtils.concat(originalTextStringBuilder.subSequence(0, matcher.start()),
            memberNameSpannable, postfix);
      }
    }
    return null;
  }

  /**
   * Parse mentioned users spannable string.
   *
   * @param text the text
   * @param mentionedUsers the mentioned users
   * @param taggedUserCallback the tagged user callback
   * @return the spannable string
   */
  public static SpannableString parseMentionedUsers(String text,
      ArrayList<MentionedUser> mentionedUsers, TaggedUserCallback taggedUserCallback) {

    SpannableString spannableString = new SpannableString(text);
    if (mentionedUsers == null) {
      return spannableString;
    }

    Matcher matcher = pattern.matcher(text);

    int order = 0;
    while (matcher.find()) {

      for (int i = 0; i < mentionedUsers.size(); i++) {
        if (mentionedUsers.get(i).getOrder().equals(order)) {

          MentionedUser mentionedUser = mentionedUsers.get(i);
          MentionedUserSpan mentionedUserSpan =
              new MentionedUserSpan(mentionedUser.getUserId(), mentionedUser.getWordCount(),
                  taggedUserCallback);

          if (mentionedUser.getWordCount() == 1) {
            spannableString.setSpan(mentionedUserSpan, matcher.start(), matcher.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          } else {

            spannableString.setSpan(mentionedUserSpan, matcher.start(),
                getEndIndexOfSpan(text.substring(matcher.end()), mentionedUser.getWordCount() - 1,
                    matcher.end()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
          }
          break;
        }
      }

      order++;
    }
    return spannableString;
  }

  /**
   * Prepare mentioned users array list.
   *
   * @param message the message
   * @return the array list
   */
  public static ArrayList<MentionedUser> prepareMentionedUsers(Editable message) {

    ArrayList<MentionedUser> mentionedUsers = new ArrayList<>();
    MentionedUserSpan[] list = message.getSpans(0, message.length(), MentionedUserSpan.class);

    for (int i = 0; i < list.length; i++) {

      mentionedUsers.add(new MentionedUser(list[i].getWordCount(), i, list[i].getUserId()));
    }

    return mentionedUsers.isEmpty() ? null : mentionedUsers;
  }

  private static int getNumberOfWords(String userName) {
    String words = userName.trim();
    if (words.isEmpty()) return 0;
    return words.split("\\s+").length;
  }

  private static int getEndIndexOfSpan(String spanPostfix, int wordsCount, int endIndex) {
    if (spanPostfix.isEmpty()) {
      return endIndex;
    } else {
      String[] wordsList = spanPostfix.trim().split("\\s+");

      String lastWordOfName = wordsList[wordsCount - 1];
      String offsetWord = " ";
      if (wordsCount > 1) {
        offsetWord = wordsList[wordsCount - 2];
      }

      return 1 + endIndex + spanPostfix.indexOf(lastWordOfName, spanPostfix.indexOf(offsetWord));
    }
  }
}
