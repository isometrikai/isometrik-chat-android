package io.isometrik.ui.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityCaptureImageResultBinding;

/**
 * The activity to preview the captured image with option to discard image.
 */
public class CapturedImagePreviewActivity extends AppCompatActivity {

  private AlertDialog.Builder builder;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IsmActivityCaptureImageResultBinding
        ismActivityCaptureImageResultBinding = IsmActivityCaptureImageResultBinding.inflate(getLayoutInflater());
    View view = ismActivityCaptureImageResultBinding.getRoot();
    setContentView(view);

    try {
      Glide.with(this)
          .load(getIntent().getExtras().getString("capturedImagePath"))
          .placeholder(R.drawable.ism_ic_conversation_image)
          .into(ismActivityCaptureImageResultBinding.ivPreview);
    } catch (IllegalArgumentException | NullPointerException ignore) {
    }

    builder = new AlertDialog.Builder(this);

    ismActivityCaptureImageResultBinding.ibBack.setOnClickListener(v -> onBackPressed());

    ismActivityCaptureImageResultBinding.rlDone.setOnClickListener(v -> {
      setResult(Activity.RESULT_OK, new Intent());
      supportFinishAfterTransition();
    });
  }

  @Override
  public void onBackPressed() {
    builder.setMessage(getString(R.string.ism_discard_image))
        .setCancelable(false)
        .setPositiveButton(getString(R.string.ism_discard), (dialog, id) -> {

          setResult(Activity.RESULT_CANCELED, new Intent());
          supportFinishAfterTransition();
        })
        .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel());
    AlertDialog alert = builder.create();
    alert.setCancelable(true);
    alert.show();
  }
}
