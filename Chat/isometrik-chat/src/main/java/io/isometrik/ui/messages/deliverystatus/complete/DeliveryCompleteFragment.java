package io.isometrik.ui.messages.deliverystatus.complete;

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
 * The fragment to fetch list of users for whom message delivery and read is complete
 * for a message, with paging.
 */
public class DeliveryCompleteFragment extends Fragment implements DeliveryCompleteContract.View {

  private DeliveryCompleteContract.Presenter deliveryCompletePresenter;
  private IsmFragmentDeliveryReadStatusBinding ismDeliveryCompleteBinding;

  private LinearLayoutManager deliveredToLayoutManager;
  private LinearLayoutManager readByLayoutManager;

  private final ArrayList<UsersModel> deliveredToUsers = new ArrayList<>();
  private final ArrayList<UsersModel> readByUsers = new ArrayList<>();
  private UsersAdapter deliveredToUsersAdapter, readByUsersAdapter;
  private boolean unregisteredListeners;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    deliveryCompletePresenter = new DeliveryCompletePresenter();

    deliveryCompletePresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismDeliveryCompleteBinding =
        IsmFragmentDeliveryReadStatusBinding.inflate(inflater, container, false);
    updateDeliveryShimmerVisibility(true);
    updateReadShimmerVisibility(true);

    Bundle extras = getActivity().getIntent().getExtras();

    deliveryCompletePresenter.initialize(extras.getString("conversationId"),
        extras.getString("messageId"), extras.getLong("sentAt"));

    deliveredToLayoutManager = new LinearLayoutManager(getActivity());
    readByLayoutManager = new LinearLayoutManager(getActivity());

    ismDeliveryCompleteBinding.rvDelivered.setLayoutManager(deliveredToLayoutManager);
    ismDeliveryCompleteBinding.rvRead.setLayoutManager(readByLayoutManager);

    deliveredToUsersAdapter = new UsersAdapter(getActivity(), deliveredToUsers, false);
    readByUsersAdapter = new UsersAdapter(getActivity(), readByUsers, false);

    ismDeliveryCompleteBinding.rvDelivered.addOnScrollListener(deliveredToOnScrollListener);
    ismDeliveryCompleteBinding.rvRead.addOnScrollListener(readByOnScrollListener);

    ismDeliveryCompleteBinding.rvDelivered.setAdapter(deliveredToUsersAdapter);
    ismDeliveryCompleteBinding.rvRead.setAdapter(readByUsersAdapter);

    fetchDeliveryCompleteStatus();

    deliveryCompletePresenter.registerMessageEventListener();
    return ismDeliveryCompleteBinding.getRoot();
  }

  @Override
  public void onDestroy() {
    unregisterListeners();
    super.onDestroy();
    deliveryCompletePresenter.detachView();
  }

  @Override
  public void onDestroyView() {
    unregisterListeners();
    super.onDestroyView();
    ismDeliveryCompleteBinding = null;
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

          deliveryCompletePresenter.fetchMessageDeliveryStatusOnScroll(
              deliveredToLayoutManager.findFirstVisibleItemPosition(),
              deliveredToLayoutManager.getChildCount(), deliveredToLayoutManager.getItemCount());
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

          deliveryCompletePresenter.fetchMessageReadStatusOnScroll(
              readByLayoutManager.findFirstVisibleItemPosition(),
              readByLayoutManager.getChildCount(), readByLayoutManager.getItemCount());
        }
      };

  private void fetchDeliveryCompleteStatus() {
    deliveryCompletePresenter.fetchMessageDeliveryStatus(0, Constants.USERS_PAGE_SIZE, false);
    deliveryCompletePresenter.fetchMessageReadStatus(0, Constants.USERS_PAGE_SIZE, false);
  }

  @Override
  public void onMessageDeliveryStatusFetchedSuccessfully(ArrayList<UsersModel> usersModels,
      boolean resultsOnScroll) {

    deliveredToUsers.addAll(usersModels);
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (deliveredToUsers.size() > 0) {
            ismDeliveryCompleteBinding.tvNoDeliveryUsers.setVisibility(View.GONE);
            ismDeliveryCompleteBinding.rvDelivered.setVisibility(View.VISIBLE);
          } else {
            ismDeliveryCompleteBinding.tvNoDeliveryUsers.setVisibility(View.VISIBLE);
            ismDeliveryCompleteBinding.tvNoDeliveryUsers.setText(
                getString(R.string.ism_delivered_to_none));
            ismDeliveryCompleteBinding.rvDelivered.setVisibility(View.GONE);
          }
        }
        deliveredToUsersAdapter.notifyDataSetChanged();
        updateDeliveryShimmerVisibility(false);
      });
    }
  }

  @Override
  public void onMessageReadStatusFetchedSuccessfully(ArrayList<UsersModel> usersModels,
      boolean resultsOnScroll) {

    readByUsers.addAll(usersModels);
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (readByUsers.size() > 0) {
            ismDeliveryCompleteBinding.tvNoReadUsers.setVisibility(View.GONE);
            ismDeliveryCompleteBinding.rvRead.setVisibility(View.VISIBLE);
          } else {
            ismDeliveryCompleteBinding.tvNoReadUsers.setVisibility(View.VISIBLE);
            ismDeliveryCompleteBinding.tvNoReadUsers.setText(getString(R.string.ism_read_by_none));
            ismDeliveryCompleteBinding.rvRead.setVisibility(View.GONE);
          }
        }
        readByUsersAdapter.notifyDataSetChanged();
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

      deliveryCompletePresenter.unregisterMessageEventListener();
    }
  }

  @Override
  public void onMessageDeliveredEvent(UsersModel usersModel) {

    if (getActivity() != null) {

      getActivity().runOnUiThread(() -> {

        int size = deliveredToUsers.size();
        String userId = usersModel.getUserId();
        boolean alreadyAddedDeliveryEvent = false;
        for (int i = 0; i < size; i++) {
          if (deliveredToUsers.get(i).getUserId().equals(userId)) {
            alreadyAddedDeliveryEvent = true;
            break;
          }
        }
        if (!alreadyAddedDeliveryEvent) {
          deliveredToUsers.add(0, usersModel);
          deliveredToUsersAdapter.notifyItemInserted(0);

          if (deliveredToUsers.size() == 1) {
            ismDeliveryCompleteBinding.tvNoDeliveryUsers.setVisibility(View.GONE);
            ismDeliveryCompleteBinding.rvDelivered.setVisibility(View.VISIBLE);
          }
        }
      });
    }
  }

  @Override
  public void onMessageReadEvent(UsersModel usersModel) {

    if (getActivity() != null) {

      getActivity().runOnUiThread(() -> {
        int size = readByUsers.size();
        String userId = usersModel.getUserId();
        boolean alreadyAddedReadEvent = false;
        for (int i = 0; i < size; i++) {
          if (readByUsers.get(i).getUserId().equals(userId)) {
            alreadyAddedReadEvent = true;
            break;
          }
        }

        if (!alreadyAddedReadEvent) {

          readByUsers.add(0, usersModel);
          readByUsersAdapter.notifyItemInserted(0);

          if (readByUsers.size() == 1) {
            ismDeliveryCompleteBinding.tvNoReadUsers.setVisibility(View.GONE);
            ismDeliveryCompleteBinding.rvRead.setVisibility(View.VISIBLE);
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
      ismDeliveryCompleteBinding.shimmerFrameLayoutDelivery.startShimmer();
      ismDeliveryCompleteBinding.shimmerFrameLayoutDelivery.setVisibility(View.VISIBLE);
    } else {
      if (ismDeliveryCompleteBinding.shimmerFrameLayoutDelivery.getVisibility() == View.VISIBLE) {
        ismDeliveryCompleteBinding.shimmerFrameLayoutDelivery.setVisibility(View.GONE);
        ismDeliveryCompleteBinding.shimmerFrameLayoutDelivery.stopShimmer();
      }
    }
  }

  private void updateReadShimmerVisibility(boolean visible) {
    if (visible) {
      ismDeliveryCompleteBinding.shimmerFrameLayoutRead.startShimmer();
      ismDeliveryCompleteBinding.shimmerFrameLayoutRead.setVisibility(View.VISIBLE);
    } else {
      if (ismDeliveryCompleteBinding.shimmerFrameLayoutRead.getVisibility() == View.VISIBLE) {
        ismDeliveryCompleteBinding.shimmerFrameLayoutRead.setVisibility(View.GONE);
        ismDeliveryCompleteBinding.shimmerFrameLayoutRead.stopShimmer();
      }
    }
  }
}
