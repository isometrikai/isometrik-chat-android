package io.isometrik.ui.messages.media.audio.util;

import android.content.Context;
import io.isometrik.chat.R;
import java.io.File;
import java.io.IOException;

/**
 * The helper class to create and delete a file for audio recording.
 */
public class AudioFileUtil {

  /**
   * Create file for recording string.
   *
   * @param context the context
   * @return the string
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  protected static String createFileForRecording(Context context) {

    File folder =
        new File(context.getFilesDir(), context.getString(R.string.ism_audio_recordings_folder));

    if (!folder.exists() && !folder.isDirectory()) {
      folder.mkdirs();
    }

    File file = new File(folder, context.getString(R.string.ism_audio_recordings_prefix)
        + System.currentTimeMillis()
        + ".3gp");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException ignore) {
      }
    }

    return file.getAbsolutePath();
  }

  /**
   * Delete recording file.
   *
   * @param audioFilePath the audio file path
   */
  public static void deleteRecordingFile(String audioFilePath) {
    File audioFile = new File(audioFilePath);

    if (audioFile.exists()) {

      //noinspection ResultOfMethodCallIgnored
      audioFile.delete();
    }
  }
}
