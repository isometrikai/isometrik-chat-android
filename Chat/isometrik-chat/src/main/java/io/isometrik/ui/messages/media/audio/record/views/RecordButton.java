package io.isometrik.ui.messages.media.audio.record.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import io.isometrik.chat.R;
import io.isometrik.ui.messages.media.audio.record.animation.ScaleAnim;
import io.isometrik.ui.messages.media.audio.record.listeners.OnRecordClickListener;

/**
 * The custom view class for record button.
 */
public class RecordButton extends AppCompatImageView implements View.OnTouchListener, View.OnClickListener {

  private ScaleAnim scaleAnim;
  private RecordView recordView;
  private boolean listenForRecord = true;
  private OnRecordClickListener onRecordClickListener;
  private OnRecordClickListener sendClickListener;
  private boolean isInLockMode = false;
  private Drawable micIcon, sendIcon;

  /**
   * Sets record view.
   *
   * @param recordView the record view
   */
  public void setRecordView(RecordView recordView) {
    this.recordView = recordView;
    recordView.setRecordButton(this);
  }

  /**
   * Instantiates a new Record button.
   *
   * @param context the context
   */
  public RecordButton(Context context) {
    super(context);
    init(context, null);
  }

  /**
   * Instantiates a new Record button.
   *
   * @param context the context
   * @param attrs the attrs
   */
  public RecordButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  /**
   * Instantiates a new Record button.
   *
   * @param context the context
   * @param attrs the attrs
   * @param defStyleAttr the def style attr
   */
  public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);


  }

  private void init(Context context, AttributeSet attrs) {
    if (attrs != null) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordButton);

      int imageResource = typedArray.getResourceId(R.styleable.RecordButton_mic_icon, -1);
      int sendResource = typedArray.getResourceId(R.styleable.RecordButton_send_icon, -1);


      if (imageResource != -1) {
        setTheImageResource(imageResource);
      }

      if (sendResource != -1) {
        sendIcon = AppCompatResources.getDrawable(getContext(), sendResource);
      }

      typedArray.recycle();
    }


    scaleAnim = new ScaleAnim(this);


    this.setOnTouchListener(this);
    this.setOnClickListener(this);


  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    setClip(this);
  }

  /**
   * Sets clip.
   *
   * @param v the v
   */
  public void setClip(View v) {
    if (v.getParent() == null) {
      return;
    }

    if (v instanceof ViewGroup) {
      ((ViewGroup) v).setClipChildren(false);
      ((ViewGroup) v).setClipToPadding(false);
    }

    if (v.getParent() instanceof View) {
      setClip((View) v.getParent());
    }
  }


  private void setTheImageResource(int imageResource) {
    Drawable image = AppCompatResources.getDrawable(getContext(), imageResource);
    setImageDrawable(image);
    micIcon = image;
  }


  @Override
  public boolean onTouch(View v, MotionEvent event) {
    if (isListenForRecord()) {
      switch (event.getAction()) {

        case MotionEvent.ACTION_DOWN:
          recordView.onActionDown((RecordButton) v, event);
          break;


        case MotionEvent.ACTION_MOVE:
          recordView.onActionMove((RecordButton) v, event);
          break;

        case MotionEvent.ACTION_UP:
          recordView.onActionUp((RecordButton) v);
          break;

      }

    }
    return isListenForRecord();


  }

  /**
   * Start scale.
   */
  protected void startScale() {
    scaleAnim.start();
  }

  /**
   * Stop scale.
   */
  public void stopScale() {
    scaleAnim.stop();
  }

  /**
   * Sets listen for record.
   *
   * @param listenForRecord the listen for record
   */
  public void setListenForRecord(boolean listenForRecord) {
    this.listenForRecord = listenForRecord;
  }

  /**
   * Is listen for record boolean.
   *
   * @return the boolean
   */
  public boolean isListenForRecord() {
    return listenForRecord;
  }

  /**
   * Sets on record click listener.
   *
   * @param onRecordClickListener the on record click listener
   */
  public void setOnRecordClickListener(OnRecordClickListener onRecordClickListener) {
    this.onRecordClickListener = onRecordClickListener;
  }

  /**
   * Sets send click listener.
   *
   * @param sendClickListener the send click listener
   */
  protected void setSendClickListener(OnRecordClickListener sendClickListener) {
    this.sendClickListener = sendClickListener;
  }

  /**
   * Sets in lock mode.
   *
   * @param inLockMode the in lock mode
   */
  protected void setInLockMode(boolean inLockMode) {
    isInLockMode = inLockMode;
  }

  /**
   * Sets send icon resource.
   *
   * @param resource the resource
   */
  public void setSendIconResource(int resource) {
    sendIcon = AppCompatResources.getDrawable(getContext(), resource);
  }

  @Override
  public void onClick(View v) {
    if (isInLockMode && sendClickListener != null) {
      sendClickListener.onClick(v);
    } else {
      if (onRecordClickListener != null)
        onRecordClickListener.onClick(v);
    }
  }

  /**
   * Change icon to send.
   */
  protected void changeIconToSend() {
    if (sendIcon != null) {
      setImageDrawable(sendIcon);
    }
  }

  /**
   * Change icon to record.
   */
  protected void changeIconToRecord() {
    if (micIcon != null) {
      setImageDrawable(micIcon);
    }
  }


}