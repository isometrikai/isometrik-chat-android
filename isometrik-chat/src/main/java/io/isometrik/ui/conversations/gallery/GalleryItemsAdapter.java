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
import io.isometrik.chat.databinding.IsmGalleryMediaItemBinding;
import com.bumptech.glide.Glide;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the gallery items on gallery page opened on click of
 * view more from conversation details page.
 */
public class GalleryItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<GalleryModel> galleryModels;

  /**
   * Instantiates a new Gallery items adapter.
   *
   * @param mContext the m context
   * @param galleryModels the gallery models
   */
  public GalleryItemsAdapter(Context mContext, ArrayList<GalleryModel> galleryModels) {
    this.mContext = mContext;
    this.galleryModels = galleryModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmGalleryMediaItemBinding ismGalleryMediaItemBinding =
        IsmGalleryMediaItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new GalleryViewHolder(ismGalleryMediaItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    GalleryViewHolder holder = (GalleryViewHolder) viewHolder;

    try {
      GalleryModel galleryModel = galleryModels.get(position);
      holder.ismGalleryMediaItemBinding.tvMediaType.setText(galleryModel.getMediaTypeText());
      holder.ismGalleryMediaItemBinding.tvSenderName.setText(galleryModel.getSenderName());
      holder.ismGalleryMediaItemBinding.tvTime.setText(galleryModel.getSentTime());
      holder.ismGalleryMediaItemBinding.ivPlayVideo.setVisibility(
          galleryModel.isVideo() ? View.VISIBLE : View.GONE);
      if(PlaceholderUtils.isValidImageUrl(galleryModel.getSenderProfileImageUrl())){
      try {
        Glide.with(mContext)
            .load(galleryModel.getSenderProfileImageUrl())
            .placeholder(R.drawable.ism_ic_profile)
            .transform(new CircleCrop())
            .into(holder.ismGalleryMediaItemBinding.ivSenderImage);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
    }else{
      PlaceholderUtils.setTextRoundDrawable(mContext, galleryModel.getSenderName(),
          holder.ismGalleryMediaItemBinding.ivSenderImage, position, 12);
    }
      try {
        Glide.with(mContext)
            .load(galleryModel.getMediaTypeIcon())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(holder.ismGalleryMediaItemBinding.ivMediaType);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }

      if (galleryModel.getMediaSize() == null) {
        holder.ismGalleryMediaItemBinding.tvMediaSize.setVisibility(View.GONE);
      } else {
        holder.ismGalleryMediaItemBinding.tvMediaSize.setText(galleryModel.getMediaSize());
        holder.ismGalleryMediaItemBinding.tvMediaSize.setVisibility(View.VISIBLE);
      }

      if (galleryModel.getMediaDescription() == null) {
        holder.ismGalleryMediaItemBinding.tvMediaDescription.setVisibility(View.GONE);
      } else {
        holder.ismGalleryMediaItemBinding.tvMediaDescription.setVisibility(View.VISIBLE);
        holder.ismGalleryMediaItemBinding.tvMediaDescription.setText(
            galleryModel.getMediaDescription());
      }

      if (galleryModel.getMediaUrl() == null) {
        holder.ismGalleryMediaItemBinding.ivMediaImage.setVisibility(View.GONE);
      } else {
        holder.ismGalleryMediaItemBinding.ivMediaImage.setVisibility(View.VISIBLE);
        try {
          Glide.with(mContext)
              .load(galleryModel.getMediaUrl())
              .into(holder.ismGalleryMediaItemBinding.ivMediaImage);
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

    private final IsmGalleryMediaItemBinding ismGalleryMediaItemBinding;

    /**
     * Instantiates a new Gallery view holder.
     *
     * @param ismGalleryMediaItemBinding the ism gallery media item binding
     */
    public GalleryViewHolder(final IsmGalleryMediaItemBinding ismGalleryMediaItemBinding) {
      super(ismGalleryMediaItemBinding.getRoot());
      this.ismGalleryMediaItemBinding = ismGalleryMediaItemBinding;
    }
  }
}
