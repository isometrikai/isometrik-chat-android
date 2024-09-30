package io.isometrik.samples.chat;

import android.app.Application;


import io.isometrik.ui.IsometrikChatSdk;

public class MyApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    IsometrikChatSdk.getInstance().sdkInitialize(this);
    String userName = "2" + getString(R.string.accountId) + getString(R.string.projectId);
    String password = getString(R.string.license_key) + getString(R.string.keysetId);
    IsometrikChatSdk.getInstance()
        .createConfiguration(getString(R.string.app_secret), getString(R.string.user_secret),
            getString(R.string.accountId),getString(R.string.projectId),getString(R.string.keysetId),
                userName,password ,getString(R.string.license_key),
            BuildConfig.APPLICATION_ID,getString(R.string.app_name), getString(R.string.google_places_api_key),
            getString(R.string.giphy_api_key));
  }

  @Override
  public void onTerminate() {
    IsometrikChatSdk.getInstance().onTerminate();
    super.onTerminate();
  }
}
