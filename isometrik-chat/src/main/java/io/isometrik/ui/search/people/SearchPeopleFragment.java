package io.isometrik.ui.search.people;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.chat.enums.ConversationType;
import io.isometrik.chat.R;
import io.isometrik.ui.conversations.newconversation.group.NewGroupConversationActivity;
import io.isometrik.ui.conversations.newconversation.onetoone.NewOneToOneConversationActivity;
import io.isometrik.chat.databinding.IsmFragmentSearchBinding;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The fragment to search for non-blocked users and fetch results with paging and pull to refresh option.Code to show popup to create new conversation with a user.
 */
public class SearchPeopleFragment extends Fragment implements SearchPeopleContract.View {

  private SearchPeopleContract.Presenter searchPeopleContractPresenter;
  private IsmFragmentSearchBinding ismFragmentSearchBinding;

  private final ArrayList<SearchPeopleModel> people = new ArrayList<>();

  private SearchPeopleAdapter searchPeopleAdapter;

  private LinearLayoutManager peopleLayoutManager;
  private AlertDialog alertDialog;
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    searchPeopleContractPresenter = new SearchPeoplePresenter();

    searchPeopleContractPresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismFragmentSearchBinding = IsmFragmentSearchBinding.inflate(inflater, container, false);
    updateShimmerVisibility(true);
    peopleLayoutManager = new LinearLayoutManager(getActivity());
    ismFragmentSearchBinding.rvSearchItems.setLayoutManager(peopleLayoutManager);
    searchPeopleAdapter = new SearchPeopleAdapter(getActivity(), people);
    ismFragmentSearchBinding.rvSearchItems.addOnScrollListener(peopleScrollListener);
    ismFragmentSearchBinding.rvSearchItems.setAdapter(searchPeopleAdapter);

    fetchPeople(false, null);
    ismFragmentSearchBinding.rvSearchItems.addOnItemTouchListener(
        new RecyclerItemClickListener(getActivity(), ismFragmentSearchBinding.rvSearchItems,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {
                  if (getActivity() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = (getActivity()).getLayoutInflater();

                    builder.setTitle(getString(R.string.ism_new_conversation_title));
                    builder.setCancelable(true);
                    View dialogView =
                        inflater.inflate(R.layout.ism_dialog_create_conversation_options, null);

                    SearchPeopleModel searchPeopleModel = people.get(position);

                    TextView tvNewPrivateOneToOneConversation =
                        dialogView.findViewById(R.id.tvNewPrivateOneToOneConversation);
                    TextView tvNewPrivateGroupConversation =
                        dialogView.findViewById(R.id.tvNewPrivateGroupConversation);
                    TextView tvNewPublicConversation =
                        dialogView.findViewById(R.id.tvNewPublicConversation);
                    TextView tvNewOpenConversation =
                        dialogView.findViewById(R.id.tvNewOpenConversation);
                    tvNewPrivateOneToOneConversation.setText(
                        getString(R.string.ism_user_new_private_one_to_one_conversation,
                            searchPeopleModel.getUserName()));
                    tvNewPrivateGroupConversation.setText(
                        getString(R.string.ism_user_new_private_group_conversation,
                            searchPeopleModel.getUserName()));
                    tvNewPublicConversation.setText(
                        getString(R.string.ism_user_new_public_group_conversation,
                            searchPeopleModel.getUserName()));
                    tvNewOpenConversation.setText(getString(R.string.ism_user_new_open_conversation,
                        searchPeopleModel.getUserName()));

                    RelativeLayout rlNewPrivateOneToOneConversation =
                        dialogView.findViewById(R.id.rlNewPrivateOneToOneConversation);
                    RelativeLayout rlNewPrivateGroupConversation =
                        dialogView.findViewById(R.id.rlNewPrivateGroupConversation);
                    RelativeLayout rlNewPublicConversation =
                        dialogView.findViewById(R.id.rlNewPublicConversation);
                    RelativeLayout rlNewOpenConversation =
                        dialogView.findViewById(R.id.rlNewOpenConversation);
                    rlNewPrivateOneToOneConversation.setOnClickListener(
                        v -> createConversation(searchPeopleModel,
                            ConversationType.PrivateConversation.getValue(), true));
                    rlNewPrivateGroupConversation.setOnClickListener(
                        v -> createConversation(searchPeopleModel,
                            ConversationType.PrivateConversation.getValue(), false));
                    rlNewPublicConversation.setOnClickListener(
                        v -> createConversation(searchPeopleModel,
                            ConversationType.PublicConversation.getValue(), false));
                    rlNewOpenConversation.setOnClickListener(
                        v -> createConversation(searchPeopleModel,
                            ConversationType.OpenConversation.getValue(), false));
                    builder.setView(dialogView);

                    alertDialog = builder.create();
                    alertDialog.show();
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));
    ismFragmentSearchBinding.refresh.setOnRefreshListener(() ->

        fetchPeople(false, null));

    return ismFragmentSearchBinding.getRoot();
  }

  @Override
  public void onDestroy() {

    super.onDestroy();
    searchPeopleContractPresenter.detachView();
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    ismFragmentSearchBinding = null;
  }

  private final RecyclerView.OnScrollListener peopleScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          searchPeopleContractPresenter.fetchSearchedPeopleOnScroll(
              peopleLayoutManager.findFirstVisibleItemPosition(),
              peopleLayoutManager.getChildCount(), peopleLayoutManager.getItemCount());
        }
      };

  /**
   * Fetch people.
   *
   * @param isSearchRequest the is search request
   * @param searchTag the search tag
   */
  public void fetchPeople(boolean isSearchRequest, String searchTag) {

    try {
      searchPeopleContractPresenter.fetchSearchedPeople(0, true, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onSearchedPeopleFetchedSuccessfully(ArrayList<SearchPeopleModel> peopleModels,
      boolean refreshRequest) {
    if (refreshRequest) {
      people.clear();
    }

    people.addAll(peopleModels);

    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (refreshRequest) {
          if (people.size() > 0) {
            ismFragmentSearchBinding.tvNoSearchItems.setVisibility(View.GONE);
            ismFragmentSearchBinding.rvSearchItems.setVisibility(View.VISIBLE);
          } else {
            ismFragmentSearchBinding.tvNoSearchItems.setVisibility(View.VISIBLE);
            ismFragmentSearchBinding.tvNoSearchItems.setText(
                getString(R.string.ism_search_no_people));
            ismFragmentSearchBinding.rvSearchItems.setVisibility(View.GONE);
          }
        }
        searchPeopleAdapter.notifyDataSetChanged();
        if (ismFragmentSearchBinding.refresh.isRefreshing()) {
          ismFragmentSearchBinding.refresh.setRefreshing(false);
        }
        updateShimmerVisibility(false);
      });
    }
  }

  @Override
  public void onError(String errorMessage) {
    if (ismFragmentSearchBinding.refresh.isRefreshing()) {
      ismFragmentSearchBinding.refresh.setRefreshing(false);
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

  private void createConversation(SearchPeopleModel searchPeopleModel, int conversationType,
      boolean oneToOneConversation) {
    if (getActivity() != null) {
      if (alertDialog != null && alertDialog.isShowing()) {
        alertDialog.dismiss();
      }
      Intent intent;

      if (oneToOneConversation) {
        intent = new Intent(getActivity(), NewOneToOneConversationActivity.class);
        intent.putExtra("lastSeenAt", searchPeopleModel.getLastSeenAt());
      } else {
        intent = new Intent(getActivity(), NewGroupConversationActivity.class);
        intent.putExtra("conversationType", conversationType);
      }
      intent.putExtra("userId", searchPeopleModel.getUserId());
      intent.putExtra("userProfileImageUrl", searchPeopleModel.getUserProfileImageUrl());
      intent.putExtra("userName", searchPeopleModel.getUserName());
      intent.putExtra("isOnline", searchPeopleModel.isOnline());

      startActivity(intent);
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismFragmentSearchBinding.shimmerFrameLayoutPeople.startShimmer();
      ismFragmentSearchBinding.shimmerFrameLayoutPeople.setVisibility(View.VISIBLE);
    } else {
      if (ismFragmentSearchBinding.shimmerFrameLayoutPeople.getVisibility() == View.VISIBLE) {
        ismFragmentSearchBinding.shimmerFrameLayoutPeople.setVisibility(View.GONE);
        ismFragmentSearchBinding.shimmerFrameLayoutPeople.stopShimmer();
      }
    }
  }
}
