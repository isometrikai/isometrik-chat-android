package io.isometrik.ui.conversations.details.participants.watchers;

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
import io.isometrik.ui.conversations.details.participants.MembersWatchersActivity;
import io.isometrik.ui.conversations.details.participants.MembersWatchersAdapter;
import io.isometrik.ui.conversations.details.participants.MembersWatchersModel;
import io.isometrik.chat.databinding.IsmFragmentMembersWatchersBinding;
import io.isometrik.chat.utils.AlertProgress;
import java.util.ArrayList;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;

/**
 * The fragment to fetch list of watchers in a conversation with paging, search and pull to refresh
 * option and kickout watcher, grant/revoke admin privileges.
 */
public class WatchersFragment extends Fragment implements WatchersContract.View {

  private WatchersContract.Presenter watchersPresenter;
  private IsmFragmentMembersWatchersBinding ismFragmentMembersWatchersBinding;

  private LinearLayoutManager watchersLayoutManager;

  private final ArrayList<MembersWatchersModel> watchers = new ArrayList<>();

  private MembersWatchersAdapter watchersAdapter;
  private String conversationId;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    watchersPresenter = new WatchersPresenter();

    watchersPresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    ismFragmentMembersWatchersBinding = IsmFragmentMembersWatchersBinding.inflate(inflater, container, false);

    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    Bundle extras = getActivity().getIntent().getExtras();
    conversationId = extras.getString("conversationId");

    watchersPresenter.initialize(conversationId, extras.getBoolean("isUserAnAdmin"), extras.getString("userId"));

    watchersLayoutManager = new LinearLayoutManager(getActivity());

    ismFragmentMembersWatchersBinding.rvMembersWatchers.setLayoutManager(watchersLayoutManager);

    watchersAdapter = new MembersWatchersAdapter(getActivity(), watchers, null, this);

    ismFragmentMembersWatchersBinding.rvMembersWatchers.addOnScrollListener(watchersOnScrollListener);

    ismFragmentMembersWatchersBinding.rvMembersWatchers.setAdapter(watchersAdapter);

    ismFragmentMembersWatchersBinding.etSearch.setHint(getString(R.string.ism_search_watchers));

    fetchWatchers(false, null, false);
    ismFragmentMembersWatchersBinding.refresh.setOnRefreshListener(() -> fetchWatchers(false, null, true));

    ismFragmentMembersWatchersBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchWatchers(true, s.toString(), false);
        } else {

          fetchWatchers(false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    return ismFragmentMembersWatchersBinding.getRoot();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    watchersPresenter.detachView();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ismFragmentMembersWatchersBinding = null;
  }

  private final RecyclerView.OnScrollListener watchersOnScrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      watchersPresenter.fetchWatchersOnScroll(watchersLayoutManager.findFirstVisibleItemPosition(),
          watchersLayoutManager.getChildCount(), watchersLayoutManager.getItemCount());
    }
  };

  private void fetchWatchers(boolean isSearchRequest, String searchTag, boolean showProgressDialog) {
    if (showProgressDialog) {
      showProgressDialog(getString(R.string.ism_fetching_conversation_watchers));
    }

    try {
      watchersPresenter.fetchWatchers(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onMemberRemovedSuccessfully(String memberId) {
    hideProgressDialog();
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        for (int i = 0; i < watchers.size(); i++) {

          if (watchers.get(i).getMemberId().equals(memberId)) {

            watchers.remove(i);
            watchersAdapter.notifyItemRemoved(i);
            ((MembersWatchersActivity) getActivity()).memberRefreshRequired = true;
            break;
          }
        }
      });
    }
  }

  @Override
  public void onMemberAdminPermissionsUpdated(String memberId, boolean admin) {
    hideProgressDialog();
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        for (int i = 0; i < watchers.size(); i++) {

          if (watchers.get(i).getMemberId().equals(memberId)) {

            MembersWatchersModel membersModel = watchers.get(i);
            membersModel.setAdmin(admin);
            watchers.set(i, membersModel);
            watchersAdapter.notifyItemChanged(i);

            ((MembersWatchersActivity) getActivity()).memberRefreshRequired = true;
            break;
          }
        }
      });
    }
  }

  @Override
  public void onWatchersFetchedSuccessfully(ArrayList<MembersWatchersModel> watchersModels, boolean refreshRequest) {
    if (refreshRequest) {
      watchers.clear();
    }
    watchers.addAll(watchersModels);
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (refreshRequest) {
          if (watchers.size() > 0) {
            ismFragmentMembersWatchersBinding.tvNoMembersWatchers.setVisibility(View.GONE);
            ismFragmentMembersWatchersBinding.rvMembersWatchers.setVisibility(View.VISIBLE);
          } else {
            ismFragmentMembersWatchersBinding.tvNoMembersWatchers.setVisibility(View.VISIBLE);
            ismFragmentMembersWatchersBinding.tvNoMembersWatchers.setText(getString(R.string.ism_no_watchers));
            ismFragmentMembersWatchersBinding.rvMembersWatchers.setVisibility(View.GONE);
          }
        }
        watchersAdapter.notifyDataSetChanged();

        hideProgressDialog();
        if (ismFragmentMembersWatchersBinding.refresh.isRefreshing()) {
          ismFragmentMembersWatchersBinding.refresh.setRefreshing(false);
        }
        updateShimmerVisibility(false);
      });
    }
  }

  @Override
  public void onError(String errorMessage) {

    hideProgressDialog();
    if (ismFragmentMembersWatchersBinding.refresh.isRefreshing()) {
      ismFragmentMembersWatchersBinding.refresh.setRefreshing(false);
    }
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

  /**
   * Kick out member.
   *
   * @param memberId the member id
   * @param memberName the member name
   */
  public void kickOutMember(String memberId, String memberName) {
    if (getActivity() != null) {
      new AlertDialog.Builder(getActivity()).setTitle(
              getString(R.string.ism_kick_out_member_heading, memberName))
          .setMessage(getString(R.string.ism_kick_out_member_alert_message))
          .setCancelable(true)
          .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

            dialog.cancel();
            showProgressDialog(getString(R.string.ism_kicking_out_member, memberName));
            watchersPresenter.kickOutMembers(conversationId, Collections.singletonList(memberId));
          })
          .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
          .create()
          .show();
    }
  }

  /**
   * Make admin.
   *
   * @param memberId the member id
   * @param memberName the member name
   */
  public void makeAdmin(String memberId, String memberName) {
    if (getActivity() != null) {
      new AlertDialog.Builder(getActivity()).setTitle(
              getString(R.string.ism_make_admin_alert_heading, memberName))
          .setMessage(getString(R.string.ism_make_admin_alert_message))
          .setCancelable(true)
          .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

            dialog.cancel();
            showProgressDialog(getString(R.string.ism_making_admin, memberName));
            watchersPresenter.makeAdmin(conversationId, memberId);
          })
          .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
          .create()
          .show();
    }
  }

  /**
   * Remove admin.
   *
   * @param memberId the member id
   * @param memberName the member name
   */
  public void removeAdmin(String memberId, String memberName) {
    if (getActivity() != null) {
      new AlertDialog.Builder(getActivity()).setTitle(
              getString(R.string.ism_remove_as_admin_alert_heading, memberName))
          .setMessage(getString(R.string.ism_remove_as_admin_alert_message))
          .setCancelable(true)
          .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

            dialog.cancel();
            showProgressDialog(getString(R.string.ism_removing_admin, memberName));
            watchersPresenter.revokeAdminPermissions(conversationId, memberId);
          })
          .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
          .create()
          .show();
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

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismFragmentMembersWatchersBinding.shimmerFrameLayout.startShimmer();
      ismFragmentMembersWatchersBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismFragmentMembersWatchersBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismFragmentMembersWatchersBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismFragmentMembersWatchersBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
