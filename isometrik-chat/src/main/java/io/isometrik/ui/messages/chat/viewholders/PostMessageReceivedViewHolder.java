package io.isometrik.ui.messages.chat.viewholders;

import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmReceivedMessagePostBinding;


public class PostMessageReceivedViewHolder extends RecyclerView.ViewHolder {

    /**
     * The Ism sent message contact binding.
     */
    public final IsmReceivedMessagePostBinding ismReceivedMessagePostBinding;

    /**
     * Instantiates a new Contact message sent view holder.
     *
     * @param ismReceivedMessagePostBinding the ism sent message contact binding
     */
    public PostMessageReceivedViewHolder(final IsmReceivedMessagePostBinding ismReceivedMessagePostBinding) {
        super(ismReceivedMessagePostBinding.getRoot());
        this.ismReceivedMessagePostBinding = ismReceivedMessagePostBinding;
    }
}
