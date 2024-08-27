package io.isometrik.chat.managers;

import io.isometrik.chat.Isometrik;
import io.isometrik.chat.services.MediaDownloadService;
import io.isometrik.chat.services.MediaUploadService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The helper class for creating connections for media upload/download operations.
 */
public class MediaTransferManager {
  private final Isometrik isometrik;

  private final OkHttpClient transactionClientInstance;

  private final MediaUploadService mediaUploadService;
  private final MediaDownloadService mediaDownloadService;

  /**
   * Instantiates a new Retrofit manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.chat.Isometrik
   */
  public MediaTransferManager(Isometrik isometrikInstance) {
    this.isometrik = isometrikInstance;
    this.transactionClientInstance = createOkHttpClient();
    Retrofit transactionInstance = createRetrofit(this.transactionClientInstance);

    this.mediaUploadService = transactionInstance.create(MediaUploadService.class);
    this.mediaDownloadService = transactionInstance.create(MediaDownloadService.class);
  }

  /**
   * Create OkHttpClient instance.
   *
   * @return OkHttpClient instance
   * @see okhttp3.OkHttpClient
   */
  private OkHttpClient createOkHttpClient() {

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.retryOnConnectionFailure(false);
    httpClient.readTimeout(60, TimeUnit.MINUTES);
    httpClient.connectTimeout(60, TimeUnit.MINUTES);
    httpClient.writeTimeout(60, TimeUnit.MINUTES);

    return httpClient.build();
  }

  /**
   * Create retrofit instance.
   *
   * @return retrofit instance
   * @see retrofit2.Retrofit
   */
  private Retrofit createRetrofit(OkHttpClient client) {
    return new Retrofit.Builder().baseUrl(isometrik.getBaseUrl())
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
   * Gets media download service.
   *
   * @return the media download service
   */
  public MediaDownloadService getMediaDownloadService() {
    return mediaDownloadService;
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
