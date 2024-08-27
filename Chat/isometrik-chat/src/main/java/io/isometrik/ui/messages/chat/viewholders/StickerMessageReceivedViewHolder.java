package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessageStickerBinding;

/**
 * The type Sticker message received view holder.
 */
public class StickerMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message sticker binding.
   */
  public final IsmReceivedMessageStickerBinding ismReceivedMessageStickerBinding;

  /**
   * Instantiates a new Sticker message received view holder.
   *
   * @param ismReceivedMessageStickerBinding the ism received message sticker binding
   */
  public StickerMessageReceivedViewHolder(
      final IsmReceivedMessageStickerBinding ismReceivedMessageStickerBinding) {
    super(ismReceivedMessageStickerBinding.getRoot());
    this.ismReceivedMessageStickerBinding = ismReceivedMessageStickerBinding;
  }
}