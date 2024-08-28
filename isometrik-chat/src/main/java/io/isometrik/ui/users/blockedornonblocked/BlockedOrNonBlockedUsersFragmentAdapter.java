package io.isometrik.ui.users.blockedornonblocked;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import io.isometrik.ui.users.blockedornonblocked.blocked.BlockedUsersFragment;
import io.isometrik.ui.users.blockedornonblocked.nonblocked.NonBlockedUsersFragment;

/**
 * The fragment adapter for the blocked or non blocked users fragments.
 */
public class BlockedOrNonBlockedUsersFragmentAdapter extends FragmentStateAdapter {
  private static final int FRAGMENTS_COUNT = 2;

  /**
   * Instantiates a new Blocked or non blocked users fragment adapter.
   *
   * @param activity the activity
   */
  BlockedOrNonBlockedUsersFragmentAdapter(@NonNull FragmentActivity activity) {
    super(activity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return new BlockedUsersFragment();

      case 1:

        return new NonBlockedUsersFragment();
      default:
        return null;
    }
  }

  @Override
  public int getItemCount() {
    return FRAGMENTS_COUNT;
  }
}