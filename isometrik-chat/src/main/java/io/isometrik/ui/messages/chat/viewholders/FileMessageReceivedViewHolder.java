package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessageFileBinding;

/**
 * The type File message received view holder.
 */
public class FileMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message file binding.
   */
  public final IsmReceivedMessageFileBinding ismReceivedMessageFileBinding;

  /**
   * Instantiates a new File message received view holder.
   *
   * @param ismReceivedMessageFileBinding the ism received message file binding
   */
  public FileMessageReceivedViewHolder(
      final IsmReceivedMessageFileBinding ismReceivedMessageFileBinding) {
    super(ismReceivedMessageFileBinding.getRoot());
    this.ismReceivedMessageFileBinding = ismReceivedMessageFileBinding;
  }
}