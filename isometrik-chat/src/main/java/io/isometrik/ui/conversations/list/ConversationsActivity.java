package io.isometrik.ui.conversations.list;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayoutMediator;
import io.isometrik.ui.IsometrikUiSdk;
import io.isometrik.chat.R;
import io.isometrik.ui.conversations.newconversation.type.SelectConversationTypeActivity;
import io.isometrik.chat.databinding.IsmActivityConversationsBinding;
import io.isometrik.ui.messages.broadcast.BroadcastMessageActivity;
import io.isometrik.ui.messages.mentioned.MentionedMessagesActivity;
import io.isometrik.ui.search.SearchActivity;
import io.isometrik.ui.users.blockedornonblocked.BlockedOrNonBlockedUsersActivity;
import io.isometrik.ui.users.details.UserDetailsActivity;
import io.isometrik.ui.users.list.UsersActivity;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;

/**
 * The activity containing fragments for the list of public, open and all conversations of which
 * user is a member.Api calls to fetch unread conversations count, all undelivered messages and
 * latest user details.
 */
public class ConversationsActivity extends FragmentActivity implements ConversationsContract.View {

  private ConversationsContract.Presenter conversationsPresenter;
  private IsmActivityConversationsBinding ismActivityConversationsBinding;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ismActivityConversationsBinding = IsmActivityConversationsBinding.inflate(getLayoutInflater());
    View view = ismActivityConversationsBinding.getRoot();
    setContentView(view);
    conversationsPresenter = new ConversationsPresenter(this);
    final ConversationsFragmentAdapter conversationsFragmentAdapter =
        new ConversationsFragmentAdapter(ConversationsActivity.this);
    ismActivityConversationsBinding.vpConversations.setAdapter(conversationsFragmentAdapter);
    ismActivityConversationsBinding.vpConversations.setOffscreenPageLimit(2);
    new TabLayoutMediator(ismActivityConversationsBinding.tabLayout,
        ismActivityConversationsBinding.vpConversations, (tab, position) -> {

      switch (position) {
        case 0: {
          tab.setText(getString(R.string.ism_all_conversations))
              .setIcon(R.drawable.ism_ic_private_group);
          break;
        }
        case 1: {
          tab.setText(getString(R.string.ism_public_conversations))
              .setIcon(R.drawable.ism_ic_public_group);
          break;
        }
        case 2: {
          tab.setText(getString(R.string.ism_open_conversations))
              .setIcon(R.drawable.ism_ic_open_chat);
          break;
        }
      }
    }).attach();

      try {
          if (!IsometrikUiSdk.getInstance().getIsometrik().isConnected()) {
              IsometrikUiSdk.getInstance()
                      .getIsometrik()
                      .getExecutor()
                      .execute(() -> IsometrikUiSdk.getInstance()
                              .getIsometrik()
                              .createConnection(IsometrikUiSdk.getInstance().getUserSession().getUserId()
                                              + IsometrikUiSdk.getInstance().getUserSession().getDeviceId(),
                                      IsometrikUiSdk.getInstance().getUserSession().getUserToken()));
          }
      } catch (Exception ignore) {

      }
    loadUserImage(IsometrikUiSdk.getInstance().getUserSession().getUserProfilePic());

    ismActivityConversationsBinding.ivNext.setOnClickListener(v -> startActivity(
        new Intent(ConversationsActivity.this, SelectConversationTypeActivity.class)));

    ismActivityConversationsBinding.ivUserImage.setOnClickListener(
        v -> startActivity(new Intent(ConversationsActivity.this, UserDetailsActivity.class)));

    ismActivityConversationsBinding.ivMentionedMessages.setOnClickListener(v -> startActivity(
        new Intent(ConversationsActivity.this, MentionedMessagesActivity.class)));

    ismActivityConversationsBinding.ivSearch.setOnClickListener(
        v -> startActivity(new Intent(ConversationsActivity.this, SearchActivity.class)));

    ismActivityConversationsBinding.ivMore.setOnClickListener(v -> {
      PopupMenu popup = new PopupMenu(this, v);
      MenuInflater inflater = popup.getMenuInflater();
      inflater.inflate(R.menu.ism_home_page_menu, popup.getMenu());
      popup.setOnMenuItemClickListener(item -> {
        if (item.getItemId() == R.id.broadcast) {
          startActivity(new Intent(ConversationsActivity.this, BroadcastMessageActivity.class));
          return true;
        } else if (item.getItemId() == R.id.blocked) {
          startActivity(
              new Intent(ConversationsActivity.this, BlockedOrNonBlockedUsersActivity.class));
          return true;
        }
        return false;
      });
      popup.show();
    });

    IsometrikUiSdk.getInstance().getIsometrik().getExecutor().execute(() -> {
      fetchUnreadConversationsCount();
      conversationsPresenter.fetchUserDetails();
    });

    askNotificationPermission();
    NotificationManagerCompat.from(this).cancelAll();
    fetchAllUndeliveredMessages();
  }

  @Override
  public void onUnreadConversationsCountFetchedSuccessfully(int count) {
    runOnUiThread(() -> {
      try {
        if (ismActivityConversationsBinding.tabLayout.getTabAt(0) != null) {
          BadgeDrawable badgeDrawable =
              ismActivityConversationsBinding.tabLayout.getTabAt(0).getOrCreateBadge();

          if (count > 0) {
            badgeDrawable.setBadgeTextColor(Color.WHITE);
            badgeDrawable.setBackgroundColor(
                ContextCompat.getColor(this, R.color.ism_select_text_blue));
            badgeDrawable.setVisible(true);
            badgeDrawable.setNumber(count);
          } else {
            badgeDrawable.setVisible(false);
          }
        }
      } catch (NullPointerException ignore) {

      }
    });
  }

  @Override
  public void onError(String errorMessage) {
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
      }
    });
  }

  /**
   * Fetch unread conversations count.
   */
  public void fetchUnreadConversationsCount() {
    conversationsPresenter.fetchUnreadConversationsCount();
  }

  /**
   * Connection state changed.
   *
   * @param connected the connected
   */
  public void connectionStateChanged(boolean connected) {
    runOnUiThread(
        () -> ismActivityConversationsBinding.incConnectionState.tvConnectionState.setVisibility(
            connected ? View.GONE : View.VISIBLE));
    if (connected) {
      fetchAllUndeliveredMessages();
    }
  }

  /**
   * Load user image.
   *
   * @param userProfileImageUrl the user profile image url
   */
  public void loadUserImage(String userProfileImageUrl) {

    runOnUiThread(() -> {
      if (PlaceholderUtils.isValidImageUrl(userProfileImageUrl)) {

        try {
          GlideApp.with(ConversationsActivity.this)
              .load(userProfileImageUrl)
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(ismActivityConversationsBinding.ivUserImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(ConversationsActivity.this,
            IsometrikUiSdk.getInstance().getUserSession().getUserName(),
            ismActivityConversationsBinding.ivUserImage, 13);
      }
    });
  }

  @Override
  public void onUserDeleted() {
    startActivity(new Intent(ConversationsActivity.this, UsersActivity.class).addFlags(
        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    finish();
  }

  private final ActivityResultLauncher<String> requestPermissionLauncher =
      registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (!isGranted) {
          Toast.makeText(this, getString(R.string.ism_notification_permission_denied),
              Toast.LENGTH_SHORT).show();
        }
      });

  private void askNotificationPermission() {
    // This is only necessary for API level >= 33 (TIRAMISU)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
          != PackageManager.PERMISSION_GRANTED) {
        if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
          new AlertDialog.Builder(this).setTitle(getString(R.string.ism_forward_description)).setMessage(
              getString(R.string.ism_notification_permission_ask))
              .setCancelable(true)
              .setPositiveButton(getString(R.string.ism_ok), (dialog, id) -> {

                dialog.cancel();
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
              })
              .setNegativeButton(getString(R.string.ism_no_thanks), (dialog, id) -> dialog.cancel())
              .create()
              .show();
        } else {
          requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
      }
    }
  }

  /**
   * Fetch all undelivered messages.
   */
  public void fetchAllUndeliveredMessages() {
    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getExecutor()
        .execute(() -> conversationsPresenter.fetchAllUndeliveredMessages(0));
  }
}
