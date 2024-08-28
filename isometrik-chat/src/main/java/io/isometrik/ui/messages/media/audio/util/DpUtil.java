package io.isometrik.ui.messages.media.audio.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * The helper class to convert pixels to dp and vice versa.
 */
public class DpUtil {
  /**
   * To pixel float.
   *
   * @param dp the dp
   * @param context the context
   * @return the float
   */
  public static float toPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }

  /**
   * To dp float.
   *
   * @param px the px
   * @param context the context
   * @return the float
   */
  public static float toDp(float px, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }
}