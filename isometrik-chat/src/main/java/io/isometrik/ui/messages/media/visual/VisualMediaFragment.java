package io.isometrik.ui.messages.media.visual;

import static io.isometrik.ui.messages.media.gifstickerutils.Constants.GIFS_STICKERS_PAGE_SIZE;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBottomsheetVisualMediaBinding;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import io.isometrik.ui.messages.media.MediaSelectedToBeShared;
import io.isometrik.ui.messages.media.gifs.GifCategoriesRepository;
import io.isometrik.ui.messages.media.stickers.StickerCategoriesRepository;
import io.isometrik.ui.messages.media.stickers.StickersAdapter;
import io.isometrik.ui.messages.media.stickers.StickersCategoriesAdapter;
import io.isometrik.ui.messages.media.stickers.StickersCategoryModel;
import io.isometrik.ui.messages.media.stickers.StickersModel;

/**
 * The fragment to fetch list of stickers in a category, with paging and
 * search.
 */
public class VisualMediaFragment extends BottomSheetDialogFragment implements VisualContract.View {
  /**
   * The constant TAG.
   */
  public static final String TAG = "VisualFragment";

  /**
   * Instantiates a stickers fragment.
   */
  public VisualMediaFragment() {
    // Required empty public constructor
  }

  private IsmBottomsheetVisualMediaBinding ismBottomsheetVisualBinding;
  private VisualContract.Presenter visualPresenter;
  private Activity activity;
  private ArrayList<StickersCategoryModel> stickersCategories;
  private ArrayList<StickersModel> stickers;
  private StickersAdapter stickersAdapter;
  private String currentlySelectedStickerCategoryId = StickerCategoriesRepository.StickerCategoryNameEnum.Featured.getValue();
  private String currentlySelectedGifCategoryId = GifCategoriesRepository.GifCategoryNameEnum.Featured.getValue();
  private GridLayoutManager gridLayoutManager;
  private MediaSelectedToBeShared mediaSelectedToBeShared;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    ismBottomsheetVisualBinding =
            IsmBottomsheetVisualMediaBinding.inflate(inflater, container, false);
    updateShimmerVisibility(true);
    stickersCategories = new ArrayList<>();


    stickers = new ArrayList<>();
    stickersAdapter = new StickersAdapter(stickers, activity);
    gridLayoutManager = new GridLayoutManager(activity, 3);
    ismBottomsheetVisualBinding.rvGifsStickers.setLayoutManager(gridLayoutManager);
    ismBottomsheetVisualBinding.rvGifsStickers.setNestedScrollingEnabled(true);
    ismBottomsheetVisualBinding.rvGifsStickers.setAdapter(stickersAdapter);

//    ismBottomsheetVisualBinding.etSearch.setText("");
    ismBottomsheetVisualBinding.pbLoading.setVisibility(View.VISIBLE);

    ismBottomsheetVisualBinding.rvGifsStickers.addOnScrollListener(stickersOnScrollListener);
    ismBottomsheetVisualBinding.rvGifsStickers.addOnItemTouchListener(
        new RecyclerItemClickListener(activity, ismBottomsheetVisualBinding.rvGifsStickers,
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

    ismBottomsheetVisualBinding.etSearch.addTextChangedListener(new TextWatcher() {

      public void afterTextChanged(Editable s) {

      }

      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (ismBottomsheetVisualBinding.rlSearch.getVisibility() == View.VISIBLE) {
          if (s.length() > 0 && !(s.toString().trim().isEmpty())) {

            visualPresenter.searchStickersInACategory(currentlySelectedStickerCategoryId,
                s.toString(), GIFS_STICKERS_PAGE_SIZE, 0);
          } else {
            visualPresenter.fetchStickersInACategory(currentlySelectedStickerCategoryId,
                GIFS_STICKERS_PAGE_SIZE, 0);
          }
        }
      }
    });

    ismBottomsheetVisualBinding.btSearch.setOnClickListener(v -> {
      String text = ismBottomsheetVisualBinding.etSearch.getText().toString();
      if (text.isEmpty()) {
        Toast.makeText(activity, R.string.ism_enter_stickers_search_text, Toast.LENGTH_SHORT).show();
      } else {
        visualPresenter.searchStickersInACategory(currentlySelectedStickerCategoryId, text,
            GIFS_STICKERS_PAGE_SIZE, 0);
      }
    });

    //To allow scroll on sticker's recyclerview
    ismBottomsheetVisualBinding.rvGifsStickers.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });

    return ismBottomsheetVisualBinding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ismBottomsheetVisualBinding = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    visualPresenter = new VisualPresenter();
    visualPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    visualPresenter.detachView();
    activity = null;
  }

  @Override
  public void onStickersCategoriesFetchedSuccessfully(
      ArrayList<StickersCategoryModel> stickersCategoryModels) {

    if (stickersCategoryModels.size() > 0) {
      stickersCategories.addAll(stickersCategoryModels);

      if (stickersCategories.size() > 0) {
        currentlySelectedStickerCategoryId = stickersCategories.get(0).getStickerCategoryName();
        stickers.addAll(stickersCategories.get(0).getStickers());
        stickersAdapter.notifyDataSetChanged();
        updateShimmerVisibility(false);
        if (currentlySelectedStickerCategoryId.equals(
            StickerCategoriesRepository.StickerCategoryNameEnum.Classic.getValue())) {
          ismBottomsheetVisualBinding.rlSearch.setVisibility(View.GONE);
        }
      }
    } else {
      ismBottomsheetVisualBinding.tvNoGifsStickers.setText(
          getString(R.string.ism_no_gifs_stickers_category, getString(R.string.ism_no_stickers)));
      ismBottomsheetVisualBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
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

            ismBottomsheetVisualBinding.tvNoGifsStickers.setText(
                getString(R.string.ism_no_stickers_in_category));
            ismBottomsheetVisualBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
            ismBottomsheetVisualBinding.rvGifsStickers.setVisibility(View.GONE);
          } else {
            ismBottomsheetVisualBinding.tvNoGifsStickers.setVisibility(View.GONE);
            ismBottomsheetVisualBinding.rvGifsStickers.setVisibility(View.VISIBLE);
            stickersAdapter.notifyDataSetChanged();
          }
          ismBottomsheetVisualBinding.pbLoading.setVisibility(View.GONE);
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

            ismBottomsheetVisualBinding.tvNoGifsStickers.setText(
                getString(R.string.ism_no_stickers_in_category));
            ismBottomsheetVisualBinding.tvNoGifsStickers.setVisibility(View.VISIBLE);
            ismBottomsheetVisualBinding.rvGifsStickers.setVisibility(View.GONE);
          } else {
            ismBottomsheetVisualBinding.tvNoGifsStickers.setVisibility(View.GONE);
            ismBottomsheetVisualBinding.rvGifsStickers.setVisibility(View.VISIBLE);
            stickersAdapter.notifyDataSetChanged();
          }
          ismBottomsheetVisualBinding.pbLoading.setVisibility(View.GONE);
          updateShimmerVisibility(false);
        });
      }
    }
  }

  @Override
  public void onError(String errorMessage) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        ismBottomsheetVisualBinding.pbLoading.setVisibility(View.GONE);
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
            visualPresenter.fetchStickersOnScroll(currentlySelectedStickerCategoryId,
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
      ismBottomsheetVisualBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetVisualBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetVisualBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetVisualBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetVisualBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
