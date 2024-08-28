package io.isometrik.chat.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.isometrik.chat.R;

/**
 * The image helper class to create a new file for camera/whiteboard images and return path of newly
 * created files.
 */
public class ImageUtil {

  /**
   * Request upload image.
   *
   * @param imagePath the image path
   * @param streamImage the stream image
   * @param publicId the public id
   * @param uploadImageResult the upload image result
   */
  public static void requestUploadImage(String imagePath, boolean streamImage, String publicId,
      UploadImageResult uploadImageResult) {
    //MediaManager.get()
    //    .upload(imagePath)
    //    .option("resource_type", "image")
    //    .option("folder", streamImage ? "streams" : "users")
    //    //.option("public_id", publicId)
    //    .option("overwrite", true)
    //    .callback(new UploadCallback() {
    //      @Override
    //      public void onStart(String requestId) {
    //        //TODO nothing
    //
    //      }
    //
    //      @Override
    //      public void onProgress(String requestId, long bytes, long totalBytes) {
    //        //TODO nothing
    //
    //      }
    //
    //      @Override
    //      public void onSuccess(String requestId, Map resultData) {
    //        uploadImageResult.uploadSuccess(requestId, resultData);
    //      }
    //
    //      @Override
    //      public void onError(String requestId, ErrorInfo error) {
    //        uploadImageResult.uploadError(requestId, error);
    //      }
    //
    //      @Override
    //      public void onReschedule(String requestId, ErrorInfo error) {
    //        uploadImageResult.uploadError(requestId, error);
    //      }
    //    })
    //    .dispatch();
  }

  /**
   * Create image file file.
   *
   * @param fileName the file name
   * @param streamImage the stream image
   * @param context the context
   * @return the file
   * @throws IOException the io exception
   */
  public static File createImageFile(String fileName, boolean streamImage, Context context)
      throws IOException {

    final File imageFolder;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

      imageFolder = new File(context.getExternalFilesDir(null) + "/" + context.getResources()
          .getString(R.string.ism_app_name) + "/" + (streamImage ? "streams" : "users"));
    } else {

      imageFolder = new File(context.getFilesDir().getPath() + "/" + context.getResources()
          .getString(R.string.ism_app_name) + "/" + (streamImage ? "streams" : "users"));
    }

    if (!imageFolder.exists() || !imageFolder.isDirectory()) {

      imageFolder.mkdirs();
    }

    return File.createTempFile(fileName,  /* prefix */
        ".jpg",         /* suffix */
        imageFolder      /* directory */);
  }

  /**
   * Save captured bitmap string.
   *
   * @param capturedBitmap the captured bitmap
   * @param context the context
   * @return the string
   */
  public static String saveCapturedBitmap(Bitmap capturedBitmap, Context context) {

    OutputStream fOutputStream;

    try {
      File file = createImageFile(String.valueOf(System.currentTimeMillis()), false, context);

      fOutputStream = new FileOutputStream(file);

      capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

      fOutputStream.flush();
      fOutputStream.close();
      return file.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Creates a new media file.
   *
   * @param fileName the file name
   * @param isImage the whether captured media is a image or video
   * @param context the context
   * @return the file
   * @throws IOException the io exception
   */
  public static File createFileForMediaCapturedFromCamera(String fileName, boolean isImage,
      Context context) throws IOException {

    final File imageFolder;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

      imageFolder = new File(context.getExternalFilesDir(null) + "/" + context.getResources()
          .getString(R.string.ism_app_name) + "/media");
    } else {

      imageFolder = new File(context.getFilesDir().getPath() + "/" + context.getResources()
          .getString(R.string.ism_app_name) + "/media");
    }

    if (!imageFolder.exists() || !imageFolder.isDirectory()) {

      imageFolder.mkdirs();
    }

    if (isImage) {
      return File.createTempFile(fileName,  /* prefix */
          ".jpg",         /* suffix */
          imageFolder      /* directory */);
    } else {
      return File.createTempFile(fileName,  /* prefix */
          ".mp4",         /* suffix */
          imageFolder      /* directory */);
    }
  }

  /**
   * Save whiteboard bitmap to a file.
   *
   * @param whiteboardBitmap the bitmap of the content drawn on the whiteboard
   * @param context the context
   * @return the path of the bitmap saved
   */
  public static String createFileForWhiteboardBitmap(Bitmap whiteboardBitmap, Context context) {
    final File imageFolder;
    //if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    //
    //  imageFolder = new File(context.getExternalFilesDir(null) + "/" + context.getResources()
    //      .getString(R.string.ism_app_name) + "/media");
    //} else {

    imageFolder = new File(context.getFilesDir().getPath() + "/" + context.getResources()
        .getString(R.string.ism_app_name) + "/media");
    //}

    if (!imageFolder.exists() || !imageFolder.isDirectory()) {

      imageFolder.mkdirs();
    }

    OutputStream fOutputStream;

    try {
      File file = File.createTempFile(String.valueOf(System.currentTimeMillis()),  /* prefix */
          ".jpg",         /* suffix */
          imageFolder      /* directory */);

      fOutputStream = new FileOutputStream(file);

      whiteboardBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

      fOutputStream.flush();
      fOutputStream.close();
      return file.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
