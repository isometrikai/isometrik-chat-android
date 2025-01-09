package io.isometrik.ui.conversations.list

import android.content.Context
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface ChatListItemBinder<T, VB : ViewBinding> {
    fun createBinding(parent: ViewGroup): VB
    fun bindData(context: Context, binding: VB, data: T)
}