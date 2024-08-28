package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessageVideoBinding;

/**
 * The type Video message received view holder.
 */
public class VideoMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message video binding.
   */
  public final IsmReceivedMessageVideoBinding ismReceivedMessageVideoBinding;

  /**
   * Instantiates a new Video message received view holder.
   *
   * @param ismReceivedMessageVideoBinding the ism received message video binding
   */
  public VideoMessageReceivedViewHolder(
      final IsmReceivedMessageVideoBinding ismReceivedMessageVideoBinding) {
    super(ismReceivedMessageVideoBinding.getRoot());
    this.ismReceivedMessageVideoBinding = ismReceivedMessageVideoBinding;
  }
}