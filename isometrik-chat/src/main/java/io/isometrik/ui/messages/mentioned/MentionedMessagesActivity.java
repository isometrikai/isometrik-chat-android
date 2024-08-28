package io.isometrik.ui.messages.mentioned;

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
import io.isometrik.chat.databinding.IsmActivityMentionedMessagesBinding;
import io.isometrik.ui.messages.chat.ConversationMessagesActivity;
import io.isometrik.chat.utils.AlertProgress;
import io.isometrik.chat.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The activity to fetch list of messages in which logged in user has been mentioned, with paging,
 * search and pull to refresh option.
 */
public class MentionedMessagesActivity extends AppCompatActivity
    implements MentionedMessagesContract.View {

  private MentionedMessagesContract.Presenter mentionedMessagesPresenter;
  private IsmActivityMentionedMessagesBinding ismActivityMentionedMessagesBinding;

  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private MentionedMessagesAdapter mentionedMessagesAdapter;
  private ArrayList<MentionedMessagesModel> mentionedMessages;
  private LinearLayoutManager mentionedMessagesLayoutManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityMentionedMessagesBinding =
        IsmActivityMentionedMessagesBinding.inflate(getLayoutInflater());
    View view = ismActivityMentionedMessagesBinding.getRoot();
    setContentView(view);
    alertProgress = new AlertProgress();
    updateShimmerVisibility(true);
    mentionedMessages = new ArrayList<>();
    mentionedMessagesLayoutManager = new LinearLayoutManager(this);
    ismActivityMentionedMessagesBinding.rvMentionedMessages.setLayoutManager(
        mentionedMessagesLayoutManager);
    mentionedMessagesAdapter = new MentionedMessagesAdapter(this, mentionedMessages);
    ismActivityMentionedMessagesBinding.rvMentionedMessages.setAdapter(mentionedMessagesAdapter);
    ismActivityMentionedMessagesBinding.rvMentionedMessages.addOnScrollListener(
        attachmentsOnScrollListener);

    mentionedMessagesPresenter = new MentionedMessagesPresenter(this);

    fetchMentionedMessages(false, null, false);
    ismActivityMentionedMessagesBinding.rvMentionedMessages.addOnItemTouchListener(
        new RecyclerItemClickListener(this, ismActivityMentionedMessagesBinding.rvMentionedMessages,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {
                  MentionedMessagesModel mentionedMessagesModel = mentionedMessages.get(position);

                  Intent intent = new Intent(MentionedMessagesActivity.this,
                      ConversationMessagesActivity.class);
                  intent.putExtra("scrollToMessageNeeded", true);
                  intent.putExtra("conversationId", mentionedMessagesModel.getConversationId());
                  intent.putExtra("messageId", mentionedMessagesModel.getMessageId());
                  intent.putExtra("isPrivateOneToOne",
                      mentionedMessagesModel.isPrivateOneToOneConversation());
                  if (mentionedMessagesModel.isPrivateOneToOneConversation()) {
                    if (mentionedMessagesModel.isMessagingDisabled()) {
                      intent.putExtra("messagingDisabled", true);
                    }
                  }
                  startActivity(intent);
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));
    ismActivityMentionedMessagesBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          fetchMentionedMessages(true, s.toString(), false);
        } else {

          fetchMentionedMessages(false, null, false);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    ismActivityMentionedMessagesBinding.ibBack.setOnClickListener(v -> onBackPressed());
    ismActivityMentionedMessagesBinding.refresh.setOnRefreshListener(
        () -> fetchMentionedMessages(false, null, true));
  }

  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();
    if (ismActivityMentionedMessagesBinding.refresh.isRefreshing()) {
      ismActivityMentionedMessagesBinding.refresh.setRefreshing(false);
    }
    updateShimmerVisibility(false);
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private final RecyclerView.OnScrollListener attachmentsOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          mentionedMessagesPresenter.fetchMentionedMessagesOnScroll(
              mentionedMessagesLayoutManager.findFirstVisibleItemPosition(),
              mentionedMessagesLayoutManager.getChildCount(),
              mentionedMessagesLayoutManager.getItemCount());
        }
      };

  @Override
  public void onMentionedMessagesFetchedSuccessfully(
      ArrayList<MentionedMessagesModel> mentionedMessagesModels, boolean resultsOnScroll) {
    if (!resultsOnScroll) {
      mentionedMessages.clear();
    }
    mentionedMessages.addAll(mentionedMessagesModels);

    runOnUiThread(() -> {
      if (!resultsOnScroll) {
        if (mentionedMessages.size() > 0) {
          ismActivityMentionedMessagesBinding.tvNoMentionedMessages.setVisibility(View.GONE);
          ismActivityMentionedMessagesBinding.rvMentionedMessages.setVisibility(View.VISIBLE);
        } else {
          ismActivityMentionedMessagesBinding.tvNoMentionedMessages.setVisibility(View.VISIBLE);
          ismActivityMentionedMessagesBinding.rvMentionedMessages.setVisibility(View.GONE);
        }
      }
      mentionedMessagesAdapter.notifyDataSetChanged();

      hideProgressDialog();
      if (ismActivityMentionedMessagesBinding.refresh.isRefreshing()) {
        ismActivityMentionedMessagesBinding.refresh.setRefreshing(false);
      }
      updateShimmerVisibility(false);
    });
  }

  private void fetchMentionedMessages(boolean isSearchRequest, String searchTag,
      boolean showProgressDialog) {
    if (showProgressDialog) {
      showProgressDialog(getString(R.string.ism_fetching_mentioned_messages));
    }

    try {
      mentionedMessagesPresenter.fetchMentionedMessages(0, false, isSearchRequest, searchTag);
    } catch (Exception e) {
      e.printStackTrace();
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
      ismActivityMentionedMessagesBinding.shimmerFrameLayout.startShimmer();
      ismActivityMentionedMessagesBinding.shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (ismActivityMentionedMessagesBinding.shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        ismActivityMentionedMessagesBinding.shimmerFrameLayout.setVisibility(View.GONE);
        ismActivityMentionedMessagesBinding.shimmerFrameLayout.stopShimmer();
      }
    }
  }
}
