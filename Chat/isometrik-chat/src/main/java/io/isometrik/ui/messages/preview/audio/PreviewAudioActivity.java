package io.isometrik.ui.messages.preview.audio;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.util.Util;
import io.isometrik.chat.R;
import io.isometrik.chat.databinding.IsmActivityPreviewAudioBinding;

/**
 * The activity to play audio message.
 */
public class PreviewAudioActivity extends AppCompatActivity {
  private IsmActivityPreviewAudioBinding ismActivityPreviewAudioBinding;
  private ExoPlayer exoPlayer;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityPreviewAudioBinding = IsmActivityPreviewAudioBinding.inflate(getLayoutInflater());
    View view = ismActivityPreviewAudioBinding.getRoot();
    setContentView(view);
    ismActivityPreviewAudioBinding.tvAudioName.setText(getIntent().getStringExtra("audioName"));
    ismActivityPreviewAudioBinding.ibClose.setOnClickListener(v -> supportFinishAfterTransition());
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initializePlayer();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (Util.SDK_INT <= 23 || exoPlayer == null) {
      initializePlayer();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {

      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {

      releasePlayer();
    }
  }

  private void initializePlayer() {

    try {

      MediaSource mediaSource =
          new ProgressiveMediaSource.Factory(new DefaultDataSource.Factory(this)).createMediaSource(
              MediaItem.fromUri(Uri.parse(getIntent().getStringExtra("audioUrl"))));

      exoPlayer = new ExoPlayer.Builder(this).build();
      exoPlayer.setMediaSource(mediaSource);
      exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
      exoPlayer.setPlayWhenReady(true);
      exoPlayer.addListener(new Player.Listener() {
        @Override
        public void onPlaybackStateChanged(int playbackState) {
          if (playbackState == Player.STATE_BUFFERING) {
            ismActivityPreviewAudioBinding.pbBuffering.setVisibility(View.VISIBLE);
          } else {

            ismActivityPreviewAudioBinding.pbBuffering.setVisibility(View.GONE);
          }
        }
      });
      exoPlayer.prepare();

      ismActivityPreviewAudioBinding.pControlView.setPlayer(exoPlayer);
    } catch (Exception e) {

      Toast.makeText(this, getString(R.string.ism_preview_video_failed), Toast.LENGTH_SHORT).show();
    }
  }

  private void releasePlayer() {
    if (exoPlayer != null) {
      try {

        exoPlayer.release();
        exoPlayer = null;
      } catch (Exception ignore) {
      }
    }
  }
}
