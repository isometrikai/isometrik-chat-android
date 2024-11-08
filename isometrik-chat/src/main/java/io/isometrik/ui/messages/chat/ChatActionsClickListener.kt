package io.isometrik.ui.messages.chat

interface ChatActionsClickListener {
    public abstract fun onNewChatIconClicked()
    public abstract fun onBlockStatusUpdate(isBlocked : Boolean, userId : String)
    public abstract fun onCallClicked(isAudio: Boolean, memberId : String,  meetingDescription : String,  opponentName : String,  opponentImageUrl : String)
    public abstract fun onSharedPostClick(postId : String)
}