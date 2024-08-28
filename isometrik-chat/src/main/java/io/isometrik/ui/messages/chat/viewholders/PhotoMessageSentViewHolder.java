package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessagePhotoBinding;

/**
 * The type Photo message sent view holder.
 */
public class PhotoMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message photo binding.
   */
  public final IsmSentMessagePhotoBinding ismSentMessagePhotoBinding;

  /**
   * Instantiates a new Photo message sent view holder.
   *
   * @param ismSentMessagePhotoBinding the ism sent message photo binding
   */
  public PhotoMessageSentViewHolder(final IsmSentMessagePhotoBinding ismSentMessagePhotoBinding) {
    super(ismSentMessagePhotoBinding.getRoot());
    this.ismSentMessagePhotoBinding = ismSentMessagePhotoBinding;
  }
}