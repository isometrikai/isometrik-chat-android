package io.isometrik.chat.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The helper class to parse the response of the fetch update user presigned url request.
 */
public class FetchUpdateUserPresignedUrlResult {
  @SerializedName("msg")
  @Expose
  private String message;

  @SerializedName("mediaUrl")
  @Expose
  private String mediaUrl;
  @SerializedName("presignedUrl")
  @Expose
  private String presignedUrl;
  @SerializedName("ttl")
  @Expose
  private int ttl;

  /**
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets media url.
   *
   * @return the media url
   */
  public String getMediaUrl() {
    return mediaUrl;
  }

  /**
   * Gets presigned url.
   *
   * @return the presigned url
   */
  public String getPresignedUrl() {
    return presignedUrl;
  }

  /**
   * Gets ttl.
   *
   * @return the ttl
   */
  public int getTtl() {
    return ttl;
  }
}
