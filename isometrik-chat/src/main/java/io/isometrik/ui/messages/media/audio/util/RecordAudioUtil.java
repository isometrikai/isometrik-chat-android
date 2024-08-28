package io.isometrik.ui.messages.media.audio.util;

import android.content.Context;
import android.media.MediaRecorder;
import java.io.IOException;

/**
 * The helper class to start and stop audio recording.
 */
public class RecordAudioUtil {

  private MediaRecorder mediaRecorder;
  private boolean recordingAudio;

  private void initMediaRecorder() {
    mediaRecorder = new MediaRecorder();
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    //Uncomment  if noise in recorded audio bug reported
    //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
  }

  /**
   * Start audio record string.
   *
   * @param context the context
   * @return the string
   */
  public String startAudioRecord(Context context) {
    if (mediaRecorder == null) {
      initMediaRecorder();
    }

    String audioFilePath = AudioFileUtil.createFileForRecording(context);
    mediaRecorder.setOutputFile(audioFilePath);
    try {
      mediaRecorder.prepare();
      mediaRecorder.start();
    } catch (IOException ignore) {
      return null;
    }
    recordingAudio = true;
    return audioFilePath;
  }

  /**
   * Stop audio record boolean.
   *
   * @param canceled the canceled
   * @param audioFilePath the audio file path
   * @return the boolean
   */
  public boolean stopAudioRecord(boolean canceled, String audioFilePath) {

    recordingAudio = false;
    try {
      mediaRecorder.stop();
      mediaRecorder.reset();
      mediaRecorder.release();
    } catch (Exception ignore) {
    }
    if (canceled) {
      AudioFileUtil.deleteRecordingFile(audioFilePath);
    }

    mediaRecorder = null;
    return true;
  }

  /**
   * Is recording audio boolean.
   *
   * @return the boolean
   */
  public boolean isRecordingAudio() {
    return recordingAudio;
  }
}
