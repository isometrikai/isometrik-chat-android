package io.isometrik.samples.chat;

import android.app.Application;
import android.util.Log;


import io.isometrik.ui.IsometrikChatSdk;

public class MyApplication extends Application {

  private static MyApplication mInstance;
  public static String APP_NAME = "chatDemo";

  @Override
  public void onCreate() {
    super.onCreate();

    mInstance = this;
    IsometrikChatSdk.getInstance().sdkInitialize(this);

    IsometrikChatSdk.getInstance()
        .createConfiguration(getString(R.string.app_secret), getString(R.string.user_secret),
            getString(R.string.accountId),getString(R.string.projectId),getString(R.string.keysetId)
                ,getString(R.string.license_key),
            BuildConfig.APPLICATION_ID,getString(R.string.app_name), getString(R.string.google_places_api_key),
            getString(R.string.giphy_api_key));
  }

  @Override
  public void onTerminate() {
    IsometrikChatSdk.getInstance().onTerminate();
    super.onTerminate();
  }

  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
    if(level == TRIM_MEMORY_COMPLETE || level == TRIM_MEMORY_UI_HIDDEN){
//      IsometrikChatSdk.getInstance().onTerminate();
    }
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
  }



  public static synchronized MyApplication getInstance() {
    return mInstance;
  }

  public String getApiToken(){
    return "token";
  }
}
