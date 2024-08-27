package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessageGifBinding;

/**
 * The type Gif message received view holder.
 */
public class GifMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message gif binding.
   */
  public final IsmReceivedMessageGifBinding ismReceivedMessageGifBinding;

  /**
   * Instantiates a new Gif message received view holder.
   *
   * @param ismReceivedMessageGifBinding the ism received message gif binding
   */
  public GifMessageReceivedViewHolder(
      final IsmReceivedMessageGifBinding ismReceivedMessageGifBinding) {
    super(ismReceivedMessageGifBinding.getRoot());
    this.ismReceivedMessageGifBinding = ismReceivedMessageGifBinding;
  }
}