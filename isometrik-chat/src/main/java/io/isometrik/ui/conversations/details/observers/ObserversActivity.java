package io.isometrik.ui.conversations.details.observers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityObserversBinding;
import io.isometrik.chat.utils.AlertProgress;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The activity to fetch list of observers in an open conversation with paging, search and pull to refresh option.
 */
public class ObserversActivity extends AppCompatActivity implements ObserversContract.View {

  private ObserversContract.Presenter observersPresenter;

  private AlertProgress alertProgress;

  private AlertDialog alertDialog;

  private final ArrayList<ObserversModel> observers = new ArrayList<>();
  private ObserversAdapter observersAdapter;

  private LinearLayoutManager layoutManager;
  private IsmActivityObserversBinding ismActivityObserversBinding;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityObserversBinding = IsmActivityObserversBinding.inflate(getLayoutInflater());
    View view = ismActivityObserversBinding.getRoot();
    setContentView(view);

    observersPresenter = new ObserversPresenter(this, getIntent().getExtras().getString("conversationId"));
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    layoutManager = new LinearLayoutManager(this);
    ismActivityObserversBinding.rvObservers.setLayoutManager(layoutManager);
    observersAdapter = new ObserversAdapter(this, observers);
    ismActivityObserversBinding.rvObservers.addOnScrollListener(recyclerViewOnScrollListener);
    ismActivityObserversBinding.rvObservers.setAdapter(observersAdapter);

    fetchLatestObservers(false, null, false);

    ismActivityObserversBinding.refresh.setOnRefreshListener(() -> fetchLatestObservers(false, null, true));

    ismActivityObserversBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchLatestObservers(true, s.toString(), false);
        } else {

          fetchLatestObservers(false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    ismActivityObserversBinding.ibBack.setOnClickListener(v -> onBackPressed());
  }

  private void fetchLatestObservers(boolean isSearchRequest, String searchTag, boolean showProgressDialog) {
    if (showProgressDialog) {

      showProgressDialog(getString(R.string.ism_fetching_observers));
    }
    try {
      observersPresenter.fetchObservers(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The Recycler view on scroll listener.
   */
  private final RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      observersPresenter.fetchObserversOnScroll(layoutManager.findFirstVisibleItemPosition(),
          layoutManager.getChildCount(), layoutManager.getItemCount());
    }
  };

  @Override
  public void onObserversFetchedSuccessfully(ArrayList<ObserversModel> observersModels, boolean refreshRequest) {
    if (refreshRequest) {
      observers.clear();
    }
    observers.addAll(observersModels);
    runOnUiThread(() -> {
      if (refreshRequest) {
        if (observers.size() > 0) {
          ismActivityObserversBinding.tvNoObservers.setVisibility(View.GONE);
          ismActivityObserversBinding.rvObservers.setVisibility(View.VISIBLE);
        } else {
          ismActivityObserversBinding.tvNoObservers.setVisibility(View.VISIBLE);
          ismActivityObserversBinding.rvObservers.setVisibility(View.GONE);
        }
      }
      observersAdapter.notifyDataSetChanged();

      hideProgressDialog();
      if (ismActivityObserversBinding.refresh.isRefreshing()) {
        ismActivityObserversBinding.refresh.setRefreshing(false);
      }
      updateShimmerVisibility(false);
    });
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  @Override
  public void onError(String errorMessage) {
    if (ismActivityObserversBinding.refresh.isRefreshing()) {
      ismActivityObserversBinding.refresh.setRefreshing(false);
    }
    hideProgressDialog();
    updateShimmerVisibility(false);
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(ObserversActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(ObserversActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismActivityObserversBinding.shimmerFrameLayout.startShimmer();
      ismActivityObserversBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismActivityObserversBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismActivityObserversBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismActivityObserversBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
