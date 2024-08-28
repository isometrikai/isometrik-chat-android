package io.isometrik.chat.models.upload.utils;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

/**
 * The custom interceptor to track upload progress.
 */
public class UploadProgressInterceptor implements Interceptor {

  private final UploadProgressListener uploadProgressListener;
  private final String requestId;
private final String requestGroupId;

  /**
   * Instantiates a new Upload progress interceptor.
   *
   * @param uploadProgressListener the upload progress listener
   * @param requestId the request id
   * @param requestGroupId the request group id
   */
  public UploadProgressInterceptor(UploadProgressListener uploadProgressListener,
      String requestId,String requestGroupId) {
    this.uploadProgressListener = uploadProgressListener;
    this.requestId = requestId;
    this.requestGroupId=requestGroupId;
  }

  @Override
  public @NotNull Response intercept(Chain chain) throws IOException {

    Request originalRequest = chain.request();

    if (originalRequest.body() == null) {
      return chain.proceed(originalRequest);
    }
    Request progressRequest = originalRequest.newBuilder()
        .method(originalRequest.method(),
            new UploadProgressRequestBody(originalRequest.body(), uploadProgressListener, requestId,requestGroupId))
        .build();

    return chain.proceed(progressRequest);
  }
}