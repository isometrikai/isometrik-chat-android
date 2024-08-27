package io.isometrik.ui.messages.media.audio.record.views;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.media.audio.record.listeners.RecordLockViewListener;
import io.isometrik.ui.messages.media.audio.util.DpUtil;

/**
 * The custom view class for record lock.
 */
public class RecordLockView extends View {
  private Drawable bottomLockDrawable, topLockDrawable;
  private Context context;
  private RecordLockViewListener recordLockViewListener;

  private int defaultCircleColor = Color.parseColor("#03A9F4");
  private int circleLockedColor = Color.parseColor("#0288D1");
  private int circleColor = defaultCircleColor;
  private int recordLockAlpha = 255;
  private int lockColor = Color.WHITE;

  private float topLockTop, topLockBottom, initialTopLockTop, initialTopLockBottom = 0f;

  private Rect bottomLockRect;

  //reduce calling DpUtil.toDp on onDraw
  private float fiveDp, fourDp, twoDp;

  /**
   * Instantiates a new Record lock view.
   *
   * @param context the context
   */
  public RecordLockView(@NonNull Context context) {
    super(context);
    init(context, null, -1, -1);
  }

  /**
   * Instantiates a new Record lock view.
   *
   * @param context the context
   * @param attrs the attrs
   */
  public RecordLockView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs, -1, -1);
  }

  /**
   * Instantiates a new Record lock view.
   *
   * @param context the context
   * @param attrs the attrs
   * @param defStyleAttr the def style attr
   */
  public RecordLockView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr, -1);
  }

  private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    this.context = context;
    bottomLockDrawable = AppCompatResources.getDrawable(context, R.drawable.ism_ic_lock_bottom);
    topLockDrawable = AppCompatResources.getDrawable(context, R.drawable.ism_ic_lock_top);

    fiveDp = DpUtil.toPixel(5, context);
    fourDp = DpUtil.toPixel(4, context);
    twoDp = DpUtil.toPixel(2, context);

    if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
      TypedArray typedArray =
          context.obtainStyledAttributes(attrs, R.styleable.RecordLockView, defStyleAttr,
              defStyleRes);

      int circleColor = typedArray.getColor(R.styleable.RecordLockView_circle_color, -1);
      int circleLockedColor =
          typedArray.getColor(R.styleable.RecordLockView_circle_locked_color, -1);
      int lockColor = typedArray.getColor(R.styleable.RecordLockView_lock_color, -1);

      if (circleColor != -1) {
        this.circleColor = circleColor;
      }
      if (circleLockedColor != -1) {
        this.circleLockedColor = circleLockedColor;
      }

      if (lockColor != -1) {
        this.lockColor = lockColor;
        bottomLockDrawable.setColorFilter(
            new PorterDuffColorFilter(lockColor, PorterDuff.Mode.SRC_IN));
        topLockDrawable.setColorFilter(
            new PorterDuffColorFilter(lockColor, PorterDuff.Mode.SRC_IN));
      }
      typedArray.recycle();
    }
  }

  private void animateAlpha() {
    ValueAnimator valueAnimator = ValueAnimator.ofInt(255, 0);
    valueAnimator.addUpdateListener(animation -> {
      recordLockAlpha = (int) animation.getAnimatedValue();
      invalidate();
    });
    valueAnimator.setDuration(700);
    valueAnimator.setInterpolator(new AnticipateInterpolator());
    valueAnimator.start();
  }

  /**
   * Reset.
   */
  protected void reset() {
    recordLockAlpha = 255;
    circleColor = defaultCircleColor;
    topLockTop = initialTopLockTop;
    topLockBottom = initialTopLockBottom;
    invalidate();
  }

  /**
   * Animate lock.
   *
   * @param fraction the fraction
   */
/*
  this will animate ONLY the top part of the lock 'R.drawable.recv_lock_top'
  we will move its top and bottom so it goes inside the bottom lock 'R.drawable.recv_lock_bottom'
   */
  protected void animateLock(float fraction) {
    if (bottomLockRect == null) {
      return;
    }

    float topLockFraction = (float) (fraction + 0.25);

    //resize topLock
    int topLockDrawableHeight = (int) (topLockDrawable.getIntrinsicHeight() / 2.0);

    float startTop = initialTopLockTop;
    float endTop = bottomLockRect.top - topLockDrawableHeight;

    float startBottom = initialTopLockBottom;
    float endBottom = bottomLockRect.top + topLockDrawableHeight;

    float differenceTop = endTop - startTop;
    float differenceBottom = endBottom - startBottom;

    float newTop = differenceTop + (startTop * topLockFraction);
    float newBottom = differenceBottom + (startBottom * topLockFraction);

    if (fraction >= 0.85) {
      recordLockViewListener.onFractionReached();
      animateAlpha();
      circleColor = circleLockedColor;
    } else {
      circleColor = defaultCircleColor;
    }

    //start animating lock (top and bottom) ONLY if gets above 0.2 and if it gets to 1.0
    if (topLockFraction <= 1.0f && fraction > 0.2) {
      startValueAnimators(newTop, newBottom);
    }

    invalidate();
  }

  private void startValueAnimators(float newTop, float newBottom) {
    ValueAnimator topLockTopAnimator = ValueAnimator.ofFloat(newTop);
    topLockTopAnimator.addUpdateListener(animation -> {
      topLockTop = (float) animation.getAnimatedValue();
    });
    topLockTopAnimator.setDuration(0);

    ValueAnimator topLockBottomAnimator = ValueAnimator.ofFloat(newBottom);
    topLockBottomAnimator.addUpdateListener(animation -> {
      topLockBottom = (float) animation.getAnimatedValue();
    });
    topLockBottomAnimator.setDuration(0);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.setDuration(0).setInterpolator(new DecelerateInterpolator());
    animatorSet.playTogether(topLockTopAnimator, topLockBottomAnimator);
    animatorSet.start();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int cx = getWidth() / 2;
    int cy = getHeight() / 2;

    int height = getHeight();

    Paint paint = new Paint();
    paint.setColor(circleColor);
    paint.setAlpha(recordLockAlpha);
    paint.setAntiAlias(true);

    canvas.drawCircle(cx, cy, getMeasuredWidth() / 2 + fourDp, paint);

    int drawableWidth = (int) (bottomLockDrawable.getIntrinsicWidth() / 1.5);
    int drawableHeight = (int) (bottomLockDrawable.getIntrinsicHeight() / 2.0);

    Rect bottomLockRect = new Rect(cx - drawableWidth / 2, (int) (cy + fiveDp - drawableHeight / 2),
        cx + drawableWidth / 2, (int) (height - fiveDp));

    if (this.bottomLockRect == null) {
      this.bottomLockRect = bottomLockRect;
    }

    bottomLockDrawable.setBounds(bottomLockRect);

    int topLockDrawableHeight = (int) (topLockDrawable.getIntrinsicHeight() / 1.3);

    if (topLockTop == 0f) {
      topLockTop = -twoDp;
      topLockBottom = topLockDrawableHeight;
      initialTopLockTop = topLockTop;
      initialTopLockBottom = topLockBottom;
    }

    Rect topLockRect =
        new Rect(bottomLockRect.left, (int) topLockTop, bottomLockRect.right, (int) topLockBottom);

    topLockDrawable.setBounds(topLockRect);

    topLockDrawable.setAlpha(recordLockAlpha);
    bottomLockDrawable.setAlpha(recordLockAlpha);

    topLockDrawable.draw(canvas);
    bottomLockDrawable.draw(canvas);
  }

  /**
   * Sets default circle color.
   *
   * @param defaultCircleColor the default circle color
   */
  public void setDefaultCircleColor(int defaultCircleColor) {
    this.defaultCircleColor = defaultCircleColor;
    invalidate();
  }

  /**
   * Sets circle locked color.
   *
   * @param circleLockedColor the circle locked color
   */
  public void setCircleLockedColor(int circleLockedColor) {
    this.circleLockedColor = circleLockedColor;
    invalidate();
  }

  /**
   * Sets lock color.
   *
   * @param lockColor the lock color
   */
  public void setLockColor(int lockColor) {
    this.lockColor = lockColor;
    bottomLockDrawable.setColorFilter(new PorterDuffColorFilter(lockColor, PorterDuff.Mode.SRC_IN));
    topLockDrawable.setColorFilter(new PorterDuffColorFilter(lockColor, PorterDuff.Mode.SRC_IN));
    invalidate();
  }

  /**
   * Sets record lock view listener.
   *
   * @param recordLockViewListener the record lock view listener
   */
  protected void setRecordLockViewListener(RecordLockViewListener recordLockViewListener) {
    this.recordLockViewListener = recordLockViewListener;
  }
}