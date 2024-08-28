package io.isometrik.ui.messages.forward.conversations;

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
 * The fragment to fetch list of conversations to forward a message to, with paging, search and pull
 * to refresh option.
 */
public class ForwardInConversationFragment extends Fragment
    implements ForwardInConversationContract.View {

  private ForwardInConversationContract.Presenter forwardInConversationPresenter;
  private IsmFragmentConversationsPeopleBinding ismFragmentConversationsPeopleBinding;

  private final ArrayList<ForwardInConversationModel> conversations = new ArrayList<>();
  private final ArrayList<ForwardInConversationModel> selectedConversations = new ArrayList<>();

  private ForwardInConversationAdapter forwardInConversationAdapter;
  private SelectedConversationsAdapter selectedConversationsAdapter;

  private LinearLayoutManager conversationsLayoutManager;
  private LinearLayoutManager selectedConversationsLayoutManager;

  private int count;
  private final int MAXIMUM_CONVERSATIONS = Constants.BROADCAST_FORWARD_CONVERSATIONS_PEOPLE_SIZE_AT_A_TIME;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;
  private boolean selectedConversationsStateNeedToBeSaved = false;
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    forwardInConversationPresenter = new ForwardInConversationPresenter();

    forwardInConversationPresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismFragmentConversationsPeopleBinding =
        IsmFragmentConversationsPeopleBinding.inflate(inflater, container, false);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    ismFragmentConversationsPeopleBinding.tvAddConversationPeople.setText(
        getString(R.string.ism_select_conversations));
    ismFragmentConversationsPeopleBinding.etSearch.setHint(getString(R.string.ism_search_conversations));
    conversationsLayoutManager = new LinearLayoutManager(getActivity());
    ismFragmentConversationsPeopleBinding.rvConversationsPeople.setLayoutManager(
        conversationsLayoutManager);
    forwardInConversationAdapter = new ForwardInConversationAdapter(getActivity(), conversations);
    ismFragmentConversationsPeopleBinding.rvConversationsPeople.addOnScrollListener(
        conversationsScrollListener);
    ismFragmentConversationsPeopleBinding.rvConversationsPeople.setAdapter(
        forwardInConversationAdapter);

    selectedConversationsLayoutManager =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
    ismFragmentConversationsPeopleBinding.rvConversationPeopleSelected.setLayoutManager(
        selectedConversationsLayoutManager);
    selectedConversationsAdapter =
        new SelectedConversationsAdapter(getActivity(), selectedConversations, this);
    ismFragmentConversationsPeopleBinding.rvConversationPeopleSelected.setAdapter(
        selectedConversationsAdapter);

    fetchConversations(false, null, false);

    ismFragmentConversationsPeopleBinding.rvConversationsPeople.addOnItemTouchListener(
        new RecyclerItemClickListener(getActivity(),
            ismFragmentConversationsPeopleBinding.rvConversationsPeople,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {

                  ForwardInConversationModel forwardInConversationModel =
                      conversations.get(position);

                  if (forwardInConversationModel.isSelected()) {
                    forwardInConversationModel.setSelected(false);
                    count--;
                    removeSelectedConversation(forwardInConversationModel.getConversationId());
                  } else {

                    if (count < MAXIMUM_CONVERSATIONS) {
                      //Maximum 100 conversations can be added
                      forwardInConversationModel.setSelected(true);
                      count++;
                      addSelectedConversation(forwardInConversationModel);
                    } else {
                      Toast.makeText(getActivity(),
                          getString(R.string.ism_forward_max_conversations_limit_reached),
                          Toast.LENGTH_SHORT).show();
                    }
                  }
                  updateSelectedConversationsText();
                  conversations.set(position, forwardInConversationModel);
                  forwardInConversationAdapter.notifyItemChanged(position);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismFragmentConversationsPeopleBinding.refresh.setOnRefreshListener(
        () -> fetchConversations(false, null, true));

    ismFragmentConversationsPeopleBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchConversations(true, s.toString(), false);
        } else {
          if (selectedConversations.size() > 0) {
            selectedConversationsStateNeedToBeSaved = true;
          }
          fetchConversations(false, null, false);
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
    forwardInConversationPresenter.detachView();
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    ismFragmentConversationsPeopleBinding = null;
  }

  private final RecyclerView.OnScrollListener conversationsScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          forwardInConversationPresenter.fetchConversationsOnScroll(
              conversationsLayoutManager.findFirstVisibleItemPosition(),
              conversationsLayoutManager.getChildCount(),
              conversationsLayoutManager.getItemCount());
        }
      };

  private void fetchConversations(boolean isSearchRequest, String searchTag,
      boolean showProgressDialog) {

    if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_conversations));

    try {
      forwardInConversationPresenter.fetchConversations(0, false, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onConversationsFetchedSuccessfully(
      ArrayList<ForwardInConversationModel> conversationModels, boolean resultsOnScroll,
      boolean isSearchRequest) {

    if (resultsOnScroll) {

      int size = conversationModels.size();
      ForwardInConversationModel forwardInConversationModel;

      for (int i = 0; i < size; i++) {

        for (int j = 0; j < selectedConversations.size(); j++) {
          if (selectedConversations.get(j)
              .getConversationId()
              .equals(conversationModels.get(i).getConversationId())) {
            forwardInConversationModel = conversationModels.get(i);
            forwardInConversationModel.setSelected(true);
            conversationModels.set(i, forwardInConversationModel);
            break;
          }
        }
      }
    } else {
      conversations.clear();

      if (isSearchRequest || selectedConversationsStateNeedToBeSaved) {
        int size = conversationModels.size();
        ForwardInConversationModel forwardInConversationModel;

        for (int i = 0; i < size; i++) {

          for (int j = 0; j < selectedConversations.size(); j++) {
            if (selectedConversations.get(j)
                .getConversationId()
                .equals(conversationModels.get(i).getConversationId())) {
              forwardInConversationModel = conversationModels.get(i);
              forwardInConversationModel.setSelected(true);
              conversationModels.set(i, forwardInConversationModel);
              break;
            }
          }
        }
        if (!isSearchRequest) selectedConversationsStateNeedToBeSaved = false;
      } else {
        if (getActivity() != null) {
          getActivity().
              runOnUiThread(() -> {
                selectedConversations.clear();
                count = 0;
                selectedConversationsAdapter.notifyDataSetChanged();
                updateSelectedConversationsText();
              });
        }
      }
    }
    conversations.addAll(conversationModels);
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (conversations.size() > 0) {
            ismFragmentConversationsPeopleBinding.tvNoConversationsPeople.setVisibility(View.GONE);
            ismFragmentConversationsPeopleBinding.rvConversationsPeople.setVisibility(View.VISIBLE);
          } else {
            ismFragmentConversationsPeopleBinding.tvNoConversationsPeople.setVisibility(
                View.VISIBLE);
            ismFragmentConversationsPeopleBinding.tvNoConversationsPeople.setText(
                getString(R.string.ism_forward_no_conversation));
            ismFragmentConversationsPeopleBinding.rvConversationsPeople.setVisibility(View.GONE);
          }
        }
        forwardInConversationAdapter.notifyDataSetChanged();
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
   * Remove conversation.
   *
   * @param conversationId the conversation id
   */
  public void removeConversation(String conversationId) {
    int size = conversations.size();
    for (int i = 0; i < size; i++) {

      if (conversations.get(i).getConversationId().equals(conversationId)) {

        ForwardInConversationModel forwardInConversationModel = conversations.get(i);
        forwardInConversationModel.setSelected(false);
        conversations.set(i, forwardInConversationModel);
        if (i == 0) {
          forwardInConversationAdapter.notifyDataSetChanged();
        } else {
          forwardInConversationAdapter.notifyItemChanged(i);
        }
        count--;
        updateSelectedConversationsText();
        break;
      }
    }

    for (int i = 0; i < selectedConversations.size(); i++) {

      if (selectedConversations.get(i).getConversationId().equals(conversationId)) {
        selectedConversations.remove(i);
        if (i == 0) {
          selectedConversationsAdapter.notifyDataSetChanged();
        } else {
          selectedConversationsAdapter.notifyItemRemoved(i);
        }

        break;
      }
    }
  }

  private void removeSelectedConversation(String conversationId) {

    for (int i = 0; i < selectedConversations.size(); i++) {
      if (selectedConversations.get(i).getConversationId().equals(conversationId)) {
        selectedConversations.remove(i);
        if (i == 0) {
          selectedConversationsAdapter.notifyDataSetChanged();
        } else {
          selectedConversationsAdapter.notifyItemRemoved(i);
        }
        break;
      }
    }
  }

  private void addSelectedConversation(ForwardInConversationModel forwardInConversationModel) {
    selectedConversations.add(forwardInConversationModel);
    try {
      selectedConversationsAdapter.notifyDataSetChanged();
      selectedConversationsLayoutManager.scrollToPosition(selectedConversations.size() - 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateSelectedConversationsText() {

    if (count > 0) {
      ismFragmentConversationsPeopleBinding.tvAddConversationPeople.setText(
          getString(R.string.ism_number_of_conversations_selected, String.valueOf(count),
              String.valueOf(MAXIMUM_CONVERSATIONS)));
    } else {
      ismFragmentConversationsPeopleBinding.tvAddConversationPeople.setText(
          getString(R.string.ism_select_conversations));
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
   * Fetch selected conversations array list.
   *
   * @return the array list
   */
  public ArrayList<String> fetchSelectedConversations() {
    ArrayList<String> conversationIds = new ArrayList<>();
    for (int i = 0; i < selectedConversations.size(); i++) {
      conversationIds.add(selectedConversations.get(i).getConversationId());
    }
    return conversationIds;
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