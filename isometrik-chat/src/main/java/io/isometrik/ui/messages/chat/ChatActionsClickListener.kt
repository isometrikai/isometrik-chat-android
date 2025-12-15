package io.isometrik.ui.messages.chat

import android.content.Context

interface ChatActionsClickListener {
    public abstract fun onCreateChatIconClicked(isGroup : Boolean)
    public abstract fun onBlockStatusUpdate(isBlocked : Boolean, userId : String)
    public abstract fun onCallClicked(context: Context, isAudio: Boolean, memberId : String,  meetingDescription : String,  opponentName : String,  opponentImageUrl : String)
    public abstract fun onSharedPostClick(postId : String)
    public abstract fun onViewSocialProfileClick(userId : String)
}