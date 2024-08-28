package io.isometrik.ui.messages.media.stickers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.databinding.IsmGifStickerItemBinding;
import io.isometrik.ui.utils.GlideApp;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of stickers.
 */
public class StickersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final ArrayList<StickersModel> stickersModels;
  private final Context mContext;

  /**
   * Instantiates a new Stickers adapter.
   *
   * @param stickersModels the stickers models
   * @param mContext the m context
   */
  public StickersAdapter(ArrayList<StickersModel> stickersModels, Context mContext) {
    this.stickersModels = stickersModels;
    this.mContext = mContext;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmGifStickerItemBinding ismStickerItemBinding =
        IsmGifStickerItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new StickersViewHolder(ismStickerItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    StickersViewHolder holder = (StickersViewHolder) viewHolder;

    try {
      StickersModel stickersModel = stickersModels.get(position);

      if (stickersModel.getStickerName() != null) {
        holder.ismStickerItemBinding.tvGifStickerName.setText(stickersModel.getStickerName());
      } else {
        holder.ismStickerItemBinding.tvGifStickerName.setText("");
      }
      try {

        GlideApp.with(mContext)
            .load(stickersModel.getStickerImageUrl())
            .fitCenter()
            .into(holder.ismStickerItemBinding.ivGifSticker);
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return stickersModels.size();
  }

  /**
   * The type Stickers view holder.
   */
  static class StickersViewHolder extends RecyclerView.ViewHolder {

    private final IsmGifStickerItemBinding ismStickerItemBinding;

    /**
     * Instantiates a new Stickers view holder.
     *
     * @param ismStickerItemBinding the ism sticker item binding
     */
    public StickersViewHolder(final IsmGifStickerItemBinding ismStickerItemBinding) {
      super(ismStickerItemBinding.getRoot());
      this.ismStickerItemBinding = ismStickerItemBinding;
    }
  }
}