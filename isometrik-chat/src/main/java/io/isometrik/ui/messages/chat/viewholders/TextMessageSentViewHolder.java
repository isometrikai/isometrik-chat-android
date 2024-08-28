package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessageTextBinding;

/**
 * The type Text message sent view holder.
 */
public class TextMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message text binding.
   */
  public final IsmSentMessageTextBinding ismSentMessageTextBinding;

  /**
   * Instantiates a new Text message sent view holder.
   *
   * @param ismSentMessageTextBinding the ism sent message text binding
   */
  public TextMessageSentViewHolder(final IsmSentMessageTextBinding ismSentMessageTextBinding) {
    super(ismSentMessageTextBinding.getRoot());
    this.ismSentMessageTextBinding = ismSentMessageTextBinding;
  }
}