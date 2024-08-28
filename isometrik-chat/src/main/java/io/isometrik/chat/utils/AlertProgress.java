package io.isometrik.chat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import io.isometrik.chat.R;

/**
 * The helper class to return an instance of alert progress.
 */
public class AlertProgress {

  /**
   * Gets progress dialog.
   *
   * @param mContext the m context
   * @param message the message
   * @return the progress dialog
   */
  public AlertDialog getProgressDialog(Context mContext, String message) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
    LayoutInflater inflater =
        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    @SuppressLint("InflateParams")
    View dialogView = inflater.inflate(R.layout.ism_progress_dialog, null);
    TextView tvProgress = dialogView.findViewById(R.id.tvProgress);
    tvProgress.setText(message);
    dialogBuilder.setView(dialogView);
    dialogBuilder.setCancelable(false);
    return dialogBuilder.create();
  }
}


