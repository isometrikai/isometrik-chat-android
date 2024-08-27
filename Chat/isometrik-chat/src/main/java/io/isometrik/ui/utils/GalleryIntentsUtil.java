package io.isometrik.ui.utils;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import io.isometrik.ui.IsometrikUiSdk;
import java.io.File;
import java.util.ArrayList;

/**
 * The helper class for gallery intents to return intents based on mimetype for photos/videos/files
 * and validate the media selected for already deleted content and maximum files selection limit.
 */
public class GalleryIntentsUtil {

  /**
   * Gets photos intent.
   *
   * @param allowMultiSelect the allow multi select
   * @return the photos intent
   */
  public static Intent getPhotosIntent(boolean allowMultiSelect) {
    return getIntent("image/*", allowMultiSelect);
  }

  /**
   * Gets videos intent.
   *
   * @param allowMultiSelect the allow multi select
   * @return the videos intent
   */
  public static Intent getVideosIntent(boolean allowMultiSelect) {
    return getIntent("video/*", allowMultiSelect);
  }

  /**
   * Gets files intent.
   *
   * @param allowMultiSelect the allow multi select
   * @return the files intent
   */
  public static Intent getFilesIntent(boolean allowMultiSelect) {

    return getIntent("*/*", allowMultiSelect);
  }

  private static Intent getIntent(String mimeType, boolean allowMultiSelect) {
    // special intent for Samsung file manager
    Intent samsungIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");

    if (IsometrikUiSdk.getInstance()
        .getContext()
        .getPackageManager()
        .resolveActivity(samsungIntent, 0) == null) {

      Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
      intent.addCategory(Intent.CATEGORY_OPENABLE);
      intent.setType(mimeType);
      intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiSelect);
      return intent;
    } else {
      //It is device with Samsung file manager
      samsungIntent.addCategory(Intent.CATEGORY_DEFAULT);
      samsungIntent.putExtra("CONTENT_TYPE", mimeType);
      samsungIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiSelect);
      return samsungIntent;
    }
  }

  /**
   * Gets file paths.
   *
   * @param data the data
   * @param context the context
   * @return the file paths
   */
  public static ArrayList<String> getFilePaths(Intent data, Context context) {
    FileUtils fileUtils = new FileUtils(context);
    ArrayList<String> filePaths = new ArrayList<>();

    if (data.getClipData() != null) {
      ClipData mClipData = data.getClipData();
      int count = mClipData.getItemCount();
      for (int i = 0; i < count; i++) {
        String path = fileUtils.getPath(data.getClipData().getItemAt(i).getUri());

        if (new File(path).exists()) {
          filePaths.add(path);
        }
      }
    } else {
      String path = fileUtils.getPath(data.getData());
      if (new File(path).exists()) {
        filePaths.add(path);
      }
    }

    if (filePaths.size() > Constants.MAXIMUM_MEDIA_SELECTION_COUNT_AT_ONCE) {
      return null;
    } else {

      return filePaths;
    }
  }
}
