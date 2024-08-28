package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessageContactBinding;

/**
 * The type Contact message received view holder.
 */
public class ContactMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message contact binding.
   */
  public final IsmReceivedMessageContactBinding ismReceivedMessageContactBinding;

  /**
   * Instantiates a new Contact message received view holder.
   *
   * @param ismReceivedMessageContactBinding the ism received message contact binding
   */
  public ContactMessageReceivedViewHolder(
      final IsmReceivedMessageContactBinding ismReceivedMessageContactBinding) {
    super(ismReceivedMessageContactBinding.getRoot());
    this.ismReceivedMessageContactBinding = ismReceivedMessageContactBinding;
  }
}