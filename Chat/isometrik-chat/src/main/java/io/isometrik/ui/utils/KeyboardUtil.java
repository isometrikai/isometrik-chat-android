package io.isometrik.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * The helper class to hide/show keyboard.
 */
public class KeyboardUtil {

  /**
   * Show soft keyboard.
   *
   * @param activity the activity
   * @param editText the edit text
   */
  // Shows the soft keyboard.
  public static void showSoftKeyboard(Activity activity, EditText editText) {
    try {
      editText.requestFocus();
      InputMethodManager inputMethodManager =
          (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    } catch (Exception ignore) {
    }
  }

  /**
   * Hide keyboard.
   *
   * @param activity the activity
   */
  public static void hideKeyboard(Activity activity) {
    try {
      InputMethodManager inputMethodManager =
          (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
      //Find the currently focused view, so we can grab the correct window token from it.
      View view = activity.getCurrentFocus();
      //If no view currently has focus, create a new one, just so we can grab a window token from it
      if (view == null) {
        view = new View(activity);
      }
      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    } catch (Exception ignore) {
    }
  }
}

