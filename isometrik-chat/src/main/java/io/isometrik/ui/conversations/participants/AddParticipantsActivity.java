package io.isometrik.ui.conversations.participants;

import android.app.Activity;
import android.content.Intent;
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
import io.isometrik.chat.databinding.IsmActivityAddParticipantsBinding;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The activity to fetch list of users that can be added as participants to a conversation with
 * paging, search and pull to refresh option.Code to add participant to a conversation.
 */
public class AddParticipantsActivity extends AppCompatActivity
    implements AddParticipantsContract.View {

  private AddParticipantsContract.Presenter addParticipantsPresenter;
  private IsmActivityAddParticipantsBinding ismActivityAddParticipantsBinding;

  private final ArrayList<ParticipantsModel> participantsModels = new ArrayList<>();
  private final ArrayList<ParticipantsModel> selectedParticipantsModels = new ArrayList<>();
  private ParticipantsAdapter participantsAdapter;
  private SelectedParticipantsAdapter selectedParticipantsAdapter;
  private LinearLayoutManager participantsLayoutManager;
  private LinearLayoutManager selectedParticipantsLayoutManager;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private int count;
  private final int MAXIMUM_MEMBERS = Constants.CONVERSATION_MEMBERS_SIZE_AT_A_TIME;
  private boolean selectedUsersStateNeedToBeSaved = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityAddParticipantsBinding =
        IsmActivityAddParticipantsBinding.inflate(getLayoutInflater());
    View view = ismActivityAddParticipantsBinding.getRoot();
    setContentView(view);
    alertProgress = new AlertProgress();

    updateShimmerVisibility(true);
    Bundle extras = getIntent().getExtras();

    ismActivityAddParticipantsBinding.tvConversationTitle.setText(
        extras.getString("conversationTitle"));
    addParticipantsPresenter =
        new AddParticipantsPresenter(this, extras.getString("conversationId"));

    participantsLayoutManager = new LinearLayoutManager(this);
    ismActivityAddParticipantsBinding.rvUsers.setLayoutManager(participantsLayoutManager);
    participantsAdapter = new ParticipantsAdapter(this, participantsModels);
    ismActivityAddParticipantsBinding.rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
    ismActivityAddParticipantsBinding.rvUsers.setAdapter(participantsAdapter);

    selectedParticipantsLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    ismActivityAddParticipantsBinding.rvUsersSelected.setLayoutManager(
        selectedParticipantsLayoutManager);
    selectedParticipantsAdapter = new SelectedParticipantsAdapter(this, selectedParticipantsModels);
    ismActivityAddParticipantsBinding.rvUsersSelected.setAdapter(selectedParticipantsAdapter);

    fetchLatestParticipantsToAddToConversation(false, null, false);

    ismActivityAddParticipantsBinding.rvUsers.addOnItemTouchListener(
        new RecyclerItemClickListener(this, ismActivityAddParticipantsBinding.rvUsers,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {

                  ParticipantsModel participantsModel = participantsModels.get(position);

                  if (participantsModel.isSelected()) {
                    participantsModel.setSelected(false);
                    count--;
                    removeSelectedParticipant(participantsModel.getUserId());
                  } else {

                    if (count < MAXIMUM_MEMBERS) {
                      //Maximum 99 members can be added
                      participantsModel.setSelected(true);
                      count++;
                      addSelectedParticipant(participantsModel);
                    } else {
                      Toast.makeText(AddParticipantsActivity.this,
                          getString(R.string.ism_max_participants_limit_reached),
                          Toast.LENGTH_SHORT).show();
                    }
                  }
                  updateSelectedMembersText();
                  participantsModels.set(position, participantsModel);
                  participantsAdapter.notifyItemChanged(position);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismActivityAddParticipantsBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchLatestParticipantsToAddToConversation(true, s.toString(), false);
        } else {
          if (selectedParticipantsModels.size() > 0) {
            selectedUsersStateNeedToBeSaved = true;
          }
          fetchLatestParticipantsToAddToConversation(false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    ismActivityAddParticipantsBinding.ivNext.setOnClickListener(v -> {

      if (selectedParticipantsModels.isEmpty()) {
        Toast.makeText(this, R.string.ism_atleast_one_participant, Toast.LENGTH_SHORT).show();
      } else {

        int size = selectedParticipantsModels.size();
        String message = (size == 1) ? getString(R.string.ism_add_participant_confirmation)
            : getString(R.string.ism_add_participants_confirmation, size);

        new AlertDialog.Builder(this).setTitle(getString(R.string.ism_add_participants)).setMessage(message)
            .setCancelable(true)
            .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

              dialog.cancel();
              showProgressDialog(getString(R.string.ism_adding_participants));
              addParticipantsPresenter.addMembers(selectedParticipantsModels);
            })
            .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
            .create()
            .show();
      }
    });
    ismActivityAddParticipantsBinding.ibBack.setOnClickListener(v -> onBackPressed());

    ismActivityAddParticipantsBinding.refresh.setOnRefreshListener(
        () -> fetchLatestParticipantsToAddToConversation(false, null, true));

  }

  @Override
  public void onMembersAddedSuccessfully() {
    hideProgressDialog();
    Intent returnIntent = new Intent();
    setResult(Activity.RESULT_OK, returnIntent);
    supportFinishAfterTransition();
  }

  @Override
  public void onMembersToAddToConversationFetched(ArrayList<ParticipantsModel> participantsModels,
      boolean latestUsers, boolean isSearchRequest) {
    if (latestUsers) {
      this.participantsModels.clear();

      if (isSearchRequest || selectedUsersStateNeedToBeSaved) {
        int size = participantsModels.size();
        ParticipantsModel participantsModel;

        for (int i = 0; i < size; i++) {

          for (int j = 0; j < selectedParticipantsModels.size(); j++) {
            if (selectedParticipantsModels.get(j)
                .getUserId()
                .equals(participantsModels.get(i).getUserId())) {
              participantsModel = participantsModels.get(i);
              participantsModel.setSelected(true);
              participantsModels.set(i, participantsModel);
              break;
            }
          }
        }
        if (!isSearchRequest) selectedUsersStateNeedToBeSaved = false;
      } else {

        runOnUiThread(() -> {
          selectedParticipantsModels.clear();
          count = 0;
          selectedParticipantsAdapter.notifyDataSetChanged();
          updateSelectedMembersText();
        });
      }
    } else {
      int size = participantsModels.size();
      ParticipantsModel participantsModel;

      for (int i = 0; i < size; i++) {

        for (int j = 0; j < selectedParticipantsModels.size(); j++) {
          if (selectedParticipantsModels.get(j)
              .getUserId()
              .equals(participantsModels.get(i).getUserId())) {
            participantsModel = participantsModels.get(i);
            participantsModel.setSelected(true);
            participantsModels.set(i, participantsModel);
            break;
          }
        }
      }
    }

    this.participantsModels.addAll(participantsModels);
    runOnUiThread(() -> {
      if (AddParticipantsActivity.this.participantsModels.size() > 0) {
        ismActivityAddParticipantsBinding.tvNoUsers.setVisibility(View.GONE);
        ismActivityAddParticipantsBinding.rvUsers.setVisibility(View.VISIBLE);
        participantsAdapter.notifyDataSetChanged();
      } else {
        ismActivityAddParticipantsBinding.tvNoUsers.setVisibility(View.VISIBLE);
        ismActivityAddParticipantsBinding.rvUsers.setVisibility(View.GONE);
      }
      selectedParticipantsAdapter.notifyDataSetChanged();

      hideProgressDialog();
      if (ismActivityAddParticipantsBinding.refresh.isRefreshing()) {
        ismActivityAddParticipantsBinding.refresh.setRefreshing(false);
      }

      updateShimmerVisibility(false);
    });
  }

  @Override
  public void onError(String errorMessage) {
    if (ismActivityAddParticipantsBinding.refresh.isRefreshing()) {
      ismActivityAddParticipantsBinding.refresh.setRefreshing(false);
    }

    hideProgressDialog();
    updateShimmerVisibility(false);
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(AddParticipantsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(AddParticipantsActivity.this, getString(R.string.ism_error),
            Toast.LENGTH_SHORT).show();
      }
    });
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

          addParticipantsPresenter.requestMembersToAddToConversationOnScroll(
              participantsLayoutManager.findFirstVisibleItemPosition(),
              participantsLayoutManager.getChildCount(), participantsLayoutManager.getItemCount());
        }
      };

  /**
   * Remove participant.
   *
   * @param userId the user id
   */
  public void removeParticipant(String userId) {
    int size = participantsModels.size();
    for (int i = 0; i < size; i++) {

      if (participantsModels.get(i).getUserId().equals(userId)) {

        ParticipantsModel selectParticipantsModel = participantsModels.get(i);
        selectParticipantsModel.setSelected(false);
        participantsModels.set(i, selectParticipantsModel);
        if (i == 0) {
          participantsAdapter.notifyDataSetChanged();
        } else {
          participantsAdapter.notifyItemChanged(i);
        }
        count--;
        updateSelectedMembersText();
        break;
      }
    }

    for (int i = 0; i < selectedParticipantsModels.size(); i++) {

      if (selectedParticipantsModels.get(i).getUserId().equals(userId)) {
        selectedParticipantsModels.remove(i);
        if (i == 0) {
          selectedParticipantsAdapter.notifyDataSetChanged();
        } else {
          selectedParticipantsAdapter.notifyItemRemoved(i);
        }

        break;
      }
    }
  }

  private void fetchLatestParticipantsToAddToConversation(boolean isSearchRequest, String searchTag,
      boolean showProgressDialog) {
    if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_participants));

    try {
      addParticipantsPresenter.fetchMembersToAddToConversation(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void removeSelectedParticipant(String userId) {

    for (int i = 0; i < selectedParticipantsModels.size(); i++) {
      if (selectedParticipantsModels.get(i).getUserId().equals(userId)) {
        selectedParticipantsModels.remove(i);
        if (i == 0) {
          selectedParticipantsAdapter.notifyDataSetChanged();
        } else {
          selectedParticipantsAdapter.notifyItemRemoved(i);
        }
        break;
      }
    }
  }

  private void addSelectedParticipant(ParticipantsModel selectMembersModel) {
    selectedParticipantsModels.add(selectMembersModel);
    try {
      selectedParticipantsAdapter.notifyDataSetChanged();
      selectedParticipantsLayoutManager.scrollToPosition(selectedParticipantsModels.size() - 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateSelectedMembersText() {

    if (count > 0) {
      ismActivityAddParticipantsBinding.tvMembersCount.setText(
          getString(R.string.ism_number_of_participants_selected, String.valueOf(count),
              String.valueOf(MAXIMUM_MEMBERS)));
    } else {
      ismActivityAddParticipantsBinding.tvMembersCount.setText(
          getString(R.string.ism_add_participants));
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
      ismActivityAddParticipantsBinding.shimmerFrameLayout.startShimmer();
      ismActivityAddParticipantsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismActivityAddParticipantsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismActivityAddParticipantsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismActivityAddParticipantsBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
