package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessageLocationBinding;

/**
 * The type Location message received view holder.
 */
public class LocationMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message location binding.
   */
  public final IsmReceivedMessageLocationBinding ismReceivedMessageLocationBinding;

  /**
   * Instantiates a new Location message received view holder.
   *
   * @param ismReceivedMessageLocationBinding the ism received message location binding
   */
  public LocationMessageReceivedViewHolder(
      final IsmReceivedMessageLocationBinding ismReceivedMessageLocationBinding) {
    super(ismReceivedMessageLocationBinding.getRoot());
    this.ismReceivedMessageLocationBinding = ismReceivedMessageLocationBinding;
  }
}