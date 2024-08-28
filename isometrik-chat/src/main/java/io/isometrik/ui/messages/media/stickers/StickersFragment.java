package io.isometrik.ui.messages.media.stickers;

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
 * The fragment to fetch list of stickers in a category, with paging and
 * search.
 */
public class StickersFragment extends BottomSheetDialogFragment implements StickersContract.View {
  /**
   * The constant TAG.
   */
  public static final String TAG = "StickersFragment";

  /**
   * Instantiates a stickers fragment.
   */
  public StickersFragment() {
    // Required empty public constructor
  }

  private IsmBottomsheetGifsStickersBinding ismBottomsheetStickersBinding;
  private StickersContract.Presenter stickersPresenter;
  private Activity activity;
  private ArrayList<StickersCategoryModel> stickersCategories;
  private ArrayList<StickersModel> stickers;
  private StickersAdapter stickersAdapter;
  private String currentlySelectedStickerCategoryId;
  private GridLayoutManager gridLayoutManager;
  private MediaSelectedToBeShared mediaSelectedToBeShared;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    ismBottomsheetStickersBinding =
        IsmBottomsheetGifsStickersBinding.inflate(inflater, container, false);
    updateShimmerVisibility(true);
    stickersCategories = new ArrayList<>();
    ismBottomsheetStickersBinding.rvGifsStickersCategories.setLayoutManager(
        new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

    stickers = new ArrayList<>();
    stickersAdapter = new StickersAdapter(stickers, activity);
    gridLayoutManager = new GridLayoutManager(activity, 2);
    ismBottomsheetStickersBinding.rvGifsStickers.setLayoutManager(gridLayoutManager);
    ismBottomsheetStickersBinding.rvGifsStickers.setNestedScrollingEnabled(true);
    ismBottomsheetStickersBinding.rvGifsStickers.setAdapter(stickersAdapter);

    stickersPresenter.fetchStickersCategories();

    ismBottomsheetStickersBinding.rvGifsStickers.addOnScrollListener(stickersOnScrollListener);
    ismBottomsheetStickersBinding.rvGifsStickers.addOnItemTouchListener(
        new RecyclerItemClickListener(activity, ismBottomsheetStickersBinding.rvGifsStickers,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {
                if (position >= 0) {
                  StickersModel stickersModel = stickers.get(position);
                  mediaSelectedToBeShared.stickerShareRequested(stickersModel.getStickerName(),
                      stickersModel.getStickerImageUrl());
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }

        ));

    ismBottomsheetStickersBinding.etSearch.addTextChangedListener(new TextWatcher() {

      public void afterTextChanged(Editable s) {

      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (ismBottomsheetStickersBinding.rlSearch.getVisibility() == View.VISIBLE) {
          if (s.length() > 0 && !(s.toString().trim().isEmpty())) {

            stickersPresenter.searchStickersInACategory(currentlySelectedStickerCategoryId,
                s.toString(), GIFS_STICKERS_PAGE_SIZE, 0);
          } else {
            stickersPresenter.fetchStickersInACategory(currentlySelectedStickerCategoryId,
                GIFS_STICKERS_PAGE_SIZE, 0);
          }
        }
      }
    });

    ismBottomsheetStickersBinding.btSearch.setOnClickListener(v -> {
      String text = ismBottomsheetStickersBinding.etSearch.getText().toString();
      if (text.isEmpty()) {
        Toast.makeText(activity, R.string.ism_enter_stickers_search_text, Toast.LENGTH_SHORT).show();
      } else {
        stickersPresenter.searchStickersInACategory(currentlySelectedStickerCategoryId, text,
            GIFS_STICKERS_PAGE_SIZE, 0);
      }
    });

    //To allow scroll on sticker's recyclerview
    ismBottomsheetStickersBinding.rvGifsStickers.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    ismBottomsheetStickersBinding.rvGifsStickersCategories.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    return ismBottomsheetStickersBinding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ismBottomsheetStickersBinding = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    stickersPresenter = new StickersPresenter();
    stickersPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    stickersPresenter.detachView();
    activity = null;
  }

  @Override
  public void onStickersCategoriesFetchedSuccessfully(
      ArrayList<StickersCategoryModel> stickersCategoryModels) {

    if (stickersCategoryModels.size() > 0) {
      stickersCategories.addAll(stickersCategoryModels);
      StickersCategoriesAdapter stickersCategoriesAdapter =
          new StickersCategoriesAdapter(stickersCategories, activity);

      if (stickersCategories.size() > 0) {
        currentlySelectedStickerCategoryId = stickersCategories.get(0).getStickerCategoryName();
        stickers.addAll(stickersCategories.get(0).getStickers());
        stickersAdapter.notifyDataSetChanged();
        updateShimmerVisibility(false);
        if (currentlySelectedStickerCategoryId.equals(
            StickerCategoriesRepository.StickerCategoryNameEnum.Classic.getValue())) {
          ismBottomsheetStickersBinding.rlSearch.setVisibility(View.GONE);
        }
      }
      ismBottomsheetStickersBinding.rvGifsStickersCategories.setAdapter(stickersCategoriesAdapter);

      ismBottomsheetStickersBinding.rvGifsStickersCategories.addOnItemTouchListener(
          new RecyclerItemClickListener(activity,
              ismBottomsheetStickersBinding.rvGifsStickersCategories,
              new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int position) {
                  if (position >= 0) {

                    StickersCategoryModel stickerCategoryModel = stickersCategories.get(position);
                    currentlySelectedStickerCategoryId =
                        stickerCategoryModel.getStickerCategoryName();
                    if (stickerCategoryModel.getStickerCategoryName()
                        .equals(StickerCategoriesRepository.StickerCategoryNameEnum.Classic.getValue())) {
                      ismBottomsheetStickersBinding.rlSearch.setVisibility(View.GONE);
                      ArrayList<StickersModel> stickersData =
                          stickersCategories.get(position).getStickers();
                      if (stickersData.size() > 0) {

                        ismBottomsheetStickersBinding.rvGifsStickers.setVisibility(View.VISIBLE);
                        ismBottomsheetStickersBinding.tvNoGifsStickers.setVisibility(View.GONE);

                        stickers.clear();
                        stickers.addAll(stickersData);
                        updateShimmerVisibility(false);
                        stickersAdapter.notifyDataSetChanged();
                      } else {

                        ismBottomsheetStickersBinding.tvNoGifsStickers.setText(
                            getString(R.string.ism_no_stickers_in_category));
                        ismBottomsheetStickersBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
                        ismBottomsheetStickersBinding.rvGifsStickers.setVisibility(View.GONE);
                      }
                    } else {
                      ismBottomsheetStickersBinding.rlSearch.setVisibility(View.VISIBLE);
                      stickers.clear();
                      stickersAdapter.notifyDataSetChanged();
                      ismBottomsheetStickersBinding.etSearch.setText("");
                      ismBottomsheetStickersBinding.pbLoading.setVisibility(View.VISIBLE);
                      updateShimmerVisibility(true);
                      //Not needed anymore, as triggered due to update of edittext to empty text.
                      //stickersPresenter.fetchStickersInACategory(currentlySelectedStickerCategoryId,
                      //    GIFS_STICKERS_PAGE_SIZE, 0);
                    }
                  }

                  for (int i = 0; i < stickersCategories.size(); i++) {
                    StickersCategoryModel stickersCategoryModel = stickersCategories.get(i);
                    stickersCategoryModel.setSelected((i == position));
                  }
                  stickersCategoriesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onItemLongClick(View view, int position) {
                }
              }

          ));

    } else {
      ismBottomsheetStickersBinding.tvNoGifsStickers.setText(
          getString(R.string.ism_no_gifs_stickers_category, getString(R.string.ism_no_stickers)));
      ismBottomsheetStickersBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onStickersFetchedInCategory(String categoryId,
      ArrayList<StickersModel> stickersModels, boolean notOnScroll) {
    if (categoryId.equals(currentlySelectedStickerCategoryId)) {

      if (activity != null) {

        activity.runOnUiThread(() -> {
          if (notOnScroll) {
            stickers.clear();
          }
          stickers.addAll(stickersModels);
          if (stickers.size() == 0) {

            ismBottomsheetStickersBinding.tvNoGifsStickers.setText(
                getString(R.string.ism_no_stickers_in_category));
            ismBottomsheetStickersBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
            ismBottomsheetStickersBinding.rvGifsStickers.setVisibility(View.GONE);
          } else {
            ismBottomsheetStickersBinding.tvNoGifsStickers.setVisibility(View.GONE);
            ismBottomsheetStickersBinding.rvGifsStickers.setVisibility(View.VISIBLE);
            stickersAdapter.notifyDataSetChanged();
          }
          ismBottomsheetStickersBinding.pbLoading.setVisibility(View.GONE);
          updateShimmerVisibility(false);
        });
      }
    }
  }

  @Override
  public void onStickersSearchResultsFetchedInCategory(String categoryId,
      ArrayList<StickersModel> stickersModels, boolean notOnScroll) {
    if (categoryId.equals(currentlySelectedStickerCategoryId)) {
      if (notOnScroll) {
        stickers.clear();
      }
      stickers.addAll(stickersModels);

      if (activity != null) {

        activity.runOnUiThread(() -> {
          if (stickers.size() == 0) {

            ismBottomsheetStickersBinding.tvNoGifsStickers.setText(
                getString(R.string.ism_no_stickers_in_category));
            ismBottomsheetStickersBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
            ismBottomsheetStickersBinding.rvGifsStickers.setVisibility(View.GONE);
          } else {
            ismBottomsheetStickersBinding.tvNoGifsStickers.setVisibility(View.GONE);
            ismBottomsheetStickersBinding.rvGifsStickers.setVisibility(View.VISIBLE);
            stickersAdapter.notifyDataSetChanged();
          }
          ismBottomsheetStickersBinding.pbLoading.setVisibility(View.GONE);
          updateShimmerVisibility(false);
        });
      }
    }
  }

  @Override
  public void onError(String errorMessage) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        ismBottomsheetStickersBinding.pbLoading.setVisibility(View.GONE);
        updateShimmerVisibility(false);
        if (errorMessage != null) {
          Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getActivity(), getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  private final RecyclerView.OnScrollListener stickersOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          if (currentlySelectedStickerCategoryId != null) {
            stickersPresenter.fetchStickersOnScroll(currentlySelectedStickerCategoryId,
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
      ismBottomsheetStickersBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetStickersBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetStickersBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetStickersBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetStickersBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
