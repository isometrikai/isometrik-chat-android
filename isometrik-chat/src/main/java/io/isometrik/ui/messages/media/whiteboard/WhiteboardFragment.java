package io.isometrik.ui.messages.media.whiteboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBottomsheetWhiteboardBinding;
import io.isometrik.ui.messages.media.MediaSelectedToBeShared;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.ImageUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * The fragment to draw using free hand sketch over whiteboard and share it as a message.
 */
public class WhiteboardFragment extends BottomSheetDialogFragment {
  /**
   * The constant TAG.
   */
  public static final String TAG = "WhiteboardFragment";

  /**
   * Instantiates a whiteboard fragment.
   */
  public WhiteboardFragment() {
    // Required empty public constructor
  }

  private IsmBottomsheetWhiteboardBinding ismBottomsheetWhiteboardBinding;
  private Activity activity;
  private MediaSelectedToBeShared mediaSelectedToBeShared;
  private float density;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    ismBottomsheetWhiteboardBinding =
        IsmBottomsheetWhiteboardBinding.inflate(inflater, container, false);

    density = getResources().getDisplayMetrics().density;
    alertProgress = new AlertProgress();

    setUpDrawTools();
    colorSelector();
    setPaintAlpha();
    setPaintWidth();

    ismBottomsheetWhiteboardBinding.ivClose.setOnClickListener(view -> {
      try {
        dismiss();
      } catch (Exception ignore) {
      }
    });

    ismBottomsheetWhiteboardBinding.btDone.setOnClickListener(view -> {
      showProgressDialog(getString(R.string.ism_preparing_whiteboard));
      mediaSelectedToBeShared.whiteboardShareRequested(ImageUtil.createFileForWhiteboardBitmap(
          ismBottomsheetWhiteboardBinding.canvasView.getBitmap(), activity));
      hideProgressDialog();
      try {
        dismiss();
      } catch (Exception ignore) {
      }
    });

    return ismBottomsheetWhiteboardBinding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ismBottomsheetWhiteboardBinding = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    activity = null;
  }

  /**
   * Update parameters.
   *
   * @param mediaSelectedToBeShared the media selected to be shared
   */
  public void updateParameters(MediaSelectedToBeShared mediaSelectedToBeShared) {

    this.mediaSelectedToBeShared = mediaSelectedToBeShared;
  }

  private void setUpDrawTools() {

    ismBottomsheetWhiteboardBinding.cvOpacity.setCircleRadius(100f);

    ismBottomsheetWhiteboardBinding.ivEraser.setOnClickListener(view -> {
      ismBottomsheetWhiteboardBinding.canvasView.toggleEraser();
      ismBottomsheetWhiteboardBinding.ivEraser.setSelected(
          ismBottomsheetWhiteboardBinding.canvasView.isEraserOn());
      toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, false);
    });

    ismBottomsheetWhiteboardBinding.ivEraser.setOnLongClickListener(view -> {
      ismBottomsheetWhiteboardBinding.canvasView.clearCanvas();
      toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, false);
      return true;
    });

    ismBottomsheetWhiteboardBinding.ivChangeWidth.setOnClickListener(view -> {
      if (ismBottomsheetWhiteboardBinding.drawTools.getTranslationY() == (56 * density)) {
        toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, true);
      } else if (ismBottomsheetWhiteboardBinding.drawTools.getTranslationY() == (0 * density)
          && ismBottomsheetWhiteboardBinding.sbWidth.getVisibility() == VISIBLE) {
        toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, false);
      }

      ismBottomsheetWhiteboardBinding.cvWidth.setVisibility(VISIBLE);
      ismBottomsheetWhiteboardBinding.cvOpacity.setVisibility(GONE);
      ismBottomsheetWhiteboardBinding.sbWidth.setVisibility(VISIBLE);
      ismBottomsheetWhiteboardBinding.sbOpacity.setVisibility(GONE);
      ismBottomsheetWhiteboardBinding.vColorPalette.llColorPalette.setVisibility(GONE);
    });

    ismBottomsheetWhiteboardBinding.ivChangeOpacity.setOnClickListener(view -> {
      if (ismBottomsheetWhiteboardBinding.drawTools.getTranslationY() == (56 * density)) {
        toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, true);
      } else if (ismBottomsheetWhiteboardBinding.drawTools.getTranslationY() == (0 * density)
          && ismBottomsheetWhiteboardBinding.sbOpacity.getVisibility() == VISIBLE) {
        toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, false);
      }

      ismBottomsheetWhiteboardBinding.cvWidth.setVisibility(GONE);
      ismBottomsheetWhiteboardBinding.cvOpacity.setVisibility(VISIBLE);
      ismBottomsheetWhiteboardBinding.sbWidth.setVisibility(GONE);
      ismBottomsheetWhiteboardBinding.sbOpacity.setVisibility(VISIBLE);
      ismBottomsheetWhiteboardBinding.vColorPalette.llColorPalette.setVisibility(GONE);
    });

    ismBottomsheetWhiteboardBinding.ivChangeColor.setOnClickListener(view -> {

      if (ismBottomsheetWhiteboardBinding.drawTools.getTranslationY() == (56 * density)) {
        toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, true);
      } else if (ismBottomsheetWhiteboardBinding.drawTools.getTranslationY() == (0 * density)
          && ismBottomsheetWhiteboardBinding.vColorPalette.llColorPalette.getVisibility()
          == VISIBLE) {
        toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, false);
      }

      ismBottomsheetWhiteboardBinding.cvWidth.setVisibility(GONE);
      ismBottomsheetWhiteboardBinding.cvOpacity.setVisibility(GONE);
      ismBottomsheetWhiteboardBinding.sbWidth.setVisibility(GONE);
      ismBottomsheetWhiteboardBinding.sbOpacity.setVisibility(GONE);
      ismBottomsheetWhiteboardBinding.vColorPalette.llColorPalette.setVisibility(VISIBLE);
    });

    ismBottomsheetWhiteboardBinding.ivUndo.setOnClickListener(view -> {
      ismBottomsheetWhiteboardBinding.canvasView.undo();
      toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, false);
    });

    ismBottomsheetWhiteboardBinding.ivRedo.setOnClickListener(view -> {
      ismBottomsheetWhiteboardBinding.canvasView.redo();
      toggleDrawTools(ismBottomsheetWhiteboardBinding.drawTools, false);
    });
  }

  private void colorSelector() {
    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBlack.setOnClickListener(view -> {
      int color = ContextCompat.getColor(activity, R.color.ism_whiteboard_color_black);
      ismBottomsheetWhiteboardBinding.canvasView.setColor(color);
      ismBottomsheetWhiteboardBinding.cvOpacity.setColor(color);
      ismBottomsheetWhiteboardBinding.cvWidth.setColor(color);
      scaleColorView(ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBlack);
    });

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorRed.setOnClickListener(view -> {
      int color = ContextCompat.getColor(activity, R.color.ism_whiteboard_color_red);
      ismBottomsheetWhiteboardBinding.canvasView.setColor(color);
      ismBottomsheetWhiteboardBinding.cvOpacity.setColor(color);
      ismBottomsheetWhiteboardBinding.cvWidth.setColor(color);
      scaleColorView(ismBottomsheetWhiteboardBinding.vColorPalette.ivColorRed);
    });

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorYellow.setOnClickListener(view -> {
      int color = ContextCompat.getColor(activity, R.color.ism_whiteboard_color_yellow);
      ismBottomsheetWhiteboardBinding.canvasView.setColor(color);
      ismBottomsheetWhiteboardBinding.cvOpacity.setColor(color);
      ismBottomsheetWhiteboardBinding.cvWidth.setColor(color);
      scaleColorView(ismBottomsheetWhiteboardBinding.vColorPalette.ivColorYellow);
    });

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorGreen.setOnClickListener(view -> {
      int color = ContextCompat.getColor(activity, R.color.ism_whiteboard_color_green);
      ismBottomsheetWhiteboardBinding.canvasView.setColor(color);
      ismBottomsheetWhiteboardBinding.cvOpacity.setColor(color);
      ismBottomsheetWhiteboardBinding.cvWidth.setColor(color);
      scaleColorView(ismBottomsheetWhiteboardBinding.vColorPalette.ivColorGreen);
    });

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBlue.setOnClickListener(view -> {
      int color = ContextCompat.getColor(activity, R.color.ism_whiteboard_color_blue);
      ismBottomsheetWhiteboardBinding.canvasView.setColor(color);
      ismBottomsheetWhiteboardBinding.cvOpacity.setColor(color);
      ismBottomsheetWhiteboardBinding.cvWidth.setColor(color);
      scaleColorView(ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBlue);
    });

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorPink.setOnClickListener(view -> {
      int color = ContextCompat.getColor(activity, R.color.ism_whiteboard_color_pink);
      ismBottomsheetWhiteboardBinding.canvasView.setColor(color);
      ismBottomsheetWhiteboardBinding.cvOpacity.setColor(color);
      ismBottomsheetWhiteboardBinding.cvWidth.setColor(color);
      scaleColorView(ismBottomsheetWhiteboardBinding.vColorPalette.ivColorPink);
    });

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBrown.setOnClickListener(view -> {
      int color = ContextCompat.getColor(activity, R.color.ism_whiteboard_color_brown);
      ismBottomsheetWhiteboardBinding.canvasView.setColor(color);
      ismBottomsheetWhiteboardBinding.cvOpacity.setColor(color);
      ismBottomsheetWhiteboardBinding.cvWidth.setColor(color);
      scaleColorView(ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBrown);
    });
  }

  private void setPaintAlpha() {
    ismBottomsheetWhiteboardBinding.sbOpacity.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            ismBottomsheetWhiteboardBinding.canvasView.setAlpha(progress);
            ismBottomsheetWhiteboardBinding.cvOpacity.setAlpha(progress);
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {
            //TODO Nothing
          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
            //TODO Nothing
          }
        });
  }

  private void setPaintWidth() {
    ismBottomsheetWhiteboardBinding.sbWidth.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            ismBottomsheetWhiteboardBinding.canvasView.setStrokeWidth((float) progress);
            ismBottomsheetWhiteboardBinding.cvWidth.setCircleRadius((float) progress);
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {
            //TODO Nothing
          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
            //TODO Nothing
          }
        });
  }

  private void scaleColorView(View view) {

    //reset scale of all views
    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBlack.setScaleX(1f);
    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBlack.setScaleY(1f);

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorRed.setScaleX(1f);
    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorRed.setScaleY(1f);

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorYellow.setScaleX(1f);
    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorYellow.setScaleY(1f);

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorGreen.setScaleX(1f);
    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorGreen.setScaleY(1f);

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBlue.setScaleX(1f);
    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBlue.setScaleY(1f);

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorPink.setScaleX(1f);
    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorPink.setScaleY(1f);

    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBrown.setScaleX(1f);
    ismBottomsheetWhiteboardBinding.vColorPalette.ivColorBrown.setScaleY(1f);

    //set scale of selected view
    view.setScaleX(1.5f);
    view.setScaleY(1.5f);
  }

  private void toggleDrawTools(View view, boolean showView) {
    if (showView) {
      view.animate().translationY(0 * density);
    } else {
      view.animate().translationY(56 * density);
    }
  }

  private void showProgressDialog(String message) {
    if (activity != null) {
      alertDialog = alertProgress.getProgressDialog(activity, message);
      if (!activity.isFinishing()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {
    if (activity != null) {
      if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }
  }
}
