package io.isometrik.ui.messages.media.whiteboard.utils

import android.graphics.Color

data class PaintOptions(var color: Int = Color.BLACK, var strokeWidth: Float = 8f, var alpha: Int = 255, var isEraserOn: Boolean = false)
