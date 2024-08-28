package io.isometrik.ui.conversations.newconversation.onetoone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityNewOnetooneConversationBinding;
import io.isometrik.ui.messages.chat.ConversationMessagesActivity;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The activity to fetch list of users to select opponent to create new one to one conversation,
 * with paging, search and pull to refresh option.Code to create a new one to one conversation.
 */
public class NewOneToOneConversationActivity extends AppCompatActivity
    implements NewOneToOneConversationContract.View {

  private NewOneToOneConversationContract.Presenter newOneToOneConversationPresenter;
  private IsmActivityNewOnetooneConversationBinding ismActivityNewOnetooneConversationBinding;

  private final ArrayList<UsersModel> users = new ArrayList<>();
  private UsersAdapter usersAdapter;
  private LinearLayoutManager usersLayoutManager;
  private UsersModel selectedUser;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private boolean cleanUpRequested = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityNewOnetooneConversationBinding =
        IsmActivityNewOnetooneConversationBinding.inflate(getLayoutInflater());
    View view = ismActivityNewOnetooneConversationBinding.getRoot();
    setContentView(view);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    newOneToOneConversationPresenter = new NewOneToOneConversationPresenter(this);

    usersLayoutManager = new LinearLayoutManager(this);
    ismActivityNewOnetooneConversationBinding.rvUsers.setLayoutManager(usersLayoutManager);
    usersAdapter = new UsersAdapter(this, users);
    ismActivityNewOnetooneConversationBinding.rvUsers.addOnScrollListener(
        recyclerViewOnScrollListener);
    ismActivityNewOnetooneConversationBinding.rvUsers.setAdapter(usersAdapter);

    Bundle extras = getIntent().getExtras();
    if (extras != null && extras.containsKey("userId")) {
      //Conversation creation requested from search user page
      selectedUser = new UsersModel(extras.getString("userId"), extras.getString("userName"),
          extras.getBoolean("isOnline"), extras.getString("userProfileImageUrl"),
          extras.getLong("lastSeenAt"));

      ismActivityNewOnetooneConversationBinding.tvSelectedUserName.setText(
          selectedUser.getUserName());
      if (PlaceholderUtils.isValidImageUrl(selectedUser.getUserProfilePic())) {

        try {
          GlideApp.with(NewOneToOneConversationActivity.this)
              .load(selectedUser.getUserProfilePic())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(ismActivityNewOnetooneConversationBinding.ivSelectedUserImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(this, selectedUser.getUserName(),
            ismActivityNewOnetooneConversationBinding.ivSelectedUserImage, 33);
      }
    }

    fetchLatestUsers(false, null, false);

    ismActivityNewOnetooneConversationBinding.rvUsers.addOnItemTouchListener(
        new RecyclerItemClickListener(this, ismActivityNewOnetooneConversationBinding.rvUsers,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {

                  UsersModel user = users.get(position);

                  if (user.isSelected()) {
                    selectedUser = null;
                    ismActivityNewOnetooneConversationBinding.tvSelectedUserName.setText(
                        getString(R.string.ism_select_user_for_conversation));

                    try {
                      GlideApp.with(NewOneToOneConversationActivity.this)
                          .load(R.drawable.ism_ic_profile)
                          .into(ismActivityNewOnetooneConversationBinding.ivSelectedUserImage);
                    } catch (IllegalArgumentException | NullPointerException ignore) {
                    }
                  } else {
                    selectedUser = user;
                    ismActivityNewOnetooneConversationBinding.tvSelectedUserName.setText(
                        user.getUserName());
                    if (PlaceholderUtils.isValidImageUrl(user.getUserProfilePic())) {

                      try {
                        GlideApp.with(NewOneToOneConversationActivity.this)
                            .load(user.getUserProfilePic())
                            .placeholder(R.drawable.ism_ic_profile)
                            .transform(new CircleCrop())
                            .into(ismActivityNewOnetooneConversationBinding.ivSelectedUserImage);
                      } catch (IllegalArgumentException | NullPointerException ignore) {
                      }
                    } else {
                      PlaceholderUtils.setTextRoundDrawable(NewOneToOneConversationActivity.this,
                          user.getUserName(),
                          ismActivityNewOnetooneConversationBinding.ivSelectedUserImage, 33);
                    }
                  }
                  user.setSelected(!user.isSelected());
                  users.set(position, user);

                  int size = users.size();
                  for (int i = 0; i < size; i++) {
                    if (users.get(i).isSelected()) {
                      if (i != position) {
                        UsersModel usersModel = users.get(i);
                        usersModel.setSelected(false);
                        users.set(i, usersModel);
                        usersAdapter.notifyItemChanged(i);
                        break;
                      }
                    }
                  }

                  usersAdapter.notifyItemChanged(position);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismActivityNewOnetooneConversationBinding.ivNext.setOnClickListener(v -> {
      if (selectedUser != null) {
        newOneToOneConversationPresenter.createNewConversation(
            ismActivityNewOnetooneConversationBinding.cbNotifications.isChecked(),
            ismActivityNewOnetooneConversationBinding.cbTypingMessage.isChecked(),
            ismActivityNewOnetooneConversationBinding.cbDeliveryReadEvents.isChecked(),
            selectedUser.getUserId(), selectedUser.getUserName());
      } else {

        Toast.makeText(NewOneToOneConversationActivity.this,
            getString(R.string.ism_no_user_selected), Toast.LENGTH_SHORT).show();
      }
    });

    ismActivityNewOnetooneConversationBinding.etSearch.addTextChangedListener(new TextWatcher() {
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

    ismActivityNewOnetooneConversationBinding.refresh.setOnRefreshListener(
        () -> fetchLatestUsers(false, null, true));

    ismActivityNewOnetooneConversationBinding.ibBack.setOnClickListener(v -> onBackPressed());
  }

  @Override
  public void onConversationCreatedSuccessfully(String conversationId) {

    Intent intent =
        new Intent(NewOneToOneConversationActivity.this, ConversationMessagesActivity.class);
    intent.putExtra("messageDeliveryReadEventsEnabled",
        ismActivityNewOnetooneConversationBinding.cbDeliveryReadEvents.isChecked());
    intent.putExtra("typingEventsEnabled",
        ismActivityNewOnetooneConversationBinding.cbTypingMessage.isChecked());
    intent.putExtra("newConversation", true);
    intent.putExtra("conversationId", conversationId);
    intent.putExtra("isPrivateOneToOne", true);
    intent.putExtra("userName", selectedUser.getUserName());
    intent.putExtra("userImageUrl", selectedUser.getUserProfilePic());
    intent.putExtra("isOnline", selectedUser.isOnline());
    if (!selectedUser.isOnline()) {
      intent.putExtra("lastSeenAt", selectedUser.getLastSeenAt());
    }

    intent.putExtra("userId", selectedUser.getUserId());
    startActivity(intent);
    supportFinishAfterTransition();
  }

  @Override
  public void onNonBlockedUsersFetched(ArrayList<UsersModel> users, boolean latestUsers) {
    if (latestUsers) {
      this.users.clear();
    }
    int size = users.size();
    UsersModel usersModel;

    for (int i = 0; i < size; i++) {

      if (selectedUser != null) {
        if (selectedUser.getUserId().equals(users.get(i).getUserId())) {
          usersModel = users.get(i);
          usersModel.setSelected(true);
          users.set(i, usersModel);
          break;
        }
      }
    }
    this.users.addAll(users);
    runOnUiThread(() -> {
      if (NewOneToOneConversationActivity.this.users.size() > 0) {
        ismActivityNewOnetooneConversationBinding.tvNoUsers.setVisibility(View.GONE);
        ismActivityNewOnetooneConversationBinding.rvUsers.setVisibility(View.VISIBLE);
        usersAdapter.notifyDataSetChanged();
      } else {
        ismActivityNewOnetooneConversationBinding.tvNoUsers.setVisibility(View.VISIBLE);
        ismActivityNewOnetooneConversationBinding.rvUsers.setVisibility(View.GONE);
      }
    });
    hideProgressDialog();
    if (ismActivityNewOnetooneConversationBinding.refresh.isRefreshing()) {
      ismActivityNewOnetooneConversationBinding.refresh.setRefreshing(false);
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
    if (ismActivityNewOnetooneConversationBinding.refresh.isRefreshing()) {
      ismActivityNewOnetooneConversationBinding.refresh.setRefreshing(false);
    }

    hideProgressDialog();
    updateShimmerVisibility(false);
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(NewOneToOneConversationActivity.this, errorMessage, Toast.LENGTH_SHORT)
            .show();
      } else {
        Toast.makeText(NewOneToOneConversationActivity.this, getString(R.string.ism_error),
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

          newOneToOneConversationPresenter.requestNonBlockedUsersDataOnScroll(
              usersLayoutManager.findFirstVisibleItemPosition(), usersLayoutManager.getChildCount(),
              usersLayoutManager.getItemCount());
        }
      };

  private void fetchLatestUsers(boolean isSearchRequest, String searchTag,
      boolean showProgressDialog) {
    if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_users));

    try {
      newOneToOneConversationPresenter.fetchNonBlockedUsers(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
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

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismActivityNewOnetooneConversationBinding.shimmerFrameLayout.startShimmer();
      ismActivityNewOnetooneConversationBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismActivityNewOnetooneConversationBinding.shimmerFrameLayout.getVisibility()
          == View.VISIBLE) {
        ismActivityNewOnetooneConversationBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismActivityNewOnetooneConversationBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
