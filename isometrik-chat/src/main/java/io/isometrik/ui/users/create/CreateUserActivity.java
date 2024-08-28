package io.isometrik.ui.users.create;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import io.isometrik.chat.R;
import io.isometrik.ui.camera.CameraActivity;
import io.isometrik.ui.conversations.list.ConversationsActivity;
import io.isometrik.chat.databinding.IsmActivityCreateUserBinding;
import io.isometrik.ui.users.list.UsersActivity;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.Constants;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.Utilities;

import java.io.File;
import java.util.ArrayList;

/**
 * The activity to create a new user.
 * It implements CreateUserContract.View{@link CreateUserContract.View}
 *
 * @see CreateUserContract.View
 */
public class CreateUserActivity extends AppCompatActivity implements CreateUserContract.View {

  private CreateUserContract.Presenter createUserPresenter;

  private AlertProgress alertProgress;

  private File imageFile;
  private AlertDialog alertDialog;
  private boolean cleanUpRequested = false;
  private IsmActivityCreateUserBinding ismActivityCreateUserBinding;
  private ActivityResultLauncher<Intent> cameraActivityLauncher;
  private AlertDialog uploadProgressDialog;
  private CircularProgressIndicator circularProgressIndicator;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityCreateUserBinding = IsmActivityCreateUserBinding.inflate(getLayoutInflater());
    View view = ismActivityCreateUserBinding.getRoot();
    setContentView(view);

    createUserPresenter = new CreateUserPresenter(this);
    alertProgress = new AlertProgress();

    ismActivityCreateUserBinding.btCreate.setOnClickListener(v -> {

      //try {
      createUserPresenter.validateUserDetails(
          ismActivityCreateUserBinding.etName.getText().toString(),
          ismActivityCreateUserBinding.etUserIdentifier.getText().toString(),
          ismActivityCreateUserBinding.etUserPassword.getText().toString(), imageFile);
      //} catch (NullPointerException ignore) {
      //  Toast.makeText(CreateUserActivity.this, getString(R.string.ism_unexpected_error),
      //      Toast.LENGTH_SHORT).show();
      //}
    });
    ismActivityCreateUserBinding.ivProfilePic.setOnClickListener(v -> {

      if ((ContextCompat.checkSelfPermission(CreateUserActivity.this, Manifest.permission.CAMERA)
          != PackageManager.PERMISSION_GRANTED)
              || !Utilities.checkSelfExternalStoragePermissionIsGranted(CreateUserActivity.this, false)
      ) {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(CreateUserActivity.this,
            Manifest.permission.CAMERA))
                || Utilities.shouldShowExternalPermissionStorageRational(CreateUserActivity.this,false)
        ) {
          Snackbar snackbar = Snackbar.make(ismActivityCreateUserBinding.rlParent,
              R.string.ism_permission_image_capture, Snackbar.LENGTH_INDEFINITE)
              .setAction(getString(R.string.ism_ok), view1 -> requestPermissions());

          snackbar.show();

          ((TextView) snackbar.getView()
              .findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(
              Gravity.CENTER_HORIZONTAL);
        } else {

          requestPermissions();
        }
      } else {

        requestImageCapture();
      }
    });
    ismActivityCreateUserBinding.ibBack.setOnClickListener(v -> onBackPressed());
    ismActivityCreateUserBinding.tvSelectUser.setOnClickListener(v -> onBackPressed());

    cameraActivityLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
          if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {

              imageFile = new File(result.getData().getStringExtra("capturedImagePath"));

              try {
                GlideApp.with(this)
                    .load(imageFile.getAbsolutePath())
                    .transform(new CircleCrop())
                    .into(ismActivityCreateUserBinding.ivProfilePic);
              } catch (IllegalArgumentException | NullPointerException ignore) {

              }
              ismActivityCreateUserBinding.ivRemoveUserImage.setVisibility(View.VISIBLE);
            } else {
              Toast.makeText(this, getString(R.string.ism_image_capture_failure), Toast.LENGTH_LONG)
                  .show();
            }
          } else {

            Toast.makeText(this, getString(R.string.ism_image_capture_canceled), Toast.LENGTH_LONG)
                .show();
          }
        });

    ismActivityCreateUserBinding.ivRemoveUserImage.setOnClickListener(v -> {
      imageFile = null;

      try {
        GlideApp.with(CreateUserActivity.this)
            .load(R.drawable.ism_ic_profile)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ismActivityCreateUserBinding.ivProfilePic);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
      ismActivityCreateUserBinding.ivRemoveUserImage.setVisibility(View.GONE);
    });
  }

  /**
   * {@link CreateUserContract.View#onUserCreated()}
   */
  @Override
  public void onUserCreated() {
    startActivity(new Intent(CreateUserActivity.this, ConversationsActivity.class));
    finish();
  }

  private void requestPermissions() {

    ArrayList<String> permissionsRequired = new ArrayList<>();
    if (ActivityCompat.checkSelfPermission(CreateUserActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
      permissionsRequired.add(Manifest.permission.CAMERA);
    }

    if (!Utilities.checkSelfExternalStoragePermissionIsGranted(CreateUserActivity.this, true)) {
      permissionsRequired.addAll(Utilities.getPermissionsListForExternalStorage(true));
    }

    ActivityCompat.requestPermissions(CreateUserActivity.this, permissionsRequired.toArray(new String[permissionsRequired.size()]), 0);

  }

  @Override
  public void onBackPressed() {
    cleanupOnActivityDestroy();
    startActivity(new Intent(CreateUserActivity.this, UsersActivity.class));
    try {
      super.onBackPressed();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onDestroy() {
    cleanupOnActivityDestroy();
    super.onDestroy();
  }

  /**
   * {@link CreateUserContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(CreateUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(CreateUserActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  /**
   * {@link CreateUserContract.View#onImageUploadResult(String)}
   */
  @Override
  public void onImageUploadResult(String url) {

    if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
      uploadProgressDialog.dismiss();
    }
    circularProgressIndicator = null;
    showProgressDialog(getString(R.string.ism_creating_user));
    createUserPresenter.requestCreateUser(ismActivityCreateUserBinding.etName.getText().toString(),
        ismActivityCreateUserBinding.etUserIdentifier.getText().toString(), url,
        ismActivityCreateUserBinding.etUserPassword.getText().toString(),
        ismActivityCreateUserBinding.etUserMetadata.getText().toString());
  }

  /**
   * {@link CreateUserContract.View#onUserDetailsValidationResult(String)}
   */
  @Override
  public void onUserDetailsValidationResult(String errorMessage) {

    if (errorMessage != null) {

      Toast.makeText(CreateUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    } else {
      if (imageFile == null) {
        new AlertDialog.Builder(this).setTitle(getString(R.string.ism_user_image_alert_heading))
            .setMessage(getString(R.string.ism_user_image_alert_message))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.ism_yes), (dialog, id) -> {

              dialog.cancel();
              showProgressDialog(getString(R.string.ism_creating_user));
              createUserPresenter.requestCreateUser(
                  ismActivityCreateUserBinding.etName.getText().toString(),
                  ismActivityCreateUserBinding.etUserIdentifier.getText().toString(),
                  Constants.DEFAULT_PLACEHOLDER_IMAGE_URL,
                  ismActivityCreateUserBinding.etUserPassword.getText().toString(),
                  ismActivityCreateUserBinding.etUserMetadata.getText().toString());
            })
            .setNegativeButton(getString(R.string.ism_no), (dialog, id) -> dialog.cancel())
            .create()
            .show();
      } else {
        //createUserPresenter.requestImageUpload(imageFile.getAbsolutePath());
        createUserPresenter.requestImageUpload(String.valueOf(System.currentTimeMillis()),
            ismActivityCreateUserBinding.etUserIdentifier.getText().toString(),
            imageFile.getAbsolutePath());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setCancelable(false);
        View dialogView = inflater.inflate(R.layout.ism_dialog_uploading_image, null);

        builder.setView(dialogView);

        uploadProgressDialog = builder.create();
        circularProgressIndicator = dialogView.findViewById(R.id.pbUpload);

        AppCompatButton btCancel = dialogView.findViewById(R.id.btCancel);

        btCancel.setOnClickListener(v -> {
          createUserPresenter.cancelUserImageUpload();
          uploadProgressDialog.dismiss();
          circularProgressIndicator = null;
        });

        uploadProgressDialog.show();
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    boolean permissionDenied = false;
    if (requestCode == 0) {

      for (int grantResult : grantResults) {
        if (grantResult != PackageManager.PERMISSION_GRANTED) {
          permissionDenied = true;
          break;
        }
      }
      if (permissionDenied) {
        Toast.makeText(this, getString(R.string.ism_permission_image_capture_denied),
            Toast.LENGTH_LONG).show();
      } else {
        requestImageCapture();
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }


  private void requestImageCapture() {

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      cameraActivityLauncher.launch(new Intent(this, CameraActivity.class));
    } else {
      Toast.makeText(CreateUserActivity.this, R.string.ism_image_capture_not_supported,
          Toast.LENGTH_SHORT).show();
    }
  }


  /**
   * {@link CreateUserContract.View#onImageUploadError(String)}
   */
  @Override
  public void onImageUploadError(String errorMessage) {
    if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
      uploadProgressDialog.dismiss();
    }
    circularProgressIndicator = null;
    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void cleanupOnActivityDestroy() {
    if (!cleanUpRequested) {
      cleanUpRequested = true;
      hideProgressDialog();
      createUserPresenter.deleteImage(imageFile);
    }
  }

  @Override
  public void onUploadProgressUpdate(int progress) {

    if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
      runOnUiThread(() -> {
        if (circularProgressIndicator != null) {
          circularProgressIndicator.setProgress(progress);
        }
      });
    }
  }
}
