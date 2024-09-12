package io.isometrik.ui.users.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityUserDetailsBinding;
import io.isometrik.ui.users.edit.EditUserActivity;
import io.isometrik.ui.users.list.UsersActivity;
import io.isometrik.chat.utils.AlertProgress;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import io.isometrik.chat.utils.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The activity to fetch logged in user's details.Code to delete user.
 * It implements UserDetailsContract.View{@link UserDetailsContract.View}
 *
 * @see UserDetailsContract.View
 */
public class UserDetailsActivity extends AppCompatActivity implements UserDetailsContract.View {

  private UserDetailsContract.Presenter userDetailsPresenter;

  private AlertProgress alertProgress;

  private UserSession userSession;
  private AlertDialog alertDialog;
  private boolean cleanUpRequested;

  private IsmActivityUserDetailsBinding ismActivityUserDetailsBinding;
  private ActivityResultLauncher<Intent> editUserActivityLauncher;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityUserDetailsBinding = IsmActivityUserDetailsBinding.inflate(getLayoutInflater());
    View view = ismActivityUserDetailsBinding.getRoot();
    setContentView(view);

    userDetailsPresenter = new UserDetailsPresenter(this);
    alertProgress = new AlertProgress();

    userSession = IsometrikUiSdk.getInstance().getUserSession();

    updateUserDetails(false, userSession.getUserName(), userSession.getUserIdentifier(),
        userSession.getUserProfilePic(), userSession.getUserMetadata(),
        userSession.getUserNotification(), false, false, false, false, false);
    userDetailsPresenter.requestUserDetails(userSession.getUserToken());

    ismActivityUserDetailsBinding.btSwitchUser.setOnClickListener(v -> switchUser());

    ismActivityUserDetailsBinding.ibEdit.setOnClickListener(v -> editUserActivityLauncher.launch(
        new Intent(UserDetailsActivity.this, EditUserActivity.class)));

    ismActivityUserDetailsBinding.ibDelete.setOnClickListener(v -> {
      showProgressDialog(getString(R.string.ism_deleting_user));
      userDetailsPresenter.requestUserDelete(userSession.getUserToken());
    });
    ismActivityUserDetailsBinding.ibBack.setOnClickListener(v -> onBackPressed());

    editUserActivityLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

          if (result.getResultCode() == Activity.RESULT_OK) {
            Intent intent = result.getData();
            if (intent != null) {
              String userMetadata = intent.getStringExtra("userMetadata");

              JSONObject metadata;
              try {
                metadata = new JSONObject(userMetadata);
              } catch (JSONException e) {
                metadata = new JSONObject();
              }

              updateUserDetails(true, intent.getStringExtra("userName"),
                  intent.getStringExtra("userIdentifier"), intent.getStringExtra("userProfilePic"),
                  metadata, intent.getBooleanExtra("userNotification", false),
                  intent.getBooleanExtra("userNameUpdated", false),
                  intent.getBooleanExtra("userIdentifierUpdated", false),
                  intent.getBooleanExtra("userProfilePicUpdated", false),
                  intent.getBooleanExtra("userMetadataUpdated", false),
                  intent.getBooleanExtra("userNotificationUpdated", false));
            }
          } else {

            Toast.makeText(this, getString(R.string.ism_edit_user_details_canceled),
                Toast.LENGTH_LONG).show();
          }
        });
  }

  /**
   * Switch user.
   */
  public void switchUser() {
    cleanupOnActivityDestroy();

    userDetailsPresenter.clearUserSession();
    startActivity(new Intent(UserDetailsActivity.this, UsersActivity.class).addFlags(
        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    finish();
  }

  @Override
  public void onBackPressed() {
    cleanupOnActivityDestroy();
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
   * {@link UserDetailsContract.View#onUserDetailsReceived(String, String, String, JSONObject,
   * boolean,
   * boolean,
   * boolean, boolean, boolean, boolean)}
   */
  @Override
  public void onUserDetailsReceived(String userName, String userIdentifier,
      String userProfilePicUrl, JSONObject userMetadata, boolean userNotification,
      boolean updateUserName, boolean updateUserIdentifier, boolean updateUserProfilePic,
      boolean updateUserMetadata, boolean updateUserNotification) {

    runOnUiThread(
        () -> updateUserDetails(true, userName, userIdentifier, userProfilePicUrl, userMetadata,
            userNotification, updateUserName, updateUserIdentifier, updateUserProfilePic,
            updateUserMetadata, updateUserNotification));
  }

  /**
   * {@link UserDetailsContract.View#onUserDeleted()}
   */
  @Override
  public void onUserDeleted() {
    switchUser();
  }

  /**
   * {@link UserDetailsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();

    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(UserDetailsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(UserDetailsActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  private void updateUserDetails(boolean remote, String userName, String userIdentifier,
      String userProfilePicUrl, JSONObject userMetadata, boolean userNotification,
      boolean updateUserName, boolean updateUserIdentifier, boolean updateUserProfilePic,
      boolean updateUserMetadata, boolean updateUserNotification) {

    if (remote) {

      if (updateUserName) {
        ismActivityUserDetailsBinding.tvNameValue.setText(userName);
      }

      if (updateUserIdentifier) {
        ismActivityUserDetailsBinding.tvUserIdentifierValue.setText(userIdentifier);
      }
      if (updateUserNotification) {

        ismActivityUserDetailsBinding.swUserNotification.setEnabled(true);
        ismActivityUserDetailsBinding.swUserNotification.setChecked(userNotification);
        ismActivityUserDetailsBinding.swUserNotification.setEnabled(false);
      }
      if (updateUserMetadata) {
        if (userMetadata.has("metadata")) {

          try {
            ismActivityUserDetailsBinding.tvUserMetadataValue.setText(
                userMetadata.getString("metadata"));
          } catch (JSONException e) {
            ismActivityUserDetailsBinding.tvUserMetadataValue.setText("");
          }
        } else {
          ismActivityUserDetailsBinding.tvUserMetadataValue.setText("");
        }
      }
      if (updateUserProfilePic) {
        if (PlaceholderUtils.isValidImageUrl(userProfilePicUrl)) {

          try {
            Glide.with(this)
                .load(userProfilePicUrl)
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(ismActivityUserDetailsBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(this, userName,
              ismActivityUserDetailsBinding.ivProfilePic, 26);
        }
      }
    } else {
      ismActivityUserDetailsBinding.swUserNotification.setEnabled(true);
      ismActivityUserDetailsBinding.swUserNotification.setChecked(userNotification);
      ismActivityUserDetailsBinding.swUserNotification.setEnabled(false);
      ismActivityUserDetailsBinding.tvNameValue.setText(userName);
      ismActivityUserDetailsBinding.tvUserIdentifierValue.setText(userIdentifier);
      if (userMetadata.has("metadata")) {

        try {
          ismActivityUserDetailsBinding.tvUserMetadataValue.setText(
              userMetadata.getString("metadata"));
        } catch (JSONException e) {
          ismActivityUserDetailsBinding.tvUserMetadataValue.setText("");
        }
      } else {
        ismActivityUserDetailsBinding.tvUserMetadataValue.setText("");
      }
      if (PlaceholderUtils.isValidImageUrl(userProfilePicUrl)) {

        try {
          Glide.with(this)
              .load(userProfilePicUrl)
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(ismActivityUserDetailsBinding.ivProfilePic);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(this, userName,
            ismActivityUserDetailsBinding.ivProfilePic, 26);
      }

      if (userSession.getUserSelected()) {

        ismActivityUserDetailsBinding.ibEdit.setVisibility(View.GONE);
        ismActivityUserDetailsBinding.ibDelete.setVisibility(View.GONE);
      } else {

        ismActivityUserDetailsBinding.ibEdit.setVisibility(View.VISIBLE);
        ismActivityUserDetailsBinding.ibDelete.setVisibility(View.VISIBLE);
      }
    }
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
    }
  }
}
