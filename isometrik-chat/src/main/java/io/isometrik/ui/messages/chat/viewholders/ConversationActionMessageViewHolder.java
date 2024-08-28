package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmConversationActionMessageBinding;

/**
 * The type Conversation action message view holder.
 */
public class ConversationActionMessageViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Ism conversation action message binding.
   */
  public final IsmConversationActionMessageBinding ismConversationActionMessageBinding;

  /**
   * Instantiates a new Conversation action message view holder.
   *
   * @param ismConversationActionMessageBinding the ism conversation action message binding
   */
  public ConversationActionMessageViewHolder(
      final IsmConversationActionMessageBinding ismConversationActionMessageBinding) {
    super(ismConversationActionMessageBinding.getRoot());
    this.ismConversationActionMessageBinding = ismConversationActionMessageBinding;
  }
}