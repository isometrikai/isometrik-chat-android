package io.isometrik.ui.conversations.details.observers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmConversationObserverItemBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the observers in an open conversation.
 */
public class ObserversAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<ObserversModel> observersModels;

  /**
   * Instantiates a new Observers adapter.
   *
   * @param mContext the m context
   * @param observersModels the observers models
   */
  public ObserversAdapter(Context mContext, ArrayList<ObserversModel> observersModels) {
    this.mContext = mContext;
    this.observersModels = observersModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmConversationObserverItemBinding ismConversationObserverItemBinding =
        IsmConversationObserverItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()),
            viewGroup, false);
    return new ObserversViewHolder(ismConversationObserverItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    ObserversViewHolder holder = (ObserversViewHolder) viewHolder;

    try {
      ObserversModel observersModel = observersModels.get(position);

      holder.ismConversationObserverItemBinding.tvUserName.setText(
          observersModel.getObserverName());

      if (PlaceholderUtils.isValidImageUrl(observersModel.getObserverProfileImageUrl())) {
        try {
          GlideApp.with(mContext)
              .load(observersModel.getObserverProfileImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismConversationObserverItemBinding.ivProfilePic);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, observersModel.getObserverName(),
            holder.ismConversationObserverItemBinding.ivProfilePic, position, 10);
      }
      if (observersModel.isOnline()) {
        holder.ismConversationObserverItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
      } else {
        holder.ismConversationObserverItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
      }
      holder.ismConversationObserverItemBinding.ivOnlineStatus.setVisibility(View.VISIBLE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return observersModels.size();
  }

  /**
   * The type Observers view holder.
   */
  static class ObserversViewHolder extends RecyclerView.ViewHolder {

    private final IsmConversationObserverItemBinding ismConversationObserverItemBinding;

    /**
     * Instantiates a new Observers view holder.
     *
     * @param ismConversationObserverItemBinding the ism conversation observer item binding
     */
    public ObserversViewHolder(
        final IsmConversationObserverItemBinding ismConversationObserverItemBinding) {
      super(ismConversationObserverItemBinding.getRoot());
      this.ismConversationObserverItemBinding = ismConversationObserverItemBinding;
    }
  }
}
