package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessageWhiteboardBinding;

/**
 * The type Whiteboard message received view holder.
 */
public class WhiteboardMessageReceivedViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism received message whiteboard binding.
   */
  public final IsmReceivedMessageWhiteboardBinding ismReceivedMessageWhiteboardBinding;

  /**
   * Instantiates a new Whiteboard message received view holder.
   *
   * @param ismReceivedMessageWhiteboardBinding the ism received message whiteboard binding
   */
  public WhiteboardMessageReceivedViewHolder(
      final IsmReceivedMessageWhiteboardBinding ismReceivedMessageWhiteboardBinding) {
    super(ismReceivedMessageWhiteboardBinding.getRoot());
    this.ismReceivedMessageWhiteboardBinding = ismReceivedMessageWhiteboardBinding;
  }
}