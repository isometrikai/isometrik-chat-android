package io.isometrik.ui.conversations.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmGalleryConversationDetailsMediaItemBinding;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the gallery items on conversation details screen.
 */
public class ConversationDetailsGalleryAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<GalleryModel> galleryModels;

  /**
   * Instantiates a new Conversation details gallery adapter.
   *
   * @param mContext the m context
   * @param galleryModels the gallery models
   */
  public ConversationDetailsGalleryAdapter(Context mContext,
      ArrayList<GalleryModel> galleryModels) {
    this.mContext = mContext;
    this.galleryModels = galleryModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmGalleryConversationDetailsMediaItemBinding ismGalleryMediaItemBinding =
        IsmGalleryConversationDetailsMediaItemBinding.inflate(
            LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
    return new GalleryViewHolder(ismGalleryMediaItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    GalleryViewHolder holder = (GalleryViewHolder) viewHolder;

    try {
      GalleryModel galleryModel = galleryModels.get(position);
      holder.ismGalleryConversationDetailsMediaItemBinding.tvMediaType.setText(
          galleryModel.getMediaTypeText());
      holder.ismGalleryConversationDetailsMediaItemBinding.tvSenderName.setText(
          galleryModel.getSenderName());
      holder.ismGalleryConversationDetailsMediaItemBinding.tvTime.setText(
          galleryModel.getSentTime());
      holder.ismGalleryConversationDetailsMediaItemBinding.ivPlayVideo.setVisibility(
          galleryModel.isVideo() ? View.VISIBLE : View.GONE);
      if(PlaceholderUtils.isValidImageUrl(galleryModel.getSenderProfileImageUrl())) {

        try {
          Glide.with(mContext)
              .load(galleryModel.getSenderProfileImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismGalleryConversationDetailsMediaItemBinding.ivSenderImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      }else{
        PlaceholderUtils.setTextRoundDrawable(mContext, galleryModel.getSenderName(),
            holder.ismGalleryConversationDetailsMediaItemBinding.ivSenderImage, position, 12);
      }
      try {
        Glide.with(mContext)
            .load(galleryModel.getMediaTypeIcon())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(holder.ismGalleryConversationDetailsMediaItemBinding.ivMediaType);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }

      if (galleryModel.getMediaSize() == null) {
        holder.ismGalleryConversationDetailsMediaItemBinding.tvMediaSize.setVisibility(View.GONE);
      } else {
        holder.ismGalleryConversationDetailsMediaItemBinding.tvMediaSize.setText(
            galleryModel.getMediaSize());
        holder.ismGalleryConversationDetailsMediaItemBinding.tvMediaSize.setVisibility(
            View.VISIBLE);
      }

      if (galleryModel.getMediaDescription() == null) {
        holder.ismGalleryConversationDetailsMediaItemBinding.tvMediaDescription.setVisibility(
            View.GONE);
      } else {
        holder.ismGalleryConversationDetailsMediaItemBinding.tvMediaDescription.setVisibility(
            View.VISIBLE);
        holder.ismGalleryConversationDetailsMediaItemBinding.tvMediaDescription.setText(
            galleryModel.getMediaDescription());
      }

      if (galleryModel.getMediaUrl() == null) {
        holder.ismGalleryConversationDetailsMediaItemBinding.ivMediaImage.setVisibility(View.GONE);
      } else {
        holder.ismGalleryConversationDetailsMediaItemBinding.ivMediaImage.setVisibility(
            View.VISIBLE);
        try {
          Glide.with(mContext)
              .load(galleryModel.getMediaUrl())
              .into(holder.ismGalleryConversationDetailsMediaItemBinding.ivMediaImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return galleryModels.size();
  }

  /**
   * The type Gallery view holder.
   */
  static class GalleryViewHolder extends RecyclerView.ViewHolder {

    private final IsmGalleryConversationDetailsMediaItemBinding
        ismGalleryConversationDetailsMediaItemBinding;

    /**
     * Instantiates a new Gallery view holder.
     *
     * @param ismGalleryConversationDetailsMediaItemBinding the ism gallery conversation details
     * media item binding
     */
    public GalleryViewHolder(
        final IsmGalleryConversationDetailsMediaItemBinding ismGalleryConversationDetailsMediaItemBinding) {
      super(ismGalleryConversationDetailsMediaItemBinding.getRoot());
      this.ismGalleryConversationDetailsMediaItemBinding =
          ismGalleryConversationDetailsMediaItemBinding;
    }
  }
}
