package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessageStickerBinding;

/**
 * The type Sticker message sent view holder.
 */
public class StickerMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message sticker binding.
   */
  public final IsmSentMessageStickerBinding ismSentMessageStickerBinding;

  /**
   * Instantiates a new Sticker message sent view holder.
   *
   * @param ismSentMessageStickerBinding the ism sent message sticker binding
   */
  public StickerMessageSentViewHolder(final IsmSentMessageStickerBinding ismSentMessageStickerBinding) {
    super(ismSentMessageStickerBinding.getRoot());
    this.ismSentMessageStickerBinding = ismSentMessageStickerBinding;
  }
}