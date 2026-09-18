package io.isometrik.ui.messages.preview.image;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import io.isometrik.chat.databinding.IsmDialogPreviewImageBinding;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;

/**
 * The helper class to show preview image popup with pinch/ click to zoom/ double tap gestures for
 * image.
 */
public class PreviewImagePopup {

  /**
   * Show.
   *
   * @param activity the activity
   * @param mediaUrl the media url
   */
  public void show(Activity activity, String mediaUrl) {
    try {
      final Dialog dialog = new Dialog(activity);
      IsmDialogPreviewImageBinding ismDialogPreviewImageBinding =
          IsmDialogPreviewImageBinding.inflate(activity.getLayoutInflater());

      dialog.setContentView(ismDialogPreviewImageBinding.getRoot());
      Window window = dialog.getWindow();
      if (window != null) {
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      }
      try {

        Glide.with(activity).load(mediaUrl).into(new CustomTarget<Drawable>() {
          @Override
          public void onResourceReady(@NonNull @NotNull Drawable resource,
              @Nullable Transition<? super Drawable> transition) {
            ismDialogPreviewImageBinding.ivPreview.setImageDrawable(resource);
            sizePreviewToImage(activity, ismDialogPreviewImageBinding.ivPreview, resource);
            if (window != null) {
              window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                  ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            if (resource instanceof GifDrawable) {
              ((GifDrawable) resource).start();
            }
          }

          @Override
          public void onLoadCleared(@Nullable Drawable placeholder) {

          }
        });
      } catch (IllegalArgumentException | NullPointerException ignore) {
      }
      ismDialogPreviewImageBinding.ibClose.setOnClickListener(v -> dialog.dismiss());

      dialog.setCancelable(true);
      dialog.setCanceledOnTouchOutside(true);
      dialog.show();
    } catch (Exception ignore) {
    }
  }

  private void sizePreviewToImage(Activity activity, View preview, Drawable resource) {
    int imgW = Math.max(resource.getIntrinsicWidth(), 1);
    int imgH = Math.max(resource.getIntrinsicHeight(), 1);
    DisplayMetrics dm = activity.getResources().getDisplayMetrics();
    int maxW = (int) (dm.widthPixels * 0.92f);
    int maxH = (int) (dm.heightPixels * 0.80f);
    float scale = Math.min(1f, Math.min(maxW / (float) imgW, maxH / (float) imgH));
    ViewGroup.LayoutParams layoutParams = preview.getLayoutParams();
    layoutParams.width = Math.round(imgW * scale);
    layoutParams.height = Math.round(imgH * scale);
    preview.setLayoutParams(layoutParams);
  }
}
