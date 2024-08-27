package io.isometrik.ui.messages.media.gifs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmGifStickerCategoryItemBinding;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of gif categories.
 */
public class GifsCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final ArrayList<GifsCategoryModel> gifsCategoryModels;
  private final Context mContext;

  /**
   * Instantiates a new Gifs categories adapter.
   *
   * @param gifsCategoryModels the gifs category models
   * @param mContext the m context
   */
  public GifsCategoriesAdapter(ArrayList<GifsCategoryModel> gifsCategoryModels, Context mContext) {

    this.gifsCategoryModels = gifsCategoryModels;
    this.mContext = mContext;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmGifStickerCategoryItemBinding ismGifCategoryItemBinding =
        IsmGifStickerCategoryItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new GifCategoriesViewHolder(ismGifCategoryItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    GifCategoriesViewHolder holder = (GifCategoriesViewHolder) viewHolder;

    try {
      GifsCategoryModel gifsCategoryModel = gifsCategoryModels.get(position);

      holder.ismGifCategoryItemBinding.tvGifStickerCategoryName.setText(
          gifsCategoryModel.getGifCategoryName());
      holder.ismGifCategoryItemBinding.ivGifStickerCategoryImage.setImageResource(
          gifsCategoryModel.getGifCategoryImage());

      if (gifsCategoryModel.isSelected()) {

        holder.ismGifCategoryItemBinding.ivGifStickerCategoryImage.setColorFilter(
            ContextCompat.getColor(mContext, R.color.ism_gif_sticker_category_selected_black),
            android.graphics.PorterDuff.Mode.SRC_IN);

        holder.ismGifCategoryItemBinding.tvGifStickerCategoryName.setSelected(true);
      } else {

        holder.ismGifCategoryItemBinding.ivGifStickerCategoryImage.setColorFilter(
            ContextCompat.getColor(mContext, R.color.ism_gif_sticker_category_unselected_grey),
            android.graphics.PorterDuff.Mode.SRC_IN);
        holder.ismGifCategoryItemBinding.tvGifStickerCategoryName.setSelected(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return gifsCategoryModels.size();
  }

  /**
   * The type Gif categories view holder.
   */
  static class GifCategoriesViewHolder extends RecyclerView.ViewHolder {

    private final IsmGifStickerCategoryItemBinding ismGifCategoryItemBinding;

    /**
     * Instantiates a new Gif categories view holder.
     *
     * @param ismGifCategoryItemBinding the ism gif category item binding
     */
    public GifCategoriesViewHolder(
        final IsmGifStickerCategoryItemBinding ismGifCategoryItemBinding) {
      super(ismGifCategoryItemBinding.getRoot());
      this.ismGifCategoryItemBinding = ismGifCategoryItemBinding;
    }
  }
}
