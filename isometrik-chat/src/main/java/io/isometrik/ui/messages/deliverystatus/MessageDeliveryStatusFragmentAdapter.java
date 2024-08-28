package io.isometrik.ui.messages.deliverystatus;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import io.isometrik.ui.messages.deliverystatus.complete.DeliveryCompleteFragment;
import io.isometrik.ui.messages.deliverystatus.pending.DeliveryPendingFragment;

/**
 * The fragment adapter for the message delivery/read pending/complete status for a message
 * fragments.
 */
public class MessageDeliveryStatusFragmentAdapter extends FragmentStateAdapter {
  private static final int FRAGMENTS_COUNT = 2;

  /**
   * Instantiates a new Message delivery status fragment adapter.
   *
   * @param activity the activity
   */
  MessageDeliveryStatusFragmentAdapter(@NonNull FragmentActivity activity) {
    super(activity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return new DeliveryCompleteFragment();

      case 1:

        return new DeliveryPendingFragment();

      default:
        return null;
    }
  }

  @Override
  public int getItemCount() {
    return FRAGMENTS_COUNT;
  }
}