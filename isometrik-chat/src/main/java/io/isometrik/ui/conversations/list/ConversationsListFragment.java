package io.isometrik.ui.conversations.list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import io.isometrik.chat.enums.ConversationType;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmFragmentConversationsBinding;
import io.isometrik.ui.messages.chat.ConversationMessagesActivity;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The fragment to fetch list of public/open and all conversations with paging, search and pull to
 * refresh
 * option, join public conversation and update ui on realtime message or action message in a conversation.
 */
public class ConversationsListFragment extends Fragment implements ConversationsListContract.View {

  private LinearLayoutManager conversationsLayoutManager;

  private ConversationsListContract.Presenter conversationsListPresenter;
  private final ArrayList<ConversationsModel> conversations = new ArrayList<>();
  private boolean unregisteredListeners = false;

  private ConversationsAdapter conversationsAdapter;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private IsmFragmentConversationsBinding ismFragmentConversationsBinding;
  private ConversationType conversationType =  ConversationType.AllConversations;

  private Handler handler;
  private String newConversationListActivity;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    conversationsListPresenter = new ConversationsListPresenter();
    conversationsListPresenter.attachView(this);

    Bundle args = getArguments();
    if (args != null) {
      switch (args.getInt("conversationType")) {
        case 3: {
          conversationType = ConversationType.AllConversations;
          break;
        }
        case 1: {
          conversationType = ConversationType.PublicConversation;
          break;
        }
        case 2: {
          conversationType = ConversationType.OpenConversation;
          break;
        }
      }
    }
    conversationsListPresenter.initialize(conversationType);
    if (conversationType == ConversationType.AllConversations) {

      handler = new Handler();
    }
  }

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    ismFragmentConversationsBinding =
        IsmFragmentConversationsBinding.inflate(inflater, container, false);
    alertProgress = new AlertProgress();

    updateShimmerVisibility(true);
    conversationsLayoutManager = new LinearLayoutManager(getActivity());

    ismFragmentConversationsBinding.rvConversations.setLayoutManager(conversationsLayoutManager);

    conversationsAdapter = new ConversationsAdapter(getActivity(), conversations, this);

    ismFragmentConversationsBinding.rvConversations.addOnScrollListener(
        conversationsOnScrollListener);

    ismFragmentConversationsBinding.rvConversations.setAdapter(conversationsAdapter);

    fetchConversations(false, null, false, false);
    if (conversationType == ConversationType.AllConversations) {
      conversationsListPresenter.registerConnectionEventListener();
      conversationsListPresenter.registerConversationEventListener();
      conversationsListPresenter.registerMessageEventListener();
      conversationsListPresenter.registerMembershipControlEventListener();
      conversationsListPresenter.registerReactionEventListener();
      conversationsListPresenter.registerUserEventListener();
    } else if (conversationType == ConversationType.PublicConversation) {
      ismFragmentConversationsBinding.etSearch.setHint(
          getString(R.string.ism_search_public_conversations));
    } else if (conversationType == ConversationType.OpenConversation) {
      ismFragmentConversationsBinding.etSearch.setHint(
          getString(R.string.ism_search_open_conversations));
    }
    ismFragmentConversationsBinding.rvConversations.addOnItemTouchListener(
        new RecyclerItemClickListener(getActivity(),
            ismFragmentConversationsBinding.rvConversations,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (conversationType != ConversationType.PublicConversation) {
                  if (position >= 0) {

                    openMessagesScreen(conversations.get(position));
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    ismFragmentConversationsBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchConversations(true, s.toString(), false, false);
        } else {

          fetchConversations(false, null, false, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    ismFragmentConversationsBinding.refresh.setOnRefreshListener(
        () -> fetchConversations(false, null, true, true));
    ismFragmentConversationsBinding.ivAdd.setOnClickListener(v -> {

      if(newConversationListActivity != null){
        Intent intent = new Intent().setClassName(getContext(), newConversationListActivity);
        startActivity(intent);
      }
    });

    if(newConversationListActivity == null) {
      ismFragmentConversationsBinding.ivAdd.setVisibility(View.GONE);
    }

      return ismFragmentConversationsBinding.getRoot();
  }

  public void setNewConversationListActivity(String className){
    newConversationListActivity = className;
  }

  @Override
  public void onDestroy() {
    unregisterListeners();
    super.onDestroy();
    conversationsListPresenter.detachView();
  }

  @Override
  public void onDestroyView() {
    unregisterListeners();
    super.onDestroyView();
    ismFragmentConversationsBinding = null;
  }

  private final RecyclerView.OnScrollListener conversationsOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          conversationsListPresenter.fetchConversationsOnScroll(
              conversationsLayoutManager.findFirstVisibleItemPosition(),
              conversationsLayoutManager.getChildCount(), conversationsLayoutManager.getItemCount(),
              conversationType);
        }
      };

  @Override
  public void onError(String errorMessage) {
    if (ismFragmentConversationsBinding.refresh.isRefreshing()) {
      ismFragmentConversationsBinding.refresh.setRefreshing(false);
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

  @Override
  public void onConversationsFetchedSuccessfully(ArrayList<ConversationsModel> conversationsModels,
      boolean resultsOnScroll) {
    if (!resultsOnScroll) {
      conversations.clear();
    }
    conversations.addAll(conversationsModels);
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        if (!resultsOnScroll) {
          if (conversations.size() > 0) {
            ismFragmentConversationsBinding.tvNoConversations.setVisibility(View.GONE);
            ismFragmentConversationsBinding.rvConversations.setVisibility(View.VISIBLE);
          } else {
            ismFragmentConversationsBinding.tvNoConversations.setVisibility(View.VISIBLE);

            if (conversationType == ConversationType.AllConversations) {
              ismFragmentConversationsBinding.tvNoConversations.setText(
                  getString(R.string.ism_no_conversations));
            } else if (conversationType == ConversationType.PublicConversation) {
              ismFragmentConversationsBinding.tvNoConversations.setText(
                  getString(R.string.ism_no_public_conversations));
            } else if (conversationType == ConversationType.OpenConversation) {
              ismFragmentConversationsBinding.tvNoConversations.setText(
                  getString(R.string.ism_no_open_conversations));
            }

            ismFragmentConversationsBinding.rvConversations.setVisibility(View.GONE);
          }
        }
        conversationsAdapter.notifyDataSetChanged();

        hideProgressDialog();
        if (ismFragmentConversationsBinding.refresh.isRefreshing()) {
          ismFragmentConversationsBinding.refresh.setRefreshing(false);
        }
        updateShimmerVisibility(false);
      });
    }
  }

  private void fetchConversations(boolean isSearchRequest, String searchTag,
      boolean showProgressDialog, boolean isPullToRefresh) {
    if (showProgressDialog) {
      if (conversationType == ConversationType.AllConversations) {
        showProgressDialog(getString(R.string.ism_fetching_conversations));
      } else if (conversationType == ConversationType.PublicConversation) {
        showProgressDialog(getString(R.string.ism_fetching_public_conversations));
      } else if (conversationType == ConversationType.OpenConversation) {
        showProgressDialog(getString(R.string.ism_fetching_open_conversations));
      }
    }

    try {
      conversationsListPresenter.fetchConversations(0, false, conversationType, isSearchRequest,
          searchTag);

      if (conversationType == ConversationType.AllConversations && isPullToRefresh) {
        fetchUnreadConversationsCount();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onConversationCreated(ConversationsModel conversationsModel,
      boolean createdByLoggedInUser) {
    if (conversationType == ConversationType.AllConversations || !createdByLoggedInUser) {
      if (getActivity() != null) {

        getActivity().runOnUiThread(() -> {
          if (conversations.size() == 0) {
            ismFragmentConversationsBinding.tvNoConversations.setVisibility(View.GONE);
            ismFragmentConversationsBinding.rvConversations.setVisibility(View.VISIBLE);
          }
          conversations.add(0, conversationsModel);
          conversationsAdapter.notifyItemInserted(0);
        });
      }
    }
  }

  @Override
  public void removeConversation(String conversationId) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        int position = conversationsListPresenter.fetchConversationPositionInList(conversations,
            conversationId, true);

        if (position != -1) {
          conversations.remove(position);
          conversationsAdapter.notifyItemRemoved(position);
          if (conversations.size() == 0) {
            ismFragmentConversationsBinding.tvNoConversations.setVisibility(View.VISIBLE);
            if (conversationType == ConversationType.AllConversations) {
              ismFragmentConversationsBinding.tvNoConversations.setText(
                  getString(R.string.ism_no_conversations));
            } else if (conversationType == ConversationType.PublicConversation) {
              ismFragmentConversationsBinding.tvNoConversations.setText(
                  getString(R.string.ism_no_public_conversations));
            } else if (conversationType == ConversationType.OpenConversation) {
              ismFragmentConversationsBinding.tvNoConversations.setText(
                  getString(R.string.ism_no_open_conversations));
            }
            ismFragmentConversationsBinding.rvConversations.setVisibility(View.GONE);
          }
        }
      });
    }
  }

  @Override
  public void onConversationTitleUpdated(String conversationId, String newTitle) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        int position = conversationsListPresenter.fetchConversationPositionInList(conversations,
            conversationId, true);

        if (position != -1) {
          ConversationsModel conversationsModel = conversations.get(position);
          conversationsModel.setConversationTitle(newTitle);

          conversations.remove(position);
          conversationsAdapter.notifyItemRemoved(position);
          conversations.add(0, conversationsModel);
          conversationsAdapter.notifyItemInserted(0);

        }
      });
    }
  }

  @Override
  public void onConversationImageUpdated(String conversationId, String conversationImageUrl) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        int position = conversationsListPresenter.fetchConversationPositionInList(conversations,
            conversationId, true);

        if (position != -1) {
          ConversationsModel conversationsModel = conversations.get(position);
          conversationsModel.setConversationImageUrl(conversationImageUrl);

          conversations.remove(position);
          conversationsAdapter.notifyItemRemoved(position);
          conversations.add(0, conversationsModel);
          conversationsAdapter.notifyItemInserted(0);
        }
      });
    }
  }

  @Override
  public void onMessagingStatusChanged(String conversationId, boolean disabled) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        int position = conversationsListPresenter.fetchConversationPositionInList(conversations,
            conversationId, false);
        if (position != -1) {
          ConversationsModel conversationsModel = conversations.get(position);
          conversationsModel.setMessagingDisabled(disabled);
          conversations.set(position, conversationsModel);
          conversationsAdapter.notifyItemChanged(position);
        }
      });
    }
  }

  @Override
  public void onConversationCleared(String conversationId, String lastMessageText,
      String lastMessageTime) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        int position = conversationsListPresenter.fetchConversationPositionInList(conversations,
            conversationId, false);
        if (position != -1) {

          ConversationsModel conversationsModel = conversations.get(position);
          conversationsModel.setLastMessageText(lastMessageText);
          conversationsModel.setLastMessageSendersProfileImageUrl(null);
          conversationsModel.setLastMessageSenderName(null);
          conversationsModel.setLastMessageTime(lastMessageTime);
          conversationsModel.setLastMessagePlaceHolderImage(null);
          conversationsModel.setLastMessageWasReactionMessage(false);
          conversationsModel.setUnreadMessagesCount(0);

          conversations.set(position, conversationsModel);
          conversationsAdapter.notifyItemChanged(position);
        }
      });
    }
  }

  @Override
  public void updateLastMessageInConversation(String conversationId, String lastMessageText,
      String lastMessageSendersProfileImageUrl, String lastMessageTime,
      Integer lastMessagePlaceHolderImage, boolean lastMessageWasReactionMessage,
      boolean updateUnreadMessagesCount, String lastMessageSendersName,
      boolean fetchRemoteConversationIfNotFoundLocally) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        int position = conversationsListPresenter.fetchConversationPositionInList(conversations,
            conversationId, fetchRemoteConversationIfNotFoundLocally);

        if (position != -1) {
          ConversationsModel conversationsModel = conversations.get(position);
          conversationsModel.setLastMessageText(lastMessageText);
          conversationsModel.setLastMessageSendersProfileImageUrl(
              lastMessageSendersProfileImageUrl);
          conversationsModel.setLastMessageSenderName(lastMessageSendersName);
          conversationsModel.setLastMessageTime(lastMessageTime);
          conversationsModel.setLastMessagePlaceHolderImage(lastMessagePlaceHolderImage);
          conversationsModel.setLastMessageWasReactionMessage(lastMessageWasReactionMessage);
          if (updateUnreadMessagesCount
              && conversationsModel.isMessageDeliveryReadEventsEnabled()) {

            if (conversationsModel.getUnreadMessagesCount() == 0) {
              fetchUnreadConversationsCount();
            }
            conversationsModel.setUnreadMessagesCount(
                conversationsModel.getUnreadMessagesCount() + 1);
          }

          conversations.remove(position);
          conversationsAdapter.notifyItemRemoved(position);
          conversations.add(0, conversationsModel);
          conversationsAdapter.notifyItemInserted(0);
        }
      });
    }
  }

  @Override
  public void onConversationSettingsUpdated(String conversationId, Boolean typingEvents,
      Boolean readEvents) {
    int position =
        conversationsListPresenter.fetchConversationPositionInList(conversations, conversationId,
            true);

    if (position != -1) {
      ConversationsModel conversationsModel = conversations.get(position);
      if (readEvents != null) conversationsModel.setMessageDeliveryReadEventsEnabled(readEvents);
      if (typingEvents != null) conversationsModel.setTypingEventsEnabled(typingEvents);
      conversations.set(position, conversationsModel);
    }
  }

  @Override
  public void updateConversationMembersCount(String conversationId, int membersCount) {
    int position =
        conversationsListPresenter.fetchConversationPositionInList(conversations, conversationId,
            true);

    if (position != -1) {
      ConversationsModel conversationsModel = conversations.get(position);
      conversationsModel.setConversationMembersCount(membersCount);
      conversations.set(position, conversationsModel);
    }
  }

  @Override
  public void onConversationJoinedSuccessfully(ConversationsModel conversationsModel) {
    if (conversationType == ConversationType.PublicConversation) {
      if (getActivity() != null) {
        getActivity().runOnUiThread(() -> {

          int position = conversationsListPresenter.fetchConversationPositionInList(conversations,
              conversationsModel.getConversationId(), false);
          if (position != -1) {

            conversations.remove(position);
            conversationsAdapter.notifyItemRemoved(position);
            if (conversations.size() == 0) {
              ismFragmentConversationsBinding.tvNoConversations.setVisibility(View.VISIBLE);
              ismFragmentConversationsBinding.tvNoConversations.setText(
                  getString(R.string.ism_no_public_conversations));
              ismFragmentConversationsBinding.rvConversations.setVisibility(View.GONE);
            }
          }
          hideProgressDialog();
          openMessagesScreen(conversationsModel);
        });
      }
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
   * Cleanup all realtime isometrik event listeners that were added at time of exit
   */
  private void unregisterListeners() {
    if (!unregisteredListeners) {

      unregisteredListeners = true;
      if (conversationType == ConversationType.AllConversations) {
        handler.removeCallbacksAndMessages(null);
        conversationsListPresenter.unregisterConnectionEventListener();
        conversationsListPresenter.unregisterConversationEventListener();
        conversationsListPresenter.unregisterMessageEventListener();
        conversationsListPresenter.unregisterMembershipControlEventListener();
        conversationsListPresenter.unregisterReactionEventListener();
        conversationsListPresenter.unregisterUserEventListener();
      }
    }
  }

  /**
   * Join conversation.
   *
   * @param conversationsModel the conversations model
   */
  public void joinConversation(ConversationsModel conversationsModel) {
    if (getActivity() != null) {
      new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.ism_join_conversation))
          .setMessage(getString(R.string.ism_join_conversation_alert_message))
          .setCancelable(true)
          .setPositiveButton(getString(R.string.ism_continue), (dialog, id) -> {

            dialog.cancel();
            showProgressDialog(getString(R.string.ism_joining_conversation));
            conversationsListPresenter.joinConversation(conversationsModel);
          })
          .setNegativeButton(getString(R.string.ism_cancel), (dialog, id) -> dialog.cancel())
          .create()
          .show();
    }
  }

  private void openMessagesScreen(ConversationsModel conversationsModel) {
    Intent intent = new Intent(getActivity(), ConversationMessagesActivity.class);
    intent.putExtra("messageDeliveryReadEventsEnabled",
        conversationsModel.isMessageDeliveryReadEventsEnabled());
    intent.putExtra("typingEventsEnabled", conversationsModel.isTypingEventsEnabled());
    intent.putExtra("newConversation", false);
    intent.putExtra("conversationId", conversationsModel.getConversationId());
    intent.putExtra("isPrivateOneToOne", conversationsModel.isPrivateOneToOneConversation());
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
      intent.putExtra("participantsCount", conversationsModel.getConversationMembersCount());
      intent.putExtra("conversationImageUrl", conversationsModel.getConversationImageUrl());

      if (conversationType == ConversationType.OpenConversation) {
        intent.putExtra("joinAsObserver", true);
      }
    }

    startActivity(intent);
  }

  @Override
  public void fetchUnreadConversationsCount() {
    if (getActivity() != null) {
      ((ConversationsActivity) getActivity()).fetchUnreadConversationsCount();
    }
  }

  @Override
  public void connectionStateChanged(boolean connected) {
    if (getActivity() != null) {
      ((ConversationsActivity) getActivity()).connectionStateChanged(connected);
    }
  }

  @Override
  public void failedToConnect(String errorMessage) {

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

  @Override
  public void onUnreadMessagesCountInConversationsFetchedSuccessfully(String conversationId,
      int count) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {
        int position = conversationsListPresenter.fetchConversationPositionInList(conversations,
            conversationId, false);
        if (position != -1) {
          ConversationsModel conversationsModel = conversations.get(position);
          conversationsModel.setUnreadMessagesCount(count);
          conversations.set(position, conversationsModel);
          conversationsAdapter.notifyItemChanged(position);
        }
      });
    }
  }

  @Override
  public void onUserProfileImageUpdated(String userProfileImageUrl) {

    if (getActivity() != null) {

      ((ConversationsActivity) getActivity()).loadUserImage(userProfileImageUrl);
    }
  }

  @Override
  public void onUserDeleted() {
    if (getActivity() != null) {

      ((ConversationsActivity) getActivity()).onUserDeleted();
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      ismFragmentConversationsBinding.shimmerFrameLayout.startShimmer();
      ismFragmentConversationsBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismFragmentConversationsBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismFragmentConversationsBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismFragmentConversationsBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }

  @Override
  public void onRemoteUserTypingEvent(String conversationId, String message) {
    if (getActivity() != null) {
      getActivity().runOnUiThread(() -> {

        int size = conversations.size();
        for (int i = 0; i < size; i++) {
          if (conversations.get(i).getConversationId().equals(conversationId)) {
            ConversationsModel conversationsModel = conversations.get(i);
            conversationsModel.setRemoteUserTyping(true);
            conversationsModel.setRemoteUserTypingMessage(message);
            conversations.set(i, conversationsModel);
            conversationsAdapter.notifyItemChanged(i);

            try {
              handler.postDelayed(() -> {
                int size1 = conversations.size();
                for (int i1 = 0; i1 < size1; i1++) {
                  if (conversations.get(i1).getConversationId().equals(conversationId)) {
                    ConversationsModel conversationsModel1 = conversations.get(i1);
                    conversationsModel1.setRemoteUserTyping(false);
                    conversations.set(i1, conversationsModel1);
                    conversationsAdapter.notifyItemChanged(i1);
                    break;
                  }
                }
              }, 1000);
            } catch (Exception ignore) {

            }
            break;
          }
        }
      });
    }
  }
}
