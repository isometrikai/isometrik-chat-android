package io.isometrik.ui.messages.broadcast;

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
import io.isometrik.chat.databinding.IsmActivityBroadcastMessageBinding;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The activity to fetch list of users to broadcast a message to, with paging, search and pull to refresh option.Code to broadcast a message to selected users.
 */
public class BroadcastMessageActivity extends AppCompatActivity
    implements BroadcastMessageContract.View {

  private BroadcastMessageContract.Presenter broadcastMessagePresenter;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private final ArrayList<BroadcastToPeopleModel> people = new ArrayList<>();
  private final ArrayList<BroadcastToPeopleModel> selectedPeople = new ArrayList<>();

  private BroadcastToPeopleAdapter broadcastToPeopleAdapter;
  private SelectedBroadcastToPeopleAdapter selectedPeopleAdapter;

  private LinearLayoutManager peopleLayoutManager;
  private LinearLayoutManager selectedPeopleLayoutManager;

  private int count;
  private final int MAXIMUM_PEOPLE =
      Constants.BROADCAST_FORWARD_CONVERSATIONS_PEOPLE_SIZE_AT_A_TIME;
  private IsmActivityBroadcastMessageBinding ismActivityBroadcastMessageBinding;
  private boolean selectedUsersStateNeedToBeSaved = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityBroadcastMessageBinding =
        IsmActivityBroadcastMessageBinding.inflate(getLayoutInflater());
    View view = ismActivityBroadcastMessageBinding.getRoot();
    setContentView(view);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    broadcastMessagePresenter = new BroadcastMessagePresenter(this);

    peopleLayoutManager = new LinearLayoutManager(this);
    ismActivityBroadcastMessageBinding.rvPeople.setLayoutManager(peopleLayoutManager);
    broadcastToPeopleAdapter = new BroadcastToPeopleAdapter(this, people);
    ismActivityBroadcastMessageBinding.rvPeople.addOnScrollListener(peopleScrollListener);
    ismActivityBroadcastMessageBinding.rvPeople.setAdapter(broadcastToPeopleAdapter);

    selectedPeopleLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    ismActivityBroadcastMessageBinding.rvPeopleSelected.setLayoutManager(
        selectedPeopleLayoutManager);
    selectedPeopleAdapter = new SelectedBroadcastToPeopleAdapter(this, selectedPeople);
    ismActivityBroadcastMessageBinding.rvPeopleSelected.setAdapter(selectedPeopleAdapter);

    fetchPeople(false, null, false);

    ismActivityBroadcastMessageBinding.rvPeople.addOnItemTouchListener(
        new RecyclerItemClickListener(this, ismActivityBroadcastMessageBinding.rvPeople,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {

                  BroadcastToPeopleModel peopleModel = people.get(position);

                  if (peopleModel.isSelected()) {
                    peopleModel.setSelected(false);
                    count--;
                    removeSelectedPeople(peopleModel.getUserId());
                  } else {

                    if (count < MAXIMUM_PEOPLE) {
                      //Maximum 100 users can be added
                      peopleModel.setSelected(true);
                      count++;
                      addSelectedPeople(peopleModel);
                    } else {
                      Toast.makeText(BroadcastMessageActivity.this,
                          getString(R.string.ism_broadcast_max_people_limit_reached),
                          Toast.LENGTH_SHORT).show();
                    }
                  }
                  updateSelectedText();
                  people.set(position, peopleModel);
                  broadcastToPeopleAdapter.notifyItemChanged(position);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismActivityBroadcastMessageBinding.refresh.setOnRefreshListener(
        () -> fetchPeople(false, null, true));

    ismActivityBroadcastMessageBinding.ivBroadcastMessage.setOnClickListener(v -> {

      ArrayList<String> userIds = new ArrayList<>();
      for (int i = 0; i < selectedPeople.size(); i++) {
        userIds.add(selectedPeople.get(i).getUserId());
      }
      if (userIds.isEmpty()) {
        Toast.makeText(this, R.string.ism_minimum_one_person_broadcast, Toast.LENGTH_SHORT).show();
      } else {
        if (ismActivityBroadcastMessageBinding.etBroadcastMessage.getText() == null) {
          Toast.makeText(this, R.string.ism_broadcasting_message_to_people_failed,
              Toast.LENGTH_SHORT).show();
        } else {
          String message =
              ismActivityBroadcastMessageBinding.etBroadcastMessage.getText().toString();

          if (message.isEmpty()) {
            Toast.makeText(this, R.string.ism_add_broadcast_message, Toast.LENGTH_SHORT).show();
          } else {

            new AlertDialog.Builder(this).setTitle(getString(R.string.ism_broadcast_message_alert_heading)).setMessage(
                getString(R.string.ism_broadcast_message_to_people_confirmation, userIds.size()))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

                  dialog.cancel();

                  showProgressDialog(getString(R.string.ism_broadcasting_message_to_people));
                  broadcastMessagePresenter.broadcastMessage(
                      ismActivityBroadcastMessageBinding.etBroadcastMessage.getText().toString(),
                      userIds);
                })
                .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
                .create()
                .show();
          }
        }
      }
    });

    ismActivityBroadcastMessageBinding.ibBack.setOnClickListener(v -> onBackPressed());

    ismActivityBroadcastMessageBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchPeople(true, s.toString(), false);
        } else {
          if (selectedPeople.size() > 0) {
            selectedUsersStateNeedToBeSaved = true;
          }
          fetchPeople(false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
  }

  @Override
  public void onMessageBroadcastedSuccessfully() {
    hideProgressDialog();
    onBackPressed();
  }

  private final RecyclerView.OnScrollListener peopleScrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      broadcastMessagePresenter.fetchPeopleOnScroll(
          peopleLayoutManager.findFirstVisibleItemPosition(), peopleLayoutManager.getChildCount(),
          peopleLayoutManager.getItemCount());
    }
  };

  private void fetchPeople(boolean isSearchRequest, String searchTag, boolean showProgressDialog) {
    if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_people));

    try {
      broadcastMessagePresenter.fetchPeople(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onPeopleFetchedSuccessfully(ArrayList<BroadcastToPeopleModel> peopleModels,
      boolean refreshRequest, boolean isSearchRequest) {

    if (refreshRequest) {
      people.clear();
      if (isSearchRequest || selectedUsersStateNeedToBeSaved) {
        int size = peopleModels.size();
        BroadcastToPeopleModel broadcastToPeopleModel;

        for (int i = 0; i < size; i++) {

          for (int j = 0; j < selectedPeople.size(); j++) {
            if (selectedPeople.get(j).getUserId().equals(peopleModels.get(i).getUserId())) {
              broadcastToPeopleModel = peopleModels.get(i);
              broadcastToPeopleModel.setSelected(true);
              peopleModels.set(i, broadcastToPeopleModel);
              break;
            }
          }
        }
        if (!isSearchRequest) selectedUsersStateNeedToBeSaved = false;
      } else {

        runOnUiThread(() -> {
          selectedPeople.clear();
          count = 0;
          selectedPeopleAdapter.notifyDataSetChanged();
          updateSelectedText();
        });
      }
    } else {

      int size = peopleModels.size();
      BroadcastToPeopleModel broadcastToPeopleModel;

      for (int i = 0; i < size; i++) {

        for (int j = 0; j < selectedPeople.size(); j++) {
          if (selectedPeople.get(j).getUserId().equals(peopleModels.get(i).getUserId())) {
            broadcastToPeopleModel = peopleModels.get(i);
            broadcastToPeopleModel.setSelected(true);
            peopleModels.set(i, broadcastToPeopleModel);
            break;
          }
        }
      }
    }
    people.addAll(peopleModels);

    runOnUiThread(() -> {
      if (refreshRequest) {
        if (people.size() > 0) {
          ismActivityBroadcastMessageBinding.tvNoPeople.setVisibility(View.GONE);
          ismActivityBroadcastMessageBinding.rvPeople.setVisibility(View.VISIBLE);
        } else {
          ismActivityBroadcastMessageBinding.tvNoPeople.setVisibility(View.VISIBLE);
          ismActivityBroadcastMessageBinding.rvPeople.setVisibility(View.GONE);
        }
      }
      broadcastToPeopleAdapter.notifyDataSetChanged();

      hideProgressDialog();
      if (ismActivityBroadcastMessageBinding.refresh.isRefreshing()) {
        ismActivityBroadcastMessageBinding.refresh.setRefreshing(false);
      }
      updateShimmerVisibility(false);
    });
  }

  @Override
  public void onError(String errorMessage) {
    if (ismActivityBroadcastMessageBinding.refresh.isRefreshing()) {
      ismActivityBroadcastMessageBinding.refresh.setRefreshing(false);
    }
    hideProgressDialog();
    updateShimmerVisibility(false);
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
      }
    });
  }

  /**
   * Remove people.
   *
   * @param userId the user id
   */
  public void removePeople(String userId) {
    int size = people.size();
    for (int i = 0; i < size; i++) {

      if (people.get(i).getUserId().equals(userId)) {

        BroadcastToPeopleModel peopleModel = people.get(i);
        peopleModel.setSelected(false);
        people.set(i, peopleModel);
        if (i == 0) {
          broadcastToPeopleAdapter.notifyDataSetChanged();
        } else {
          broadcastToPeopleAdapter.notifyItemChanged(i);
        }
        count--;
        updateSelectedText();
        break;
      }
    }

    for (int i = 0; i < selectedPeople.size(); i++) {

      if (selectedPeople.get(i).getUserId().equals(userId)) {
        selectedPeople.remove(i);
        if (i == 0) {
          selectedPeopleAdapter.notifyDataSetChanged();
        } else {
          selectedPeopleAdapter.notifyItemRemoved(i);
        }

        break;
      }
    }
  }

  private void removeSelectedPeople(String userId) {

    for (int i = 0; i < selectedPeople.size(); i++) {
      if (selectedPeople.get(i).getUserId().equals(userId)) {
        selectedPeople.remove(i);
        if (i == 0) {
          selectedPeopleAdapter.notifyDataSetChanged();
        } else {
          selectedPeopleAdapter.notifyItemRemoved(i);
        }
        break;
      }
    }
  }

  private void addSelectedPeople(BroadcastToPeopleModel peopleModel) {
    selectedPeople.add(peopleModel);
    try {
      selectedPeopleAdapter.notifyDataSetChanged();
      selectedPeopleLayoutManager.scrollToPosition(selectedPeople.size() - 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateSelectedText() {

    if (count > 0) {
      ismActivityBroadcastMessageBinding.tvAddPeople.setText(
          getString(R.string.ism_number_of_people_selected, String.valueOf(count),
              String.valueOf(MAXIMUM_PEOPLE)));
    } else {
      ismActivityBroadcastMessageBinding.tvAddPeople.setText(getString(R.string.ism_select_people));
    }
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
      ismActivityBroadcastMessageBinding.shimmerFrameLayout.startShimmer();
      ismActivityBroadcastMessageBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismActivityBroadcastMessageBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismActivityBroadcastMessageBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismActivityBroadcastMessageBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}