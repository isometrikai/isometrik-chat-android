package io.isometrik.ui.messages.media.audio.record.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.facebook.shimmer.ShimmerFrameLayout;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.media.audio.record.animation.AnimationHelper;
import io.isometrik.ui.messages.media.audio.record.animation.OnBasketAnimationEnd;
import io.isometrik.ui.messages.media.audio.record.listeners.OnRecordListener;
import io.isometrik.ui.messages.media.audio.record.listeners.RecordLockViewListener;
import io.isometrik.ui.messages.media.audio.util.DpUtil;
import java.io.IOException;


/**
 * The custom view class for record view.
 */
public class RecordView extends RelativeLayout implements RecordLockViewListener {

  /**
   * The constant DEFAULT_CANCEL_BOUNDS.
   */
  public static final int DEFAULT_CANCEL_BOUNDS = 8; //8dp
  private ImageView ivMic, ivBasket;
  private Chronometer chCounter;
  private TextView tvSlideToCancel, tvCancel, tvSend;
  private ShimmerFrameLayout slideToCancelLayout;
  private ImageView ivArrow;
  private float initialRecordButtonX, initialRecordButtonY, recordButtonYInWindow, basketInitialY,
      difX = 0;
  private float cancelBounds = DEFAULT_CANCEL_BOUNDS;
  private long startTime, elapsedTime = 0;
  private final Context context;
  private OnRecordListener recordListener;
  private boolean isSwiped, isLessThanSecondAllowed = false;
  private boolean isSoundEnabled = true;
  private int RECORD_START = R.raw.record_start;
  private int RECORD_FINISHED = R.raw.record_start;
  private int RECORD_ERROR = R.raw.record_start;
  //private int RECORD_FINISHED = R.raw.record_finished;
  //private int RECORD_ERROR = R.raw.record_error;
  private AnimationHelper animationHelper;
  private boolean isRecordButtonGrowingAnimationEnabled = true;
  private boolean shimmerEffectEnabled = true;
  private long timeLimit = -1;
  private Runnable runnable;
  private Handler handler;
  private RecordButton recordButton;

  private boolean canRecord = true;

  private RecordLockView recordLockView;
  private boolean isLockEnabled = false;
  /**
   * The Record lock y in window.
   */
  float recordLockYInWindow = 0f;
  /**
   * The Record lock x in window.
   */
  float recordLockXInWindow = 0f;
  private boolean fractionReached = false;
  private float currentYFraction = 0f;
  private boolean isLockInSameParent = false;

  /**
   * Instantiates a new Record view.
   *
   * @param context the context
   */
  public RecordView(Context context) {
    super(context);
    this.context = context;
    init(context, null, -1, -1);
  }

  /**
   * Instantiates a new Record view.
   *
   * @param context the context
   * @param attrs the attrs
   */
  public RecordView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    init(context, attrs, -1, -1);
  }

  /**
   * Instantiates a new Record view.
   *
   * @param context the context
   * @param attrs the attrs
   * @param defStyleAttr the def style attr
   */
  public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
    init(context, attrs, defStyleAttr, -1);
  }

  private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    View view = View.inflate(context, R.layout.ism_record_view_layout, null);
    addView(view);

    ViewGroup viewGroup = (ViewGroup) view.getParent();
    viewGroup.setClipChildren(false);

    ivArrow = view.findViewById(R.id.ivArrow);
    tvSlideToCancel = view.findViewById(R.id.tvSlideToCancel);
    ivMic = view.findViewById(R.id.ivMic);
    chCounter = view.findViewById(R.id.chCounter);
    ivBasket = view.findViewById(R.id.ivBasket);
    slideToCancelLayout = view.findViewById(R.id.slideToCancelLayout);
    tvCancel = view.findViewById(R.id.tvCancel);
    tvSend = view.findViewById(R.id.tvSend);
    hideViews(true);

    if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
      TypedArray typedArray =
          context.obtainStyledAttributes(attrs, R.styleable.RecordView, defStyleAttr, defStyleRes);

      int slideArrowResource =
          typedArray.getResourceId(R.styleable.RecordView_slide_to_cancel_arrow, -1);
      String slideToCancelText = typedArray.getString(R.styleable.RecordView_slide_to_cancel_text);
      int slideMarginRight =
          (int) typedArray.getDimension(R.styleable.RecordView_slide_to_cancel_margin_right, 30);
      int counterTimeColor = typedArray.getColor(R.styleable.RecordView_counter_time_color, -1);
      int arrowColor = typedArray.getColor(R.styleable.RecordView_slide_to_cancel_arrow_color, -1);

      String cancelText = typedArray.getString(R.styleable.RecordView_cancel_text);
      int cancelMarginRight =
          (int) typedArray.getDimension(R.styleable.RecordView_cancel_text_margin_right, 30);
      int cancelTextColor = typedArray.getColor(R.styleable.RecordView_cancel_text_color, -1);

      int cancelBounds =
          typedArray.getDimensionPixelSize(R.styleable.RecordView_slide_to_cancel_bounds, -1);

      if (cancelBounds != -1) {
        setCancelBounds(cancelBounds,
            false);//don't convert it to pixels since it's already in pixels
      }

      if (slideArrowResource != -1) {
        Drawable slideArrow = AppCompatResources.getDrawable(getContext(), slideArrowResource);
        ivArrow.setImageDrawable(slideArrow);
      }

      if (slideToCancelText != null) tvSlideToCancel.setText(slideToCancelText);

      if (counterTimeColor != -1) setCounterTimeColor(counterTimeColor);

      if (arrowColor != -1) setSlideToCancelArrowColor(arrowColor);

      if (cancelText != null) {
        tvCancel.setText(cancelText);
      }

      if (cancelTextColor != -1) {
        tvCancel.setTextColor(cancelTextColor);
      }

      setMarginRight(slideMarginRight, true);
      setCancelMarginRight(cancelMarginRight, true);

      typedArray.recycle();
    }

    animationHelper =
        new AnimationHelper(context, ivBasket, ivMic, isRecordButtonGrowingAnimationEnabled);

    tvCancel.setOnClickListener(v -> {
      animationHelper.animateBasket(basketInitialY);
      cancelAndDeleteRecord();
    });

    tvSend.setOnClickListener(v -> finishAndSaveRecord());
  }

  private void cancelAndDeleteRecord() {
    if (isTimeLimitValid()) {
      removeTimeLimitCallbacks();
    }

    isSwiped = true;

    animationHelper.setStartRecorded(false);

    if (recordListener != null) {
      recordListener.onCancel();
    }

    resetRecord(recordButton);
  }

  private boolean isTimeLimitValid() {
    return timeLimit > 0;
  }

  private void initTimeLimitHandler() {
    handler = new Handler();
    runnable = () -> {

      if (recordListener != null && !isSwiped) recordListener.onFinish(elapsedTime, true);

      removeTimeLimitCallbacks();

      animationHelper.setStartRecorded(false);

      if (!isSwiped) playSound(RECORD_FINISHED);

      if (recordButton != null) {
        resetRecord(recordButton);
      }
      isSwiped = true;
    };
  }

  private void hideViews(boolean hideSmallMic) {
    slideToCancelLayout.setVisibility(GONE);
    chCounter.setVisibility(GONE);
    tvCancel.setVisibility(GONE);
    tvSend.setVisibility(GONE);
    if (isLockEnabled && recordLockView != null) {
      recordLockView.setVisibility(GONE);
    }
    if (hideSmallMic) ivMic.setVisibility(GONE);
  }

  private void showViews() {
    slideToCancelLayout.setVisibility(VISIBLE);
    ivMic.setVisibility(VISIBLE);
    chCounter.setVisibility(VISIBLE);
    if (isLockEnabled && recordLockView != null) {
      recordLockView.setVisibility(VISIBLE);
    }
  }

  private boolean isLessThanOneSecond(long time) {
    return time <= 1000;
  }

  private void playSound(int soundRes) {

    if (isSoundEnabled) {
      if (soundRes == 0) return;

      try {
        MediaPlayer player = new MediaPlayer();
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(soundRes);
        if (afd == null) return;
        player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        afd.close();
        player.prepare();
        player.start();
        player.setOnCompletionListener(MediaPlayer::release);
        player.setLooping(false);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * On action down.
   *
   * @param recordBtn the record btn
   * @param motionEvent the motion event
   */
  protected void onActionDown(RecordButton recordBtn, MotionEvent motionEvent) {

    boolean hasRecordingPermission = isRecordPermissionGranted();

    if (recordListener != null) recordListener.onStart(hasRecordingPermission);
    if (!hasRecordingPermission) {
      return;
    }
    if (isTimeLimitValid()) {
      removeTimeLimitCallbacks();
      handler.postDelayed(runnable, timeLimit);
    }

    animationHelper.setStartRecorded(true);
    animationHelper.resetBasketAnimation();
    animationHelper.resetSmallMic();

    if (isRecordButtonGrowingAnimationEnabled) {
      recordBtn.startScale();
    }

    if (shimmerEffectEnabled) {
      slideToCancelLayout.startShimmer();
    }

    initialRecordButtonX = recordBtn.getX();

    int[] recordButtonLocation = new int[2];
    recordBtn.getLocationInWindow(recordButtonLocation);

    initialRecordButtonY = recordButton.getY();

    if (isLockEnabled && recordLockView != null) {
      isLockInSameParent = isLockAndRecordButtonHaveSameParent();
      int[] recordLockLocation = new int[2];
      recordLockView.getLocationInWindow(recordLockLocation);
      recordLockXInWindow = recordLockLocation[0];
      recordLockYInWindow = isLockInSameParent ? recordLockView.getY() : recordLockLocation[1];
      recordButtonYInWindow = isLockInSameParent ? recordButton.getY() : recordButtonLocation[1];
    }

    basketInitialY = ivBasket.getY() + 90;

    playSound(RECORD_START);

    showViews();

    animationHelper.animateSmallMicAlpha();
    chCounter.setBase(SystemClock.elapsedRealtime());
    startTime = System.currentTimeMillis();
    chCounter.start();
    isSwiped = false;
    currentYFraction = 0f;
  }

  /**
   * On action move.
   *
   * @param recordBtn the record btn
   * @param motionEvent the motion event
   */
  protected void onActionMove(final RecordButton recordBtn, MotionEvent motionEvent) {

    if (!canRecord || fractionReached) {
      return;
    }

    long time = System.currentTimeMillis() - startTime;

    if (!isSwiped) {

      //Swipe To Cancel
      if (slideToCancelLayout.getX() != 0
          && slideToCancelLayout.getX() <= chCounter.getRight() + cancelBounds) {

        //if the time was less than one second then do not start basket animation
        if (isLessThanOneSecond(time)) {
          hideViews(true);
          animationHelper.clearAlphaAnimation(false);

          animationHelper.onAnimationEnd();
        } else {
          hideViews(false);
          animationHelper.animateBasket(basketInitialY);
        }

        animationHelper.moveRecordButtonAndSlideToCancelBack(recordBtn, slideToCancelLayout,
            initialRecordButtonX, initialRecordButtonY, difX, isLockEnabled);

        chCounter.stop();
        if (shimmerEffectEnabled) {
          slideToCancelLayout.stopShimmer();
        }

        isSwiped = true;

        animationHelper.setStartRecorded(false);

        if (recordListener != null) recordListener.onCancel();

        if (isTimeLimitValid()) {
          removeTimeLimitCallbacks();
        }
      } else {

        if (canMoveX(motionEvent)) {
          recordBtn.animate().x(motionEvent.getRawX()).setDuration(0).start();

          if (difX == 0) difX = (initialRecordButtonX - slideToCancelLayout.getX());

          slideToCancelLayout.animate().x(motionEvent.getRawX() - difX).setDuration(0).start();
        }

                  /*
                  if RecordLock was NOT inside the same parent as RecordButton
                   animate.y() OR view.setY() will setY value INSIDE its parent
                   we need a way to convert the inner value to outer value
                   since motionEvent.getRawY() returns Y's location onScreen
                   we had to get screen height and get the difference between motionEvent and screen height
                 */
        float newY = isLockInSameParent ? motionEvent.getRawY()
            : motionEvent.getRawY() - recordButtonYInWindow;
        if (canMoveY(motionEvent, newY)) {

          recordBtn.animate().y(newY).setDuration(0).start();

          float currentY = motionEvent.getRawY();
          float minY = recordLockYInWindow;
          float maxY = recordButtonYInWindow;

          float fraction = (currentY - minY) / (maxY - minY);
          fraction = 1 - fraction;
          currentYFraction = fraction;

          recordLockView.animateLock(fraction);

          if (isRecordButtonGrowingAnimationEnabled) {
            //convert fraction to scale
            //so instead of starting from 0 to 1, it will start from 1 to 0
            float scale = 1 - fraction + 1;
            recordBtn.animate().scaleX(scale).scaleY(scale).setDuration(0).start();
          }
        }
      }
    }
  }

  private boolean canMoveX(MotionEvent motionEvent) {
    //Prevent Swiping out of bounds
    if (motionEvent.getRawX() < initialRecordButtonX) {
      if (isLockEnabled) {
        //prevent swiping X if record button goes up
        return currentYFraction <= 0.3;
      }
      return true;
    }

    return false;
  }

  private boolean canMoveY(MotionEvent motionEvent, float dif) {

    if (isLockEnabled) {
            /*
             1. prevent swiping below record button
             2. prevent swiping up if record button is NOT near record Lock's X
             */
      if (isLockInSameParent) {
        return motionEvent.getRawY() < initialRecordButtonY
            && motionEvent.getRawX() >= recordLockXInWindow;
      } else {
        return dif <= initialRecordButtonY && motionEvent.getRawX() >= recordLockXInWindow;
      }
    }

    return false;
  }

  /**
   * On action up.
   *
   * @param recordBtn the record btn
   */
  protected void onActionUp(RecordButton recordBtn) {

    if (!canRecord || fractionReached) {
      return;
    }

    finishAndSaveRecord();
  }

  private void finishAndSaveRecord() {
    elapsedTime = System.currentTimeMillis() - startTime;

    if (!isLessThanSecondAllowed && isLessThanOneSecond(elapsedTime) && !isSwiped) {
      if (recordListener != null) recordListener.onLessThanSecond();

      removeTimeLimitCallbacks();
      animationHelper.setStartRecorded(false);

      playSound(RECORD_ERROR);
    } else {
      if (recordListener != null && !isSwiped) recordListener.onFinish(elapsedTime, false);

      removeTimeLimitCallbacks();

      animationHelper.setStartRecorded(false);

      if (!isSwiped) playSound(RECORD_FINISHED);
    }

    resetRecord(recordButton);
  }

  private void switchToLockedMode() {
    tvCancel.setVisibility(VISIBLE);
    tvSend.setVisibility(VISIBLE);
    slideToCancelLayout.setVisibility(GONE);

    recordButton.animate().x(initialRecordButtonX).y(initialRecordButtonY).setDuration(100).start();

    if (isRecordButtonGrowingAnimationEnabled) {
      recordButton.stopScale();
    }

    recordButton.setListenForRecord(false);
    recordButton.setInLockMode(true);
    recordButton.changeIconToSend();

    if(recordListener!=null){
      recordListener.switchedToLockedMode();
    }
  }

  private boolean isLockAndRecordButtonHaveSameParent() {
    if (recordLockView == null) {
      return false;
    }

    ViewParent lockParent = recordLockView.getParent();
    ViewParent recordButtonParent = recordButton.getParent();
    if (lockParent == null || recordButtonParent == null) {
      return false;
    }
    return lockParent == recordButtonParent;
  }

  private void resetRecord(RecordButton recordBtn) {
    //if user has swiped then do not hide SmallMic since it will be hidden after swipe Animation
    hideViews(!isSwiped);
    fractionReached = false;

    if (!isSwiped) animationHelper.clearAlphaAnimation(true);

    animationHelper.moveRecordButtonAndSlideToCancelBack(recordBtn, slideToCancelLayout,
        initialRecordButtonX, initialRecordButtonY, difX, isLockEnabled);
    chCounter.stop();
    if (shimmerEffectEnabled) {
      slideToCancelLayout.stopShimmer();
    }

    if (isLockEnabled) {
      recordLockView.reset();
      recordBtn.changeIconToRecord();
    }
    tvSend.setVisibility(GONE);
    tvCancel.setVisibility(GONE);
    recordBtn.setListenForRecord(true);
    recordBtn.setInLockMode(false);
  }

  private void removeTimeLimitCallbacks() {
    if (isTimeLimitValid()) {
      handler.removeCallbacks(runnable);
    }
  }

  private boolean isRecordPermissionGranted() {

    canRecord = ContextCompat.checkSelfPermission(activityContext, Manifest.permission.RECORD_AUDIO)
        == PermissionChecker.PERMISSION_GRANTED;
    return canRecord;
  }

  private Activity activityContext;

  /**
   * Sets activity context.
   *
   * @param activityContext the activity context
   */
  public void setActivityContext(Activity activityContext) {
    this.activityContext = activityContext;
  }

  private void setMarginRight(int marginRight, boolean convertToDp) {
    RelativeLayout.LayoutParams layoutParams =
        (RelativeLayout.LayoutParams) slideToCancelLayout.getLayoutParams();
    if (convertToDp) {
      layoutParams.rightMargin = (int) DpUtil.toPixel(marginRight, context);
    } else {
      layoutParams.rightMargin = marginRight;
    }

    slideToCancelLayout.setLayoutParams(layoutParams);
  }

  private void setCancelMarginRight(int marginRight, boolean convertToDp) {
    RelativeLayout.LayoutParams layoutParams =
        (RelativeLayout.LayoutParams) slideToCancelLayout.getLayoutParams();
    if (convertToDp) {
      layoutParams.rightMargin = (int) DpUtil.toPixel(marginRight, context);
    } else {
      layoutParams.rightMargin = marginRight;
    }

    tvCancel.setLayoutParams(layoutParams);
  }

  /**
   * Sets on record listener.
   *
   * @param recrodListener the recrod listener
   */
  public void setOnRecordListener(OnRecordListener recrodListener) {
    this.recordListener = recrodListener;
  }

  /**
   * Sets on basket animation end listener.
   *
   * @param onBasketAnimationEndListener the on basket animation end listener
   */
  public void setOnBasketAnimationEndListener(OnBasketAnimationEnd onBasketAnimationEndListener) {
    animationHelper.setOnBasketAnimationEndListener(onBasketAnimationEndListener);
  }

  /**
   * Sets sound enabled.
   *
   * @param isEnabled the is enabled
   */
  public void setSoundEnabled(boolean isEnabled) {
    isSoundEnabled = isEnabled;
  }

  /**
   * Sets less than second allowed.
   *
   * @param isAllowed the is allowed
   */
  public void setLessThanSecondAllowed(boolean isAllowed) {
    isLessThanSecondAllowed = isAllowed;
  }

  /**
   * Sets slide to cancel text.
   *
   * @param text the text
   */
  public void setSlideToCancelText(String text) {
    tvSlideToCancel.setText(text);
  }

  /**
   * Sets slide to cancel text color.
   *
   * @param color the color
   */
  public void setSlideToCancelTextColor(int color) {
    tvSlideToCancel.setTextColor(color);
  }

  /**
   * Sets small mic color.
   *
   * @param color the color
   */
  public void setSmallMicColor(int color) {
    ivMic.setColorFilter(color);
  }

  /**
   * Sets small mic icon.
   *
   * @param icon the icon
   */
  public void setSmallMicIcon(int icon) {
    ivMic.setImageResource(icon);
  }

  /**
   * Sets slide margin right.
   *
   * @param marginRight the margin right
   */
  public void setSlideMarginRight(int marginRight) {
    setMarginRight(marginRight, true);
  }

  /**
   * Sets custom sounds.
   *
   * @param startSound the start sound
   * @param finishedSound the finished sound
   * @param errorSound the error sound
   */
  public void setCustomSounds(int startSound, int finishedSound, int errorSound) {
    //0 means do not play sound
    RECORD_START = startSound;
    RECORD_FINISHED = finishedSound;
    RECORD_ERROR = errorSound;
  }

  /**
   * Gets cancel bounds.
   *
   * @return the cancel bounds
   */
  public float getCancelBounds() {
    return cancelBounds;
  }

  /**
   * Sets cancel bounds.
   *
   * @param cancelBounds the cancel bounds
   */
  public void setCancelBounds(float cancelBounds) {
    setCancelBounds(cancelBounds, true);
  }

  /**
   * Sets counter time color.
   *
   * @param color the color
   */
  //set Chronometer color
  public void setCounterTimeColor(int color) {
    chCounter.setTextColor(color);
  }

  /**
   * Sets slide to cancel arrow color.
   *
   * @param color the color
   */
  public void setSlideToCancelArrowColor(int color) {
    ivArrow.setColorFilter(color);
  }

  private void setCancelBounds(float cancelBounds, boolean convertDpToPixel) {
    this.cancelBounds = convertDpToPixel ? DpUtil.toPixel(cancelBounds, context) : cancelBounds;
  }

  /**
   * Is record button growing animation enabled boolean.
   *
   * @return the boolean
   */
  public boolean isRecordButtonGrowingAnimationEnabled() {
    return isRecordButtonGrowingAnimationEnabled;
  }

  /**
   * Sets record button growing animation enabled.
   *
   * @param recordButtonGrowingAnimationEnabled the record button growing animation enabled
   */
  public void setRecordButtonGrowingAnimationEnabled(boolean recordButtonGrowingAnimationEnabled) {
    isRecordButtonGrowingAnimationEnabled = recordButtonGrowingAnimationEnabled;
    animationHelper.setRecordButtonGrowingAnimationEnabled(recordButtonGrowingAnimationEnabled);
  }

  /**
   * Is shimmer effect enabled boolean.
   *
   * @return the boolean
   */
  public boolean isShimmerEffectEnabled() {
    return shimmerEffectEnabled;
  }

  /**
   * Sets shimmer effect enabled.
   *
   * @param shimmerEffectEnabled the shimmer effect enabled
   */
  public void setShimmerEffectEnabled(boolean shimmerEffectEnabled) {
    this.shimmerEffectEnabled = shimmerEffectEnabled;
  }

  /**
   * Gets time limit.
   *
   * @return the time limit
   */
  public long getTimeLimit() {
    return timeLimit;
  }

  /**
   * Sets time limit.
   *
   * @param timeLimit the time limit
   */
  public void setTimeLimit(long timeLimit) {
    this.timeLimit = timeLimit;

    if (handler != null && runnable != null) {
      removeTimeLimitCallbacks();
    }
    initTimeLimitHandler();
  }

  /**
   * Sets trash icon color.
   *
   * @param color the color
   */
  public void setTrashIconColor(int color) {
    animationHelper.setTrashIconColor(color);
  }

  /**
   * Sets record lock image view.
   *
   * @param recordLockView the record lock view
   */
  public void setRecordLockImageView(RecordLockView recordLockView) {
    this.recordLockView = recordLockView;
    this.recordLockView.setRecordLockViewListener(this);
    this.recordLockView.setVisibility(INVISIBLE);
  }

  /**
   * Sets lock enabled.
   *
   * @param lockEnabled the lock enabled
   */
  public void setLockEnabled(boolean lockEnabled) {
    isLockEnabled = lockEnabled;
  }

  /**
   * Sets record button.
   *
   * @param recordButton the record button
   */
  protected void setRecordButton(RecordButton recordButton) {
    this.recordButton = recordButton;
    this.recordButton.setSendClickListener(v -> {
      finishAndSaveRecord();
    });
  }

  /**
   * Finish record.
   */
/*
  Use this if you want to Finish And save the Record if user closes the app for example in 'onPause()'
   */
  public void finishRecord() {
    finishAndSaveRecord();
  }

  /**
   * Cancel record.
   */
/*
  Use this if you want to Cancel And delete the Record if user closes the app for example in 'onPause()'
   */
  public void cancelRecord() {
    hideViews(true);
    animationHelper.clearAlphaAnimation(false);
    cancelAndDeleteRecord();
  }

  @Override
  public void onFractionReached() {
    fractionReached = true;
    switchToLockedMode();
  }
}