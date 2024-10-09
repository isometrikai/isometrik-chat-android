package io.isometrik.ui.messages.chat

interface ChatActionsClickListener {
    public abstract fun onNewChatIconClicked()
    public abstract fun onCallClicked(isAudio: Boolean, memberId : String,  meetingDescription : String,  opponentName : String,  opponentImageUrl : String)
}