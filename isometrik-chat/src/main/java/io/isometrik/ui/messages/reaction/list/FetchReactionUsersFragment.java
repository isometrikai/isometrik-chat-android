package io.isometrik.ui.messages.reaction.list;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmBottomsheetReactionUsersBinding;
import io.isometrik.ui.messages.action.MessageActionCallback;
import io.isometrik.ui.messages.reaction.add.ReactionModel;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.Constants;
import io.isometrik.ui.utils.GlideApp;

import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The fragment to fetch list of users who have added a particular reaction to a message to, with
 * paging and pull to refresh option.Code to remove reaction added by logged in user.
 */
public class FetchReactionUsersFragment extends BottomSheetDialogFragment
    implements FetchReactionUsersContract.View {

  /**
   * The constant TAG.
   */
  public static final String TAG = "FetchReactionUsersFragment";

  private FetchReactionUsersContract.Presenter fetchReactionUsersPresenter;
  private IsmBottomsheetReactionUsersBinding ismBottomsheetReactionUsersBinding;
  private ArrayList<ReactionUsersModel> reactionUsers;
  private FetchReactionUsersAdapter fetchReactionUsersAdapter;
  private Activity activity;
  private String messageId, conversationId;
  private ReactionModel reactionModel;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private LinearLayoutManager reactionUsersLayoutManager;
  private MessageActionCallback messageActionCallback;
  private boolean messagingDisabled;

  /**
   * Instantiates a new reaction fragment.
   */
  public FetchReactionUsersFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    ismBottomsheetReactionUsersBinding =
        IsmBottomsheetReactionUsersBinding.inflate(inflater, container, false);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    reactionUsers = new ArrayList<>();
    reactionUsersLayoutManager = new LinearLayoutManager(activity);
    ismBottomsheetReactionUsersBinding.rvReactions.setLayoutManager(reactionUsersLayoutManager);
    ismBottomsheetReactionUsersBinding.rvReactions.addOnScrollListener(
        recyclerViewOnScrollListener);
    fetchReactionUsersAdapter =
        new FetchReactionUsersAdapter(activity, reactionUsers, this, messagingDisabled);
    ismBottomsheetReactionUsersBinding.rvReactions.setAdapter(fetchReactionUsersAdapter);
    fetchReactionUsers();

    ismBottomsheetReactionUsersBinding.tvReactionName.setText(reactionModel.getReactionName());

    try {
      GlideApp.with(activity)
          .load(reactionModel.getReactionIcon())
          .into(ismBottomsheetReactionUsersBinding.ivReactionImage);
    } catch (IllegalArgumentException | NullPointerException ignore) {
    }

    ismBottomsheetReactionUsersBinding.refresh.setOnRefreshListener(this::fetchReactionUsers);

    return ismBottomsheetReactionUsersBinding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ismBottomsheetReactionUsersBinding = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    fetchReactionUsersPresenter =
        new FetchReactionUsersPresenter(conversationId, messageId, reactionModel.getReactionType());
    fetchReactionUsersPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    fetchReactionUsersPresenter.detachView();
    activity = null;
  }

  @Override
  public void onError(String errorMessage) {
    if (ismBottomsheetReactionUsersBinding.refresh.isRefreshing()) {
      ismBottomsheetReactionUsersBinding.refresh.setRefreshing(false);
    }
    hideProgressDialog();
    updateShimmerVisibility(false);
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  @Override
  public void onReactionsFetchedSuccessfully(ArrayList<ReactionUsersModel> reactionUsers,
      boolean latestUsers) {
    if (latestUsers) {
      this.reactionUsers.clear();
    }

    this.reactionUsers.addAll(reactionUsers);
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (this.reactionUsers.size() > 0) {
          ismBottomsheetReactionUsersBinding.tvNoUsers.setVisibility(View.GONE);
          ismBottomsheetReactionUsersBinding.rvReactions.setVisibility(View.VISIBLE);
          fetchReactionUsersAdapter.notifyDataSetChanged();
        } else {
          ismBottomsheetReactionUsersBinding.tvNoUsers.setVisibility(View.VISIBLE);
          ismBottomsheetReactionUsersBinding.rvReactions.setVisibility(View.GONE);
        }
        hideProgressDialog();
        if (ismBottomsheetReactionUsersBinding.refresh.isRefreshing()) {
          ismBottomsheetReactionUsersBinding.refresh.setRefreshing(false);
        }

        updateShimmerVisibility(false);
      });
    }
  }

  @Override
  public void onReactionRemovedSuccessfully(int reactionUserPosition, String messageId,
      ReactionModel reactionModel) {
    hideProgressDialog();
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int position = fetchReactionUsersPresenter.verifyUserPositionInList(reactionUserPosition,
            reactionUsers);
        if (position != -1) {
          fetchReactionUsersAdapter.updateReactionUsersListOnReactionRemoved(position);
        }

        if (reactionUsers.size() == 0) {

          ismBottomsheetReactionUsersBinding.tvNoUsers.setVisibility(View.VISIBLE);
          ismBottomsheetReactionUsersBinding.rvReactions.setVisibility(View.GONE);
        }
      });
    }
    messageActionCallback.updateMessageReaction(messageId, reactionModel,false);
  }

  /**
   * Remove reaction.
   *
   * @param position the position
   */
  public void removeReaction(int position) {
    showProgressDialog(getString(R.string.ism_removing_reaction, reactionModel.getReactionName()));
    fetchReactionUsersPresenter.removeReaction(conversationId, messageId,
        reactionModel.getReactionType(), position);
  }

  private final RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          fetchReactionUsersPresenter.requestReactionUsersDataOnScroll(
              reactionUsersLayoutManager.findFirstVisibleItemPosition(),
              reactionUsersLayoutManager.getChildCount(),
              reactionUsersLayoutManager.getItemCount());
        }
      };

  private void fetchReactionUsers() {

    showProgressDialog(
        getString(R.string.ism_fetching_reaction_users, reactionModel.getReactionName()));

    try {
      fetchReactionUsersPresenter.fetchReactionUsers(conversationId, messageId,
          reactionModel.getReactionType(), 0, Constants.USERS_PAGE_SIZE, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new BottomSheetDialog(activity, R.style.TransparentBackgroundDialogStyle);
  }

  private void showProgressDialog(String message) {
    if (activity != null) {
      alertDialog = alertProgress.getProgressDialog(activity, message);
      if (!activity.isFinishing()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  /**
   * Update parameters.
   *
   * @param conversationId the conversation id
   * @param messageId the message id
   * @param reactionModel the reaction model
   * @param messageActionCallback the message action callback
   * @param messagingDisabled the messaging disabled
   */
  public void updateParameters(String conversationId, String messageId, ReactionModel reactionModel,
      MessageActionCallback messageActionCallback, boolean messagingDisabled) {
    this.messageId = messageId;
    this.conversationId = conversationId;
    this.reactionModel = reactionModel;
    this.messageActionCallback = messageActionCallback;
    this.messagingDisabled = messagingDisabled;
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismBottomsheetReactionUsersBinding.shimmerFrameLayout.startShimmer();
      ismBottomsheetReactionUsersBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismBottomsheetReactionUsersBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismBottomsheetReactionUsersBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismBottomsheetReactionUsersBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
