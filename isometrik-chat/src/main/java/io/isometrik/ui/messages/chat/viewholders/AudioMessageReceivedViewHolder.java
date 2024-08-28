package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessageAudioBinding;

/**
 * The type Audio message received view holder.
 */
public class AudioMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message audio binding.
   */
  public final IsmReceivedMessageAudioBinding ismReceivedMessageAudioBinding;

  /**
   * Instantiates a new Audio message received view holder.
   *
   * @param ismReceivedMessageAudioBinding the ism received message audio binding
   */
  public AudioMessageReceivedViewHolder(
      final IsmReceivedMessageAudioBinding ismReceivedMessageAudioBinding) {
    super(ismReceivedMessageAudioBinding.getRoot());
    this.ismReceivedMessageAudioBinding = ismReceivedMessageAudioBinding;
  }
}