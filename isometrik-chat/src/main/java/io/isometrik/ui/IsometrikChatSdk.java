package io.isometrik.ui;

import static com.google.common.reflect.Reflection.getPackageName;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import io.isometrik.chat.IMConfiguration;
import io.isometrik.chat.Isometrik;
import io.isometrik.chat.enums.IMLogVerbosity;
import io.isometrik.chat.enums.IMRealtimeEventsVerbosity;
import io.isometrik.chat.utils.UserSession;
import io.isometrik.ui.messages.chat.ChatActionsClickListener;

/**
 * The IsometrikUiSdk singleton to expose sdk functionality to other modules.
 */
public class IsometrikChatSdk {

  private Isometrik isometrik;
  private UserSession userSession;
  private String applicationId;
  private String applicationName;
  private Context applicationContext;
  private static volatile IsometrikChatSdk isometrikChatSdk;
  private ChatActionsClickListener chatActionsClickListener;
  /**
   * private constructor.
   */
  private IsometrikChatSdk() {

    //Prevent form the reflection api.
    if (isometrikChatSdk != null) {
      throw new RuntimeException(
          "Use getInstance() method to get the single instance of this class.");
    }
  }

  /**
   * Gets instance.
   *
   * @return the IsometrikUiSdk instance
   */
  public static IsometrikChatSdk getInstance() {
    if (isometrikChatSdk == null) {
      synchronized (IsometrikChatSdk.class) {
        if (isometrikChatSdk == null) {
          isometrikChatSdk = new IsometrikChatSdk();
        }
      }
    }
    return isometrikChatSdk;
  }

  /**
   * Sdk initialize.
   *
   * @param applicationContext the application context
   */
  public void sdkInitialize(Context applicationContext) {
    if (applicationContext == null) {
      throw new RuntimeException(
          "Sdk initialization failed as application context cannot be null.");
    } else if (!(applicationContext instanceof Application)) {
      throw new RuntimeException(
          "Sdk initialization failed as context passed in parameter is not application context.");
    }

    this.applicationContext = applicationContext;

  }

  /**
   * Create configuration.
   *
   * @param appSecret the app secret
   * @param userSecret the user secret
   * @param accountId the accountId
   * @param projectId the projectId
   * @param keysetId the keysetId
   * @param userName the userName
   * @param password the password
   * @param licenseKey the license key
   * @param applicationId the application id
   * @param applicationName the application name
   * @param googlePlacesApiKey the google places api key
   * @param giphyApiKey the giphy api key
   */
  public void createConfiguration(String appSecret, String userSecret, String accountId,
                                  String projectId, String keysetId, String userName, String password,
      String licenseKey, String applicationId, String applicationName, String googlePlacesApiKey,
      String giphyApiKey) {

    if (applicationContext == null) {
      throw new RuntimeException("Initialize the sdk before creating configuration.");
    } else if (appSecret == null || appSecret.isEmpty()) {
      throw new RuntimeException("Pass a valid appSecret for isometrik sdk initialization.");
    } else if (userSecret == null || userSecret.isEmpty()) {
      throw new RuntimeException("Pass a valid userSecret for isometrik sdk initialization.");
    } else if (accountId == null || accountId.isEmpty()) {
      throw new RuntimeException("Pass a valid accountId for isometrik sdk initialization.");
    } else if (projectId == null || projectId.isEmpty()) {
      throw new RuntimeException("Pass a valid projectId for isometrik sdk initialization.");
    } else if (keysetId == null || keysetId.isEmpty()) {
      throw new RuntimeException("Pass a valid keysetId for isometrik sdk initialization.");
    } else if (userName == null || userName.isEmpty()) {
      throw new RuntimeException("Pass a valid userName for isometrik sdk initialization.");
    }else if (password == null || password.isEmpty()) {
      throw new RuntimeException("Pass a valid password for isometrik sdk initialization.");
    } else if (licenseKey == null || licenseKey.isEmpty()) {
      throw new RuntimeException("Pass a valid licenseKey for isometrik sdk initialization.");
    } else if (googlePlacesApiKey == null || googlePlacesApiKey.isEmpty()) {
      throw new RuntimeException(
          "Pass a valid googlePlacesApiKey for isometrik sdk initialization.");
    } else if (giphyApiKey == null || giphyApiKey.isEmpty()) {
      throw new RuntimeException(
          "Pass a valid giphyApiKey for isometrik sdk initialization.");
    }else if (applicationId == null || applicationId.isEmpty()) {
      throw new RuntimeException("Pass a valid applicationId for isometrik sdk initialization.");
    } else if (applicationName == null || applicationName.isEmpty()) {
      throw new RuntimeException("Pass a valid applicationName for isometrik sdk initialization.");
    }
    this.applicationId = applicationId;
    this.applicationName = applicationName;
    IMConfiguration imConfiguration =
        new IMConfiguration(applicationContext, licenseKey, appSecret, userSecret,
            accountId, projectId, keysetId, userName, password,googlePlacesApiKey,giphyApiKey);
    imConfiguration.setLogVerbosity(IMLogVerbosity.BODY);
    imConfiguration.setRealtimeEventsVerbosity(IMRealtimeEventsVerbosity.FULL);
    isometrik = new Isometrik(imConfiguration);
    userSession = new UserSession(applicationContext);
    setGooglePlacesApiKey(googlePlacesApiKey);
  }

  private void setGooglePlacesApiKey(String apiKey) {
    try {
      // Get the ApplicationInfo object which contains the meta-data
      ApplicationInfo appInfo = applicationContext.getPackageManager().getApplicationInfo(applicationContext.getPackageName(), PackageManager.GET_META_DATA);

      // Update the API key in the meta-data
      if (appInfo.metaData != null) {
        appInfo.metaData.putString("com.google.android.geo.API_KEY", apiKey);
      }
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets isometrik.
   *
   * @return the isometrik
   */
  public Isometrik getIsometrik() {
    if (isometrik == null) {
      throw new RuntimeException("Create configuration before trying to access isometrik object.");
    }

    return isometrik;
  }

  /**
   * Gets user session.
   *
   * @return the user session
   */
  public UserSession getUserSession() {
    if (userSession == null) {
      throw new RuntimeException(
          "Create configuration before trying to access user session object.");
    }

    return userSession;
  }

  /**
   * Gets context.
   *
   * @return the context
   */
  public Context getContext() {
    if (isometrik == null) {
      throw new RuntimeException("Create configuration before trying to access context object.");
    }
    return applicationContext;
  }

  /**
   * On terminate.
   */
  public void onTerminate() {
    if (isometrik == null) {
      throw new RuntimeException("Create configuration before trying to access isometrik object.");
    }


    isometrik.destroy();
  }

  /**
   * Gets application id.
   *
   * @return the application id
   */
  public String getApplicationId() {
    return applicationId;
  }

  /**
   * Gets application name.
   *
   * @return the application name
   */
  public String getApplicationName() {
    return applicationName;
  }

  public boolean isConnected(){
    if (isometrik != null) {
      return isometrik.isConnected();
    }
    return false;
  }

  /**
   * register conversations Action callbacl
  * */

  public void addClickListeners(ChatActionsClickListener chatActionsClickListener){
     this.chatActionsClickListener = chatActionsClickListener;
  }

  public ChatActionsClickListener getChatActionsClickListener(){
    return chatActionsClickListener;
  }

}
