package io.isometrik.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;

/**
 * The helper class to generate dynamic placeholders using initials of text, code to check if image
 * url is valid.
 */
public class PlaceholderUtils {

  /**
   * The constant BITMAP_HEIGHT.
   */
  public static final int BITMAP_HEIGHT = 30;
  /**
   * The constant BITMAP_WIDTH.
   */
  public static final int BITMAP_WIDTH = 30;

  /**
   * Is valid image url boolean.
   *
   * @param url the url
   * @return the boolean
   */
  public static boolean isValidImageUrl(String url) {
    return url != null && !url.isEmpty() && !url.equals(Constants.DEFAULT_PLACEHOLDER_IMAGE_URL);
  }

  /**
   * Sets text round drawable.
   *
   * @param context the context
   * @param firstName the first name
   * @param ivProfile the iv profile
   * @param position the position
   * @param fontSize the font size
   */
  public static void setTextRoundDrawable(Context context, String firstName, ImageView ivProfile,
      int position, int fontSize) {

    String initials = "";
    if (firstName != null) {
      if (firstName.equals(
          IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user))) {
        firstName = firstName.substring(1);
      }
      if (firstName.length() >= 2) {
        initials = firstName.substring(0, 2);
      } else {
        initials = initials + firstName.charAt(0);
      }
    }
    try {
      float density = context.getResources().getDisplayMetrics().density;
      ivProfile.setImageDrawable(TextDrawable.builder()
          .beginConfig()
          .textColor(Color.WHITE)
          .useFont(Typeface.DEFAULT)
          .fontSize((int) ((fontSize) * density))
          .bold()
          .toUpperCase()
          .endConfig()
          .buildRound(initials,
              position == -1 ? ContextCompat.getColor(context, R.color.ism_contact_background)
                  : Color.parseColor(ColorsUtil.getColorCode(position % 19))));
    } catch (Exception ignore) {
    }
  }

  /**
   * Sets text round drawable.
   *
   * @param context the context
   * @param firstName the first name
   * @param ivProfile the iv profile
   * @param fontSize the font size
   */
  public static void setTextRoundDrawable(Context context, String firstName, ImageView ivProfile,
      int fontSize) {

    String initials = "";
    if (firstName != null) {
      if (firstName.equals(
          IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_deleted_user))) {
        firstName = firstName.substring(1);
      }
      if (firstName.length() >= 2) {
        initials = firstName.substring(0, 2);
      } else {
        initials = initials + firstName.charAt(0);
      }
    }
    try {
      float density = context.getResources().getDisplayMetrics().density;
      ivProfile.setImageDrawable(TextDrawable.builder()
          .beginConfig()
          .textColor(Color.WHITE)
          .useFont(Typeface.DEFAULT)
          .fontSize((int) ((fontSize) * density))
          .bold()
          .toUpperCase()
          .endConfig()
          .buildRound(initials, ContextCompat.getColor(context, R.color.ism_contact_background)));
    } catch (Exception ignore) {
    }
  }

  /**
   * Sets text round rectangle drawable.
   *
   * @param context the context
   * @param firstName the first name
   * @param ivProfile the iv profile
   * @param fontSize the font size
   * @param radius the radius
   */
  public static void setTextRoundRectangleDrawable(Context context, String firstName,
      ImageView ivProfile, int fontSize, int radius) {

    String initials = "";
    if (firstName != null) {
      if (firstName.length() >= 2) {
        initials = firstName.substring(0, 2);
      } else {
        initials = initials + firstName.charAt(0);
      }
    }
    try {
      float density = context.getResources().getDisplayMetrics().density;
      ivProfile.setImageDrawable(TextDrawable.builder()
          .beginConfig()
          //.height((int) ((100) * density))
          //.width((int) ((100) * density))
          .textColor(Color.WHITE)
          .useFont(Typeface.DEFAULT)
          .fontSize((int) ((fontSize) * density))
          .bold()
          .toUpperCase()
          .endConfig()
          .buildRoundRect(initials, ContextCompat.getColor(context, R.color.ism_contact_background),
              (int) ((radius) * density)));
    } catch (Exception ignore) {
    }
  }

  /**
   * Gets bitmap.
   *
   * @param context the context
   * @param firstName the first name
   * @param timestamp the timestamp
   * @return the bitmap
   */
  public static Bitmap getBitmap(Context context, String firstName, long timestamp) {

    String initials = "";
    if (firstName != null) {
      if (firstName.length() >= 2) {
        initials = firstName.substring(0, 2);
      } else {
        initials = initials + firstName.charAt(0);
      }
    }
    try {
      float density = context.getResources().getDisplayMetrics().density;
      Drawable drawable = TextDrawable.builder()
          .beginConfig()
          .textColor(Color.WHITE)
          .useFont(Typeface.DEFAULT)
          .fontSize((int) ((BITMAP_WIDTH / 3) * density))
          .height((int) ((BITMAP_HEIGHT) * density))
          .width((int) ((BITMAP_WIDTH) * density))
          .bold()
          .toUpperCase()
          .endConfig()
          .buildRound(initials, Color.parseColor(ColorsUtil.getColorCode((int) timestamp % 19)));
      return drawableToBitmap(drawable);
    } catch (Exception ignore) {
    }
    return null;
  }

  /**
   * Drawable to bitmap bitmap.
   *
   * @param drawable the drawable
   * @return the bitmap
   */
  public static Bitmap drawableToBitmap(Drawable drawable) {
    Bitmap bitmap;

    if (drawable instanceof BitmapDrawable) {
      BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
      if (bitmapDrawable.getBitmap() != null) {
        return bitmapDrawable.getBitmap();
      }
    }

    if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
      bitmap = Bitmap.createBitmap(1, 1,
          Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
    } else {
      bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
          Bitmap.Config.ARGB_8888);
    }

    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);
    return bitmap;
  }
}