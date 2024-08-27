//package io.isometrik.chat.ui.conversations.newconversation.group.search;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.Toast;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import io.isometrik.chat.R;
//import io.isometrik.chat.conversations.newconversation.group.SelectedUsersAdapter;
//import io.isometrik.chat.conversations.newconversation.group.UsersAdapter;
//import io.isometrik.chat.conversations.newconversation.group.UsersModel;
//import io.isometrik.chat.databinding.IsmActivitySearchParticipantsBinding;
//import io.isometrik.chat.utils.AlertProgress;
//import io.isometrik.chat.utils.Constants;
//import io.isometrik.chat.utils.KeyboardUtil;
//import io.isometrik.chat.utils.RecyclerItemClickListener;
//import java.util.ArrayList;
//import org.jetbrains.annotations.NotNull;
//
//public class SearchParticipantsActivity extends AppCompatActivity
//    implements SearchParticipantsContract.View {
//
//  private SearchParticipantsContract.Presenter searchParticipantsPresenter;
//  private IsmActivitySearchParticipantsBinding ismActivitySearchParticipantsBinding;
//
//  private final ArrayList<UsersModel> users = new ArrayList<>();
//  private final ArrayList<UsersModel> selectedUsers = new ArrayList<>();
//  private UsersAdapter usersAdapter;
//  private SelectedUsersAdapter selectedUsersAdapter;
//  private LinearLayoutManager usersLayoutManager;
//  private LinearLayoutManager selectedUsersLayoutManager;
//  private boolean selectedUsersStateNeedToBeSaved = false;
//
//  private AlertProgress alertProgress;
//  private AlertDialog alertDialog;
//
//  private int count;
//  private final int MAXIMUM_MEMBERS = Constants.CONVERSATION_MEMBERS_SIZE_AT_A_TIME;
//
//  @Override
//  protected void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    ismActivitySearchParticipantsBinding =
//        IsmActivitySearchParticipantsBinding.inflate(getLayoutInflater());
//    View view = ismActivitySearchParticipantsBinding.getRoot();
//    setContentView(view);
//    alertProgress = new AlertProgress();
//    searchParticipantsPresenter = new SearchParticipantsPresenter(this);
//
//    usersLayoutManager = new LinearLayoutManager(this);
//    ismActivitySearchParticipantsBinding.rvUsers.setLayoutManager(usersLayoutManager);
//    usersAdapter = new UsersAdapter(this, users);
//    ismActivitySearchParticipantsBinding.rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
//    ismActivitySearchParticipantsBinding.rvUsers.setAdapter(usersAdapter);
//
//    selectedUsersLayoutManager =
//        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//    ismActivitySearchParticipantsBinding.rvUsersSelected.setLayoutManager(
//        selectedUsersLayoutManager);
//    selectedUsersAdapter = new SelectedUsersAdapter(this, selectedUsers);
//    ismActivitySearchParticipantsBinding.rvUsersSelected.setAdapter(selectedUsersAdapter);
//
//    //noinspection unchecked
//    ArrayList<UsersModel> selectedUsers =
//        (ArrayList<UsersModel>) getIntent().getSerializableExtra("selectedParticipants");
//    if (selectedUsers.size() > 0) {
//
//      this.selectedUsers.addAll(selectedUsers);
//      selectedUsersAdapter.notifyDataSetChanged();
//      count = selectedUsers.size();
//      updateSelectedMembersText();
//      selectedUsersStateNeedToBeSaved = true;
//    }
//
//    String conversationTitle = getIntent().getStringExtra("conversationTitle");
//    if (conversationTitle.isEmpty()) {
//      conversationTitle = getString(R.string.ism_conversation_title);
//    }
//    ismActivitySearchParticipantsBinding.tvConversationTitle.setText(conversationTitle);
//
//    fetchLatestUsers(false, null, true);
//
//    ismActivitySearchParticipantsBinding.rvUsers.addOnItemTouchListener(
//        new RecyclerItemClickListener(this, ismActivitySearchParticipantsBinding.rvUsers,
//            new RecyclerItemClickListener.OnItemClickListener() {
//              @Override
//              public void onItemClick(View view, int position) {
//                if (position >= 0) {
//
//                  UsersModel user = users.get(position);
//
//                  if (user.isSelected()) {
//                    user.setSelected(false);
//                    count--;
//                    removeSelectedUser(user.getUserId());
//                  } else {
//
//                    if (count < MAXIMUM_MEMBERS) {
//                      //Maximum 99 members can be added
//                      user.setSelected(true);
//                      count++;
//                      addSelectedUser(user);
//                    } else {
//                      Toast.makeText(SearchParticipantsActivity.this,
//                          getString(R.string.ism_max_participants_limit_reached),
//                          Toast.LENGTH_SHORT).show();
//                    }
//                  }
//                  updateSelectedMembersText();
//                  users.set(position, user);
//                  usersAdapter.notifyItemChanged(position);
//                }
//              }
//
//              @Override
//              public void onItemLongClick(View view, int position) {
//              }
//            }));
//
//    ismActivitySearchParticipantsBinding.btDone.setOnClickListener(v -> {
//      KeyboardUtil.hideKeyboard(this);
//      Intent returnIntent = new Intent();
//      returnIntent.putExtra("selectedParticipants", this.selectedUsers);
//      setResult(Activity.RESULT_OK, returnIntent);
//      supportFinishAfterTransition();
//    });
//
//    ismActivitySearchParticipantsBinding.refresh.setOnRefreshListener(
//        () -> fetchLatestUsers(false, null, true));
//
//    ismActivitySearchParticipantsBinding.ibBack.setOnClickListener(v -> {
//      KeyboardUtil.hideKeyboard(this);
//      onBackPressed();
//    });
//
//    ismActivitySearchParticipantsBinding.etSearch.addTextChangedListener(new TextWatcher() {
//      @Override
//      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//      }
//
//      @Override
//      public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (s.length() > 0) {
//          fetchLatestUsers(true, s.toString(), false);
//        } else {
//          if (SearchParticipantsActivity.this.selectedUsers.size() > 0) {
//            selectedUsersStateNeedToBeSaved = true;
//          }
//          fetchLatestUsers(false, null, false);
//        }
//      }
//
//      @Override
//      public void afterTextChanged(Editable s) {
//
//      }
//    });
//  }
//
//  @Override
//  public void onNonBlockedUsersFetched(ArrayList<UsersModel> users, boolean latestUsers,
//      boolean isSearchRequest) {
//    if (latestUsers) {
//      this.users.clear();
//      if (isSearchRequest || selectedUsersStateNeedToBeSaved) {
//        int size = users.size();
//        UsersModel usersModel;
//
//        for (int i = 0; i < size; i++) {
//
//          for (int j = 0; j < selectedUsers.size(); j++) {
//            if (selectedUsers.get(j).getUserId().equals(users.get(i).getUserId())) {
//              usersModel = users.get(i);
//              usersModel.setSelected(true);
//              users.set(i, usersModel);
//              break;
//            }
//          }
//        }
//        if (!isSearchRequest) selectedUsersStateNeedToBeSaved = false;
//      } else {
//
//        runOnUiThread(() -> {
//          selectedUsers.clear();
//          count = 0;
//          selectedUsersAdapter.notifyDataSetChanged();
//          updateSelectedMembersText();
//        });
//      }
//    } else {
//
//      int size = users.size();
//      UsersModel usersModel;
//
//      for (int i = 0; i < size; i++) {
//
//        for (int j = 0; j < selectedUsers.size(); j++) {
//          if (selectedUsers.get(j).getUserId().equals(users.get(i).getUserId())) {
//            usersModel = users.get(i);
//            usersModel.setSelected(true);
//            users.set(i, usersModel);
//            break;
//          }
//        }
//      }
//    }
//    this.users.addAll(users);
//
//    runOnUiThread(() -> {
//      if (SearchParticipantsActivity.this.users.size() > 0) {
//        ismActivitySearchParticipantsBinding.tvNoUsers.setVisibility(View.GONE);
//        ismActivitySearchParticipantsBinding.rvUsers.setVisibility(View.VISIBLE);
//      } else {
//        ismActivitySearchParticipantsBinding.tvNoUsers.setVisibility(View.VISIBLE);
//        ismActivitySearchParticipantsBinding.rvUsers.setVisibility(View.GONE);
//      }
//      usersAdapter.notifyDataSetChanged();
//    });
//    hideProgressDialog();
//    if (ismActivitySearchParticipantsBinding.refresh.isRefreshing()) {
//      ismActivitySearchParticipantsBinding.refresh.setRefreshing(false);
//    }
//  }
//
//  @Override
//  public void onError(String errorMessage) {
//    if (ismActivitySearchParticipantsBinding.refresh.isRefreshing()) {
//      ismActivitySearchParticipantsBinding.refresh.setRefreshing(false);
//    }
//
//    hideProgressDialog();
//
//    runOnUiThread(() -> {
//      if (errorMessage != null) {
//        Toast.makeText(SearchParticipantsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//      } else {
//        Toast.makeText(SearchParticipantsActivity.this, getString(R.string.ism_error),
//            Toast.LENGTH_SHORT).show();
//      }
//    });
//  }
//
//  public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
//      new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
//          super.onScrollStateChanged(recyclerView, newState);
//        }
//
//        @Override
//        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
//          super.onScrolled(recyclerView, dx, dy);
//
//          searchParticipantsPresenter.requestNonBlockedUsersDataOnScroll(
//              usersLayoutManager.findFirstVisibleItemPosition(), usersLayoutManager.getChildCount(),
//              usersLayoutManager.getItemCount());
//        }
//      };
//
//  public void removeUser(String userId) {
//    int size = users.size();
//    for (int i = 0; i < size; i++) {
//
//      if (users.get(i).getUserId().equals(userId)) {
//
//        UsersModel selectMembersModel = users.get(i);
//        selectMembersModel.setSelected(false);
//        users.set(i, selectMembersModel);
//        if (i == 0) {
//          usersAdapter.notifyDataSetChanged();
//        } else {
//          usersAdapter.notifyItemChanged(i);
//        }
//        count--;
//        updateSelectedMembersText();
//        break;
//      }
//    }
//
//    for (int i = 0; i < selectedUsers.size(); i++) {
//
//      if (selectedUsers.get(i).getUserId().equals(userId)) {
//        selectedUsers.remove(i);
//        if (i == 0) {
//          selectedUsersAdapter.notifyDataSetChanged();
//        } else {
//          selectedUsersAdapter.notifyItemRemoved(i);
//        }
//
//        break;
//      }
//    }
//  }
//
//  private void fetchLatestUsers(boolean isSearchRequest, String searchTag,
//      boolean showProgressDialog) {
//    if (showProgressDialog) showProgressDialog(getString(R.string.ism_fetching_users));
//
//    try {
//      searchParticipantsPresenter.fetchNonBlockedUsers(0, true, isSearchRequest, searchTag);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  private void removeSelectedUser(String userId) {
//
//    for (int i = 0; i < selectedUsers.size(); i++) {
//      if (selectedUsers.get(i).getUserId().equals(userId)) {
//        selectedUsers.remove(i);
//        if (i == 0) {
//          selectedUsersAdapter.notifyDataSetChanged();
//        } else {
//          selectedUsersAdapter.notifyItemRemoved(i);
//        }
//        break;
//      }
//    }
//  }
//
//  private void addSelectedUser(UsersModel selectMembersModel) {
//    selectedUsers.add(selectMembersModel);
//    try {
//      selectedUsersAdapter.notifyDataSetChanged();
//      selectedUsersLayoutManager.scrollToPosition(selectedUsers.size() - 1);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  private void updateSelectedMembersText() {
//
//    if (count > 0) {
//      ismActivitySearchParticipantsBinding.tvMembersCount.setText(
//          getString(R.string.ism_number_of_participants_selected, String.valueOf(count),
//              String.valueOf(MAXIMUM_MEMBERS)));
//    } else {
//      ismActivitySearchParticipantsBinding.tvMembersCount.setText(
//          getString(R.string.ism_add_participants));
//    }
//  }
//
//  private void showProgressDialog(String message) {
//
//    alertDialog = alertProgress.getProgressDialog(this, message);
//    if (!isFinishing()) alertDialog.show();
//  }
//
//  private void hideProgressDialog() {
//
//    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
//  }
//}
