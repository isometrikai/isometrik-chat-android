package io.isometrik.ui.conversations.details.participants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.google.android.material.tabs.TabLayoutMediator;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityMembersWatchersBinding;

/**
 * The activity containing fragments for the members and watchers list in a conversation.
 */
public class MembersWatchersActivity extends FragmentActivity {

  /**
   * The Member refresh required.
   */
  public boolean memberRefreshRequired;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IsmActivityMembersWatchersBinding ismActivityMembersWatchersBinding =
        IsmActivityMembersWatchersBinding.inflate(getLayoutInflater());
    View view = ismActivityMembersWatchersBinding.getRoot();
    setContentView(view);

    final MembersWatchersFragmentAdapter membersWatchersFragmentAdapter = new MembersWatchersFragmentAdapter(MembersWatchersActivity.this);
    ismActivityMembersWatchersBinding.vpMembersWatchers.setAdapter(membersWatchersFragmentAdapter);
    ismActivityMembersWatchersBinding.vpMembersWatchers.setOffscreenPageLimit(1);
    new TabLayoutMediator(ismActivityMembersWatchersBinding.tabLayout,
        ismActivityMembersWatchersBinding.vpMembersWatchers, (tab, position) -> {
      if (position == 0) {
        tab.setText(getString(R.string.ism_members)).setIcon(R.drawable.ism_ic_member);
      } else {
        tab.setText(getString(R.string.ism_watchers)).setIcon(R.drawable.ism_ic_watcher);
      }
    }).attach();

    ismActivityMembersWatchersBinding.tvTitle.setText(
        getIntent().getExtras().getString("conversationTitle"));

    ismActivityMembersWatchersBinding.ibBack.setOnClickListener(v -> onBackPressed());
  }

  @Override
  public void finish() {

    if (memberRefreshRequired) {
      setResult(Activity.RESULT_OK, new Intent());
    }
    super.finish();
  }
}
