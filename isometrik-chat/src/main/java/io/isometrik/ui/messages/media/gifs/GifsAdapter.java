package io.isometrik.ui.messages.media.gifs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmGifStickerItemBinding;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of gifs.
 */
public class GifsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final ArrayList<GifsModel> gifsModels;
  private final Context mContext;

  /**
   * Instantiates a new Gifs adapter.
   *
   * @param gifsModels the gifs models
   * @param mContext the m context
   */
  public GifsAdapter(ArrayList<GifsModel> gifsModels, Context mContext) {
    this.gifsModels = gifsModels;
    this.mContext = mContext;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmGifStickerItemBinding ismGifItemBinding =
        IsmGifStickerItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new GifsViewHolder(ismGifItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    GifsViewHolder holder = (GifsViewHolder) viewHolder;

    try {
      GifsModel gifsModel = gifsModels.get(position);

      if (gifsModel.getGifName() != null) {
        holder.ismGifItemBinding.tvGifStickerName.setText(gifsModel.getGifName());
      } else {
        holder.ismGifItemBinding.tvGifStickerName.setText("");
      }
      try {

        Glide.with(mContext)
            .load(gifsModel.getGifImageUrl())
            .fitCenter()
            .into(holder.ismGifItemBinding.ivGifSticker);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return gifsModels.size();
  }

  /**
   * The type Gifs view holder.
   */
  static class GifsViewHolder extends RecyclerView.ViewHolder {

    private final IsmGifStickerItemBinding ismGifItemBinding;

    /**
     * Instantiates a new Gifs view holder.
     *
     * @param ismGifItemBinding the ism gif item binding
     */
    public GifsViewHolder(final IsmGifStickerItemBinding ismGifItemBinding) {
      super(ismGifItemBinding.getRoot());
      this.ismGifItemBinding = ismGifItemBinding;
    }
  }
}