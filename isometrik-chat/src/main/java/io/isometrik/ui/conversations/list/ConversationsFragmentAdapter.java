package io.isometrik.ui.conversations.list;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import io.isometrik.chat.enums.ConversationType;

/**
 * The fragment adapter for the open/public and all conversations fragments.
 */
public class ConversationsFragmentAdapter extends FragmentStateAdapter {
  private static final int FRAGMENTS_COUNT = 3;

  /**
   * Instantiates a new Conversations fragment adapter.
   *
   * @param activity the activity
   */
  ConversationsFragmentAdapter(@NonNull FragmentActivity activity) {
    super(activity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    Fragment fragment = new ConversationsListFragment();
    Bundle args = new Bundle();
    switch (position) {
      case 0:
        args.putInt("conversationType", ConversationType.AllConversations.getValue());
        fragment.setArguments(args);
        return fragment;

      case 1:
        args.putInt("conversationType", ConversationType.PublicConversation.getValue());
        fragment.setArguments(args);
        return fragment;
      case 2:
        args.putInt("conversationType", ConversationType.OpenConversation.getValue());
        fragment.setArguments(args);
        return fragment;
      default:
        return null;
    }
  }

  @Override
  public int getItemCount() {
    return FRAGMENTS_COUNT;
  }
}