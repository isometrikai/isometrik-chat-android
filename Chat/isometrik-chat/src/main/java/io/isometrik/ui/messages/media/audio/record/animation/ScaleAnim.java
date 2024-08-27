package io.isometrik.ui.messages.media.audio.record.animation;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * The helper class for Scale animation.
 */
public class ScaleAnim {
  private final View view;

  /**
   * Instantiates a new Scale anim.
   *
   * @param view the view
   */
  public ScaleAnim(View view) {
    this.view = view;
  }

  /**
   * Start.
   */
  public void start() {
    AnimatorSet set = new AnimatorSet();

    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 2.0f);
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 2.0f);

    set.setDuration(150);
    set.setInterpolator(new AccelerateDecelerateInterpolator());
    set.playTogether(scaleY, scaleX);
    set.start();
  }

  /**
   * Stop.
   */
  public void stop() {
    AnimatorSet set = new AnimatorSet();

    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f);
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f);

    set.setDuration(150);
    set.setInterpolator(new AccelerateDecelerateInterpolator());
    set.playTogether(scaleY, scaleX);
    set.start();
  }
}