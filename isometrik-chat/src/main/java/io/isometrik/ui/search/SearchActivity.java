package io.isometrik.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayoutMediator;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivitySearchBinding;
import io.isometrik.ui.search.conversations.SearchConversationsFragment;
import io.isometrik.ui.search.messages.SearchMessagesFragment;
import io.isometrik.ui.search.people.SearchPeopleFragment;

/**
 * The activity containing fragments for the search conversations, messages and people results.
 */
public class SearchActivity extends AppCompatActivity {

  private SearchConversationsFragment searchConversationsFragment;
  private SearchMessagesFragment searchMessagesFragment;
  private SearchPeopleFragment searchPeopleFragment;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IsmActivitySearchBinding ismActivitySearchBinding =
        IsmActivitySearchBinding.inflate(getLayoutInflater());
    View view = ismActivitySearchBinding.getRoot();
    setContentView(view);

    final SearchFragmentAdapter searchFragmentAdapter =
        new SearchFragmentAdapter(SearchActivity.this);
    ismActivitySearchBinding.vpSearchItems.setAdapter(searchFragmentAdapter);
    ismActivitySearchBinding.vpSearchItems.setOffscreenPageLimit(2);
    new TabLayoutMediator(ismActivitySearchBinding.tabLayout,
        ismActivitySearchBinding.vpSearchItems, (tab, position) -> {
      switch (position) {
        case 0:
          tab.setText(getString(R.string.ism_conversations))
              .setIcon(R.drawable.ism_ic_forward_conversation);
          break;
        case 1:
          tab.setText(getString(R.string.ism_messages)).setIcon(R.drawable.ism_ic_search_messages);
          break;
        case 2:
          tab.setText(getString(R.string.ism_people)).setIcon(R.drawable.ism_ic_search_people);
          break;
      }
    }).attach();

    ismActivitySearchBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchConversationsFragment =
            (SearchConversationsFragment) getSupportFragmentManager().findFragmentByTag("f0");
        searchMessagesFragment =
            (SearchMessagesFragment) getSupportFragmentManager().findFragmentByTag("f1");
        searchPeopleFragment =
            (SearchPeopleFragment) getSupportFragmentManager().findFragmentByTag("f2");

        if (s.length() > 0) {
          if (searchConversationsFragment != null) {

            searchConversationsFragment.fetchConversations(true, s.toString());
          }
          if (searchMessagesFragment != null) {

            searchMessagesFragment.fetchMessages(true, s.toString());
          }
          if (searchPeopleFragment != null) {
            searchPeopleFragment.fetchPeople(true, s.toString());
          }
        } else {
          if (searchConversationsFragment != null) {

            searchConversationsFragment.fetchConversations(false, null);
          }
          if (searchMessagesFragment != null) {

            searchMessagesFragment.fetchMessages(false, null);
          }
          if (searchPeopleFragment != null) {
            searchPeopleFragment.fetchPeople(false, null);
          }
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    ismActivitySearchBinding.ibBack.setOnClickListener(v -> onBackPressed());
  }
}
