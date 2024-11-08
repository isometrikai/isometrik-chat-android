package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmSentMessagePostBinding;


public class PostMessageSentViewHolder extends RecyclerView.ViewHolder {

    /**
     * The Ism sent message contact binding.
     */
    public final IsmSentMessagePostBinding ismSentMessagePostBinding;

    /**
     * Instantiates a new Contact message sent view holder.
     *
     * @param ismSentMessagePostBinding the ism sent message contact binding
     */
    public PostMessageSentViewHolder(final IsmSentMessagePostBinding ismSentMessagePostBinding) {
        super(ismSentMessagePostBinding.getRoot());
        this.ismSentMessagePostBinding = ismSentMessagePostBinding;
    }
}

