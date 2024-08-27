package io.isometrik.ui.search.messages;

import android.content.Intent;
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
import io.isometrik.chat.databinding.IsmFragmentSearchBinding;
import io.isometrik.ui.messages.chat.ConversationMessagesActivity;
import io.isometrik.ui.messages.chat.MessagesModel;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The fragment to search for messages across all conversations and fetch results with paging and pull to refresh option.
 */
public class SearchMessagesFragment extends Fragment implements SearchMessagesContract.View {

  private final ArrayList<MessagesModel> messages = new ArrayList<>();
  private SearchMessagesContract.Presenter searchMessagesContractPresenter;
  private IsmFragmentSearchBinding ismFragmentSearchBinding;
  private SearchMessagesAdapter searchMessagesAdapter;

  private LinearLayoutManager messagesLayoutManager;
  private final RecyclerView.OnScrollListener messagesScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          searchMessagesContractPresenter.fetchSearchedMessagesOnScroll(
              messagesLayoutManager.findFirstVisibleItemPosition(),
              messagesLayoutManager.getChildCount(), messagesLayoutManager.getItemCount());
        }
      };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    searchMessagesContractPresenter = new SearchMessagesPresenter();

    searchMessagesContractPresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismFragmentSearchBinding =
        IsmFragmentSearchBinding.inflate(inflater, container,
            false);
    updateShimmerVisibility(true);
    messagesLayoutManager = new LinearLayoutManager(getActivity());
    ismFragmentSearchBinding.rvSearchItems.setLayoutManager(messagesLayoutManager);
    searchMessagesAdapter = new SearchMessagesAdapter(getActivity(), messages);
    ismFragmentSearchBinding.rvSearchItems.addOnScrollListener(messagesScrollListener);
    ismFragmentSearchBinding.rvSearchItems.setAdapter(searchMessagesAdapter);

    fetchMessages(false, null);
    ismFragmentSearchBinding.rvSearchItems.addOnItemTouchListener(
        new RecyclerItemClickListener(getActivity(), ismFragmentSearchBinding.rvSearchItems,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {
                  if (getActivity() != null) {
                    MessagesModel messagesModel = messages.get(position);

                    Intent intent = new Intent(getActivity(), ConversationMessagesActivity.class);
                    intent.putExtra("scrollToMessageNeeded", true);
                    intent.putExtra("conversationId", messagesModel.getConversationId());
                    intent.putExtra("messageId", messagesModel.getMessageId());
                    intent.putExtra("isPrivateOneToOne", messagesModel.isPrivateOneToOne());
                    if (messagesModel.isMessagingDisabled()) {
                      intent.putExtra("messagingDisabled", true);
                    }
                    startActivity(intent);
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));
    ismFragmentSearchBinding.refresh.setOnRefreshListener(() -> fetchMessages(false, null));

    return ismFragmentSearchBinding.getRoot();
  }

  @Override
  public void onDestroy() {

    super.onDestroy();
    searchMessagesContractPresenter.detachView();
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    ismFragmentSearchBinding = null;
  }

  /**
   * Fetch messages.
   *
   * @param isSearchRequest the is search request
   * @param searchTag the search tag
   */
  public void fetchMessages(boolean isSearchRequest, String searchTag) {

    try {
      searchMessagesContractPresenter.fetchSearchedMessages(0, false, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onSearchedMessagesFetchedSuccessfully(ArrayList<MessagesModel> messagesModels,
      boolean resultsOnScroll) {
    if (!resultsOnScroll) {
      messages.clear();
    }

    messages.addAll(messagesModels);

    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (messages.size() > 0) {
            ismFragmentSearchBinding.tvNoSearchItems.setVisibility(View.GONE);
            ismFragmentSearchBinding.rvSearchItems.setVisibility(View.VISIBLE);
          } else {
            ismFragmentSearchBinding.tvNoSearchItems.setVisibility(View.VISIBLE);
            ismFragmentSearchBinding.tvNoSearchItems.setText(
                getString(R.string.ism_search_no_messages));
            ismFragmentSearchBinding.rvSearchItems.setVisibility(View.GONE);
          }
        }
        searchMessagesAdapter.notifyDataSetChanged();
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

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismFragmentSearchBinding.shimmerFrameLayoutMessages.startShimmer();
      ismFragmentSearchBinding.shimmerFrameLayoutMessages.setVisibility(View.VISIBLE);
    } else {
      if (ismFragmentSearchBinding.shimmerFrameLayoutMessages.getVisibility() == View.VISIBLE) {
        ismFragmentSearchBinding.shimmerFrameLayoutMessages.setVisibility(View.GONE);
        ismFragmentSearchBinding.shimmerFrameLayoutMessages.stopShimmer();
      }
    }
  }
}
