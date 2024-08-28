package io.isometrik.ui.messages.forward.people;

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
import io.isometrik.chat.databinding.IsmFragmentConversationsPeopleBinding;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.Constants;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The fragment to fetch list of users to forward a message to, with paging, search and pull to
 * refresh option.
 */
public class ForwardToPeopleFragment extends Fragment implements ForwardToPeopleContract.View {

  private ForwardToPeopleContract.Presenter forwardToPeoplePresenter;
  private IsmFragmentConversationsPeopleBinding ismFragmentConversationsPeopleBinding;

  private final ArrayList<PeopleModel> people = new ArrayList<>();
  private final ArrayList<PeopleModel> selectedPeople = new ArrayList<>();

  private ForwardToPeopleAdapter forwardToPeopleAdapter;
  private SelectedPeopleAdapter selectedPeopleAdapter;

  private LinearLayoutManager peopleLayoutManager;
  private LinearLayoutManager selectedPeopleLayoutManager;

  private int count;
  private final int MAXIMUM_PEOPLE =
      Constants.BROADCAST_FORWARD_CONVERSATIONS_PEOPLE_SIZE_AT_A_TIME;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private boolean selectedUsersStateNeedToBeSaved = false;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    forwardToPeoplePresenter = new ForwardToPeoplePresenter();

    forwardToPeoplePresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismFragmentConversationsPeopleBinding =
        IsmFragmentConversationsPeopleBinding.inflate(inflater, container, false);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    ismFragmentConversationsPeopleBinding.tvAddConversationPeople.setText(
        getString(R.string.ism_select_people));

    peopleLayoutManager = new LinearLayoutManager(getActivity());
    ismFragmentConversationsPeopleBinding.rvConversationsPeople.setLayoutManager(
        peopleLayoutManager);
    forwardToPeopleAdapter = new ForwardToPeopleAdapter(getActivity(), people);
    ismFragmentConversationsPeopleBinding.rvConversationsPeople.addOnScrollListener(
        peopleScrollListener);
    ismFragmentConversationsPeopleBinding.rvConversationsPeople.setAdapter(forwardToPeopleAdapter);

    selectedPeopleLayoutManager =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
    ismFragmentConversationsPeopleBinding.rvConversationPeopleSelected.setLayoutManager(
        selectedPeopleLayoutManager);
    selectedPeopleAdapter = new SelectedPeopleAdapter(getActivity(), selectedPeople, this);
    ismFragmentConversationsPeopleBinding.rvConversationPeopleSelected.setAdapter(
        selectedPeopleAdapter);

    fetchPeople(false, null, false);

    ismFragmentConversationsPeopleBinding.rvConversationsPeople.addOnItemTouchListener(
        new RecyclerItemClickListener(getActivity(),
            ismFragmentConversationsPeopleBinding.rvConversationsPeople,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {

                  PeopleModel peopleModel = people.get(position);

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
                      Toast.makeText(getActivity(),
                          getString(R.string.ism_forward_max_people_limit_reached),
                          Toast.LENGTH_SHORT).show();
                    }
                  }
                  updateSelectedPeopleText();
                  people.set(position, peopleModel);
                  forwardToPeopleAdapter.notifyItemChanged(position);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismFragmentConversationsPeopleBinding.refresh.setOnRefreshListener(
        () -> fetchPeople(false, null, true));

    ismFragmentConversationsPeopleBinding.etSearch.addTextChangedListener(new TextWatcher() {
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
    return ismFragmentConversationsPeopleBinding.getRoot();
  }

  @Override
  public void onDestroy() {

    super.onDestroy();
    forwardToPeoplePresenter.detachView();
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    ismFragmentConversationsPeopleBinding = null;
  }

  private final RecyclerView.OnScrollListener peopleScrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      forwardToPeoplePresenter.fetchPeopleOnScroll(
          peopleLayoutManager.findFirstVisibleItemPosition(), peopleLayoutManager.getChildCount(),
          peopleLayoutManager.getItemCount());
    }
  };

  private void fetchPeople(boolean isSearchRequest, String searchTag, boolean showProgressDialog) {
    if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_people));

    try {
      forwardToPeoplePresenter.fetchPeople(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onPeopleFetchedSuccessfully(ArrayList<PeopleModel> peopleModels,
      boolean refreshRequest, boolean isSearchRequest) {
    if (refreshRequest) {
      people.clear();

      if (isSearchRequest || selectedUsersStateNeedToBeSaved) {
        int size = peopleModels.size();
        PeopleModel peopleModel;

        for (int i = 0; i < size; i++) {

          for (int j = 0; j < selectedPeople.size(); j++) {
            if (selectedPeople.get(j).getUserId().equals(peopleModels.get(i).getUserId())) {
              peopleModel = peopleModels.get(i);
              peopleModel.setSelected(true);
              peopleModels.set(i, peopleModel);
              break;
            }
          }
        }
        if (!isSearchRequest) selectedUsersStateNeedToBeSaved = false;
      } else {
        if (getActivity() != null) {
          getActivity().
              runOnUiThread(() -> {
                selectedPeople.clear();
                count = 0;
                selectedPeopleAdapter.notifyDataSetChanged();
                updateSelectedPeopleText();
              });
        }
      }
    } else {

      int size = peopleModels.size();
      PeopleModel peopleModel;

      for (int i = 0; i < size; i++) {

        for (int j = 0; j < selectedPeople.size(); j++) {
          if (selectedPeople.get(j).getUserId().equals(peopleModels.get(i).getUserId())) {
            peopleModel = peopleModels.get(i);
            peopleModel.setSelected(true);
            peopleModels.set(i, peopleModel);
            break;
          }
        }
      }
    }

    people.addAll(peopleModels);

    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (refreshRequest) {
          if (people.size() > 0) {
            ismFragmentConversationsPeopleBinding.tvNoConversationsPeople.setVisibility(View.GONE);
            ismFragmentConversationsPeopleBinding.rvConversationsPeople.setVisibility(View.VISIBLE);
          } else {
            ismFragmentConversationsPeopleBinding.tvNoConversationsPeople.setVisibility(
                View.VISIBLE);
            ismFragmentConversationsPeopleBinding.tvNoConversationsPeople.setText(
                getString(R.string.ism_forward_no_people));
            ismFragmentConversationsPeopleBinding.rvConversationsPeople.setVisibility(View.GONE);
          }
        }
        forwardToPeopleAdapter.notifyDataSetChanged();

        hideProgressDialog();
        if (ismFragmentConversationsPeopleBinding.refresh.isRefreshing()) {
          ismFragmentConversationsPeopleBinding.refresh.setRefreshing(false);
        }
        updateShimmerVisibility(false);
      });
    }
  }

  @Override
  public void onError(String errorMessage) {
    if (ismFragmentConversationsPeopleBinding.refresh.isRefreshing()) {
      ismFragmentConversationsPeopleBinding.refresh.setRefreshing(false);
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

  /**
   * Remove people.
   *
   * @param userId the user id
   */
  public void removePeople(String userId) {
    int size = people.size();
    for (int i = 0; i < size; i++) {

      if (people.get(i).getUserId().equals(userId)) {

        PeopleModel peopleModel = people.get(i);
        peopleModel.setSelected(false);
        people.set(i, peopleModel);
        if (i == 0) {
          forwardToPeopleAdapter.notifyDataSetChanged();
        } else {
          forwardToPeopleAdapter.notifyItemChanged(i);
        }
        count--;
        updateSelectedPeopleText();
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

  private void addSelectedPeople(PeopleModel peopleModel) {
    selectedPeople.add(peopleModel);
    try {
      selectedPeopleAdapter.notifyDataSetChanged();
      selectedPeopleLayoutManager.scrollToPosition(selectedPeople.size() - 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateSelectedPeopleText() {

    if (count > 0) {
      ismFragmentConversationsPeopleBinding.tvAddConversationPeople.setText(
          getString(R.string.ism_number_of_people_selected, String.valueOf(count),
              String.valueOf(MAXIMUM_PEOPLE)));
    } else {
      ismFragmentConversationsPeopleBinding.tvAddConversationPeople.setText(
          getString(R.string.ism_select_people));
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
   * Fetch selected people array list.
   *
   * @return the array list
   */
  public ArrayList<String> fetchSelectedPeople() {
    ArrayList<String> people = new ArrayList<>();
    for (int i = 0; i < selectedPeople.size(); i++) {
      people.add(selectedPeople.get(i).getUserId());
    }
    return people;
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismFragmentConversationsPeopleBinding.shimmerFrameLayout.startShimmer();
      ismFragmentConversationsPeopleBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismFragmentConversationsPeopleBinding.shimmerFrameLayout.getVisibility()
          == View.VISIBLE) {
        ismFragmentConversationsPeopleBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismFragmentConversationsPeopleBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
