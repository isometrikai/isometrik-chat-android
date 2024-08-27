package io.isometrik.ui.users.edit;

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

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.camera.CameraActivity;
import io.isometrik.chat.databinding.IsmActivityEditUserBinding;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import io.isometrik.chat.utils.UserSession;
import io.isometrik.chat.utils.Utilities;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The activity to update details of logged in user.
 * It implements EditUserContract.View{@link EditUserContract.View}
 *
 * @see EditUserContract.View
 */
public class EditUserActivity extends AppCompatActivity implements EditUserContract.View {

    private EditUserContract.Presenter editUserPresenter;

    private AlertProgress alertProgress;

    private File imageFile;
    private AlertDialog alertDialog;
    private boolean cleanUpRequested = false;
    private UserSession userSession;

    private String initialUserName, initialUserIdentifier, initialUserMetadata;

    private boolean initialUserNotification, userNotificationUpdated, profilePicUpdated,
            userMetadataUpdated, userIdentifierUpdated, userNameUpdated;

    private IsmActivityEditUserBinding ismActivityEditUserBinding;
    private ActivityResultLauncher<Intent> cameraActivityLauncher;
    private AlertDialog uploadProgressDialog;
    private CircularProgressIndicator circularProgressIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ismActivityEditUserBinding = IsmActivityEditUserBinding.inflate(getLayoutInflater());
        View view = ismActivityEditUserBinding.getRoot();
        setContentView(view);

        editUserPresenter = new EditUserPresenter(this);
        alertProgress = new AlertProgress();

        userSession = IsometrikUiSdk.getInstance().getUserSession();
        addUserDetails();

        ismActivityEditUserBinding.btSave.setOnClickListener(v -> {

            editUserPresenter.validateUserDetails(
                    ismActivityEditUserBinding.etUserName.getText().toString(),
                    ismActivityEditUserBinding.etUserIdentifier.getText().toString(), imageFile,
                    profilePicUpdated);
        });
        ismActivityEditUserBinding.ibBack.setOnClickListener(v -> onBackPressed());
        ismActivityEditUserBinding.ibAddImage.setOnClickListener(v -> {
            if ((ContextCompat.checkSelfPermission(EditUserActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED)
                    || !Utilities.checkSelfExternalStoragePermissionIsGranted(EditUserActivity.this, false)
            ) {

                if ((ActivityCompat.shouldShowRequestPermissionRationale(EditUserActivity.this,
                        Manifest.permission.CAMERA))
                        || Utilities.shouldShowExternalPermissionStorageRational(EditUserActivity.this,false)
                ) {
                    Snackbar snackbar = Snackbar.make(ismActivityEditUserBinding.rlParent,
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

        cameraActivityLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {

                            imageFile = new File(result.getData().getStringExtra("capturedImagePath"));

                            try {
                                GlideApp.with(this)
                                        .load(imageFile.getAbsolutePath())
                                        .transform(new CircleCrop())
                                        .into(ismActivityEditUserBinding.ivProfilePic);
                            } catch (IllegalArgumentException | NullPointerException ignore) {

                            }
                            profilePicUpdated = true;
                        } else {
                            deleteImage();
                            Toast.makeText(this, getString(R.string.ism_image_capture_failure), Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        deleteImage();
                        Toast.makeText(this, getString(R.string.ism_image_capture_canceled), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        cleanupOnActivityDestroy();

        setResult(Activity.RESULT_CANCELED, new Intent());
        finish();

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


    private void requestPermissions() {

        ArrayList<String> permissionsRequired = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(EditUserActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsRequired.add(Manifest.permission.CAMERA);
        }

        if (!Utilities.checkSelfExternalStoragePermissionIsGranted(EditUserActivity.this, true)) {
            permissionsRequired.addAll(Utilities.getPermissionsListForExternalStorage(true));
        }

        ActivityCompat.requestPermissions(EditUserActivity.this, permissionsRequired.toArray(new String[permissionsRequired.size()]), 0);

    }

    /**
     * {@link EditUserContract.View#onUserDetailsValidationResult(String)}
     */
    @Override
    public void onUserDetailsValidationResult(String errorMessage) {
        if (errorMessage != null) {

            Toast.makeText(EditUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        } else {

            userNameUpdated =
                    !initialUserName.equals(ismActivityEditUserBinding.etUserName.getText().toString());
            userIdentifierUpdated = !initialUserIdentifier.equals(
                    ismActivityEditUserBinding.etUserIdentifier.getText().toString());
            userMetadataUpdated = !initialUserMetadata.equals(
                    ismActivityEditUserBinding.etUserMetadata.getText().toString());
            userNotificationUpdated =
                    initialUserNotification != ismActivityEditUserBinding.swUserNotification.isChecked();

            if (profilePicUpdated) {
                //editUserPresenter.requestImageUpload(imageFile.getAbsolutePath());
                editUserPresenter.requestImageUpload(String.valueOf(System.currentTimeMillis()),
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
                    editUserPresenter.cancelUserImageUpload();
                    uploadProgressDialog.dismiss();
                    circularProgressIndicator = null;
                });

                uploadProgressDialog.show();
            } else {

                editUserPresenter.requestUserDetailsUpdate(userSession.getUserToken(),
                        ismActivityEditUserBinding.etUserName.getText().toString(),
                        ismActivityEditUserBinding.etUserIdentifier.getText().toString(), null,
                        ismActivityEditUserBinding.etUserMetadata.getText().toString(), userNameUpdated,
                        userIdentifierUpdated, userMetadataUpdated, userNotificationUpdated,
                        ismActivityEditUserBinding.swUserNotification.isChecked());
            }
        }
    }

    /**
     * {@link EditUserContract.View#onImageUploadResult(String)}
     */
    @Override
    public void onImageUploadResult(String url) {

        if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
            uploadProgressDialog.dismiss();
        }
        circularProgressIndicator = null;
        showProgressDialog(getString(R.string.ism_updating_user_details));
        editUserPresenter.requestUserDetailsUpdate(userSession.getUserToken(),
                ismActivityEditUserBinding.etUserName.getText().toString(),
                ismActivityEditUserBinding.etUserIdentifier.getText().toString(), url,
                ismActivityEditUserBinding.etUserMetadata.getText().toString(), userNameUpdated,
                userIdentifierUpdated, userMetadataUpdated, userNotificationUpdated,
                ismActivityEditUserBinding.swUserNotification.isChecked());
    }

    /**
     * {@link EditUserContract.View#onImageUploadError(String)}
     */
    @Override
    public void onImageUploadError(String errorMessage) {
        if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
            uploadProgressDialog.dismiss();
        }
        circularProgressIndicator = null;
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * {@link EditUserContract.View#onUserDetailsUpdated(String)}
     */
    @Override
    public void onUserDetailsUpdated(String userProfilePicUrl) {

        Intent intent = new Intent();
        intent.putExtra("userName", ismActivityEditUserBinding.etUserName.getText().toString());
        intent.putExtra("userIdentifier",
                ismActivityEditUserBinding.etUserIdentifier.getText().toString());
        intent.putExtra("userNameUpdated", userNameUpdated);
        intent.putExtra("userIdentifierUpdated", userIdentifierUpdated);
        intent.putExtra("userProfilePic", userProfilePicUrl);
        intent.putExtra("userProfilePicUpdated", profilePicUpdated);

        if (userMetadataUpdated) {
            intent.putExtra("userMetadata",
                    IsometrikUiSdk.getInstance().getUserSession().getUserMetadata().toString());
        } else {
            JSONObject metadata = new JSONObject();
            try {
                metadata.put("metadata", ismActivityEditUserBinding.etUserMetadata.getText().toString());
            } catch (JSONException ignore) {

            }
            intent.putExtra("userMetadata", metadata.toString());
        }

        intent.putExtra("userMetadataUpdated", userMetadataUpdated);
        intent.putExtra("userNotification", ismActivityEditUserBinding.swUserNotification.isChecked());
        intent.putExtra("userNotificationUpdated", userNotificationUpdated);

        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    /**
     * {@link EditUserContract.View#onError(String)}
     */
    @Override
    public void onError(String errorMessage) {
        hideProgressDialog();

        runOnUiThread(() -> {
            if (errorMessage != null) {
                Toast.makeText(EditUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditUserActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void addUserDetails() {
        initialUserName = userSession.getUserName();
        initialUserIdentifier = userSession.getUserIdentifier();
        initialUserNotification = userSession.getUserNotification();
        try {
            JSONObject metadata = userSession.getUserMetadata();

            if (metadata.has("metadata")) {
                initialUserMetadata = (String) metadata.get("metadata");
            } else {
                initialUserMetadata = "";
            }
        } catch (JSONException e) {
            initialUserMetadata = "";
        }
        ismActivityEditUserBinding.swUserNotification.setChecked(initialUserNotification);
        ismActivityEditUserBinding.etUserName.setText(initialUserName);
        ismActivityEditUserBinding.etUserIdentifier.setText(initialUserIdentifier);
        ismActivityEditUserBinding.etUserMetadata.setText(initialUserMetadata);
        if (PlaceholderUtils.isValidImageUrl(userSession.getUserProfilePic())) {

            try {
                GlideApp.with(this)
                        .load(userSession.getUserProfilePic())
                        .transform(new CircleCrop())
                        .into(ismActivityEditUserBinding.ivProfilePic);
            } catch (IllegalArgumentException | NullPointerException ignore) {
            }
        } else {
            PlaceholderUtils.setTextRoundDrawable(this, userSession.getUserName(),
                    ismActivityEditUserBinding.ivProfilePic, 26);
        }
    }

    private void showProgressDialog(String message) {

        alertDialog = alertProgress.getProgressDialog(this, message);
        if (!isFinishing()) alertDialog.show();
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    private void requestImageCapture() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cameraActivityLauncher.launch(new Intent(this, CameraActivity.class));
        } else {
            Toast.makeText(EditUserActivity.this, R.string.ism_image_capture_not_supported,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteImage() {

        editUserPresenter.deleteImage(imageFile);
    }

    private void cleanupOnActivityDestroy() {
        if (!cleanUpRequested) {
            cleanUpRequested = true;
            hideProgressDialog();
            deleteImage();
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
