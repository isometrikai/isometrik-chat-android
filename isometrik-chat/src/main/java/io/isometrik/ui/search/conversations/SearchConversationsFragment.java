package io.isometrik.ui.search.conversations;

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
import io.isometrik.ui.conversations.list.ConversationsModel;
import io.isometrik.ui.messages.chat.ConversationMessagesActivity;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The fragment to search for conversations and fetch results with paging and pull to refresh option.
 */
public class SearchConversationsFragment extends Fragment
    implements SearchConversationsContract.View {

  private final ArrayList<ConversationsModel> conversations = new ArrayList<>();
  private SearchConversationsContract.Presenter searchConversationsContractPresenter;
  private IsmFragmentSearchBinding ismFragmentSearchBinding;
  private SearchConversationsAdapter searchConversationsAdapter;

  private LinearLayoutManager conversationsLayoutManager;
  private final RecyclerView.OnScrollListener conversationsScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          searchConversationsContractPresenter.fetchSearchedConversationsOnScroll(
              conversationsLayoutManager.findFirstVisibleItemPosition(),
              conversationsLayoutManager.getChildCount(),
              conversationsLayoutManager.getItemCount());
        }
      };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    searchConversationsContractPresenter = new SearchConversationsPresenter();

    searchConversationsContractPresenter.attachView(this);
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismFragmentSearchBinding =
        IsmFragmentSearchBinding.inflate(inflater, container,
            false);
    updateShimmerVisibility(true);
    conversationsLayoutManager = new LinearLayoutManager(getActivity());
    ismFragmentSearchBinding.rvSearchItems.setLayoutManager(conversationsLayoutManager);
    searchConversationsAdapter = new SearchConversationsAdapter(getActivity(), conversations);
    ismFragmentSearchBinding.rvSearchItems.addOnScrollListener(conversationsScrollListener);
    ismFragmentSearchBinding.rvSearchItems.setAdapter(searchConversationsAdapter);

    fetchConversations(false, null);
    ismFragmentSearchBinding.rvSearchItems.addOnItemTouchListener(
        new RecyclerItemClickListener(getActivity(), ismFragmentSearchBinding.rvSearchItems,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {
                  ConversationsModel conversationsModel = conversations.get(position);
                  Intent intent = new Intent(getActivity(), ConversationMessagesActivity.class);
                  intent.putExtra("messageDeliveryReadEventsEnabled",
                      conversationsModel.isMessageDeliveryReadEventsEnabled());
                  intent.putExtra("typingEventsEnabled",
                      conversationsModel.isTypingEventsEnabled());
                  intent.putExtra("newConversation", false);
                  intent.putExtra("conversationId", conversationsModel.getConversationId());
                  intent.putExtra("isPrivateOneToOne",
                      conversationsModel.isPrivateOneToOneConversation());
                  if (conversationsModel.isPrivateOneToOneConversation()) {
                    intent.putExtra("userName", conversationsModel.getConversationTitle());
                    intent.putExtra("userImageUrl", conversationsModel.getConversationImageUrl());
                    intent.putExtra("isOnline", conversationsModel.isOnline());
                    if (!conversationsModel.isOnline()) {
                      intent.putExtra("lastSeenAt", conversationsModel.getLastSeenAt());
                    }
                    intent.putExtra("userId", conversationsModel.getOpponentId());
                    if (conversationsModel.isMessagingDisabled()) {
                      intent.putExtra("messagingDisabled", true);
                    }
                  } else {
                    intent.putExtra("conversationTitle", conversationsModel.getConversationTitle());
                    intent.putExtra("participantsCount",
                        conversationsModel.getConversationMembersCount());
                    intent.putExtra("conversationImageUrl",
                        conversationsModel.getConversationImageUrl());
                  }

                  startActivity(intent);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));
    ismFragmentSearchBinding.refresh.setOnRefreshListener(() -> fetchConversations(false, null));

    return ismFragmentSearchBinding.getRoot();
  }

  @Override
  public void onDestroy() {

    super.onDestroy();
    searchConversationsContractPresenter.detachView();
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    ismFragmentSearchBinding = null;
  }

  /**
   * Fetch conversations.
   *
   * @param isSearchRequest the is search request
   * @param searchTag the search tag
   */
  public void fetchConversations(boolean isSearchRequest, String searchTag) {

    try {
      searchConversationsContractPresenter.fetchSearchedConversations(0, false, isSearchRequest,
          searchTag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onSearchedConversationsFetchedSuccessfully(
      ArrayList<ConversationsModel> conversationsModels, boolean resultsOnScroll) {
    if (!resultsOnScroll) {
      conversations.clear();
    }

    conversations.addAll(conversationsModels);

    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (conversations.size() > 0) {
            ismFragmentSearchBinding.tvNoSearchItems.setVisibility(View.GONE);
            ismFragmentSearchBinding.rvSearchItems.setVisibility(View.VISIBLE);
          } else {
            ismFragmentSearchBinding.tvNoSearchItems.setVisibility(View.VISIBLE);
            ismFragmentSearchBinding.tvNoSearchItems.setText(
                getString(R.string.ism_no_conversations));
            ismFragmentSearchBinding.rvSearchItems.setVisibility(View.GONE);
          }
        }
        searchConversationsAdapter.notifyDataSetChanged();
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
      ismFragmentSearchBinding.shimmerFrameLayoutConversations.startShimmer();
      ismFragmentSearchBinding.shimmerFrameLayoutConversations.setVisibility(View.VISIBLE);
    } else {
      if (ismFragmentSearchBinding.shimmerFrameLayoutConversations.getVisibility()
          == View.VISIBLE) {
        ismFragmentSearchBinding.shimmerFrameLayoutConversations.setVisibility(View.GONE);
        ismFragmentSearchBinding.shimmerFrameLayoutConversations.stopShimmer();
      }
    }
  }
}
