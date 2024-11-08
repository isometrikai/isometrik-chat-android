package io.isometrik.ui.messages.preview.image;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;

import io.isometrik.chat.databinding.IsmActivityPreviewImageBinding;

/**
 * The activity to preview video message.
 */
public class PreviewImageActivity extends AppCompatActivity {
  private IsmActivityPreviewImageBinding ismActivityPreviewImageBinding;
  private ExoPlayer exoPlayer;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityPreviewImageBinding = IsmActivityPreviewImageBinding.inflate(getLayoutInflater());
    View view = ismActivityPreviewImageBinding.getRoot();
    setContentView(view);
    try {
      Glide.with(this)
          .load(getIntent().getStringExtra("imageUrl"))
          .into(ismActivityPreviewImageBinding.ivCoverImage);
    } catch (IllegalArgumentException | NullPointerException ignore) {
    }


    ismActivityPreviewImageBinding.ibClose.setOnClickListener(v -> supportFinishAfterTransition());
  }
}
