package io.isometrik.ui.messages.tag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmTagUserItemBinding;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of members in conversation that can be tagged in a
 * message.
 */
public class TagUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final Context mContext;
  private final ArrayList<TagUserModel> tagUserModels;

  /**
   * Instantiates a new Tag user adapter.
   *
   * @param mContext the m context
   * @param tagUserModels the tag user models
   */
  public TagUserAdapter(Context mContext, ArrayList<TagUserModel> tagUserModels) {
    this.mContext = mContext;
    this.tagUserModels = tagUserModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    IsmTagUserItemBinding ismTagUserItemBinding =
        IsmTagUserItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new TagUserViewHolder(ismTagUserItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    TagUserViewHolder holder = (TagUserViewHolder) viewHolder;

    try {
      TagUserModel tagUserModel = tagUserModels.get(position);
      if (tagUserModel != null) {
        holder.ismTagUserItemBinding.tvUserName.setText(tagUserModel.getMemberName());

        if (tagUserModel.isOnline()) {
          holder.ismTagUserItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
        } else {
          holder.ismTagUserItemBinding.ivOnlineStatus.setImageDrawable(
              ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
        }

        holder.ismTagUserItemBinding.tvAdmin.setVisibility(
            tagUserModel.isAdmin() ? View.VISIBLE : View.GONE);
        if (PlaceholderUtils.isValidImageUrl(tagUserModel.getMemberProfileImageUrl())) {

          try {
            Glide.with(mContext)
                .load(tagUserModel.getMemberProfileImageUrl())
                .placeholder(R.drawable.ism_ic_profile)
                .transform(new CircleCrop())
                .into(holder.ismTagUserItemBinding.ivProfilePic);
          } catch (IllegalArgumentException | NullPointerException ignore) {
          }
        } else {
          PlaceholderUtils.setTextRoundDrawable(mContext, tagUserModel.getMemberName(),
              holder.ismTagUserItemBinding.ivProfilePic, position, 10);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return tagUserModels.size();
  }

  /**
   * The type Tag user view holder.
   */
  static class TagUserViewHolder extends RecyclerView.ViewHolder {

    private final IsmTagUserItemBinding ismTagUserItemBinding;

    /**
     * Instantiates a new Tag user view holder.
     *
     * @param ismTagUserItemBinding the ism tag user item binding
     */
    public TagUserViewHolder(final IsmTagUserItemBinding ismTagUserItemBinding) {
      super(ismTagUserItemBinding.getRoot());
      this.ismTagUserItemBinding = ismTagUserItemBinding;
    }
  }
}
