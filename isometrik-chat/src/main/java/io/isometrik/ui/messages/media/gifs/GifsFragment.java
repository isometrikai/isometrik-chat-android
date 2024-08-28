package io.isometrik.ui.messages.media.gifs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBottomsheetGifsStickersBinding;
import io.isometrik.ui.messages.media.MediaSelectedToBeShared;
import io.isometrik.chat.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

import static io.isometrik.ui.messages.media.gifstickerutils.Constants.GIFS_STICKERS_PAGE_SIZE;

/**
 * The fragment to fetch list of gifs in a category, with paging and
 * search.
 */
public class GifsFragment extends BottomSheetDialogFragment implements GifsContract.View {
  /**
   * The constant TAG.
   */
  public static final String TAG = "GifsFragment";

  /**
   * Instantiates a new gifs fragment.
   */
  public GifsFragment() {
    // Required empty public constructor
  }

  private IsmBottomsheetGifsStickersBinding ismBottomsheetGifsBinding;
  private GifsContract.Presenter gifsPresenter;
  private Activity activity;
  private ArrayList<GifsCategoryModel> gifsCategories;
  private ArrayList<GifsModel> gifs;
  private GifsAdapter gifsAdapter;
  private String currentlySelectedGifCategoryId;
  private GridLayoutManager gridLayoutManager;
  private MediaSelectedToBeShared mediaSelectedToBeShared;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    ismBottomsheetGifsBinding =
        IsmBottomsheetGifsStickersBinding.inflate(inflater, container, false);
    updateShimmerVisibility(true);
    gifsCategories = new ArrayList<>();
    ismBottomsheetGifsBinding.rvGifsStickersCategories.setLayoutManager(
        new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

    gifs = new ArrayList<>();
    gifsAdapter = new GifsAdapter(gifs, activity);
    gridLayoutManager = new GridLayoutManager(activity, 2);
    ismBottomsheetGifsBinding.rvGifsStickers.setLayoutManager(gridLayoutManager);
    ismBottomsheetGifsBinding.rvGifsStickers.setNestedScrollingEnabled(true);
    ismBottomsheetGifsBinding.rvGifsStickers.setAdapter(gifsAdapter);

    gifsPresenter.fetchGifsCategories();

    ismBottomsheetGifsBinding.rvGifsStickers.addOnScrollListener(gifsOnScrollListener);
    ismBottomsheetGifsBinding.rvGifsStickers.addOnItemTouchListener(
        new RecyclerItemClickListener(activity, ismBottomsheetGifsBinding.rvGifsStickers,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {
                if (position >= 0) {
                  GifsModel giftsModel = gifs.get(position);

                  mediaSelectedToBeShared.gifShareRequested(giftsModel.getGifName(),
                      giftsModel.getGifImageUrl(), giftsModel.getGifStillUrl());
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }

        ));

    ismBottomsheetGifsBinding.etSearch.addTextChangedListener(new TextWatcher() {

      public void afterTextChanged(Editable s) {

      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (ismBottomsheetGifsBinding.rlSearch.getVisibility() == View.VISIBLE) {
          if (s.length() > 0 && !(s.toString().trim().isEmpty())) {

            gifsPresenter.searchGifsInACategory(currentlySelectedGifCategoryId, s.toString(),
                GIFS_STICKERS_PAGE_SIZE, 0);
          } else {
            gifsPresenter.fetchGifsInACategory(currentlySelectedGifCategoryId,
                GIFS_STICKERS_PAGE_SIZE, 0);
          }
        }
      }
    });

    ismBottomsheetGifsBinding.btSearch.setOnClickListener(v -> {
      String text = ismBottomsheetGifsBinding.etSearch.getText().toString();
      if (text.isEmpty()) {
        Toast.makeText(activity, R.string.ism_enter_gifs_search_text, Toast.LENGTH_SHORT).show();
      } else {
        gifsPresenter.searchGifsInACategory(currentlySelectedGifCategoryId, text,
            GIFS_STICKERS_PAGE_SIZE, 0);
      }
    });

    //To allow scroll on gif's recyclerview
    ismBottomsheetGifsBinding.rvGifsStickers.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    ismBottomsheetGifsBinding.rvGifsStickersCategories.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    ismBottomsheetGifsBinding.etSearch.setHint(getString(R.string.ism_search_gifs));
    return ismBottomsheetGifsBinding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ismBottomsheetGifsBinding = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    gifsPresenter = new GifsPresenter();
    gifsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    gifsPresenter.detachView();
    activity = null;
  }

  @Override
  public void onGifsCategoriesFetchedSuccessfully(ArrayList<GifsCategoryModel> gifsCategoryModels) {

    if (gifsCategoryModels.size() > 0) {
      gifsCategories.addAll(gifsCategoryModels);
      GifsCategoriesAdapter gifsCategoriesAdapter =
          new GifsCategoriesAdapter(gifsCategories, activity);

      if (gifsCategories.size() > 0) {
        currentlySelectedGifCategoryId = gifsCategories.get(0).getGifCategoryName();

        gifs.addAll(gifsCategories.get(0).getGifs());
        gifsAdapter.notifyDataSetChanged();
        updateShimmerVisibility(false);
        if (currentlySelectedGifCategoryId.equals(
            GifCategoriesRepository.GifCategoryNameEnum.Classic.getValue())) {
          ismBottomsheetGifsBinding.rlSearch.setVisibility(View.GONE);
        }
      }
      ismBottomsheetGifsBinding.rvGifsStickersCategories.setAdapter(gifsCategoriesAdapter);

      ismBottomsheetGifsBinding.rvGifsStickersCategories.addOnItemTouchListener(
          new RecyclerItemClickListener(activity,
              ismBottomsheetGifsBinding.rvGifsStickersCategories,
              new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {
                  if (position >= 0) {

                    GifsCategoryModel gifsCategoryModel = gifsCategories.get(position);

                    currentlySelectedGifCategoryId = gifsCategoryModel.getGifCategoryName();
                    if (gifsCategoryModel.getGifCategoryName()
                        .equals(GifCategoriesRepository.GifCategoryNameEnum.Classic.getValue())) {
                      ismBottomsheetGifsBinding.rlSearch.setVisibility(View.GONE);
                      ArrayList<GifsModel> gifsData = gifsCategories.get(position).getGifs();
                      if (gifsData.size() > 0) {

                        ismBottomsheetGifsBinding.rvGifsStickers.setVisibility(View.VISIBLE);
                        ismBottomsheetGifsBinding.tvNoGifsStickers.setVisibility(View.GONE);

                        gifs.clear();
                        gifs.addAll(gifsData);
                        updateShimmerVisibility(false);
                        gifsAdapter.notifyDataSetChanged();
                      } else {

                        ismBottomsheetGifsBinding.tvNoGifsStickers.setText(
                            getString(R.string.ism_no_gifs_in_category));
                        ismBottomsheetGifsBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
                        ismBottomsheetGifsBinding.rvGifsStickers.setVisibility(View.GONE);
                      }
                    } else {
                      ismBottomsheetGifsBinding.rlSearch.setVisibility(View.VISIBLE);
                      gifs.clear();
                      gifsAdapter.notifyDataSetChanged();
                      ismBottomsheetGifsBinding.etSearch.setText("");
                      ismBottomsheetGifsBinding.pbLoading.setVisibility(View.VISIBLE);
                      updateShimmerVisibility(true);
                      //Not needed anymore, as triggered due to update of edittext to empty text.
                      //gifsPresenter.fetchGifsInACategory(currentlySelectedGifCategoryId,
                      //    GIFS_STICKERS_PAGE_SIZE, 0);
                    }
                  }

                  for (int i = 0; i < gifsCategories.size(); i++) {
                    GifsCategoryModel gifsCategoryModel = gifsCategories.get(i);
                    gifsCategoryModel.setSelected((i == position));
                  }
                  gifsCategoriesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
              }

          ));

    } else {
      ismBottomsheetGifsBinding.tvNoGifsStickers.setText(
          getString(R.string.ism_no_gifs_stickers_category, getString(R.string.ism_no_gifs)));
      ismBottomsheetGifsBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onGifsFetchedInCategory(String categoryId, ArrayList<GifsModel> gifsModels,
      boolean notOnScroll) {
    if (categoryId.equals(currentlySelectedGifCategoryId)) {

      if (activity != null) {

        activity.runOnUiThread(() -> {

          if (notOnScroll) {
            gifs.clear();
          }
          gifs.addAll(gifsModels);

          if (gifs.size() == 0) {

            ismBottomsheetGifsBinding.tvNoGifsStickers.setText(
                getString(R.string.ism_no_gifs_in_category));
            ismBottomsheetGifsBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
            ismBottomsheetGifsBinding.rvGifsStickers.setVisibility(View.GONE);
          } else {
            ismBottomsheetGifsBinding.tvNoGifsStickers.setVisibility(View.GONE);
            ismBottomsheetGifsBinding.rvGifsStickers.setVisibility(View.VISIBLE);
            gifsAdapter.notifyDataSetChanged();
          }
          ismBottomsheetGifsBinding.pbLoading.setVisibility(View.GONE);
          updateShimmerVisibility(false);
        });
      }
    }
  }

  @Override
  public void onGifsSearchResultsFetchedInCategory(String categoryId,
      ArrayList<GifsModel> gifsModels, boolean notOnScroll) {
    if (categoryId.equals(currentlySelectedGifCategoryId)) {
      if (notOnScroll) {
        gifs.clear();
      }
      gifs.addAll(gifsModels);
      if (activity != null) {

        activity.runOnUiThread(() -> {
          if (gifs.size() == 0) {

            ismBottomsheetGifsBinding.tvNoGifsStickers.setText(
                getString(R.string.ism_no_gifs_in_category));
            ismBottomsheetGifsBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
            ismBottomsheetGifsBinding.rvGifsStickers.setVisibility(View.GONE);
          } else {
            ismBottomsheetGifsBinding.tvNoGifsStickers.setVisibility(View.GONE);
            ismBottomsheetGifsBinding.rvGifsStickers.setVisibility(View.VISIBLE);
            gifsAdapter.notifyDataSetChanged();
          }
          ismBottomsheetGifsBinding.pbLoading.setVisibility(View.GONE);
          updateShimmerVisibility(false);
        });
      }
    }
  }

  @Override
  public void onError(String errorMessage) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        ismBottomsheetGifsBinding.pbLoading.setVisibility(View.GONE);
        updateShimmerVisibility(false);
        if (errorMessage != null) {
          Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getActivity(), getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  private final RecyclerView.OnScrollListener gifsOnScrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);
      if (currentlySelectedGifCategoryId != null) {
        gifsPresenter.fetchGifsOnScroll(currentlySelectedGifCategoryId,
            gridLayoutManager.findFirstVisibleItemPosition(), gridLayoutManager.getChildCount(),
            gridLayoutManager.getItemCount());
      }
    }
  };

  /**
   * Update parameters.
   *
   * @param mediaSelectedToBeShared the media selected to be shared
   */
  public void updateParameters(MediaSelectedToBeShared mediaSelectedToBeShared) {

    this.mediaSelectedToBeShared = mediaSelectedToBeShared;
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetGifsBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetGifsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetGifsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetGifsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetGifsBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
