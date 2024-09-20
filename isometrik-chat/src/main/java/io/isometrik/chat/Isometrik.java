package io.isometrik.chat;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.isometrik.chat.listeners.RealtimeEventsListenerManager;
import io.isometrik.chat.managers.BasePathManager;
import io.isometrik.chat.managers.MediaTransferManager;
import io.isometrik.chat.managers.RetrofitManager;
import io.isometrik.chat.models.events.MessageEvents;
import io.isometrik.chat.models.events.PresenceEvents;
import io.isometrik.chat.network.ConnectivityReceiver;
import io.isometrik.chat.network.IsometrikConnection;
import io.isometrik.chat.remote.RemoteUseCases;
import io.isometrik.chat.response.error.BaseResponse;
import io.isometrik.chat.response.error.IsometrikError;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jetbrains.annotations.NotNull;

/**
 * The helper class for interacting with Isometrik backend for api calls, realtime events, media
 * upload/download.
 */
public class Isometrik {
  private IMConfiguration configuration;

  private final RealtimeEventsListenerManager realtimeEventsListenerManager;

  private final RemoteUseCases remoteUseCases;

  private final ExecutorService executor = Executors.newFixedThreadPool(10);

  private final BasePathManager basePathManager;
  private final RetrofitManager retrofitManager;
  private final MediaTransferManager mediaTransferManager;

  private final IsometrikConnection isometrikConnection;
  /**
   * Model classes for realtime events handling
   */
  private final MessageEvents messageEvents;
  private final PresenceEvents presenceEvents;

  /**
   * For paring json response
   */
  private final Gson gson;

  private static final String ISOMETRIK_SUCCESS_TAG = "isometrik-success-logs";
  private static final String ISOMETRIK_ERROR_TAG = "isometrik-error-logs";

  /**
   * Instantiates a new Isometrik object.
   *
   * @param initialConfig the initial config
   */
  public Isometrik(@NotNull IMConfiguration initialConfig) {
    configuration = initialConfig;

    basePathManager = new BasePathManager(initialConfig);
    retrofitManager = new RetrofitManager(this);
    mediaTransferManager = new MediaTransferManager(this);

    realtimeEventsListenerManager = new RealtimeEventsListenerManager(this);

    messageEvents = new MessageEvents();
    presenceEvents = new PresenceEvents();

    isometrikConnection = new IsometrikConnection(this);

    gson = new GsonBuilder().create();
    remoteUseCases =
        new RemoteUseCases(configuration, retrofitManager, new BaseResponse(), gson, this,mediaTransferManager);

    registerConnectivityReceiver();
  }

  private void registerConnectivityReceiver() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      try {

        ConnectivityManager connectivityManager = (ConnectivityManager) configuration.getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
          connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(),
              new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(@NotNull Network network) {

                  reConnect();
                }

                @Override
                public void onLost(@NotNull Network network) {

                }
              });
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      IntentFilter filter = new IntentFilter();
      filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
      configuration.getContext().registerReceiver(new ConnectivityReceiver(this), filter);
    }
  }

  /**
   * Sets configuration.
   *
   * @param configuration the configuration
   */
  public void setConfiguration(@NotNull IMConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Gets gson.
   *
   * @return the gson
   */
  public Gson getGson() {
    return gson;
  }

  /**
   * Gets isometrik success tag.
   *
   * @return the isometrik success tag
   */
  public static String getIsometrikSuccessTag() {
    return ISOMETRIK_SUCCESS_TAG;
  }

  /**
   * Gets isometrik error tag.
   *
   * @return the isometrik error tag
   */
  public static String getIsometrikErrorTag() {
    return ISOMETRIK_ERROR_TAG;
  }

  /**
   * Gets base url.
   *
   * @return the base url
   */
  @NotNull
  public String getBaseUrl() {
    return this.basePathManager.getBasePath();
  }

  /**
   * Gets connection base url.
   *
   * @return the connection base url
   */
  @NotNull
  public String getConnectionBaseUrl() {
    return this.basePathManager.getConnectionsBasePath();
  }

  public int getPort(){
    return this.basePathManager.getPort();
  }

  /**
   * Gets configuration.
   *
   * @return the configuration
   */
  public IMConfiguration getConfiguration() {
    return configuration;
  }

  /**
   * Gets message events.
   *
   * @return the message events
   */
  public MessageEvents getMessageEvents() {
    return messageEvents;
  }

  /**
   * Gets presence events.
   *
   * @return the presence events
   */
  public PresenceEvents getPresenceEvents() {
    return presenceEvents;
  }

  /**
   * Create connection.
   *
   * @param clientId the client id
   * @param userToken the user token
   */
  public void createConnection(String clientId, String userToken) {
    configuration.setClientId(clientId);
    configuration.setUserToken(userToken);
    IsometrikError isometrikError = configuration.validateConnectionConfiguration();
    if (isometrikError == null) {
      isometrikConnection.createConnection(this);
    } else {
      realtimeEventsListenerManager.getConnectionListenerManager().announce(isometrikError);
    }
  }

  /**
   * Drop connection for realtime events.
   */
  public void dropConnection() {
    isometrikConnection.dropConnection();
  }

  /**
   * Re connect for realtime events.
   */
  public void reConnect() {

    isometrikConnection.reConnect();
  }

  /**
   * Destroy the SDK to cancel all ongoing requests and stop heartbeat timer.
   */
  public void destroy() {
    try {

      isometrikConnection.dropConnection(false);
      retrofitManager.destroy(false);
      mediaTransferManager.destroy(false);
      executor.shutdown();
    } catch (Exception error) {
      error.printStackTrace();
    }
  }

  /**
   * Force destroy the SDK to evict the connection pools and close executors.
   */
  public void forceDestroy() {
    try {

      retrofitManager.destroy(true);
      mediaTransferManager.destroy(true);
      isometrikConnection.dropConnection(true);
      executor.shutdownNow();
    } catch (Exception error) {
      error.printStackTrace();
    }
  }

  /**
   * Gets remoteUseCases.
   *
   * @return the remote use case to be used for accessing respective use cases for various remote operations
   */
  public RemoteUseCases getRemoteUseCases() {
    return remoteUseCases;
  }

  /**
   * Gets realtimeEventsListenerManager.
   *
   * @return the listenerManager for registering/unregistering listener for realtime events
   */
  public RealtimeEventsListenerManager getRealtimeEventsListenerManager() {
    return realtimeEventsListenerManager;
  }

  /**
   * Gets executor.
   *
   * @return the executor
   */
  public ExecutorService getExecutor() {
    return executor;
  }

  /**
   * Sets delivery status updated upto.
   *
   * @param deliveryStatusUpdatedUpto the delivery status updated upto
   */
  public void setDeliveryStatusUpdatedUpto(long deliveryStatusUpdatedUpto) {
    configuration.getContext()
        .getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        .edit()
        .putLong("deliveryStatusUpdatedUpto", deliveryStatusUpdatedUpto)
        .apply();
  }

  public boolean isConnected(){
    return isometrikConnection.isConnected();
  }
}
