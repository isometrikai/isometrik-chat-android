package io.isometrik.ui.messages.media.location;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * The helper class to check if location is enabled on device.
 */
public class LocationUtils {
  /**
   * Is location enabled boolean.
   *
   * @param context the context
   * @return the boolean
   */
  public static boolean isLocationEnabled(Context context) {
    int locationMode = 0;
    String locationProviders;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      try {
        locationMode =
            Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
      } catch (Settings.SettingNotFoundException e) {
        e.printStackTrace();
      }
      return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    } else {
      locationProviders = Settings.Secure.getString(context.getContentResolver(),
          Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
      return !TextUtils.isEmpty(locationProviders);
    }
  }
}
