package io.isometrik.ui.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import io.isometrik.chat.databinding.ActivityVideoPreviewBinding;


public class VideoPreviewActivity extends AppCompatActivity {

    private ActivityVideoPreviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String videoPath = getIntent().getStringExtra("videoUri");
        Uri videoUri = Uri.parse(videoPath);
        // Setup VideoView
        binding.videoView.setVideoURI(videoUri);
        binding.videoView.setOnPreparedListener(mp -> {
            binding.videoView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    float videoAspect = (float) mp.getVideoWidth() / mp.getVideoHeight();
                    float viewAspect = (float) binding.videoView.getWidth() / binding.videoView.getHeight();

                    if (videoAspect > viewAspect) {
                        // Video wider than view
                        float scale = (float) binding.videoView.getHeight() / mp.getVideoHeight();
                        float newWidth = scale * mp.getVideoWidth();
                        binding.videoView.getLayoutParams().width = (int) newWidth;
                        binding.videoView.requestLayout();
                    } else {
                        // Video taller than view
                        float scale = (float) binding.videoView.getWidth() / mp.getVideoWidth();
                        float newHeight = scale * mp.getVideoHeight();
                        binding.videoView.getLayoutParams().height = (int) newHeight;
                        binding.videoView.requestLayout();
                    }

                    // Remove the listener once layout adjustment is done
                    binding.videoView.removeOnLayoutChangeListener(this);
                }
            });

            binding.videoView.start();
        });

        // Set up button listeners
        binding.ivNext.setOnClickListener(v -> {
            // After video processing in VideoPreviewActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("videoUri", videoUri.toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        binding.ibBack.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;  // Prevent memory leaks
    }
}