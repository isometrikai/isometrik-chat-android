package io.isometrik.chat.builder.download;

/**
 * The query builder for canceling media download request.
 */
public class CancelMediaDownloadQuery {

  private final String messageId;

  private CancelMediaDownloadQuery(CancelMediaDownloadQuery.Builder builder) {
    this.messageId = builder.messageId;
  }

  /**
   * The builder class for building cancel media download query.
   */
  public static class Builder {
    private String messageId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     * @return the message id
     */
    public CancelMediaDownloadQuery.Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Build cancel media download query.
     *
     * @return the cancel media download query
     */
    public CancelMediaDownloadQuery build() {
      return new CancelMediaDownloadQuery(this);
    }
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }
}
