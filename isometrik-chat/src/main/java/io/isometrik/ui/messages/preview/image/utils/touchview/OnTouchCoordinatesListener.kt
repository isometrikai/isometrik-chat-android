package io.isometrik.ui.messages.preview.image.utils.touchview

import android.graphics.PointF
import android.view.MotionEvent
import android.view.View

interface OnTouchCoordinatesListener {
  fun onTouchCoordinate(view: View, event: MotionEvent, bitmapPoint: PointF)
}