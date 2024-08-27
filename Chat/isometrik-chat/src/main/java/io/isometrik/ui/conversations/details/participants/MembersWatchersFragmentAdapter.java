package io.isometrik.ui.conversations.details.participants;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import io.isometrik.ui.conversations.details.participants.members.MembersFragment;
import io.isometrik.ui.conversations.details.participants.watchers.WatchersFragment;

/**
 * The fragment adapter for the members and watchers fragments.
 */
public class MembersWatchersFragmentAdapter extends FragmentStateAdapter {
  private static final int FRAGMENTS_COUNT = 2;

  /**
   * Instantiates a new Members watchers fragment adapter.
   *
   * @param activity the activity
   */
  MembersWatchersFragmentAdapter(@NonNull FragmentActivity activity) {
    super(activity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return new MembersFragment();

      case 1:

        return new WatchersFragment();

      default:
        return null;
    }
  }

  @Override
  public int getItemCount() {
    return FRAGMENTS_COUNT;
  }
}