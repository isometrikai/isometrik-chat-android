package io.isometrik.ui.messages.chat.contactList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.isometrik.chat.databinding.IsmItemSharedContactBinding;

/**
 * The recycler view adapter for the list of contact shared
 */
public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final ArrayList<ContactSharedModel> contacts;
    private ContactListener listener;

    /**
     * Instantiates a new Users adapter.
     *
     * @param mContext the m context
     * @param contacts the users
     */
    ContactListAdapter(Context mContext, ArrayList<ContactSharedModel> contacts, ContactListener listener) {
        this.mContext = mContext;
        this.contacts = contacts;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        IsmItemSharedContactBinding ismItemSharedContactBinding =
                IsmItemSharedContactBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
                        viewGroup, false);
        return new ContactViewHolder(ismItemSharedContactBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        ContactViewHolder holder = (ContactViewHolder) viewHolder;

        try {
            ContactSharedModel contact = contacts.get(position);
            if (contact != null) {
                holder.ismUnselectedMemberItemBinding.tvContactName.setText(contact.getName());
                holder.ismUnselectedMemberItemBinding.tvContactIdentifier.setText(contact.getIdentifier());

                holder.ismUnselectedMemberItemBinding.tvAdd.setOnClickListener(v -> listener.onAddClick(contact));
                holder.ismUnselectedMemberItemBinding.ivCall.setOnClickListener(v -> listener.onCallClick(contact));
                holder.ismUnselectedMemberItemBinding.ivSms.setOnClickListener(v -> listener.onSMSClick(contact));


//        if (PlaceholderUtils.isValidImageUrl(contact.getUserProfilePic())) {
//
//          try {
//            GlideApp.with(mContext)
//                .load(contact.p())
//                .placeholder(R.drawable.ism_ic_profile)
//                .transform(new CircleCrop())
//                .into(holder.ismUnselectedMemberItemBinding.ivProfilePic);
//          } catch (IllegalArgumentException | NullPointerException ignore) {
//          }
//        } else {
//          PlaceholderUtils.setTextRoundDrawable(mContext, contact.getUserName(),
//              holder.ismUnselectedMemberItemBinding.ivProfilePic, position, 10);
//
//        }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * The type Users view holder.
     */
    static class ContactViewHolder extends RecyclerView.ViewHolder {

        private final IsmItemSharedContactBinding ismUnselectedMemberItemBinding;

        /**
         * Instantiates a new Users view holder.
         *
         * @param ismItemSharedContactBinding the ism unselected member item binding
         */
        public ContactViewHolder(final IsmItemSharedContactBinding ismItemSharedContactBinding) {
            super(ismItemSharedContactBinding.getRoot());
            this.ismUnselectedMemberItemBinding = ismItemSharedContactBinding;
        }
    }

    interface ContactListener{
        void onAddClick(ContactSharedModel contact);
        void onCallClick(ContactSharedModel contact);
        void onSMSClick(ContactSharedModel contact);
    }
}
