package io.isometrik.ui.conversations.newconversation.type;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.isometrik.chat.enums.ConversationType;
import io.isometrik.ui.conversations.newconversation.group.NewGroupConversationActivity;
import io.isometrik.ui.conversations.newconversation.onetoone.NewOneToOneConversationActivity;
import io.isometrik.chat.databinding.IsmActivitySelectConversationTypeBinding;

/**
 * The activity to select type of new conversation to be created.
 */
public class SelectConversationTypeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    IsmActivitySelectConversationTypeBinding
        ismActivitySelectConversationTypeBinding =
        IsmActivitySelectConversationTypeBinding.inflate(getLayoutInflater());
    View view = ismActivitySelectConversationTypeBinding.getRoot();
    setContentView(view);

    ismActivitySelectConversationTypeBinding.rlPrivateOneToOne.setOnClickListener(v -> {
      Intent intent =
          new Intent(SelectConversationTypeActivity.this, NewOneToOneConversationActivity.class);

      startActivity(intent);
      supportFinishAfterTransition();
    });

    ismActivitySelectConversationTypeBinding.rlPrivateGroup.setOnClickListener(v -> {
      Intent intent =
          new Intent(SelectConversationTypeActivity.this, NewGroupConversationActivity.class);
      intent.putExtra("conversationType", ConversationType.PrivateConversation.getValue());

      startActivity(intent);
      supportFinishAfterTransition();
    });

    ismActivitySelectConversationTypeBinding.rlPublicGroup.setOnClickListener(v -> {
      Intent intent =
          new Intent(SelectConversationTypeActivity.this, NewGroupConversationActivity.class);
      intent.putExtra("conversationType", ConversationType.PublicConversation.getValue());

      startActivity(intent);
      supportFinishAfterTransition();
    });

    ismActivitySelectConversationTypeBinding.rlOpen.setOnClickListener(v -> {
      Intent intent =
          new Intent(SelectConversationTypeActivity.this, NewGroupConversationActivity.class);
      intent.putExtra("conversationType", ConversationType.OpenConversation.getValue());

      startActivity(intent);
      supportFinishAfterTransition();
    });

    ismActivitySelectConversationTypeBinding.ibBack.setOnClickListener(v -> {
      onBackPressed();
    });
  }
}
