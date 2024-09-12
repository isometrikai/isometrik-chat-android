package io.isometrik.ui.users.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import io.isometrik.chat.R;
import io.isometrik.ui.conversations.list.ConversationsActivity;
import io.isometrik.chat.databinding.IsmActivityUsersBinding;
import io.isometrik.ui.users.create.CreateUserActivity;
import io.isometrik.chat.utils.AlertProgress;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The activity to fetch list of users to login with paging, search and pull to refresh
 * option.Code to authorize credentials of selected user.
 * It implements UsersContract.View{@link UsersContract.View}
 *
 * @see UsersContract.View
 */
public class UsersActivity extends AppCompatActivity implements UsersContract.View {

  private UsersContract.Presenter usersPresenter;


  private AlertProgress alertProgress;

  private AlertDialog alertDialog, selectUserAlertDialog;

  private final ArrayList<UsersModel> users = new ArrayList<>();
  private UsersAdapter usersAdapter;

  private LinearLayoutManager layoutManager;
  private IsmActivityUsersBinding ismActivityUsersBinding;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityUsersBinding = IsmActivityUsersBinding.inflate(getLayoutInflater());
    View view = ismActivityUsersBinding.getRoot();
    setContentView(view);

    usersPresenter = new UsersPresenter(this);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    layoutManager = new LinearLayoutManager(this);
    ismActivityUsersBinding.rvUsers.setLayoutManager(layoutManager);
    usersAdapter = new UsersAdapter(this, users);
    ismActivityUsersBinding.rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
    ismActivityUsersBinding.rvUsers.setAdapter(usersAdapter);

    fetchLatestUsers(false, null, false);

    ismActivityUsersBinding.refresh.setOnRefreshListener(() -> fetchLatestUsers(false, null, true));

    ismActivityUsersBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchLatestUsers(true, s.toString(), false);
        } else {

          fetchLatestUsers(false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    ismActivityUsersBinding.ivCreateUser.setOnClickListener(v -> {
      startActivity(new Intent(this, CreateUserActivity.class));
      finish();
    });
  }

  /**
   * Request user credentials.
   *
   * @param user the user who is to be selected
   */
  public void requestUserCredentials(UsersModel user) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    @SuppressLint("InflateParams")
    final View vSelectUser = getLayoutInflater().inflate(R.layout.ism_select_user_popup, null);

    TextView tvUserName = vSelectUser.findViewById(R.id.tvUserName);
    tvUserName.setText(user.getUserName());
    AppCompatImageView ivUserImage = vSelectUser.findViewById(R.id.ivUserImage);
    AppCompatEditText etUserPassword = vSelectUser.findViewById(R.id.etUserPassword);
    if (PlaceholderUtils.isValidImageUrl(user.getUserProfileImageUrl())) {

      try {
        Glide.with(this)
            .load(user.getUserProfileImageUrl())
            .transform(new CircleCrop())
            .placeholder(R.drawable.ism_ic_profile)
            .into(ivUserImage);
      } catch (NullPointerException | IllegalArgumentException ignore) {
      }
    } else {
      PlaceholderUtils.setTextRoundDrawable(this, user.getUserName(), ivUserImage, 20);
    }
    alertDialogBuilder.setView(vSelectUser);
    alertDialogBuilder.setPositiveButton(getString(R.string.ism_continue), (dialog, which) -> {
      usersPresenter.authorizeUser(user, etUserPassword.getText().toString());
    });
    alertDialogBuilder.setNegativeButton(getString(R.string.ism_cancel),
        (dialog, which) -> selectUserAlertDialog.cancel());

    selectUserAlertDialog = alertDialogBuilder.create();
    selectUserAlertDialog.setCancelable(false);
    selectUserAlertDialog.setCanceledOnTouchOutside(false);
    if (!isFinishing()) selectUserAlertDialog.show();
  }

  private void fetchLatestUsers(boolean isSearchRequest, String searchTag,
      boolean showProgressDialog) {
    if (showProgressDialog) {

      showProgressDialog(getString(R.string.ism_fetching_users));
    }
    try {
      usersPresenter.requestUsersData(true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The Recycler view on scroll listener.
   */
  private final RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          usersPresenter.requestUsersDataOnScroll(layoutManager.findFirstVisibleItemPosition(),
              layoutManager.getChildCount(), layoutManager.getItemCount());
        }
      };

  /**
   * {@link UsersContract.View#onUsersDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onUsersDataReceived(ArrayList<UsersModel> users, boolean latestUsers) {

    runOnUiThread(() -> {

      if (latestUsers) {
        this.users.clear();
      }
      this.users.addAll(users);

      if (UsersActivity.this.users.size() > 0) {
        ismActivityUsersBinding.tvNoUsers.setVisibility(View.GONE);
        ismActivityUsersBinding.rvUsers.setVisibility(View.VISIBLE);
        usersAdapter.notifyDataSetChanged();
      } else {
        ismActivityUsersBinding.tvNoUsers.setVisibility(View.VISIBLE);
        ismActivityUsersBinding.rvUsers.setVisibility(View.GONE);
      }
    });
    hideProgressDialog();
    if (ismActivityUsersBinding.refresh.isRefreshing()) {
      ismActivityUsersBinding.refresh.setRefreshing(false);
    }
    updateShimmerVisibility(false);
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  /**
   * {@link UsersContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (ismActivityUsersBinding.refresh.isRefreshing()) {
      ismActivityUsersBinding.refresh.setRefreshing(false);
    }
    hideProgressDialog();
    updateShimmerVisibility(false);
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(UsersActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(UsersActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  /**
   * {@link UsersContract.View#onUserAuthorized()}
   */
  @Override
  public void onUserAuthorized() {
    if (selectUserAlertDialog != null && selectUserAlertDialog.isShowing()) {
      selectUserAlertDialog.dismiss();
    }
    startActivity(new Intent(this, ConversationsActivity.class));
    finish();
  }

  /**
   * {@link UsersContract.View#onUserAuthorizationError(String)}
   */
  @Override
  public void onUserAuthorizationError(String errorMessage) {
    if (selectUserAlertDialog != null && selectUserAlertDialog.isShowing()) {
      selectUserAlertDialog.dismiss();
    }
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(UsersActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(UsersActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismActivityUsersBinding.shimmerFrameLayout.startShimmer();
      ismActivityUsersBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismActivityUsersBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismActivityUsersBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismActivityUsersBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
