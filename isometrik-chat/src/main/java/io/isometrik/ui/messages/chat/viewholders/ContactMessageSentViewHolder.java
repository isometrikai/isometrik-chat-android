package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessageContactBinding;

/**
 * The type Contact message sent view holder.
 */
public class ContactMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message contact binding.
   */
  public final IsmSentMessageContactBinding ismSentMessageContactBinding;

  /**
   * Instantiates a new Contact message sent view holder.
   *
   * @param ismSentMessageContactBinding the ism sent message contact binding
   */
  public ContactMessageSentViewHolder(final IsmSentMessageContactBinding ismSentMessageContactBinding) {
    super(ismSentMessageContactBinding.getRoot());
    this.ismSentMessageContactBinding = ismSentMessageContactBinding;
  }
}