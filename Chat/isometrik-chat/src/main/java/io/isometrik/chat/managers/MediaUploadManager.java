package io.isometrik.chat.managers;

import io.isometrik.chat.models.upload.utils.UploadProgressInterceptor;
import io.isometrik.chat.services.MediaUploadService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The helper class for media upload management.
 */
public class MediaUploadManager {

  private final OkHttpClient transactionClientInstance;
  private final MediaUploadService mediaUploadService;

  /**
   * Instantiates a new Retrofit manager.
   *
   * @param uploadProgressInterceptor the upload progress interceptor
   */
  public MediaUploadManager(UploadProgressInterceptor uploadProgressInterceptor) {

    this.transactionClientInstance = createOkHttpClient(uploadProgressInterceptor);
    Retrofit transactionInstance = createRetrofit(this.transactionClientInstance);

    this.mediaUploadService = transactionInstance.create(MediaUploadService.class);
  }

  /**
   * Create OkHttpClient instance.
   *
   * @return OkHttpClient instance
   * @see OkHttpClient
   */
  private OkHttpClient createOkHttpClient(UploadProgressInterceptor uploadProgressInterceptor) {

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.retryOnConnectionFailure(false);
    httpClient.readTimeout(60, TimeUnit.MINUTES);
    httpClient.connectTimeout(60, TimeUnit.MINUTES);
    httpClient.writeTimeout(60, TimeUnit.MINUTES);
    httpClient.addNetworkInterceptor(uploadProgressInterceptor);

    return httpClient.build();
  }

  /**
   * Create retrofit instance.
   *
   * @return retrofit instance
   * @see Retrofit
   */
  private Retrofit createRetrofit(OkHttpClient client) {
    return new Retrofit.Builder().baseUrl("https://apis.isometrik.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
  }

  /**
   * Gets media upload service.
   *
   * @return the media upload service
   */
  public MediaUploadService getMediaUploadService() {
    return mediaUploadService;
  }

  /**
   * Destroy.
   *
   * @param force whether to destroy forcibly
   */
  public void destroy(boolean force) {
    if (this.transactionClientInstance != null) {
      closeExecutor(this.transactionClientInstance, force);
    }
  }

  /**
   * Closes the OkHttpClient execute
   *
   * @param client OkHttpClient whose requests are to be canceled
   * @param force whether to forcibly shutdown the OkHttpClient and evict all connection pool
   */
  private void closeExecutor(OkHttpClient client, boolean force) {
    try {
      new Thread(() -> {
        client.dispatcher().cancelAll();
        if (force) {

          ExecutorService executorService = client.dispatcher().executorService();
          executorService.shutdown();
          client.connectionPool().evictAll();
        }
      }).start();
    } catch (Exception ignore) {
    }
  }
}
