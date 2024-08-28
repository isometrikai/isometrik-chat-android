package io.isometrik.ui.users.blockedornonblocked;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayoutMediator;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityBlockedOrNonBlockedUsersBinding;

/**
 * The activity containing fragments for the blocked or non blocked users list for the logged in
 * user.
 */
public class BlockedOrNonBlockedUsersActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IsmActivityBlockedOrNonBlockedUsersBinding ismActivityBlockedOrNonBlockedUsersBinding =
        IsmActivityBlockedOrNonBlockedUsersBinding.inflate(getLayoutInflater());
    View view = ismActivityBlockedOrNonBlockedUsersBinding.getRoot();
    setContentView(view);

    final BlockedOrNonBlockedUsersFragmentAdapter blockedOrNonBlockedUsersFragmentAdapter =
        new BlockedOrNonBlockedUsersFragmentAdapter(BlockedOrNonBlockedUsersActivity.this);
    ismActivityBlockedOrNonBlockedUsersBinding.vpUsers.setAdapter(
        blockedOrNonBlockedUsersFragmentAdapter);
    ismActivityBlockedOrNonBlockedUsersBinding.vpUsers.setOffscreenPageLimit(2);
    new TabLayoutMediator(ismActivityBlockedOrNonBlockedUsersBinding.tabLayout,
        ismActivityBlockedOrNonBlockedUsersBinding.vpUsers, (tab, position) -> {
      switch (position) {
        case 0:
          tab.setText(getString(R.string.ism_blocked_users))
              .setIcon(R.drawable.ism_ic_blocked_user);
          break;
        case 1:
          tab.setText(getString(R.string.ism_non_blocked_users))
              .setIcon(R.drawable.ism_ic_search_people);
          break;
      }
    }).attach();

    ismActivityBlockedOrNonBlockedUsersBinding.ibBack.setOnClickListener(v -> onBackPressed());
  }
}

