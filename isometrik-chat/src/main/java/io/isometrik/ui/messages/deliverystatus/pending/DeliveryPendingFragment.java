package io.isometrik.ui.messages.deliverystatus.pending;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmFragmentDeliveryReadStatusBinding;
import io.isometrik.ui.messages.deliverystatus.MessageDeliveryStatusActivity;
import io.isometrik.ui.messages.deliverystatus.UsersAdapter;
import io.isometrik.ui.messages.deliverystatus.UsersModel;
import io.isometrik.chat.utils.Constants;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The fragment to fetch list of users for whom message delivery and read is pending for a message,
 * with paging.
 */
public class DeliveryPendingFragment extends Fragment implements DeliveryPendingContract.View {

  private DeliveryPendingContract.Presenter deliveryPendingPresenter;
  private IsmFragmentDeliveryReadStatusBinding ismDeliveryPendingBinding;

  private LinearLayoutManager pendingDeliveredToLayoutManager;
  private LinearLayoutManager pendingReadByLayoutManager;

  private final ArrayList<UsersModel> pendingDeliveredToUsers = new ArrayList<>();
  private final ArrayList<UsersModel> pendingReadByUsers = new ArrayList<>();
  private UsersAdapter pendingDeliveredToUsersAdapter, pendingReadByUsersAdapter;
  private boolean unregisteredListeners;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    deliveryPendingPresenter = new DeliveryPendingPresenter();

    deliveryPendingPresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismDeliveryPendingBinding =
        IsmFragmentDeliveryReadStatusBinding.inflate(inflater, container, false);
    updateDeliveryShimmerVisibility(true);
    updateReadShimmerVisibility(true);

    ismDeliveryPendingBinding.tvRead.setText(getString(R.string.ism_pending_read_by));
    ismDeliveryPendingBinding.tvDelivered.setText(getString(R.string.ism_pending_delivery_to));

    Bundle extras = getActivity().getIntent().getExtras();

    deliveryPendingPresenter.initialize(extras.getString("conversationId"),
        extras.getString("messageId"), extras.getLong("sentAt"));

    pendingDeliveredToLayoutManager = new LinearLayoutManager(getActivity());
    pendingReadByLayoutManager = new LinearLayoutManager(getActivity());

    ismDeliveryPendingBinding.rvDelivered.setLayoutManager(pendingDeliveredToLayoutManager);
    ismDeliveryPendingBinding.rvRead.setLayoutManager(pendingReadByLayoutManager);

    pendingDeliveredToUsersAdapter = new UsersAdapter(getActivity(), pendingDeliveredToUsers, true);
    pendingReadByUsersAdapter = new UsersAdapter(getActivity(), pendingReadByUsers, true);

    ismDeliveryPendingBinding.rvDelivered.addOnScrollListener(deliveredToOnScrollListener);
    ismDeliveryPendingBinding.rvRead.addOnScrollListener(readByOnScrollListener);

    ismDeliveryPendingBinding.rvDelivered.setAdapter(pendingDeliveredToUsersAdapter);
    ismDeliveryPendingBinding.rvRead.setAdapter(pendingReadByUsersAdapter);

    fetchDeliveryCompleteStatus();
    deliveryPendingPresenter.registerMessageEventListener();
    return ismDeliveryPendingBinding.getRoot();
  }

  @Override
  public void onDestroy() {
    unregisterListeners();
    super.onDestroy();
    deliveryPendingPresenter.detachView();
  }

  @Override
  public void onDestroyView() {
    unregisterListeners();
    super.onDestroyView();
    ismDeliveryPendingBinding = null;
  }

  private final RecyclerView.OnScrollListener deliveredToOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          deliveryPendingPresenter.fetchMessagePendingDeliveryStatusOnScroll(
              pendingDeliveredToLayoutManager.findFirstVisibleItemPosition(),
              pendingDeliveredToLayoutManager.getChildCount(),
              pendingDeliveredToLayoutManager.getItemCount());
        }
      };

  private final RecyclerView.OnScrollListener readByOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          deliveryPendingPresenter.fetchMessagePendingReadStatusOnScroll(
              pendingReadByLayoutManager.findFirstVisibleItemPosition(),
              pendingReadByLayoutManager.getChildCount(),
              pendingReadByLayoutManager.getItemCount());
        }
      };

  private void fetchDeliveryCompleteStatus() {
    deliveryPendingPresenter.fetchMessagePendingDeliveryStatus(0, Constants.USERS_PAGE_SIZE, false);
    deliveryPendingPresenter.fetchMessagePendingReadStatus(0, Constants.USERS_PAGE_SIZE, false);
  }

  @Override
  public void onMessagePendingDeliveryStatusFetchedSuccessfully(ArrayList<UsersModel> usersModels,
      boolean resultsOnScroll) {
    pendingDeliveredToUsers.addAll(usersModels);
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (pendingDeliveredToUsers.size() > 0) {
            ismDeliveryPendingBinding.tvNoDeliveryUsers.setVisibility(View.GONE);
            ismDeliveryPendingBinding.rvDelivered.setVisibility(View.VISIBLE);
          } else {
            ismDeliveryPendingBinding.tvNoDeliveryUsers.setVisibility(View.VISIBLE);
            ismDeliveryPendingBinding.tvNoDeliveryUsers.setText(
                getString(R.string.ism_delivery_pending_to_none));
            ismDeliveryPendingBinding.rvDelivered.setVisibility(View.GONE);
          }
        }
        pendingDeliveredToUsersAdapter.notifyDataSetChanged();
        updateDeliveryShimmerVisibility(false);
      });
    }
  }

  @Override
  public void onMessagePendingReadStatusFetchedSuccessfully(ArrayList<UsersModel> usersModels,
      boolean resultsOnScroll) {
    pendingReadByUsers.addAll(usersModels);
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (pendingReadByUsers.size() > 0) {
            ismDeliveryPendingBinding.tvNoReadUsers.setVisibility(View.GONE);
            ismDeliveryPendingBinding.rvRead.setVisibility(View.VISIBLE);
          } else {
            ismDeliveryPendingBinding.tvNoReadUsers.setVisibility(View.VISIBLE);
            ismDeliveryPendingBinding.tvNoReadUsers.setText(
                getString(R.string.ism_read_pending_by_none));
            ismDeliveryPendingBinding.rvRead.setVisibility(View.GONE);
            //ismDeliveryPendingBinding.tvReadMore.setVisibility(View.GONE);
          }
        }
        pendingReadByUsersAdapter.notifyDataSetChanged();
        updateReadShimmerVisibility(false);
      });
    }
  }

  @Override
  public void onError(String errorMessage) {

    updateDeliveryShimmerVisibility(false);
    updateReadShimmerVisibility(false);
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
   * Cleanup all realtime isometrik event listeners that were added at time of exit
   */
  private void unregisterListeners() {

    if (!unregisteredListeners) {

      unregisteredListeners = true;

      deliveryPendingPresenter.unregisterMessageEventListener();
    }
  }

  @Override
  public void onMessageDeliveredEvent(String userId) {
    if (getActivity() != null) {

      getActivity().runOnUiThread(() -> {
        int size = pendingDeliveredToUsers.size();
        for (int i = 0; i < size; i++) {
          if (pendingDeliveredToUsers.get(i).getUserId().equals(userId)) {
            pendingDeliveredToUsers.remove(i);
            pendingDeliveredToUsersAdapter.notifyItemRemoved(i);

            if (size == 1) {

              ismDeliveryPendingBinding.tvNoDeliveryUsers.setVisibility(View.VISIBLE);
              ismDeliveryPendingBinding.tvNoDeliveryUsers.setText(
                  getString(R.string.ism_delivery_pending_to_none));
              ismDeliveryPendingBinding.rvDelivered.setVisibility(View.GONE);
            }
            break;
          }
        }
      });
    }
  }

  @Override
  public void onMessageReadEvent(String userId) {
    if (getActivity() != null) {

      getActivity().runOnUiThread(() -> {
        int size = pendingReadByUsers.size();
        for (int i = 0; i < size; i++) {
          if (pendingReadByUsers.get(i).getUserId().equals(userId)) {
            pendingReadByUsers.remove(i);
            pendingReadByUsersAdapter.notifyItemRemoved(i);

            if (size == 1) {

              ismDeliveryPendingBinding.tvNoReadUsers.setVisibility(View.VISIBLE);
              ismDeliveryPendingBinding.tvNoReadUsers.setText(
                  getString(R.string.ism_read_pending_by_none));
              ismDeliveryPendingBinding.rvRead.setVisibility(View.GONE);
            }
            break;
          }
        }
      });
    }
  }

  @Override
  public void onMessageDeliveryReadEventsDisabled() {
    updateDeliveryShimmerVisibility(false);
    updateReadShimmerVisibility(false);
    if (getActivity() != null) {
      ((MessageDeliveryStatusActivity) getActivity()).onMessageDeliveryReadEventsDisabledForConversation();
    }
  }

  private void updateDeliveryShimmerVisibility(boolean visible) {
    if (visible) {
      ismDeliveryPendingBinding.shimmerFrameLayoutDelivery.startShimmer();
      ismDeliveryPendingBinding.shimmerFrameLayoutDelivery.setVisibility(View.VISIBLE);
    } else {
      if (ismDeliveryPendingBinding.shimmerFrameLayoutDelivery.getVisibility() == View.VISIBLE) {
        ismDeliveryPendingBinding.shimmerFrameLayoutDelivery.setVisibility(View.GONE);
        ismDeliveryPendingBinding.shimmerFrameLayoutDelivery.stopShimmer();
      }
    }
  }

  private void updateReadShimmerVisibility(boolean visible) {
    if (visible) {
      ismDeliveryPendingBinding.shimmerFrameLayoutRead.startShimmer();
      ismDeliveryPendingBinding.shimmerFrameLayoutRead.setVisibility(View.VISIBLE);
    } else {
      if (ismDeliveryPendingBinding.shimmerFrameLayoutRead.getVisibility() == View.VISIBLE) {
        ismDeliveryPendingBinding.shimmerFrameLayoutRead.setVisibility(View.GONE);
        ismDeliveryPendingBinding.shimmerFrameLayoutRead.stopShimmer();
      }
    }
  }
}