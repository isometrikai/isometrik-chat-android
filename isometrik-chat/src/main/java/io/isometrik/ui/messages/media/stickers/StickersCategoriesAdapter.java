package io.isometrik.ui.messages.media.stickers;

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
 * The recycler view adapter for the list of sticker categories.
 */
public class StickersCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final ArrayList<StickersCategoryModel> stickersCategoryModels;
  private final Context mContext;

  /**
   * Instantiates a new Stickers categories adapter.
   *
   * @param stickersCategoryModels the stickers category models
   * @param mContext the m context
   */
  public StickersCategoriesAdapter(ArrayList<StickersCategoryModel> stickersCategoryModels,
      Context mContext) {

    this.stickersCategoryModels = stickersCategoryModels;
    this.mContext = mContext;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmGifStickerCategoryItemBinding ismStickerCategoryItemBinding =
        IsmGifStickerCategoryItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new StickerCategoriesViewHolder(ismStickerCategoryItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    StickerCategoriesViewHolder holder = (StickerCategoriesViewHolder) viewHolder;

    try {
      StickersCategoryModel stickersCategoryModel = stickersCategoryModels.get(position);

      holder.ismStickerCategoryItemBinding.tvGifStickerCategoryName.setText(
          stickersCategoryModel.getStickerCategoryName());
      holder.ismStickerCategoryItemBinding.ivGifStickerCategoryImage.setImageResource(
          stickersCategoryModel.getStickerCategoryImage());

      if (stickersCategoryModel.isSelected()) {

        holder.ismStickerCategoryItemBinding.ivGifStickerCategoryImage.setColorFilter(
            ContextCompat.getColor(mContext, R.color.ism_gif_sticker_category_selected_black),
            android.graphics.PorterDuff.Mode.SRC_IN);

        holder.ismStickerCategoryItemBinding.tvGifStickerCategoryName.setSelected(true);
      } else {

        holder.ismStickerCategoryItemBinding.ivGifStickerCategoryImage.setColorFilter(
            ContextCompat.getColor(mContext, R.color.ism_gif_sticker_category_unselected_grey),
            android.graphics.PorterDuff.Mode.SRC_IN);
        holder.ismStickerCategoryItemBinding.tvGifStickerCategoryName.setSelected(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return stickersCategoryModels.size();
  }

  /**
   * The type Sticker categories view holder.
   */
  static class StickerCategoriesViewHolder extends RecyclerView.ViewHolder {

    private final IsmGifStickerCategoryItemBinding ismStickerCategoryItemBinding;

    /**
     * Instantiates a new Sticker categories view holder.
     *
     * @param ismStickerCategoryItemBinding the ism sticker category item binding
     */
    public StickerCategoriesViewHolder(
        final IsmGifStickerCategoryItemBinding ismStickerCategoryItemBinding) {
      super(ismStickerCategoryItemBinding.getRoot());
      this.ismStickerCategoryItemBinding = ismStickerCategoryItemBinding;
    }
  }
}
