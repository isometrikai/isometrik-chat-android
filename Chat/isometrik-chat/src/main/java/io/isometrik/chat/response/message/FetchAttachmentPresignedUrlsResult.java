package io.isometrik.chat.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * The helper class to parse the response of the fetch attachment presigned urls request.
 */
public class FetchAttachmentPresignedUrlsResult {
  @SerializedName("msg")
  @Expose
  private String message;
  @SerializedName("presignedUrls")
  @Expose
  private ArrayList<PresignedUrl> presignedUrls;

  /**
   * The type Presigned url.
   */
  public static class PresignedUrl {
    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;
    @SerializedName("thumbnailPresignedUrl")
    @Expose
    private String  thumbnailPresignedUrl;
    @SerializedName("mediaUrl")
    @Expose
    private String mediaUrl;
    @SerializedName("mediaPresignedUrl")
    @Expose
    private String mediaPresignedUrl;
    @SerializedName("mediaId")
    @Expose
    private String mediaId;
    @SerializedName("ttl")
    @Expose
    private int ttl;

    /**
     * Gets media url.
     *
     * @return the media url
     */
    public String getMediaUrl() {
      return mediaUrl;
    }

    /**
     * Gets media presigned url.
     *
     * @return the media presigned url
     */
    public String getMediaPresignedUrl() {
      return mediaPresignedUrl;
    }

    /**
     * Gets media id.
     *
     * @return the media id
     */
    public String getMediaId() {
      return mediaId;
    }

    /**
     * Gets ttl.
     *
     * @return the ttl
     */
    public int getTtl() {
      return ttl;
    }

    /**
     * Gets thumbnail url.
     *
     * @return the thumbnail url
     */
    public String getThumbnailUrl() {
      return thumbnailUrl;
    }

    /**
     * Gets thumbnail presigned url.
     *
     * @return the thumbnail presigned url
     */
    public String getThumbnailPresignedUrl() {
      return thumbnailPresignedUrl;
    }
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets presigned urls.
   *
   * @return the presigned urls
   */
  public ArrayList<PresignedUrl> getPresignedUrls() {
    return presignedUrls;
  }
}
