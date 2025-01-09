package io.isometrik.ui.messages.chat.common

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.isometrik.chat.R

object ChatConfig {
    var topViewHandler: ChatTopViewHandler? = null
    @ColorRes
    var baseColor = R.color.ism_theme_base
    @ColorRes
    var chatBackGroundColor = R.color.ism_white
    @StringRes
    var noConversationsStringResId = R.string.ism_no_conversations
    @DrawableRes
    var noConversationsImageResId = R.drawable.ism_ic_no_members
    var hideCreateChatOption: Boolean = true
    var hideAudioCallOption: Boolean = true
    var hideVideoCallOption: Boolean = true
    var hideCaptureCameraOption: Boolean = true
    var hideRecordAudioOption: Boolean = true
}
