package io.isometrik.ui.users.blockedornonblocked.nonblocked;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmFragmentBlockedOrNonBlockedUsersBinding;
import io.isometrik.ui.users.blockedornonblocked.BlockedOrNonBlockedUsersModel;
import io.isometrik.chat.utils.AlertProgress;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The fragment to fetch list of users neither blocked by logged in user nor logged
 * in user blocked by them with paging, search and pull to refresh
 * option.Code to block user.
 */
public class NonBlockedUsersFragment extends Fragment implements NonBlockedUsersContract.View {

  private NonBlockedUsersContract.Presenter nonBlockedUsersContractPresenter;
  private IsmFragmentBlockedOrNonBlockedUsersBinding ismFragmentNonBlockedUsersBinding;

  private final ArrayList<BlockedOrNonBlockedUsersModel> nonBlockedUsers = new ArrayList<>();

  private NonBlockedUsersAdapter nonBlockedUsersAdapter;

  private LinearLayoutManager nonBlockedUsersLayoutManager;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    nonBlockedUsersContractPresenter = new NonBlockedUsersPresenter();

    nonBlockedUsersContractPresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismFragmentNonBlockedUsersBinding =
        IsmFragmentBlockedOrNonBlockedUsersBinding.inflate(inflater, container, false);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    ismFragmentNonBlockedUsersBinding.etSearch.setHint(
        getString(R.string.ism_search_non_blocked_users_hint));
    ismFragmentNonBlockedUsersBinding.tvNoUsers.setText(
        getString(R.string.ism_no_non_blocked_users_found));

    nonBlockedUsersLayoutManager = new LinearLayoutManager(getActivity());
    ismFragmentNonBlockedUsersBinding.rvUsers.setLayoutManager(nonBlockedUsersLayoutManager);
    nonBlockedUsersAdapter = new NonBlockedUsersAdapter(getActivity(), nonBlockedUsers, this);
    ismFragmentNonBlockedUsersBinding.rvUsers.addOnScrollListener(nonBlockedUsersScrollListener);
    ismFragmentNonBlockedUsersBinding.rvUsers.setAdapter(nonBlockedUsersAdapter);

    fetchNonBlockedUsers(false, null, false);
    ismFragmentNonBlockedUsersBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchNonBlockedUsers(true, s.toString(), false);
        } else {

          fetchNonBlockedUsers(false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    ismFragmentNonBlockedUsersBinding.refresh.setOnRefreshListener(() ->

        fetchNonBlockedUsers(false, null, true));

    return ismFragmentNonBlockedUsersBinding.getRoot();
  }

  @Override
  public void onDestroy() {

    super.onDestroy();
    nonBlockedUsersContractPresenter.detachView();
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    ismFragmentNonBlockedUsersBinding = null;
  }

  private final RecyclerView.OnScrollListener nonBlockedUsersScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          nonBlockedUsersContractPresenter.fetchNonBlockedUsersOnScroll(
              nonBlockedUsersLayoutManager.findFirstVisibleItemPosition(),
              nonBlockedUsersLayoutManager.getChildCount(),
              nonBlockedUsersLayoutManager.getItemCount());
        }
      };

  /**
   * Fetch non blocked users.
   *
   * @param isSearchRequest the is search request
   * @param searchTag the search tag
   * @param showProgressDialog the show progress dialog
   */
  public void fetchNonBlockedUsers(boolean isSearchRequest, String searchTag,
      boolean showProgressDialog) {
    if (showProgressDialog) {
      showProgressDialog(getString(R.string.ism_fetching_non_blocked_users));
    }
    try {
      nonBlockedUsersContractPresenter.fetchNonBlockedUsers(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onNonBlockedUsersFetchedSuccessfully(
      ArrayList<BlockedOrNonBlockedUsersModel> nonBlockedUsersModels, boolean refreshRequest) {
    if (refreshRequest) {
      nonBlockedUsers.clear();
    }

    nonBlockedUsers.addAll(nonBlockedUsersModels);

    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (refreshRequest) {
          if (nonBlockedUsers.size() > 0) {
            ismFragmentNonBlockedUsersBinding.tvNoUsers.setVisibility(View.GONE);
            ismFragmentNonBlockedUsersBinding.rvUsers.setVisibility(View.VISIBLE);
          } else {
            ismFragmentNonBlockedUsersBinding.tvNoUsers.setVisibility(View.VISIBLE);
            ismFragmentNonBlockedUsersBinding.rvUsers.setVisibility(View.GONE);
          }
        }
        nonBlockedUsersAdapter.notifyDataSetChanged();
        if (ismFragmentNonBlockedUsersBinding.refresh.isRefreshing()) {
          ismFragmentNonBlockedUsersBinding.refresh.setRefreshing(false);
        }
        hideProgressDialog();
        updateShimmerVisibility(false);
      });
    }
  }

  @Override
  public void onError(String errorMessage) {
    if (ismFragmentNonBlockedUsersBinding.refresh.isRefreshing()) {
      ismFragmentNonBlockedUsersBinding.refresh.setRefreshing(false);
    }
    hideProgressDialog();
    updateShimmerVisibility(false);
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (errorMessage != null) {
          Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getActivity(), getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  private void showProgressDialog(String message) {

    if (getActivity() != null) {
      alertDialog = alertProgress.getProgressDialog(getActivity(), message);
      if (!getActivity().isFinishing()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  /**
   * Block user.
   *
   * @param opponentId the opponent id
   * @param opponentName the opponent name
   * @param position the position
   */
  public void blockUser(String opponentId, String opponentName, int position) {

    if (getActivity() != null) {
      new AlertDialog.Builder(getActivity()).setTitle(
          getString(R.string.ism_block_user, opponentName))
          .setMessage(getString(R.string.ism_block_user_alert_message))
          .setCancelable(true)
          .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

            dialog.cancel();

            showProgressDialog(getString(R.string.ism_blocking_user, opponentName));
            nonBlockedUsersContractPresenter.blockUser(opponentId, position);
          })
          .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
          .create()
          .show();
    }
  }

  @Override
  public void onUserBlockedSuccessfully(String opponentId, int position) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (position < nonBlockedUsers.size() && nonBlockedUsers.get(position)
            .getUserId()
            .equals(opponentId)) {
          nonBlockedUsers.remove(position);
          nonBlockedUsersAdapter.notifyItemRemoved(position);
        } else {
          int size = nonBlockedUsers.size();
          for (int i = 0; i < size; i++) {
            if (nonBlockedUsers.get(i).getUserId().equals(opponentId)) {
              nonBlockedUsers.remove(i);
              nonBlockedUsersAdapter.notifyItemRemoved(i);
              break;
            }
          }
        }
        if (nonBlockedUsers.size() == 0) {
          ismFragmentNonBlockedUsersBinding.tvNoUsers.setVisibility(View.VISIBLE);
          ismFragmentNonBlockedUsersBinding.rvUsers.setVisibility(View.GONE);
        }
        hideProgressDialog();
      });
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismFragmentNonBlockedUsersBinding.shimmerFrameLayout.startShimmer();
      ismFragmentNonBlockedUsersBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismFragmentNonBlockedUsersBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismFragmentNonBlockedUsersBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismFragmentNonBlockedUsersBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}