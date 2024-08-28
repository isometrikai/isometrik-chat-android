package io.isometrik.ui.conversations.gallery;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityGalleryMediaItemsBinding;
import io.isometrik.ui.messages.preview.PreviewMessageUtil;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The activity to fetch list of media items in gallery with paging, search and pull to refresh
 * option.
 */
public class GalleryMediaItemsActivity extends AppCompatActivity
    implements GalleryMediaItemsContract.View {

  private GalleryMediaItemsContract.Presenter galleryMediaItemsPresenter;
  private IsmActivityGalleryMediaItemsBinding ismActivityGalleryMediaItemsBinding;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private ArrayList<GalleryMediaTypeHeaderModel> galleryMediaTypes;

  private GalleryItemsAdapter galleryItemsAdapter;
  private ArrayList<GalleryModel> galleryItems;
  private GridLayoutManager galleryItemsLayoutManager;
  private String customMediaMessageType;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityGalleryMediaItemsBinding =
        IsmActivityGalleryMediaItemsBinding.inflate(getLayoutInflater());
    View view = ismActivityGalleryMediaItemsBinding.getRoot();
    setContentView(view);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    galleryItems = new ArrayList<>();
    galleryItemsLayoutManager = new GridLayoutManager(this, 2);
    ismActivityGalleryMediaItemsBinding.rvAttachments.setLayoutManager(
        galleryItemsLayoutManager);
    galleryItemsAdapter = new GalleryItemsAdapter(this, galleryItems);
    ismActivityGalleryMediaItemsBinding.rvAttachments.setAdapter(galleryItemsAdapter);
    ismActivityGalleryMediaItemsBinding.rvAttachments.addOnScrollListener(
        attachmentsOnScrollListener);

    galleryMediaTypes = new ArrayList<>();
    ismActivityGalleryMediaItemsBinding.rvMediaTypeHeader.setLayoutManager(
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

    GalleryMediaItemsSettingsUtil galleryMediaItemsSettingsUtil =
        (GalleryMediaItemsSettingsUtil) getIntent().getSerializableExtra(
            "galleryMediaItemsSettingsUtil");
    galleryMediaTypes = new ArrayList<>();
    galleryMediaTypes.addAll(galleryMediaItemsSettingsUtil.getGalleryMediaTypesHeader());
    GalleryMediaTypeHeaderAdapter galleryMediaTypesAdapter =
        new GalleryMediaTypeHeaderAdapter(this, galleryMediaTypes);
    ismActivityGalleryMediaItemsBinding.rvMediaTypeHeader.setAdapter(
        galleryMediaTypesAdapter);

    ismActivityGalleryMediaItemsBinding.rvMediaTypeHeader.addOnItemTouchListener(
        new RecyclerItemClickListener(this,
            ismActivityGalleryMediaItemsBinding.rvMediaTypeHeader,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {
                  if (ismActivityGalleryMediaItemsBinding.rlEmptyGallery.getVisibility()
                      == View.VISIBLE) {
                    ismActivityGalleryMediaItemsBinding.rlEmptyGallery.setVisibility(View.GONE);
                  } else {
                    galleryItems.clear();
                    galleryItemsAdapter.notifyDataSetChanged();
                  }

                  updateShimmerVisibility(true);
                  customMediaMessageType =
                      galleryMediaTypes.get(position).getCustomMediaMessageType();
                  fetchGalleryMediaItems(customMediaMessageType, false, null, false);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    galleryMediaItemsPresenter = new GalleryMediaItemsPresenter(this);

    Bundle extras = getIntent().getExtras();
    galleryMediaItemsPresenter.initialize(extras.getString("conversationId"));
    customMediaMessageType = galleryMediaItemsSettingsUtil.getGalleryMediaTypesHeader()
        .get(0)
        .getCustomMediaMessageType();
    fetchGalleryMediaItems(customMediaMessageType, false, null, false);

    ismActivityGalleryMediaItemsBinding.rvAttachments.addOnItemTouchListener(
        new RecyclerItemClickListener(this, ismActivityGalleryMediaItemsBinding.rvAttachments,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {

                  GalleryModel galleryModel = galleryItems.get(position);

                  PreviewMessageUtil.previewMessage(GalleryMediaItemsActivity.this, galleryModel);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismActivityGalleryMediaItemsBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchGalleryMediaItems(customMediaMessageType, true, s.toString(), false);
        } else {

          fetchGalleryMediaItems(customMediaMessageType, false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    ismActivityGalleryMediaItemsBinding.ibBack.setOnClickListener(v -> onBackPressed());
    ismActivityGalleryMediaItemsBinding.refresh.setOnRefreshListener(
        () -> fetchGalleryMediaItems(customMediaMessageType, false, null, true));
  }

  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();
    if (ismActivityGalleryMediaItemsBinding.refresh.isRefreshing()) {
      ismActivityGalleryMediaItemsBinding.refresh.setRefreshing(false);
    }

    updateShimmerVisibility(false);
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private final RecyclerView.OnScrollListener attachmentsOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          galleryMediaItemsPresenter.fetchGalleryMediaItemsOnScroll(
              galleryItemsLayoutManager.findFirstVisibleItemPosition(),
              galleryItemsLayoutManager.getChildCount(), galleryItemsLayoutManager.getItemCount(),
              customMediaMessageType);
        }
      };

  @Override
  public void onGalleryMediaItemsFetchedSuccessfully(ArrayList<GalleryModel> galleryItemModels,
      boolean resultsOnScroll) {
    if (!resultsOnScroll) {
      galleryItems.clear();
    }
    galleryItems.addAll(galleryItemModels);

    runOnUiThread(() -> {
      if (!resultsOnScroll) {
        if (galleryItems.size() > 0) {
          ismActivityGalleryMediaItemsBinding.rlEmptyGallery.setVisibility(View.GONE);
          ismActivityGalleryMediaItemsBinding.rvAttachments.setVisibility(View.VISIBLE);
        } else {
          ismActivityGalleryMediaItemsBinding.rlEmptyGallery.setVisibility(View.VISIBLE);
          ismActivityGalleryMediaItemsBinding.tvEmptyDescription.setText(
              getString(R.string.ism_no_media));
          ismActivityGalleryMediaItemsBinding.rvAttachments.setVisibility(View.GONE);
        }
      }
      galleryItemsAdapter.notifyDataSetChanged();

      hideProgressDialog();
      if (ismActivityGalleryMediaItemsBinding.refresh.isRefreshing()) {
        ismActivityGalleryMediaItemsBinding.refresh.setRefreshing(false);
      }
      updateShimmerVisibility(false);
    });
  }

  private void fetchGalleryMediaItems(String customMediaMessageType, boolean isSearchRequest,
      String searchTag, boolean showProgressDialog) {
    if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_attachments));

    galleryMediaItemsPresenter.fetchGalleryMediaItems(customMediaMessageType, 0, false,
        isSearchRequest, searchTag);
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismActivityGalleryMediaItemsBinding.shimmerFrameLayout.startShimmer();
      ismActivityGalleryMediaItemsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismActivityGalleryMediaItemsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismActivityGalleryMediaItemsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismActivityGalleryMediaItemsBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}