package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessageAudioBinding;

/**
 * The type Audio message sent view holder.
 */
public class AudioMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message audio binding.
   */
  public final IsmSentMessageAudioBinding ismSentMessageAudioBinding;

  /**
   * Instantiates a new Audio message sent view holder.
   *
   * @param ismSentMessageAudioBinding the ism sent message audio binding
   */
  public AudioMessageSentViewHolder(final IsmSentMessageAudioBinding ismSentMessageAudioBinding) {
    super(ismSentMessageAudioBinding.getRoot());
    this.ismSentMessageAudioBinding = ismSentMessageAudioBinding;
  }
}