package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessagePhotoBinding;

/**
 * The type Photo message received view holder.
 */
public class PhotoMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message photo binding.
   */
  public final IsmReceivedMessagePhotoBinding ismReceivedMessagePhotoBinding;

  /**
   * Instantiates a new Photo message received view holder.
   *
   * @param ismReceivedMessagePhotoBinding the ism received message photo binding
   */
  public PhotoMessageReceivedViewHolder(
      final IsmReceivedMessagePhotoBinding ismReceivedMessagePhotoBinding) {
    super(ismReceivedMessagePhotoBinding.getRoot());
    this.ismReceivedMessagePhotoBinding = ismReceivedMessagePhotoBinding;
  }
}