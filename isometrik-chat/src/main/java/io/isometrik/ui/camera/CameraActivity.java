package io.isometrik.ui.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityCameraBinding;
import io.isometrik.chat.utils.ImageUtil;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The activity to open camera and capture image.
 */
public class CameraActivity extends AppCompatActivity {

  private ActivityResultLauncher<Intent> capturedImagePreviewActivityLauncher;
  private String capturedImagePath;
  private final Executor executor = Executors.newSingleThreadExecutor();
  private IsmActivityCameraBinding ismActivityCameraBinding;
  private ImageCapture imageCapture;
  private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

  private int lensFacing = CameraSelector.LENS_FACING_FRONT;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityCameraBinding = IsmActivityCameraBinding.inflate(getLayoutInflater());
    View view = ismActivityCameraBinding.getRoot();
    setContentView(view);

    capturedImagePreviewActivityLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

          if (result.getResultCode() == Activity.RESULT_OK) {
            String caption = "";
            if (result.getData() != null && result.getData().hasExtra("caption")) {
              caption = result.getData().getStringExtra("caption");
              if (caption == null) {
                caption = "";
              }
            }
            setResult(Activity.RESULT_OK,
                new Intent()
                    .putExtra("capturedImagePath", capturedImagePath)
                    .putExtra("caption", caption));
            supportFinishAfterTransition();

          } else {
            setResult(Activity.RESULT_CANCELED, new Intent());
          }

        });

      startCamera();
      ismActivityCameraBinding.ivCapture.setOnClickListener(v -> captureImage());

      ismActivityCameraBinding.ibBack.setOnClickListener(v -> onBackPressed());
    ismActivityCameraBinding.ivSwitchCamera.setOnClickListener(v -> switchCamera());
  }

  @Override
  public void onBackPressed() {

    setResult(Activity.RESULT_CANCELED, new Intent());
    supportFinishAfterTransition();
  }

  private void startCamera() {

    cameraProviderFuture = ProcessCameraProvider.getInstance(this);

    cameraProviderFuture.addListener(() -> {
      try {

        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
        bindPreview(cameraProvider);
      } catch (ExecutionException | InterruptedException e) {
        // No errors need to be handled for this Future.
        // This should never be reached.
      }
    }, ContextCompat.getMainExecutor(this));
  }

  private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

    Preview preview = new Preview.Builder().build();

    CameraSelector cameraSelector =
        new CameraSelector.Builder().requireLensFacing(lensFacing).build();

    ImageCapture.Builder builder = new ImageCapture.Builder();

    imageCapture =
        builder.setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
            .build();
    preview.setSurfaceProvider(ismActivityCameraBinding.previewView.getSurfaceProvider());
    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
  }

  private void captureImage() {

    try {
      File file =
          ImageUtil.createFileForMediaCapturedFromCamera(String.valueOf(System.currentTimeMillis()),
              true, this);
      ImageCapture.Metadata metadata = new ImageCapture.Metadata();
      metadata.setReversedHorizontal(lensFacing == CameraSelector.LENS_FACING_FRONT);

      ImageCapture.OutputFileOptions outputFileOptions =
          new ImageCapture.OutputFileOptions.Builder(file).setMetadata(metadata).build();

      imageCapture.takePicture(outputFileOptions, executor,
          new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

              capturedImagePath = file.getAbsolutePath();
              capturedImagePreviewActivityLauncher.launch(
                  new Intent(CameraActivity.this, CapturedImagePreviewActivity.class).putExtra(
                      "capturedImagePath", capturedImagePath));
            }

            @Override
            public void onError(@NonNull ImageCaptureException error) {
              Toast.makeText(CameraActivity.this, R.string.ism_image_capture_failure,
                  Toast.LENGTH_SHORT).show();
            }
          });
    } catch (IOException ignore) {
      Toast.makeText(CameraActivity.this, R.string.ism_image_capture_failure, Toast.LENGTH_SHORT)
          .show();
    }
  }

  @Override
  protected void onDestroy() {
      ProcessCameraProvider cameraProvider;
      try {
        if (cameraProviderFuture != null) {
          cameraProvider = cameraProviderFuture.get();
          cameraProvider.unbindAll();
        }
      } catch (ExecutionException | InterruptedException e) {
        e.printStackTrace();
      }
      super.onDestroy();
  }

  /**
   * Switch camera.
   */
  public void switchCamera() {
    lensFacing = lensFacing == CameraSelector.LENS_FACING_FRONT ? CameraSelector.LENS_FACING_BACK
        : CameraSelector.LENS_FACING_FRONT;
    //unbind immediately to avoid mirrored frame.
    ProcessCameraProvider cameraProvider;
    try {
      cameraProvider = cameraProviderFuture.get();
      cameraProvider.unbindAll();
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    startCamera();
  }
}
