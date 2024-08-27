package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessageGifBinding;

/**
 * The type Gif message sent view holder.
 */
public class GifMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message gif binding.
   */
  public final IsmSentMessageGifBinding ismSentMessageGifBinding;

  /**
   * Instantiates a new Gif message sent view holder.
   *
   * @param ismSentMessageGifBinding the ism sent message gif binding
   */
  public GifMessageSentViewHolder(final IsmSentMessageGifBinding ismSentMessageGifBinding) {
    super(ismSentMessageGifBinding.getRoot());
    this.ismSentMessageGifBinding = ismSentMessageGifBinding;
  }
}