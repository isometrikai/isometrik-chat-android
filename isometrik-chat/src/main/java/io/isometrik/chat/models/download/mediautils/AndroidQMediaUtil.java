package io.isometrik.chat.models.download.mediautils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * The helper class to add media to gallery for Android Q+ devices.
 */
@TargetApi(29)
public class AndroidQMediaUtil {

  /**
   * Add media to gallery string.
   *
   * @param mediaFileName the media file name
   * @param context the context
   * @param downloadedFile the downloaded file
   * @param appName the app name
   * @param mimeType the mime type
   * @return the string
   */
  public static String addMediaToGallery(String mediaFileName, Context context, File downloadedFile,
      String appName, String mimeType) {

    ContentValues contentValues;
    contentValues = new ContentValues();
    Uri collection;

    String relativePath;

    if (mimeType.startsWith("image")) {
      contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,
          Environment.DIRECTORY_PICTURES + "/" + appName);
      contentValues.put(MediaStore.Images.Media.TITLE, mediaFileName);
      contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, mediaFileName);
      contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
      contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
      contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
      contentValues.put(MediaStore.Images.Media.IS_PENDING, 1);
      collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

      relativePath = Environment.getExternalStorageDirectory()
          + File.separator
          + Environment.DIRECTORY_PICTURES
          + File.separator
          + appName
          + File.separator
          + mediaFileName;
    } else if (mimeType.startsWith("video")) {
      contentValues.put(MediaStore.Video.Media.RELATIVE_PATH,
          Environment.DIRECTORY_MOVIES + "/" + appName);
      contentValues.put(MediaStore.Video.Media.TITLE, mediaFileName);
      contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, mediaFileName);
      contentValues.put(MediaStore.Video.Media.MIME_TYPE, mimeType);
      contentValues.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
      contentValues.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
      contentValues.put(MediaStore.Video.Media.IS_PENDING, 1);
      collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

      relativePath = Environment.getExternalStorageDirectory()
          + File.separator
          + Environment.DIRECTORY_MOVIES
          + File.separator
          + appName
          + File.separator
          + mediaFileName;
    } else if (mimeType.startsWith("audio")) {
      contentValues.put(MediaStore.Audio.Media.RELATIVE_PATH,
          Environment.DIRECTORY_MUSIC + "/" + appName);
      contentValues.put(MediaStore.Audio.Media.TITLE, mediaFileName);
      contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, mediaFileName);
      contentValues.put(MediaStore.Audio.Media.MIME_TYPE, mimeType);
      contentValues.put(MediaStore.Audio.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
      contentValues.put(MediaStore.Audio.Media.DATE_TAKEN, System.currentTimeMillis());
      contentValues.put(MediaStore.Audio.Media.IS_PENDING, 1);
      collection = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

      relativePath = Environment.getExternalStorageDirectory()
          + File.separator
          + Environment.DIRECTORY_MUSIC
          + File.separator
          + appName
          + File.separator
          + mediaFileName;
    } else if (mimeType.startsWith("text")
        || mimeType.startsWith("application")
        || mimeType.startsWith("font")) {
      contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,
          Environment.DIRECTORY_DOCUMENTS + "/" + appName);
      contentValues.put(MediaStore.Files.FileColumns.TITLE, mediaFileName);
      contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, mediaFileName);
      contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType);
      contentValues.put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
      contentValues.put(MediaStore.Files.FileColumns.DATE_TAKEN, System.currentTimeMillis());
      contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 1);
      collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

      relativePath = Environment.getExternalStorageDirectory()
          + File.separator
          + Environment.DIRECTORY_DOCUMENTS
          + File.separator
          + appName
          + File.separator
          + mediaFileName;
    } else {
      contentValues.put(MediaStore.Downloads.RELATIVE_PATH,
          Environment.DIRECTORY_DOWNLOADS + "/" + appName);
      contentValues.put(MediaStore.Files.FileColumns.TITLE, mediaFileName);
      contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, mediaFileName);
      contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, mimeType);
      contentValues.put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
      contentValues.put(MediaStore.Files.FileColumns.DATE_TAKEN, System.currentTimeMillis());
      contentValues.put(MediaStore.Files.FileColumns.IS_PENDING, 1);
      collection = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

      relativePath = Environment.getExternalStorageDirectory()
          + File.separator
          + Environment.DIRECTORY_DOWNLOADS
          + File.separator
          + appName
          + File.separator
          + mediaFileName;
    }


    if (collection != null) {
      ContentResolver resolver = context.getContentResolver();

      Uri mediaUri = resolver.insert(collection, contentValues);
      ParcelFileDescriptor pfd;

      try {
        pfd = context.getContentResolver().openFileDescriptor(mediaUri, "w");

        FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());

        FileInputStream in = new FileInputStream(downloadedFile);

        byte[] buf = new byte[8192];
        int len;
        while ((len = in.read(buf)) > 0) {

          out.write(buf, 0, len);
        }

        out.close();
        in.close();
        pfd.close();
      } catch (Exception e) {

        e.printStackTrace();
      }

      contentValues.clear();

      MediaScannerConnection.scanFile(context, new String[] { relativePath }, null, null);

      contentValues.put("is_pending", 0);
      context.getContentResolver().update(mediaUri, contentValues, null, null);
      deleteMediaFromDisk(downloadedFile.getAbsolutePath());

      return relativePath;
    }
    return downloadedFile.getAbsolutePath();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static void deleteMediaFromDisk(String mediaPath) {
    try {
      File file = new File(mediaPath);

      if (file.exists()) {

        file.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
