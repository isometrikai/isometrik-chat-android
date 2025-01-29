package io.isometrik.ui.utils

import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import io.isometrik.ui.IsometrikChatSdk
import io.isometrik.ui.messages.chat.common.ChatConfig

/**
 * The helper class to return random colors for background of image placeholders based on positions.
 */
object ColorsUtil {
    private val colors: List<String> = mutableListOf(
        "#FFCDD2", "#D1C4E9", "#B3E5FC", "#C8E6C9", "#FFF9C4", "#FFCCBC", "#CFD8DC",
        "#F8BBD0", "#C5CAE9", "#B2EBF2", "#DCEDC8", "#FFECB3", "#D7CCC8", "#F5F5F5", "#FFE0B2",
        "#F0F4C3", "#B2DFDB", "#BBDEFB", "#E1BEE7"
    )

    /**
     * Gets color code.
     *
     * @param position the position
     * @return the color code
     */
    fun getColorCode(position: Int): String {
        return colors[position]
    }

    @ColorInt
    fun getThemeColor() = ContextCompat.getColor(IsometrikChatSdk.instance.context, ChatConfig.baseColor)
}
