package io.isometrik.chat;

import android.content.Context;
import io.isometrik.chat.enums.IMLogVerbosity;
import io.isometrik.chat.enums.IMRealtimeEventsVerbosity;
import io.isometrik.chat.response.error.IsometrikError;
import io.isometrik.chat.response.error.IsometrikErrorBuilder;

/**
 * The configuration class for Isometrik sdk initialization.
 */
public class IMConfiguration {
  private static final int REQUEST_TIMEOUT = 10;
  private static final int CONNECT_TIMEOUT = 10;

  private final Context context;
  /**
   * License Key provided by Isometrik.
   */
  private final String licenseKey;
  private String clientId;
  private String userName;
  private String password;
  /**
   * If proxies are forcefully caching requests, set to true to allow the client to randomize the
   * subdomain.
   * This configuration is not supported if custom origin is enabled.
   */
  private boolean cacheBusting;

  /**
   * set to true to switch the client to HTTPS:// based communications.
   */

  private final boolean secure;
  /**
   * toggle to enable verbose logging for api request.
   */

  private IMLogVerbosity logVerbosity;

  /**
   * toggle to enable verbose logging for realtime events.
   */

  private IMRealtimeEventsVerbosity realtimeEventsVerbosity;

  /**
   * Stores the maximum number of seconds which the client should wait for connection before timing
   * out.
   */
  private int connectTimeout;

  /**
   * Reference on number of seconds which is used by client during operations to
   * check whether response potentially failed with 'timeout' or not.
   */
  private int requestTimeout;

  private final String appSecret;
  private final String userSecret;
  private final String googlePlacesApiKey;
  private final String giphyApiKey;
  private String userToken;
  private final String accountId;
  private final String projectId;
  private final String keysetId;

  /**
   * Instantiates a new Im configuration.
   *
   * @param context the context
   * @param licenseKey the license key
   * @param appSecret the app secret
   * @param userSecret the user secret
   * @param accountId the accountId
   * @param projectId the projectId
   * @param keysetId the keysetId
   * @param userName the userName
   * @param password the password
   * @param googlePlacesApiKey the google places api key
   * @param giphyApiKey the giphy api key
   */
  public IMConfiguration(Context context, String licenseKey, String appSecret, String userSecret,
                         String accountId, String projectId, String keysetId, String userName,
                         String password, String googlePlacesApiKey, String giphyApiKey) {

    requestTimeout = REQUEST_TIMEOUT;

    connectTimeout = CONNECT_TIMEOUT;

    logVerbosity = IMLogVerbosity.NONE;

    realtimeEventsVerbosity = IMRealtimeEventsVerbosity.NONE;

    cacheBusting = false;
    secure = true;
    this.context = context;

    this.licenseKey = licenseKey;
    this.appSecret = appSecret;
    this.userSecret = userSecret;
    this.accountId = accountId;
    this.projectId = projectId;
    this.keysetId = keysetId;
    this.userName = userName;
    this.password = password;
    this.googlePlacesApiKey = googlePlacesApiKey;
    this.giphyApiKey = giphyApiKey;
  }

  /**
   * Gets context.
   *
   * @return the context
   */
  public Context getContext() {
    return context;
  }

  /**
   * Is cache busting boolean.
   *
   * @return the cache busting
   */
  public boolean isCacheBusting() {
    return cacheBusting;
  }

  /**
   * Is secure boolean.
   *
   * @return the secure
   */
  public boolean isSecure() {
    return secure;
  }

  /**
   * Gets log verbosity.
   *
   * @return the log verbosity
   */
  public IMLogVerbosity getLogVerbosity() {
    return logVerbosity;
  }

  /**
   * Gets realtime message verbosity.
   *
   * @return the realtime message verbosity
   */
  public IMRealtimeEventsVerbosity getRealtimeEventsVerbosity() {
    return realtimeEventsVerbosity;
  }

  /**
   * Gets connect timeout.
   *
   * @return the connect timeout
   */
  public int getConnectTimeout() {
    return connectTimeout;
  }

  /**
   * Gets request timeout.
   *
   * @return the request timeout
   */
  public int getRequestTimeout() {
    return requestTimeout;
  }

  /**
   * Gets license key.
   *
   * @return the license key
   */
  public String getLicenseKey() {
    return licenseKey;
  }

  /**
   * Gets client id.
   *
   * @return the client id
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * Sets client id.
   *
   * @param clientId the client id
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Gets username.
   *
   * @return the username
   */

  public String getUserName() {
    return userName;
  }

  /**
   * Sets user name.
   *
   * @param userName username
   */

  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * Gets password.
   *
   * @return the password
  * */

  public String getPassword() {
    return password;
  }


  /**
   * Sets password.
   *
   * @param password the password
   */

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets app secret.
   *
   * @return the app secret
   */
  public String getAppSecret() {
    return appSecret;
  }

  /**
   * Gets user secret.
   *
   * @return the user secret
   */
  public String getUserSecret() {
    return userSecret;
  }

  /**
   * Sets cache busting.
   *
   * @param cacheBusting the cache busting
   */
  public void setCacheBusting(boolean cacheBusting) {
    this.cacheBusting = cacheBusting;
  }

  /**
   * Sets log verbosity.
   *
   * @param logVerbosity the log verbosity
   */
  public void setLogVerbosity(IMLogVerbosity logVerbosity) {
    this.logVerbosity = logVerbosity;
  }

  /**
   * Sets connect timeout.
   *
   * @param connectTimeout the connect timeout
   */
  public void setConnectTimeout(int connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  /**
   * Sets request timeout.
   *
   * @param requestTimeout the request timeout
   */
  public void setRequestTimeout(int requestTimeout) {
    this.requestTimeout = requestTimeout;
  }

  /**
   * Sets realtime events verbosity.
   *
   * @param realtimeEventsVerbosity the realtime events verbosity
   */
  public void setRealtimeEventsVerbosity(IMRealtimeEventsVerbosity realtimeEventsVerbosity) {
    this.realtimeEventsVerbosity = realtimeEventsVerbosity;
  }

  /**
   * Validate remote network call common params isometrik error.
   *
   * @param userTokenAvailable the user token available
   * @return the isometrik error
   */
  public IsometrikError validateRemoteNetworkCallCommonParams(boolean userTokenAvailable) {

    if (appSecret == null || appSecret.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_APP_SECRET_MISSING;
    } else if (licenseKey == null || licenseKey.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_LICENSE_KEY_MISSING;
    } else {
      if (userTokenAvailable) {
        return null;
      } else {
        if (userSecret == null || userSecret.isEmpty()) {

          return IsometrikErrorBuilder.IMERROBJ_USER_SECRET_MISSING;
        } else {
          return null;
        }
      }
    }
  }

  /**
   * Validate connection configuration isometrik error.
   *
   * @return the isometrik error
   */
  IsometrikError validateConnectionConfiguration() {
    if (licenseKey == null || licenseKey.isEmpty()) {

      return IsometrikErrorBuilder.IMERROBJ_LICENSE_KEY_MISSING;
    } else if (accountId == null || accountId.isEmpty()) {
      return IsometrikErrorBuilder.IMERROBJ_CONNECTION_STRING_MISSING;
    } else if (projectId == null || projectId.isEmpty()) {
      return IsometrikErrorBuilder.IMERROBJ_CONNECTION_STRING_MISSING;
    } else if (keysetId == null || keysetId.isEmpty()) {
      return IsometrikErrorBuilder.IMERROBJ_CONNECTION_STRING_MISSING;
    }  else {
      return null;
    }
  }


  /**
   * Gets google places api key.
   *
   * @return the google places api key
   */
  public String getGooglePlacesApiKey() {
    return googlePlacesApiKey;
  }

  /**
   * Gets giphy api key.
   *
   * @return the giphy api key
   */
  public String getGiphyApiKey() {
    return giphyApiKey;
  }

  /**
   * Gets user token.
   *
   * @return the user token
   */
  public String getUserToken() {
    return userToken;
  }

  /**
   * Sets user token.
   *
   * @param userToken the user token
   */
  public void setUserToken(String userToken) {
    this.userToken = userToken;
  }

  /**
   * Gets accountId.
   *
   * @return accountId
   */
  public String getAccountId() {
    return accountId;
  }

  /**
   * Gets keysetId.
   *
   * @return keysetId
   */
  public String getKeysetId() {
    return keysetId;
  }

  /**
   * Gets projectId.
   *
   * @return projectId
   */

  public String getProjectId() {
    return projectId;
  }
}
