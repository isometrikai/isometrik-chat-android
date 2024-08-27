package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessageFileBinding;

/**
 * The type File message sent view holder.
 */
public class FileMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message file binding.
   */
  public final IsmSentMessageFileBinding ismSentMessageFileBinding;

  /**
   * Instantiates a new File message sent view holder.
   *
   * @param ismSentMessageFileBinding the ism sent message file binding
   */
  public FileMessageSentViewHolder(final IsmSentMessageFileBinding ismSentMessageFileBinding) {
    super(ismSentMessageFileBinding.getRoot());
    this.ismSentMessageFileBinding = ismSentMessageFileBinding;
  }
}