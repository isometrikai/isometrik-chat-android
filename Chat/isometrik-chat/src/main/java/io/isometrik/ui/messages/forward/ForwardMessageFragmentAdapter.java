package io.isometrik.ui.messages.forward;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import io.isometrik.ui.messages.forward.conversations.ForwardInConversationFragment;
import io.isometrik.ui.messages.forward.people.ForwardToPeopleFragment;

/**
 * The fragment adapter for the forward message to people and forward in conversations fragments.
 */
public class ForwardMessageFragmentAdapter extends FragmentStateAdapter {
  private static final int FRAGMENTS_COUNT = 2;

  /**
   * Instantiates a new Forward message fragment adapter.
   *
   * @param activity the activity
   */
  ForwardMessageFragmentAdapter(@NonNull FragmentActivity activity) {
    super(activity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return new ForwardInConversationFragment();

      case 1:

        return new ForwardToPeopleFragment();

      default:
        return null;
    }
  }

  @Override
  public int getItemCount() {
    return FRAGMENTS_COUNT;
  }
}