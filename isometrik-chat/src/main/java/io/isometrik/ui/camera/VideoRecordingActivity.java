package io.isometrik.ui.camera;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

import io.isometrik.chat.R;
import io.isometrik.chat.databinding.ActivityVideoRecordingBinding;


public class VideoRecordingActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private ActivityVideoRecordingBinding binding;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private File videoFile;
    private Camera camera;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK; // Start with the rear camera
    private static final int VIDEO_PREVIEW_REQUEST_CODE = 8; // New request code for video preview


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // You need to create this layout file
        binding = ActivityVideoRecordingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.surfaceView.getHolder().addCallback(this);

        binding.ivRecord.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
                // When finishing VideoRecordingActivity
                Intent previewIntent = new Intent(this, VideoPreviewActivity.class);
                previewIntent.putExtra("videoUri", videoFile.getAbsolutePath());
                startActivityForResult(previewIntent, VIDEO_PREVIEW_REQUEST_CODE);
            } else {
                startRecording();
            }
        });

        binding.ivSwitchCamera.setOnClickListener(view -> {
            switchCamera();
        });
    }

    private void switchCamera() {
        if (Camera.getNumberOfCameras() > 1) {
            cameraId = (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
                    ? Camera.CameraInfo.CAMERA_FACING_FRONT
                    : Camera.CameraInfo.CAMERA_FACING_BACK;
            openCamera();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openCamera();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void openCamera() {
        releaseCamera(); // Release any existing instance
        try {
            camera = Camera.open(cameraId);
            setCameraDisplayOrientation();
            camera.setPreviewDisplay(binding.surfaceView.getHolder());
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // Compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result);
    }

    private void startRecording() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.reset();
                mediaRecorder.release();
            }
            prepareMediaRecorder();
            mediaRecorder.start();
            isRecording = true;
            binding.ivRecord.setImageResource(R.drawable.capture_button); // Change to stop recording icon
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException stopException) {
                // Exception handling for unexpected stop failures
            } finally {
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                camera.lock();
                isRecording = false;
                binding.ivRecord.setImageResource(R.drawable.capture_button); // Change back to record icon
            }
        }
    }

    private void prepareMediaRecorder() throws IOException {
        mediaRecorder = new MediaRecorder();
        camera.unlock();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        videoFile = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), "recorded_video.mp4");
        mediaRecorder.setOutputFile(videoFile.getAbsolutePath());

        mediaRecorder.setVideoEncodingBitRate(10000000);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(1920, 1080);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        // Set orientation hint to always be portrait
        mediaRecorder.setOrientationHint(90); // Fixed rotation to portrait

        mediaRecorder.setPreviewDisplay(binding.surfaceView.getHolder().getSurface());
        mediaRecorder.prepare();
    }

    // Optionally update or remove this method if orientation hint is fixed
    private int getCurrentOrientation() {
        // This logic is no longer necessary if we always set to 90 in prepareMediaRecorder
        return 90;  // Always return 90 for portrait
    }


    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_PREVIEW_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String videoUriString = data.getStringExtra("videoUri");
            if (videoUriString != null) {
                // Handle the recorded video result
                Intent resultIntent = new Intent();
                resultIntent.putExtra("videoUri", videoUriString);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }
    }
}