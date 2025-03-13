package io.isometrik.ui.messages.chat.common

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.isometrik.chat.R
import io.isometrik.chat.utils.Constants
import io.isometrik.ui.messages.chat.ConversationMessagesActivity

object ChatConfig {
    var mqttConnectionUrl: String? = null
    var baseUrl: String? = null
    var mqttPort: Int? = null
    var topViewHandler: ChatTopViewHandler? = null
    @ColorRes
    var baseColor = R.color.ism_theme_base
    @ColorRes
    var chatBackGroundColor = R.color.ism_white
    @StringRes
    var noConversationsStringResId = R.string.ism_no_conversations
    @DrawableRes
    var noConversationsImageResId = R.drawable.ism_ic_no_members
    @DrawableRes
    var textSentBubbleResId = R.drawable.ism_sent_message_bubble
    @DrawableRes
    var textReceivedBubbleResId = R.drawable.ism_received_message_bubble
    var hideCreateChatOption: Boolean = false
    var hideAudioCallOption: Boolean = false
    var hideVideoCallOption: Boolean = false
    var hideSenderNameInMessageCell: Boolean = true
    var hideCaptureCameraOption: Boolean = false
    var hideRecordAudioOption: Boolean = false
    var disableReScheduleMqtt: Boolean = false
    var disableOfflineLabel: Boolean = false
    var hideSubHeader: Boolean = false
    var disableTopHeaderClickAction: Boolean = false

    var DEFAULT_PLACEHOLDER_IMAGE_URL: String = Constants.DEFAULT_PLACEHOLDER_IMAGE_URL
    var dontShowToastList : List<String> = arrayListOf()

    private val _typingUiState = MutableLiveData<Boolean>()
    val typingUiState: LiveData<Boolean> get() = _typingUiState

    fun updateBottomTypingView(makeVisible : Boolean){
        _typingUiState.value = makeVisible
    }
}
