package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessageLocationBinding;

/**
 * The type Location message sent view holder.
 */
public class LocationMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message location binding.
   */
  public final IsmSentMessageLocationBinding ismSentMessageLocationBinding;

  /**
   * Instantiates a new Location message sent view holder.
   *
   * @param ismSentMessageLocationBinding the ism sent message location binding
   */
  public LocationMessageSentViewHolder(
      final IsmSentMessageLocationBinding ismSentMessageLocationBinding) {
    super(ismSentMessageLocationBinding.getRoot());
    this.ismSentMessageLocationBinding = ismSentMessageLocationBinding;
  }
}