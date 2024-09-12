package io.isometrik.ui.messages.preview.image;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
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
      try {

        Glide.with(activity).load(mediaUrl).into(new CustomTarget<Drawable>() {
          @Override
          public void onResourceReady(@NonNull @NotNull Drawable resource,
              @Nullable Transition<? super Drawable> transition) {
            ismDialogPreviewImageBinding.ivPreview.setImageDrawable(resource);
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
}
