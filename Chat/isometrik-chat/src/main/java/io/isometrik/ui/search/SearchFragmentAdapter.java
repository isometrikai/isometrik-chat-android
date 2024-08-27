package io.isometrik.ui.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import io.isometrik.ui.search.conversations.SearchConversationsFragment;
import io.isometrik.ui.search.messages.SearchMessagesFragment;
import io.isometrik.ui.search.people.SearchPeopleFragment;

/**
 * The fragment adapter for the search conversations, messages and people fragments.
 */
public class SearchFragmentAdapter extends FragmentStateAdapter {
  private static final int FRAGMENTS_COUNT = 3;

  /**
   * Instantiates a new Search fragment adapter.
   *
   * @param activity the activity
   */
  SearchFragmentAdapter(@NonNull FragmentActivity activity) {
    super(activity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return new SearchConversationsFragment();

      case 1:

        return new SearchMessagesFragment();
      case 2:

        return new SearchPeopleFragment();
      default:
        return null;
    }
  }

  @Override
  public int getItemCount() {
    return FRAGMENTS_COUNT;
  }
}