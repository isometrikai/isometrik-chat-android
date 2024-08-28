package io.isometrik.chat.models.download.mediautils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import io.isometrik.chat.enums.DownloadMediaType;
import java.io.File;

/**
 * The helper class for download folder paths based on media types.
 */
public class DownloadFolderPathUtility {
  private static String imageFolderPath = null;
  private static String videoFolderPath = null;
  private static String fileFolderPath = null;
  private static String audioFolderPath = null;

  /**
   * Gets download folder path.
   *
   * @param context the context
   * @param applicationName the application name
   * @param downloadMediaType the download media type
   * @return the download folder path
   */
  public static String getDownloadFolderPath(Context context, String applicationName,
      DownloadMediaType downloadMediaType) {

    switch (downloadMediaType) {
      case Image:
      case Gif:
      case Sticker:
      case Whiteboard: {
        if (imageFolderPath == null) {
          final File imageFolder;
          if (Environment.getExternalStorageState().

              equals(Environment.MEDIA_MOUNTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
              imageFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                  + "/"
                  + applicationName
                  + "/DownloadedMedia/");
            } else {
              imageFolder = new File(
                  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                      + "/"
                      + applicationName);
            }
          } else {

            imageFolder = new File(
                context.getFilesDir().getPath() + "/" + applicationName + "/DownloadedMedia/");
          }

          if (!imageFolder.exists() || !imageFolder.isDirectory()) {

            //noinspection ResultOfMethodCallIgnored
            imageFolder.mkdirs();
          }

          imageFolderPath = imageFolder.getAbsolutePath();
        }
        return imageFolderPath;
      }
      case Audio: {
        if (audioFolderPath == null) {
          final File audioFolder;
          if (Environment.getExternalStorageState().

              equals(Environment.MEDIA_MOUNTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
              audioFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                  + "/"
                  + applicationName
                  + "/DownloadedMedia/");
            } else {
              audioFolder = new File(
                  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                      + "/"
                      + applicationName);
            }
          } else {

            audioFolder = new File(
                context.getFilesDir().getPath() + "/" + applicationName + "/DownloadedMedia/");
          }

          if (!audioFolder.exists() || !audioFolder.isDirectory()) {

            //noinspection ResultOfMethodCallIgnored
            audioFolder.mkdirs();
          }

          audioFolderPath = audioFolder.getAbsolutePath();
        }
        return audioFolderPath;
      }

      case Video: {
        if (videoFolderPath == null) {
          final File videoFolder;
          if (Environment.getExternalStorageState().

              equals(Environment.MEDIA_MOUNTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
              videoFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
                  + "/"
                  + applicationName
                  + "/DownloadedMedia/");
            } else {
              videoFolder = new File(
                  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                      + "/"
                      + applicationName);
            }
          } else {

            videoFolder = new File(
                context.getFilesDir().getPath() + "/" + applicationName + "/DownloadedMedia/");
          }

          if (!videoFolder.exists() || !videoFolder.isDirectory()) {

            //noinspection ResultOfMethodCallIgnored
            videoFolder.mkdirs();
          }

          videoFolderPath = videoFolder.getAbsolutePath();
        }
        return videoFolderPath;
      }

      case File: {
        if (fileFolderPath == null) {
          final File fileFolder;
          if (Environment.getExternalStorageState().

              equals(Environment.MEDIA_MOUNTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
              fileFolder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                  + "/"
                  + applicationName
                  + "/DownloadedMedia/");
            } else {
              fileFolder = new File(
                  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                      + "/"
                      + applicationName);
            }
          } else {

            fileFolder = new File(
                context.getFilesDir().getPath() + "/" + applicationName + "/DownloadedMedia/");
          }

          if (!fileFolder.exists() || !fileFolder.isDirectory()) {

            //noinspection ResultOfMethodCallIgnored
            fileFolder.mkdirs();
          }

          fileFolderPath = fileFolder.getAbsolutePath();
        }
        return fileFolderPath;
      }
    }

    return null;
  }
}
