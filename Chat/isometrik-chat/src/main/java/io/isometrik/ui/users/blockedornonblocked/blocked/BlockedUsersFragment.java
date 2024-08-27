package io.isometrik.ui.users.blockedornonblocked.blocked;

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
 * The fragment to fetch list of users blocked by logged in user with paging, search and pull to refresh
 * option.Code to unblock user.
 */
public class BlockedUsersFragment extends Fragment implements BlockedUsersContract.View {

  private BlockedUsersContract.Presenter blockedUsersContractPresenter;
  private IsmFragmentBlockedOrNonBlockedUsersBinding ismFragmentBlockedUsersBinding;

  private final ArrayList<BlockedOrNonBlockedUsersModel> blockedUsers = new ArrayList<>();

  private BlockedUsersAdapter blockedUsersAdapter;

  private LinearLayoutManager blockedUsersLayoutManager;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    blockedUsersContractPresenter = new BlockedUsersPresenter();

    blockedUsersContractPresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismFragmentBlockedUsersBinding =
        IsmFragmentBlockedOrNonBlockedUsersBinding.inflate(inflater, container, false);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    blockedUsersLayoutManager = new LinearLayoutManager(getActivity());
    ismFragmentBlockedUsersBinding.rvUsers.setLayoutManager(blockedUsersLayoutManager);
    blockedUsersAdapter = new BlockedUsersAdapter(getActivity(), blockedUsers, this);
    ismFragmentBlockedUsersBinding.rvUsers.addOnScrollListener(blockedUsersScrollListener);
    ismFragmentBlockedUsersBinding.rvUsers.setAdapter(blockedUsersAdapter);

    fetchBlockedUsers(false, null, false);
    ismFragmentBlockedUsersBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchBlockedUsers(true, s.toString(), false);
        } else {

          fetchBlockedUsers(false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
    ismFragmentBlockedUsersBinding.refresh.setOnRefreshListener(() ->

        fetchBlockedUsers(false, null, true));

    return ismFragmentBlockedUsersBinding.getRoot();
  }

  @Override
  public void onDestroy() {

    super.onDestroy();
    blockedUsersContractPresenter.detachView();
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    ismFragmentBlockedUsersBinding = null;
  }

  private final RecyclerView.OnScrollListener blockedUsersScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          blockedUsersContractPresenter.fetchBlockedUsersOnScroll(
              blockedUsersLayoutManager.findFirstVisibleItemPosition(),
              blockedUsersLayoutManager.getChildCount(), blockedUsersLayoutManager.getItemCount());
        }
      };

  /**
   * Fetch blocked users.
   *
   * @param isSearchRequest the is search request
   * @param searchTag the search tag
   * @param showProgressDialog the show progress dialog
   */
  public void fetchBlockedUsers(boolean isSearchRequest, String searchTag,
      boolean showProgressDialog) {
    if (showProgressDialog) {
      showProgressDialog(getString(R.string.ism_fetching_blocked_users));
    }

    try {
      blockedUsersContractPresenter.fetchBlockedUsers(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onBlockedUsersFetchedSuccessfully(
      ArrayList<BlockedOrNonBlockedUsersModel> blockedUsersModels, boolean refreshRequest) {
    if (refreshRequest) {
      blockedUsers.clear();
    }

    blockedUsers.addAll(blockedUsersModels);

    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (refreshRequest) {
          if (blockedUsers.size() > 0) {
            ismFragmentBlockedUsersBinding.tvNoUsers.setVisibility(View.GONE);
            ismFragmentBlockedUsersBinding.rvUsers.setVisibility(View.VISIBLE);
          } else {
            ismFragmentBlockedUsersBinding.tvNoUsers.setVisibility(View.VISIBLE);
            ismFragmentBlockedUsersBinding.rvUsers.setVisibility(View.GONE);
          }
        }
        blockedUsersAdapter.notifyDataSetChanged();
        if (ismFragmentBlockedUsersBinding.refresh.isRefreshing()) {
          ismFragmentBlockedUsersBinding.refresh.setRefreshing(false);
        }
        hideProgressDialog();
        updateShimmerVisibility(false);
      });
    }
  }

  @Override
  public void onError(String errorMessage) {
    if (ismFragmentBlockedUsersBinding.refresh.isRefreshing()) {
      ismFragmentBlockedUsersBinding.refresh.setRefreshing(false);
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
   * Unblock user.
   *
   * @param opponentId the opponent id
   * @param opponentName the opponent name
   * @param position the position
   */
  public void unblockUser(String opponentId, String opponentName, int position) {

    if (getActivity() != null) {
      new AlertDialog.Builder(getActivity()).setTitle(
          getString(R.string.ism_unblock_user, opponentName))
          .setMessage(getString(R.string.ism_unblock_user_alert_message))
          .setCancelable(true)
          .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

            dialog.cancel();

            showProgressDialog(getString(R.string.ism_unblocking_user, opponentName));
            blockedUsersContractPresenter.unblockUser(opponentId, position);
          })
          .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
          .create()
          .show();
    }
  }

  @Override
  public void onUserUnblockedSuccessfully(String opponentId, int position) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (position < blockedUsers.size() && blockedUsers.get(position)
            .getUserId()
            .equals(opponentId)) {
          blockedUsers.remove(position);
          blockedUsersAdapter.notifyItemRemoved(position);
        } else {
          int size = blockedUsers.size();
          for (int i = 0; i < size; i++) {
            if (blockedUsers.get(i).getUserId().equals(opponentId)) {
              blockedUsers.remove(i);
              blockedUsersAdapter.notifyItemRemoved(i);
              break;
            }
          }
        }
        if (blockedUsers.size() == 0) {
          ismFragmentBlockedUsersBinding.tvNoUsers.setVisibility(View.VISIBLE);
          ismFragmentBlockedUsersBinding.rvUsers.setVisibility(View.GONE);
        }
        hideProgressDialog();
      });
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismFragmentBlockedUsersBinding.shimmerFrameLayout.startShimmer();
      ismFragmentBlockedUsersBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismFragmentBlockedUsersBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismFragmentBlockedUsersBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismFragmentBlockedUsersBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}