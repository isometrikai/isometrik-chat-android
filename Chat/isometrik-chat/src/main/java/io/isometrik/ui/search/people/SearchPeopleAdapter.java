package io.isometrik.ui.search.people;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmSearchPeopleItemBinding;
import io.isometrik.ui.utils.GlideApp;
import io.isometrik.chat.utils.PlaceholderUtils;
import java.util.ArrayList;

/**
 * The recycler view adapter for the list of the search people results.
 */
public class SearchPeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final Context mContext;
  private final ArrayList<SearchPeopleModel> peopleModels;

  /**
   * Instantiates a new Search people adapter.
   *
   * @param mContext the m context
   * @param peopleModels the people models
   */
  public SearchPeopleAdapter(Context mContext, ArrayList<SearchPeopleModel> peopleModels) {
    this.mContext = mContext;
    this.peopleModels = peopleModels;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    IsmSearchPeopleItemBinding ismSearchPeopleItemBinding =
        IsmSearchPeopleItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup,
            false);
    return new PeopleViewHolder(ismSearchPeopleItemBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    PeopleViewHolder holder = (PeopleViewHolder) viewHolder;

    try {
      SearchPeopleModel peopleModel = peopleModels.get(position);

      holder.ismSearchPeopleItemBinding.tvUserName.setText(peopleModel.getUserName());
      holder.ismSearchPeopleItemBinding.tvUserIdentifier.setText(peopleModel.getUserIdentifier());
      if (PlaceholderUtils.isValidImageUrl(peopleModel.getUserProfileImageUrl())) {

        try {
          GlideApp.with(mContext)
              .load(peopleModel.getUserProfileImageUrl())
              .placeholder(R.drawable.ism_ic_profile)
              .transform(new CircleCrop())
              .into(holder.ismSearchPeopleItemBinding.ivUserImage);
        } catch (IllegalArgumentException | NullPointerException ignore) {
        }
      } else {
        PlaceholderUtils.setTextRoundDrawable(mContext, peopleModel.getUserName(),
            holder.ismSearchPeopleItemBinding.ivUserImage, position, 20);
      }
      if (peopleModel.isOnline()) {
        holder.ismSearchPeopleItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_online_status_circle));
      } else {
        holder.ismSearchPeopleItemBinding.ivOnlineStatus.setImageDrawable(
            ContextCompat.getDrawable(mContext, R.drawable.ism_user_offline_status_circle));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return peopleModels.size();
  }

  /**
   * The type People view holder.
   */
  static class PeopleViewHolder extends RecyclerView.ViewHolder {

    private final IsmSearchPeopleItemBinding ismSearchPeopleItemBinding;

    /**
     * Instantiates a new People view holder.
     *
     * @param ismSearchPeopleItemBinding the ism search people item binding
     */
    public PeopleViewHolder(final IsmSearchPeopleItemBinding ismSearchPeopleItemBinding) {
      super(ismSearchPeopleItemBinding.getRoot());
      this.ismSearchPeopleItemBinding = ismSearchPeopleItemBinding;
    }
  }
}
