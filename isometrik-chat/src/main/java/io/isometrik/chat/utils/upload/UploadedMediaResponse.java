package io.isometrik.chat.utils.upload;

public class UploadedMediaResponse {
    private final String mediaId;
    private final String mediaUrl;
    private final String thumbnailUrl;

    public UploadedMediaResponse(String mediaId, String mediaUrl, String thumbnailUrl) {
        this.mediaId = mediaId;
        this.mediaUrl = mediaUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
