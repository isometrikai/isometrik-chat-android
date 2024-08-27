package io.isometrik.ui.messages.preview.video;

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
import io.isometrik.chat.databinding.IsmActivityPreviewVideoBinding;
import io.isometrik.ui.utils.GlideApp;

/**
 * The activity to preview video message.
 */
public class PreviewVideoActivity extends AppCompatActivity {
  private IsmActivityPreviewVideoBinding ismActivityPreviewVideoBinding;
  private ExoPlayer exoPlayer;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ismActivityPreviewVideoBinding = IsmActivityPreviewVideoBinding.inflate(getLayoutInflater());
    View view = ismActivityPreviewVideoBinding.getRoot();
    setContentView(view);
    try {
      GlideApp.with(this)
          .load(getIntent().getStringExtra("videoThumbnailUrl"))
          .into(ismActivityPreviewVideoBinding.ivCoverImage);
    } catch (IllegalArgumentException | NullPointerException ignore) {
    }

    ////Creating MediaController
    //MediaController mediaController = new MediaController(this);
    //mediaController.setAnchorView(ismActivityPreviewVideoBinding.vvPreviewVideo);
    //
    ////specify the location of media file
    //Uri uri = Uri.parse(getIntent().getStringExtra("videoMainUrl"));
    //
    ////Setting MediaController and URI, then starting the videoView
    //ismActivityPreviewVideoBinding.vvPreviewVideo.setMediaController(mediaController);
    //ismActivityPreviewVideoBinding.vvPreviewVideo.setVideoURI(uri);
    //ismActivityPreviewVideoBinding.vvPreviewVideo.requestFocus();
    //ismActivityPreviewVideoBinding.vvPreviewVideo.setOnPreparedListener(mp -> {
    //  ismActivityPreviewVideoBinding.vvPreviewVideo.start();
    //  ismActivityPreviewVideoBinding.ivCoverImage.setVisibility(View.GONE);
    //});

    ismActivityPreviewVideoBinding.ibClose.setOnClickListener(v -> supportFinishAfterTransition());
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initializePlayer();
      ismActivityPreviewVideoBinding.vvPreviewVideo.onResume();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (Util.SDK_INT <= 23 || exoPlayer == null) {
      initializePlayer();
      ismActivityPreviewVideoBinding.vvPreviewVideo.onResume();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {

      ismActivityPreviewVideoBinding.vvPreviewVideo.onPause();

      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {

      ismActivityPreviewVideoBinding.vvPreviewVideo.onPause();

      releasePlayer();
    }
  }

  private void initializePlayer() {

    try {

      MediaSource mediaSource =
          new ProgressiveMediaSource.Factory(new DefaultDataSource.Factory(this)).createMediaSource(
              MediaItem.fromUri(Uri.parse(getIntent().getStringExtra("videoMainUrl"))));

      exoPlayer = new ExoPlayer.Builder(this).build();
      exoPlayer.setMediaSource(mediaSource);
      exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
      exoPlayer.setPlayWhenReady(true);
      exoPlayer.addListener(new Player.Listener() {
        @Override
        public void onPlaybackStateChanged(int playbackState) {
          if (playbackState == Player.STATE_BUFFERING) {
            ismActivityPreviewVideoBinding.pbBuffering.setVisibility(View.VISIBLE);
          } else {

            if (playbackState == Player.STATE_READY) {
              ismActivityPreviewVideoBinding.ivCoverImage.setVisibility(View.GONE);
            }
            ismActivityPreviewVideoBinding.pbBuffering.setVisibility(View.GONE);
          }
        }
      });
      exoPlayer.prepare();

      ismActivityPreviewVideoBinding.vvPreviewVideo.setPlayer(exoPlayer);
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
