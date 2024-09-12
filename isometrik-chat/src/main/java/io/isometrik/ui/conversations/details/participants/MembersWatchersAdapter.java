package io.isometrik.ui.conversations.details.participants;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.ui.conversations.details.groupconversation.ConversationDetailsActivity;
import io.isometrik.ui.conversations.details.participants.members.MembersFragment;
import io.isometrik.ui.conversations.details.participants.watchers.WatchersFragment;
import io.isometrik.chat.databinding.IsmConversationMemberItemBinding;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;

import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the members/watchers in a conversation.
 */
public class MembersWatchersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final Context mContext;
  private final ArrayList<MembersWatchersModel> membersModels;
  private final MembersFragment membersFragment;
  private final WatchersFragment watchersFragment;

  /**
   * Instantiates a new Members watchers adapter.
   *
   * @param mContext the m context
   * @param membersModels the members models
   * @param membersFragment the members fragment
   * @param watchersFragment the watchers fragment
   */
  public MembersWatchersAdapter(Context mContext, ArrayList<MembersWatchersModel> membersModels,
      MembersFragment membersFragment, WatchersFragment watchersFragment) {
    this.mContext = mContext;
    this.membersModels = membersModels;
    this.membersFragment = membersFragment;
    this.watchersFragment = watchersFragment;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    IsmConversationMemberItemBinding ismConversationMemberItemBinding =
        IsmConversationMemberItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new MemberViewHolder(ismConversationMemberItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    MemberViewHolder holder = (MemberViewHolder) viewHolder;

    try {
      MembersWatchersModel membersModel = membersModels.get(position);
      if (membersModel != null) {
        if (membersModel.isDeletedUser()) {
          SpannableString spannableString = new SpannableString(membersModel.getMemberName());
          spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spannableString.length(), 0);
          holder.ismConversationMemberItemBinding.tvUserName.setText(spannableString);
        } else {
          holder.ismConversationMemberItemBinding.tvUserName.setText(membersModel.getMemberName());
        }
        if (membersModel.isOnline()) {
          holder.ismConversationMemberItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
        } else {
          holder.ismConversationMemberItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
        }

        if (!membersModel.isDeletedUser()
            && membersModel.isUserAnAdmin()
            && !membersModel.isSelf()) {
          holder.ismConversationMemberItemBinding.ivKickOut.setVisibility(View.VISIBLE);
          holder.ismConversationMemberItemBinding.ivKickOut.setOnClickListener(v -> {

            if (mContext instanceof ConversationDetailsActivity) {
              ((ConversationDetailsActivity) mContext).kickOutMember(membersModel.getMemberId(),
                  membersModel.getMemberName());
            } else if (membersFragment != null) {
              membersFragment.kickOutMember(membersModel.getMemberId(),
                  membersModel.getMemberName());
            } else if (watchersFragment != null) {
              watchersFragment.kickOutMember(membersModel.getMemberId(),
                  membersModel.getMemberName());
            }
          });
        } else {
          holder.ismConversationMemberItemBinding.ivKickOut.setVisibility(View.GONE);
        }
        if (membersModel.isAdmin()) {
          holder.ismConversationMemberItemBinding.tvAdmin.setText(
              mContext.getString(R.string.ism_admin));
          holder.ismConversationMemberItemBinding.tvAdmin.setVisibility(View.VISIBLE);

          if (!membersModel.isDeletedUser()
              && membersModel.isUserAnAdmin()
              && !membersModel.isSelf()) {
            holder.ismConversationMemberItemBinding.ivRemoveAdmin.setVisibility(View.VISIBLE);

            holder.ismConversationMemberItemBinding.ivRemoveAdmin.setOnClickListener(v -> {

              if (mContext instanceof ConversationDetailsActivity) {
                ((ConversationDetailsActivity) mContext).removeAdmin(membersModel.getMemberId(),
                    membersModel.getMemberName());
              } else if (membersFragment != null) {
                membersFragment.removeAdmin(membersModel.getMemberId(),
                    membersModel.getMemberName());
              } else if (watchersFragment != null) {
                watchersFragment.removeAdmin(membersModel.getMemberId(),
                    membersModel.getMemberName());
              }
            });
          } else {
            holder.ismConversationMemberItemBinding.ivRemoveAdmin.setVisibility(View.GONE);
          }
        } else {
          holder.ismConversationMemberItemBinding.ivRemoveAdmin.setVisibility(View.GONE);
          if (!membersModel.isDeletedUser() && membersModel.isUserAnAdmin()) {
            holder.ismConversationMemberItemBinding.tvAdmin.setText(
                mContext.getString(R.string.ism_make_admin));
            holder.ismConversationMemberItemBinding.tvAdmin.setVisibility(View.VISIBLE);

            holder.ismConversationMemberItemBinding.tvAdmin.setOnClickListener(v -> {

              if (mContext instanceof ConversationDetailsActivity) {
                ((ConversationDetailsActivity) mContext).makeAdmin(membersModel.getMemberId(),
                    membersModel.getMemberName());
              } else if (membersFragment != null) {
                membersFragment.makeAdmin(membersModel.getMemberId(), membersModel.getMemberName());
              } else if (watchersFragment != null) {
                watchersFragment.makeAdmin(membersModel.getMemberId(),
                    membersModel.getMemberName());
              }
            });
          } else {
            holder.ismConversationMemberItemBinding.tvAdmin.setVisibility(View.GONE);
          }
        }
        if (PlaceholderUtils.isValidImageUrl(membersModel.getMemberProfileImageUrl())) {

          try {
            Glide.with(mContext)
                .load(membersModel.getMemberProfileImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismConversationMemberItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, membersModel.getMemberName(),
              holder.ismConversationMemberItemBinding.ivProfilePic, position, 10);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return membersModels.size();
  }

  /**
   * The type Member view holder.
   */
  static class MemberViewHolder extends RecyclerView.ViewHolder {

    private final IsmConversationMemberItemBinding ismConversationMemberItemBinding;

    /**
     * Instantiates a new Member view holder.
     *
     * @param ismConversationMemberItemBinding the ism conversation member item binding
     */
    public MemberViewHolder(
        final IsmConversationMemberItemBinding ismConversationMemberItemBinding) {
      super(ismConversationMemberItemBinding.getRoot());
      this.ismConversationMemberItemBinding = ismConversationMemberItemBinding;
    }
  }
}
