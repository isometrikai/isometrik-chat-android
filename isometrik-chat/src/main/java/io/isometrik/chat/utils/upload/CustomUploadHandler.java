package io.isometrik.chat.utils.upload;

import java.util.function.Consumer;

public class CustomUploadHandler {

    private static UploadCallback uploadCallback;

    public interface UploadCallback {
        void uploadMedia(String mediaId, String mediaPath, Consumer<UploadedMediaResponse> callback);
    }

    /**
     * Registers a custom upload callback from the base module.
     */
    public static void registerUploadHandler(UploadCallback callback) {
        uploadCallback = callback;
    }

    /**
     * Checks if a custom upload handler is registered.
     */
    public static boolean isRegistered() {
        return uploadCallback != null;
    }

    /**
     * Calls the registered upload handler from the base module.
     */
    public static void uploadMedia(String mediaId, String mediaPath, Consumer<UploadedMediaResponse> callback) {
        if (uploadCallback != null) {
            uploadCallback.uploadMedia(mediaId, mediaPath, callback);
        } else {
            callback.accept(null);
        }
    }
}
