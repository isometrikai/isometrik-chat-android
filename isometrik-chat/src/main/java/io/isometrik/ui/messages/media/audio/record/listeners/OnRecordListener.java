package io.isometrik.ui.messages.media.audio.record.listeners;

/**
 * The interface On record listener.
 */
public interface OnRecordListener {
  /**
   * On start.
   *
   * @param hasRecordingPermission the has recording permission
   */
  void onStart(boolean hasRecordingPermission);

  /**
   * On cancel.
   */
  void onCancel();

  /**
   * On finish.
   *
   * @param recordTime the record time
   * @param limitReached the limit reached
   */
  void onFinish(long recordTime,boolean limitReached);

  /**
   * On less than second.
   */
  void onLessThanSecond();

  /**
   * Switched to locked mode.
   */
  void switchedToLockedMode();
}