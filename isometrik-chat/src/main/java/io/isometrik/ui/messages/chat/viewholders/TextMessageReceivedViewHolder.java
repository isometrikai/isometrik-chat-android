package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessageTextBinding;

/**
 * The type Text message received view holder.
 */
public class TextMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message text binding.
   */
  public final IsmReceivedMessageTextBinding ismReceivedMessageTextBinding;

  /**
   * Instantiates a new Text message received view holder.
   *
   * @param ismReceivedMessageTextBinding the ism received message text binding
   */
  public TextMessageReceivedViewHolder(
      final IsmReceivedMessageTextBinding ismReceivedMessageTextBinding) {
    super(ismReceivedMessageTextBinding.getRoot());
    this.ismReceivedMessageTextBinding = ismReceivedMessageTextBinding;
  }
}