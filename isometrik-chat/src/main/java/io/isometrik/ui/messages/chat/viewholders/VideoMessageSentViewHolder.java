package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessageVideoBinding;

/**
 * The type Video message sent view holder.
 */
public class VideoMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message video binding.
   */
  public final IsmSentMessageVideoBinding ismSentMessageVideoBinding;

  /**
   * Instantiates a new Video message sent view holder.
   *
   * @param ismSentMessageVideoBinding the ism sent message video binding
   */
  public VideoMessageSentViewHolder(final IsmSentMessageVideoBinding ismSentMessageVideoBinding) {
    super(ismSentMessageVideoBinding.getRoot());
    this.ismSentMessageVideoBinding = ismSentMessageVideoBinding;
  }
}