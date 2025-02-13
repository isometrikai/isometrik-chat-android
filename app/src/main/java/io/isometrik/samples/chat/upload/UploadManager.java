package io.isometrik.samples.chat.upload;

import android.util.Log;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.tus.java.client.TusClient;
import io.tus.java.client.TusUploader;

public class UploadManager {

    private final TusClient client;

    public UploadManager(TusClient client) {
        this.client = client;
    }

    public Future<?> uploadFile(String path, String type, UploadCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                try {
                    TusAndroidUpload upload = new TusAndroidUpload(path);
                    TusUploader uploader = client.resumeOrCreateUpload(upload);
                    long totalBytes = upload.getSize();
                    long uploadedBytes = uploader.getOffset();

                    uploader.setChunkSize(1024 * 1024); // 1 MiB chunks

                    while (uploader.uploadChunk() > 0) {
                        uploadedBytes = uploader.getOffset();
                        callback.onProgress(new UploadResult.Progress(uploadedBytes, totalBytes));
                    }

                    uploader.finish();
                    URL uploadURL = uploader.getUploadURL();

                    String optimisedUrl = "" + uploadURL;

                    if (type.equals("video")) {
                        optimisedUrl = optimisedUrl.replace("https://fetchfile.fax.tm/files/","https://fetchfile.fax.tm/hls/");
                        optimisedUrl = optimisedUrl + "/master.m3u8";
                        Log.d("optimised: Video ",  optimisedUrl);
                    } else {
                        Log.d("optimised: Image ",  optimisedUrl);
                    }

//                    optimisedUrl = optimisedUrl.replace("https://fetchfile.fax.tm/files/", "https://fetchfile.fax.tm/cdn/");

                    callback.onSuccess(new UploadResult.Success(uploadURL,optimisedUrl));
                } catch (Exception e) {
                    callback.onError(new UploadResult.Error(e));
                }
                return null;
            }
        });
    }

    public interface UploadCallback {
        void onProgress(UploadResult.Progress progress);

        void onSuccess(UploadResult.Success success);

        void onError(UploadResult.Error error);
    }

    public static class UploadResult {
        public static class Progress extends UploadResult {
            private final long uploadedBytes;
            private final long totalBytes;

            public Progress(long uploadedBytes, long totalBytes) {
                this.uploadedBytes = uploadedBytes;
                this.totalBytes = totalBytes;
            }

            public long getUploadedBytes() {
                return uploadedBytes;
            }

            public long getTotalBytes() {
                return totalBytes;
            }
        }

        public static class Success extends UploadResult {
            private final URL uploadURL;
            private final String finalURL;

            public Success(URL uploadURL, String finalUrl) {
                this.uploadURL = uploadURL;
                this.finalURL = finalUrl;
            }

            public URL getUploadURL() {
                return uploadURL;
            }
            public String getFinalURL() {
                return finalURL;
            }
        }

        public static class Error extends UploadResult {
            private final Exception exception;

            public Error(Exception exception) {
                this.exception = exception;
            }

            public Exception getException() {
                return exception;
            }
        }
    }
}
