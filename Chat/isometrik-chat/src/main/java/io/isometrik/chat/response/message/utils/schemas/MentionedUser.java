package io.isometrik.chat.response.message.utils.schemas;

/**
 * The helper class for schema of the mentioned user.
 */
public class MentionedUser {

  private final Integer wordCount;
  private final Integer order;
  private final String userId;

  /**
   * Instantiates a new Mentioned user.
   *
   * @param wordCount the word count
   * @param order the order
   * @param userId the user id
   */
  public MentionedUser(Integer wordCount, Integer order, String userId) {
    this.wordCount = wordCount;
    this.order = order;
    this.userId = userId;
  }

  /**
   * Gets word count.
   *
   * @return the word count
   */
  public Integer getWordCount() {
    return wordCount;
  }

  /**
   * Gets order.
   *
   * @return the order
   */
  public Integer getOrder() {
    return order;
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }
}