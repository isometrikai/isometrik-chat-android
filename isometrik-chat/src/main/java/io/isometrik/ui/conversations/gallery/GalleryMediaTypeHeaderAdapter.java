package io.isometrik.ui.conversations.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import io.isometrik.chat.databinding.IsmGalleryMediaTypeHeaderItemBinding;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the media items types on gallery page opened on click of
 * view more from conversation details page.
 */
public class GalleryMediaTypeHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<GalleryMediaTypeHeaderModel> galleryMediaTypeHeaderModels;

  /**
   * Instantiates a new Gallery media type header adapter.
   *
   * @param mContext the m context
   * @param galleryModels the gallery models
   */
  public GalleryMediaTypeHeaderAdapter(Context mContext,
      ArrayList<GalleryMediaTypeHeaderModel> galleryModels) {
    this.mContext = mContext;
    this.galleryMediaTypeHeaderModels = galleryModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmGalleryMediaTypeHeaderItemBinding ismGalleryMediaTypeHeaderItemBinding =
        IsmGalleryMediaTypeHeaderItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new GalleryMediaTypeHeaderViewHolder(ismGalleryMediaTypeHeaderItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    GalleryMediaTypeHeaderViewHolder holder = (GalleryMediaTypeHeaderViewHolder) viewHolder;

    try {
      GalleryMediaTypeHeaderModel galleryMediaTypeHeaderModel =
          galleryMediaTypeHeaderModels.get(position);
      holder.ismGalleryMediaTypeHeaderItemBinding.tvMediaType.setText(
          galleryMediaTypeHeaderModel.getMediaTypeText());

      try {
        Glide.with(mContext)
            .load(galleryMediaTypeHeaderModel.getMediaTypeIcon())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(holder.ismGalleryMediaTypeHeaderItemBinding.ivMediaType);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return galleryMediaTypeHeaderModels.size();
  }

  /**
   * The type Gallery media type header view holder.
   */
  static class GalleryMediaTypeHeaderViewHolder extends RecyclerView.ViewHolder {

    private final IsmGalleryMediaTypeHeaderItemBinding ismGalleryMediaTypeHeaderItemBinding;

    /**
     * Instantiates a new Gallery media type header view holder.
     *
     * @param ismGalleryMediaTypeHeaderItemBinding the ism gallery media type header item binding
     */
    public GalleryMediaTypeHeaderViewHolder(
        final IsmGalleryMediaTypeHeaderItemBinding ismGalleryMediaTypeHeaderItemBinding) {
      super(ismGalleryMediaTypeHeaderItemBinding.getRoot());
      this.ismGalleryMediaTypeHeaderItemBinding = ismGalleryMediaTypeHeaderItemBinding;
    }
  }
}
