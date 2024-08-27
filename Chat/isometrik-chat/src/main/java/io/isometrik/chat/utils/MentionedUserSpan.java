package io.isometrik.chat.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

import io.isometrik.ui.messages.tag.TaggedUserCallback;

/**
 * The helper class for the mentioned user(clickable and bold) custom span with callback on click of
 * tagged user.
 */
public class MentionedUserSpan extends ClickableSpan {

  private final String userId;
  private final int wordCount;
  private final TaggedUserCallback taggedUserCallback;

  /**
   * Instantiates a new Mentioned user span.
   *
   * @param userId the user id
   * @param wordCount the word count
   * @param taggedUserCallback the tagged user callback
   */
  public MentionedUserSpan(String userId, int wordCount, TaggedUserCallback taggedUserCallback) {

    super();
    this.userId = userId;
    this.wordCount = wordCount;
    this.taggedUserCallback = taggedUserCallback;
  }

  @Override
  public void updateDrawState(@NonNull TextPaint ds) {
    super.updateDrawState(ds);
    ds.setTypeface(Typeface.DEFAULT_BOLD);
    ds.setColor(Color.BLACK);
    ds.setUnderlineText(false);

  }

  @Override
  public void onClick(View widget) {
    if (taggedUserCallback != null) taggedUserCallback.onTaggedUserClicked(userId);
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets word count.
   *
   * @return the word count
   */
  public int getWordCount() {
    return wordCount;
  }
}
