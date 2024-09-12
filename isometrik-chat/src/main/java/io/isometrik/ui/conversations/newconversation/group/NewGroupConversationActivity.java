package io.isometrik.ui.conversations.newconversation.group;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import io.isometrik.chat.enums.ConversationType;
import io.isometrik.chat.R;
import io.isometrik.ui.camera.CameraActivity;
import io.isometrik.chat.databinding.IsmActivityNewGroupConversationBinding;
import io.isometrik.ui.messages.chat.ConversationMessagesActivity;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.Constants;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.KeyboardUtil;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import io.isometrik.chat.utils.Utilities;

import java.io.File;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The activity to fetch list of users to select members to create new group conversation,
 * with paging, search and pull to refresh option.Code to create a new group conversation.
 */
public class NewGroupConversationActivity extends AppCompatActivity
    implements NewGroupConversationContract.View {

  private NewGroupConversationContract.Presenter newGroupConversationPresenter;
  private IsmActivityNewGroupConversationBinding ismActivityNewGroupConversationBinding;

  private final ArrayList<UsersModel> users = new ArrayList<>();
  private final ArrayList<UsersModel> selectedUsers = new ArrayList<>();
  private UsersAdapter usersAdapter;
  private SelectedUsersAdapter selectedUsersAdapter;
  private LinearLayoutManager usersLayoutManager;
  private LinearLayoutManager selectedUsersLayoutManager;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private File imageFile;
  private boolean cleanUpRequested = false, selectedUsersStateNeedToBeSaved = false;
  private int conversationType, count;
  private final int MAXIMUM_MEMBERS = Constants.CONVERSATION_MEMBERS_SIZE_AT_A_TIME;
  private ActivityResultLauncher<Intent> cameraActivityLauncher;
  //private ActivityResultLauncher<Intent> searchParticipantsActivityLauncher;
  private int conversationImagePadding;
  private AlertDialog uploadProgressDialog;
  private CircularProgressIndicator circularProgressIndicator;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityNewGroupConversationBinding =
        IsmActivityNewGroupConversationBinding.inflate(getLayoutInflater());
    View view = ismActivityNewGroupConversationBinding.getRoot();
    setContentView(view);
    conversationImagePadding = (int) (28 * getResources().getDisplayMetrics().density);

    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    newGroupConversationPresenter = new NewGroupConversationPresenter(this);

    Bundle extras = getIntent().getExtras();
    conversationType = extras.getInt("conversationType", 0);
    switch (extras.getInt("conversationType")) {
      case 0:
        //Private conversation
        ismActivityNewGroupConversationBinding.tvTitle.setText(
            getString(R.string.ism_new_private_group_conversation));
        ismActivityNewGroupConversationBinding.etConversationTitle.setHint(
            getString(R.string.ism_private_conversation_title_hint));
        conversationType = ConversationType.PrivateConversation.getValue();
        break;

      case 1:
        //Public conversation
        ismActivityNewGroupConversationBinding.tvTitle.setText(
            getString(R.string.ism_new_public_group_conversation));
        ismActivityNewGroupConversationBinding.etConversationTitle.setHint(
            getString(R.string.ism_public_conversation_title_hint));
        conversationType = ConversationType.PublicConversation.getValue();
        break;

      case 2:
        ismActivityNewGroupConversationBinding.tvTitle.setText(
            getString(R.string.ism_new_open_conversation));
        ismActivityNewGroupConversationBinding.etConversationTitle.setHint(
            getString(R.string.ism_open_conversation_title_hint));
        conversationType = ConversationType.OpenConversation.getValue();
        //Open conversation
        break;

      default:
        //Unsupported conversationType
        Toast.makeText(NewGroupConversationActivity.this,
            getString(R.string.ism_invalid_conversation_type), Toast.LENGTH_SHORT).show();
        try {
          new Handler().postDelayed(this::onBackPressed, 1000);
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    usersLayoutManager = new LinearLayoutManager(this);
    ismActivityNewGroupConversationBinding.rvUsers.setLayoutManager(usersLayoutManager);
    usersAdapter = new UsersAdapter(this, users);
    ismActivityNewGroupConversationBinding.rvUsers.addOnScrollListener(
        recyclerViewOnScrollListener);
    ismActivityNewGroupConversationBinding.rvUsers.setAdapter(usersAdapter);

    selectedUsersLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    ismActivityNewGroupConversationBinding.rvUsersSelected.setLayoutManager(
        selectedUsersLayoutManager);
    selectedUsersAdapter = new SelectedUsersAdapter(this, selectedUsers);
    ismActivityNewGroupConversationBinding.rvUsersSelected.setAdapter(selectedUsersAdapter);
    if (extras.containsKey("userId")) {
      //Conversation creation requested from user details or search user page
      UsersModel usersModel =
          new UsersModel(extras.getString("userId"), extras.getString("userName"),
              extras.getString("userProfileImageUrl"), extras.getBoolean("isOnline"));
      selectedUsers.add(usersModel);
      selectedUsersAdapter.notifyItemInserted(0);

      count = 1;
      updateSelectedMembersText();
      selectedUsersStateNeedToBeSaved=true;
    }

    fetchLatestUsers(false, null, false);

    ismActivityNewGroupConversationBinding.rvUsers.addOnItemTouchListener(
        new RecyclerItemClickListener(this, ismActivityNewGroupConversationBinding.rvUsers,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {

                  UsersModel user = users.get(position);

                  if (user.isSelected()) {
                    user.setSelected(false);
                    count--;
                    removeSelectedUser(user.getUserId());
                  } else {

                    if (count < MAXIMUM_MEMBERS) {
                      //Maximum 99 members can be added
                      user.setSelected(true);
                      count++;
                      addSelectedUser(user);
                    } else {
                      Toast.makeText(NewGroupConversationActivity.this,
                          getString(R.string.ism_max_participants_limit_reached),
                          Toast.LENGTH_SHORT).show();
                    }
                  }
                  updateSelectedMembersText();
                  users.set(position, user);
                  usersAdapter.notifyItemChanged(position);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismActivityNewGroupConversationBinding.ivConversationImage.setOnClickListener(v -> {
      if ((ContextCompat.checkSelfPermission(NewGroupConversationActivity.this,
          Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
              || !Utilities.checkSelfExternalStoragePermissionIsGranted(NewGroupConversationActivity.this, false)
         ) {

        if ((ActivityCompat.shouldShowRequestPermissionRationale(NewGroupConversationActivity.this,
            Manifest.permission.CAMERA))
                || Utilities.shouldShowExternalPermissionStorageRational(NewGroupConversationActivity.this,false)
        ) {
          Snackbar snackbar = Snackbar.make(ismActivityNewGroupConversationBinding.rlParent,
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

    ismActivityNewGroupConversationBinding.ivNext.setOnClickListener(v -> {
      KeyboardUtil.hideKeyboard(this);
      newGroupConversationPresenter.validateConversationDetails(
          ismActivityNewGroupConversationBinding.etConversationTitle.getText().toString(),
          imageFile, selectedUsers);
    });

    ismActivityNewGroupConversationBinding.refresh.setOnRefreshListener(
        () -> fetchLatestUsers(false, null, true));

    ismActivityNewGroupConversationBinding.ibBack.setOnClickListener(v -> onBackPressed());
    ismActivityNewGroupConversationBinding.ivRemoveConversationImage.setOnClickListener(v -> {
      imageFile = null;
      ismActivityNewGroupConversationBinding.ivConversationImage.setPadding(
          conversationImagePadding, conversationImagePadding, conversationImagePadding,
          conversationImagePadding);

      try {
        Glide.with(NewGroupConversationActivity.this)
            .load(R.drawable.ism_ic_add_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ismActivityNewGroupConversationBinding.ivConversationImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
      ismActivityNewGroupConversationBinding.ivRemoveConversationImage.setVisibility(View.GONE);
    });
    cameraActivityLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
          if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {

              imageFile = new File(result.getData().getStringExtra("capturedImagePath"));

              ismActivityNewGroupConversationBinding.ivConversationImage.setPadding(0, 0, 0, 0);

              try {
                Glide.with(this)
                    .load(imageFile.getAbsolutePath())
                    .transform(new CircleCrop())
                    .into(ismActivityNewGroupConversationBinding.ivConversationImage);
              } catch (IllegalArgumentException | NullPointerException ignore) {
              }
              ismActivityNewGroupConversationBinding.ivRemoveConversationImage.setVisibility(
                  View.VISIBLE);
            } else {

              Toast.makeText(this, getString(R.string.ism_image_capture_failure), Toast.LENGTH_LONG)
                  .show();
            }
          } else {

            Toast.makeText(this, getString(R.string.ism_image_capture_canceled), Toast.LENGTH_LONG)
                .show();
          }
        });

    //searchParticipantsActivityLauncher =
    //    registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
    //      if (result.getResultCode() == Activity.RESULT_OK) {
    //        if (result.getData() != null) {
    //          //noinspection unchecked
    //          ArrayList<UsersModel> selectedUsers = (ArrayList<UsersModel>) result.getData()
    //              .getSerializableExtra("selectedParticipants");
    //
    //          this.selectedUsers.clear();
    //          this.selectedUsers.addAll(selectedUsers);
    //
    //          selectedUsersAdapter.notifyDataSetChanged();
    //          count = selectedUsers.size();
    //          updateSelectedMembersText();
    //          fetchLatestUsers();
    //        }
    //      }
    //    });

    //ismActivityNewGroupConversationBinding.rlSearch.setOnClickListener(v -> {
    //  Intent intent =
    //      new Intent(NewGroupConversationActivity.this, SearchParticipantsActivity.class);
    //  intent.putExtra("selectedParticipants", selectedUsers);
    //  intent.putExtra("conversationTitle",
    //      ismActivityNewGroupConversationBinding.etConversationTitle.getText().toString());
    //  searchParticipantsActivityLauncher.launch(intent);
    //});

    ismActivityNewGroupConversationBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchLatestUsers(true, s.toString(), false);
        } else {
          if (selectedUsers.size() > 0) {
            selectedUsersStateNeedToBeSaved = true;
          }
          fetchLatestUsers(false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
  }

  @Override
  public void conversationDetailsValidationResult(String errorMessage) {

    if (errorMessage == null) {

      if (imageFile == null) {
        new AlertDialog.Builder(this).setTitle(
            getString(R.string.ism_conversation_image_alert_heading))
            .setMessage(getString(R.string.ism_conversation_image_alert_message))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.ism_yes), (dialog, id) -> {

              dialog.cancel();
              showProgressDialog(getString(R.string.ism_creating_conversation));
              newGroupConversationPresenter.createNewConversation(
                  ismActivityNewGroupConversationBinding.etConversationTitle.getText().toString(),
                  Constants.DEFAULT_PLACEHOLDER_IMAGE_URL, conversationType,
                  ismActivityNewGroupConversationBinding.cbNotifications.isChecked(),
                  ismActivityNewGroupConversationBinding.cbTypingMessage.isChecked(),
                  ismActivityNewGroupConversationBinding.cbDeliveryReadEvents.isChecked(),
                  selectedUsers);
            })
            .setNegativeButton(getString(R.string.ism_no), (dialog, id) -> dialog.cancel())
            .create()
            .show();
      } else {
        //newGroupConversationPresenter.requestImageUpload(imageFile.getAbsolutePath());
        newGroupConversationPresenter.requestImageUpload(String.valueOf(System.currentTimeMillis()),
            ismActivityNewGroupConversationBinding.etConversationTitle.getText().toString(),
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
          newGroupConversationPresenter.cancelConversationImageUpload();
          uploadProgressDialog.dismiss();
          circularProgressIndicator = null;
        });

        uploadProgressDialog.show();
      }
    } else {

      Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onConversationCreatedSuccessfully(String conversationId, String conversationTitle,
      String conversationImageUrl, int participantsCount) {

    Intent intent =
        new Intent(NewGroupConversationActivity.this, ConversationMessagesActivity.class);
    intent.putExtra("messageDeliveryReadEventsEnabled",
        ismActivityNewGroupConversationBinding.cbDeliveryReadEvents.isChecked());
    intent.putExtra("typingEventsEnabled",
        ismActivityNewGroupConversationBinding.cbTypingMessage.isChecked());
    intent.putExtra("newConversation", true);
    intent.putExtra("conversationId", conversationId);
    intent.putExtra("conversationTitle", conversationTitle);
    intent.putExtra("conversationImageUrl", conversationImageUrl);
    intent.putExtra("isPrivateOneToOne", false);
    intent.putExtra("conversationType", conversationType);
    intent.putExtra("participantsCount", participantsCount);


    startActivity(intent);
    supportFinishAfterTransition();
  }

  @Override
  public void onNonBlockedUsersFetched(ArrayList<UsersModel> users, boolean latestUsers,
      boolean isSearchRequest) {
    if (latestUsers) {
      this.users.clear();
      if (isSearchRequest || selectedUsersStateNeedToBeSaved) {
        int size = users.size();
        UsersModel usersModel;

        for (int i = 0; i < size; i++) {

          for (int j = 0; j < selectedUsers.size(); j++) {
            if (selectedUsers.get(j).getUserId().equals(users.get(i).getUserId())) {
              usersModel = users.get(i);
              usersModel.setSelected(true);
              users.set(i, usersModel);
              break;
            }
          }
        }
        if (!isSearchRequest) selectedUsersStateNeedToBeSaved = false;
      } else {

        runOnUiThread(() -> {
          selectedUsers.clear();
          count = 0;
          selectedUsersAdapter.notifyDataSetChanged();
          updateSelectedMembersText();
        });
      }
    } else {

      int size = users.size();
      UsersModel usersModel;

      for (int i = 0; i < size; i++) {

        for (int j = 0; j < selectedUsers.size(); j++) {
          if (selectedUsers.get(j).getUserId().equals(users.get(i).getUserId())) {
            usersModel = users.get(i);
            usersModel.setSelected(true);
            users.set(i, usersModel);
            break;
          }
        }
      }
    }
    this.users.addAll(users);

    runOnUiThread(() -> {
      if (NewGroupConversationActivity.this.users.size() > 0) {
        ismActivityNewGroupConversationBinding.tvNoUsers.setVisibility(View.GONE);
        ismActivityNewGroupConversationBinding.rvUsers.setVisibility(View.VISIBLE);
      } else {
        ismActivityNewGroupConversationBinding.tvNoUsers.setVisibility(View.VISIBLE);
        ismActivityNewGroupConversationBinding.rvUsers.setVisibility(View.GONE);
      }
      usersAdapter.notifyDataSetChanged();
    });
    hideProgressDialog();
    if (ismActivityNewGroupConversationBinding.refresh.isRefreshing()) {
      ismActivityNewGroupConversationBinding.refresh.setRefreshing(false);
    }
    updateShimmerVisibility(false);
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

  @Override
  public void onError(String errorMessage) {
    if (ismActivityNewGroupConversationBinding.refresh.isRefreshing()) {
      ismActivityNewGroupConversationBinding.refresh.setRefreshing(false);
    }

    hideProgressDialog();
    updateShimmerVisibility(false);
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(NewGroupConversationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(NewGroupConversationActivity.this, getString(R.string.ism_error),
            Toast.LENGTH_SHORT).show();
      }
    });
  }

  private final RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          newGroupConversationPresenter.requestNonBlockedUsersDataOnScroll(
              usersLayoutManager.findFirstVisibleItemPosition(), usersLayoutManager.getChildCount(),
              usersLayoutManager.getItemCount());
        }
      };

  /**
   * Remove user.
   *
   * @param userId the user id
   */
  public void removeUser(String userId) {
    int size = users.size();
    for (int i = 0; i < size; i++) {

      if (users.get(i).getUserId().equals(userId)) {

        UsersModel selectMembersModel = users.get(i);
        selectMembersModel.setSelected(false);
        users.set(i, selectMembersModel);
        if (i == 0) {
          usersAdapter.notifyDataSetChanged();
        } else {
          usersAdapter.notifyItemChanged(i);
        }
        count--;
        updateSelectedMembersText();
        break;
      }
    }

    for (int i = 0; i < selectedUsers.size(); i++) {

      if (selectedUsers.get(i).getUserId().equals(userId)) {
        selectedUsers.remove(i);
        if (i == 0) {
          selectedUsersAdapter.notifyDataSetChanged();
        } else {
          selectedUsersAdapter.notifyItemRemoved(i);
        }

        break;
      }
    }
  }

  private void fetchLatestUsers(boolean isSearchRequest, String searchTag,
      boolean showProgressDialog) {
    if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_users));

    try {
      newGroupConversationPresenter.fetchNonBlockedUsers(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void removeSelectedUser(String userId) {

    for (int i = 0; i < selectedUsers.size(); i++) {
      if (selectedUsers.get(i).getUserId().equals(userId)) {
        selectedUsers.remove(i);
        if (i == 0) {
          selectedUsersAdapter.notifyDataSetChanged();
        } else {
          selectedUsersAdapter.notifyItemRemoved(i);
        }
        break;
      }
    }
  }

  private void addSelectedUser(UsersModel selectMembersModel) {
    selectedUsers.add(selectMembersModel);
    try {
      selectedUsersAdapter.notifyDataSetChanged();
      selectedUsersLayoutManager.scrollToPosition(selectedUsers.size() - 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateSelectedMembersText() {

    if (count > 0) {
      ismActivityNewGroupConversationBinding.tvAddParticipants.setText(
          getString(R.string.ism_number_of_participants_selected, String.valueOf(count),
              String.valueOf(MAXIMUM_MEMBERS)));
    } else {
      ismActivityNewGroupConversationBinding.tvAddParticipants.setText(
          getString(R.string.ism_add_participants));
    }
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
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
    if (ActivityCompat.checkSelfPermission(NewGroupConversationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
      permissionsRequired.add(Manifest.permission.CAMERA);
    }

    if (!Utilities.checkSelfExternalStoragePermissionIsGranted(NewGroupConversationActivity.this, true)) {
      permissionsRequired.addAll(Utilities.getPermissionsListForExternalStorage(true));
    }

    ActivityCompat.requestPermissions(NewGroupConversationActivity.this, permissionsRequired.toArray(new String[permissionsRequired.size()]), 0);

  }

  private void requestImageCapture() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      cameraActivityLauncher.launch(new Intent(this, CameraActivity.class));
    } else {
      Toast.makeText(NewGroupConversationActivity.this, R.string.ism_image_capture_not_supported,
          Toast.LENGTH_SHORT).show();
    }
  }


  @Override
  public void onImageUploadResult(String url) {

    if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
      uploadProgressDialog.dismiss();
    }
    circularProgressIndicator = null;
    showProgressDialog(getString(R.string.ism_creating_conversation));
    newGroupConversationPresenter.createNewConversation(
        ismActivityNewGroupConversationBinding.etConversationTitle.getText().toString(), url,
        conversationType, ismActivityNewGroupConversationBinding.cbNotifications.isChecked(),
        ismActivityNewGroupConversationBinding.cbTypingMessage.isChecked(),
        ismActivityNewGroupConversationBinding.cbDeliveryReadEvents.isChecked(), selectedUsers);
  }

  @Override
  public void onImageUploadError(String errorMessage) {
    if (uploadProgressDialog != null && uploadProgressDialog.isShowing()) {
      uploadProgressDialog.dismiss();
    }
    circularProgressIndicator = null;
    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
  }

  private void cleanupOnActivityDestroy() {
    if (!cleanUpRequested) {
      cleanUpRequested = true;
      hideProgressDialog();
      newGroupConversationPresenter.deleteImage(imageFile);
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismActivityNewGroupConversationBinding.shimmerFrameLayout.startShimmer();
      ismActivityNewGroupConversationBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismActivityNewGroupConversationBinding.shimmerFrameLayout.getVisibility()
          == View.VISIBLE) {
        ismActivityNewGroupConversationBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismActivityNewGroupConversationBinding.shimmerFrameLayout.stopShimmer();
      }
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
