package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessageWhiteboardBinding;

/**
 * The type Whiteboard message sent view holder.
 */
public class WhiteboardMessageSentViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism sent message whiteboard binding.
   */
  public final IsmSentMessageWhiteboardBinding ismSentMessageWhiteboardBinding;

  /**
   * Instantiates a new Whiteboard message sent view holder.
   *
   * @param ismSentMessageWhiteboardBinding the ism sent message whiteboard binding
   */
  public WhiteboardMessageSentViewHolder(
      final IsmSentMessageWhiteboardBinding ismSentMessageWhiteboardBinding) {
    super(ismSentMessageWhiteboardBinding.getRoot());
    this.ismSentMessageWhiteboardBinding = ismSentMessageWhiteboardBinding;
  }
}